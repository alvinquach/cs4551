package homework4.models.image.motion;

import homework4.models.Coord;
import homework4.models.image.Image;
import homework4.models.image.Pixel;

public class Macroblock extends Block {

	public Macroblock(int n) throws Exception {
		super(n);
	}

	public Macroblock(int n, Coord start, Image image) throws Exception {
		this(n);
		int[] rgb = new int[3];
		for (int y = 0; y < n; y++) {
			for (int x = 0; x < n; x++) {
				try {
					image.getPixel(x + start.x, y + start.y, rgb);
				}
				catch (ArrayIndexOutOfBoundsException e) {
					// This gives the same effect as padding with zeros.
					pixels[x][y] = new Pixel(0, 0, 0);
					continue;
				}
				pixels[x][y] = Pixel.fromArray(rgb);
			}
		}
	}

	@Override
	public Macroblock clone() {
		Macroblock result = null;
		try {
			result = new Macroblock(n);
		}
		catch (Exception e) {
			// Not possible to occur.
		}
		for (int x = 0; x < n; x++) {
			for (int y = 0; y < n; y++) {
				result.pixels[x][y] = pixels[x][y].clone();
			}
		}
		return result;
	}
	
}
