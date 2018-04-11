package cs4551.homework3.models.image.ycbcr;

import cs4551.homework3.models.image.ImageConstants;
import cs4551.homework3.models.image.dct.DCTBlock;
import cs4551.homework3.models.image.dct.DCTBlocks;
import cs4551.homework3.utils.ImageUtils;

/** Contains DCT values of Y, Cb, and Cr components. */
public class YCbCrDCT {
	
	private int width;
	
	private int height;
	
	private DCTBlocks luma;
	
	private DCTBlocks cb;
	
	private DCTBlocks cr;
	
	private ChromaSubsampling subsampling;
	
	public YCbCrDCT(YCbCrImage image) throws Exception {
		width = image.getLuma().length;
		height = image.getLuma()[0].length;
		luma = new DCTBlocks(image.getLuma());
		cb = new DCTBlocks(image.getCb());
		cr = new DCTBlocks(image.getCr());
		subsampling = image.getSubsampling();
	}
	
	protected YCbCrDCT(DCTBlock[] luma, DCTBlock[] cb, DCTBlock[] cr, ChromaSubsampling subsampling, int width, int height) {
		
		// TODO Add error checking.
		this.width = width;
		this.height = height;
		
		int lumaHBlockCount = width / ImageConstants.JPEG_BLOCK_SIZE;
		int lumaVBlockCount = height / ImageConstants.JPEG_BLOCK_SIZE;
		int chromaHBlockCount = (int)Math.ceil(ImageUtils.getChromaWidth(width, subsampling) / (double)ImageConstants.JPEG_BLOCK_SIZE);
		int chromaVBlockCount = (int)Math.ceil(ImageUtils.getChromaHeight(height, subsampling) / (double)ImageConstants.JPEG_BLOCK_SIZE);
		
		this.luma = new DCTBlocks(luma, lumaHBlockCount, lumaVBlockCount);
		this.cb = new DCTBlocks(cb, chromaHBlockCount, chromaVBlockCount);
		this.cr = new DCTBlocks(cr, chromaHBlockCount, chromaVBlockCount);
		this.subsampling = subsampling;
		
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public DCTBlocks getLuma() {
		return luma;
	}

	public DCTBlocks getCb() {
		return cb;
	}

	public DCTBlocks getCr() {
		return cr;
	}

	public ChromaSubsampling getSubsampling() {
		return subsampling;
	}

	public YCbCrImage reconstructYCbCrImage() throws Exception {

		// Reconstruct luma from DCT values and copy to result array.
		float[][] luma = this.luma.reconstructComponent();
		YCbCrImage result = new YCbCrImage(luma.length, luma[0].length, subsampling);
		for (int i = 0; i < luma.length; i++) {
			System.arraycopy(luma[i], 0, result.getLuma()[i], 0, luma[i].length);
		}
		
		// Reconstruct chroma components from DCT values and copy to result array.
		float[][] cb = this.cb.reconstructComponent();
		float[][] cr = this.cr.reconstructComponent();
		for (int i = 0; i < cb.length; i++) {
			System.arraycopy(cb[i], 0, result.getCb()[i], 0, result.getCb()[i].length);
			System.arraycopy(cr[i], 0, result.getCr()[i], 0, result.getCr()[i].length);
		}

		return result;
	}
	
}
