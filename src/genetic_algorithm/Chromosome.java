package genetic_algorithm;

public class Chromosome {
	int[] chromosome ;
	int profit=0;
	int weight=0;
	float range_lower_bound;
	float range_upper_bound;


/**Initialize a random sequence of 0-1 values: the genes of a Chromosome. 
 * The instance_dimension value is equal to the sequence lenght of the Chromosome.
 * @param instance_dimension
 * @param profits
 * @param weights
 * @return Chromosome
 */
public static Chromosome buildChromosome(int instance_dimension, int[] profits, int[] weights) {
	
	Chromosome x = new Chromosome();
	x.chromosome = new int[instance_dimension];
	for (int i=0; i<instance_dimension; i++) {
		x.chromosome[i]=(int)Math.round(Math.random());
	}
	for(int i=0; i<instance_dimension; i++) {
		x.weight+=x.chromosome[i]*weights[i];
		x.profit+=x.chromosome[i]*profits[i];
	}
	return x;
}

/**Initialize a population of population_dimension Chromosomes whose genes lenght is equal to instance_dimension
 * @param population_dimension
 * @param instance_dimension
 * @param profits
 * @param weights
 * @return Chromosome []
 * 
 */
public static Chromosome[] buildChromosomePopulation(int population_dimension, int instance_dimension, int[] profits, int[] weights) {
	
	Chromosome[] population = new Chromosome[population_dimension];
	for(int i=0; i<population_dimension; i++) {
		population[i]= buildChromosome(instance_dimension, profits, weights);
		
	}
	return population;
}

/**Given a population of Chromosomes, list all the genes of each Chromosome in a fancy way with information about 
 * profit and total weight
 * @param population
 */
public static void printPopulation(Chromosome[] population) {
	for (int i=0; i<population.length; i++) {
		System.out.print(i+1);
		System.out.print(": ");
		for (int j=0; j<population[i].chromosome.length;j++) {
			System.out.print("["+population[i].chromosome[j]+"]");
		}
		System.out.print(" P: "+population[i].profit+" ; W: "+population[i].weight+"\n");
	}
}

/**Given a population of Chromosomes, randomly modificy each Chromosome for being feasibile to the capacity
 * constraint of the Knapsack Problem
 * @param population
 * @param profits
 * @param weights
 * @param capacity
 * @return Chromosome []
 */
public static Chromosome[] validateChromosomePopulation(Chromosome[] population, int[] profits, int[] weights, int capacity) {
	
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
			population[i] = validateChromosome(population[i],profits,weights,capacity);
		}
	}
	return population;
	
}

/**Given a Chromosome, randomly modify it for being feasible to the capacity
 * constraint of the Knapsack Problem
 * @param wrong_chromosome
 * @param profits
 * @param weights
 * @param capacity
 * @return Chromosome
 */
public static Chromosome validateChromosome(Chromosome wrong_chromosome, int[] profits, int [] weights, int capacity) {
	boolean changed = false;
	while (!changed) {
		int position = (int)Math.round(Math.random()*(wrong_chromosome.chromosome.length-1));
		if (wrong_chromosome.chromosome[position] == 1) {
			wrong_chromosome.chromosome[position] = 0;
			changed=true;
		}
		wrong_chromosome.weight=0;
		wrong_chromosome.profit=0;
		
		for(int i=0; i<wrong_chromosome.chromosome.length; i++) {
			wrong_chromosome.weight+=wrong_chromosome.chromosome[i]*weights[i];
			wrong_chromosome.profit+=wrong_chromosome.chromosome[i]*profits[i];
		}
	}
	return wrong_chromosome;
}

/**Given a population, defines a range for each chromosome. This process is needed for the Roulette Wheel Selection
 * @param population
 */
public static void defineChromosomesRanges(Chromosome[] population) {
	float total_profit = 0;
	for(int i=0; i<population.length; i++) {
		total_profit+=population[i].profit;
	}
	population[0].range_lower_bound=0;
	population[0].range_upper_bound=(population[0].profit/total_profit);
	//System.out.println("["+population[0].range_lower_bound+","+population[0].range_upper_bound+"]"); //debug only
	for (int i=1; i<population.length; i++) {
		population[i].range_lower_bound=population[i-1].range_upper_bound;
		population[i].range_upper_bound=population[i].range_lower_bound+population[i].profit/total_profit;
	//System.out.println("["+population[i].range_lower_bound+","+population[i].range_upper_bound+"]");  //debug only
	}
}



/*public static Chromosome[] sortPopulation(Chromosome[] population, int [] profits) {
	int [] total_profit = new int[population.length];
	for(int i=0; i<population.length; i++) {
		tota
		l_profit[i] =0;
		for (int j=0; j<population[i].chromosome.length;j++) {
			total_profit[i] += population[i].chromosome[j]*profits[j];
			}
	}
	//Arrays.sort
	return population;
}*/


}
