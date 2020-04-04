package bean;

public class ChanceAction {
	
	private double probability;
	private String action;
	
	public ChanceAction(String action) {
		this.action = action;
	}
	
	public ChanceAction(String action, double probability) {
		this.action = action;
		this.probability = probability;
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

}
