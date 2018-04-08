package cs4551.homework3.models.image.dct;

import cs4551.homework3.utils.PrintUtils;

/** 8x8 DCT block */
public class DCTBlock {

	private float[][] values;
	
	public DCTBlock(float[][] input) throws Exception {
		
		if (input.length != 8 || input[0].length != 8) {
			throw new Exception("Input must be an 8x8 array!");
		}
		
		values = new float[8][8];
		
		// Apply the DCT to input.
		for (int u = 0; u < 8; u++) {
			for (int v = 0; v < 8; v++) {
				float c = getC(u) * getC(v) / 4;
				float sum = 0;
				for (int i = 0; i < 8; i++) {
					float cos = (float)Math.cos((2 * i + 1) * u * Math.PI / 16);
					for (int j = 0; j < 8; j++) {
						sum +=  cos * Math.cos((2 * j + 1) * v * Math.PI / 16) * input[i][j];
					}
				}
				values[u][v] = c * sum;
			}
		}
		
	}
	
	public void printContents() {
		printContents(null);
	}
	
	public void printContents(String name) {
		PrintUtils.printArray(name, values);
	}
	
	/** Helper method for calculating the C coefficient. */
	private static float getC(int coord) {
		return coord == 0 ? (float)Math.sqrt(2) / 2 : 1.0f;
	}
	
}
