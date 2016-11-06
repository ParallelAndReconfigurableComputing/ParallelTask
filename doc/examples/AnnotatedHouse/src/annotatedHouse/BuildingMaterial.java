package annotatedHouse;

import java.util.concurrent.atomic.AtomicBoolean;

public class BuildingMaterial {
	private int x;
	private int y;
	private int width;
	private int height;
	private AtomicBoolean isVisible;
	
	public BuildingMaterial(int x, int y, int width, int height, boolean isVisible){
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.isVisible = new AtomicBoolean(isVisible);
	}
	
	public int getX(){return this.x;}
	
	public int getY(){return this.y;}
	
	public int getWidth(){return this.width;}
	
	public int getHeight(){return this.height;}
	
	public boolean isVisible() {return this.isVisible.get();}
	
	public boolean getAndSetVisible(boolean isVisilbe){
		return this.isVisible.getAndSet(isVisilbe);
	}
}
