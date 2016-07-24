package model;

public class Utilities {
	
    
    public double SaturaingVaporPressure(double Temperature) {
    //This function calcaultes the saturating water vapor pressure at certain temperature. The unit of the pressure is mbar.

         return 6.13755 * Math.exp(Temperature * (18.564 - Temperature / 254.4)/ (Temperature + 255.57));

    }
    
    public double ConvertHumiditytoVPD(double RelHumidity, double Temperature) {
    /*
    This subroutine takes an input of the relative humidity of air (%)
    at a specified temperature and converts this to a vapour pressure
    defecit value (mol/mol). The VPD value is reported as kPa
    */
    //First find the water vapour pressure when the air is totally saturated
        double SatWaterVapPressure = GetSatWaterVapPressure(Temperature);

    //Convert the humidity from percentage terms if necessary
        double Humidity = RelHumidity / 100;

    //Now calculate the vapour pressure defecit
        return  SatWaterVapPressure - (Humidity * SatWaterVapPressure);

    }
            
    public double GetSatWaterVapPressure(double Temperature) {
 /*
 This function calculates the saturation vapour pressure
 of water. The equation comes from appendix 4 of the
 Hamlyn G. Jones book on Plants and Microclimate and
 should give a good approximation to tabulated data
 over the normal range of physiologucal values.
 */

 //Temperature (°C)
 //Saturated water vapour pressure is returned in kPa

        return  10 * 0.061375 * Math.exp((17.502 * Temperature) / (240.97 + Temperature));
    
    }



}
