import java.util.ArrayList;


public class Constants {
	public static String outputFile = "Outputs(24).txt";
	public static boolean secondCrossover = false;//Flag to set if we use the second crossover operator or not
	public static boolean secondMutation = false;//Flag to set if we use the second mutation operator or not
	public static boolean foolish = false;//Flag to set if the SA is a foolish hillclimber or not
	public static int populationSize = 1000;//The population size
	public static int generationsToCheck = 200;//The minimum amount of generations to check. Irrelevent with the Stagnation Value above this point.
	public static double stopThreshold = .01; //This variable determines the minimum improvement that must be found every so often for us to not terminate
	public static double mutationChance = .05;//Chance for mutation each time a child is produced
	public static int dataSize = 0;//How many items there are
	public static double penaltyModifier = .25; //The penalty modifier for the fitness function
	public static int sizeConstraint = 6404180; //The knapsack's capacity
	public static ArrayList<Item> items = new ArrayList<Item>();//The items
	public static ArrayList<City> cities = new ArrayList<City>();//The cities
	public static String dataSizes = "Sizes_test_24";//the file with the item sizes to read from
	public static String dataValues = "Values_test_24";//The file with the values to read from
	public static long mutations = 0; // The number of mutations that occur
	public static boolean logDev = false; //Flag to set if we want to log stdDev/mu
	public static int numValuesChangedforMutation = 0;//No longer used
	public static boolean Roulette = false;//Flag to set if we use Roulette for parent selection. We use Rank if not
	public static boolean secondSA = false;//Flag to set if we use the first or second mutation operator in the SA algorithm
	public static int stagnationValue = 500; //Number of generations to terminate after if not new best is found
}
