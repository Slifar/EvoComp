import java.util.ArrayList;
import java.util.Random;


public class GA {
	ArrayList<Chromasome> population = new ArrayList<Chromasome>();
	int populationSize = Constants.populationSize;
	ArrayList<Chromasome> parents = new ArrayList<Chromasome>(populationSize);
	ArrayList<Chromasome> children = new ArrayList<Chromasome>(populationSize);
	ArrayList<Chromasome> rouletteTable = new ArrayList<Chromasome>(100);
	Random rand = new Random();
	Chromasome currentBest;
	/**
	 * Function to generate the initial population
	 */
	public void initialPop(){
		for(int i = 0; i < populationSize; i++){
			Chromasome chro = new Chromasome(i);
			chro.generate();
			population.add(chro);
		}
	}
	
	public void runGA(){
		boolean terminate = false;
		initialPop(); // Initialize population
		//begin GA loop
		do{
			parentRoulette();//Generate the parent pool for this generation
			//Enter recombination loop
			for(int i = 0; i < populationSize/2; i++){
				int select1 = rand.nextInt(parents.size());
				Chromasome parent1 = parents.remove(select1);//Randomly select one parent
				Chromasome parent2 = parents.remove(rand.nextInt(parents.size()));//Randomly select the other parent
				children.add(genChild(parent1, parent2));//Generate one of the children
				children.add(genChild(parent2, parent1));//Generate the other child
			}
			population = children;
			population.set(0, currentBest);//We dump the first child for our current best, to provide elitism
			
		}while(!terminate);
	}

	/**
	 * Method to recombine two chromasomes into a single child, 
	 * using a single-point crossover
	 * @param parent1 - The primary parent, whos first half will be used
	 * @param parent2 - The secondary parent, whos second half will be used
	 */
	private Chromasome genChild(Chromasome parent1, Chromasome parent2) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Method to fill the parent pool, using Roulette. 
	 */
	private void parentRoulette() {
		// TODO Auto-generated method stub
		int currentIndex = 0;
	}
}
