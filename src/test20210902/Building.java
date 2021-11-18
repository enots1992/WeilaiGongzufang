package test20210902;

import com.vividsolutions.jts.geom.Geometry;

import Vec.Vec;
import jtsUtil.JTSRender;
import processing.core.PApplet;
import wblut.processing.WB_Render;

public abstract class Building {

	public Block block;
	public Geometry boundary, buffer;
	public int floorNum;
	public Vec pos;
	public double areaFloor, areaAll;

	/**
	 * double[]{x0,x1,y0,y1}
	 */
	public double[] distanceBetweenBuilding;

	public Building(Block block) {
		this.block = block;
	}

	public void setDistanceBetweenBuilding(double x0, double x1, double y0, double y1) {
		this.distanceBetweenBuilding = new double[] { x0, x1, y0, y1 };
	}

	public void setDistanceBetweenBuilding(double[] distance) {
		this.distanceBetweenBuilding = distance;
	}

	public void setBoundary(Geometry boundary) {
		this.boundary = boundary;
	}

	public Building(Block block, Geometry boundary) {
		this.block = block;
		this.boundary = boundary;

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

	/**
	 * drawing
	 * 
	 * @param app
	 * @param wrender
	 * @param jrender
	 */
	public abstract void drawBuilding(PApplet app, WB_Render wrender, JTSRender jrender);
}
