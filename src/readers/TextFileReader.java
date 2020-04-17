package readers;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import bean.State;
import constants.Truths;

// this class reads the text file and gets all the start states and probabilities for building the simulator
public class TextFileReader {
	
	private String fileName;
	private Truths constants;
	
	public TextFileReader(String fileName, Truths constants) {
		this.fileName = fileName;
		this.constants = constants;
	}
	
	/**
	 * Text file must be of the form:
	 * startState/action/endState/probability
	 * @return list of the ground truth states
	 * @throws IOException
	 */
	public void loadStates() throws IOException {
		List<String> linesToRead = this.readTextFile();
		for (String line : linesToRead) {
			String[] components = line.split("/");
			String name = components[0];
			String action = components[1];
			String endState = components[2];
			double probability = Double.parseDouble(components[3]);
			State t = constants.getTruthStateFromName(name);
			t.addAction(action, probability, constants.getTruthStateFromName(endState));
			constants.addTruthStateToMap(t);
			constants.addStateName(name);
			constants.addStateName(endState);
		}		
	}
	
	public List<String> readTextFile() throws IOException {
		BufferedReader buffy = new BufferedReader(new FileReader(this.fileName));
		List<String> linesToRead = new ArrayList<>();
		String line = buffy.readLine();
		while (line != null) {
			linesToRead.add(line);
			line = buffy.readLine();
		}
		buffy.close();
		return linesToRead;
	}

}
