package cs4551.homework3.models.image.dct;

public class DCTBlocks {
	
	public static final int BLOCK_SIZE = DCTBlock.BLOCK_SIZE;
	
	private DCTBlock[] dctBlocks;
	
	private int hBlockCount;
	
	private int vBlockCount;
	
	public DCTBlocks(float[][] component) throws Exception {
		
		if (component.length % BLOCK_SIZE > 0 || component[0].length % BLOCK_SIZE > 0) {
			throw new Exception("Image component dimensions must be multiples of " + BLOCK_SIZE + "!");
		}
		
		hBlockCount = component.length / BLOCK_SIZE;
		vBlockCount = component[0].length / BLOCK_SIZE;
		
		dctBlocks = new DCTBlock[hBlockCount * vBlockCount];
		
		// Temporary array to store subsamples of the image component.
		float[][] subsample = new float[BLOCK_SIZE][BLOCK_SIZE];

		// Break image component into blocks.
		// Blocks are scanned in raster order - left to right, to to bottom.
		int blockIndex = 0;
		for (int y = 0; y < vBlockCount; y++) {
			for (int x = 0; x < hBlockCount; x++) {
				for (int _x = 0; _x < BLOCK_SIZE; _x++) {
					for (int _y = 0; _y < BLOCK_SIZE; _y++) {
						subsample[_x][_y] = component[_x + x * BLOCK_SIZE][_y + y * BLOCK_SIZE];
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
