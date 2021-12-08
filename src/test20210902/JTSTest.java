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
	Polygon p0, p1, p2,p3,p4;
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

		Coordinate[] t1 = new Coordinate[] { new Coordinate(45.30986818035488, 164.33051271018607, 0),
				new Coordinate(45.30986818041309, 161.68051271121053, 0),
				new Coordinate(47.40978542789706, 161.68051271121053, 0),
				new Coordinate(47.40978542789706, 163.48051271148992, 0),
				new Coordinate(53.00978542781557, 163.48051271148992, 0),
				new Coordinate(53.00978542839765, 158.88051271186245, 0),
				new Coordinate(53.80978542826959, 158.8805127113968, 0),
				new Coordinate(53.8097854283278, 152.68051271800917, 0),
				new Coordinate(50.20978542829286, 152.68051271296142, 0),
				new Coordinate(50.209785428292854, 150.88051270771805, 0),
				new Coordinate(37.40986818225248, 150.88051270771805, 0),
				new Coordinate(37.409868182252474, 152.68051271296142, 0),
				new Coordinate(33.80986818221754, 152.68051271800917, 0),
				new Coordinate(33.80986818227575, 158.8805127113968, 0),
				new Coordinate(34.60986818214769, 158.88051271186245, 0),
				new Coordinate(34.60986818272977, 163.48051271148992, 0),
				new Coordinate(40.20986818264828, 163.48051271148992, 0),
				new Coordinate(40.20986818264828, 161.68051271121053, 0),
				new Coordinate(42.30978543013225, 161.68051271121053, 0),
				new Coordinate(42.30978543019046, 164.33051271018607, 0),
				new Coordinate(45.30986818035488, 164.33051271018607, 0) };

		Coordinate[] t2 = new Coordinate[] { new Coordinate(127.50000014507783, 46.50000000083958, 0),
				new Coordinate(-22.578170269154654, 46.50000000079871, 0.0),
				new Coordinate(-20.00000000011921, 118.97634364318849, 0.0),
				new Coordinate(-20.000000000171266, 291.3190403532982, 0),
				new Coordinate(127.5000001450777, 291.3190403532982, 0),
				new Coordinate(127.5000001450777, 112.19492085036298, 0),
				new Coordinate(127.5000001450777, 46.50000000084094, 0),
				new Coordinate(127.50000014507783, 46.50000000083958, 0) };

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
		


		println(p3.contains(p4));
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
