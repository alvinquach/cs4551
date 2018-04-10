package cs4551.homework3.utils;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cs4551.homework3.models.encode.RunLengthEncode;
import cs4551.homework3.models.encode.RunLengthPair;
import cs4551.homework3.models.image.quantized.QuantizedBlock;

public class EncodeUtils {
	
	/** Generates a list of run-length pairs from an array of quantized DCT blocks. */
	public static List<RunLengthPair> generateRunLengthPairsFromQuantizedBlocks(QuantizedBlock[] quantizedBlocks) {
		return Arrays.stream(quantizedBlocks)
				.flatMap(block -> {
					int[] enumerated = ArrayUtils.enumerateArray(block.getValues());
					List<RunLengthPair> pairs = new RunLengthEncode(enumerated, 1).getRunLengthPairs();
					pairs.add(0, new RunLengthPair(enumerated[0], 0));
					return pairs.stream();
				})
				.collect(Collectors.toList());
	}
	
	public static int calculateImageEncodedSize(List<RunLengthPair> runLengthPairs, int codeSizeLimit, int lengthSizeLimit) {
		return runLengthPairs.stream().mapToInt(pair -> codeSizeLimit + (pair.getLength() == 0 ? 0 : lengthSizeLimit)).sum();
	}

}
