package test20210902;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Point;
import com.vividsolutions.jts.geom.Polygon;

import Vec.Vec;
import gui.CameraController;
import jtsUtil.JTSRender;
import jtsUtil.MoveFilter;
import processing.core.PApplet;

public class JTSTest extends PApplet {
	JTSRender jrender;
	CameraController cam;
	Polygon p0, p1, p2, p3, p4;
	Point pt;

	public void setup() {
		size(800, 600, P3D);
		GeometryFactory gf = new GeometryFactory();
		jrender = new JTSRender(this);
		cam = new CameraController(this, 100);

		Coordinate c0 = new Coordinate(0, 0, 0);
		Coordinate c1 = new Coordinate(100, 0, 0);
		Coordinate c2 = new Coordinate(100, 100, 0);
		Coordinate c3 = new Coordinate(0, 100, 0);

		Coordinate c4 = new Coordinate(100, 100, 0);
		Coordinate c5 = new Coordinate(200, 100, 0);
		Coordinate c6 = new Coordinate(200, 200, 0);
		Coordinate c7 = new Coordinate(100, 200, 0);

		Coordinate c4_ = new Coordinate(10, 10, 10);
		Coordinate c8 = new Coordinate(90, 10, 10);
		Coordinate c2_ = new Coordinate(90, 90, 10);
		Coordinate c9 = new Coordinate(10, 90, 10);

		Coordinate[] cs1 = new Coordinate[] { c0, c1, c2, c3, c0 };
		Coordinate[] cs2 = new Coordinate[] { c4, c5, c6, c7, c4 };

		Coordinate[] cs3 = new Coordinate[] { c4_, c8, c2_, c9, c4_ };

		Coordinate[] t1 = new Coordinate[] { new Coordinate(64.13137170597486, 187.67578976616647, 0),
				new Coordinate(64.13137170603306, 185.02578976719093, 0),
				new Coordinate(66.23128895351704, 185.02578976719093, 0),
				new Coordinate(66.23128895351704, 186.82578976747033, 0),
				new Coordinate(71.83128895343555, 186.82578976747033, 0),
				new Coordinate(71.83128895401762, 182.22578976784285, 0),
				new Coordinate(72.63128895388957, 182.2257897673772, 0),
				new Coordinate(72.63128895394777, 176.02578977398957, 0),
				new Coordinate(69.03128895391283, 176.02578976894182, 0),
				new Coordinate(69.03128895391282, 174.22578976369846, 0),
				new Coordinate(56.231371707872455, 174.22578976369846, 0),
				new Coordinate(56.23137170787245, 176.02578976894182, 0),
				new Coordinate(52.631371707837516, 176.02578977398957, 0),
				new Coordinate(52.631371707895724, 182.2257897673772, 0),
				new Coordinate(53.43137170776767, 182.22578976784285, 0),
				new Coordinate(53.431371708349744, 186.82578976747033, 0),
				new Coordinate(59.03137170826825, 186.82578976747033, 0),
				new Coordinate(59.03137170826825, 185.02578976719093, 0),
				new Coordinate(61.131288955752225, 185.02578976719093, 0),
				new Coordinate(61.13128895581043, 187.67578976616647, 0),
				new Coordinate(64.13137170597486, 187.67578976616647, 0), };

		Coordinate[] t2 = new Coordinate[] { new Coordinate(127.50000014507783, 46.50000000083958, 0),
				new Coordinate(-22.578170269154654, 46.50000000079871, 0.0),
				new Coordinate(-20.00000000011921, 118.97634364318849, 0.0),
				new Coordinate(-20.000000000171266, 291.3190403532982, 0),
				new Coordinate(127.5000001450777, 291.3190403532982, 0),
				new Coordinate(127.5000001450777, 112.19492085036298, 0),
				new Coordinate(127.5000001450777, 46.50000000084094, 0),
				new Coordinate(127.50000014507783, 46.50000000083958, 0), };

		p0 = gf.createPolygon(cs1);
		p1 = gf.createPolygon(cs2);
		p2 = gf.createPolygon(cs3);

		p3 = gf.createPolygon(t1);
		p4 = gf.createPolygon(t2);

//		p2 = (Polygon) p2.buffer(-0.1);

		pt = gf.createPoint(new Coordinate(100, 100));

		MoveFilter m = new MoveFilter(new Vec(300, 300, 0));

//		p0.apply(m);

//		println(p2.overlaps(p1));
//		println(p2.touches(p1));
//		println(p1.contains(p2));
//		println(p1.contains(pt));
//
//		println(p0.contains(p2));

		println(p4.contains(p3));
	}

	public void draw() {
		background(255);

//		jrender.setStroke(1);
//		jrender.setFill(0xffff0000);
//		jrender.draw(p0);
//		jrender.setFill(0xff00ff00);
//		jrender.draw(p1);
//		jrender.setFill(0xff0000ff);
//		jrender.draw(p2);
//		jrender.setFill(0xff0ff0ff);
//		jrender.draw(pt);

		jrender.setFill(0xffff0000);
		jrender.draw(p3);
		jrender.setFill(0xff00ff00);
		jrender.draw(p4);

	}

}
