package function;

import org.jfree.data.xy.XYSeries;

public class MicroClimateRes {
	
	public XYSeries xys_directPPFD 		= new XYSeries("direct light");  // Ac ~ time curve
	public XYSeries xys_diffusePPFD 	= new XYSeries("diffuse light");  // ...
	
	public XYSeries xys_PPFD_sunlit 	= new XYSeries("light on sunlit leaf");
	public XYSeries xys_PPFD_shaded 	= new XYSeries("light on shaded leaf");
	public XYSeries xys_PPFD_scattered 	= new XYSeries("light on scattered leaf");
	
	public XYSeries xys_solar_zenith_agl= new XYSeries("solar zenith angle");  
	
	public XYSeries xys_LAI_sunlit 		= new XYSeries("LAI of sunlit leaf");
	public XYSeries xys_LAI_shaded		= new XYSeries("LAI of shaded leaf");

	public XYSeries xys_N_sunlit 		= new XYSeries("Nitrogen of sunlit leaf");
	public XYSeries xys_N_shaded 		= new XYSeries("Nitrogen of shaded leaf");
	
	public XYSeries xys_T_air 			= new XYSeries("air temperature");  
	
}
