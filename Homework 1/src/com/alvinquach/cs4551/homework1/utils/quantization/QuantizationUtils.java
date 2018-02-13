package com.alvinquach.cs4551.homework1.utils.quantization;

import com.alvinquach.cs4551.homework1.utils.quantization.lookuptable.IntensityLUT;
import com.alvinquach.cs4551.homework1.utils.quantization.lookuptable.LUTEntry;
import com.alvinquach.cs4551.homework1.utils.quantization.lookuptable.LUTSegmentInfo;

/**
 * @author Alvin Quach
 */
public class QuantizationUtils {
	
	public static IntensityLUT generateUniformIntensityLUT(int levels) {
		
		// Sanitize input
		int steps = levels < 2 ? 2 : levels > 256 ? 256 : levels;
		
		IntensityLUT result = new IntensityLUT();
		
		int stepSize = 255 / steps;
		
		int prevMax = 0;
		for (int i = 0; i < steps; i++) {
			int max = (i + 1) * stepSize + i;
			result.getEntries().add(new LUTEntry(max, (prevMax + max) / 2));
			prevMax = max;
		}
		
		return result;
	}
	
	public static IntensityLUT generateThresholdIntensityLUT(int levels) {
		
		// Sanitize input
		int steps = levels < 2 ? 2 : levels > 256 ? 256 : levels;
		
		IntensityLUT result = new IntensityLUT();
		
		int prevMax = -127 / (steps - 1);
		for (int i = 0; i < steps; i++) {
			int max = (int)((i + 0.5) * 255 / (steps - 1)); // Have to perform decimal calculation in order to avoid rounding errors.
			result.getEntries().add(new LUTEntry(max, (prevMax + max + 1) / 2));
			prevMax = max;
		}
		
		return result;
	}
	
	/** 
	 * Given an intensity value and a LUT, returns a {@code LUTSegmentInfo}
	 * containing the index and value of the segment that the value belongs to.
	 */
	public static LUTSegmentInfo quantizeIntensityToLUT(int value, IntensityLUT lut) {
		
		// Sanitize inputs
		value = value < 0 ? 0 : value > 255 ? 255 : value;
		
		int i = 0;
		for (LUTEntry entry : lut.getEntries()) {
			if (value <= entry.getMax() || i == lut.size() - 1) {
				return new LUTSegmentInfo(i, entry.getValue());
			}
			i++;
		}
		
		// The code should in theory never reach here.
		return null;
		
	}

}
