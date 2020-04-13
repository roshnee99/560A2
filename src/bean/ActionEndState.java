package bean;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class ActionEndState {

	//this class works similar to ActionUtility
	//but instead of keeping a running record of the utilities
	//keep a running record of all the ending states
	//and then produce a probability table based on the counts of 
	//each states occurence in a list
	private String action;
	List<State> recordOfStatesVisited;
	
	private Map<State, Double> probabilityMap;
			
	public ActionEndState(String action) {
		this.action = action;
		recordOfStatesVisited = new ArrayList<>();
	}
	
	public String getActionName() {
		return this.action;
	}
	
	public void addEndState(State endState) {
		this.recordOfStatesVisited.add(endState);
	}
	
	public Set<State> getUniqueStatesVisited() {
		return this.getProbabilitiesForOutcome().keySet();
	}
	
	public String toString() {
		DecimalFormat df = new DecimalFormat("0.00");
		Map<State, Double> probabilities = this.getProbabilitiesForOutcome();
		StringBuilder builder = new StringBuilder();
		for (Entry<State, Double> e :  probabilities.entrySet()) {
			builder.append("Action: " + this.getActionName());
			builder.append("\t");
			builder.append("End State: " + e.getKey().getName());
			builder.append("\t");
			builder.append("Probability: " + df.format(e.getValue()));
			builder.append("\n");
		}
		return builder.toString();
	}
	
	public Map<State, Double> getProbabilitiesForOutcome() {
		if (probabilityMap == null) {
			return this.getProbabilitiesForOutcomeFirstTime();
		}
		return probabilityMap;
	}
	
	private Map<State, Double> getProbabilitiesForOutcomeFirstTime() {
		probabilityMap = new HashMap<>();
		Map<State, Double> frequencyMap = this.getStateFrequencyMap();
		for (State s : frequencyMap.keySet()) {
			double probability = frequencyMap.get(s) / this.recordOfStatesVisited.size();
			probabilityMap.put(s, probability);
		}
		return probabilityMap;
	}
	
	private Map<State, Double> getStateFrequencyMap() {
		Map<State, Double> frequencyMap = new HashMap<>();
		for (State s: recordOfStatesVisited) {
			Double count = frequencyMap.get(s);
			if (count == null)
				count = 0.0;
			frequencyMap.put(s, count + 1);
		}
		return frequencyMap;
	}
	
	
}
