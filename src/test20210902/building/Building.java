package test20210902.building;

import com.vividsolutions.jts.geom.Geometry;

import Vec.Vec;
import jtsUtil.JTSRender;
import processing.core.PApplet;
import test20210902.Block;
import wblut.processing.WB_Render;

public abstract class Building {

	public static String commercial = "commercial";
	public static String publicRentalHouse = "publicRentalHouse";
	public static String residence = "residence";
	public static String kindergarten = "kindergarten";

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

	public Building(Block block, Geometry boundary) {
		this.block = block;
		this.boundary = boundary;

	}

	public Building setDistanceBetweenBuilding(double x0, double x1, double y0, double y1) {
		this.distanceBetweenBuilding = new double[] { x0, x1, y0, y1 };
		return this;
	}

	public Building setDistanceBetweenBuilding(double[] distance) {
		this.distanceBetweenBuilding = distance;
		return this;
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

	/**
	 * drawing
	 * 
	 * @param app
	 * @param wrender
	 * @param jrender
	 */
	public abstract void drawBuilding(PApplet app, WB_Render wrender, JTSRender jrender);
}
