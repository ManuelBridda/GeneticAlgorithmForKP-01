package genetic_algorithm;

public class Main {

	public static void main(String[] args) {
		// Instance Parameters
		
		int instance_dimension = 20;
		int population_dimension = 100;
		int child_per_breed =50;
		
		int [] profits= {16, 30, 40, 28, 14, 36, 33, 37, 46, 13, 34, 45, 5, 29, 39, 41, 42, 9, 15, 31};
		int [] weights = {4, 10 , 2, 4, 2, 7, 7, 1, 1, 2, 3, 5, 5, 3, 2, 2, 5, 9, 2, 2};
		int capacity = 30;
		
		
		
		//Computations
		Chromosome[] population = Chromosome.buildChromosomePopulation(population_dimension,instance_dimension,profits,weights);
		population = Chromosome.sortPopulation(population);
		Chromosome.printPopulation(population);
		System.out.println("");
		population = Chromosome.validateChromosomePopulation(population, profits, weights, capacity);
		population = Chromosome.sortPopulation(population);
		Chromosome.printPopulation(population);
		System.out.println("");
		Chromosome.defineChromosomesRanges(population);
		//Chromosome.printPopulation(Chromosome.breedPopulation(population, child_per_breed, profits, weights, capacity));
		population = Chromosome.breedPopulation(population, child_per_breed, profits, weights, capacity);
		Chromosome.printPopulation(population);
	}

}
