package com.alvinquach.cs4551.homework1.utils.quantization.lookuptable;

/**
 * @author Alvin Quach
 */
public class ColorLUT {
	
	private IntensityLUT red = new IntensityLUT();
	
	private IntensityLUT green = new IntensityLUT();
	
	private IntensityLUT blue = new IntensityLUT();

	public IntensityLUT getRed() {
		return red;
	}

	public IntensityLUT getGreen() {
		return green;
	}

	public IntensityLUT getBlue() {
		return blue;
	}
	
}
