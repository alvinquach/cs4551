package homework2.resampler;

import homework2.models.image.Image;

/**
 * @author Alvin Quach
 */
public abstract class ImageDownsampler {

	public abstract Image resample(Image image, int K);

	protected final boolean isValidInput(Image image, int K) {

		return 

				// These resamplers can only operate on images that have power of 2 dimensions.
				image.getW() == Integer.highestOneBit(image.getW()) && image.getH() == Integer.highestOneBit(image.getH())

				// The downsampling factor also needs to be a power of 2, and it should not be greater than either of the image dimensions.
				&& K == Integer.highestOneBit(K) && K <= image.getH() && K <= image.getW();

	}

}
