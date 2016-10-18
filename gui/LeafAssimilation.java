package gui;
/*
 * LeafAssimilationTestArea.java
 * 
 * This is the interface for C3/C4 leaf assimilation  
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
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import model.*;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.OutputStreamWriter;

public class LeafAssimilation extends JPanel implements ActionListener, ItemListener {

    private static final long serialVersionUID = 1L;

    private JButton C3,start,saveR,parameterfile;

    private LabelTextfieldGroup tf1, tf2, tf3;
    
    private RadioButtonGroup radio1, radio2, radio3;
   
    //constructor
    public LeafAssimilation() {

        
        Border loweredbevel=BorderFactory.createLoweredBevelBorder();
        Border raisedbevel=BorderFactory.createRaisedBevelBorder();
        
        JPanel panel1=new JPanel(new FlowLayout());    
        panel1.setBorder(loweredbevel);   
        
        String title0[]={"Light_0 (umol.m-2.s-1)", "CO2 conc(Ca)_0 (ppm)", "Temperature_0 (oC)"};
            		  
        String title1[]={"Light_1 (umol.m-2.s-1)", "CO2 conc(Ca)_1 (ppm)", "Temperature_1 (oC)"};
        radio1=new RadioButtonGroup(3,title0);
        panel1.add(radio1.createRadioButtonGroup());
        radio1.rb[0].addActionListener(this);
        radio1.rb[1].addActionListener(this);
        radio1.rb[2].addActionListener(this);
        
        String title2[]={"Start  ", "Finish  ", "Interval  "};
        double default1[]={0,2000,25};
        tf1=new LabelTextfieldGroup(3,title2,default1);
        panel1.add(tf1.createHorizontalLabelTextfieldGroup());    
    //    JLabel u1=new JLabel("(umol/m2/s)");
    //    panel1.add(u1);
        
        JPanel panel2=new JPanel(new FlowLayout());
        panel2.setBorder(loweredbevel);

        String title3[]={"Assimilation", "Quantum yield"};  // "LCP"
        radio2=new RadioButtonGroup(2,title3);              // 3s
        panel2.add(radio2.createRadioButtonGroup());
        
        JLabel blank=new JLabel("                                                               ");
        panel2.add(blank);
        
        JPanel panel3=new JPanel(new FlowLayout());
        panel3.setBorder(loweredbevel);
        
        radio3=new RadioButtonGroup(3,title1);
        panel3.add(radio3.createRadioButtonGroup());
        radio3.disable(0);
        radio3.setDefault(1);
        radio3.rb[0].addActionListener(this);
        radio3.rb[1].addActionListener(this);
        radio3.rb[2].addActionListener(this);
        
        String title4[]={"1st  ", "2nd  ", "3rd  "};
        double default2[]={200,380,700};
        tf2=new LabelTextfieldGroup(3,title4,default2);
        panel3.add(tf2.createHorizontalLabelTextfieldGroup());
 //       JLabel u2=new JLabel("(umol/m2/s)");
  //      panel3.add(u2);
        
        
        JPanel panel4=new JPanel(new FlowLayout());
        panel4.setBorder(loweredbevel);
        String title5[]={"O2 concentration (mmol/mol)   ", 
                         "Temperature (oC)   ", 
                         "CO2 concentration (ppm)   ",
                         "Relative humidity near leaf (0 ~ 1)   ",
                         "Light  (umol.m-2.s-1) "};
        double default3[]={210,25,0,0.7,0};
        tf3=new LabelTextfieldGroup(5,title5,default3);
        panel4.add(tf3.createHorizontalLabelTextfieldGroup());
        
        String title6[]={"Plot Ci rather than Ca"};
        CheckBoxGroup cb1=new CheckBoxGroup(1,title6);
   //     panel4.add(cb1.createCheckBoxGroup());
   //     cb1.setDefault(0);
        
        
        C3=new JButton("   C3   ");
        Font f1=new Font("Times New Roman", Font.BOLD, 20);
        C3.setFont(f1);
        C3.setBorder(raisedbevel);
        C3.addActionListener(this);
        
        start=new JButton("   Start   ");
        start.setFont(f1);
        start.setBorder(raisedbevel);
        start.addActionListener(this);
        
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
                .addComponent(panel1)
                .addComponent(panel2)
                .addComponent(panel3)
                .addComponent(panel4)    
                .addGroup(layout.createSequentialGroup()    
                    .addComponent(C3)
                    .addComponent(start)  
                    .addComponent(saveR)
                    .addComponent(parameterfile)               
                )
        );
        
        layout.setVerticalGroup(layout.createSequentialGroup()         
                .addComponent(panel1)
                .addComponent(panel2)
                .addComponent(panel3)
                .addComponent(panel4)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.TRAILING)
                  .addComponent(C3)
                  .addComponent(start)
                  .addComponent(saveR)
                  .addComponent(parameterfile) 
                )
         );        
             
    }
    
    public JMenuBar getMenuBar() {      
        MenuBarGroup bar=new MenuBarGroup();
        return bar.createMenuBar();
    }
   
    public void calculationLight_CO2loop() {

    	double[] CO2 = new double [3];
        CO2[0] = Double.valueOf(tf2.tfHorizontal[0].getText());
        CO2[1] = Double.valueOf(tf2.tfHorizontal[1].getText());
        CO2[2] = Double.valueOf(tf2.tfHorizontal[2].getText());
        double O2    = Double.valueOf(tf3.tfHorizontal[0].getText());
        double T     = Double.valueOf(tf3.tfHorizontal[1].getText());
        double RH    = Double.valueOf(tf3.tfHorizontal[3].getText());
         
        double PPFD_start	=Double.valueOf(tf1.tfHorizontal[0].getText());
        double PPFD_end		=Double.valueOf(tf1.tfHorizontal[1].getText());
        double PPFD_step	=Double.valueOf(tf1.tfHorizontal[2].getText());
        
        Environment env = new Environment();
        env.air.current_T = T;
        env.air.RH = RH;
        env.air.O2_concentration = O2;
        
        
        Leaf leaf = new Leaf();
        leaf.Os = O2;
        leaf.RH = RH;
        leaf.Tair = T;
               
        
        JFrame result = null;
        RunLeafModel rlm = new RunLeafModel();
        final XYSeriesCollection c = new XYSeriesCollection();    
        
        String title = "A/Light Curve at Different CO2 (".concat(Constants.C3orC4);
        title = title.concat(" plant)");

        try {
             PrintWriter pw1=new PrintWriter(new OutputStreamWriter(new FileOutputStream("temp/WIMOVAC_OutputFile_Leaf.csv")),true);              
           
                                
            for (int n = 0; n<3; n++){
                pw1.print("CO2 concentration: ");
                pw1.println(CO2[n]+"ppm");
                
                
        	leaf.Cs = CO2[n];
        	env.air.CO2_concentration = CO2[n];

        	LeafRes lr = rlm.A_Light_curve(Double.toString(CO2[n]),env, leaf, PPFD_start, PPFD_end, PPFD_step);
       
        	if(radio2.rb[0].isSelected()){
                c.addSeries(lr.xys_A);
                result = new Graph(c,title,"Light (umol/m2/s)","A (umol/m2/s)");
        	}else if(radio2.rb[1].isSelected()){
        		c.addSeries(lr.xys_QY);
        		result = new Graph(c,title,"Light (umol/m2/s)","Quantum yield");
        	}else if(radio2.rb[2].isSelected()){
        		c.addSeries(lr.xys_LCP);
        		result = new Graph(c,title,"Light (umol/m2/s)","Light competiation point (umol/m2/s)");
        	}else{
        		System.out.println("error, leaf model radio2 should be selected");
        	}
        	
                int number=lr.xys_A.getItemCount();
                pw1.println("PPFD (micro mol.m-2.s-1), A (micro mol.m-2.s-1), Quantum yield (mol CO2.mol-1 photon)");
                for (int i=0; i<number; i++){
                    pw1.print(lr.xys_A.getX(i)+",");
                    pw1.print(lr.xys_A.getY(i)+",");
                    pw1.println(lr.xys_QY.getY(i)+",");
                //    pw1.println(lr.xys_LCP.getY(i));
                }
                        
             }
         }
         catch (IOException o) { 
                             // catch io errors from FileInputStream 
                             System.out.println("Uh oh, got an IOException error!" + o.getMessage()); 
         }
        
        result.setSize(400, 400);
        result.setVisible(true);
    }
    public void calculationLight_Temloop( ){
       
        double[] T = new double [3];
        T[0] = Double.valueOf(tf2.tfHorizontal[0].getText());
        T[1] = Double.valueOf(tf2.tfHorizontal[1].getText());
        T[2] = Double.valueOf(tf2.tfHorizontal[2].getText());
        double O2    = Double.valueOf(tf3.tfHorizontal[0].getText());
        double CO2     = Double.valueOf(tf3.tfHorizontal[2].getText());
        double RH    = Double.valueOf(tf3.tfHorizontal[3].getText());
         
        double PPFD_start	=Double.valueOf(tf1.tfHorizontal[0].getText());
        double PPFD_end		=Double.valueOf(tf1.tfHorizontal[1].getText());
        double PPFD_step	=Double.valueOf(tf1.tfHorizontal[2].getText());
        
        Environment env = new Environment();
        
        env.air.RH = RH;
        env.air.O2_concentration = O2;
        env.air.CO2_concentration = CO2;
        
        Leaf leaf = new Leaf();
        leaf.Os = O2;
        leaf.RH = RH;
        leaf.Cs = CO2;
        
        String title = "A/Light Curve at Different Temperature (".concat(Constants.C3orC4);
        title = title.concat(" plant)");
        JFrame result = null;
                
        RunLeafModel rlm = new RunLeafModel();
        final XYSeriesCollection c = new XYSeriesCollection();              
        try {
             PrintWriter pw1=new PrintWriter(new OutputStreamWriter(new FileOutputStream("temp/WIMOVAC_OutputFile_Leaf.csv")),true);              
           
                                
            for (int n = 0; n<3; n++){
                pw1.print("Temperature: ");
                pw1.println(T[n]+" oC");
                env.air.current_T = T[n];
                leaf.Tair = T[n];
        	
        	LeafRes lr = rlm.A_Light_curve(Double.toString(T[n]),env, leaf, PPFD_start, PPFD_end, PPFD_step);
        	
        	if(radio2.rb[0].isSelected()){
                c.addSeries(lr.xys_A);
                result = new Graph(c,title,"Light (umol/m2/s)","A (umol/m2/s)");
        	}else if(radio2.rb[1].isSelected()){
        		c.addSeries(lr.xys_QY);
        		result = new Graph(c,title,"Light (umol/m2/s)","Quantum yield");
        	}else if(radio2.rb[2].isSelected()){
        		c.addSeries(lr.xys_LCP);
        		result = new Graph(c,title,"Light (umol/m2/s)","Light competiation point (umol/m2/s)");
        	}else{
        		System.out.println("error, leaf model radio2 should be selected");
        	}

                int number=lr.xys_A.getItemCount();
                pw1.println("PPFD (micro mol.m-2.s-1), A (micro mol.m-2.s-1), Quantum yield (mol CO2.mol-1 photon)");
                for (int i=0; i<number; i++){
                    pw1.print(lr.xys_A.getX(i)+",");
                    pw1.print(lr.xys_A.getY(i)+",");
                    pw1.println(lr.xys_QY.getY(i));
                    
                }
                        
             }
         }
         catch (IOException o) { 
                             // catch io errors from FileInputStream 
                             System.out.println("Uh oh, got an IOException error!" + o.getMessage()); 
         }

        result.setSize(400, 400);
        result.setVisible(true);
        
    } 
    public void calculationCO2_Lightloop(){
        
        double[] Light = new double [3];
        Light[0] = Double.valueOf(tf2.tfHorizontal[0].getText());
        Light[1] = Double.valueOf(tf2.tfHorizontal[1].getText());
        Light[2] = Double.valueOf(tf2.tfHorizontal[2].getText());
        double O2    = Double.valueOf(tf3.tfHorizontal[0].getText());
        double TEM     = Double.valueOf(tf3.tfHorizontal[1].getText());
        double RH    = Double.valueOf(tf3.tfHorizontal[3].getText());
         
        double CO2_start	=Double.valueOf(tf1.tfHorizontal[0].getText());
        double CO2_end		=Double.valueOf(tf1.tfHorizontal[1].getText());
        double CO2_step	=Double.valueOf(tf1.tfHorizontal[2].getText());
        
        Environment env = new Environment();
        env.air.current_T = TEM;
        env.air.RH = RH;
        env.air.O2_concentration = O2;
        
        
        Leaf leaf = new Leaf();
        leaf.Os = O2;
        leaf.RH = RH;
        leaf.Tair = TEM;
        
        //env.air.CO2_concentration = CO2;
        String title = "A/CO2 Curve at Different Light (".concat(Constants.C3orC4);
        title = title.concat(" plant)");
        JFrame result = null;
        RunLeafModel rlm = new RunLeafModel();
        final XYSeriesCollection c = new XYSeriesCollection();              
        try {
             PrintWriter pw1=new PrintWriter(new OutputStreamWriter(new FileOutputStream("temp/WIMOVAC_OutputFile_Leaf.csv")),true);              
           
                                
            for (int n = 0; n<3; n++){
                pw1.print("PPFD: ");
                pw1.println(Light[n]+" micro mol.m-2.s-1");
                
                leaf.PPFD = Light[n];
        	
        	LeafRes lr = rlm.A_CO2_curve(Double.toString(Light[n]), env, leaf, CO2_start, CO2_end, CO2_step);
        	if(radio2.rb[0].isSelected()){
                c.addSeries(lr.xys_A);
                result = new Graph(c,title,"CO2 (ppm)","A (umol/m2/s)");
        	}else if(radio2.rb[1].isSelected()){
        		
        		c.addSeries(lr.xys_QY);
        		result = new Graph(c,title,"CO2 (ppm)","Quantum yield");
        	}else if(radio2.rb[2].isSelected()){
        		c.addSeries(lr.xys_LCP);
        		result = new Graph(c,title,"CO2 (ppm)","Light competiation point (umol/m2/s)");
        	}else{
        		System.out.println("error, leaf model radio2 should be selected");
        		
        	}

                int number=lr.xys_A.getItemCount();
                pw1.println("CO2 (ppm), A (micro mol.m-2.s-1), Quantum yield (mol CO2.mol-1 photon)");
                for (int i=0; i<number; i++){
                    pw1.print(lr.xys_A.getX(i)+",");
                    pw1.print(lr.xys_A.getY(i)+",");
                    pw1.println(lr.xys_QY.getY(i));
                }
                        
             }
         }
         catch (IOException o) { 
                             // catch io errors from FileInputStream 
                             System.out.println("Uh oh, got an IOException error!" + o.getMessage()); 
         }

        result.setSize(400, 400);
        result.setVisible(true);
        
    } 
    public void calculationCO2_Temloop( ){
        
        double[] T = new double [3];
        T[0] = Double.valueOf(tf2.tfHorizontal[0].getText());
        T[1] = Double.valueOf(tf2.tfHorizontal[1].getText());
        T[2] = Double.valueOf(tf2.tfHorizontal[2].getText());
        double O2    = Double.valueOf(tf3.tfHorizontal[0].getText());
        double LIGHT     = Double.valueOf(tf3.tfHorizontal[4].getText());
        double RH    = Double.valueOf(tf3.tfHorizontal[3].getText());
         
        double CO2_start	=Double.valueOf(tf1.tfHorizontal[0].getText());
        double CO2_end		=Double.valueOf(tf1.tfHorizontal[1].getText());
        double CO2_step	=Double.valueOf(tf1.tfHorizontal[2].getText());
        
        Environment env = new Environment();
        
        env.air.RH = RH;
        env.air.O2_concentration = O2;
        
        
        Leaf leaf = new Leaf();
        leaf.Os = O2;
        leaf.RH = RH;
        leaf.PPFD = LIGHT;
        
                
        RunLeafModel rlm = new RunLeafModel();
        final XYSeriesCollection c = new XYSeriesCollection();           
        String title = "A/CO2 Curve at Different Temperature (".concat(Constants.C3orC4);
        title = title.concat(" plant)");
        JFrame result = null;
        try {
             PrintWriter pw1=new PrintWriter(new OutputStreamWriter(new FileOutputStream("temp/WIMOVAC_OutputFile_Leaf.csv")),true);              
           
                                
            for (int n = 0; n<3; n++){
                pw1.print("Temperature: ");
                pw1.println(T[n]+" oC");
                env.air.current_T = T[n];
        	leaf.Tair = T[n];
        	LeafRes lr = rlm.A_CO2_curve(Double.toString(T[n]), env, leaf, CO2_start, CO2_end, CO2_step);
        	if(radio2.rb[0].isSelected()){
                c.addSeries(lr.xys_A);
                result = new Graph(c,title,"CO2 (ppm)","A (umol/m2/s)");
        	}else if(radio2.rb[1].isSelected()){
        		c.addSeries(lr.xys_QY);
        		result = new Graph(c,title,"CO2 (ppm)","Quantum yield");
        	}else if(radio2.rb[2].isSelected()){
        		c.addSeries(lr.xys_LCP);
        		result = new Graph(c,title,"CO2 (ppm)","Light competiation point (umol/m2/s)");
        	}else{
        		System.out.println("error, leaf model radio2 should be selected");
        	}
  
                int number=lr.xys_A.getItemCount();
                pw1.println("CO2 (ppm), A (micro mol.m-2.s-1), Quantum yield (mol CO2.mol-1 photon)");
                for (int i=0; i<number; i++){
                    pw1.print(lr.xys_A.getX(i)+",");
                    pw1.print(lr.xys_A.getY(i)+",");
                    pw1.println(lr.xys_QY.getY(i));
                }
                        
            
             }
         }
         catch (IOException o) { 
                             // catch io errors from FileInputStream 
                             System.out.println("Uh oh, got an IOException error!" + o.getMessage()); 
         }
        
       
        result.setSize(400, 400);
        result.setVisible(true);
        
    } 
    
    public void calculationTem_Lightloop( ){
        
        double[] Light = new double [3];
        Light[0] = Double.valueOf(tf2.tfHorizontal[0].getText());
        Light[1] = Double.valueOf(tf2.tfHorizontal[1].getText());
        Light[2] = Double.valueOf(tf2.tfHorizontal[2].getText());
        double O2    = Double.valueOf(tf3.tfHorizontal[0].getText());
        double CO2    = Double.valueOf(tf3.tfHorizontal[2].getText());
        double RH    = Double.valueOf(tf3.tfHorizontal[3].getText());
         
        double Tem_start	=Double.valueOf(tf1.tfHorizontal[0].getText());
        double Tem_end		=Double.valueOf(tf1.tfHorizontal[1].getText());
        double Tem_step	=Double.valueOf(tf1.tfHorizontal[2].getText());
        
        Environment env = new Environment();
        
        env.air.RH = RH;
        env.air.O2_concentration = O2;
        env.air.CO2_concentration = CO2;
        
        Leaf leaf = new Leaf();
        leaf.Os = O2;
        leaf.RH = RH;
        leaf.Cs = CO2;
        
        //env.air.CO2_concentration = CO2;
                
        RunLeafModel rlm = new RunLeafModel();
        final XYSeriesCollection c = new XYSeriesCollection();      
        String title = "A/Temperature Curve at Different Light (".concat(Constants.C3orC4);
        title = title.concat(" plant)");
        JFrame result = null;
        
        try {
             PrintWriter pw1=new PrintWriter(new OutputStreamWriter(new FileOutputStream("temp/WIMOVAC_OutputFile_Leaf.csv")),true);              
           
                                
            for (int n = 0; n<3; n++){
                pw1.print("PPFD: ");
                pw1.println(Light[n]+" micro mol.m-2.s-1");
                leaf.PPFD = Light[n];
                
                
        	LeafRes lr = rlm.A_Temperature_curve(Double.toString(Light[n]), env, leaf, Tem_start, Tem_end, Tem_step);
        	if(radio2.rb[0].isSelected()){
                c.addSeries(lr.xys_A);
                result = new Graph(c,title,"Temperature (oC)","A (umol/m2/s)");
        	}else if(radio2.rb[1].isSelected()){
        		c.addSeries(lr.xys_QY);
        		result = new Graph(c,title,"Temperature (oC)","Quantum yield");
        	}else if(radio2.rb[2].isSelected()){
        		c.addSeries(lr.xys_LCP);
        		result = new Graph(c,title,"Temperature (oC)","Light Competiation Point (umol/m2/s)");
        	}else{
        		System.out.println("error, leaf model radio2 should be selected");
        	}

                int number=lr.xys_A.getItemCount();
                pw1.println("Temperature (oC), A (micro mol.m-2.s-1), Quantum yield (mol CO2.mol-1 photon)");
                for (int i=0; i<number; i++){
                    pw1.print(lr.xys_A.getX(i)+",");
                    pw1.print(lr.xys_A.getY(i)+",");
                    pw1.println(lr.xys_QY.getY(i));
                }
                        
                                 
             }
         }
         catch (IOException o) { 
                             // catch io errors from FileInputStream 
                             System.out.println("Uh oh, got an IOException error!" + o.getMessage()); 
         }
        
        result.setSize(400, 400);
        result.setVisible(true);
        
    } 
     public void calculationTem_CO2loop() {

    	double[] CO2 = new double [3];
        CO2[0] = Double.valueOf(tf2.tfHorizontal[0].getText());
        CO2[1] = Double.valueOf(tf2.tfHorizontal[1].getText());
        CO2[2] = Double.valueOf(tf2.tfHorizontal[2].getText());
        double O2    = Double.valueOf(tf3.tfHorizontal[0].getText());
        double LIGHT     = Double.valueOf(tf3.tfHorizontal[4].getText());
        double RH    = Double.valueOf(tf3.tfHorizontal[3].getText());
         
        double Tem_start	=Double.valueOf(tf1.tfHorizontal[0].getText());
        double Tem_end		=Double.valueOf(tf1.tfHorizontal[1].getText());
        double Tem_step	=Double.valueOf(tf1.tfHorizontal[2].getText());
        
        Environment env = new Environment();
        
        env.air.RH = RH;
        env.air.O2_concentration = O2;
        
        
        Leaf leaf = new Leaf();
        leaf.Os = O2;
        leaf.RH = RH;
        leaf.PPFD=LIGHT;
                
        RunLeafModel rlm = new RunLeafModel();
        final XYSeriesCollection c = new XYSeriesCollection();      
        String title = "A/Temperature Curve at Different CO2 (".concat(Constants.C3orC4);
        title = title.concat(" plant)");
        JFrame result = null;
        try {
             PrintWriter pw1=new PrintWriter(new OutputStreamWriter(new FileOutputStream("temp/WIMOVAC_OutputFile_Leaf.csv")),true);              
           
                                
            for (int n = 0; n<3; n++){
                pw1.print("CO2 concentration: ");
                pw1.println(CO2[n]+" ppm");
                
                
               
        	leaf.Cs = CO2[n];
        	env.air.CO2_concentration = CO2[n];
        	LeafRes lr = rlm.A_Temperature_curve(Double.toString(CO2[n]), env, leaf, Tem_start, Tem_end, Tem_step);
        	if(radio2.rb[0].isSelected()){
                c.addSeries(lr.xys_A);
                result = new Graph(c,title,"Temperature (oC)","A (umol/m2/s)");
        	}else if(radio2.rb[1].isSelected()){
        		c.addSeries(lr.xys_QY);
        		result = new Graph(c,title,"Temperature (oC)","Quantum yield");
        	}else if(radio2.rb[2].isSelected()){
        		c.addSeries(lr.xys_LCP);
        		result = new Graph(c,title,"Temperature (oC)","Light Competiation Point (umol/m2/s)");
        	}else{
        		System.out.println("error, leaf model radio2 should be selected");
        	}

                int number=lr.xys_A.getItemCount();
                pw1.println("Temperature (oC), A (micro mol.m-2.s-1), Quantum yield (mol CO2.mol-1 photon)");
                for (int i=0; i<number; i++){
                    pw1.print(lr.xys_A.getX(i)+",");
                    pw1.print(lr.xys_A.getY(i)+",");
                    pw1.println(lr.xys_QY.getY(i));
                }
                        
                                     
             }
         }
         catch (IOException o) { 
                             // catch io errors from FileInputStream 
                             System.out.println("Uh oh, got an IOException error!" + o.getMessage()); 
         }
       
        result.setSize(400, 400);
        result.setVisible(true);
    }
    
     //handle action events from all the components
    public void actionPerformed(ActionEvent e) {
        
           String text = (String)e.getActionCommand();
           if (text.equals("   C3   ")) {
              C3.setText("   C4   ");
              Constants.C3orC4="C4";
              radio1.disable(2);  
              
           }
           if (text.equals("   C4   ")) {
              C3.setText("   C3   ");
              Constants.C3orC4="C3";
              radio1.enable(2);  
              
           }
           if ( text.equals("   Start   ")) {
        	   
        	   if (isAllFilled()){
        		   if(radio1.isSelected(0)){
                       if(radio3.isSelected(1))
                            calculationLight_CO2loop();  
                       else if(radio3.isSelected(2))
                            calculationLight_Temloop(); 
                   }
                   if(radio1.isSelected(1)){
                       if(radio3.isSelected(0))       
                            calculationCO2_Lightloop(); 
                       else if(radio3.isSelected(2))
                            calculationCO2_Temloop(); 
                   }
                   if(radio1.isSelected(2)){
                       if(radio3.isSelected(0)) {
                            calculationTem_Lightloop();  
                       }
                        else if(radio3.isSelected(1))
                            calculationTem_CO2loop(); 
                   }
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
					copy("temp/WIMOVAC_OutputFile_Leaf.csv",Absolutefilename);
				} catch (IOException e3) {
					// TODO Auto-generated catch block
					e3.printStackTrace();
				}

          	    }
        	   
           }
           
           // QIngfeng add
           
           if (text.equals("Light_0 (umol.m-2.s-1)") ) {
        	   
               radio3.disable(0);
               radio3.enable(1);
               radio3.setDefault(1);
               radio3.enable(2);
               tf1.tfHorizontal[0].setText("0");  //Light: start, end, interval default values
               tf1.tfHorizontal[1].setText("2000");
               tf1.tfHorizontal[2].setText("25");
               
               tf3.tfHorizontal[1].setText("25"); // Temperature
               tf3.tfHorizontal[2].setText("380"); // CO2
               tf3.tfHorizontal[4].setText("N/A"); // Light
               
           }
           if (text.equals("Light_1 (umol.m-2.s-1)") ) {
               

               tf2.tfHorizontal[0].setText("100");  //Light: start, end, interval default values
               tf2.tfHorizontal[1].setText("500");
               tf2.tfHorizontal[2].setText("1500");
               
               tf3.tfHorizontal[1].setText("25"); // Temperature
               tf3.tfHorizontal[2].setText("380"); // CO2
               tf3.tfHorizontal[4].setText("N/A"); // Light
               
           }
           if (text.equals("CO2 conc(Ca)_0 (ppm)") ) {
        	  
               radio3.enable(0);
               
               radio3.disable(1);
               
               radio3.enable(2);
               radio3.setDefault(0);
               tf1.tfHorizontal[0].setText("0");  //CO2 start, end, interval default values
               tf1.tfHorizontal[1].setText("1800");
               tf1.tfHorizontal[2].setText("50");
               
               tf3.tfHorizontal[1].setText("25"); // Temperature
               tf3.tfHorizontal[2].setText("N/A"); // CO2
               tf3.tfHorizontal[4].setText("1500"); // Light

           } 
           if (text.equals("CO2 conc(Ca)_1 (ppm)") ) {
               

               tf2.tfHorizontal[0].setText("200");  //CO2 start, end, interval default values
               tf2.tfHorizontal[1].setText("380");
               tf2.tfHorizontal[2].setText("700");

               tf3.tfHorizontal[1].setText("25"); // Temperature
               tf3.tfHorizontal[2].setText("N/A"); //  CO2 
               tf3.tfHorizontal[4].setText("1500"); // Light


           } 
           if (text.equals("Temperature_0 (oC)") ) {

               radio3.enable(0);
               radio3.setDefault(0);
               radio3.enable(1);
               radio3.disable(2);
               tf1.tfHorizontal[0].setText("15");  // temperature start, end, interval default values
               tf1.tfHorizontal[1].setText("45");
               tf1.tfHorizontal[2].setText("3");
               
              
               tf3.tfHorizontal[1].setText("N/A"); // Temperature
               tf3.tfHorizontal[2].setText("380"); //  CO2 
               tf3.tfHorizontal[4].setText("1500"); // Light
               
           } 
           if (text.equals("Temperature_1 (oC)") ) {
               
               tf2.tfHorizontal[0].setText("10");
               tf2.tfHorizontal[1].setText("25");
               tf2.tfHorizontal[2].setText("40");
               
               tf3.tfHorizontal[1].setText("N/A"); // Temperature
               tf3.tfHorizontal[2].setText("380"); //  CO2 
               tf3.tfHorizontal[4].setText("1500"); // Light


           } 
           if (text.equals("Parameter File")) {
               ParameterFile pf=new ParameterFile(1);
               pf.customerFrame();
           }
           
           // QIngfeng add
           
           
           
           
    }
    //handle item change events from all the components
    public void itemStateChanged(ItemEvent e) {
        
        
                
    }

    
    /**
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
    
        JFrame frame = new JFrame("C3/C4 Leaf Assimilation Module");
   //     frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //Create and set up the content pane.
        LeafAssimilation newContentPane = new LeafAssimilation();
        frame.setJMenuBar(newContentPane.getMenuBar());      
        frame.setContentPane(newContentPane);         
       
        //Display the window
        frame.pack();
        frame.setLocationRelativeTo(null); //center it
        frame.setSize(500,550);
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