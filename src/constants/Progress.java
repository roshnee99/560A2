package constants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bean.State;

// this class keeps track of any progress and what states have been explored on the simulator
public class Progress {
		
	private Map<String, State> nameToStateExplored = new HashMap<>();
	private Set<String> allStatesExplored = new HashSet<>();
	
	private Truths constantFile;
	
	public Progress(Truths constantFile) {
		this.constantFile = constantFile;
	}
	
	//need to also store all the progress states currently explored
	public Map<String, State> getNameToStateMapExplored() {
		return this.nameToStateExplored;
	}
	public State getStateFromName(String name) {
		if (!this.nameToStateExplored.containsKey(name)) {
			return constantFile.getTruthStateFromName(name);
		}
		return this.nameToStateExplored.get(name);
	}
	public void addExploredStateToMap(State t) {
		this.nameToStateExplored.put(t.getName(), t);
		this.allStatesExplored.add(t.getName());
	}

	public void clearAll() {
		nameToStateExplored = new HashMap<>();
		allStatesExplored = new HashSet<>();
	}
	

}
