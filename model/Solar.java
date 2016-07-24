package model;

import gui.WIMOVAC;

public class Solar {
	public double ZenithAngle;
	public double SolarElevation;
	public double AzimuthAngle;
	public double SunUp;
	public double SunDown;
	public double Daylength;
	
	public Solar(){
		//default value TODO
	}
	
	public Solar (CurrentTime t, Location lct){
		calSolarGeometry(t,lct);
	}
	
	public void update(CurrentTime t, Location lct){
		calSolarGeometry(t,lct);
	}
	
	private void calSolarGeometry(CurrentTime t, Location lct){

		/*The sun zenith and azimuth are needed in the calculation of incident
	      angles of direct radiation to leaf surfaces. They are calculated
	      according to the atronomical formulae derived by Norman. All angles
	      are expressed in degrees rather than radians, they therefore need
	      to be converted.

	      Major features of radiation at the surface of the Earth are determined by
	      Earths rotation about its own axis and by its eliptical orbit around the
	      sun. The polar axis about which the Earth rotates is fixed in space at a mean
	      angle of 66.5' to the plane of its orbit and the Earths equatorial plane therefore
	      oscillates between a maximum of 90-66.5=23.5 in midsummer and a minimum of -23.5
	      in midwinter. This angle is known as the SOLAR DECLINATION.
	    */

	      // Driving variables
	    double MacroclimateDay = t.day;
	    double NumYears = (int)(MacroclimateDay / 365);       // Handle multiple years as the first year repeated
	    double Julian_Day = MacroclimateDay - NumYears * 365;
	    // Auxilary variables

//	    double TimeCorrectionFactor=0;

	    /*1. Calculate the solar declination in degress. The solar declination angle
	      is the angle between the sun's rays and the equitorial plane of the earth and
	      is function of the time of year with a minimum on DOY 355 (Dec 21) and a maximum
	      on DOY 172.5 (Jun 21/22)
	      Alternative formulation which gives the same results - N.B. Pickering
	       AngleRadians = 2 * pi * (Julian_Day% + 10!) / 365!
	     */
	     double TimeAngleRadians = Maths.DegtoRad(360 * (Julian_Day + 10) / 365);
	     double SolarDeclination = -23.5 * Math.cos(TimeAngleRadians);

	     // 2. Convert some of the key parameters from degrees to radians
	     double LatitudeR = lct.Latitude*3.14/180;
	     double SolarDeclinationR = SolarDeclination*3.14/180;
	     double TimeFactorR = 3.14*(15 * (t.hour - Double.valueOf(WIMOVAC.constants.getProperty("TimeSolarNoon"))))/180;
	     
	     

	     //The time factor is the hour angle of the sun (the angular distance from the meridian of the observer)

	     /*Insert one step to calculate the azimuth angle; Currently we use a azimuth angle less than 90 degree.
	       This routine is strictly good for northern hemisphere. Notice one interesting problem in the algorithm is that
	       there is one sharp decrease in this angle at solar noon.
	     */
	     double SSin = Math.sin(SolarDeclinationR) * Math.sin(LatitudeR);
	     double CCos = Math.cos(SolarDeclinationR) * Math.cos(LatitudeR);
	     
	     double AltitudeAngle = Math.asin(SSin + CCos * Math.cos(TimeFactorR)); 
	      
	     double AzimuthAngle1 = Math.acos(-(Math.cos(LatitudeR) * Math.sin(SolarDeclinationR) - Math.cos(SolarDeclinationR) 
	                                  * Math.sin(LatitudeR) * Math.cos(TimeFactorR)) / Math.cos(AltitudeAngle));
	     
	     
	     //double TcorrectionFactor = ((Longitude-StandardMeridian)*4+EoTime)/60;

	    
	     /* 3. Calculate the time factor correction. Based upon time fit data from List (1963)
	       max error+/-0.6 min. Longitude and standard meridian are +(E) or -(W). In the Western
	       hemisphere the standard time at local apparent noon = 12.00 - TimefactorR-4*Latitude (degrees)
	       Not sure what this is yet or how to apply it but seems important. I think that this
	       is the amount that the time factor equation has to be modified to account for effects
	       of longitude if standard time is being used rather than local time - revisit at a later date
	     */
//	     double EoTime = 0.01789 - 7.141 * Math.sin(TimeAngleRadians) + 1.804 * Math.cos(TimeAngleRadians) -
//	                            9.937 * Math.sin(2 * TimeAngleRadians) + 0.3564 * Math.sin(2 * TimeAngleRadians);
	     // where is StandardMeridian ?????????
	     //double TCorrectionFactor = ((Longitude - StandardMeridian) * 4 + EoTime) / 60;

	     // Calculate sun angles limited for latitudes outside of polar circles
	    
	     double SOC = SSin / CCos;
	     SOC = Math.min(Math.max(SOC, -1), 1);

	     /*4. Calculate the solar zenith angle. The solar zenith angle has a value of around 90o
	         before sunrise and after sunset and approaches towards 0 as time of day reaches solar noon
	         may not reach 0 depending on latitude(?) 
	         Prevent a calculation error is cos_theta is a small number by using numerical fudge.
	      */
	    double CosZenithAngle = SSin + CCos * Math.cos(TimeFactorR);
	    if (CosZenithAngle <= 1E-10)
	        CosZenithAngle = 1E-10;
	    double ZenithAngle1 = Maths.RadtoDeg(Math.acos(CosZenithAngle));

	    /*5. Calculate the daylength, the number of hours that the sun is above the horizon
	         First calculate the hour angle of the sun at sunrise and sunset.
	         Care must be taken to evaluate the inverse cosine in the correct quadrant,
	         or else the equation fails. If (2*Coshour/15) is positive then 0<=15h/2<=90
	         else if it is negative then 90<=15h/2<=180
	    */
	    double CosHour = -Math.tan(LatitudeR) * Math.tan(SolarDeclinationR);
	    // It is wrong ??????????
	    double CosHourDeg = Maths.RadtoDeg(CosHour);
	    //It should be
	    //double CosHourDeg = RadtoDeg(Math.acos(CosHour));
	    if (CosHourDeg < -57)
	        CosHour = -0.994;   // Force calculations into the correct quadrant, is this correct??
	    double Daylength1 = 2 * Maths.RadtoDeg(Math.acos(CosHour)) / 15;
	    // Daylength = 12 + 24 * ArcSin(SOC) / Pi;   Alternative coding to daylength equation - gives same answer

	    //6. Calculate the approximate time of sun rise and sun set (hrs since midnight)
	    double SunUp1 = 12 - Daylength1 / 2;
	    double SunDown1 = 12 + Daylength1 / 2;

	    /*7. Calculate solar elevation at a site, use these results tentatively until the results can be confirmed.
	         This relationship between solar zenith angle and solar elevation is as given in: Plants and Microclimate.
	         Hamlyn G. Jones.
	    */
	    double SinSolarElevation = CosZenithAngle;
	    double SolarElevation1 = Maths.RadtoDeg(Math.asin(SinSolarElevation));

	    /*8. Calculate Idir, the photon flux in direct beam solar radiation. This is
	         based upon the Solar constant(photon flux in a plane perpendicular to the
	         solar beam above the atmosphere), atmospheric transmittance, atmospheric pressures
	    */

	    ZenithAngle = ZenithAngle1;
	    SolarElevation = SolarElevation1;
	    AzimuthAngle = AzimuthAngle1;
	    Daylength = Daylength1;
	    SunUp = SunUp1;
	    SunDown = SunDown1;
	  //  macroClimate.setTimeCorrectionFactor(TimeCorrectionFactor);

	}

}
