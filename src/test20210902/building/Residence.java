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
 * סլ¥�����͹̶�
 * 
 * @author enots1992
 *
 */
public class Residence extends Building {
	String path = "E:\\workspace\\30#WeiLai\\data\\houseType01.3dm";
	boolean mirrorHouse = true;
	/**
	 * ������״
	 */
	Geometry boundary_house;
	/**
	 * �����ռ���״
	 */
	Geometry boundary_support;

	public Residence(Block block) {
		super(block);
		this.distanceBetweenBuilding = new double[] {};
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
			System.out.println("layerCrvNum:" + layer.getCurveNum());

			switch (layer.name()) {
			case "01":
				// ������״
				boundary_house = this.getJTSPolygonFromFromICurves(layer.getCurves());
				break;
			case "02":
				// �����ռ���״
				boundary_support = this.getJTSPolygonFromFromICurves(layer.getCurves());
				break;
			case "03":
				// �ܱ߽���״
				boundary = this.getJTSPolygonFromFromICurves(layer.getCurves());
				break;
			}
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
				// ������״
				boundary_house = this.getJTSPolygonFromFromICurves(layer.getCurves());
				break;
			case "02":
				// �����ռ���״
				boundary_support = this.getJTSPolygonFromFromICurves(layer.getCurves());
				break;
			case "03":
				// �ܱ߽���״
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
		return p;
	}

}
