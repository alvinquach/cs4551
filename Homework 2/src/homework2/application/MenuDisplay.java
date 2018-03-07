package homework2.application;

/** 
 * Displays the menu(s) and messages in the application.
 * @author Alvin Quach
 */
public class MenuDisplay {

	private String[] mainMenuItems = new String[] {
			"Aliasing",
			"Dictionary Coding",
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

	/** Prints request for M, N, K, etc. to the console. */
	public void displayAliasingSubmenu(char varName) {
		displayAliasingSubmenu(varName, null);
	}
	
	/** Prints request for M, N, K, etc. to the console. */
	public void displayAliasingSubmenu(char varName, String desc) {
		System.out.println("Enter a value for " + varName + (desc == null ? "" : " (" + desc + ")") + ":");
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
