package test20210902;

import gui.CameraController;
import igeo.ICurve;
import igeo.IG;
import jtsUtil.JTSRender;
import processing.core.PApplet;
import wblut.processing.WB_Render;

public class PlanManager {
	PSite site;

	public PlanManager() {
	}

	public void openSite(String path) {
		site = new PSite();
		site.openFile(path);
	}

	public void generateRoad() {
		site.generateRoad();
	}

	///selection
	public void nextEdge() {
		site.drawNextEdge();
	}

	public void nextFace() {
		site.drawNextFace();
	}

	public void draw(PApplet app, WB_Render render, JTSRender jrender, CameraController cam) {
		site.draw(app, render, jrender, true, true, cam);
	}

}
