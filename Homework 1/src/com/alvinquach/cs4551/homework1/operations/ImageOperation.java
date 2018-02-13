package com.alvinquach.cs4551.homework1.operations;

import com.alvinquach.cs4551.homework1.models.image.ClonableImage;
import com.alvinquach.cs4551.homework1.models.image.Image;

/**
 * @author Alvin Quach
 */
public abstract class ImageOperation {
	
	protected ClonableImage image;
	
	protected String fileSuffix;
	
	protected ImageOperation(ClonableImage image) {
		this.image = image;
	}
	
	abstract public void apply();
	
	public final void applyAndDisplay() {
		apply();
		image.display();
	}
	
	public void save(String name) {
		image.write2PPM(name + "-" + getFileSuffix() + ".ppm");
	}
	
	protected abstract String getFileSuffix();

}
