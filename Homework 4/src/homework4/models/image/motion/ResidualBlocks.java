package homework4.models.image.motion;

import java.util.Arrays;
import java.util.stream.Collectors;

import com.sun.glass.ui.Pixels;

import homework4.models.Coord;
import homework4.models.image.Image;
import homework4.models.image.Pixel;
import homework4.utils.MathUtils;

public class ResidualBlocks {
	
	private ResidualBlock[][] blocks;
	
	private ResidualBlocks(int hCount, int vCount) {
		blocks = new ResidualBlock[hCount][vCount];
	}

	public ResidualBlock[][] getBlocks() {
		return blocks;
	}
	
	public int getBlockSize() {
		return blocks[0][0].getBlockSize();
	}
	
	public int getHCount() {
		return blocks.length;
	}
	
	public int getVCount() {
		return blocks[0].length;
	}
	
	public void normalize() {
		int min = Integer.MAX_VALUE;
		int max = Integer.MIN_VALUE;
		int n = getBlockSize();
		for (Block block : Arrays.stream(blocks).flatMap(c -> Arrays.stream(c)).collect(Collectors.toList())) {
			for (int x = 0; x < n; x++) {
				for (int y = 0; y < n; y++) {
					int value = block.pixels[x][y].getR(); // Assume red, green, and blue values are all the same.
					if (value < min) {
						min = value;
					}
					if (value > max) {
						max = value;
					}
				}
			}
		}
		
		// Only normalize of min and max values are different.
		if (min != max) {
			final int _min = min;
			final int _max = max;
			Arrays.stream(blocks)
				.flatMap(c -> Arrays.stream(c))
				.parallel()
				.forEach(block -> {
					for (int x = 0; x < n; x++) {
						for (int y = 0; y < n; y++) {
							Pixel pixel = block.pixels[x][y];
							int value = pixel.getR();  // Assume red, green, and blue values are all the same.
							int normalized = MathUtils.normalize(value, _min, _max, 0, 255);
							pixel.setIntensity(normalized);
						}
					}
				});
		}
	}
	
	public void colorizeDynamicBlocks() {
		Arrays.stream(blocks)
			.flatMap(c -> Arrays.stream(c))
			.forEach(block -> {
				if (block.getMotionVector().x == 0 && block.getMotionVector().y == 0) {
					return;
				}
				Arrays.stream(block.getPixels()).flatMap(p -> Arrays.stream(p))
				.forEach(pixel -> {
					pixel.setR((255 + pixel.getR()) / 2);
				});
			});
	}
	
	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int y = 0; y < blocks[0].length; y++) {
			for (int x = 0; x < blocks.length; x++) {
				sb.append(blocks[x][y].getMotionVector() + " ");
			}
			sb.append("\n");
		}
		return sb.toString();
	}

	public static ResidualBlocks fromComparison(Macroblocks macroblocks, Image reference, int p, BlockSearch searchType) throws Exception {
		Macroblock[][] blocks = macroblocks.getBlocks();
		int hCount = blocks.length;
		int vCount = blocks[0].length;
		int n = macroblocks.getBlockSize();
		ResidualBlocks result = new ResidualBlocks(hCount, vCount);
		for (int i = 0; i < hCount; i++) {
			for (int j = 0; j < vCount; j++) {
				Macroblock macroblock = blocks[i][j];
				result.blocks[i][j] = ResidualBlock.fromComparison(macroblock, reference, new Coord(i * n, j * n), p, searchType);
			}
		}
		return result;
	}
	
}
