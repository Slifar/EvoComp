import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;


public class main {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		setupDataSet();
		GA ga = new GA();
		ga.runGA();
		System.out.println("Ding!");
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
