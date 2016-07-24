package model;

public class Soil {
	public double radiation_on_soil;
	public int    multi_layer_flag;
    public double WtAverageSoilWaterPotential;
    public double [] LayerSoilWaterPotential;
    public double light_on_soil;
    
    
    public Soil(){
    	
    }
//    
//    public Soil (Plant plant, CurrentTime ct, Location lct){
//    	
//    	CalcLightOnSoil(plant);      // Set the total expected light on the soil surface (umol/m2/s)
//    	
//    	
//    }
//
//public void SoilMicroclimateDriver() {
//
//// This driver calls subroutines to calculate soil water and energy balance.
//// Please note that the macroclimate driver should be called before this driver because precipitated water is
//// required here to calculate the new soil volumetric water content.
//
//	CalcLightOnSoil(plant);      // Set the total expected light on the soil surface (umol/m2/s)
//    
//    CalcAddPrecipitationtoSoil();   // Add precipitation water (rainfall mm/day) to the soil
//    
//    CalcWaterRunOff();        // Calculate run off from soil
//
//    CalcSoilEvaporation();    // Calculates direct evaporation from soil surface based upon incident radiation
//
//    CalcWaterBalance();       // Calculate water balance for selected soil using simple water model
//       
//    CalcSoilTemperature();    // Calculate soil temperature - multilayre / single layer
//    
//    CalculateSoilHeatFlux();  // Calculate soil heat flux/temperature from energy balance
//    
//    CalcSoilWaterPotential(); // Calculate soil and leaf water potential
//        
//}
//
//
//public void CalcLightOnSoil(Plant plant) {
//
//	// This subroutine sets the expected light incident upon the soil surface (umol/m2/s)
//	// The assumption used here is that the light reaching the soil surface is similar to
//	// that received by the shaded leaves in the bottom layer of which ever canopy model
//	// is being used.
//
//	// Use the canopy type flag to determine which light intensity to use
//	    
//	    double Irad = 0;
//	    switch (0) {
//	        case 0: // Single layer canopy model
//	             // Set irradiance at soil equal to irradiance at shaded leaves
//	             Irad = plant.canopy.shaded_leaf.PPFD; // umol m-2 s-1
//	        case 1: // Multiple layer canopy model
//	            // Get the number of layers in the multiple layer canopy
//	            // Set irradiance at soil equal to irradiance at shaded leaves in bottom layer of canopy
//	            //Irad = MultiLayerCanopyMicroclimateOut.LayerTotalLight(MultiLayerCanopyMicroclimateAux.numlayers);
//	        case 2: // Hedgerow canopy model
//	            // Calculate the total irradiance at the sunlit and shaded leaves in the hedgerow canopy model
//	            // and the fraction of shadow and and set the irradiance reaching the soil surface equal to this
//	        //    double irad1 = canopyout.getShadedLeafPhotonFlux() * canopyout.getFractionShadow(); //umol m-2 s-1
//	        //    double irad2 = (macroout.getDirectIncidentPhotonFlux() + macroout.getDiffuseIncidentPhotonFlux()) * (1 - canopyout.getFractionShadow());
//	      //      Irad = irad1 + irad2;
//	        case 3: // Bare soil
//	        //    Irad = macroout.getDirectIncidentPhotonFlux() + macroout.getDiffuseIncidentPhotonFlux(); // umol m-2 s-1
//	        default: // Catch all for non specified cabopy model types
//	        //    Irad = canopyout.getShadedLeafPhotonFlux(); //umol m-2 s-1
//	    }
//	    light_on_soil = Irad;
//	 //   soilaux.setRadiationIncidentOnSoil( Irad);
//
//	}
//
//
//public void CalculateSoilHeatFlux() {
//    
//    
///*
//*********************************************************************************************************************
//*                                                                                                                   *
//*           The Module calculates heat and water contents of a multilayer one dimensional soil                     *
//*                      profile with variable inputs at the upper boundary through time                              *
//*                                                                                                                   *
//* The upper boundary condition is first solved (Soil Surface Temperature, rainfall infiltration and/or Evaporation) *
//* from basic weather data.                                                                                          *                                                                                         *
//* Using known physical properties for the soil in question and values for current water content a number of further *
//* soil properties are derived for each node in the soil.                                                            *
//* Heat and water flow are then solved by an implicit finite difference method solving a tridiagonal matrix          *
//* using a Thomas algorithm.                                                                                         *
//*                                                                                                                   *
//*********************************************************************************************************************
//* 
//*/
//
//// This subroutine calcultes the soil heat fluxes and soil temperature.
//// Initially get the weather data for the current time step by converting macroclimate and canopy microclimate parameters to appropriate units.
//
//	int SoilTemperatureCalcFlag = 0;
//	
//    if (SoilTemperatureCalcFlag == 0) {
//        // The calculation of soil heat flux has not been selected in the model parameter
//        // file, therefore retain the initial soil temperature settings of the SetSoilMicroclimateVariables
//        // routine and exit this subroutine directly.
//        return;
//    }
//        
//    int Numsoillayers = 0;
//    
//    
//    Numsoillayers = Numsoillayers + 1;
//    
//    // Use the number of soil layers to resize working arrays
//    double psi[][];  // Pressure head in metres (m) of water
//    psi=new double[Numsoillayers][2];
//    double tsl[][]; // Temperature of soil layers
//    tsl=new double[Numsoillayers][2];
//    double temp[];
//    temp=new double[Numsoillayers];
//    double capa[];  // Volumetric heat flux density
//    capa=new double[Numsoillayers];
//    double alpa[];  // coefficient alpha in the Van Genuchten's retention
//    alpa=new double[Numsoillayers];
//    double ramda[]; // Soil thermal conductivity
//    ramda=new double[Numsoillayers];
//    double cond[];  // Hydraulic conductivity
//    cond=new double[Numsoillayers];
//    double spwc[];
//    spwc=new double[Numsoillayers];
//    double coef[][];// This is the tridiagonal coefficient matrix that will eventually be solved
//    coef=new double[Numsoillayers][3];
//
//    double NodeDepth = soilaux.getSoilLayerDepth(); //(m)
//    double mxpool = soilaux.getMaxsurfacepool();    //(m)
//    
//    double aalpa = soilaux.getVanGenuchtensCoefAALPA();         // Get the Van Genuchtens Aalpa coefficient
//    double en = soilaux.getVanGenuchtensCoefEN();                //Get the Van Genuchtens EN coefficient
//    double conds = soilaux.getSaturatedhydraulicConductivity(); //Get the soil saturated hyraulic conductivity
//    double Thetas = soilaux.getFieldCapacity();                 // Get the soil field capacity
//    double Thetar = soilaux.getResidualWaterContentCoefThetar(); //Get the residual water content coefficient
//    double sfwater = soilaux.getSurfacewater();                 // Get the surface water pool
//    
//    for (int SoilLayer = 1; SoilLayer<=Numsoillayers; SoilLayer++) {
//        psi[SoilLayer][2] = soilaux.getLayerPressureHead(SoilLayer);
//        tsl[SoilLayer][2] = soilaux.getLayerSoiltemperature(SoilLayer);
//    }
//            
//    int Soiltype = soilaux.getSoiltype();                        // Obtain standard soil type
//    double InitialPressureHead = soilaux.getInitialPressureHead();  // Obtain initial pressure head
//    double rough = soilaux.getSoilSurfaceRoughnessLength();         //Obtain soil roughness length
//    
//    double tol = 0.01; // Set the tolerance for iterations within the energy balance subroutine
//    
//    double Tstep = ct.getIntervalHour() * 3600 * ct.getIntervalDay(); // Number of seconds in integration step
//    double wct=0;
//    
//    if (ct.getIntervalDay() == 0)
//        Tstep = ct.getIntervalHour() * 3600;
//    
//    double Irain;
//       
//    if (ct.getDailyCalcFlag() == 0)
//        //Once a day add any rainfall to the soil temperature model
//        Irain = macroout.getDailyPrecipitation() / 1000; //rainfall in metres per day
//    else
//        Irain = 0;
//    
//    
//    double Irad = soilaux.getRadiationIncidentOnSoil() * 2 * 0.235; // See CalcLightOnSoil subroutine for details of how this is obtained
//    
//    switch (canopyaux.getCanopyMicroclimateTypeFlag()) {
//        case 0: // Single layer canopy model
//             // Set initial guess soil temperature to shaded leaf temperature
//             ctemp = macroout.getInstantAirTemperature();
//             // Set irradiance at soil equal to irradiance at shaded leaves
//             // Set wind speed to that above the canopy
//             double WSpeed = macroout.getWindSpeedAboveCanopy();     // metres s-1
//             // Set the relative humidity at the soil surface
//             double rh = macroout.getRelativeHumidity() * 100;       // Convert relative humidity to %
//       /* case 1: // Multiple layer canopy model
//            // Get the number of layers in the multiple layer canopy
//            double NumCanopyLayers = MultiLayerCanopyMicroclimateAux.numlayers;
//            // Set the initial guess for soil temperature equal to the temperature of the shaded leaves in the bottom layer of the canopy
//            ctemp = macroout.getInstantAirTemperature();
//            // If the multiple layer relative humidity calculations have been performed then
//            // use the relative humidity for the leaves at the bottom of the canopy
//            if (MultiLayerCanopyMicroclimateAux.UseLayeredRelativeHumidityFlag ==true) 
//                rh = MultiLayerCanopyMicroclimateOut.LayerRelativeHumidity(NumCanopyLayers%) * 100; // Convert relative humidity to %
//            else
//                rh = MacroclimateOut.RelativeHumidity * 100; // Convert relative humidity to %
//            
//            // Set wind speed to that at the bottom of the canopy
//            WSpeed = MultiLayerCanopyMicroclimateOut.LayerWindSpeed(NumCanopyLayers%)
//             * 
//             */
//        case 2: // Hedgerow canopy model
//            // Set the initial guess for soil temperature equal to the ambient air temperature
//            // when the currently selected canopy is a hedgerow
//            ctemp = macroout.getInstantAirTemperature();
//            rh = macroout.getRelativeHumidity() * 100; // Convert relative humidity to %
//            WSpeed = macroout.getWindSpeedAboveCanopy();
//        case 3: // Bare soil
//            // Set the initial guess for soil temperature equal to ambient air temperature
//            // when the bare soil canopy model is specified
//            ctemp = macroout.getInstantAirTemperature(); //∞C
//            rh = macroout.getRelativeHumidity() * 100; //%
//            WSpeed = macroout.getWindSpeedAboveCanopy();
//        default: // Catch all for non specified cabopy model types
//            ctemp = macroout.getInstantAirTemperature();
//            rh = macroout.getRelativeHumidity() * 100; // Convert relative humidity to %
//            WSpeed = macroout.getWindSpeedAboveCanopy();
//    }
//                                
// // Given the initial status of the soil calculate the soil properties
//     for (int SoilLayer = 1; SoilLayer<=Numsoillayers;SoilLayer++) {
//        double  tmp=0;
//        if (psi[SoilLayer][2] >= 0)
//           psi[SoilLayer][2] = -0.000001;
//        double Psh = psi[SoilLayer][2];
//        
//        if (Psh > -0.01) {
//            spwc[SoilLayer] = 0.01;
//            cond[SoilLayer] = soilaux.getSaturatedhydraulicConductivity();
//        }
//        else {
//            // Calculate the soil hydraulic conductivity, equation 24 in the paper
//            // The equation characteristically has a value of 10-2 to 10-3 cm/s in Sandy Soil; 10-6 to 10-7 in clay
//            double ap = aalpa * Math.abs(Psh);
//            double bline =Math.pow( (1 + Math.pow(ap, en)), ((en - 1) / (2 * en)));
//            double tline = 1 + Math.pow(ap, ((1 - en) / en));
//            cond[SoilLayer] = ((Math.pow((1 - (Math.pow(ap, (en - 1)) * tline)),2)) / bline) * conds;
//                        
//            // Calculate the soil specific water capacity. Equation 25 in the paper
//            // Alternative formulation for the soil specific water capacity. Soil_Specific_water_capacity = (en - 1) * (wct - thetar) * (1 - ((thet - thetar) / (thetas - thetar)) ^ (en / (en - 1))) / pre
//            // Care needs to be taken here since exponential can only work
//            // on negative values if the exponential part is an integer
//                     
//            wct = Soilpresheadtowatercont(Math.abs(Psh)); // need to calculate wct (water content) from pressure head
//            tmp = Math.pow((wct - Thetar) / (Thetas - Thetar), en / (en - 1));
//            
//            spwc[SoilLayer] = (en - 1) * (wct - Thetar) * (1 - tmp) / (Math.abs(Psh));
//            
//        }
//    
//        // Calculate the volumetric soil heat capacity
//        // In the program the calculation is given as below
//        // But in the text is given as: Soil_volumetric_heat_capacity = ((1 - thetas) * 1920000) + (4180000 * wct)
//        // Following DeVries (1963)
//        // In fact solid is (1-thetas) and organ is always zero in the examples given, so the expressions are equivalent.
//        // The basis for this is not explained in the paper.
//                                
//        // Equation 25 in the paper. The slope of the relationship between soil water content and soil water potential at a given value
//        // Alternative formulation for the soil specific water capacity. Soil_Specific_water_capacity = (en - 1) * (wct - thetar) * (1 - ((thet - thetar) / (thetas - thetar)) ^ (en / (en - 1))) / pre
//        double Soil_specific_water_capacity = (en - 1) * (wct - Thetar) * (1 - tmp) / Psh;
//            
//        // Calculate the soil volumetric heat capacity
//        capa[SoilLayer] = 1920000 * soilaux.getSoilSolidFraction() + 2510000 * soilaux.getSoilOrganFraction() + 4180000 * wct; // J/degC/m3 [EQN 22]
//        
//        // Calculate the soil thermal conductivity
//        double b1 = soilaux.getSoilThermalConductivityCoefb1();
//        double b2 = soilaux.getSoilThermalConductivityCoefb2();
//        double b3 = soilaux.getSoilThermalConductivityCoefb3();
//        
//        wct = Soilpresheadtowatercont(Psh); // need to calculate wct (water content) from pressure head
//        
//        ramda[SoilLayer] = b1 + b2 * wct + (b3 * Math.pow(wct, 0.5)); // W/m/deg C [EQN 21]
//        
//        alpa[SoilLayer] = ramda[SoilLayer] / capa[SoilLayer];
//        
//   }
//                                 
//// Handle heat flux boundary conditions at top and bottom soil layers
//
//// Use weather data to calculate upper boundary conditions for Soil Temperature.
//// Also returns the Waterflux (Evaporation=negative and Infiltration=positive).
//// Top Boundary Condition
//
//    double dzdt = NodeDepth * NodeDepth / Tstep;
//    double surface_water=0, rinfilt,waterflux;
//    
//   if (Irain > 0 || surface_water > 0)  {   // This is the orginal code
//        // Returns soil waterflux (infiltration per second)
//        double psi1 = 0;
//        if (sfwater > 0) psi1 = sfwater;
//        double ndep = 0.05; // Equivalent to delz
//        double cap = ((psi1 - psi[2][2]) / ndep + 1) * 0.5 * (cond[2] + conds) * Tstep; // Gradient * Conductivity * Time = Potential Infiltration Volume
//        
//        // Is the amount of rain and surface water less than the maximum capacity?
//        if ((Irain + sfwater) < cap ) {
//            rinfilt = Irain + sfwater; //Yes = Add it all
//            sfwater = 0;
//            //SoilMicroclimateOut.SoilWaterRunOff = 0
//        }
//        else{
//            rinfilt = cap; // No = Infiltration is maximum, what is left is input-infiltration-surface water-runoff
//            sfwater = sfwater + Irain - rinfilt;
//        
//            if (sfwater > mxpool)
//                //SoilMicroclimateOut.SoilWaterRunOff = sfwater - mxpool
//                sfwater = mxpool;
//            
//        }
//        
//        soilaux.setSurfacewater( sfwater);
//        
//        if (rinfilt > 0)
//            waterflux = rinfilt / Tstep; // Rate of infiltration per second
//        else
//            waterflux = 0;
//        
//                
//        temp[1] = ctemp; //Set Soil temperature to default temperature determined above
//    } 
//   else {
//        //Routine to calculate energy balance, returns layered temperatures and waterfluxes
//        wct = Soilpresheadtowatercont(psi[1][2]); // Water content of old soil layer 1
//        
//        double capaa = 1154.8 + 303.16 / (ctemp + 273.16);              // Volumetric Heat Capacity of the air
//        double capas = 0.5 * (capa[1] + capa[2]);                      // Volumetric Heat Capacity of soil layer 1
//        double thrmcond = 0.5 * (alpa[1] * capa[1] + alpa[2] * capa[2]); // Thermal Conductivity of soil layer 1
//        double emis = 0.9 + 0.18 * wct;                                  // Soil Surface Emissivity  [EQN 19]
//        double albed;
//        
//        if (wct > 0.25) albed = 0.1;
//        if (wct < 0.1)  albed = 0.25; // Albedo of the soil surface [EQN 20]
//        else
//           albed = 0.35 - wct;
//        
//        
//        double a = tsl[1][2];
//        double b = tsl[1][2];
//        double Res=0, fa=0, fb, amid=0, evap=0,WSpeed=0,rh=0;
//        for (int X = 1;X<=20;X++) {
//            a = a - 0.5;
//            b = b + 0.5;
//            bisec(a, capas, capaa, thrmcond, albed, emis, Res, evap, WSpeed, psi, tsl, Tstep, rough, NodeDepth, ctemp, rh, Irad);
//            fa = Res;
//            bisec(b, capas, capaa, thrmcond, albed, emis, Res, evap, WSpeed, psi, tsl, Tstep, rough, NodeDepth, ctemp, rh, Irad);
//            fb = Res;
//            if (fa * fb < 0) 
//                break;
//        }
//                     
//        for (int X = 1;X<=20;X++) {
//            amid = (a + b) / 2;
//            if ((amid - a) < tol) break;
//            
//            bisec(amid, capas, capaa, thrmcond, albed, emis, Res, evap, WSpeed, psi, tsl, Tstep, rough, NodeDepth, ctemp, rh, Irad);
//            
//            double fmid = Res;
//            if ((fa * fmid) > 0)
//                a = amid;
//            else
//                b = amid;
//            
//       }
//                
//        temp[1] = amid;
//        waterflux = -evap; //Rate of evaporation per second (See Infiltration, where this is positive)
//         
//     }
//              
//// Handle heat flux of middle nodes
//    for (int LAYER = 2; LAYER<= (Numsoillayers - 1);LAYER++)
//        temp[LAYER]= tsl[LAYER][2] * dzdt;
//    
//    
//// Bottom Boundary Condition, bottom soil layer interface with deeper soil layers
//    double hflux = -ramda[Numsoillayers] * (tsl[Numsoillayers][2] - tsl[Numsoillayers - 1][2]) / NodeDepth;
//    temp[Numsoillayers] = (dzdt * tsl[Numsoillayers][2]) + (0.5 * (alpa[Numsoillayers] + alpa[Numsoillayers - 1]) * hflux * NodeDepth * 2 / ramda[Numsoillayers]);
//
//// Set the heat tridiagonal matrix for later calculations of flux
//// Tridiagonal Matrices are composed entirely of zeros except for three diagonal lines of numbers from one corner to the one diagonally opposite.
//    dzdt = NodeDepth * NodeDepth / Tstep;
//    coef[1][1] = 0;
//    coef[1][2] = 1;
//    coef[1][3] = 0;
//    
//    for (int LAYER = 2; LAYER<=(Numsoillayers - 1);LAYER++) {
//        coef[LAYER][1] = -0.5 * (alpa[LAYER - 1] + alpa[LAYER]);
//        coef[LAYER][3] = -0.5 * (alpa[LAYER] + alpa[LAYER + 1]);
//        coef[LAYER][2] = dzdt - coef[LAYER][1] - coef[LAYER][3];
//   }
//    
//    coef[Numsoillayers][1] = -2 * 0.5 * (alpa[Numsoillayers - 1] + alpa[Numsoillayers]);
//    coef[Numsoillayers][2] = dzdt - coef[Numsoillayers][1];
//    coef[Numsoillayers][3] = 0;
//
//// Solve the heat tridiagonal matrix. This subroutine solves tridiagonal matrices without pivoting.
//// Diagonally dominant matrices are safe. ff is multiplier for foreward elimination step
//    for (int LAYER = 2;LAYER<= Numsoillayers;LAYER++) {
//        double ff = coef[LAYER][1] / coef[LAYER - 1][2];
//        coef[LAYER][2] = coef[LAYER][2] - ff * coef[LAYER - 1][3];
//        coef[LAYER][1] = ff;
//    }
//    
//// Foreward elimination step
//// Temp  :On input, vector of right hand sides
////      :On exit, Solution vector
//    for (int LAYER = 2;LAYER<=Numsoillayers;LAYER++) 
//        temp[LAYER] = temp[LAYER] - coef[LAYER][1] * temp[LAYER - 1];
//    
//    
//// Back Substitution Loop
//    temp[Numsoillayers] = temp[Numsoillayers] / coef[Numsoillayers][2];
//    for (int LAYER = 2;LAYER<= Numsoillayers;LAYER++) {
//        int jb = Numsoillayers - LAYER + 1;
//        temp[jb] = (temp[jb] - coef[jb][3] * temp[jb + 1]) / coef[jb][2];
//    }
//    
//// Assign values from temp() to tsl(); Clear temp() and coef()
//    for (int LAYER = 1; LAYER<=Numsoillayers;LAYER++) {
//        tsl[LAYER][1] = temp[LAYER];
//        temp[LAYER] = 0;
//        coef[LAYER][1] = 0; 
//        coef[LAYER][2] = 0;
//        coef[LAYER][3] = 0;
//    }
//   
//// Set water tridiagonal matrix. This subroutine solves tridiagonal matrices without pivoting.
//    dzdt = NodeDepth * NodeDepth / Tstep;
//    double terms = spwc[1] * NodeDepth / (2 * Tstep);
//    double cbar = 0.5 * (cond[1] + cond[2]); // cm/s Hydraulic Conductivity - Dependence of Flux on Hydraulic Gradient
//    double psio = ((waterflux - cbar + cbar * psi[2][2] / NodeDepth) + psi[1][2] * terms) / (terms + cbar / NodeDepth);
//    
//// Pressure head at imaginary points above soil surface
//    coef[1][1] = 0;
//    coef[1][2] = 1;
//    coef[1][3] = 0;
//    temp[1] = -ctemp; // psio
//    
//    for (int LAYER = 2; LAYER<=Numsoillayers - 1;LAYER++) {
//        coef[LAYER][1] = -0.5 * (cond[LAYER - 1] + cond[LAYER]);
//        coef[LAYER][3] = -0.5 * (cond[LAYER] + cond[LAYER + 1]);
//        coef[LAYER][2] = spwc[LAYER] * dzdt - coef[LAYER][1] - coef[LAYER][3];
//        temp[LAYER] = spwc[LAYER] * dzdt * psi[LAYER][2] - NodeDepth / 2 * (cond[LAYER+ 1] - cond[LAYER - 1]);
//    }
//    
//    double fluxn = Math.sqrt(cond[Numsoillayers - 1] * cond[Numsoillayers]) * (1 - (psi[Numsoillayers][2] - psi[Numsoillayers - 1][2]/ NodeDepth));
//      
//// Drainage out of the system at the bottom boundary. Not used at present
//    coef[Numsoillayers][1] = -1;
//    coef[Numsoillayers][2] = 1;
//    coef[Numsoillayers][3] = 0;
//    temp[Numsoillayers] = 0;
//   
//// Solve water tridiagonal matrix.
////This subroutine solves tridiagonal matrices without pivoting. Diagonally dominant matrices are safe
//     
////ff is multiplier for foreward elimination step
//    for (int LAYER = 2;LAYER<= Numsoillayers;LAYER++) {
//        double ff = coef[LAYER][1] / coef[LAYER- 1][2];
//        coef[LAYER][2] = coef[LAYER][2] - ff * coef[LAYER - 1][3];
//        coef[LAYER][1] = ff;
//    }
//    
//// Foreward elimination step
//// Temp :On input, vector of right hand sides
////      :On exit, Solution vector
//    for (int LAYER = 2;LAYER<= Numsoillayers;LAYER++)
//        temp[LAYER] = temp[LAYER] - coef[LAYER][1] * temp[LAYER - 1];
//    
//    
////Back Substitution Loop
//    temp[Numsoillayers] = temp[Numsoillayers] / coef[Numsoillayers][2];
//    
//    for (int LAYER = 2;LAYER<= Numsoillayers;LAYER++) {
//        int jb = Numsoillayers - LAYER + 1;
//        temp[jb] = (temp[jb] - coef[jb][3] * temp[jb + 1]) / coef[jb][2];
//    }
//    
////Assign values from temp() to psi(); Clear temp() and coef()
//    for (int LAYER = 1; LAYER<=Numsoillayers;LAYER++) {
//        psi[LAYER][1] = temp[LAYER];
//        temp[LAYER] = 0;
//        coef[LAYER][1] = 0;
//        coef[LAYER][2] = 0;
//        coef[LAYER][3] = 0;
//    }
//    
////Export to Global and Move new values to old
//    for (int LAYER = 1;LAYER<= Numsoillayers;LAYER++) {
//        soilaux.setLayerPressureHead(LAYER, psi[LAYER][1]);
//        soilaux.setLayerSoiltemperature(LAYER, tsl[LAYER][1]);
//   }
//
//}
//               
//        
//private double CalcSaturatedHumidity(double tp) {
//
//// From Goff-Gratch formulation (1946) Trans. Amer. Soc. and Vent. Eng. 52,95
//
//    double dk = tp + 272.16; //Convert to deg K
//    double ews = 1013.246; //  S.V.P of pure water at steam point(mbar)
//    
//    double a = -7.90298 * (372.16 / dk - 1); // where 372.16 is the steam point of water (deg K)
//    double b = 5.02808 * (Math.log(372.16 / dk) / 2.302585);
//    double c = -1.3816 * 0.0000001 * (Math.pow(10, (11.344 * (1 - dk / 372.16))) - 1);
//    double d = 8.1328 * 0.001 * Math.pow(10, (-3.49149 * (372.16 / dk - 1))) + Math.log(ews) / 2.3025851;
//    
//    double svp = Math.pow(10, (a + b + c + d)); //   Saturated vapour pressure at this temperature (mbar)
//    //vp = (relativehumid * svp) / 100 ' Vapour pressure at this temperature (mbar)
//    //vp = vp * 100 '                    Convert to Pa (10mbar=1kPa=1000Pa)
//    
//    double vp = svp * 100;     //                 Convert to Pa (10mbar=1kPa=1000Pa)
//    double CalcSaturatedHumidity = (2.17 / dk) * vp; //     Absolute saturated humidity (g/m3)
//    
//    return CalcSaturatedHumidity;
//
//}
//
//
//private double Soilpresheadtowatercont(double PressureHead) {
//    
//// Pressure Head to water content
////This is an empirical equation, from Van Genucheten Soil Sci Am J 44:892 (1980)
////Pressure head in metres, water content I assume on a volumetric basis
//    double Soilpresheadtowatercont;
//    if (PressureHead > 0)
//    
//        Soilpresheadtowatercont = soilaux.getFieldCapacity();
//        
//    else {
//    
//        double aalpa = soilaux.getVanGenuchtensCoefAALPA();
//        double en = soilaux.getVanGenuchtensCoefEN();
//        double Thetas = soilaux.getFieldCapacity();
//        double Thetar = soilaux.getResidualWaterContentCoefThetar();
//        double Psh = Math.abs(PressureHead);
//        double t = 1 - (1 / en);
//        Soilpresheadtowatercont = Math.pow(1 / (Math.pow(aalpa * Psh, en) + 1),t)* (Thetas - Thetar) + Thetar;
//        
//    }
//        
//    return Soilpresheadtowatercont;
//
//}
//
//
//
//
//public void bisec(double Ts, double capas, double capaa, double thrmcond, double albed,
//                  double emis, double Res, double evap, double WSpeed, double psi[][],
//                  double tsl[][], double Tstep, double rough, double NodeDepth, double ctemp, double rh, double Irad) {
//
//// Bisector root finding algorithm (James (1977))
//
//// Irad is the sum of 'Direct' and 'Diffuse' radiation reaching the soil surface on a per m2 basis
////This is really the core of the soil heat flux temperature model. Note that light intensity
//
//
//
//    double psi1 = psi[1][2];
//    
//    if (WSpeed <= 0) WSpeed = 0.1;
//    
//    double ra = Math.log(2 / rough)* Math.log(2 / rough)/ (0.16 * WSpeed); // Aerodynamic Boundary layer resistance [EQN 10]
//    
//    double sha = CalcSaturatedHumidity(ctemp) / 1000;   //Saturated Humidity of the air (kg/m3)
//      
//    double ha = (rh * sha) / 100; //Absolute humidity of the air
//    
//    double sky = StefanBoltzmanConst * Math.pow((ctemp + 273.16), 4) * (0.605 + 0.048 * Math.sqrt(1370 * ha)); // Longwave Sky Irradiance W/m2 (incoming) [EQN 5- From Brunt's formula]
//    
//    double shs = CalcSaturatedHumidity(Ts) / 1000; // Saturated Humidity at the soil surface  (kg/m3)
//        
//    double emit = emis * StefanBoltzmanConst * Math.pow((Ts + 273.16), 4); // Emitted Heat Flux Density W/m2 (Stefan-Bolzman Law)
//    
//    double radin = (1 - albed) * Irad + sky - emit; // Net radiation Heat Flux Density W/m2 (incoming) [EQN 4]
//    
//    double senshfxd = capaa * (Ts - ctemp) / ra;    //Sensible Heat Flux Density [EQN 9](outgoing)
//    
//    if (psi1 > 0) psi1 = -0.5;
//    
//    double hss = shs *Math.exp(psi1 / (46.97 * (Ts + 273.16)));   //Absolute humidity of soil surface (kg/m3) [EQN 11]
//    
//    evap = (sha - ha) / ra / 1000;                      //Evaporative flux (m/s) [EQN 6]
//    
//    double alh = 2.49463E9 - 2247000 * Ts;               //Latent Heat of Vapourisation (j/m3) Forsythe,1964 [EQN 7]
//    
//    double ales = evap * alh;                                  //Latent Heat Flux Density [EQN 8]  (outgoing)
//    
//    double soilhfxd = thrmcond * (Ts - tsl[2][2])/NodeDepth + (Ts - tsl[1][2]) * capas * NodeDepth / (2 * Tstep);
//    
//                                              //[EQN 14] Soil Heat Flux Density (into the soil)
//    Res = radin - senshfxd - ales - soilhfxd; //[EQN 3]   res=0 when there is energy balance
//
// }
//
//
//
//public void CalcSoilEvaporation() {
//
//// This is the subroutine which actually performs the soil evaporation calculations.
//
//// Set the calculation parameters
//
//// Units of evaporation from this subroutine Kg/m2/s
////By working through the evaporation theory in power/mass terms the resultant
////evaportion rate is in terms of Kg/m2/s. This makes it directly compatible
////with volumetric water content. 1m3 water = 1000Kg water/m3
//
////The total PAR radiation incident on the soil is passed to this subroutine
////from the canopy microclimate module. For the multi-layer canopy module the
////radiation is taken to be that at 1 layer's worth of LAI below the canopy.
//    double totalradiation = soilaux.getRadiationIncidentOnSoil();
//    
//// Get the maximum volumetric capacity of the current soil type
//    double MaxWaterContent = soilaux.getFieldCapacity();
////Use a simple single layer water content, later versions will be
//// multiple layer and so an array has been used
//    double ActualWaterContent = soilaux.getVolumetricWaterContent(1);
//// Get the soil temperature, later versions will be multiple layer
//// and so an array has been used.
//    double SoilTemperature = 20;  //SoilMicroclimateOut.LayerSoiltemperature(1)
//// Use the wind speed at the bottom of the canopy as an approximation to windspeed
//// at the soil surface.
//    //double WindSpeed = canopyout.getLayerWindSpeed(10);
//    
//    double WindSpeed=0;
//    if (WindSpeed < 0.5)  WindSpeed = 0.5;
//// Get the clod size
//    double ClodSize = soilaux.getSoilClodSize();
//// Get the soil transmission coefficient
//    double SoilTransmission = soilaux.getSoilTransmissionCoefficient();
//// Get the soil reflectance coefficient
//    double SoilReflectance = soilaux.getSoilReflectanceCoefficient();
//// Get the specific heat capacity of air
//    double SpecificHeatCapacity = transaux.getSpecificHeatCapacityAir();
//
//    totalradiation = totalradiation * 0.235; // Convert light from µmoles to Watts (this for PAR)
//
//// Get the temperature sensitive physical parameters, use the same routine as the
//// transpiration module, not all the parameters are needed by that ok!
//    
//    double PhysicalParams[];
//    PhysicalParams=new double[4];
//    
//    PhysicalParams = evapo.GetPhysicalParams(ctemp);
//    
//    
//    double DensityDryAir=PhysicalParams[0];
//    double LatentHeatVapourisation=PhysicalParams[1];
//    double SlopeFactor_s=PhysicalParams[2];
//    double SatWaterVapConc=PhysicalParams[3];
//    
//    
//    
//    
//    
//    
//    
//    
//
//// Convert units for the parameter from MJ/Kg to J/Kg
//    LatentHeatVapourisation = LatentHeatVapourisation * 1000000;
////Convert units for the parameter from grams/m3/K to kg/m3/K
//    SlopeFactor_s = SlopeFactor_s * 0.001;
////Convert units for the parameter from g/m3 to Kg/m3
//    SatWaterVapConc = SatWaterVapConc * 0.001;
//    
//    double PsycParam = (DensityDryAir * SpecificHeatCapacity) / LatentHeatVapourisation;
//       
//// Calculate the boundary layer thickness at the surface of the soil
//    double BoundaryLayerThickness = 0.004 * Math.sqrt(ClodSize / WindSpeed);
//
//// Calculate the diffusivity of water vapour in the air, which is a function of temperature
//    double DiffCoef = (2.126 * Math.pow(10,-5)) + ((1.48 * Math.pow(10, -7)) * SoilTemperature);
//    double SoilBoundaryLayer = DiffCoef / BoundaryLayerThickness;
//
//// Modify soil conductance according to the ration of the volumetric water
//// content of the top layer of the soil to the maximum water content.
//    SoilBoundaryLayer = SoilBoundaryLayer * (ActualWaterContent / MaxWaterContent);
//    
////Calculate total radiation absorbed by leaves in layer being considered
//    double Ja = (2 * totalradiation * ((1 - SoilReflectance - SoilTransmission) / (1 - SoilTransmission)));
//    
//// Calculate the longwave radiation re-emitted from the soil, assuming that soil and air temperatures
//// are not too different.
//    double rlc = (4 * (StefanBoltzmanConst) * (Math.pow((273 + SoilTemperature), 3)) * SoilTemperature);
//  
//    double PhiN = Ja - rlc; // Calculate the net radiation balance
//    if (PhiN < 0)  PhiN = 0; // Prevent negative values
//    double Evaporation;
//    double rh=0;
//    switch (soilaux.getEvaporationModelMethod()) {
//        case 0:
//            // No evaporation calculation
//            Evaporation = 0;
//        case 1:
//            // Priestly-Taylor potential evaporation calculation
//            Evaporation = 1.26 * ((SlopeFactor_s * PhiN) / (LatentHeatVapourisation * (SlopeFactor_s + PsycParam)));
//        default:
//            //Penman Monteith potential evaporation calculation
//            
//            double DeltaPVa = SatWaterVapConc * (1 - rh / 100); // rh is relativehumidity
//            
//            Evaporation = (((SlopeFactor_s * PhiN) + LatentHeatVapourisation * PsycParam * SoilBoundaryLayer * DeltaPVa)) / (LatentHeatVapourisation * (SlopeFactor_s + PsycParam));
//    }
//
//// Report back the soil evaporation rate in Units mmoles/m2/s
//    Evaporation = Evaporation * 1000;  // Convert Kg H20/m2/s to g H20/m2/s
//    Evaporation = Evaporation / 18;     // Convert g H20/m2/s to moles H20/m2/s
//    Evaporation = Evaporation * 1000;   // Convert moles H20/m2/s to mmoles H20/m2/s
//    
//    if (Evaporation <= 0)  Evaporation = 0.00001; // Prevent any odd looking values which might get through at very low light levels
//    
//    soilaux.setSoilEvaporation (Evaporation); // Report the final soil evaporation figure
//    
//      
//}
//
//public void CalcSoilTemperature() {
//
//// This subroutine sets the temperature for soil layers if soil heat flux calculations are not switched on
//
//// Note that the soil temperature calculations are performed in the soil heat flux subroutine within this module
//
//    if (soilaux.getSoilTemperatureCalcFlag() != 1) {
//        // Soil heat flux calculations are turned on in the model parameter file
//        // therefore don't manually set the soil temperatures
//        
//    
//
//// Soil temperature will not be calculated by the soil heat flux routine and so we must
//// manually set the temperature of individual soil layers here. For most of the options this
//// will already have been done in the SetSoilMicroclimateVariables routine
//
//    switch (soilaux.getInitialSoilTemperatureFlag()) {
//         case 2:
//            // Set the temperature for all soil layers to the instantaneous air temperature calculated by the macroclimate module
//            for (int Counter = 1; Counter<= soilaux.getNumsoillayers();Counter++)
//                soilaux.setLayerSoiltemperature(Counter, macroout.getInstantAirTemperature());
//            
//    }
//    
//    }
//  
//}
//
//public void CalcSoilWaterPotential() {
//
//// ####################################################################################################################
//// This subroutine calculates the soil water potential based upon a chapter 12 - Simulation of water uptake
//// by plant roots, G.S. Campbell in the book Modelling plant and soil systems edited by John Hanks and J.T. Ritchie.
//// ####################################################################################################################
//
//// Need to find the references to CAMPBELL given in the back of the paper particularly with reference to the soil B factor which is a constant
//// that depends on the soil texture and ranges typically from 2 to 3.5 (Campbell, 1974)/ Convert energy terms MJ/Kg to pressure terms Mpa using the assumption that
//// they are numerically equal Plant Physiology, Salisbury and Rose. Note that when the water content of the top soil layers becomes very low
//// the soil water potential is not reported. Check on this we should get a large negative water potential I think? The leaf water potential appears to give the correct results
//// Don't forget to use mm/s the routine presented here converts from a daily transpiration rate to an instantaneous rate by dividing by 86400
//
//// ## Assumptions ##
//// This program simulates the water flow in the soil and makes the following assumptions:
//// 1).   Water moves from the soil, through the plant, to the evaporating surfaces in the substomatal cavities in response to gradients in water
//// potential. Only matric and gravitational potential are explicitly accounted for.
//
//// 2).   Resistances to flow in the liquid phase are mainly in the soil, the root epidermis and the leaf. These resistances are assumed constant and
//// known during the period of the simulation. Interfacial and xylem resistances could have been included but were assumed negligable for this model.
//
//// 3).   Low leaf water potential reduces transpiration
//
//// 4).   The root resistance to water uptake in a soil layer is inversely proportional to the fraction of the total root system present in that layer
//
//// 5).   Steady flow is assumed during the period of one time step.
//
//// Improvements needed in the future.
//// 1). Canopy transpiration Et is divided into evaporation (Ep) and transpiration (Tp) arbitrary
//// this could be improved by using algorithyms taking into account boundary conditins as described by Norman and Campbell (1983).
//// 2). Add soil evaporation drying effects to the top soil layer
//// 3). Rain or irrigation can be added by setting E(0) to the desired flux density
//
//
// 
//    if (soilaux.getSoilWaterPotentialCalcFlag() < 2) return; // If the database indicates that we don't need to perform a calculation then don't do it and simply exit the subroutine without further processing
//    
//    int numlayers = soilaux.getNumsoillayers();                     // Number of soil layers to be considered (Nodes)
//    double SoilBValue = soilaux.getSoilBValue();                        // Soil B value - default 3
//    double AirEntryPot = -Math.abs(soilaux.getSoilAirEntryPotential());      // Air entry potential (J/Kg) - default -2
//    double SatConductivity = soilaux.getSaturatedConductivity();         // Saturated conductivity (Kg s/m3)
//    double TotalRootResistance = soilaux.getTotalRootDensity();         // Total root resistance, partitioned up according to root distribution through root profile
//    double StomatalClosure = soilaux.getSpeciesStomatalClosureParam();   // Species dependent stomatal closure factor
//    double LeafResistance = soilaux.getLeafResistanceToWaterPassage();  // Leaf resistance to passage of water
//    double LayerDepth = soilaux.getSoilDepth() / numlayers;           // Sets the depth of the soil layers
//    double MaxStomatalClosure = soilaux.getMaxStomatalClosure();
//    double RootRadius = soilaux.getRootRadius();
//    double SoilEvap = transout.getSoilEvaporation();                        // Obtain the amount of soil evaporation
//    
//    double MinAcceptablePotential = 10000;
//    double Et=0;
//       
//    switch (soilaux.getSoilTranspirationMethodFlag()) {         // Obtain the instantaneous canopy transpiration rate from the transpiration module (umol/m2/s)
//        case 0:
//            Et = transout.getPenmanPotentialTranspirationRate();     // Use Penman/Monteith potential transpiratin
//        case 1:
//            Et = transout.getPriestlyPotentialTranspirationRate();   // Use Priestly/Taylor potential transpiration
//        case 2:
//            Et = transout.getActualCanopyTranspirationRate();        // Use Penman/Monteith actual transpiration rate
//    }
//    
//           
//    Et = Et * Math.pow(10,-6);  // Convert transpiration rate (umol/m2/s) to (moles/m2/s)
//    Et = Et * 18;       // Convert from (moles/m2/s) to (g H2O/m2/s)
//    
//    double Ep = 0.1 * Et;      // Calculate arbitrary evaporation (g H2O/m2/s)
//   
//    double tp = Et - Ep;       // Calculate arbitrary transpiration (g H2O/m2/s)
//    
//    if (ct.getDailyCalcFlag() == 0) {
//        // Once a day add precipitation to the soil surface. Convert rainfall from mm to Kg, assume 1m3 equals 1000Kg
//        double Rain = (macroout.getDailyPrecipitation() / 1000) / 1000 / 86400;
//        tp = tp - (Rain * 1000); // Convert Kg rainfall to g by multiplying by 1000
//    }
//    
//// Reserve dimensioned space for the model variables. Arrays are used to store values for each soil layer.
//    int SIZE1=numlayers;
//    int SIZE2=numlayers+2;
//    double  a[]=new double[SIZE2];
//    double  b[]=new double[SIZE2];
//    double  c[]=new double[SIZE2];
//    double  f[]=new double[SIZE2];
//    double  p[]=new double[SIZE2];
//    double  z[]=new double[SIZE2]; // Stores the depth of a soil layer (node) below the surface
//    double  V[]=new double[SIZE2];
//    double  DP[]=new double[SIZE2];
//    double  w[]=new double[SIZE2];
//    double  WN[]=new double[SIZE2];
//    double  K[]=new double[SIZE2];
//    double  CP[]=new double[SIZE2];
//    double  H[]=new double[SIZE2];
//    double  DV[]=new double[SIZE2];
//    double  JV[]=new double[SIZE2];
//    double  DJ[]=new double[SIZE2];
//    double  RootResistance[]=new double[SIZE1]; // Stores soil layer root resistance values
//    
//    double  l[]=new double[SIZE1];// Stores root density values cm/cm3 for soil layers
//    double  e[]=new double[SIZE1];// Stores water uptake from each layer values
//    double  RS[]=new double[SIZE1];// Stores soil layer resistances
//    double  PR[]=new double[SIZE1];
//    double  BZ[]=new double[SIZE1];
//    
//   
//    
//    double BD = 1.3;
//    double WS = 1 - BD / 2.6;
//    double b1 = 1 / SoilBValue;
//    double n = 2 + 3 / SoilBValue;
//    double N1 = 1 - n;
//    int WD = 1000;
//
//    if (ct.getIntervalHour() <= 0) {
//        //CR$ = Chr$(13) + Chr$(10)
//        // TheMessage$ = "No integration interval specified in code. Continuing with soil water potential calculation with an integration interval of 1hr."
//        //TheStyle = 48
//        //TheTitle$ = "Integration Interval Warning!"
//        //MsgBox TheMessage$, TheStyle, TheTitle$
//        ct.setIntervalHour(1);
//    }
//
//    // Set the integration interval according to simulation hour and day intervals
//    double dt = ct.getIntervalHour() * 3600 * ct.getIntervalDay();
//       
//    z[0] = 0; z[1] = 0; z[2] = 0.01; z[3] = 0.075;
//    double MW = 0.018; // Weight of 1 mole of water (kg)
//
//    for (int LAYER = 1; LAYER<= numlayers;LAYER++) {
//        p[LAYER] = soilaux.getLayerSoilWaterPotential(LAYER); // Setup the initial conditions of the soil layers
//        if (p[LAYER] == 0)  p[LAYER] = 0.00001;                       // Prevent division by 0 error
//        w[LAYER] = WS * Math.pow( (AirEntryPot / p[LAYER]) , b1);
//        WN[LAYER] = w[LAYER];
//        H[LAYER] = Math.exp(MW * p[LAYER] / (RealGasConstant * (soilaux.getLayerSoiltemperature(LAYER) + 273)));
//        K[LAYER] = SatConductivity * Math.pow((AirEntryPot / p[LAYER]), n);
//        if (LAYER > 2)  z[LAYER + 1] = z[LAYER] + LayerDepth;
//        V[LAYER] = WD * (z[LAYER + 1] - z[LAYER- 1]) / 2;
//   }
//
//    p[numlayers + 1] = p[numlayers];
//    H[numlayers + 1] = H[numlayers];
//    z[0] = -1E+10;
//    K[numlayers+ 1] = SatConductivity * Math.pow((AirEntryPot / p[numlayers + 1]), n);
//   
//// Initialise the root water uptake variables convert the root densities for each layer
//// from cm/cm3 to m/m3 and sum to give the total root density throughout the soil profile
//    double LSUM = 0; // Set the sum of the root densities to zero
//    for (int LAYER = 1; LAYER<= numlayers;LAYER++) {
//        l[LAYER] = soilaux.getLayerRootDensity(LAYER) * 10000; // Convert layer root density from cm/cm3 to m/m3
//        LSUM = LSUM + l[LAYER]; // Sum root densities to give total root density over all layers
//    }
//  
//// Calculate the root resistance and BZ for each soil layer
//    
//    double Pi=3.1415925;
//    for (int LAYER = 1; LAYER<= numlayers;LAYER++) {
//        if (l[LAYER] > 0) {
//            RootResistance[LAYER] = TotalRootResistance * LSUM / l[LAYER]; // Calculate the root resistance
//            BZ[LAYER] = N1 * Math.log(Pi * RootRadius*RootRadius * l[LAYER]) / (2 * Pi * l[LAYER] * (z[LAYER + 1] - z[LAYER - 1])); // Calculate BZ
//        }
//        else{
//            RootResistance[LAYER] = 1E+20; // When no roots are present set root resitance to large value to prevent calculations problems.
//            BZ[LAYER] = 0;                 // When no roots present set BZ=0
//        }
//   }
//   
//    double IM = 0.000001, DValue = 0.000024, vp = 0.017;
//    p[0] = p[1];
//    K[0] = 0;
//    double ha = 0.5;
//    double Pl=0;
//    double XP=0;
//    z[numlayers + 1] = 1E+20;
//
//// Plant water uptake subroutine, calculate the weighted mean soil water potential (PB) and the mean soil resistance (RB)
//    double PB = 0, RB = 0; // Reset weighted mean soil water potential and mean soil resistance
//    for (int LAYER = 1; LAYER<= numlayers;LAYER++) {
//        RS[LAYER] = BZ[LAYER] / K[LAYER];
//        PB = PB + (p[LAYER] - Acc_Gravity * z[LAYER]) / (RootResistance[LAYER] + RS[LAYER]); // Calc weighted mean soil water potential
//        RB = RB + 1 / (RS[LAYER] + RootResistance[LAYER]); // Calc mean soil resistance
//    }
//
//    PB = PB / RB; // Calc weighted mean soil water potential
//    RB = 1 / RB;  // Calc mean soil resistance
//  
//    switch (soilaux.getSoilWaterPotentialCalcFlag()) {
//        case 2:
//            // Perform simple model of stomatal closure and its effects on effective transpiration
//            // Uses potential transpiration as a baseline and reduces this appropriately for the
//            // stomatal closure resulting from leaf water potential. NOTE: stomatal closure effects
//            // are not passed back to the stomatal conductance routines in the leaf module.
//
//            // Start with an initial 'guess' of leaf water potential
//            if (Pl > PB)  Pl = PB - tp * (LeafResistance + RB);
//
//            double fvalue = 99999; // Set F to a high value to force operation of the iterative loop
//            double Counter = 0;
//            while (Math.abs(fvalue) > 30 && Counter < 200) {
//                // Perform the Newton/Raphson iteration to calculate the leaf water potential until the
//                // calculated leaf water potential is within 30 J/Kg of the mass balance value.
//                // Pl is the leaf water potential
//                XP = (Pl / MaxStomatalClosure);
//                XP = Math.pow(XP, StomatalClosure);
//                double SL = tp * (LeafResistance + RB) * StomatalClosure * XP / (Pl * (1 + XP) * (1 + XP)) - 1.05;
//                fvalue = PB - Pl - tp * (LeafResistance + RB) / (1 + XP);
//                Pl = Pl - fvalue / SL;
//                Counter = Counter + 1;
//            }
//            if (Counter > 200)  Pl = PB - tp * (LeafResistance + RB); // Iteration failed retain the best guess
//            // here made a little change, 200 instead of 20 in the statement, Counter%>20.
//    //    Case 3
//            // Use actual transpiration which accounts for canopy conductance/resistance in a detailed fashion
//            // to calculate leaf water potential. Need to introduce stomatal conductance effect in the leaf
//            // module to see canopy conductance and leaf water potential effects. We should iterate between this
//            // module and the canopy assimilation module. But this is likely to be too processor intensive.
//
//            // Start with an initial 'guess' of leaf water potential
//     //     If Pl > PB Then Pl = PB - TranspirationOut.ActualCanopyTranspirationRate * (LeafResistance + RB)
//    }
//   
//    double Tr = tp / (1 + XP);
//    for (int i = 1;i<=numlayers;i++) {
//        e[i] = (p[i] - Acc_Gravity * z[i] - Pl - LeafResistance * Tr) / (RootResistance[i] + RS[i]);
//    }
//    
//    double SE = 999999; //Set SE to a very high value to force the calculation loop
//    int IterationCounter = 0;
//    while (SE > IM && IterationCounter < 1000) {
//        SE = 0;
//        for (int LAYER = 1;LAYER<= numlayers;LAYER++) 
//            K[LAYER] = SatConductivity * Math.pow((AirEntryPot / p[LAYER]), n);
//        
//
//        JV[0] = Ep * (H[1] - ha) / (1 - ha);
//        DJ[0] = Ep * MW * H[1] / (RealGasConstant * (soilaux.getLayerSoiltemperature(1) + 273) * (1 - ha));
//        for (int LAYER = 1; LAYER<= numlayers; LAYER++) {
//            double KV = 0.66 * DValue * vp * (WS - (WN[LAYER] + WN[LAYER + 1]) / 2) / (z[LAYER + 1] - z[LAYER]);
//            JV[LAYER] = KV * (H[LAYER + 1] - H[LAYER]);
//            DJ[LAYER] = MW * H[LAYER] * KV / (RealGasConstant * (soilaux.getLayerSoiltemperature(LAYER) + 273));
//            CP[LAYER] = -V[LAYER] * WN[LAYER] / (SoilBValue * p[LAYER] * dt);
//            a[LAYER] = -K[LAYER - 1] / (z[LAYER] - z[LAYER - 1]) + Acc_Gravity * n * K[LAYER - 1] / p[LAYER - 1];
//            c[LAYER] = -K[LAYER% + 1] / (z[LAYER% + 1] - z[LAYER]);
//            b[LAYER] = K[LAYER] / (z[LAYER] - z[LAYER - 1]) + K[LAYER] / (z[LAYER + 1] - z[LAYER]) + CP[LAYER] - Acc_Gravity * n * K[LAYER] / p[LAYER] + DJ[LAYER - 1] + DJ[LAYER];
//            f[LAYER] = ((p[LAYER] * K[LAYER] - p[LAYER - 1] * K[LAYER - 1]) / (z[LAYER] - z[LAYER - 1]) - (p[LAYER + 1] * K[LAYER + 1] - p[LAYER] * K[LAYER]) / (z[LAYER + 1] - z[LAYER])) / N1 + V[LAYER] * (WN[LAYER] - w[LAYER]) / dt - Acc_Gravity * (K[LAYER - 1] - K[LAYER]) + JV[LAYER - 1] - JV[LAYER] + e[LAYER];
//            SE = SE + Math.abs(f[LAYER]);
//        }
//
//        for (int LAYER = 1;LAYER<= (numlayers - 1);LAYER++) {
//            c[LAYER]= c[LAYER] / b[LAYER];
//            f[LAYER] = f[LAYER] / b[LAYER];
//            b[LAYER + 1] = b[LAYER + 1] - a[LAYER + 1] * c[LAYER];
//            f[LAYER + 1] = f[LAYER + 1] - a[LAYER + 1] * f[LAYER];
//        }
//
//        DP[numlayers] = f[numlayers] / b[numlayers];
//        p[numlayers] = p[numlayers] - DP[numlayers];
//        if (p[numlayers] > AirEntryPot)  p[numlayers] = AirEntryPot;
//
//        for (int LAYER = numlayers - 1; LAYER>=1; LAYER--) {
//            DP[LAYER] = f[LAYER] - c[LAYER] * DP[LAYER + 1];
//            p[LAYER] = p[LAYER] - DP[LAYER];
//            if( p[LAYER] > AirEntryPot )
//                    p[LAYER] = (p[LAYER] + DP[LAYER] + AirEntryPot) / 2;
//            if (p[LAYER] < -MinAcceptablePotential ) {
//                    p[LAYER] = -MinAcceptablePotential;
//                    IterationCounter = 1000; // Prevent daft low values
//            }
//       }
//
//        for (int LAYER = 1;LAYER<= numlayers;LAYER++) {
//            WN[LAYER] = WS *Math.pow( (AirEntryPot / p[LAYER]), b1);
//            H[LAYER] = Math.exp(MW * p[LAYER] / (RealGasConstant * (soilaux.getLayerSoiltemperature(LAYER) + 273)));
//       }
//        
//        IterationCounter = IterationCounter + 1;
//   }
//  
//    H[numlayers + 1] = H[numlayers];
//    double SW = 0;
//
//    if (leafaux.getWaterStressCalcFlag() == 1 )
//        // Calculate the stomatal conductance modifying factor used in the more complex treatment
//        // of the model which feedsback onto stomatal conductance in the leaf assimilation module
//        CalcStomatalStressFactor();
//    
//
//    for (int LAYER = 1; LAYER<= numlayers;LAYER++) {
//        SW = SW + V[LAYER] * (WN[LAYER] - w[LAYER]);
//        w[LAYER] = WN[LAYER];
//        soilaux.setDepthofLayer(LAYER, z[LAYER]);             // Output the depth of the current layer
//        soilaux.setLayerSoilWaterPotential(LAYER, p[LAYER]);  // Output the soil water potential of the current layer
//        soilaux.setVolumetricWaterContent(LAYER, WN[LAYER]);  // Output the volumetric water content of the current layer
//    }
//        
// // Calculate a weighted average of the soil water potentials in all soil layers. This single value
// // is then useful for characterising the soil in the Tiger model of stomatal conductance. Multiply
// // layer water potentials by length root in layer and sum for all layers. Then divide by total root
// //length for all layers.
//    double TotalRootLength = 0;
//    for (int LAYER = 1;LAYER<= numlayers;LAYER++)
//         TotalRootLength = TotalRootLength + soilaux.getLayerRootDensity(LAYER);
//    
//            
//    double Avgpotential = 0;
//    for (int LAYER = 1; LAYER<= numlayers;LAYER++) 
//        Avgpotential += soilaux.getLayerSoilWaterPotential(LAYER) * soilaux.getLayerRootDensity(LAYER);
//    
//    
//    Avgpotential = Avgpotential * (1 / TotalRootLength);
//    soilaux.setWtAverageSoilWaterPotential(Avgpotential);
//
//    leafaux.setLeafWaterPotential(PI); // Output the leaf water potential
//    
//}
//
//public void CalcStomatalStressFactor() {
//
//// This function calculates the stomatal stress factor (0-1) representing an estimated % reduction
//// in the stomatal conductance calculated by the leaf assimilation routine. A value of 0 indicates
//// that no stomatal conductance reduction (closure) needs to be imposed, a value of 1 indicates
//// that stomatal conductance should be forced to 0 indicating complete stomatal closure.
//
//    double StressFactor;
//    switch (leafaux.getWaterStressCalcFlag()) {
//        case 0:
//            StressFactor = 0;
//            
//        case 1:   
//            if (leafaux.getLeafWaterPotential() > leafaux.getThresholdWaterPotential()) 
//                StressFactor = 0; // Leaf water potential not low enough yet to cause stomatal closure
//            else {
//                // Simulate a linear response of stomatal conductance reduction to lowering leaf water potential.
//                double WaterStress = (Math.abs(leafaux.getLeafWaterPotential()) - Math.abs(leafaux.getThresholdWaterPotential())) / 1000;
//                StressFactor = WaterStress * leafaux.getWaterStressSensitivity();
//                if (StressFactor > leafaux.getMaxReductionStomatalConduction() )
//                    StressFactor = leafaux.getMaxReductionStomatalConduction(); //Ensure that the maximum reduction in stomatal conductance is not exceded
//            }     
//            
//            
//        default:
//        
//            // Other model relationships between stomatal conductance reduction and leaf water potential can be inserted here
//            StressFactor = 0;
//            
//    }
//    
//    leafaux.setgsWaterStressModifier( StressFactor); // Set stress factor
//
//}
//
//public void CalcWaterBalance() {
//
//// The Water balance calculations are based upon a simple model by Ian Johnson See the help file for further details.
//
//// If the water content of the top layers of soil area above the wilting point then the equivalent amount of water transpired by the canopy
//// plus that lost by evaporation is lost from the soil.
//
//// If the soil is at the wilting point or below then only evaporative water loss is taken into account. This is a simple model of soil water content
//// similar in its limitations to the sunlit/shaded canopy module, future modules should be multi-layer in nature and take into account
//// water conductivity between layers. The routine can accept transpiration and evaporation rates in terms of daily, hourly or second time intervals.
//    
//    if (soilaux.getSoilWaterPotentialCalcFlag() != 1) return; // Don't perform this calculation unless the soil water balance flag is set to simple single layer water balance model
//    
//    soilaux.setNumsoillayers(1); // We are only dealing with a single soil layer
//
//    double CriticalValue = soilaux.getCriticalValue();
//    double WiltingPoint = soilaux.getWiltingValue();
//    double WaterContent = soilaux.getVolumetricWaterContent(1);
//    double Evaporation = soilaux.getSoilEvaporation();
//    double SoilProfileDepth = soilaux.getSoilDepth();
//    double TranspirationRate=0;
//
//    switch (soilaux.getSoilTranspirationMethodFlag()) {
//        case 0:
//            // Penman-Monteith potential transpiration, mmoles/m2/s
//            TranspirationRate = transout.getPenmanPotentialTranspirationRate();
//        case 1:
//            // Priestly-Taylow potential transpiration, mmoles/m2/s
//            TranspirationRate = transout.getPriestlyPotentialTranspirationRate();
//        case 2:
//            // Penman-Monteith actual transpiration rate , mmoles/m2/s
//            TranspirationRate = transout.getActualCanopyTranspirationRate();
//    }
//   
//// Convert evaporation units from Kg rate to m3 rate assuming 1m3 water=1000Kg/m3
//    Evaporation = Evaporation / 1000;
//// Convert from mmole rate to moles rate
//    TranspirationRate = TranspirationRate / 1000;
//// Convert from mole rate to gram rate
//    TranspirationRate = TranspirationRate * 18;
//// Convert from gram rate to Kg rate
//    TranspirationRate = TranspirationRate / 1000;
//// Convert from Kg rate to m3 water rate, assume 1m3 water=1000Kg/m3
//    TranspirationRate = TranspirationRate / 1000;
//          
//    if (WaterContent > CriticalValue) {
//        // If water content of the soil is greater than critical value then
//        // full potential transpiration is assumed.
//        WaterContent = WaterContent - (TranspirationRate * (1 / SoilProfileDepth));
//        // Remove soil evaporation from the water content, evaporation in Kg/m2/s
//        // can taken away from volumetric water content directly. This assumes
//        // that 1m3 is equivalent to 1000Kg water/m3
//        WaterContent = WaterContent - soilaux.getSoilEvaporation();
//    }
//
//    if (WaterContent <= CriticalValue && WaterContent > WiltingPoint) {
//        // If water content is less than or equal to the critical value and greater than
//        // the wilting point then apply linearly decreasing between potential and zero
//        // transpiration for intermediate values of soil water content.
//        TranspirationRate = (TranspirationRate * (WaterContent - WiltingPoint) / (CriticalValue - WiltingPoint));
//        WaterContent = WaterContent - (TranspirationRate * (1 / SoilProfileDepth));
//        // Remove soil evaporation from the water content, evaporation in Kg/m2/s
//        // can taken away from volumetric water content directly. This assumes
//        // that 1m3 is equivalent to 1000Kg water/m3
//         WaterContent = WaterContent - soilaux.getSoilEvaporation();
//     }
//
//    if (WaterContent <= WiltingPoint) {
//        // If the water content is less than or equal to the wilting point of the soil
//        // then no transpirational water loss can occur. Only evaporational water loss
//        // directly from the soil surface can occur.
//        // Remove soil evaporation from the water content, evaporation in Kg/m2/s
//        // can taken away from volumetric water content directly. This assumes
//        // that 1m3 is equivalent to 1000Kg water/m3
//        WaterContent = WaterContent - soilaux.getSoilEvaporation();
//    }
//
//// Output the current water content of the soil after transpiration+evaporation ---------------------------------------
//    soilaux.setVolumetricWaterContent(1, WaterContent);
//}
//
//public void CalcWaterRunOff() {
//
////This subroutine looks at the current volumetric water content of the soil
//// and checks this against the maximum holding capacity. Any water above
//// content above this is assumed to run of the soil either directly at the
//// surface or by passing directly to lower layers.
//
//// This subroutine should be modified to take account of multiple layers in
//// the long term. I guess we would look at the water content/max water content
//// of each layer and assume water either runs of the surface of passes to
//// lower layers.
//
//    double MaxWaterContent = soilaux.getFieldCapacity();
//    double WaterContent = soilaux.getVolumetricWaterContent(1);
//
//    if (WaterContent > MaxWaterContent) {
//        // Too much water present for top soil layer to hold, we must loose some water
//        // due to run off or drain through. Don't currently has drain through but can
//        // calculate the volume of water which runs off m3.
//        soilaux.setSoilWaterRunOff( WaterContent - MaxWaterContent);
//        soilaux.setVolumetricWaterContent(1, MaxWaterContent);
//    }
//
//}



    

}
