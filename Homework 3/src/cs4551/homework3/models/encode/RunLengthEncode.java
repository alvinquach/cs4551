package cs4551.homework3.models.encode;

import java.util.ArrayList;
import java.util.List;

import cs4551.homework3.utils.MathUtils;

public class RunLengthEncode {
	
	List<RunLengthPair> runLengthPairs = new ArrayList<>();
	
	public RunLengthEncode() {
		
	}

	public RunLengthEncode(int[] values) {
		this(values, 0);
	}
	
	public RunLengthEncode(int[] values, int startIndex) {
		
		startIndex = MathUtils.clamp(startIndex, 0, values.length - 1);
		
		int currentValue = values[startIndex];
		int runLength = 0;
		
		for (int i = startIndex; i < values.length; i++) {
			if (values[i] == currentValue) {
				runLength++;
			}
			else {
				runLengthPairs.add(new RunLengthPair(currentValue, runLength));
				currentValue = values[i];
				runLength = 1;
			}
		}
		
		// Add the last run-length pair.
		runLengthPairs.add(new RunLengthPair(currentValue, runLength));
		
	}

	public List<RunLengthPair> getRunLengthPairs() {
		return runLengthPairs;
	}
	
}
