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
	public Geometry boundary, buffer_toLow, buffer_toMulti, buffer_toHigh;
	public int floorNum;
	public Vec pos;
	public double areaFloor, areaAll;
	public double floorHeight, height;

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

	public abstract boolean isLowStoreyBuilding();

	public abstract boolean isMultiStoreyBuilding();

	public abstract boolean isHighStoreyBuilding();

	public abstract void move(Vec v);

	public abstract void rotate(double angle);

	/**
	 * drawing
	 * 
	 * @param app
	 * @param wrender
	 * @param jrender
	 */
	public abstract void drawBuilding(PApplet app, WB_Render wrender, JTSRender jrender);
}
