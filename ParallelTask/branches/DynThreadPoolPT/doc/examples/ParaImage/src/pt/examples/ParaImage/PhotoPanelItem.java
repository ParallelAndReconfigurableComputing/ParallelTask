package pt.examples.ParaImage;//####[1]####
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
import pt.examples.ParaImage.flickr.Search;//####[24]####
import com.aetrion.flickr.photos.Photo;//####[26]####
//####[26]####
//-- ParaTask related imports//####[26]####
import pt.runtime.*;//####[26]####
import java.util.concurrent.ExecutionException;//####[26]####
import java.util.concurrent.locks.*;//####[26]####
import java.lang.reflect.*;//####[26]####
import pt.runtime.GuiThread;//####[26]####
import java.util.concurrent.BlockingQueue;//####[26]####
import java.util.ArrayList;//####[26]####
import java.util.List;//####[26]####
//####[26]####
public class PhotoPanelItem extends JPanel implements ActionListener {//####[28]####
    static{ParaTask.init();}//####[28]####
    /*  ParaTask helper method to access private/protected slots *///####[28]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[28]####
        if (m.getParameterTypes().length == 0)//####[28]####
            m.invoke(instance);//####[28]####
        else if ((m.getParameterTypes().length == 1))//####[28]####
            m.invoke(instance, arg);//####[28]####
        else //####[28]####
            m.invoke(instance, arg, interResult);//####[28]####
    }//####[28]####
//####[30]####
    private Photo photo;//####[30]####
//####[31]####
    private Image imageSquare;//####[31]####
//####[32]####
    private Image imageLarge;//####[32]####
//####[33]####
    private File preferredDir;//####[33]####
//####[35]####
    private String defaultName = "unnamed";//####[35]####
//####[37]####
    private JButton btnDownload = new JButton(new ImageIcon(Utils.getImg("download.png")));//####[37]####
//####[38]####
    private JButton btnView = new JButton(new ImageIcon(Utils.getImg("openfull.png")));//####[38]####
//####[39]####
    private JButton btnSave = new JButton(new ImageIcon(Utils.getImg("save2.png")));//####[39]####
//####[44]####
    private static int height = 100;//####[44]####
//####[46]####
    public PhotoPanelItem(Photo photo, Image imageSquare, File preferredDir) {//####[46]####
        this.photo = photo;//####[47]####
        this.imageSquare = imageSquare;//####[48]####
        this.preferredDir = preferredDir;//####[49]####
        setLayout(null);//####[51]####
        JLabel lblImage = new JLabel(new ImageIcon(imageSquare));//####[53]####
        add(lblImage);//####[54]####
        Dimension size = lblImage.getPreferredSize();//####[56]####
        lblImage.setBounds(40, 20, size.width, size.height);//####[57]####
        String photoTitle = photo.getTitle();//####[59]####
        if (photoTitle.equals("")) //####[60]####
        photoTitle = defaultName;//####[61]####
        JLabel lblTitle = new JLabel(photoTitle);//####[63]####
        add(lblTitle);//####[64]####
        size = lblTitle.getPreferredSize();//####[65]####
        lblTitle.setBounds(150, 20, size.width, size.height);//####[66]####
        JPanel pnlButtons = new JPanel();//####[68]####
        pnlButtons.add(btnDownload);//####[70]####
        btnDownload.setToolTipText("Retrieve full size");//####[71]####
        pnlButtons.add(btnView);//####[72]####
        btnView.setToolTipText("View full size");//####[73]####
        pnlButtons.add(btnSave);//####[74]####
        btnSave.setToolTipText("Save to file");//####[75]####
        btnView.setEnabled(false);//####[76]####
        btnSave.setEnabled(false);//####[77]####
        btnDownload.addActionListener(this);//####[78]####
        btnView.addActionListener(this);//####[79]####
        btnSave.addActionListener(this);//####[80]####
        Dimension btnSize = new Dimension(45, 45);//####[82]####
        btnDownload.setPreferredSize(btnSize);//####[83]####
        btnView.setPreferredSize(btnSize);//####[84]####
        btnSave.setPreferredSize(btnSize);//####[85]####
        add(pnlButtons);//####[87]####
        size = pnlButtons.getPreferredSize();//####[88]####
        pnlButtons.setBounds(580, 20, size.width, size.height);//####[89]####
        setPreferredSize(new Dimension(MainFrame.width - 100, height));//####[91]####
    }//####[92]####
//####[94]####
    public Photo getPhoto() {//####[94]####
        return photo;//####[95]####
    }//####[96]####
//####[98]####
    public Image getSquarePhoto() {//####[98]####
        return imageSquare;//####[99]####
    }//####[100]####
//####[102]####
    private void downloadCompleteTask(TaskID<Image> id) {//####[102]####
        try {//####[103]####
            downloadComplete(id.getReturnResult());//####[104]####
        } catch (ExecutionException e) {//####[105]####
            e.printStackTrace();//####[106]####
        } catch (InterruptedException e) {//####[107]####
            e.printStackTrace();//####[108]####
        }//####[109]####
    }//####[110]####
//####[112]####
    private void downloadComplete(Image image) {//####[112]####
        imageLarge = image;//####[113]####
        btnView.setEnabled(true);//####[114]####
        btnSave.setEnabled(true);//####[115]####
    }//####[116]####
//####[119]####
    @Override//####[119]####
    public void actionPerformed(ActionEvent e) {//####[119]####
        if (e.getSource() == btnDownload) //####[120]####
        {//####[120]####
            btnDownload.setEnabled(false);//####[121]####
            if (MainFrame.isParallel) //####[122]####
            {//####[122]####
                TaskInfo __pt__id = new TaskInfo();//####[123]####
//####[123]####
                boolean isEDT = GuiThread.isEventDispatchThread();//####[123]####
//####[123]####
//####[123]####
                /*  -- ParaTask notify clause for 'id' -- *///####[123]####
                try {//####[123]####
                    Method __pt__id_slot_0 = null;//####[123]####
                    __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "downloadCompleteTask", new Class[] { TaskID.class });//####[123]####
                    TaskID __pt__id_slot_0_dummy_0 = null;//####[123]####
                    if (false) downloadCompleteTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[123]####
                    __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, this, false));//####[123]####
//####[123]####
                } catch(Exception __pt__e) { //####[123]####
                    System.err.println("Problem registering method in clause:");//####[123]####
                    __pt__e.printStackTrace();//####[123]####
                }//####[123]####
                TaskID<Image> id = Search.getMediumImageTask(photo, __pt__id);//####[123]####
            } else {//####[124]####
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[125]####
                Image result = Search.getMediumImage(photo);//####[126]####
                downloadComplete(result);//####[127]####
                setCursor(Cursor.getDefaultCursor());//####[128]####
            }//####[129]####
        } else if (e.getSource() == btnView) //####[131]####
        {//####[131]####
            JLabel label = new JLabel(new ImageIcon(imageLarge));//####[132]####
            JOptionPane.showConfirmDialog(this, label, photo.getTitle(), JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);//####[133]####
        } else if (e.getSource() == btnSave) //####[134]####
        {//####[134]####
            UIManager.put("FileChooser.readOnly", Boolean.TRUE);//####[135]####
            JFileChooser fc = new JFileChooser(preferredDir);//####[136]####
            String fileName = photo.getTitle();//####[137]####
            if (fileName.equals("")) //####[138]####
            fileName = defaultName;//####[139]####
            fileName += ".jpeg";//####[140]####
            fc.setSelectedFile(new File(preferredDir, fileName));//####[141]####
            int returnVal = fc.showSaveDialog(this);//####[142]####
            if (returnVal == JFileChooser.APPROVE_OPTION) //####[143]####
            {//####[143]####
                File file = fc.getSelectedFile();//####[144]####
                File outputFile = new File(file.getParent(), file.getName());//####[145]####
                if (outputFile.exists()) //####[147]####
                {//####[147]####
                    JOptionPane.showConfirmDialog(this, "File already exists, please select another name.", "File exists.", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);//####[148]####
                } else {//####[149]####
                    RenderedImage rendered = null;//####[150]####
                    if (imageLarge instanceof RenderedImage) //####[151]####
                    {//####[151]####
                        rendered = (RenderedImage) imageLarge;//####[152]####
                    } else {//####[153]####
                        BufferedImage buffered = new BufferedImage(imageLarge.getWidth(null), imageLarge.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[154]####
                        Graphics2D g = buffered.createGraphics();//####[155]####
                        g.drawImage(imageLarge, 0, 0, null);//####[156]####
                        g.dispose();//####[157]####
                        rendered = buffered;//####[158]####
                    }//####[159]####
                    try {//####[160]####
                        ImageIO.write(rendered, "JPEG", outputFile);//####[161]####
                    } catch (IOException e1) {//####[162]####
                        e1.printStackTrace();//####[163]####
                    }//####[164]####
                }//####[165]####
            }//####[166]####
        }//####[167]####
    }//####[168]####
}//####[168]####
