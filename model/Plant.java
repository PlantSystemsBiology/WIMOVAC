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
	
//	public double elapsed_thermal_days  	= 0;     // elapsed_thermal_days: thermal time used for determining growth stage
	ArrayList<Double> thermal_days_for_aging = new ArrayList<Double>(); // a list for store thermal hours data for every day
	double today_thermal_hours = 0;
//	public double above_ground_dry_weight 	= 0;
//	public double below_ground_dry_weight 	= 0;
//	public double above_ground_stand_dead 	= 0;
//	public double above_ground_surface_dead = 0;
//	public double below_ground_stand_dead 	= 0;
//	public double below_ground_surface_dead = 0;
	
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

        

    public boolean isCutDay = false;  // if true, the plant was cut today. 
    public int Day_cut1 = 0;  // time of cutting, cut 1. 
    public int Day_cut2 = 0;
    public int Day_cut3 = 0;
    public int Day_cut4 = 0;  // assume only 4 cuts per year. 
    
    private int growthDay = 1;
    
    public double a = 0.2, b_leaf = 0.03, b_stem=0.015, b_root=0.01;
    
    private double interval_h;

	public Plant(){
		
		//load the parameters values from Constants to the objects
		leaf.load_component(0);
		stem.load_component(1);
		s_root.load_component(2);
		f_root.load_component(3);
		storage_organ.load_component(4);
		pod.load_component(5);
		seed.load_component(6);
				
        //calculate the general conversion efficiency from glucose to each organ material(include all)
  		leaf			.calTotalConvEff();
  		stem			.calTotalConvEff();
  		f_root			.calTotalConvEff();
  		s_root			.calTotalConvEff();
  		seed			.calTotalConvEff();
  		storage_organ	.calTotalConvEff();
  		
  		Constants.elapsed_thermal_days = 0;
  		
		leaf.dry_weight = (double)Double.valueOf(WIMOVAC.constants.getProperty("Init_DW_leaf"));
		stem.dry_weight = (double)Double.valueOf(WIMOVAC.constants.getProperty("Init_DW_stem"));
		s_root.dry_weight = (double)Double.valueOf(WIMOVAC.constants.getProperty("Init_DW_s_root"));
		f_root.dry_weight = (double)Double.valueOf(WIMOVAC.constants.getProperty("Init_DW_f_root"));
		
		seed.dry_weight = 1; // seed has a default 
		seed.dry_weight = (double)Double.valueOf(WIMOVAC.constants.getProperty("Init_DW_seed"));
		
		storage_organ.dry_weight = (double)Double.valueOf(WIMOVAC.constants.getProperty("Init_DW_storage_organ"));
		
		leaf.dead_on_ground_surface = 0;
		
		canopy.LAI = 0.0001;
		canopy.shaded_leaf.LAI = 0.0001;
		canopy.sunlit_leaf.LAI = 0.0001;
		
		leaf.thermalTimeDeath = (double)Double.valueOf(WIMOVAC.constants.getProperty("LeafDeathThermalTime"));//1200;				// the thermal days of death for leaf
		stem.thermalTimeDeath = (double)Double.valueOf(WIMOVAC.constants.getProperty("StemDeathThermalTime"));//2500;				// raw parameter is hour, convert to day (QF)
		s_root.thermalTimeDeath = (double)Double.valueOf(WIMOVAC.constants.getProperty("SRootDeathThermalTime"));//3000;
		f_root.thermalTimeDeath = (double)Double.valueOf(WIMOVAC.constants.getProperty("FRootLeafDeathThermalTime"));//3000;
		pod.thermalTimeDeath = (double)Double.valueOf(WIMOVAC.constants.getProperty("PodDeathThermalTime"));//2000;

        
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
    	
        Constants.DegDayEndStages[0]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage1"));
        Constants.DegDayEndStages[1]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage2"));
        Constants.DegDayEndStages[2]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage3"));
        Constants.DegDayEndStages[3]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage4"));
        Constants.DegDayEndStages[4]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage5"));
        Constants.DegDayEndStages[5]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage6"));
        Constants.DegDayEndStages[6]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage7"));
        Constants.DegDayEndStages[7]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage8"));
        Constants.DegDayEndStages[8]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage9"));
        Constants.DegDayEndStages[9]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage10"));
        Constants.DegDayEndStages[10]=(double)Double.valueOf(WIMOVAC.constants.getProperty("DegDayEndStage11"));
    	
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

		
		//** step 1 **//
		
		// part 1 //
		calDailyCarbonUptake (ct, lct, env, weather, pw0);		//daily CO2 uptake of plant canopy
		// part 2 //
		calRespiration();
		
//		System.out.println("DOY: "+ ct.day);
//		System.out.println("calDailyCarbonUptake Finish!  .... "+daily_carbon_uptake);
		
		
		
		    // ** step 2 ** //
		if (!Constants.growthFromSeed){ // if not grow from seed, use this. 
			if (growthDay<=5){  // set the first 5 days stage is 0, which will re-growing after cut. 
				growthDay += 1;
				Constants.stage = 0;
			}else{
				calGrowthStage(ct);					//determine stage
				
			}
		}else{
			calGrowthStage(ct);					//determine stage
		}
		
//		System.out.println("stage: "+Constants.stage);
		
//		System.out.println("calGrowthStage       Finish!  .... "+Constants.stage);
//		System.out.println("calDailyCarbonUptake Finish2!  .... "+daily_carbon_uptake);
		// *** Qingfeng change the aging before partitioning, because the reabsorbed C can be used for partitioning at the same day.  *** //

		

		    // ** step 3 ** //
		if(Constants.LeafSenescenceModel == 0){
			calAgeing();             // Call the plant ageing and senescence driver
		}else if(Constants.LeafSenescenceModel == 1){
			calAgeing_leaf_senescence_rate(); // Using leaf senescence rate model
		}
		
//		System.out.println("calAgeing            Finish!  .... "+Constants.stage);
//		System.out.println("calDailyCarbonUptake Finish3!  .... "+daily_carbon_uptake);
		
		
		
		    // ** step 4 ** //
		if(Constants.stage>=0){
			calPartitioning();	
		    
			
			// ** step 5 ** //
		updateBiomassPools();
		
		
		
			// ** step 6 ** //
		updateStructure();

		}
	}
	
	//** step 1 part 1 ** checked!//
	public void calDailyCarbonUptake (CurrentTime ct, Location lct, Environment env, Weather weather, PrintWriter pw0){
		
		/*
		 * total Ac' for a whole day
		 */
		
		// initializing 
		daily_carbon_uptake = 0;
		
		today_thermal_hours = 0;

		interval_h = 1; //unit: hour
		
		// run a loop for 24 hours
		for (double h = 0.5*interval_h ; h<24; h+=interval_h){      		//QF set, the time point selected are the mid-point of each interval. 
			
			// update environment
			ct.hour = h;
			env.update(ct, lct);  									//set env
			
			// output model prediction environment
			pw0.println(ct.day+","+ct.hour+","+env.air.current_T+","+env.light.direct_PPFD+","+env.light.diffuse_PPFD);  

			// if use weather input data, here re set part of the environment data. 
			if (Constants.use_weather_data){
							
				// ATTENDTION: the format of weather data should be fixed. 
				
					env.air.current_T = 0.5* ( weather.Temperature[(int)(ct.day * 24 + ct.hour - 0.5)] + weather.Temperature[(int)(ct.day * 24 + ct.hour + 0.5)] );
					env.light.total_radiation = 0.5 * (weather.PPFD[(int)(ct.day * 24 + ct.hour - 0.5)] + weather.PPFD[(int)(ct.day * 24 + ct.hour + 0.5)] );
					env.light.direct_PPFD = env.light.total_radiation * 0.7;
					env.light.diffuse_PPFD = env.light.total_radiation * 0.3; // assume half diret and half diffuse light. 
					env.air.RH = 0.5* ( weather.RH[(int)(ct.day * 24 + ct.hour - 0.5)] + weather.RH[(int)(ct.day * 24 + ct.hour + 0.5)] );
			
			}
						

			// set leaf temperature as current air temperature
			leaf.T = env.air.current_T;
			
			// run leaf model. 
			if("C3".equals(Constants.C3orC4)){
				leaf.cal_c3(env);
			}
			else if("C4".equals(Constants.C3orC4)){
				
				leaf.cal_c4_Von(env);
				
			}
			
			// run canopy micro climate model
			canopy.CanopyMicroClimateDriver(env, leaf);  					//calculate canopy micro-climate and use leaf 's Vcmax and Jmax for SunShade

			// run canopy assimilation model
			canopy.CanopyAssimilationDriver(env);
			
			// add up current hour carbon uptake. 
			daily_carbon_uptake += canopy.CO2_uptake_rate * interval_h * 3600 * 1e-6; 	//add up 24 times of umol.m-2.h-1 to give a umol.m-2.day-1 and convert to mol.m-2.day-1
			
				if(env.air.current_T > 10)
					today_thermal_hours += env.air.current_T * interval_h;  	// unit is: temperature * hour			
		}

	}
	
	// ** step1 part2 ** checked! ** //
	private void calRespiration (){
		
		// Assume it is for 25 degree. 
		double today_respiration = 0;
		
		for (double h = 0.5*interval_h ; h<24; h+=interval_h){
			
			double Nleaf = Constants.LeafNperArea[Constants.stage];

			double Rd  = 31.355* Nleaf/1000 - 5.088;   // unit: Nleaf is g/m2
					
		//	Rd = 0.4;
		//	System.out.println("Rd: "+Rd);
			
			double daily_respiration_cost = canopy.LAI * Rd * interval_h * 3600 *1e-6;  // canopy respiraiton cost per hour
					
			today_respiration += daily_respiration_cost;
		}
	//	System.out.println("today Rc: "+today_respiration);
		daily_carbon_uptake -= today_respiration;

	}

	
	
	// ** step 2 ** checked! //
	public void calGrowthStage (CurrentTime ct){
		
		/*
		 * calculate the growth stage by comparing the thermal days that the plant accumulated and required thermal days for a stage
		 */
		
		if(Constants.stage == -2){ // not planted
			
			// determination of planting! 
			seed.DeterminePlanting(ct);	
			
			if(seed.isPlantted && !seed.isGerminated) { // if planted, but not germinated
				Constants.stage = -1; // set stage to be -1. 
				return;
			}else{
				// Do Nothing, and directly go back to next day. 
				return;
			}
			
		}else if(Constants.stage == -1){
			
			//determine if germinating
			seed.determineGermination(ct);	
			
			if (seed.isGerminated) {
				Constants.stage = 0;   		//QF set, 0 is germinated stage
				return;
			}else{
				// Do Nothing, and directly go back to next day. 
				return;
			}
			
		}else{  // when stage >= 0
			
			// Set the total number of stages, read from GUI
			double num_stages = (double)Double.valueOf(WIMOVAC.constants.getProperty("TotalNumGrowingStages"));

			// if too big. set to maximal length of stages vector. 
			if (num_stages > Constants.DegDayEndStages.length){
				num_stages = Constants.DegDayEndStages.length;
			}

			// System.out.println("elapsed_thermal_days: "+elapsed_thermal_days);

			// update elapsed thermal days, by add up today thermal value.
			Constants.elapsed_thermal_days +=  today_thermal_hours / 24;          		// unit is: temperature * day
			
			// determine stage by DegDayEndStages. 
			for (int i=0; i<num_stages; i++){
				if (Constants.elapsed_thermal_days <= Constants.DegDayEndStages[i]){
					Constants.stage = i;   	//QF set, the first value is the end day of first stage
					return; 
				}
			}
			
		}
		
	}

	
	// ** step 3 ** Part 1 checked! //
	private void calAgeing(){
		
		//Calculation of Thermal Days list: a list of 'thermal days for aging',  
		for (int i = 0; i < thermal_days_for_aging.size(); i++)
			thermal_days_for_aging.set(i, thermal_days_for_aging.get(i) + today_thermal_hours / 24);
				
		thermal_days_for_aging.add(0.0); // add today, init as 0. will be added up in next day. 

		
		//Do Aging process based on the above Thermal Days list. 
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

	
	// ** step 3 ** Part 2 checked! //
	private void calAgeing_leaf_senescence_rate(){
		
		// this method is used for calculating leaf senescence dry weight
		double lsr;
		
		if(Constants.stage>=0){
			lsr = Constants.LeafSenescenceRate[Constants.stage];
		}else{
			lsr = 0;
		}

		
		leaf.dead_on_ground_surface += leaf.dry_weight * lsr;  //add-up everyday dead leaf on ground
		leaf.dry_weight -= leaf.dry_weight * lsr; 
	
	}

	
	// ** step 4 ** checked! //
	private void calPartitioning() {

		// This is the main plant partitioning driver subroutine which is
		// responsible for calling all functions and
		// subroutines necessary to partition assimilates throughout a model
		// plant canopy.

		/*
		 * This subroutine examines the current thermal time (degree days) of
		 * the simulation and uses the partitioning calendar to allocate the
		 * carbon assimilated in a given interval to leaves, stem, roots etc.
		 * 
		 * It then uses the amount allocated to leaf developement in order to
		 * calculate the leaf area index.
		 * 
		 * This subroutine examines the partitioning coefficients for the
		 * current developmental stage if there are any negative values we need
		 * to convert a fraction of the specified sink dry weight into dry
		 * matter available for plant growth.
		 */

		// calculation of Carbon gain, CO2 -> CH2O, unit: g
		double Cgain;
		// We do have some leaves and so add any photosynthates as normal
		Cgain = daily_carbon_uptake * 30;
		// Convert from moles/m2/day to gdry weight/m2/day. Note 30 is molecular
		// mass of CH20 generally considered equivalent to dry weight.
		if (leaf.dry_weight == 0 && canopy.LAI < 0.001) {
			Cgain = 0; // if there is no leaf, but canopy leaf area index is not
						// zero, set CO2 uptake = 0;
		}

//		System.out.println("daily carbon uptake 2: " + daily_carbon_uptake);

//		System.out.println("Cgain: " + Cgain);

		/*
		 * Allocate Cgain to plant structures on the basis of the partitioning
		 * calendar principle
		 * 
		 * First check to see if we have reached the germination day. If we have
		 * not yet reached germination then do not allow any reallocation from
		 * sinks to growth. If we have reached germination then check to see if
		 * any of the partitioning coefficients negative. If there are negative
		 * coefficients then reallocate sink material to growth Cgain in the
		 * appropriate proportions
		 */

		// calculation of reallocation from sink, both C and N.
		sink_source = 0; // initial value
		for (int n = 0; n < 7; n++) {
			if (Constants.RelPartition[Constants.stage][n] < 0) {
				double k = Constants.RelPartition[Constants.stage][n];
				switch (n) {
				case 0:// leaf
					leaf.re_allocate_weight = leaf.dry_weight * (-k);
					leaf.dry_weight -= leaf.re_allocate_weight;
					sink_source += leaf.re_allocate_weight;
					nitrogen_sink_source += leaf.re_allocate_weight
							* leaf.comp_protein / 100;
					break;
				case 1: // stem
					stem.re_allocate_weight = stem.dry_weight * (-k);
					stem.dry_weight -= stem.re_allocate_weight;
					sink_source += stem.re_allocate_weight;
					nitrogen_sink_source += stem.re_allocate_weight
							* stem.comp_protein / 100;
					break;
				case 2: // s_root
					s_root.re_allocate_weight = s_root.dry_weight * (-k);
					s_root.dry_weight -= s_root.re_allocate_weight;
					sink_source += s_root.re_allocate_weight;
					nitrogen_sink_source += s_root.re_allocate_weight
							* s_root.comp_protein / 100;
					break;
				case 3: // f_root
					f_root.re_allocate_weight = f_root.dry_weight * (-k);
					f_root.dry_weight -= f_root.re_allocate_weight;
					sink_source += f_root.re_allocate_weight;
					nitrogen_sink_source += f_root.re_allocate_weight
							* f_root.comp_protein / 100;
					break;
				case 4: // storage_organ
					storage_organ.re_allocate_weight = storage_organ.dry_weight
							* (-k);
					storage_organ.dry_weight -= storage_organ.re_allocate_weight;
					sink_source += storage_organ.re_allocate_weight;
					nitrogen_sink_source += storage_organ.re_allocate_weight
							* storage_organ.comp_protein / 100;
					break;
				case 5: // pod
					pod.re_allocate_weight = pod.dry_weight * (-k);
					pod.dry_weight -= pod.re_allocate_weight;
					sink_source += pod.re_allocate_weight;
					nitrogen_sink_source += pod.re_allocate_weight
							* pod.comp_protein / 100;
					break;
				case 6: // seed
					seed.re_allocate_weight = seed.dry_weight * (-k);
					seed.dry_weight -= seed.re_allocate_weight;
					sink_source += seed.re_allocate_weight;
					nitrogen_sink_source += seed.re_allocate_weight
							* seed.comp_protein / 100;
					break;
				}// switch
			}// if
		}// for

		// Carbon gain, add the carbon reallocated from sink.
		Cgain += sink_source; // Add any dry matter coming from the sinks
								// (reallocation) to any photosynthates

		// Carbon gain, add the carbon re-absorbed from dead materials.
		Cgain += reabsorbed_from_aged;

		// Calculate the nitrogen uptake. As mentioned earlier, we assume that
		// the nodule grows as a components of the
		// fine root. Or in another word, we count the fine root as another name
		// for nodule. The nitrogen uptake amount can be calculated
		// based on the amount of fine root and the nitrogen uptake capacity

		// N uptake
		double NUptake = f_root.dry_weight * Constants.k_N_uptakeCapcity; // The
																			// NuptakeCapacity
																			// needs
																			// to
																			// be
																			// defined

		// minus the C used for N uptake.
		Cgain -= NUptake * Constants.k_GlucosePerNitrogen; // GlucosePerNitrogen
															// needs to be
															// defined

		// N uptake + reallocated N.
		double totalN = NUptake + nitrogen_sink_source;

//		System.out.println("Cgain 2: " + Cgain);

		// calculate the total nitrogen demand
		total_nitrogen_demand = leaf.partition_weight * leaf.total_conv_eff
				* leaf.comp_protein + stem.partition_weight
				* stem.total_conv_eff * stem.comp_protein
				+ f_root.partition_weight * f_root.total_conv_eff
				* f_root.comp_protein + s_root.partition_weight
				* s_root.total_conv_eff * s_root.comp_protein
				+ seed.partition_weight * seed.total_conv_eff
				* seed.comp_protein + storage_organ.partition_weight
				* storage_organ.total_conv_eff * storage_organ.comp_protein;

		double RatioDemandSupply = total_nitrogen_demand / totalN;
		
		
		double ModifyingFactor1 = 1;
		
		
		if (RatioDemandSupply < 1) {
			
			// We need to decrease the amount of nitrogen uptake by nodule since
			// it would be useless to do that now.
			ModifyingFactor1 = 1;
			
		} else {
						
			// *** N supply modifying to partitioning coefficient ** //
			
/*			double NuptakeCapacity = 0;
			double ModifyingFactor = 1 / RatioDemandSupply;
			NUptake = f_root.dry_weight * NuptakeCapacity * ModifyingFactor;
			Cgain += f_root.dry_weight * NuptakeCapacity
					* (1 - ModifyingFactor);

			// Now change the partitioning coefficient for all the tissues in
			// this iteration
			double Partition2Pod = Constants.RelPartition[Constants.stage][5]
					* ModifyingFactor;
			double ThreshholdVeg = 0.9;
			double temp = 1 - ThreshholdVeg;

			if (Partition2Pod > temp)
				Partition2Pod = temp;

			if (Constants.stage > 9)
				Partition2Pod = 0;

			ModifyingFactor1 = (1 - Partition2Pod)
					/ (1 - Constants.RelPartition[Constants.stage][5]);*/
			
			// *** N supply modifying to partitioning coefficient ** //
		}			
			
		// finally, calculate the partition weight of each organ.
			leaf.partition_weight = Cgain
					* Constants.RelPartition[Constants.stage][0]
					* ModifyingFactor1;
			stem.partition_weight = Cgain
					* Constants.RelPartition[Constants.stage][1]
					* ModifyingFactor1;
			s_root.partition_weight = Cgain
					* Constants.RelPartition[Constants.stage][2]
					* ModifyingFactor1;
			f_root.partition_weight = Cgain
					* Constants.RelPartition[Constants.stage][3]
					* ModifyingFactor1;
			storage_organ.partition_weight = Cgain
					* Constants.RelPartition[Constants.stage][4]
					* ModifyingFactor1;
			pod.partition_weight = Cgain
					* Constants.RelPartition[Constants.stage][5]
					* ModifyingFactor1;
			seed.partition_weight = Cgain
					* Constants.RelPartition[Constants.stage][6]
					* ModifyingFactor1;
		

	}

	// ** step 5 ** checked! //
	private void updateBiomassPools() {

		/*
		 * update the biomass of each organ
		 */

		  // The change in dry weight during a given interval may be positive or negative depending
		  // on growth or senescence of the plant.
		
		// add this day biomass into the dailyBMList, which will be used for calculation of senencensed biomass
		
		leaf			.dailyBMList.add( leaf		.partition_weight * leaf	.total_conv_eff);
		stem			.dailyBMList.add( stem		.partition_weight * stem	.total_conv_eff);
		s_root			.dailyBMList.add( s_root	.partition_weight * s_root	.total_conv_eff);
		f_root			.dailyBMList.add( f_root	.partition_weight * f_root	.total_conv_eff);
		seed			.dailyBMList.add( seed		.partition_weight * seed	.total_conv_eff);
		pod				.dailyBMList.add( pod		.partition_weight * pod		.total_conv_eff);
		storage_organ   .dailyBMList.add(storage_organ.partition_weight*storage_organ.total_conv_eff);
		 
		
		
		// update dry weight. 
		leaf			.dry_weight += leaf		.partition_weight * leaf	.total_conv_eff;
		stem			.dry_weight += stem		.partition_weight * stem	.total_conv_eff;
		s_root			.dry_weight += s_root	.partition_weight * s_root	.total_conv_eff;
		f_root			.dry_weight += f_root	.partition_weight * f_root	.total_conv_eff;
		seed			.dry_weight += seed		.partition_weight * seed	.total_conv_eff;
		pod				.dry_weight += pod		.partition_weight * pod		.total_conv_eff;
		storage_organ   .dry_weight += storage_organ.partition_weight * storage_organ.total_conv_eff;

		 
		//If negative value, set to be 0. 
		 // small negative biomasses can occurr in the model due to fixed respiration values but obviously cannot occurr naturally.
		if (leaf	.dry_weight <0) leaf	.dry_weight = 0;
		if (stem	.dry_weight <0) stem	.dry_weight = 0;
		if (s_root	.dry_weight <0) s_root	.dry_weight = 0;
		if (f_root	.dry_weight <0) f_root	.dry_weight = 0;
		if (seed	.dry_weight <0) seed	.dry_weight = 0;
		if (pod		.dry_weight <0) pod		.dry_weight = 0;
		if (storage_organ.dry_weight <0) storage_organ.dry_weight = 0;

	}
	
	
	// ** step 6 ** checked!//
	private void updateStructure() {
		

		leaf.updateLA();      				// Use new assimilates assigned to leaf pool to grow new leaf material	    
  
		stem.updateStemLength();    		// Use new assimilates assigned to stem pool to grow new stem material
	     
		f_root.updateRootLength();     		// Use new assimilates assigned to fine root pool to grow new fine root material
	    
		s_root.updateRootLength();     		// Use new assimilates assigned to structural root pool to grow new structural root material
	    
		pod.updatePodNum();           		// update new assimilate assigned to pod to grow new pod
		
		canopy.updateCanopyStructure(stem, leaf);// Update the canopy height parameter now that the stem may have grown
		
		
	}

	
}
