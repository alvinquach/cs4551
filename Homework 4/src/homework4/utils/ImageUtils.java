package homework4.utils;

import homework4.models.image.ClonableImage;
import homework4.models.image.Image;
import homework4.models.image.Pixel;
import homework4.models.image.motion.Block;

public class ImageUtils {
	
	public static int calculateGrayValue(Pixel pixel) {
		return calculateGrayValue(new int[] {pixel.getR(), pixel.getG(), pixel.getB()});
	}
	
	public static int calculateGrayValue(int[] rgb) {
		return Math.round(0.299f * rgb[0] + 0.587f * rgb[1] + 0.114f * rgb[2]);
	}
	
	public static Image blocksToImage(Block[][] blocks, int width, int height) {
		return blocksToImage(blocks, new ClonableImage(width, height));
	}
	
	public static Image blocksToImage(Block[][] blocks, Image image) {
		if (image == null) {
			return null;
		}
		int n = blocks[0][0].getBlockSize();
		for (int i = 0; i < blocks.length; i++) {
			for (int j = 0; j < blocks[0].length; j++) {
				Pixel[][] pixel = blocks[i][j].getPixels();
				for (int x = 0; x < pixel.length; x++) {
					for (int y = 0; y < pixel[0].length; y++) {
						try {
							image.setPixel(i * n + x, j * n + y, pixel[x][y].toArray());
						}
						catch (ArrayIndexOutOfBoundsException e) {
							// Very lazy and inefficient way to handle out of bounds.
						}
					}
				}
			}
		}
		return image;
	}

}
