package gui;
/*
 * Graph.java
 * 
 * This class show the reuslt graph
 *
 * @author Dairui Chen
 */

import java.awt.*;
import javax.swing.*;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.xy.XYSeriesCollection;


public class Graph extends JFrame {
     
        public Graph(XYSeriesCollection c,String title,String unit1,String unit2) {
            super("WIMOVAC Simulation - Graph Module");
            setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                      
            final JFreeChart chart = ChartFactory.createXYLineChart(title,unit1,unit2,c,PlotOrientation.VERTICAL,
                                     true,true,true);
            
	    final ChartPanel chartpanel = new ChartPanel(chart);
	    Insets insets = chartpanel.getInsets();
	    Dimension panelSize = new Dimension(450,350);
	    chartpanel.setBounds(0 + insets.left, 10 + insets.top, panelSize.width, panelSize.height);
              
            setContentPane(chartpanel);
            
            
            
             
        }  
        
        /*
        public static void main(final String[] args) {
            
            XYSeriesCollection c = new XYSeriesCollection();

            final Graph demo = new Graph(c," ", " ", " ");
            demo.pack();
            demo.setVisible(true);
        }
         * 
         */
          
         
        
        
}