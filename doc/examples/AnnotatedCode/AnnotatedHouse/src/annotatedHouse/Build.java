package annotatedHouse;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

import pt.runtime.ParaTask;
import pt.runtime.ParaTask.PTSchedulingType;
import sp.annotations.Future;
import sp.annotations.TaskInfoType;


public class Build extends JFrame implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String sequential = "Uh oh! this is sequential (click to change)";
	private String concurrent = "Better... This is concurrent (click to change)";
	private String parallel = "Great! This is parallel (click to change)";
	private String currentJob;
	
	private boolean isParallel = false;
	private long startTime;
	
	private JButton btnBuild;
	private JButton btnCancel;
	private JButton btnParSeq;
	
	
	private JRadioButtonMenuItem level_1 = new JRadioButtonMenuItem("Low");
	private JRadioButtonMenuItem level_2 = new JRadioButtonMenuItem("Medium");
	private JRadioButtonMenuItem level_3 = new JRadioButtonMenuItem("High");
	
	private JButton btnRoof;
	private JButton btnWalls;
	private Color colorRoof = Color.BLUE;
	private Color colorWalls = Color.YELLOW;
	
	private House houseApplet;
	
	public Build(String name){
		super(name);
		createGUI();
	}
	
	private void finishedBuilding(){
		btnBuild.setEnabled(true);
		long time = System.currentTimeMillis() - startTime;
		System.out.println(currentJob + " time: " + time+ " ms");
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if(level_1.isSelected())
			houseApplet.setCompletionLevel(10);
		else if (level_2.isSelected())
			houseApplet.setCompletionLevel(28);
		else if (level_3.isSelected())
			houseApplet.setCompletionLevel(50);
		
		btnBuild.setEnabled(false);
		houseApplet.reset();
		startTime = System.currentTimeMillis();
		
		if (btnParSeq.getText().equals(sequential)){
			currentJob = "Sequential";
			houseApplet.build(colorWalls, colorRoof);
			finishedBuilding();
		}
		else if (btnParSeq.getText().equals(concurrent)){
			currentJob = "Concurrent";
			
			@Future(taskType=TaskInfoType.ONEOFF, notifies="finishedBuilding()")
			Void task = houseApplet.buildSingleTask(colorWalls, colorRoof);
		}
		else{
			currentJob = "Parallel";
			ParaTask.setSchedulingType(PTSchedulingType.WorkSharing);
			
			@Future(notifies="finishedBuilding()")
			Void task = houseApplet.buildTask(colorWalls, colorRoof);
		}
		//btnBuild.setEnabled(true);
	}
	
	private void createGUI(){
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu("Computation Level");
		
		ButtonGroup levelGroup = new ButtonGroup();
		level_2.setSelected(true);
		levelGroup.add(level_1);
		levelGroup.add(level_2);
		levelGroup.add(level_3);
		
		menu.add(level_1);
		menu.add(level_2);
		menu.add(level_3);
		menuBar.add(menu);
		
		setJMenuBar(menuBar);
		
		int width = 200;
		int height = 80;
		
		btnRoof = new JButton("Select...");
		btnRoof.addActionListener((ActionEvent e)->{
			Color c = JColorChooser.showDialog(Build.this, "Select a colour for the roof", colorRoof);
			if (c != null){
				colorRoof = c;
				btnRoof.setBackground(colorRoof);
			}
		});
		
		btnWalls = new JButton("Select...");
		btnWalls.addActionListener((ActionEvent e)->{
			Color c = JColorChooser.showDialog(Build.this, "Select a colour for the walls", colorWalls);
			if (c != null){
				colorWalls = c;
				btnWalls.setBackground(colorWalls);
			}
		});
		
		btnBuild = new JButton("Build!");
		btnBuild.setPreferredSize(new Dimension(width, height));
		btnBuild.addActionListener(this);
		
		btnCancel = new JButton("Clear");
		btnCancel.setPreferredSize(new Dimension(width, height));
		btnCancel.addActionListener((ActionEvent e)->houseApplet.reset());
		
		JPanel panelRoof = new JPanel(new GridLayout(1,2));
		panelRoof.add(new JLabel("Roof color:", JLabel.CENTER));
		btnRoof.setBackground(colorRoof);
		panelRoof.add(btnRoof);
		panelRoof.setPreferredSize(new Dimension(width, height));
		
		JPanel panelWalls = new JPanel(new GridLayout(1, 2));
		panelWalls.add(new JLabel("Wall color:", JLabel.CENTER));
		btnWalls.setBackground(colorWalls);
		panelWalls.add(btnWalls);
		panelWalls.setPreferredSize(new Dimension(width, height));
		
		JPanel panelLeft = new JPanel();
		panelLeft.setLayout(new GridLayout(4, 1));
		panelLeft.add(panelRoof);
		panelLeft.add(panelWalls);
		panelLeft.add(btnBuild);
		panelLeft.add(btnCancel);
		
		getContentPane().add(panelLeft, BorderLayout.WEST);
		
		//Bottom Menu
		if (isParallel){
			btnParSeq = new JButton("switchToSequential");
			btnParSeq.setBackground(new Color(0, 153, 0));
		}else{
			btnParSeq = new JButton(sequential);
			btnParSeq.setBackground(Color.RED);
		}
		
		Font f = btnParSeq.getFont();
		btnParSeq.setFont(new Font(f.getName(), Font.BOLD, 25));
		btnParSeq.addActionListener((ActionEvent e)->{
			isParallel = !isParallel;
			if(btnParSeq.getText().equals(sequential)){
				btnParSeq.setText(concurrent);
				btnParSeq.setBackground(Color.ORANGE);
			}else if (btnParSeq.getText().equals(concurrent)){
				btnParSeq.setText(parallel);
				btnParSeq.setBackground(Color.GREEN);
			}else{
				btnParSeq.setText(sequential);
				btnParSeq.setBackground(Color.RED);
			}
		});
		
		btnParSeq.setPreferredSize(new Dimension(width, height));
		getContentPane().add(btnParSeq, BorderLayout.SOUTH);
		
		//position the fram
		Toolkit tk = Toolkit.getDefaultToolkit();
		Dimension screenSize = tk.getScreenSize();
		int screenWidth = (int)screenSize.getWidth();
		int screenHeight = (int)screenSize.getHeight();
		setLocation(screenWidth/4, screenHeight/4);
		
		houseApplet = new House(this);
		getContentPane().add(houseApplet, BorderLayout.CENTER);
	}
	
	public static void main(String[] args){
		ParaTask.init();
		Build app = new Build("Build a house");
		app.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		app.setResizable(false);
		app.pack();
		app.setVisible(true);
	}
}
