package utils;

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

	/**
	 * Multiplies a matrix by a vector. 
	 * Note that the matrix should be arranged in a[row][column] instead of a[x][y].
	 * @param a Matrix
	 * @param b Vector
	 * @return A vector representing the product of a and b
	 * @throws Exception
	 */
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
