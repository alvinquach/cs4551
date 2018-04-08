package cs4551.homework3.models.image.ycbcr;

public enum ChromaSubsampling {
	
	YCRCB_444 (4, 4),
	YCRCB_440 (4, 0),
	YCRCB_422 (2, 2),
	YCRCB_420 (2, 0),
	YCRCB_411 (1, 1);
	
	private int h;
	private int v;
	
	private ChromaSubsampling(int h, int v) {
		this.h = h;
		this.v = v;
	}

	public int getH() {
		return h;
	}
	
	public int getV() {
		return v;
	}
	
}
