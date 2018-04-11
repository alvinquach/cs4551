package cs4551.homework3.models.encode.rle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cs4551.homework3.models.image.quantized.QuantizedBlock;
import cs4551.homework3.utils.ArrayUtils;
import cs4551.homework3.utils.MathUtils;

public class RunLengthEncode {
	
	List<RunLengthPair> runLengthPairs = new ArrayList<>();
	
	private RunLengthEncode() {
		
	}

	public List<RunLengthPair> getRunLengthPairs() {
		return runLengthPairs;
	}
	
	public static RunLengthEncode fromIntArray(int[] values) {
		return fromIntArray(values, 0);
	}
	
	public static RunLengthEncode fromIntArray(int[] values, int startIndex) {
		
		RunLengthEncode result = new RunLengthEncode();
		
		startIndex = MathUtils.clamp(startIndex, 0, values.length - 1);
		
		int currentValue = values[startIndex];
		int runLength = 0;
		
		for (int i = startIndex; i < values.length; i++) {
			if (values[i] == currentValue) {
				runLength++;
			}
			else {
				result.runLengthPairs.add(new RunLengthPair(currentValue, runLength));
				currentValue = values[i];
				runLength = 1;
			}
		}
		
		// Add the last run-length pair.
		result.runLengthPairs.add(new RunLengthPair(currentValue, runLength));
		
		return result;
		
	}

	/** Generates a list of run-length pairs from an array of quantized DCT blocks. */
	public static List<RunLengthPair> generateRunLengthPairsFromQuantizedBlocks(QuantizedBlock[] quantizedBlocks) {
		return Arrays.stream(quantizedBlocks)
				.flatMap(block -> {
					int[] enumerated = ArrayUtils.enumerateArray(block.getValues());
					List<RunLengthPair> pairs = RunLengthEncode.fromIntArray(enumerated, 1).getRunLengthPairs();
					pairs.add(0, new RunLengthPair(enumerated[0], 0));
					return pairs.stream();
				})
				.collect(Collectors.toList());
	}
	
	public static int calculateImageEncodedSize(List<RunLengthPair> runLengthPairs, int codeSizeLimit, int lengthSizeLimit) {
		return runLengthPairs.stream().mapToInt(pair -> codeSizeLimit + (pair.getLength() == 0 ? 0 : lengthSizeLimit)).sum();
	}
	
}
