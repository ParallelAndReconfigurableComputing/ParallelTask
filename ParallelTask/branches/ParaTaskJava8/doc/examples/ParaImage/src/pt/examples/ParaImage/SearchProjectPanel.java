package pt.examples.ParaImage;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusAdapter;
import java.awt.event.FocusEvent;
import java.awt.event.KeyAdapter;

import javax.swing.JOptionPane;

import java.awt.event.KeyEvent;
import java.util.concurrent.ExecutionException;
import java.util.*;

import pt.examples.ParaImage.flickr.PhotoWithImage;
import pt.runtime.TaskID;
import static pt.runtime.Task.*;

import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JProgressBar;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import com.aetrion.flickr.photos.Photo;
import com.aetrion.flickr.photos.PhotoList;

import pt.examples.ParaImage.flickr.Search;
import pt.runtime.TaskID;

public class SearchProjectPanel extends ProjectPanel implements ActionListener {
	
	private int currentOffset = 1;

	private String tempTxt = "[enter search criteria here]";
	private JTextField txtSearch = new JTextField("", 18);
	private JButton btnSearch = new JButton(new ImageIcon(Utils.getImg("search.png")));
	private JButton btnStop = new JButton(new ImageIcon(Utils.getImg("stop.png")));
	private JSpinner spnResultsPerPage;
	private JLabel lblResPP = new JLabel("#pics");
//	private JSpinner spnPageOffset;
	private JButton btnNext = new JButton(new ImageIcon(Utils.getImg("right.png")));
	private JButton btnPrev = new JButton(new ImageIcon(Utils.getImg("left.png")));
	private JTextField txtCurrentPage = new JTextField("-",7);
	
    private JProgressBar progressBar = new JProgressBar(0,100);
	private JPanel thumbnailsPanel;
	private Font userFont;
	private Font emptyFont;
	
	private boolean searchFieldInvalid() {
		return txtSearch.getText().trim().equals("") || txtSearch.getText().equals(tempTxt);
	}
	
	public SearchProjectPanel(MainFrame mainFrame, String projectName) {
		super(mainFrame, projectName);
		Dimension sizeBtns = new Dimension(35,35);
		
		setLayout(new BorderLayout());
		btnSearch.addActionListener(this);
		btnStop.addActionListener(this);
		btnSearch.setPreferredSize(sizeBtns);
		btnSearch.setToolTipText("Search on Flickr");
		
		userFont = txtSearch.getFont();
		emptyFont = new Font(txtSearch.getFont().getName(),Font.ITALIC,txtSearch.getFont().getSize());
		 
		txtSearch.setMargin(new Insets(0, 5, 0, 5));   
		txtSearch.setText(tempTxt);
		btnSearch.setEnabled(false);
		txtSearch.setFont(emptyFont); 
		txtSearch.setForeground(Color.GRAY);
		
		JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));
		//panelSearch.add(new JLabel(new ImageIcon(Utils.getImagePath("flickr.jpg")));
		panelSearch.add(txtSearch);
		
		progressBar.setStringPainted(true);
		
		txtSearch.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER && !searchFieldInvalid()) {
					clearResults();
					disableButtons();
					currentOffset = 1;
					search();
				}
			}
		});
		
		
		txtSearch.getDocument().addDocumentListener(new DocumentListener() {
			@Override
			public void changedUpdate(DocumentEvent e) {
			}
			@Override
			public void insertUpdate(DocumentEvent e) {
				btnSearch.setEnabled(!searchFieldInvalid());
			}
			@Override
			public void removeUpdate(DocumentEvent e) {
				btnSearch.setEnabled(!searchFieldInvalid());
			}
		});
		txtSearch.addFocusListener(new FocusAdapter() {
			@Override
			public void focusGained(FocusEvent e) {
				txtSearch.setForeground(Color.BLACK);
				txtSearch.setFont(userFont);
				if (searchFieldInvalid()) {
					txtSearch.setText("");
				} 
			}
			@Override
			public void focusLost(FocusEvent e) {
				if (searchFieldInvalid()) {
					txtSearch.setForeground(Color.GRAY);
					txtSearch.setFont(emptyFont);
					txtSearch.setText(tempTxt);
				} else {
					txtSearch.setForeground(Color.BLACK);
					txtSearch.setFont(userFont);
				}
			}
		});

		txtSearch.setPreferredSize(new Dimension(txtSearch.getPreferredSize().width, sizeBtns.height));
		txtSearch.setToolTipText("Enter search criteria here");
		panelSearch.add(btnSearch);
        btnStop.setPreferredSize(sizeBtns);
        btnStop.setToolTipText("Cancel search");
        btnStop.setEnabled(false);
		panelSearch.add(btnStop);
        progressBar.setPreferredSize(new Dimension(100, sizeBtns.height));
		panelSearch.add(progressBar);
		
		JLabel space = new JLabel();
		space.setPreferredSize(new Dimension(15, sizeBtns.height));
		panelSearch.add(space);
		
		panelSearch.add(lblResPP);
		lblResPP.setToolTipText("Number of photos returned per page");
		SpinnerNumberModel spnModel = new SpinnerNumberModel(8,1,99,1);
		spnResultsPerPage = new JSpinner(spnModel);
		spnResultsPerPage.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				btnPrev.setEnabled(false);
				btnNext.setEnabled(false);
			}
		});
		spnResultsPerPage.setPreferredSize(new Dimension(spnResultsPerPage.getPreferredSize().width, sizeBtns.height));
		spnResultsPerPage.setToolTipText("Number of photos returned per page");
		panelSearch.add(spnResultsPerPage);
		
		panelSearch.add(new JSeparator());
		panelSearch.add(new JSeparator());

		btnPrev.addActionListener(this);
		btnNext.setToolTipText("View next page of results");
		btnPrev.setToolTipText("View previous page of results");
		btnNext.addActionListener(this);
		btnPrev.setEnabled(false);
		btnNext.setEnabled(false);
		panelSearch.add(btnPrev);
		txtCurrentPage.setHorizontalAlignment(JTextField.CENTER);
		txtCurrentPage.setEditable(false);
		txtCurrentPage.setToolTipText("Current page of results");
		panelSearch.add(txtCurrentPage);
		txtCurrentPage.setPreferredSize(new Dimension(txtCurrentPage.getPreferredSize().width, sizeBtns.height));
		panelSearch.add(btnNext);
		btnPrev.setPreferredSize(sizeBtns);
		btnNext.setPreferredSize(sizeBtns);
		
		
		add(panelSearch, BorderLayout.NORTH);
		
		thumbnailsPanel = new JPanel();
		thumbnailsPanel.setLayout(new BoxLayout(thumbnailsPanel, BoxLayout.Y_AXIS));
		JScrollPane scroll = new JScrollPane(thumbnailsPanel);
		thumbnailsPanel.setVisible(true);
		scroll.setVisible(true);
		add(scroll, BorderLayout.CENTER);
	}
	
	private void clearResults() {
    	progressBar.setValue(0);
        thumbnailsPanel.removeAll();
        thumbnailsPanel.updateUI();
	}
	/*
    private void displayResultsTask(TaskID<List<PhotoWithImage>> id) {
        try {
            displayResults(id.getReturnResult());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
    */

	private void finishedSearch() {
        txtCurrentPage.setText("page " + (currentOffset));
        thumbnailsPanel.updateUI();
        isModified = true;
        mainFrame.updateTabIcons();
        enableButtons();
        currentSearch = null;
	}
	
 //   private void displayResults(List<PhotoWithImage> list) {
 //       for (PhotoWithImage pi : list) {
  //      	addToDisplay(pi);
  //      }
  //  }
	
    public void addToDisplay(PhotoWithImage pi) {
    	thumbnailsPanel.add(new PhotoPanelItem(pi.getPhoto(), pi.getImage(), projectDir));
    }
    
	
//	TASK_INTERACTIVE private PhotoList doSearchTask(String search, int resultsPP, int offset) {
//		return doSearch(search, resultsPP, offset);
//	}
	
	
//	private PhotoList doSearch(String search, int resultsPP, int offset) {
//		return Search.search(search, resultsPP, offset);
//	}
	
    private void enableButtons() {
        btnStop.setEnabled(false);
        btnSearch.setEnabled(true);
        lblResPP.setEnabled(true);
        txtSearch.setEnabled(true);
        btnNext.setEnabled(true);
        spnResultsPerPage.setEnabled(true);
        if (currentOffset == 1) 
        	btnPrev.setEnabled(false); 
    	else 
    		btnPrev.setEnabled(true);
    }
    
    // intermediate result
    private void receiveIntermediate(TaskID id, PhotoWithImage pi) {
    	addToDisplay(pi);
        progressBar.setValue(id.getProgress());
        updateUI();
        
   //     System.out.println("Intermediate result: "+pi.getPhoto().getTitle());
    }
    
    
	private void search() {
		String search = txtSearch.getText();
		int resPP = (Integer)spnResultsPerPage.getValue();
		
		if (MainFrame.isParallel) {
//			TaskID<List<PhotoWithImage>> id = Search.searchTask(search, resPP, currentOffset) notify(finishedSearch());
			
			//currentSearch = Search.searchTask(search, resPP, currentOffset) 
			//	notify(finishedSearch())
			//	notifyInterim(receiveIntermediate(TaskID,PhotoWithImage));
			
			currentSearch = asIOTask(() -> Search.searchTask(search, resPP, currentOffset))
					.withHandler(this::finishedSearch)
					.withInterimHandler((future, result) -> receiveIntermediate((TaskID)future, (PhotoWithImage)result))
					.start();
				
		} else {
		
        	setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
        	
			List<PhotoWithImage> results = Search.search(search, resPP, currentOffset);
            //displayResults(results);
            
         	for (PhotoWithImage pi : results) {
        		addToDisplay(pi);
        	}
            
        	progressBar.setValue(100);
            finishedSearch();
            //enableButtons();
            
            setCursor(Cursor.getDefaultCursor());
		}
    }
    
    private void disableButtons() {
        btnStop.setEnabled(true);
        btnSearch.setEnabled(false);
        lblResPP.setEnabled(false);
        txtSearch.setEnabled(false);
        btnNext.setEnabled(false);
        btnPrev.setEnabled(false);
        spnResultsPerPage.setEnabled(false);
    }

    private TaskID<List<PhotoWithImage>> currentSearch = null;
    
    @Override
    public void actionPerformed(ActionEvent e) {
    	if (e.getSource() == btnStop) {
    		if (currentSearch != null) {
    			currentSearch.cancelAttempt();
    		} else {
    			JOptionPane.showMessageDialog(this, "Sorry, cancel currently only works with ParaTask.");
    		}
    	} else {
            clearResults();
            disableButtons();
            if (e.getSource() == btnSearch) {
                currentOffset = 1;
            } else if (e.getSource() == btnPrev) {
                currentOffset--;
            } else if (e.getSource() == btnNext) {
                currentOffset++;
            }
            search();
    	}
    }
}
