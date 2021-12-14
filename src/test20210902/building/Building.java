package test20210902.building;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;

import Vec.Vec;
import jtsUtil.JTSRender;
import jtsUtil.MoveFilter;
import processing.core.PApplet;
import test20210902.Block;
import wblut.processing.WB_Render;

public abstract class Building {

	public static String commercial = "commercial";
	public static String publicRentalHouse = "publicRentalHouse";
	public static String residence = "residence";
	public static String kindergarten = "kindergarten";

	public Block block;
	public Geometry boundary, buffer_toLow, buffer_toMulti, buffer_toHigh;
	public int floorNum;
	public double areaFloor, areaAll;
	public double floorHeight, height;

	public Vec v;

	/**
	 * double[]{dx,dy}
	 */
	public double[] distance_toLow, distance_toMulti, distance_toHigh;

	/**
	 * main direction of the building
	 */
	public Vec dir;

	public Building(Block block) {
		this.block = block;
		dir = new Vec(0, 1, 0);
		v = new Vec(0, 0, 0);
	}

	public Building(Block block, Geometry boundary) {
		this.block = block;
		this.boundary = boundary;
		dir = new Vec(0, 1, 0);

	}

	public Building setBoundary(Geometry boundary) {
		this.boundary = boundary;
		return this;
	}

	public Block getBlock() {
		return this.block;
	}

	public double getAreaBoundary() {
		return this.boundary.getArea();
	}

	public double getAreaAll() {
		return this.getAreaBoundary() * floorNum;
	}

	public void addv(Vec v) {
		this.v.addLocal(v);
	}

	public abstract void updatePosition();

	/**
	 * get boundary intersection
	 * 
	 * @param b b2
	 * @return
	 */
	public Geometry Intersection(Building b) {
		return this.boundary.intersection(b.boundary);
	}

	/**
	 * this building touches building b
	 * 
	 * @param b
	 * @return
	 */
	public boolean overlaps(Building b) {
		if (isLowStoreyBuilding()) {
			return this.boundary.overlaps(b.buffer_toLow);
		} else if (isMultiStoreyBuilding()) {
			return this.boundary.overlaps(b.buffer_toMulti);
		} else if (isHighStoreyBuilding()) {
			return this.boundary.overlaps(b.buffer_toHigh);
		}

		return false;
	}

	public double getDistance(Building b) {
		Vec v0 = this.getCenter();
		Vec v1 = b.getCenter();
		return v0.getDistance(v1);
	}

	/**
	 * get center of Geometry boundary
	 * 
	 * @return Vec
	 */
	public Vec getCenter() {
		return new Vec(this.boundary.getCentroid().getCoordinate());
	}

	/**
	 * get aabb double[]{minx,miny,maxx,maxy} of this building's Geometry.boundary
	 * 
	 * @return
	 */
	public double[] GetBuildingAABB() {
		return getGeoAABB(this.boundary);
	}

	/**
	 * get aabb double[]{minx,miny,maxx,maxy} of a geometry
	 * 
	 * @param geo
	 * @return
	 */
	public double[] getGeoAABB(Geometry geo) {
		double minx = Double.MAX_VALUE;
		double miny = Double.MAX_VALUE;
		double maxx = Double.MIN_VALUE;
		double maxy = Double.MIN_VALUE;
		for (Coordinate c : geo.getCoordinates()) {
			minx = (c.x < minx) ? c.x : minx;
			miny = (c.y < miny) ? c.y : miny;
			maxx = (c.x > maxx) ? c.x : maxx;
			maxy = (c.y > maxy) ? c.y : maxy;

		}
		return new double[] { minx, miny, maxx, maxy };
	}

	public abstract boolean isLowStoreyBuilding();

	public abstract boolean isMultiStoreyBuilding();

	public abstract boolean isHighStoreyBuilding();

	public abstract void move(Vec v);

	public abstract void setz(double d);

	public abstract void rotate(double angle);

	public abstract void changeFloorNum(int num);

	/**
	 * drawing
	 * 
	 * @param app
	 * @param wrender
	 * @param jrender
	 */
	public abstract void drawBuilding(PApplet app, WB_Render wrender, JTSRender jrender);

	public void drawExtrude(Geometry profile, double h, double z, PApplet app, WB_Render wrender, JTSRender jrender) {
		app.pushStyle();
		Coordinate[] cs = profile.getCoordinates();
		for (int i = 0; i < cs.length - 1; i++) {
			app.beginShape();
			Vec v0 = new Vec(cs[i]);
			Vec v1 = new Vec(cs[i + 1]);

			app.vertex(v0.xf(), v0.yf(), (float) z);
			app.vertex(v1.xf(), v1.yf(), (float) z);
			app.vertex(v1.xf(), v1.yf(), (float) (z + h));
			app.vertex(v0.xf(), v0.yf(), (float) (z + h));

			app.endShape();

		}

		app.popStyle();

	}
}
