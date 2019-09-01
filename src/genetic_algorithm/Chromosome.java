package genetic_algorithm;

public class Chromosome {
	int[] chromosome ;


/**
 * Initialize a random sequence of 0-1 values: the genes of a Chromosome. 
 * The instance_dimension value is equal to the sequence lenght of the Chromosome.
 * @param instance_dimension
 * @return Chromosome
 */
public static Chromosome buildChromosome(int instance_dimension) {
	
	Chromosome x = new Chromosome();
	x.chromosome = new int[instance_dimension];
	for (int i=0; i<instance_dimension; i++) {
		x.chromosome[i]=(int)Math.round(Math.random());
	}
	return x;
}


/**Initialize a population of population_dimension Chromosomes whose genes lenght is equal to instance_dimension
 * @param population_dimension
 * @param instance_dimension
 * @return Chromosome []
 * 
 */
public static Chromosome[] buildChromosomePopulation(int population_dimension, int instance_dimension) {
	
	Chromosome[] population = new Chromosome[population_dimension];
	for(int i=0; i<population_dimension; i++) {
		population[i]= buildChromosome(instance_dimension);
		
	}
	return population;
}

/**Given a population of Chromosomes, list all the genes of each chromosome in a fancy way 
 * @param population
 */
public static void printPopulation(Chromosome[] population) {

	for (int i=0; i<population.length; i++) {
		System.out.print(i+1);
		System.out.print(": ");
		for (int j=0; j<population[i].chromosome.length;j++) {
			System.out.print("["+population[i].chromosome[j]+"]");
		}
		System.out.print("\n");
	}
}  
/**Given a population of Chromosomes, list all the genes of each Chromosome in a fancy way with information about 
 * profit and total weight
 * @param population
 * @param profits
 * @param weights
 */
public static void printPopulation(Chromosome[] population, int[] profits, int [] weights) {
	int total_profit;
	int total_weight;
	for (int i=0; i<population.length; i++) {
		total_profit = 0;
		total_weight = 0;
		System.out.print(i+1);
		System.out.print(": ");
		for (int j=0; j<population[i].chromosome.length;j++) {
			System.out.print("["+population[i].chromosome[j]+"]");
			total_profit += profits[j]*population[i].chromosome[j];
			total_weight += weights[j]*population[i].chromosome[j];
		}
		System.out.print(" P: "+total_profit+" ; W: "+total_weight+"\n");
	}
}

/**Given a population of Chromosomes, randomly modificy each Chromosome for being feasibile to the capacity
 * constraint of the Knapsack Problem
 * @param population
 * @param weights
 * @param capacity
 * @return Chromosome []
 */
public static Chromosome[] validateChromosomePopulation(Chromosome[] population, int[] weights, int capacity) {
	
	for (int i=0; i<population.length; i++) {
		int totalweight = 0;
		for (int j=0; j<population[i].chromosome.length;j++) {
			totalweight += population[i].chromosome[j]*weights[j];
			}
		while (totalweight > capacity) {
			totalweight = 0;
			for (int j=0; j<population[i].chromosome.length;j++) {
				totalweight += population[i].chromosome[j]*weights[j];
				}
			population[i] = validateChromosome(population[i],weights,capacity);
		}
	}
	return population;
	
}

/**Given a Chromosome, randomly modificy it for being feasibile to the capacity
 * constraint of the Knapsack Problem
 * @param wrong_chromosome
 * @param weights
 * @param capacity
 * @return Chromosome
 */
public static Chromosome validateChromosome(Chromosome wrong_chromosome, int [] weights, int capacity) {
	boolean changed = false;
	while (!changed) {
		int position = (int)Math.round(Math.random()*(wrong_chromosome.chromosome.length-1));
		if (wrong_chromosome.chromosome[position] == 1) {
			wrong_chromosome.chromosome[position] = 0;
			changed=true;
		}
	}
	return wrong_chromosome;
}

}
