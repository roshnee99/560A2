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

public class State {
	// name of the state
	private String name;
	
	/********** FOR THE SIMULATOR *******************/
	// list of possible actions to end state pairings
	private List<ChanceAction> actions;
	// the name of the action to the list of probabilities and end states
	private Map<String, List<ChanceAction>> actionNameToChanceMap;
	
	/********** FOR THE MODEL FREE LEARNING *******************/
	// this is a name to ActionUtilityMap so can grab one single instance
	private Map<String, ActionUtilityMF> nameToActionUtilityObject;
	// this is a priority queue with the utilities ranked by what's the current best action
	private Queue<ActionUtilityMF> sortedModelFreeUtilities;
	// this is an "average" style utility -- regardless of the action, what your utility will be
	private double currentUtilityModelFree;
		
	/********** FOR THE MODEL BASED LEARNING *******************/
	// this is a name to  ActionEndState object to grab one single instance
	private Map<String, ActionEndStateMB> nameToEndState;
	// this is the ideal action to take at any point
	private String currentBestActionModelBased;
	// this is the current expected utility based on all previous iterations
	private double currentUtilityModelBased;
		
	
	public State(String name) {
		actions = new ArrayList<>();
		this.name = name;
		actionNameToChanceMap = new HashMap<>();
		nameToActionUtilityObject = new HashMap<>();
		sortedModelFreeUtilities = new PriorityQueue<>();
		this.nameToEndState = new HashMap<>();
		currentBestActionModelBased = "";
		currentUtilityModelBased = 1;
		currentUtilityModelFree = 0;
	}
	
	public String getName() {
		return this.name;
	}
	public boolean isInHole() {
		return this.getName().equals("In");
	}
	
	/*********** BECAUSE WE USE STATE AS THE KEY TO HASHMAPS ***************/
	@Override
	public int hashCode() {
		return this.name.hashCode();
	}
	
	public boolean equals(State other) {
		return this.getName().equals(other.getName());
	}
	
	/*********** FOR BUILDING GROUND TRUTH ***************/
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
	
	
	/*********** FOR BUILDING SIMULATOR *****************/
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
	
	private State generateRandomState(TreeMap<Double, State> map) {
		double randomNumber = Math.random();
		Entry<Double, State> e = map.floorEntry(randomNumber);
		return e.getValue();
	}
	
	/*********** FOR MODEL FREE LEARNING  ***************/
	public ActionUtilityMF getBestActionModelFree() {
		return this.sortedModelFreeUtilities.element();
	}
	public double getCurrentUtilityModelFree() {
		if (this.isInHole()) {
			return 0;
		} 
		return currentUtilityModelFree;
	}
	// just a getter method for the Action Utility objects
	public ActionUtilityMF getActionUtilityObject(String action, double learningRate) {
		if (!nameToActionUtilityObject.containsKey(action)) {
			ActionUtilityMF a = new ActionUtilityMF(action, learningRate);
			nameToActionUtilityObject.put(action, a);
			return a;
		}
		return nameToActionUtilityObject.get(action);
	}
	// both updates the expected utility after taking an action AND overall utility for the state
	// at the same learning rate (adds a percentage of the difference between current and observed utility)
	public void updateCurrentUtilityModelFree (double learningRate, double observedUtility, String action) {
		ActionUtilityMF a = this.nameToActionUtilityObject.get(action);
		a.updateCurrentUtilityModelFree(observedUtility);
		this.currentUtilityModelFree += learningRate*(observedUtility - currentUtilityModelFree);
	}
	
	// whenever you add an action, add the new utility object
	// maintain the queue of current best utility and action
	public void addNewActionUtility(String action, int utility, double learningRate) {
		ActionUtilityMF a = this.getActionUtilityObject(action, learningRate);
		addActionToQueue(a);
	}
	private void addActionToQueue(ActionUtilityMF a) {
		if (!this.sortedModelFreeUtilities.contains(a)) {
			this.sortedModelFreeUtilities.add(a);
		}
	}
	
	/*********** FOR MODEL BASED LEARNING ***************/
	public String getCurrentBestActionModelBased() {
		return this.currentBestActionModelBased;
	}
	public double getCurrentUtilityModelBased() {
		if (this.isInHole()) {
			return 0;
		}
		return this.currentUtilityModelBased;
	}
	// just a getter for the ActionEndState object
	public ActionEndStateMB getActionEndStateObject(String action) {
		if (!this.nameToEndState.containsKey(action)) {
			ActionEndStateMB e = new ActionEndStateMB(action);
			this.nameToEndState.put(action, e);
			return e;
		}
		return nameToEndState.get(action);
	}
	// helper to add new end states to the actionEndState
	// need to keep a record of every state visited for best probability measure
	public void addNewEndStateDest(String action, State endState) {
		ActionEndStateMB e = this.getActionEndStateObject(action);
		e.addEndState(endState);
	}
	
	public void updateCurrentUtilityModelBased (double discountValue) {
		// if in hole, utility must remain 0
		if (this.isInHole()) {
			currentUtilityModelBased += 0;
		}
		// keeps track of minimum sum amongst all the actions
		double minSum = Double.MAX_VALUE;
		// iterate over all the actions to calculate individual utilities for each action.
		// goal is to find the action that results in lowest utility
		for(String a : this.nameToEndState.keySet()) {
			ActionEndStateMB aePair = this.nameToEndState.get(a); 
			Map<State, Double> probabilities = aePair.getProbabilitiesForOutcome();
			Set<State> possibleEndStates = probabilities.keySet();
			double sum = 0;
			// see all the possible end states. For each end state, multiply the current expected utility and the 
			// probability of ending up in that state
			for (State endState : possibleEndStates) {
				double probability = probabilities.get(endState);
				double expectedUtility = endState.getCurrentUtilityModelBased();
				sum += (expectedUtility * probability);
			}
			// pick the minimum term thus far
			if (sum < minSum) {
				minSum = sum;
				this.currentBestActionModelBased = a;
			}
		}
		// current utility is reward + (discountValue * minimum term)
		currentUtilityModelBased = 1 + (discountValue*minSum);
	}

	/*********** FOR PRINTING PROBABILITY TABLES ***************/
	public String printProbabilityTables() {
		StringBuilder builder = new StringBuilder();
		builder.append("STATE: " + this.getName());
		builder.append("\n");
		for (ActionEndStateMB e : this.nameToEndState.values()) {
			builder.append(e.toString());
		}
		builder.append("\n");
		return builder.toString();
	}
	
}
