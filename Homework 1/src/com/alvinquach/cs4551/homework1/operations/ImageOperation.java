package com.alvinquach.cs4551.homework1.operations;

import com.alvinquach.cs4551.homework1.image.Image;

/**
 * @author Alvin Quach
 */
public abstract class ImageOperation {
	
	protected Image image;
	
	protected String fileSuffix;
	
	protected ImageOperation(Image image) {
		this.image = image;
	}
	
	abstract public void apply();
	
	public final void applyAndDisplay() {
		apply();
		image.display();
	}
	
	public final void save(String name) {
		image.write2PPM(name + "-" + getFileSuffix() + ".ppm");
	}
	
	protected abstract String getFileSuffix();

}
