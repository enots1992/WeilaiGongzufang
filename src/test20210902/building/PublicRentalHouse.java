package test20210902.building;

import com.vividsolutions.jts.geom.Geometry;

import Vec.Vec;
import igeo.ICurve;
import igeo.IG;
import igeo.ILayer;
import jtsUtil.JTSRender;
import processing.core.PApplet;
import test20210902.Block;
import wblut.processing.WB_Render;

/**
 * ���ⷿ������̶������ɱ�
 * 
 * @author enots1992
 *
 */
public class PublicRentalHouse extends Building {

	public PublicRentalHouse(Block block) {
		super(block);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void drawBuilding(PApplet app, WB_Render wrender, JTSRender jrender) {
		// TODO Auto-generated method stub

	}

	@Override
	public void move(Vec v) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean isMultiStoreyBuilding() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isHighStoreyBuilding() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void rotate(double angle) {
		// TODO Auto-generated method stub
	}

	@Override
	public boolean isLowStoreyBuilding() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void changeFloorNum(int num) {
		// TODO Auto-generated method stub
		
	}

}
