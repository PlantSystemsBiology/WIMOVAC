package function;

import org.jfree.data.xy.XYSeries;

public class CanopyRes {
	
	public XYSeries xys_Ac 			= new XYSeries("Ac");  // Ac ~ time curve
	public XYSeries xys_Ac_sunlit 	= new XYSeries("Ac of sunlit");  // ...
	public XYSeries xys_Ac_shaded 	= new XYSeries("Ac of shaded");
	public XYSeries xys_N_sunlit 	= new XYSeries("Nitrogen of sunlit");
	public XYSeries xys_N_shaded 	= new XYSeries("Nitrogen of shaded");
	public XYSeries xys_LAI_sunlit 	= new XYSeries("LAI of sunlit");
	public XYSeries xys_LAI_shaded 	= new XYSeries("LAI of shaded");
	public XYSeries xys_conductance	= new XYSeries("canoy conductance");

}


