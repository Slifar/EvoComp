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
		GA ga;
		for(int i = 0; i < 5; i++){
			ga = new GA();
			ga.runGA();
			out.println("");
		}
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

}
