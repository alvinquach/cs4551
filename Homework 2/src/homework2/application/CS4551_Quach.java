package homework2.application;

import java.util.InputMismatchException;
import java.util.Scanner;

import homework2.models.image.ClonableImage;
import homework2.utils.DrawUtils;

/**
 * @author Alvin Quach
 */
public class CS4551_Quach {
	
	private static MenuDisplay menuDisplay = new MenuDisplay();

//	private static ClonableImage image;
//
//	private static String filename;

	public static void main(String[] args) {

//		if (args.length != 1) {
//			menuDisplay.displayApplicationUsage(CS4551_Quach.class.getSimpleName());
//			System.exit(1);
//		}
//
//		try {
//			filename = Paths.get(args[0]).getFileName().toString().split("\\.")[0];
//			image = new ClonableImage(args[0]);
//		}
//		catch (FileNotFoundException e) {
//			menuDisplay.displayFileNotFound(args[0]);
//			System.exit(1);
//		}
//		catch (IOException e) {
//			System.err.println(e.getMessage());
//			System.exit(1);
//		}

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
						ClonableImage image = (ClonableImage)DrawUtils.DrawCircles(M, N, new ClonableImage(dim, dim));
						
						image.display();
					}

					// Dictionary Coding
					else if (choice == 2) {

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
