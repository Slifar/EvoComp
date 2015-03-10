import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class main {
	public static PrintWriter out; 
	public static PrintWriter devOut;
	public static double currentBest = 0;
	public static double totalFit = 0;
	public static int totalChroms = 0;
	private static PrintWriter aggregateOut;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			out = new PrintWriter(new FileWriter(Constants.outputFile));
			devOut = new PrintWriter(new FileWriter("ConvergenceData.txt"));
			aggregateOut = new PrintWriter(new FileWriter(Constants.aggregateOutputFile));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		setupDataSet();
		GA ga;
		for(int i = 0; i < Constants.numTrials; i++){
			ga = new GA();
			ga.runGA();
			out.println("");
		}
		printTotalOutput();
		totalFit = 0;
		totalChroms=0;
		currentBest = 0;
		Constants.Roulette = true;
		for(int i = 0; i < Constants.numTrials; i++){
			ga = new GA();
			ga.runGA();
			out.println("");
		}
		printTotalOutput();
		totalFit = 0;
		totalChroms=0;
		currentBest = 0;
		Constants.genBothChildren = true;
		for(int i = 0; i < Constants.numTrials; i++){
			ga = new GA();
			ga.runGA();
			out.println("");
		}
		printTotalOutput();
		totalFit = 0;
		totalChroms=0;
		currentBest = 0;
		Constants.Roulette = false;
		for(int i = 0; i < Constants.numTrials; i++){
			ga = new GA();
			ga.runGA();
			out.println("");
		}
		printTotalOutput();
		totalFit = 0;
		totalChroms=0;
		currentBest = 0;
		Constants.removeMothers = true;
		for(int i = 0; i < Constants.numTrials; i++){
			ga = new GA();
			ga.runGA();
			out.println("");
		}
		printTotalOutput();
		totalFit = 0;
		totalChroms=0;
		currentBest = 0;
		Constants.genBothChildren = false;
		for(int i = 0; i < Constants.numTrials; i++){
			ga = new GA();
			ga.runGA();
			out.println("");
		}
		printTotalOutput();
		System.out.println("Ding!");
	}

	private static void setupDataSet() {
		Scanner cityScan = null;
		try {
			cityScan = new Scanner(new File(Constants.cityFile));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(cityScan.hasNext()){
			City city = new City();
			city.setCityNum(cityScan.nextInt());
			city.setxChoord(cityScan.nextDouble());
			city.setyChoord(cityScan.nextDouble());
			Constants.cities.add(city);
		}
		
	}
	
	private static void printTotalOutput(){
		String crossoverUsed = "PMX";
		String parentSelectionUsed = "All Chromasomes";
		String mutationUsed = "Random Bit Change";
		String numChildrenProduced = "population size-2";
		if(Constants.genChildrenDivideorMinus) numChildrenProduced = "populationSize/2";
		if(Constants.secondCrossover) crossoverUsed = "Double-Bridge Kick";
		if(Constants.secondMutation) mutationUsed = "Bit swap";
		if(Constants.Roulette) parentSelectionUsed = "Roulette";
		aggregateOut.println("GAs finished."
				+ "\n Number of trials ran: " + Constants.numTrials
				+ "\n used constant first city: " + Constants.constantFirstCity
				+ "\n The parent selection used was " + parentSelectionUsed
				+ "\n We ran for " + Constants.generationsToCheck + " generations."
				+ "\n removeMothers was: " + Constants.removeMothers
				+ "\n genBothChildren was: " + Constants.genBothChildren
				+ "\n Had a constant first city: " + Constants.firstCity
				+ "\n The population size was: " + Constants.populationSize
				+ "\n Used tournament for child selection: " +Constants.childTourney
				+ "\n Number of children generated each generation: " + numChildrenProduced
				+ "\n The best fitness found was: " + currentBest
				+ "\n The average fitness found was: " + (totalFit/totalChroms)
				);
		main.aggregateOut.flush();
	}

}
