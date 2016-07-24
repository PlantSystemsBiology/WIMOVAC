package function;

import org.jfree.data.xy.XYSeries;

public class LeafRes {
	
	public XYSeries xys_A;  //when change light, it is A~light curve, when change CO2, it is A~CO2 curve            
	public XYSeries xys_QY;  //...
	public XYSeries xys_LCP;
	public LeafRes(String name){
		xys_A	= new XYSeries(name);  //when change light, it is A~light curve, when change CO2, it is A~CO2 curve            
		xys_QY 	= new XYSeries(name);  //...
		xys_LCP	= new XYSeries(name);
	}

}
