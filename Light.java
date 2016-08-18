package model;

import gui.WIMOVAC;

public class Light {
	public double direct_PPFD, diffuse_PPFD, scatter_PPFD; //direct: from solar, diffuse: atmospheric diffuse light, scatter: leaf scattered light. 

	public Light(){

		direct_PPFD =0;
		diffuse_PPFD=0;
		scatter_PPFD=0;

	}
	
	
	public Light(Solar solar){
		calLight(solar);
	}
	
	public void update(Solar solar){
		calLight(solar);
	}
	
	private void calLight(Solar solar){

		// D. DePury, G. Farquhar 1997, PCE, (compbell 1977)
		
		// parameters used for the calculation: 
	    double Is = Double.valueOf(Constants.SolarConstant); // Solar constant value, total solar PPFD. 
		double alpha = Double.valueOf(WIMOVAC.constants.getProperty("Atmospheric_Transmittance")); // atmospheric transmission coefficient of PAR. 
	    double P = Double.valueOf(WIMOVAC.constants.getProperty("Atmospheric_Pressure")); // atmospheric pressure. 
		double P0 = Double.valueOf(Constants.Atmospheric_Pressure_sea_level); // atmospheric pressure at sea level
		double theta = Maths.DegtoRad(solar.ZenithAngle);    // solar zenith angle, which equals to Pi - solar elevation angle.
		
		double m = ( P / P0 ) / Math.cos(theta); // optical air mass. 
		
		// Equations: 
		double Idir = Is * Math.pow(alpha,m) * Math.cos(theta);  // the light intensity is for horizontal plane. 
		double Idiff = 0.5 * Is * ( 1 - Math.pow(alpha,m) ) * Math.cos(theta); 

	    direct_PPFD = Idir;
	    diffuse_PPFD = Idiff;
	    
	    //default
	    scatter_PPFD=0;
	}

}
