package com.alvinquach.cs4551.homework1.operations;

import com.alvinquach.cs4551.homework1.models.image.ClonableImage;
import com.alvinquach.cs4551.homework1.models.quantization.QuantizedIntensities;
import com.alvinquach.cs4551.homework1.utils.MathUtils;
import com.alvinquach.cs4551.homework1.utils.QuantizationUtils;

/**
 * @author Alvin Quach
 */
public class NLevelErrorDiffuser extends ImageOperation {

	private int levels;
	
	private QuantizedIntensities quantizedIntensities;

	public NLevelErrorDiffuser(ClonableImage image, int levels) {
		super(image);

		// Make sure level is at least 2 and not greater than 256.
		this.levels = MathUtils.clamp(levels, 2, 256);
		
		quantizedIntensities = QuantizationUtils.generateThresholdQuantizedIntensities(this.levels);
	}

	@Override
	public void apply() {

		// Convert the image to grayscale first.
		new GrayscaleConverter(image).apply();

		// Replace image intensity values with quantized values.
		for (int y = 0; y < image.getH(); y++) {
			for (int x = 0; x < image.getW(); x++) {
				int[] rgb = new int[3];
				image.getPixel(x, y, rgb);
				int value = rgb[0];
				int quantized = QuantizationUtils.getQuantizedIntensitySegment(value, quantizedIntensities).getValue();
				image.setPixel(x, y, new int[] {quantized, quantized, quantized});
				int error = value - quantized;
				distrubuteError(x, y, error);
			}
		}	
	}

	private void distrubuteError(int x, int y, int error) {
		int[] rgb = new int[3];

		// Bottom pixels
		if (y < image.getH() - 1) {

			// Bottom left pixel
			if (x > 0) {
				image.getPixel(x - 1, y + 1, rgb);
				int value = rgb[0];
				int newValue = MathUtils.clamp(value + error * 3 / 16, 0, 255);
				image.setPixel(x - 1, y + 1, new int[]{newValue, newValue, newValue});
			}

			// Bottom right pixel
			if (x < image.getW() - 1) {
				image.getPixel(x + 1, y + 1, rgb);
				int value = rgb[0];
				int newValue = MathUtils.clamp(value + error * 1 / 16, 0, 255);
				image.setPixel(x + 1, y + 1, new int[]{newValue, newValue, newValue});
			}

			// Bottom center pixel
			image.getPixel(x, y + 1, rgb);
			int value = rgb[0];
			int newValue = MathUtils.clamp(value + error * 5 / 16, 0, 255);
			image.setPixel(x, y + 1, new int[]{newValue, newValue, newValue});
		}

		// Right pixel
		if (x < image.getW() - 1) {
			image.getPixel(x + 1, y, rgb);
			int value = rgb[0];
			int newValue = MathUtils.clamp(value + error * 7 / 16, 0, 255);
			image.setPixel(x + 1, y, new int[]{newValue, newValue, newValue});
		}

	}

	@Override
	protected String getFileSuffix() {
		return "errordiffusion-" + levels + "level";
	}

}
