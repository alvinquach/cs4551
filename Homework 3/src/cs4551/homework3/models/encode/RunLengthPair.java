package cs4551.homework3.models.encode;

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

}
