package test20210902.building;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.Geometry;
import com.vividsolutions.jts.geom.GeometryFactory;
import com.vividsolutions.jts.geom.Polygon;

import igeo.ICurve;
import igeo.IG;
import igeo.ILayer;
import igeo.IVec;

public class ResidenceType {
	ICurve crv1[], crv2[], crv3[];

	public ResidenceType(String path) {
		IG.init();
		IG.open(path);
		for (ILayer layer : IG.layers()) {

			switch (layer.name()) {
			case "图层 01":
				// 户型形状
				crv1 = layer.getCurves();
				break;
			case "图层 02":
				// 辅助空间形状
				crv2 = layer.getCurves();
				break;
			case "图层 03":
				// 总边界形状
				crv3 = layer.getCurves();
				break;
			}
		}

	}

	/**
	 * 
	 * @return户型形状
	 */
	public Geometry getBoundary_house() {
		return this.getJTSPolygonFromFromICurves(crv1);
	}

	/**
	 * 
	 * @return辅助空间形状
	 */
	public Geometry getBoundary_support() {
		return this.getJTSPolygonFromFromICurves(crv2);
	}

	/**
	 * 
	 * @return总边界形状
	 */
	public Geometry getBoundary() {
		return this.getJTSPolygonFromFromICurves(crv3);
	}

	/**
	 * 
	 * @param crvs
	 * @return
	 */
	private Geometry getJTSPolygonFromFromICurves(ICurve[] crvs) {
		GeometryFactory gf = new GeometryFactory();
		Coordinate[] cs = new Coordinate[crvs.length + 1];
		for (int i = 0; i < crvs.length; i++) {
			IVec v = crvs[i].pt(0);
			cs[i] = new Coordinate(v.x, v.y, v.z);
		}
		cs[crvs.length] = cs[0];

		Polygon p = gf.createPolygon(cs);

//		System.out.println("p:" + p.getArea());
		return p;
	}

}
