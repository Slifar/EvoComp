import java.util.ArrayList;
import java.util.Random;


public class Chromasome {
	int[] chromasome;
	int numItems = 0;
	double fitness = 0;
	int itemSize = 0;
	boolean isFeasible = true;
	Random rand = new Random();
	
	public Chromasome(int length){
		chromasome = null;
		chromasome = new int[length];
		numItems = length;
	}
	public Chromasome(){
		chromasome = null;
		chromasome = new int[Constants.items.size()];
		numItems = chromasome.length;
	}
	/**
	 * Method to randomly initialize the chromasome to a random state..
	 */
	public void generate(){
		ArrayList<City> holds = new ArrayList<City>();
		for (int i = 0; i < Constants.cities.size(); i++)
			holds.add(Constants.cities.get(i));
		for(int i = 0; i < numItems; i++){
			int toPick = rand.nextInt(holds.size());
			chromasome[i] = toPick;
			holds.remove(toPick);
		}
	}
	public void generateFeasible(){//This function should not be needed
		for(int i = 0; i < numItems; i++){//For each item in existance
			int randomPick = rand.nextInt(chromasome.length);
			chromasome[randomPick] = 1;
			getFitness();
			if(!isFeasible){
				chromasome[randomPick] = 0;
				getFitness();
			}
		}
		getFitness();
	}
	public int[] returnChrom(){
		return this.chromasome;
	}
	public void setChrom(int[] chrom){
		this.chromasome = chrom;
	}
	
	/**
	 * Method that both computes and returns a chromasome's fitness
	 * Fitness formula: Total item value - (penalty modifier)*(Amount over capacity)
	 * @return
	 */
	public double getFitness(){
		for(int i = 0; i < chromasome.length - 1; i++){
			double x = Constants.cities.get(chromasome[i + 1]).getxChoord() - Constants.cities.get(chromasome[i]).getxChoord();
			double y = Constants.cities.get(chromasome[i+ 1]).getyChoord() - Constants.cities.get(chromasome[i]).getyChoord();
			this.fitness = Math.sqrt((x * x) + (y * y));
		}
		return this.fitness;
	}
	public void mutate() {
		int toChange;// = rand.nextInt(chromasome.length);
		if (Constants.secondMutation == false) {
			/*for(int i = 0; i < (Constants.items.size()/4) - 1; i++){
				toChange = rand.nextInt(chromasome.length);
				if (chromasome[toChange] == 1)
					chromasome[toChange] = 0;
				else
					chromasome[toChange] = 1;
			}
			/*if (chromasome[toChange] == 1)
				chromasome[toChange] = 0;
			else
				chromasome[toChange] = 1;*/
			/*toChange = rand.nextInt(chromasome.length);
			if (chromasome[toChange] == 1)
				chromasome[toChange] = 0;
			else
				chromasome[toChange] = 1;*/
			toChange = rand.nextInt(chromasome.length);
			if (chromasome[toChange] == 1)
				chromasome[toChange] = 0;
			else
				chromasome[toChange] = 1;
		}
		else{
			int firstIndex = rand.nextInt(chromasome.length);
			int secondIndex = rand.nextInt(chromasome.length);
			while(firstIndex == secondIndex) secondIndex = rand.nextInt(chromasome.length);
			int atFirst = chromasome[firstIndex];
			chromasome[firstIndex] = chromasome[secondIndex];
			chromasome[secondIndex] = atFirst;
			/*firstIndex = rand.nextInt(chromasome.length);
			secondIndex = rand.nextInt(chromasome.length);
			while(firstIndex == secondIndex) secondIndex = rand.nextInt(chromasome.length);
			atFirst = chromasome[firstIndex];
			chromasome[firstIndex] = chromasome[secondIndex];
			chromasome[secondIndex] = atFirst;*/
		}
	}
	public boolean isFeasible(){
		return this.isFeasible;
	}
	public String getChromString(){
		String toReturn = "";
		for(int i = 0; i < chromasome.length; i++){
			toReturn += chromasome[i];
		}
		return toReturn;
	}
}
