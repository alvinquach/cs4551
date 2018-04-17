package application;

import models.image.ClonableImage;
import models.image.Image;

public class Testing123 {
	
	private static class Coord {
		int x, y;
	}
	
	public static void main(String[] args) {
		
		Image reference = new ClonableImage(500, 500);
		
		Image frame = new ClonableImage(500, 500);
		
		
		
	}
	
	private static Coord search(Image frame, Image reference, Coord blockLocation, int blockSize, int p) {
		
//		int hStart = MathUtils.clamp(blockLocation.x - p, 0, reference.getW() - 1);
//		int hEnd = MathUtils.clamp(blockLocation.x + p, 0, reference.getW() - 1);
//		int vStart = MathUtils.clamp(blockLocation.y - p, 0, reference.getH() - 1);
//		int vEnd = MathUtils.clamp(b, min, max)
		
		int difference = 0;
		int[] frameColor = new int[3];
		int[] refColor = new int[3];
		int minDiff = Integer.MAX_VALUE;
		Coord coord = new Coord();
		for (int x = blockLocation.x - p; x < blockLocation.x + p; x++) {
			for (int y = blockLocation.y - p; y < blockLocation.y + p; y++) {
				for (int _x = 0; _x < blockSize; _x++) {
					for (int _y = 0; _y < blockSize; _y++) {
						frame.getPixel(_x + blockLocation.x, _y + blockLocation.y, frameColor);
						reference.getPixel(_x + x, _y + _x + x, frameColor);
						int diff = frameColor[1] - refColor[1]; // Using only green channel for now.
						difference += diff * diff;
					}
				}
				if (difference < minDiff) {
					minDiff = difference;
					coord.x = x;
					coord.y = y;
				}
				difference = 0;
			}
		}
		return coord;
	}

}
