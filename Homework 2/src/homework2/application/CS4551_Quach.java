package homework2.application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Scanner;

import homework2.compression.lzw.LzwEncoder;
import homework2.models.image.ClonableImage;
import homework2.models.image.Image;
import homework2.resampler.Convolution3x3Downsampler;
import homework2.resampler.NearestNeighborDownsampler;
import homework2.utils.FileUtils;
import homework2.utils.ImageUtils;

/**
 * @author Alvin Quach
 */
public class CS4551_Quach {

	private static MenuDisplay menuDisplay = new MenuDisplay();

	//	private static ClonableImage image;
	//
	//	private static String filename;

	public static void main(String[] args) {

		// Not really needed...
		if (args.length != 0) {
			menuDisplay.displayApplicationUsage(CS4551_Quach.class.getSimpleName());
			System.exit(1);
		}

		run();

	}

	private static void run() {
		Scanner sc = new Scanner(System.in);
		menuDisplay.displayMainMenu();

		// Wait for user input.
		while (true) {
			try {
				int choice = sc.nextInt();
				if (choice < 1 || choice > menuDisplay.getMainMenuChoiceCount()) {
					menuDisplay.displayInvalidInput();
				}

				// Exit if the last choice was picked.
				else if (choice == menuDisplay.getMainMenuChoiceCount()) {
					System.out.println("Exiting...");
					System.exit(0);
					break;
				}

				// Valid choice...
				else {

					// Aliasing
					if (choice == 1) {
						int N = 0, M = 0, K = 0;

						// Request N
						menuDisplay.displayAliasingSubmenu('N', "difference in successive radii");
						while (true) {
							try {
								N = sc.nextInt();
								// TODO Add sanity checks.
								break;
							}
							catch (InputMismatchException e) {
								menuDisplay.displayInvalidInput();
								sc.nextLine();
							}
						}

						// Request M
						menuDisplay.displayAliasingSubmenu('M', "thickness");
						while (true) {
							try {
								M = sc.nextInt();
								// TODO Add sanity checks.
								break;
							}
							catch (InputMismatchException e) {
								menuDisplay.displayInvalidInput();
								sc.nextLine();
							}
						}

						// Request K
						menuDisplay.displayAliasingSubmenu('K', "subsampling factor, power of 2, and 2 <= K <= 16");
						while (true) {
							try {
								K = sc.nextInt();
								if (K < 2 || K > 16 || K != Integer.highestOneBit(K)) {
									menuDisplay.displayInvalidInput();
								}
								else {
									break;
								}
							}
							catch (InputMismatchException e) {
								menuDisplay.displayInvalidInput();
								sc.nextLine();
							}
						}

						int dim = (int)Math.pow(2, AppConstants.CIRCLE_IMG_DIM);
						Image image = ImageUtils.DrawCircles(M, N, new ClonableImage(dim, dim));
						image.display();
						image.write2PPM("Circle_" + M + "_" + N + ".ppm");

						// Nearest neighbor
						Image ds1 = new NearestNeighborDownsampler().resample(image, K);
						ds1.display();
						ds1.write2PPM("Circle_" + M + "_" + N + "_K" + K + "_NoFilter.ppm");

						// Box blur
						Image ds2 = new Convolution3x3Downsampler(1, 1, 1, 1, 1, 1, 1, 1, 1).resample(image, K);
						ds2.display();
						ds2.write2PPM("Circle_" + M + "_" + N + "_K" + K + "_BoxFilter.ppm");

						// Gaussian blur
						Image ds3 = new Convolution3x3Downsampler(1, 2, 1, 2, 4, 2, 1, 2, 1).resample(image, K);
						ds3.display();
						ds3.write2PPM("Circle_" + M + "_" + N + "_K" + K + "_GaussianFilter.ppm");

					}

					// Dictionary Coding
					else if (choice == 2) {

						// Request the filename/path
						menuDisplay.displayFilePathRequest();

						String filename = "";
						while (sc.hasNextLine()) {
							filename = sc.nextLine();
							if (!filename.isEmpty()) {
								break;
							}
						}
						
						Path path = Paths.get(filename);
						try {
							String fileContents = FileUtils.readPlainTextFile(path);
							System.out.println(fileContents);
							LzwEncoder encoder = new LzwEncoder(fileContents);
							encoder.printDictionary();
							encoder.printEncodedBytes();
						}
						catch (FileNotFoundException e) {
							menuDisplay.displayFileNotFound(filename, "Returning to main menu.");
						}
						catch (IOException e) {
							e.printStackTrace();
						}
					}

					// Display main menu again.
					menuDisplay.displayMainMenu();
				}
			}
			catch (InputMismatchException e) {
				menuDisplay.displayInvalidInput();
				sc.nextLine();
			}
		}
		sc.close();
	}

}
