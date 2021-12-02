package test20210902;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Geometry;

import jtsUtil.JTSRender;
import processing.core.PApplet;
import test20210902.building.Building;
import test20210902.building.Commercial;
import test20210902.building.PublicRentalHouse;
import test20210902.building.Residence;
import wblut.processing.WB_Render;

public class BuildingGroup {
	/**
	 * parent block
	 */
	public Block block;

	/**
	 * building type
	 */
	public String type;
	/**
	 * min area of this type
	 */
	double minArea;
	/**
	 * collection of buildings
	 */
	ArrayList<Building> bs;

	/**
	 * union boundary
	 */
	Geometry boundary;

	public BuildingGroup(Block b, String type, double minArea, boolean isSingle) {
		this.block = b;
		this.type = type;
		bs = new ArrayList<Building>();
		updateBuilding(type, minArea, isSingle);
		updateBoundary();
	}

	/**
	 * update buildings in this area
	 * 
	 * @param type
	 * @param minArea
	 * @param isSingle
	 */
	public void updateBuilding(String type, double minArea, boolean isSingle) {

		if (isSingle) {

		} else {
			Building b = null;
			double area = 0;

			if (type == Building.commercial) {
				b = new Commercial(block);
			} else if (type == Building.publicRentalHouse) {
				b = new PublicRentalHouse(block);
			} else if (type == Building.residence) {
				b = new Residence(block);
			} else if (type == Building.kindergarten) {

			}
			System.out.println("barea:" + b.getAreaBoundary());
			do {
				area += b.getAreaAll();
				bs.add(b);
			} while (area < minArea);

		}

	}

	public void update() {
		updateBoundary();
	}

	public void updateBoundary() {

	}

	public ArrayList<Building> getBuildings() {
		return this.bs;

	}

	public int getBuildingNum() {
		return this.bs.size();
	}

	/**
	 * 
	 * @param app     papplet
	 * @param jrender jts render
	 * @param wrender wb render
	 */
	public void drawGroup(PApplet app, JTSRender jrender, WB_Render wrender) {
		for (Building b : this.bs) {
			b.drawBuilding(app, wrender, jrender);
		}
	}

}
