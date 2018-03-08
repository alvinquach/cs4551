package homework2.compression.lzw;

import java.util.ArrayList;
import java.util.List;

public class LzwDecoder {

	private byte[] input;
	
	private List<String> dictionary = new ArrayList<>(256);
	
	private StringBuilder decodedString = new StringBuilder();
	
	public LzwDecoder(byte[] input) {
		setInput(input);
	}

	public byte[] getInput() {
		return input;
	}

	public void setInput(byte[] input) {
		this.input = input;
		decode();
	}

	public List<String> getDictionary() {
		return dictionary;
	}

	public StringBuilder getDecodedString() {
		return decodedString;
	}
	
	private void decode() {
		
	}
	
}
