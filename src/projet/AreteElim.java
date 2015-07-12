package projet;

import java.awt.Point;

public class AreteElim extends Point {

	private static final long serialVersionUID = 1L;
	public int value;

	public AreteElim(Point point, int value) {
		super(point.x, point.y);
		this.value = value;
	}

	@Override
	public boolean equals(Object o) {
		if (o == null) {
			return false;
		}
		AreteElim tmp = (AreteElim) o;

		if (this.x == tmp.x && this.y == tmp.y) {
			return true;
		}
		return false;
	}

	public String toString() {
		return String.valueOf(this.x) + " " + String.valueOf(this.y)
				+ " valeur :" + this.value;

	}
}