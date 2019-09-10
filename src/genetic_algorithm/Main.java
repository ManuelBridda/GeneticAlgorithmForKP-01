package genetic_algorithm;

public class Main {

	public static void main(String[] args) {
		// Instance Parameters
		
		int instance_dimension = 20;
		int population_dimension = 1000;
		int child_per_breed =50;
		int stop_iteration = 200;
		
		int [] profits= {16, 30, 40, 28, 14, 36, 33, 37, 46, 13, 34, 45, 5, 29, 39, 41, 42, 9, 15, 31};
		int [] weights = {4, 10 , 2, 4, 2, 7, 7, 1, 1, 2, 3, 5, 5, 3, 2, 2, 5, 9, 2, 2};
		int capacity = 31;
		
		
		
		//Computations
		Chromosome.resolveProblem(population_dimension, instance_dimension, profits, weights, capacity, child_per_breed, stop_iteration);
		
	}
	
}
