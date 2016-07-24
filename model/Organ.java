package model;

import java.util.ArrayList;

public class Organ {
	public double dry_weight;         								//unit: g biomass
	public double retired_dry_weight = 0;                               //unit: g biomass
	public double dead_on_stand = 0;
	public double dead_on_ground_surface = 0;
	double partition_weight;   								//unit: g glucose
	double re_allocate_weight;                              //unit: g ??
	double comp_protein, comp_lipid, comp_carbohydrate,     
	comp_lignin, comp_organic_acid, comp_minerals; 			//unit: % [0 - 1]
	double thermalTimeDeath;
	
	double total_conv_eff = 0;
	
	ArrayList<Double> dailyBMList = new ArrayList<Double>();
	
	
	
	public void calTotalConvEff(){
		double a, b, c, d, e, f;
		a = comp_carbohydrate 	* Constants.GlucosetoCarboEff;
		b = comp_protein		* Constants.GlucosetoProteinEff;
		c = comp_lipid 			* Constants.GlucosetolipidEff;
		d = comp_organic_acid 	* Constants.GlucosetoOrganicAcidEff;
		e = comp_minerals 		* Constants.GlucosetoMineralsEff;
		f = comp_lignin 		* Constants.GlucosetoLigninEff;
		double all = a+b+c+d+e+f;
		
		total_conv_eff 	= 1/all;
	}
	public void load_component(int n){
		comp_protein 		= Constants.ComponentOfOrgans[0][n];
		comp_lipid 			= Constants.ComponentOfOrgans[1][n];
		comp_carbohydrate	= Constants.ComponentOfOrgans[2][n];
		comp_lignin 		= Constants.ComponentOfOrgans[3][n];
		comp_organic_acid 	= Constants.ComponentOfOrgans[4][n];
		comp_minerals 		= Constants.ComponentOfOrgans[5][n];
	}

}
