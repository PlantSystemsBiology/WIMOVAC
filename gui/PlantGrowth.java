package gui;
/*
 * PlantGrowthTestArea.java
 * 
 * This is the interface for Plant Growth Module
 *
 * @author Dairui Chen
 */
    
import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.jfree.data.xy.XYSeriesCollection;

import function.*;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;

import model.*;

public class PlantGrowth extends JPanel implements ActionListener, ItemListener {

    private LabelTextfieldGroup tf1;
    private JButton weather,C3,Start,Stop,saveR,parameterfile;
    private RadioButtonGroup rbg1;
    private CheckBoxGroup cb1,cb2,cb3,cb4,cb5;

    //constructor
    public PlantGrowth() {
        
        
        Border loweredbevel=BorderFactory.createLoweredBevelBorder();
        Border raisedbevel=BorderFactory.createRaisedBevelBorder();
        Font f1=new Font("Times New Roman", Font.BOLD, 20);
        
        
        JPanel north=new JPanel();
        TitledBorder titledborder = BorderFactory.createTitledBorder(loweredbevel);
        north.setBorder(titledborder);
        
        String title1[]={"Latitude (degree)  ", "Atmospheric [O2] (mmol.mol-1) ", "Atmospheric [CO2] (ppm)",
                         "Year  ",  "Start Day  ", "Finish Day "};
        double default1[]={52,210,360,2014,100,250};
        tf1=new LabelTextfieldGroup(6,title1,default1);
        north.add(tf1.createHorizontalLabelTextfieldGroup());
         
        JPanel south=new JPanel();
        south.setBorder(titledborder);
        
        JTabbedPane tabpane = new JTabbedPane();
        JPanel tab1 = new JPanel();
        tabpane.addTab("Canopy",tab1);
        
        JPanel tab2 = new JPanel();
        tabpane.addTab("Weather",tab2);
        
        JPanel tab3 = new JPanel(new BorderLayout());
        tabpane.addTab("Biomass",tab3);
        
        JPanel tab4 = new JPanel();
   //     tabpane.addTab("Soil",tab4);
        
        JPanel tab5 = new JPanel();
   //     tabpane.addTab("Soil C",tab5);
        
        JPanel tab6 = new JPanel();
   //     tabpane.addTab("Soil N",tab6);
        
        south.add(tabpane);
        
        // create tab1: Canopy
        String title2[]={"Net assimilation rate (mol CO2.m-2.day-1)", 
                         "Elapsed assimilation (mol CO2.m-2.day-1)", 
                         "Canopy conductance (mol H2O.m-2.day-1)",
                         "Evapo/transpiration (mol H2O.m-2.day-1)",
                         "Cumulative evapo/transpiration (mol H2O.m-2)",
                         "Leaf area index (LAI)"
                    //   "Leaf water potential (J/Kg)",
                    //   "Respiration (gCO2/m2/day)",
                    //   "Cumulative respiration (gCO2/m2)",
                    //   "Water use efficiency (Daily)" 
        				}	;
        cb1 = new CheckBoxGroup(6,title2);
        
        tab1.add(cb1.createCheckBoxGroup());
        cb1.setDefault(5);
        
        
        // create tab2: Weather
        String title3[]={//"Air temperature (oC)", 
                         "Daily mean air temperature (oC)", 
                         "Daily highest temperature (oC)",
                         "Daily lowest temperature (oC)",
                         "Elapsed thermal time (days)",
                         "Elapsed thermal time (hours)",
                         "Relative humidity"
                     //    "Ozone concentration (nmol/mol)",
                     //    "Direct sunlight (mol/m2/s)", 
                     //    "Diffuse sunlight (mol/m2/s)", 
                     //    "Total radiation (mol/m2/s)",
                     //    "Daily radiation (mol/m2/day)",
                     //    "Rainfall (mm/day)",
                     //    "Cumulative rainfall (mm)",
                     //    "Plot values within day"
                         };
        cb2 =new CheckBoxGroup(6,title3);
        tab2.add(cb2.createCheckBoxGroup());
        
        // create tab3: Biomass
        JPanel west1=new JPanel();
        JPanel center1=new JPanel();
        JPanel east1=new JPanel();
        
        TitledBorder titledborder1 = BorderFactory.createTitledBorder(loweredbevel, "Above Ground");
        west1.setBorder(titledborder1);
        String title4[]={"Leaf (g.m-2)", 
                         "Stem (g.m-2)", 
                         "Standing dead (g.m-2)",
                         "Surface litter dead (g.m-2)",
                         "Seed (g.m-2)",
                         "Pod (g.m-2)"};
        cb3 =new CheckBoxGroup(6,title4);    
        west1.add(cb3.createCheckBoxGroup());
        tab3.add(west1,BorderLayout.WEST);
        cb3.setDefault(0);
        cb3.setDefault(1);
        cb3.setDefault(4);
        
        TitledBorder titledborder2 = BorderFactory.createTitledBorder(loweredbevel, "Below Ground");
        center1.setBorder(titledborder2);
        String title5[]={"Structural root (g.m-2)", 
                         "Fine root (g.m-2)", 
                         "Storage (rhizome) (g.m-2)",
                         "Root dead material (g.m-2)"};
        cb4 =new CheckBoxGroup(4,title5); 
        center1.add(cb4.createCheckBoxGroup());
        tab3.add(center1,BorderLayout.CENTER);
        cb4.setDefault(0);
        
        TitledBorder titledborder3 = BorderFactory.createTitledBorder(loweredbevel, "Biomass units");
        east1.setBorder(titledborder3);
        String title6[]={//"gCO2", 
                         //"gC", 
                         //"gCHO",
                         "gCH2O (Dry weight)"};     
        
        rbg1=new RadioButtonGroup(1,title6);
        east1.add(rbg1.createRadioButtonGroup());
        tab3.add(east1,BorderLayout.EAST); 
        rbg1.setDefault(0);
   //     rbg1.disable(0);rbg1.disable(1);rbg1.disable(2);  // disable the first three, for now, we do not have this function. 
        
        // create tab4: Soil
        String title7[]={"Soil evaporation (mol H2O.m-2.day-1)", 
                         "Running total soil evaporation (mol H2O.m-2)", 
                         "Water content (m3.m-3 layers)",
                         "Water potential (J.Kg-1)",
                         "Temperature (oC)"};
        cb5 =new CheckBoxGroup(5,title7);
    //    tab4.add(cb5.createCheckBoxGroup());
              
        weather=new JButton("   Weather Data   ");
        weather.setFont(f1);
        weather.setBorder(raisedbevel);
        weather.addActionListener(this);
        
        C3=new JButton("   C3   ");
        C3.setFont(f1);
        C3.setBorder(raisedbevel);
        C3.addActionListener(this);
        
        Start=new JButton("   Start   ");
        Start.setFont(f1);
        Start.setBorder(raisedbevel);
        Start.addActionListener(this);
        
        Stop=new JButton("   Stop   ");
        Stop.setFont(f1);
        Stop.setBorder(raisedbevel);
        Stop.addActionListener(this);
        

        saveR=new JButton("Save Results");
        saveR.setFont(f1);
        saveR.setBorder(raisedbevel);
        saveR.addActionListener(this);
        
        parameterfile=new JButton("Parameter File");
        parameterfile.setFont(f1);
        parameterfile.setBorder(raisedbevel);
        parameterfile.addActionListener(this);
        
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)          
                .addComponent(north)
          //      .addComponent(weather)                          
                .addComponent(south)
                .addGroup(layout.createSequentialGroup()    
                    .addComponent(C3)
                    .addComponent(Start)
                    .addComponent(Stop)  
                    .addComponent(saveR)
                    .addComponent(parameterfile)
                )
        );
        
        layout.setVerticalGroup(layout.createSequentialGroup()         
               .addComponent(north)
         //      .addComponent(weather)
               .addComponent(south)
               .addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                 .addComponent(C3)
                 .addComponent(Start)
                 .addComponent(Stop)
                 .addComponent(saveR)
                 .addComponent(parameterfile)
                )
         );        
    }
    
     public JMenuBar getMenuBar() {      
        MenuBarGroup bar=new MenuBarGroup();
        return bar.createMenuBar();
    }
 
    
    public void calculation() {
        
    	//get values from GUI
    	
    	//
    	
        double startYear = Double.valueOf(tf1.tfHorizontal[3].getText());  
        int StartYear = (int) startYear;
    
        
        double daystart = Double.valueOf(tf1.tfHorizontal[4].getText());
        int day_start = (int) daystart;
        double dayend = Double.valueOf(tf1.tfHorizontal[5].getText()); 
        int day_end = (int) dayend;
        System.out.println("**"+day_start+"\t"+day_end);
        Location lct = new Location();
        lct.Latitude = 51;
        lct.Longitude = 0;
        
        CurrentTime ct = new CurrentTime();
        ct.year = StartYear;
        
        RunGrowthModel rgm = new RunGrowthModel();
        GrowthRes gr = new GrowthRes();
        Environment env = new Environment(ct, lct);
        
        
        final XYSeriesCollection c1 = new XYSeriesCollection();
        final XYSeriesCollection c2 = new XYSeriesCollection();
        final XYSeriesCollection c3 = new XYSeriesCollection();
        final XYSeriesCollection c4 = new XYSeriesCollection();
        final XYSeriesCollection c5 = new XYSeriesCollection();
        final XYSeriesCollection c6 = new XYSeriesCollection();
        final XYSeriesCollection c7 = new XYSeriesCollection();
        final XYSeriesCollection c8 = new XYSeriesCollection();
        final XYSeriesCollection c9 = new XYSeriesCollection();
        final XYSeriesCollection c10 = new XYSeriesCollection();
        
        int day_step = 1;
        
        gr = rgm.all_days_curve(ct, lct, env, day_start, day_end, day_step);

        // draw figures

        if(cb1.cb[0].isSelected()){
        	c1.addSeries(gr.xys_NetAssiRate); 
            	JFrame result = new Graph(c1,"Plant Growth Module","Time (days)","Assimilation rate (mol CO2 .m-2.day-1 )");
                result.setSize(400, 400);
                result.setVisible(true);
        }
        if(cb1.cb[1].isSelected()){
        	c2.addSeries(gr.xys_ElapsedAssimilation); 
  
            	JFrame result = new Graph(c2,"Plant Growth Module","Time (days)","Elapsed Assimilation (mol CO2 .m-2 )");
                result.setSize(400, 400);
                result.setVisible(true);
        }
        
        if(cb1.cb[2].isSelected()){
        	c3.addSeries(gr.xys_CanopyConductance);
        	JFrame result = new Graph(c3,"Plant Growth Module","Time (days)","Canopy conductance (mol H2O .m-2 )");
            result.setSize(400, 400);
            result.setVisible(true);
        }
        if (cb1.cb[3].isSelected()){
        	c4.addSeries(gr.xys_Evapo_transpiration);
        	JFrame result = new Graph(c4,"Plant Growth Module","Time (days)","Evapo-transpiration (mol H2O .m-2 )");
            result.setSize(400, 400);
            result.setVisible(true);
        }
        if(cb1.cb[4].isSelected()){
        	c5.addSeries(gr.xys_CumulativeEvapoTranspiration);
        	JFrame result = new Graph(c5,"Plant Growth Module","Time (days)","Cumulative Evapo-Transpiration (mol H2O .m-2 )");
            result.setSize(400, 400);
            result.setVisible(true);
        }
        if(cb1.cb[5].isSelected()){
        	c6.addSeries(gr.xys_LAI);
        	JFrame result = new Graph(c6,"Plant Growth Module","Time (days)","LAI (m2 .m-2 )");
            result.setSize(400, 400);
            result.setVisible(true);
        }
//        if(cb1.cb[6].isSelected()){
//        	c7.addSeries(gr.xys_LeafWaterPotential);
//        	JFrame result = new Graph(c7,"Plant Growth Module","Time (days)","Leaf water potential (J /kg )");
//            result.setSize(400, 400);
//            result.setVisible(true);
//        }
//        if(cb1.cb[7].isSelected()){
//        	c8.addSeries(gr.xys_Respiration);
//        	JFrame result = new Graph(c8,"Plant Growth Module","Time (days)","Respiration (g CO2 /m2/day )");
//            result.setSize(400, 400);
//            result.setVisible(true);
//        }
//        if(cb1.cb[8].isSelected()){
//        	c9.addSeries(gr.xys_CumulativeRespiration);
//        	JFrame result = new Graph(c9,"Plant Growth Module","Time (days)","CumulativeRespiration (g CO2 /m2 )");
//            result.setSize(400, 400);
//            result.setVisible(true);
//        }
//        if(cb1.cb[9].isSelected()){
//        	c10.addSeries(gr.xys_WaterUseEfficiency);
//        	JFrame result = new Graph(c10,"Plant Growth Module","Time (days)","Water use efficiency (daily)");
//            result.setSize(400, 400);
//            result.setVisible(true);
//        }
        
        
        // tab 2
       
        final XYSeriesCollection c11 = new XYSeriesCollection();  // temperature
        final XYSeriesCollection c12 = new XYSeriesCollection();  // thermal days
        final XYSeriesCollection c13 = new XYSeriesCollection();  // thermal hours
        final XYSeriesCollection c14 = new XYSeriesCollection();  // RH
        final XYSeriesCollection c15 = new XYSeriesCollection();  //  O3
        final XYSeriesCollection c16 = new XYSeriesCollection();  //  light
        final XYSeriesCollection c17 = new XYSeriesCollection();  //  rain fall

        boolean draw11 = false;
  //      if(cb2.cb[0].isSelected())
  //      	{c11.addSeries(gr.xys_AirTemp); draw11 = true;} 
        if(cb2.cb[0].isSelected())
    		{c11.addSeries(gr.xys_DayMeanTemp); draw11 = true;} 
        if(cb2.cb[1].isSelected())
    		{c11.addSeries(gr.xys_DayHighTemp); draw11 = true;} 
        if(cb2.cb[2].isSelected())
    		{c11.addSeries(gr.xys_DayLowTemp); draw11 = true;} 
        	
        if(draw11){
        JFrame result = new Graph(c11,"Plant Growth Module","Time (days)","Temperature (oC)");
        result.setSize(400, 400);
        result.setVisible(true);
        }
        
        boolean draw12 = false;
        if(cb2.cb[3].isSelected()){
        	c12.addSeries(gr.xys_ElapsedThermalTime_day);
        	JFrame result = new Graph(c12,"Plant Growth Module","Time (days)","Thermal days (day)");
            result.setSize(400, 400);
            result.setVisible(true);
        }
        if(cb2.cb[4].isSelected()){
        	c13.addSeries(gr.xys_ElapsedThermalTime_hour);
        	JFrame result = new Graph(c13,"Plant Growth Module","Time (days)","Thermal hours (hour)");
            result.setSize(400, 400);
            result.setVisible(true);
        }
        if(cb2.cb[5].isSelected()){
        	c14.addSeries(gr.xys_RelativeHumidity);
        	JFrame result = new Graph(c14,"Plant Growth Module","Time (days)","Relative humidity");
            result.setSize(400, 400);
            result.setVisible(true);
        }
//        if (cb2.cb[7].isSelected()){
//        	c14.addSeries(gr.xys_OzoneConcentration);
//        	JFrame result = new Graph(c14,"Plant Growth Module","Time (days)","OzoneConcentration (nmol/mol)");
//            result.setSize(400, 400);
//            result.setVisible(true);
//        }
//        
//        boolean draw15 = false;
//        if(cb2.cb[8].isSelected()){
//        	c15.addSeries(gr.xys_DirectSunLight); draw15 = true;}
//        if(cb2.cb[9].isSelected()){
//        	c15.addSeries(gr.xys_DiffuseSunLight); draw15 = true;}
//        if(cb2.cb[10].isSelected()){
//        	c15.addSeries(gr.xys_TotalRadiation); draw15 = true;}
//        if(cb2.cb[11].isSelected()){
//        	c15.addSeries(gr.xys_DayRadiation); draw15 = true;}
//        if(draw15){
//
//        	JFrame result = new Graph(c15,"Plant Growth Module","Time (days)","light (umol/m2/s)");
//            result.setSize(400, 400);
//            result.setVisible(true);
//        }
//        
//        if(cb2.cb[12].isSelected()){
//        	c16.addSeries(gr.xys_RainFall);
//        	JFrame result = new Graph(c16,"Plant Growth Module","Time (days)","Rain fall (mm/day)");
//            result.setSize(400, 400);
//            result.setVisible(true);
//        }
//        if(cb2.cb[13].isSelected()){
//        	c17.addSeries(gr.xys_CumulativeRainFall);
//        	JFrame result = new Graph(c17,"Plant Growth Module","Time (days)","Cumulative rain fall (mm)");
//            result.setSize(400, 400);
//            result.setVisible(true);
//        }
//        
        
        // tab 3.1 and tab 3.2
        
        final XYSeriesCollection c21 = new XYSeriesCollection();  // dry weight

        boolean draw21 = false;
        if(cb3.cb[0].isSelected()){
        	c21.addSeries(gr.xys_leaf_dry_weight);draw21 = true;
        }
        if(cb3.cb[1].isSelected()){
        	c21.addSeries(gr.xys_stem_dry_weight);draw21 = true;
        }
        if(cb3.cb[2].isSelected()){
        	c21.addSeries(gr.xys_standing_dead);draw21 = true;
        }
        if(cb3.cb[3].isSelected()){
        	c21.addSeries(gr.xys_surface_dead);draw21 = true;
        }
        if(cb3.cb[4].isSelected()){
        	c21.addSeries(gr.xys_seed_dry_weight);draw21 = true;
        }
        if(cb3.cb[5].isSelected()){
        	c21.addSeries(gr.xys_pod_dry_weight);draw21 = true;
        }
        
        if(cb4.cb[0].isSelected()){
        	c21.addSeries(gr.xys_s_root_dry_weight);draw21 = true;
        }
        if(cb4.cb[1].isSelected()){
        	c21.addSeries(gr.xys_f_root_dry_weight);draw21 = true;
        }
        if(cb4.cb[2].isSelected()){
        	c21.addSeries(gr.xys_storage);draw21 = true;
        }
        if(cb4.cb[3].isSelected()){
        	c21.addSeries(gr.xys_root_dead);draw21 = true;
        }

        
        if(draw21){
        	JFrame result = new Graph(c21,"Plant Growth Module","Time (days)","Dry Weight (g.m-2)");
            
            result.setSize(400, 400);
            result.setVisible(true);
        }

        
        
        try {
             PrintWriter pw1=new PrintWriter(new OutputStreamWriter(new FileOutputStream("temp/WIMOVAC_OutputFile_Plant.csv")),true);              
             
             pw1.println("Time (day), Net assimilation rate (mol.m-1.day-1), Elapsed assimilation rate (mol.m-2), Canopy conductance (mol.m-2.day-1), Evapo/Transpiration (mol.m-2.day-1), Cumulative evapo/transpiration (mol.m-2), leaf area index (m2.m-2),"        
            		 +"Air temperature (oC), Daily mean temperature (oC), Daily highest temperature (oC), Daily lowest temperature (oC), Elapsed thermal time (days), Elapased thermal time (hours), Relative Humidity, "
            		 + "Leaf biomass (g.m-2), Stem biomass (g.m-2), Standing dead (g.m-2), Surface litter dead (g.m-2), Seed (g.m-2), Pod (g.m-2), Structual root (g.m-2), Fine root (g.m-2), Storage (g.m-2), Root dead material (g.m-2)" );

             int number=gr.xys_seed_dry_weight.getItemCount();
             for (int i=0; i<number; i++){
                  pw1.println(gr.xys_NetAssiRate.getX(i)+","+gr.xys_NetAssiRate.getY(i)+","+gr.xys_ElapsedAssimilation.getY(i)+","+gr.xys_CanopyConductance.getY(i)+","+gr.xys_Evapo_transpiration.getY(i)+","+gr.xys_CumulativeEvapoTranspiration.getY(i)
                		  +","+gr.xys_LAI.getY(i)+","+gr.xys_AirTemp.getY(i)+","+gr.xys_DayMeanTemp.getY(i)+","+gr.xys_DayHighTemp.getY(i)+","+gr.xys_DayLowTemp.getY(i)+","+gr.xys_ElapsedThermalTime_day.getY(i)+","+gr.xys_ElapsedThermalTime_hour.getY(i)+","+gr.xys_RelativeHumidity.getY(i)
                		  +","+gr.xys_leaf_dry_weight.getY(i)+","+gr.xys_stem_dry_weight.getY(i)+","+gr.xys_standing_dead.getY(i)+","+gr.xys_surface_dead.getY(i)+","+gr.xys_seed_dry_weight.getY(i)+","+gr.xys_pod_dry_weight.getY(i)+","+gr.xys_s_root_dry_weight.getY(i)+","+gr.xys_f_root_dry_weight.getY(i)
                		  +","+gr.xys_storage.getY(i)+","+gr.xys_root_dead.getY(i));

             }
             pw1.close();
             
        }
         catch (IOException o) { 
                             // catch io errors from FileInputStream 
              System.out.println("Uh oh, got an IOException error!" + o.getMessage()); 
         }
        
    } 
     //handle action events from all the components
    public void actionPerformed(ActionEvent e) {
        
           String text = (String)e.getActionCommand();
           if (text.equals("   C3   ")) {
              C3.setText("   C4   ");
              Constants.C3orC4="C4";
           }
           if (text.equals("   C4   ")) {
              C3.setText("   C3   ");
              Constants.C3orC4="C3";
           }
           if (text.equals("   Start   ")) {
        	   if (isAllFilled()){
        		   calculation();
        	   }else{
        		   JOptionPane.showMessageDialog(null, "No parameters for model ! \n You Can OPEN a parameter file from WIMVOAC or directly input from 'Parameter File'");
        	   }
           }   
           if (text.equals("Save Results")) {


          	 //SAVE to a user choose file. 
      	    	JFileChooser fc;
      	    	if(WIMOVAC.ResultDirOpened){
      	    		fc = new JFileChooser(WIMOVAC.ResultDir);
      	    	}else{
      	    		String current="";
    				try {
    					current = new File( "." ).getCanonicalPath();
    				} catch (IOException e1) {
    					// TODO Auto-generated catch block
    					e1.printStackTrace();
    					fc = new JFileChooser();
    				}
        			fc = new JFileChooser(current);
      	    	}

            	   FileNameExtensionFilter filter = new FileNameExtensionFilter(
            		        ".csv", "csv");
            	   fc.setFileFilter(filter);
            	   int returnVal = fc.showSaveDialog(getParent());
            	   if(returnVal == JFileChooser.APPROVE_OPTION) {
            	       System.out.println("You chose to open this file: " +
            	            fc.getSelectedFile().getAbsoluteFile());
            	       
            	       String Absolutefilename = fc.getSelectedFile().getAbsolutePath();
            	       if(!Absolutefilename.endsWith(".csv")){
            	    	   Absolutefilename = Absolutefilename.concat(".csv");
            	    	   
            	       }
            	       WIMOVAC.ResultDir = fc.getSelectedFile().getParent();
              	     WIMOVAC.ResultDirOpened = true;
            	     try {
  					copy("temp/WIMOVAC_OutputFile_Plant.csv",Absolutefilename);
  				} catch (IOException e3) {
  					// TODO Auto-generated catch block
  					e3.printStackTrace();
  				}

            	    }
          	   
             }
             
             // QIngfeng add
           
           if (text.equals("Parameter File")) {
               ParameterFile pf=new ParameterFile(3);
               pf.customerFrame();
           }
    }
    
    //handle item change events from all the components
    public void itemStateChanged(ItemEvent e) {
         
                
    }
     /*
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    public void createAndShowGUI() {
        //Use the Java look and feel
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) { }
    
        JFrame frame = new JFrame("Plant Growth Module");
  //      frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Create and set up the content pane.
        PlantGrowth newContentPane = new PlantGrowth();
        frame.setJMenuBar(newContentPane.getMenuBar());      
        frame.setContentPane(newContentPane);         
       
        //Display the window
        frame.pack();
        frame.setLocationRelativeTo(null); //center it
        frame.setSize(700,600);
        frame.setVisible(true);
    }  
    public static void copy(String sourcePath, String destinationPath) throws IOException {
    	File f3 = new File(destinationPath);
    	FileOutputStream fs = new FileOutputStream(f3);
        Files.copy(Paths.get(sourcePath), fs);
        fs.close();
    }
    private boolean isAllFilled(){
    	if (WIMOVAC.constants.isEmpty())
    		return false;
    	else
    		return true;
    }
}




