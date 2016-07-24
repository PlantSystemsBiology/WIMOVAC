package model;


public class Environment {
	
	public Air air;
	public Light light;
	public Soil soil;
	public Solar solar;
	public double wind_speed = 1;
	public double rain_fall = 0;
	
	
	//construction
	public Environment(){
		air 	= new Air();
		light 	= new Light();
//		soil 	= new Soil();
		solar 	= new Solar();

	}
	public Environment(CurrentTime ct, Location lct){

		calculate_Env(ct, lct);
	}
	
	public void calculate_Env (CurrentTime ct, Location lct){
		air = new Air(ct, lct);
		solar = new Solar(ct, lct);
		light = new Light(solar);

	}
	
	public void calculate_Env (CurrentTime ct, Location lct, Plant plant){
		air = new Air(ct, lct);
		solar = new Solar(ct, lct);
		light = new Light(solar);
//		soil = new Soil(plant, ct, lct);

	}
	
	public void update(CurrentTime ct, Location lct){
		air.update(ct, lct);
		solar.update(ct, lct);
		light.update(solar);
	}

	
	//
	
	
	
	
	
	

}
