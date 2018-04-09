package cs4551.homework3.models.image.ycbcr;

import cs4551.homework3.models.image.ClonableImage;
import cs4551.homework3.models.image.Image;
import cs4551.homework3.utils.MathUtils;

public class YCbCrImage {

	private static final float[][] RGB_TO_YCBCR = {
			{ 0.2990f,  0.5870f,  0.1140f},
			{-0.1687f, -0.3313f,  0.5000f},
			{ 0.5000f, -0.4187f, -0.0813f}
	};

	private static final float[][] YCBCR_TO_RGB = {
			{1.0000f,  0.0000f,  1.4020f},
			{1.0000f, -0.3441f, -0.7141f},
			{1.0000f,  1.7720f,  0.0000f}
	};

	private ChromaSubsampling subsampling;

	private float[][] luma;

	private float[][] cb;

	private float[][] cr;

	public YCbCrImage(int width, int height, ChromaSubsampling subsampling) throws Exception {

		if (width < 4 || height < 4) {
			throw new Exception("Invalid image dimensions.");
		}

		this.subsampling = subsampling;

		luma = new float[width][height];

		int chromaWidth =  subsampling.getH() * width / 4 ;
		int chromaHeight = subsampling.getV() == 0 ? height / 2 : height;

		cb = new float[chromaWidth][chromaHeight];
		cr = new float[chromaWidth][chromaHeight];

	}

	public YCbCrImage(Image image, ChromaSubsampling subsampling) throws Exception {

		this(image.getW(), image.getH(), subsampling);

		int width = luma.length;
		int height = luma[0].length;

		// Temporary arrays for storing YCbCr and RGB values.
		float[][][] temp = new float[width][height][3];
		int[] rgb = new int[3];

		// Convert RGB to YCbCr and store it in temporary array.
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				image.getPixel(x, y, rgb);
				float[] yCbCr = MathUtils.multiplyMatrix(RGB_TO_YCBCR, new float[] {rgb[0], rgb[1], rgb[2]});
				temp[x][y] = yCbCr;

				// Copy luma value to luma array, since the luma does not 
				// need any further processing besides shifting by -128.
				luma[x][y] = yCbCr[0] - 128;
			}
		}

		int chromaWidth = cb.length;
		int chromaHeight = cb[0].length;
		int chromaWidthRatio = width / chromaWidth;
		int chromaHeightRatio = height / chromaHeight;
		int subsamples = chromaWidthRatio * chromaHeightRatio;

		// Generate chroma values.
		for (int x = 0; x < chromaWidth; x++) {
			for (int y = 0; y < chromaHeight; y++) {

				float cb = 0;
				float cr = 0;

				// No subsampling needed if 4:4:4
				if (subsampling == ChromaSubsampling.YCRCB_444) {
					float[] yCbCr = temp[x][y];
					cb = yCbCr[1];
					cr = yCbCr[2];
				}

				// Subsample chroma values.
				else {
					for (int _x = 0; _x < chromaWidthRatio; _x++) {
						for (int _y = 0; _y < chromaHeightRatio; _y++) {
							float[] yCbCr = temp[_x + x * chromaWidthRatio][_y + y * chromaHeightRatio];
							cb += yCbCr[1];
							cr += yCbCr[2];
						}
					}

					// Divide by number of subsamples to get the average.
					cb /= subsamples;
					cr /= subsamples;
				}

				// Shift chroma values by -0.5, and clamp if needed.
				cb = MathUtils.clamp(cb - 0.5f, -128, 127);
				cr = MathUtils.clamp(cr - 0.5f, -128, 127);

				// Assign values to cb and cr arrays.
				this.cb[x][y] = cb;
				this.cr[x][y] = cr;

			}
		}

	}

	public ChromaSubsampling getSubsampling() {
		return subsampling;
	}

	public float[][] getLuma() {
		return luma;
	}
	
	public float[][] getCb() {
		return cb;
	}

	public float[][] getCr() {
		return cr;
	}

	/**
	 * Generates an RGB representation of this {@code YCbCrImage}.
	 * @return An {@code Image} object
	 * @throws Exception
	 */
	public Image toRGBImage() throws Exception {

		int width = luma.length;
		int height = luma[0].length;

		Image result = new ClonableImage(width, height);

		int chromaWidth = cb.length;
		int chromaHeight = cb[0].length;
		int chromaWidthRatio = width / chromaWidth;
		int chromaHeightRatio = height / chromaHeight;

		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {

				int chromaX = x / chromaWidthRatio;
				int chromaY = y / chromaHeightRatio;

				// Add 128 to luma, supersample and add 0.5 to chroma.
				float[] yCbCr = {luma[x][y] + 128, cb[chromaX][chromaY] + 0.5f, cr[chromaX][chromaY] + 0.5f};

				// Convert values to RGB.
				float[] rgbFloats = MathUtils.multiplyMatrix(YCBCR_TO_RGB, yCbCr);
				
				// Round and clamp RGB components
				int[] rgb = new int[3];
				for (int i = 0; i < 3; i++) {
					rgb[i] = MathUtils.clamp((int)Math.round(rgbFloats[i]), 0, 255);
				}

				// Set pixel value
				result.setPixel(x, y, rgb);

			}
		}

		return result;

	}

}
