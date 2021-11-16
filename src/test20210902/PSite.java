package test20210902;

import java.util.ArrayList;

import com.vividsolutions.jts.geom.Coordinate;
import com.vividsolutions.jts.geom.GeometryFactory;

import Vec.Vec;
import Vec.dist.Intersection3d;
import gui.CameraController;
import igeo.ICurve;
import igeo.IG;
import igeo.IVec;
import jtsUtil.JTSRender;
import processing.core.PApplet;
import wblut.geom.WB_Coord;
import wblut.geom.WB_GeometryFactory;
import wblut.geom.WB_Polygon;
import wblut.hemesh.HEC_FromFacelist;
import wblut.hemesh.HEC_FromPolygons;
import wblut.hemesh.HEC_Polygon;
import wblut.hemesh.HE_Face;
import wblut.hemesh.HE_Halfedge;
import wblut.hemesh.HE_Mesh;
import wblut.hemesh.HE_Vertex;
import wblut.processing.WB_Render;

/**
 * site,road
 * 
 * @author 1
 *
 */
public class PSite {

	public HE_Mesh site_boundray, site_all, site;
	public Vec[] boundary;

	public double[] roadRangeX, roadRangeY;
	public ArrayList<ArrayList<Vec>> forbid;
	boolean drawRoad = false;
	public ArrayList<Block> blocks;

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
		generateBlocks();

		for (ArrayList<Vec> l : forbid) {
			System.out.println(l.size());
		}

	}

	/**
	 * generate blocks by faces
	 */
	public void generateBlocks() {
		blocks = new ArrayList<Block>();
		for (HE_Face f : site.getFaces()) {
			blocks.add(new Block(f, halfLength));
		}

	}

	/**
	 * set road position
	 */
	public void separateSite() {
		roads = new Vec[4][];
		road_y2 = roadRangeY[1] - halfLength;
		road_y1 = roadRangeY[0] + halfLength;
		road_x1 = (roadRangeX[0] + roadRangeX[1]) / 2;

		updateRoadVecs();
		updateSite();
	}

	public IntersectionInfo getSiteLineIntersection(Vec[] line) {
		IntersectionInfo out = null;
		for (HE_Halfedge e : site_boundray.getBoundaryHalfedges()) {
			Vec a = line[0];
			Vec b = line[1];
			Vec c = new Vec(e.getStartVertex());
			Vec d = new Vec(e.getEndVertex());
			Vec intersection[] = Intersection3d.get2SegIntersection3d(a, b.subInstance(a), c, d.subInstance(c));

			if (this.vecEqual(intersection[0], intersection[1])) {
				out = new IntersectionInfo(intersection[0], e);
			}
		}

		return out;
	}

	/**
	 * update sites
	 */
	Vec intersection0, intersection2, intersection3;

	ArrayList<WB_Polygon> qs = new ArrayList<WB_Polygon>();
	ArrayList<HE_Vertex> vs = new ArrayList<HE_Vertex>();
	int[][] fs = new int[3][];

	public void updateSite() {

		for (HE_Vertex v : site_boundray.getVertices()) {
			vs.add(v);
		}

		WB_GeometryFactory gf = WB_GeometryFactory.instance();
		ArrayList<HE_Vertex> points = new ArrayList<HE_Vertex>();

		IntersectionInfo i0 = getSiteLineIntersection(roads[0]);
		IntersectionInfo i2 = getSiteLineIntersection(roads[2]);
		IntersectionInfo i3 = getSiteLineIntersection(roads[3]);

		i0.e.setLabel(11);
		i2.e.setLabel(12);
		i3.e.setLabel(13);

		boolean end = false;

		// p1 intersection
		intersection0 = i0.v;

		// p2 intersection

		intersection2 = i2.v;
		// p3 intersection

		intersection3 = i3.v;

		HE_Vertex v0 = intersection0.getHE_Vertex();
		HE_Vertex v2 = intersection2.getHE_Vertex();
		HE_Vertex v3 = intersection3.getHE_Vertex();
		HE_Vertex va = roads[1][0].getHE_Vertex();
		HE_Vertex vb = roads[1][1].getHE_Vertex();

		vs.add(v0);
		vs.add(v2);
		vs.add(v3);
		vs.add(va);
		vs.add(vb);

		// set polygon points
		// p1
		end = false;
		HE_Halfedge e1 = i0.e;
		HE_Halfedge e1_ = e1;
		points.add(v3);
		points.add(vb);
		points.add(va);
		points.add(v0);
		points.add(e1.getEndVertex());

		do {
			if (e1_.getNextInFace().getLabel() == 13) {
//				points.add(e1_.getEndVertex());
				end = true;
			} else {
				e1_ = e1_.getNextInFace();
				points.add(e1_.getEndVertex());
			}

		} while (end == false);
		fs[0] = new int[points.size()];
		for (int i = 0; i < points.size(); i++) {
			HE_Vertex v = points.get(i);
			fs[0][i] = vs.indexOf(v);
		}

		WB_Polygon p1 = gf.createSimplePolygon(getHE_Vertex_Instance(points));
		qs.add(p1);

		// p2
		end = false;
		points = new ArrayList<HE_Vertex>();
		HE_Halfedge e2 = i3.e;
		HE_Halfedge e2_ = e2;
		points.add(v2);
		points.add(vb);
		points.add(v3);
		points.add(e2.getEndVertex());

		do {
			if (e2_.getNextInFace().getLabel() == 12) {
//				points.add(e2_.getStartVertex());
				end = true;
			} else {
				e2_ = e2_.getNextInFace();
				points.add(e2_.getEndVertex());
			}

		} while (end == false);

		fs[1] = new int[points.size()];
		for (int i = 0; i < points.size(); i++) {
			HE_Vertex v = points.get(i);
			fs[1][i] = vs.indexOf(v);
		}

		WB_Polygon p2 = gf.createSimplePolygon(getHE_Vertex_Instance(points));
		qs.add(p2);

		// p3
		end = false;
		points = new ArrayList<HE_Vertex>();
		HE_Halfedge e3 = i2.e;
		HE_Halfedge e3_ = e3;

		points.add(v0);
		points.add(va);
		points.add(vb);
		points.add(v2);
		points.add(e3.getEndVertex());

		do {
			if (e3_.getNextInFace().getLabel() == 11) {
//				points.add(e3_.getEndVertex());
				end = true;
			} else {
				e3_ = e3_.getNextInFace();
				points.add(e3_.getEndVertex());
			}

		} while (end == false);

		fs[2] = new int[points.size()];
		for (int i = 0; i < points.size(); i++) {
			HE_Vertex v = points.get(i);
			fs[2][i] = vs.indexOf(v);
		}

		WB_Polygon p3 = gf.createSimplePolygon(getHE_Vertex_Instance(points));
		qs.add(p3);

		// set polygons
		HEC_FromFacelist creator = new HEC_FromFacelist();
		ArrayList<HE_Vertex> vvs = new ArrayList<HE_Vertex>();
		for (HE_Vertex v : vs) {
			HE_Vertex v_ = new HE_Vertex(v.xd(), v.yd(), v.zd());
			vvs.add(v_);
		}
		creator.setVertices(vvs);
		creator.setFaces(fs);
		HEC_FromPolygons creatorp = new HEC_FromPolygons();
		creatorp.setPolygons(qs);

		site = new HE_Mesh(creatorp);

		f = site.getFaces().get(selectedFaceIndex);

//		for (int[] ids : fs) {
//			System.out.println("");
//			for (int id : ids) {
//				System.out.println("id:" + id);
//			}
//		}

		System.out.println("site size:" + site.getFaces().size());

//		for (WB_Polygon p : qs) {
//			System.out.println("=========new polygon===========");
//			for (WB_Coord c : p.getPoints()) {
//				new Vec(c.xd(), c.yd(), c.zd()).print("coord");
//			}
//		}

	}

	private ArrayList<HE_Vertex> getHE_Vertex_Instance(ArrayList<HE_Vertex> vs) {
		ArrayList<HE_Vertex> vvs = new ArrayList<HE_Vertex>();
		for (HE_Vertex v : vs) {
			vvs.add(new HE_Vertex(v.xd(), v.yd(), v.zd()));
		}
		return vvs;
	}

	private int selectedFaceIndex = 0;

	/**
	 * set roads
	 */
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

	/**
	 * get roadRange
	 */
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

	/**
	 * sort double[][]
	 * 
	 * @param ns
	 */
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

	/**
	 * sort double[]
	 * 
	 * @param ns
	 */
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

	/**
	 * union segs
	 * 
	 * @param seg
	 * @param segs
	 * @return
	 */
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

		int pos = 0;

		int count = -1;
		for (int i = 0; i < ds.length; i++) {
			if (ds[i] < a) {
				pos = 0;
				count++;
			} else if (ds[i] == a) {
				pos = 1;

			} else if (ds[i] < b) {
				pos = 2;
				count++;
			} else if (ds[i] == b) {
				pos = 3;
			} else {
				pos = 4;
				count++;
			}

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
//		System.out.println("");
//		System.out.print("//union segs");
//		for (double[] s : out) {
//			System.out.print("[" + s[0] + "," + s[1] + "]");
//		}
//		System.out.print("union segs//");
		return out;

	}

	/**
	 * import site info
	 */
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
		site_boundray = new HE_Mesh(creator);
		e = site_boundray.getBoundaryHalfedges().get(0);
	}

	/**
	 * import forbid area info
	 */
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

	private double threshold = 0.00000001;

	public boolean vecEqual(Vec v1, Vec v2) {
		if (Math.abs(v1.x - v2.x) < threshold && Math.abs(v1.y - v2.y) < threshold
				&& Math.abs(v1.z - v2.z) < threshold) {
			return true;
		} else {
			return false;
		}

	}

	private HE_Halfedge e;
	private HE_Face f;

//	public void drawNextEdge() {
//		e = e.getNextInFace();
//
//	}
//
//	public void drawNextFace() {
//		this.selectedFaceIndex++;
//		selectedFaceIndex = selectedFaceIndex % site.getFaces().size();
//		f = site.getFaces().get(selectedFaceIndex);
//
//		e = f.getFaceEdges().get(0);
//	}

	public void drawNextEdge() {
		e = e.getNextInFace();

	}

	public void drawNextFace() {
		this.selectedFaceIndex++;
		selectedFaceIndex = selectedFaceIndex % site.getFaces().size();
		f = site.getFaces().get(selectedFaceIndex);
		e = f.getFaceHalfedges().get(0);
	}

	/**
	 * draw site
	 * 
	 * @param app
	 * @param wrender
	 */
	public void draw(PApplet app, WB_Render wrender, JTSRender jrender, boolean drawSelectEdge, boolean drawSelectFace,
			CameraController cam) {

		app.pushStyle();
		// draw site_boundray import
		if (false) {
			app.strokeWeight(1);
			wrender.drawEdges(site_boundray);
			app.noStroke();
			app.fill(255, 0, 0);
			wrender.drawFaces(site_boundray);
		}
		// draw site by blocks
		if (true) {
			app.strokeWeight(1);
			wrender.drawEdges(site);
			app.noStroke();
			app.fill(255, 128, 128);
			wrender.drawFaces(site);
		}

		if (false) {
			// draw selected edge
			if (drawSelectEdge) {
				app.pushStyle();
				app.strokeWeight(10);
				app.stroke(0, 128, 255);
				wrender.drawEdge(e);
				app.popStyle();
			}
			// draw selected face
			if (drawSelectFace) {
				app.pushStyle();
				app.fill(255, 0, 255);
				app.stroke(255, 255, 128);
				wrender.drawFace(f);
				app.popStyle();
			}
		}

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

			app.strokeWeight(2);

			for (Vec[] vv : roads) {
				app.line((float) vv[0].x, (float) vv[0].y, (float) vv[1].x, (float) vv[1].y);
			}
			app.strokeWeight(10);
			app.point(intersection0.xf(), intersection0.yf(), intersection0.zf());
			app.point(intersection2.xf(), intersection2.yf(), intersection2.zf());
			app.point(intersection3.xf(), intersection3.yf(), intersection3.zf());

			app.popStyle();
		}

	}

	public void drawBlock(PApplet app, WB_Render wrender, JTSRender jrender) {
		for (Block b : blocks) {
			b.drawBlock(jrender, app);
		}
	}

	private class IntersectionInfo {
		Vec v;
		HE_Halfedge e;

		private IntersectionInfo(Vec v, HE_Halfedge e) {
			this.v = v;
			this.e = e;
//			System.out.println("===new intersection===");
//			v.print("intersection point");
//			new Vec(e.getStartVertex()).print("e start");
//			new Vec(e.getEndVertex()).print("e end");

		}
	}

}
