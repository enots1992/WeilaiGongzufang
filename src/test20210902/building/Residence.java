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
		openHouse();
		// TODO Auto-generated constructor stub

	}

	public void openHouse() {
		IG.init();
		IG.open(path);
		for (ILayer layer : IG.layers()) {
			System.out.println("layerCrvNum:" + layer.getCurveNum());

			switch (layer.name()) {
			case "01":
				// 户型形状
				break;
			case "02":
				// 辅助空间形状
				break;
			case "03":
				boundary = this.getJTSPolygonFromFromICurves(layer.getCurves());
				// 总边界形状
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
		Polygon p = gf.createPolygon(cs);
		return null;
	}

}
