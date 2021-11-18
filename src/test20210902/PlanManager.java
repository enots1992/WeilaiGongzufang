package test20210902;

import Vec.Vec;
import gui.CameraController;
import gzf.Vec_Guo;
import igeo.ICurve;
import igeo.IG;
import jtsUtil.JTSRender;
import processing.core.PApplet;
import wblut.processing.WB_Render;

public class PlanManager {
	PSite site;
	private boolean changeroad = false;

	public PlanManager() {
	}

	public void openSite(String path) {
		site = new PSite();
		site.openFile(path);
	}

	public void generateBuildings() {
		site.generateBuildings();

	}

	public void optimizeBuildings() {

	}

	/// selection
	public void nextEdge() {
		site.drawNextEdge();
	}

	public void nextFace() {
		site.drawNextFace();
	}

	/**
	 * 
	 * @param v road change factor
	 */
	public void changeRoad(Vec v) {
		if (changeroad) {
			site.changeRoad(v);
		}

	}

	/**
	 * change boolean
	 */
	public void changeRoad() {
		changeroad = !changeroad;
		if (changeroad) {

			System.out.println("road change:start");
		} else {
			site.selectedRoad = null;
			site.applyChange();
			System.out.println("road change:end");
		}

	}

	/**
	 * if change set selectedRoad
	 */
	public void setSelectedRoad() {
		site.setSelectedRoad();
	}

	public void setCurrentRoad(PApplet app, CameraController cam) {

		if (changeroad) {
			Vec_Guo vv = cam.pick3dXYPlane(app.mouseX, app.mouseY);
			Vec v = new Vec(vv.x, vv.y, 0);
			site.setCurrentRoad(v);

		}

	}

	public void draw(PApplet app, WB_Render wrender, JTSRender jrender, CameraController cam) {
		setCurrentRoad(app, cam);
		site.draw(app, wrender, jrender, true, true, cam);
		site.drawBlock(app, wrender, jrender);
	}

}
