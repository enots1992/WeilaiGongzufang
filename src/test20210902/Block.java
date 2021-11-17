package test20210902;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.LineString;
import com.vividsolutions.jts.geom.LinearRing;
import com.vividsolutions.jts.geom.Polygon;

import Vec.Vec;
import jtsUtil.JTSRender;
import processing.core.PApplet;
import wblut.hemesh.HE_Face;
import wblut.hemesh.HE_Halfedge;
import wblut.hemesh.HE_Vertex;

/**
 * 
 * @author enots1992
 *
 */
public class Block {
	private HE_Face f;
	private double halfLength;
	int fill;
	/**
	 * all area
	 */
	Geometry site;
	/**
	 * road area
	 */
	Geometry road;
	/**
	 * building area
	 */
	Geometry block;

	public Block(HE_Face f, double halfLength) {
		this.f = f;
		this.halfLength = halfLength;
		fill = 0xffff8800;
		update();
	}

	public void update() {
		GeometryFactory gf = new GeometryFactory();
		Coordinate[] cs = new Coordinate[f.getFaceVertices().size() + 1];
		for (int i = 0; i < f.getFaceVertices().size(); i++) {
			HE_Vertex v = f.getFaceVertices().get(i);
			cs[i] = new Coordinate(v.xd(), v.yd(), v.zd());
		}
		cs[f.getFaceVertices().size()] = cs[0];
		site = gf.createPolygon(cs);
		block = gf.createPolygon(cs);
//		System.out.println("face edge size" + f.getFaceEdges().size());
		for (HE_Halfedge e : f.getFaceHalfedges()) {

			if (e.getPair().getFace() == null) {
//is edge
			} else {
				// is road
				Vec v0 = new Vec(e.getStartVertex());
				Vec v1 = new Vec(e.getEndVertex());
				Coordinate[] ls = new Coordinate[] { v0.getCoordinate(), v1.getCoordinate() };
				LineString line = gf.createLineString(ls);
				block = block.difference(line.buffer(halfLength));
			}

		}

		road = site.difference(block);

	}

	/**
	 * drawing
	 */
	public void drawBlock(JTSRender jrender, PApplet app) {
		app.pushStyle();
		app.strokeWeight(1);
		app.stroke(0);
		
		jrender.setFill(fill);
		jrender.draw(block);
		jrender.setFill(128);
		jrender.draw(road);
		
		app.popStyle();

	}

	/**
	 * set block color
	 * 
	 * @param fill int[]{r,g,b}
	 */
	public void setFill(int fill) {
		this.fill = fill;
	}

	/**
	 * info
	 */
	public void printBlockInfo() {
		System.out.println("===block info===");

		System.out.println("地块面积：");
		System.out.println("道路面积：");
		System.out.println("地块功能：");
		System.out.println("建筑面积：");
	}

}
