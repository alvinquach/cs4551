package com.alvinquach.cs4551.homework1.utils.quantization.lookuptable;

/**
 * Contains the index and value of the LUT segment.
 * @author Alvin Quach
 */
public class LUTSegmentInfo {
	
	private int index;
	
	private int value;
	
	public LUTSegmentInfo(int index, int value) {
		this.index = index;
		this.value = value;
	}

	public int getIndex() {
		return index;
	}

	public int getValue() {
		return value;
	}

}
