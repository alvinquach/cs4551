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
		StringBuilder sb = new StringBuilder()
				.append("Index\t\tEntry\n")
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

	/**
	 * Formats the result of the LZW encoding and decoding. 
	 * @param input The input string that was encoded by the LZW algorithm.
	 * @param dictionary The dictionary that was produced by the LZW algorithm (either through encode or decode).
	 * @param encoded The encoded byte array.
	 * @param decoded The decoded string.
	 * @param An identifier (ie. filename) of the input string.
	 * @return Formatted string containg the results of the LZW encode/decode.
	 */
	public static String formatLzwResults(String input, List<String> dictionary, byte[] encoded, String decoded, String identifier) {
		StringBuilder sb = new StringBuilder()
				.append(identifier + " Results:").append("\n\n")
				.append("Original Text:").append("\n")
				.append(input).append("\n\n")
				.append(formatLzwDictionary(dictionary)).append("\n")
				.append("Encoded Text:").append("\n")
				.append(formatByteArray(encoded)).append("\n\n")
				.append("Decoded Text:").append("\n")
				.append(decoded).append("\n\n")
				.append("Compression Ratio: ")
				.append((float)input.length() / encoded.length);
		return sb.toString();
	}

}
