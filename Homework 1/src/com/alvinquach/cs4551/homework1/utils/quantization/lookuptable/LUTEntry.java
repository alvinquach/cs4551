package com.alvinquach.cs4551.homework1.utils.quantization.lookuptable;

/**
 * @author Alvin Quach
 */
public class LUTEntry implements Comparable<LUTEntry> {

	private int max;
	
	private int value;
	
	public LUTEntry(int max, int value) {
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
	public int compareTo(LUTEntry o) {
		return max - o.getMax();
	}
	
	@Override
	public String toString() {
		return "Max: " + max + ", Value: " + value;
	}
	
}
