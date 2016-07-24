
package gui;

/*
 * ParameterFile.java
 * 
 * This is the interface for parameter file  
 * 
 * @author Dairui Chen
 */

import  gui.WIMOVAC;

import java.awt.*;

import javax.swing.*;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.GroupLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.UIManager;
import javax.swing.border.Border;
import javax.swing.border.TitledBorder;

import model.Constants;

import java.io.*;
import java.util.*;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.FileOutputStream;


public class ParameterFile extends JPanel implements ActionListener{
    /**
	 * 
	 */
	private static final String const_file_all = "ParameterFile\\ConstantsFile.csv";
	
	private static final String const_file_leaf = "ParameterFile\\ConstantsFile_leaf.csv";
	private static final String update_file_leaf = "ParameterFile\\UPDATE_ConstantsFile_leaf.csv";
	
	private static final String const_file_canopy = "ParameterFile\\ConstantsFile_canopy.csv";
	private static final String update_file_canopy = "ParameterFile\\UPDATE_ConstantsFile_canopy.csv";
	
	private static final String const_file_plant ="ParameterFile\\ConstantsFile_plant.csv";
	private static final String update_file_plant ="ParameterFile\\UPDATE_ConstantsFile_plant.csv";
	
	
	private static final long serialVersionUID = 1L;
	private int index, p;  
    private LabelTextfieldGroup tf, tf_2;
    public static Properties parameterfile, parameterfile_2;
    private String[] parametername,updated;
    private double[] parametervalue;
    private JPanel center;
    private String type[]={" ", "Leaf Properties", "Canopy Properties", "Plant Growth Properties"};
    File f;
    
    ArrayList<String> Leafparameters = new ArrayList<String>();// values of parameters
    ArrayList<Double> Leafparameters_v = new ArrayList<Double>();
    

    ArrayList<String> CanopyLevelparameters = new ArrayList<String>();
    ArrayList<Double> CanopyLevelparameters_v = new ArrayList<Double>();
    

    ArrayList<String> PlantLevelparameters = new ArrayList<String>();
    ArrayList<Double> PlantLevelparameters_v = new ArrayList<Double>();
    
//    public String leafFile="original";
//    public String canopyFile="original";
//    public String plantFile="original";
    
    
    public ParameterFile(int i){
        index=i;
        parametername=new String[500];
        parametervalue=new double[500];
        updated=new String[500];
        
        Font f1=new Font("Times New Roman", Font.BOLD, 30);
        Font f2=new Font("Times New Roman", Font.BOLD, 20);

        Border loweredbevel=BorderFactory.createLoweredBevelBorder();
        Border raisedbevel=BorderFactory.createRaisedBevelBorder();
        
        JLabel title=new JLabel("Customer Designed Parameter File");
        title.setFont(f1);
       
        center=new JPanel(new FlowLayout());
        TitledBorder titledborder = BorderFactory.createTitledBorder(loweredbevel,type[i]);
        center.setBorder(titledborder);
        
        JScrollPane scrollable = new JScrollPane(center);
   
        JButton save=new JButton("   Save   ");
        save.setFont(f2);
        save.setBorder(raisedbevel);
        save.addActionListener(this);
        
        JButton newsave=new JButton("Load Saved Data");
        newsave.setFont(f2);
        newsave.setBorder(raisedbevel);
        newsave.addActionListener(this);
        
        JButton original=new JButton("Load Original Data");
        original.setFont(f2);
        original.setBorder(raisedbevel);
        original.addActionListener(this);
        
        try {
            parameterfile = new Properties();
            if(index==1)  {  f = new File(const_file_all);} 
            if(index==2)  {  f = new File(const_file_canopy);} 
            if(index==3)  {  f = new File(const_file_plant);} 
            FileInputStream fid = new FileInputStream(f); 
            parameterfile.load(fid); 
        }
        
        catch (IOException e) { 
           // catch io errors from FileInputStream 
           System.out.println("Uh oh, got an IOException error!" + e.getMessage()); 
        }
       
        if (index == 1){
        // QF set the classification of all leaf parameters. 

        
        // leaf optical parameters
        Leafparameters.add("Leaf_Spectral_Imbalance");
        Leafparameters.add("Leaf_Reflectance");
        
        // climate parameters
        Leafparameters.add("Leaf_AtmosphericPressure");

        // C3 photosynthesis parameters
        Leafparameters.add("Leaf_Kco");
        Leafparameters.add("Leaf_Koo");
        Leafparameters.add("Leaf_Vcmaxo");
        Leafparameters.add("Leaf_Vomaxo");
        Leafparameters.add("Leaf_JMaxo");
        Leafparameters.add("Leaf_Rate_tri_phos_util");
        Leafparameters.add("Leaf_Curvature_Factor");
        Leafparameters.add("Leaf_DarkRespirationCoef");
        
		// stomatal parameters
        Leafparameters.add("Leaf_Stomatal_conductance_flag");
        Leafparameters.add("Leaf_Stomatal_Coef_g0");
        Leafparameters.add("Leaf_Stomatal_Coef_g1");
        Leafparameters.add("Leaf_Stomatal_Coef_g0_650");
        Leafparameters.add("Leaf_Stomatal_Coef_g1_650");
		
		// C4 photosynthesis parameters
        Leafparameters.add("Leaf_C4Vpmax");
        Leafparameters.add("Leaf_C4Kp");
        Leafparameters.add("Leaf_C4Vcmax");
        Leafparameters.add("Leaf_C4Vpr");
        Leafparameters.add("Leaf_C4Jmax");
   //     Leafparameters.add("Leaf_C4MaxRubiscoCapacity");  // not used in C4 von model. 
        Leafparameters.add("Leaf_C4LeafRespirationRate");
        Leafparameters.add("Leaf_C4Ci2CsRatio");
		Leafparameters.add("Q10_Coefficient");
		
		// respiration parameters
        Leafparameters.add("Respiration_CalculationMethod");
        Leafparameters.add("Leaf_RespirationTemperatureResponse");

		
		// temperature response parameters
		
        Leafparameters.add("Leaf_EJmax");
        Leafparameters.add("Leaf_EKo");
        Leafparameters.add("Leaf_EVomax");
        Leafparameters.add("Leaf_EKc");
        Leafparameters.add("Leaf_EVcmax");
        Leafparameters.add("Leaf_ERd");
		Leafparameters.add("Leaf_EGstar");
		Leafparameters.add("Leaf_cVcmax");
		Leafparameters.add("Leaf_cJmax");
		Leafparameters.add("Leaf_cKo");
		Leafparameters.add("Leaf_cKc");
		Leafparameters.add("Leaf_cGstar");
		Leafparameters.add("Leaf_cRd");

		p = 0;
		
		for (int t=0;t<Leafparameters.size();t++){
			Leafparameters_v.add( Double.valueOf(parameterfile.getProperty(Leafparameters.get(t)) ));
			parametername[p] = Leafparameters.get(t);
			parametervalue[p] = Double.valueOf(parameterfile.getProperty(Leafparameters.get(t)));
			p++;
		}
		
		
        }
		if (index == 2){
			// QF set the classification of all canopy parameters. 
	        
	        // atmosphere pressure
	        CanopyLevelparameters.add("Atmospheric_Pressure");
	        CanopyLevelparameters.add("Atmospheric_Transmittance");
	        CanopyLevelparameters.add("FracSkyCoveredClouds");
	        
	        // solar time noon
	        CanopyLevelparameters.add("TimeSolarNoon");

	        // Wind speed
	        CanopyLevelparameters.add("WindSpeedAboveCanopy");
	        CanopyLevelparameters.add("WindSpeedRoughnessLength");
	        CanopyLevelparameters.add("WindSpeedHeight");
	        CanopyLevelparameters.add("WindSpeedHeightCoeff");
	        CanopyLevelparameters.add("ZetaRoughnessCoef");
	        CanopyLevelparameters.add("ZetaMRoughnessCoef");
	        CanopyLevelparameters.add("SinkMomentum");
	        
			// canopy height and leaf width
	        CanopyLevelparameters.add("CanopyHeight");
	        CanopyLevelparameters.add("LeafWidth");
	        
	        // flags
			CanopyLevelparameters.add("TranspirationUnitsFlag");
			CanopyLevelparameters.add("CanopyCalcultion");
			CanopyLevelparameters.add("ExtinctionCalcSwitch");
			
			// extinction of light
			CanopyLevelparameters.add("LeafAreaIndex");
			CanopyLevelparameters.add("HorizVerticalProjAreaRatio");
			CanopyLevelparameters.add("CanopyExtinctionCoef");
			
			// temperature calculation 
			CanopyLevelparameters.add("AnnMeanAirTemperature");
			CanopyLevelparameters.add("AmplitudeAnnualTemperatureChange");
			CanopyLevelparameters.add("AmplitudeDailyTemperatureChange");
			CanopyLevelparameters.add("MaximumDailyTemperatureChange");
			CanopyLevelparameters.add("StartDayTemperatureCycle");
			CanopyLevelparameters.add("TemperaturePeakHour");

			
			p = 0;
			
			for (int t=0;t<CanopyLevelparameters.size();t++){
				CanopyLevelparameters_v.add( Double.valueOf(parameterfile.getProperty(CanopyLevelparameters.get(t)) ));
				parametername[p] = CanopyLevelparameters.get(t);
				parametervalue[p] = Double.valueOf(parameterfile.getProperty(CanopyLevelparameters.get(t)));
				p++;
			}			
		}
		
		if (index == 3){
			// QF set the classification of all plant growth parameters. 
	        
	        // switch
	        PlantLevelparameters.add("DeadMaterialInBiomassCalcSwitch");
	        
	        // SLA
	        PlantLevelparameters.add("SpecificLeafArea");
	        
	        // time 
	        PlantLevelparameters.add("PlantingDay");
	        PlantLevelparameters.add("GerminationDay");

	        // max LAI
	        PlantLevelparameters.add("MaxCanopyLAI");
	        
	        //
	        PlantLevelparameters.add("TotalNumGrowingStages");
	        
	        // 
	        PlantLevelparameters.add("DegDayEndStage1");
	        PlantLevelparameters.add("DegDayEndStage2");
	        PlantLevelparameters.add("DegDayEndStage3");
	        PlantLevelparameters.add("DegDayEndStage4");
	        PlantLevelparameters.add("DegDayEndStage5");
	        PlantLevelparameters.add("DegDayEndStage6");
	        PlantLevelparameters.add("DegDayEndStage7");
	        PlantLevelparameters.add("DegDayEndStage8");
	        PlantLevelparameters.add("DegDayEndStage9");
	        PlantLevelparameters.add("DegDayEndStage10");
	        PlantLevelparameters.add("DegDayEndStage11");
	        
	        //
	        PlantLevelparameters.add("LeafDeathThermalTime");
	        PlantLevelparameters.add("StemDeathThermalTime");
	        PlantLevelparameters.add("SRootDeathThermalTime");
	        PlantLevelparameters.add("FRootLeafDeathThermalTime");
	        PlantLevelparameters.add("PodDeathThermalTime");

	        
			// Plant height and leaf width
	        PlantLevelparameters.add("GrowthRespirationCoef");
	        PlantLevelparameters.add("LeafMaintenanceRespirationCoef");
	        PlantLevelparameters.add("StemMaintenanceRespirationCoef");
			PlantLevelparameters.add("StructuralRootMaintenanceRespirationCoef");
			PlantLevelparameters.add("StorageMaintenanceRespirationCoef");
			
			//
			PlantLevelparameters.add("Frac_DeadLeafReabsorbed");
	        PlantLevelparameters.add("Frac_DeadPodReabsorbed");
	        PlantLevelparameters.add("Frac_DeadStemReabsorbed");
	        PlantLevelparameters.add("Frac_DeadSrootReabsorbed");
	        PlantLevelparameters.add("Frac_DeadFrootReabsorbed");
	        PlantLevelparameters.add("Frac_DeadStorageReabsorbed");
	        PlantLevelparameters.add("Frac_DeadLeafGoesSurface");
	        PlantLevelparameters.add("Frac_DeadPodGoesSurface");
	        PlantLevelparameters.add("Frac_DeadStemGoesSurface");
	        PlantLevelparameters.add("Frac_DeadSrootGoesSurface");
	        PlantLevelparameters.add("Frac_DeadFrootGoesSurface");
	        PlantLevelparameters.add("Frac_DeadStorageGoesSurface");
			
	        //stem  sroot froot stor pod   seed
	        PlantLevelparameters.add("RelPartition_stage1_leaf");
	        PlantLevelparameters.add("RelPartition_stage1_stem");
	        PlantLevelparameters.add("RelPartition_stage1_sroot");
	        PlantLevelparameters.add("RelPartition_stage1_froot");
	        PlantLevelparameters.add("RelPartition_stage1_stor");
	        PlantLevelparameters.add("RelPartition_stage1_pod");
	        PlantLevelparameters.add("RelPartition_stage1_seed");
	        
	        PlantLevelparameters.add("RelPartition_stage2_leaf");
	        PlantLevelparameters.add("RelPartition_stage2_stem");
	        PlantLevelparameters.add("RelPartition_stage2_sroot");
	        PlantLevelparameters.add("RelPartition_stage2_froot");
	        PlantLevelparameters.add("RelPartition_stage2_stor");
	        PlantLevelparameters.add("RelPartition_stage2_pod");
	        PlantLevelparameters.add("RelPartition_stage2_seed");
	        
	        PlantLevelparameters.add("RelPartition_stage3_leaf");
	        PlantLevelparameters.add("RelPartition_stage3_stem");
	        PlantLevelparameters.add("RelPartition_stage3_sroot");
	        PlantLevelparameters.add("RelPartition_stage3_froot");
	        PlantLevelparameters.add("RelPartition_stage3_stor");
	        PlantLevelparameters.add("RelPartition_stage3_pod");
	        PlantLevelparameters.add("RelPartition_stage3_seed");
	        
	        PlantLevelparameters.add("RelPartition_stage4_leaf");
	        PlantLevelparameters.add("RelPartition_stage4_stem");
	        PlantLevelparameters.add("RelPartition_stage4_sroot");
	        PlantLevelparameters.add("RelPartition_stage4_froot");
	        PlantLevelparameters.add("RelPartition_stage4_stor");
	        PlantLevelparameters.add("RelPartition_stage4_pod");
	        PlantLevelparameters.add("RelPartition_stage4_seed");
	        
	        PlantLevelparameters.add("RelPartition_stage5_leaf");
	        PlantLevelparameters.add("RelPartition_stage5_stem");
	        PlantLevelparameters.add("RelPartition_stage5_sroot");
	        PlantLevelparameters.add("RelPartition_stage5_froot");
	        PlantLevelparameters.add("RelPartition_stage5_stor");
	        PlantLevelparameters.add("RelPartition_stage5_pod");
	        PlantLevelparameters.add("RelPartition_stage5_seed");
	        
	        PlantLevelparameters.add("RelPartition_stage6_leaf");
	        PlantLevelparameters.add("RelPartition_stage6_stem");
	        PlantLevelparameters.add("RelPartition_stage6_sroot");
	        PlantLevelparameters.add("RelPartition_stage6_froot");
	        PlantLevelparameters.add("RelPartition_stage6_stor");
	        PlantLevelparameters.add("RelPartition_stage6_pod");
	        PlantLevelparameters.add("RelPartition_stage6_seed");
	        
	        PlantLevelparameters.add("RelPartition_stage7_leaf");
	        PlantLevelparameters.add("RelPartition_stage7_stem");
	        PlantLevelparameters.add("RelPartition_stage7_sroot");
	        PlantLevelparameters.add("RelPartition_stage7_froot");
	        PlantLevelparameters.add("RelPartition_stage7_stor");
	        PlantLevelparameters.add("RelPartition_stage7_pod");
	        PlantLevelparameters.add("RelPartition_stage7_seed");
	        
	        PlantLevelparameters.add("RelPartition_stage8_leaf");
	        PlantLevelparameters.add("RelPartition_stage8_stem");
	        PlantLevelparameters.add("RelPartition_stage8_sroot");
	        PlantLevelparameters.add("RelPartition_stage8_froot");
	        PlantLevelparameters.add("RelPartition_stage8_stor");
	        PlantLevelparameters.add("RelPartition_stage8_pod");
	        PlantLevelparameters.add("RelPartition_stage8_seed");
	        
	        PlantLevelparameters.add("RelPartition_stage9_leaf");
	        PlantLevelparameters.add("RelPartition_stage9_stem");
	        PlantLevelparameters.add("RelPartition_stage9_sroot");
	        PlantLevelparameters.add("RelPartition_stage9_froot");
	        PlantLevelparameters.add("RelPartition_stage9_stor");
	        PlantLevelparameters.add("RelPartition_stage9_pod");
	        PlantLevelparameters.add("RelPartition_stage9_seed");
	        
	        PlantLevelparameters.add("RelPartition_stage10_leaf");
	        PlantLevelparameters.add("RelPartition_stage10_stem");
	        PlantLevelparameters.add("RelPartition_stage10_sroot");
	        PlantLevelparameters.add("RelPartition_stage10_froot");
	        PlantLevelparameters.add("RelPartition_stage10_stor");
	        PlantLevelparameters.add("RelPartition_stage10_pod");
	        PlantLevelparameters.add("RelPartition_stage10_seed");
	        
	        PlantLevelparameters.add("RelPartition_stage11_leaf");
	        PlantLevelparameters.add("RelPartition_stage11_stem");
	        PlantLevelparameters.add("RelPartition_stage11_sroot");
	        PlantLevelparameters.add("RelPartition_stage11_froot");
	        PlantLevelparameters.add("RelPartition_stage11_stor");
	        PlantLevelparameters.add("RelPartition_stage11_pod");
	        PlantLevelparameters.add("RelPartition_stage11_seed");
	        
	        
			p = 0;
			
			for (int t=0;t<PlantLevelparameters.size();t++){
				PlantLevelparameters_v.add( Double.valueOf(parameterfile.getProperty(PlantLevelparameters.get(t)) ));
				parametername[p] = PlantLevelparameters.get(t);
				parametervalue[p] = Double.valueOf(parameterfile.getProperty(PlantLevelparameters.get(t)));
				p++;
			}	
			
		}

		
        // QF
        
//        Enumeration em = parameterfile.keys();
//        p=0;
//        while(em.hasMoreElements()){
//              parametername[p] = (String)em.nextElement();
//              parametervalue[p]= Double.valueOf(parameterfile.getProperty(parametername[p]));
//              p++;
//        }
        
        tf=new LabelTextfieldGroup(p,parametername,parametervalue);
        center.add(tf.createHorizontalLabelTextfieldGroup()); 
          
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)          
                .addComponent(title)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)    
                    .addComponent(scrollable) 
                )
                .addGroup(layout.createSequentialGroup()
                    .addComponent(save)
                    .addComponent(newsave)
                    .addComponent(original)
                )
        );
        
        layout.setVerticalGroup(layout.createSequentialGroup()         
               .addComponent(title)
               .addGroup(layout.createSequentialGroup()      
                 .addComponent(scrollable)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(save)
                    .addComponent(newsave)
                    .addComponent(original)
                )
         );
    }   
    
    public void actionPerformed(ActionEvent e) {
        
           String text = (String)e.getActionCommand();
           if (text.equals("   Save   ")) {      
                   for (int i=0; i<p; i++) 
                        updated[i] = tf.tfHorizontal[i].getText();
                   //System.out.println( "GET_TEXTFIELD_VALUE=======================");
                   //System.out.println(Arrays.toString(updated)); 
                   Properties d=new Properties();
                   for (int i=0; i<p; i++)        
                      d.setProperty(parametername[i],updated[i]);         
                   
                   if(index==1)  {  
                       try{
                            File f3 = new File(update_file_leaf); 
                            FileOutputStream fid3 = new FileOutputStream(f3);   
                            d.store(fid3, "ConstantsFile_leaf");  
                        }
                        catch (IOException o) { 
                            // catch io errors from FileOutputStream 
                            System.out.println("Uh oh, got an IOException error!" + o.getMessage()); 
                        }
                   
                    } 
                 
                   if(index==2)  {  
                       try{
                            File f3 = new File(update_file_canopy); 
                            FileOutputStream fid3 = new FileOutputStream(f3);   
                            d.store(fid3, "ConstantsFile_canopy");  
                        }
                        catch (IOException o) { 
                            // catch io errors from FileOutputStream 
                            System.out.println("Uh oh, got an IOException error!" + o.getMessage()); 
                        }
                   
                    } 
                   
                   if(index==3)  {  
                       try{
                            File f3 = new File(update_file_plant); 
                            FileOutputStream fid3 = new FileOutputStream(f3);   
                            d.store(fid3, "ConstantsFile_plant");  
                        }
                        catch (IOException o) { 
                            // catch io errors from FileOutputStream 
                            System.out.println("Uh oh, got an IOException error!" + o.getMessage()); 
                        }
                   
                    }      
                   
                   // after Save, automatic load the saved value. 

                   String file1,file2,file3;
                   if (Constants.leafFile=="original") 
                       file1=const_file_leaf;
                   else file1=update_file_leaf;
                   if (Constants.canopyFile=="original") 
                       file2=const_file_canopy;
                   else file2=update_file_canopy;
                   if (Constants.plantFile=="original") 
                       file3=const_file_plant;
                   else file3=update_file_plant;
                   
                    FileInputStream in1 = null;
                    FileInputStream in2=null;
                    FileInputStream in3=null;
                    FileOutputStream out = null;
                    try {
                          int i1,i2,i3;
                          in1 = new FileInputStream(file1);
                          in2 = new FileInputStream(file2);
                          in3 = new FileInputStream(file3);
                          out = new FileOutputStream(const_file_all);

                          while ((i1 = in1.read()) != -1){
                             out.write(i1);
                          }
                          while ((i2 = in2.read()) != -1){
                             out.write(i2);
                          }
                          while ((i3 = in3.read()) != -1){
                             out.write(i3);
                          }
                          
                          File f = new File(const_file_all); 
                          FileInputStream fid = new FileInputStream(f); 
                          WIMOVAC.constants.load(fid);
                          System.out.println("totalvalue"+WIMOVAC.constants.size());

                     }
                     
                    catch (IOException o) { 
                       // catch io errors from FileInputStream 
                       System.out.println("Uh oh, got an IOException error!" + o.getMessage()); 
                    }
               } 
               
               if (text.equals("Load Saved Data")) { 
                   try {
                       parameterfile = new Properties();
                       if(index==1)  {  
                           f = new File(update_file_leaf);
                           Constants.leafFile="update";
                       } 
                       if(index==2)  {  
                           f = new File(update_file_canopy);
                           Constants.canopyFile="update";
                       } 
                       if(index==3)  {  
                           f = new File(update_file_plant);
                           Constants.plantFile="update";
                       } 
                       FileInputStream fid = new FileInputStream(f); 
                       parameterfile.load(fid); 
                    }
        
                    catch (IOException o) { 
                        // catch io errors from FileInputStream 
                        System.out.println("Uh oh, got an IOException error!" + o.getMessage()); 
                     }
                   
                 //QF
                   for (int t=0; t<parameterfile.size(); t++){
                  	 if (index == 1)
                  		 tf.tfHorizontal[t].setText(parameterfile.getProperty(Leafparameters.get(t)));
                  	 if (index == 2)
                  		 tf.tfHorizontal[t].setText(parameterfile.getProperty(CanopyLevelparameters.get(t)));
                  	 if (index == 3)
                  		 tf.tfHorizontal[t].setText(parameterfile.getProperty(PlantLevelparameters.get(t)));
                   }
//        
//                     Enumeration em = parameterfile.keys();
//                     int p1=0;
//                     while(em.hasMoreElements()){
//                         parametername[p1] = (String)em.nextElement();
//                         tf.tfHorizontal[p1].setText(parameterfile.getProperty(parametername[p1]));
//                         p1++;
//                     } 
                     
                     //update constants property
                     
                     
                    String file1,file2,file3;
                    if (Constants.leafFile=="original") 
                        file1=const_file_leaf;
                    else file1=update_file_leaf;
                    if (Constants.canopyFile=="original") 
                        file2=const_file_canopy;
                    else file2=update_file_canopy;
                    if (Constants.plantFile=="original") 
                        file3=const_file_plant;
                    else file3=update_file_plant;
                    
                     FileInputStream in1 = null;
                     FileInputStream in2=null;
                     FileInputStream in3=null;
                     FileOutputStream out = null;
                     try {
                           int i1,i2,i3;
                           in1 = new FileInputStream(file1);
                           in2 = new FileInputStream(file2);
                           in3 = new FileInputStream(file3);
                           out = new FileOutputStream(const_file_all);

                           while ((i1 = in1.read()) != -1){
                              out.write(i1);
                           }
                           while ((i2 = in2.read()) != -1){
                              out.write(i2);
                           }
                           while ((i3 = in3.read()) != -1){
                              out.write(i3);
                           }
                           
                           File f = new File(const_file_all); 
                           FileInputStream fid = new FileInputStream(f); 
                           WIMOVAC.constants.load(fid);
                           System.out.println("totalvalue"+WIMOVAC.constants.size());

                      }
                      
                     catch (IOException o) { 
                        // catch io errors from FileInputStream 
                        System.out.println("Uh oh, got an IOException error!" + o.getMessage()); 
                     }
                          
                     
                }
               
               if (text.equals("Load Original Data")) {                  
                     try {
                       parameterfile = new Properties();
                       if(index==1)  {  f = new File(const_file_leaf);  Constants.leafFile="original";} 
                       if(index==2)  {  f = new File(const_file_canopy);Constants.canopyFile="original";} 
                       if(index==3)  {  f = new File(const_file_plant); Constants.plantFile="original";} 
                       FileInputStream fid = new FileInputStream(f);
                       parameterfile.load(fid); 
                    }
                    catch (IOException o) { 
                        // catch io errors from FileInputStream 
                        System.out.println("Uh oh, got an IOException error!" + o.getMessage()); 
                     }
                     
                     //QF
                     System.out.println("QFQF:"+parameterfile.size());
                     for (int t=0; t<parameterfile.size(); t++){
                    	 if (index == 1)
                    		 tf.tfHorizontal[t].setText(parameterfile.getProperty(Leafparameters.get(t)));
                    	 if (index == 2)
                    		 tf.tfHorizontal[t].setText(parameterfile.getProperty(CanopyLevelparameters.get(t)));
                    	 if (index == 3)
                    		 tf.tfHorizontal[t].setText(parameterfile.getProperty(PlantLevelparameters.get(t)));
                     }
                     
                   //  Enumeration em = parameterfile.keys();
//                     int p2=0;
//                     while(em.hasMoreElements()){
//                         parametername[p2] = (String)em.nextElement();
//                         tf.tfHorizontal[p2].setText(parameterfile.getProperty(parametername[p2]));
//                         p2++;
//                     }  
                     String file1,file2,file3;
                    if (Constants.leafFile=="original") 
                        file1=const_file_leaf;
                    else file1=update_file_leaf;
                    if (Constants.canopyFile=="original") 
                        file2=const_file_canopy;
                    else file2=update_file_canopy;
                    if (Constants.plantFile=="original") 
                        file3=const_file_plant;
                    else file3=update_file_plant;
                    
                     FileInputStream in1 = null;
                     FileInputStream in2 = null;
                     FileInputStream in3 = null;
                     FileOutputStream out = null;
                     try {
                           int i1,i2,i3;
                           in1 = new FileInputStream(file1);
                           in2 = new FileInputStream(file2);
                           in3 = new FileInputStream(file3);
                           out = new FileOutputStream(const_file_all);

                           while ((i1 = in1.read()) != -1){
                              out.write(i1);
                           }
                           while ((i2 = in2.read()) != -1){
                              out.write(i2);
                           }
                           while ((i3 = in3.read()) != -1){
                              out.write(i3);
                           }
                      }
                      
                     catch (IOException o) { 
                        // catch io errors from FileInputStream 
                        System.out.println("Uh oh, got an IOException error!" + o.getMessage()); 
                     }
                     try {
                       
                       File f = new File(const_file_all); 
                       FileInputStream fid = new FileInputStream(f); 
                       WIMOVAC.constants.load(fid);               
                     } 
                     catch (IOException o) { 
                          // catch io errors from FileInputStream 
                         System.out.println("Uh oh, got an IOException error!" + o.getMessage()); 
                      }                  
                     
               }           
    }
          
    
        
     public void customerFrame(){
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) { }
    
        JFrame frame = new JFrame("Customer Designed Parameter File");
   //     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        //Create and set up the content pane.
        ParameterFile newContentPane = new ParameterFile(index);  
        frame.setContentPane(newContentPane);         
        
        //Display the window
        frame.pack();
        frame.setLocationRelativeTo(null); //center it
        frame.setSize(500,550);
        frame.setVisible(true);
     }
    
}






