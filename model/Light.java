package model;

import gui.WIMOVAC;

public class Light {
	public double direct_PPFD, diffuse_PPFD, scatter_PPFD; //direct: from solar, diffuse: atmospheric diffuse light
    public double total_radiation; 
	//scatter: leaf scatter light.
	
	public Light(){
		//default
		direct_PPFD =0;
		diffuse_PPFD=0;
		scatter_PPFD=0;
		total_radiation = 0;  // we need a model to calculate this. Qingfeg
	}
	
	
	public Light(Solar solar){
		calLight(solar);
	}
	
	public void update(Solar solar){
		calLight(solar);
	}
	
	private void calLight(Solar solar){

		// D. DePury, G. Farquhar 1997, PCE, (compbell 1977)
	    double Idir = Double.valueOf(Constants.SolarConstant) * 
                    Math.pow(Double.valueOf(WIMOVAC.constants.getProperty("Atmospheric_Transmittance")), 
                    (Double.valueOf(WIMOVAC.constants.getProperty("Atmospheric_Pressure")) / 
                    Double.valueOf(Constants.Atmospheric_Pressure_sea_level)) 
                / Math.cos(Maths.DegtoRad(solar.ZenithAngle))) * Math.cos(Maths.DegtoRad(solar.ZenithAngle));

	    double Idiff = 0.5 * Double.valueOf(Constants.SolarConstant) * 
	    (1 - Math.pow(Double.valueOf(WIMOVAC.constants.getProperty("Atmospheric_Transmittance")), 
                    (Double.valueOf(WIMOVAC.constants.getProperty("Atmospheric_Pressure")) / 
                    Double.valueOf(Constants.Atmospheric_Pressure_sea_level)) / 
                    Math.cos(Maths.DegtoRad(solar.ZenithAngle))))* Math.cos(Maths.DegtoRad(solar.ZenithAngle));

	    direct_PPFD = Idir;
	    diffuse_PPFD = Idiff;
	    
	    //default
	    scatter_PPFD=0;
	}

}
