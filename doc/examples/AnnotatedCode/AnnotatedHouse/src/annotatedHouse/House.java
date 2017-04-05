package annotatedHouse;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import sp.annotations.Future;
import sp.annotations.TaskInfoType;

public class House extends Applet{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int width = 500;
	private int height = 320;
	private int N = 20;
	private boolean clearScreen = true;
	
	private CopyOnWriteArrayList<BuildingMaterial> foundation = null;
	private CopyOnWriteArrayList<BuildingMaterial> wallSiding = null;
	private CopyOnWriteArrayList<BuildingMaterial> roofTiles = null;
	private CopyOnWriteArrayList<BuildingMaterial> windows = null;
	
	private BuildingMaterial door = null;
	private BuildingMaterial forSaleSign = null;
	
	private Color colorRoof;
	private Color colorWalls;
	
	public House(Build builder){
		setPreferredSize(new Dimension(width, height));
		initialiseMaterial();
	}
	
	public Void buildSingleTask(Color colorWalls, Color colorRoof){
		build(colorWalls, colorRoof);
		return null;
	}
	
	public void build(Color colorWalls, Color colorRoof){
		this.colorWalls = colorWalls;
		this.colorRoof = colorRoof;
		initialiseMaterial();
		
		buildAll(foundation);
		buildAll(wallSiding);
		buildAll(roofTiles);
		buildItem(door);
		buildAll(windows);
		buildItem(forSaleSign);
	}
	
	public Void buildTask(Color colorWalls, Color colorRoof){
		this.colorWalls = colorWalls;
		this.colorRoof = colorRoof;
		initialiseMaterial();
		
		@Future(taskType=TaskInfoType.MULTI)
		Void foundationTask = buildAll(foundation);
		
		@Future(taskType=TaskInfoType.MULTI, depends="foundationTask")
		Void wallTask = buildAll(wallSiding);

		@Future(taskType=TaskInfoType.MULTI, depends="wallTask")
		Void roofTask = buildAll(roofTiles);
		
		@Future(taskType=TaskInfoType.ONEOFF, depends="wallTask")
		Void doorTask = buildItem(door);
		
		@Future(taskType=TaskInfoType.MULTI, depends="wallTask")
		Void windowsTask = buildAll(windows);
		
		@Future(depends="roofTask, doorTask, windowTask")
		Void[] signTask = new Void[1];
		signTask[0] = buildItem(forSaleSign);
		
		Void var = signTask[0];	
		return var;
	}

	private Void buildItem(BuildingMaterial item){
		if (!item.getAndSetVisible(true)){
			simulateWork(N);
			repaint();
		}
		return null;
	}
	
	private Void buildAll(List<BuildingMaterial> items){
		for(BuildingMaterial item : items)
			buildItem(item);
		return null;
	}
	
	public void update(Graphics g){
		paint(g);
	}
	
	public void paint(Graphics g){
		
		if (clearScreen){
			g.clearRect(0,  0,  width, height);
			clearScreen = false;
		}
		
		//draw foundation
		for(BuildingMaterial b : foundation){
			if(b.isVisible()){
				g.setColor(new Color(162, 158, 24));
				g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
				g.setColor(Color.BLACK);
				g.drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
			}
		}
		
		//draw wall
		for(BuildingMaterial b : wallSiding){
			if(b.isVisible()){
				g.setColor(colorWalls);
				g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
				g.setColor(Color.BLACK);
				g.drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
			}
		}
		
		//draw roof
		for(BuildingMaterial b : roofTiles){
			if(b.isVisible()){
				g.setColor(colorRoof);
				g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
				g.setColor(Color.BLACK);
				g.drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
			}
		}
		
		//draw door
		if (door.isVisible()){
			g.setColor(new Color(152, 118, 84));
			g.fillRect(door.getX(), door.getY(), door.getWidth(), door.getHeight());
			g.setColor(Color.BLACK);
			g.drawRect(door.getX(), door.getY(), door.getWidth(), door.getHeight());
		}
		
		//draw windows
		for (BuildingMaterial b : windows){
			if(b.isVisible()){
				g.setColor(new Color(173, 216, 230));
				g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
				g.setColor(Color.BLACK);
				g.drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());
			}
		}
		
		//draw sign
		if (forSaleSign.isVisible()){
			g.setColor(new Color(152, 118, 84));
			g.fillRect(400, 260, 15, 20);
			g.fillRect(445, 260, 15, 20);
			g.setColor(Color.BLACK);
			g.drawRect(400, 260, 15, 20);
			g.drawRect(445, 260, 15, 20);
			
			g.setColor(new Color(225, 239, 0));
			g.fillRect(forSaleSign.getX(), forSaleSign.getY(), forSaleSign.getWidth(), forSaleSign.getHeight());
			g.setColor(Color.BLACK);
			g.drawRect(forSaleSign.getX(), forSaleSign.getY(), forSaleSign.getWidth(), forSaleSign.getHeight());
			
			Font f = g.getFont();
			g.setFont(new Font(f.getName(), Font.BOLD, 21));
			g.drawString("For", forSaleSign.getX()+30, forSaleSign.getY()+30);
			g.drawString("Sale", forSaleSign.getX()+25, forSaleSign.getY()+60);
		}
	}

	public void initialiseMaterial(){
		this.foundation = new CopyOnWriteArrayList<>();
		this.wallSiding = new CopyOnWriteArrayList<>();
		this.roofTiles = new CopyOnWriteArrayList<>();
		this.windows = new CopyOnWriteArrayList<>();
		
		initFoundation();
		initWallSiding();
		initRoofTiles();
		initWindowsAndDoor();
		initForSaleSign();
	}
	
	private void initFoundation(){

		int numCols = 31;
		int w = 15;
		int h = 10;
		
		int x = 20;
		int y = 280;
		for (int i = 0; i < numCols; i++)
			foundation.add(new BuildingMaterial(x+i*w, y, w, h, false));
		
		x -= 7;
		y += h;
		for (int i = 0; i < numCols; i++)
			foundation.add(new BuildingMaterial(x+i*w, y, w, h, false));
		
		x += 7;
		y += h;
		for (int i = 0; i < numCols; i++)
			foundation.add(new BuildingMaterial(x+i*w, y, w, h, false));
		
		List<BuildingMaterial> list = new ArrayList<>();
		list.addAll(foundation);
		Collections.shuffle(list);
		foundation.clear();
		foundation.addAll(list);
	}
	
	private void initWallSiding(){
		int numPlanks = 40;
		int totalW = 320;
		int w = totalW / numPlanks;
		int h = 150;
		
		int x = 40;
		int y = 130;
		for (int i = 0; i < numPlanks; i++)
			wallSiding.add(new BuildingMaterial(x+i*w, y, w, h, false));
		
		List<BuildingMaterial> list = new ArrayList<>();
		list.addAll(wallSiding);
		Collections.shuffle(list);
		wallSiding.clear();
		wallSiding.addAll(list);
	}

	private void initRoofTiles(){
		int numPlanks = 12;
		int dec = 10;
		int h = 10;
		int w = 360;
		
		int x = 20;
		int y = 130-h;
		for (int i = 0; i < numPlanks; i++){
			roofTiles.add(new BuildingMaterial(x+i*dec, y-i*h, w, h, false));
			w-=2*dec;
		}
		
		List<BuildingMaterial> list = new ArrayList<>();
		list.addAll(roofTiles);
		Collections.shuffle(list);
		roofTiles.clear();
		roofTiles.addAll(list);
	}

	private void initWindowsAndDoor(){
		door = new BuildingMaterial(63, 160, 75, 120, false);
		windows.add(new BuildingMaterial(160, 160, 80, 60, false));
		windows.add(new BuildingMaterial(253, 160, 75, 60, false));
	}

	private void initForSaleSign(){
		forSaleSign = new BuildingMaterial(380, 180, 100, 80, false);
	}

	public void setCompletionLevel(int N){
		this.N = N;
	}

	public void reset(){
		clearScreen = true;
		for (BuildingMaterial b : wallSiding)
			b.getAndSetVisible(false);
		for (BuildingMaterial b : foundation)
			b.getAndSetVisible(false);
		for (BuildingMaterial b : roofTiles)
			b.getAndSetVisible(false);
		for (BuildingMaterial b : windows)
			b.getAndSetVisible(false);
		door.getAndSetVisible(false);
		forSaleSign.getAndSetVisible(false);
		repaint();
	}

	private void simulateWork(int N){
		double xmin = -1.0;
		double ymin = -1.0;
		double width = 2.0;
		double height = 2.0;
		for (int i = 0; i < N; i++){
			for (int j = 0; j < N; j++){
				double x = xmin + i * width/N;
				double y = ymin + j * height/N;
				Complex z = new Complex(x, y);
				newton(z);
			}
		}
	}

	private Color newton(Complex z){
		double EPSILON	= 0.00000001;
		Complex four = new Complex(4, 0);
		Complex one = new Complex(1, 0);
		Complex root1 = new Complex(1, 0);
		Complex root2 = new Complex(-1, 0);
		Complex root3 = new Complex(0, 1);
		Complex root4 = new Complex(0, -1);
		for (int i = 0; i < 100; i++){
			Complex f = z.times(z).times(z).times(z).minus(one);
			Complex fp = four.times(z).times(z).times(z);
			z = z.minus(f.divides(fp));
			if (z.minus(root1).abs() <= EPSILON) return Color.WHITE;
			if (z.minus(root2).abs() <= EPSILON) return Color.RED;
			if (z.minus(root3).abs() <= EPSILON) return Color.GREEN;
			if (z.minus(root4).abs() <= EPSILON) return Color.BLUE;
		}
		return Color.BLACK;
	}
}