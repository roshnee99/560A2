package constants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bean.State;

public class Progress {
	
	//this  is where we will record data for each iteration.
	//this is just going to focus on gathering utilities rather than probabilities
	//will record utility for each state/action pair
	public int iterationNumber;
	
	private Map<String, State> nameToStateExplored = new HashMap<>();
	private Set<String> allStatesExplored = new HashSet<>();
	
	public Progress() {
		iterationNumber = 0;
	}
	
	//need to also store all the progress states currently explored
	public Map<String, State> getNameToStateMapExplored() {
		return this.nameToStateExplored;
	}
	public State getStateFromName(String name) {
		if (!this.nameToStateExplored.containsKey(name)) {
			return new State(name);
		}
		return this.nameToStateExplored.get(name);
	}
	public void addExploredStateToMap(State t) {
		this.nameToStateExplored.put(t.getName(), t);
		this.allStatesExplored.add(t.getName());
	}
	public void updateIteration() {
		iterationNumber++;
	}
	

}
