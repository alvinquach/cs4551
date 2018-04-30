package homework4.application;

/** 
 * Displays the menu(s) and messages in the application.
 * @author Alvin Quach
 */
public class MenuDisplay {
	
	public enum Menu {
		
		MAIN_MENU ("Main Menu", new String[] {
				"Block-Based Motion Compensation",
				"Removing Moving Objects",
				"Quit"
		});
		
		private String name;
		private String[] items;
		
		private Menu(String name, String[] items) {
			this.name = name;
			this.items = items;
		}

		public String getName() {
			return name;
		}
		
		public int itemCount() {
			return items.length;
		}
		
	}

	/** Prints a menu to the console. */
	public void displayMenu(Menu menu) {
		System.out.print("\n" + menu.name);
		for (int i = 0; i < 44 - menu.name.length(); i++) {
			System.out.print("-");
		}
		System.out.println();
		for (int i = 0; i < menu.items.length; i++) {
			System.out.println((i + 1) + ". " + menu.items[i]);
		}
		System.out.print("\n" + "Please enter the task number [1-" + menu.items.length + "]:");
	}

	/** Prints request for n, p, etc. to the console. */
	public void displayAliasingSubmenu(char varName) {
		displayAliasingSubmenu(varName, null);
	}
	
	/** Prints request for n, p, etc. to the console. */
	public void displayAliasingSubmenu(char varName, String desc) {
		System.out.println("Enter a value for " + varName + (desc == null ? "" : " (" + desc + ")") + ":");
	}
	
	/** Prints the request for the filename/path. */
	public void displayFilePathRequest() {
		displayFilePathRequest(null);
	}
	
	/** Prints the request for the filename/path. */
	public void displayFilePathRequest(String desc) {
		System.out.println("Enter the path to the" + (desc == null ? "" : " " + desc) + " file: ");
	}

	/** Prints the syntax for running the application to the console. */
	public void displayApplicationUsage(String applicationName) {
		System.out.println("Usage: java " + applicationName + "\n");
	}
	
	/** Prints file not found error to the console */
	public void displayFileNotFound(String filePath) {
		displayFileNotFound(filePath, "");
	}

	/** Prints file not found error to the console */
	public void displayFileNotFound(String filePath, String additional) {
		System.out.println("File \"" + filePath + "\" could not be found. " + additional);
	}
	
	/** Prints file cannot be read error to the console */
	public void displayFileCannotBeRead(String filePath) {
		displayFileCannotBeRead(filePath, "");
	}
	
	/** Prints file cannot be read error to the console */
	public void displayFileCannotBeRead(String filePath, String additional) {
		System.out.println("File \"" + filePath + "\" could not be read. " + additional);
	}

	/** Prints invalid input choice to the console */
	public void displayInvalidInput() {
		System.out.println("Invalid input.");
	}

}
