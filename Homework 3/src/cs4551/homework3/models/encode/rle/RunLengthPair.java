package cs4551.homework3.models.encode.rle;

import java.util.ArrayList;
import java.util.List;

public class RunLengthPair {

	private int code;

	private int length;

	public RunLengthPair(int code, int length) {
		super();
		this.code = code;
		this.length = length;
	}

	public int getCode() {
		return code;
	}

	public int getLength() {
		return length;
	}

	public int calculateSize(int codeSizeLimit, int lengthSizeLimit) {
		return codeSizeLimit + (length == 0 ? 0 : lengthSizeLimit);
	}

	public static RunLengthPair fromBits(boolean[] bits, int codeSizeLimit, int lengthSizeLimit) throws Exception {
		if (bits.length != codeSizeLimit && bits.length != codeSizeLimit + lengthSizeLimit) {
			throw new Exception("Invalid bit stream length!");
		}
		int code = 0;
		for (int i = 1; i < codeSizeLimit; i++) {
			code = (code << 1) + (bits[i] ? 1 : 0);
		}
		if (bits[0]) {
			code = -code - 1;
		}
		int length = 0;
		if (bits.length != codeSizeLimit) {
			for (int i = codeSizeLimit; i < lengthSizeLimit + codeSizeLimit; i++) {
				length = (length << 1) + (bits[i] ? 1 : 0);
			}
		}
		return new RunLengthPair(code, length);
	}

	/** Helper method for generating bits. */
	protected List<Boolean> generateBits(int codeSizeLimit, int lengthSizeLimit) {
		
		List<Boolean> bits = new ArrayList<>(codeSizeLimit + (length > 0 ? lengthSizeLimit : 0));
		
		// Negative value
		if ((code & (1 << 31)) != 0) {
			bits.add(true); // Negative sign
			for (int i = codeSizeLimit - 2; i >= 0; i--) {
				bits.add((code & (1 << i)) == 0);
			}
		}
		
		// Positive value
		else {
			bits.add(false); // Positive sign
			for (int i = codeSizeLimit - 2; i >= 0; i--) {
				bits.add((code & (1 << i)) != 0);
			}
		}
		
		// Length can only be positive.
		if (length > 0) {
			for (int i = lengthSizeLimit - 1; i >= 0; i--) {
				bits.add((length & (1 << i)) != 0);
			}
		}
		return bits;
	}
	
	@Override
	public String toString() {
		return "(" + code + ", " + length + ")";
	}

}
