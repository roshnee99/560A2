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
	// starts playing the hole on simulator keeping track of start state, action taken, and end state
	public void playHoleModelBased(String startState, boolean random, double discountRate) {
		State t = constantFile.getTruthStateFromName(startState);
		if (t.isInHole()) {
			return;
		}
		String actionToPlay = random ? t.generateRandomAction() : t.getCurrentBestActionModelBased();
		State endState = getEndingState(startState, actionToPlay);
		this.updateModelBasedFiles(t, actionToPlay, endState);
		playHoleModelBased(endState.getName(), random, discountRate);
		t.updateCurrentUtilityModelBased(discountRate);
	}
	// starts playing the hole on simulator keeping track of start state, action taken, and number of shots to hole
	public int playHoleModelFree(String startState, boolean random, double learningRate) {
		State t = constantFile.getTruthStateFromName(startState);
		if (t.isInHole()) {
			return 0;
		}
		String actionToPlay = random ? t.generateRandomAction() : t.getBestActionModelFree().getActionName();
		State endState = getEndingState(startState, actionToPlay);
		int utility = 1 + playHoleModelFree(endState.getName(), random, learningRate);
		updateModelFreeFiles(t, actionToPlay, utility, learningRate);
		t.updateCurrentUtilityModelFree(learningRate, utility, actionToPlay);
		return utility;
	}
	
	// function of the actual simulator.
	// uses ground truth to determine some state where ball ends up in
	private State getEndingState(String state, String action) {
		State startState = constantFile.getTruthStateFromName(state);
		return startState.getRandomEndingState(action);
	}
	// updates all the files pertaining to a model free search
	// does NOT track the actual end state, merely the action taken and how many strokes to the end hole
	private void updateModelFreeFiles(State startState, String action, int util, double learningRate) {
		startState.addNewActionUtility(action, util, learningRate);
		progressFile.addExploredStateToMap(startState);
	}
	// updates all files pertaining to a model based search
	// includes the endState and the action taken from a given start state
	private void updateModelBasedFiles(State startState, String action, State endState) {
		startState.addNewEndStateDest(action, endState);
		progressFile.addExploredStateToMap(startState);
	}

}
