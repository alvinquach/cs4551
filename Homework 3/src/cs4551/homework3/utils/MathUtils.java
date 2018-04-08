package cs4551.homework3.utils;

/**
 * @author Alvin Quach
 */
public class MathUtils {
	
	public static int clamp(int value, int min, int max) {
		return value < min ? min : value > max ? max : value;
	}
	
	public static float clamp(float value, float min, float max) {
		return value < min ? min : value > max ? max : value;
	}
	
	public static float[] multiplyMatrix(float[][] a, float[] b) throws Exception {
		
		if (a[0].length != b.length) {
			throw new Exception("Matrix dimensions mismatch!");
		}
		
		int resultLength = a.length;
		
		float[] result = new float[resultLength];
		
		for (int r = 0; r < resultLength; r++) {
			result[r] = 0;
			for (int c = 0; c < b.length; c++) {
				result[r] += a[r][c] * b[c];
			}
		}
		
		return result;
		
	}
	
}
