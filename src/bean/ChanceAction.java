package bean;

public class ChanceAction {
	
	private double probability;
	private String action;
	private State endingState;
	
	public ChanceAction(String action, State state) {
		this.action = action;
		this.endingState = state;
	}
	
	
	public ChanceAction(String action, double probability, State state) {
		this.action = action;
		this.probability = probability;
		this.endingState = state;
	}
	
	public void addProbability(double probability) {
		this.probability = probability;
	}
	
	public String getAction() {
		return this.action;
	}
	
	public double getChance() {
		return this.probability;
	}
	
	public State getEndingState() {
		return this.endingState;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Action: " + action);
		builder.append("\t");
		builder.append("EndingState: " + endingState.getName());
		builder.append("\t");
		builder.append("Probability: " + probability);
		return builder.toString();
	}

}
