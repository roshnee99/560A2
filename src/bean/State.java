package bean;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.TreeMap;

import constants.Truths;

public class State {	
	private String name;
	private List<ChanceAction> actions;
	
	private Map<String, List<ChanceAction>> actionNameToChanceMap;
	
	//and the following is for maintaining progress on utility
	private Map<String, ActionUtility> nameToActionUtilityObject;
	private Queue<ActionUtility> sortedUtilities;
	
	//and the following is for maintain progress on action probability
	private Map<String, ActionEndState> nameToEndState;
	private String currentBestAction;
		
	public State(String name) {
		actions = new ArrayList<>();
		this.name = name;
		actionNameToChanceMap = new HashMap<>();
		nameToActionUtilityObject = new HashMap<>();
		sortedUtilities = new PriorityQueue<>();
		this.nameToEndState = new HashMap<>();
		currentBestAction = "";
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
	
	public Set<String> getActionNames() {
		return this.nameToEndState.keySet();
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
	
	//following methods are for maintaining progress
	public void addNewUtility(String action, int utility) {
		ActionUtility a = this.getActionUtilityObject(action);
		a.addUtility(utility);
		addActionToQueue(a);
	}
	
	public ActionUtility getActionUtilityObject(String action) {
		if (!nameToActionUtilityObject.containsKey(action)) {
			return new ActionUtility(action);
		}
		return nameToActionUtilityObject.get(action);
	}
	
	public ActionUtility getCurrentBestActionToPerform() {
		return this.sortedUtilities.element();
	}
	
	public String printUtilityTable() {
		StringBuilder builder = new StringBuilder();
		builder.append("StartState: " + this.getName());
		builder.append("\t->\t");
		builder.append(this.getCurrentBestActionToPerform().toString());
		return builder.toString();
	}
	
	//the following methods are for creating a transition table
	public void addNewEndState(String action, State endState) {
		ActionEndState e = this.getActionEndStateObject(action);
		this.nameToEndState.put(action, e);
		e.addEndState(endState);
	}
	
	public List<ActionEndState> getListOfEndStates() {
		return Arrays.asList(this.nameToEndState.values().toArray(new ActionEndState[this.nameToEndState.values().size()]));
	}
	
	public ActionEndState getActionEndStateObject(String action) {
		if (!this.nameToEndState.containsKey(action)) {
			return new ActionEndState(action);
		}
		return nameToEndState.get(action);
	}
	
	public double getExpectedUtility(Truths constantFile, double discountValue) {
		// no utility once you reach the hole
		if (this.isInHole(constantFile)) {
			return 0;
		}
		double minSum = Double.MAX_VALUE;
		
		// traverse through all the actions
		System.out.println(this.getActionNames());
		for(String a : this.getActionNames()) {
			ActionEndState aePair = this.getActionEndStateObject(a);
			Map<State, Double> probabilities = aePair.getProbabilitiesForOutcome();
			Set<State> possibleEndStates = probabilities.keySet();
			double sum = 0;
			
			//sum over all the possible states
			for (State endState : possibleEndStates) {
				double probability = probabilities.get(endState);
				// if end state is the same as the state we're looking at
				if (endState.getName().equals(this.getName())) {
					continue;
				} else {
					double expectedUtility = endState.getExpectedUtility(constantFile, discountValue);
					sum += (expectedUtility * probability);
				}
			}
			if (sum < minSum) {
				minSum = sum;
			}
		}
		// take the minimum expected utility and multiply it by discount value + reward
		return 1 + (discountValue*minSum);
	}
	

	public double getExpectedUtilityForAction(String actionName, double discountValue, Truths constantFile) {
		if (this.isInHole(constantFile)) {
			return 0;
		}
		ActionEndState aePair = this.getActionEndStateObject(actionName);
		Map<State, Double> probabilities = aePair.getProbabilitiesForOutcome();
		Set<State> possibleEndStates = aePair.getUniqueStatesVisited();
		double sum = 0;
		for (State endState : possibleEndStates) {
			if (endState.equals(this)) {
				
			}
			double probability = probabilities.get(endState);
			System.out.println(probability);
			double expectedUtility = endState.getExpectedUtility(constantFile, discountValue);
			double multiplied = expectedUtility * probability;
			System.out.println(multiplied);
			sum += multiplied;
		}
		return sum;
	}
	
	public String getCurrentBestAction() {
		return this.currentBestAction;
	}
	
	public String printProbabilityTables() {
		StringBuilder builder = new StringBuilder();
		builder.append("STATE: " + this.getName());
		builder.append("\n");
		for (ActionEndState e : this.nameToEndState.values()) {
			builder.append(e.toString());
		}
		builder.append("\n");
		return builder.toString();
	}
	
	
	private void addActionToQueue(ActionUtility a) {
		if (!this.sortedUtilities.contains(a)) {
			this.sortedUtilities.add(a);
		}
	}
	
	private State generateRandomState(TreeMap<Double, State> map) {
		double randomNumber = Math.random();
		Entry<Double, State> e = map.floorEntry(randomNumber);
		return e.getValue();
	}

}
