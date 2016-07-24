
package gui;

/*
 * CreateConstantsFile.java
 * 
 * This class creates constants file for WIMOVAC
 * 
 * @author Dairui Chen
 */

import java.io.*; 
import java.util.Properties;

public class CreateConstantsFile {   
    public void setKeyValuePair(){
        try {
           Properties c=new Properties();
               
           c.setProperty("Leaf_Spectral_Imbalance","0.25");
           c.setProperty("Leaf_Reflectance","0.2");
           c.setProperty("Leaf_Curvature_Factor","0.7");
           c.setProperty("Leaf_DarkRespirationCoef","1.1");
           c.setProperty("Leaf_RespirationTemperatureResponse","0");
           c.setProperty("Canopy_Q10TemperatureResponse","1.85");
           c.setProperty("Leaf_Kco","404");
           c.setProperty("Leaf_Koo","278");
           c.setProperty("Leaf_Vcmaxo","90");
           c.setProperty("Leaf_EKc","79.43");
           c.setProperty("Leaf_EKo","36.38");
           c.setProperty("Leaf_EVcmax","65.33");
           c.setProperty("Leaf_EVomax","60.11");
           c.setProperty("Leaf_EJmax","43.54");
           c.setProperty("Leaf_ERd","46.39");
           c.setProperty("Leaf_JMaxo","170");
           c.setProperty("Leaf_Rate_tri_phos_util","23");
           c.setProperty("Use_Nitrogen_Factor_Flag", "0");
           c.setProperty("Leaf_Nitrogen_Vcmax_K3","35.7");
           c.setProperty("Leaf_Nitrogen_Vcmax_K4","12.4");
           c.setProperty("Leaf_Nitrogen_Jmax_K5","92.55");
           c.setProperty("Leaf_Nitrogen_Jmax_K6","13.85");
           c.setProperty("Leaf_Nitrogen_Rd_K7","0.775");
           c.setProperty("Leaf_Nitrogen_Rd_K8","-0.238");
           c.setProperty("Leaf_VcmaxnitrogenHarleyK1","-6.6");
           c.setProperty("Leaf_VcmaxnitrogenHarleyK2","52.8");
           c.setProperty("Leaf_VcmaxnitrogenHarleyK1_650","-9.6");
           c.setProperty("Leaf_VcmaxnitrogenHarleyK2_650","60");
           c.setProperty("Leaf_JmaxnitrogenHarleyK1","-4.6");
           c.setProperty("Leaf_JmaxnitrogenHarleyK2","98.1");
           c.setProperty("Leaf_TPUNitrogenHarleyK1","0.63");
           c.setProperty("Leaf_TPUNitrogenHarleyK2","5.13");
           c.setProperty("Leaf_NitrogenConcentration","2");
           c.setProperty("Leaf_Stomatal_conductance_flag","1");
           c.setProperty("Leaf_Stomatal_Coef_g0","19.148");
           c.setProperty("Leaf_Stomatal_Coef_g1","9.8465");
           c.setProperty("Leaf_Stomatal_Coef_g0_650","20");
           c.setProperty("Leaf_Stomatal_Coef_g1_650","11.35");
         // The above parameters are exactly the same as VB code
           
           
           c.setProperty("Leaf_Maximum_stomatal_conductance","1041");
           c.setProperty("Leaf_StomatalCoef1","25");
           c.setProperty("Leaf_StomatalCoef2","-0.004");
           c.setProperty("Leaf_StomatalWaterStressFactor","0");
           c.setProperty("Respiration_CalculationMethod","0");
           c.setProperty("Leaf_AtmosphericPressure","101");
           c.setProperty("Leaf_C4CO2ResponseSlope","0.22");
           c.setProperty("Leaf_C4MaxRubiscoCapacity","31.4");   
           c.setProperty("Leaf_C4LightResponseSlope","0.04");
           c.setProperty("Leaf_C4LeafRespirationRate","0.8");
           c.setProperty("Leaf_C4StomatalSlopeFactor","4.7824");
           c.setProperty("Leaf_C4StomatalInterceptFactor","32.061");
           c.setProperty("Leaf_C4CurvatureParam1","0.83");
           c.setProperty("Leaf_C4CurvatureParam2","0.93");
           
           File f1 = new File("ConstantsFile_leaf.csv"); 
           FileOutputStream fid1 = new FileOutputStream(f1);   
           c.store(fid1, "WIMOVAC ConstantsFile_leaf");  

           // canopy parameters
           Properties d=new Properties();
           d.setProperty("Atmospheric_Pressure","101");
           d.setProperty("Atmospheric_Transmittance","0.85");
           d.setProperty("Solar_Constant","2600");
           d.setProperty("TimeSolarNoon","13");
           d.setProperty("Std_Atmospheric_Pressure_Sea_Level","101.324");  
           d.setProperty("AnnMeanAirTemperature","10");
           d.setProperty("AmplitudeAnnualTemperatureChange","15");
           d.setProperty("AmplitudeDailyTemperatureChange","6");
           d.setProperty("MaximumDailyTemperatureChange","10");
           
           d.setProperty("StartDayTemperatureCycle","120");
           d.setProperty("TemperaturePeakHour","14");
           d.setProperty("LeafAreaIndex","3");
           d.setProperty("ExtinctionCalcSwitch","0");
           d.setProperty("HorizVerticalProjAreaRatio","1");

           d.setProperty("TranspirationUnitsFlag","0");
           d.setProperty("KarmansConstant","0.41");
           d.setProperty("WindSpeedAboveCanopy","2");
           d.setProperty("WindSpeedHeight","2");
           d.setProperty("ZetaRoughnessCoef","0.026");
           d.setProperty("ZetaMRoughnessCoef","0.13");
           d.setProperty("CanopyHeight","0.5");
           
           d.setProperty("FracSkyCoveredClouds","0.3");

           d.setProperty("SpecificHeatCapacityAir","1010");
           d.setProperty("SinkMomentum","0.77");
           d.setProperty("LeafWidth","0.04");
           d.setProperty("WindSpeedRoughnessLength","0.13");
           d.setProperty("WindSpeedHeightCoeff","0.77");  // not for sure
           d.setProperty("StefanBoltzmanConst","5.67E-8");
           d.setProperty("CanopyExtinctionCoef","0.8");
           d.setProperty("CanopyCalcultion","1");
           
           File f2 = new File("WIMOVAC_ConstantsFile_canopy.csv"); 
           FileOutputStream fid2 = new FileOutputStream(f2);   
           d.store(fid2, "WIMOVAC ConstantsFile_canopy");
           
           //Leaf aux for plant growth
           Properties e=new Properties();
           e.setProperty("PartitioningModelSwitch","2");
           e.setProperty("DegDayEndStage1","185");
           e.setProperty("DegDayEndStage2","230");
           e.setProperty("DegDayEndStage3","260");
           e.setProperty("DegDayEndStage4","302");
           e.setProperty("DegDayEndStage5","591");
           e.setProperty("DegDayEndStage6","1048");
           e.setProperty("DegDayEndStage7","1453");
           e.setProperty("DegDayEndStage8","1614");
           e.setProperty("DegDayEndStage9","2109");
           e.setProperty("DegDayEndStage10","2286");
           e.setProperty("DegDayEndStage11","4000");
           e.setProperty("GrowthModelSwitch","0");           
           e.setProperty("DeadMaterialInBiomassCalcSwitch","0");
           e.setProperty("NumDevelopmentalStages","11");
           
    
           e.setProperty("MaxRUBPSatRateCarboxylation","90");
           e.setProperty("LightSatRateElectronTransport","170");
           e.setProperty("RateTrioseUse","23");
           e.setProperty("C4CO2ResponseSlope","0.22");
           e.setProperty("C4MaxRubiscoCapacity","31.4");
           e.setProperty("C4LightResponseSlope","0.04"); 
           e.setProperty("AnnMeanAirTemperature","10");
            
           e.setProperty("AmplitudeAnnualTemperatureChange","15");
           e.setProperty("AmplitudeDailyTemperatureChange","6");
           e.setProperty("StartDayTemperatureCycle","120");
           e.setProperty("MaximumDailyTemperatureRange","10");
           
           e.setProperty("TemperaturePeakHour","14");
           e.setProperty("PlantingDay","100");
           e.setProperty("GerminationDay","120");
           e.setProperty("EndGrowthCycle","0");//not sure value
           e.setProperty("MaxCanopyLAI","9");
           e.setProperty("SpecificLeafArea","50");
           
           e.setProperty("GrowthRespirationCoef","0.2");
           e.setProperty("LeafMaintenanceRespirationCoef","0.03");
           e.setProperty("StemMaintenanceRespirationCoef","0.015");
           e.setProperty("StructuralRootMaintenanceRespirationCoef","0.01");
           e.setProperty("StorageMaintenanceRespirationCoef","0.01");
           
           //Ageing and senescence for plant growth
    
           e.setProperty("AccelerationSwitch","0");
            
           File f3 = new File("WIMOVAC_ConstantsFile_plant.csv"); 
           FileOutputStream fid3 = new FileOutputStream(f3);   
           e.store(fid3, "WIMOVAC ConstantsFile_plant");                 
        } 
        catch (IOException e) { 
           // catch io errors from FileOutputStream 
           System.out.println("Uh oh, got an IOException error!" + e.getMessage()); 
        }
    }
    public static void main(String arg[]) {
        CreateConstantsFile cf=new CreateConstantsFile();
        cf.setKeyValuePair();
    }
}
