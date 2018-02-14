package com.alvinquach.cs4551.homework1.application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.alvinquach.cs4551.homework1.menu.MenuDisplay;
import com.alvinquach.cs4551.homework1.models.image.ClonableImage;
import com.alvinquach.cs4551.homework1.operations.GrayscaleConverter;
import com.alvinquach.cs4551.homework1.operations.HybridErrorDiffuser;
import com.alvinquach.cs4551.homework1.operations.ImageOperation;
import com.alvinquach.cs4551.homework1.operations.LogarithmicColorQuantizer;
import com.alvinquach.cs4551.homework1.operations.NLevelErrorDiffuser;
import com.alvinquach.cs4551.homework1.operations.NLevelThresholdConverter;
import com.alvinquach.cs4551.homework1.operations.UniformColorQuantizer;

/**
 * @author Alvin Quach
 */
public class CS4551_Quach {

	private static MenuDisplay menuDisplay = new MenuDisplay();

	private static ClonableImage image;

	private static String filename;

	public static void main(String[] args) {

		if (args.length != 1) {
			menuDisplay.displayApplicationUsage(CS4551_Quach.class.getSimpleName());
			System.exit(1);
		}

		try {
			filename = Paths.get(args[0]).getFileName().toString().split("\\.")[0];
			image = new ClonableImage(args[0]);
		}
		catch (FileNotFoundException e) {
			menuDisplay.displayFileNotFound(args[0]);
			System.exit(1);
		}
		catch (IOException e) {
			System.err.println(e.getMessage());
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

				else {
					ClonableImage result = image.clone();
					if (choice == 1) {
						try {
							GrayscaleConverter converter = new GrayscaleConverter(result);
							converter.applyAndDisplay();
							converter.save(filename);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
					else if (choice == 2) {
						menuDisplay.displayNRequest();
						int levels = 0;
						while (true) {
							try {
								levels = sc.nextInt();
								if (levels != Integer.highestOneBit(levels) || levels > 128) {
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
						try {
							NLevelThresholdConverter thresholdConverter = new NLevelThresholdConverter(result, levels);
							thresholdConverter.applyAndDisplay();
							thresholdConverter.save(filename);

							// Re-clone the original image.
							result = image.clone();

							NLevelErrorDiffuser errorDiffusionConverter = new NLevelErrorDiffuser(result, levels);
							errorDiffusionConverter.applyAndDisplay();
							errorDiffusionConverter.save(filename);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
					else {
						ImageOperation quantizer =
								choice == 3 ? new UniformColorQuantizer(result) :
								choice == 4 ? new LogarithmicColorQuantizer(result) :
								choice == 5 ? new HybridErrorDiffuser(result) :
								null;
								
						if (quantizer != null) {
							try {
								quantizer.applyAndDisplay();
								quantizer.save(filename);
							}
							catch (Exception e) {
								e.printStackTrace();
							}
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
