package gui;
/*
 * WIMOVAC.java
 * 
 * This is the main file for WIMOVAC
 * 
 * @author Dairui Chen
 * 
 */

import java.io.*; 
import java.util.Properties;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.BorderFactory; 
import javax.swing.GroupLayout;

public class WIMOVAC extends JPanel implements ActionListener {
    
    public  static Properties constants;
    public static String PhotosynthesisType="C3";
    
    //  Auxilary variables for transpiration
    public static double[] DryAirDensity={1.316, 1.292, 1.296, 1.246, 1.225, 1.204, 1.183, 1.164, 1.146, 1.128, 1.11};
    public static double[] LatentHeatVapour={2.513, 2.501, 2.489, 2.477, 2.465, 2.454, 2.442, 2.43, 2.418, 2.406, 2.394};
    public static double[] SlopeFactorS={0.25, 0.28, 0.32, 0.36, 0.4, 0.45, 0.51, 0.57, 0.63, 0.7, 0.78, 0.87, 0.96,
                                         1.07, 1.18, 1.3, 1.43, 1.57, 1.72, 1.89, 2.07, 2.26, 2.46, 2.68, 2.92, 3.17};
    public static double[] SatWaterVapConc={3.41, 3.93, 4.52, 5.19, 5.95, 6.79, 7.75, 8.82, 10.01, 11.35, 12.83, 14.48, 16.31, 18.34,
                                            20.58, 23.05, 25.78, 28.78, 32.07, 35.68, 39.63, 43.96, 48.67, 53.82, 59.41, 65.9};
    
    private JRadioButton r1,r2,r3,r4;
    
    public WIMOVAC() {
        
        
        Font f1=new Font("Times New Roman", Font.BOLD, 30);
        Font f2=new Font("Times New Roman", Font.BOLD, 20);

        Border loweredbevel=BorderFactory.createLoweredBevelBorder();
        Border raisedbevel=BorderFactory.createRaisedBevelBorder();
        
        JLabel title=new JLabel("Tobacco Model");
        title.setFont(f1);
        
        JPanel north=new JPanel();
        TitledBorder titledborder1 = BorderFactory.createTitledBorder(loweredbevel,"Leaf Properties");
        north.setBorder(titledborder1);
        
        r1=new JRadioButton(" C3/C4 Leaf Assimilation Module");
        r1.setFont(f2);
        r1.setSelected(true);
        north.add(r1);
        
        JPanel center=new JPanel(new BorderLayout());
        TitledBorder titledborder2 = BorderFactory.createTitledBorder(loweredbevel,"Canopy Properties");
        center.setBorder(titledborder2);
        
        r2=new JRadioButton(" Sunlit/shaded Canopy MicroClimate Module");
        r2.setFont(f2);
        center.add(r2,BorderLayout.CENTER);
        
        r4=new JRadioButton(" Sunlit/shaded Canopy Assimilation Module");
        r4.setFont(f2);
        center.add(r4,BorderLayout.SOUTH);
        
        JPanel south=new JPanel();
        TitledBorder titledborder3 = BorderFactory.createTitledBorder(loweredbevel,"Plant Growth & Development");
        south.setBorder(titledborder3);
        
        r3=new JRadioButton(" Plant Growth Module");
        r3.setFont(f2);
        south.add(r3);
        
        ButtonGroup b = new ButtonGroup();
        b.add(r1);
        b.add(r2);
        b.add(r3);
        b.add(r4);
        
        JButton start=new JButton("   Start   ");
        start.setFont(f2);
        start.setBorder(raisedbevel);
        start.addActionListener(this);
        
        GroupLayout layout = new GroupLayout(this);
        setLayout(layout);
        layout.setAutoCreateGaps(true);
        layout.setAutoCreateContainerGaps(true);
        
        layout.setHorizontalGroup(layout.createParallelGroup(GroupLayout.Alignment.CENTER)          
                .addComponent(title)
                .addGroup(layout.createParallelGroup(GroupLayout.Alignment.LEADING)    
                    .addComponent(north)
                    .addComponent(center)
                    .addComponent(south)  
                )
                .addComponent(start)
        );
        
        layout.setVerticalGroup(layout.createSequentialGroup()         
               .addComponent(title)
               .addGroup(layout.createSequentialGroup()
                 .addComponent(north)
                 .addComponent(center)
                 .addComponent(south)
                )
                .addComponent(start)
         );        
    }

    public static void createAndShowGUI() {
        //Use the Java look and feel
        try {
            UIManager.setLookAndFeel(
                UIManager.getCrossPlatformLookAndFeelClassName());
        } catch (Exception e) { }

        JFrame frame = new JFrame("Tobacco Model Launcher");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Create and set up the content pane.
        WIMOVAC newContentPane = new WIMOVAC();
        frame.setContentPane(newContentPane);

        //Display the window
        frame.pack();
        frame.setLocationRelativeTo(null); //center it
        frame.setSize(480,400);
        frame.setVisible(true);
    }

    public void actionPerformed(ActionEvent e) {

           if (r1.isSelected()) {
               LeafAssimilation  leafassimilation=new LeafAssimilation();
               leafassimilation.createAndShowGUI();
           }
           else if (r2.isSelected()) {
               CanopyMicroclimate  canopymicro=new CanopyMicroclimate();
               canopymicro.createAndShowGUI();
           }
           else if (r4.isSelected()) {
               CanopyAssimilation  canopyassimilation=new CanopyAssimilation();
               canopyassimilation.createAndShowGUI();
           }
           else if (r3.isSelected()) {
               PlantGrowth  plantgrowth=new PlantGrowth();
               plantgrowth.createAndShowGUI();
           }          
    }

    public static void main(String[] args){  
        
        try {
           constants = new Properties();
           File f = new File("ParameterFile\\ConstantsFile.csv"); 
           FileInputStream fid = new FileInputStream(f); 
           constants.load(fid);               
        } 
        catch (IOException e) { 
           // catch io errors from FileInputStream 
           System.out.println("Uh oh, got an IOException error!" + e.getMessage()); 
        }
        
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                
                createAndShowGUI();
            }
        });
        
    }
}
