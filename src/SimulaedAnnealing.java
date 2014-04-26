import java.util.Random;


public class SimulaedAnnealing {
	Chromasome currentChromasome = new Chromasome();
	//Chromasome currentBest = null;
	//Chromasome bestFeasible = null;
	
	int iteration = 0;

    double temperature = 1000000.0;
    double deltaDistance = 0;
    double coolingRate = 0.99999;
    double endTemp = 0.00001;
    Random rand = new Random();

    public void SimulatedAnnealing(){
    	long initialTime = System.nanoTime();
    	currentChromasome = new Chromasome();
    	currentChromasome.generateFeasible();
    	GA.currentBest = currentChromasome;
    	GA.currentFeasibleBest = null;
    	if(currentChromasome.isFeasible()) GA.currentFeasibleBest = currentChromasome;
        while (temperature > endTemp)
        {
        	Chromasome child = new Chromasome();
        	int index = 0;
			while (index < child.chromasome.length) {//Copy the current chromasome to the child as a base, before we flip it
				child.chromasome[index] = currentChromasome.chromasome[index];
				index++;
			}
			/*int toSwap = rand.nextInt(child.chromasome.length);
			if(child.chromasome[toSwap] == 1)child.chromasome[toSwap] = 0;
			else child.chromasome[toSwap] = 1;*/
			child.mutate();
			
            if ((child.getFitness() > currentChromasome.getFitness()) || (child.getFitness() > 0 && 
               !Constants.foolish &&  Math.exp(((double)(currentChromasome.getFitness() - child.getFitness())) / temperature) > rand.nextDouble()))
            {
                currentChromasome = child;
                if(child.isFeasible()) GA.currentFeasibleBest = child;
                GA.currentBest = child;
                //temperature *= coolingRate;
            }

            //cool down the temperature
            temperature *= coolingRate;

            iteration++;
        }
        while(!currentChromasome.isFeasible()){
        	Chromasome child = new Chromasome();
        	int index = 0;
			while (index < child.chromasome.length) {//Copy the current chromasome to the child as a base, before we flip it
				child.chromasome[index] = currentChromasome.chromasome[index];
				index++;
			}
			int toSwap = rand.nextInt(child.chromasome.length);
			if(child.chromasome[toSwap] == 1)child.chromasome[toSwap] = 0;
			else child.chromasome[toSwap] = 1;
			
            if ((child.getFitness() > currentChromasome.getFitness()) || (child.getFitness() > 0 && 
               !Constants.foolish &&  Math.exp(((double)(currentChromasome.getFitness() - child.getFitness())) / temperature) > rand.nextDouble()))
            {
                currentChromasome = child;
                if(child.isFeasible()) GA.currentFeasibleBest = child;
                GA.currentBest = child;
            }
        }
        long finalTime = System.nanoTime();
        main.out.println("Simulated Annealing finished. \n"
				+ " " + GA.currentFeasibleBest.getChromString() + "\n with a fitness of " + GA.currentFeasibleBest.fitness
				+ "\n Was foolish hillclimber: " + Constants.foolish
				+ "\n The runtime was: " + ((double)(finalTime - initialTime))/ (double)1000000000
				+ " seconds");
		main.out.flush();

    }
    
    public void bitSwap(double tempurature){
    	Chromasome child = new Chromasome();
    	int index = 0;
		while (index < child.chromasome.length) {//Copy the current chromasome to the child as a base, before we flip it
			child.chromasome[index] = currentChromasome.chromasome[index];
			index++;
		}
		/*int toSwap = rand.nextInt(child.chromasome.length);
		if(child.chromasome[toSwap] == 1)child.chromasome[toSwap] = 0;
		else child.chromasome[toSwap] = 1;*/
		child.mutate();
		
        if ((child.getFitness() > currentChromasome.getFitness()) || (child.getFitness() > 0 && 
           !Constants.foolish &&  Math.exp(((double)(currentChromasome.getFitness() - child.getFitness())) / temperature) > rand.nextDouble()))
        {
            currentChromasome = child;
            if(child.isFeasible()) GA.currentFeasibleBest = child;
            GA.currentBest = child;
        }
    }
}
