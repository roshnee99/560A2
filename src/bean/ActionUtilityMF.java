package bean;

// This object is connected to the state object
// It keeps track of each action and the utility to get to the "In" state given that action

public class ActionUtilityMF implements Comparable<ActionUtilityMF> {

	private String action;

	private double currentUtility;
	private double learningRate;

	public ActionUtilityMF(String action, double learningRate) {
		this.action = action;
		this.learningRate = learningRate;
		this.currentUtility = 0;
	}

	public String getActionName() {
		return this.action;
	}

	/**
	 * Takes the difference between the current utility and the new utility
	 * Adds, at a discount, it to the currentUtility
	 * @param observedUtility
	 */
	public void updateCurrentUtilityModelFree(double observedUtility) {
		currentUtility += learningRate * (observedUtility - currentUtility);
	}

	public double getExpectedUtilityModelFree() {
		return this.currentUtility;
	}

	/**
	 * @param other
	 *            ActionUtility object to compare to
	 * @return -1 if this is worse than other, 0 if equal to other, 1 is better
	 *         than other
	 */
	@Override
	public int compareTo(ActionUtilityMF other) {
		if (this.getExpectedUtilityModelFree() < other.getExpectedUtilityModelFree()) {
			return -1;
		}
		if (this.getExpectedUtilityModelFree() == other.getExpectedUtilityModelFree()) {
			return 0;
		}
		return 1;
	}

}
