package com.alvinquach.cs4551.homework1.models.quantization;

/**
 * Contains the index and value of the quantized segment.
 * @author Alvin Quach
 */
public class QuantizationSegmentInfo {
	
	private int index;
	
	private int value;
	
	public QuantizationSegmentInfo(int index, int value) {
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
