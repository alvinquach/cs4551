package homework4.models;

public class Coord {
	
	public int x;
	public int y;
	
	public Coord() {
		x = 0;
		y = 0;
	}
	
	public Coord(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public boolean isZero() {
		return x == 0 && y == 0;
	}
	
	@Override
	public String toString() {
		String x = String.valueOf(this.x);
		String y = String.valueOf(this.y);
		StringBuilder sb = new StringBuilder("[");
		for (int i = 0; i < 3 - x.length(); i++) {
			sb.append(" ");
		}
		sb.append(x).append(",");
		for (int i = 0; i < 3 - y.length(); i++) {
			sb.append(" ");
		}
		return sb.append(y).append("]").toString();
	}
	
}
