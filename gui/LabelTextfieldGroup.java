package gui;
/*
 * LabelTextfieldGroup.java
 * 
 * This class creates a group of JLabel JTextfield pair
 *
 * @author Dairui Chen
 */

import java.awt.*;
import javax.swing.*;

public class LabelTextfieldGroup {
    private int number;
    private int row;  // number of row including the top label row
    private int column;  // number of column including the left label column
    private String titleH[];  // horizontal title
    private String titleV[];  // vertical title
    private double defaultValue[];
    private double originalValue[][];
    private String updateValue[];
    
    public JTextField tfHorizontal[];
    public JTextField tfVertical[][];
    // when pass value to originalValue[][], please set o[0][j]=0, because we only use from o[1][j] to o[row-1][j]
    // when pass value to titleV[], please set titleV[0]=" ", because we only use from titleV[1] to titleV[row-1]
    // when pass value to titleH[], please set titleH[0]=" ", because we only use from titleH[1] to titleH[column-1]
    
    public LabelTextfieldGroup (int n, String t[], double d[]) {
        number=n;
        titleV=t;    
        defaultValue=d;
    }
    public LabelTextfieldGroup (int r, int c, String tV[], String tH[], double o[][]) {
        row=r;
        column=c;
        titleV=tV;     
        titleH=tH;
        originalValue=o;  
    }
    
    public JComponent createHorizontalLabelTextfieldGroup() {
        JLabel l[] = new JLabel[number];  
        tfHorizontal = new JTextField[number];
       
        
        Box box = Box.createVerticalBox();
             
        for (int i=0; i<number; i++) {
             Box box1 = Box.createHorizontalBox();
             l[i] = new JLabel(titleV[i]);             
             box1.add(l[i]);
             
             tfHorizontal[i] = new JTextField(Double.toString(defaultValue[i]), 10);
             box1.add(tfHorizontal[i]);
             box.add(box1);
            
        }      
        
        
        return box;        
    }
    
    public String[] getTextFieldValue() {
        for (int i=0; i<number; i++) {
            System.out.println("i: "+i);
            updateValue[i] = tfHorizontal[i].getText();
        }       
        return updateValue;   
    }
    
    public JComponent updateTextField(int n, String t[], double d[]) {
        number=n;
        titleV=t;    
        defaultValue=d;
        return createHorizontalLabelTextfieldGroup();
    }
    
    public JComponent createVerticalLabelTextfieldGroup() {
        JLabel lV[] = new JLabel[row];  
        JLabel lH[] = new JLabel[column];  
        tfVertical = new JTextField[row][column];
        Box boxV = Box.createVerticalBox();
              
        for (int i=0; i<row; i++) {
            Box boxH = Box.createHorizontalBox();                                 
            for (int j=0; j<column; j++) {       
                   if (i==0){
                        lH[j] = new JLabel(titleH[j]);  
                        boxH.add(Box.createHorizontalGlue());
                        boxH.add(Box.createRigidArea(new Dimension(20,20)));
                        boxH.add(lH[j]);
                   }
                   else {
                       if(j==0) {
                           lV[i] = new JLabel(titleV[i]);  
                           boxH.add(Box.createHorizontalGlue());
                           boxH.add(Box.createRigidArea(new Dimension(20,20)));
                           boxH.add(lV[i]);
                       } else {
                           tfVertical[i][j] = new JTextField(Double.toString(originalValue[i][j]), 10);
                           boxH.add(Box.createHorizontalGlue());
                           boxH.add(Box.createRigidArea(new Dimension(20,20)));
                           boxH.add(tfVertical[i][j]);
                       }
                   }
            }         
            boxV.add(boxH);
        }      
        return boxV;
    }
}
