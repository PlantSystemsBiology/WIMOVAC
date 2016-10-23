
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
import javax.swing.filechooser.FileNameExtensionFilter;

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
	private String parameterFileName = ""; // for the new Dialog Save Function. 
	
//	private static final String const_file_all = "ParameterFile\\WIMOVAC_ConstantsFile.csv";
	
//	private static final String const_file_leaf = "ParameterFile\\WIMOVAC_ConstantsFile_leaf.csv";
//	private static final String update_file_leaf = "ParameterFile\\UPDATE_WIMOVAC_ConstantsFile_leaf.csv";
	
//	private static final String const_file_canopy = "ParameterFile\\WIMOVAC_ConstantsFile_canopy.csv";
//	private static final String update_file_canopy = "ParameterFile\\UPDATE_WIMOVAC_ConstantsFile_canopy.csv";
	
//	private static final String const_file_plant ="ParameterFile\\WIMOVAC_ConstantsFile_plant.csv";
//	private static final String update_file_plant ="ParameterFile\\UPDATE_WIMOVAC_ConstantsFile_plant.csv";
	
	
	private static final long serialVersionUID = 1L;
	private int index, p;  
    private LabelTextfieldGroup tf, tf_2;
//    public static Properties parameterfile, parameterfile_2;
    private String[] parametername,updated;
    private double[] parametervalue;
    private JPanel center;
    private JFrame frame;
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
    	
    	
    	//QF
     	try {
             UIManager.setLookAndFeel(
                 UIManager.getCrossPlatformLookAndFeelClassName());
         } catch (Exception e) { }
     
         frame = new JFrame("Customer Designed Parameter File");
    //     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
         //Create and set up the content pane.
     //    ParameterFile newContentPane = new ParameterFile(index);  
         
         
         
     	//QF
        
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
   
        JButton save=new JButton("   Apply   ");
        save.setFont(f2);
        save.setBorder(raisedbevel);
        save.addActionListener(this);
        
        JButton ok=new JButton("  OK  ");
        ok.setFont(f2);
        ok.setBorder(raisedbevel);
        ok.addActionListener(this);
        
        JButton cancel=new JButton("  Cancel  ");
        cancel.setFont(f2);
        cancel.setBorder(raisedbevel);
        cancel.addActionListener(this);

       
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
		//	Leafparameters_v.add( Double.valueOf(parameterfile.getProperty(Leafparameters.get(t)) ));
			parametername[p] = Leafparameters.get(t);
		//	parametervalue[p] = Double.valueOf(parameterfile.getProperty(Leafparameters.get(t)));
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
		//		CanopyLevelparameters_v.add( Double.valueOf(parameterfile.getProperty(CanopyLevelparameters.get(t)) ));
				parametername[p] = CanopyLevelparameters.get(t);
		//		parametervalue[p] = Double.valueOf(parameterfile.getProperty(CanopyLevelparameters.get(t)));
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
		//		PlantLevelparameters_v.add( Double.valueOf(parameterfile.getProperty(PlantLevelparameters.get(t)) ));
				parametername[p] = PlantLevelparameters.get(t);
		//		parametervalue[p] = Double.valueOf(parameterfile.getProperty(PlantLevelparameters.get(t)));
				p++;
			}	
			
		}

        
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
                    .addComponent(ok)
                    .addComponent(cancel)

                )
        );
        
        layout.setVerticalGroup(layout.createSequentialGroup()         
               .addComponent(title)
               .addGroup(layout.createSequentialGroup()      
                 .addComponent(scrollable)
                )
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                    .addComponent(save)
                    .addComponent(ok)
                    .addComponent(cancel)

                )
         );
        
        // for (int t=0; t<parameterfile.size(); t++){
      	 if (index == 1)
      		 for (int t=0; t<tf.tfHorizontal.length; t++)
      		 tf.tfHorizontal[t].setText(WIMOVAC.constants.getProperty(Leafparameters.get(t)));
      	 if (index == 2)
      		 for (int t=0; t<tf.tfHorizontal.length; t++)
      		 tf.tfHorizontal[t].setText(WIMOVAC.constants.getProperty(CanopyLevelparameters.get(t)));
      	 if (index == 3)
      		 for (int t=0; t<tf.tfHorizontal.length; t++)
      		 tf.tfHorizontal[t].setText(WIMOVAC.constants.getProperty(PlantLevelparameters.get(t)));
      //   }
      	 
      	frame.setContentPane(this);         
        
        //Display the window
        frame.pack();
        frame.setLocationRelativeTo(null); //center it
        frame.setSize(500,550);
        frame.setVisible(true);
    }   
    
    public void actionPerformed(ActionEvent e) {
        
           String text = (String)e.getActionCommand();
           if (text.equals("   Apply   ")) {    // Use this As Apply.   
        	   // Apply Update Parameters Directly to WIMOVAC constants
        	  save();
                   
           }
           if (text.equals("  OK  ")) {    // Use this As Apply.   
        	// Apply Update Parameters Directly to WIMOVAC constants
               save();
        	   frame.dispose();
        	   
           }
           
           if (text.equals("  Cancel  ")) {    // Use this As Apply.   
        	   frame.dispose();
        	   
           }
    }
    
    private void save(){
    	
    	if (isAllFilled()){
    		for (int i=0; i<p; i++) 
                updated[i] = tf.tfHorizontal[i].getText();

            for (int i=0; i<p; i++)        
        	   WIMOVAC.constants.setProperty(parametername[i],updated[i]);   
    	}
     	else{
     		JOptionPane.showMessageDialog(null, "Some parameters NOT filled ! \n You Can OPEN a parameter file from WIMVOAC");
     	}
    	
    	
    	
    }
    
    private boolean isAllFilled(){
    	for (int i=0; i<p; i++){ 
            if (tf.tfHorizontal[i].getText().isEmpty())
            	return false;
    	}
    	return true;
    }
                   

     public void customerFrame(){
        
    	// put it here. 
     }
    
}






