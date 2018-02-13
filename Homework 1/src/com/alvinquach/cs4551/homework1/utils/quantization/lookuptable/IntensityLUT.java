package com.alvinquach.cs4551.homework1.utils.quantization.lookuptable;

import java.util.TreeSet;

/**
 * @author Alvin Quach
 */
public class IntensityLUT {
	
	private TreeSet<LUTEntry> entries = new TreeSet<>();

	public TreeSet<LUTEntry> getEntries() {
		return entries;
	}
	
	public int size() {
		return entries.size();
	}
	
	@Override
	public String toString() {
		String result = "";
		for (LUTEntry entry : entries) {
			result += entry.toString() + "\n";
		}
		return result;
	}
	
}
