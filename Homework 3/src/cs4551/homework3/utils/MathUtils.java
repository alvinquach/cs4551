package cs4551.homework3.utils;

/**
 * @author Alvin Quach
 */
public class MathUtils {
	
	public static int clamp(int value, int min, int max) {
		return value < min ? min : value > max ? max : value;
	}
	
}
