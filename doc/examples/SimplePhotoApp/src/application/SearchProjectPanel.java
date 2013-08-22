package application;//####[1]####
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
import application.flickr.PhotoWithImage;//####[20]####
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
import paratask.runtime.*;//####[38]####
import com.aetrion.flickr.photos.Photo;//####[40]####
import com.aetrion.flickr.photos.PhotoList;//####[41]####
import application.flickr.Search;//####[43]####
//####[43]####
//-- ParaTask related imports//####[43]####
import paratask.runtime.*;//####[43]####
import java.util.concurrent.ExecutionException;//####[43]####
import java.util.concurrent.locks.*;//####[43]####
import java.lang.reflect.*;//####[43]####
import javax.swing.SwingUtilities;//####[43]####
//####[43]####
public class SearchProjectPanel extends ProjectPanel implements ActionListener {//####[45]####
//####[45]####
    /*  ParaTask helper method to access private/protected slots *///####[45]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[45]####
        if (m.getParameterTypes().length == 0)//####[45]####
            m.invoke(instance);//####[45]####
        else if ((m.getParameterTypes().length == 1))//####[45]####
            m.invoke(instance, arg);//####[45]####
        else //####[45]####
            m.invoke(instance, arg, interResult);//####[45]####
    }//####[45]####
//####[47]####
    private int currentOffset = 1;//####[47]####
//####[49]####
    private String tempTxt = "[enter search criteria here]";//####[49]####
//####[50]####
    private JTextField txtSearch = new JTextField("", 18);//####[50]####
//####[51]####
    private JButton btnSearch = new JButton(new ImageIcon("images/search.png"));//####[51]####
//####[52]####
    private JButton btnStop = new JButton(new ImageIcon("images/stop.png"));//####[52]####
//####[53]####
    private JSpinner spnResultsPerPage;//####[53]####
//####[54]####
    private JLabel lblResPP = new JLabel("#pics");//####[54]####
//####[56]####
    private JButton btnNext = new JButton(new ImageIcon("images/right.png"));//####[56]####
//####[57]####
    private JButton btnPrev = new JButton(new ImageIcon("images/left.png"));//####[57]####
//####[58]####
    private JTextField txtCurrentPage = new JTextField("-", 7);//####[58]####
//####[60]####
    private JProgressBar progressBar = new JProgressBar(0, 100);//####[60]####
//####[61]####
    private JPanel thumbnailsPanel;//####[61]####
//####[62]####
    private Font userFont;//####[62]####
//####[63]####
    private Font emptyFont;//####[63]####
//####[65]####
    private boolean searchFieldInvalid() {//####[65]####
        return txtSearch.getText().trim().equals("") || txtSearch.getText().equals(tempTxt);//####[66]####
    }//####[67]####
//####[69]####
    public SearchProjectPanel(MainFrame mainFrame, String projectName) {//####[69]####
        super(mainFrame, projectName);//####[70]####
        Dimension sizeBtns = new Dimension(35, 35);//####[71]####
        setLayout(new BorderLayout());//####[73]####
        btnSearch.addActionListener(this);//####[74]####
        btnStop.addActionListener(this);//####[75]####
        btnSearch.setPreferredSize(sizeBtns);//####[76]####
        btnSearch.setToolTipText("Search on Flickr");//####[77]####
        userFont = txtSearch.getFont();//####[79]####
        emptyFont = new Font(txtSearch.getFont().getName(), Font.ITALIC, txtSearch.getFont().getSize());//####[80]####
        txtSearch.setMargin(new Insets(0, 5, 0, 5));//####[82]####
        txtSearch.setText(tempTxt);//####[83]####
        btnSearch.setEnabled(false);//####[84]####
        txtSearch.setFont(emptyFont);//####[85]####
        txtSearch.setForeground(Color.GRAY);//####[86]####
        JPanel panelSearch = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));//####[88]####
        panelSearch.add(txtSearch);//####[90]####
        progressBar.setStringPainted(true);//####[92]####
        txtSearch.addKeyListener(new KeyAdapter() {//####[92]####
//####[96]####
            @Override//####[96]####
            public void keyPressed(KeyEvent e) {//####[96]####
                if (e.getKeyCode() == KeyEvent.VK_ENTER && !searchFieldInvalid()) //####[97]####
                {//####[97]####
                    clearResults();//####[98]####
                    disableButtons();//####[99]####
                    currentOffset = 1;//####[100]####
                    search();//####[101]####
                }//####[102]####
            }//####[103]####
        });//####[103]####
        txtSearch.getDocument().addDocumentListener(new DocumentListener() {//####[103]####
//####[109]####
            @Override//####[109]####
            public void changedUpdate(DocumentEvent e) {//####[109]####
            }//####[110]####
//####[112]####
            @Override//####[112]####
            public void insertUpdate(DocumentEvent e) {//####[112]####
                btnSearch.setEnabled(!searchFieldInvalid());//####[113]####
            }//####[114]####
//####[116]####
            @Override//####[116]####
            public void removeUpdate(DocumentEvent e) {//####[116]####
                btnSearch.setEnabled(!searchFieldInvalid());//####[117]####
            }//####[118]####
        });//####[118]####
        txtSearch.addFocusListener(new FocusAdapter() {//####[118]####
//####[122]####
            @Override//####[122]####
            public void focusGained(FocusEvent e) {//####[122]####
                txtSearch.setForeground(Color.BLACK);//####[123]####
                txtSearch.setFont(userFont);//####[124]####
                if (searchFieldInvalid()) //####[125]####
                {//####[125]####
                    txtSearch.setText("");//####[126]####
                }//####[127]####
            }//####[128]####
//####[130]####
            @Override//####[130]####
            public void focusLost(FocusEvent e) {//####[130]####
                if (searchFieldInvalid()) //####[131]####
                {//####[131]####
                    txtSearch.setForeground(Color.GRAY);//####[132]####
                    txtSearch.setFont(emptyFont);//####[133]####
                    txtSearch.setText(tempTxt);//####[134]####
                } else {//####[135]####
                    txtSearch.setForeground(Color.BLACK);//####[136]####
                    txtSearch.setFont(userFont);//####[137]####
                }//####[138]####
            }//####[139]####
        });//####[139]####
        txtSearch.setPreferredSize(new Dimension(txtSearch.getPreferredSize().width, sizeBtns.height));//####[142]####
        txtSearch.setToolTipText("Enter search criteria here");//####[143]####
        panelSearch.add(btnSearch);//####[144]####
        btnStop.setPreferredSize(sizeBtns);//####[145]####
        btnStop.setToolTipText("Cancel search");//####[146]####
        btnStop.setEnabled(false);//####[147]####
        panelSearch.add(btnStop);//####[148]####
        progressBar.setPreferredSize(new Dimension(100, sizeBtns.height));//####[149]####
        panelSearch.add(progressBar);//####[150]####
        JLabel space = new JLabel();//####[152]####
        space.setPreferredSize(new Dimension(15, sizeBtns.height));//####[153]####
        panelSearch.add(space);//####[154]####
        panelSearch.add(lblResPP);//####[156]####
        lblResPP.setToolTipText("Number of photos returned per page");//####[157]####
        SpinnerNumberModel spnModel = new SpinnerNumberModel(8, 1, 99, 1);//####[158]####
        spnResultsPerPage = new JSpinner(spnModel);//####[159]####
        spnResultsPerPage.addChangeListener(new ChangeListener() {//####[159]####
//####[162]####
            @Override//####[162]####
            public void stateChanged(ChangeEvent e) {//####[162]####
                btnPrev.setEnabled(false);//####[163]####
                btnNext.setEnabled(false);//####[164]####
            }//####[165]####
        });//####[165]####
        spnResultsPerPage.setPreferredSize(new Dimension(spnResultsPerPage.getPreferredSize().width, sizeBtns.height));//####[167]####
        spnResultsPerPage.setToolTipText("Number of photos returned per page");//####[168]####
        panelSearch.add(spnResultsPerPage);//####[169]####
        panelSearch.add(new JSeparator());//####[171]####
        panelSearch.add(new JSeparator());//####[172]####
        btnPrev.addActionListener(this);//####[174]####
        btnNext.setToolTipText("View next page of results");//####[175]####
        btnPrev.setToolTipText("View previous page of results");//####[176]####
        btnNext.addActionListener(this);//####[177]####
        btnPrev.setEnabled(false);//####[178]####
        btnNext.setEnabled(false);//####[179]####
        panelSearch.add(btnPrev);//####[180]####
        txtCurrentPage.setHorizontalAlignment(JTextField.CENTER);//####[181]####
        txtCurrentPage.setEditable(false);//####[182]####
        txtCurrentPage.setToolTipText("Current page of results");//####[183]####
        panelSearch.add(txtCurrentPage);//####[184]####
        txtCurrentPage.setPreferredSize(new Dimension(txtCurrentPage.getPreferredSize().width, sizeBtns.height));//####[185]####
        panelSearch.add(btnNext);//####[186]####
        btnPrev.setPreferredSize(sizeBtns);//####[187]####
        btnNext.setPreferredSize(sizeBtns);//####[188]####
        add(panelSearch, BorderLayout.NORTH);//####[191]####
        thumbnailsPanel = new JPanel();//####[193]####
        thumbnailsPanel.setLayout(new BoxLayout(thumbnailsPanel, BoxLayout.Y_AXIS));//####[194]####
        JScrollPane scroll = new JScrollPane(thumbnailsPanel);//####[195]####
        thumbnailsPanel.setVisible(true);//####[196]####
        scroll.setVisible(true);//####[197]####
        add(scroll, BorderLayout.CENTER);//####[198]####
    }//####[199]####
//####[201]####
    private void clearResults() {//####[201]####
        progressBar.setValue(0);//####[202]####
        thumbnailsPanel.removeAll();//####[203]####
        thumbnailsPanel.updateUI();//####[204]####
    }//####[205]####
//####[218]####
    private void finishedSearch() {//####[218]####
        txtCurrentPage.setText("page " + (currentOffset));//####[219]####
        thumbnailsPanel.updateUI();//####[220]####
        isModified = true;//####[221]####
        mainFrame.updateTabIcons();//####[222]####
        enableButtons();//####[223]####
        currentSearch = null;//####[224]####
    }//####[225]####
//####[233]####
    public void addToDisplay(PhotoWithImage pi) {//####[233]####
        thumbnailsPanel.add(new PhotoPanelItem(pi.getPhoto(), pi.getImage(), projectDir));//####[234]####
    }//####[235]####
//####[247]####
    private void enableButtons() {//####[247]####
        btnStop.setEnabled(false);//####[248]####
        btnSearch.setEnabled(true);//####[249]####
        lblResPP.setEnabled(true);//####[250]####
        txtSearch.setEnabled(true);//####[251]####
        btnNext.setEnabled(true);//####[252]####
        spnResultsPerPage.setEnabled(true);//####[253]####
        if (currentOffset == 1) //####[254]####
        btnPrev.setEnabled(false); else btnPrev.setEnabled(true);//####[255]####
    }//####[258]####
//####[261]####
    private void receiveIntermediate(TaskID id, PhotoWithImage pi) {//####[261]####
        addToDisplay(pi);//####[262]####
        progressBar.setValue(id.getProgress());//####[263]####
        updateUI();//####[264]####
    }//####[267]####
//####[270]####
    private void search() {//####[270]####
        String search = txtSearch.getText();//####[271]####
        int resPP = (Integer) spnResultsPerPage.getValue();//####[272]####
        if (MainFrame.isParallel) //####[274]####
        {//####[274]####
            TaskInfo __pt__currentSearch = new TaskInfo();//####[276]####
//####[276]####
            boolean isEDT = SwingUtilities.isEventDispatchThread();//####[276]####
//####[276]####
//####[276]####
            /*  -- ParaTask notify clause for 'currentSearch' -- *///####[276]####
            try {//####[276]####
                Method __pt__currentSearch_slot_0 = null;//####[276]####
                __pt__currentSearch_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "finishedSearch", new Class[] {});//####[277]####
                if (false) finishedSearch(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[277]####
                __pt__currentSearch.addSlotToNotify(new Slot(__pt__currentSearch_slot_0, this, isEDT, false));//####[277]####
//####[277]####
            } catch(Exception __pt__e) { //####[277]####
                System.err.println("Problem registering method in clause:");//####[277]####
                __pt__e.printStackTrace();//####[277]####
            }//####[277]####
//####[277]####
            /*  -- ParaTask notify-intermediate clause for 'currentSearch' -- *///####[277]####
            try {//####[277]####
                Method __pt__currentSearch_inter_slot_0 = null;//####[277]####
                __pt__currentSearch_inter_slot_0 = ParaTaskHelper.getDeclaredMethod(getClass(), "receiveIntermediate", new Class[] {TaskID.class, PhotoWithImage.class});//####[279]####
                TaskID __pt__currentSearch_inter_slot_0_dummy_0 = null;//####[279]####
                PhotoWithImage __pt__currentSearch_inter_slot_0_dummy_1 = null;//####[279]####
                if (false) receiveIntermediate(__pt__currentSearch_inter_slot_0_dummy_0, __pt__currentSearch_inter_slot_0_dummy_1); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[279]####
                __pt__currentSearch.addInterSlotToNotify(new Slot(__pt__currentSearch_inter_slot_0, this, isEDT, true));//####[279]####
//####[279]####
            } catch(Exception __pt__e) { //####[279]####
                System.err.println("Problem registering method in clause:");//####[279]####
                __pt__e.printStackTrace();//####[279]####
            }//####[279]####
            currentSearch = Search.searchTask(search, resPP, currentOffset, __pt__currentSearch);//####[279]####
        } else {//####[281]####
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[283]####
            List<PhotoWithImage> results = Search.search(search, resPP, currentOffset);//####[285]####
            for (PhotoWithImage pi : results) //####[288]####
            {//####[288]####
                addToDisplay(pi);//####[289]####
            }//####[290]####
            progressBar.setValue(100);//####[292]####
            finishedSearch();//####[293]####
            setCursor(Cursor.getDefaultCursor());//####[296]####
        }//####[297]####
    }//####[298]####
//####[300]####
    private void disableButtons() {//####[300]####
        btnStop.setEnabled(true);//####[301]####
        btnSearch.setEnabled(false);//####[302]####
        lblResPP.setEnabled(false);//####[303]####
        txtSearch.setEnabled(false);//####[304]####
        btnNext.setEnabled(false);//####[305]####
        btnPrev.setEnabled(false);//####[306]####
        spnResultsPerPage.setEnabled(false);//####[307]####
    }//####[308]####
//####[310]####
    private TaskID<List<PhotoWithImage>> currentSearch = null;//####[310]####
//####[313]####
    @Override//####[313]####
    public void actionPerformed(ActionEvent e) {//####[313]####
        if (e.getSource() == btnStop) //####[314]####
        {//####[314]####
            if (currentSearch != null) //####[315]####
            {//####[315]####
                currentSearch.cancelAttempt();//####[316]####
            } else {//####[317]####
                JOptionPane.showMessageDialog(this, "Sorry, cancel currently only works with ParaTask.");//####[318]####
            }//####[319]####
        } else {//####[320]####
            clearResults();//####[321]####
            disableButtons();//####[322]####
            if (e.getSource() == btnSearch) //####[323]####
            {//####[323]####
                currentOffset = 1;//####[324]####
            } else if (e.getSource() == btnPrev) //####[325]####
            {//####[325]####
                currentOffset--;//####[326]####
            } else if (e.getSource() == btnNext) //####[327]####
            {//####[327]####
                currentOffset++;//####[328]####
            }//####[329]####
            search();//####[330]####
        }//####[331]####
    }//####[332]####
}//####[332]####
