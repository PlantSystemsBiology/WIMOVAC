package gui;
/*
 * CheckBoxGroup.java
 * 
 * This class creates a group of JCheckBox
 *
 * @author Dairui Chen
 */

import javax.swing.*;

public class CheckBoxGroup {
    private int number;
    private String title[];
    public JCheckBox cb[];
        
    public CheckBoxGroup (int n, String t[]) {
        number=n;
        title=t;
         
    }
       
    public JComponent createCheckBoxGroup() {  
        
        cb = new JCheckBox[number];       
        Box box = Box.createVerticalBox();
        
        for (int i=0; i<number; i++) {
             cb[i] = new JCheckBox(title[i]);             
             cb[i].setSelected(false);      
             box.add(cb[i]);
        }      
        return box;
    }
    public void setDefault(int i) {
        cb[i].setSelected(true);
    }
    
}


       

