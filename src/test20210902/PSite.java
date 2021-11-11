package test20210902;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import Vec.Vec;
import igeo.ICurve;
import igeo.IG;
import igeo.IVec;
import processing.core.PApplet;
import wblut.geom.WB_Coord;
import wblut.geom.WB_Polygon;
import wblut.hemesh.HEC_Polygon;
import wblut.hemesh.HE_Mesh;
import wblut.hemesh.HE_Vertex;
import wblut.processing.WB_Render;

public class PSite {

	public HE_Mesh site;
	public Vec[] boundary;

	public double[] roadRangeX, roadRangeY;
	public ArrayList<ArrayList<Vec>> forbid;
	boolean drawRoad = false;

	double road_y1, road_y2, road_x1;
	Vec[][] roads;

	private double halfLength = 5;

	public PSite() {
		forbid = new ArrayList<ArrayList<Vec>>();
	}

	public void openFile(String path) {
		IG.init();
		IG.open(path);

		importSite();
		importForbid();
		generateRoad();
		separateSite();

		for (ArrayList<Vec> l : forbid) {
			System.out.println(l.size());
		}

	}

	public void separateSite() {
		roads = new Vec[4][];
		road_y2 = roadRangeY[1] - halfLength;
		road_y1 = roadRangeY[0] + halfLength;
		road_x1 = (roadRangeX[0] + roadRangeX[1]) / 2;
		updateRoadVecs();
	}

	public void updateRoadVecs() {
		Vec v0 = new Vec(-100, road_y2);
		Vec v1 = new Vec(road_x1, road_y2);
		Vec v2 = new Vec(road_x1, road_y1);
		Vec v3 = new Vec(road_x1, -100);
		Vec v4 = new Vec(500, road_y1);

		roads[0] = new Vec[] { v0, v1 };
		roads[1] = new Vec[] { v1, v2 };
		roads[2] = new Vec[] { v2, v3 };
		roads[3] = new Vec[] { v2, v4 };

	}

	public void generateRoad() {
		drawRoad = true;
		ArrayList<double[]> boundaryx = new ArrayList<double[]>();
		ArrayList<double[]> boundaryy = new ArrayList<double[]>();
		for (ArrayList<Vec> line : forbid) {
			double xmax = Double.MIN_VALUE;
			double xmin = Double.MAX_VALUE;
			double ymax = Double.MIN_VALUE;
			double ymin = Double.MAX_VALUE;
			for (Vec v : line) {
				xmax = (xmax > v.x) ? xmax : v.x;
				xmin = (xmin < v.x) ? xmin : v.x;
				ymax = (ymax > v.y) ? ymax : v.y;
				ymin = (ymin < v.y) ? ymin : v.y;
			}
			boundaryx.add(new double[] { xmin, xmax });
			boundaryy.add(new double[] { ymin, ymax });
		}
		sort(boundaryx);
		sort(boundaryy);

		ArrayList<double[]> rangex = new ArrayList<double[]>();
		rangex.add(boundaryx.get(0));
		for (int i = 1; i < boundaryx.size(); i++) {
			rangex = union(boundaryx.get(i), rangex);
		}

		ArrayList<double[]> rangey = new ArrayList<double[]>();
		rangey.add(boundaryy.get(0));
		for (int i = 1; i < boundaryy.size(); i++) {
			rangey = union(boundaryy.get(i), rangey);
		}

		roadRangeX = new double[] { rangex.get(0)[1], rangex.get(1)[0] };
		roadRangeY = new double[] { rangey.get(0)[1], rangey.get(1)[0] };

//		for (double[] ds : range) {
//			for (double d : ds) {
//				System.out.println("d:" + d);
//
//			}
//		}

	}

	public void sort(ArrayList<double[]> ns) {

		for (int i = 0; i < ns.size() - 1; i++)
			for (int j = 0; j < ns.size() - 1 - i; j++) {
				if (ns.get(j)[0] > ns.get(j + 1)[0]) {
					double temp0 = ns.get(j)[0];
					double temp1 = ns.get(j)[1];

					ns.get(j)[0] = ns.get(j + 1)[0];
					ns.get(j)[1] = ns.get(j + 1)[1];

					ns.get(j + 1)[0] = temp0;
					ns.get(j + 1)[1] = temp1;
				}
			}

	}

	public void sort(double[] ns) {

		for (int i = 0; i < ns.length - 1; i++)
			for (int j = 0; j < ns.length - 1 - i; j++) {
				if (ns[j] > ns[j + 1]) {
					double temp = ns[j];
					ns[j] = ns[j + 1];
					ns[j + 1] = temp;
				}
			}

	}

	public ArrayList<double[]> union(double[] seg, ArrayList<double[]> segs) {
		ArrayList<double[]> out = new ArrayList<double[]>();
		double a = seg[0];
		double b = seg[1];

		double[] ds = new double[2 + segs.size() * 2];
		ds[0] = seg[0];
		ds[1] = seg[1];

		int t = 2;
		for (double[] seg_ : segs) {
			for (double d : seg_) {
				ds[t] = d;
				t++;
			}
		}

		sort(ds);

		double[] outseg = new double[2];

		int ida = -1, idb = -1;

		int pos = 0;

		int count = -1;
		for (int i = 0; i < ds.length; i++) {
			if (ds[i] < a) {
				pos = 0;
				count++;
			} else if (ds[i] == a) {
				ida = i;
				pos = 1;

			} else if (ds[i] < b) {
				pos = 2;
				count++;
			} else if (ds[i] == b) {
				idb = i;
				pos = 3;
			} else {
				pos = 4;
				count++;
			}

//			if (i % 2 == 0) {
//				outseg[0] = ds[i];
//			} else {
//				outseg[1] = ds[i];
//				out.add(outseg);
//				outseg = new double[2];
//
//			}
			if (outseg[0] == 0) {
				outseg[0] = ds[i];

			} else {
				if (pos == 0) {
					outseg[1] = ds[i];
				} else if (pos == 1) {

				} else if (pos == 2) {

				} else if (pos == 3) {
					if ((count + 2) % 2 == i % 2) {
						outseg[1] = ds[i];
					}
				} else {
					if ((count + 2) % 2 == i % 2) {
						outseg[1] = ds[i];
					}
				}

				if (outseg[1] == 0) {

				} else {
					out.add(outseg);
					outseg = new double[2];
				}

			}

		}
		System.out.println("//union segs");
		for (double[] s : out) {
			System.out.println("[" + s[0] + "," + s[1] + "]");
		}
		System.out.println("union segs//");
		return out;

	}

	public void importSite() {
		/**
		 * import site
		 */
		ArrayList<WB_Coord> cs = new ArrayList<WB_Coord>();

		for (int i = 0; i < IG.layer("01").getCurves().length; i++) {
			IVec pt = IG.layer("01").getCurves()[i].pt(0);
			cs.add(new HE_Vertex(pt.x, pt.y, pt.z));
		}

		WB_Polygon poly = new WB_Polygon(cs);
		HEC_Polygon creator = new HEC_Polygon(poly, 0);
		site = new HE_Mesh(creator);
	}

	public void importForbid() {
		/**
		 * import forbid area
		 */

		System.out.println(IG.layer("02").getCurves().length);

		ArrayList<ICurve> crvs = new ArrayList<ICurve>();
		for (ICurve crv : IG.layer("02").getCurves()) {
			crvs.add(crv);
		}

		for (ICurve crv : crvs) {

			ArrayList<Vec> nline;
			Vec va = new Vec(crv.pt(0));
			Vec vb = new Vec(crv.pt(1));

			if (forbid.size() == 0) {
				nline = new ArrayList<Vec>();
				nline.add(va);
				nline.add(vb);
				forbid.add(nline);
			} else {
				nline = new ArrayList<Vec>();
				ArrayList<Vec> addline = new ArrayList<Vec>();
				Vec pre = null;
				Vec next = null;
				for (ArrayList<Vec> aline : forbid) {
					Vec v0 = aline.get(0);
					Vec v1 = aline.get(aline.size() - 1);
					if (vecEqual(va, v0)) {
						addline = aline;
						pre = vb;
					} else if (vecEqual(va, v1)) {
						addline = aline;
						next = vb;
					} else if (vecEqual(vb, v0)) {
						addline = aline;
						pre = va;
					} else if (vecEqual(vb, v1)) {
						addline = aline;
						next = va;
					} else {

						nline.add(va);
						nline.add(vb);
					}

				}
				if (addline.size() == 0) {
					forbid.add(nline);
				} else {
					if (pre != null) {
						addline.add(0, pre);
					} else if (next != null) {
						addline.add(next);
					}
				}

			}

		}
	}

	public boolean vecEqual(Vec v1, Vec v2) {
		if (v1.x == v2.x && v1.x == v2.x && v1.y == v2.y && v1.z == v2.z) {
			return true;
		} else {
			return false;
		}

	}

	public void draw(PApplet app, WB_Render wrender) {

		// draw site
		app.pushStyle();
		app.strokeWeight(1);
		wrender.drawEdges(site);
		app.noStroke();
		app.fill(255, 128, 128);
		wrender.drawFaces(site);

		app.popStyle();

		// draw forbid

		app.pushStyle();
		app.strokeWeight(5);

		app.stroke(255, 0, 0);
		for (ArrayList<Vec> line : forbid) {

			for (int i = 0; i < line.size() - 1; i++) {
				Vec v0 = line.get(i);
				Vec v1 = line.get(i + 1);

				app.line(v0.xf(), v0.yf(), v1.xf(), v1.yf());
			}

		}

		app.popStyle();
//draw road
		if (drawRoad) {

			app.pushStyle();
			app.strokeWeight(1);
			app.line((float) roadRangeX[0], -100, (float) roadRangeX[0], 500);
			app.line((float) roadRangeX[1], -100, (float) roadRangeX[1], 500);

			app.line(-100, (float) roadRangeY[0], 500, (float) roadRangeY[0]);
			app.line(-100, (float) roadRangeY[1], 500, (float) roadRangeY[1]);

			app.strokeWeight(3);

			for (Vec[] vv : roads) {
				app.line((float) vv[0].x, (float) vv[0].y, (float) vv[1].x, (float) vv[1].y);
			}

			app.popStyle();
		}

	}

}
