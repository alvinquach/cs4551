package homework4.utils;

/**
 * @author Alvin Quach
 */
public class ValidationUtils {

	public static boolean nIsValid(int n) {
		if (n % 8 == 0 && n > 0 && n <= 24) {
			return true;
		}
		return false;
	}
	
	public static boolean pIsValid(int p) {
		if (p % 4 == 0 && p > 0 && p <= 16) {
			return true;
		}
		return false;
	}
	
}
