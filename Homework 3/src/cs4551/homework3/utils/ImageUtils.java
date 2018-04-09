package cs4551.homework3.utils;

import cs4551.homework3.models.image.rgb.ClonableImage;
import cs4551.homework3.models.image.rgb.Image;
import cs4551.homework3.models.image.ycbcr.ChromaSubsampling;

/**
 * @author Alvin Quach
 */
public class ImageUtils {

	
	/**
	 * Pads an {@code Image} such that the width and height are multiples of the specified number.
	 * The padded pixels will be black (0, 0, 0), and a new instance of an {@code Image} will be returned.
	 * @param source The source {@code Image} to be padded.
	 * @param multiple The resulting dimensions of {@code Image} will be a multiple of this number.
	 * @return A new {@code Image} instance with padded pixels.
	 */
	public static Image padImage(Image source, int multiple) {
		
		int width = source.getW();
		int height = source.getH();

		int paddedWidth = width % multiple > 0 ? width / multiple * multiple + multiple : width;
		int paddedHeight = height % multiple > 0 ? height / multiple * multiple + multiple : height;

		Image result = new ClonableImage(paddedWidth, paddedHeight);

		int[] rgb = new int[3];
		for (int y = 0; y < source.getH(); y++) {
			for (int x = 0; x < source.getW(); x++) {
				source.getPixel(x, y, rgb);
				result.setPixel(x, y, rgb);
			}
		}

		return result;
	}

	
	/**
	 * Crops an image to an area.
	 * @param source The {@code Image} to be cropped.
	 * @param startX The x-coordinate of the top-left corner of the crop area.
	 * @param startY The y-coordinate of the top-left corner of the crop area.
	 * @param width The width of the crop area.
	 * @param height The height of the crop area.
	 * @return A new {@code Image} instance containing the cropped area.
	 */
	public static Image cropImage(Image source, int startX, int startY, int width, int height) {

		// Sanitize inputs
		startX = MathUtils.clamp(startX, 0, source.getW() - 2);
		startY = MathUtils.clamp(startY, 0, source.getH() - 2);
		width = MathUtils.clamp(width, 1, source.getW() - startX - 1);
		height = MathUtils.clamp(height, 1, source.getH() - startY - 1);

		Image result = new ClonableImage(width, height);

		int[] rgb = new int[3];
		for (int y = startY; y < height + startY; y++) {
			for (int x = startX; x < width + startX; x++) {
				source.getPixel(x, y, rgb);
				result.setPixel(x, y, rgb);
			}
		}

		return result;
	}
	
	
	/** Calculates the width of the chroma components given the width of the luma component and the subsampling rate. */
	public static int getChromaWidth(int width, ChromaSubsampling subsampling) {
		return subsampling.getH() * width / 4;
	}
	
	
	/** Calculates the height of the chroma components given the height of the luma component and the subsampling rate. */
	public static int getChromaHeight(int height, ChromaSubsampling subsampling) {
		return subsampling.getV() == 0 ? height / 2 : height;
	}

}
