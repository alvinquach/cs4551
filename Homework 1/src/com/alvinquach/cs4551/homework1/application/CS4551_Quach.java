package com.alvinquach.cs4551.homework1.application;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

import com.alvinquach.cs4551.homework1.image.ClonableImage;
import com.alvinquach.cs4551.homework1.image.Image;
import com.alvinquach.cs4551.homework1.menu.MenuDisplay;
import com.alvinquach.cs4551.homework1.operations.GrayscaleConvertor;

public class CS4551_Quach {

	private static final String OUTPUT_FILE = "Result.ppm";

	private static MenuDisplay menuDisplay = new MenuDisplay();

	private static ClonableImage image;

	public static void main(String[] args) {

		if (args.length != 1) {
			menuDisplay.displayApplicationUsage(CS4551_Quach.class.getSimpleName());
			System.exit(1);
		}

		try {
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
					break;
				}

				else {
					Image result = image.clone();
					if (choice == 1) {
						try {
							new GrayscaleConvertor().applyAndDisplay(result);
							result.write2PPM(OUTPUT_FILE);
						}
						catch (Exception e) {
							e.printStackTrace();
						}
					}
					
					// Display main menu again.
					menuDisplay.displayMainMenu();
				}
			}
			catch (InputMismatchException e) {
				menuDisplay.displayInvalidInput();
			}
		}
		sc.close();
	}

}
