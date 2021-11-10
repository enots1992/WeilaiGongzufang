package test20210902;

import gui.CameraController;
import igeo.IG;
import processing.core.PApplet;
import wblut.processing.WB_Render;

public class Test20210902 extends PApplet {
	PlanManager manager;
	String path = "E:\\workspace\\30#WeiLai\\data\\01.3dm";
	CameraController cam;
	WB_Render render;

	public void setup() {
		size(800, 600, P3D);

		cam = new CameraController(this, 1000);
		cam.top();
		manager = new PlanManager();
		manager.openSite(path);
		render = new WB_Render(this);
	}

	public void draw() {
		background(255, 255, 255);

		cam.drawSystem(10000);
		manager.draw(this, render);
	}

	public void keyPressed() {
		switch (keyCode) {
		case 'T':
			cam.top();
			System.out.println("view:top");
			break;
		case 'P':
			cam.perspective();
			System.out.println("view:perspective");
			break;

		case 'G':

			cam.top();
			System.out.println("view:top");
			manager.generateRoad();
			break;

		}
	}

}
