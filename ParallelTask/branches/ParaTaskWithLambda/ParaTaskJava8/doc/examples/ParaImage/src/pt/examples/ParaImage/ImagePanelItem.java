package pt.examples.ParaImage;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ButtonModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import pt.runtime.*;

public class ImagePanelItem extends JPanel implements ChangeListener {
	
	private File file;
	private ImageProjectPanel parentPanel;
	private Image imageSquare;
	private Image imageSquareOriginal;
	private Image imageOriginal;
	private Image imageLarge;
	private Image imageMediumOriginal;
	private Image imageMedium;

	private ArrayList<TaskID> history = new ArrayList<TaskID>();
//	private TaskIDGroup<Image> history = new TaskIDGroup<Image>();
	
	public static int imageSize = 180;
	
	private JCheckBox checkBox = new JCheckBox();
	private JLabel lblImage;
	
	private static int height = 250;
	
	public TaskIDGroup<Image> getHistory() {
		int size = history.size();
		TaskIDGroup<Image> grp = new TaskIDGroup<Image>(size);
		for (int i = 0; i < size; i++)
			grp.add(history.get(i));
		return grp;
	}
	
	public void addToHistory(TaskID job) {
		history.add(job);
	}
	
	private boolean isModified = false;
	
	public void restore() {
		imageLarge = imageOriginal;
		imageMedium = imageMediumOriginal;
		imageSquare = imageSquareOriginal; 
		updateImageLabel();
		isModified = false;
	}
	
	public void commit() {
		imageOriginal = imageLarge;
		imageMediumOriginal = imageMedium;
		imageSquareOriginal = imageSquare;
		isModified = false;
		updateBorder();
	}

	public boolean isModified() {
		return isModified;
	}
	
	public boolean isSelected() {
		return checkBox.isSelected();
	}

	public void setImageTask(TaskID<ImageCombo> id) {
		try {
			setImage(id.getReturnResult());
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	public void setImage(ImageCombo combo) {
		this.imageLarge = combo.getImageLarge();
        imageSquare = combo.getImageSquare();
        imageMedium = combo.getImageMedium();
		isModified = true;
        updateImageLabel();
        updateBorder();
	}
	
	private void updateBorder() {
		Border raisedBorder = BorderFactory.createRaisedBevelBorder(); 
		Border loweredBorder = BorderFactory.createLoweredBevelBorder();
		Border redBorder = BorderFactory.createLineBorder(Color.RED);
		Border border = BorderFactory.createCompoundBorder(raisedBorder, loweredBorder);
		Border borderMod = BorderFactory.createCompoundBorder(border, redBorder);
		
		if (isSelected() && !isModified())
			lblImage.setBorder(border);
		if (isModified() && isSelected())
			lblImage.setBorder(borderMod);
		if (!isSelected() && isModified())
			lblImage.setBorder(redBorder);
		if (!isSelected() && !isModified())
			lblImage.setBorder(null);
			
		updateUI();
	}
	
	private void updateImageLabel() {
		if (lblImage != null)
			remove(lblImage);
		
//		System.out.println("1");
		lblImage = new JLabel(new ImageIcon(imageSquare));
//		System.out.println("1");
		lblImage.setEnabled(checkBox.isSelected());
		
		add(lblImage);
		Dimension size = lblImage.getPreferredSize();
		lblImage.setBounds(50, 20, size.width, size.height);
		lblImage.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				setSelected(!isSelected());
			}
			@Override
			public void mouseEntered(MouseEvent e) {
				lblImage.setEnabled(true);
			}
			@Override
			public void mouseExited(MouseEvent e) {
				lblImage.setEnabled(isSelected());
			}
		});
	}
	
	public void setSelected(boolean selected) {
		checkBox.setSelected(selected);
	}

	public static int dialogWidth = 800;
	public static int dialogHeight = 600;
	
	public ImagePanelItem(final File file, Image large, Image square, Image medium, final ImageProjectPanel parentPanel) {
		this.file = file;
		this.imageLarge = large;
		this.imageOriginal = large;
		this.imageMedium = medium;
		this.imageMediumOriginal = medium;
		this.imageSquare = square;
		this.imageSquareOriginal = square;
		this.parentPanel = parentPanel;
//		try {
//			imageLarge = ImageIO.read(file);
//	        imageOriginal = imageLarge;
//	        imageSquareOriginal = imageLarge.getScaledInstance(imageSize, imageSize, Image.SCALE_SMOOTH);
//	        imageSquare = imageSquareOriginal;
//		} catch (IOException e) {
//			e.printStackTrace();
//		}

		setLayout(null);
		updateImageLabel();

		int nameLimit = 20;
		String name = file.getName();
		if (name.length() > nameLimit) 
			name = name.substring(0,nameLimit)+"...";
		
//		JLabel lblTitle = new JLabel(name);
//		add(lblTitle);
//		Dimension size = lblTitle.getPreferredSize();
//		lblTitle.setBounds(50, imageSize+35, size.width, size.height);
		
		JButton btnOpen = new JButton(name);
		btnOpen.setIcon(new ImageIcon(Utils.getImg("viewmag.png")));
		add(btnOpen);
		btnOpen.setToolTipText("View full size");
		Dimension size = btnOpen.getPreferredSize();
		btnOpen.setBounds(50, imageSize+20, imageSize, size.height);
		btnOpen.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JLabel label = new JLabel(new ImageIcon(imageMedium));
	            JOptionPane.showConfirmDialog(ImagePanelItem.this.parentPanel.mainFrame, label, 
	            		file.getName(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
			}
		});
		
		add(checkBox);
		size = checkBox.getPreferredSize();
		checkBox.addChangeListener(this);
		checkBox.setBounds(15, 100, size.width, size.height);
		
		setPreferredSize(new Dimension(height, height));
	}
	
	public Image getImageSquare() {
		return imageSquare;
	}

	public Image getImageMedium() {
		return imageMedium;
	}
	
	public Image getImageLarge() {
		return imageLarge;
//		try {
//			return ImageIO.read(file);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//		return null;
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		lblImage.setEnabled(isSelected());
		
		AbstractButton abstractButton = (AbstractButton)e.getSource();
        ButtonModel buttonModel = abstractButton.getModel();
        if (buttonModel.isRollover())
        	lblImage.setEnabled(true);
		
		parentPanel.updateActions();
		updateBorder();
	}

}