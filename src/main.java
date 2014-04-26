import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;


public class main {
	public static PrintWriter out; 
	public static PrintWriter devOut;
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			out = new PrintWriter(new FileWriter(Constants.outputFile));
			devOut = new PrintWriter(new FileWriter("ConvergenceData.txt"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} 
		setupDataSet();
		/*Constants.generationsToCheck = 1;
		Constants.stagnationValue = 20;
		Constants.logDev = true;
		GA ga = new GA();
		ga.runGA();
		out.println("");
		Constants.logDev = false;
		ga = new GA();
		ga.runGA();
		out.println("");
		ga = new GA();
		ga.runGA();
		out.println("");
		Constants.stagnationValue = 50;
		ga = new GA();
		ga.runGA();
		out.println("");
		ga = new GA();
		ga.runGA();
		out.println("");
		ga = new GA();
		ga.runGA();
		out.println("");
		Constants.stagnationValue = 100;
		ga = new GA();
		ga.runGA();
		out.println("");
		ga = new GA();
		ga.runGA();
		out.println("");
		ga = new GA();
		ga.runGA();
		out.println("");
		Constants.stagnationValue = 300;
		ga = new GA();
		ga.runGA();
		out.println("");
		ga = new GA();
		ga.runGA();
		out.println("");
		ga = new GA();
		ga.runGA();
		out.println("");///
		/*ga = new GA();
		ga.runGA();
		out.println("");
		Constants.secondCrossover = true;
		ga = new GA();
		ga.runGA();
		out.println("");
		ga = new GA();
		ga.runGA();
		out.println("");
		Constants.secondCrossover = false;
		Constants.secondMutation = true;
		ga = new GA();
		ga.runGA();
		out.println("");
		ga = new GA();
		ga.runGA();
		out.println("");
		Constants.secondCrossover = true;
		ga = new GA();
		ga.runGA();
		out.println("");
		ga = new GA();
		ga.runGA();
		out.println("");*/
		SimulaedAnnealing SA;
		Constants.secondMutation = true;
		for(int i = 0; i < 5; i ++){
		SA = new SimulaedAnnealing();
		SA.SimulatedAnnealing();
		out.println();
		}
		Constants.foolish = true;
		Constants.secondMutation = true;
		//out.println();
		for(int i = 0; i < 5; i ++){
			SA = new SimulaedAnnealing();
			SA.SimulatedAnnealing();
			System.out.println("Ding!");
			out.println();
		}
	}

	private static void setupDataSet() {
		Scanner sizeScan = null;
		Scanner valueScan = null;
		try {
			sizeScan = new Scanner(new File(Constants.dataSizes));
			valueScan = new Scanner(new File(Constants.dataValues));
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		while(sizeScan.hasNext()){
			Item item = new Item(sizeScan.nextInt(), valueScan.nextInt());
			Constants.items.add(item);
			Constants.dataSize++;
		}
		
	}

}
