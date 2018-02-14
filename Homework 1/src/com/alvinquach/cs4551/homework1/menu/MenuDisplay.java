package com.alvinquach.cs4551.homework1.menu;

/** 
 * Displays the menu(s) and messages in the application.
 * @author Alvin Quach
 */
public class MenuDisplay {

	private String[] mainMenuItems = new String[] {
			"Conversion to Gray-scale Image (24bits->8bits)",
			"Conversion to N-level Image",
			"Conversion to 8bit Indexed Color Image using Uniform Color Quantization (24bits->8bits)",
			"Conversion to 8bit Indexed Color Image using Logarithmic Color Quantization (24bits->8bits)",
			"Conversion to 8bit Indexed Color Image using Hybrid Color Error Diffusion (24bits->8bits)",
			"Quit"
	};

	/** Prints the main menu to the console. */
	public void displayMainMenu() {
		System.out.println("\nMain Menu-----------------------------------");
		for (int i = 0; i < mainMenuItems.length; i++) {
			System.out.println((i + 1) + ". " + mainMenuItems[i]);
		}
		System.out.print("\n" + "Please enter the task number [1-" + mainMenuItems.length + "]:");
	}

	/** Prints N request to the console. */
	public void displayNRequest() {
		System.out.println("Enter a value for N (must be a power of 2, maximum 128):");
	}

	/** Prints the syntax for running the application to the console. */
	public void displayApplicationUsage(String applicationName) {
		System.out.println("Usage: java " + applicationName + " <path to ppm file>\n");
	}

	/** Prints file not found error to the console */
	public void displayFileNotFound(String filePath) {
		System.err.println("File \"" + filePath + "\" could not be found.");
	}

	/** Prints invalid input choice to the console */
	public void displayInvalidInput() {
		System.out.println("Invalid input.");
	}

	public int getMainMenuChoiceCount() {
		return mainMenuItems.length;
	}

}
