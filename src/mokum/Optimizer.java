package mokum;

import java.util.Arrays;

public class Optimizer {
	
	public static Dienstregeling Optimize(int population_size, int max_iterations, double kids_per_parent){	//aanroepen als bijv Optimize(100,100,4)
		Dienstregeling[] population = new Dienstregeling[population_size]; 	//maak een array van dienstregelingen aan
		double survive_ratio = 1/kids_per_parent; // survive_ratio wordt aangepast om plek te maken voor de kids in de populatie
		int survivors_each_iteration = (int) Math.ceil(population_size * survive_ratio);
		int current_population_size = 0;
		Dienstregeling best_dienstregeling = new Dienstregeling();
		int score_best_dienstregeling = 0;
		int best_found_during_iteration = 0;
	
				
		for(int i=0; i<max_iterations; i++){
			
			if(current_population_size==0){ //indien er nog geen populatie bestaat
				//creëer initiele populatie
				for(int j=0; j<population_size; j++){	
					population[j] = new Dienstregeling(true);
					current_population_size++;
				}
			}
			else{ //als er al wel een populatie bestaat
				//maak kids aan de hand van de survivors van vorige iteratie
				current_population_size = (int) (current_population_size * survive_ratio);
				for(int j=0; j<survivors_each_iteration; j++){
					for(int k=0; k<kids_per_parent; k++){
						population[current_population_size%population_size] = population[j];
						population[current_population_size%population_size].wijzigRandomLandingVanRandomVliegtuig();
						current_population_size++;
					}
				}
			}
			
			Arrays.sort(population); //sorteer population
						
			current_population_size = survivors_each_iteration; //ZOMBIE APOCALYPSE!!! Laat enkel de survivors over
			
			if(population[0].telPassagiersKilometers()>score_best_dienstregeling){
				score_best_dienstregeling = population[0].telPassagiersKilometers();
				best_dienstregeling = new Dienstregeling(population[0]);
				best_found_during_iteration = i;
			}
			
			System.out.println(i + " - DEBUG >> winner: " + score_best_dienstregeling + " - current: " + population[0].telPassagiersKilometers() + " - found during iteration: " + best_found_during_iteration + "/" + max_iterations);
		}
		
		return best_dienstregeling;
	}
	
	
	public static Dienstregeling Optimize2(int population_size, int max_iterations, double survive_ratio, double elite_ratio){	//aanroepen als bijv Optimize(100,100,0.25)
		Dienstregeling[] population = new Dienstregeling[population_size]; 	//maak een array van dienstregelingen aan
		int survivors_each_iteration = (int) Math.ceil(population_size * survive_ratio);
		int elite_population = (int) Math.ceil(population_size * elite_ratio);
		int current_population_size = 0;
		Dienstregeling best_dienstregeling = new Dienstregeling();
		int score_best_dienstregeling = 0;
		int best_found_during_iteration = 0;
	
				
		for(int i=0; i<max_iterations; i++){
			
			if(current_population_size==0){ //indien er nog geen populatie bestaat
				//creëer initiele populatie
				for(int j=0; j<population_size; j++){	
					population[j] = new Dienstregeling(true);
					current_population_size++;
				}
			}
			else{ //als er al wel een populatie bestaat
				//maak nieuwe kids aan
				current_population_size = survivors_each_iteration;
				for(int j=survivors_each_iteration; j<population_size; j++){
					population[j] = population[j%survivors_each_iteration];
					population[j].wijzigRandomLandingVanRandomVliegtuig();
					current_population_size++;
				}
				for(int j=elite_population; j<survivors_each_iteration; j++){
					population[j].wijzigRandomLandingVanRandomVliegtuig();
					current_population_size++;
				}
			}
			
			Arrays.sort(population); //sorteer population
						
			current_population_size = survivors_each_iteration; //ZOMBIE APOCALYPSE!!! Laat enkel de survivors over
			
			if(population[0].telPassagiersKilometers()>score_best_dienstregeling){
				score_best_dienstregeling = population[0].telPassagiersKilometers();
				best_dienstregeling = new Dienstregeling(population[0]);
				best_found_during_iteration = i;
			}
			
			System.out.println(i + " - DEBUG >> winner: " + score_best_dienstregeling + " - current: " + population[0].telPassagiersKilometers() + " - found during iteration: " + best_found_during_iteration + "/" + max_iterations);
		}
		
		return best_dienstregeling;
	}
}
