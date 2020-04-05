package main;

import java.io.IOException;

import constants.Truths;
import readers.TextFileReader;
import sim.Simulator;

public class Runner {
	
	public static void main(String [] args) throws IOException {
		Truths constantFile = new Truths();
		TextFileReader reader = new TextFileReader("Test Probability File.txt", constantFile);
		reader.loadStates();
//		Map<String, TruthState> truthStates = constantFile.getNameToStateMap();
//		for (TruthState t : truthStates.values()) {
//			System.out.println(t.toString());
//		}
		Simulator sim = new Simulator(constantFile);
		System.out.println(sim.lengthToFinish("Fairway"));
//		System.out.println(sim.getEndingState("Same", "Putt").getName());
//		System.out.println(constantFile.getEndStateName());
	}

}
