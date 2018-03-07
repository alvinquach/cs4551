package homework2.resampler;

import homework2.models.image.ClonableImage;
import homework2.models.image.Image;
import homework2.utils.ImageUtils;

/**
 * @author Alvin Quach
 */
public class Convolution3x3Downsampler extends ImageDownsampler {
	
	private float[][] filter;
	
	public Convolution3x3Downsampler(int a, int b, int c, int d, int e, int f, int g, int h, int i) {
		float scale = a + b + c + d + e + f + g + h + i;
		filter = new float[][] {
			new float[] {a / scale, b / scale, c / scale},
			new float[] {d / scale, e / scale, f / scale},
			new float[] {g / scale, h / scale, i / scale}
		};
	}

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
				float[] rgbSum = new float[] {0f, 0f, 0f};
				for (int i = 0; i < 3; i++) {
					for (int j = 0; j < 3; j++) {
						int[] rgb = ImageUtils.getBoundarySafePixel(image, x * K + i - 1, y * K + j - 1);
						for (int k = 0; k < 3; k++) {
							rgbSum[k] += rgb[k] * filter[i][j];
						}
					}
				}
				result.setPixel(x, y, new int[] {(int)rgbSum[0], (int)rgbSum[1], (int)rgbSum[2]});
			}
		}
		
		return result;
	}

}
