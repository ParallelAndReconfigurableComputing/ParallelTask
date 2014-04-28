package pt.examples.ParaImage;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.Cursor;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.ExecutionException;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.UIManager;

import pt.examples.ParaImage.flickr.Search;
import pt.runtime.Future;
import static pt.runtime.Task.*;

import com.aetrion.flickr.photos.Photo;

public class PhotoPanelItem extends JPanel implements ActionListener {
	
	private Photo photo;
	private Image imageSquare;
	private Image imageLarge;
	private File preferredDir;
	
	private String defaultName = "unnamed";

	private JButton btnDownload = new JButton(new ImageIcon(Utils.getImg("download.png")));
	private JButton btnView = new JButton(new ImageIcon(Utils.getImg("openfull.png")));
	private JButton btnSave = new JButton(new ImageIcon(Utils.getImg("save2.png")));
//	private JButton btnDownload = new JButton("Download");
//	private JButton btnView = new JButton("View");
//	private JButton btnSave = new JButton("Save");
	
	private static int height = 100;
	
	public PhotoPanelItem(Photo photo, Image imageSquare, File preferredDir) {
		this.photo = photo;
		this.imageSquare = imageSquare;
		this.preferredDir = preferredDir;
		
		setLayout(null);
		
		JLabel lblImage = new JLabel(new ImageIcon(imageSquare));
		add(lblImage);
		
		Dimension size = lblImage.getPreferredSize();
		lblImage.setBounds(40, 20, size.width, size.height);
		
		String photoTitle = photo.getTitle();
		if (photoTitle.equals(""))
			photoTitle = defaultName;
		
		JLabel lblTitle = new JLabel(photoTitle);
		add(lblTitle);
		size = lblTitle.getPreferredSize();
		lblTitle.setBounds(150, 20, size.width, size.height);

		JPanel pnlButtons = new JPanel();
//		JPanel pnlButtons = new JPanel(new GridLayout(3,1));
		pnlButtons.add(btnDownload);
		btnDownload.setToolTipText("Retrieve full size");
		pnlButtons.add(btnView);
		btnView.setToolTipText("View full size");
		pnlButtons.add(btnSave);
		btnSave.setToolTipText("Save to file");
		btnView.setEnabled(false);
		btnSave.setEnabled(false);
		btnDownload.addActionListener(this);
		btnView.addActionListener(this);
		btnSave.addActionListener(this);
		
		Dimension btnSize = new Dimension(45,45);
		btnDownload.setPreferredSize(btnSize);
		btnView.setPreferredSize(btnSize);
		btnSave.setPreferredSize(btnSize);
		
		add(pnlButtons);
		size = pnlButtons.getPreferredSize();
		pnlButtons.setBounds(580, 20, size.width, size.height);
		
		setPreferredSize(new Dimension(MainFrame.width-100, height));
	}
	
	public Photo getPhoto() {
		return photo;
	}
	
	public Image getSquarePhoto() {
		return imageSquare;
	}

	private void downloadCompleteTask(Future<Image> id) {
		try {
			downloadComplete(id.getReturnResult());
		} catch (ExecutionException e) {
			e.printStackTrace();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private void downloadComplete(Image image) {
		imageLarge = image;
		btnView.setEnabled(true);
		btnSave.setEnabled(true);
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == btnDownload) {
			btnDownload.setEnabled(false);
			if (MainFrame.isParallel) {
				//TaskID<Image> id = Search.getMediumImageTask(photo) notify(downloadCompleteTask(TaskID));
				Future<Image> id = asIOTask(() -> Search.getMediumImageTask(photo))
						.withHandler(future -> downloadCompleteTask((Future<Image>)future))
						.run();
			} else {
        		setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				Image result = Search.getMediumImage(photo);
				downloadComplete(result);
            	setCursor(Cursor.getDefaultCursor());
			}
			
		} else if (e.getSource() == btnView) {
			JLabel label = new JLabel(new ImageIcon(imageLarge));
			JOptionPane.showConfirmDialog(this, label, photo.getTitle(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE); 
		} else if (e.getSource() == btnSave) {
			UIManager.put("FileChooser.readOnly", Boolean.TRUE);
			JFileChooser fc = new JFileChooser(preferredDir);
			String fileName = photo.getTitle();
			if (fileName.equals(""))
				fileName = defaultName;
			fileName+=".jpeg";
			fc.setSelectedFile(new File(preferredDir, fileName));
			int returnVal = fc.showSaveDialog(this);
			if (returnVal == JFileChooser.APPROVE_OPTION) {
				File file = fc.getSelectedFile();
				File outputFile = new File(file.getParent(), file.getName());
				
				if (outputFile.exists()) {
					JOptionPane.showConfirmDialog(this, "File already exists, please select another name.", "File exists.", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);
				} else {
					RenderedImage rendered = null;
			        if (imageLarge instanceof RenderedImage) {
			            rendered = (RenderedImage) imageLarge;
			        } else {
			            BufferedImage buffered = new BufferedImage(imageLarge.getWidth(null), imageLarge.getHeight(null), BufferedImage.TYPE_INT_RGB);
			            Graphics2D g = buffered.createGraphics();
			            g.drawImage(imageLarge, 0, 0, null);
			            g.dispose();
			            rendered = buffered;
			        }
			        try {
						ImageIO.write(rendered, "JPEG", outputFile);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		}
	}
}
