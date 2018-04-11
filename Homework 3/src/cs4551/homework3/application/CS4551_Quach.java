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


			// Step E1. Resize the input
			Image resizedImage = ImageUtils.padImage(sourceImage, ImageConstants.JPEG_BLOCK_SIZE);


			// Step E2. Color conversion and subsampling
			YCbCrImage yCbCrImage = new YCbCrImage(resizedImage, ChromaSubsampling.YCRCB_420);


			// Step E3. DCT
			YCbCrDCT dctImage = new YCbCrDCT(yCbCrImage);


			// Step E4. Quantization
			YCbCrQuantized quantizedImage = new YCbCrQuantized(dctImage, n);


			// Step E5. Intermediate representation
			RunLengthEncode lumaRunLengthEncoded = RunLengthEncode.fromQuantizedBlocks(quantizedImage.getQuantizedLumaBlocks());
			RunLengthEncode cbRunLengthEncoded = RunLengthEncode.fromQuantizedBlocks(quantizedImage.getQuantizedCbBlocks());
			RunLengthEncode crRunLengthEncoded = RunLengthEncode.fromQuantizedBlocks(quantizedImage.getQuantizedCrBlocks());
			int lumaSize = RunLengthEncode.calculateImageEncodedSize(lumaRunLengthEncoded, 10 - n, 6);
			int cbSize = RunLengthEncode.calculateImageEncodedSize(cbRunLengthEncoded, 9 - n, 6);
			int crSize = RunLengthEncode.calculateImageEncodedSize(crRunLengthEncoded, 9 - n, 6);
			PrintUtils.printCompressionRatio(n, sourceImage.getW(), sourceImage.getH(), lumaSize, cbSize, crSize); // Print to console.


			// Step EC. Bit packing
			List<Boolean[]> bits = new ArrayList<>();
			bits.add(FileUtils.encodeMetadata(sourceImage, yCbCrImage.getSubsampling(), n));
			bits.add(lumaRunLengthEncoded.toBitStream(10 - n, 6));
			bits.add(cbRunLengthEncoded.toBitStream(9 - n, 6));
			bits.add(crRunLengthEncoded.toBitStream(9 - n, 6));
			byte[] dataBytes = FileUtils.bitsToBytes(bits.stream().flatMap(b -> Arrays.stream(b)).toArray(Boolean[]::new));
			FileUtils.writeBinaryFile(dataBytes, outputFilePath, false);


			// Step D1. Bit unpacking
			boolean[] fileBits = FileUtils.bytesToBits(FileUtils.readFileAsBytes(outputFilePath));
			YCbCrQuantized unpackedQuantizedImage = new YCbCrQuantized(fileBits);


			// Step D2. De-quantization
			YCbCrDCT dequantizedImage = unpackedQuantizedImage.dequantize();


			// Step D3. IDCT
			YCbCrImage reconstructedYCbCrImage = dequantizedImage.reconstructYCbCrImage();


			// Step D4. Inverse color conversion and subsampling
			Image rgbImage = reconstructedYCbCrImage.toRGBImage();


			// Step D5. Restore the original size
			Image decompressedImage = ImageUtils.cropImage(rgbImage, 0, 0, unpackedQuantizedImage.getWidth(), unpackedQuantizedImage.getHeight());
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
