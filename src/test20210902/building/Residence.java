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
 * 住宅楼，户型固定
 * 
 * @author enots1992
 *
 */
public class Residence extends Building {
	String path = "E:\\workspace\\30#WeiLai\\data\\houseType01.3dm";
	boolean mirrorHouse = true;
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
		this.distanceBetweenBuilding = new double[] {};
		this.floorNum = 18;
		openFile();
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

//		System.out.println("boundary_house:" + boundary_house.getArea());
//		System.out.println("boundary_support:" + boundary_support.getArea());
//		System.out.println("boundary:" + boundary.getArea());
		if (mirrorHouse) {
			boundary = mirrorGeo(boundary);
			boundary_support = mirrorGeo(boundary_support);
			boundary_house = mirrorGeo(boundary_house);
		}
	}

	private double[] getGeoAABB(Geometry geo) {
		return null;
	}

	private Geometry mirrorGeo(Geometry geo) {
		Geometry mir;
		GeometryFactory gf = new GeometryFactory();
		Coordinate[] mirShell;
		Vec center = null;
		int dim = geo.getDimension();
		if (dim == 2) {
			if (geo instanceof Polygon) {
				Polygon poly = (Polygon) geo;
				Coordinate[] shell = poly.getExteriorRing().getCoordinates();
				mirShell = new Coordinate[shell.length];

				for (int i = 0; i < shell.length; i++) {
					Vec v = new Vec(shell[i]);
					Vec vv = v.mirrorInstance(center, new Vec(1, 0, 0));
					mirShell[i] = null;
				}
			}
		}

		return null;
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

	@Override
	public void move(Vec v) {
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

}
