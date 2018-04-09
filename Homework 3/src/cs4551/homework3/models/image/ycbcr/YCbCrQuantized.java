package cs4551.homework3.models.image.ycbcr;

import cs4551.homework3.models.image.dct.DCTBlock;
import cs4551.homework3.models.image.quantized.QuantizationTable;
import cs4551.homework3.models.image.quantized.QuantizedBlock;

public class YCbCrQuantized {

	private QuantizedBlock[] quantizedLumaBlocks;

	private QuantizedBlock[] quantizedCbBlocks;

	private QuantizedBlock[] quantizedCrBlocks;
	
	private int compressionLevel;
	
	private ChromaSubsampling subsampling;

	private YCbCrQuantized() {

	}

	public YCbCrQuantized(YCbCrDCT yCbCrDCTImage, int n) throws Exception {
		
		compressionLevel = n;
		subsampling = yCbCrDCTImage.getSubsampling();

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
	public YCbCrDCT dequantize(int width, int height) {
		
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
		
		return new YCbCrDCT(lumaBlocks, cbBlocks, crBlocks, subsampling, width, height);
		
	}

}
