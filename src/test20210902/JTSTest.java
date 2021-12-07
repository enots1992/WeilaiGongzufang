package test20210902;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import processing.core.PApplet;

public class JTSTest extends PApplet {

	public void setup() {
		size(800, 600);
		GeometryFactory gf = new GeometryFactory();

		Coordinate c0 = new Coordinate(0, 0, 0);
		Coordinate c1 = new Coordinate(1, 0, 0);
		Coordinate c2 = new Coordinate(1, 1, 0);
		Coordinate c3 = new Coordinate(0, 1, 0);

		Coordinate c4 = new Coordinate(0.5, 0.5, 0);
		Coordinate c5 = new Coordinate(1.5, 0.5, 0);
		Coordinate c6 = new Coordinate(1.5, 1.5, 0);
		Coordinate c7 = new Coordinate(0.5, 1.5, 0);

		Coordinate[] cs1 = new Coordinate[] { c0, c1, c2, c3, c0 };
		Coordinate[] cs2 = new Coordinate[] { c4, c5, c6, c7, c4 };

		Polygon p0 = gf.createPolygon(cs1);

		Polygon p1 = gf.createPolygon(cs2);

		Point pt = gf.createPoint(c4);

		println(p0.overlaps(p1));
		println(p0.touches(p1));
		println(p0.contains(p1));
		println(p0.contains(pt));
	}

	public void draw() {

	}

}
