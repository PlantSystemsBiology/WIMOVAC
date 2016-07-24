package function;

import org.jfree.data.xy.XYSeries;

public class GrowthRes {
	
	
	// Tab 1
	public XYSeries xys_NetAssiRate 				= new XYSeries("Net Assimilation Rate");  // mol CO2/m2/day
	public XYSeries xys_ElapsedAssimilation 				= new XYSeries("Elapsed Assimilation");  // mol CO2/m2
	public XYSeries xys_CanopyConductance 				= new XYSeries("Canopy Conductance");  // mol H2O/m2/day ??
	public XYSeries xys_Evapo_transpiration 				= new XYSeries("Evapo-transpiration");  // mol H2O/m2/day
	public XYSeries xys_CumulativeEvapoTranspiration 				= new XYSeries("Cumulative evapo-transpiration");  //
	public XYSeries xys_LAI 				= new XYSeries("LAI");  //
	public XYSeries xys_LeafWaterPotential 				= new XYSeries("Leaf Water Potential");  //
	public XYSeries xys_Respiration 				= new XYSeries("Respiration");  //
	public XYSeries xys_CumulativeRespiration 				= new XYSeries("Cumulative Respiration");  //
	public XYSeries xys_WaterUseEfficiency 				= new XYSeries("Water Use Efficiency");  //
	
	// Tab 2 
	public XYSeries xys_AirTemp 				= new XYSeries("Air temperature");  //
	public XYSeries xys_DayMeanTemp 				= new XYSeries("Daily mean temperature");  //
	public XYSeries xys_DayHighTemp 				= new XYSeries("Daily highest temperature");  //
	public XYSeries xys_DayLowTemp 				= new XYSeries("Daily lowest temperature");  //
	public XYSeries xys_ElapsedThermalTime_day 				= new XYSeries("Elapsed thermal time (day)");  //
	public XYSeries xys_ElapsedThermalTime_hour 				= new XYSeries("Elapsed thermal time (hour)");  //
	public XYSeries xys_RelativeHumidity 				= new XYSeries("Relative Humidity");  //
	public XYSeries xys_OzoneConcentration 				= new XYSeries("Ozone concentration");  //
	public XYSeries xys_DirectSunLight 				= new XYSeries("Direct sun light");  //
	public XYSeries xys_DiffuseSunLight 				= new XYSeries("Diffuse sun light");  //
	public XYSeries xys_TotalRadiation 				= new XYSeries("Total radiation");  //
	public XYSeries xys_DayRadiation 				= new XYSeries("Daily radiation");  //
	public XYSeries xys_RainFall 				= new XYSeries("Rain fall");  //
	public XYSeries xys_CumulativeRainFall 				= new XYSeries("Cumulative Rain fall");  //
	
	
	// Tab 3
	public XYSeries xys_leaf_dry_weight 	= new XYSeries("leaf DW");  // ...
	public XYSeries xys_stem_dry_weight 	= new XYSeries("stem DW");  // ...
	public XYSeries xys_standing_dead 	= new XYSeries("Standing dead");  // ...
	public XYSeries xys_surface_dead 	= new XYSeries("Surface dead");  // ...
	public XYSeries xys_seed_dry_weight 	= new XYSeries("seed DW");  // ...
	public XYSeries xys_pod_dry_weight 		= new XYSeries("pod DW");  // ...
	
	
	// Tab 4
	public XYSeries xys_s_root_dry_weight 	= new XYSeries("Structure Root DW");  // ...
	public XYSeries xys_f_root_dry_weight 	= new XYSeries("Fine Root DW");  // ...
	public XYSeries xys_storage 	= new XYSeries("Storage");  // ...
	public XYSeries xys_root_dead 	= new XYSeries("Root Dead");  // ...
	

}






