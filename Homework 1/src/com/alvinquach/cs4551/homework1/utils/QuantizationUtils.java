package com.alvinquach.cs4551.homework1.utils;

import com.alvinquach.cs4551.homework1.models.ColorLUT;
import com.alvinquach.cs4551.homework1.models.quantization.QuantizationSegment;
import com.alvinquach.cs4551.homework1.models.quantization.QuantizationSegmentInfo;
import com.alvinquach.cs4551.homework1.models.quantization.QuantizedIntensities;

/**
 * @author Alvin Quach
 */
public class QuantizationUtils {
	
	public static ColorLUT generateUniformColorLUT() {
		ColorLUT result = new ColorLUT();
		QuantizedIntensities quantizedRedGreen = QuantizationUtils.generateUniformQuantizedIntensities(8);
		QuantizedIntensities quantizedBlue = QuantizationUtils.generateUniformQuantizedIntensities(4);
		for (QuantizationSegment redSegment : quantizedRedGreen.getSegments()) {
			for (QuantizationSegment greenSegment : quantizedRedGreen.getSegments()) {
				for (QuantizationSegment blueSegment : quantizedBlue.getSegments()) {
					result.add(redSegment.getValue(), greenSegment.getValue(), blueSegment.getValue());
				}
			}
		}
		return result;
	}
	
	public static QuantizedIntensities generateUniformQuantizedIntensities(int levels) {
		
		// Sanitize input
		int steps = MathUtils.clamp(levels, 2, 256);
		
		QuantizedIntensities result = new QuantizedIntensities();
		
		int stepSize = 128 / steps;
		
		for (int i = 1; i <= 2 * steps; i += 2) {
			int max = (i + 1) * stepSize;
			int value = i * stepSize;
			result.getSegments().add(new QuantizationSegment(max, value));
		}
		
		return result;
	}
	
	public static QuantizedIntensities generateThresholdQuantizedIntensities(int levels) {
		
		// Sanitize input
		int steps = MathUtils.clamp(levels, 2, 256);

		QuantizedIntensities result = new QuantizedIntensities();
		
		// Have to perform decimal calculation in order to avoid rounding errors.
		float stepSize = 127.5f / (steps - 1);
		
		for (int i = 0; i < 2 * steps; i += 2) {
			int max = (int)((i + 1) * stepSize);
			int value = (int)(i * stepSize);
			result.getSegments().add(new QuantizationSegment(max, value));
		}
		
		return result;
	}
	
	public static QuantizationSegmentInfo getQuantizedIntensitySegment(int value, QuantizedIntensities quantization) {
		
		// Sanitize inputs
		value = MathUtils.clamp(value, 0, 255);
		
		int i = 0;
		for (QuantizationSegment entry : quantization.getSegments()) {
			if (value <= entry.getMax() || i == quantization.size() - 1) {
				return new QuantizationSegmentInfo(i, entry.getValue());
			}
			i++;
		}
		
		// The code should in theory never reach here.
		return null;
		
	}

}
