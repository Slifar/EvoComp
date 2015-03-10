import java.util.ArrayList;
import java.util.Random;


public class GA {
	ArrayList<Chromasome> population = new ArrayList<Chromasome>();
	int populationSize = Constants.populationSize;
	ArrayList<Chromasome> maxParents = new ArrayList<Chromasome>(populationSize);
	ArrayList<Chromasome> minParents = new ArrayList<Chromasome>(populationSize);
	ArrayList<Chromasome> children = new ArrayList<Chromasome>(populationSize);
	ArrayList<Chromasome> rouletteTable = new ArrayList<Chromasome>(100);//Table used to hold chromasomes for Rank selection
	int genFound = 0;
	double mu = 0;
	double Variance = 0;
	double stdDeviation = 0;
	Random rand = new Random();
	static Chromasome currentFeasibleBest = null;//The current feasible best solution. Generally not relevent, due to the fitness function
	int generationsUnchanged = 0;//The number of generations we have gone without finding a better solution
	static Chromasome currentBest;// The best solution we have currently found
	int checkCount = 0;
	double currentCheck = 0;
	int generationCount = 0;
	long initialTime = 0;
	
	/**
	 * Function to generate the initial population
	 */
	public void initialPop(){
		for(int i = 0; i < populationSize; i++){
			Chromasome chro = new Chromasome();
			chro.generate();
			population.add(chro);
		}
	}
	
	public void runGA(){
		currentFeasibleBest = null;
		currentBest = null;//We have to clear out our static variables, in case this isn't the first time this has been run!
		Constants.numValuesChangedforMutation = Constants.items.size()/4 - 1;
		boolean terminate = false;
		initialTime = System.nanoTime();
		initialPop(); // Initialize population
		currentBest = population.get(0); //We have to set the current best to be a chromosome, otherwise we'll register a best fitness of 0!
		findBest();
		currentCheck = currentBest.fitness;
		//begin GA loop
		do{
			double diff = stdDeviation/mu;
			parentRoulette();//Generate the parent pool for this generation
			if (!Constants.Roulette) {
				//Enter recombination loop
				//population.clear();
				for (int i = 0; i < populationSize; i++) {
					maxParents.set(i, population.get(i));
					minParents.set(i, population.get(i));
				}
			}
			rouletteTable.clear();
			int loopTo;
			if(Constants.genChildrenDivideorMinus) loopTo = populationSize/2;
			else loopTo = populationSize-2;
			for(int i = 0; i < loopTo; i++){
				int select1 = rand.nextInt(maxParents.size());
				Chromasome parent1 = maxParents.remove(select1);//Randomly select one parent
				minParents.remove(parent1);
				Chromasome parent2 = findMaxMate(parent1, maxParents);//maxParents.remove(rand.nextInt(maxParents.size()));//Randomly select the other parent
				if(Constants.removeMothers){
					maxParents.remove(parent2);
				}
				Chromasome child1;
				Chromasome child2;
				child1 = genChild(parent1, parent2);//Generate one of the children
				if(Constants.genBothChildren || Constants.childTourney){
					child2=genChild(parent2, parent1);//Generate the other child
					if(Constants.childTourney){
						children.add(childTourney(child1, child2));
					}
					else{
						children.add(child1);
						children.add(child2);
					}
				}
				parent2 = findMinMate(parent1, minParents);//maxParents.remove(rand.nextInt(maxParents.size()));//Randomly select the other parent
				if(Constants.removeMothers){
					minParents.remove(parent2);
				}
				child1 = genChild(parent1, parent2);
				if(Constants.genBothChildren || Constants.childTourney){
					child2 = genChild(parent1, parent2);//Generate the other child
					if(Constants.childTourney){
						children.add(childTourney(child1, child2));
					}
					else{
						children.add(child1);
						children.add(child2);
					}
					
				}
				else children.add(child1);
			}
			maxParents.clear();
			population = prunePopulation(children, population);//new ArrayList<Chromasome>(children);
			children.clear();
			//population.set(0, currentBest);//We dump the first child for our current best, to provide elitism
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
				if(true){
					terminate = true;
					long finalTime = System.nanoTime();
					String crossoverUsed = "PMX";
					String parentSelectionUsed = "All Chromasomes";
					String mutationUsed = "Random Bit Change";
					String numChildrenProduced = "population size-2";
					if(Constants.genChildrenDivideorMinus) numChildrenProduced = "populationSize/2";
					if(Constants.secondCrossover) crossoverUsed = "Double-Bridge Kick";
					if(Constants.secondMutation) mutationUsed = "Bit swap";
					if(Constants.Roulette) parentSelectionUsed = "Roulette";
					main.out.println("GA finished."
							/*+ "\n The crossover used was " + crossoverUsed
							+ "\n The mutation used was " + mutationUsed*/
							+ "\n The parent selection used was " + parentSelectionUsed
							+ "\n We ran for " + Constants.generationsToCheck + " generations."
							+ "\n removeMothers was: " + Constants.removeMothers
							+ "\n genBothChildren was: " + Constants.genBothChildren
							+ "\n Had a constant first city: " + Constants.firstCity
							+ "\n The population size was: " + population.size()
							+ "\n Used tournament for child selection: " +Constants.childTourney
							+ "\n Number of children generated each generation: " + numChildrenProduced
							//+ "\n The minimum number of generations to run was: " + Constants.generationsToCheck
							+ "\n The solution was found on generation: " + genFound
							+ "\n It took " + this.generationCount + " generations, " + Constants.mutations + " mutations, and the best chromasome was:\n"
							+ " " + this.currentBest.getChromString() + "\n with a fitness of " + currentBest.fitness
							+ "\n The runtime was: " + ((double)(finalTime - initialTime))/ (double)1000000000
							+ " seconds"
							+ "\n The final standard deviation of the population was: "
							+ this.stdDeviation
							+ "\n The final Mu value for the population was :"
							+ this.mu);
					main.out.flush();
					if(this.currentBest.fitness < main.currentBest || main.currentBest == 0) main.currentBest = this.currentBest.fitness;
					for(int i = 0; i < populationSize; i++){
						main.totalFit += population.get(i).getFitness();
					}
					main.totalChroms += this.populationSize;
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
		Chromasome child = new Chromasome();
		int[] childChrom = new int[par1.length];
		for(int i = 0; i < par1.length; i++){
			childChrom[i] = par1[i];
		}
		int crossoverPoint1 = rand.nextInt(parent1.chromasome.length - 1) + 1;
		int crossoverPoint2 = rand.nextInt(parent1.chromasome.length - 1) + 1;
		if (crossoverPoint1 > crossoverPoint2) //if point 1 is bigger than point 2 swap them
	    {
	        int temp = crossoverPoint1;
	        crossoverPoint1 = crossoverPoint2;
	        crossoverPoint2 = temp;
	    }
		for(int i = crossoverPoint1; i <= crossoverPoint2; i++){
			//Insert PMX swapping code here.
			int toFind = par2[i];
			int toReplace = childChrom[i];
			for(int q = 0; q < childChrom.length; q++){
			if(childChrom[q] == toFind){
					childChrom[q] = toReplace;
					break;
				}
			}
			childChrom[i] = toFind;
		}
		child.setChrom(childChrom);
		double mutationRoll = rand.nextDouble();
		if(mutationRoll <= Constants.mutationChance) child.mutate();
		return child;
	}
	
	public Chromasome childTourney(Chromasome child1, Chromasome child2){
		double totalFitness = (1/child1.getFitness()) + (1/child2.getFitness());
		Random rand = new Random();
		double roll = rand.nextDouble() * totalFitness;
		if(roll - child1.getFitness() < 0) return child1;
		else return child2;
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
			double num = (chro.getFitness() - mu);
			varHold += (num * num);
		}
		Variance = varHold / populationSize;
		stdDeviation = Math.sqrt(Variance);
		double invertedFitness = 0;
		for(Chromasome chro : population){
			invertedFitness += 1/chro.getFitness();
		}
		while(maxParents.size() < populationSize){
			double selection = (rand.nextDouble() * invertedFitness);
			int index = 0;
			while(selection >= 0){
				Chromasome selected = population.get(index);
				selection -= 1/selected.getFitness();
				if(selection<0){
					maxParents.add(selected);
					minParents.add(selected);
					
				}
				index++;
			}
		}
		
	}
	
	private void parentRank() {
		// TODO Auto-generated method stub
		long totalFitness = 0;
		for(Chromasome chro : population){
			totalFitness += chro.getFitness();
			int index = 0;
			while(true){
				if(index + 1 > maxParents.size()){
					rouletteTable.add(chro);
					break;
				}
				else if(chro.getFitness() < maxParents.get(index).getFitness()){
					rouletteTable.add(index, chro);
					break;
				}
				index++;
			}
		}
		mu = totalFitness / populationSize;
		long varHold = 0;
		for(Chromasome chro : population){
			double num = (chro.getFitness() - mu);
			varHold += (num * num);
		}
		int toAdd = 2;
		long selectionRange = 1;
		while(toAdd <= populationSize){
			selectionRange += toAdd;
			toAdd ++;
		}
		Variance = varHold / populationSize;
		stdDeviation = Math.sqrt(Variance);
		while(maxParents.size() < populationSize){
			long selection = (long)(rand.nextDouble() * selectionRange);
			int index = 0;
			while(selection > 0){
				Chromasome selected = rouletteTable.get(index);
				selection -= index + 1;
				if(selection<=0){
					maxParents.add(selected);
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
		if(currentBest == null){
			currentBest = population.get(0);
			genFound = this.generationCount;
		}
		for(Chromasome c : population){
			if(c.getFitness() < currentBest.getFitness()){
				int i = 0;
				currentBest = c;
				generationsUnchanged = -1;
				genFound = this.generationCount;
				//System.out.println("Current Best changed to: " + currentBest.fitness);
			}
		}
		
	}
	
	private double getChromDifferenceDistance(Chromasome a, Chromasome b){
		double distance = 0;
		for(int i = 0; i < Constants.cities.size(); i++){
			double x = Constants.cities.get(a.chromasome[i]).getxChoord() - Constants.cities.get(b.chromasome[i]).getxChoord();
			double y = Constants.cities.get(a.chromasome[i]).getyChoord() - Constants.cities.get(b.chromasome[i]).getyChoord();
			distance += Math.sqrt((x * x) + (y * y));
		}
		return distance;
	}
	
	private Chromasome findMinMate(Chromasome father, ArrayList<Chromasome> parentpool){
		Chromasome mother = null;
		double currentDistance = 0;
		double holdDistance = 0;
		boolean first = true;
		for(Chromasome c : parentpool){
			if(true){
				if(first){
					mother = c;
					first = false;
					currentDistance = getChromDifferenceDistance(father, mother);
				}
				else{
					holdDistance = getChromDifferenceDistance(father, c);
					if(holdDistance < currentDistance){
						mother = c;
						currentDistance = holdDistance;
					}
				}
			}
		}
		return mother;
	}
	
	private Chromasome findMaxMate(Chromasome father, ArrayList<Chromasome> parentpool){
		Chromasome mother = null;
		double currentDistance = 0;
		double holdDistance = 0;
		boolean first = true;
		for(Chromasome c : parentpool){
			if(true){
				if(first){
					mother = c;
					first = false;
					currentDistance = getChromDifferenceDistance(father, mother);
				}
				else{
					holdDistance = getChromDifferenceDistance(father, c);
					if(holdDistance > currentDistance){
						mother = c;
						currentDistance = holdDistance;
					}
				}
			}
			else System.out.println("Error!");
		}
		return mother;
	}
	
	private ArrayList<Chromasome> prunePopulation(ArrayList<Chromasome> children, ArrayList<Chromasome> currentPop){
		ArrayList<Chromasome> toReturn = new ArrayList<Chromasome>();
		ArrayList<Chromasome> holding = new ArrayList<Chromasome>();
		holding.addAll(children);
		holding.addAll(currentPop);
		ArrayList<Chromasome> toPrune = new ArrayList<Chromasome>();
		for(Chromasome c : holding){
			if(toPrune.size() < 1) toPrune.add(c);
			else{
				boolean inserted = false;
				for(int i = 0; i < toPrune.size(); i++){
					if(c.getFitness() < toPrune.get(i).getFitness()){
						toPrune.add(i, c);
						inserted = true;
						break;
					}
				}
				if(!inserted)toPrune.add(c);
			}
		}
		for(int i = 0; i < Constants.populationSize; i++){
			toReturn.add(toPrune.get(i));
		}
		return toReturn;
		
	}
	
}
