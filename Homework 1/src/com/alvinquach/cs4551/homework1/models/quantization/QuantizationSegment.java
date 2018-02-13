package com.alvinquach.cs4551.homework1.models.quantization;

/**
 * @author Alvin Quach
 */
public class QuantizationSegment implements Comparable<QuantizationSegment> {

	private int max;
	
	private int value;
	
	public QuantizationSegment(int max, int value) {
		this.max = max;
		this.value = value;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	@Override
	public int compareTo(QuantizationSegment o) {
		return max - o.getMax();
	}
	
	@Override
	public String toString() {
		return "Max: " + max + ", Value: " + value;
	}
	
}
