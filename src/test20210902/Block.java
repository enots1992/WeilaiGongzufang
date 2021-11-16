package test20210902;

import wblut.hemesh.HE_Face;

public class Block {
	private HE_Face f;

	public Block(HE_Face f) {
		this.f = f;
	}

	public void printBlockInfo() {
		System.out.println("===block info===");

		System.out.println("地块面积：");
		System.out.println("道路面积：");
	}

}
