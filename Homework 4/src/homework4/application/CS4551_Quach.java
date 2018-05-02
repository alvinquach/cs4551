package homework4.application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Scanner;

import homework4.application.MenuDisplay.Menu;
import homework4.models.image.ClonableImage;
import homework4.models.image.Image;
import homework4.models.image.motion.BlockSearch;
import homework4.models.image.motion.Macroblocks;
import homework4.models.image.motion.ResidualBlocks;
import homework4.utils.FileUtils;
import homework4.utils.ImageUtils;
import homework4.utils.ValidationUtils;

/**
 * @author Alvin Quach
 */
public class CS4551_Quach {

	private static MenuDisplay menuDisplay = new MenuDisplay();

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
		menuDisplay.displayMenu(Menu.MAIN_MENU);

		// Wait for user input.
		while (true) {
			try {
				int choice = sc.nextInt();
				if (choice < 1 || choice > Menu.MAIN_MENU.itemCount()) {
					menuDisplay.displayInvalidInput();
				}

				// Exit if the last choice was picked.
				else if (choice == Menu.MAIN_MENU.itemCount()) {
					System.out.println("Exiting...");
					System.exit(0);
					break;
				}

				// Valid choice...
				else {

					// Block-Based Motion Compensation
					if (choice == 1) {
						
						String targetImagePath = "", referenceImagePath = "";
						Image targetImage = null, referenceImage = null;
						int n = 0, p = 0;
						
						// Request target image.
						menuDisplay.displayFilePathRequest("target image");
						while (sc.hasNextLine()) {
							targetImagePath = sc.nextLine();
							if (!targetImagePath.isEmpty()) {
								try {
									targetImage = new ClonableImage(targetImagePath);
									break;
								}
								catch (FileNotFoundException e) {
									menuDisplay.displayFileNotFound(targetImagePath, "Enter a valid path to the target image:");
								}
								catch (IOException e) {
									menuDisplay.displayFileCannotBeRead(targetImagePath, "Enter another image file:");
								}
							}
						}
						
						// Request reference image.
						menuDisplay.displayFilePathRequest("reference image");
						while (sc.hasNextLine()) {
							referenceImagePath = sc.nextLine();
							if (!referenceImagePath.isEmpty()) {
								try {
									referenceImage = new ClonableImage(referenceImagePath);
									break;
								}
								catch (FileNotFoundException e) {
									menuDisplay.displayFileNotFound(referenceImagePath, "Enter a valid path to the reference image:");
								}
								catch (IOException e) {
									menuDisplay.displayFileCannotBeRead(referenceImagePath, "Enter another image file:");
								}
							}
						}
						
						// Request n
						menuDisplay.displayMacroBlockSubmenu('n', "macro block size (8, 16, or 24)");
						while (true) {
							try {
								n = sc.nextInt();
								if (!ValidationUtils.nIsValid(n)) {
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
						
						// Request p
						menuDisplay.displayMacroBlockSubmenu('p', "search window (4, 8, 12, or 16)");
						while (true) {
							try {
								p = sc.nextInt();
								if (!ValidationUtils.pIsValid(p)) {
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

						// Divide the target image into a set of nxn macro blocks
						Macroblocks macroblocks = Macroblocks.fromImage(targetImage, n);
						
						// Calculate motion vectors and error blocks
						ResidualBlocks residualBlocks = ResidualBlocks.fromComparison(macroblocks, referenceImage, p, BlockSearch.LINEAR);
						
						// Scale error values to range [0, 255]
						residualBlocks.normalize();
						
						// Display and write residual image.
						Image residualImage = ImageUtils.blocksToImage(residualBlocks.getBlocks(), targetImage.getW(), targetImage.getH());
						residualImage.display("Residual Image");
						residualImage.write2PPM(FileUtils.generateNewFilePath(targetImagePath, "error_", null, null));
						
						// Write motion vectors to file.
						String basePath = FileUtils.extractPath(targetImagePath);
						String motionVectors = residualBlocks.toString();
						System.out.println(motionVectors);
						StringBuilder sb = new StringBuilder()
							.append("# Name: Alvin Quach").append("\n")
							.append("# Target image name: ").append(FileUtils.extractFilename(targetImagePath)).append("\n")
							.append("# Reference  image name: ").append(FileUtils.extractFilename(referenceImagePath)).append("\n")
							.append("# Number of target macro blocks: ")
							.append(residualBlocks.getHCount() + " x " + residualBlocks.getVCount())
							.append(" (image size is " + targetImage.getW() + " x " + targetImage.getH() + ")").append("\n")
							.append("\n")
							.append(motionVectors);
						FileUtils.writeToFile(sb.toString(), basePath + "mv.txt");
						
						System.out.println("Residual image and motion vectors were written to directory " + basePath);
						
					}

					// Removing Moving Objects
					else if (choice == 2) {

						// Request the filename/path
						menuDisplay.displayDirectoryPathRequest("IDB files");

						String idbDirectory = "";
						while (sc.hasNextLine()) {
							idbDirectory = sc.nextLine();
							if (!idbDirectory.isEmpty()) {
								break;
							}
						}
						
						// Request sequence number
						int sequence = 0;
						menuDisplay.displayImageSequenceNumber(19, 179);
						while (true) {
							try {
								sequence = sc.nextInt();
								if (!ValidationUtils.frameIsValid(sequence)) {
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
						
						String targetImagePath = FileUtils.join(idbDirectory, FileUtils.idbFilename(sequence));
						String referenceImagePath = FileUtils.join(idbDirectory, FileUtils.idbFilename(sequence - 2));
						Image targetImage = null, referenceImage = null;
						
						try {
							targetImage = new ClonableImage(targetImagePath); 
						}
						catch (FileNotFoundException e) {
							menuDisplay.displayFileNotFound(targetImagePath, "Exiting...");
							System.exit(1);
						}
						catch (IOException e) {
							menuDisplay.displayFileCannotBeRead(targetImagePath, "Exiting...");
							System.exit(1);
						}

						try {
							referenceImage = new ClonableImage(referenceImagePath);
						}
						catch (FileNotFoundException e) {
							menuDisplay.displayFileNotFound(referenceImagePath, "Exiting...");
							System.exit(1);
						}
						catch (IOException e) {
							menuDisplay.displayFileCannotBeRead(targetImagePath, "Exiting...");
							System.exit(1);
						}
						
						// Divide the target image into a set of nxn macro blocks
						Macroblocks macroblocks = Macroblocks.fromImage(targetImage, 8);
						
						// Calculate motion vectors and error blocks
						ResidualBlocks residualBlocks = ResidualBlocks.fromComparison(macroblocks, referenceImage, 8, BlockSearch.LINEAR);
						
						// Display and write colorized residual image.
						residualBlocks.normalize();
						residualBlocks.colorizeDynamicBlocks();
						Image residualImage = ImageUtils.blocksToImage(residualBlocks.getBlocks(), targetImage.getW(), targetImage.getH());
						residualImage.display("Dynamic Blocks");
						residualImage.write2PPM(FileUtils.generateNewFilePath(targetImagePath, null, "_dynamic_blocks", null));
						
					}

					// Display main menu again.
					menuDisplay.displayMenu(Menu.MAIN_MENU);
				}
			}
			catch (InputMismatchException e) {
				menuDisplay.displayInvalidInput();
				sc.nextLine();
			}
			catch (Exception e) {
				e.printStackTrace();
				System.exit(1);
			}
		}
		sc.close();
	}
	
}
