package homework4.models.image.motion;

import homework4.models.Coord;
import homework4.models.image.Image;
import homework4.utils.ValidationUtils;

public class Macroblocks {
	
	private Macroblock[][] blocks;
	
	private Macroblocks(int hCount, int vCount) {
		blocks = new Macroblock[hCount][vCount];
	}
	
	public Macroblock[][] getBlocks() {
		return blocks;
	}
	
	public int getBlockSize() {
		return blocks[0][0].getBlockSize();
	}
	
	public static Macroblocks fromImage(Image image, int n) throws Exception {
		if (!ValidationUtils.nIsValid(n)) {
			throw new Exception("Invalid n value of " + n);
		}
		int hCount = (int)Math.ceil(image.getW() / (double)n);
		int vCount = (int)Math.ceil(image.getH() / (double)n);
		Macroblocks result = new Macroblocks(hCount, vCount);
		for (int i = 0; i < hCount; i++) {
			for (int j = 0; j < vCount; j++) {
				result.blocks[i][j] = new Macroblock(n, new Coord(i * n, j * n), image);
			}
		}
		return result;
	}

}
