package test20210902.building;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

import Vec.Vec;
import Vec.dist.Intersection3d;
import igeo.ICurve;
import igeo.IG;
import igeo.ILayer;
import igeo.IVec;
import jtsUtil.JTSRender;
import processing.core.PApplet;
import test20210902.Block;
import wblut.processing.WB_Render;

/**
 * 住宅楼(租赁房)，户型固定
 * 
 * @author enots1992
 *
 */
public class Residence extends Building {
	String path = "E:\\workspace\\30#WeiLai\\data\\houseType01.3dm";
	public boolean mirrorHouse = false;
	/**
	 * 户型形状
	 */
	Geometry boundary_house;
	/**
	 * 辅助空间形状
	 */
	Geometry boundary_support;

	public Residence(Block block) {
		super(block);

		this.floorNum = 18;
		this.floorHeight = 3.3;
		this.height = floorNum * floorHeight;
		openFile();
		updateBuildingBuffer2();

		// TODO Auto-generated constructor stub

	}

	private void setPosition() {

	}

	/**
	 * open default
	 */
	public void openFile() {
		IG.init();
		IG.open(path);
		for (ILayer layer : IG.layers()) {

			switch (layer.name()) {
			case "图层 01":
				// 户型形状
				boundary_house = this.getJTSPolygonFromFromICurves(layer.getCurves());
				break;
			case "图层 02":
				// 辅助空间形状
				boundary_support = this.getJTSPolygonFromFromICurves(layer.getCurves());
				break;
			case "图层 03":
				// 总边界形状
				boundary = this.getJTSPolygonFromFromICurves(layer.getCurves());
				break;
			}
		}

		if (mirrorHouse) {
			boundary = mirrorGeo(boundary);
			boundary_support = mirrorGeo(boundary_support);
			boundary_house = mirrorGeo(boundary_house);
		}
	}

	/**
	 * update distance gap&shape
	 */
	private void updateBuildingBuffer() {
		updateDistanceBetweenBuilding();
		updateBufferShape();
	}

	/**
	 * update shape
	 */
	private void updateBufferShape() {
		this.buffer_toHigh = bufferXY(this.boundary, this.distance_toHigh);
		this.buffer_toMulti = bufferXY(this.boundary, this.distance_toMulti);
		this.buffer_toLow = bufferXY(this.boundary, this.distance_toLow);

	}

	/**
	 * buffer ,xy distance are different
	 * 
	 * @param g        geometry
	 * @param distance double[]{gapx,gapy}
	 * @return
	 */
	private Geometry bufferXY(Geometry g, double[] distance) {
		GeometryFactory gf = new GeometryFactory();
		Coordinate[] cs = this.boundary.getCoordinates();
		Vec[][] bufferlines = new Vec[cs.length - 1][];
		Coordinate[] cs_ = new Coordinate[cs.length];

		Vec center = new Vec(g.getCentroid().getCoordinate());
		center.z = 0;
//		System.out.println("cslenght:" + cs.length);
		for (int i = 0; i < cs.length - 1; i++) {
			Vec v0 = new Vec(cs[i]);
			Vec v1 = new Vec(cs[i + 1]);

			v0.z = 0;
			v1.z = 0;

			// 线段方向
			Vec dir = v1.subInstance(v0);

			// offset 方向
			Vec bufferDir = dir.duplicate().rotate(Math.PI / 2);

			// offset方向与x方向夹角
			double angle = bufferDir.getAngleBetween(new Vec(1, 0, 0));

			if (equal(angle, Math.PI / 2, 0.00001)) {
				// y方向
//				System.out.println("y方向:"+angle/Math.PI*180);
				bufferDir.setLengthLocal(distance[1]);
			} else {
				// x方向

//				System.out.println("x方向:"+angle/Math.PI*180);

				bufferDir.setLengthLocal(distance[0]);
			}

			bufferlines[i] = new Vec[] { v0.addInstance(bufferDir), v1.addInstance(bufferDir) };
		}

		for (int i = 0; i < bufferlines.length; i++) {
			Vec[] line0 = bufferlines[i];
			Vec[] line1 = bufferlines[(i + 1) % bufferlines.length];

			Vec intersection = Intersection3d.get2LineIntersection2d(line0[0], line0[1], line1[0], line1[1]);

			if (intersection == null) {
				intersection = line0[1];
			}
//			if (intersection.y > 100) {
//				System.out.println("i:" + i);
//
//				line0[0].print("line0[0]");
//				line0[1].print("line0[1]");
//				line1[0].print("line1[0]");
//				line1[1].print("line1[1]");
//				intersection.print("intersection");
//			}
			cs_[i] = intersection.getCoordinate();
		}
		cs_[cs_.length - 1] = cs_[0];
		Geometry p = gf.createPolygon(cs_);
		p = p.buffer(0.001);
		p = p.buffer(-0.001);

		return p;
	}

	private void updateBuildingBuffer2() {
		updateDistanceBetweenBuilding();
		updateBufferShape2();
	}

	private void updateBufferShape2() {
		this.buffer_toHigh = bufferXY2(this.boundary, this.distance_toHigh);
		this.buffer_toMulti = bufferXY2(this.boundary, this.distance_toMulti);
		this.buffer_toLow = bufferXY2(this.boundary, this.distance_toLow);

	}

	/**
	 * 只有直线方向buffer
	 * 
	 * @param g
	 * @param distance
	 * @return
	 */
	private Geometry bufferXY2(Geometry g, double[] distance) {
		GeometryFactory gf = new GeometryFactory();
		Coordinate[] cs = this.boundary.getCoordinates();
		Vec[][] bufferlines = new Vec[cs.length - 1][];
		Coordinate[] cs_ = new Coordinate[cs.length];

		Geometry p = g.buffer(0.1);
		p = g.buffer(-0.1);

		Vec center = new Vec(g.getCentroid().getCoordinate());
		center.z = 0;
//		System.out.println("cslenght:" + cs.length);
		for (int i = 0; i < cs.length - 1; i++) {
			Vec v0 = new Vec(cs[i]);
			Vec v1 = new Vec(cs[i + 1]);

			v0.z = 0;
			v1.z = 0;

			// 线段方向
			Vec dir = v1.subInstance(v0);

			// offset 方向
			Vec bufferDir = dir.duplicate().rotate(Math.PI / 2);

			// offset方向与x方向夹角
			double angle = bufferDir.getAngleBetween(new Vec(1, 0, 0));

			if (equal(angle, Math.PI / 2, 0.00001)) {
				// y方向
//				System.out.println("y方向:"+angle/Math.PI*180);
				bufferDir.setLengthLocal(distance[1]);
			} else {
				// x方向

//				System.out.println("x方向:"+angle/Math.PI*180);

				bufferDir.setLengthLocal(distance[0]);
			}

			bufferlines[i] = new Vec[] { v0.addInstance(bufferDir), v1.addInstance(bufferDir) };

			Coordinate[] rect = new Coordinate[] { v0.getCoordinate(), v1.getCoordinate(),
					bufferlines[i][1].getCoordinate(), bufferlines[i][0].getCoordinate(), v0.getCoordinate() };

			Polygon pp = gf.createPolygon(rect);
			p = p.union(pp);
		}

		p = p.buffer(0.001);
		p = p.buffer(-0.001);

		return p;
	}

	private boolean equal(double num1, double num2, double tolerance) {
		if (Math.abs(num1 - num2) < tolerance) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 根据合肥城市技术管理规定第四章有关要求，在南北向平行布局的基础上设置建筑间距
	 */
	private void updateDistanceBetweenBuilding() {
		double gapx_toLow = 0, gapx_toMul = 0, gapx_toHigh = 0, gapy_toLow = 0, gapy_toMul = 0, gapy_toHigh = 0;
		// gapx
		if (isLowStoreyBuilding()) {
			gapx_toLow = 6;
			gapx_toMul = 6;
			gapx_toHigh = 9;
		} else if (isMultiStoreyBuilding()) {
			gapx_toLow = 6;
			gapx_toMul = 6;
			gapx_toHigh = 9;
		} else if (isHighStoreyBuilding()) {
			gapx_toLow = 9;
			gapx_toMul = 9;
			gapx_toHigh = 13;
		}
		// gapy
		if (isLowStoreyBuilding()) {
			gapy_toLow = ((1.35 * this.height) > 8) ? 1.35 * this.height : 8;
			gapy_toMul = 1.23 * this.height;
			gapy_toHigh = 13;
		} else if (isMultiStoreyBuilding()) {
			if (this.height > 10 && this.height < 18) {
				gapy_toLow = 1.23 * this.height;
				gapy_toMul = 1.23 * this.height;
				gapy_toHigh = ((1.23 * this.height) > 13) ? 1.23 * this.height : 13;
			} else if (this.height >= 18 && this.height < 27) {
				gapy_toLow = 1.26 * this.height;
				gapy_toMul = 1.26 * this.height;
				gapy_toHigh = ((1.26 * this.height) > 13) ? 1.26 * this.height : 16;
			}

		} else if (isHighStoreyBuilding()) {
			// 同时需要根据遮挡长度设定距离
			gapy_toLow = 30;
			gapy_toMul = 30;
			gapy_toHigh = 30;
		}

		this.distance_toLow = new double[] { gapx_toLow, gapy_toLow };
		this.distance_toMulti = new double[] { gapx_toMul, gapy_toMul };
		this.distance_toHigh = new double[] { gapx_toHigh, gapy_toHigh };

	}

	/**
	 * get aabb double[]{minx,miny,maxx,maxy} of a geometry
	 * 
	 * @param geo
	 * @return
	 */
	private double[] getGeoAABB(Geometry geo) {
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

	/**
	 * get the mirror of a geometry
	 * 
	 * @param geo
	 * @return
	 */
	private Geometry mirrorGeo(Geometry geo) {
		Geometry mir;
		GeometryFactory gf = new GeometryFactory();
		Coordinate[] mirShell = null;
		Vec center = new Vec(getGeoAABB(geo)[2], 0, 0);

		int dim = geo.getDimension();
		if (dim == 2) {
			if (geo instanceof Polygon) {
				Polygon poly = (Polygon) geo;
				Coordinate[] shell = poly.getExteriorRing().getCoordinates();
				mirShell = new Coordinate[shell.length];

				for (int i = 0; i < shell.length; i++) {
					Vec v = new Vec(shell[i]);
					Vec vv = v.mirrorInstance(center, new Vec(1, 0, 0));
					mirShell[i] = vv.getCoordinate();
				}
			}
		}

		mir = gf.createPolygon(mirShell);
		mir = mir.union(geo);

		return mir;
	}

	@Override
	public void changeFloorNum(int num) {
		// TODO Auto-generated method stub
		boolean changed = true;
		this.floorNum += num;
		this.height = floorNum * this.floorHeight;
		if (height > 100 || height < 3) {
			this.floorNum -= num;
			this.height = floorNum * this.floorHeight;
			changed = false;
		}

		if (changed) {
			this.updateBuildingBuffer2();
		}

	}

	/**
	 * open from file
	 */
	public void openFile(String path) {
		IG.init();
		IG.open(path);
		for (ILayer layer : IG.layers()) {
			System.out.println("layerCrvNum:" + layer.getCurveNum());

			switch (layer.name()) {
			case "01":
				// 户型形状
				boundary_house = this.getJTSPolygonFromFromICurves(layer.getCurves());
				break;
			case "02":
				// 辅助空间形状
				boundary_support = this.getJTSPolygonFromFromICurves(layer.getCurves());
				break;
			case "03":
				// 总边界形状
				boundary = this.getJTSPolygonFromFromICurves(layer.getCurves());
				break;

			}

		}
	}

	@Override
	public void drawBuilding(PApplet app, WB_Render wrender, JTSRender jrender) {
		// TODO Auto-generated method stub

		app.pushStyle();
		Coordinate c = this.boundary.getCentroid().getCoordinate();
		app.strokeWeight(5);
		app.point((float) c.x, (float) c.y);
		app.popStyle();
		// draw buffer

		jrender.setFill(false);
		jrender.setStroke(0xffffff00);
		jrender.draw(buffer_toHigh);
		jrender.setStroke(0xffff9999);
		jrender.draw(buffer_toMulti);
		jrender.setStroke(0xffff00ff);
		jrender.draw(buffer_toLow);

		// drawbuilding

//		jrender.setFill(true);
//		jrender.setFill(0xff000000);
		jrender.draw(this.boundary);
		for (int i = 0; i < this.floorNum; i++) {
			app.pushStyle();
			app.fill(128, 0, 0, 128);
			this.drawExtrude(this.boundary_house, this.floorHeight, i * floorHeight, app, wrender, jrender);
			app.fill(128, 0);
			this.drawExtrude(this.boundary_support, this.floorHeight, i * floorHeight, app, wrender, jrender);

			// bottom
			app.pushMatrix();
			app.translate(0, 0, (float) (i * floorHeight));
			jrender.setFill(true);
			jrender.draw(this.boundary);
			app.popMatrix();

			// cap
			app.pushMatrix();
			app.translate(0, 0, (float) ((i + 1) * floorHeight));
			jrender.setFill(true);
			jrender.draw(this.boundary_support);
			jrender.draw(this.boundary_house);
			app.popMatrix();

			app.popStyle();
		}

	}

	private Geometry getJTSPolygonFromFromICurves(ICurve[] crvs) {
		GeometryFactory gf = new GeometryFactory();
		Coordinate[] cs = new Coordinate[crvs.length + 1];
		for (int i = 0; i < crvs.length; i++) {
			IVec v = crvs[i].pt(0);
			cs[i] = new Coordinate(v.x, v.y, v.z);
		}
		cs[crvs.length] = cs[0];

		Polygon p = gf.createPolygon(cs);

		System.out.println("p:" + p.getArea());
		return p;
	}

	@Override
	/**
	 * 住宅建筑，10-27米,多层
	 */
	public boolean isMultiStoreyBuilding() {
		boolean b;
		if (height < 27 && height >= 10) {
			b = true;
		} else {
			b = false;
		}
		// TODO Auto-generated method stub
		return b;
	}

	@Override
	/**
	 * 住宅建筑，27米高层
	 */
	public boolean isHighStoreyBuilding() {
		boolean b = (height < 27) ? false : true;
		// TODO Auto-generated method stub
		return b;
	}

	@Override
	public boolean isLowStoreyBuilding() {

		boolean b = (height < 10) ? true : false;
		// TODO Auto-generated method stub
		return b;
	}

	@Override
	public void rotate(double angle) {
		// TODO Auto-generated method stub
	}

	@Override
	public void move(Vec v) {
		// TODO Auto-generated method stub

	}

}
