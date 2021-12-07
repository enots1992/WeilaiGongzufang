package test20210902;

import java.util.ArrayList;
import java.util.Random;

import com.vividsolutions.jts.geom.Geometry;

import Vec.Vec;
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

	Random ran;

	public BuildingGroup(Block b, String type, double minArea, boolean isSingle) {
		this.block = b;
		this.type = type;
		bs = new ArrayList<Building>();
		ran = new Random();
		ran.setSeed(0);
		updateBuilding(type, minArea, isSingle);
	}

	/**
	 * update buildings in this area
	 * 
	 * @param type
	 * @param minArea
	 * @param isSingle
	 */
	public void updateBuilding(String type, double minArea, boolean isSingle) {

		double area = 0;

		do {

			Building b = null;

			if (isSingle) {

				return;
			} else {

				if (type == Building.commercial) {
					b = new Commercial(block);
				} else if (type == Building.publicRentalHouse) {
					b = new PublicRentalHouse(block);
				} else if (type == Building.residence) {
					b = new Residence(block);
				} else if (type == Building.kindergarten) {

				}

			}

			area += b.getAreaAll();
			addBuilding(b);
		} while (area < minArea);

//		updateBuildingPostion();

		System.out.println("building num:" + this.getBuildings().size());

	}

	private int iter = 10;

	/**
	 * update its position,away from boundary and other buildings
	 */
	public void updateBuildingPostion() {

		for (int i = 0; i < iter; i++) {
			for (Building b1 : bs) {

				Vec center = new Vec(b1.boundary.getCentroid().getCoordinate());
				// 与边界的关系

//				if (!(g2.contains(g1))) {
//					Geometry g3 = g1.union(g2);
//					Geometry g4 = g3.difference(g2);
//					System.out.println("g1:" + g1.getArea());
//					System.out.println("g2:" + g2.getArea());
//					System.out.println("g3:" + g3.getArea());
//					System.out.println("g4:" + g4.getArea());
//					Vec v4 = new Vec(g4.getCentroid().getCoordinate());
//					Vec v2 = new Vec(g2.getCentroid().getCoordinate());
//					Vec dir = v4.subInstance(v2);
//					dir.setLengthLocal(0.5);
//					b1.addv(dir);
//
//				}

				// 与其他建筑的关系
				for (Building b2 : bs) {
					if (b1 != b2) {

						if (b1.overlaps(b2)) {
							// 两个建筑接触则远离
							Vec v1 = new Vec(b1.boundary.getCentroid().getCoordinate());
							Vec v2 = new Vec(b2.boundary.getCentroid().getCoordinate());

							Vec dir = v2.subInstance(v1);
							dir.setLengthLocal(0.1);

							b1.addv(dir);

						}

					}
				}

			}

			for (Building b1 : bs) {
				b1.updatePosition();
			}
			System.out.println("updateBuildingPosition:" + ((double) i / (double) iter * 100) + "%");
		}
	}

	/**
	 * add building,then update its position
	 * 
	 * @param b
	 */
	public void addBuilding(Building b) {
		this.bs.add(b);
		Geometry g1 = b.boundary;
		Geometry g2 = this.block.blockBoundary;

		Vec vb = new Vec(g1.getCentroid().getCoordinate());
		Vec vblock = new Vec(g2.getCentroid().getCoordinate());

		Vec dir = vblock.subInstance(vb);
		Vec dir2 = new Vec(15, 0, 0);
		dir2.rotate(Math.PI * 2 * ran.nextDouble());
		b.move(dir.addLocal(dir2));

//		System.out.println("g2ctg1:" + (g2.contains(g1)));

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
