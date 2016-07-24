package model;



public class CanopyProperties extends Leaf{
	
	public double cond;                  // canopy level conductance
	public double actual_canopy_E;   // actual canopy evapo-transpiration rate. QF
	public double priestly_potential_E;
	public double penman_potential_E;
	public double CO2_uptake_rate;       // CO2_uptake_rate (umol CO2.m-2 ground area. s-1), which is different from Assimilation rate of leaf (umol CO2. m-2 leaf area. s-1). 
	public double LAI;                   // LAI: leaf area index

	public double canopyWaterUseEfficiency;
}
