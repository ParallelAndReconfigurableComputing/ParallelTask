package application;//####[1]####
//####[1]####
import java.awt.Dimension;//####[3]####
import java.awt.Graphics2D;//####[4]####
import java.awt.Image;//####[5]####
import java.awt.Cursor;//####[6]####
import java.awt.event.ActionEvent;//####[7]####
import java.awt.event.ActionListener;//####[8]####
import java.awt.image.BufferedImage;//####[9]####
import java.awt.image.RenderedImage;//####[10]####
import java.io.File;//####[11]####
import java.io.IOException;//####[12]####
import java.util.concurrent.ExecutionException;//####[13]####
import javax.imageio.ImageIO;//####[15]####
import javax.swing.ImageIcon;//####[16]####
import javax.swing.JButton;//####[17]####
import javax.swing.JFileChooser;//####[18]####
import javax.swing.JLabel;//####[19]####
import javax.swing.JOptionPane;//####[20]####
import javax.swing.JPanel;//####[21]####
import javax.swing.UIManager;//####[22]####
import paratask.runtime.*;//####[24]####
import application.flickr.Search;//####[26]####
import com.aetrion.flickr.photos.Photo;//####[28]####
//####[28]####
//-- ParaTask related imports//####[28]####
import pt.runtime.*;//####[28]####
import java.util.concurrent.ExecutionException;//####[28]####
import java.util.concurrent.locks.*;//####[28]####
import java.lang.reflect.*;//####[28]####
import pt.runtime.GuiThread;//####[28]####
import java.util.concurrent.BlockingQueue;//####[28]####
import java.util.ArrayList;//####[28]####
import java.util.List;//####[28]####
//####[28]####
public class PhotoPanelItem extends JPanel implements ActionListener {//####[30]####
    static{ParaTask.init();}//####[30]####
    /*  ParaTask helper method to access private/protected slots *///####[30]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[30]####
        if (m.getParameterTypes().length == 0)//####[30]####
            m.invoke(instance);//####[30]####
        else if ((m.getParameterTypes().length == 1))//####[30]####
            m.invoke(instance, arg);//####[30]####
        else //####[30]####
            m.invoke(instance, arg, interResult);//####[30]####
    }//####[30]####
//####[32]####
    private Photo photo;//####[32]####
//####[33]####
    private Image imageSquare;//####[33]####
//####[34]####
    private Image imageLarge;//####[34]####
//####[35]####
    private File preferredDir;//####[35]####
//####[37]####
    private String defaultName = "unnamed";//####[37]####
//####[39]####
    private JButton btnDownload = new JButton(new ImageIcon("images/download.png"));//####[39]####
//####[40]####
    private JButton btnView = new JButton(new ImageIcon("images/openfull.png"));//####[40]####
//####[41]####
    private JButton btnSave = new JButton(new ImageIcon("images/save2.png"));//####[41]####
//####[46]####
    private static int height = 100;//####[46]####
//####[48]####
    public PhotoPanelItem(Photo photo, Image imageSquare, File preferredDir) {//####[48]####
        this.photo = photo;//####[49]####
        this.imageSquare = imageSquare;//####[50]####
        this.preferredDir = preferredDir;//####[51]####
        setLayout(null);//####[53]####
        JLabel lblImage = new JLabel(new ImageIcon(imageSquare));//####[55]####
        add(lblImage);//####[56]####
        Dimension size = lblImage.getPreferredSize();//####[58]####
        lblImage.setBounds(40, 20, size.width, size.height);//####[59]####
        String photoTitle = photo.getTitle();//####[61]####
        if (photoTitle.equals("")) //####[62]####
        photoTitle = defaultName;//####[63]####
        JLabel lblTitle = new JLabel(photoTitle);//####[65]####
        add(lblTitle);//####[66]####
        size = lblTitle.getPreferredSize();//####[67]####
        lblTitle.setBounds(150, 20, size.width, size.height);//####[68]####
        JPanel pnlButtons = new JPanel();//####[70]####
        pnlButtons.add(btnDownload);//####[72]####
        btnDownload.setToolTipText("Retrieve full size");//####[73]####
        pnlButtons.add(btnView);//####[74]####
        btnView.setToolTipText("View full size");//####[75]####
        pnlButtons.add(btnSave);//####[76]####
        btnSave.setToolTipText("Save to file");//####[77]####
        btnView.setEnabled(false);//####[78]####
        btnSave.setEnabled(false);//####[79]####
        btnDownload.addActionListener(this);//####[80]####
        btnView.addActionListener(this);//####[81]####
        btnSave.addActionListener(this);//####[82]####
        Dimension btnSize = new Dimension(45, 45);//####[84]####
        btnDownload.setPreferredSize(btnSize);//####[85]####
        btnView.setPreferredSize(btnSize);//####[86]####
        btnSave.setPreferredSize(btnSize);//####[87]####
        add(pnlButtons);//####[89]####
        size = pnlButtons.getPreferredSize();//####[90]####
        pnlButtons.setBounds(580, 20, size.width, size.height);//####[91]####
        setPreferredSize(new Dimension(MainFrame.width - 100, height));//####[93]####
    }//####[94]####
//####[96]####
    public Photo getPhoto() {//####[96]####
        return photo;//####[97]####
    }//####[98]####
//####[100]####
    public Image getSquarePhoto() {//####[100]####
        return imageSquare;//####[101]####
    }//####[102]####
//####[104]####
    private void downloadCompleteTask(TaskID<Image> id) {//####[104]####
        try {//####[105]####
            downloadComplete(id.getReturnResult());//####[106]####
        } catch (ExecutionException e) {//####[107]####
            e.printStackTrace();//####[108]####
        } catch (InterruptedException e) {//####[109]####
            e.printStackTrace();//####[110]####
        }//####[111]####
    }//####[112]####
//####[114]####
    private void downloadComplete(Image image) {//####[114]####
        imageLarge = image;//####[115]####
        btnView.setEnabled(true);//####[116]####
        btnSave.setEnabled(true);//####[117]####
    }//####[118]####
//####[121]####
    @Override//####[121]####
    public void actionPerformed(ActionEvent e) {//####[121]####
        if (e.getSource() == btnDownload) //####[122]####
        {//####[122]####
            btnDownload.setEnabled(false);//####[123]####
            if (MainFrame.isParallel) //####[124]####
            {//####[124]####
                TaskInfo __pt__id = new TaskInfo();//####[125]####
//####[125]####
                boolean isEDT = GuiThread.isEventDispatchThread();//####[125]####
//####[125]####
//####[125]####
                /*  -- ParaTask notify clause for 'id' -- *///####[125]####
                try {//####[125]####
                    Method __pt__id_slot_0 = null;//####[125]####
                    __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "downloadCompleteTask", new Class[] { TaskID.class });//####[125]####
                    TaskID __pt__id_slot_0_dummy_0 = null;//####[125]####
                    if (false) downloadCompleteTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[125]####
                    __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, this, false));//####[125]####
//####[125]####
                } catch(Exception __pt__e) { //####[125]####
                    System.err.println("Problem registering method in clause:");//####[125]####
                    __pt__e.printStackTrace();//####[125]####
                }//####[125]####
                TaskID<Image> id = Search.getMediumImageTask(photo, __pt__id);//####[125]####
            } else {//####[126]####
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[127]####
                Image result = Search.getMediumImage(photo);//####[128]####
                downloadComplete(result);//####[129]####
                setCursor(Cursor.getDefaultCursor());//####[130]####
            }//####[131]####
        } else if (e.getSource() == btnView) //####[133]####
        {//####[133]####
            JLabel label = new JLabel(new ImageIcon(imageLarge));//####[134]####
            JOptionPane.showConfirmDialog(this, label, photo.getTitle(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);//####[135]####
        } else if (e.getSource() == btnSave) //####[136]####
        {//####[136]####
            UIManager.put("FileChooser.readOnly", Boolean.TRUE);//####[137]####
            JFileChooser fc = new JFileChooser(preferredDir);//####[138]####
            String fileName = photo.getTitle();//####[139]####
            if (fileName.equals("")) //####[140]####
            fileName = defaultName;//####[141]####
            fileName += ".jpeg";//####[142]####
            fc.setSelectedFile(new File(preferredDir, fileName));//####[143]####
            int returnVal = fc.showSaveDialog(this);//####[144]####
            if (returnVal == JFileChooser.APPROVE_OPTION) //####[145]####
            {//####[145]####
                File file = fc.getSelectedFile();//####[146]####
                File outputFile = new File(file.getParent(), file.getName());//####[147]####
                if (outputFile.exists()) //####[149]####
                {//####[149]####
                    JOptionPane.showConfirmDialog(this, "File already exists, please select another name.", "File exists.", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);//####[150]####
                } else {//####[151]####
                    RenderedImage rendered = null;//####[152]####
                    if (imageLarge instanceof RenderedImage) //####[153]####
                    {//####[153]####
                        rendered = (RenderedImage) imageLarge;//####[154]####
                    } else {//####[155]####
                        BufferedImage buffered = new BufferedImage(imageLarge.getWidth(null), imageLarge.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[156]####
                        Graphics2D g = buffered.createGraphics();//####[157]####
                        g.drawImage(imageLarge, 0, 0, null);//####[158]####
                        g.dispose();//####[159]####
                        rendered = buffered;//####[160]####
                    }//####[161]####
                    try {//####[162]####
                        ImageIO.write(rendered, "JPEG", outputFile);//####[163]####
                    } catch (IOException e1) {//####[164]####
                        e1.printStackTrace();//####[165]####
                    }//####[166]####
                }//####[167]####
            }//####[168]####
        }//####[169]####
    }//####[170]####
}//####[170]####
