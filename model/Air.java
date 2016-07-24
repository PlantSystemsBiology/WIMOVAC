package model;

import  gui.WIMOVAC;

public class Air {
	public double current_T, avg_T, min_T, max_T;                     // avg_T: average temperature, min_T, max_T: minimal and 
	// maximal temperature of air

	public double CO2_concentration, O2_concentration, O3;
	public double RH;
	
	public double dry_AirDensity;
	public double latent_HeatVapour;
	public double slope_FactorS;
	public double sat_WaterVapConc;
	
	public Air(){
		current_T 			= 25;
		avg_T				= 25;
		min_T				= 25;
		max_T				= 25;
		CO2_concentration 	= 380;   //ubar
		O2_concentration  	= 210;   //mbar
		RH 					= 0.7;   //%
	}
	
	public Air(CurrentTime ct, Location lct){
		getAirTemperature(ct, lct);
		
		//default value
		CO2_concentration 	= 380;   //ubar
		O2_concentration  	= 210;   //mbar
		RH 					= 0.7;   //%
		getPhysicalParameters();
		
	}
	
	public void update(CurrentTime ct, Location lct){
		getAirTemperature(ct, lct);
		getPhysicalParameters();
	}
	
	public void getAirTemperature(CurrentTime ct, Location lct){
	        double YearAvgTemp 		= Double.valueOf(WIMOVAC.constants.getProperty("AnnMeanAirTemperature"));
		    double YearAvgTempRange = Double.valueOf(WIMOVAC.constants.getProperty("AmplitudeAnnualTemperatureChange"));
		    double DailyAvgRange	= Double.valueOf(WIMOVAC.constants.getProperty("AmplitudeDailyTemperatureChange"));
		    double MaxDailyRange 	= Double.valueOf(WIMOVAC.constants.getProperty("MaximumDailyTemperatureChange"));
		    double SineWaveStart	= Double.valueOf(WIMOVAC.constants.getProperty("StartDayTemperatureCycle"));
		    double TemperaturePeakHour= Double.valueOf(WIMOVAC.constants.getProperty("TemperaturePeakHour"));

		    //Calculate air temperatures above the canopy temperatures.
		    double CurrentDailyMean = YearAvgTemp + YearAvgTempRange * Math.sin(2 * Math.PI * ((ct.day - SineWaveStart) / 365));
	///	    double PreviousDailyMean= YearAvgTemp + YearAvgTempRange * Math.sin(2 * Math.PI * ((t.day - 1 - SineWaveStart) / 365));
		    double DailyRange 		= DailyAvgRange + (MaxDailyRange - DailyAvgRange) * Math.sin(2 * Math.PI * ((ct.day - SineWaveStart) / 365));
		    double DailyExcursion 	= Math.sin(2 * Math.PI * ((ct.hour - (TemperaturePeakHour - 6)) / 24));
		    double InstantTemp 		= CurrentDailyMean + DailyRange * DailyExcursion;

		    //Calculate the highest and lowest daily temperatures.
		    double CurrentHighest 	= CurrentDailyMean + DailyRange;
		    double CurrentLowest  	= CurrentDailyMean - DailyRange;

		    //Output the Results through the appropriate type definitions
		    //calcualted
		    current_T = InstantTemp;
		    avg_T     = CurrentDailyMean;
		    max_T     = CurrentHighest;
		    min_T     = CurrentLowest;
		 //   System.out.println("avg T: "+avg_T);

	}
	
	private void getPhysicalParameters(){
			/*
			  This subroutine examines the auxilary variable arrays for temperature sensitive
			  physical parameters required by transpiration. For a given leaf temperature
			  the nearest value for dry air density, latent heat of vapourisation and slope
			  factor s is returned. Note that the s value and the saturated water vapour
			  concentration are in CONCENTRATION terms and not pressure terms. Concentration
			  terms are needed by our working of the Penman/Monteith equation which
			  may be worked in either concentration or pressure terms.
			  If we can find a conversion between the sat water concentration and the
			  corresponding sat wat vap pressure then we can use this with the empiracle
			  calculation of sat wat vap pressure calculation in the global functions module.
			 */

			 // All temperatures are in degrees C
			     double DryAirDensity	=0.0;
			     double LatentHeatVapour=0.0;
			     double SlopeFactorS	=0.0;
			     double SatWaterVapConc	=0.0;

			 // If actual leaf temperature is less than we have tabulated values for then use
			 // the value for the lowest temperature we have
			     if (current_T < -5) {
			          DryAirDensity 	= Constants.DryAirDensity[0];
			          LatentHeatVapour 	= Constants.LatentHeatVapour[0];
			          SlopeFactorS 		= Constants.SlopeFactorS[0];
			          SatWaterVapConc 	= Constants.SatWaterVapConc[0];
			          
			     }
			 // If the actual leaf temperature is greater that we have tabulates values for then
			 // use the value for the highest temperature.
			     if (current_T > 45) {
			           DryAirDensity 	= Constants.DryAirDensity[10];
			           LatentHeatVapour = Constants.LatentHeatVapour[10];
			           SlopeFactorS 	= Constants.SlopeFactorS[25];
			           SatWaterVapConc 	= Constants.SatWaterVapConc[25];
			     }

			 // The actual leaf temperature must be somewhere within the range of tabulated values
			 // The calculation of TemperatureValue determines the temperature that each piece of
			 // data in the array corresponds to. This must be modified if later additional entries
			 // to the tabulated data are made.
			     
			     if (current_T >= -5 && current_T <= 45){
			       for(int Counter=1; Counter<=12;Counter++) {
			         int TemperatureValue = -5 + (Counter - 1) * 5;
			         if (TemperatureValue > current_T) {
			             DryAirDensity 		= Constants.DryAirDensity[Counter - 2];
			             LatentHeatVapour 	= Constants.LatentHeatVapour[Counter - 2];
			             Counter 			= 12; // Clumsy but it works!!
			         }
			        }
			 // Get the temperature sensitive values of the slope factor s, store values in an array
			 // The calculation of TemperatureValue determines the temperature that each piece of
			 // data in the array corresponds to. This must be modified if later additional entries
			 // to the tabulated data are made.
			       for(int Counter=1; Counter<=27;Counter++) {
			         int TemperatureValue = -5 +(Counter - 1) * 2;
			         if (TemperatureValue > current_T) {
			             SlopeFactorS 		= Constants.SlopeFactorS[Counter - 2];
			             SatWaterVapConc 	= Constants.SatWaterVapConc[Counter - 2];
			             Counter 			= 27; // Clumsy but it works!!
			         }
			       }
			     }
			     dry_AirDensity=DryAirDensity;
			     latent_HeatVapour=LatentHeatVapour;
			     slope_FactorS=SlopeFactorS;
			     sat_WaterVapConc=SatWaterVapConc;

	}
	
	

}
