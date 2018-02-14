package com.alvinquach.cs4551.homework1.operations;

import com.alvinquach.cs4551.homework1.models.image.ClonableImage;
import com.alvinquach.cs4551.homework1.models.quantization.QuantizedIntensities;
import com.alvinquach.cs4551.homework1.utils.MathUtils;
import com.alvinquach.cs4551.homework1.utils.QuantizationUtils;

/**
 * @author Alvin Quach
 */
public class NLevelThresholdConverter extends ImageOperation {

	private int levels;
	
	private QuantizedIntensities quantizedIntensities;

	public NLevelThresholdConverter(ClonableImage image, int levels) {
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
				int quantized = QuantizationUtils.getQuantizedIntensitySegment(rgb[0], quantizedIntensities).getValue();
				image.setPixel(x, y, new int[]{quantized, quantized, quantized});
			}
		}	
	}

	@Override
	protected String getFileSuffix() {
		return "threshold-" + levels + "level";
	}

}
