package pt.test;

public class BuildingMaterial {
 
	private int x;
	private int y;
	private int width;
	private int height;
	private boolean isVisible;
	
	public BuildingMaterial(int x, int y, int width, int height, boolean isVisible) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isVisible = isVisible;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}

	public int getWidth() {
		return width;
	}

	public int getHeight() {
		return height;
	}

	public boolean isVisible() {
		return isVisible;
	}

	public void setVisible(boolean isVisible) {
		this.isVisible = isVisible;
	}
}
