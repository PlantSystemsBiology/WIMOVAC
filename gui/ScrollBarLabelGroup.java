package gui;
/*
 * ScrollBarLabelGroup.java
 * 
 * This class creates a group of JScrollBar and JLabel 
 * 
 * @author Dairui Chen
 */

import java.awt.*;
//import java.awt.event.*;
import javax.swing.*;

public class ScrollBarLabelGroup {
    private int number;   
    private String title[];
    
    public ScrollBarLabelGroup (int n, String t[]) {
        number=n;
        title=t;    
    }
    public JComponent createScrollBarLabelGroup() {
        JScrollBar sb[] = new JScrollBar[number];  
        JLabel l[] = new JLabel[number];
        Box box1 = Box.createVerticalBox();       
        Box box2 = Box.createVerticalBox();       
        Box box = Box.createHorizontalBox();
        
        for (int i=0; i<number; i++) {
             sb[i]= new JScrollBar(JScrollBar.HORIZONTAL, 20, 0, 0, 40);
             box1.add(sb[i]);
             l[i] = new JLabel(title[i]);          
             box2.add(l[i]);
        }      
        box.add(box1);
        box.add(box2);
        return box;        
    }   
    
}
