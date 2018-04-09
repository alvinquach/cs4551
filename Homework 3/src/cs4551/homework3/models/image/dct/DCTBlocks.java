package cs4551.homework3.models.image.dct;

import cs4551.homework3.models.image.ImageConstants;

public class DCTBlocks {
	
	private static final int BLOCK_SIZE = ImageConstants.JPEG_BLOCK_SIZE;
	
	private DCTBlock[] dctBlocks;
	
	private int hBlockCount;
	
	private int vBlockCount;
	
	public DCTBlocks(DCTBlock[] dctBlocks, int hBlockCount, int vBlockCount) {
		this.dctBlocks = dctBlocks;
		this.hBlockCount = hBlockCount;
		this.vBlockCount = vBlockCount;
	}

	/** Breaks an image component into multiple DCT blocks. */
	public DCTBlocks(float[][] component) throws Exception {
			
		hBlockCount = (int)Math.ceil(component.length / (double)BLOCK_SIZE);
		vBlockCount = (int)Math.ceil(component[0].length / (double)BLOCK_SIZE);
		
		int width = component.length;
		int height = component[0].length;
		
		dctBlocks = new DCTBlock[hBlockCount * vBlockCount];
		
		// Temporary array to store subsamples of the image component.
		float[][] subsample = new float[BLOCK_SIZE][BLOCK_SIZE];

		// Break image component into blocks.
		// Blocks are scanned in raster order - left to right, to to bottom.
		int blockIndex = 0;
		for (int y = 0; y < height; y += BLOCK_SIZE) {
			for (int x = 0; x < width; x += BLOCK_SIZE) {
				for (int _x = 0; _x < BLOCK_SIZE; _x++) {
					for (int _y = 0; _y < BLOCK_SIZE; _y++) {
						try {
							subsample[_x][_y] = component[_x + x][_y + y];
						}
						catch (ArrayIndexOutOfBoundsException e) {
	
							// Use zero if out of bounds.
							// This gives the same effect as padding with zeros.
							subsample[_x][_y] = 0;
						}
					}
				}
				dctBlocks[blockIndex++] = new DCTBlock(subsample);
			}
		}
		
	}
	
	public float[][] reconstructComponent() {
		
		float[][] result = new float[hBlockCount * BLOCK_SIZE][vBlockCount * BLOCK_SIZE];
		
		// Go through each DCT block and compute the IDCT.
		int blockIndex = 0;
		for (int y = 0; y < vBlockCount; y++) {
			for (int x = 0; x < hBlockCount; x++) {
				float[][] iDct = dctBlocks[blockIndex++].computeIDCT();
				for (int _x = 0; _x < BLOCK_SIZE; _x++) {
					for (int _y = 0; _y < BLOCK_SIZE; _y++) {
						 result[_x + x * BLOCK_SIZE][_y + y * BLOCK_SIZE] = iDct[_x][_y];
					}
				}
			}
		}
		return result;
	}

	public DCTBlock[] getDctBlocks() {
		return dctBlocks;
	}

	public int gethBlockCount() {
		return hBlockCount;
	}

	public int getvBlockCount() {
		return vBlockCount;
	}
	
}
