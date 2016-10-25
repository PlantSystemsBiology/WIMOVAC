package model;

import gui.WIMOVAC;

import java.io.PrintWriter;
import java.util.ArrayList;


public class Plant{
	
	public Pod pod          = new Pod();
	public Leaf leaf        = new Leaf();
	public Stem stem        = new Stem();
	public StrucRoot s_root = new StrucRoot();
	public FineRoot  f_root = new FineRoot();
	public Seed seed        = new Seed();
	public StorageOrgan storage_organ = new StorageOrgan();
	public Canopy canopy    = new Canopy();
	
	int stage_of_development				= -1;    // -1: unplanted, 0: ungerminated, 1..13 ? 
	public double elapsed_thermal_days  	= 0;     // elapsed_thermal_days: thermal time used for determining growth stage
	ArrayList<Double> thermal_days_for_aging = new ArrayList<Double>(); // a list for store thermal hours data for every day
	double today_thermal_hours = 0;
	public double above_ground_dry_weight 	= 0;
	public double below_ground_dry_weight 	= 0;
	public double above_ground_stand_dead 	= 0;
	public double above_ground_surface_dead = 0;
	public double below_ground_stand_dead 	= 0;
	public double below_ground_surface_dead = 0;
	
	public double Frac_DeadLeafReabsorbed 	= 0.3;
	public double Frac_DeadPodReabsorbed 	= 0.5;
	public double Frac_DeadStemReabsorbed 	= 0;
	public double Frac_DeadSrootReabsorbed 	= 0;
	public double Frac_DeadFrootReabsorbed 	= 0;
	public double Frac_DeadStorageReabsorbed = 0;
	
	 public double Frac_DeadLeafGoesSurface 	= 0;
	 public double Frac_DeadPodGoesSurface 	= 0.2;
	 public double Frac_DeadStemGoesSurface 	= 0.1;
	 public double Frac_DeadSrootGoesSurface 	= 0;
	 public double Frac_DeadFrootGoesSurface 	= 0;
	 public double Frac_DeadStorageGoesSurface= 0;
	
	
	
	public double daily_carbon_uptake = 0; 			// the total canopy uptake , unit : mol.m-2.day-1
	public double sink_source = 0;         			// for relocation
	public double nitrogen_sink_source = 0; 		// for relocation
	public double total_nitrogen_demand = 0;        //
	public double reabsorbed_from_aged = 0;
    public double[] DegDayEndStageLocal;
        

    public boolean isCutDay = false;  // if true, the plant was cut today. 
    public int Day_cut1 = 0;  // time of cutting, cut 1. 
    public int Day_cut2 = 0;
    public int Day_cut3 = 0;
    public int Day_cut4 = 0;  // assume only 4 cuts per year. 
    
    private int growthDay = 1;
    
    public double a = 0.2, b_leaf = 0.03, b_stem=0.015, b_root=0.01;
    

	public Plant(){
		
		
		leaf.dry_weight = (double)Double.valueOf(WIMOVAC.constants.getProperty("Init_DW_leaf"));
		stem.dry_weight = (double)Double.valueOf(WIMOVAC.constants.getProperty("Init_DW_stem"));
		s_root.dry_weight = (double)Double.valueOf(WIMOVAC.constants.getProperty("Init_DW_s_root"));
		f_root.dry_weight = (double)Double.valueOf(WIMOVAC.constants.getProperty("Init_DW_f_root"));
		
		seed.dry_weight = 1; // seed has a default 
		seed.dry_weight = (double)Double.valueOf(WIMOVAC.constants.getProperty("Init_DW_seed"));
		
		storage_organ.dry_weight = (double)Double.valueOf(WIMOVAC.constants.getProperty("Init_DW_storage_organ"));
		
		
		canopy.LAI = 0.0001;
		canopy.shaded_leaf.LAI = 0.0001;
		canopy.sunlit_leaf.LAI = 0.0001;
		
		leaf.thermalTimeDeath = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafDeathThermalTime"));//1200;				// the thermal days of death for leaf
		stem.thermalTimeDeath = (double)Double.valueOf(WIMOVAC.constants.getProperty("StemDeathThermalTime"));//2500;				// raw parameter is hour, convert to day (QF)
		s_root.thermalTimeDeath = (double)Double.valueOf(WIMOVAC.constants.getProperty("SRootDeathThermalTime"));//3000;
		f_root.thermalTimeDeath = (double)Double.valueOf(WIMOVAC.constants.getProperty("FRootLeafDeathThermalTime"));//3000;
		pod.thermalTimeDeath = (double)Double.valueOf(WIMOVAC.constants.getProperty("PodDeathThermalTime"));//2000;
        DegDayEndStageLocal = new double[11];
        
    	Frac_DeadLeafReabsorbed 	= (double)Double.valueOf(WIMOVAC.constants.getProperty("Frac_DeadLeafReabsorbed"));
    	Frac_DeadPodReabsorbed 	    = (double)Double.valueOf(WIMOVAC.constants.getProperty("Frac_DeadPodReabsorbed"));
    	Frac_DeadStemReabsorbed 	= (double)Double.valueOf(WIMOVAC.constants.getProperty("Frac_DeadStemReabsorbed"));
    	Frac_DeadSrootReabsorbed 	= (double)Double.valueOf(WIMOVAC.constants.getProperty("Frac_DeadSrootReabsorbed"));
    	Frac_DeadFrootReabsorbed 	= (double)Double.valueOf(WIMOVAC.constants.getProperty("Frac_DeadFrootReabsorbed"));
    	Frac_DeadStorageReabsorbed = (double)Double.valueOf(WIMOVAC.constants.getProperty("Frac_DeadStorageReabsorbed"));
    	
    	Frac_DeadLeafGoesSurface 	= (double)Double.valueOf(WIMOVAC.constants.getProperty("Frac_DeadLeafGoesSurface"));
    	Frac_DeadPodGoesSurface 	= (double)Double.valueOf(WIMOVAC.constants.getProperty("Frac_DeadPodGoesSurface"));
    	Frac_DeadStemGoesSurface 	= (double)Double.valueOf(WIMOVAC.constants.getProperty("Frac_DeadStemGoesSurface"));
    	Frac_DeadSrootGoesSurface 	= (double)Double.valueOf(WIMOVAC.constants.getProperty("Frac_DeadSrootGoesSurface"));
    	Frac_DeadFrootGoesSurface 	= (double)Double.valueOf(WIMOVAC.constants.getProperty("Frac_DeadFrootGoesSurface"));
    	Frac_DeadStorageGoesSurface = (double)Double.valueOf(WIMOVAC.constants.getProperty("Frac_DeadStorageGoesSurface"));
    	
    	
    	Constants.LeafNperArea[0] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafNperArea_stage1"));
    	Constants.LeafNperArea[1] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafNperArea_stage2"));
    	Constants.LeafNperArea[2] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafNperArea_stage3"));
    	Constants.LeafNperArea[3] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafNperArea_stage4"));
    	Constants.LeafNperArea[4] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafNperArea_stage5"));
    	Constants.LeafNperArea[5] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafNperArea_stage6"));
    	Constants.LeafNperArea[6] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafNperArea_stage7"));
    	Constants.LeafNperArea[7] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafNperArea_stage8"));
    	Constants.LeafNperArea[8] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafNperArea_stage9"));
    	Constants.LeafNperArea[9] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafNperArea_stage10"));
    	Constants.LeafNperArea[10] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafNperArea_stage11"));
    	
    	Constants.LeafSenescenceRate[0] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafSenescenceRate_stage1"));
    	Constants.LeafSenescenceRate[1] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafSenescenceRate_stage2"));
    	Constants.LeafSenescenceRate[2] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafSenescenceRate_stage3"));
    	
    	Constants.LeafSenescenceRate[3] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafSenescenceRate_stage4"));
    	Constants.LeafSenescenceRate[4] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafSenescenceRate_stage5"));
    	Constants.LeafSenescenceRate[5] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafSenescenceRate_stage6"));
    	Constants.LeafSenescenceRate[6] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafSenescenceRate_stage7"));
    	Constants.LeafSenescenceRate[7] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafSenescenceRate_stage8"));
    	Constants.LeafSenescenceRate[8] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafSenescenceRate_stage9"));
    	Constants.LeafSenescenceRate[9] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafSenescenceRate_stage10"));
    	Constants.LeafSenescenceRate[10] = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafSenescenceRate_stage11"));
    	
    //  leaf   stem  sroot froot stor pod   seed
    	Constants.RelPartition[0][0] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage1_leaf"));
    	Constants.RelPartition[0][1] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage1_stem"));
    	Constants.RelPartition[0][2] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage1_sroot"));
    	Constants.RelPartition[0][3] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage1_froot"));
    	Constants.RelPartition[0][4] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage1_stor"));
    	Constants.RelPartition[0][5] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage1_pod"));
    	Constants.RelPartition[0][6] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage1_seed"));
    	
    	Constants.RelPartition[1][0] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage2_leaf"));
    	Constants.RelPartition[1][1] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage2_stem"));
    	Constants.RelPartition[1][2] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage2_sroot"));
    	Constants.RelPartition[1][3] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage2_froot"));
    	Constants.RelPartition[1][4] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage2_stor"));
    	Constants.RelPartition[1][5] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage2_pod"));
    	Constants.RelPartition[1][6] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage2_seed"));
    	
    	Constants.RelPartition[2][0] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage3_leaf"));
    	Constants.RelPartition[2][1] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage3_stem"));
    	Constants.RelPartition[2][2] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage3_sroot"));
    	Constants.RelPartition[2][3] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage3_froot"));
    	Constants.RelPartition[2][4] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage3_stor"));
    	Constants.RelPartition[2][5] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage3_pod"));
    	Constants.RelPartition[2][6] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage3_seed"));
    	
    	Constants.RelPartition[3][0] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage4_leaf"));
    	Constants.RelPartition[3][1] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage4_stem"));
    	Constants.RelPartition[3][2] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage4_sroot"));
    	Constants.RelPartition[3][3] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage4_froot"));
    	Constants.RelPartition[3][4] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage4_stor"));
    	Constants.RelPartition[3][5] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage4_pod"));
    	Constants.RelPartition[3][6] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage4_seed"));
    	
    	Constants.RelPartition[4][0] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage5_leaf"));
    	Constants.RelPartition[4][1] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage5_stem"));
    	Constants.RelPartition[4][2] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage5_sroot"));
    	Constants.RelPartition[4][3] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage5_froot"));
    	Constants.RelPartition[4][4] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage5_stor"));
    	Constants.RelPartition[4][5] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage5_pod"));
    	Constants.RelPartition[4][6] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage5_seed"));
    	
    	Constants.RelPartition[5][0] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage6_leaf"));
    	Constants.RelPartition[5][1] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage6_stem"));
    	Constants.RelPartition[5][2] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage6_sroot"));
    	Constants.RelPartition[5][3] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage6_froot"));
    	Constants.RelPartition[5][4] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage6_stor"));
    	Constants.RelPartition[5][5] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage6_pod"));
    	Constants.RelPartition[5][6] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage6_seed"));
    	
    	Constants.RelPartition[6][0] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage7_leaf"));
    	Constants.RelPartition[6][1] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage7_stem"));
    	Constants.RelPartition[6][2] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage7_sroot"));
    	Constants.RelPartition[6][3] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage7_froot"));
    	Constants.RelPartition[6][4] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage7_stor"));
    	Constants.RelPartition[6][5] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage7_pod"));
    	Constants.RelPartition[6][6] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage7_seed"));
    	
    	Constants.RelPartition[7][0] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage8_leaf"));
    	Constants.RelPartition[7][1] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage8_stem"));
    	Constants.RelPartition[7][2] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage8_sroot"));
    	Constants.RelPartition[7][3] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage8_froot"));
    	Constants.RelPartition[7][4] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage8_stor"));
    	Constants.RelPartition[7][5] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage8_pod"));
    	Constants.RelPartition[7][6] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage8_seed"));
    	
    	Constants.RelPartition[8][0] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage9_leaf"));
    	Constants.RelPartition[8][1] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage9_stem"));
    	Constants.RelPartition[8][2] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage9_sroot"));
    	Constants.RelPartition[8][3] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage9_froot"));
    	Constants.RelPartition[8][4] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage9_stor"));
    	Constants.RelPartition[8][5] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage9_pod"));
    	Constants.RelPartition[8][6] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage9_seed"));
    	
    	Constants.RelPartition[9][0] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage10_leaf"));
    	Constants.RelPartition[9][1] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage10_stem"));
    	Constants.RelPartition[9][2] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage10_sroot"));
    	Constants.RelPartition[9][3] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage10_froot"));
    	Constants.RelPartition[9][4] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage10_stor"));
    	Constants.RelPartition[9][5] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage10_pod"));
    	Constants.RelPartition[9][6] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage10_seed"));
    	
    	Constants.RelPartition[10][0] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage11_leaf"));
    	Constants.RelPartition[10][1] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage11_stem"));
    	Constants.RelPartition[10][2] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage11_sroot"));
    	Constants.RelPartition[10][3] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage11_froot"));
    	Constants.RelPartition[10][4] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage11_stor"));
    	Constants.RelPartition[10][5] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage11_pod"));
    	Constants.RelPartition[10][6] = (double)Double.valueOf(WIMOVAC.constants.getProperty("RelPartition_stage11_seed"));
    	
    	
    	
    	
        
	}

	public void runDailyPlantProcess (CurrentTime ct, Location lct, Environment env, Weather weather, PrintWriter pw0){
		/*
		 * a day of the plant, from uptaking CO2, partitioning, to growing biomass
		 */
			
	//	calDailyCarbonUptake (ct, lct, env);		//daily CO2 uptake of plant canopy
		
		calDailyCarbonUptake (ct, lct, env, weather, pw0);		//daily CO2 uptake of plant canopy
		System.out.println("DOY: "+ ct.day);
		System.out.println("calDailyCarbonUptake Finish!  .... "+daily_carbon_uptake);
		 
		if (growthDay<=5){  // set the first 5 days stage is 0, which will re-growing after cut. 
			growthDay += 1;
			stage_of_development = 0;
		}else{
			calGrowthStage(ct);					//determine stage
			System.out.println("stage: "+stage_of_development);
		}
		System.out.println("calGrowthStage       Finish!  .... "+stage_of_development);
		System.out.println("calDailyCarbonUptake Finish2!  .... "+daily_carbon_uptake);
		// *** Qingfeng change the aging before partitioning, because the reabsorbed C can be used for partitioning at the same day.  *** //
		
//		System.out.println("Stage: "+stage_of_development);
		
		
		//Respiration ... a * daily_carbon_uptake +  b_leaf * leaf.dry_weight + b_stem * stem.dry_weight  +
	//	double Rtotal =  b_root * (f_root.dry_weight + s_root.dry_weight); 
	//	System.out.println("leaf.dry_weight: "  +leaf.dry_weight);
	//	if(Rtotal<daily_carbon_uptake){
	//		daily_carbon_uptake = daily_carbon_uptake - Rtotal; 
	//	}else{
	//		daily_carbon_uptake = 0;
	//	}
		
		
		//Qingfeng change the aging before partitioning, because the reabsorbed C can be used for partitioning at the same day.
		if(Constants.LeafSenescenceModel == 0){
			calAgeing();             // Call the plant ageing and senescence driver
		}else if(Constants.LeafSenescenceModel == 1){
			calAgeing_leaf_senescence_rate(); // Using leaf senescence rate model
		}
		
		System.out.println("calAgeing            Finish!  .... "+stage_of_development);
		System.out.println("calDailyCarbonUptake Finish3!  .... "+daily_carbon_uptake);
		
		
		if(stage_of_development>=0){
			calPartitioning();					//partitioning
		
		updateBiomassPools();
		
		
		leaf.updateLA();      				// Use new assimilates assigned to leaf pool to grow new leaf material	    
  
		stem.updateStemLength();    		// Use new assimilates assigned to stem pool to grow new stem material
	     
		f_root.updateRootLength();     		// Use new assimilates assigned to fine root pool to grow new fine root material
	    
		s_root.updateRootLength();     		// Use new assimilates assigned to structural root pool to grow new structural root material
	    
		pod.updatePodNum();           		// update new assimilate assigned to pod to grow new pod
		
		canopy.updateCanopyStructure(stem, leaf);// Update the canopy height parameter now that the stem may have grown
		
		updateAboveBelowBiomassTotals();   	// only for getting a result 
		}
//		System.out.print("day="+ct.day+"; stage\t"+stage_of_development+"; dCarbup="+String.format("%1$.2f",daily_carbon_uptake)+"; LAI="+String.format("%1$.2f",canopy.LAI));

	}
	
	
	public void calDailyCarbonUptake (CurrentTime ct, Location lct, Environment env, Weather weather, PrintWriter pw0){
		
		/*
		 * total Ac' for a whole day
		 */
		
	//	Environment env = new Environment();
		
		today_thermal_hours = 0;
		daily_carbon_uptake = 0;
		double interval_h = 1; //hour
		for (double h = 0.5*interval_h ; h<24; h+=interval_h){      		//QF set, the time point selected are the mid-point of each interval. 
			
			ct.hour = h;
			env.update(ct, lct);  									//set env
			
			pw0.println(ct.day+","+ct.hour+","+env.air.current_T+","+env.light.direct_PPFD+","+env.light.diffuse_PPFD);  

			// if use weather input data, here re set part of the environment data. 
			if (Constants.use_weather_data){
							
					env.air.current_T = 0.5* ( weather.Temperature[(int)(ct.day * 24 + ct.hour - 0.5)] + weather.Temperature[(int)(ct.day * 24 + ct.hour + 0.5)] );
					env.light.total_radiation = 0.5 * (weather.PPFD[(int)(ct.day * 24 + ct.hour - 0.5)] + weather.PPFD[(int)(ct.day * 24 + ct.hour + 0.5)] );
					env.light.direct_PPFD = env.light.total_radiation * 0.7;
					env.light.diffuse_PPFD = env.light.total_radiation * 0.3; // assume half diret and half diffuse light. 
					env.air.RH = 0.5* ( weather.RH[(int)(ct.day * 24 + ct.hour - 0.5)] + weather.RH[(int)(ct.day * 24 + ct.hour + 0.5)] );
			
			}
						
			//	leaf.T = 25; // TEMP
			leaf.T = env.air.current_T;  									//set leaf T
			
			if("C3".equals(Constants.C3orC4)){
				leaf.cal_c3(env);
			}
			else if("C4".equals(Constants.C3orC4)){
				
				leaf.cal_c4_Von(env);
				
			}
			
			            									//calcualte the Vcmax and Jmax of leaf
			
			canopy.CanopyMicroClimateDriver(env, leaf);  					//calculate canopy micro-climate and use leaf 's Vcmax and Jmax for SunShade
			canopy.CanopyAssimilationDriver(env);
			daily_carbon_uptake += canopy.CO2_uptake_rate * interval_h * 3600 * 1e-6; 	//add up 24 times of umol.m-2.h-1 to give a umol.m-2.day-1 and convert to mol.m-2.day-1
			
			// here Qingfeng assume it is for 25 degree. 
						double Nleaf = 170; //unit: g/m2
						if (stage_of_development>=0){
								Nleaf = Constants.LeafNperArea[stage_of_development];//Constants.LeafNleafarea_Festulolium[Constants.stage_of_development];
						}
						double Rd  = 31.355* Nleaf/1000 - 5.088;   // unit: Nleaf is g/m2
						
						// Do not need the following two line, because the leaf Rd was calculated in the leaf model based on leaf N. 
						double daily_respiration_cost = canopy.LAI * Rd * interval_h * 3600 *1e-6;  // canopy respiraiton cost per day. mol.m-2.day-1
						
					//	System.out.println("daily respiration cost: "+daily_respiration_cost);
						
						daily_carbon_uptake -= daily_respiration_cost;
						//		if (isCut)
			
		//	if (seed.isGerminated)
				if(env.air.current_T > 10)
					today_thermal_hours += env.air.current_T * interval_h;  	// unit is: temperature * hour
						
		}
		
		elapsed_thermal_days +=  today_thermal_hours / 24;          		// unit is: temperature * day
	//	System.out.println("themalDays: " + elapsed_thermal_days);
		
	}
	//
	
	
	public void calGrowthStage (CurrentTime ct){
		
		/*
		 * calculate the growth stage by comparing the thermal days that the plant accumulated and required thermal days for a stage
		 */
		if(stage_of_development == -2){
		seed.DeterminePlanting(ct);											//determine if plantting
		if(seed.isPlantted) {
			stage_of_development = -1;   		//QF set, -1 is planted
			return;
		}
		return; 
		}
		if(stage_of_development == -1){
		seed.determineGermination(ct);										//determine if germinating
		if (seed.isGerminated) {
			stage_of_development = 0;   		//QF set, 0 is germinated stage
			return;
		}
		return;
		}
		
		//seed is germinated, then determine the growth stage
               // System.out.println("songq:"+WIMOVAC.constants.getProperty("DegDayEndStage1"));
                DegDayEndStageLocal[0]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage1"));
                DegDayEndStageLocal[1]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage2"));
                DegDayEndStageLocal[2]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage3"));
                DegDayEndStageLocal[3]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage4"));
                DegDayEndStageLocal[4]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage5"));
                DegDayEndStageLocal[5]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage6"));
                DegDayEndStageLocal[6]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage7"));
                DegDayEndStageLocal[7]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage8"));
                DegDayEndStageLocal[8]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage9"));
                DegDayEndStageLocal[9]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage10"));
                DegDayEndStageLocal[10]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage11"));
                
		int num_stages_maximal = Constants.DegDayEndStages.length;
		double num_stages = (double)Double.valueOf(WIMOVAC.constants.getProperty("TotalNumGrowingStages"));
		
		if (num_stages>num_stages_maximal){
			num_stages = num_stages_maximal;
		}
		System.out.println("elapsed_thermal_days: "+elapsed_thermal_days);
		for (int i=0; i<num_stages; i++)
			if (elapsed_thermal_days <= DegDayEndStageLocal[i]){
				stage_of_development = i+1;   	//QF set, the first value is the end day of first stage
				Constants.stage = stage_of_development;
                             //   
				return; 
			}
//		// If we are dealing with the simple or perennial growth models then the only other growth phase is a fallow one
//        // however if we are dealing with the annual model the next growth phase would be post grain fill. We need to
//        // ensure that the two models are treated differently here.
//		switch (Integer.valueOf(WIMOVAC.constants.getProperty("GrowthModelSwitch"))) {  //=1, final in constant
//        case 0:  //Simple model
//            if (stage_of_development!= 14)
//            	stage_of_development = 14;
//            break;
//        case 1:
//            // Annual growth model
//            if (stage_of_development != 13)
//                stage_of_development = 13;
//            break;
//        case 2:
//            // Perennial growth model
//            if (stage_of_development != 14)
//            	stage_of_development = 14;
//            break;
//		}
	}
	//
	
	
	
	private void calPartitioning() {
		
		/*
		 * calculate the partitioning, choose partitioning method :
		 * (a) partitioning_method_1
		 * (b) ...
		 */
		
		//load the parameters values from Constants to the objects
		leaf.load_component(0);
		stem.load_component(1);
		s_root.load_component(2);
		f_root.load_component(3);
		storage_organ.load_component(4);
		pod.load_component(5);
		seed.load_component(6);
		
		// This is the main plant partitioning driver subroutine which is responsible for calling all functions and
		//subroutines necessary to partition assimilates throughout a model plant canopy.
		if(leaf.dry_weight == 0 && canopy.LAI < 0.001){
			
			//when plant is very small
		}else{
		}
		partitioning_method_1(); 			//Partitions up the days assimilates	    

	}
	
	private void updateBiomassPools() {

		/*
		 * update the biomass of each organ
		 */

		/* The plant growth driver routine that calls this subroutine is initialised after the photosynthesis, respiration, and
		' ageing routines so that the DryWeightInInterval variables contain the delta (change) in carbon over the interval.
		' We now need to update the plant structure pools using these delta quantities which may be either positive or
		' negative depending on wether carbon from sources partitioned, from photosynthesis and from reabsorption of dead leaf
		' material are greater than the C lost to dead material and to respiration.
		 */

		  // The change in dry weight during a given interval may be positive or negative depending
		  // on growth or senescence of the plant.
		
		// add this day biomass into the dailyBMList
		leaf			.dailyBMList.add( leaf		.partition_weight * leaf	.total_conv_eff);
		stem			.dailyBMList.add( stem		.partition_weight * stem	.total_conv_eff);
		s_root			.dailyBMList.add( s_root	.partition_weight * s_root	.total_conv_eff);
		f_root			.dailyBMList.add( f_root	.partition_weight * f_root	.total_conv_eff);
		seed			.dailyBMList.add( seed		.partition_weight * seed	.total_conv_eff);
		pod				.dailyBMList.add( pod		.partition_weight * pod		.total_conv_eff);
		storage_organ   .dailyBMList.add( storage_organ.partition_weight * storage_organ.total_conv_eff);
		
		// for aging calculation
		for (int i = 0; i < thermal_days_for_aging.size(); i++)
			thermal_days_for_aging.set(i, thermal_days_for_aging.get(i) + today_thermal_hours / 24);
		
		thermal_days_for_aging.add(0.0); 									// thermal days for the BM synthesis in this day (QF: need to check)

		
		leaf			.dry_weight += leaf		.partition_weight * leaf	.total_conv_eff;
		stem			.dry_weight += stem		.partition_weight * stem	.total_conv_eff;
		s_root			.dry_weight += s_root	.partition_weight * s_root	.total_conv_eff;
		f_root			.dry_weight += f_root	.partition_weight * f_root	.total_conv_eff;
		seed			.dry_weight += seed		.partition_weight * seed	.total_conv_eff;
		pod				.dry_weight += pod		.partition_weight * pod		.total_conv_eff;
		storage_organ   .dry_weight += storage_organ.partition_weight * storage_organ.total_conv_eff;

		//Having now updated the biomass pools we need to ensure that none of the pools have obtained a negative value
		 // small negative biomasses can occurr in the model due to fixed respiration values but obviously cannot occurr naturally.
		if (leaf	.dry_weight <0) leaf	.dry_weight = 0;
		if (stem	.dry_weight <0) stem	.dry_weight = 0;
		if (s_root	.dry_weight <0) s_root	.dry_weight = 0;
		if (f_root	.dry_weight <0) f_root	.dry_weight = 0;
		if (seed	.dry_weight <0) seed	.dry_weight = 0;
		if (pod		.dry_weight <0) pod		.dry_weight = 0;
		if (storage_organ.dry_weight <0) storage_organ.dry_weight = 0;

	}
	
	private void calAgeing_leaf_senescence_rate(){
		// this method is used for calculating leaf senescence dry weight
		if(stage_of_development>=0){
			double lsr = Constants.LeafSenescenceRate[stage_of_development];
		System.out.println("LSR: "+lsr);
			leaf.dead_on_ground_surface += leaf.dry_weight * lsr;  //add-up everyday dead leaf on ground
			leaf.dry_weight -= leaf.dry_weight * lsr; 
		}
		// delete leaf dry weight. 
	
	}
	
	private void calAgeing(){
		reabsorbed_from_aged = 0;
		for (int i = 0; i < (thermal_days_for_aging.size()); i++){
		//	System.out.println(thermal_days_for_aging.size());
			if(thermal_days_for_aging.get(i)>=leaf.thermalTimeDeath){		//leaf aging
				leaf.dry_weight 		-= leaf.dailyBMList.get(i);
				leaf.dead_on_stand 		+= leaf.dailyBMList.get(i) * (1-Frac_DeadLeafReabsorbed)*(1-Frac_DeadLeafGoesSurface);
				leaf.dead_on_ground_surface += leaf.dailyBMList.get(i) * (1-Frac_DeadLeafReabsorbed)*(Frac_DeadLeafGoesSurface);
				reabsorbed_from_aged	+= leaf.dailyBMList.get(i) * Frac_DeadLeafReabsorbed;
				leaf.retired_dry_weight += leaf.dailyBMList.get(i);
				leaf.dailyBMList.set(i, 0.0);        						//set the daily biomass list to 0 when this biomass aged
			}				
			if(thermal_days_for_aging.get(i)>=stem.thermalTimeDeath){		//stem aging
				stem.dry_weight 		-= stem.dailyBMList.get(i);
				stem.dead_on_stand 		+= stem.dailyBMList.get(i) * (1-Frac_DeadStemReabsorbed)*(1-Frac_DeadStemGoesSurface);
				stem.dead_on_ground_surface += stem.dailyBMList.get(i) * (1-Frac_DeadStemReabsorbed)*(Frac_DeadStemGoesSurface);
				reabsorbed_from_aged	+= stem.dailyBMList.get(i) * Frac_DeadStemReabsorbed;
				
				stem.retired_dry_weight += stem.dailyBMList.get(i);
				stem.dailyBMList.set(i, 0.0);
			}
			if(thermal_days_for_aging.get(i)>=s_root.thermalTimeDeath){		//s_root aging
				s_root.dry_weight 			-= s_root.dailyBMList.get(i);
				s_root.dead_on_stand 		+= s_root.dailyBMList.get(i) * (1-Frac_DeadSrootReabsorbed)*(1-Frac_DeadSrootGoesSurface);
				s_root.dead_on_ground_surface += s_root.dailyBMList.get(i) * (1-Frac_DeadSrootReabsorbed)*(Frac_DeadSrootGoesSurface);
				reabsorbed_from_aged	+= s_root.dailyBMList.get(i) * Frac_DeadSrootReabsorbed;
				
				s_root.retired_dry_weight 	+= s_root.dailyBMList.get(i);
				s_root.dailyBMList.set(i, 0.0);
			}
			if(thermal_days_for_aging.get(i)>=f_root.thermalTimeDeath){		//f_root aging
				f_root.dry_weight 			-= f_root.dailyBMList.get(i);
				f_root.dead_on_stand 		+= f_root.dailyBMList.get(i) * (1-Frac_DeadFrootReabsorbed)*(1-Frac_DeadFrootGoesSurface);
				f_root.dead_on_ground_surface += f_root.dailyBMList.get(i) * (1-Frac_DeadFrootReabsorbed)*(Frac_DeadFrootGoesSurface);
				reabsorbed_from_aged	+= f_root.dailyBMList.get(i) * Frac_DeadFrootReabsorbed;
				f_root.retired_dry_weight 	+= f_root.dailyBMList.get(i);
				f_root.dailyBMList.set(i, 0.0);
			}
			if(thermal_days_for_aging.get(i)>=pod.thermalTimeDeath){		//f_root aging
				pod.dry_weight 			-= pod.dailyBMList.get(i);
				pod.dead_on_stand 		+= pod.dailyBMList.get(i) * (1-Frac_DeadPodReabsorbed)*(1-Frac_DeadPodGoesSurface);
				pod.dead_on_ground_surface += pod.dailyBMList.get(i) * (1-Frac_DeadPodReabsorbed)*(Frac_DeadPodGoesSurface);
				reabsorbed_from_aged	+= pod.dailyBMList.get(i) * Frac_DeadPodReabsorbed;
				pod.retired_dry_weight 	+= pod.dailyBMList.get(i);
				pod.dailyBMList.set(i, 0.0);
			}
			if(thermal_days_for_aging.get(i)>=storage_organ.thermalTimeDeath){		//f_root aging
				storage_organ.dry_weight 			-= storage_organ.dailyBMList.get(i);
				storage_organ.dead_on_stand 		+= storage_organ.dailyBMList.get(i) * (1-Frac_DeadStorageReabsorbed)*(1-Frac_DeadStorageGoesSurface);
				storage_organ.dead_on_ground_surface+= storage_organ.dailyBMList.get(i) * (1-Frac_DeadStorageReabsorbed)*(Frac_DeadStorageGoesSurface);
				reabsorbed_from_aged	+= storage_organ.dailyBMList.get(i) * Frac_DeadStorageReabsorbed;
				storage_organ.retired_dry_weight 	+= storage_organ.dailyBMList.get(i);
				storage_organ.dailyBMList.set(i, 0.0);
			}

		}
	}
	

	private void partitioning_method_1() {
		/*
		 * one of partitioning method "partitioning method 1"
		 */


		/*
		 This subroutine examines the current thermal time (degree days) of the
		 simulation and uses the partitioning calendar to allocate the carbon
		 assimilated in a given interval to leaves, stem, roots etc.

		 It then uses the amount allocated to leaf developement in order to calculate the leaf area index.

		 This subroutine examines the partitioning coefficients for the current developmental stage if there are any
		 negative values we need to convert a fraction of the specified sink dry weight into dry matter available for plant growth.
		 * 
		 */     
		    double Cgain;
		        // We do have some leaves and so add any photosynthates as normal
	        Cgain = daily_carbon_uptake * 30; 
		       	// Convert from moles/m2/day to gdry weight/m2/day. Note 30 is molecular mass of CH20 generally considered equivalent to dry weight.
	        if(leaf.dry_weight == 0 && canopy.LAI < 0.001){
	        	Cgain = 0; // if there is no leaf, but canopy leaf area index is not zero, set CO2 uptake = 0;
			}
	        System.out.println("daily carbon uptake 2: "+daily_carbon_uptake);
	        
	        System.out.println("Cgain: "+Cgain);
	        
		    switch ((int)(double)Double.valueOf(Constants.PartitioningModelSwitch)) {  //=2
		        case 0:
		            // Do not allocate assimilated C to any plant structure
		            break;
		        case 1:
		            // Allocate all assimilate C to the plant leaves by setting the relative partitioning coefficient
		            // for leaves to 1 and for all other plant structures to 0
		            leaf			.partition_weight = Cgain;
		            stem			.partition_weight = 0;
		            s_root			.partition_weight = 0;
		            f_root			.partition_weight = 0;
		            storage_organ	.partition_weight = 0;
		            seed			.partition_weight = 0;
		            pod				.partition_weight = 0;
		            break;
		        case 2:
		            /*
		             Allocate Cgain to plant structures on the basis of the partitioning calendar principle
		                                                                                   
		             First check to see if we have reached the germination day. If we have not yet reached germination
		             then do not allow any reallocation from sinks to growth. If we have reached germination then check to
		             see if any of the partitioning coefficients negative. If there are negative coefficients then
		             reallocate sink material to growth Cgain in the appropriate proportions
		             * 
		            */
		        	sink_source = 0; // reset.
		            for (int n=0; n<7; n++){
		            	if (Constants.RelPartition[stage_of_development][n] < 0 ){
		            		double k = Constants.RelPartition[stage_of_development][n];
		            		switch (n){
		            		case 0://leaf
		            			leaf.re_allocate_weight = leaf.dry_weight * (-k);
		            			leaf.dry_weight         -= leaf.re_allocate_weight;
		            			sink_source += leaf.re_allocate_weight;
		            			nitrogen_sink_source += leaf.re_allocate_weight * leaf.comp_protein / 100;
		            			break;
		            		case 1: //stem
		            			stem.re_allocate_weight = stem.dry_weight * (-k);
		            			stem.dry_weight         -= stem.re_allocate_weight;
		            			sink_source += stem.re_allocate_weight;
		            			nitrogen_sink_source += stem.re_allocate_weight * stem.comp_protein / 100;
		            			break;
		            		case 2: //s_root
		            			s_root.re_allocate_weight = s_root.dry_weight * (-k);
		            			s_root.dry_weight         -= s_root.re_allocate_weight;
		            			sink_source += s_root.re_allocate_weight;
		            			nitrogen_sink_source += s_root.re_allocate_weight * s_root.comp_protein / 100;
		            			break;
		            		case 3: //f_root
		            			f_root.re_allocate_weight = f_root.dry_weight * (-k);
		            			f_root.dry_weight         -= f_root.re_allocate_weight;
		            			sink_source += f_root.re_allocate_weight;
		            			nitrogen_sink_source += f_root.re_allocate_weight * f_root.comp_protein / 100;
		            			break;
		            		case 4: //storage_organ
		            			storage_organ.re_allocate_weight = storage_organ.dry_weight * (-k);
		            			storage_organ.dry_weight         -= storage_organ.re_allocate_weight;
		            			sink_source += storage_organ.re_allocate_weight;
		            			nitrogen_sink_source += storage_organ.re_allocate_weight * storage_organ.comp_protein / 100;
		            			break;
		            		case 5: //pod
		            			pod.re_allocate_weight = pod.dry_weight * (-k);
		            			pod.dry_weight         -= pod.re_allocate_weight;
		            			sink_source += pod.re_allocate_weight;
		            			nitrogen_sink_source += pod.re_allocate_weight * pod.comp_protein / 100;
		            			break;
		            		case 6: //seed
		            			seed.re_allocate_weight = seed.dry_weight * (-k);
		            			seed.dry_weight         -= seed.re_allocate_weight;
		            			sink_source += seed.re_allocate_weight;
		            			nitrogen_sink_source += seed.re_allocate_weight * seed.comp_protein / 100;
		            			break;
		            		}//switch
		            	}//if
		            }//for
    
		           // Calculate the nitrogen uptake. As mentioned earlier, we assume that the nodule grows as a components of the
		           // fine root. Or in another word, we count the fine root as another name for nodule. The nitrogen uptake amount can be calculated
		           // based on the amount of fine root and the nitrogen uptake capacity
		           
		           double NUptake = f_root.dry_weight * Constants.k_N_uptakeCapcity;     // The NuptakeCapacity needs to be defined
		           
		           double totalN  = NUptake + nitrogen_sink_source;
		        
		           Cgain 	+= sink_source; // Add any dry matter coming from the sinks (reallocation) to any photosynthates
		        
		           Cgain 	-= NUptake * Constants.k_GlucosePerNitrogen;             // GlucosePerNitrogen needs to be defined
		        
		           Cgain   	+= reabsorbed_from_aged;

		           System.out.println("Cgain 2: "+Cgain);
		           
		     //      System.out.println("Cgain: \t"+Cgain+"\ttotalN: \t"+totalN);
		           // Here we need to develop another section to include the potential changes in the partitioning due to the
		           // demand of nitrogen.   For easy of code development, I will first assume that the fine root carried the function
		           // of nitrogen fixation in this code. In another word, we assume that the nodule formation occurs simultenously
		           // with the fine root.
		            
		           // First we need to calculate the amount of nitrogen potentially required for the new tissues.
		           
		           //original coefficient
		           	leaf			.partition_weight = Cgain * Constants.RelPartition[stage_of_development][0];
		            stem			.partition_weight = Cgain * Constants.RelPartition[stage_of_development][1];
		            s_root			.partition_weight = Cgain * Constants.RelPartition[stage_of_development][2];
		            f_root			.partition_weight = Cgain * Constants.RelPartition[stage_of_development][3];
		            storage_organ	.partition_weight = Cgain * Constants.RelPartition[stage_of_development][4];
		            pod				.partition_weight = Cgain * Constants.RelPartition[stage_of_development][5];
		            seed			.partition_weight = Cgain * Constants.RelPartition[stage_of_development][6];
		         
		   //         System.out.print("leaf:" +String.format("%1$.2f",leaf.partition_weight));
		   //         System.out.print("; stem:" +String.format("%1$.2f",stem.partition_weight)+"\t");
		   //         System.out.print("; s_root:" +String.format("%1$.2f",s_root.partition_weight)+"\t");
		   //         System.out.print("; f_root:" +String.format("%1$.2f",f_root.partition_weight)+"\t");
		   //         System.out.print("; storage:" +String.format("%1$.2f",storage_organ.partition_weight)+"\t");
		   //         System.out.println("; seed:" +String.format("%1$.2f",seed.partition_weight)+"\t");
		            
		            
		            //   System.out.println("Cgain\t"+Cgain+"\tRelPart\t"+Constants.RelPartition[stage_of_development][0]+"\tLeafPW0: \t"+leaf.partition_weight);
		          //calculate the general conversion efficiency from glucose to each organ material(include all)
		    		leaf			.calTotalConvEff();
		    		stem			.calTotalConvEff();
		    		f_root			.calTotalConvEff();
		    		s_root			.calTotalConvEff();
		    		seed			.calTotalConvEff();
		    		storage_organ	.calTotalConvEff();
		    		
		    	 //
		    		
		           calNitrogenDemand();      // This routine calculate the total amount of nitrogen required.
		           
		           
		           double RatioDemandSupply = total_nitrogen_demand / totalN; 
		           
		           
		           if (RatioDemandSupply < 1) {
		                // We need to decrease the amount of nitrogen uptake by nodule since it would be useless to do that now.
		        	 
		        	    leaf			.partition_weight = Cgain * Constants.RelPartition[stage_of_development][0];
			            stem			.partition_weight = Cgain * Constants.RelPartition[stage_of_development][1];
			            s_root			.partition_weight = Cgain * Constants.RelPartition[stage_of_development][2];
			            f_root			.partition_weight = Cgain * Constants.RelPartition[stage_of_development][3];
			            storage_organ	.partition_weight = Cgain * Constants.RelPartition[stage_of_development][4];
			            pod				.partition_weight = Cgain * Constants.RelPartition[stage_of_development][5];
			            seed			.partition_weight = Cgain * Constants.RelPartition[stage_of_development][6];
			       //     System.out.println("LeafPW1: \t"+leaf.partition_weight);
		            }
		           else {
		                // Now it seems like that the available nitrogen is not enough to provide nitrogen.
		                // First, we need to increase the amount of nitrogen fixation by a given amount
		                double NuptakeCapacity = 0;
		                double ModifyingFactor = 1 / RatioDemandSupply;
		                NUptake = f_root.dry_weight * NuptakeCapacity * ModifyingFactor;
		                Cgain += f_root.dry_weight * NuptakeCapacity * (1 - ModifyingFactor);
		                
		                // Now change the partitioning coefficient for all the tissues in this iteration
		                double Partition2Pod = Constants.RelPartition[stage_of_development][5] * ModifyingFactor;
		                double ThreshholdVeg = 0.9;
		                double temp = 1 - ThreshholdVeg;
		                
		                if (Partition2Pod > temp)
		                    Partition2Pod = temp;
		               
		                
		                if (stage_of_development > 9)
		                    Partition2Pod = 0;

		                double ModifyingFactor1 = (1 - Partition2Pod) / (1 - Constants.RelPartition[stage_of_development][5]);
		                
		                //  The negative partitioning coefficient does not matter at here.
		                
		                // Now, we need to know the partitioning coefficient for all others.
		                ModifyingFactor1 = 1;
		                Partition2Pod = Constants.RelPartition[stage_of_development][5] * ModifyingFactor1; // Needs to be deleted later on
		                
		              //after modified by nitrogen supply
		                leaf			.partition_weight = Cgain * Constants.RelPartition[stage_of_development][0] * ModifyingFactor1;
			            stem			.partition_weight = Cgain * Constants.RelPartition[stage_of_development][1] * ModifyingFactor1;
			            s_root			.partition_weight = Cgain * Constants.RelPartition[stage_of_development][2] * ModifyingFactor1;
			            f_root			.partition_weight = Cgain * Constants.RelPartition[stage_of_development][3] * ModifyingFactor1;
			            storage_organ	.partition_weight = Cgain * Constants.RelPartition[stage_of_development][4] * ModifyingFactor1;
			            pod				.partition_weight = Cgain * Constants.RelPartition[stage_of_development][5] * ModifyingFactor1;
			            seed			.partition_weight = Cgain * Constants.RelPartition[stage_of_development][6] * ModifyingFactor1;
		           }
		       //    System.out.println("LeafPW2: \t"+leaf.partition_weight);
		           break;
		    }
		}

	
	private void calNitrogenDemand(){
		
		/*
		 * calculate the nitrogen demand, used in the "partitioning method 1"
		 */

		//calculate the total nitrogen demand 
		total_nitrogen_demand 	= leaf.			partition_weight 	* leaf.			total_conv_eff * leaf.comp_protein
								+ stem.			partition_weight 	* stem.			total_conv_eff * stem.comp_protein
								+ f_root.		partition_weight 	* f_root.		total_conv_eff * f_root.comp_protein
								+ s_root.		partition_weight 	* s_root.		total_conv_eff * s_root.comp_protein
								+ seed.			partition_weight 	* seed.			total_conv_eff * seed.comp_protein
								+ storage_organ.partition_weight 	* storage_organ.total_conv_eff * storage_organ.comp_protein;
	}

	private void updateAboveBelowBiomassTotals() {
		
		/*
		 * update the above and below ground biomass by adding up the above (below) ground organ's biomass
		 */
		
		/*
		 This subroutine takes the amount of biomass in each category and totals them appropriately to
		 give the total above and below ground biomass for the plant canopy. Note that this is some
		 uncertainty here as to whether the dead material should be included as biomass.
		 Total above ground biomass is made up of the total of leaf, stem, standind dead,
		 surface litter dead and seed dry weights.
		 */
			above_ground_dry_weight = leaf	.dry_weight + stem	.dry_weight + seed			.dry_weight + pod.dry_weight;
			below_ground_dry_weight = s_root.dry_weight + f_root.dry_weight + storage_organ	.dry_weight;
			
		    if (Double.valueOf(WIMOVAC.constants.getProperty("DeadMaterialInBiomassCalcSwitch")) != 0) {
		        // The model parameter database indicates that the above ground standing and surface dead pools
		        // should be included in the above ground biomass calculations
		    	above_ground_dry_weight += (above_ground_stand_dead + above_ground_surface_dead);
		    	below_ground_dry_weight += (below_ground_stand_dead + below_ground_surface_dead);
		    }
	}

}
