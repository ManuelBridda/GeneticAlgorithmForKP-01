package genetic_algorithm;

public class Main {

	public static void main(String[] args) {
		// Instance Parameters
		int population_dimension = 100;
		int number_of_breed =50 ; //2 children per breed
		int stop_iteration = 2000;
		int[][] instance = {  
	//{Profit, Weight}
		{100, 995},
		{94, 485},
		{506, 326},
		{416, 248},
		{992, 421},
		{649, 322},
		{237, 795},
		{457, 43},
		{815, 845},
		{446, 955},
		{422, 252},
		{791, 9},
		{359, 901},
		{667, 122},
		{598, 94},
		{7, 738},
		{544, 574},
		{334, 715},
		{766, 882},
		{994, 367},
		{893, 984},
		{633, 299},
		{131, 433},
		{428, 682},
		{700, 72},
		{617, 874},
		{874, 138},
		{720, 856},
		{419, 145},
		{794,995},
		{196, 529},
		{997, 199},
		{116,277},
		{908, 97},
		{539, 719},
		{707, 242},
		{569, 107},
		{537, 122},
		{931, 70},
		{726, 98},
		{487, 600},
		{772, 645},
		{513, 267},
		{81, 972},
		{943, 895},
		{58, 213},
		{303, 748},
		{764, 487},
		{536, 923},
		{724, 29},
		{789, 674},
		{479, 540},
		{142, 554},
		{339, 467},
		{641, 46},
		{196, 710},
		{494, 553},
		{66, 191},
		{824,724},
		{208, 730},
		{711, 988},
		{800, 90},
		{314, 340},
		{289, 549},
		{401, 196},
		{466, 865},
		{689, 678},
		{833, 570},
		{225, 936},
		{244, 722},
		{849, 651},
		{113, 123},
		{379, 431},
		{361, 508},
		{65,585},
		{486, 853},
		{686, 642},
		{286, 992},
		{889, 725},
		{24, 286},
		{491, 812},
		{891, 859},
		{90, 663},
		{181, 88},
		{214, 179},
		{17, 187},
		{472, 619},
		{418, 261},
		{419, 846},
		{356, 192},
		{682, 261},
		{306, 514},
		{201, 886},
		{385, 530},
		{952, 849},
		{500, 294},
		{194, 799},
		{737, 391},
		{324, 330},
		{992, 298},
		{224, 790}};
		
		instance = Chromosome.sortInstance(instance);
		/*for(int i=0; i<instance.length;i++){
			System.out.println("["+instance[i][0]+","+instance[i][1]+"]"+instance[i][0]/instance[i][1]);
		}*/
		int [] profits=new int[instance.length];
		int [] weights=new int[instance.length];
		for(int i=0; i< instance.length;i++) {
			profits[i]=instance[i][0];
			weights[i]=instance[i][1];
		}
		int capacity = 1000;
		
		//Computations
		Chromosome.resolveProblem(population_dimension, instance.length, profits, weights, capacity, number_of_breed, stop_iteration);
		
	}
	
}
