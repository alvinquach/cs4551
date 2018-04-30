package homework4.application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.sun.imageio.plugins.common.ImageUtil;

import homework4.application.MenuDisplay.Menu;
import homework4.models.image.ClonableImage;
import homework4.models.image.Image;
import homework4.models.image.motion.BlockSearch;
import homework4.models.image.motion.Macroblocks;
import homework4.models.image.motion.ResidualBlock;
import homework4.models.image.motion.ResidualBlocks;
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
						menuDisplay.displayAliasingSubmenu('n', "macro block size (8, 16, or 24)");
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
						menuDisplay.displayAliasingSubmenu('p', "search window (4, 8, 12, or 16)");
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
						
						ImageUtils.blocksToImage(residualBlocks.getBlocks(), targetImage.getW(), targetImage.getH()).display();
						System.out.println(residualBlocks);
					}

					// Removing Moving Objects
					else if (choice == 2) {

						// Request the filename/path
						menuDisplay.displayFilePathRequest();

						String filePath = "";
						while (sc.hasNextLine()) {
							filePath = sc.nextLine();
							if (!filePath.isEmpty()) {
								break;
							}
						}
						
						Path path = Paths.get(filePath);
						try {

							//	DO SOMETHING
							
						}
						catch (Exception e) {
							e.printStackTrace();
						}
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
