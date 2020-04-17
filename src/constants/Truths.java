package constants;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import bean.State;

// this class keeps track of all the ground truths for building the simulator
public class Truths {

	private Map<String, State> trueNameToState = new HashMap<>();
	private Set<String> allStateName = new HashSet<>();

	public Truths() {
		//do nothing
	}

	public void addStateName(String stateName) {
		this.allStateName.add(stateName);
	}
	public Set<String> getStateNames() {
		return this.allStateName;
	}
	public Map<String, State> getTruthNameToStateMap() {
		return this.trueNameToState;
	}
	public State getTruthStateFromName(String name) {
		if (!this.trueNameToState.containsKey(name)) {
			State s = new State(name);
			this.trueNameToState.put(name, s);
			return s;
		}
		return this.trueNameToState.get(name);
	}
	public void addTruthStateToMap(State t) {
		this.trueNameToState.put(t.getName(), t);
	}

}
