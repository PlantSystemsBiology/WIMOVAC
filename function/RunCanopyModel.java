package function;

import gui.WIMOVAC;
import model.Environment;
import model.*;

public class RunCanopyModel {
	
	public CanopyRes all_time_curve (CurrentTime ct, Location lct, Environment env, double hour_start, double hour_end, double hour_step){
		
		/*
		 * all_time_curve:
		 * calcualte all the properties of canopy at a series of time points
		 */
		
		CanopyRes cl = new CanopyRes();
		
		for (double h=hour_start; h<=hour_end; h+=hour_step){
			ct.hour = h;													//change time
			env.update(ct, lct);											//update environment factors
			Leaf leaf = new Leaf(env);
			leaf.cal_c3(env);												//calculate the leaf properties Vcmax and Jmax that will be used for the canopy
			
			Canopy canopy = new Canopy();
                        canopy.LAI = Double.valueOf(WIMOVAC.constants.getProperty("LeafAreaIndex"));
                        System.out.println("update LAI: "+canopy.LAI);
			canopy.CanopyMicroClimateDriver(env, leaf);						//calculate the canopy micro-climate factors
			canopy.CanopyAssimilationDriver(env);							//calculate the canopy photosynthesis and transpiration
			
			cl.xys_Ac			.add(h, canopy.CO2_uptake_rate);			//add results to XYSeries in CanopyRes
			cl.xys_Ac_sunlit	.add(h, canopy.sunlit_leaf.CO2_uptake_rate);// ...
			cl.xys_Ac_shaded	.add(h, canopy.shaded_leaf.CO2_uptake_rate);
			
			cl.xys_LAI_sunlit	.add(h, canopy.sunlit_leaf.LAI);
			cl.xys_LAI_shaded	.add(h, canopy.shaded_leaf.LAI);
			
	//		cl.xys_N_sunlit		.add(h, canopy.sunlit_leaf.N);              // not included for now
	//		cl.xys_N_shaded		.add(h, canopy.shaded_leaf.N); 
			
			cl.xys_conductance	.add(h, canopy.cond);
		
		}
		return cl;
	}
}


