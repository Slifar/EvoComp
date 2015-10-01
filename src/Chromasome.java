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
		chromasome = new int[Constants.cities.size()];
		numItems = chromasome.length;
	}
	/**
	 * Method to randomly initialize the chromasome to a random state..
	 */
	public void generate(){
		ArrayList<Integer> holds = new ArrayList<Integer>();
		//ArrayList<City> holds = new ArrayList<City>();
		for (int i = 0; i < Constants.cities.size(); i++)
			//holds.add(Constants.cities.get(i));
			holds.add(i);
		for(int i = 0; i < numItems; i++){
			rand = new Random();
			int checker = 0;
			int toPick = 0;
			checker = holds.size();
			int index = rand.nextInt(checker);
			toPick = holds.get(index);
			chromasome[i] = toPick;
			holds.remove(index);
		}
		getFitness();
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
	 * Fitness formula: Total distance the tour travels.
	 * @return
	 */
	public double getFitness(){
		ArrayList<Boolean> checkList = new ArrayList<Boolean>();
		for(int i = 0; i < Constants.cities.size(); i++){
			checkList.add(false);
		}
		this.fitness = 0;
		for(int i = 0; i < chromasome.length - 1; i++){
			double x = Constants.cities.get(chromasome[i + 1]).getxChoord() - Constants.cities.get(chromasome[i]).getxChoord();
			double y = Constants.cities.get(chromasome[i + 1]).getyChoord() - Constants.cities.get(chromasome[i]).getyChoord();
			this.fitness += Math.sqrt((x * x) + (y * y));
			if(checkList.get(chromasome[i]) == true){
				System.out.println("ERROR! DUPLICATE CITY DETECTED! City #:" + i);
			}
			else checkList.set(chromasome[i], true);
		}
		double x = Constants.cities.get(chromasome[chromasome.length - 1]).getxChoord() - Constants.cities.get(chromasome[0]).getxChoord();
		double y = Constants.cities.get(chromasome[chromasome.length - 1]).getyChoord() - Constants.cities.get(chromasome[0]).getyChoord();
		this.fitness += Math.sqrt((x * x) + (y * y));
		return this.fitness;
	}
	public void mutate() {
		ArrayList<Integer> mainSub = new ArrayList<Integer>();
		ArrayList<Integer> firstSub = new ArrayList<Integer>();
		ArrayList<Integer> secondSub = new ArrayList<Integer>();
		for(int i = 0; i < chromasome.length; i++){
			mainSub.add(chromasome[i]);
		}
		int cutIndex1 = rand.nextInt(chromasome.length);// Choose the first node to grab for our subsection
		int cutIndex2 = rand.nextInt(chromasome.length);//Choose the second node to grab for the subsection
		while(cutIndex2 == cutIndex1){
			cutIndex2 = rand.nextInt(chromasome.length);
		}
		if(cutIndex2 < cutIndex1){//if the second cut index is less than the first, then swap them.
			int temp = cutIndex2;
			cutIndex2 = cutIndex1;
			cutIndex1 = temp;
		}
		if(cutIndex1 != 0){
			for(int i = 0; i < cutIndex1; i++){
				firstSub.add(mainSub.get(i));
			}
		}
		for(int i = cutIndex1; i <= cutIndex2; i++){//For all the alleles from cutIndex 1 to cutIndex 2
			secondSub.add(mainSub.get(i));
		}
		if(cutIndex2 != chromasome.length - 1){
			for(int i = cutIndex2 + 1; i < chromasome.length; i++){
				firstSub.add(mainSub.get(i));
			}
		}
		if(firstSub.size() == 0){
			for(int i = 0; i < chromasome.length; i++){
				chromasome[i] = secondSub.get(i);
			}
		}
		else{
			int bridgeIndex = rand.nextInt(firstSub.size());
			firstSub.addAll(bridgeIndex, secondSub);
			for(int i = 0; i < chromasome.length; i++){
				chromasome[i] = firstSub.get(i);
			}
		}
		Constants.mutations++;
		if(Constants.constantFirstCity){
			if(chromasome[0] != Constants.firstCity){
				int hold = chromasome[0];
				for(int i = 0; i < chromasome.length; i++){
					if(chromasome[i] == Constants.firstCity){
						chromasome[0] = Constants.firstCity;
						chromasome[i] = hold;
						break;
					}
				}
			}
		}
	}
	public boolean isFeasible(){
		return this.isFeasible;
	}
	public String getChromString(){
		String toReturn = "";
		for(int i = 0; i < chromasome.length; i++){
			toReturn += Constants.cities.get(chromasome[i]).getCityNum() + " ";
		}
		return toReturn;
	}
}
