package com.alvinquach.cs4551.homework1.operations;

import com.alvinquach.cs4551.homework1.models.image.ClonableImage;
import com.alvinquach.cs4551.homework1.models.quantization.QuantizedIntensities;
import com.alvinquach.cs4551.homework1.utils.ErrorDiffusionUtils;
import com.alvinquach.cs4551.homework1.utils.QuantizationUtils;

/**
 * @author Alvin Quach
 */
public class HybridErrorDiffuser extends ImageOperation {

	private QuantizedIntensities quantizedRedGreen;
	private QuantizedIntensities quantizedBlue;

	public HybridErrorDiffuser(ClonableImage image) {
		super(image);
		quantizedRedGreen = QuantizationUtils.generateUniformQuantizedIntensities(8);
		quantizedBlue = QuantizationUtils.generateUniformQuantizedIntensities(4);
	}

	@Override
	public void apply() {
		
		// Apply Error Diffusion.
		for (int y = 0; y < image.getH(); y++) {
			for (int x = 0; x < image.getW(); x++) {
				int[] rgb = new int[3];
				int[] error = new int[3];
				image.getPixel(x, y, rgb);
				
				// Calculate quantized color and error for each channel of the pixel,
				// and store the new color back in the rgb array.
				for (int i = 0; i < 3; i++) {
					int value = rgb[i];
					rgb[i] = QuantizationUtils.getQuantizedIntensitySegment(value, i == 2 ? quantizedBlue : quantizedRedGreen).getValue();
					error[i] = value - rgb[i];
				}
				
				// Apply new quantized color and distribute the error.
				image.setPixel(x, y, rgb);
				ErrorDiffusionUtils.distrubuteError(image, x, y, error);
			}
		}	

	}

	@Override
	protected String getFileSuffix() {
		return "colorerrordiffusion";
	}

}
