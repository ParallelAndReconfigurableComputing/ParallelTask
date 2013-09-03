package application;//####[1]####
//####[1]####
import java.awt.BorderLayout;//####[3]####
import java.awt.Dimension;//####[4]####
import java.awt.Graphics2D;//####[5]####
import java.awt.Image;//####[6]####
import java.awt.Cursor;//####[7]####
import java.awt.event.ActionEvent;//####[8]####
import java.awt.event.ActionListener;//####[9]####
import java.awt.image.BufferedImage;//####[10]####
import java.awt.image.RenderedImage;//####[11]####
import java.io.File;//####[12]####
import java.io.IOException;//####[13]####
import java.util.concurrent.ExecutionException;//####[14]####
import javax.imageio.ImageIO;//####[16]####
import javax.swing.ImageIcon;//####[17]####
import javax.swing.JButton;//####[18]####
import javax.swing.JFileChooser;//####[19]####
import javax.swing.JFrame;//####[20]####
import javax.swing.JLabel;//####[21]####
import javax.swing.JOptionPane;//####[22]####
import javax.swing.JPanel;//####[23]####
import javax.swing.UIManager;//####[24]####
import javax.swing.WindowConstants;//####[25]####
import paratask.runtime.*;//####[27]####
import application.flickr.Search;//####[29]####
import com.aetrion.flickr.photos.Photo;//####[31]####
//####[31]####
//-- ParaTask related imports//####[31]####
import pt.runtime.*;//####[31]####
import java.util.concurrent.ExecutionException;//####[31]####
import java.util.concurrent.locks.*;//####[31]####
import java.lang.reflect.*;//####[31]####
import pt.runtime.GuiThread;//####[31]####
import java.util.concurrent.BlockingQueue;//####[31]####
import java.util.ArrayList;//####[31]####
import java.util.List;//####[31]####
//####[31]####
public class PhotoPanelItem extends JPanel implements ActionListener {//####[33]####
    static{ParaTask.init();}//####[33]####
    /*  ParaTask helper method to access private/protected slots *///####[33]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[33]####
        if (m.getParameterTypes().length == 0)//####[33]####
            m.invoke(instance);//####[33]####
        else if ((m.getParameterTypes().length == 1))//####[33]####
            m.invoke(instance, arg);//####[33]####
        else //####[33]####
            m.invoke(instance, arg, interResult);//####[33]####
    }//####[33]####
//####[35]####
    private Photo photo;//####[35]####
//####[36]####
    private Image imageSquare;//####[36]####
//####[37]####
    private Image imageLarge;//####[37]####
//####[38]####
    private File preferredDir;//####[38]####
//####[39]####
    private SearchProjectPanel parent;//####[39]####
//####[41]####
    private String defaultName = "unnamed";//####[41]####
//####[43]####
    private JButton btnDownload = new JButton(new ImageIcon("images/download.png"));//####[43]####
//####[44]####
    private JButton btnView = new JButton(new ImageIcon("images/openfull.png"));//####[44]####
//####[45]####
    private JButton btnSave = new JButton(new ImageIcon("images/save2.png"));//####[45]####
//####[46]####
    private JButton btnHash = new JButton(new ImageIcon("images/hashComp.png"));//####[46]####
//####[47]####
    private JButton btnColor = new JButton(new ImageIcon("images/colorComp.png"));//####[47]####
//####[48]####
    private JButton btnSettings = new JButton(new ImageIcon("images/settingsSmall.png"));//####[48]####
//####[50]####
    private static int height = 100;//####[50]####
//####[52]####
    public PhotoPanelItem(Photo photo, Image imageSquare, File preferredDir, SearchProjectPanel parent) {//####[52]####
        this.photo = photo;//####[53]####
        this.imageSquare = imageSquare;//####[54]####
        this.preferredDir = preferredDir;//####[55]####
        this.parent = parent;//####[56]####
        setLayout(null);//####[58]####
        JLabel lblImage = new JLabel(new ImageIcon(imageSquare));//####[60]####
        add(lblImage);//####[61]####
        Dimension size = lblImage.getPreferredSize();//####[63]####
        lblImage.setBounds(40, 20, size.width, size.height);//####[64]####
        String photoTitle = photo.getTitle();//####[66]####
        if (photoTitle.equals("")) //####[67]####
        photoTitle = defaultName;//####[68]####
        JLabel lblTitle = new JLabel(photoTitle);//####[70]####
        add(lblTitle);//####[71]####
        size = lblTitle.getPreferredSize();//####[72]####
        lblTitle.setBounds(150, 20, size.width, size.height);//####[73]####
        JPanel pnlButtons = new JPanel();//####[75]####
        pnlButtons.add(btnDownload);//####[77]####
        btnDownload.setToolTipText("Retrieve full size");//####[78]####
        pnlButtons.add(btnView);//####[79]####
        btnView.setToolTipText("View full size");//####[80]####
        pnlButtons.add(btnSave);//####[81]####
        btnSave.setToolTipText("Save to file");//####[82]####
        pnlButtons.add(btnHash);//####[83]####
        btnHash.setToolTipText("Compare images by hash");//####[84]####
        pnlButtons.add(btnColor);//####[85]####
        btnColor.setToolTipText("Compare images by color");//####[86]####
        pnlButtons.add(btnSettings);//####[87]####
        btnSettings.setToolTipText("Modify attributes related to comparisons");//####[88]####
        btnView.setEnabled(false);//####[90]####
        btnSave.setEnabled(false);//####[91]####
        btnDownload.addActionListener(this);//####[92]####
        btnView.addActionListener(this);//####[93]####
        btnSave.addActionListener(this);//####[94]####
        btnHash.addActionListener(this);//####[95]####
        btnColor.addActionListener(this);//####[96]####
        btnSettings.addActionListener(this);//####[97]####
        Dimension btnSize = new Dimension(45, 45);//####[99]####
        btnDownload.setPreferredSize(btnSize);//####[100]####
        btnView.setPreferredSize(btnSize);//####[101]####
        btnSave.setPreferredSize(btnSize);//####[102]####
        btnHash.setPreferredSize(btnSize);//####[103]####
        btnColor.setPreferredSize(btnSize);//####[104]####
        btnSettings.setPreferredSize(btnSize);//####[105]####
        add(pnlButtons);//####[107]####
        size = pnlButtons.getPreferredSize();//####[108]####
        pnlButtons.setBounds(580, 20, size.width, size.height);//####[109]####
        setPreferredSize(new Dimension(MainFrame.width - 100, height));//####[111]####
    }//####[112]####
//####[114]####
    public Photo getPhoto() {//####[114]####
        return photo;//####[115]####
    }//####[116]####
//####[118]####
    public Image getSquarePhoto() {//####[118]####
        return imageSquare;//####[119]####
    }//####[120]####
//####[122]####
    private void downloadCompleteTask(TaskID<Image> id) {//####[122]####
        try {//####[123]####
            downloadComplete(id.getReturnResult());//####[124]####
        } catch (ExecutionException e) {//####[125]####
            e.printStackTrace();//####[126]####
        } catch (InterruptedException e) {//####[127]####
            e.printStackTrace();//####[128]####
        }//####[129]####
    }//####[130]####
//####[132]####
    private void downloadComplete(Image image) {//####[132]####
        imageLarge = image;//####[133]####
        btnView.setEnabled(true);//####[134]####
        btnSave.setEnabled(true);//####[135]####
    }//####[136]####
//####[139]####
    @Override//####[139]####
    public void actionPerformed(ActionEvent e) {//####[139]####
        if (e.getSource() == btnDownload) //####[140]####
        {//####[140]####
            Timer timer = new Timer("Download");//####[141]####
            btnDownload.setEnabled(false);//####[142]####
            if (MainFrame.isParallel) //####[143]####
            {//####[143]####
                TaskInfo __pt__id = new TaskInfo();//####[144]####
//####[144]####
                boolean isEDT = GuiThread.isEventDispatchThread();//####[144]####
//####[144]####
//####[144]####
                /*  -- ParaTask notify clause for 'id' -- *///####[144]####
                try {//####[144]####
                    Method __pt__id_slot_0 = null;//####[144]####
                    __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "downloadCompleteTask", new Class[] { TaskID.class });//####[144]####
                    TaskID __pt__id_slot_0_dummy_0 = null;//####[144]####
                    if (false) downloadCompleteTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[144]####
                    __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, this, false));//####[144]####
//####[144]####
                    Method __pt__id_slot_1 = null;//####[144]####
                    __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[144]####
                    if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[144]####
                    __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, timer, false));//####[144]####
//####[144]####
                } catch(Exception __pt__e) { //####[144]####
                    System.err.println("Problem registering method in clause:");//####[144]####
                    __pt__e.printStackTrace();//####[144]####
                }//####[144]####
                TaskID<Image> id = Search.getMediumImageTask(photo, __pt__id);//####[144]####
            } else {//####[145]####
                setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[146]####
                Image result = Search.getMediumImage(photo);//####[147]####
                downloadComplete(result);//####[148]####
                setCursor(Cursor.getDefaultCursor());//####[149]####
                timer.taskComplete();//####[150]####
            }//####[151]####
        } else if (e.getSource() == btnView) //####[153]####
        {//####[153]####
            JFrame frame = new JFrame(photo.getTitle());//####[154]####
            frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);//####[155]####
            ZoomCanvas canvas = new ZoomCanvas(imageLarge);//####[156]####
            frame.add(canvas, BorderLayout.CENTER);//####[157]####
            frame.pack();//####[158]####
            frame.setVisible(true);//####[159]####
            frame.setResizable(false);//####[160]####
            canvas.createBufferStrategy(3);//####[161]####
        } else if (e.getSource() == btnSave) //####[162]####
        {//####[162]####
            UIManager.put("FileChooser.readOnly", Boolean.TRUE);//####[163]####
            JFileChooser fc = new JFileChooser(preferredDir);//####[164]####
            String fileName = photo.getTitle();//####[165]####
            if (fileName.equals("")) //####[166]####
            fileName = defaultName;//####[167]####
            fileName += ".jpeg";//####[168]####
            fc.setSelectedFile(new File(preferredDir, fileName));//####[169]####
            int returnVal = fc.showSaveDialog(this);//####[170]####
            if (returnVal == JFileChooser.APPROVE_OPTION) //####[171]####
            {//####[171]####
                File file = fc.getSelectedFile();//####[172]####
                File outputFile = new File(file.getParent(), file.getName());//####[173]####
                if (outputFile.exists()) //####[175]####
                {//####[175]####
                    JOptionPane.showConfirmDialog(this, "File already exists, please select another name.", "File exists.", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);//####[176]####
                } else {//####[177]####
                    RenderedImage rendered = null;//####[178]####
                    if (imageLarge instanceof RenderedImage) //####[179]####
                    {//####[179]####
                        rendered = (RenderedImage) imageLarge;//####[180]####
                    } else {//####[181]####
                        BufferedImage buffered = new BufferedImage(imageLarge.getWidth(null), imageLarge.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[182]####
                        Graphics2D g = buffered.createGraphics();//####[183]####
                        g.drawImage(imageLarge, 0, 0, null);//####[184]####
                        g.dispose();//####[185]####
                        rendered = buffered;//####[186]####
                    }//####[187]####
                    try {//####[188]####
                        ImageIO.write(rendered, "JPEG", outputFile);//####[189]####
                    } catch (IOException e1) {//####[190]####
                        e1.printStackTrace();//####[191]####
                    }//####[192]####
                }//####[193]####
            }//####[194]####
        } else if (e.getSource() == btnHash) //####[195]####
        {//####[195]####
            parent.compareHash(this);//####[196]####
        } else if (e.getSource() == btnColor) //####[197]####
        {//####[197]####
            parent.compareColor(this);//####[198]####
        } else if (e.getSource() == btnSettings) //####[199]####
        {//####[199]####
            parent.compareSettings();//####[200]####
        }//####[201]####
    }//####[202]####
}//####[202]####
