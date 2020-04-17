package bean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

// This object is connected to a State object. 
// It keeps track of a given action and what states it ends up in

public class ActionEndStateMB {

	private String action;
	private List<State> recordOfStatesVisited;

	public ActionEndStateMB(String action) {
		this.action = action;
		recordOfStatesVisited = new ArrayList<>();
	}

	public String getActionName() {
		return this.action;
	}

	public void addEndState(State endState) {
		this.recordOfStatesVisited.add(endState);
	}

	public String toString() {
		DecimalFormat df = new DecimalFormat("0.0000");
		Map<State, Double> probabilities = this.getProbabilitiesForOutcome();
		StringBuilder builder = new StringBuilder();
		for (Entry<State, Double> e : probabilities.entrySet()) {
			builder.append("Action: " + this.getActionName());
			builder.append("\t");
			builder.append("Probability: " + df.format(e.getValue()));
			builder.append("\t");
			builder.append("End State: " + e.getKey().getName());
			builder.append("\n");

		}
		return builder.toString();
	}

	public Map<State, Double> getProbabilitiesForOutcome() {
		Map<State, Double> probabilityMap = new HashMap<>();
		Map<State, Double> frequencyMap = this.getStateFrequencyMap();
		for (State s : frequencyMap.keySet()) {
			double probability = frequencyMap.get(s) / this.recordOfStatesVisited.size();
			probabilityMap.put(s, probability);
		}
		return probabilityMap;
	}

	private Map<State, Double> getStateFrequencyMap() {
		Map<State, Double> frequencyMap = new HashMap<>();
		for (State s : recordOfStatesVisited) {
			Double count = frequencyMap.get(s);
			if (count == null)
				count = 0.0;
			frequencyMap.put(s, count + 1);
		}
		return frequencyMap;
	}

}
