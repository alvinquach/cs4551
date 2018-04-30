package homework4.models.image.motion;

import homework4.models.Coord;
import homework4.models.image.Image;

public class ResidualBlocks {
	
	private ResidualBlock[][] blocks;
	
	private ResidualBlocks(int hCount, int vCount) {
		blocks = new ResidualBlock[hCount][vCount];
	}

	public ResidualBlock[][] getBlocks() {
		return blocks;
	}
	
	public int getBlockSize() {
		return blocks[0][0].getBlockSize();
	}

	public static ResidualBlocks fromComparison(Macroblocks macroblocks, Image reference, int p, BlockSearch searchType) throws Exception {
		Macroblock[][] blocks = macroblocks.getBlocks();
		int hCount = blocks.length;
		int vCount = blocks[0].length;
		int n = macroblocks.getBlockSize();
		ResidualBlocks result = new ResidualBlocks(hCount, vCount);
		for (int i = 0; i < hCount; i++) {
			for (int j = 0; j < vCount; j++) {
				Macroblock macroblock = blocks[i][j];
				result.blocks[i][j] = ResidualBlock.fromComparison(macroblock, reference, new Coord(i * n, j * n), p, searchType);
			}
		}
		return result;
	}
	
}
