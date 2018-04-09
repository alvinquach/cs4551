package cs4551.homework3.utils;

public class PrintUtils {
	
	public static void printArray(String name, float[][] array) {
		if (name != null & !name.isEmpty()) {
			System.out.println("Array " + name + ":");
		}
		for (int x = 0; x < array.length; x++) {
			float[] column = array[x];
			for (int y = 0; y < column.length; y++) {
				System.out.printf("%.1f\t", column[y]);
			}
			System.out.println();
		}
		System.out.println();
	}
	
	public static void printValues(String... strings) {
		System.out.println(String.join(", ", strings));
	}

}
