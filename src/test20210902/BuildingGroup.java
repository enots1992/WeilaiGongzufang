package test20210902;

import java.util.ArrayList;
import java.util.Random;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.Polygon;

import Vec.Vec;
import jtsUtil.JTSRender;
import jtsUtil.SetZFilter;
import processing.core.PApplet;
import test20210902.building.Building;
import test20210902.building.Commercial;
import test20210902.building.PublicRentalHouse;
import test20210902.building.Residence;
import test20210902.building.ResidenceType;
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
	String typepath = "E:\\workspace\\30#WeiLai\\data\\houseType01.3dm";

	public BuildingGroup(Block b, String type, double minArea, boolean isSingle) {
		this.block = b;
		this.type = type;
		bs = new ArrayList<Building>();
		ran = new Random();
		ran.setSeed(0);
		updateBuildings(type, minArea, isSingle);
	}

	/**
	 * update buildings in this area
	 * 
	 * @param type
	 * @param minArea
	 * @param isSingle
	 */
	public void updateBuildings(String type, double minArea, boolean isSingle) {
		ResidenceType t1 = null;
		if (type == Building.residence) {

			t1 = new ResidenceType(typepath);
		}
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
					b = new Residence(block).setType(t1);
				} else if (type == Building.kindergarten) {

				}

			}

			area += b.getAreaAll();
			addBuilding(b);
		} while (area < minArea);

//		for(Building b:bs) {
//			Vec center=new Vec(b.boundary.getCentroid().getCoordinate());
//			center.print("center");
//		}

		updateBuildingPostion();

		System.out.println("building num:" + this.getBuildings().size());

	}

	private int iter = 200;

	/**
	 * update its position,away from boundary and other buildings
	 */
	public void updateBuildingPostion() {

		Geometry g2 = this.block.blockBoundary;
		for (int i = 0; i < iter; i++) {

			for (int j = 0; j < bs.size(); j++) {
				Building b1 = bs.get(j);
				// 与边界的关系
				Geometry g1 = b1.boundary.buffer(0.001).buffer(-0.001);

				if (!(g2.contains(g1))) {

					Geometry g3 = g1.union(g2);
					Geometry g4 = g3.difference(g2);

					Vec v4 = new Vec(g4.getCentroid().getCoordinate());
					Vec v2 = new Vec(g2.getCentroid().getCoordinate());
					Vec dir = v2.subInstance(v4);
					dir.setLengthLocal(3);
					b1.addv(dir);
				}
				for (int k = j + 1; k < bs.size(); k++) {
					Building b2 = bs.get(k);
					// 住宅遮挡方向的交错(仅正交)
					if ((b1 instanceof Residence) && (b2 instanceof Residence)) {
						double[] aabb1 = b1.GetBuildingAABB();
						double[] aabb2 = b2.GetBuildingAABB();

						Vec center1 = b1.getCenter();
						Vec center2 = b2.getCenter();
						double disty = Math.abs(center1.y - center2.y);
						double[] intersection = getIntersectionRange(aabb1, aabb2);

						if (intersection != null) {
							double dist = intersection[1] - intersection[0];
							double t = 0;
							if (dist < 30) {
								t = 0.3;
							} else if (30 <= dist || dist < 40) {
								t = 0.4;
							} else {
								t = 0.5;
							}
							Building southB = (center1.y < center2.y) ? b1 : b2;
							double gapy = southB.height * t;

							if (disty < gapy) {
								System.out.println("遮挡");
								Vec dir1 = new Vec(0, center1.y - center2.y, 0);
								Vec dir2 = new Vec(0, center2.y - center1.y, 0);

								dir1.setLengthLocal(0.1 + (1 - disty / gapy) * 0.3);
								dir2.setLengthLocal(0.1 + (1 - disty / gapy) * 0.3);

								b1.addv(dir1);
								b2.addv(dir2);

							}

						}
					}

					if (b1.overlaps(b2)) {
						// 两个建筑接触则远离(不包括x方向的交错)
						Vec v1 = new Vec(b1.boundary.getCentroid().getCoordinate());
						Vec v2 = new Vec(b2.boundary.getCentroid().getCoordinate());

						Geometry intersection = b1.Intersection(b2);

						Vec dir = v1.subInstance(v2);
						double dist = v1.getDistance(v2);

						double t = 1 / Math.pow(Math.E, dist / 100);
						dir.setLengthLocal(t);
						b1.addv(dir);
						b2.addv(dir.mulInstance(-1));

					}

				}

			}

			// move to center
			Vec currentCenter = new Vec(0, 0, 0);
			int num = this.bs.size();

			for (Building b : bs) {
				Vec v = b.getCenter();
				currentCenter = currentCenter.addInstance(v.mulInstance(1. / (double) num));
			}

			for (Building b : bs) {
				Vec c = b.getCenter();
				Vec dir = currentCenter.subInstance(c);
				dir.setLengthLocal(0.02);
				b.addv(dir);
			}

			// update
			for (Building b1 : bs) {
				b1.updatePosition();
			}

			if (i % 5 == 0) {
				System.out.println("updateBuildingPosition:" + ((double) i / (double) iter * 100) + "%");
			}
		}
	}

	/**
	 * 
	 * @param aabb1
	 * @param aabb2
	 * @return double[]{min,max}/null
	 */
	private double[] getIntersectionRange(double[] aabb1, double[] aabb2) {
		double[] intersection = null;
		double min1 = aabb1[0];
		double max1 = aabb1[2];

		double min2 = aabb2[0];
		double max2 = aabb2[2];

		double min = (min1 < min2) ? min1 : min2;
		double max = (max1 > max2) ? max1 : max2;

		if ((min == min1) && (max == max1)) {
			// 包含 min1,(min2,max2),max1
			intersection = new double[] { min2, max2 };
		} else if ((min == min2) && (max == max2)) {
			// 包含 min2,(min1,max1),max2
			intersection = new double[] { min1, max1 };
		} else if ((min == min1) && (max == max2) && (max1 > min2)) {
			// 交错 min1,(min2,max1),max2
			intersection = new double[] { min2, max1 };
		} else if ((min == min2) && (max == max1) && (max2 > min1)) {
			// 交错 min2,(min1,max2),max1
			intersection = new double[] { min1, max2 };
		}

		return intersection;
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

		dir.addLocal(dir2);
		b.move(dir);
//		b.setz(0);

//		System.out.println("");
//
//		dir.print("move");
//		System.out.println("g1 area:" + g1.getArea());
//		System.out.println("g2 area:" + g2.getArea());
//
//		vb = new Vec(g1.getCentroid().getCoordinate());
//		vblock = new Vec(g2.getCentroid().getCoordinate());
//
//		vb.print("g1center");
//		vblock.print("g2center");
//
//		System.out.println("g1 is p:" + (g1 instanceof Polygon));
//		System.out.println("g2 is p:" + (g2 instanceof Polygon));
//		for (Coordinate c : g1.getCoordinates()) {
//			System.out.print("new Coordinate(" + c.x + "," + c.y + "," + c.z + "),");
//		}
//		System.out.println("");
//		for (Coordinate c : g2.getCoordinates()) {
//			System.out.print("new Coordinate(" + c.x + "," + c.y + "," + c.z + "),");
//		}

//		System.out.println("");
//		System.out.println("g2 contain g1:" + (g2.contains(g1)));
//		System.out.println("g1 contain g2:" + (g1.contains(g2)));

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
