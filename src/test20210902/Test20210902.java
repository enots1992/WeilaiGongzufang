package test20210902;

import gui.CameraController;
import igeo.IG;
import jtsUtil.JTSRender;
import processing.core.PApplet;
import wblut.processing.WB_Render;

public class Test20210902 extends PApplet {
	PlanManager manager;
	String path = "E:\\workspace\\30#WeiLai\\data\\03.3dm";
	CameraController cam;
	WB_Render render;
	JTSRender jrender;

	public void setup() {
		size(1200, 900, P3D);

		cam = new CameraController(this, 1000);
		cam.top();
		manager = new PlanManager();
		manager.openSite(path);
		render = new WB_Render(this);
		jrender = new JTSRender(this);
	}

	public void draw() {
		background(255, 255, 255);
		cam.drawSystem(1000);
		manager.draw(this, render, jrender, cam);
	}

	public void keyPressed() {
		switch (keyCode) {
		case 'C':
			//change road
			manager.changeRoad();
			break;
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
		case 'E':
			System.out.println("draw next edge");
			manager.nextEdge();
			break;
		case 'F':
			System.out.println("draw next face");
			manager.nextFace();
			break;

		}
	}

}
