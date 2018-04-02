package cs4551.homework3.utils;

import cs4551.homework3.models.image.ClonableImage;
import cs4551.homework3.models.image.Image;

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
		
		int paddedWidth = source.getW() + (source.getW() % multiple > 0 ? multiple : 0);
		int paddedHeight = source.getH() + (source.getH() % multiple > 0 ? multiple : 0);
		
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

}
