package model;

import gui.WIMOVAC;

public class Seed extends Organ {
	boolean isGerminated = false;  // isGerminated: seed germinated (true) or not (false)
	boolean isPlantted = false;    // isPlantted:   seed is plantted in the soil (true) or have not been (false)

	public void determineGermination(CurrentTime ct) {
		/*
		 This subroutine sets the germination flag to true if today is the germination day set in the model
		 parameter database. This flag only has an effect when the annual growth model is being used.

		 At a later date a more complex condition may be applied here such that other environmental criteria must occurr
		 for germination to be sucessful. These criteric for example may include the necessity of a warm or cold period
		 prior to germination in which germination will be delayed after the germination date specified in the model
		 parameter database untill the environmental criteria are met.
		 * 
		 */
		    switch ((int)(double)Double.valueOf(Constants.GrowthModelSwitch)) {  //=1
		        case 1:
		            // Annual growth model is being used and so test for germination day
		            if (ct.day == (int)(double)Double.valueOf(WIMOVAC.constants.getProperty("GerminationDay")))
		                 // The germination day has arrived and so we need to trigger germination/leaf emergence events.
		                 // This includes starting the germination thermal time clock counter.
		                 isGerminated = true;
		            break;
		    }
	}
	public void DeterminePlanting(CurrentTime ct) {

		// This subroutine examines the current simulation day to determine whether today is the planting day
		// for the annual growth model.

		    switch (Integer.valueOf(WIMOVAC.constants.getProperty("GrowthModelSwitch"))) {  //=1
		        case 1:
		             // Annual growth model is being used and so check for the planting day
		            if (ct.day == Integer.valueOf(WIMOVAC.constants.getProperty("PlantingDay"))) {
		            	isPlantted = true;
		            
		                // During the fallow period all plant pools are zeroed but the soil status is retained
		                // On the planting day the plant pools need to be initialised with the values stored in
		                // the model parameter database. The planting day must occur before the germination day
		                // and the period between planting and germination is called the pre-germination period.
		                                
		               // if (aux.getDevelopmentalStage() != PreGermination) {
		                
		               //     SetGlobalPartitioningVariables();
		                    
		               //     ReSetGrowthParamsFromDatabase();   // Reset all plant pools to the model parameter database defaults
		                    
		               //     aux.setDevelopmentalStage (PreGermination); // Set the plant growth stage to pre-germination
		                    
		               //     aux.setSummaryTableGrowthRow(aux.getSummaryTableGrowthRow() + 1); // Increment the summary table row counter
		                    
		             //    }
		            }
		           break;
		     }

		}
}
