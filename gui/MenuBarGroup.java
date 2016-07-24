package gui;
/*
 * MenuBarGroup.java
 * 
 * This class creates the menu bar for the input GUI frame
 * 
 * @author Dairui Chen
 */

import java.awt.event.*;

import javax.swing.*;

import java.io.File;
import java.io.IOException;

public class MenuBarGroup implements ActionListener, ItemListener {
    private JMenuBar bar;
    private JMenu print;
    private JMenu parameter;
    private JMenu help;
    
    public MenuBarGroup () {
        bar = new JMenuBar();
        print = new JMenu("Print");
        parameter = new JMenu("Model Parameters");
        help = new JMenu("Help");
             
    }
    
    public JMenuBar createMenuBar() {
         
      
        JMenuItem Item1 = new JMenuItem("Index");
        
        
        Item1.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                System.out.println("AClicked Index");  
            }  
        });  
        
   //     help.add(Item1);
        JMenuItem Item2 = new JMenuItem("Using Help");
        
        Item2.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                System.out.println("AClicked Using Help");  
                File f = new File("Doc\\help1.pdf");
                Runtime runtime = Runtime.getRuntime();  
                try {
					runtime.exec("rundll32 url.dll FileProtocolHandler "+f.getAbsolutePath());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
            }  
        });  
        
        help.add(Item2);
        JMenuItem Item3 = new JMenuItem("About");
        Item3.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                System.out.println("A Clicked About");  
                File f = new File("Doc\\About.pdf");
                Runtime runtime = Runtime.getRuntime();  
                try {
					runtime.exec("rundll32 url.dll FileProtocolHandler "+f.getAbsolutePath());
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} 
                
            }  
        });  
        
        help.add(Item3);
        JMenuItem Item4 = new JMenuItem("Component");
        
        Item4.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                System.out.println("A Clicked Component");  
            }  
        });  
  //      help.add(Item4);
        JMenuItem Item5 = new JMenuItem("System");
        help.add(Item5);
        Item5.addActionListener(new ActionListener() {  
            public void actionPerformed(ActionEvent e) {  
                System.out.println("A Clicked System");  
                JOptionPane.showMessageDialog(null, "Version 1.0, Copyright @ 2016", "System Information", JOptionPane.INFORMATION_MESSAGE);
                
            }  
        });  
   //     help.addActionListener(this);
   //     bar.add(print);
   //     bar.add(parameter);
        bar.add(help);        
        return bar;
    }
    
    //handle action events from all the components
    public void actionPerformed(ActionEvent e) {

    }
    //handle item change events from all the components
    public void itemStateChanged(ItemEvent e) {
    
    }
}
