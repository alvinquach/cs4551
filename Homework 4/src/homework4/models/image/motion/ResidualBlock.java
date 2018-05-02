package homework4.models.image.motion;

import homework4.models.Coord;
import homework4.models.image.Image;
import homework4.models.image.Pixel;
import homework4.utils.ImageUtils;
import homework4.utils.ValidationUtils;

public class ResidualBlock extends Block {
	
	private Coord motionVector;
	
	private int matchCount;
	
	private ResidualBlock(int n) throws Exception {
		super(n);
		motionVector = new Coord();
		matchCount = 0;
	}

	public Coord getMotionVector() {
		return motionVector;
	}

	public int getMatchCount() {
		return matchCount;
	}

	public static ResidualBlock fromComparison(Block block, Image reference, Coord start, int p, BlockSearch searchType) throws Exception {
		if (!ValidationUtils.pIsValid(p)) {
			throw new Exception("Invalid p value of " + p);
		}
		
		int n = block.getBlockSize();
		ResidualBlock result = new ResidualBlock(n);
		
		if (searchType == BlockSearch.LINEAR) {
			int minDiff = Integer.MAX_VALUE;
			for (int py = -p; py <= p; py++) {
				for (int px = -p; px <= p; px++) {
					int blockDiff = 0;
					for (int x = 0; x < n; x++) {
						for (int y = 0; y < n; y++) {
							int pixelDiff = calculateAbsoluteDifference(block, reference, start, new Coord(x, y), new Coord(-px, -py));
							blockDiff += pixelDiff * pixelDiff; // MSD
						}
					}
					if (blockDiff < minDiff) {
						minDiff = blockDiff;
						result.motionVector.x = -px;
						result.motionVector.y = -py;
					}
				}
			}
			
		}
		else if (searchType == BlockSearch.LOGARITHMIC) {
			// TODO Implement this.
		}
		
		// Generate error block
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < n; y++) {
				int diff = calculateAbsoluteDifference(block, reference, start, new Coord(x, y), result.motionVector);
				result.pixels[x][y] = new Pixel(diff);
			}
		}
		
		return result;
	}
	
	private static int calculateAbsoluteDifference(Block block, Image reference, Coord start, Coord current, Coord offset) {
		int[] rgb = new int[3];
		try {
			reference.getPixel(start.x + current.x - offset.x, start.y + current.y - offset.y, rgb);
		}
		catch (ArrayIndexOutOfBoundsException e) {
			rgb[0] = rgb[1] = rgb[2] = 0;
		}
		int blockPixelValue = ImageUtils.calculateGrayValue(block.pixels[current.x][current.y]);
		int referencePixelValue = ImageUtils.calculateGrayValue(rgb);
		return Math.abs(blockPixelValue - referencePixelValue);
	}
	
}
