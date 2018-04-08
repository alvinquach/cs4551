package cs4551.homework3.application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

import cs4551.homework3.models.image.ClonableImage;
import cs4551.homework3.models.image.Image;
import cs4551.homework3.utils.ImageUtils;

/**
 * @author Alvin Quach
 */
public class CS4551_Quach {

	private static Image sourceImage;

	private static String filename;

	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Usage: java " + CS4551_Quach.class.getSimpleName() + " <path to ppm>");
			System.exit(1);
		}

		try {
			
			
			filename = Paths.get(args[0]).getFileName().toString().split("\\.")[0];
			
			// Read image
			sourceImage = new ClonableImage(args[0]);
			
			// Step E1. Resize the input
			Image resizedImage = ImageUtils.padImage(sourceImage, 8);
			resizedImage.display();
			
			// Step D5. Restore the original size
			Image decompressedImage = ImageUtils.cropImage(resizedImage, 0, 0, sourceImage.getW(), sourceImage.getH());
			decompressedImage.display();
			
		}
		catch (FileNotFoundException e) {
			System.err.println("File " + args[0] + "not found.");
			System.exit(1);
		}
		catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		run();

	}

	private static void run() {

	}

}
