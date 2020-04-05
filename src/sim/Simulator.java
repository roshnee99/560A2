package sim;

import bean.State;
import constants.Progress;
import constants.Truths;

public class Simulator {
	
	private Truths constantFile;
	private Progress progressFile;
	
	public Simulator(Truths constantFile, Progress progressFile) {
		this.constantFile = constantFile;
		this.progressFile = progressFile;
	}
	
	/**
	 * Allows user to specify the first action taken -- helpful for exploring varying actions
	 * @param startState
	 * @param action
	 * @return
	 */
	public int lengthToFinishWithAction(String startState, String action) {
		State s = progressFile.getStateFromName(startState);
		State endState = getEndingState(startState, action);
		int utility = 1 + lengthToFinish(endState.getName());
		updateFiles((State) s, action, utility);
		return utility;
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
		System.out.println("StartState: " + startState + "\t->\t" + "Action: " + actionToPlay + "\t->\t" +
		"EndState: " + endState.getName());
		int utility = 1 + lengthToFinish(endState.getName());
		updateFiles((State) t, actionToPlay, utility);
		return utility;
	}
	
	
	public State getEndingState(String state, String action) {
		State startState = constantFile.getTruthStateFromName(state);
		return startState.getRandomEndingState(action);
	}
	
	private void updateFiles(State startState, String action, int util) {
		startState.addNewUtility(action, util);
		progressFile.addExploredStateToMap(startState);
	}

}
