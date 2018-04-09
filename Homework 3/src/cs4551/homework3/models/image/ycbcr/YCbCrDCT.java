package cs4551.homework3.models.image.ycbcr;

import cs4551.homework3.models.image.dct.DCTBlocks;

/** Contains DCT values of Y, Cb, and Cr components. */
public class YCbCrDCT {
	
	private DCTBlocks luma;
	
	private DCTBlocks cb;
	
	private DCTBlocks cr;
	
	private ChromaSubsampling subsampling;
	
	public YCbCrDCT(YCbCrImage image) throws Exception {
		luma = new DCTBlocks(image.getLuma());
		cb = new DCTBlocks(image.getCb());
		cr = new DCTBlocks(image.getCr());
		subsampling = image.getSubsampling();
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
