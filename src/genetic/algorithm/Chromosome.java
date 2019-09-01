package genetic.algorithm;

public class Chromosome {
	int[] chromosome ;


public static Chromosome buildChromosome(int instance_dimension) {
	
	Chromosome x = new Chromosome();
	x.chromosome = new int[instance_dimension];
	for (int i=0; i<instance_dimension; i++) {
		x.chromosome[i]=(int)Math.round(Math.random());
	}
	return x;
}

public static Chromosome[] buildChromosomePopulation(int population_dimension, int instance_dimension) {
	
	Chromosome[] population = new Chromosome[population_dimension];
	for(int i=0; i<population_dimension; i++) {
		population[i]= buildChromosome(instance_dimension);
		
	}
	return population;
}

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
