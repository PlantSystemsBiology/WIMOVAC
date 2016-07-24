package model;

public class FineRoot extends Root{
	
	public void updateRootLength(){
		
		switch (Constants.FRootGrowthModelType) {
        case 0:
            // Do not grow fine root structures
            break;
        case 1:
             // Growth fine root length according to the assimilates partitioned or reallocated to the fine root
             // pool during the previous time interval. Distribute the new roots vertically according to the
             // root distribution model.
        	L = dry_weight / Constants.SpecificFRootLength;
        	if (L < 0.0000001)  L = 0.0000001; // Prevent calculation funies
             
             // We now have new root material and need to update the fine root distribution.
             switch (Constants.RootDistributionModelType) {
                case 0:
                    // Distribute the root growth uniformly through the soil root profile
                    break;
                case 1:
                    // Distribute the root growth exponentially through the soil root profile
                    break;
                case 2:
                    // Root grows from the soil surface first and passes to lower soil layers only when a
                    // threshold layer root density is reached.
                    break;
             }
            
             break;
      }
	}

	
}
