package homework2.utils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/** 
 * Utility class for reading and writing files.
 * @author Alvin Quach
 */
public class FileUtils {

	public static String readPlainTextFile(Path path) throws IOException {
		if (!Paths.get(path.toUri()).toFile().exists()) {
			throw new FileNotFoundException();
		}
		byte[] bytes = Files.readAllBytes(path);
		return new String(bytes);
	}
	
}
