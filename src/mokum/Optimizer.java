package mokum;

import java.util.Arrays;

public class Optimizer {
	
	public static Dienstregeling Optimize(int population_size, int max_iterations, double survive_ratio){	//aanroepen als bijv Optimize(100,100,0.25)
		Dienstregeling[] population = new Dienstregeling[population_size]; 	// maak een array van dienstregelingen aan
		int current_population_size = 0;
				
		for(int i=0; i<max_iterations; i++){
			
			for(int j=current_population_size; j<population_size; j++){	
				population[j] = new Dienstregeling(true);
				current_population_size++;
			}
			
			Arrays.sort(population); //sorteer population
						
			current_population_size = (int) Math.ceil(current_population_size * survive_ratio); //ZOMBIE APOCALYPSE!!! Laat enkel de survivors over
			
		}
		return population[0];
	}
}