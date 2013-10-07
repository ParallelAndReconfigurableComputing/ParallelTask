package pt.examples.ParaImage;//####[1]####
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
import pt.examples.ParaImage.flickr.PhotoWithImage;//####[20]####
import javax.swing.BoxLayout;//####[22]####
import javax.swing.ImageIcon;//####[23]####
import javax.swing.JButton;//####[24]####
import javax.swing.JProgressBar;//####[25]####
import javax.swing.JLabel;//####[26]####
import javax.swing.JPanel;//####[27]####
import javax.swing.JScrollPane;//####[28]####
import javax.swing.JSeparator;//####[29]####
import javax.swing.JSpinner;//####[30]####
import javax.swing.JTextField;//####[31]####
import javax.swing.SpinnerNumberModel;//####[32]####
import javax.swing.event.ChangeEvent;//####[33]####
import javax.swing.event.ChangeListener;//####[34]####
import javax.swing.event.DocumentEvent;//####[35]####
import javax.swing.event.DocumentListener;//####[36]####
import com.aetrion.flickr.photos.Photo;//####[38]####
import com.aetrion.flickr.photos.PhotoList;//####[39]####
import pt.examples.ParaImage.flickr.Search;//####[41]####
//####[41]####
//-- ParaTask related imports//####[41]####
import pt.runtime.*;//####[41]####
import java.util.concurrent.ExecutionException;//####[41]####
import java.util.concurrent.locks.*;//####[41]####
import java.lang.reflect.*;//####[41]####
import pt.runtime.GuiThread;//####[41]####
import java.util.concurrent.BlockingQueue;//####[41]####
import java.util.ArrayList;//####[41]####
import java.util.List;//####[41]####
//####[41]####
public class SearchProjectPanel extends ProjectPanel implements ActionListener {//####[43]####
    static{ParaTask.init();}//####[43]####
    /*  ParaTask helper method to access private/protected slots *///####[43]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[43]####
        if (m.getParameterTypes().length == 0)//####[43]####
            m.invoke(instance);//####[43]####
        else if ((m.getParameterTypes().length == 1))//####[43]####
            m.invoke(instance, arg);//####[43]####
        else //####[43]####
            m.invoke(instance, arg, interResult);//####[43]####
    }//####[43]####
//####[45]####
    private int currentOffset = 1;//####[45]####
//####[47]####
    private String tempTxt = "[enter search criteria here]";//####[47]####
//####[48]####
    private JTextField txtSearch = new JTextField("", 18);//####[48]####
//####[49]####
    private JButton btnSearch = new JButton(new ImageIcon(Utils.getImg("search.png")));//####[49]####
//####[50]####
    private JButton btnStop = new JButton(new ImageIcon(Utils.getImg("stop.png")));//####[50]####
//####[51]####
    private JSpinner spnResultsPerPage;//####[51]####
//####[52]####
    private JLabel lblResPP = new JLabel("#pics");//####[52]####
//####[54]####
    private JButton btnNext = new JButton(new ImageIcon(Utils.getImg("right.png")));//####[54]####
//####[55]####
    private JButton btnPrev = new JButton(new ImageIcon(Utils.getImg("left.png")));//####[55]####
//####[56]####
    private JTextField txtCurrentPage = new JTextField("-", 7);//####[56]####
//####[58]####
    private JProgressBar progressBar = new JProgressBar(0, 100);//####[58]####
//####[59]####
    private JPanel thumbnailsPanel;//####[59]####
//####[60]####
    private Font userFont;//####[60]####
//####[61]####
    private Font emptyFont;//####[61]####
//####[63]####
    private boolean searchFieldInvalid() {//####[63]####
        return txtSearch.getText().trim().equals("") || txtSearch.getText().equals(tempTxt);//####[64]####
    }//####[65]####
//####[67]####
    public SearchProjectPanel(MainFrame mainFrame, String projectName) {//####[67]####
        super(mainFrame, projectName);//####[68]####
        Dimension sizeBtns = new Dimension(35, 35);//####[69]####
        setLayout(new BorderLayout());//####[71]####
        btnSearch.addActionListener(this);//####[72]####
        btnStop.addActionListener(this);//####[73]####
        btnSearch.setPreferredSize(sizeBtns);//####[74]####
        btnSearch.setToolTipText("Search on Flickr");//####[75]####
        userFont = txtSearch.getFont();//####[77]####
        emptyFont = new Font(txtSearch.getFont().getName(), Font.ITALIC, txtSearch.getFont().getSize());//####[78]####
        txtSearch.setMargin(new Insets(0, 5, 0, 5));//####[80]####
        txtSearch.setText(tempTxt);//####[81]####
        btnSearch.setEnabled(false);//####[82]####
        txtSearch.setFont(emptyFont);//####[83]####
        txtSearch.setForeground(Color.GRAY);//####[84]####
        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));//####[86]####
        panelSearch.add(txtSearch);//####[88]####
        progressBar.setStringPainted(true);//####[90]####
        txtSearch.addKeyListener(new KeyAdapter() {//####[90]####
//####[94]####
            @Override//####[94]####
            public void keyPressed(KeyEvent e) {//####[94]####
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !searchFieldInvalid()) //####[95]####
                {//####[95]####
                    clearResults();//####[96]####
                    disableButtons();//####[97]####
                    currentOffset = 1;//####[98]####
                    search();//####[99]####
                }//####[100]####
            }//####[101]####
        });//####[101]####
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {//####[101]####
//####[107]####
            @Override//####[107]####
            public void changedUpdate(DocumentEvent e) {//####[107]####
            }//####[108]####
//####[110]####
            @Override//####[110]####
            public void insertUpdate(DocumentEvent e) {//####[110]####
                btnSearch.setEnabled(!searchFieldInvalid());//####[111]####
            }//####[112]####
//####[114]####
            @Override//####[114]####
            public void removeUpdate(DocumentEvent e) {//####[114]####
                btnSearch.setEnabled(!searchFieldInvalid());//####[115]####
            }//####[116]####
        });//####[116]####
        txtSearch.addFocusListener(new FocusAdapter() {//####[116]####
//####[120]####
            @Override//####[120]####
            public void focusGained(FocusEvent e) {//####[120]####
                txtSearch.setForeground(Color.BLACK);//####[121]####
                txtSearch.setFont(userFont);//####[122]####
                if (searchFieldInvalid()) //####[123]####
                {//####[123]####
                    txtSearch.setText("");//####[124]####
                }//####[125]####
            }//####[126]####
//####[128]####
            @Override//####[128]####
            public void focusLost(FocusEvent e) {//####[128]####
                if (searchFieldInvalid()) //####[129]####
                {//####[129]####
                    txtSearch.setForeground(Color.GRAY);//####[130]####
                    txtSearch.setFont(emptyFont);//####[131]####
                    txtSearch.setText(tempTxt);//####[132]####
                } else {//####[133]####
                    txtSearch.setForeground(Color.BLACK);//####[134]####
                    txtSearch.setFont(userFont);//####[135]####
                }//####[136]####
            }//####[137]####
        });//####[137]####
        txtSearch.setPreferredSize(new Dimension(txtSearch.getPreferredSize().width, sizeBtns.height));//####[140]####
        txtSearch.setToolTipText("Enter search criteria here");//####[141]####
        panelSearch.add(btnSearch);//####[142]####
        btnStop.setPreferredSize(sizeBtns);//####[143]####
        btnStop.setToolTipText("Cancel search");//####[144]####
        btnStop.setEnabled(false);//####[145]####
        panelSearch.add(btnStop);//####[146]####
        progressBar.setPreferredSize(new Dimension(100, sizeBtns.height));//####[147]####
        panelSearch.add(progressBar);//####[148]####
        JLabel space = new JLabel();//####[150]####
        space.setPreferredSize(new Dimension(15, sizeBtns.height));//####[151]####
        panelSearch.add(space);//####[152]####
        panelSearch.add(lblResPP);//####[154]####
        lblResPP.setToolTipText("Number of photos returned per page");//####[155]####
        SpinnerNumberModel spnModel = new SpinnerNumberModel(8, 1, 99, 1);//####[156]####
        spnResultsPerPage = new JSpinner(spnModel);//####[157]####
        spnResultsPerPage.addChangeListener(new ChangeListener() {//####[157]####
//####[160]####
            @Override//####[160]####
            public void stateChanged(ChangeEvent e) {//####[160]####
                btnPrev.setEnabled(false);//####[161]####
                btnNext.setEnabled(false);//####[162]####
            }//####[163]####
        });//####[163]####
        spnResultsPerPage.setPreferredSize(new Dimension(spnResultsPerPage.getPreferredSize().width, sizeBtns.height));//####[165]####
        spnResultsPerPage.setToolTipText("Number of photos returned per page");//####[166]####
        panelSearch.add(spnResultsPerPage);//####[167]####
        panelSearch.add(new JSeparator());//####[169]####
        panelSearch.add(new JSeparator());//####[170]####
        btnPrev.addActionListener(this);//####[172]####
        btnNext.setToolTipText("View next page of results");//####[173]####
        btnPrev.setToolTipText("View previous page of results");//####[174]####
        btnNext.addActionListener(this);//####[175]####
        btnPrev.setEnabled(false);//####[176]####
        btnNext.setEnabled(false);//####[177]####
        panelSearch.add(btnPrev);//####[178]####
        txtCurrentPage.setHorizontalAlignment(JTextField.CENTER);//####[179]####
        txtCurrentPage.setEditable(false);//####[180]####
        txtCurrentPage.setToolTipText("Current page of results");//####[181]####
        panelSearch.add(txtCurrentPage);//####[182]####
        txtCurrentPage.setPreferredSize(new Dimension(txtCurrentPage.getPreferredSize().width, sizeBtns.height));//####[183]####
        panelSearch.add(btnNext);//####[184]####
        btnPrev.setPreferredSize(sizeBtns);//####[185]####
        btnNext.setPreferredSize(sizeBtns);//####[186]####
        add(panelSearch, BorderLayout.NORTH);//####[189]####
        thumbnailsPanel = new JPanel();//####[191]####
        thumbnailsPanel.setLayout(new BoxLayout(thumbnailsPanel, BoxLayout.Y_AXIS));//####[192]####
        JScrollPane scroll = new JScrollPane(thumbnailsPanel);//####[193]####
        thumbnailsPanel.setVisible(true);//####[194]####
        scroll.setVisible(true);//####[195]####
        add(scroll, BorderLayout.CENTER);//####[196]####
    }//####[197]####
//####[199]####
    private void clearResults() {//####[199]####
        progressBar.setValue(0);//####[200]####
        thumbnailsPanel.removeAll();//####[201]####
        thumbnailsPanel.updateUI();//####[202]####
    }//####[203]####
//####[216]####
    private void finishedSearch() {//####[216]####
        txtCurrentPage.setText("page " + (currentOffset));//####[217]####
        thumbnailsPanel.updateUI();//####[218]####
        isModified = true;//####[219]####
        mainFrame.updateTabIcons();//####[220]####
        enableButtons();//####[221]####
        currentSearch = null;//####[222]####
    }//####[223]####
//####[231]####
    public void addToDisplay(PhotoWithImage pi) {//####[231]####
        thumbnailsPanel.add(new PhotoPanelItem(pi.getPhoto(), pi.getImage(), projectDir));//####[232]####
    }//####[233]####
//####[245]####
    private void enableButtons() {//####[245]####
        btnStop.setEnabled(false);//####[246]####
        btnSearch.setEnabled(true);//####[247]####
        lblResPP.setEnabled(true);//####[248]####
        txtSearch.setEnabled(true);//####[249]####
        btnNext.setEnabled(true);//####[250]####
        spnResultsPerPage.setEnabled(true);//####[251]####
        if (currentOffset == 1) //####[252]####
        btnPrev.setEnabled(false); else btnPrev.setEnabled(true);//####[253]####
    }//####[256]####
//####[259]####
    private void receiveIntermediate(TaskID id, PhotoWithImage pi) {//####[259]####
        addToDisplay(pi);//####[260]####
        progressBar.setValue(id.getProgress());//####[261]####
        updateUI();//####[262]####
    }//####[265]####
//####[268]####
    private void search() {//####[268]####
        String search = txtSearch.getText();//####[269]####
        int resPP = (Integer) spnResultsPerPage.getValue();//####[270]####
        if (MainFrame.isParallel) //####[272]####
        {//####[272]####
            TaskInfo __pt__currentSearch = new TaskInfo();//####[274]####
//####[274]####
            boolean isEDT = GuiThread.isEventDispatchThread();//####[274]####
//####[274]####
//####[274]####
            /*  -- ParaTask notify clause for 'currentSearch' -- *///####[274]####
            try {//####[274]####
                Method __pt__currentSearch_slot_0 = null;//####[274]####
                __pt__currentSearch_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "finishedSearch", new Class[] {});//####[275]####
                if (false) finishedSearch(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[275]####
                __pt__currentSearch.addSlotToNotify(new Slot(__pt__currentSearch_slot_0, this, false));//####[275]####
//####[275]####
            } catch(Exception __pt__e) { //####[275]####
                System.err.println("Problem registering method in clause:");//####[275]####
                __pt__e.printStackTrace();//####[275]####
            }//####[275]####
//####[275]####
            /*  -- ParaTask notify-intermediate clause for 'currentSearch' -- *///####[275]####
            try {//####[275]####
                Method __pt__currentSearch_inter_slot_0 = null;//####[275]####
                __pt__currentSearch_inter_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "receiveIntermediate", new Class[] {TaskID.class, PhotoWithImage.class});//####[277]####
                TaskID __pt__currentSearch_inter_slot_0_dummy_0 = null;//####[277]####
                PhotoWithImage __pt__currentSearch_inter_slot_0_dummy_1 = null;//####[277]####
                if (false) receiveIntermediate(__pt__currentSearch_inter_slot_0_dummy_0, __pt__currentSearch_inter_slot_0_dummy_1); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[277]####
                __pt__currentSearch.addInterSlotToNotify(new Slot(__pt__currentSearch_inter_slot_0, this, true));//####[277]####
//####[277]####
            } catch(Exception __pt__e) { //####[277]####
                System.err.println("Problem registering method in clause:");//####[277]####
                __pt__e.printStackTrace();//####[277]####
            }//####[277]####
            currentSearch = Search.searchTask(search, resPP, currentOffset, __pt__currentSearch);//####[277]####
        } else {//####[279]####
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[281]####
            List<PhotoWithImage> results = Search.search(search, resPP, currentOffset);//####[283]####
            for (PhotoWithImage pi : results) //####[286]####
            {//####[286]####
                addToDisplay(pi);//####[287]####
            }//####[288]####
            progressBar.setValue(100);//####[290]####
            finishedSearch();//####[291]####
            setCursor(Cursor.getDefaultCursor());//####[294]####
        }//####[295]####
    }//####[296]####
//####[298]####
    private void disableButtons() {//####[298]####
        btnStop.setEnabled(true);//####[299]####
        btnSearch.setEnabled(false);//####[300]####
        lblResPP.setEnabled(false);//####[301]####
        txtSearch.setEnabled(false);//####[302]####
        btnNext.setEnabled(false);//####[303]####
        btnPrev.setEnabled(false);//####[304]####
        spnResultsPerPage.setEnabled(false);//####[305]####
    }//####[306]####
//####[308]####
    private TaskID<List<PhotoWithImage>> currentSearch = null;//####[308]####
//####[311]####
    @Override//####[311]####
    public void actionPerformed(ActionEvent e) {//####[311]####
        if (e.getSource() == btnStop) //####[312]####
        {//####[312]####
            if (currentSearch != null) //####[313]####
            {//####[313]####
                currentSearch.cancelAttempt();//####[314]####
            } else {//####[315]####
                JOptionPane.showMessageDialog(this, "Sorry, cancel currently only works with ParaTask.");//####[316]####
            }//####[317]####
        } else {//####[318]####
            clearResults();//####[319]####
            disableButtons();//####[320]####
            if (e.getSource() == btnSearch) //####[321]####
            {//####[321]####
                currentOffset = 1;//####[322]####
            } else if (e.getSource() == btnPrev) //####[323]####
            {//####[323]####
                currentOffset--;//####[324]####
            } else if (e.getSource() == btnNext) //####[325]####
            {//####[325]####
                currentOffset++;//####[326]####
            }//####[327]####
            search();//####[328]####
        }//####[329]####
    }//####[330]####
}//####[330]####
