package cs4551.homework3.models.encode.rle;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import cs4551.homework3.models.image.ImageConstants;
import cs4551.homework3.models.image.quantized.QuantizationTable;
import cs4551.homework3.models.image.quantized.QuantizedBlock;
import cs4551.homework3.utils.ArrayUtils;

public class RunLengthEncode {

	List<RunLengthPair> runLengthPairs = new ArrayList<>();

	public RunLengthEncode() {

	}

	public List<RunLengthPair> getRunLengthPairs() {
		return runLengthPairs;
	}

	public int totalLength() {
		return runLengthPairs.stream()
				.mapToInt(pair -> pair.getLength())
				.sum();
	}

	public Boolean[] toBitStream() {
		return toBitStream(32, 32);
	}

	public Boolean[] toBitStream(int codeSizeLimit, int lengthSizeLimit) {
		return runLengthPairs.stream()
				.flatMap(pair -> pair.generateBits(codeSizeLimit, lengthSizeLimit).stream())
				.toArray(Boolean[]::new);
	}

	public QuantizedBlock[] toQuantizedBlocks(int n, QuantizationTable quantizationTable) throws Exception {
		int blockSize = ImageConstants.JPEG_BLOCK_SIZE;
		QuantizedBlock[] result = new QuantizedBlock[totalLength() / (blockSize * blockSize - 1)];
		int blockIndex = 0;
		int[] values = new int[blockSize * blockSize];
		int valueIndex = 0;
		for (RunLengthPair pair : runLengthPairs) {
			if (pair.getLength() == 0) {
				values[valueIndex++] = pair.getCode();
				continue;
			}
			for (int i = 0; i < pair.getLength(); i++) {
				try {
					values[valueIndex++] = pair.getCode();
				}
				catch(ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
					System.exit(1);
				}
			}
			if (valueIndex >= values.length) {
				QuantizedBlock block = new QuantizedBlock(n, quantizationTable);
				int[][] array = ArrayUtils.rebuildArray(values, blockSize, blockSize);
				for (int j = 0; j < blockSize; j++) {
					System.arraycopy(array[j], 0, block.getValues()[j], 0, blockSize);
				}
				try {
					result[blockIndex++] = block;
				}
				catch(ArrayIndexOutOfBoundsException e) {
					e.printStackTrace();
					System.exit(1);
				}
				valueIndex = 0;
			}
		}
		return result;
	}

	public static RunLengthEncode fromIntArray(int[] values) {
		return fromIntArray(values, false);
	}

	public static RunLengthEncode fromIntArray(int[] values, boolean hasDc) {

		RunLengthEncode result = new RunLengthEncode();
		
		int startIndex = 0;

		if (hasDc) {
			result.runLengthPairs.add(new RunLengthPair(values[startIndex++], 0));
		}

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
	public static RunLengthEncode fromQuantizedBlocks(QuantizedBlock[] quantizedBlocks) {
		RunLengthEncode result = new RunLengthEncode();
		result.runLengthPairs.addAll(Arrays.stream(quantizedBlocks)
				.flatMap(block -> {
					int[] enumerated = ArrayUtils.enumerateArray(block.getValues());
					return RunLengthEncode.fromIntArray(enumerated, true).getRunLengthPairs().stream();
				})
				.collect(Collectors.toList()));
		return result;
	}

	public static int calculateImageEncodedSize(RunLengthEncode runLengthPairs, int codeSizeLimit, int lengthSizeLimit) {
		return runLengthPairs.runLengthPairs.stream().mapToInt(pair -> codeSizeLimit + (pair.getLength() == 0 ? 0 : lengthSizeLimit)).sum();
	}

}
