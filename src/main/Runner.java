package main;

import java.io.IOException;
import java.util.Map;
import java.util.Set;

import bean.State;
import constants.Progress;
import constants.Truths;
import readers.TextFileReader;
import sim.Simulator;

public class Runner {
	
	public static void main(String [] args) throws IOException {
		Truths constantFile = new Truths();
		Progress progressFile = new Progress();
		TextFileReader reader = new TextFileReader("Test Probability File.txt", constantFile);
		reader.loadStates();

		Simulator sim = new Simulator(constantFile, progressFile);
		Set<String> stateNames = constantFile.getStateNames();
		for (String s : stateNames) {
			for (int i = 0; i < 1000; i++) {
				sim.lengthToFinish(s);
			}
		}
		System.out.println("\nUTILITY ANALYSIS:\n");
		Map<String, State> exploredStates = progressFile.getNameToStateMapExplored();
		for (State t : exploredStates.values()) {
			System.out.println(t.printUtilityTable());
		}
//		System.out.println(sim.getEndingState("Same", "Putt").getName());
//		System.out.println(constantFile.getEndStateName());
	}

}
