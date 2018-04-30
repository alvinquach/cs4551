package homework4.utils;

/**
 * @author Alvin Quach
 */
public class MathUtils {
	
	public static int clamp(int value, int min, int max) {
		return value < min ? min : value > max ? max : value;
	}
	
	public static int normalize(int value, int originalMin, int originalMax, int newMin, int newMax ) {
		float percentage = (float)(value - originalMin) / (originalMax - originalMin);
		return Math.round(percentage * (newMax - newMin) + newMin);
	}
	
}
