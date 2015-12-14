package benchmarks.queens;

import java.awt.Color;
import java.awt.FileDialog;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

public final class Picture implements ActionListener {

	private BufferedImage image;    				//the rasterized image
	private JFrame frame;							//on-screen view
	private String fileName;							//name of file
	private boolean isOriginUpperLeft = true;		//location of Origin
	private final int width, height;
	
	public Picture(int w, int h){
		if (w < 0) throw new IllegalArgumentException("width must be non-negative");
		if (h < 0) throw new IllegalArgumentException("height must be non-negative");
		width = w; height = h;
		image = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
		fileName = w + "-by" + h;
	}
	
	public Picture(Picture pic){
		width = pic.width();
		height = pic.height();
		image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		fileName = pic.fileName();
		for (int x = 0; x < height(); x++){
			for (int y = 0; y < width(); y++){
				image.setRGB(x, y, pic.get(x, y).getRGB());
			}
		}
	}
	
	public Picture(String fileName){
		this.fileName = fileName;
		try{
			File file = new File(fileName);
			if (file.isFile()){
				image = ImageIO.read(file);
			}
			else {
				URL url = getClass().getResource(fileName);
				if (url == null) {url = new URL(fileName);}
				image = ImageIO.read(url);
			}
			width = image.getWidth(null);
			height = image.getHeight(null);
		}catch(IOException e){
			throw new RuntimeException("Could not open the file: " + fileName);
		}
	}
	
	public Picture(File file){
		try{
			image = ImageIO.read(file);
			}catch(IOException e){
				e.printStackTrace();
				throw new RuntimeException("Could not open file: " + file);
			}
		if (image == null){
			throw new RuntimeException("Invalid image file: " + file);
		}
		width = image.getWidth(null);
		height = image.getHeight(null);
		fileName = file.getName();
	}
	
	public JLabel getJLabel(){
		if (image == null){return null;}
		ImageIcon icon = new ImageIcon(image);
		return new JLabel(icon);
	}
	
	public void setOriginUpperLeft(){
		isOriginUpperLeft = true;
	}
	
	public void setOriginLowerLeft(){
		isOriginUpperLeft = false;
	}
	
	public void show(){
		if (frame == null){
			frame = new JFrame();
			JMenuBar menuBar = new JMenuBar();
			JMenu menu = new JMenu("File");
			menuBar.add(menu);
			JMenuItem menuItem1 = new JMenuItem("  Save ...  ");
			menuItem1.addActionListener(this);
			menuItem1.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_S,
								Toolkit.getDefaultToolkit().getMenuShortcutKeyMask()));
			menu.add(menuItem1);
			frame.setJMenuBar(menuBar);
			frame.setContentPane(getJLabel());
			frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
			frame.setTitle(fileName);
			frame.setResizable(false);
			frame.pack();
			frame.setVisible(true);
		}
		frame.repaint();
	}
	
	public Color get(int x, int y){
		if (x < 0 || x >= width())  throw new IndexOutOfBoundsException("x must be between 0 and " + (width() - 1));
		if (y < 0 || y >= height()) throw new IndexOutOfBoundsException("y must be between 0 and " + (height() - 1));
		if (isOriginUpperLeft) return new Color(image.getRGB(x, y));
		else return new Color(image.getRGB(x, height - y - 1));
	}
		
	public void set(int x, int y, Color color){
		if (x < 0 || x >= width())  throw new IndexOutOfBoundsException("x must be between 0 and " + (width() - 1));
		if (y < 0 || y >= height()) throw new IndexOutOfBoundsException("y must be between 0 and " + (height() - 1));
		if (color == null) throw new NullPointerException("cannot set Color to null");
		if (isOriginUpperLeft) image.setRGB(x, y, color.getRGB());
		else  image.setRGB(x, height - y - 1, color.getRGB());
	}
	
	public boolean equals(Object obj){
		if (obj == this) return true;
		if (obj == null) return false;
		if (obj.getClass() != this.getClass()) return false;
		Picture that  = (Picture) obj;
		if (this.width() != that.width()) return false;
		if (this.height() != that.height()) return false;
		for (int x = 0; x < width(); x++)
			for (int y = 0; y < height(); y++)
				if (!this.get(x, y).equals(that.get(x, y))) return false;
		return true;
	}
	
	public void save(String name){
		save(new File(name));
	}
	
	public void save(File file){
		this.fileName = file.getName();
		if (frame != null) {frame.setTitle(fileName);}
		String suffix = fileName.substring(fileName.lastIndexOf('.')+1);
		suffix = suffix.toLowerCase();
		if (suffix.equals("jpg") || suffix.equals("png")){
			try{ImageIO.write(image, suffix, file);}
			catch(IOException e){ e.printStackTrace();}
		}else {
			System.out.println("Error: filename must end in .jpg or .png");
		}
	}
	
	public int width(){
		return this.width;
	}
	
	public int height(){
		return this.height;
	}
	
	public String fileName(){
		return this.fileName;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		FileDialog chooser = new FileDialog(frame,
				"Use a .png or .jpg extension", FileDialog.SAVE);
		chooser.setVisible(true);
		if (chooser.getFile() != null){
			save(chooser.getDirectory() + File.separator + chooser.getFile());
		}		
	}
	
}
