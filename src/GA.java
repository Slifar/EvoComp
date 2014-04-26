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
	int generationsUnchanged = 0;
	static Chromasome currentBest;
	int checkCount = 0;
	int currentCheck = 0;
	int generationCount = 0;
	long initialTime = 0;
	
	/**
	 * Function to generate the initial population
	 */
	public void initialPop(){
		for(int i = 0; i < populationSize; i++){
			Chromasome chro = new Chromasome(Constants.dataSize);
			chro.generate();
			population.add(chro);
		}
		Chromasome feas = new Chromasome();
		feas.generateFeasible();
		if(!feas.isFeasible()) System.out.println("Something bad happened!");
		currentBest = feas;
		population.set(0, feas);
	}
	
	public void runGA(){
		currentFeasibleBest = null;
		currentBest = null;//We have to clear out our static variables, in case this isn't the first time this has been run!
		Constants.numValuesChangedforMutation = Constants.items.size()/4 - 1;
		boolean terminate = false;
		initialTime = System.nanoTime();
		initialPop(); // Initialize population
		findBest();
		currentCheck = currentBest.fitness;
		//begin GA loop
		do{
			double diff = stdDeviation/mu;
			if(Constants.Roulette) parentRoulette();//Generate the parent pool for this generation
			else parentRank();
			//Enter recombination loop
			//population.clear();
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
			generationsUnchanged++;
			if(generationsUnchanged > Constants.stagnationValue && !currentBest.isFeasible() && currentFeasibleBest == null){
				
			}
			if(Constants.logDev){
				main.devOut.println(this.stdDeviation / mu);
				main.devOut.flush();
			}
			if(checkCount >= Constants.generationsToCheck){
				double improvement = (double)(currentBest.fitness-currentCheck)/(double)(currentCheck);
				if(((stdDeviation/mu) < Constants.stopThreshold || generationsUnchanged > Constants.stagnationValue) && (currentBest.isFeasible() || currentFeasibleBest != null)){
					if(!currentBest.isFeasible) currentBest = currentFeasibleBest;
					terminate = true;
					long finalTime = System.nanoTime();
					String crossoverUsed = "Uniform crossover";
					String parentSelectionUsed = "Roulette";
					String mutationUsed = "Random Bit Change";
					if(Constants.secondCrossover) crossoverUsed = "Single-Point Crossover";
					if(Constants.secondMutation) mutationUsed = "Bit swap";
					main.out.println("GA finished."
							+ "\n The crossover used was " + crossoverUsed
							+ "\n The mutation used was " + mutationUsed
							+ "\n The parent selection used was " + parentSelectionUsed
							+ "\n The population size was: " + population.size()
							+ "\n The minimum number of generations to run was: " + Constants.generationsToCheck
							+ "\n The stagnation value was " + Constants.stagnationValue
							+ "\n It took " + this.generationCount + " generations, " + Constants.mutations + " mutations, and the best chromasome was:\n"
							+ " " + this.currentBest.getChromString() + "\n with a fitness of " + currentBest.fitness
							+ "\n The runtime was: " + ((double)(finalTime - initialTime))/ (double)1000000000
							+ " seconds"
							+ "\n The final standard deviation of the population was: "
							+ this.stdDeviation
							+ "\n The final Mu value for the population was :"
							+ this.mu);
					main.out.flush();
					System.out.println("One GA Done");
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
		if (Constants.secondCrossover) {
			int index = 0;
			while (index < par1.length / 2) {
				child.chromasome[index] = par1[index];
				index++;
			}
			while (index < par1.length) {
				child.chromasome[index] = par2[index];
				index++;
			}
			
		}
		else{
			for(int i = 0; i < child.chromasome.length; i++){
				if(rand.nextBoolean()){
					child.chromasome[i] = par1[i];
				}
				else child.chromasome[i] = par2[i];
				
			}
		}
		if (rand.nextDouble() < Constants.mutationChance) {
			child.mutate();
			Constants.mutations++;
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
		//long selection = (long)(rand.nextDouble() * totalFitness);
		for(Chromasome chro : population){
			//int numSlots = (int)((double)(chro.fitness)/(double)(totalFitness) * 100);
			double num = (chro.getFitness() - mu);
			varHold += (num * num);
			/*for(int i = 0; i < numSlots; i++){
				rouletteTable.add(chro);
			}*/
		}
		Variance = varHold / populationSize;
		stdDeviation = Math.sqrt(Variance);
		while(parents.size() < populationSize){
			long selection = (long)(rand.nextDouble() * totalFitness);
			int index = 0;
			while(selection >= 0){
				Chromasome selected = population.get(index);
				selection -= selected.getFitness();
				if(selection<0){
					parents.add(selected);
				}
				index++;
			}
			//parents.add(rouletteTable.get(rand.nextInt(rouletteTable.size())));
		}
		
	}
	
	private void parentRank() {
		// TODO Auto-generated method stub
		long totalFitness = 0;
		for(Chromasome chro : population){
			totalFitness += chro.getFitness();
			int index = 0;
			while(true){
				if(index + 1 > parents.size()){
					rouletteTable.add(chro);
					break;
				}
				else if(chro.getFitness() < parents.get(index).getFitness()){
					rouletteTable.add(index, chro);
					break;
				}
				index++;
			}
		}
		mu = totalFitness / populationSize;
		long varHold = 0;
		//long selection = (long)(rand.nextDouble() * totalFitness);
		for(Chromasome chro : population){
			//int numSlots = (int)((double)(chro.fitness)/(double)(totalFitness) * 100);
			double num = (chro.getFitness() - mu);
			varHold += (num * num);
			/*for(int i = 0; i < numSlots; i++){
				rouletteTable.add(chro);
			}*/
		}
		int toAdd = 2;
		long selectionRange = 1;
		while(toAdd <= populationSize){
			selectionRange += toAdd;
			toAdd ++;
		}
		Variance = varHold / populationSize;
		stdDeviation = Math.sqrt(Variance);
		while(parents.size() < populationSize){
			long selection = (long)(rand.nextDouble() * selectionRange);
			int index = 0;
			while(selection > 0){
				Chromasome selected = rouletteTable.get(index);
				selection -= index + 1;
				if(selection<=0){
					parents.add(selected);
				}
				index++;
			}
			//parents.add(rouletteTable.get(rand.nextInt(rouletteTable.size())));
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
				generationsUnchanged = -1;
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
