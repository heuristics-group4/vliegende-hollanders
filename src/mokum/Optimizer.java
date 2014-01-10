package mokum;

import java.util.Arrays;

public class Optimizer {
	private Dienstregeling[] population;
	private int current_population_size;
	
	public Dienstregeling Optimize(int population_size, int max_iterations, double survive_ratio){ //aanroepen als bijv Optimize(100,100,0.25)
		population = new Dienstregeling[population_size]; 	// maak een array van dienstregelingen aan
		current_population_size = 0;
				
		for(int i=0; i<max_iterations; i++){
			
			for(int j=current_population_size; j<population_size; j++){	
				population[i] = new Dienstregeling(true);
			}
			//sorteer population
			
			
			//kill (1-survive_ratio)*population	
			
			
			
		}
		return population[0];
	}
}