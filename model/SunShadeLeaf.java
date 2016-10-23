package model;

import gui.WIMOVAC;


public class SunShadeLeaf extends CanopyProperties {
	
//	double PPFD;          // PPFD: photosynthetic photo flux density on the surface of leaf

	public boolean is_canopy_calculation;
	
	
	public void calSunShadeVcmaxJmax(Leaf leaf){
		// Set the Jmax and Vcmax values for sunlit/shaded leaves
		switch (Constants.UseSunShadeVcmax) {
        case 0:
       	 	Vcmax = leaf.Vcmax;        // Set canopy sunlit leaves Vcmax to standard leaf vcmax
       	 	break;
        case 1:
        								// Use the parameter database settings for Vcmax values
            break;
		}
		switch (Constants.UseSunShadeJmax) {
        case 0:
       	 	Jmax = leaf.Jmax; break;    // Set canopy sunlit leaves Jmax to standard leaf vcmax
        case 1:
        							    // Use the parameter database settings for Jmax values
            break; 
		}
	}
	
	public void calSunShadeCO2UptakeRate(Environment env){

        //Calculate sunlit leaves assimilation rate
   // 	Cs 						= env.air.CO2_concentration;
    	is_canopy_calculation 	= true;

        if (Constants.C3orC4.equals("C3"))
        	cal_c3(env); //
        else if (Constants.C3orC4.equals("C4"))
        	cal_c4_Von(env); //
        
 //       System.out.println("Leaf A: " + A);
        
        CO2_uptake_rate = A  * LAI;
   //     System.out.println("A: "+A+"; LAI:"+LAI+"; SunShA: "+CO2_uptake_rate);
        
        cond 			= Gs * LAI;
// ???
	}

	 public void CalcSunShadeTranspiration(Environment env) {
			/*
		 This subroutine calculates the expected transpiration rates for a canopy made up of
		 sunlit/shaded leaves. It is very limited in as much as because we have no idea
		 of where in the canopy the sunlit and shaded leaves occur it is not possible to
		 apply a relative humidity or wind speed appropriate to that height, nor is it
		 possible to calculate a boundary layer conductance appropriate to the height.
		*/
		// RelativeHumidity, the relative humidity (0-100%)
		    double RelativeHumidity = env.air.RH * 100; // suppose all days are sunny days

		// Wind speed, take /2 that at top canopy as mean wind speed for canopy (m/s)
		    double WindSpeed = env.wind_speed;   

		// Get the average leaf width in the direction of the wind (m)
		    double LeafWidth = Double.valueOf(WIMOVAC.constants.getProperty("LeafWidth"));

		// Atmospheric air temperature
		    double AirTemperature = env.air.current_T;

		// Get the mass/moles flag which indicates the nature of the calling arguements
		    int Units = (int)(double)Double.valueOf(WIMOVAC.constants.getProperty("TranspirationUnitsFlag"));

		// Kappa is von Karmans constant (dimensionless)
		    double kappa = Double.valueOf(Constants.KarmansConstant);
		    //Windspeed is the height (metres) at which the wind speed (Wind speed) m/s is measured
		     double WindSpeedHeight = Double.valueOf(WIMOVAC.constants.getProperty("WindSpeedHeight"));  // (m)

		// SinkMomentum (m) is the apparent sink momentum which is the point where the wind
		// speed profile above the canopy, if extrapolated into the canopy is zero.
		    double SinkMomentum = Double.valueOf(WIMOVAC.constants.getProperty("SinkMomentum"));

		// tau (dimensionless) is the leaf transmission coefficient for solar radiation
		    double tau = Double.valueOf(WIMOVAC.constants.getProperty("Leaf_Reflectance"));
		/*
		 Zeta (m) and Zetam (m) are roughness parameters which indicate
		 effects of canopy surface on turbulent transfer: in general there
		 is more turbulence over a rough surface than over a smooth one.
		 The parameters kappa, WindSpeedHeight,d, zeta and zetam are determined empirically
		 in Campbell (1977).
		*/
		    double ZetaCoef = Double.valueOf(WIMOVAC.constants.getProperty("ZetaRoughnessCoef"));
		    double ZetaMCoef = Double.valueOf(WIMOVAC.constants.getProperty("ZetaMRoughnessCoef")); 

		// Get the canopy height (m)
		    double CanopyHeight = Double.valueOf(WIMOVAC.constants.getProperty("CanopyHeight"));
		     if (CanopyHeight < 0.1)
		         CanopyHeight = 0.1;
		     
		   //K is the canopy extinction coefficient
	//	     double CanopyExtinctionCoef = canopy_extinction_coef;
		    //Fraction of sky covered by clouds, C=0 for no cloud cover, C=1 for full cloud cover
///		     double FracSkycovered = Constants.FracSkyCoveredClouds;
		    //r (dimensionless) is the leaf reflectance coefficient for solar radiation
		     double LeafReflectance = Double.valueOf(WIMOVAC.constants.getProperty("Leaf_Reflectance"));
		    //SpecificHeat, specific heat capacity of air (J/Kg/K)
		     double SpecificHeat = Double.valueOf(Constants.SpecificHeatCapacityAir);

		// Fix wind speed so as not to exagerate boundary layer effects at low
		// wind velocity (m/s)
		    if (WindSpeed < 0.05)  WindSpeed = 0.05;

		// Fix wind speed measurement height so that if the user has entered a measurement
		// height lower than the height of the canopy the measurement height is added to the
		// height of the canopy (otherwise calculation errors result)
		    if (WindSpeedHeight <= CanopyHeight)  WindSpeedHeight = CanopyHeight + 0.1;

		    double ZoCoef = Double.valueOf(WIMOVAC.constants.getProperty("WindSpeedRoughnessLength"));
		    double dCoef = Double.valueOf(WIMOVAC.constants.getProperty("WindSpeedHeightCoeff"));
		    double d = dCoef * CanopyHeight;
		    double Zo = ZoCoef * CanopyHeight;

		// Adjust roughness parameter values accoding to canopy height
///		    double Zeta = ZetaCoef * CanopyHeight;
///		    double ZetaM = ZetaMCoef * CanopyHeight;
		    SinkMomentum = SinkMomentum * CanopyHeight;  // d is the apparent sink momentum
		    double totalradiation;
		    double LeafAreaIndex;
		    double LayerConductance;
		    
		    
		// Handle calculations for sunlit and shaded leaves

		            // Sunlit leaves within canopy
		             totalradiation 	= PPFD; //canopymicroout.getSunlitLeafPhotonFlux();
		             LeafAreaIndex 		= LAI; //canopymicroout.getTotalSunlitLAI();
		             LayerConductance 	= cond;

		        // Convert units of arguements for light and conductance to make
		        // input suitable for calculations
		            if (Units == 0) {
		                // Light and conductance parameters passed have been in mole terms
		                // This is the default setting
		                // µmol/m2/s light and mmol/m2/s conductance are assumed
		                // Convert light assuming 1 µmol PAR photons=0.235J/s Watts
		                totalradiation = totalradiation * 0.235;
		                // Convert mmoles/m2/s to moles/m2/s
		                LayerConductance = LayerConductance * 0.001;
		                // Convert moles/m2/s to mm/s
		                LayerConductance = LayerConductance * 24.39;
		                // Convert mm/s to m/s
		                LayerConductance = LayerConductance * 0.001;
		            }else{
		                // Light and conductance parameters have been passed in mass terms
		                // Watts/m2 light and mm/s conductance are assumed
		                // Convert conductance from mm/s to m/s
		                LayerConductance = LayerConductance * 0.001;
		            }

		            if (LayerConductance <= 0)  LayerConductance = 0.0001;
		                // Prevent a division by zero error which may occur if LayerConductance is 0

		        // Get the temperature sensitive physical parameters
		            double[] GetPhysicalParam=new double[4];
		            double DensityDryAir=0;
		            double LatentHeatVapourisation=0;
		            
		            double SlopeFactor_s=0;
		            double SatWaterVapConc=0;
		            //GetPhysicalParams(AirTemperature, DensityDryAir, LatentHeatVapourisation, SlopeFactor_s, SatWaterVapConc);
		            DensityDryAir=env.air.dry_AirDensity;
		            LatentHeatVapourisation=env.air.latent_HeatVapour;
		            SlopeFactor_s=env.air.slope_FactorS;
		            SatWaterVapConc=env.air.sat_WaterVapConc;

		            // Convert units for the parameter from MJ/Kg to J/Kg
		            LatentHeatVapourisation = LatentHeatVapourisation* 1000000;

		        // Convert units for the parameter from grams/m3/K to kg/m3/K
		            SlopeFactor_s = SlopeFactor_s * 0.001;

		        // Convert units for the parameter from g/m3 to Kg/m3
		            SatWaterVapConc = SatWaterVapConc * 0.001;
		        /*
		         The introduction of deltapva and the slope parameter s is the major contribution
		         by Penman which allows the elimentation of canopy temperature from the
		         equations and replaces it with atmospheric temperature. Hence we use air
		         not leaf temperature.
		        */

		            double DeltaPVa = SatWaterVapConc * (1 - RelativeHumidity / 100);
		        /*
		         Physical parameters used in the Penman/monteith equation
		         LatentHeatVapourisation is the latent heat of vaporisation of water, the energy
		         required to transfer 1Kg of water from the liquid to vapour phase
		         at standard pressure. LatentHeatVapourisation has units of J/Kg, although it is
		         more usually expressed as MJ/Kg. The LatentHeatVapourisation term is used to calculate
		         the psychrometric parameter PsycParam which has units Kg/m3/K.
		         DensityDryAir is the density of dry air (Kg/m3), the specific heat capacity
		         of air SpecificHeat (J/Kg/K)
		        */

		        // Calculate the psychrometic parameter
		            double PsycParam = (DensityDryAir * SpecificHeat) / LatentHeatVapourisation;
		        /*
		         Boundary conductance parameters, ga
		         Prevent error in calculation of ga when Windspeed is 0 by setting Windspeed to
		         a low but non zero value. Values less than 0.5m/s cause problems when calculating
		         the temperature difference between canopy leaf temperature and the atmospheric
		         temperature.
		        */

		         // First calc ustar for canopy with known wind speed at wind height
		            double UStar = (WindSpeed * kappa) / Math.log10((WindSpeedHeight - d) / Zo);
		            double ga = (UStar*UStar) / (WindSpeed * 0.4);

		        // Calculate the boundary layer thickness from the size of the leaf
		        // in the direction of the wind and the wind speed. Equation from Reynolds
		            double BoundaryLayerThickness = 0.004 * Math.sqrt(LeafWidth / (WindSpeed * 0.4));
		        /*
		         Calculate the diffusion coefficient of water vapour (m2/s) where airtemperature is in C
		         We should replace air temperature below with the expected temperature of the air in the
		         layer after solar heating etc. This process would need to be iterative and is too
		         complicated for intial use.
		        */

		            double DiffCoef = 2.126 * Math.pow(10,-5) + 1.48 * Math.pow(10,-7) * AirTemperature;
		            double LeafboundaryLayer = DiffCoef / BoundaryLayerThickness;  // It is Lb

		        // Add the canopy/leaf layer boundary conductance value to the leaf boundary layer
		        // conductance to give the total conductance to H20
		            ga = (ga * LeafboundaryLayer) / (ga + LeafboundaryLayer);

		            double Ja = 2*totalradiation*((1 - LeafReflectance - tau) / (1 - tau)) * LeafAreaIndex; 
		            //?????not the same wth the previous function???

		            double Deltat = 0.01;
		            double ChangeInLeafTemp = 10;
		            double PhiN=0;

		        int Counter = 0;
		        
		        
		        
		        while (ChangeInLeafTemp > 0.1 && Counter < 5) {

		            double OldDeltaT = Deltat;
		        /*
		         Calculate total radiation absorbed by leaves in layer being considered
		         Full Stefans law equation is given below
		         rlc = (0.9 * (5.67 * 10 ^ -8) * (273.16 + Airtemperature + Deltat) ^ 4) * LeafAreaIndex
		         Calculate radiation emission based upon Temperature difference between leaf/air
		         See Monteith and Unsworth
		        */
		            double StefanBoltzmanConst=Double.valueOf(Constants.StefanBoltzmanConst);
		            double rlc = 4 * StefanBoltzmanConst * Math.pow((273 + AirTemperature),3) * Deltat * LeafAreaIndex; 
		            // ??????no LeafAreaIndex in original thesis
		            //???????where StefanBoltzmanConst come from  =5.67*Math.pow(10,-8)

		        // The net radiation balance of the canopy phin is given by
		            PhiN = Ja - rlc;

		        // Calculate the temperature differece between the canopy and the atmosphere
		            double TopValue = PhiN * (1 / ga + 1 / LayerConductance) - LatentHeatVapourisation * DeltaPVa;
		            double BottomValue = LatentHeatVapourisation * (SlopeFactor_s + PsycParam * (1 + ga / LayerConductance));
		            Deltat = TopValue / BottomValue;
		            
		            ChangeInLeafTemp = Math.abs(OldDeltaT - Deltat);
		            Counter++;
		            
		        }
		        // It is possible to get a negative radiation balance when the leaf and air temperatures are at certain values, this would
		        // cause negative transpiration rates which are physically impossible and so we prevent them here by checking for a
		        // net negative leaf radiation balance.
		            if (PhiN < 0) PhiN = 0;

		        // Actual canopy transpiration is calculated by,
		            double TranspirationRate=(SlopeFactor_s*PhiN+LatentHeatVapourisation*PsycParam*ga*DeltaPVa)/
		                                     (LatentHeatVapourisation*(SlopeFactor_s + PsycParam * (1 + ga / LayerConductance)));

		        // Calculate the Priestly-Taylor potential transpiration rate, this is likely to
		        // be inaccurate at low irradiances.
		            double EPriestlyPotential=1.26 * (SlopeFactor_s * PhiN) / (LatentHeatVapourisation * (SlopeFactor_s + PsycParam));

		        // Calculate the Penman-Monteith potential transpiration rate
		            double EPenmanPotential=(SlopeFactor_s*PhiN+LatentHeatVapourisation*PsycParam*ga*DeltaPVa)/
		                                     (LatentHeatVapourisation*(SlopeFactor_s+PsycParam));

		        // TranspirationRate is calculated internally within this routine in mass units
		        // the mass units for transpiration are Kg/m2/s. g/m2/s is reported
		        // by the routine however as this is more commonly used

		            if (Units == 0) {
		                // This module was given a molar input for light and conductance
		                // and so we should calculate a molar output from the internal
		                // mass transpiration calculation.
		                // Convert Kg H2O/m2/s to g H2O/m2/s
		                TranspirationRate = TranspirationRate * 1000;
		                EPriestlyPotential = EPriestlyPotential * 1000;
		                EPenmanPotential = EPenmanPotential * 1000;
		                // Convert from g H2O/m2/s to moles H20/m2/s, molecular weight water = 18
		                TranspirationRate = TranspirationRate / 18;
		                EPriestlyPotential = EPriestlyPotential / 18;
		                EPenmanPotential = EPenmanPotential / 18;
		                // Convert from moles H2O to mmol H2O
		                TranspirationRate = TranspirationRate * 1000;
		                EPriestlyPotential = EPriestlyPotential * 1000;
		                EPenmanPotential = EPenmanPotential * 1000;
		            }else{
		                // Simply report the canopy transpiration rate in mass units
		                // Convert Kg H2O/m2/s to g H2O/m2/s
		                TranspirationRate = TranspirationRate * 1000;
		                EPriestlyPotential = EPriestlyPotential * 1000;
		                EPenmanPotential = EPenmanPotential * 1000;
		            }

		                if (LeafAreaIndex < 0.0001) {
		                	actual_canopy_E = 0;
		                	penman_potential_E = 0;
		                	priestly_potential_E = 0;
		                    T = AirTemperature + Deltat;

		                }else{
		                	actual_canopy_E = TranspirationRate;
		                	penman_potential_E = EPenmanPotential;
		                	priestly_potential_E = EPriestlyPotential;
		                    T = AirTemperature + Deltat;
		                }

		}
	
	
}
