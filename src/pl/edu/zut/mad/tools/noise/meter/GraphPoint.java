package pl.edu.zut.mad.tools.noise.meter;

public class GraphPoint {

	private int x;
	private double y;

	public GraphPoint(int x, double y) {
		this.setX(x);
		this.setY(y);

	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public double getY() {
		return y;
	}

	public void setY(double y) {
		this.y = y;
	}

}
