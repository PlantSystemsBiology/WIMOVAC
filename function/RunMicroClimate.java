package function;
import model.Canopy;
import model.CurrentTime;
import model.Environment;
import model.Leaf;
import model.Location;

public class RunMicroClimate {
	
	public MicroClimateRes all_time_curve (CurrentTime ct, Location lct, Environment env, double hour_start, double hour_end, double hour_step, double LAI_input){
		
		/*
		 * all_time_curve:
		 * calcualte all the properties of canopy at a series of time points
		 */
		
		MicroClimateRes mcr = new MicroClimateRes();
		
		for (double h=hour_start; h<=hour_end; h+=hour_step){
			ct.hour = h;													//change time
			env.update(ct, lct);											//update environment factors
			Leaf leaf = new Leaf(env);
			leaf.cal_c3(env);												//calculate the leaf properties Vcmax and Jmax that will be used for the canopy
			
			Canopy canopy = new Canopy();
                        canopy.LAI = LAI_input; 
			canopy.CanopyMicroClimateDriver(env, leaf);						//calculate the canopy micro-climate factors
		//	canopy.CanopyAssimilationDriver(env);							//calculate the canopy photosynthesis and transpiration
			
			mcr.xys_directPPFD		.add(h, env.light.direct_PPFD);			//add results to XYSeries in CanopyRes
			mcr.xys_diffusePPFD		.add(h, env.light.diffuse_PPFD);		// ...
		//	System.out.println("h: "+h);
		//	System.out.println("PPFD: "+canopy.sunlit_leaf.PPFD);
			mcr.xys_PPFD_sunlit		.add(h, canopy.sunlit_leaf.PPFD);
			
			mcr.xys_PPFD_shaded		.add(h, canopy.shaded_leaf.PPFD);
			mcr.xys_PPFD_scattered	.add(h, canopy.scatter_light_temp);
			
			mcr.xys_solar_zenith_agl.add(h, env.solar.ZenithAngle);              
			mcr.xys_LAI_sunlit		.add(h, canopy.sunlit_leaf.LAI); 
			
			mcr.xys_LAI_shaded		.add(h, canopy.shaded_leaf.LAI);
	//		mcr.xys_N_sunlit	.add(h, canopy.sunlit_leaf.N);                  // not included for now
	//		mcr.xys_N_shaded	.add(h, canopy.shaded_leaf.N);
			mcr.xys_T_air			.add(h, env.air.current_T);

		}
		return mcr;
	}

}
