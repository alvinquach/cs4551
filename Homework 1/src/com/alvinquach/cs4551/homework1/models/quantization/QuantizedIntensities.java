package com.alvinquach.cs4551.homework1.models.quantization;

import java.util.TreeSet;

/**
 * @author Alvin Quach
 */
public class QuantizedIntensities {
	
	private TreeSet<QuantizationSegment> segments = new TreeSet<>();

	public TreeSet<QuantizationSegment> getSegments() {
		return segments;
	}
	
	public int size() {
		return segments.size();
	}
	
	@Override
	public String toString() {
		String result = "";
		for (QuantizationSegment segment : segments) {
			result += segment.toString() + "\n";
		}
		return result;
	}
	
}
