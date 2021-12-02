package test20210902.building;

import Vec.Vec;
import jtsUtil.JTSRender;
import processing.core.PApplet;
import test20210902.Block;
import wblut.processing.WB_Render;

/**
 * 商业 量形比例固定范围内变化
 * 
 * @author enots1992
 *
 */
public class Commercial extends Building {

	public Commercial(Block block) {
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
	public void rotate(double angle) {
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
	public boolean isLowStoreyBuilding() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void changeFloorNum(int num) {
		// TODO Auto-generated method stub
		
	}

}
