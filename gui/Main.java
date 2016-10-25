package gui;
import java.io.PrintWriter;
import java.util.ArrayList;

import model.*;

public class Main {

	/**
	 * @param args
	 * 
	 * this is only for testing the model without GUI
	 * for running GUI, please run WIMOVAC.java
	 */
	public static void main(String[] args) {
		
		// TODO Auto-generated method stub
		System.out.println("Start!");
		
		// load parameters from file to Constants
		
		// read user input parameters from GUI to Constants

		//test leaf photosynthesis
		
		ArrayList<Leaf> leaf_array = new ArrayList<Leaf>();
		
		double Cs = 300, Os = 210, Tair = 25, RH = 0.7;
		double PPFD = 0, PPFD_step = 100;
		for (int i=0; i<20; i++){
			PPFD = PPFD + PPFD_step;
			Leaf leaf1       = new Leaf(Cs, Os, PPFD, RH, Tair);
			Environment env = new Environment();
			leaf1.cal_c3(env);
			leaf_array.add(leaf1);
		}
		
		for (int i=0; i<20; i++){
			System.out.println("A: "+leaf_array.get(i).A);
		} 
		
		//test environment
		CurrentTime ct = new CurrentTime();
		Location lct = new Location();
		lct.Latitude = 52;
		
		Environment env = new Environment();
		
		env.air.CO2_concentration = 360;
		env.air.O2_concentration  = 210;
		ct.year = 1996;
		ct.day  = 250; 

		//
		
		//test canopy
		Leaf leaf = new Leaf();
		Canopy canopy = new Canopy();
		canopy.LAI = 3;
		for (int i = 0; i<=23; i++){
			ct.hour = i;
			env.calculate_Env(ct, lct);
			canopy.CanopyMicroClimateDriver(env, leaf);
			canopy.CanopyAssimilationDriver(env);
			System.out.println(canopy.sunlit_leaf.CO2_uptake_rate);
		}

		//test plant growth
		Weather weather = new Weather();
		PrintWriter pw0 = null;
		Plant plant = new Plant();
		for (int d = 150; d<=300; d++){
			plant.runDailyPlantProcess(ct, lct, env, weather,pw0);
			
			double Ac_per_day = plant.daily_carbon_uptake;
			double LAI = plant.canopy.LAI;
			
		}
		
		

	}

}
