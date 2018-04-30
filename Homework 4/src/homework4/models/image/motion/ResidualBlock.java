package homework4.models.image.motion;

import homework4.models.Coord;
import homework4.models.image.Image;
import homework4.utils.ValidationUtils;

public class ResidualBlock extends Block {
	
	private Coord motionVector;
	
	private ResidualBlock(int n) throws Exception {
		super(n);
		motionVector = new Coord();
	}

	public Coord getMotionVector() {
		return motionVector;
	}

	public static ResidualBlock fromComparison(Block block, Image reference, Coord start, int p, BlockSearch searchType) throws Exception {
		if (!ValidationUtils.pIsValid(p)) {
			throw new Exception("Invalid p value of " + p);
		}
		int n = block.getBlockSize();
		ResidualBlock result = new ResidualBlock(n);
		// TODO Implement this.
		return result;
	}
	
}
