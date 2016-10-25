package function;

import java.io.PrintWriter;

import org.jfree.data.xy.XYSeries;

import model.*;


public class RunGrowthModel {
	
	public GrowthRes all_days_curve (CurrentTime ct, Location lct, Environment env, int day_start, int day_end, int day_step, Weather weather, PrintWriter pw0){
		
		/*
		 * all_day_curve:
		 * calcualte all the properties of plant growth at a series of days
		 */

		GrowthRes gr = new GrowthRes();
		Plant plant = new Plant();
		
		// for cumulative varible
		double elapsedAssimilation = 0;
		double cumulativeE = 0;
		double cumulativeRd = 0;
		double cumulativeRainFall = 0;
		
		for (int d = day_start; d<=day_end; d+=day_step) {
        	ct.day = d;
        	plant.runDailyPlantProcess(ct, lct, env, weather, pw0);
        	
        	// Tab 1  
        	gr.xys_NetAssiRate.add(d, plant.daily_carbon_uptake);   // mol CO2/m2/day
        	elapsedAssimilation += plant.daily_carbon_uptake;      
        	gr.xys_ElapsedAssimilation.add(d, elapsedAssimilation); // mol CO2/m2
        	gr.xys_CanopyConductance.add(d,plant.canopy.cond);      // mol H2O/m2/day   ??? 
        	gr.xys_Evapo_transpiration.add(d,plant.canopy.actual_canopy_E);  		            // mol H2O/m2/day
        	
        	cumulativeE += plant.canopy.actual_canopy_E;
        	gr.xys_CumulativeEvapoTranspiration.add(d,cumulativeE);  //
        	gr.xys_LAI.add(d, plant.canopy.LAI);

        	gr.xys_LeafWaterPotential.add(d, plant.leaf.leafWaterPotential);  //
        	gr.xys_Respiration.add(d,plant.canopy.Rd);     //
        	
        	cumulativeRd += plant.canopy.Rd;
        	gr.xys_CumulativeRespiration.add(d,cumulativeRd); //
        	gr.xys_WaterUseEfficiency.add(d,plant.canopy.canopyWaterUseEfficiency);  //
        	
        	
        	
        	// Tab 2 
        	gr.xys_AirTemp 				.add(d,env.air.current_T);   // should be every hour
        	
        	gr.xys_DayMeanTemp 				.add(d,env.air.avg_T);
        	
        	
        	gr.xys_DayHighTemp 				.add(d,env.air.max_T);
        	gr.xys_DayLowTemp 				.add(d,env.air.min_T);
        	
        	gr.xys_ElapsedThermalTime_day 				.add(d,Constants.elapsed_thermal_days);
        	gr.xys_ElapsedThermalTime_hour 				.add(d,Constants.elapsed_thermal_days*24);
        	gr.xys_RelativeHumidity 				.add(d,env.air.RH);
        	gr.xys_OzoneConcentration 				.add(d,env.air.O3);
        	gr.xys_DirectSunLight 				.add(d,env.light.direct_PPFD);
        	gr.xys_DiffuseSunLight 				.add(d,env.light.diffuse_PPFD);
        //	gr.xys_TotalRadiation 				.add(d,env.light.total_radiation);
        //	gr.xys_DayRadiation 				.add(d,env.light.total_radiation); // ??? check; total radiation... 
        	gr.xys_RainFall 				    .add(d,env.rain_fall);
        	
        	cumulativeRainFall += env.rain_fall;
        	gr.xys_CumulativeRainFall 			.add(d,cumulativeRainFall);    //
        	
        	
        	// Tab 3.1
        	gr.xys_leaf_dry_weight.add(d, plant.leaf.dry_weight);
        	gr.xys_stem_dry_weight.add(d, plant.stem.dry_weight);
        	gr.xys_standing_dead.add(d,plant.leaf.dead_on_stand);  // ...
        	gr.xys_surface_dead.add(d,plant.leaf.dead_on_ground_surface);  // ...
        	gr.xys_seed_dry_weight.add(d,plant.seed.dry_weight);
        	gr.xys_pod_dry_weight.add(d,plant.pod.dry_weight);
        	
        	
        	// Tab 3.2

        	gr.xys_s_root_dry_weight.add(d, plant.s_root.dry_weight);
        	gr.xys_f_root_dry_weight.add(d,plant.f_root.dry_weight);
        	
        	gr.xys_storage.add(d,plant.storage_organ.dry_weight);
        	gr.xys_root_dead.add(d,plant.f_root.dead_on_stand + plant.s_root.dead_on_stand);  // check 
        	


        }
		return gr;
	}

}










