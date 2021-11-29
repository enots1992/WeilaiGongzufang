package test20210902.building;

import gzf.gui.CameraController;
import jtsUtil.JTSRender;
import processing.core.PApplet;
import wblut.processing.WB_Render;

public class BuildingDrawingTest extends PApplet {
	CameraController cam;
	WB_Render wrender;
	JTSRender jrender;
	Building b;

	public void setup() {
		size(800, 600, P3D);
		cam = new CameraController(this);
		wrender = new WB_Render(this);
		jrender = new JTSRender(this);

		b = new Residence(null);

	}

	public void draw() {
		background(0);
		cam.drawSystem(100);
		b.drawBuilding(this, wrender, jrender);

	}

}
