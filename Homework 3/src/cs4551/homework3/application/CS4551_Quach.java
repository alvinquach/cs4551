package cs4551.homework3.application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;

import cs4551.homework3.models.image.ClonableImage;
import cs4551.homework3.models.image.Image;
import cs4551.homework3.models.image.ycbcr.ChromaSubsampling;
import cs4551.homework3.models.image.ycbcr.YCbCrImage;
import cs4551.homework3.models.image.ycbcr.YCbCrImageDCT;
import cs4551.homework3.utils.ImageUtils;
import cs4551.homework3.utils.PrintUtils;

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
			if (displayIntermediateSteps) resizedImage.display("Step E1 Result");
			
			// Step E2. Color conversion and subsampling
			YCbCrImage yCbCrImage = new YCbCrImage(resizedImage, ChromaSubsampling.YCRCB_444);
			System.out.println("E2 DONE");
			
			// Step E3. DCT
			YCbCrImageDCT dctImage = new YCbCrImageDCT(yCbCrImage);
			System.out.println("E3 DONE");
			
			// Step D3. IDCT
			YCbCrImage reconstructedYCbCrImage = dctImage.reconstructYCbCrImage();
			System.out.println("D3 DONE");
			
			// Step D4. Inverse color conversion and subsampling
			Image rgbImage = reconstructedYCbCrImage.toRGBImage();
			if (displayIntermediateSteps) rgbImage.display("Step D4 Result");
			
			// Step D5. Restore the original size
			Image decompressedImage = ImageUtils.cropImage(rgbImage, 0, 0, sourceImage.getW(), sourceImage.getH());
			decompressedImage.display("Step D5 Result");
			
			
		}
		catch (FileNotFoundException e) {
			System.err.println("File " + args[0] + "not found.");
			System.exit(1);
		}
		catch (IOException e) {
			System.err.println(e.getMessage());
			System.exit(1);
		}
		catch (Exception e) {
			e.printStackTrace();
			System.exit(1);
		}

	}

}
