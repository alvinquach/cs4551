package homework4.models.image;

import homework4.utils.MathUtils;

public class Pixel {
	
	private int r;
	private int g;
	private int b;
	
	public Pixel(int intensity) {
		setIntensity(intensity);
	}
	
	public Pixel(int r, int g, int b) {
		setR(r);
		setG(g);
		setB(b);
	}

	public int getR() {
		return r;
	}

	public void setR(int r) {
		this.r = MathUtils.clamp(r, 0, 255);
	}

	public int getG() {
		return g;
	}

	public void setG(int g) {
		this.g = MathUtils.clamp(g, 0, 255);
	}

	public int getB() {
		return b;
	}

	public void setB(int b) {
		this.b = MathUtils.clamp(b, 0, 255);
	}
	
	public void setIntensity(int intensity) {
		intensity = MathUtils.clamp(intensity, 0, 255);
		r = intensity;
		g = intensity;
		b = intensity;
	}
	
	public int[] toArray() {
		return new int[] {r, g, b};
	}
	
	public int[] toArray(int[] rgb) {
		rgb[0] = r;
		rgb[1] = g;
		rgb[2] = b;
		return rgb;
	}
	
	public static Pixel fromArray(int[] rgb) {
		return new Pixel(rgb[0], rgb[1], rgb[2]);
	}

}
