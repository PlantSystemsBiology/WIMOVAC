package function;
import model.*;

public class RunLeafModel {
	public LeafRes A_Light_curve (String name, Environment env, Leaf leaf, double PPFD_start, double PPFD_end, double PPFD_step){
		LeafRes lr = new LeafRes(name);
		
		for (double PPFD=PPFD_start; PPFD<=PPFD_end; PPFD+=PPFD_step){
			
		//	System.out.println("OK1-2");
			
			leaf.PPFD = PPFD;
			
			if("C3".equals(Constants.C3orC4))
				leaf.cal_c3(env);
			else if("C4".equals(Constants.C3orC4)){
				// run 
				leaf.cal_c4_Von(env);
			//	System.out.println(leaf.A);  // A is NaN
			}
			else{
				//error
			}
			lr.xys_A.add(PPFD, leaf.A);
			
				// function to calculate quantum yield of the leaf in a given conditions of T, CO2,
		    	// QY = ( A(ppfd=50) - A(ppfd=0) )  / (100-50);
		    // calculation of QY	
			leaf.PPFD = 50;   
			
			if("C3".equals(Constants.C3orC4))
				leaf.cal_c3(env);
			else if("C4".equals(Constants.C3orC4)){
				leaf.cal_c4_Von(env);
			}
			double A50 = leaf.A;
		    
			leaf.PPFD = 0;    
		    if("C3".equals(Constants.C3orC4))
				leaf.cal_c3(env);
			else if("C4".equals(Constants.C3orC4)){
				leaf.cal_c4_Von(env);
			}
		    
		    double A0 = leaf.A;
		    
		    leaf.QY = (A50 - A0) / 50;
		    
		    leaf.LCP = -A0/leaf.QY; 
		 	// LCP = - A(ppfd=0) / QY;
			
			lr.xys_QY.add(PPFD, leaf.QY);
			lr.xys_LCP.add(PPFD, leaf.LCP);
		}
		return lr;
	}
	public LeafRes A_CO2_curve (String name,Environment env, Leaf leaf, double CO2_start, double CO2_end, double CO2_step){
		LeafRes lr = new LeafRes(name);
		
		for (double CO2=CO2_start; CO2<=CO2_end; CO2+=CO2_step){
			leaf.Cs = CO2;
			env.air.CO2_concentration = CO2;
			if("C3".equals(Constants.C3orC4)){
                            System.out.println("leaf.PPFD "+leaf.PPFD);
				leaf.cal_c3(env);
                        }
			else if("C4".equals(Constants.C3orC4))
				leaf.cal_c4_Von(env);
                        else{}
				//error
				
			lr.xys_A.add(CO2, leaf.A);
			
			
			double old_PPFD = leaf.PPFD;
			// function to calculate quantum yield of the leaf in a given conditions of T, CO2,
	    	// QY = ( A(ppfd=50) - A(ppfd=0) )  / (100-50);
	    // calculation of QY	
		leaf.PPFD = 50;   
		
		if("C3".equals(Constants.C3orC4))
			leaf.cal_c3(env);
		else if("C4".equals(Constants.C3orC4)){
			leaf.cal_c4_Von(env);
		}
		double A50 = leaf.A;
	    
		leaf.PPFD = 0;    
	    if("C3".equals(Constants.C3orC4))
			leaf.cal_c3(env);
		else if("C4".equals(Constants.C3orC4)){
			leaf.cal_c4_Von(env);
		}
	    
	    double A0 = leaf.A;
	    
	    leaf.QY = (A50 - A0) / 50;
	    
	    leaf.LCP = -A0/leaf.QY; 
	 	// LCP = - A(ppfd=0) / QY;
			
			
			lr.xys_QY.add(CO2, leaf.QY);
			lr.xys_LCP.add(CO2, leaf.LCP);
			
			leaf.PPFD = old_PPFD;
		}
		return lr;
	}
	
	public LeafRes A_Temperature_curve (String name, Environment env, Leaf leaf, double T_start, double T_end, double T_step){
		LeafRes lr = new LeafRes(name);
		
		for (double T=T_start; T<=T_end; T+=T_step){
			leaf.T = T;
                        env.air.current_T=T;
                        leaf.Tair=T;
			
                        
                       
			if("C3".equals(Constants.C3orC4))
				leaf.cal_c3(env);
			else if("C4".equals(Constants.C3orC4))
				leaf.cal_c4_Von(env);
			
				
			lr.xys_A.add(T, leaf.A);
			
			double old_PPFD = leaf.PPFD;
			// function to calculate quantum yield of the leaf in a given conditions of T, CO2,
	    	// QY = ( A(ppfd=50) - A(ppfd=0) )  / (100-50);
	    // calculation of QY	
		leaf.PPFD = 50;  
		
		if("C3".equals(Constants.C3orC4))
			leaf.cal_c3(env);
		else if("C4".equals(Constants.C3orC4)){
			leaf.cal_c4_Von(env);
		}
		double A50 = leaf.A;
	    
		leaf.PPFD = 0;   
	    if("C3".equals(Constants.C3orC4))
			leaf.cal_c3(env);
		else if("C4".equals(Constants.C3orC4)){
			leaf.cal_c4_Von(env);
		}
	    
	    double A0 = leaf.A;
	    
	    leaf.QY = (A50 - A0) / 50;
	    
	    leaf.LCP = -A0/leaf.QY; 
	 	// LCP = - A(ppfd=0) / QY;
			
	    
			lr.xys_QY.add(T, leaf.QY);
			lr.xys_LCP.add(T, leaf.LCP);
			leaf.PPFD = old_PPFD;
		}
		return lr;
	}

}


