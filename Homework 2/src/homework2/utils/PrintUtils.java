package homework2.utils;

import java.util.List;

/**
 * Utility class for printing data to console.
 * @author Alvin
 */
public class PrintUtils {

	/**
	 * Formats an LZW dictionary into a string for printing or for writing to a file.
	 * @param dictionary The LZW dictionary
	 * @return A formatted string containing the contents of the dictionary
	 */
	public static String formatLzwDictionary(List<String> dictionary) {
		StringBuilder sb = new StringBuilder();
		sb.append("Index\t\tEntry\n")
		.append("---------------------\n");
		int i = 0;
		for (String entry : dictionary) {
			sb.append(i++ + "\t\t" + entry).append("\n");
		}
		return sb.toString();
	}
	
	/**
	 * Formats a byte array into a space delimited string.
	 * @param bytes The byte array
	 * @return A space delimited string
	 */
	public static String formatByteArray(byte[] bytes) {
		StringBuilder sb = new StringBuilder();
		for (byte b : bytes) {
			sb.append(b & 0xFF).append(" ");
		}
		return sb.toString();
	}

}
