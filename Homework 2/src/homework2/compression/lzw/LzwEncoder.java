package homework2.compression.lzw;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import homework2.utils.PrintUtils;

/**
 * Encodes an input string using the LZW algorithm.
 * @author Alvin Quach
 */
public class LzwEncoder {

	private String input;
	
	private List<String> dictionary = new ArrayList<>(256);
	
	private List<Byte> encodedBytes = new LinkedList<>();
	
	public LzwEncoder(String input) {
		setInput(input);
	}

	public String getInput() {
		return input;
	}

	public void setInput(String input) {
		this.input = input;
		encode(input);
	}
	
	public List<String> getDictionary() {
		return new ArrayList<>(dictionary);
	}

	public byte[] getEncodedBytes() {
		byte[] bytes = new byte[encodedBytes.size()];
		int i = 0;
		for (Byte b : encodedBytes) {
			bytes[i++] = b;
		}
		return bytes;
	}
	
	public void printDictionary() {
		System.out.println(PrintUtils.formatLzwDictionary(dictionary));
	}
	
	public void printEncodedBytes() {
		System.out.println(PrintUtils.formatByteArray(getEncodedBytes()));
	}

	private void encode(String input) {
		
		// Clear any existing entries.
		dictionary.clear();
		encodedBytes.clear();
		
		// Add all unique characters to the dictionary.
		for (char c : input.toCharArray()) {
			String s = Character.toString(c);
			if (!dictionary.contains(s)) {
				dictionary.add(s);
			}
		}
		
		int index = 0; // The start index of the sequence.
		
		while (true) {
			
			String sequence = "";
			
			// Find longest substring that matches a dictionary entry;
			while (true) {

				// Attach next character to the sequence.
				String nextSequence = sequence + input.charAt(index + sequence.length());
				
				// If the dictionary contains the new sequence, then continue on to
				// the next iteration, unless the end of the string has been reached.
				if (dictionary.contains(nextSequence)) {
					
					sequence = nextSequence;
					
					// If the end of the string has been reached, then 
					// encode the final sequence and then break out.
					if (index + sequence.length() >= input.length()) {
						encodedBytes.add((byte)(dictionary.indexOf(sequence)));
						break;
					}
					
				}
				else {
					if (dictionary.size() < LzwProperties.MAX_DICT_SIZE) {
						dictionary.add(nextSequence);
					}
					encodedBytes.add((byte)(dictionary.indexOf(sequence)));
					break;
				}
				
			}
			
			// Increment the next sequence's start index accordingly.
			index += sequence.length();
			
			if (index > input.length() - 1) {
				break;
			}
		}
		
	}
	
}
