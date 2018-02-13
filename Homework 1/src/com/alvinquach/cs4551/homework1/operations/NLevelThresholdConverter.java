package com.alvinquach.cs4551.homework1.operations;

import com.alvinquach.cs4551.homework1.image.Image;
import com.alvinquach.cs4551.homework1.utils.math.MathUtils;
import com.alvinquach.cs4551.homework1.utils.quantization.QuantizationUtils;
import com.alvinquach.cs4551.homework1.utils.quantization.lookuptable.IntensityLUT;

public class NLevelThresholdConverter extends ImageOperation {

	private int levels;

	public NLevelThresholdConverter(Image image, int levels) {
		super(image);

		// Make sure level is at least 2 and not greater than 256.
		this.levels = MathUtils.clamp(levels, 2, 256);
	}

	@Override
	public void apply() {

		// Convert the image to grayscale first.
		new GrayscaleConverter(image).apply();

		// Generate a linear LUT based on desired bit level.
		IntensityLUT lut = QuantizationUtils.generateThresholdIntensityLUT(levels);

		// Replace image intensity values with values from LUT.
		for (int y = 0; y < image.getH(); y++) {
			for (int x = 0; x < image.getW(); x++) {
				int[] rgb = new int[3];
				image.getPixel(x, y, rgb);
				int quantized = QuantizationUtils.quantizeIntensityToLUT(rgb[0], lut).getValue();
				image.setPixel(x, y, new int[]{quantized, quantized, quantized});
			}
		}	
	}

	@Override
	protected String getFileSuffix() {
		return "threshold-" + levels + "level";
	}

}
