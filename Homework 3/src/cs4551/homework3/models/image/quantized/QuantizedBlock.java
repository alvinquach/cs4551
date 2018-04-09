package cs4551.homework3.models.image.quantized;

import cs4551.homework3.models.image.ImageConstants;
import cs4551.homework3.utils.MathUtils;

public class QuantizedBlock {

	private static final int BLOCK_SIZE = ImageConstants.JPEG_BLOCK_SIZE;
	
	private int[][] values;

	private int compressionLevel;
	
	private QuantizationTable quantizationTable;
	
	public QuantizedBlock(float[][] values, int n, QuantizationTable quantizationTable) throws Exception {

		this(n, quantizationTable);

		if (values.length != BLOCK_SIZE || values[0].length != BLOCK_SIZE) {
			throw new Exception("Input must be an " + BLOCK_SIZE + "x" + BLOCK_SIZE + " array!");
		}
		
		// Quantization table
		int[][] q = quantizationTable.getTable();

		// Compression coefficient
		float k = (float)Math.pow(2, compressionLevel);
		
		// Quantize values
		for (int y = 0; y < BLOCK_SIZE; y++) {
			for (int x = 0; x < BLOCK_SIZE; x++) {
				this.values[x][y] = Math.round(values[x][y] / (q[x][y] * k));
			}
		}

	}

	public QuantizedBlock(int n, QuantizationTable quantizationTable) throws Exception {
		values = new int[BLOCK_SIZE][BLOCK_SIZE];
		compressionLevel = MathUtils.clamp(n, 0, ImageConstants.MAX_COMPRESSION_LEVEL);
		this.quantizationTable = quantizationTable;
	}

	public int[][] getValues() {
		return values;
	}

	public QuantizationTable getQuantizationTable() {
		return quantizationTable;
	}
	
	public float[][] dequantize() {
		float[][] result = new float[BLOCK_SIZE][BLOCK_SIZE];
		int[][] q = quantizationTable.getTable();
		float k = (float)Math.pow(2, compressionLevel);
		for (int y = 0; y < BLOCK_SIZE; y++) {
			for (int x = 0; x < BLOCK_SIZE; x++) {
				result[x][y] = values[x][y] * q[x][y] * k;
			}
		}
		return result;
	}

}
