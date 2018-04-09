package cs4551.homework3.models.image.dct;

import cs4551.homework3.utils.MathUtils;

/** 8x8 DCT block */
public class DCTBlock {
	
	public static final int BLOCK_SIZE = 8;
	
	private static final float DCT_MIN_VALUE = -(float)Math.pow(2, 10);
	private static final float DCT_MAX_VALUE =  (float)Math.pow(2, 10);

	private float[][] values;
	
	public DCTBlock(float[][] input) throws Exception {
		
		if (input.length != BLOCK_SIZE || input[0].length != BLOCK_SIZE) {
			throw new Exception("Input must be an " + BLOCK_SIZE + "x" + BLOCK_SIZE + " array!");
		}
		
		values = new float[BLOCK_SIZE][BLOCK_SIZE];
		
		// Apply the DCT to input.
		for (int u = 0; u < BLOCK_SIZE; u++) {
			for (int v = 0; v < BLOCK_SIZE; v++) {
				float c = getC(u) * getC(v) / 4;
				float sum = 0;
				for (int i = 0; i < BLOCK_SIZE; i++) {
					float cos = (float)Math.cos((2 * i + 1) * u * Math.PI / (2 * BLOCK_SIZE));
					for (int j = 0; j < BLOCK_SIZE; j++) {
						sum += cos * Math.cos((2 * j + 1) * v * Math.PI / (2 * BLOCK_SIZE)) * input[i][j];
					}
				}
				float value = c * sum; // Raw DCT value.
				
				// Clamp the value to between -2e10 and 2e10, and then assign it to the array.
				values[u][v] = MathUtils.clamp(value, DCT_MIN_VALUE, DCT_MAX_VALUE);
			}
		}
		
	}
	
	public float[][] computeIDCT() {
		float[][] result = new float[BLOCK_SIZE][BLOCK_SIZE];
		for (int i = 0; i < BLOCK_SIZE; i++) {
			for (int j = 0; j < BLOCK_SIZE; j++) {
				float sum = 0;
				for (int u = 0; u < BLOCK_SIZE; u++) {
					float cos = (float)Math.cos((2 * i + 1) * u * Math.PI / (2 * BLOCK_SIZE));
					for (int v = 0; v < BLOCK_SIZE; v++) {
						sum += getC(u) * getC(v) / 4 * cos * Math.cos((2 * j + 1) * v * Math.PI / (2 * BLOCK_SIZE)) * values[u][v];
					}
				}
				result[i][j] = sum;
			}
		}
		return result;
	}
	
	public float[][] getValues() {
		return values;
	}

	/** Helper method for calculating the C coefficient. */
	private static float getC(int coord) {
		return coord == 0 ? (float)Math.sqrt(2) / 2 : 1.0f;
	}
	
}
