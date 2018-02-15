package com.alvinquach.cs4551.homework1.utils;

import com.alvinquach.cs4551.homework1.models.image.Image;

/**
 * @author Alvin Quach
 */
public class ErrorDiffusionUtils {
	
	public static void distrubuteError(Image image, int x, int y, int error) {
		distrubuteError(image, x, y, new int[] {error, error, error});
	}
	
	public static void distrubuteError(Image image, int x, int y, int[] error) {
		if (error.length < 3) {
			return;
		}
		
		int[] rgb = new int[3];

		// Bottom pixels
		if (y < image.getH() - 1) {

			// Bottom left pixel
			if (x > 0) {
				image.getPixel(x - 1, y + 1, rgb);
				for (int i = 0; i < 3; i++) {
					rgb[i] = MathUtils.clamp(rgb[i] + error[i] * 3 / 16, 0, 255);
				}
				image.setPixel(x - 1, y + 1, rgb);
			}

			// Bottom right pixel
			if (x < image.getW() - 1) {
				image.getPixel(x + 1, y + 1, rgb);
				for (int i = 0; i < 3; i++) {
					rgb[i] = MathUtils.clamp(rgb[i] + error[i] * 1 / 16, 0, 255);
				}
				image.setPixel(x + 1, y + 1, rgb);
			}

			// Bottom center pixel
			image.getPixel(x, y + 1, rgb);
			for (int i = 0; i < 3; i++) {
				rgb[i] = MathUtils.clamp(rgb[i] + error[i] * 5 / 16, 0, 255);
			}
			image.setPixel(x, y + 1, rgb);
		}

		// Right pixel
		if (x < image.getW() - 1) {
			image.getPixel(x + 1, y, rgb);
			for (int i = 0; i < 3; i++) {
				rgb[i] = MathUtils.clamp(rgb[i] + error[i] * 7 / 16, 0, 255);
			}
			image.setPixel(x + 1, y, rgb);
		}

	}

}
