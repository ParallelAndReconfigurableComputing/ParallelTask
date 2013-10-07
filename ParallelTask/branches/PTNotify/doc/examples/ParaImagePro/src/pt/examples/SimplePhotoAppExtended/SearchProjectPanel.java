package pt.examples.SimplePhotoAppExtended;//####[1]####
//####[1]####
import java.awt.BorderLayout;//####[3]####
import java.awt.Color;//####[4]####
import java.awt.Cursor;//####[5]####
import java.awt.Dimension;//####[6]####
import java.awt.FlowLayout;//####[7]####
import java.awt.Font;//####[8]####
import java.awt.Image;//####[9]####
import java.awt.Insets;//####[10]####
import java.awt.event.ActionEvent;//####[11]####
import java.awt.event.ActionListener;//####[12]####
import java.awt.event.FocusAdapter;//####[13]####
import java.awt.event.FocusEvent;//####[14]####
import java.awt.event.KeyAdapter;//####[15]####
import javax.swing.JOptionPane;//####[16]####
import java.awt.event.KeyEvent;//####[17]####
import java.util.concurrent.ExecutionException;//####[18]####
import java.util.*;//####[19]####
import pt.examples.SimplePhotoAppExtended.flickr.PhotoWithImage;//####[20]####
import javax.swing.BorderFactory;//####[22]####
import javax.swing.BoxLayout;//####[23]####
import javax.swing.ImageIcon;//####[24]####
import javax.swing.JButton;//####[25]####
import javax.swing.JProgressBar;//####[26]####
import javax.swing.JLabel;//####[27]####
import javax.swing.JPanel;//####[28]####
import javax.swing.JScrollPane;//####[29]####
import javax.swing.JSeparator;//####[30]####
import javax.swing.JSlider;//####[31]####
import javax.swing.JSpinner;//####[32]####
import javax.swing.JTextField;//####[33]####
import javax.swing.SpinnerNumberModel;//####[34]####
import javax.swing.event.ChangeEvent;//####[35]####
import javax.swing.event.ChangeListener;//####[36]####
import javax.swing.event.DocumentEvent;//####[37]####
import javax.swing.event.DocumentListener;//####[38]####
import pt.runtime.*;//####[40]####
import com.aetrion.flickr.photos.Photo;//####[42]####
import com.aetrion.flickr.photos.PhotoList;//####[43]####
import pt.examples.SimplePhotoAppExtended.flickr.Search;//####[45]####
//####[45]####
//-- ParaTask related imports//####[45]####
import pt.runtime.*;//####[45]####
import java.util.concurrent.ExecutionException;//####[45]####
import java.util.concurrent.locks.*;//####[45]####
import java.lang.reflect.*;//####[45]####
import pt.runtime.GuiThread;//####[45]####
import java.util.concurrent.BlockingQueue;//####[45]####
import java.util.ArrayList;//####[45]####
import java.util.List;//####[45]####
//####[45]####
public class SearchProjectPanel extends ProjectPanel implements ActionListener {//####[47]####
    static{ParaTask.init();}//####[47]####
    /*  ParaTask helper method to access private/protected slots *///####[47]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[47]####
        if (m.getParameterTypes().length == 0)//####[47]####
            m.invoke(instance);//####[47]####
        else if ((m.getParameterTypes().length == 1))//####[47]####
            m.invoke(instance, arg);//####[47]####
        else //####[47]####
            m.invoke(instance, arg, interResult);//####[47]####
    }//####[47]####
//####[49]####
    private int currentOffset = 1;//####[49]####
//####[51]####
    private int accuracy = 1;//####[51]####
//####[52]####
    private int sensitivity = 32;//####[52]####
//####[54]####
    private String tempTxt = "[enter search criteria here]";//####[54]####
//####[55]####
    private JTextField txtSearch = new JTextField("", 18);//####[55]####
//####[56]####
    private JButton btnSearch = new JButton(new ImageIcon(Utils.getImg("search.png")));//####[56]####
//####[57]####
    private JButton btnStop = new JButton(new ImageIcon(Utils.getImg("stop.png")));//####[57]####
//####[58]####
    private JSpinner spnResultsPerPage;//####[58]####
//####[59]####
    private JLabel lblResPP = new JLabel("#pics");//####[59]####
//####[61]####
    private JButton btnNext = new JButton(new ImageIcon(Utils.getImg("right.png")));//####[61]####
//####[62]####
    private JButton btnPrev = new JButton(new ImageIcon(Utils.getImg("left.png")));//####[62]####
//####[63]####
    private JTextField txtCurrentPage = new JTextField("-", 7);//####[63]####
//####[65]####
    private JProgressBar progressBar = new JProgressBar(0, 100);//####[65]####
//####[66]####
    private JPanel thumbnailsPanel;//####[66]####
//####[67]####
    private Font userFont;//####[67]####
//####[68]####
    private Font emptyFont;//####[68]####
//####[70]####
    private boolean searchFieldInvalid() {//####[70]####
        return txtSearch.getText().trim().equals("") || txtSearch.getText().equals(tempTxt);//####[71]####
    }//####[72]####
//####[74]####
    public SearchProjectPanel(MainFrame mainFrame, String projectName) {//####[74]####
        super(mainFrame, projectName);//####[75]####
        Dimension sizeBtns = new Dimension(35, 35);//####[76]####
        setLayout(new BorderLayout());//####[78]####
        btnSearch.addActionListener(this);//####[79]####
        btnStop.addActionListener(this);//####[80]####
        btnSearch.setPreferredSize(sizeBtns);//####[81]####
        btnSearch.setToolTipText("Search on Flickr");//####[82]####
        userFont = txtSearch.getFont();//####[84]####
        emptyFont = new Font(txtSearch.getFont().getName(), Font.ITALIC, txtSearch.getFont().getSize());//####[85]####
        txtSearch.setMargin(new Insets(0, 5, 0, 5));//####[87]####
        txtSearch.setText(tempTxt);//####[88]####
        btnSearch.setEnabled(false);//####[89]####
        txtSearch.setFont(emptyFont);//####[90]####
        txtSearch.setForeground(Color.GRAY);//####[91]####
        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));//####[93]####
        panelSearch.add(txtSearch);//####[95]####
        progressBar.setStringPainted(true);//####[97]####
        txtSearch.addKeyListener(new KeyAdapter() {//####[97]####
//####[101]####
            @Override//####[101]####
            public void keyPressed(KeyEvent e) {//####[101]####
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !searchFieldInvalid()) //####[102]####
                {//####[102]####
                    clearResults();//####[103]####
                    disableButtons();//####[104]####
                    currentOffset = 1;//####[105]####
                    search();//####[106]####
                }//####[107]####
            }//####[108]####
        });//####[108]####
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {//####[108]####
//####[114]####
            @Override//####[114]####
            public void changedUpdate(DocumentEvent e) {//####[114]####
            }//####[115]####
//####[117]####
            @Override//####[117]####
            public void insertUpdate(DocumentEvent e) {//####[117]####
                btnSearch.setEnabled(!searchFieldInvalid());//####[118]####
            }//####[119]####
//####[121]####
            @Override//####[121]####
            public void removeUpdate(DocumentEvent e) {//####[121]####
                btnSearch.setEnabled(!searchFieldInvalid());//####[122]####
            }//####[123]####
        });//####[123]####
        txtSearch.addFocusListener(new FocusAdapter() {//####[123]####
//####[127]####
            @Override//####[127]####
            public void focusGained(FocusEvent e) {//####[127]####
                txtSearch.setForeground(Color.BLACK);//####[128]####
                txtSearch.setFont(userFont);//####[129]####
                if (searchFieldInvalid()) //####[130]####
                {//####[130]####
                    txtSearch.setText("");//####[131]####
                }//####[132]####
            }//####[133]####
//####[135]####
            @Override//####[135]####
            public void focusLost(FocusEvent e) {//####[135]####
                if (searchFieldInvalid()) //####[136]####
                {//####[136]####
                    txtSearch.setForeground(Color.GRAY);//####[137]####
                    txtSearch.setFont(emptyFont);//####[138]####
                    txtSearch.setText(tempTxt);//####[139]####
                } else {//####[140]####
                    txtSearch.setForeground(Color.BLACK);//####[141]####
                    txtSearch.setFont(userFont);//####[142]####
                }//####[143]####
            }//####[144]####
        });//####[144]####
        txtSearch.setPreferredSize(new Dimension(txtSearch.getPreferredSize().width, sizeBtns.height));//####[147]####
        txtSearch.setToolTipText("Enter search criteria here");//####[148]####
        panelSearch.add(btnSearch);//####[149]####
        btnStop.setPreferredSize(sizeBtns);//####[150]####
        btnStop.setToolTipText("Cancel search");//####[151]####
        btnStop.setEnabled(false);//####[152]####
        panelSearch.add(btnStop);//####[153]####
        progressBar.setPreferredSize(new Dimension(100, sizeBtns.height));//####[154]####
        panelSearch.add(progressBar);//####[155]####
        JLabel space = new JLabel();//####[157]####
        space.setPreferredSize(new Dimension(15, sizeBtns.height));//####[158]####
        panelSearch.add(space);//####[159]####
        panelSearch.add(lblResPP);//####[161]####
        lblResPP.setToolTipText("Number of photos returned per page");//####[162]####
        SpinnerNumberModel spnModel = new SpinnerNumberModel(8, 1, 99, 1);//####[163]####
        spnResultsPerPage = new JSpinner(spnModel);//####[164]####
        spnResultsPerPage.addChangeListener(new ChangeListener() {//####[164]####
//####[167]####
            @Override//####[167]####
            public void stateChanged(ChangeEvent e) {//####[167]####
                btnPrev.setEnabled(false);//####[168]####
                btnNext.setEnabled(false);//####[169]####
            }//####[170]####
        });//####[170]####
        spnResultsPerPage.setPreferredSize(new Dimension(spnResultsPerPage.getPreferredSize().width, sizeBtns.height));//####[172]####
        spnResultsPerPage.setToolTipText("Number of photos returned per page");//####[173]####
        panelSearch.add(spnResultsPerPage);//####[174]####
        panelSearch.add(new JSeparator());//####[176]####
        panelSearch.add(new JSeparator());//####[177]####
        btnPrev.addActionListener(this);//####[179]####
        btnNext.setToolTipText("View next page of results");//####[180]####
        btnPrev.setToolTipText("View previous page of results");//####[181]####
        btnNext.addActionListener(this);//####[182]####
        btnPrev.setEnabled(false);//####[183]####
        btnNext.setEnabled(false);//####[184]####
        panelSearch.add(btnPrev);//####[185]####
        txtCurrentPage.setHorizontalAlignment(JTextField.CENTER);//####[186]####
        txtCurrentPage.setEditable(false);//####[187]####
        txtCurrentPage.setToolTipText("Current page of results");//####[188]####
        panelSearch.add(txtCurrentPage);//####[189]####
        txtCurrentPage.setPreferredSize(new Dimension(txtCurrentPage.getPreferredSize().width, sizeBtns.height));//####[190]####
        panelSearch.add(btnNext);//####[191]####
        btnPrev.setPreferredSize(sizeBtns);//####[192]####
        btnNext.setPreferredSize(sizeBtns);//####[193]####
        add(panelSearch, BorderLayout.NORTH);//####[196]####
        thumbnailsPanel = new JPanel();//####[198]####
        thumbnailsPanel.setLayout(new BoxLayout(thumbnailsPanel, BoxLayout.Y_AXIS));//####[199]####
        JScrollPane scroll = new JScrollPane(thumbnailsPanel);//####[200]####
        thumbnailsPanel.setVisible(true);//####[201]####
        scroll.setVisible(true);//####[202]####
        add(scroll, BorderLayout.CENTER);//####[203]####
    }//####[204]####
//####[206]####
    private void clearResults() {//####[206]####
        progressBar.setValue(0);//####[207]####
        thumbnailsPanel.removeAll();//####[208]####
        thumbnailsPanel.updateUI();//####[209]####
    }//####[210]####
//####[223]####
    private void finishedSearch() {//####[223]####
        txtCurrentPage.setText("page " + (currentOffset));//####[224]####
        thumbnailsPanel.updateUI();//####[225]####
        isModified = true;//####[226]####
        mainFrame.updateTabIcons();//####[227]####
        enableButtons();//####[228]####
        currentSearch = null;//####[229]####
    }//####[230]####
//####[238]####
    public void addToDisplay(PhotoWithImage pi) {//####[238]####
        thumbnailsPanel.add(new PhotoPanelItem(pi.getPhoto(), pi.getImage(), projectDir, this));//####[239]####
    }//####[240]####
//####[252]####
    private void enableButtons() {//####[252]####
        btnStop.setEnabled(false);//####[253]####
        btnSearch.setEnabled(true);//####[254]####
        lblResPP.setEnabled(true);//####[255]####
        txtSearch.setEnabled(true);//####[256]####
        btnNext.setEnabled(true);//####[257]####
        spnResultsPerPage.setEnabled(true);//####[258]####
        if (currentOffset == 1) //####[259]####
        btnPrev.setEnabled(false); else btnPrev.setEnabled(true);//####[260]####
    }//####[263]####
//####[266]####
    private void receiveIntermediate(TaskID id, PhotoWithImage pi) {//####[266]####
        addToDisplay(pi);//####[267]####
        progressBar.setValue(id.getProgress());//####[268]####
        updateUI();//####[269]####
    }//####[272]####
//####[275]####
    private void search() {//####[275]####
        Timer timer = new Timer("Search");//####[276]####
        String search = txtSearch.getText();//####[277]####
        int resPP = (Integer) spnResultsPerPage.getValue();//####[278]####
        if (MainFrame.isParallel) //####[280]####
        {//####[280]####
            TaskInfo __pt__currentSearch = new TaskInfo();//####[282]####
//####[282]####
            boolean isEDT = GuiThread.isEventDispatchThread();//####[282]####
//####[282]####
//####[282]####
            /*  -- ParaTask notify clause for 'currentSearch' -- *///####[282]####
            try {//####[282]####
                Method __pt__currentSearch_slot_0 = null;//####[282]####
                __pt__currentSearch_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "finishedSearch", new Class[] {});//####[283]####
                if (false) finishedSearch(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[283]####
                __pt__currentSearch.addSlotToNotify(new Slot(__pt__currentSearch_slot_0, this, false));//####[283]####
//####[283]####
                Method __pt__currentSearch_slot_1 = null;//####[283]####
                __pt__currentSearch_slot_1 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[283]####
                if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[283]####
                __pt__currentSearch.addSlotToNotify(new Slot(__pt__currentSearch_slot_1, timer, false));//####[283]####
//####[283]####
            } catch(Exception __pt__e) { //####[283]####
                System.err.println("Problem registering method in clause:");//####[283]####
                __pt__e.printStackTrace();//####[283]####
            }//####[283]####
//####[283]####
            /*  -- ParaTask notify-intermediate clause for 'currentSearch' -- *///####[283]####
            try {//####[283]####
                Method __pt__currentSearch_inter_slot_0 = null;//####[283]####
                __pt__currentSearch_inter_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "receiveIntermediate", new Class[] {TaskID.class, PhotoWithImage.class});//####[285]####
                TaskID __pt__currentSearch_inter_slot_0_dummy_0 = null;//####[285]####
                PhotoWithImage __pt__currentSearch_inter_slot_0_dummy_1 = null;//####[285]####
                if (false) receiveIntermediate(__pt__currentSearch_inter_slot_0_dummy_0, __pt__currentSearch_inter_slot_0_dummy_1); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[285]####
                __pt__currentSearch.addInterSlotToNotify(new Slot(__pt__currentSearch_inter_slot_0, this, true));//####[285]####
//####[285]####
            } catch(Exception __pt__e) { //####[285]####
                System.err.println("Problem registering method in clause:");//####[285]####
                __pt__e.printStackTrace();//####[285]####
            }//####[285]####
            currentSearch = Search.searchTask(search, resPP, currentOffset, __pt__currentSearch);//####[285]####
        } else {//####[287]####
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[289]####
            List<PhotoWithImage> results = Search.search(search, resPP, currentOffset);//####[291]####
            for (PhotoWithImage pi : results) //####[294]####
            {//####[294]####
                addToDisplay(pi);//####[295]####
            }//####[296]####
            progressBar.setValue(100);//####[298]####
            finishedSearch();//####[299]####
            setCursor(Cursor.getDefaultCursor());//####[302]####
            timer.taskComplete();//####[303]####
        }//####[304]####
    }//####[305]####
//####[307]####
    private void disableButtons() {//####[307]####
        btnStop.setEnabled(true);//####[308]####
        btnSearch.setEnabled(false);//####[309]####
        lblResPP.setEnabled(false);//####[310]####
        txtSearch.setEnabled(false);//####[311]####
        btnNext.setEnabled(false);//####[312]####
        btnPrev.setEnabled(false);//####[313]####
        spnResultsPerPage.setEnabled(false);//####[314]####
    }//####[315]####
//####[317]####
    private TaskID<List<PhotoWithImage>> currentSearch = null;//####[317]####
//####[320]####
    @Override//####[320]####
    public void actionPerformed(ActionEvent e) {//####[320]####
        if (e.getSource() == btnStop) //####[321]####
        {//####[321]####
            if (currentSearch != null) //####[322]####
            {//####[322]####
                currentSearch.cancelAttempt();//####[323]####
            } else {//####[324]####
                JOptionPane.showMessageDialog(this, "Sorry, cancel currently only works with ParaTask.");//####[325]####
            }//####[326]####
        } else {//####[327]####
            clearResults();//####[328]####
            disableButtons();//####[329]####
            if (e.getSource() == btnSearch) //####[330]####
            {//####[330]####
                currentOffset = 1;//####[331]####
            } else if (e.getSource() == btnPrev) //####[332]####
            {//####[332]####
                currentOffset--;//####[333]####
            } else if (e.getSource() == btnNext) //####[334]####
            {//####[334]####
                currentOffset++;//####[335]####
            }//####[336]####
            search();//####[337]####
        }//####[338]####
    }//####[339]####
//####[341]####
    protected void compareHash(PhotoPanelItem compare) {//####[341]####
        Timer timer = new Timer("Hash Compare");//####[342]####
        if (MainFrame.isParallel) //####[344]####
        {//####[344]####
            List<PhotoPanelItem> result = SearchCompare.compareHash2(thumbnailsPanel, compare, accuracy);//####[345]####
            thumbnailsPanel.removeAll();//####[347]####
            for (PhotoPanelItem pi : result) //####[348]####
            {//####[348]####
                thumbnailsPanel.add(pi);//####[349]####
            }//####[350]####
            thumbnailsPanel.updateUI();//####[351]####
            timer.taskComplete();//####[352]####
        } else {//####[354]####
            List<PhotoPanelItem> result = SearchCompare.compareHash(thumbnailsPanel, compare, accuracy);//####[355]####
            thumbnailsPanel.removeAll();//####[357]####
            for (PhotoPanelItem pi : result) //####[358]####
            {//####[358]####
                thumbnailsPanel.add(pi);//####[359]####
            }//####[360]####
            thumbnailsPanel.updateUI();//####[361]####
            timer.taskComplete();//####[362]####
        }//####[363]####
    }//####[364]####
//####[366]####
    protected void compareColor(PhotoPanelItem compare) {//####[366]####
        Timer timer = new Timer("Color Compare");//####[367]####
        if (MainFrame.isParallel) //####[369]####
        {//####[369]####
            List<PhotoPanelItem> result = SearchCompare.compareColor2(thumbnailsPanel, compare, sensitivity, accuracy);//####[370]####
            thumbnailsPanel.removeAll();//####[372]####
            for (PhotoPanelItem pi : result) //####[373]####
            {//####[373]####
                thumbnailsPanel.add(pi);//####[374]####
            }//####[375]####
            thumbnailsPanel.updateUI();//####[376]####
            timer.taskComplete();//####[377]####
        } else {//####[379]####
            List<PhotoPanelItem> result = SearchCompare.compareColor(thumbnailsPanel, compare, sensitivity, accuracy);//####[380]####
            thumbnailsPanel.removeAll();//####[382]####
            for (PhotoPanelItem pi : result) //####[383]####
            {//####[383]####
                thumbnailsPanel.add(pi);//####[384]####
            }//####[385]####
            thumbnailsPanel.updateUI();//####[386]####
            timer.taskComplete();//####[387]####
        }//####[388]####
    }//####[389]####
//####[391]####
    protected void compareSettings() {//####[391]####
        JPanel panel = new JPanel();//####[392]####
        JSlider accuracySlider = new JSlider(JSlider.HORIZONTAL, 1, 75, accuracy);//####[393]####
        accuracySlider.setBorder(BorderFactory.createTitledBorder("Accuracy Level"));//####[395]####
        accuracySlider.setMajorTickSpacing(74);//####[396]####
        accuracySlider.setMinorTickSpacing(5);//####[397]####
        accuracySlider.setPaintTicks(true);//####[398]####
        accuracySlider.setPaintLabels(true);//####[399]####
        class accuracySliderListener implements ChangeListener {//####[401]####
//####[401]####
            /*  ParaTask helper method to access private/protected slots *///####[401]####
            public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[401]####
                if (m.getParameterTypes().length == 0)//####[401]####
                    m.invoke(instance);//####[401]####
                else if ((m.getParameterTypes().length == 1))//####[401]####
                    m.invoke(instance, arg);//####[401]####
                else //####[401]####
                    m.invoke(instance, arg, interResult);//####[401]####
            }//####[401]####
//####[402]####
            public void stateChanged(ChangeEvent e) {//####[402]####
                JSlider source = (JSlider) e.getSource();//####[403]####
                if (!source.getValueIsAdjusting()) //####[404]####
                {//####[404]####
                    accuracy = (int) source.getValue();//####[405]####
                }//####[406]####
            }//####[407]####
        }//####[407]####
        accuracySlider.addChangeListener(new accuracySliderListener());//####[410]####
        panel.add(accuracySlider);//####[411]####
        JSlider sensitivitySlider = new JSlider(JSlider.HORIZONTAL, 1, 128, sensitivity);//####[413]####
        sensitivitySlider.setBorder(BorderFactory.createTitledBorder("Color Matching Sensitivity"));//####[415]####
        sensitivitySlider.setMajorTickSpacing(127);//####[416]####
        sensitivitySlider.setMinorTickSpacing(6);//####[417]####
        sensitivitySlider.setPaintTicks(true);//####[418]####
        sensitivitySlider.setPaintLabels(true);//####[419]####
        class sensitivitySliderListener implements ChangeListener {//####[421]####
//####[421]####
            /*  ParaTask helper method to access private/protected slots *///####[421]####
            public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[421]####
                if (m.getParameterTypes().length == 0)//####[421]####
                    m.invoke(instance);//####[421]####
                else if ((m.getParameterTypes().length == 1))//####[421]####
                    m.invoke(instance, arg);//####[421]####
                else //####[421]####
                    m.invoke(instance, arg, interResult);//####[421]####
            }//####[421]####
//####[422]####
            public void stateChanged(ChangeEvent e) {//####[422]####
                JSlider source = (JSlider) e.getSource();//####[423]####
                if (!source.getValueIsAdjusting()) //####[424]####
                {//####[424]####
                    sensitivity = (int) source.getValue();//####[425]####
                }//####[426]####
            }//####[427]####
        }//####[427]####
        sensitivitySlider.addChangeListener(new sensitivitySliderListener());//####[430]####
        panel.add(sensitivitySlider);//####[431]####
        JOptionPane.showConfirmDialog(SearchProjectPanel.this, panel, "Compare Settings", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);//####[433]####
    }//####[434]####
}//####[434]####
