import java.util.ArrayList;


public class Constants {
	public static String outputFile = "Outputs.txt";
	public static boolean secondCrossover = false;
	public static boolean secondMutation = false;
	public static boolean foolish = false;
	public static int populationSize = 1000;
	public static int generationsToCheck = 200;
	public static double stopThreshold = .01; //This variable determines the minimum improvement that must be found every so often for us to not terminate
	public static double mutationChance = .05;//Chance for mutation each time a child is produced
	public static int dataSize = 0;
	public static double penaltyModifier = .25; 
	public static int sizeConstraint = 1650; //The knapsack's capacity
	public static ArrayList<Item> items = new ArrayList<Item>();
	public static String dataSizes = "Sizes_test_1";
	public static String dataValues = "Values_test_1";
	public static long mutations = 0;
	public static boolean logDev = false;
	public static int numValuesChangedforMutation = 0;
	public static boolean Roulette = false;
	public static boolean secondSA = false;
	public static int stagnationValue = 500; //Number of generations to terminate after if not new best is found
}
