package pt.examples.ParaImage;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.UIManager;
import javax.swing.SwingUtilities;

import pt.runtime.TaskID;
import pt.runtime.TaskIDGroup;
import static pt.runtime.Task.*;

public class ImageProjectPanel extends ProjectPanel {
	
	private JPanel thumbnailsPanel;
	
    //TASK 
	private void addToThumbnailsPanelTask(final File file, final TaskID<Image> large, final TaskID<Image> square, final TaskID<Image> med) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    addToThumbnailsPanel(file, large.getReturnResult(), square.getReturnResult(), med.getReturnResult());
                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    
    private void addToThumbnailsPanel(File file, Image large, Image square, Image medium) {
        thumbnailsPanel.add(new ImagePanelItem(file, large, square, medium, ImageProjectPanel.this));
        updateUI();
    }
    
    //TASK 
    private void finishedAddingNewPanelItemsTask() {
    	SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				finishedAddingNewPanelItems();
			}
    	});
    }

    private void finishedAddingNewPanelItems() {
        isModified = true;
        updateActions();
        thumbnailsPanel.updateUI();
        mainFrame.updateTabIcons();
        mainFrame.updateProjectActions();
    }

    private Action actionAddImage = new AbstractAction() {

        @Override
        public void actionPerformed(ActionEvent arg0) {
            UIManager.put("FileChooser.readOnly", Boolean.TRUE);
            JFileChooser fc = new JFileChooser(projectDir);
            fc.setMultiSelectionEnabled(true);
            fc.setAcceptAllFileFilterUsed(false);
            fc.addChoosableFileFilter(new ImageFilter());
            int retValue = fc.showOpenDialog(ImageProjectPanel.this);
            if (retValue == JFileChooser.APPROVE_OPTION) {
                File[] inputImages = fc.getSelectedFiles();
                if (MainFrame.isParallel) {
                	TaskIDGroup<?> grp = new TaskIDGroup<>(inputImages.length);
                	for (int i = 0; i < inputImages.length; i++) {
                		final int index = i;
                    	//TaskID<Image> idImage = ImageManipulation.getImageFullTask(inputImages[i]);
                    	TaskID<Image> idImage = asTask(() -> ImageManipulation.getImageFullTask(inputImages[index])).start();
                    	                    	
                    	//TaskID<Image> idMedium = ImageManipulation.getMediumTask(idImage) dependsOn(idImage);
                    	TaskID<Image> idMedium = asTask(() -> ImageManipulation.getMediumTask(idImage))
                    			.dependsOn(idImage).start();
                    	
                    	//TaskID<Image> idSmall = ImageManipulation.getSmallSquareTask(idImage) dependsOn(idImage);
                    	TaskID<Image> idSmall = asTask(() -> ImageManipulation.getSmallSquareTask(idImage))
                    			.dependsOn(idImage).start();
                    	
                    	//TaskID id = addToThumbnailsPanelTask(inputImages[i], idImage, idSmall, idMedium) dependsOn(idSmall, idMedium);
                    	TaskID<Void> id = asTask(() -> addToThumbnailsPanelTask(inputImages[index], idImage, idSmall, idMedium))
                    			.dependsOn(idSmall, idMedium)
                    			.start();
                    	grp.add(id);
                	}
                	
                	//TaskID finalTask = finishedAddingNewPanelItemsTask() dependsOn(grp);
                	TaskID<Void> finalTask = asTask(() -> finishedAddingNewPanelItemsTask()).dependsOn(grp).start();
                } else {
        			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
                	for (int i = 0; i < inputImages.length; i++) {
                    	Image large = ImageManipulation.getImageFull(inputImages[i]);
                    	Image small = ImageManipulation.getSmallSquare(large);
                    	Image medium = ImageManipulation.getMedium(large);
                    	addToThumbnailsPanel(inputImages[i], large, small, medium);
                	}
                    finishedAddingNewPanelItems();
            		setCursor(Cursor.getDefaultCursor());
                }
            }
        }
    };

	
	private Action actionUndo = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Iterator<ImagePanelItem> it = getSelectedPanels().iterator();
			while (it.hasNext()) {
				ImagePanelItem panel = it.next();
				panel.restore();
			}
            updateActions();
		}
	};
	
	
	private Action actionSelectAll = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Component[] comps = thumbnailsPanel.getComponents();
			for (int i = 0; i < comps.length; i++) {
				ImagePanelItem panel = (ImagePanelItem) comps[i];
				panel.setSelected(true);
			}
            updateActions();
		}
	};
	
	private List<ImagePanelItem> getAllPanels() {
		ArrayList<ImagePanelItem> list = new ArrayList<ImagePanelItem>();
		
		if (thumbnailsPanel != null) {
			Component[] comps = thumbnailsPanel.getComponents();
			for (int i = 0; i < comps.length; i++) {
				ImagePanelItem panel = (ImagePanelItem) comps[i];
				list.add(panel);
			}
		}
		
		return list;
	}
	
	private List<ImagePanelItem> getSelectedPanels() {
		ArrayList<ImagePanelItem> list = new ArrayList<ImagePanelItem>();
		
		Component[] comps = thumbnailsPanel.getComponents();
		for (int i = 0; i < comps.length; i++) {
			ImagePanelItem panel = (ImagePanelItem) comps[i];
			if (panel.isSelected())
				list.add(panel);
		}
		return list;
	}
	
	private Action actionSelectNone = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			Component[] comps = thumbnailsPanel.getComponents();
			for (int i = 0; i < comps.length; i++) {
				ImagePanelItem panel = (ImagePanelItem) comps[i];
				panel.setSelected(false);
			}
            updateActions();
		}
	};
	
	private void disableButtons() {
		actionInvert.setEnabled(false);
		actionApplyEdge.setEnabled(false);
		actionBlur.setEnabled(false);
		actionSharpen.setEnabled(false);
	}
	
	private void enableButtons() {
		actionInvert.setEnabled(true);
		actionApplyEdge.setEnabled(true);
		actionBlur.setEnabled(true);
		actionSharpen.setEnabled(true);
	}
	
	private Action actionInvert = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			Iterator<ImagePanelItem> it = getSelectedPanels().iterator();
			
			while (it.hasNext()) {
				ImagePanelItem panel = it.next();

				if (mainFrame.isParallel) {
					// TaskIDGroup = .. 
					//TaskID<ImageCombo> id = ImageManipulation.invertTask(panel) 
					//		notify(panel::setImageTask(TaskID), ImageProjectPanel.this::guiChanged()) 
					//		dependsOn(panel.getHistory());
					
					TaskID<ImageCombo> id = asTask(() -> ImageManipulation.invertTask(panel))
							.withHandler(future -> panel.setImageTask((TaskID<ImageCombo>)future))
							.withHandler(ImageProjectPanel.this::guiChanged)
							.dependsOn(panel.getHistory())
							.start();
					panel.addToHistory(id);
					
					// grp.add(id) ...
				} else {
				
        			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				
					ImageCombo res = ImageManipulation.invert(panel);
					panel.setImage(res);
					guiChanged();
            		setCursor(Cursor.getDefaultCursor());
				}
			}
		}
	};
	
	private void guiChanged() {
		isModified = true;
		updateActions();
        thumbnailsPanel.updateUI();
        mainFrame.updateTabIcons();
        mainFrame.updateProjectActions();
	}
	
	private Action actionBlur = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			Iterator<ImagePanelItem> it = getSelectedPanels().iterator();
			//if (it.hasNext())
				//isModified = true;
			
			while (it.hasNext()) {
				ImagePanelItem panel = it.next();

				if (mainFrame.isParallel) {
					// TaskIDGroup = .. 
					//TaskID<ImageCombo> id = ImageManipulation.blurTask(panel) 
					//		notify(panel::setImageTask(TaskID), ImageProjectPanel.this::guiChanged()) 
					//		dependsOn(panel.getHistory());
					
					TaskID<ImageCombo> id = asTask(() -> ImageManipulation.blurTask(panel))
							.withHandler(future -> panel.setImageTask((TaskID<ImageCombo>)future))
							.withHandler(ImageProjectPanel.this::guiChanged)
							.dependsOn(panel.getHistory())
							.start();
					
					panel.addToHistory(id);
					
					// grp.add(id) ...
				} else {
        			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					ImageCombo res = ImageManipulation.blur(panel);
					panel.setImage(res);
					guiChanged();
            		setCursor(Cursor.getDefaultCursor());
				}
			}
		}
	};
	
	private Action actionSharpen = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			Iterator<ImagePanelItem> it = getSelectedPanels().iterator();
			//if (it.hasNext())
				//isModified = true;
			
			while (it.hasNext()) {
				ImagePanelItem panel = it.next();

				if (mainFrame.isParallel) {
					// TaskIDGroup = .. 
					//TaskID<ImageCombo> id = ImageManipulation.sharpenTask(panel) 
					//		notify(panel::setImageTask(TaskID), ImageProjectPanel.this::guiChanged()) 
					//		dependsOn(panel.getHistory());
					
					TaskID<ImageCombo> id = asTask(() -> ImageManipulation.sharpenTask(panel))
							.withHandler(future -> panel.setImageTask((TaskID<ImageCombo>) future))
							.withHandler(ImageProjectPanel.this::guiChanged)
							.dependsOn(panel.getHistory())
							.start();
					
					panel.addToHistory(id);
					// grp.add(id) ...
				} else {
        			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					ImageCombo res = ImageManipulation.sharpen(panel);
					panel.setImage(res);
					guiChanged();
            		setCursor(Cursor.getDefaultCursor());
				}
			}
		}
	};
	
	private void savePanels(List<ImagePanelItem> list) {
		Iterator<ImagePanelItem> it = list.iterator();
		while (it.hasNext()) {
			ImagePanelItem panel = it.next();
			panel.commit();
		}
		updateActions();
	}
	
	@Override
	public void saveProject() {
		super.saveProject();
		savePanels(getAllPanels());
	}
	
	private Action actionSaveSelected = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent e) {
//			mainFrame.saveCurrentProject();
			savePanels(getSelectedPanels());
		}
	};
	
	private Action actionApplyEdge = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			Iterator<ImagePanelItem> it = getSelectedPanels().iterator();
			//if (it.hasNext())
				//isModified = true;
			
			while (it.hasNext()) {
				ImagePanelItem panel = it.next();

				if (mainFrame.isParallel) {
					// TaskIDGroup = .. 
					//TaskID<ImageCombo> id = ImageManipulation.edgeDetectTask(panel) 
					//		notify(panel::setImageTask(TaskID), ImageProjectPanel.this::guiChanged()) 
					//		dependsOn(panel.getHistory());
					
					TaskID<ImageCombo> id = asTask(() -> ImageManipulation.edgeDetectTask(panel))
							.withHandler(future -> panel.setImageTask((TaskID<ImageCombo>)future))
							.withHandler(ImageProjectPanel.this::guiChanged)
							.dependsOn(panel.getHistory())
							.start();
					
					panel.addToHistory(id);
					// grp.add(id) ...
				} else {
        			setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
					ImageCombo res = ImageManipulation.edgeDetect(panel);
					panel.setImage(res);
					guiChanged();
            		setCursor(Cursor.getDefaultCursor());
				}
			}
		}
	};
	
	private Action actionRemoveImage = new AbstractAction() {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			
			Iterator<ImagePanelItem> it = getSelectedPanels().iterator();
			if (it.hasNext())
				isModified = true;	
			while (it.hasNext()) {
				thumbnailsPanel.remove(it.next());
			}

            updateActions();
            thumbnailsPanel.updateUI();
            mainFrame.updateTabIcons();
            mainFrame.updateProjectActions();
		}
	};
	
	private boolean canUndoSomethingSelected() {
		Iterator<ImagePanelItem> it = getAllPanels().iterator();
		while (it.hasNext()) {
			ImagePanelItem panel = it.next();
			if (panel.isModified() && panel.isSelected())
				return true;
		}
		return false;
	}
	
	public void updateActions() {
		
		boolean empty = true;
		boolean somethingSelected = false;
		boolean allSelected = false;
		
		if (thumbnailsPanel != null) {
			Component[] comps = thumbnailsPanel.getComponents();
			if (comps.length != 0) {
				empty = false;
				somethingSelected = getSelectedPanels().size() > 0;
				allSelected = getSelectedPanels().size() == comps.length;
			} 
		}
		
		if (!empty) {
			actionSelectAll.setEnabled(!allSelected);
			actionRemoveImage.setEnabled(somethingSelected);
			actionSelectNone.setEnabled(somethingSelected);
			actionInvert.setEnabled(somethingSelected);
			actionApplyEdge.setEnabled(somethingSelected);
			actionBlur.setEnabled(somethingSelected);
			actionSharpen.setEnabled(somethingSelected);
		} else {
			actionSelectAll.setEnabled(false);
			actionSelectNone.setEnabled(false);
			actionRemoveImage.setEnabled(false);
			actionInvert.setEnabled(false);
			actionApplyEdge.setEnabled(false);
			actionBlur.setEnabled(false);
			actionSharpen.setEnabled(false);
		}
		actionUndo.setEnabled(canUndoSomethingSelected());
		actionSaveSelected.setEnabled(canUndoSomethingSelected());
	}
	
	public ImageProjectPanel(MainFrame mainFrame, String projectName) {
		super(mainFrame, projectName);
		setLayout(new BorderLayout());
		
		addToolButtonsPanel();
		
		thumbnailsPanel = new JPanel(new GridLayout(0,3));
		JScrollPane scroll = new JScrollPane(thumbnailsPanel);
		thumbnailsPanel.setVisible(true);
		scroll.setVisible(true);
		add(scroll, BorderLayout.CENTER);
	}
	
	private int buttonSize = 80;
	
	private JButton makeButton(String icon, Action action, String tooltip) {
		JButton btn = new JButton(action);
		btn.setToolTipText(tooltip);
		btn.setIcon(new ImageIcon(Utils.getImg(icon)));
		btn.setPreferredSize(new Dimension(buttonSize,buttonSize));
		return btn;
	}
	
	private void addToolButtonsPanel() {
		JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));

		panel.add(makeButton("add.png", actionAddImage, "Add more image(s) to the project"));
		panel.add(makeButton("saveimage.png", actionSaveSelected, "Apply changes to the selected image(s)"));
		panel.add(makeButton("undo.png", actionUndo, "Undo changes to the selected image(s)"));
		panel.add(makeButton("remove.png", actionRemoveImage, "Remove selected image(s) from view"));
		panel.add(makeButton("gradient.png", actionApplyEdge, "Edge detect on the selected image(s)"));
		panel.add(makeButton("video.png", actionInvert, "Invert colors on the selected image(s)"));
		panel.add(makeButton("blur.png", actionBlur, "Blur the selected image(s)"));
		panel.add(makeButton("sharpen.png", actionSharpen, "Sharpen the selected image(s)"));
		
		JPanel grp = new JPanel(new GridLayout(3,1));
		grp.add(new JLabel("Select..",JLabel.CENTER));
		JButton btnAll = new JButton(actionSelectAll);
		btnAll.setText("All");
		btnAll.setToolTipText("Select all image(s)");
		grp.add(btnAll);
		JButton btnNone = new JButton(actionSelectNone);
		btnNone.setText("None");
		btnNone.setToolTipText("Deselect all image(s)");
		grp.add(btnNone);
		grp.setPreferredSize(new Dimension(buttonSize,buttonSize));
		panel.add(grp);
		
		add(panel, BorderLayout.NORTH);
		
		updateActions();
	}
}
