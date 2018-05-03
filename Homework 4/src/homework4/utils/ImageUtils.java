package homework4.utils;

import homework4.models.Coord;
import homework4.models.image.ClonableImage;
import homework4.models.image.Image;
import homework4.models.image.Pixel;
import homework4.models.image.motion.Block;
import homework4.models.image.motion.Macroblocks;
import homework4.models.image.motion.ResidualBlock;
import homework4.models.image.motion.ResidualBlocks;

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
	
	public static void replaceDynamicWithClosestStatic(Macroblocks image, ResidualBlocks residual) {
		// TODO Check if image and residual have the same number of blocks.
		for (int i = 0; i < residual.getHCount(); i++) {
			for (int j = 0; j < residual.getVCount(); j++) {
				ResidualBlock residualBlock = residual.getBlocks()[i][j];
				if (residualBlock.getMotionVector().isZero()) {
					continue;
				}
				Coord closest = findClosestStaticBlock(residual, new Coord(i, j));
				if (closest != null) {
					image.getBlocks()[i][j] = image.getBlocks()[closest.x][closest.y].clone();
				}
			}
		}
	}
	
	public static void replaceDynamicWithReference(Macroblocks source, Macroblocks destination, ResidualBlocks residual) {
		// TODO Check if images have same number of blocks.
		for (int i = 0; i < residual.getHCount(); i++) {
			for (int j = 0; j < residual.getVCount(); j++) {
				ResidualBlock residualBlock = residual.getBlocks()[i][j];
				if (residualBlock.getMotionVector().isZero()) {
					continue;
				}
				destination.getBlocks()[i][j] = source.getBlocks()[i][j].clone();
			}
		}
	}
	
	public static Coord findClosestStaticBlock(ResidualBlocks residual, Coord blockCoord) {
		// Very lazy programming
		Coord[] deltas = new Coord[] {
			new Coord(-1,  0),
			new Coord( 0, -1),
			new Coord( 1,  0),
			new Coord( 0,  1),
			new Coord(-1, -1),
			new Coord( 1, -1),
			new Coord( 1,  1),
			new Coord( -1, 1)
		};
		int distance = 1;
		while (true) {
			boolean hasInBound = false;
			for (Coord delta : deltas) {
				try {
					Coord currentSearch = new Coord(distance * delta.x + blockCoord.x, distance * delta.y + blockCoord.y);
					ResidualBlock block = residual.getBlocks()[currentSearch.x][currentSearch.y];
					if (block.getMotionVector().isZero()) {
						return currentSearch;
					}
				}
				catch (ArrayIndexOutOfBoundsException e) {
					continue;
				}
				hasInBound = true;
			}
			if (!hasInBound) {
				return null;
			}
			distance++;
		}
	}

}
