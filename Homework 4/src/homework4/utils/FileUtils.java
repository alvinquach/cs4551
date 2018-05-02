package homework4.utils;

import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

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
	

	public static void writeToFile(String string, String path) throws IOException {
		writeToFile(string, Paths.get(path));
	}
	
	public static String extractPath(String filePath) {
		Path path = Paths.get(filePath);
		String filename = path.getFileName().toString();
		return filePath.substring(0, filePath.indexOf(filename));
	}
	
	public static String extractFilename(String filePath) {
		Path path = Paths.get(filePath);
		return path.getFileName().toString();
	}
	
	public static String generateNewFilePath(String existingFilePath, String prefix, String suffix, String extension) {
		Path path = Paths.get(existingFilePath);
		String filename = path.getFileName().toString();
		String basePath = existingFilePath.substring(0, existingFilePath.indexOf(filename));
		if (prefix != null) {
			filename = prefix + filename;
		}
		if (suffix != null) {
			int extensionDelimiterIndex = filename.lastIndexOf('.');
			filename = filename.substring(0, extensionDelimiterIndex) + suffix + filename.substring(extensionDelimiterIndex);
		}
		if (extension != null) {
			int extensionDelimiterIndex = filename.lastIndexOf('.');
			filename = filename.substring(0, extensionDelimiterIndex) + "." + extension;
		}
		return basePath + filename;
	}

}
