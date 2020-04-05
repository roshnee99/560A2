package bean;

import java.util.ArrayList;
import java.util.List;

public class ActionUtility implements Comparable<ActionUtility> {
	
	private String action;
	private double expectedUtility;
	
	List<Integer> recordOfUtilities;
		
	public ActionUtility(String action) {
		this.action = action;
		this.expectedUtility = 1;
		recordOfUtilities = new ArrayList<>();
	}
	
	public String getActionName() {
		return this.action;
	}
	
	public void addUtility(int utility) {
		this.recordOfUtilities.add(utility);
		this.updateExpectedUtility();
	}
	
	public double getExpectedUtility() {
		return expectedUtility;
	}
	
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Action: " + this.getActionName());
		builder.append("\t");
		builder.append("Utility: " + this.getExpectedUtility());
		return builder.toString();
	}

	private void updateExpectedUtility() {
		//some algorithm here based on iterations
		//for now it just returns the average but should be some function that takes current expected
		//and the most recent addition to the recordOfUtilities list
		double sum = 0.0;
		for (Integer i : recordOfUtilities) {
			sum += i;
		}
		expectedUtility = sum / recordOfUtilities.size();
	}
	
	/**
	 * @param other ActionUtility object to compare to
	 * @return -1 if this is worse than other, 0 if equal to other, 1 is better than other
	 */
	@Override
	public int compareTo(ActionUtility other) {
		if (this.getExpectedUtility() < other.getExpectedUtility()) {
			return -1;
		} if (this.getExpectedUtility() == other.getExpectedUtility()) {
			return 0;
		} 
		return 1;
	}
	
	
	
	
	

}
