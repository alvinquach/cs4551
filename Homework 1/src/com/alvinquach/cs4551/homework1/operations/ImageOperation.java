package com.alvinquach.cs4551.homework1.operations;

import com.alvinquach.cs4551.homework1.image.Image;

/**
 * @author Alvin Quach
 */
public abstract class ImageOperation {
	
	abstract public void apply(Image image);
	
	public void applyAndDisplay(Image image) {
		apply(image);
		image.display();
	}

}
