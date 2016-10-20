package model;

public class Constants {
	/*
	 * Constant values and input parameters
	 * that do not change during the running
	 * 
	 */
	//Qingfeng add
	
	static public int stage = 0; 
	static public double [] LeafNperArea = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0}; 
	
	
	
	
	
//	static public double Frac_DeadLeafReabsorbed 	= 0.3;
//	static public double Frac_DeadPodReabsorbed 	= 0.5;
//	static public double Frac_DeadStemReabsorbed 	= 0;
//	static public double Frac_DeadSrootReabsorbed 	= 0;
//	static public double Frac_DeadFrootReabsorbed 	= 0;
//	static public double Frac_DeadStorageReabsorbed = 0;
//	
//	static public double Frac_DeadLeafGoesSurface 	= 0;
//	static public double Frac_DeadPodGoesSurface 	= 0.2;
//	static public double Frac_DeadStemGoesSurface 	= 0.1;
//	static public double Frac_DeadSrootGoesSurface 	= 0;
//	static public double Frac_DeadFrootGoesSurface 	= 0;
//	static public double Frac_DeadStorageGoesSurface= 0;
	
	// for the parameterFile class
	static public String leafFile="original";
	static public String canopyFile="original";
	static public String plantFile="original";
	
	static public String C3orC4 = "C3";
	static public boolean UseAQ = false;
	static public boolean runGrowthModel = false;
	
 	static public double RealGasConstant  = 8.314;
 	
 	public static double[] DryAirDensity={1.316, 1.292, 1.296, 1.246, 1.225, 1.204, 1.183, 1.164, 1.146, 1.128, 1.11};
        public static double[] LatentHeatVapour={2.513, 2.501, 2.489, 2.477, 2.465, 2.454, 2.442, 2.43, 2.418, 2.406, 2.394};
        public static double[] SlopeFactorS={0.25, 0.28, 0.32, 0.36, 0.4, 0.45, 0.51, 0.57, 0.63, 0.7, 0.78, 0.87, 0.96,
                                         1.07, 1.18, 1.3, 1.43, 1.57, 1.72, 1.89, 2.07, 2.26, 2.46, 2.68, 2.92, 3.17};
        public static double[] SatWaterVapConc={3.41, 3.93, 4.52, 5.19, 5.95, 6.79, 7.75, 8.82, 10.01, 11.35, 12.83, 14.48, 16.31, 18.34,
                                            20.58, 23.05, 25.78, 28.78, 32.07, 35.68, 39.63, 43.96, 48.67, 53.82, 59.41, 65.9};
    
	/*
	 * Time
	 */
	static public int    StartYear;
        static public int    FinishYear;
	static public double StartHour;
	static public double FinishHour;
	static public double IntervalHour;
	static public double StartDay;
	static public double FinishDay;
	static public double IntervalDay;
	
	static public double TimeSolarNoon                  		= 12; 
	
	/*
	 * Geometry of earth and solar
	 */
	static public double Latitude;
	static public double Longitude;
	static public final double SolarConstant                 			= 2600;
	static public double SolarRadiationMJ;
        static public double Tmax;
        static public double Tmin;
        static public double Rain;
        static public double NumWetDays;
	/*
	 * Environment factors
	 */
        static public double AtmosphericTransmittance              	= 0.85;
        static public double Atmospheric_Pressure           		= 101;
        static public final double Atmospheric_Pressure_sea_level         = 101.324;
        static public double AtmosphericCO2Conc;
        static public double AtmosphericO2Conc;
        static public double DailyPrecipitation;
        static public double CumulativePrecipitation;
        static public double RelativeHumidity;
        static public double VapourPressureDefecit;
    
        static public double ElapsedThermalTimeSinceGermination;
        static public double ElapsedThermalDaysSinceGermination;

        static public double TimeCorrectionFactor;
        static public double DayCumPhotonFlux;
        static public double DayCumThermalHours;
        static public double RunTotalPhotonFlux;
        static public double RunTotalThermalHours;
    
 //       static public double AnnMeanAirTemperature       			    = 10;
 //       static public double AmplitudeAnnualTemperatureChange    	    = 15;
 //       static public double AmplitudeDailyTemperatureChange    	    = 6;
 //       static public double MaximumDailyTemperatureChange      		= 10;
 //       static public double StartDayTemperatureCycle        			= 120;
 //       static public double TemperaturePeakHour        				= 14;
 //       static public double LeafAreaIndex        						= 3;
 //       static public int	 ExtinctionCalcSwitch        				= 1;        //QF we need to use different extinction coefficient for growth.  0: use default 0.8;  or  1: calcualte it depends on the canopy
 //       static public double HorizVerticalProjAreaRatio        			= 1;
        
        static public int    TranspirationUnitsFlag        				= 0;
        static public final double KarmansConstant        					= 0.41;
        static public double WindSpeedAboveCanopy        				= 2;
        static public double WindSpeedHeight        					= 2;
        static public double ZetaRoughnessCoef        					= 0.026;
        static public double ZetaMRoughnessCoef        					= 0.13;
        static public double CanopyHeight        						= 0.5;
        static public double FracSkyCoveredClouds        				= 0.3;
        static public final double SpecificHeatCapacityAir        			= 1010;
        static public double SinkMomentum        						= 0.77;
        static public double LeafWidth        							= 0.04;
        static public double WindSpeedRoughnessLength        			= 0.13;
        static public double WindSpeedHeightCoeff        				= 0.77;  // not for sure
        static public final double StefanBoltzmanConst        				= 5.67E-8;
        static public double CanopyExtinctionCoef       				= 0.8;      //a default 0.8
        static public double CanopyCalcultion        					= 1;
	/*
	 * leaf model
	 */

   //     static public double Spectral_Imbalance         	= 0.25; //
   //     static public double Leaf_Reflectance           	= 0.2; //
   //     static public double Curvature_Factor				= 0.7; // not used in the model. use a T depended equiation to calculate it. 
   //     static public double Kco  	= 404, 		Koo  		= 278; //
   //     static public double Vcmaxo							= 90;//
   //     static public double EKc	= 79.43, EKo			= 36.38;//
   //     static public double EVcmax = 65.33, EVomax	= 60.11, EJmax = 43.54;//
   //     static public double ERd							= 46.39;//
   //     static public double DarkRespirationCoef			= 1.1; //
   //     static public int    RespTemperatureResponse			= 1; //
   //     static public double JMaxo							= 170;// 
   //     static public double Rate_tri_phos_util				= 23;//
   //     static public double Nitrogen_Vcmax_K3				= 35.7;//
   //     static public double Nitrogen_Vcmax_K4				= 12.4;//
   //     static public double Nitrogen_Jmax_K5				= 92.55;//
   //     static public  double Nitrogen_Jmax_K6				= 13.85;//
   //     static public double  Nitrogen_Rd_K7					= 0.775;//
   //     static public  double Nitrogen_Rd_K8				= -0.238;//
   
        
        static public int Use_Nitrogen_Calculations			= 0;//			
    
//        static public double Q10_Coefficient				= 1.85; // ??? Canopy_Q10TemperatureResponse ???

//        static public int RespirationCalculationMethod      = 1;    // QF set 1, use respiration, this code not used. 
    
    
    //C4
    
    
 //       static public double CO2_Response_Slope   ;  
 //       static public double Max_Rubisco_Capacity ;
        static public double alpha = 0.7; //Qingfeng: the Theta is 0 now, which is error. 0.7 qingfeng assumed temp
//        static public double Leaf_Respiration_Rate;
//        static public double Stomatal_slope;
//        static public double Stomatal_intercept;
        static public double Theta = 0.7;  //Qingfeng: the Theta is 0 now, which is error. 0.7 qingfeng assumed temp
        static public double beta = 0.7;   //Qingfeng: the Theta is 0 now, which is error. 0.7 qingfeng assumed temp
    
    

        static public boolean canopycalculation  = false;  //will be changed when canopy calculation
//        static public double LeafPhotonFlux;

        static public double QuantumYield;
        static public double LightCompensationPoint;

        static public double Mesophyll_CO2_Conc;
        static public double GamaStar;
        static public double CurvatureFactor;

        static public double RateTrioseUse;    
    
        static public double StomatalConductanceCoefficientg0       =19.148;
        static public double StomatalConductanceCoefficientg1       =9.8465;    
        static public double StomatalConductanceCoefficientg0650    =20;
        static public double StomatalConductanceCoefficientg1650    =11.35;
        static public double MaxStomatalConductance                 =1041;
        static public double StomatalCoef1                          =25;
        static public double StomatalCoef2                          =-0.004;
        static public double StomatalCoef3;
        static public double MaxReductionStomatalConduction;
    
        static public double Rd;
        static public double C3DarkRespirationCoef;
        static public int    RespirationTemperatureResponse;
    
        static public double LeafTranspiration;
        static public double LeafWaterPotential; 
    
        static public int    WaterStressCalcFlag;
        static public double ThresholdWaterPotential;
        static public double WaterStressSensitivity;
        static public double gsWaterStressModifier                  =0;  //?? Leaf_StomatalWaterStressFactor ??
	
        static public double LightFractionNotAbsorbed;
        static public double SpectralImbalance;
        static public double LeafReflectance;
    
        static public double ActivationEnergyForKc;
        static public double ActivationEnergyForKo;
        static public double ActivationEnergyForVcmax;
        static public double ActivationEnergyForVomax;    
        static public double ActivationEnergyForJmax;
        static public double ActivationEnergyDarkRespiration;
    
        static public double LeafNitrogenConcentration              = 2; 
        static public int    UseNitrogenFactorsFlag           		= 1; // ?? Use_Nitrogen_Factor_Flag ??
        static public int 	 UseStomatalConductanceFlag             = 1;
        static public double VcmaxNitrogenConstantk3;
        static public double VcmaxNitrogenConstantk4;
        static public double JmaxNitrogenConstantk5;
        static public double JmaxNitrogenConstantk6;
        static public double RdNitrogenConstantk7;    
        static public double RdNitrogenConstantk8;
        static public double VcmaxnitrogenHarleyK1					= -6.6;
        static public double VcmaxnitrogenHarleyK2					= 52.8;
        static public double VcmaxnitrogenHarleyK1_650				= -9.6;
        static public double VcmaxnitrogenHarleyK2_650				= 60;
        static public double JmaxnitrogenHarleyK1					= -4.6;    
        static public double JmaxnitrogenHarleyK2					= 98.1;
        static public double TPUNitrogenHarleyK1					= 0.63;
        static public double TPUNitrogenHarleyK2					= 5.13;
    
        static public double C4CO2ResponseSlope                     =0.22;
        static public double C4MaxRubiscoCapacity                   =31.4;
        static public double C4LightResponseSlope                   =0.04;
        static public double C4LeafRespirationRate                  =0.8;
        static public double C4StomatalSlopeFactor                  =4.7824;    
        static public double C4StomatalInterceptFactor              =32.061;
        static public double C4CurvatureParam1                      =0.83;
        static public double C4CurvatureParam2                      =0.93;
    
        static public double Amax;    
        static public double Convexity;
        static public double AmaxCO2Response;
        static public double QuantumYieldCO2Response;
    
	/*
	 * canopy model (sunlit/shaded model)
	 */
        static public double CanopyMicroclimateTypeFlag;

        static public double CanopyWidth;
        static public double RowDistance;
        static public double InRowPlantSpacing;
        static public int 		RowAzimuth;
        static public double Sunlitvcmax;
        static public double Shadedvcmax;
        static public double SunlitJmax;
        static public double ShadedJmax;
        static public int 		UseSunShadeVcmax = 0;
        static public int 		UseSunShadeJmax  = 0;

        static public int 		InitialCanopyHeightSwitch  = 0; // 0: canopy height do not change; 1: update with stem increase
        static public int 		CanopyHeightCalcSwitch;
        static public double 	PlantingDensity            = 20; // plantting density (number of stems.m-2), it will be used for calculate canopy height  
	
	/*
	 * growth model
	 */
        // default value ** for most of plants: (this in future will extends to more crops)
        static public double ComponentOfOrgans[][] = { 
    	//leaf stem s_root f_root storage pod seed
    	{0.1,  0.05, 0.05, 0.1 ,0.05  ,0.15  ,0.15}, //ProteinComponent
    	{0.2,  0.2 , 0.2 , 0.2 ,0.25  ,0.2   ,0.2 }, //LipidComponent
    	{0.6,  0.65, 0.65, 0.6 ,0.6   ,0.55  ,0.55}, //CarbohydrateComponent
    	{0  ,  0   , 0   , 0   ,0     ,0     ,0   }, //LigninComponent
    	{0  ,  0   , 0   , 0   ,0     ,0     ,0   }, //OrganicAcidComponent
    	{0.1,  0.1 , 0.1 , 0.1 ,0.1   ,0.1   ,0.1 }  //Minerals
    };
    
        static public double RelPartition[][]    	= { 
    //  leaf   stem  sroot froot stor pod   seed
    	{0.0,  0.0, 0.0,  0.0, 0.0, 0,    0  }, // the first stage ( stage 0 )seed is -1, reallocation of seed to others
        {0.0,  0.0, 0.0,  0.00, 0.0, 0.0,  0.0 }, //2
        {0.0, 0.0,  0.0,  0.0, 0.0, 0.0,  0.0 }, //3
        {0.0, 0.0,  0.0,  0.0, 0.0, 0.0,  0.0 }, //4
        {0.0,  0.0, 0.0,  0.0, 0.0, 0.0,  0.0 }, //5
        {0.0, 0.0,  0.0, 0.0, 0.0, 0.0, 0.0 }, //6 changed of sroot from 0.36 to 0.35 
        {0.0, 0.0,  0.0,  0.0, 0.0, 0.0,  0.0 }, //7
        {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0 }, //8
        {0.0, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, //9
        {0.0,  0.0, 0.0, 0.0, 0.0, 0.0, 0.0}, //10
        {0.0,  0,    0.0, 0.0,  0.0, 0.0, 0.0}, //11
        {0.0,  0,    0.0, 0.0,  0.0, 0.0, 0.0}, //11 12
        {0.0,  0,    0.0, 0.0,  0.0, 0.0, 0.0}, //11 13
        {0.0,  0,    0.0, 0.0,  0.0, 0.0, 0.0}, //11 14 assume some with 11
        {0,0,0,0,0,0,0} };   //OK, partitioning coefficients of 14 stages for 7 organs 
//    static public double PartitionActual[][]    = new double[14][7]; 	//QF, actual partitioning coefficients of 14 stages for 7 organs, calcualted from RelPartition and modifier
    
        static public double k_N_uptakeCapcity   	= 0.05; 				//This is a guess. It needs to be modified   !!!!
        static public double k_GlucosePerNitrogen 	= 0.1;					// This needs to be modified later.          !!!!
    
        static public int InitialStemLengthSwitch;
        static public int InitialSRootLengthSwitch;
        static public int InitialFRootLengthSwitch;
        static public int StemGrowthModelType;
        static public int SRootGrowthModelType;                             // \
        static public int FRootGrowthModelType;                             // / QF, both these two are together 
        static public int PodGrowthModelType;
        static public int RootDistributionModelType;   
    
        static public int LeafAreaTemperatureLimitSwitch;
        static public int LeafAreaWaterPotentialSwitch;
    
        static public double MaxCanopyLAI  = 9;
        static public double SpecificLeafArea  = 50; //g/m2 
        static public double SpecificStemLength = 60;
        static public double SpecificFRootLength = 10;
        static public double SpecificSRootLength = 60;
        static public double SpecificPodNumber = 20;
    

        static public double GlucosetoProteinEff 	= 2.556;
        static public double GlucosetoCarboEff  	= 1.242;
        static public double GlucosetolipidEff 		= 3.106;
        static public double GlucosetoOrganicAcidEff= 0.929;
        static public double GlucosetoMineralsEff 	= 0.05; 
        static public double GlucosetoLigninEff 	= 2.174;
    
        static public double InitialLeafAreaMethod;
        static public double GerminationDay                     = 101;
	
        static public int DeadMaterialInBiomassCalcSwitch;
        static public int PlantingDay                           = 100;
        static public int[] DegDayEndStages                     = {185, 230, 260, 302, 591, 1048, 1453, 1614, 2109, 2286,  4000};//right
        static public int NumDevelopmentalStages;
    
        static public String DevelopmentalStageDescription[];  //[14]
        static public double DryWeightInInterval[];  //[7]
    
        static public double SugarPhosphatePool;
        static public double InorganicPhosphatePool;
        static public double StarchPool;
        static public double CytosolicSucrosePool;
        static public double VacuolarSucrosePool;    
        static public double ApoplasticSucrosePool;
        static public double LoadingSucrosePool;
        static public double UnloadingSucrosePool;
        static public double LabileSucrosePool;
        static public double SinkVacuolarSucrosePool;
        static public double GrowthSucrosePool;
    
        static public double Vals;
        static public double Vac;
        static public double Vur;
        static public double Kac;
        static public double Kca;
        static public double Kla;
        static public double Kal;
        static public double Kxt;
        static public double Ktx;
        static public double Kxc;
        static public double Kcv;
        static public double Kvc;
        static public double Krw;
        static public double Kwr;
        static public double Kur;
        static public double Kro;
        static public double Yg;
        static public double TransportParam;
        static public double MichaelisMentenk1;
        static public double MichaelisMentenk2;
        static public double MichaelisMentenk3;
        static public double MichaelisMentenk4;
        static public double Jox;
        static public double Jxo;
        static public double Jxt;
        static public double Jtx;
        static public double Jxc;
        static public double Jal;
        static public double Jla;
        static public double Jcv;
        static public double Jvc;
        static public double Jca;
        static public double Jac;
        static public double Jlu;
        static public double Jur;
        static public double Jrh;
        static public double Jhr;
        static public double Jro;
        static public double Jwr;
        static public double Jrw;
        static public double CarbonGain;
        static public double NUptake;
        static public double NReallocation;
        static public double TotalNDemand;
        static public double ThreshholdVeg; 
    
        static public double PartionActual[]; //[7]
        static public boolean RealPartition;
        static public double NuptakeCapcity;
        static public double GlucosePerNitrogen;   
        static public double PartitionAmount[]; //[7]    // This represent the temporary variable that can potentially be allocated to a tissue
        static public String ImageFileSourse;
        static public boolean ChangeImage;
    
	/*
	 * solar etc
	 */
    
    /*
     * soil
     */

    
        static public int Numsoillayers;
        static public int Soiltype;
        static public int IgnoreSoilTypeParam;
        static public int SoilTranspirationMethodFlag;
        static public int SoilWaterPotentialCalcFlag;
        static public double EvaporationModelMethod;
        static public double SoilClodSize;
        static public double SoilReflectanceCoefficient;
        static public double SoilTransmissionCoefficient;
        static public double LeafResistanceToWaterPassage;
        static public double SpeciesStomatalClosureParam;
        static public double MaxStomatalClosure;
        static public double RootRadius;
        static public double SoilDepth;
        static public double TotalRootDensity;
        static public double FieldCapacity;
        static public double CriticalValue;
        static public double WiltingValue;
        static public double LayerRootDensity[]=new double[25];
        static public double SoilBValue;
        static public double SoilAirEntryPotential; 
        static public double SaturatedConductivity; 
        static public int 	 SoilTemperatureCalcFlag;
        static public int 	 InitialSoilTemperatureFlag;
        static public double SoilLayerDepth;
        static public double SoilSurfaceRoughnessLength;
        static public double InitialPressureHead;
        static public double VanGenuchtensCoefAALPA;
        static public double VanGenuchtensCoefEN;
        static public double SaturatedhydraulicConductivity;
        static public double SoilThermalConductivityCoefb1;
        static public double SoilThermalConductivityCoefb2;
        static public double SoilThermalConductivityCoefb3;
        static public double ResidualWaterContentCoefThetar;
        static public double LayerPressureHead[]=new double[25];
        static public double SoilSolidFraction;
        static public double SoilOrganFraction;
        static public double Maxsurfacepool;
        static public double Surfacewater;

	
	/*
	 * etc
	 */
	
	/*
	 * model related
	 */
	static public int ModelType;
	static public int LeafGrowthModelType = 1;                          // QF set, 0: do not growth, 1: growth
	static public final int PartitioningModelSwitch = 2;                          // 0: do not growth, 1: only leaf growth, 2: 
	
	static public int FirstCalcResetFlag;
        static public int DailyCalcFlag;
	
	static public int DisplaySummaryTable;
        static public int SummaryTableGrowthRow;
        static public int SummaryTableMetRow;
        static public int SimulationStartDay = 100;
        static public int SimulationEndDay  = 300;
	
        static public int EndGrowthCycle;
        static final public int GrowthModelSwitch                             = 1; //germination 
}
