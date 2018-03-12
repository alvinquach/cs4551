package homework2.compression.lzw;

import java.util.ArrayList;
import java.util.List;

import homework2.application.AppConstants;

/**
 * Decodes a byte array that was encoded using the LZW algorithm.
 * @author Alvin Quach
 */
public class LzwDecoder {

	private byte[] input;
	
	private List<String> initialDictionary;
	
	private List<String> dictionary = new ArrayList<>(256);
	
	private StringBuilder decodedString = new StringBuilder();
	
	/** 
	 * Decodes the input byte array using the provided initial dictionary.
	 * The final dictionary and decoded string can be retrieved through member methods.
	 * @param input The byte array to decode.
	 */
	public LzwDecoder(byte[] input, List<String> initialDictionary) {
		this.input = input.clone();
		this.initialDictionary = new ArrayList<>(initialDictionary);
		decode();
	}

	/** Retrieves that last byte array that was decoded. */
	public byte[] getInput() {
		return input;
	}

	/** Decode a new byte array. Dictionary and encoded string will be updated automatically. */
	public void setInput(byte[] input) {
		this.input = input.clone();
		decode();
	}

	/** Returns a copy of the entire dictionary. */
	public List<String> getDictionary() {
		return dictionary;
	}

	/** Returns the decoded string. */
	public String getDecodedString() {
		return decodedString.toString();
	}
	
	/** Redefines the initial dictionary entries. The byte array will be re-decoded. */
	public void setInitialDictionary(List<String> initialDictionary) {
		this.initialDictionary = new ArrayList<>(initialDictionary);
		decode();
	}
	
	private void decode() {
		
		// Clear any existing data.
		dictionary.clear();
		dictionary.addAll(initialDictionary);
		decodedString.setLength(0);
		
		// Loop through the encoded byte array.
		for (int index = 0; index < input.length; index++) {
			
			// TODO Add error handling.
			
			// Add entry at the index from the dictionary to decoded string.
			String entry = dictionary.get(input[index] & 0xFF);
			decodedString.append(entry);
			
			// If the dictionary size is already maxed out, then skip to the next index.
			// The rest of the code in this for-loop is for adding more dictionary entries.
			if (dictionary.size() >= AppConstants.MAX_DICT_SIZE) {
				continue;
			}
			
			if (index + 1 < input.length) {
				
				// Get the next dictionary index from the next encoded symbol.
				int nextDictIndex = input[index + 1] & 0xFF;
				
				// If the next index exists in the dictionary, then add E + First_Symbol(E`) to the dictionary.
				if (nextDictIndex < dictionary.size()) {
					dictionary.add(entry + dictionary.get(nextDictIndex).charAt(0));
				}
				
				// If the next index doesn't exist in the dictionary, then add E + First_Symbol(E) to the dictionary.
				else {
					dictionary.add(entry + entry.charAt(0));
				}
				
			}
			
		}
		
	}
	
}
