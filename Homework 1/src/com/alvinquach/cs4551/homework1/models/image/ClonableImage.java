package com.alvinquach.cs4551.homework1.models.image;

import java.io.IOException;

/**
 * Implementation of the {@code Image} class that provides cloning capabilities.
 * @author Alvin Quach
 */
public class ClonableImage extends Image implements Cloneable {

	public ClonableImage(int w, int h) {
		super(w, h);
	}

	public ClonableImage(String fn) throws IOException {
		super(fn);
	}

	/** Set pixel color by sRGB value. */
	public void setPixel(int x, int y, int rgb) {
		img.setRGB(x, y, rgb);
	}

	/** Returns a clone of this Image. */
	public ClonableImage clone() {
		ClonableImage newImage = new ClonableImage(img.getWidth(), img.getHeight());
		for (int y = 0; y < img.getHeight(); y++) {
			for (int x = 0; x < img.getWidth(); x++) {
				newImage.setPixel(x, y, img.getRGB(x, y));
			}
		}
		return newImage;
	}

}
