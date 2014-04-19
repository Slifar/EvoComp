import java.util.ArrayList;


public class Constants {
	public static int populationSize = 100;
	public static int generationsToCheck = 10000;
	public static double stopThreshold = .05; //This variable determines the minimum improvement that must be found every so often for us to not terminate
	public static double mutationChance = .05;//Chance for mutation each time a child is produced
	public static int dataSize = 0;
	public static double penaltyModifier = 1.5; 
	public static int sizeConstraint = 165;
	public static ArrayList<Item> items = new ArrayList<Item>();
	public static String dataSizes = "Sizes_test_1";
	public static String dataValues = "Values_test_1";
}
