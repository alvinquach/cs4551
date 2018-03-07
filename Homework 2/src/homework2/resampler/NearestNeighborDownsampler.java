package homework2.resampler;

import homework2.models.image.ClonableImage;
import homework2.models.image.Image;

/**
 * @author Alvin Quach
 */
public class NearestNeighborDownsampler extends ImageDownsampler {

	@Override
	public Image resample(Image image, int K) {
		
		if (!isValidInput(image, K)) {
			return null;
		}
		
		int width = image.getW() / K;
		int height = image.getH() / K;
		
		Image result = new ClonableImage(width, height);
		
		for (int y = 0; y < height; y++) {
			for (int x = 0; x < width; x++) {
				int[] rgb = new int[3];
				image.getPixel(x * K, y * K, rgb);
				result.setPixel(x, y, rgb);
			}
		}
		
		return result;
	}
	
}
