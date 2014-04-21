import java.util.ArrayList;
import java.util.Random;


public class GA {
	ArrayList<Chromasome> population = new ArrayList<Chromasome>();
	int populationSize = Constants.populationSize;
	ArrayList<Chromasome> parents = new ArrayList<Chromasome>(populationSize);
	ArrayList<Chromasome> children = new ArrayList<Chromasome>(populationSize);
	ArrayList<Chromasome> rouletteTable = new ArrayList<Chromasome>(100);
	double mu = 0;
	double Variance = 0;
	double stdDeviation = 0;
	Random rand = new Random();
	static Chromasome currentFeasibleBest = null;
	static Chromasome currentBest;
	int checkCount = 0;
	int currentCheck = 0;
	int generationCount = 0;
	
	/**
	 * Function to generate the initial population
	 */
	public void initialPop(){
		for(int i = 0; i < populationSize; i++){
			Chromasome chro = new Chromasome(Constants.dataSize);
			chro.generate();
			population.add(chro);
		}
	}
	
	public void runGA(){
		boolean terminate = false;
		initialPop(); // Initialize population
		findBest();
		currentCheck = currentBest.fitness;
		//begin GA loop
		do{
			parentRoulette();//Generate the parent pool for this generation
			//Enter recombination loop
			population.clear();
			rouletteTable.clear();
			for(int i = 0; i < populationSize/2; i++){
				int select1 = rand.nextInt(parents.size());
				Chromasome parent1 = parents.remove(select1);//Randomly select one parent
				Chromasome parent2 = parents.remove(rand.nextInt(parents.size()));//Randomly select the other parent
				children.add(genChild(parent1, parent2));//Generate one of the children
				children.add(genChild(parent2, parent1));//Generate the other child
			}
			parents.clear();
			population = new ArrayList<Chromasome>(children);
			children.clear();
			population.set(0, currentBest);//We dump the first child for our current best, to provide elitism
			if(currentFeasibleBest != null){
				population.set(population.size()-1, currentFeasibleBest);//We need to keep the best feasible solution, too!
			}
			findBest();
			checkCount++;
			generationCount++;
			if(checkCount >= Constants.generationsToCheck){
				double improvement = (double)(currentBest.fitness-currentCheck)/(double)(currentCheck);
				if((stdDeviation/mu) < Constants.stopThreshold){
					if(!currentBest.isFeasible) currentBest = currentFeasibleBest;
					terminate = true;
					String crossoverUsed = "Single-point crossover";
					String parentSelectionUsed = "Roulette";
					String mutationUsed = "Triple Random Change";
					if(Constants.secondCrossover);
					if(Constants.secondMutation);
					main.out.println("GA finished.\n It took " + this.generationCount + " generations, " + Constants.mutations + " mutations, and the best chromasome was:\n"
							+ this.currentBest.getChromString() + " with a fitness of " + currentBest.fitness
							+ "\n The crossover used was " + crossoverUsed
							+ "\n The mutation used was " + mutationUsed
							+ "\n The parent selection used was " + parentSelectionUsed);
					main.out.flush();
				}
				currentCheck = currentBest.getFitness();
			}
		}while(!terminate);
	}

	/**
	 * Method to recombine two chromasomes into a single child, 
	 * using a single-point crossover
	 * @param parent1 - The primary parent, whos first half will be used
	 * @param parent2 - The secondary parent, whos second half will be used
	 */
	private Chromasome genChild(Chromasome parent1, Chromasome parent2) {
		int[] par1 = parent1.returnChrom();
		int[] par2 = parent2.returnChrom();
		Chromasome child = new Chromasome(par1.length);
		if (!Constants.secondCrossover) {
			int index = 0;
			while (index < par1.length / 2) {
				child.chromasome[index] = par1[index];
				index++;
			}
			while (index < par1.length) {
				child.chromasome[index] = par2[index];
				index++;
			}
			if (rand.nextDouble() < Constants.mutationChance) {
				child.mutate();
				Constants.mutations++;
			}
		}
		return child;
	}

	/**
	 * Method to fill the parent pool, using Roulette. 
	 */
	private void parentRoulette() {
		// TODO Auto-generated method stub
		long totalFitness = 0;
		for(Chromasome chro : population){
			totalFitness += chro.getFitness();
		}
		mu = totalFitness / populationSize;
		long varHold = 0;
		for(Chromasome chro : population){
			int numSlots = (int)((double)(chro.fitness)/(double)(totalFitness) * 100);
			double num = (chro.getFitness() - mu);
			varHold += (num * num);
			for(int i = 0; i < numSlots; i++){
				rouletteTable.add(chro);
			}
		}
		Variance = varHold / populationSize;
		stdDeviation = Math.sqrt(Variance);
		while(parents.size() < populationSize){
			parents.add(rouletteTable.get(rand.nextInt(rouletteTable.size())));
		}
		
	}
	
	/**
	 * Method to simply find the most fit chromasome
	 */
	private void findBest() {
		if(currentBest == null) currentBest = population.get(0);
		for(Chromasome c : population){
			if(c.getFitness() > currentBest.getFitness()){
				currentBest = c;
			}
			if(currentFeasibleBest == null && c.isFeasible()){
				currentFeasibleBest = c;
			}
			else if(c.isFeasible() && c.getFitness() > currentFeasibleBest.getFitness()){
				currentFeasibleBest = c;
			}
		}
		
	}
}
