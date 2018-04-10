package cs4551.homework3.utils;

public class ArrayUtils {
	
	private static class ZigZagEnumerationState {
		boolean edgeHit = false;
		boolean nextMove = true; // true = up right, false = down left;
		int x = 0, y = 0, index = 0;
	}
	
	
	/** Enumerate an integer array in zig-zag order. */
	public static int[] enumerateArray(int[][] arr) {
		
		// TODO Check if any dimension of the input array is 0;
		
		int width = arr.length;
		int height = arr[0].length;
		
		int[] result = new int[width * height];
		
		ZigZagEnumerationState state = new ZigZagEnumerationState();
		do {
			result[state.index++] = arr[state.x][state.y];
		}
		while (!navigateZigZag(state, width, height));
		
		return result;
	}

	
	/** Rebuilds a 2D array from a 1D array that was enumerated in zig-zag order. */
	public static int[][] rebuildArray(int[] values, int width, int height) throws Exception {
		
		if (values.length < width * height) {
			throw new Exception("Not enough values to rebuild array!");
		}
		
		int[][] result = new int[width][height];
		
		ZigZagEnumerationState state = new ZigZagEnumerationState();
		do {
			result[state.x][state.y] = values[state.index++];
		}
		while (!navigateZigZag(state, width, height));
		
		return result;
	}


	/** Helper method for navigation through an array in zig-zag pattern. */
	private static boolean navigateZigZag(ZigZagEnumerationState state, int width, int height) {
		
//		System.out.println(state.x + ", " + state.y);
		
		// Reached the end
		if (state.x == width - 1 && state.y == height - 1) {
			return true;
		}
					
		// If at the boundary...
		if (state.x == 0 || state.x == width - 1 || state.y == 0 || state.y == height - 1) {
			
			// If it wasn't at the boundary during the previous step...
			if (!state.edgeHit) {
				if ((state.y == 0 || state.y == height - 1) && state.x != width -1) {
					state.x++;
				}
				else if (state.x == 0 || state.x == width - 1) {
					state.y++;
				}
				state.edgeHit = true; // Mark boundary as being encountered during this step.
				return false;
			}
			
			// If it was already at the boundary during the previous step, then change directions.
			else {
				state.nextMove = !state.nextMove;
				state.edgeHit = false;
			}
		}
					
		// Move position
		if (state.nextMove) {
			state.x++; state.y--;
		}
		else {
			state.x--; state.y++;
		}
		
		return false;
	}
}
