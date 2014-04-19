import java.util.Random;


public class Chromasome {
	int[] chromasome;
	int numItems = 0;
	int fitness = 0;
	boolean isFeasible = true;
	Random rand = new Random();
	
	public Chromasome(int length){
		chromasome = new int[length];
		numItems = length;
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
		int sizeOver = 0;
		if(totalSize > Constants.sizeConstraint){
			sizeOver = totalSize - Constants.sizeConstraint;
			isFeasible = false;
		}
		this.fitness = (int) (totalValue - (Constants.penaltyModifier * sizeOver));
		return this.fitness;
	}
	public void mutate() {
		// TODO Auto-generated method stub
		
	}
	public boolean isFeasible(){
		return this.isFeasible;
	}
}
