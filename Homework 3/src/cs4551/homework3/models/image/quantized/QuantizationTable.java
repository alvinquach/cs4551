package cs4551.homework3.models.image.quantized;

public enum QuantizationTable {
	
	LUMA_QUANTIZATION (new int[][] {
			{ 4, 4, 4, 8, 8,16,16,32},
			{ 4, 4, 4, 8, 8,16,16,32},
			{ 4, 4, 8, 8,16,16,32,32},
			{ 8, 8, 8,16,16,32,32,32},
			{ 8, 8,16,16,32,32,32,32},
			{16,16,16,32,32,32,32,32},
			{16,16,32,32,32,32,32,32},
			{32,32,32,32,32,32,32,32}
	}),

	CHROMA_QUANTIZATION (new int[][] {
			{ 8, 8, 8,16,32,32,32,32},
			{ 8, 8, 8,16,32,32,32,32},
			{ 8, 8,16,32,32,32,32,32},
			{16,16,32,32,32,32,32,32},
			{32,32,32,32,32,32,32,32},
			{32,32,32,32,32,32,32,32},
			{32,32,32,32,32,32,32,32},
			{32,32,32,32,32,32,32,32}
	});
	
	private int[][] table;
	
	private QuantizationTable(int[][] table) {
		this.table = table;
	}

	public int[][] getTable() {
		return table.clone();
	}

}
