package main;

import java.io.IOException;
import java.util.Map;

import bean.ActionUtilityMF;
import bean.State;
import constants.Progress;
import constants.Truths;
import readers.TextFileReader;
import sim.Simulator;

public class Runner {

	private static final double MODEL_FREE_LEARNING_RATE = 0.1;
	// implies x% exploration, (1-x)% exploitation ---- where x is MODEL_FREE_EPSILON
	private static final double MODEL_FREE_EPSILON = 0.75;
	private static final int MODEL_FREE_ITERATIONS = 16000;

	private static final double MODEL_BASED_DISCOUNT_RATE = 0.9;
	// implies x% exploration, (1-x)% exploitation ---- where x is MODEL_BASED_EPSILON
	private static final double MODEL_BASED_EPSILON = 0.8;
	private static final int MODEL_BASED_ITERATIONS = 4000;

	public static void main(String[] args) throws IOException {
		// initialize process and constant tracking files
		Truths constantFile = new Truths();
		Progress progressFile = new Progress(constantFile);
		TextFileReader reader = new TextFileReader("Test Probability File.txt", constantFile);
		// load all the states from the text file and their ground truth probabilities
		reader.loadStates();
		String startState = "Fairway"; // start state
		String endState = "In"; // end state
		constantFile.getTruthStateFromName(endState);
		
		// start up simulator
		Simulator sim = new Simulator(constantFile, progressFile);
		// initialize map tracking which states have been visited
		Map<String, State> exploredStates;
		// initialize the exploration and exploitation balance for model-free learning
		int numExploreModelFree = (int) Math.floor(MODEL_FREE_EPSILON * MODEL_FREE_ITERATIONS);
		int numExploitModelFree = MODEL_FREE_ITERATIONS - numExploreModelFree;
		// initialize the exploration and exploitation balance for model-based learning
		int numExploreModelBased = (int) Math.floor(MODEL_BASED_EPSILON * MODEL_BASED_ITERATIONS);
		int numExploitModelBased = MODEL_BASED_ITERATIONS - numExploreModelBased;
		

		/*********
		 * NOW WE PERFORM A MODEL FREE ANALYSIS. NO PROBABILITIES ARE TRACKED.
		 ************/

		for (int i = 0; i < numExploreModelFree; i++) { // explore
			sim.playHoleModelFree(startState, true, MODEL_FREE_LEARNING_RATE);
		}
		for (int i = 0; i < numExploitModelFree; i++) { // exploit
			sim.playHoleModelFree(startState, false, MODEL_FREE_LEARNING_RATE);
		}
		System.out.println("UTILITY ANALYSIS - MODEL FREE:\n");
		exploredStates = progressFile.getNameToStateMapExplored();
		for (State t : exploredStates.values()) {
			System.out.print("STATE: " + t.getName());
			ActionUtilityMF a = t.getBestActionModelFree();
			System.out.print("\tEXPECTED UTILITY: " + a.getExpectedUtilityModelFree());
			System.out.println("\tBEST ACTION: " + a.getActionName());
		}
		progressFile.clearAll();
		
		/*********
		 * NOW WE PERFORM A MODEL BASED ANALYSIS. PROBABILITIES ARE TRACKED.
		 ************/

		System.out.println("\nUTILITY MODEL BASED:\n");
		for (int i = 0; i < numExploreModelBased; i++) { // explore
			sim.playHoleModelBased(startState, true, MODEL_BASED_DISCOUNT_RATE);
		}
		for (int i = 0; i < numExploitModelBased; i++) { // exploit
			sim.playHoleModelBased(startState, false, MODEL_BASED_DISCOUNT_RATE);
		}
		exploredStates = progressFile.getNameToStateMapExplored();
		for (State t : exploredStates.values()) {
			System.out.print("STATE: " + t.getName());
			System.out.print("\tEXPECTED UTILITY: " + t.getCurrentUtilityModelBased());
			System.out.println("\tBEST ACTION: " + t.getCurrentBestActionModelBased());
		}

		/*********
		 * WE CAN ALSO PRINT THE PROBABILITY TABLE FROM THE MODEL-BASED ANALYSIS
		 ************/
		System.out.println("\nPROBABILITY TABLES:\n");
		exploredStates = progressFile.getNameToStateMapExplored();
		for (State t : exploredStates.values()) {
			System.out.println(t.printProbabilityTables());
		}

	}

}
