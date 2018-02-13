package com.alvinquach.cs4551.homework1.operations;

import com.alvinquach.cs4551.homework1.models.ColorLUT;
import com.alvinquach.cs4551.homework1.models.ColorRGB;
import com.alvinquach.cs4551.homework1.models.image.ClonableImage;
import com.alvinquach.cs4551.homework1.models.image.Image;
import com.alvinquach.cs4551.homework1.models.quantization.QuantizedIntensities;
import com.alvinquach.cs4551.homework1.utils.QuantizationUtils;

public class UniformColorQuantizer extends ImageOperation {

	private Image indexImage;
	private ColorLUT lut;
	private QuantizedIntensities quantizedRedGreen;
	private QuantizedIntensities quantizedBlue;

	public UniformColorQuantizer(ClonableImage image) {
		super(image);
		indexImage = image.clone();
		lut = QuantizationUtils.generateUniformColorLUT();
		quantizedRedGreen = QuantizationUtils.generateUniformQuantizedIntensities(8);
		quantizedBlue = QuantizationUtils.generateUniformQuantizedIntensities(4);
		System.out.println(lut);
	}

	@Override
	public void apply() {
		
		// Generate index image.
		for (int y = 0; y < indexImage.getH(); y++) {
			for (int x = 0; x < indexImage.getW(); x++) {
				int[] rgb = new int[3];
				indexImage.getPixel(x, y, rgb);
				int index = 
						(QuantizationUtils.getQuantizedIntensitySegment(rgb[0], quantizedRedGreen).getIndex() << 5) + 
						(QuantizationUtils.getQuantizedIntensitySegment(rgb[1], quantizedRedGreen).getIndex() << 2) +
						QuantizationUtils.getQuantizedIntensitySegment(rgb[2], quantizedBlue).getIndex();
				indexImage.setPixel(x, y, new int[]{index, index, index});
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
				image.setPixel(x, y, new int[]{color.getRed(), color.getGreen(), color.getBlue()});
			}
		}
		
	}
	
	@Override
	public void save(String name) {
		indexImage.write2PPM(name + "-index.ppm");
		image.write2PPM(name + "-" + getFileSuffix() + ".ppm");
	}

	@Override
	protected String getFileSuffix() {
		return "QT8";
	}



}
