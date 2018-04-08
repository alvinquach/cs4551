package cs4551.homework3.application;

import java.io.FileNotFoundException;
import java.nio.file.Paths;

import cs4551.homework3.models.image.ClonableImage;
import cs4551.homework3.models.image.Image;
import cs4551.homework3.models.image.ycbcr.ChromaSubsampling;
import cs4551.homework3.models.image.ycbcr.YCbCrImage;
import cs4551.homework3.utils.ImageUtils;

/**
 * @author Alvin Quach
 */
public class CS4551_Quach {
	
	private static boolean displayIntermediateSteps = true;

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
			resizedImage.display("Step E1 Result");
			
			// Step E2. Color conversion and subsampling
			YCbCrImage yCbCrImage = new YCbCrImage(resizedImage, ChromaSubsampling.YCRCB_444);
			
			// Step D4. Inverse color conversion and subsampling
			Image rgbImage = yCbCrImage.toRGBImage();
			if (displayIntermediateSteps) rgbImage.display("Step D4 Result");
			
			// Step D5. Restore the original size
			Image decompressedImage = ImageUtils.cropImage(rgbImage, 0, 0, sourceImage.getW(), sourceImage.getH());
			decompressedImage.display("Step D5 Result");
			
			
		}
		catch (FileNotFoundException e) {
			System.err.println("File " + args[0] + "not found.");
			System.exit(1);
		}
		catch (Exception e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}

		run();

	}

	private static void run() {

	}

}
