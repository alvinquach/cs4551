package cs4551.homework3.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
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

	public static byte[] readFileAsBytes(Path path) throws IOException {
		if (!path.toFile().exists()) {
			throw new FileNotFoundException();
		}
		return Files.readAllBytes(path);
	}

	public static void writePlainTextFile(String string, Path path) throws IOException {
		FileWriter fw = new FileWriter(path.toFile());
		PrintWriter pw = new PrintWriter(fw);
		for (String s : string.split("\n")) {
			pw.println(s);
		}
		pw.close();
	}

	public static void writeBinaryFile(byte[] bytes, Path path, boolean append) throws IOException {
		FileOutputStream fos;
		fos = new FileOutputStream(path.toFile(), append);
		fos.write(bytes);
		fos.close();
	}

	public static byte[] bitsToBytes(Boolean[] bits) {
		int byteCount = (int)Math.ceil(bits.length / 8f);
		byte[] result = new byte[byteCount];
		for (int i = 0; i < byteCount; i++) {
			byte _byte = 0;
			for (int j = 0; j < 8; j++) {
				try {
					_byte = (byte)((_byte << 1) + (bits[i * 8 + j] ? 1 : 0));
				}
				catch (ArrayIndexOutOfBoundsException e) {
					_byte = (byte)(_byte << 1);
				}
			}
			result[i] = _byte;
		}
		return result;
	}

	public static boolean[] bytesToBits(byte[] bytes) {
		int bitCount = bytes.length * 8;
		boolean[] result = new boolean[bitCount];
		for (int i = 0; i < bytes.length; i++) {
			byte _byte = bytes[i];
			for (int j = 0; j < 8; j++) {
				result[i * 8 + j] = (_byte & (128 >> j)) != 0;
			}
		}
		return result;
	}

}