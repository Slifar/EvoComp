import java.util.ArrayList;
import java.util.Random;


public class GA {
	ArrayList<Chromasome> population = new ArrayList<Chromasome>();
	int populationSize = Constants.populationSize;
	ArrayList<Chromasome> parents = new ArrayList<Chromasome>(populationSize);
	ArrayList<Chromasome> children = new ArrayList<Chromasome>(populationSize);
	ArrayList<Chromasome> rouletteTable = new ArrayList<Chromasome>(100);
	Random rand = new Random();
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
			for(int i = 0; i < populationSize/2; i++){
				int select1 = rand.nextInt(parents.size());
				Chromasome parent1 = parents.remove(select1);
				Chromasome parent2 = parents.remove(rand.nextInt(parents.size()));
			}
		}while(!terminate);
	}

	/**
	 * Method to fill the parent pool, using Roulette. 
	 */
	private void parentRoulette() {
		// TODO Auto-generated method stub
		int currentIndex = 0;
	}
}
