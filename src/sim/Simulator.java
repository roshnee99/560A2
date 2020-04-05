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
	 * Here we play the hole from a given state and record the probabilities
	 * @param startState
	 */
	public void playHoleCalcProb(String startState, boolean random) {
		State t = constantFile.getTruthStateFromName(startState);
		if (t.isInHole(constantFile)) {
			return;
		}
		String actionToPlay = random ? t.generateRandomAction() : t.getCurrentBestActionToPerform().toString();
		State endState = getEndingState(startState, actionToPlay);
		this.updateProbabilityFiles(t, actionToPlay, endState);
		playHoleCalcProb(endState.getName(), random);
	}
	
	/**
	 * Finds the number of shots to get to the "In" state from a random state
	 * Will choose a random action at each time
	 * @param startState
	 * @return
	 */
	public int playHoleTrackUtility(String startState, boolean random) {
		State t = constantFile.getTruthStateFromName(startState);
		if (t.isInHole(constantFile)) {
			return 0;
		}
		System.out.println(t.getName());
		String actionToPlay = random ? t.generateRandomAction() : t.getCurrentBestActionToPerform().toString();
		State endState = getEndingState(startState, actionToPlay);
		//can comment out - allows to see the path to hole
//		System.out.println("StartState: " + startState + "\t->\t" + "Action: " + actionToPlay + "\t->\t" +
//		"EndState: " + endState.getName());
		int utility = 1 + playHoleTrackUtility(endState.getName(), random);
		updateUtilityFiles(t, actionToPlay, utility);
		return utility;
	}
	
	
	public State getEndingState(String state, String action) {
		State startState = constantFile.getTruthStateFromName(state);
		return startState.getRandomEndingState(action);
	}
	
	private void updateUtilityFiles(State startState, String action, int util) {
		startState.addNewUtility(action, util);
		progressFile.addExploredStateToMap(startState);
		progressFile.updateIteration();
	}
	
	private void updateProbabilityFiles(State startState, String action, State endState) {
		startState.addNewEndState(action, endState);
		progressFile.addExploredStateToMap(startState);
		progressFile.updateIteration();
	}

}
