package genetic_algorithm;
import java.util.*; 

public class Chromosome {
	int[] chromosome ; 
	int profit=0;
	int weight=0;
	float range_lower_bound;
	float range_upper_bound;

	
/**Given a Knapsack instance, return it ordered by density (profit/weight)
 * @param instance
 * @return sorted population
 */
public static int[][] sortInstance(int[][] instance) {
	double[][] instancedoubled = new double[instance.length][2];
	for(int i=0; i<instance.length;i++) {
		instancedoubled[i][0]=instance[i][0];
		instancedoubled[i][1]=instance[i][1];
	}
		List<double[]> instance_list = new ArrayList<double[]> (Arrays.asList(instancedoubled));
		Collections.sort(instance_list,(a1, a2) -> Double.compare(a1[1]/a1[0],(a2[1]/a2[0])));
		int[][] sortedInstance= new int[instance.length][2];
		for (int i=0;i<instance.length;i++) {
			sortedInstance[i][0]=(int)instance_list.get(i)[0];
			sortedInstance[i][1]=(int)instance_list.get(i)[1];
		}
		return sortedInstance;
		
}

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

/**Initialize a population of population_dimension Chromosomes whose genes length is equal to instance_dimension
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
	System.out.println("");
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

/**Given a population of Chromosomes, randomly modify each Chromosome for being feasible to the capacity
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
			population[i] = validateChromosomeGreedy(population[i],profits,weights); //works only with a set ordered by density
			//population[i] = validateChromosomeRandom(population[i],profits,weights); //works with any set
		}
		//optional
		population[i]=addBestItem(population[i], profits, weights, capacity); //works only with a set ordered by density
		
		
	}
	return population;
	
}

public static Chromosome addBestItem(Chromosome chromosome,int [] profits, int [] weights, int capacity) {
	int totalweight = 0;
	for (int j=0; j<chromosome.chromosome.length;j++) {
		totalweight += chromosome.chromosome[j]*weights[j];
		}
	int residual_capacity = capacity-totalweight;
	for (int i=0; i<chromosome.chromosome.length;i++)
		if(weights[i]<=residual_capacity && chromosome.chromosome[i]==0) {
			chromosome.chromosome[i]=1;
			chromosome.profit+=profits[i];
			chromosome.weight+=weights[i];
			break;
		}
	return chromosome;
}

/**Given a Chromosome, randomly modify it for being feasible to the capacity
 * constraint of the Knapsack Problem
 * @param wrong_chromosome
 * @param profits
 * @param weights
 * @param capacity
 * @return Chromosome
 */
public static Chromosome validateChromosomeRandom(Chromosome wrong_chromosome, int[] profits, int [] weights) {
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

/**Given a Chromosome, randomly modify it for being feasible to the capacity
 * 
 * @param wrong_chromosome
 * @param profits
 * @param weights
 * @return
 */
public static Chromosome validateChromosomeGreedy(Chromosome wrong_chromosome, int[] profits, int [] weights) {
	
	for(int i=wrong_chromosome.chromosome.length-1; i >=0; i--) {
		if (wrong_chromosome.chromosome[i]==1) {
			wrong_chromosome.chromosome[i]=0;
			break;
		}
		
	}
	wrong_chromosome.weight=0;
	wrong_chromosome.profit=0;
	
	for(int i=0; i<wrong_chromosome.chromosome.length; i++) {
		wrong_chromosome.weight+=wrong_chromosome.chromosome[i]*weights[i];
		wrong_chromosome.profit+=wrong_chromosome.chromosome[i]*profits[i];
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
	population[population.length-1].range_upper_bound=1;
}

/**Given the instance parameters, perform Roulette Wheel Selection, cross_over, mutation, validation on the current population and returns the population with children
 * @param population
 * @param number_of_breed
 * @param profits
 * @param weights
 * @param capacity
 * @return new_population
 */
public static Chromosome[] breedPopulation(Chromosome[] population, int number_of_breed, int[] profits, int[] weights, int capacity) {
	
	Chromosome[] new_population = new Chromosome[population.length+number_of_breed*2];
	for (int i=0; i< population.length; i++) {
		new_population[i] = population[i];
	}
	for (int j=population.length; j<population.length+(number_of_breed*2)-1;j=j+2) {
		Random rd1 = new Random();
		Random rd2 = new Random();
		float random_number1 = rd1.nextFloat();
		float random_number2 =rd2.nextFloat();
		/**For some reasons, sometimes, parent1 or parent2 are not defined
		 * To solve this parent1 and 2 are just created randomly and are overwrited after.
		 */
		Chromosome parent1 = new Chromosome(); //buildChromosome(population[0].chromosome.length, profits, weights);
		Chromosome parent2 = new Chromosome();//buildChromosome(population[0].chromosome.length, profits, weights);
		for (int i=0; i< population.length; i++) {
			if (population[i].range_lower_bound<=random_number1 && population[i].range_upper_bound>random_number1)
				copyChromosome(parent1,population[i]);
			if (population[i].range_lower_bound<=random_number2 && population[i].range_upper_bound>random_number2)
				copyChromosome(parent2,population[i]);
			}
		Chromosome[] children = new Chromosome[2];
		children = crossOverandMutate(parent1,parent2);
		new_population[j]=children[0];
		new_population[j+1]=children[1];
		new_population[j].weight=0;
		new_population[j].profit=0;
		new_population[j+1].weight=0;
		new_population[j+1].profit=0;
		try{for(int i=0;i<new_population[j].chromosome.length;i++) {
			new_population[j].weight+=new_population[j].chromosome[i]*weights[i];
			new_population[j].profit+=new_population[j].chromosome[i]*profits[i];
			new_population[j+1].weight+=new_population[j+1].chromosome[i]*weights[i];
			new_population[j+1].profit+=new_population[j+1].chromosome[i]*profits[i];
			}
		}
	catch(NullPointerException e) {
		System.out.println(population[population.length-1].range_lower_bound);
		System.out.println(population[population.length-1].range_upper_bound);
		System.out.println(random_number1);
		System.out.println(random_number2);
		printChromosome(parent1);
		printChromosome(parent2);
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
public static Chromosome[] crossOverandMutate(Chromosome parent1, Chromosome parent2) {
	Chromosome[] child = new Chromosome[2];
	child[0] = new Chromosome ();
	child[1] = new Chromosome ();
	try {
		int crossing_point = ((int)(Math.round(Math.random()*(parent1.chromosome.length-2))))+1;
		//System.out.println(crossing_point);
		
		child[0].chromosome = new int[parent1.chromosome.length];
		child[1].chromosome = new int[parent1.chromosome.length];
		for(int i=0; i<crossing_point; i++) {
			child[0].chromosome[i]=parent1.chromosome[i];
			child[1].chromosome[i]=parent2.chromosome[i];
		}
		for(int i=crossing_point;i<parent1.chromosome.length;i++) {
			child[0].chromosome[i]=parent2.chromosome[i];
			child[1].chromosome[i]=parent1.chromosome[i];
		}
		boolean changed = false;
		while (!changed) {
			int position = (int)Math.round(Math.random()*(child[0].chromosome.length-1));
			if (child[0].chromosome[position] == 0) {
				child[0].chromosome[position] = 1;
				changed=true;
			}
		}
		changed = false;
		while (!changed) {
			int position = (int)Math.round(Math.random()*(child[1].chromosome.length-1));
			if (child[1].chromosome[position] == 0) {
				child[1].chromosome[position] = 1;
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
 * @param number_of_breed
 * @param stop_iteration
 */
public static void resolveProblem(int population_dimension, int instance_dimension, int[] profits, int[] weights, int capacity, int number_of_breed, int stop_iteration) {
	int counter = 1;
	int best_solution_so_far=0;
	Chromosome[] population = Chromosome.buildChromosomePopulation(population_dimension,instance_dimension,profits,weights);
	population = Chromosome.validateChromosomePopulation(population, profits, weights, capacity);
	population = Chromosome.sortPopulation(population);
	best_solution_so_far=population[0].profit;
	System.out.println("Initial solution: "+best_solution_so_far);


	while(counter<stop_iteration) {
		Chromosome.defineChromosomesRanges(population);
		population = Chromosome.breedPopulation(population, number_of_breed, profits, weights, capacity);
		//Chromosome.printPopulation(population);
		if(best_solution_so_far == population[0].profit) {
			counter++;
			}
		else {
			best_solution_so_far = population[0].profit;
			System.out.println("Best solution so far = "+best_solution_so_far+" after "+counter+" iterations");
			counter=1;
			//printPopulation(population);
			}
		}
	System.out.println("<<Stopped after "+counter+ " iterations without solution's value improvement>>\n");
	System.out.println("The best solution found is: "+best_solution_so_far+" with a weight of: "+population[0].weight);
	Chromosome.printChromosome(population[0]);
	}
}
