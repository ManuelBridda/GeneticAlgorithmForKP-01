package genetic_algorithm;
import java.util.*; 

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

/**Given a chromosome, prints all the genes in a fancy way
 * @param chromosome
 */
public static void printChromosome(Chromosome chromosome) {
	for (int j=0; j<chromosome.chromosome.length;j++) {
		System.out.print("["+chromosome.chromosome[j]+"]");
	}
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

/**Given the instance parameters, perform Roulette Wheel Selection, cross_over, mutation, validation on the current population and returns the population with childs
 * @param population
 * @param child_number
 * @param profits
 * @param weights
 * @param capacity
 * @return new_population
 */
public static Chromosome[] breedPopulation(Chromosome[] population, int child_number, int[] profits, int[] weights, int capacity) {
	
	Chromosome[] new_population = new Chromosome[population.length+child_number];
	for (int i=0; i< population.length; i++) {
		new_population[i] = population[i];
	}
	for (int j=population.length; j<population.length+child_number;j++) {
		double random_number1 = Math.random();
		double random_number2 = Math.random();
		Chromosome parent1 = new Chromosome();
		Chromosome parent2 = new Chromosome();
		for (int i=0; i< population.length; i++) {
			if (population[i].range_lower_bound<=random_number1 && population[i].range_upper_bound>=random_number1)
				copyChromosome(parent1,population[i]);
			if (population[i].range_lower_bound<=random_number2 && population[i].range_upper_bound>=random_number2)
				copyChromosome(parent2,population[i]);
			}
		new_population[j]=crossOverandMutate(parent1,parent2);
		new_population[j].weight=0;
		new_population[j].profit=0;
		for(int i=0;i<new_population[j].chromosome.length;i++) {
			new_population[j].weight+=new_population[j].chromosome[i]*weights[i];
			new_population[j].profit+=new_population[j].chromosome[i]*profits[i];
			}	
	}
	validateChromosomePopulation(new_population, profits, weights, capacity);
	return reducePopulation(new_population, population.length);
	}

/**Copy the chromosome property of a source in the target
 * @param target
 * @param source
 */
public static void copyChromosome(Chromosome target, Chromosome source) {
	target.chromosome = new int[source.chromosome.length];
	for (int i=0; i<source.chromosome.length;i++) {
		target.chromosome[i]=source.chromosome[i];
	}
}

/**Given two parents, perform the cross_over phase in a random cross_point and get a child, then mutate the child by adding 1 in a random way
 * @param parent1
 * @param parent2
 * @return
 */
public static Chromosome crossOverandMutate(Chromosome parent1, Chromosome parent2) {
	Chromosome child = new Chromosome();
	try {
		int crossing_point = ((int)(Math.round(Math.random()*(parent1.chromosome.length-2))))+1;
		//System.out.println(crossing_point);
		
		child.chromosome = new int[parent1.chromosome.length];
		for(int i=0; i<crossing_point; i++) {
			child.chromosome[i]=parent1.chromosome[i];
		}
		for(int i=crossing_point;i<parent1.chromosome.length;i++) {
			child.chromosome[i]=parent2.chromosome[i];
		}
		boolean changed = false;
		while (!changed) {
			int position = (int)Math.round(Math.random()*(child.chromosome.length-1));
			if (child.chromosome[position] == 0) {
				child.chromosome[position] = 1;
				changed=true;
			}
		}
	}
catch (Exception e) {
	}
	return child;
}

/**Given a population, this method sort it
 * @param population
 * @return
 */
public static Chromosome[] sortPopulation(Chromosome[] population) {
	List<Chromosome> population_list = new ArrayList<Chromosome> (Arrays.asList(population));
	Collections.sort(population_list,(a1, a2) -> a1.profit-a2.profit);
	Collections.reverse(population_list);
	Chromosome[] sortedPopulation= new Chromosome[population.length];
	for (int i=0;i<population.length;i++) {
		sortedPopulation[i] = new Chromosome();
		sortedPopulation[i].chromosome = new int[population[i].chromosome.length];
		sortedPopulation[i].profit=population_list.get(i).profit;
		sortedPopulation[i].weight=population_list.get(i).weight;
		for (int j=0; j<population[i].chromosome.length;j++) {
			sortedPopulation[i].chromosome[j]=population_list.get(i).chromosome[j];
		}
	}
	return sortedPopulation;
	
}

/**Given a population of length greater than population_dimension, return a population of population
 * -dimension chromosomes
 * @param population
 * @param population_dimension
 * @return
 */
public static Chromosome[] reducePopulation(Chromosome[] population, int population_dimension) {
	List<Chromosome> population_list = new ArrayList<Chromosome> (Arrays.asList(population));
	Collections.sort(population_list,(a1, a2) -> a1.profit-a2.profit);
	Collections.reverse(population_list);
	Chromosome[] reducedPopulation= new Chromosome[population_dimension];
	for (int i=0;i<population_dimension;i++) {
		reducedPopulation[i] = new Chromosome();
		reducedPopulation[i].chromosome = new int[population[i].chromosome.length];
		reducedPopulation[i].profit=population_list.get(i).profit;
		reducedPopulation[i].weight=population_list.get(i).weight;
		for (int j=0; j<population[i].chromosome.length;j++) {
			reducedPopulation[i].chromosome[j]=population_list.get(i).chromosome[j];
			}
		}
	return reducedPopulation;
	}

/**The resolver's core. Given the inputs it resolves the instance by building, breeding and selecting an initial population
 * @param population_dimension
 * @param instance_dimension
 * @param profits
 * @param weights
 * @param capacity
 * @param child_per_breed
 * @param stop_iteration
 */
public static void resolveProblem(int population_dimension, int instance_dimension, int[] profits, int[] weights, int capacity, int child_per_breed, int stop_iteration) {
	int counter = 0;
	int best_solution_so_far=0;
	Chromosome[] population = Chromosome.buildChromosomePopulation(population_dimension,instance_dimension,profits,weights);
	population = Chromosome.validateChromosomePopulation(population, profits, weights, capacity);
	population = Chromosome.sortPopulation(population);
	best_solution_so_far=population[0].profit;
	System.out.println("Best solution so far: "+best_solution_so_far);


	while(counter<stop_iteration) {
		Chromosome.defineChromosomesRanges(population);
		population = Chromosome.breedPopulation(population, child_per_breed, profits, weights, capacity);
		//Chromosome.printPopulation(population);
		if(best_solution_so_far == population[0].profit) {
			counter++;
			}
		else {
			best_solution_so_far = population[0].profit;
			System.out.println("Best solution so far = "+best_solution_so_far+" after "+counter+" iterations");
			counter=0;
			}
		}
	System.out.println("<<Stopped after "+counter+ " iterations without solution's value improvement>>\n");
	System.out.println("The best solution found is: "+best_solution_so_far+" with a weight of: "+population[0].weight);
	Chromosome.printChromosome(population[0]);
	}
}
