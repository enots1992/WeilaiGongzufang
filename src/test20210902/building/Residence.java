package test20210902.building;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

import Vec.Vec;
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
	public boolean mirrorHouse = true;
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
		updateBuildingBuffer();

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
	
	private void updateBuildingBuffer() {
		updateDistanceBetweenBuilding();
		Coordinate[]cs=this.boundary.getCoordinates();
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
