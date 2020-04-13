package main;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import bean.ChanceAction;
import bean.State;
import constants.Progress;
import constants.Truths;
import readers.TextFileReader;
import sim.Simulator;

public class Runner {
	
	//TO DO STILL:
	//need to fix the return expectedUtility of actionUtility
	//at the moment, it just returns the average of every utility found -- no loss formula
	
	public static void main(String [] args) throws IOException {
		Truths constantFile = new Truths();
		Progress progressFile = new Progress();
		TextFileReader reader = new TextFileReader("Test Probability File.txt", constantFile);
		reader.loadStates();

		Simulator sim = new Simulator(constantFile, progressFile);
		Set<String> stateNames = constantFile.getStateNames();
		for (String s : stateNames) {
			for (int i = 0; i < 5; i++) {
				sim.playHoleTrackUtility(s, true);
			}
		}
		System.out.println("\nUTILITY ANALYSIS:\n");
		Map<String, State> exploredStates = progressFile.getNameToStateMapExplored();
		for (State t : exploredStates.values()) {
			System.out.println(t.printUtilityTable());
		}
		
		stateNames = constantFile.getStateNames();
		for (String s : stateNames) {
			for (int i = 0; i < 500; i++) {
				sim.playHoleCalcProb(s, true);
			}
		}
		
		
		
		System.out.println("\nPROBABILITY ANALYSIS:\n");
		exploredStates = progressFile.getNameToStateMapExplored();
		for (State t : exploredStates.values()) {
			System.out.println(t.printProbabilityTables());
		}
		
		System.out.println("\nUTILITY - MODEL-BASED ANALYSIS");
		exploredStates = progressFile.getNameToStateMapExplored();
		for (State t : exploredStates.values()) {
			System.out.println("STATE: " + t.getName());
			for (String a : t.getActionNames()) {
				double expectedUtility = t.getExpectedUtilityForAction(a, 0.09, constantFile);
				System.out.println(a +  "\t->\tExpected Utility: " + expectedUtility);
			}
		}
//		
		
//		System.out.println(sim.getEndingState("Same", "Putt").getName());
//		System.out.println(constantFile.getEndStateName());
	}

}
