package cs4551.homework3.utils;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;

import cs4551.homework3.models.image.rgb.Image;
import cs4551.homework3.models.image.ycbcr.ChromaSubsampling;

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
	
	public static byte[] bitsToBytes(boolean[] bits) {
		return bitsToBytes(ArrayUtils.convertBooleanArray(bits));
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


	/** Encodes image metadata into a bit stream. */
	public static Boolean[] encodeMetadata(Image image, ChromaSubsampling subsampling, int n) {
		byte[] bytes = ByteBuffer.allocate(16)
				.putInt(0, image.getW())
				.putInt(4, image.getH())
				.putInt(8, subsampling.ordinal())
				.putInt(12, n)
				.array();
		return ArrayUtils.convertBooleanArray(bytesToBits(bytes));
	}
	
	/** Decodes image metadata from a bit stream. */
	public static int[] decodeMetadata(boolean[] bits) throws Exception {
		if (bits.length != 128) {
			throw new Exception("Metadata must be 128 bits long.");
		}
		byte[] bytes = bitsToBytes(bits);
		int[] result = new int[4];
		result[0] = bytes[0] << 24 | (bytes[1] & 0xFF) << 16 | (bytes[2] & 0xFF) << 8 | (bytes[3] & 0xFF); // Width
		result[1] = bytes[4] << 24 | (bytes[5] & 0xFF) << 16 | (bytes[6] & 0xFF) << 8 | (bytes[7] & 0xFF); // Height
		result[2] = bytes[8] << 24 | (bytes[9] & 0xFF) << 16 | (bytes[10] & 0xFF) << 8 | (bytes[11] & 0xFF); // Subsampling
		result[3] = bytes[12] << 24 | (bytes[13] & 0xFF) << 16 | (bytes[14] & 0xFF) << 8 | (bytes[15] & 0xFF); // Compression
		return result;
	}

}