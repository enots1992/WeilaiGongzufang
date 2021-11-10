package test20210902;

import gui.CameraController;
import igeo.ICurve;
import igeo.IG;
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

	public void draw(PApplet app, WB_Render render) {
		site.draw(app, render);
	}

}
