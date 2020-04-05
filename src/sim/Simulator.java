package sim;

import bean.State;
import constants.Truths;

public class Simulator {
	
	private Truths constantFile;
	
	public Simulator(Truths constantFile) {
		this.constantFile = constantFile;
	}
	
	/**
	 * Finds the number of shots to get to the "In" state from a random state
	 * Will choose a random action at each time
	 * @param startState
	 * @return
	 */
	public int lengthToFinish(String startState) {
		State t = constantFile.getTruthStateFromName(startState);
		if (t.isInHole(constantFile)) {
			return 0;
		}
		String actionToPlay = t.generateRandomAction();
		State endState = getEndingState(startState, actionToPlay);
		//can comment out - allows to see the path to hole
		System.out.println("StartState: " + startState + "\t->\tEndState: " + endState.getName());
		return 1 + lengthToFinish(endState.getName());
	}
	
	
	public State getEndingState(String state, String action) {
		State startState = constantFile.getTruthStateFromName(state);
		return startState.getRandomEndingState(action);
	}

}
