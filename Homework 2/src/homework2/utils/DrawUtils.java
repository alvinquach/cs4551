package homework2.utils;

import homework2.models.image.Image;

/**
 * @author Alvin Quach
 */
public class DrawUtils {
	
	/**
	 * Replaces the contents of the provided {@code Image} with circles of specified M and N.
	 * @param M Thickness of the circles.
	 * @param N Difference in successive radii.
	 * @param image The {@code Image} that will be updated.
	 * @return The image with its contents replaced by circles.
	 */ 
	public static Image DrawCircles(int M, int N, Image image) {
		
		// Fill image with white.
		for (int y = 0; y < image.getH(); y++) {
			for (int x = 0; x < image.getW(); x++) {
				image.setPixel(x, y, new int[] {255, 255, 255});
			}
		}
		
		// Find the max radius (including thickness) of a circle.
		int maxDistance = Math.min(image.getW(), image.getH()) / 2; 
		
		// Center of the image/circle.
		int centerX = image.getH() / 2;
		int centerY = image.getW() / 2;
		
		int circleCount = 0;
		while (true) {
			int inner = (circleCount + 1) * N;
			int outer = inner + M;
			
			// Don't draw circles that clip or are fully outside of the image.
			if (outer > maxDistance) {
				break;
			}

			for (int r = inner; r < outer; r++) {
				
				// Step size is inverse of circumference.
				double stepSize = 1 / (2 * Math.PI * r);
				
				for (double theta = 0; theta < Math.PI / 2; theta += stepSize) {
					int x = (int)Math.round(r * Math.cos(theta));
					int y = (int)Math.round(r * Math.sin(theta));
					
					// Set pixels for all four quadrants.
					int[] rgb = new int[] {0, 0, 0};
					image.setPixel(x + centerX, centerY - y, rgb); // Quadrant 1
					image.setPixel(centerX - x, centerY - y, rgb); // Quadrant 2
					image.setPixel(centerX - x, y + centerY, rgb); // Quadrant 3
					image.setPixel(x + centerX, y + centerY, rgb); // Quadrant 4
				}
			}
			
			circleCount++;
		}
		
		return image;
	}

}
