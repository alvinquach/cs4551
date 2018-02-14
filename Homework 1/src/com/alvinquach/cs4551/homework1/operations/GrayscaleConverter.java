package com.alvinquach.cs4551.homework1.operations;

import com.alvinquach.cs4551.homework1.models.image.ClonableImage;

/**
 * @author Alvin Quach
 */
public class GrayscaleConverter extends ImageOperation {

	public GrayscaleConverter(ClonableImage image) {
		super(image);
	}

	@Override
	public void apply() {
		for (int y = 0; y < image.getH(); y++) {
			for (int x = 0; x < image.getW(); x++) {
				int[] rgb = new int[3];
				image.getPixel(x, y, rgb);
				int intensity = toGrayscale(rgb);
				image.setPixel(x, y, new int[]{intensity, intensity, intensity});
			}
		}	
	}

	private int toGrayscale(int[] rgb) {
		return (int)Math.round(0.299 * rgb[0] + 0.587 * rgb[1] + 0.114 * rgb[2]);
	}

	@Override
	protected String getFileSuffix() {
		return "gray";
	}

}
