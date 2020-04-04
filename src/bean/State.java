package bean;

import java.util.ArrayList;
import java.util.List;

public class State {
	
	private String name;
	
	private List<ChanceAction> actions;
	
	public State(String name) {
		actions = new ArrayList<>();
		this.name = name;
	}
	
	public void addAction(String action, double probability) {
		actions.add(new ChanceAction(action, probability));
	}
	
	public void addAction(ChanceAction c) {
		actions.add(c);
	}
	
	public List<ChanceAction> getActions() {
		return this.actions;
	}
	
	public String getName() {
		return this.name;
	}

}
