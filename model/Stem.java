package model;

public class Stem extends Organ {
	double L;   // L: stem length

	public void updateStemLength() {

	     //This subroutine calculates the additional stem length which can be developed from the assimilates
	     //partitioned or re-allocated to the leaf pool in the last time interval

	      switch (Constants.StemGrowthModelType) {
	        case 0:
	            // Do not grow stem structures. Keep the fixed value read in from the model parameter database
	            break;
	        case 1:
	            // Grow stem material according to the assimilates partitioned or reallocated to the stem pool
	            // during the previous time interval. Set canopy height equal to stem lengths divided by number of plants per metre square?
	            L = dry_weight / Constants.SpecificStemLength;
	            if (L < 0.0001)  L = 0.0001; // Prevent calculation funies
	            break;
	      }
	         
	   }
}
