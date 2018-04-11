package cs4551.homework3.models.image.ycbcr;

import java.util.Arrays;

import cs4551.homework3.models.encode.rle.RunLengthEncode;
import cs4551.homework3.models.encode.rle.RunLengthPair;
import cs4551.homework3.models.image.ImageConstants;
import cs4551.homework3.models.image.dct.DCTBlock;
import cs4551.homework3.models.image.quantized.QuantizationTable;
import cs4551.homework3.models.image.quantized.QuantizedBlock;
import cs4551.homework3.utils.FileUtils;
import cs4551.homework3.utils.ImageUtils;

public class YCbCrQuantized {

	private QuantizedBlock[] quantizedLumaBlocks;

	private QuantizedBlock[] quantizedCbBlocks;

	private QuantizedBlock[] quantizedCrBlocks;

	private int compressionLevel;

	private ChromaSubsampling subsampling;
	
	private int width;
	
	private int height;

	/** Quantizes a Y`CbCr image. */
	public YCbCrQuantized(YCbCrDCT yCbCrDCTImage, int n) throws Exception {

		compressionLevel = n;
		subsampling = yCbCrDCTImage.getSubsampling();
		width = yCbCrDCTImage.getWidth();
		height = yCbCrDCTImage.getHeight();

		// Quantize luma
		DCTBlock[] lumaBlocks = yCbCrDCTImage.getLuma().getDctBlocks();
		quantizedLumaBlocks = new QuantizedBlock[lumaBlocks.length];
		for (int i = 0; i < lumaBlocks.length; i++) {
			quantizedLumaBlocks[i] = new QuantizedBlock(lumaBlocks[i].getValues(), n, QuantizationTable.LUMA_QUANTIZATION);
		}

		// Quantize Cb
		DCTBlock[] cbBlocks = yCbCrDCTImage.getCb().getDctBlocks();
		quantizedCbBlocks = new QuantizedBlock[cbBlocks.length];
		for (int i = 0; i < cbBlocks.length; i++) {
			quantizedCbBlocks[i] = new QuantizedBlock(cbBlocks[i].getValues(), n, QuantizationTable.CHROMA_QUANTIZATION);
		}

		// Quantize Cr
		DCTBlock[] crBlocks = yCbCrDCTImage.getCr().getDctBlocks();
		quantizedCrBlocks = new QuantizedBlock[crBlocks.length];
		for (int i = 0; i < crBlocks.length; i++) {
			quantizedCrBlocks[i] = new QuantizedBlock(crBlocks[i].getValues(), n, QuantizationTable.CHROMA_QUANTIZATION);
		}

	}

	/** Reconstructs quantized Y'CbCr data from a bit array. Bit array should include image metadata header. */
	public YCbCrQuantized(boolean[] bits) throws Exception {

		// Parse metadata header
		int[] metadata = FileUtils.decodeMetadata(Arrays.copyOf(bits, 128));
		width = metadata[0];
		height = metadata[1];
		subsampling = ChromaSubsampling.values()[metadata[2]];
		int n = compressionLevel = metadata[3];

		int blockSize = ImageConstants.JPEG_BLOCK_SIZE;

		// Pad dimensions to multiple of 8.
		int paddedWidth = width % blockSize > 0 ? width / blockSize * blockSize + blockSize : width;
		int paddedHeight = height % blockSize > 0 ? height / blockSize * blockSize + blockSize : height;

		int lumaBlockCount = paddedWidth * paddedHeight / (blockSize * blockSize);
		int chromaHBlockCount = (int)Math.ceil(ImageUtils.getChromaWidth(paddedWidth, subsampling) / (double)ImageConstants.JPEG_BLOCK_SIZE);
		int chromaVBlockCount = (int)Math.ceil(ImageUtils.getChromaHeight(paddedHeight, subsampling) / (double)ImageConstants.JPEG_BLOCK_SIZE);
		int chromaBlockCount = chromaHBlockCount * chromaVBlockCount;

		int lumaCodeSizeLimit = 10 - n;
		int chromaCodeSizeLimit = 9 - n;
		int lengthSizeLimit = 6;

		int bitIndex = 128; // Skip metadata
		boolean newBlock = true;

		// Reconstruct Y'
		RunLengthEncode lumaRunLengthEncoded = new RunLengthEncode();
		while (true) {
			int endBitIndex = bitIndex + lumaCodeSizeLimit + (newBlock ? 0 : lengthSizeLimit);
			RunLengthPair pair = RunLengthPair.fromBits(Arrays.copyOfRange(bits, bitIndex, endBitIndex), lumaCodeSizeLimit, lengthSizeLimit);
			lumaRunLengthEncoded.getRunLengthPairs().add(pair);
			bitIndex = endBitIndex;
			if (lumaRunLengthEncoded.totalLength() % (blockSize * blockSize - 1) == 0) {
				newBlock = !newBlock;
			}
			if (lumaRunLengthEncoded.totalLength() >= lumaBlockCount * (blockSize * blockSize - 1)) {
				newBlock = true;
				break;
			}
		}
		quantizedLumaBlocks = lumaRunLengthEncoded.toQuantizedBlocks(n, QuantizationTable.LUMA_QUANTIZATION);

		// Reconstruct Cb
		RunLengthEncode cbRunLengthEncoded = new RunLengthEncode();
		while (true) {
			int endBitIndex = bitIndex + chromaCodeSizeLimit + (newBlock ? 0 : lengthSizeLimit);
			RunLengthPair pair = RunLengthPair.fromBits(Arrays.copyOfRange(bits, bitIndex, endBitIndex), chromaCodeSizeLimit, lengthSizeLimit);
			cbRunLengthEncoded.getRunLengthPairs().add(pair);
			bitIndex = endBitIndex;
			if (cbRunLengthEncoded.totalLength() % (blockSize * blockSize - 1) == 0) {
				newBlock = !newBlock;
			}
			if (cbRunLengthEncoded.totalLength() >= chromaBlockCount * (blockSize * blockSize - 1)) {
				newBlock = true;
				break;
			}
		}
		quantizedCbBlocks = cbRunLengthEncoded.toQuantizedBlocks(n, QuantizationTable.CHROMA_QUANTIZATION);

		// Reconstruct Cr
		RunLengthEncode crRunLengthEncoded = new RunLengthEncode();
		while (true) {
			int endBitIndex = bitIndex + chromaCodeSizeLimit + (newBlock ? 0 : lengthSizeLimit);
			RunLengthPair pair = RunLengthPair.fromBits(Arrays.copyOfRange(bits, bitIndex, endBitIndex), chromaCodeSizeLimit, lengthSizeLimit);
			crRunLengthEncoded.getRunLengthPairs().add(pair);
			bitIndex = endBitIndex;
			if (crRunLengthEncoded.totalLength() % (blockSize * blockSize - 1) == 0) {
				newBlock = !newBlock;
			}
			if (crRunLengthEncoded.totalLength() >= chromaBlockCount * (blockSize * blockSize - 1)) {
				newBlock = true;
				break;
			}
		}
		quantizedCrBlocks = crRunLengthEncoded.toQuantizedBlocks(n, QuantizationTable.CHROMA_QUANTIZATION);

	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public QuantizedBlock[] getQuantizedLumaBlocks() {
		return quantizedLumaBlocks;
	}

	public QuantizedBlock[] getQuantizedCbBlocks() {
		return quantizedCbBlocks;
	}

	public QuantizedBlock[] getQuantizedCrBlocks() {
		return quantizedCrBlocks;
	}

	public int getCompressionLevel() {
		return compressionLevel;
	}

	public ChromaSubsampling getSubsampling() {
		return subsampling;
	}


	/**
	 * De-quantized the values back to DCT coefficients.
	 * Image dimensions need to be manually provided since the
	 * quantized blocks are stored sequentially in an array.
	 * @param width Width of the image or luma component.
	 * @param height Height of the image or luma component.
	 * @return DCT representation of the Y'CbCr values.
	 */
	public YCbCrDCT dequantize() {

		// De-quantize luma
		DCTBlock[] lumaBlocks = new DCTBlock[quantizedLumaBlocks.length];
		for (int i = 0; i < quantizedLumaBlocks.length; i++) {
			lumaBlocks[i] = new DCTBlock();
			float[][] values = quantizedLumaBlocks[i].dequantize();
			for (int j = 0; j < values.length; j++) {
				System.arraycopy(values[j], 0, lumaBlocks[i].getValues()[j], 0, values[j].length);
			}
		}

		// De-quantize Cb
		DCTBlock[] cbBlocks = new DCTBlock[quantizedCbBlocks.length];
		for (int i = 0; i < quantizedCbBlocks.length; i++) {
			cbBlocks[i] = new DCTBlock();
			float[][] values = quantizedCbBlocks[i].dequantize();
			for (int j = 0; j < values.length; j++) {
				System.arraycopy(values[j], 0, cbBlocks[i].getValues()[j], 0, values[j].length);
			}
		}

		// De-quantize Cr
		DCTBlock[] crBlocks = new DCTBlock[quantizedCrBlocks.length];
		for (int i = 0; i < quantizedCrBlocks.length; i++) {
			crBlocks[i] = new DCTBlock();
			float[][] values = quantizedCrBlocks[i].dequantize();
			for (int j = 0; j < values.length; j++) {
				System.arraycopy(values[j], 0, crBlocks[i].getValues()[j], 0, values[j].length);
			}
		}

		// Pad dimensions to multiple of 8.
		int blockSize = ImageConstants.JPEG_BLOCK_SIZE;
		int paddedWidth = width % blockSize > 0 ? width / blockSize * blockSize + blockSize : width;
		int paddedHeight = height % blockSize > 0 ? height / blockSize * blockSize + blockSize : height;
		
		return new YCbCrDCT(lumaBlocks, cbBlocks, crBlocks, subsampling, paddedWidth, paddedHeight);

	}

}
