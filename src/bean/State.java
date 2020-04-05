package bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import constants.Truths;

public class State {	
	private String name;
	private List<ChanceAction> actions;
	
	private Map<String, List<ChanceAction>> actionNameToChanceMap;
		
	public State(String name) {
		actions = new ArrayList<>();
		this.name = name;
		actionNameToChanceMap = new HashMap<>();
	}
	
	public void addAction(String action, double probability, State endingState) {
		this.addAction(new ChanceAction(action, probability, endingState));
	}
	
	public void addAction(ChanceAction c) {
		actions.add(c);
		addItemToActionMap(c);
	}
	
	private void addItemToActionMap(ChanceAction c) {
		if (!actionNameToChanceMap.containsKey(c.getAction())) {
			actionNameToChanceMap.put(c.getAction(), new ArrayList<>());
		}
		actionNameToChanceMap.get(c.getAction()).add(c);
	}
	
	public List<ChanceAction> getActions() {
		return this.actions;
	}
	
	public String getName() {
		return this.name;
	}
	
	public State getRandomEndingState(String action) {
		List<ChanceAction> possibleActions = this.actionNameToChanceMap.get(action);
		double currProbCount = 0;
		TreeMap<Double, State> probToEndingState = new TreeMap<>();
		for (ChanceAction a : possibleActions) {
			probToEndingState.put(currProbCount, a.getEndingState());
			currProbCount += a.getChance();
		}
		return generateRandomState(probToEndingState);
	}
	
	public String generateRandomAction() {
		List<String> actionName = getPossibleActions();
		Random random = new Random();
		int randomIndex = random.nextInt(actionName.size());
		return actionName.get(randomIndex);
	}
	
	public List<String> getPossibleActions() {
		Set<String> names = this.actionNameToChanceMap.keySet();
		List<String> actionName = Arrays.asList(names.toArray(new String[names.size()]));
		return actionName;
	}
	
	public List<State> getPossibleDestinations() {
		List<State> destinationStates = new ArrayList<>();
		for (ChanceAction a : this.actions) {
			destinationStates.add(a.getEndingState());
		}
		return destinationStates;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("StartState: " + name + "\n");
		for (ChanceAction c : this.actions) {
			builder.append(c.toString());
			builder.append("\n");
		}
		return builder.toString();
	}	
	
	public boolean isInHole(Truths constantFile) {
		return this.getName().equals(constantFile.getEndStateName());
	}
	
	private State generateRandomState(TreeMap<Double, State> map) {
		double randomNumber = Math.random();
		Entry<Double, State> e = map.floorEntry(randomNumber);
		return e.getValue();
	}

}
