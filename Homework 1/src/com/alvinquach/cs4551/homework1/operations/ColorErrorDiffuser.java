package com.alvinquach.cs4551.homework1.operations;

import com.alvinquach.cs4551.homework1.models.ColorLUT;
import com.alvinquach.cs4551.homework1.models.ColorRGB;
import com.alvinquach.cs4551.homework1.models.image.ClonableImage;
import com.alvinquach.cs4551.homework1.models.image.Image;
import com.alvinquach.cs4551.homework1.models.quantization.QuantizedIntensities;
import com.alvinquach.cs4551.homework1.utils.ErrorDiffusionUtils;
import com.alvinquach.cs4551.homework1.utils.QuantizationUtils;

/**
 * @author Alvin Quach
 */
public class ColorErrorDiffuser extends ImageOperation {

	private Image indexImage;
	private ColorLUT lut;
	private QuantizedIntensities quantizedRedGreen;
	private QuantizedIntensities quantizedBlue;

	public ColorErrorDiffuser(ClonableImage image) {
		super(image);
		indexImage = image.clone();
		lut = QuantizationUtils.generateUniformColorLUT();
		quantizedRedGreen = QuantizationUtils.generateUniformQuantizedIntensities(8);
		quantizedBlue = QuantizationUtils.generateUniformQuantizedIntensities(4);
		System.out.println(lut);
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
					int quantized = QuantizationUtils.getQuantizedIntensitySegment(rgb[i], i == 2 ? quantizedBlue : quantizedRedGreen).getValue();
					error[i] = rgb[i] - quantized;
				}
				
				// Distribute the error, but without applying quantized color to the pixel.
				// This will be done using the indices and LUT instead.
				ErrorDiffusionUtils.distrubuteError(image, x, y, error);
				
				// Calculate the LUT index of the pixel and updated the index image.
				int index = 
						(QuantizationUtils.getQuantizedIntensitySegment(rgb[0], quantizedRedGreen).getIndex() << 5) + 
						(QuantizationUtils.getQuantizedIntensitySegment(rgb[1], quantizedRedGreen).getIndex() << 2) +
						QuantizationUtils.getQuantizedIntensitySegment(rgb[2], quantizedBlue).getIndex();
				indexImage.setPixel(x, y, new int[] {index, index, index});
			}
		}	
		
		// Display index image.
		indexImage.display();
		
		// Replace image color values with values from LUT.
		for (int y = 0; y < image.getH(); y++) {
			for (int x = 0; x < image.getW(); x++) {
				int[] rgb = new int[3];
				indexImage.getPixel(x, y, rgb);
				ColorRGB color = lut.get(rgb[0]);
				image.setPixel(x, y, new int[] {color.getRed(), color.getGreen(), color.getBlue()});
			}
		}

	}
	
	@Override
	public void save(String name) {
		indexImage.write2PPM(name + "-index-3.ppm");
		image.write2PPM(name + "-" + getFileSuffix() + ".ppm");
	}

	@Override
	protected String getFileSuffix() {
		return "QT8-3";
	}

}
