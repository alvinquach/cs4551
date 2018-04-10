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
	
	public static void printCompressionRatio(int n, int width, int height, int lumaSize, int cbSize, int crSize) {
		System.out.println(formatCompressionRatio(n, width, height, lumaSize, cbSize, crSize));
	}
	
	public static String formatCompressionRatio(int n, int width, int height, int lumaSize, int cbSize, int crSize) {
		int originalSize = width * height * 24;
		int compressedSize = lumaSize + cbSize + crSize;
		StringBuilder sb = new StringBuilder()
				.append("-----------------------------------------------------------------").append("\n")
				.append("For a quantization level n = " + n + ":").append("\n")
				.append("The original image cost, (S), is " + originalSize + " bits.").append("\n")
				.append("The Y' values cost is " + lumaSize + " bits.").append("\n")
				.append("The Cb values cost is " + cbSize + " bits.").append("\n")
				.append("The Cr values cost is " + crSize + " bits.").append("\n")
				.append("The total compressed Image cost, (D), is " + compressedSize + " bits.").append("\n")
				.append("The compression ratio, (S/D), is " + ((float)originalSize / compressedSize) + ".").append("\n");
		return sb.toString();
	}

}
