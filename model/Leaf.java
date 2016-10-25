package model; 

import gui.WIMOVAC;

public class Leaf extends Organ {

	public double LA;

	// physiology properties
	public double Gs, Ci, Cm, T; // Gs: stomatal conductance, Ci: intercellar
									// CO2 partial pressure, Cm: mesophyll CO2
									// concentration for C4 model, T: leaf
									// temperature
	public double A, Rd=0, E; // A: assimilation rate, E: transpiration rate
	public double QY, LCP; // QY: quantum_yield
	// leaf water potential
	public double leafWaterPotential; // Qingfeng add
	public double leafWaterUseEfficiency;

	String rate_limiting_step; // "Vc", "Vj" or "Vu" ; label the limiting step
								// of the three in Farquhar model.

	// microClimate factors of the leaf
	public double Cs, Os; // Cs: CO2 partial pressure on leaf surface, Os: O2
							// ...
	public double PPFD;
	public double RH; // RH: relative humidity
	public double Tair; // Tair: air temperature
	//

	// commom variables
	double Vomax, Vcmax, Jmax, TPU; // Vomax, Vcmax, Jmax
	double curvature_Factor; //
	double FEII = 0.85; // Spectral imbalance (Evans 1987), used in (Von
						// Caemmerer Book)
	double Ko; // Ko
	double Kc; // Kc
	double g0, g1;
	
	// variables for AQ curve model, used in Festulolium, add in 2015-04-28, Qingfeng , Xiurong
		double Amax, phi, theta; 
		double Nleaf = 5; //%   

	// for C4_Von model
	double Vpmax = 120;
	double Kp = 80;
	double Vpr = 80;

	Utilities utl = new Utilities();

	public Leaf() {
		Cs = 380; // ubar
		Os = 210; // mbar
		PPFD = 1000; // umol.m-1.s-1
	}

	public Leaf(double CO2, double O2, double Light) {
		Cs = CO2;
		Os = O2;
		PPFD = Light;
	}

	public Leaf(double CO2, double O2, double Light, double relativeHumidity,
			double TemperatureOfair) {
		Cs = CO2;
		Os = O2;
		PPFD = Light;
		RH = relativeHumidity;
		Tair = TemperatureOfair;
	}

	public Leaf(Environment env) {
		Cs = env.air.CO2_concentration;
		Os = env.air.O2_concentration;
		RH = env.air.RH;
		Tair = env.air.current_T;

		PPFD = 1000;
		// PPFD=env.light;
	}

	public void cal_c3(Environment env){
		
		if(Constants.UseAQ){
			cal_AQ(env);
			
		}else{
			cal_Farquhar(env);
		}
		
		
		
		
	}
	
	private void cal_Farquhar(Environment env) {

		T = Tair;
		Ci = Cs * SOLC(T) * 0.7;

		double Tolerance = 0.01; // Set some dummy values
		double MaxIterations = 200; // Normally don't perform more than this
									// number

		/*
		 * calculate intermediate parameters
		 */
		cal_temperature_nitrogen_adj();

		/*
		 * calculate the assimilation rate
		 */
		cal_assimilation();

		/*
		 * Interative calculation until Ci stable
		 */

		double OldCi = Ci; // Set some dummy values
		// thrid, calculate the stomatal until stable
		int IterationCounter = 0; // Reset the iteration counter
		while (true) {
			cal_stomatal(env);
			cal_assimilation();
			if (IterationCounter >= MaxIterations)
				break;
			if (Math.abs(Ci - OldCi) < Tolerance)
				break;
			OldCi = Ci;
			IterationCounter++;
		}
		// System.out.println("final Ci: "+ Ci);
		if (A < -Rd) {

			A = 0;
		}
		// System.out.println("leaf A: "+A);
		// Set the output variables for the module
	}


	private void cal_AQ(Environment env){
		double PAR = PPFD;
		T = Tair;
		Amax = Double.valueOf(WIMOVAC.constants.getProperty("Leaf_AQ_Amax"));
		phi  = Double.valueOf(WIMOVAC.constants.getProperty("Leaf_AQ_phi"));
		theta = Double.valueOf(WIMOVAC.constants.getProperty("Leaf_AQ_theta"));

	//  when run for whole season, use N to predict Amax and phi. 
		if(Constants.runGrowthModel)
		N_adj_Amax_phi();
	
		// adjust by temperature based on N-adjusted Amax and phi. 
		
		Temperature_adj_Amax_phi();
		
		// use AQ curve model to calculate Pn. 
		// PN ~ (PAR * phi + Amax - sqrt((phi * PAR + Amax)^2 - 4 * theta * phi * PAR * Amax))/(2 * theta)

		A = (PAR * phi + Amax - Math.sqrt((phi * PAR + Amax)*(phi * PAR + Amax) - 4 * theta * phi * PAR * Amax))/(2 * theta);  
		
		// A is photosynthesis only, not including Rd. 

	}    
	
	// ** used for FestGrowth ** //
	private void N_adj_Amax_phi(){

		if (Constants.stage>=0){
			Nleaf = Constants.LeafNperArea[Constants.stage]; // this value is input from GUI to WIMOVAC.constants and in Plant(), it is restored to Constants
		}
		
		// *** N % **** //
	//	Amax = 5.8736 * Nleaf - 5.9267; // R2 = 0.8132, the unit of Nleaf is %, usually 2.3~5.3%
	//	phi  = 0.005  * Nleaf + 0.0185; // R2= 0.3148. 
		
		// *** N per leaf area *** //
		Amax = 0.16811* Nleaf - 18.621;   // unit: Nleaf is g/m2
		phi = 0.2018 * Nleaf/1000 - 0.0045; // unit: Nleaf is g/m2	
		
	}  
	
	// ** used for FestGrowth ** //
	private void Temperature_adj_Amax_phi(){
		
		Amax = (-0.0433* T*T + 2.2774 *T - 5.9541) / (-0.0433* 25*25 + 2.2774 *25 - 5.9541) * Amax; // R2 = 0.9993, data from Yamori, W., et al. (2014). Xiurong
		phi = (-0.001*T + 0.084) / (-0.001*25 + 0.084) * phi; //R2= 1 , data from Ehleringer, J. and R. W. Pearcy (1983). Xiurong
		
	}
	
	
	// not used.
	private void cal_temperature_adj_c4() {

		double KQ10 = Math.pow(Double.valueOf(WIMOVAC.constants.getProperty("Q10_Coefficient")), ((T - 25) / 10));
		Vpmax = 0;
		Kp = 0;

		Vpmax = Double.valueOf(WIMOVAC.constants.getProperty("Leaf_C4Vpmax"));
		Kp = Double.valueOf(WIMOVAC.constants.getProperty("Leaf_C4Kp"));
		Vpr = Double.valueOf(WIMOVAC.constants.getProperty("Leaf_C4Vpr"));
		Jmax = Double.valueOf(WIMOVAC.constants.getProperty("Leaf_C4Jmax"));

		Vpmax = Vpmax * KQ10;
		Kp = Kp * KQ10;
		Vpr = Vpr * KQ10;
		Jmax = Jmax * KQ10;

	}

	private void cal_assimilation_c4() {

	}

	private void cal_stomatal_c4() {

	}

	public void cal_c4(Environment env) {

		boolean ReiterateC4 = true;
		String WarningMessage;// WarningTitle;
		boolean SeriousModuleError = false;

		T = Tair;
		double Cs = env.air.CO2_concentration; // surface CO2 concentration
												// equals to the environment CO2
												// concentration

		if (RH > 1 && !SeriousModuleError) {

			WarningMessage = "Oops. Something went wrong. Relative humidty is supposed to be passed "
					+ "to the C4 leaf photosynthesis module with a value between 0"
					+ " and 1. However a value outside this range has been encountered.\n\n";
			WarningMessage
					.concat("Please check your inputs to ensure that they conform to appropriate requirements.\n\n");
			WarningMessage.concat("Relative humidty value received =");
			WarningMessage.concat(Double.toString(RH));
			// WarningTitle = "C4 Photosynthesis Module";
			// ***********************I will pass WarningMessage and
			// WarningTitle to Warning Message Dialogue Box
			// ***********************TheAnswer = MsgBox(TheMessage$, TheStyle,
			// TheTitle$)
			SeriousModuleError = true;
		}

		if (Cs < 50 && !SeriousModuleError) {
			// Display warning to the user once that the module cannot calculate
			// very low values of Ci
			SeriousModuleError = true;
			WarningMessage = "Sorry can't reliably calculate A/Ci relationships using the "
					+ "iterative Ball/Berry stomatal conductance routines when atmospheric [CO2]'s less than 50(µmoles/mol).\n\n";
			WarningMessage
					.concat("Will attempt to continue anyway but be cautious when interpreting results for low Ca values.\n\n");
			// WarningTitle = "C4 Leaf Photosynthesis Warning!";
			// ***********************I will pass WarningMessage and
			// WarningTitle to Warning Message Dialogue Box
			// ***********************TheAnswer = MsgBox(TheMessage$, TheStyle,
			// TheTitle$)
		}

		if (Cs < 50) {
			Cs = 100;
		}

		// Convert atmospheric pressure from kPa to Pa.
		double Atmospheric_Pressure = Double.valueOf(WIMOVAC.constants.getProperty("Atmospheric_Pressure")) * 1000;

		// Convert the surface CO2 concentration in µmol/mol to a partial
		// pressure (Pa).
		Cs = (Cs * 1E-6) * Atmospheric_Pressure; // unit: Pa, 30 Pa CO2.

		if (Cs <= 0.05) {
			Cs = 0.05;
		}
		double InterCellularCO2 = Cs * 0.4; // Initial guestimate
		/*
		 * Temperature response of Kt, Vt and Rt, based on Arrhenius equations
		 * from SPLONG's paper. The original Q10 functions from the Collatz
		 * paper were ambiguous and gave temperature response problems.
		 */
		double KQ10 = Math.pow(Double.valueOf(WIMOVAC.constants.getProperty("Q10_Coefficient")), ((T - 25) / 10));
		double Kt = Double.valueOf(WIMOVAC.constants.getProperty("Leaf_C4CO2ResponseSlope")) * KQ10;

		/*
		 * We need to be able to express leaf nitrogen effects for C4 plants At
		 * the moment we do not have a mechanistic basis for this as we do for
		 * C3 and C4 plants. Steve Long has indicated that for a given nitrogen
		 * concentration we can expect C4 plants to express a Vcmax
		 * approximately twice that we calculate using Field or Harley's data
		 */
		double Vt = 0;
		switch (Constants.Use_Nitrogen_Calculations) {
		case 0:
			// Vt = Max_Rubisco_Capacity * TENZ(LeafTemperature, EVcmax, 1)
			// Original code
			double a = Double.valueOf(WIMOVAC.constants
					.getProperty("Leaf_C4MaxRubiscoCapacity"))
					* Math.pow(Double.valueOf(WIMOVAC.constants.getProperty("Q10_Coefficient")), ((T - 25) / 10));
			double b1 = 1 + Math.exp(0.3 * (13 - T));
			double b2 = 1 + Math.exp(0.3 * (T - 36));
			double c = a / (b1 * b2);
			Vt = c;
			// System.out.println("T: "+T);
			// System.out.println("Math.pow(Constants.Q10_Coefficient, ((T - 25) / 10)):  "+Math.pow(Constants.Q10_Coefficient,
			// ((T - 25) / 10)));
			// System.out.println("Vt"+Vt +"a: "+a+"b1:"+b1+"b2: "+b2);
			break;
		case 1:
			// Nitrogen effects from Field
			Vt = 2 * (Double.valueOf(WIMOVAC.constants
					.getProperty("Leaf_Nitrogen_Vcmax_K3"))
					* Double.valueOf(WIMOVAC.constants
							.getProperty("Constants.Leaf_NitrogenConcentration")) + Double
					.valueOf(WIMOVAC.constants
							.getProperty("Leaf_Nitrogen_Vcmax_K4")));
			Vt = Vt
					* TENZ(T, Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_EVcmax")), 1);
			break;
		case 2:
			// Nitrogen effects from harley, use different coefficients for
			// CO2>650
			if (Cs < 650) {
				Vt = 2 * (Double.valueOf(WIMOVAC.constants
						.getProperty("Leaf_VcmaxnitrogenHarleyK1")) + Double
						.valueOf(WIMOVAC.constants
								.getProperty("Leaf_VcmaxnitrogenHarleyK2"))
						* Double.valueOf(WIMOVAC.constants
								.getProperty("Leaf_NitrogenConcentration")));
				Vt = Vt
						* TENZ(T, Double.valueOf(WIMOVAC.constants
								.getProperty("Leaf_EVcmax")), 1);
			} else {
				Vt = 2 * (Double.valueOf(WIMOVAC.constants
						.getProperty("Leaf_VcmaxnitrogenHarleyK1_650")) + Double
						.valueOf(WIMOVAC.constants
								.getProperty("Leaf_VcmaxnitrogenHarleyK2_650"))
						* Double.valueOf(WIMOVAC.constants
								.getProperty("Leaf_NitrogenConcentration")));
				Vt = Vt
						* TENZ(T, Double.valueOf(WIMOVAC.constants
								.getProperty("Leaf_EVcmax")), 1);
			}

			break;
		}

		/*
		 * Currently we do not have the nitrogen effects coefficients necessary
		 * to correctly calculate the dark respiration rate for C4 leaves. To
		 * simplify matters dark respiration in c4 plants is assumed to be equal
		 * to that expected in c3 plants.
		 */
		double Rt = 0;
		switch (Constants.Use_Nitrogen_Calculations) {
		case 0:
			Rt = Double.valueOf(WIMOVAC.constants
					.getProperty("Leaf_C4LeafRespirationRate"));
			break;
		default:
			// Use this with either the Field or Harley nitrogen coefficients
			Rt = Double.valueOf(WIMOVAC.constants
					.getProperty("Leaf_Nitrogen_Rd_K7"))
					* Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_NitrogenConcentration"))
					+ Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_Nitrogen_Rd_K8"));
			break;
		}

		// Handle respiration according to model parameters database
		switch (Integer.valueOf(WIMOVAC.constants
				.getProperty("Respiration_CalculationMethod"))) {
		case 0:
			Rt = 0;
			// Rtn = Leaf_Respiration_Rate * Math.pow(2, ((LeafTemperature - 25)
			// / 10));
			// Rtd = 1+Math.exp(1.3 * (LeafTemperature - 55));
			// Rt = Rtn / Rtd ;
			break;
		case 1:
			switch (Integer.valueOf(WIMOVAC.constants
					.getProperty("Leaf_RespirationTemperatureResponse"))) {
			case 1:
				// Calculate the leaf dark respiration rate with arrhenius
				// temperature response
				Rt = Rt
						* TENZ(T, Double.valueOf(WIMOVAC.constants
								.getProperty("Leaf_ERd")), 6);
				break;
			case 2:
				// Calculate dark respiration rate with Q10 temperature response
				// function
				Rt = Calculate_Q10_Rate(Rt, Double.valueOf(WIMOVAC.constants.getProperty("Q10_Coefficient")), T);
				break;
			}
		default:
			// Use canopy respiration calculation is indicated
			Rt = 0;
			break;
		}

		// Convert from moles to umoles to keep the pseudo first order rate
		// constant kt in keeping with the calculation.

		Kt = Kt * 1E6;

		// Setup the quadratic coefficients ready for the calculation of real
		// roots

		double Quada = Vt * Constants.alpha * PPFD;
		double Quadb = Vt + Constants.alpha * PPFD;
		double Quadc = Constants.Theta;

		/*
		 * Get the two roots which represent the gross photosynthetic rate
		 * UpperM is the CO2 flux determined by the rubisco and light limited
		 * capacities.
		 */
		double M1 = (Quadb + Math.sqrt(Quadb * Quadb - 4 * Quada * Quadc))
				/ (2 * Quadc);
		double M2 = (Quadb - Math.sqrt(Quadb * Quadb - 4 * Quada * Quadc))
				/ (2 * Quadc);
		// System.out.println(M1+ "  " +M2); //
		// Take the smaller real root.
		double LowerM = 0;
		if (M1 < M2)
			LowerM = M1;
		else
			LowerM = M2;

		// Keep track of the number of iterations in the calcualtion of
		// assimilation
		// rate and stomatal conductance which have occurred. Limit the number
		// to 20.

		double Assim = 500;
		double AssimOld = 500; // Set some dummy values
		// Tolerance = 0.01; // Set some dummy values
		int IterationCounter = 0; // Reset the iteration counter
		// MaxIterations = 200;
		// System.out.println("Kt: "+Kt);
		double Gs = 0;
		while (ReiterateC4) {
			/*
			 * Now solve the nested quadratic to calculate the gross
			 * assimilation rate. Note that the calculation of the QuadB term
			 * below would seem to require bracketers around the LowerM+Kt term
			 * according to the Collatz paper-however in practise this gives
			 * apparently incorrect results.
			 */
			Quada = LowerM * Kt * InterCellularCO2 / Atmospheric_Pressure;
			Quadb = LowerM + Kt * (InterCellularCO2 / Atmospheric_Pressure);

			Quadc = Constants.beta;

			// Trial and error has dictated that the second root is the
			// appropriate
			// one to use in this case.

			// a1 = (Quadb + Sqr(Quadb ^ 2 - (4 * Quada * Quadc))) / (2 * Quadc)
			double a2 = (Quadb - Math.sqrt(Quadb * Quadb - 4 * Quada * Quadc))
					/ (2 * Quadc);

			// System.out.println("a2: " + a2);

			// Calculate the assimilation rate (net or gross depending on
			// whether Rt has been calculated)
			Assim = a2 - Rt;

			// If Humidity is 0 we may get a calculation error and so ensure
			// that the humidity is always a small positive number

			if (RH <= 0)
				RH = 0.05;

			// Calculate Stomatal conductance in moles/m2/s
			// Gs = Stomatal_slope * (((Assim * 10 ^ -6) * humidity *
			// Atmospheric_Pressure) / Csurface) + Stomatal_intercept

			g0 = Double.valueOf(WIMOVAC.constants
					.getProperty("Leaf_C4StomatalInterceptFactor"));
			g1 = Double.valueOf(WIMOVAC.constants
					.getProperty("Leaf_C4StomatalSlopeFactor"));
			Gs = getGsfromRH(RH);

			Gs = Gs * (1 - Constants.gsWaterStressModifier);
			// Adjust the stomatal conductance to effect stomatal closure due to
			// water stress
			// if we are running the soil and leaf water potential module

			// Check for an faulty calculations and give audible warning
			if (Gs <= 0)
				Gs = 0.01;
			InterCellularCO2 = Cs - (Assim * 1E-6) * 1.6 * Atmospheric_Pressure
					/ (Gs * 0.001);
			if (InterCellularCO2 < 2)
				InterCellularCO2 = 2;

			double Tolerance = 0.01;

			// Numerical tolerance in the iteration, no more change than this
			// between
			// the values of assimilation rate between iterations are allowed.

			if (IterationCounter < 1000) {
				if (Assim < (AssimOld - Tolerance)
						|| Assim > (AssimOld + Tolerance)) {
					AssimOld = Assim;
					IterationCounter++;

				} else {
					ReiterateC4 = false;
				}
			} else {
				ReiterateC4 = false;
			}
		} // end of ReiterateC4 loop

		// Set the output variables for the module
		Vcmax = Vt;
		Rd = Rt;

		Cm = (InterCellularCO2
				/ Double.valueOf(WIMOVAC.constants
						.getProperty("Leaf_AtmosphericPressure")) * 1E6);

		A = Assim;

	}

	public void cal_c4_Von(Environment env) {

		/*
		 * parameters for C4 model, Von
		 */
		Vcmax = Double.valueOf(WIMOVAC.constants.getProperty("Leaf_C4Vcmax"));
		Vpmax = Double.valueOf(WIMOVAC.constants.getProperty("Leaf_C4Vpmax"));
		Kp = Double.valueOf(WIMOVAC.constants.getProperty("Leaf_C4Kp"));
		Vpr = Double.valueOf(WIMOVAC.constants.getProperty("Leaf_C4Vpr"));
		Jmax = Double.valueOf(WIMOVAC.constants.getProperty("Leaf_C4Jmax"));
		curvature_Factor = Double.valueOf(WIMOVAC.constants
				.getProperty("Leaf_Curvature_Factor"));

		double Rm = 0.5 * Rd; // OK.
		double gL = 0.003; // unit: mol.m-2.s-1, OK. this is the bundle sheath
							// conductance.
		double x = 0.4; // OK.

		Cs = env.air.CO2_concentration; // here the Cs is leaf surface CO2. not
										// BS CO2
		T = Tair;

		/*
		 * calculate intermediate parameters
		 */
		// cal_temperature_adj_c4();

		/*
		 * calculate the assimilation rate
		 */
		// cal_assimilation_c4();

			double Ci2CsRatio = Double.valueOf(WIMOVAC.constants.getProperty("Leaf_C4Ci2CsRatio"));
			Ci = Ci2CsRatio*Cs;    

			Cm = Ci; // assume Cm equals to Ci;

			double Vp = Cm * Vpmax / (Cm + Kp); // (Eqn 24)
			if (Vpr < Vp) { // Vpr, is the maximal Vp.
				Vp = Vpr;
			}

			double Ac1 = Vp - Rm + gL * Cm; // (Eqn 25)
			
			
			double Ac2 = Vcmax - Rd;
			double Ac = Ac1;

			if (Ac2 < Ac1) { // (Eqn 30)
				Ac = Ac2;
			}

			FEII = 0.85;

			double I2 = PPFD
					* FEII
					* (1 - Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_Reflectance"))) / 2;

			double Jt = (I2 + Jmax - Math.pow((Math.pow((I2 + Jmax), 2) - 4
					* curvature_Factor * I2 * Jmax), 0.5))
					/ (2 * curvature_Factor); // the Jt calculation is the same
												// as J for C3.

			double Aj1 = (1 - x) * Jt / 3 - Rd; // (Eqn. 27)

			double Aj2 = x * Jt / 2 - Rm + gL * Cm; // (Eqn. 28)

			// System.out.println("Cs: "+Cs+" Ac: "+Ac +" Aj: "+Aj +" Rd: "+Rd);
			double Aj = Aj1;
			if (Aj2 < Aj) {
				Aj = Aj2;
			}

			A = Ac;
			if (Aj < Ac) {
				A = Aj;
			}
	
	}

	private void cal_assimilation() {
		/*
		 * Calculate J, the rate of electron transport from I2, Jmax, and a
		 * curvature factor. Curvature factor determines how quickly the
		 * transition is made from the region of maximum quantum yield to the
		 * light saturated light, value range 0<=curve_factor<=1. Curve_factor
		 * value of 0 causes the equation generates a rectangular hyperbola, and
		 * a value of 1 generates the Blackman response of two straight lines
		 * representing light-dependent and light-saturated rates.
		 */

		double I2 = PPFD
				* FEII
				* (1 - Double.valueOf(WIMOVAC.constants
						.getProperty("Leaf_Reflectance"))) / 2;

		// I2 = photon_flux * (1 - Spectral_Imbalance) * (1 - Leaf_Reflectance)
		// / 2;
		// double Jmax = leaf.getJmax();
		double j = (I2 + Jmax - Math.pow((Math.pow((I2 + Jmax), 2) - 4
				* curvature_Factor * I2 * Jmax), 0.5))
				/ (2 * curvature_Factor);
		// j = (I2 + Jmax - Math.pow((Math.pow((I2 + Jmax), 2) - 4 *
		// Curvature_Factor * I2 * Jmax), 0.5)) / (2 * Curvature_Factor);

		// J = Jmax * Photon_Flux / (Photon_Flux + 2.1 * Jmax): ' Long prog
		// J = (Jmax * .67 * Photon_Flux) / (.67 * Photon_Flux + 2.1 * Jmax): '
		// Long 1st Paper
		// alpha = .24: ' Harleys paper, light use efficiency parameter
		// J = alpha * Photon_Flux / ((1 + ((alpha ^ 2 * Photon_Flux ^ 2) / Jmax
		// ^ 2)) ^ .5): ' Harleys paper
		// System.out.print(I2+"\t");
		/*
		 * If Ca, the atmospheric concentration of CO2 is 0 then an error will
		 * occur even though this is a reasonable value to want to simulate
		 * therefore, apply a small fix which makes Ca a small positive number
		 * which a zero or negative number is entered by the user.
		 */

		if (Cs <= 0) {
			Cs = 0.0001;
		} // Prevent calculation error

		/*
		 * Calculate Ci, the intercellular concentration of CO2 in air,
		 * corrected for solubility relative to 25C. This calculation of Ci is
		 * in the form of a polynomial and provides a good INITIAL estimate of
		 * Ci for use in the stomatal conductance equations from Ball (1988)
		 * 
		 * Note that SOLC supplies the adjustment factor for solubility at
		 * temperatures other than 25C. Note that SOLC is adjusted to return a
		 * value of 1 at 25C and that this implies that Ci equals 0.7 of the
		 * atmospheric CO2 conc Ca at 25c. This is a guestimate
		 */

		// double Ci = Cs * SOLC(T) * 0.7;

		/*
		 * Calculate Oi, the intercellular concentration of O2 in air, corrected
		 * for solubility relative to 25C. SOLO is a function containing the
		 * polynomial description of solubility which allows for this
		 * adjustment. Note that at 25C SOLO is adjusted to return a value of 1
		 * which means that Oi=Osurface at 25C.
		 */
		double Oi = Os * SOLO(T);

		/*
		 * Calculate Gstar, the CO2 compensation point of photosynthesis in the
		 * absence of dark respiration. Note that the traditional symbol for
		 * this looks like an upside down L with an asterix next to it.
		 */

		// Gstar = 0.5 * Vomax * kc * Oi / (Vcmax * Ko) This is a test, 22004
		/*
		 * Gstart calculation input leaf temperature
		 */
		double Gstar = TENZ(T,
				Double.valueOf(WIMOVAC.constants.getProperty("Leaf_EGstar")), 7); // This
																					// is
																					// to
																					// calculate
																					// the
																					// actual
																					// Gama
																					// Star
																					// which
																					// is
																					// based
		// on the paper published by Carl Bernacchi et al on Plant Cell and
		// Environment.

		Gstar = 0.5 * Oi * Vomax * Kc / Ko / Vcmax; // from: S. Von Caemmerer
													// book, page 35 and 36,
													// changed by Qingfeng
													// 2015-02-26

		/*
		 * Note that the equation for Wc comes from the paper by Evans. Wc is
		 * the rate of carboxylation limited solely by the amount activation
		 * state and kinetics of RUBISCO and obeys Michaelis Menten kinetics
		 * with respect to O2 and CO2
		 */
		// double Vcmax = leaf.getVcmax();
		// double Kc = leaf.getMichaelisConstantCO2();
		// double Ko = leaf.getMichaelisConstantO2();

		double Wc = (Vcmax * Ci) / (Ci + Kc * (1 + Oi / Ko)); // Long paper/also
																// Harley
		// Wc = Vcmax * (Ci - Gstar) / (Ci + Kc * (1 + Oi / Ko)): ' Long prog

		/*
		 * Calculate Wj, which is the rubp limited rate of carboxylation. Wj is
		 * the rate of carboxylation limited solely by the rate of RuBP
		 * regeneration in the Calvin Cycle. Wj is mediated by the rate of
		 * electron transport. Implicit in the formulation for Wj is the
		 * assumption that four electrons generate sufficient ATP and NADPH for
		 * the regeneration of RuBP in the Calvin cycle.
		 */

		// This equation for Wj is taken from the Farquhar paper.

		double Wj = (j * Ci) / ((4 * Ci) + 8 * Gstar); // (Von Caemmerer
														// Modeling Book)

		// Wj = j * (Ci - Gstar) / (4.5 * Ci + 10 * Gstar); //' Long prog
		// tau = Exp(-3.949 - (-28.99 / (.00831 * (LeafTemperature + 273)))): '
		// Harley Paper
		// Wj = (J * Ci) / (4 * (Ci + Oi / tau)): ' Harley paper

		/*
		 * Calculate Wp, which is the the rate of carboxylation under conditions
		 * where RuBP regeneration is limited by the availability of Pi (organic
		 * phosphate), and assimilation becomes insensitive to both CO2 and O2
		 * (Sharkey 1985)
		 */
		double Wp = (3 * Double.valueOf(WIMOVAC.constants
				.getProperty("Leaf_Rate_tri_phos_util"))) / (1 - Gstar / Ci);

		/*
		 * Determine which is the smaller value, the rubP saturated rate of
		 * carboxylation Wc or the rubP limited rate of carboxylation Wj or .
		 * Which ever is the smaller of the two return as the variable VC which
		 * is the velocity of carboxylation. We need a slightly more complex set
		 * of conditions here than just finding the smallest value because at
		 * low values of Ci the term 1-gamma * / Ci can give rise to a negative
		 * number and this does not make any sense. We could use the absolute
		 * value of Wp here for testing conditions or simple set Vc=Wc if Wj has
		 * become negative. Note that Wj only seems to become negative at low Ci
		 * values and so assuming Wc applies is probably ok
		 */

		/*
		 * Vc is the minimal of Wc, Wj and Wp
		 */
		double Vc = 0;
		// String RateLimitingStep = "";
		if (Wc < Wj && Wc < Wp) {
			Vc = Wc;
			// RateLimitingStep = "Carboxylation";
		} else if (Wj < Wc && Wj < Wp) {
			Vc = Wj;
			// RateLimitingStep = "Electon transport";
		} else if (Wp < Wc && Wp < Wj) {
			if (Wp < 0) {
				Wp = 0;
			}
			Vc = Wp;
			// RateLimitingStep = "Inorganic phosphate";
		}
		// System.out.println("Wc: "+Wc);
		// System.out.println("Wj: "+Wj);
		// System.out.println("Wp: "+Wp);
		// System.out.println("Vc: "+Vc);
		// Calculate the net rate of CO2 assimilation per unit leaf area.
		// Vc is the velocity of carboxylation, units of assimilation are
		// µmol/m2/s

		/*
		 * leaf CO2 assimilation rate A calculation
		 */
		// double Rd = 0;

		switch ((int) (double) Double.valueOf(WIMOVAC.constants
				.getProperty("Respiration_CalculationMethod"))) {
		case 1:
			// Leaf dark respiration has been selected, decide whether to base
			// this on leaf
			// nitrogen content
			switch (Constants.Use_Nitrogen_Calculations) {
			case 0:
				Rd = Double.valueOf(WIMOVAC.constants
						.getProperty("Leaf_DarkRespirationCoef"));
				break;
			default:
				// Use this with either the Field or Harley nitrogen
				// coefficients

				Rd = Double.valueOf(WIMOVAC.constants
						.getProperty("Leaf_Nitrogen_Rd_K7"))
						* Double.valueOf(WIMOVAC.constants
								.getProperty("Leaf_NitrogenConcentration"))
						+ Double.valueOf(WIMOVAC.constants
								.getProperty("Leaf_Nitrogen_Rd_K8"));

				break;
			}

			switch (Integer.valueOf(WIMOVAC.constants
					.getProperty("Leaf_RespirationTemperatureResponse"))) {
			case 0:
				// Don't use a temperature response function
			case 1:
				// Use an arrhenius equation temperature response
				// System.out.println("Rd1: "+Rd);
				Rd = Rd
						* TENZ(T, Double.valueOf(WIMOVAC.constants
								.getProperty("Leaf_ERd")), 6); // Long prog
																// Original 6
				// System.out.println("Rd2: "+Rd);
				break;
			case 2:
				// Use a Q10 function temperature response
				Rd = Calculate_Q10_Rate(Rd, Double.valueOf(WIMOVAC.constants.getProperty("Q10_Coefficient")), T);
				// ***************I will find out in which file this function
				// has been defined
				break;
			}
			break;
		default:
			// Either don't calculate respiration or use canopy level
			// respiration calc
			Rd = 0;
			break;
		}

		A = (1 - Gstar / Ci) * Vc - Rd; // Splongs original formulation

		// / leaf2.setLeafAssimilationRate(AssimilationRate);
		// leaf2.setRd(Rd);
		// AssimilationRate = Vc - Rd
		// assimilationrate = (1 - ((.5 * Oi) / (tau * Ci))) * vc - Rd: ' Harley

		// results: Rd

	}

	private void cal_temperature_nitrogen_adj() {

		// double Cs = env.air.CO2_concentration; // surface CO2 concentration
		// equals to the environment CO2 concentration

		Vcmax = 0;
		Jmax = 0;
		int Use_Nitrogen_Calculations = 0;
		switch (Use_Nitrogen_Calculations) {
		case 0: // hang here
			Vcmax = Double
					.valueOf(WIMOVAC.constants.getProperty("Leaf_Vcmaxo"))
					* TENZ(T, Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_EVcmax")), 1); // Long prog
																// Original 1
			Vomax = Double
					.valueOf(WIMOVAC.constants.getProperty("Leaf_Vomaxo"))
					* TENZ(T, Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_EVcmax")), 1); // Long prog
																// Original 1
			Jmax = Double.valueOf(WIMOVAC.constants.getProperty("Leaf_JMaxo"))
					* TENZ(T, Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_EJmax")), 5); // Long prog
																// Original 5
			break;
		case 1:
			System.out.println("case1");
			// Nitrogen effects from Field
			Vcmax = (Double.valueOf(WIMOVAC.constants
					.getProperty("Leaf_Nitrogen_Vcmax_K3"))
					* Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_NitrogenConcentration")) + Double
						.valueOf(WIMOVAC.constants
								.getProperty("Leaf_Nitrogen_Vcmax_K4")))
					* TENZ(T, Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_EVcmax")), 1);
			Vomax = 0.25 * Vcmax; // when Vcmax is calculated based on leaf N
									// content, then the Vomax was assumed
									// corresponding to Vcmax
			if (Vcmax <= 0) {
				Vcmax = 0.001;
			}
			Jmax = (Double.valueOf(WIMOVAC.constants
					.getProperty("Leaf_Nitrogen_Jmax_K5"))
					* Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_NitrogenConcentration")) + Double
						.valueOf(WIMOVAC.constants
								.getProperty("Leaf_Nitrogen_Jmax_K6")))
					* TENZ(T, Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_EJmax")), 5);
			if (Jmax <= 0) {
				Jmax = 0.001;
			}
			break;
		case 2:
			System.out.println("case2");
			// Nitrogen effects from harley, use different coefficients for
			// CO2>650
			if (Cs < 650)
				Vcmax = (Double.valueOf(WIMOVAC.constants
						.getProperty("Leaf_VcmaxnitrogenHarleyK1")) + (Double
						.valueOf(WIMOVAC.constants
								.getProperty("Leaf_VcmaxnitrogenHarleyK2")) * Double
						.valueOf(WIMOVAC.constants
								.getProperty("Leaf_NitrogenConcentration"))))
						* TENZ(T, Double.valueOf(WIMOVAC.constants
								.getProperty("Leaf_EVcmax")), 1);
			else
				Vcmax = (Double.valueOf(WIMOVAC.constants
						.getProperty("Leaf_VcmaxnitrogenHarleyK1_650")) + (Double
						.valueOf(WIMOVAC.constants
								.getProperty("Leaf_VcmaxnitrogenHarleyK2_650")) * Double
						.valueOf(WIMOVAC.constants
								.getProperty("Leaf_NitrogenConcentration"))))
						* TENZ(T, Double.valueOf(WIMOVAC.constants
								.getProperty("Leaf_EVcmax")), 1);
			// Set the rate of triosphosphate utilisation

			TPU = Double.valueOf(WIMOVAC.constants
					.getProperty("Leaf_TPUNitrogenHarleyK1"))
					+ (Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_TPUNitrogenHarleyK2")) * Double
							.valueOf(WIMOVAC.constants
									.getProperty("Leaf_NitrogenConcentration")));
			if (Vcmax <= 0) {
				Vcmax = 0.001;
			}
			Vomax = 0.25 * Vcmax; // when Vcmax is calculated based on leaf N
									// content, then the Vomax was assumed
									// corresponding to Vcmax

			Jmax = (Double.valueOf(WIMOVAC.constants
					.getProperty("Leaf_JmaxnitrogenHarleyK1")) + (Double
					.valueOf(WIMOVAC.constants
							.getProperty("Leaf_JmaxnitrogenHarleyK2")) * Double
					.valueOf(WIMOVAC.constants
							.getProperty("Leaf_NitrogenConcentration"))))
					* TENZ(T, Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_EJmax")), 5);
			if (Jmax <= 0) {
				Jmax = 0.001;
			}

			if (TPU <= 0) {
				TPU = 0.001;
			}
			break;

		}

		// double Vomax = Vcmaxo * TENZ(LeafTemperature, EVomax, 2); // 60.11 is
		// the activation energy from Bernacchi et al PCE. 2001

		/*
		 * Calculate the Michaelis constant for oxygen from EKo and adjust to
		 * allow for temperature corrections. Note that EKo is the activation
		 * energy for oxygen and is defined in the global variables section of
		 * the model. Koo is the Michaelis constant at 25C.
		 */

		Ko = Double.valueOf(WIMOVAC.constants.getProperty("Leaf_Koo"))
				* TENZ(T, Double.valueOf(WIMOVAC.constants
						.getProperty("Leaf_EKo")), 3); // Long prog Original 3

		/*
		 * Calculate the Michaelis constant for CO2 from EKc and adjust to allow
		 * for temperature corrections. Note that EKc is the activation energy
		 * for CO2 and is defined in the parameter file. Kco is the Michaelis
		 * constant at 25C.
		 */

		Kc = Double.valueOf(WIMOVAC.constants.getProperty("Leaf_Kco"))
				* TENZ(T, Double.valueOf(WIMOVAC.constants
						.getProperty("Leaf_EKc")), 4); // Long prog Original 4

		/*
		 * Calculate Jmax, which is the light saturated potential rate of
		 * electron transport, and adjust for temperature. Note that EJmax is
		 * the activation energy of light sat electron transport and that Jmaxo
		 * is a global variable containing the light saturated potential rate of
		 * electron transport at 25C.
		 */

		// Add in one step to calculate the differnet theta values at different
		// temperature;

		curvature_Factor = Double.valueOf(WIMOVAC.constants
				.getProperty("Leaf_Curvature_Factor")); // 0.76 + 0.018 * T -
														// 3.7 * Math.pow(10,
														// -4) * Math.pow(T, 2);

		/*
		 * The equation for the calculation of J specified in Steve's paper has
		 * been changed to that suggested by Evans/Farquhar, modeling at the
		 * chloroplast level(chpt) in Modeling Crop Photosynthesis-from
		 * biochemistry to canopy.
		 * 
		 * This involves a two step calculation. Which initially includes a
		 * consideration of the relationship between potential electron
		 * transport rate J and the irradiance usefully absorbed by photosysthem
		 * II, I2
		 * 
		 * Calculate the irradiance sucessfully absorbed by photosystem II, I2
		 * from the incident radiation (400-700nm), photon_flux. The spectral
		 * imbalance term allows for the spectral imbalance of light and
		 * Leaf_Reflectance allows for reflectance plus any small transmittance
		 * of the leaf or crop to photosynthetically active radiation. The
		 * photon flux term is divided by 2 to allow for the fact that light
		 * needs to be absorbed by both photosysthem I and II to drive one
		 * electron from H2O to NADP+.
		 */

		// output Vcmax, Jmax, Ko, Kc, curvature_factor,
	}

	private double getGsfromRH(double rh) {

		// double Cs = env.air.CO2_concentration; // surface CO2 concentration
		// equals to the environment CO2 concentration

		// double rh = 0.7;

		if (rh < 0.2) {
			rh = 0.2;
		} // Prevents the model making incorrect values at low RH

		double Ca = Cs / 10; // Pa
		// Debug.Print (Ca)
		double a = A; // micromole per meter square per second
		double gb = 1000;
		double cs = Ca - (1.4 * 100 / gb) * a;
		// In the boundary layer, not only the diffusion but also the turbulence
		// is involved. Therefore,
		// 1.4 instead of 1.6 should be used.

		double esat_leaf = utl.SaturaingVaporPressure(T);

		double esat_air; // hang here
		if (Constants.canopycalculation) {
			esat_air = utl.SaturaingVaporPressure(Tair);

			// System.out.println("CanopyCalculation== " +
			// String.valueOf(CanopyCalculation));
			// System.out.println("macrooutT== " + String.valueOf(macrooutT));
		} else {
			esat_air = esat_leaf;
		}

		double rhp = rh * esat_air / esat_leaf;
		// double g1 = leaf.getG1();
		double A1 = g1 * a * 100;

		// double g0 = leaf.getG0();

		double b = g0;
		double c = rhp * gb;
		double d = cs * gb;

		double a2 = cs;
		double b2 = -(b * cs + A1 - d);
		double c2 = -(A1 * c + b * d);

		double Gs = (-b2 + Math.sqrt(b2 * b2 - 4 * a2 * c2)) / (2 * a2);

		// System.out.println("song Gs: "+Gs);
		return Gs;

	}

	private void cal_stomatal(Environment env) {

		// double Cs = env.air.CO2_concentration; // surface CO2 concentration
		// equals to the environment CO2 concentration

		/*
		 * stomatal conductance and Ci calculation input Assimilation rate
		 */
		double ppCsurface = 0;
		// double g0, g1;
		double ppNewCi;
		double NewCi = 0;

		// input
		double AssimilationRate = A;

		// output
		Gs = 0;
		// and output Ci

		// Here, only used the first method: "Leaf_Stomatal_conductance_flag" ==
		// 1
		if ((int) (double) Double.valueOf(WIMOVAC.constants
				.getProperty("Leaf_Stomatal_conductance_flag")) == 1) {

			// Use the Ball (1988) stomatal conductance model.
			// gs is the approximate stomatal conductance and 1.6 represents
			// the correction factor for the diffusivities of water and CO2.
			// Convert the surface CO2 concentration in µmol/mol terms to
			// a partial pressure (Pa) term by multiplying by atm pressure
			ppCsurface = 1E5 * (Cs * 1E-6); // unit: Pa

			// Once method for implementing the water stress reduction
			if (Cs <= 350) {
				g1 = Double.valueOf(WIMOVAC.constants
						.getProperty("Leaf_Stomatal_Coef_g1"));
				g0 = Double.valueOf(WIMOVAC.constants
						.getProperty("Leaf_Stomatal_Coef_g0"));
			} else {
				g1 = Double.valueOf(WIMOVAC.constants
						.getProperty("Leaf_Stomatal_Coef_g1_650"));
				g0 = Double.valueOf(WIMOVAC.constants
						.getProperty("Leaf_Stomatal_Coef_g0_650"));
			}

			// not used water stress. Qingfeng
			// if
			// (Double.valueOf(WIMOVAC.constants.getProperty("Leaf_StomatalWaterStressFactor"))
			// > 0.5 ) {
			// Reduce the number of iterations under conditions of
			// extreme water stress
			// MaxIterations = 1;
			// }

			// g1 = g1 * (1 -
			// Double.valueOf(WIMOVAC.constants.getProperty("Leaf_StomatalWaterStressFactor")));

			// CanopyCalculation = canopycalculation;
			// CanopyCalculation=0; // For leave level and canopy single layer
			// level CanopyCalculation=0, for canopy multi-layer
			// CanopyCalculation=1

			Gs = getGsfromRH(RH);

			// Calculate the stomatal conductance in (mmol/m2/s)
			// Gs = g0 + g1 * AssimilationRate * (humidity / ppCsurface) This is
			// the orginal calculation

			// Gs = Gs * (1 -
			// Double.valueOf(WIMOVAC.constants.getProperty("Leaf_StomatalWaterStressFactor")));
			// Adjust the stomatal conductance to effect stomatal closure due to
			// water stress
			// if we are running the soil and leaf water potential module.
			// Debug.Print (Gs);

			if (Gs <= 1) {
				Gs = 1;
			} // Prevent divide by 0 error

			// Calculate a new value of Ci partial pressure based on the
			// calculated stomatal conductance and assimilation rate
			ppNewCi = ppCsurface - (AssimilationRate * 1.6 * 100) / Gs; // Original:
																		// unit:
																		// Pa

			// Convert ppNewCi back into units of umol/mol
			NewCi = (ppNewCi / 1E5 * 1E6) * SOLC(T); // Original
			// System.out.println("song NewCi: "+NewCi);

			/*
			 * ##################################################################
			 * ####################################################### Tiger
			 * stomatal conductance model. Need to ensure that soil water
			 * potential and canopy transpiration are calculated.
			 * ###############
			 * ###################################################
			 * #######################################################
			 * 
			 * We need a single value to represent the soil water potential for
			 * the stomatal conductance model. However the soil water potential
			 * model is multi-layered. What we need is a weighted mean of the
			 * layered soil water potentials based upon the root distribution in
			 * each of the soil layers and their respective soil water
			 * potential. In Campbell soil water potential model he defines the
			 * leaf water potential in this manner and so this has been used in
			 * preference to the soil water potential in the top soil layer.
			 * 
			 * If we dont appear to have a value for leaf water potential
			 * because soil calculations have not been performed or soil water
			 * potential is being supplied as a single value from a model dialog
			 * then use first layer of the soil water potential values.
			 */
		} else if (Double.valueOf(WIMOVAC.constants
				.getProperty("Leaf_Stomatal_conductance_flag")) == 2) {
			// ====================use it when canopy calculation involved

			// double Rd = leaf.getRd();
			if ((AssimilationRate + Rd) < 0.5) {
				AssimilationRate = 0.5;
			}
			double SoilWaterPotential;
			double[] SoilWaterPotentialArray;
			if (env.soil.WtAverageSoilWaterPotential < 0) {
				// We have a leaf water potential value (soil model is being
				// run)
				SoilWaterPotential = env.soil.WtAverageSoilWaterPotential; // kPa
				if (SoilWaterPotential >= 0) {
					SoilWaterPotential = -1;
				} // Ensure soil water potential is negative
			} else {
				// We dont have a weighted average water potential use the first
				// soil layer instead
				SoilWaterPotentialArray = env.soil.LayerSoilWaterPotential;
				SoilWaterPotential = SoilWaterPotentialArray[1]; // kPa
				if (SoilWaterPotential >= 0) {
					SoilWaterPotential = -1;
				} // Ensure soil water potential is negative
			}
			// Get the transpiration rate
			// use this param to pass value from stomatal conductance module not
			// ready for general canopy use.
			// Transpiration = LeafAux.getLeafTranspiration(); // should be
			// canopy transpiration for leaf layer

			// double Transpiration = leaf.getLeafTranspiration();
			double Transpiration = E / 1000; // convert from mmoles/m2/s to
												// moles/m2/s

			SoilWaterPotential = SoilWaterPotential / 1000; // Convert
															// transpiration
															// from mmoles/m2/s
															// to moles/m2/s

			// Direct effect of light is on gsmax
			double gsmax = (Double.valueOf(WIMOVAC.constants
					.getProperty("Leaf_Maximum_stomatal_conductance")) / 1000)
					* (1 - Math.exp(Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_StomatalCoef2")) * PPFD)); // To
																			// give
																			// mols/m2/s

			// Calculate the VPD from the relative humidity near the leaf and
			// the ambient air temperature

			double VPD = utl.ConvertHumiditytoVPD(RH, T); // Gives VPD in kPa
			// ******************I will find out where this function come from

			// double e = VPD * Gs;
			// VPD in the stomatal conductance model uses (mol/mol) we need to
			// convert from kPa
			// to do this we can use the ideal gas law P*V=n*R*T
			double MoleFractionWater = (VPD * 22.4 / (T + 273)) / 8.314;

			VPD = MoleFractionWater;

			// New formulation for gs gives low stomatal conductance at low A
			// and high Ci when low gs

			// double Ci = leaf.getCi();
			Gs = gsmax
					+ Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_StomatalCoef1"))
					* (SoilWaterPotential - 1) * (Transpiration + VPD) - 700
					* (Ci * 1E-6);

			Gs = Gs * 1000; // Convert from moles to mmoles

			gsmax = gsmax * 1000;

			if (Gs > gsmax) {
				Gs = gsmax;
			} // Should the calculated gs be limited by gsmax???????
			if (Gs <= 0) {
				Gs = 0.0001;
			} // Prevent potential calculation problems resulting from 0 gs
				// values

			// Calculate a new value of Ci partial pressure based on the
			// calculated stomatal conductance and assimilation rate.
			ppCsurface = 1E5 * (Cs * 1E-6);
			// double AssimilationRate = leaf.getLeafAssimilationRate();
			ppNewCi = ppCsurface - (AssimilationRate * 1.6 * 100) / Gs;

			// Convert ppNewCi back into units of µmol/mol
			NewCi = (ppNewCi / (1E5) * (1E6)) * SOLC(T);
			if (NewCi <= 0.1) {
				NewCi = 0.1;
			}

			// Perform iteration until tolerance limits are reached ie.
			// Ci does not change by more than the tolerance amount between
			// iterations.

		}
		Ci = NewCi;
	}

	private double TENZ(double LeafTemperature, double ActivationEnergy,
			int VarOption) {

		double TENZ = 0;
		/*
		 * Function found: Plant cell and Environment, (1991) 14, 729-739.
		 * Modification of the response of photosynthetic activity to rising
		 * temperature by atmospheric CO2 concentrations: Has its importance
		 * been underestimated? This function takes the temperature and an
		 * activation energy (E) and calculates a Km which is returned as the
		 * function value.
		 */
		switch (VarOption) {
		case 0:
			TENZ = Math
					.exp(((LeafTemperature - 25) * ActivationEnergy)
							/ (298 * (273 + LeafTemperature) * Constants.RealGasConstant))
					* (Math.sqrt((273 + LeafTemperature) / 298));
			// This is the original temperature function
			break;
		case 1:
			TENZ = Math
					.exp(Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_cVcmax"))
							- (ActivationEnergy / (Constants.RealGasConstant / 1000 * (LeafTemperature + 273.15))));
			break;
		case 2:
			// TENZ =
			// Math.exp(Double.valueOf(WIMOVAC.constants.getProperty("Leaf_cVcmax"))
			// - (ActivationEnergy / (Constants.RealGasConstant / 1000 *
			// (LeafTemperature + 273.15))));
			break;
		case 3:
			TENZ = Math
					.exp(Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_cKo"))
							- (ActivationEnergy / (Constants.RealGasConstant / 1000 * (LeafTemperature + 273.15))))
					/ Math.exp(Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_cKo"))
							- (ActivationEnergy / (Constants.RealGasConstant / 1000 * (25 + 273.15))));// 276.9;//
																										// Double.valueOf(WIMOVAC.constants.getProperty("Leaf_Koo"));//276.9;
			break;
		case 4:
			TENZ = Math
					.exp(Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_cKc"))
							- (ActivationEnergy / (Constants.RealGasConstant / 1000 * (LeafTemperature + 273.15))))
					/ Math.exp(Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_cKc"))
							- (ActivationEnergy / (Constants.RealGasConstant / 1000 * (25 + 273.15))));// 406.066;//
																										// Double.valueOf(WIMOVAC.constants.getProperty("Leaf_Kco"));//406.066;
			break;
		case 5:
			TENZ = Math
					.exp(Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_cJmax"))
							- (ActivationEnergy / (Constants.RealGasConstant / 1000 * (LeafTemperature + 273.15))));
			break;
		case 6:
			TENZ = Math
					.exp(Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_cRd"))
							- (ActivationEnergy / (Constants.RealGasConstant / 1000 * (LeafTemperature + 273.15))));
			break;
		case 7:
			TENZ = Math
					.exp(Double.valueOf(WIMOVAC.constants
							.getProperty("Leaf_cGstar"))
							- (ActivationEnergy / (Constants.RealGasConstant / 1000 * (LeafTemperature + 273.15))));
			break;

		}
		return TENZ;
		// TENZ = 1
	} // end of TENZ

	private double SOLC(double LeafTemperature) {

		double SOLC;
		/*
		 * This function returns the solubility of CO2 at temperature t relative
		 * to CO2 solubility at 25C it does this by using a polynomial which has
		 * been fitted to experimental data. Function result units: micro mol
		 * mol^-1 Function found: Plant cell and Environment, (1991) 14,
		 * 729-739. Modification of the response of photosynthetic activity to
		 * rising temperature by atmospheric CO2 concentrations: Has its
		 * importance been underestimated? Note that this polynomial adjustment
		 * should be replaced in later components with the stomatal conductance
		 * calculation of Ci.
		 */

		if (LeafTemperature > 24 && LeafTemperature < 26)
			SOLC = 1;
		else
			SOLC = (1.673998 - 0.0612936 * LeafTemperature + 0.00116875
					* Math.pow(LeafTemperature, 2) - 8.874081E-06 * Math.pow(
					LeafTemperature, 3)) / 0.735465; // This equation is the
														// same used in (Song
														// 2013).

		return SOLC;
	}

	private double SOLO(double LeafTemperature) {
		double SOLO;

		/*
		 * This function returns the solubility of oxygen at a given temperature
		 * t relative to 25C. It does this by applying a polynomial of
		 * solubility fitted to experimental data. Function found: Plant cell
		 * and Environment, (1991) 14, 729-739. Modification of the response of
		 * photosynthetic activity to rising temperature by atmospheric CO2
		 * concentrations: Has its importance been underestimated?
		 */
		if (LeafTemperature > 24 && LeafTemperature < 26)
			SOLO = 1;
		else
			SOLO = (0.047 - 0.0013087 * LeafTemperature + 2.5603E-05
					* Math.pow(LeafTemperature, 2) - 2.1441E-07 * Math.pow(
					LeafTemperature, 3)) / 0.026934;

		return SOLO;

	}

	private double Calculate_Q10_Rate(double StandardReactionRate,
			double Q10_Coefficient, double ReactionTemperature) {

		/*
		 * This is a general function designed to calculate the Q10 response of
		 * reaction rates to temperature. Q10 is the factor by which reaction
		 * velocity is increased for a temperature rise of 10C. The inputs to
		 * the function are: The rate of reaction at 0C The Q10 coefficient The
		 * reaction site temperature Warning Q10 functions are only generally
		 * applicable over a short range of temperatures.
		 */

		// Calculate the rate of reaction given the indicated reaction
		// temperature.
		return StandardReactionRate
				* Math.pow(Q10_Coefficient, (ReactionTemperature - 20) / 10);

	}

	/*
	 * **************************** for growth model ***************************
	 */
	public void updateLA() {
		// This subroutine calculates the additional canopy leaf area which can
		// be developed from the assimilates
		// partitioned or re-allocated to the leaf pool in the last time
		// interval
		double x = Constants.ElapsedThermalDaysSinceGermination;
	    double SLW=-3.068e-5*x*x + 0.06989*x + 21.91; // the SLW fitted with measured data by Xiurong. (2015)
	    
	    if (x>1274){
	    	SLW = 61;
	    }
		
		switch (Constants.LeafGrowthModelType) {
		case 0:
			// Accept the fixed LAI indicated in the canopy parameters section
			// of
			// the model parameters database. Don't change the canopy LAI
			break;
		case 1:
			// Use the assimilates partitioned or reallocated to the leaf pool
			// and the specific leaf area to calculate
			// the area of new leaf that can be grown.
			LA = dry_weight/SLW; 
			//		/ Double.valueOf(WIMOVAC.constants
			//				.getProperty("SpecificLeafArea")); // calculate the
																// Leaf Area
																// with
																// dry_weight

			break;
		case 2:
			LA = dry_weight
					/ Double.valueOf(WIMOVAC.constants
							.getProperty("SpecificLeafArea")); // calculate the
																// Leaf Area
																// with
																// dry_weight
			break;
		}
		// There is a plant specific maximum leaf area index that the plant can
		// obtain set in the model parameter file, use this to reduce the
		// modelled canopy size if necessary. At a later date we may need to
		// convert excess LAI into C for other sinks
		// / if (canopymicroaux.getLeafAreaIndex() > aux.getMaxCanopyLAI())
		// canopymicroaux.setLeafAreaIndex ( aux.getMaxCanopyLAI());
	}

}
