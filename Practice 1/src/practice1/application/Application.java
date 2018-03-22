package practice1.application;

public class Application {

	public static void main(String[] args) {
		
		// Initial Array
		float[][] A = {
			{139,144,149,153,155,155,155,155},
            {144,151,153,156,159,156,156,156},
            {150,155,160,163,158,156,156,156},
            {159,161,162,160,160,159,159,159},
            {159,160,161,162,162,155,155,155},
            {161,161,161,161,160,157,157,157},
            {162,162,161,163,162,157,157,157},
            {162,162,161,161,163,158,158,158}
        };
		printArray("A", A);
		
		offsetValues(A, -128);
		
		// DCT
		float[][] B = new float[8][8];
		for (int u = 0; u < 8; u++) {
			for (int v = 0; v < 8; v++) {
				float c = getC(u) * getC(v) / 4;
				float sum = 0;
				for (int i = 0; i < 8; i++) {
					for (int j = 0; j < 8; j++) {
						sum += Math.cos((2 * i + 1) * u * Math.PI / 16) * Math.cos((2 * j + 1) * v * Math.PI / 16) * A[i][j];
					}
				}
				B[u][v] = c * sum;
			}
		}
		printArray("B", B);
		
		// IDCT
		float[][] C = new float[8][8];
		for (int i = 0; i < 8; i++) {
			for (int j = 0; j < 8; j++) {
				float sum = 0;
				for (int u = 0; u < 8; u++) {
					for (int v = 0; v < 8; v++) {
						sum += getC(u) * getC(v) / 4 * Math.cos((2 * i + 1) * u * Math.PI / 16) * Math.cos((2 * j + 1) * v * Math.PI / 16) * B[u][v];
					}
				}
				C[i][j] = sum;
			}
		}
		offsetValues(C, 128);
		printArray("C", C);
		
	}
	
	private static void printArray(String name, float[][] array) {
		System.out.println("Array " + name + ":");
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				System.out.printf("%.1f\t", array[y][x]);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	private static void offsetValues(float[][] array, float offset) {
		for (int y = 0; y < 8; y++) {
			for (int x = 0; x < 8; x++) {
				array[x][y] += offset;
			}
		}
	}
	
	private static float getC(int coord) {
		return coord == 0 ? (float)Math.sqrt(2) / 2 : 1.0f;
	}

}
