import java.util.Random;


public class Chromasome {
	int[] chromasome;
	int numItems = 0;
	int fitness = 0;
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
		for(int i = 0; i < numItems; i++){//For each item in existance
			if(rand.nextBoolean()){ //If the next randomly produced boolean is true
				chromasome[i]=1;//put the current item in the bag
			}
			else chromasome[i] = 0; //Dont put the item in the bag
		}
		getFitness();
		//if(!this.isFeasible())generate(); //It is easier if we simply start off with all feasible chromasomes
	}
	public void generateFeasible(){
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
	public int getFitness(){
		int totalValue = 0;
		int totalSize = 0;
		for(int i = 0; i < chromasome.length; i++){
			if(chromasome[i] == 1){
				totalValue += Constants.items.get(i).value;
				totalSize += Constants.items.get(i).size;
			}
		}
		this.itemSize = totalSize;
		int sizeOver = 0;
		if(totalSize > Constants.sizeConstraint){
			sizeOver = totalSize - Constants.sizeConstraint;
			isFeasible = false;
		}
		else isFeasible = true;
		if(!this.isFeasible && GA.currentBest != null && GA.currentBest.isFeasible){
			this.fitness = (int) (GA.currentBest.getFitness() - (sizeOver * Constants.penaltyModifier));
		}
		else if(!this.isFeasible && GA.currentFeasibleBest != null){
			this.fitness = (int) (GA.currentFeasibleBest.getFitness() - (sizeOver * Constants.penaltyModifier));
		}
		else this.fitness = (int) (totalValue - (Constants.penaltyModifier * sizeOver));
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
