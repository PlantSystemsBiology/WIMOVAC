package gui;
/*
 * RadioButtonGroup.java
 * 
 * This class creates a group of JRadioButton
 * 
 * @author Dairui Chen
 */

import javax.swing.*;

public class RadioButtonGroup {
    private int number;
    private String title[];
    public JRadioButton rb[];  
    public RadioButtonGroup (int n, String t[]) {
        number=n;
        title=t;
    }
    
    public JComponent createRadioButtonGroup() {        
        ButtonGroup b = new ButtonGroup();
        rb = new JRadioButton[number];  
        Box box = Box.createVerticalBox();
        
        for (int i=0; i<number; i++) {
             rb[i] = new JRadioButton(title[i]);
             rb[i].setSelected(false);
             b.add(rb[i]);
             box.add(rb[i]);
        }
        rb[0].setSelected(true);
        

        return box;
    }
    public void disable(int i) {
        rb[i].setEnabled(false);
    }
    public void enable(int i) {
        rb[i].setEnabled(true);
    }
    public void setDefault(int i) {
        rb[i].setSelected(true);
    }
    
    public boolean isSelected(int i) {
        return rb[i].isSelected();
    }
}


       


