package cs4551.homework3.application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;

import cs4551.homework3.models.encode.rle.RunLengthEncode;
import cs4551.homework3.models.image.ImageConstants;
import cs4551.homework3.models.image.rgb.ClonableImage;
import cs4551.homework3.models.image.rgb.Image;
import cs4551.homework3.models.image.ycbcr.ChromaSubsampling;
import cs4551.homework3.models.image.ycbcr.YCbCrDCT;
import cs4551.homework3.models.image.ycbcr.YCbCrImage;
import cs4551.homework3.models.image.ycbcr.YCbCrQuantized;
import cs4551.homework3.utils.FileUtils;
import cs4551.homework3.utils.ImageUtils;
import cs4551.homework3.utils.PrintUtils;

/**
 * @author Alvin Quach
 */
public class CS4551_Quach {

	private static boolean displayIntermediateSteps = true;

	private static boolean runTimeDebug = true;

	private static Image sourceImage;

	private static String filename;

	private static Path outputFilePath;

	public static void main(String[] args) {

		if (args.length != 1) {
			System.err.println("Usage: java " + CS4551_Quach.class.getSimpleName() + " <path to ppm>");
			System.exit(1);
		}

		try {

			filename = Paths.get(args[0]).getFileName().toString().split("\\.")[0];

			outputFilePath = Paths.get(filename + ".dat");

			// Read image
			sourceImage = new ClonableImage(args[0]);

			// Ask user for compression level.
			int n = 0;
			Scanner sc = new Scanner(System.in);
			System.out.println("\nPlease enter a compression level (0-" + ImageConstants.MAX_COMPRESSION_LEVEL + "): ");
			while (true) {
				try {
					n = sc.nextInt();
					if (n < 0 || n > ImageConstants.MAX_COMPRESSION_LEVEL) {
						System.out.println("Invalid input.");
						continue;
					}
					break;
				}
				catch (InputMismatchException e) {
					System.out.println("Invalid input.");
					sc.nextLine();
				}
			}

			sc.close();

			Long start = System.nanoTime();


			// Step E1. Resize the input
			Image resizedImage = ImageUtils.padImage(sourceImage, ImageConstants.JPEG_BLOCK_SIZE);
			if (displayIntermediateSteps) resizedImage.display("Step E1 Result");
			if (runTimeDebug) {
				System.out.println("E1 finished in " + runTimeAsString(start));
				start = System.nanoTime();
			}


			// Step E2. Color conversion and subsampling
			YCbCrImage yCbCrImage = new YCbCrImage(resizedImage, ChromaSubsampling.YCRCB_420);
			if (runTimeDebug) {
				System.out.println("E2 finished in " + runTimeAsString(start));
				start = System.nanoTime();
			}


			// Step E3. DCT
			YCbCrDCT dctImage = new YCbCrDCT(yCbCrImage);
			if (runTimeDebug) {
				System.out.println("E3 finished in " + runTimeAsString(start));
				start = System.nanoTime();
			}


			// Step E4. Quantization
			YCbCrQuantized quantizedImage = new YCbCrQuantized(dctImage, n);
			if (runTimeDebug) {
				System.out.println("E4 finished in " + runTimeAsString(start));
				start = System.nanoTime();
			}


			// Step E5. Intermediate representation
			RunLengthEncode lumaRunLengthEncoded = RunLengthEncode.fromQuantizedBlocks(quantizedImage.getQuantizedLumaBlocks());
			RunLengthEncode cbRunLengthEncoded = RunLengthEncode.fromQuantizedBlocks(quantizedImage.getQuantizedCbBlocks());
			RunLengthEncode crRunLengthEncoded = RunLengthEncode.fromQuantizedBlocks(quantizedImage.getQuantizedCrBlocks());
			int lumaSize = RunLengthEncode.calculateImageEncodedSize(lumaRunLengthEncoded, 10 - n, 6);
			int cbSize = RunLengthEncode.calculateImageEncodedSize(cbRunLengthEncoded, 9 - n, 6);
			int crSize = RunLengthEncode.calculateImageEncodedSize(crRunLengthEncoded, 9 - n, 6);
			PrintUtils.printCompressionRatio(n, sourceImage.getW(), sourceImage.getH(), lumaSize, cbSize, crSize); // Print to console.
			if (runTimeDebug) {
				System.out.println("E5 finished in " + runTimeAsString(start));
				start = System.nanoTime();
			}


			// Step EC. Bit packing
			List<Boolean[]> bits = new ArrayList<>();
			bits.add(FileUtils.encodeMetadata(sourceImage, yCbCrImage.getSubsampling(), n));
			bits.add(lumaRunLengthEncoded.toBitStream(10 - n, 6));
			bits.add(cbRunLengthEncoded.toBitStream(9 - n, 6));
			bits.add(crRunLengthEncoded.toBitStream(9 - n, 6));
			byte[] dataBytes = FileUtils.bitsToBytes(bits.stream().flatMap(b -> Arrays.stream(b)).toArray(Boolean[]::new));
			FileUtils.writeBinaryFile(dataBytes, outputFilePath, false);
			System.out.println("Compressed image data was written to " + outputFilePath.toAbsolutePath() + "\n");
			if (runTimeDebug) {
				System.out.println("EC finished in " + runTimeAsString(start));
				start = System.nanoTime();
			}


			// Step D1. Bit unpacking
			boolean[] fileBits = FileUtils.bytesToBits(FileUtils.readFileAsBytes(outputFilePath));
			YCbCrQuantized unpackedQuantizedImage = new YCbCrQuantized(fileBits);
			if (runTimeDebug) {
				System.out.println("D1 finished in " + runTimeAsString(start));
				start = System.nanoTime();
			}


			// Step D2. De-quantization
			YCbCrDCT dequantizedImage = unpackedQuantizedImage.dequantize();
			if (runTimeDebug) {
				System.out.println("D2 finished in " + runTimeAsString(start));
				start = System.nanoTime();
			}


			// Step D3. IDCT
			YCbCrImage reconstructedYCbCrImage = dequantizedImage.reconstructYCbCrImage();
			if (runTimeDebug) {
				System.out.println("D3 finished in " + runTimeAsString(start));
				start = System.nanoTime();
			}


			// Step D4. Inverse color conversion and subsampling
			Image rgbImage = reconstructedYCbCrImage.toRGBImage();
			if (displayIntermediateSteps) rgbImage.display("Step D4 Result");
			if (runTimeDebug) {
				System.out.println("D4 finished in " + runTimeAsString(start));
				start = System.nanoTime();
			}


			// Step D5. Restore the original size
			Image decompressedImage = ImageUtils.cropImage(rgbImage, 0, 0, unpackedQuantizedImage.getWidth(), unpackedQuantizedImage.getHeight());
			decompressedImage.display("Step D5 Result");
			if (runTimeDebug) {
				System.out.println("D5 finished in " + runTimeAsString(start));
				start = System.nanoTime();
			}

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

	private static String runTimeAsString(Long start) {
		return ((System.nanoTime() - start) / 1000000f) + " ms";
	}

}
