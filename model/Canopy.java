package model;

import gui.WIMOVAC;
public class Canopy extends CanopyProperties{
	
	public double 	canopy_height;  
	public double 	canopy_extinction_coef; 			// canopy extinction coefficient
	
	public SunShadeLeaf 	sunlit_leaf = new SunShadeLeaf();
	public SunShadeLeaf 	shaded_leaf = new SunShadeLeaf();
	
	public double 	scatter_light_temp;
	public int 		extinction_Calc_Switch;

	// ** checked ! ** //
	public void updateCanopyStructure(Stem stem, Leaf leaf){
		/*
		 * update canopy height and LAI
		 */
	            // The canopy height should be calculated from
	            // the total stem length per metre square of ground and the number of plants per metre square
	        canopy_height = stem.L / Constants.PlantingDensity; 
	    
	    // update LAI
	    double ground_area = 1;                           //QF set, 1 m2, because all the other unit is per ground area
	    
	    LAI = leaf.LA / ground_area ;
	}
	
	
	/*
	 * calculate the canopy assimilation rate and canopy transpiration rate
	 */ 
	// ** checked! ** //
	public void CanopyAssimilationDriver(Environment env)  {
		
		if (LAI<0.0001)
			return;


		// photosynthesis part //
		
	    sunlit_leaf.calSunShadeCO2UptakeRate(env);
	    shaded_leaf.calSunShadeCO2UptakeRate(env);
		
		cond 			= (sunlit_leaf.cond * sunlit_leaf.LAI + shaded_leaf.cond * shaded_leaf.LAI)/(sunlit_leaf.LAI + shaded_leaf.LAI);
	    CO2_uptake_rate = sunlit_leaf.CO2_uptake_rate 	+ shaded_leaf.CO2_uptake_rate;
//	    System.out.println("A_sunlit: "+sunlit_leaf.CO2_uptake_rate+ "  A_shaded: "+shaded_leaf.CO2_uptake_rate);


	    // transpiration part // 
	    
	    sunlit_leaf.CalcSunShadeTranspiration(env);
   		shaded_leaf.CalcSunShadeTranspiration(env);
   		
   		// * canopy values = sunlit value + shaded value * //
   		actual_canopy_E 		= sunlit_leaf.actual_canopy_E 		+ shaded_leaf.actual_canopy_E;
   		penman_potential_E 		= sunlit_leaf.penman_potential_E 	+ shaded_leaf.penman_potential_E;
   		priestly_potential_E 	= sunlit_leaf.priestly_potential_E 	+ shaded_leaf.priestly_potential_E;
   		T 						= sunlit_leaf.T 					+ shaded_leaf.T;
   		
    }
	
	/*
	 * calculate the microClimate in canopy
	 */
	
	public void CanopyMicroClimateDriver(Environment env, Leaf leaf) {
        // This is the Canopy microclimate driver main subroutine
        // Each main component should consist of a number of calls to smaller modules of related functions.

        // Don't bother to perform the canopy microclimate calculations if there is no canopy.
        // This may help to speed up the growth model prior to germination
        if (LAI > 0) {
        	
        	sunlit_leaf.Cs = env.air.CO2_concentration;
    		shaded_leaf.Cs = env.air.CO2_concentration;
    		
    		sunlit_leaf.Os = env.air.O2_concentration;
    		shaded_leaf.Os = env.air.O2_concentration;
    		
    		sunlit_leaf.RH = env.air.RH;
    		shaded_leaf.RH = env.air.RH;
    		
    		sunlit_leaf.Tair = env.air.current_T;
    		shaded_leaf.Tair = env.air.current_T;
    		
    		
        	calcSunShadeLight_and_LAI(env, leaf);    // Calculate the Proportion of sunlit foliage in terms of LAI
   		 	
        	
        	sunlit_leaf.calSunShadeVcmaxJmax(leaf);
   		 	shaded_leaf.calSunShadeVcmaxJmax(leaf);
        }
	}
	
	
	
	// ** checked ! ** //
	private void calcSunShadeLight_and_LAI(Environment env, Leaf leaf) {
		
	//	LAI = leaf.LA / 1; // the area of ground is 1 m2, here the LAI of total canopy is re-calcualted from the area of leaf
		
	/*This subroutine takes the total canopy leaf area index and calculates the
	  proportion of the leaves which are sunlit or shaded (Fsun and Fshade respectively).
	  It also takes the total incident radiation at the top of the canopy and calculates
	  the photon flux received by sunlit and shaded leaf populations.
	*/
	    double HoriztoVerticalRatio = Double.valueOf(WIMOVAC.constants.getProperty("HorizVerticalProjAreaRatio"));
	    
	    double Cossolarzenithangle = Math.cos(Maths.DegtoRad(env.solar.ZenithAngle));
	    if (Cossolarzenithangle <= 0)
	        Cossolarzenithangle = 0.001;
	 
	    switch ((int)(double)Double.valueOf(WIMOVAC.constants.getProperty("ExtinctionCalcSwitch")))  {
	          case 0:  
	        	  canopy_extinction_coef = Double.valueOf(WIMOVAC.constants.getProperty("CanopyExtinctionCoef")); 
	              break;
	          case 1:
	              //Use an elipsoidal leaf angle distribution with a horizontal:vertical
	              //projected area ratio from the model parameters database
	        	  canopy_extinction_coef = SphericalLeafExtincCoef(HoriztoVerticalRatio, env.solar.ZenithAngle);    
	              break;
	          case 2: // Qingfeng will add the new canopy extinction coefficent here, because the above two are not exactly. 
	        	  // TO do
	    }
	    
	    // Calculate Fsun, the sunlit leaf area index.
   //         System.out.println("LAI here: "+LAI);
	    double SUNLITLEAVESLAI = (1 - Math.exp(-canopy_extinction_coef * LAI/ Cossolarzenithangle)) 
	                             * Cossolarzenithangle / canopy_extinction_coef;

	//    System.out.println("SUNLITLEAVESLAI: "+SUNLITLEAVESLAI);
	    
	    //What remains of the total canopy leaf area index must be shaded.
	    double SHADEDLEAVESLAI = LAI - SUNLITLEAVESLAI;

	    // Calculate photon flux which is scattered by canopy plant surfaces
	    double ScatteredTerm = (1.1 - 0.1 * LAI) * Math.exp(-Cossolarzenithangle);
	    if (ScatteredTerm < 0)
	        ScatteredTerm = 0; //Prevent problems with large canopies

	    double ScatteredPhotonFlux = 0.07 * env.light.direct_PPFD * ScatteredTerm;

	    //Calculate the photon flux received by the shaded leaves in the canopy
	    double ShadedLeafPhotonFlux = env.light.diffuse_PPFD * Math.exp(-0.5 * Math.pow(LAI,0.7)) + ScatteredPhotonFlux;
	                                  
	    /*Calculate the photon flux received by the sunlit leaves in the canopy
	      After personal communication with Dr Irvin Forseth it appears that the
	      equations given in the UNEP photosynthesis and production in a changing
	      environment book are incorrect, the value I given below should be a multiplier
	      of direct photon flux rather than raising to a power.
	      cos(d)/cos(theta) = extinctioncoef/cossolarzenithangle is a correction
	      for the leaf orientation relative to a horizontal plane.
	    */
	    double SunlitLeafPhotonFlux = env.light.direct_PPFD * canopy_extinction_coef + ShadedLeafPhotonFlux;
	    
	    //Set the output variables
	    
	    // canopy_extinction_coef
	    sunlit_leaf.LAI 	= SUNLITLEAVESLAI;
	    shaded_leaf.LAI 	= SHADEDLEAVESLAI;
	    
	    sunlit_leaf.PPFD 	= SunlitLeafPhotonFlux;
	    shaded_leaf.PPFD 	= ShadedLeafPhotonFlux;
	    scatter_light_temp 	= ScatteredPhotonFlux;  
	    
//	    System.out.println("sunlit light: "+sunlit_leaf.PPFD +" LAI: "+sunlit_leaf.LAI);
//	    System.out.println("shaded light: "+shaded_leaf.PPFD +" LAI: "+shaded_leaf.LAI);
	    
	    
	    }
	
	
	 private double SphericalLeafExtincCoef(double HoriztoVerticalRatio, double SolarZenithAngle) {

	        /*This function calculates a canopy extinction coefficient/foliar absorption coefficient
	          assuming an elipsoidal leaf angle distribution whether the horizontal to vertical ratio
	          of the leaf angles is used.
	        */
	         // Calculate Cos the solar zenith angle
	            double Cossolarzenithangle = Math.cos(Maths.DegtoRad(SolarZenithAngle));

	         // Calculate the extinction coefficient
	            double TopValue = Math.sqrt(HoriztoVerticalRatio*HoriztoVerticalRatio + 
	                              Math.pow(Math.tan(Maths.DegtoRad(SolarZenithAngle)),2)) * Cossolarzenithangle;
	            double BottomValue = HoriztoVerticalRatio + 1.744 * Math.pow((HoriztoVerticalRatio + 1.182),-0.733);  

	            double ExtinctionCoef = TopValue / BottomValue;
	            if (ExtinctionCoef == 0)
	               ExtinctionCoef = 0.001; //Prevent divide by zero error in later calculations

	            return Math.abs(ExtinctionCoef); //Return the function result
	   }
	 
	
}


