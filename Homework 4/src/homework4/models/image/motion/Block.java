package homework4.models.image.motion;

import homework4.models.image.Pixel;
import homework4.utils.ValidationUtils;

public abstract class Block {

	protected Pixel[][] pixels;
	
	private int n;
	
	public Block(int n) throws Exception {
		if (!ValidationUtils.nIsValid(n)) {
			throw new Exception("Invalid n value of " + n);
		}
		pixels = new Pixel[n][n];
		this.n = n;
	}

	public Pixel[][] getPixels() {
		return pixels;
	}
	
	public final int getBlockSize() {
		return n;
	}
	
}
