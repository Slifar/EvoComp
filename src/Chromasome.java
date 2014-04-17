import java.util.Random;


public class Chromasome {
	int[] chromasome;
	int numItems = 0;
	int fitness = 0;
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
}
