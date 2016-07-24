package model;

public class Pod extends Organ {
	public double pod_num;

	public void updatePodNum() {



	      //This subroutine calculates the additional pod which can be developed from the assimilates
	      //partitioned or re-allocated to the leaf pool in the last time interval

	    switch (Constants.PodGrowthModelType) {
	        case 0:
	            // Do not new pod
	            break;
	        case 1:
	             // Growth the number of new pod according to the assimilates partitioned or reallocated to new pod
	             //during the previous time interval.
	             pod_num = dry_weight / Constants.SpecificPodNumber;
	             	             
	             if (pod_num < 0.0000001)  pod_num = 0.0000001; // Prevent calculation funies

	             // We now have new pod and must distribute it appropriately in the vertical plant profile
	             switch (Constants.PodGrowthModelType) {
	                case 0:
	                    // distributing the pod evenly in the plant
	                    break;
	                case 1:
	                    // Distribute the pod exponentially through the soil root profile
	                    break;
	                case 2:
	                    // pod growth should be empasised on the new stem. This process should be given more
	                    // development in the future.
	                    break;
	             }                     
	                     
	         break;
	             
	      }
	    
	    }
} 
