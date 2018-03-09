package homework2.utils;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;

/** 
 * Utility class for reading and writing files.
 * @author Alvin Quach
 */
public class FileUtils {

	public static String readPlainTextFile(Path path) throws IOException {
		if (!path.toFile().exists()) {
			throw new FileNotFoundException();
		}
		byte[] bytes = Files.readAllBytes(path);
		return new String(bytes);
	}
	
	public static void writeToFile(String string, Path path) throws IOException {
		FileWriter fw = new FileWriter(path.toFile());
		PrintWriter pw = new PrintWriter(fw);
		for (String s : string.split("\n")) {
			pw.println(s);
		}
		pw.close();
	}
	
}
