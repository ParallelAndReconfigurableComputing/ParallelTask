package application;//####[1]####
//####[1]####
import java.awt.BorderLayout;//####[3]####
import java.awt.Component;//####[4]####
import java.awt.Cursor;//####[5]####
import java.awt.Dimension;//####[6]####
import java.awt.FlowLayout;//####[7]####
import java.awt.GridLayout;//####[8]####
import java.awt.Image;//####[9]####
import java.awt.event.ActionEvent;//####[10]####
import java.io.File;//####[11]####
import java.util.ArrayList;//####[12]####
import java.util.Collections;//####[13]####
import java.util.Iterator;//####[14]####
import java.util.List;//####[15]####
import javax.swing.AbstractAction;//####[17]####
import javax.swing.Action;//####[18]####
import javax.swing.BorderFactory;//####[19]####
import javax.swing.ImageIcon;//####[20]####
import javax.swing.JButton;//####[21]####
import javax.swing.JFileChooser;//####[22]####
import javax.swing.JLabel;//####[23]####
import javax.swing.JOptionPane;//####[24]####
import javax.swing.JPanel;//####[25]####
import javax.swing.JScrollPane;//####[26]####
import javax.swing.JSlider;//####[27]####
import javax.swing.UIManager;//####[28]####
import javax.swing.event.ChangeEvent;//####[30]####
import javax.swing.event.ChangeListener;//####[31]####
//####[31]####
//-- ParaTask related imports//####[31]####
import paratask.runtime.*;//####[31]####
import java.util.concurrent.ExecutionException;//####[31]####
import java.util.concurrent.locks.*;//####[31]####
import java.lang.reflect.*;//####[31]####
import javax.swing.SwingUtilities;//####[31]####
//####[31]####
public class ImageProjectPanel extends ProjectPanel {//####[33]####
//####[33]####
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
    private JPanel thumbnailsPanel;//####[35]####
//####[36]####
    private List<PaletteItem> palette = Collections.synchronizedList(new ArrayList<PaletteItem>());//####[36]####
//####[38]####
    private int parallelism = 1;//####[38]####
//####[39]####
    private int density = 16;//####[39]####
//####[40]####
    private int size = 16;//####[40]####
//####[42]####
    private Method __pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_method = null;//####[42]####
    private Lock __pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_lock = new ReentrantLock();//####[42]####
    private TaskID<Void> addToThumbnailsPanelTask(final File file, final TaskID<Image> large, final TaskID<Image> square, final TaskID<Image> med)  {//####[42]####
//####[42]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[42]####
        return addToThumbnailsPanelTask(file, large, square, med, new TaskInfo());//####[42]####
    }//####[42]####
    private TaskID<Void> addToThumbnailsPanelTask(final File file, final TaskID<Image> large, final TaskID<Image> square, final TaskID<Image> med, TaskInfo taskinfo)  {//####[42]####
        if (__pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_method == null) {//####[42]####
            try {//####[42]####
                __pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_lock.lock();//####[42]####
                if (__pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_method == null) //####[42]####
                    __pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_method = ParaTaskHelper.getDeclaredMethod(getClass(), "__pt__addToThumbnailsPanelTask", new Class[] {File.class, TaskID.class, TaskID.class, TaskID.class});//####[42]####
            } catch (Exception e) {//####[42]####
                e.printStackTrace();//####[42]####
            } finally {//####[42]####
                __pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_lock.unlock();//####[42]####
            }//####[42]####
        }//####[42]####
//####[42]####
        Object[] args = new Object[] {file, large, square, med};//####[42]####
        taskinfo.setTaskArguments(args);//####[42]####
        taskinfo.setMethod(__pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_method);//####[42]####
        taskinfo.setInstance(this);//####[42]####
//####[42]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[42]####
    }//####[42]####
    public void __pt__addToThumbnailsPanelTask(final File file, final TaskID<Image> large, final TaskID<Image> square, final TaskID<Image> med) {//####[42]####
        SwingUtilities.invokeLater(new Runnable() {//####[42]####
//####[45]####
            @Override//####[45]####
            public void run() {//####[45]####
                try {//####[46]####
                    addToThumbnailsPanel(file, large.getReturnResult(), square.getReturnResult(), med.getReturnResult());//####[47]####
                } catch (ExecutionException e) {//####[48]####
                    e.printStackTrace();//####[49]####
                } catch (InterruptedException e) {//####[50]####
                    e.printStackTrace();//####[51]####
                }//####[52]####
            }//####[53]####
        });//####[53]####
    }//####[55]####
//####[55]####
//####[57]####
    private void addToThumbnailsPanel(File file, Image large, Image square, Image medium) {//####[57]####
        thumbnailsPanel.add(new ImagePanelItem(file, large, square, medium, ImageProjectPanel.this));//####[58]####
        updateUI();//####[59]####
    }//####[60]####
//####[62]####
    private Method __pt__finishedAddingNewPanelItemsTask_method = null;//####[62]####
    private Lock __pt__finishedAddingNewPanelItemsTask_lock = new ReentrantLock();//####[62]####
    private TaskID<Void> finishedAddingNewPanelItemsTask()  {//####[62]####
//####[62]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[62]####
        return finishedAddingNewPanelItemsTask(new TaskInfo());//####[62]####
    }//####[62]####
    private TaskID<Void> finishedAddingNewPanelItemsTask(TaskInfo taskinfo)  {//####[62]####
        if (__pt__finishedAddingNewPanelItemsTask_method == null) {//####[62]####
            try {//####[62]####
                __pt__finishedAddingNewPanelItemsTask_lock.lock();//####[62]####
                if (__pt__finishedAddingNewPanelItemsTask_method == null) //####[62]####
                    __pt__finishedAddingNewPanelItemsTask_method = ParaTaskHelper.getDeclaredMethod(getClass(), "__pt__finishedAddingNewPanelItemsTask", new Class[] {});//####[62]####
            } catch (Exception e) {//####[62]####
                e.printStackTrace();//####[62]####
            } finally {//####[62]####
                __pt__finishedAddingNewPanelItemsTask_lock.unlock();//####[62]####
            }//####[62]####
        }//####[62]####
//####[62]####
        taskinfo.setMethod(__pt__finishedAddingNewPanelItemsTask_method);//####[62]####
        taskinfo.setInstance(this);//####[62]####
//####[62]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[62]####
    }//####[62]####
    public void __pt__finishedAddingNewPanelItemsTask() {//####[62]####
        SwingUtilities.invokeLater(new Runnable() {//####[62]####
//####[65]####
            @Override//####[65]####
            public void run() {//####[65]####
                finishedAddingNewPanelItems();//####[66]####
            }//####[67]####
        });//####[67]####
    }//####[69]####
//####[69]####
//####[71]####
    private void finishedAddingNewPanelItems() {//####[71]####
        isModified = true;//####[72]####
        updateActions();//####[73]####
        thumbnailsPanel.updateUI();//####[74]####
        mainFrame.updateTabIcons();//####[75]####
        mainFrame.updateProjectActions();//####[76]####
    }//####[77]####
//####[79]####
    private Action actionAddImage = new AbstractAction() {//####[79]####
//####[82]####
        @Override//####[82]####
        public void actionPerformed(ActionEvent arg0) {//####[82]####
            UIManager.put("FileChooser.readOnly", Boolean.TRUE);//####[83]####
            JFileChooser fc = new JFileChooser(projectDir);//####[84]####
            fc.setMultiSelectionEnabled(true);//####[85]####
            fc.setAcceptAllFileFilterUsed(false);//####[86]####
            fc.addChoosableFileFilter(new ImageFilter());//####[87]####
            int retValue = fc.showOpenDialog(ImageProjectPanel.this);//####[88]####
            if (retValue == JFileChooser.APPROVE_OPTION) //####[89]####
            {//####[89]####
                Timer timer = new Timer(fc.getSelectedFiles().length, "Add Image");//####[90]####
                File[] inputImages = fc.getSelectedFiles();//####[91]####
                if (MainFrame.isParallel) //####[92]####
                {//####[92]####
                    TaskIDGroup grp = new TaskIDGroup(inputImages.length);//####[93]####
                    for (int i = 0; i < inputImages.length; i++) //####[94]####
                    {//####[94]####
                        TaskID<Image> idImage = ImageManipulation.getImageFullTask(inputImages[i]);//####[95]####
                        TaskInfo __pt__idMedium = new TaskInfo();//####[96]####
//####[96]####
                        /*  -- ParaTask dependsOn clause for 'idMedium' -- *///####[96]####
                        __pt__idMedium.addDependsOn(idImage);//####[96]####
//####[96]####
                        TaskID<Image> idMedium = ImageManipulation.getMediumTask(idImage, __pt__idMedium);//####[96]####
                        TaskInfo __pt__idSmall = new TaskInfo();//####[97]####
//####[97]####
                        /*  -- ParaTask dependsOn clause for 'idSmall' -- *///####[97]####
                        __pt__idSmall.addDependsOn(idImage);//####[97]####
//####[97]####
                        TaskID<Image> idSmall = ImageManipulation.getSmallSquareTask(idImage, __pt__idSmall);//####[97]####
                        TaskInfo __pt__id = new TaskInfo();//####[98]####
//####[98]####
                        /*  -- ParaTask dependsOn clause for 'id' -- *///####[98]####
                        __pt__id.addDependsOn(idSmall);//####[100]####
                        __pt__id.addDependsOn(idMedium);//####[100]####
//####[100]####
                        boolean isEDT = SwingUtilities.isEventDispatchThread();//####[100]####
//####[100]####
//####[100]####
                        /*  -- ParaTask notify clause for 'id' -- *///####[100]####
                        try {//####[100]####
                            Method __pt__id_slot_0 = null;//####[100]####
                            __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[100]####
                            if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[100]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, timer, isEDT, false));//####[100]####
//####[100]####
                        } catch(Exception __pt__e) { //####[100]####
                            System.err.println("Problem registering method in clause:");//####[100]####
                            __pt__e.printStackTrace();//####[100]####
                        }//####[100]####
                        TaskID id = addToThumbnailsPanelTask(inputImages[i], idImage, idSmall, idMedium, __pt__id);//####[100]####
                        grp.add(id);//####[101]####
                    }//####[102]####
                    TaskInfo __pt__finalTask = new TaskInfo();//####[103]####
//####[103]####
                    /*  -- ParaTask dependsOn clause for 'finalTask' -- *///####[103]####
                    __pt__finalTask.addDependsOn(grp);//####[103]####
//####[103]####
                    TaskID finalTask = finishedAddingNewPanelItemsTask(__pt__finalTask);//####[103]####
                } else {//####[104]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[105]####
                    for (int i = 0; i < inputImages.length; i++) //####[106]####
                    {//####[106]####
                        Image large = ImageManipulation.getImageFull(inputImages[i]);//####[107]####
                        Image small = ImageManipulation.getSmallSquare(large);//####[108]####
                        Image medium = ImageManipulation.getMedium(large);//####[109]####
                        addToThumbnailsPanel(inputImages[i], large, small, medium);//####[110]####
                        timer.taskComplete();//####[111]####
                    }//####[112]####
                    finishedAddingNewPanelItems();//####[113]####
                    setCursor(Cursor.getDefaultCursor());//####[114]####
                }//####[115]####
            }//####[116]####
        }//####[117]####
    };//####[117]####
//####[121]####
    private Action actionUndo = new AbstractAction() {//####[121]####
//####[123]####
        @Override//####[123]####
        public void actionPerformed(ActionEvent arg0) {//####[123]####
            Timer timer = new Timer("Undo");//####[124]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[125]####
            while (it.hasNext()) //####[126]####
            {//####[126]####
                ImagePanelItem panel = it.next();//####[127]####
                panel.restore();//####[128]####
            }//####[129]####
            updateActions();//####[130]####
            timer.taskComplete();//####[131]####
        }//####[132]####
    };//####[132]####
//####[136]####
    private Action actionSelectAll = new AbstractAction() {//####[136]####
//####[138]####
        @Override//####[138]####
        public void actionPerformed(ActionEvent arg0) {//####[138]####
            Timer timer = new Timer("Select All");//####[139]####
            Component[] comps = thumbnailsPanel.getComponents();//####[140]####
            for (int i = 0; i < comps.length; i++) //####[141]####
            {//####[141]####
                ImagePanelItem panel = (ImagePanelItem) comps[i];//####[142]####
                panel.setSelected(true);//####[143]####
            }//####[144]####
            updateActions();//####[145]####
            timer.taskComplete();//####[146]####
        }//####[147]####
    };//####[147]####
//####[150]####
    private List<ImagePanelItem> getAllPanels() {//####[150]####
        ArrayList<ImagePanelItem> list = new ArrayList<ImagePanelItem>();//####[151]####
        if (thumbnailsPanel != null) //####[153]####
        {//####[153]####
            Component[] comps = thumbnailsPanel.getComponents();//####[154]####
            for (int i = 0; i < comps.length; i++) //####[155]####
            {//####[155]####
                ImagePanelItem panel = (ImagePanelItem) comps[i];//####[156]####
                list.add(panel);//####[157]####
            }//####[158]####
        }//####[159]####
        return list;//####[161]####
    }//####[162]####
//####[164]####
    private List<ImagePanelItem> getSelectedPanels() {//####[164]####
        ArrayList<ImagePanelItem> list = new ArrayList<ImagePanelItem>();//####[165]####
        Component[] comps = thumbnailsPanel.getComponents();//####[167]####
        for (int i = 0; i < comps.length; i++) //####[168]####
        {//####[168]####
            ImagePanelItem panel = (ImagePanelItem) comps[i];//####[169]####
            if (panel.isSelected()) //####[170]####
            list.add(panel);//####[171]####
        }//####[172]####
        return list;//####[173]####
    }//####[174]####
//####[176]####
    private Action actionSelectNone = new AbstractAction() {//####[176]####
//####[178]####
        @Override//####[178]####
        public void actionPerformed(ActionEvent arg0) {//####[178]####
            Timer timer = new Timer("Select None");//####[179]####
            Component[] comps = thumbnailsPanel.getComponents();//####[180]####
            for (int i = 0; i < comps.length; i++) //####[181]####
            {//####[181]####
                ImagePanelItem panel = (ImagePanelItem) comps[i];//####[182]####
                panel.setSelected(false);//####[183]####
            }//####[184]####
            updateActions();//####[185]####
            timer.taskComplete();//####[186]####
        }//####[187]####
    };//####[187]####
//####[190]####
    private Action actionInvert = new AbstractAction() {//####[190]####
//####[192]####
        @Override//####[192]####
        public void actionPerformed(ActionEvent arg0) {//####[192]####
            Timer timer = new Timer(getSelectedPanels().size(), "Invert Colors");//####[193]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[194]####
            while (it.hasNext()) //####[196]####
            {//####[196]####
                ImagePanelItem panel = it.next();//####[197]####
                if (mainFrame.isParallel) //####[199]####
                {//####[199]####
                    TaskInfo __pt__id = new TaskInfo();//####[201]####
//####[201]####
                    /*  -- ParaTask dependsOn clause for 'id' -- *///####[201]####
                    __pt__id.addDependsOn(panel.getHistory());//####[203]####
//####[203]####
                    boolean isEDT = SwingUtilities.isEventDispatchThread();//####[203]####
//####[203]####
//####[203]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[203]####
                    try {//####[203]####
                        Method __pt__id_slot_0 = null;//####[203]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { TaskID.class });//####[203]####
                        TaskID __pt__id_slot_0_dummy_0 = null;//####[203]####
                        if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[203]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, isEDT, false));//####[203]####
//####[203]####
                        Method __pt__id_slot_1 = null;//####[203]####
                        __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[203]####
                        if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[203]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, isEDT, false));//####[203]####
//####[203]####
                        Method __pt__id_slot_2 = null;//####[203]####
                        __pt__id_slot_2 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[203]####
                        if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[203]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_2, timer, isEDT, false));//####[203]####
//####[203]####
                    } catch(Exception __pt__e) { //####[203]####
                        System.err.println("Problem registering method in clause:");//####[203]####
                        __pt__e.printStackTrace();//####[203]####
                    }//####[203]####
                    TaskID<ImageCombo> id = ImageManipulation.invertTask(panel, __pt__id);//####[203]####
                    panel.addToHistory(id);//####[204]####
                } else {//####[207]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[209]####
                    ImageCombo res = ImageManipulation.invert(panel);//####[211]####
                    panel.setImage(res);//####[212]####
                    guiChanged();//####[213]####
                    setCursor(Cursor.getDefaultCursor());//####[214]####
                    timer.taskComplete();//####[215]####
                }//####[216]####
            }//####[217]####
        }//####[218]####
    };//####[218]####
//####[221]####
    private void guiChanged() {//####[221]####
        isModified = true;//####[222]####
        updateActions();//####[223]####
        thumbnailsPanel.updateUI();//####[224]####
        mainFrame.updateTabIcons();//####[225]####
        mainFrame.updateProjectActions();//####[226]####
    }//####[227]####
//####[229]####
    private Action actionBlur = new AbstractAction() {//####[229]####
//####[231]####
        @Override//####[231]####
        public void actionPerformed(ActionEvent arg0) {//####[231]####
            Timer timer = new Timer(getSelectedPanels().size(), "Blur");//####[232]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[233]####
            while (it.hasNext()) //####[237]####
            {//####[237]####
                ImagePanelItem panel = it.next();//####[238]####
                if (mainFrame.isParallel) //####[240]####
                {//####[240]####
                    TaskInfo __pt__id = new TaskInfo();//####[242]####
//####[242]####
                    /*  -- ParaTask dependsOn clause for 'id' -- *///####[242]####
                    __pt__id.addDependsOn(panel.getHistory());//####[244]####
//####[244]####
                    boolean isEDT = SwingUtilities.isEventDispatchThread();//####[244]####
//####[244]####
//####[244]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[244]####
                    try {//####[244]####
                        Method __pt__id_slot_0 = null;//####[244]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { TaskID.class });//####[244]####
                        TaskID __pt__id_slot_0_dummy_0 = null;//####[244]####
                        if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[244]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, isEDT, false));//####[244]####
//####[244]####
                        Method __pt__id_slot_1 = null;//####[244]####
                        __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[244]####
                        if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[244]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, isEDT, false));//####[244]####
//####[244]####
                        Method __pt__id_slot_2 = null;//####[244]####
                        __pt__id_slot_2 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[244]####
                        if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[244]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_2, timer, isEDT, false));//####[244]####
//####[244]####
                    } catch(Exception __pt__e) { //####[244]####
                        System.err.println("Problem registering method in clause:");//####[244]####
                        __pt__e.printStackTrace();//####[244]####
                    }//####[244]####
                    TaskID<ImageCombo> id = ImageManipulation.blurTask(panel, __pt__id);//####[244]####
                    panel.addToHistory(id);//####[245]####
                } else {//####[248]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[249]####
                    ImageCombo res = ImageManipulation.blur(panel);//####[250]####
                    panel.setImage(res);//####[251]####
                    guiChanged();//####[252]####
                    setCursor(Cursor.getDefaultCursor());//####[253]####
                    timer.taskComplete();//####[254]####
                }//####[255]####
            }//####[256]####
        }//####[257]####
    };//####[257]####
//####[260]####
    private Action actionSharpen = new AbstractAction() {//####[260]####
//####[262]####
        @Override//####[262]####
        public void actionPerformed(ActionEvent arg0) {//####[262]####
            Timer timer = new Timer(getSelectedPanels().size(), "Sharpen");//####[263]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[264]####
            while (it.hasNext()) //####[268]####
            {//####[268]####
                ImagePanelItem panel = it.next();//####[269]####
                if (mainFrame.isParallel) //####[271]####
                {//####[271]####
                    TaskInfo __pt__id = new TaskInfo();//####[273]####
//####[273]####
                    /*  -- ParaTask dependsOn clause for 'id' -- *///####[273]####
                    __pt__id.addDependsOn(panel.getHistory());//####[275]####
//####[275]####
                    boolean isEDT = SwingUtilities.isEventDispatchThread();//####[275]####
//####[275]####
//####[275]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[275]####
                    try {//####[275]####
                        Method __pt__id_slot_0 = null;//####[275]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { TaskID.class });//####[275]####
                        TaskID __pt__id_slot_0_dummy_0 = null;//####[275]####
                        if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[275]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, isEDT, false));//####[275]####
//####[275]####
                        Method __pt__id_slot_1 = null;//####[275]####
                        __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[275]####
                        if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[275]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, isEDT, false));//####[275]####
//####[275]####
                        Method __pt__id_slot_2 = null;//####[275]####
                        __pt__id_slot_2 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[275]####
                        if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[275]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_2, timer, isEDT, false));//####[275]####
//####[275]####
                    } catch(Exception __pt__e) { //####[275]####
                        System.err.println("Problem registering method in clause:");//####[275]####
                        __pt__e.printStackTrace();//####[275]####
                    }//####[275]####
                    TaskID<ImageCombo> id = ImageManipulation.sharpenTask(panel, __pt__id);//####[275]####
                    panel.addToHistory(id);//####[276]####
                } else {//####[278]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[279]####
                    ImageCombo res = ImageManipulation.sharpen(panel);//####[280]####
                    panel.setImage(res);//####[281]####
                    guiChanged();//####[282]####
                    setCursor(Cursor.getDefaultCursor());//####[283]####
                    timer.taskComplete();//####[284]####
                }//####[285]####
            }//####[286]####
        }//####[287]####
    };//####[287]####
//####[290]####
    private void savePanels(List<ImagePanelItem> list) {//####[290]####
        Iterator<ImagePanelItem> it = list.iterator();//####[291]####
        while (it.hasNext()) //####[292]####
        {//####[292]####
            ImagePanelItem panel = it.next();//####[293]####
            panel.commit();//####[294]####
        }//####[295]####
        updateActions();//####[296]####
    }//####[297]####
//####[300]####
    @Override//####[300]####
    public void saveProject() {//####[300]####
        super.saveProject();//####[301]####
        savePanels(getAllPanels());//####[302]####
    }//####[303]####
//####[305]####
    private Action actionSaveSelected = new AbstractAction() {//####[305]####
//####[307]####
        @Override//####[307]####
        public void actionPerformed(ActionEvent e) {//####[307]####
            Timer timer = new Timer("Apply Changes");//####[308]####
            savePanels(getSelectedPanels());//####[310]####
            timer.taskComplete();//####[311]####
        }//####[312]####
    };//####[312]####
//####[315]####
    private Action actionApplyEdge = new AbstractAction() {//####[315]####
//####[317]####
        @Override//####[317]####
        public void actionPerformed(ActionEvent arg0) {//####[317]####
            Timer timer = new Timer(getSelectedPanels().size(), "Edge Detect");//####[318]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[319]####
            while (it.hasNext()) //####[323]####
            {//####[323]####
                ImagePanelItem panel = it.next();//####[324]####
                if (mainFrame.isParallel) //####[326]####
                {//####[326]####
                    TaskInfo __pt__id = new TaskInfo();//####[328]####
//####[328]####
                    /*  -- ParaTask dependsOn clause for 'id' -- *///####[328]####
                    __pt__id.addDependsOn(panel.getHistory());//####[330]####
//####[330]####
                    boolean isEDT = SwingUtilities.isEventDispatchThread();//####[330]####
//####[330]####
//####[330]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[330]####
                    try {//####[330]####
                        Method __pt__id_slot_0 = null;//####[330]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { TaskID.class });//####[330]####
                        TaskID __pt__id_slot_0_dummy_0 = null;//####[330]####
                        if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[330]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, isEDT, false));//####[330]####
//####[330]####
                        Method __pt__id_slot_1 = null;//####[330]####
                        __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[330]####
                        if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[330]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, isEDT, false));//####[330]####
//####[330]####
                        Method __pt__id_slot_2 = null;//####[330]####
                        __pt__id_slot_2 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[330]####
                        if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[330]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_2, timer, isEDT, false));//####[330]####
//####[330]####
                    } catch(Exception __pt__e) { //####[330]####
                        System.err.println("Problem registering method in clause:");//####[330]####
                        __pt__e.printStackTrace();//####[330]####
                    }//####[330]####
                    TaskID<ImageCombo> id = ImageManipulation.edgeDetectTask(panel, __pt__id);//####[330]####
                    panel.addToHistory(id);//####[331]####
                } else {//####[333]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[334]####
                    ImageCombo res = ImageManipulation.edgeDetect(panel);//####[335]####
                    panel.setImage(res);//####[336]####
                    guiChanged();//####[337]####
                    setCursor(Cursor.getDefaultCursor());//####[338]####
                    timer.taskComplete();//####[339]####
                }//####[340]####
            }//####[341]####
        }//####[342]####
    };//####[342]####
//####[345]####
    private Action actionRemoveImage = new AbstractAction() {//####[345]####
//####[347]####
        @Override//####[347]####
        public void actionPerformed(ActionEvent arg0) {//####[347]####
            Timer timer = new Timer("Remove Image");//####[348]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[349]####
            if (it.hasNext()) //####[350]####
            isModified = true;//####[351]####
            while (it.hasNext()) //####[352]####
            {//####[352]####
                thumbnailsPanel.remove(it.next());//####[353]####
            }//####[354]####
            updateActions();//####[356]####
            thumbnailsPanel.updateUI();//####[357]####
            mainFrame.updateTabIcons();//####[358]####
            mainFrame.updateProjectActions();//####[359]####
            timer.taskComplete();//####[360]####
        }//####[361]####
    };//####[361]####
//####[364]####
    private boolean canUndoSomethingSelected() {//####[364]####
        Iterator<ImagePanelItem> it = getAllPanels().iterator();//####[365]####
        while (it.hasNext()) //####[366]####
        {//####[366]####
            ImagePanelItem panel = it.next();//####[367]####
            if (panel.isModified() && panel.isSelected()) //####[368]####
            return true;//####[369]####
        }//####[370]####
        return false;//####[371]####
    }//####[372]####
//####[374]####
    public void updateActions() {//####[374]####
        boolean empty = true;//####[376]####
        boolean somethingSelected = false;//####[377]####
        boolean allSelected = false;//####[378]####
        boolean paletteReady = false;//####[379]####
        if (thumbnailsPanel != null) //####[381]####
        {//####[381]####
            Component[] comps = thumbnailsPanel.getComponents();//####[382]####
            if (comps.length != 0) //####[383]####
            {//####[383]####
                empty = false;//####[384]####
                somethingSelected = getSelectedPanels().size() > 0;//####[385]####
                allSelected = getSelectedPanels().size() == comps.length;//####[386]####
                paletteReady = palette.size() > 0;//####[387]####
            }//####[388]####
        }//####[389]####
        if (!empty) //####[391]####
        {//####[391]####
            actionSelectAll.setEnabled(!allSelected);//####[392]####
            actionRemoveImage.setEnabled(somethingSelected);//####[393]####
            actionSelectNone.setEnabled(somethingSelected);//####[394]####
            actionInvert.setEnabled(somethingSelected);//####[395]####
            actionApplyEdge.setEnabled(somethingSelected);//####[396]####
            actionBlur.setEnabled(somethingSelected);//####[397]####
            actionSharpen.setEnabled(somethingSelected);//####[398]####
            actionBuildMosaic.setEnabled(somethingSelected);//####[399]####
            actionBuildImageMosaic.setEnabled(somethingSelected && paletteReady);//####[400]####
            actionBuildPalette.setEnabled(somethingSelected && !paletteReady);//####[401]####
            actionClearPalette.setEnabled(paletteReady);//####[402]####
        } else {//####[403]####
            actionSelectAll.setEnabled(false);//####[404]####
            actionSelectNone.setEnabled(false);//####[405]####
            actionRemoveImage.setEnabled(false);//####[406]####
            actionInvert.setEnabled(false);//####[407]####
            actionApplyEdge.setEnabled(false);//####[408]####
            actionBlur.setEnabled(false);//####[409]####
            actionSharpen.setEnabled(false);//####[410]####
            actionBuildMosaic.setEnabled(false);//####[411]####
            actionBuildImageMosaic.setEnabled(false);//####[412]####
            actionBuildPalette.setEnabled(false);//####[413]####
            actionClearPalette.setEnabled(paletteReady);//####[414]####
        }//####[415]####
        actionUndo.setEnabled(canUndoSomethingSelected());//####[416]####
        actionSaveSelected.setEnabled(canUndoSomethingSelected());//####[417]####
    }//####[418]####
//####[420]####
    public ImageProjectPanel(MainFrame mainFrame, String projectName) {//####[420]####
        super(mainFrame, projectName);//####[421]####
        setLayout(new BorderLayout());//####[422]####
        addToolButtonsPanel();//####[424]####
        thumbnailsPanel = new JPanel(new GridLayout(0, 5));//####[426]####
        JScrollPane scroll = new JScrollPane(thumbnailsPanel);//####[427]####
        thumbnailsPanel.setVisible(true);//####[428]####
        scroll.setVisible(true);//####[429]####
        add(scroll, BorderLayout.CENTER);//####[430]####
    }//####[431]####
//####[433]####
    private int buttonSize = 80;//####[433]####
//####[435]####
    private JButton makeButton(String icon, Action action, String tooltip) {//####[435]####
        JButton btn = new JButton(action);//####[436]####
        btn.setToolTipText(tooltip);//####[437]####
        btn.setIcon(new ImageIcon(icon));//####[438]####
        btn.setPreferredSize(new Dimension(buttonSize, buttonSize));//####[439]####
        return btn;//####[440]####
    }//####[441]####
//####[443]####
    private void addToolButtonsPanel() {//####[443]####
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));//####[444]####
        panel.add(makeButton("images/add.png", actionAddImage, "Add more image(s) to the project"));//####[446]####
        panel.add(makeButton("images/saveimage.png", actionSaveSelected, "Apply changes to the selected image(s)"));//####[447]####
        panel.add(makeButton("images/undo.png", actionUndo, "Undo changes to the selected image(s)"));//####[448]####
        panel.add(makeButton("images/remove.png", actionRemoveImage, "Remove selected image(s) from view"));//####[449]####
        panel.add(makeButton("images/gradient.png", actionApplyEdge, "Edge detect on the selected image(s)"));//####[450]####
        panel.add(makeButton("images/video.png", actionInvert, "Invert colors on the selected image(s)"));//####[451]####
        panel.add(makeButton("images/blur.png", actionBlur, "Blur the selected image(s)"));//####[452]####
        panel.add(makeButton("images/sharpen.png", actionSharpen, "Sharpen the selected image(s)"));//####[453]####
        panel.add(makeButton("images/canvas.png", actionBuildMosaic, "Build a mosaic of the selected image(s)"));//####[454]####
        panel.add(makeButton("images/artwork.png", actionBuildImageMosaic, "Build an image mosaic of the selected image(s)"));//####[455]####
        panel.add(makeButton("images/palette.png", actionBuildPalette, "Build the palette to be used to make image mosaics"));//####[456]####
        panel.add(makeButton("images/clearPalette.png", actionClearPalette, "Clear the palette of images"));//####[457]####
        panel.add(makeButton("images/settings.png", actionMosaicSettings, "Modify attributes related to building mosaics"));//####[458]####
        JPanel grp = new JPanel(new GridLayout(3, 1));//####[460]####
        grp.add(new JLabel("Select..", JLabel.CENTER));//####[461]####
        JButton btnAll = new JButton(actionSelectAll);//####[462]####
        btnAll.setText("All");//####[463]####
        btnAll.setToolTipText("Select all image(s)");//####[464]####
        grp.add(btnAll);//####[465]####
        JButton btnNone = new JButton(actionSelectNone);//####[466]####
        btnNone.setText("None");//####[467]####
        btnNone.setToolTipText("Deselect all image(s)");//####[468]####
        grp.add(btnNone);//####[469]####
        grp.setPreferredSize(new Dimension(buttonSize, buttonSize));//####[470]####
        panel.add(grp);//####[471]####
        add(panel, BorderLayout.NORTH);//####[473]####
        updateActions();//####[475]####
    }//####[476]####
//####[478]####
    private Action actionBuildMosaic = new AbstractAction() {//####[478]####
//####[481]####
        @Override//####[481]####
        public void actionPerformed(ActionEvent arg0) {//####[481]####
            Timer timer = new Timer(getSelectedPanels().size(), "Build Mosaic");//####[482]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[483]####
            while (it.hasNext()) //####[484]####
            {//####[484]####
                ImagePanelItem panel = it.next();//####[485]####
                if (mainFrame.isParallel) //####[487]####
                {//####[487]####
                    TaskInfo __pt__id = new TaskInfo();//####[488]####
//####[488]####
                    /*  -- ParaTask dependsOn clause for 'id' -- *///####[488]####
                    __pt__id.addDependsOn(panel.getHistory());//####[490]####
//####[490]####
                    boolean isEDT = SwingUtilities.isEventDispatchThread();//####[490]####
//####[490]####
//####[490]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[490]####
                    try {//####[490]####
                        Method __pt__id_slot_0 = null;//####[490]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { TaskID.class });//####[490]####
                        TaskID __pt__id_slot_0_dummy_0 = null;//####[490]####
                        if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[490]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, isEDT, false));//####[490]####
//####[490]####
                        Method __pt__id_slot_1 = null;//####[490]####
                        __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[490]####
                        if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[490]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, isEDT, false));//####[490]####
//####[490]####
                        Method __pt__id_slot_2 = null;//####[490]####
                        __pt__id_slot_2 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[490]####
                        if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[490]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_2, timer, isEDT, false));//####[490]####
//####[490]####
                    } catch(Exception __pt__e) { //####[490]####
                        System.err.println("Problem registering method in clause:");//####[490]####
                        __pt__e.printStackTrace();//####[490]####
                    }//####[490]####
                    TaskID<ImageCombo> id = MosaicBuilder.buildMosaicTask(panel, density, size, __pt__id);//####[490]####
                    panel.addToHistory(id);//####[491]####
                } else {//####[492]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[493]####
                    ImageCombo res = MosaicBuilder.buildMosaic(panel, density, size);//####[494]####
                    panel.setImage(res);//####[495]####
                    guiChanged();//####[496]####
                    setCursor(Cursor.getDefaultCursor());//####[497]####
                    timer.taskComplete();//####[498]####
                }//####[499]####
            }//####[500]####
        }//####[501]####
    };//####[501]####
//####[504]####
    private Action actionBuildImageMosaic = new AbstractAction() {//####[504]####
//####[507]####
        @Override//####[507]####
        public void actionPerformed(ActionEvent arg0) {//####[507]####
            Timer timer = new Timer(getSelectedPanels().size(), "Build Image Mosaic");//####[508]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[509]####
            while (it.hasNext()) //####[510]####
            {//####[510]####
                ImagePanelItem panel = it.next();//####[511]####
                if (mainFrame.isParallel) //####[513]####
                {//####[513]####
                    if (parallelism == 1) //####[514]####
                    {//####[514]####
                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[515]####
                        ImageCombo res = MosaicBuilder.buildImageMosaic2(panel, palette, density);//####[516]####
                        panel.setImage(res);//####[517]####
                        guiChanged();//####[518]####
                        setCursor(Cursor.getDefaultCursor());//####[519]####
                        timer.taskComplete();//####[520]####
                    } else if (parallelism == 2) //####[521]####
                    {//####[521]####
                        TaskInfo __pt__id = new TaskInfo();//####[522]####
//####[522]####
                        /*  -- ParaTask dependsOn clause for 'id' -- *///####[522]####
                        __pt__id.addDependsOn(panel.getHistory());//####[524]####
//####[524]####
                        boolean isEDT = SwingUtilities.isEventDispatchThread();//####[524]####
//####[524]####
//####[524]####
                        /*  -- ParaTask notify clause for 'id' -- *///####[524]####
                        try {//####[524]####
                            Method __pt__id_slot_0 = null;//####[524]####
                            __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { TaskID.class });//####[524]####
                            TaskID __pt__id_slot_0_dummy_0 = null;//####[524]####
                            if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[524]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, isEDT, false));//####[524]####
//####[524]####
                            Method __pt__id_slot_1 = null;//####[524]####
                            __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[524]####
                            if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[524]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, isEDT, false));//####[524]####
//####[524]####
                            Method __pt__id_slot_2 = null;//####[524]####
                            __pt__id_slot_2 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[524]####
                            if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[524]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_2, timer, isEDT, false));//####[524]####
//####[524]####
                        } catch(Exception __pt__e) { //####[524]####
                            System.err.println("Problem registering method in clause:");//####[524]####
                            __pt__e.printStackTrace();//####[524]####
                        }//####[524]####
                        TaskID<ImageCombo> id = MosaicBuilder.buildImageMosaicTask(panel, palette, density, parallelism, __pt__id);//####[524]####
                        panel.addToHistory(id);//####[525]####
                    } else if (parallelism == 3) //####[526]####
                    {//####[526]####
                        TaskInfo __pt__id = new TaskInfo();//####[527]####
//####[527]####
                        /*  -- ParaTask dependsOn clause for 'id' -- *///####[527]####
                        __pt__id.addDependsOn(panel.getHistory());//####[529]####
//####[529]####
                        boolean isEDT = SwingUtilities.isEventDispatchThread();//####[529]####
//####[529]####
//####[529]####
                        /*  -- ParaTask notify clause for 'id' -- *///####[529]####
                        try {//####[529]####
                            Method __pt__id_slot_0 = null;//####[529]####
                            __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { TaskID.class });//####[529]####
                            TaskID __pt__id_slot_0_dummy_0 = null;//####[529]####
                            if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[529]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, isEDT, false));//####[529]####
//####[529]####
                            Method __pt__id_slot_1 = null;//####[529]####
                            __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[529]####
                            if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[529]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, isEDT, false));//####[529]####
//####[529]####
                            Method __pt__id_slot_2 = null;//####[529]####
                            __pt__id_slot_2 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[529]####
                            if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[529]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_2, timer, isEDT, false));//####[529]####
//####[529]####
                        } catch(Exception __pt__e) { //####[529]####
                            System.err.println("Problem registering method in clause:");//####[529]####
                            __pt__e.printStackTrace();//####[529]####
                        }//####[529]####
                        TaskID<ImageCombo> id = MosaicBuilder.buildImageMosaicTask3(panel, palette, density, parallelism, __pt__id);//####[529]####
                        panel.addToHistory(id);//####[530]####
                    } else if (parallelism == 4) //####[531]####
                    {//####[531]####
                        TaskInfo __pt__id = new TaskInfo();//####[532]####
//####[532]####
                        /*  -- ParaTask dependsOn clause for 'id' -- *///####[532]####
                        __pt__id.addDependsOn(panel.getHistory());//####[534]####
//####[534]####
                        boolean isEDT = SwingUtilities.isEventDispatchThread();//####[534]####
//####[534]####
//####[534]####
                        /*  -- ParaTask notify clause for 'id' -- *///####[534]####
                        try {//####[534]####
                            Method __pt__id_slot_0 = null;//####[534]####
                            __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { TaskID.class });//####[534]####
                            TaskID __pt__id_slot_0_dummy_0 = null;//####[534]####
                            if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[534]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, isEDT, false));//####[534]####
//####[534]####
                            Method __pt__id_slot_1 = null;//####[534]####
                            __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[534]####
                            if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[534]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, isEDT, false));//####[534]####
//####[534]####
                            Method __pt__id_slot_2 = null;//####[534]####
                            __pt__id_slot_2 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[534]####
                            if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[534]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_2, timer, isEDT, false));//####[534]####
//####[534]####
                        } catch(Exception __pt__e) { //####[534]####
                            System.err.println("Problem registering method in clause:");//####[534]####
                            __pt__e.printStackTrace();//####[534]####
                        }//####[534]####
                        TaskID<ImageCombo> id = MosaicBuilder.buildImageMosaicTask4(panel, palette, density, parallelism, __pt__id);//####[534]####
                        panel.addToHistory(id);//####[535]####
                    } else {//####[536]####
                        TaskInfo __pt__id = new TaskInfo();//####[537]####
//####[537]####
                        /*  -- ParaTask dependsOn clause for 'id' -- *///####[537]####
                        __pt__id.addDependsOn(panel.getHistory());//####[539]####
//####[539]####
                        boolean isEDT = SwingUtilities.isEventDispatchThread();//####[539]####
//####[539]####
//####[539]####
                        /*  -- ParaTask notify clause for 'id' -- *///####[539]####
                        try {//####[539]####
                            Method __pt__id_slot_0 = null;//####[539]####
                            __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { TaskID.class });//####[539]####
                            TaskID __pt__id_slot_0_dummy_0 = null;//####[539]####
                            if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[539]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, isEDT, false));//####[539]####
//####[539]####
                            Method __pt__id_slot_1 = null;//####[539]####
                            __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[539]####
                            if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[539]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, isEDT, false));//####[539]####
//####[539]####
                            Method __pt__id_slot_2 = null;//####[539]####
                            __pt__id_slot_2 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[539]####
                            if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[539]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_2, timer, isEDT, false));//####[539]####
//####[539]####
                        } catch(Exception __pt__e) { //####[539]####
                            System.err.println("Problem registering method in clause:");//####[539]####
                            __pt__e.printStackTrace();//####[539]####
                        }//####[539]####
                        TaskID<ImageCombo> id = MosaicBuilder.buildImageMosaicTask2(panel, palette, density, parallelism, __pt__id);//####[539]####
                        panel.addToHistory(id);//####[540]####
                    }//####[541]####
                } else {//####[542]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[543]####
                    ImageCombo res = MosaicBuilder.buildImageMosaic(panel, palette, density);//####[544]####
                    panel.setImage(res);//####[545]####
                    guiChanged();//####[546]####
                    setCursor(Cursor.getDefaultCursor());//####[547]####
                    timer.taskComplete();//####[548]####
                }//####[549]####
            }//####[550]####
        }//####[551]####
    };//####[551]####
//####[554]####
    private Action actionBuildPalette = new AbstractAction() {//####[554]####
//####[557]####
        @Override//####[557]####
        public void actionPerformed(ActionEvent arg0) {//####[557]####
            Timer timer = new Timer(getSelectedPanels().size(), "Build Palette");//####[558]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[559]####
            while (it.hasNext()) //####[560]####
            {//####[560]####
                ImagePanelItem panel = it.next();//####[561]####
                if (mainFrame.isParallel) //####[563]####
                {//####[563]####
                    TaskInfo __pt__id = new TaskInfo();//####[564]####
//####[564]####
                    boolean isEDT = SwingUtilities.isEventDispatchThread();//####[564]####
//####[564]####
//####[564]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[564]####
                    try {//####[564]####
                        Method __pt__id_slot_0 = null;//####[564]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[565]####
                        if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[565]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, ImageProjectPanel.this, isEDT, false));//####[565]####
//####[565]####
                        Method __pt__id_slot_1 = null;//####[565]####
                        __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[565]####
                        if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[565]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, timer, isEDT, false));//####[565]####
//####[565]####
                    } catch(Exception __pt__e) { //####[565]####
                        System.err.println("Problem registering method in clause:");//####[565]####
                        __pt__e.printStackTrace();//####[565]####
                    }//####[565]####
                    TaskID<List<PaletteItem>> id = MosaicBuilder.buildMosaicPaletteItemTask(panel, palette, size, __pt__id);//####[565]####
                    try {//####[566]####
                        palette = id.getReturnResult();//####[567]####
                    } catch (ExecutionException e) {//####[568]####
                        e.printStackTrace();//####[569]####
                    } catch (InterruptedException e) {//####[570]####
                        e.printStackTrace();//####[571]####
                    }//####[572]####
                } else {//####[574]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[575]####
                    palette = MosaicBuilder.buildMosaicPaletteItem(panel, palette, size);//####[576]####
                    guiChanged();//####[577]####
                    setCursor(Cursor.getDefaultCursor());//####[578]####
                    timer.taskComplete();//####[579]####
                }//####[580]####
            }//####[581]####
        }//####[582]####
    };//####[582]####
//####[585]####
    private Action actionClearPalette = new AbstractAction() {//####[585]####
//####[588]####
        @Override//####[588]####
        public void actionPerformed(ActionEvent arg0) {//####[588]####
            Timer timer = new Timer("Clear Palette");//####[589]####
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[590]####
            palette.clear();//####[591]####
            guiChanged();//####[592]####
            setCursor(Cursor.getDefaultCursor());//####[593]####
            timer.taskComplete();//####[594]####
        }//####[595]####
    };//####[595]####
//####[598]####
    private Action actionMosaicSettings = new AbstractAction() {//####[598]####
//####[601]####
        @Override//####[601]####
        public void actionPerformed(ActionEvent arg0) {//####[601]####
            JPanel panel = new JPanel();//####[602]####
            JSlider parallelismSlider = new JSlider(JSlider.HORIZONTAL, 1, 5, parallelism);//####[603]####
            parallelismSlider.setBorder(BorderFactory.createTitledBorder("Parallelism Level"));//####[605]####
            parallelismSlider.setMajorTickSpacing(2);//####[606]####
            parallelismSlider.setMinorTickSpacing(1);//####[607]####
            parallelismSlider.setPaintTicks(true);//####[608]####
            parallelismSlider.setPaintLabels(true);//####[609]####
            class ParallelismSliderListener implements ChangeListener {//####[611]####
//####[611]####
                /*  ParaTask helper method to access private/protected slots *///####[611]####
                public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[611]####
                    if (m.getParameterTypes().length == 0)//####[611]####
                        m.invoke(instance);//####[611]####
                    else if ((m.getParameterTypes().length == 1))//####[611]####
                        m.invoke(instance, arg);//####[611]####
                    else //####[611]####
                        m.invoke(instance, arg, interResult);//####[611]####
                }//####[611]####
//####[612]####
                public void stateChanged(ChangeEvent e) {//####[612]####
                    JSlider source = (JSlider) e.getSource();//####[613]####
                    if (!source.getValueIsAdjusting()) //####[614]####
                    {//####[614]####
                        parallelism = (int) source.getValue();//####[615]####
                    }//####[616]####
                }//####[617]####
            }//####[617]####
            parallelismSlider.addChangeListener(new ParallelismSliderListener());//####[620]####
            panel.add(parallelismSlider);//####[621]####
            JSlider densitySlider = new JSlider(JSlider.HORIZONTAL, 1, 64, density);//####[623]####
            densitySlider.setBorder(BorderFactory.createTitledBorder("Mosaic Density"));//####[625]####
            densitySlider.setMajorTickSpacing(63);//####[626]####
            densitySlider.setMinorTickSpacing(3);//####[627]####
            densitySlider.setPaintTicks(true);//####[628]####
            densitySlider.setPaintLabels(true);//####[629]####
            class DensitySliderListener implements ChangeListener {//####[631]####
//####[631]####
                /*  ParaTask helper method to access private/protected slots *///####[631]####
                public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[631]####
                    if (m.getParameterTypes().length == 0)//####[631]####
                        m.invoke(instance);//####[631]####
                    else if ((m.getParameterTypes().length == 1))//####[631]####
                        m.invoke(instance, arg);//####[631]####
                    else //####[631]####
                        m.invoke(instance, arg, interResult);//####[631]####
                }//####[631]####
//####[632]####
                public void stateChanged(ChangeEvent e) {//####[632]####
                    JSlider source = (JSlider) e.getSource();//####[633]####
                    if (!source.getValueIsAdjusting()) //####[634]####
                    {//####[634]####
                        density = (int) source.getValue();//####[635]####
                    }//####[636]####
                }//####[637]####
            }//####[637]####
            densitySlider.addChangeListener(new DensitySliderListener());//####[640]####
            panel.add(densitySlider);//####[641]####
            JSlider sizeSlider = new JSlider(JSlider.HORIZONTAL, 1, 64, size);//####[643]####
            sizeSlider.setBorder(BorderFactory.createTitledBorder("Paint Size (requires new palette)"));//####[645]####
            sizeSlider.setMajorTickSpacing(63);//####[646]####
            sizeSlider.setMinorTickSpacing(3);//####[647]####
            sizeSlider.setPaintTicks(true);//####[648]####
            sizeSlider.setPaintLabels(true);//####[649]####
            class SizeSliderListener implements ChangeListener {//####[651]####
//####[651]####
                /*  ParaTask helper method to access private/protected slots *///####[651]####
                public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[651]####
                    if (m.getParameterTypes().length == 0)//####[651]####
                        m.invoke(instance);//####[651]####
                    else if ((m.getParameterTypes().length == 1))//####[651]####
                        m.invoke(instance, arg);//####[651]####
                    else //####[651]####
                        m.invoke(instance, arg, interResult);//####[651]####
                }//####[651]####
//####[652]####
                public void stateChanged(ChangeEvent e) {//####[652]####
                    JSlider source = (JSlider) e.getSource();//####[653]####
                    if (!source.getValueIsAdjusting()) //####[654]####
                    {//####[654]####
                        size = (int) source.getValue();//####[655]####
                    }//####[656]####
                }//####[657]####
            }//####[657]####
            sizeSlider.addChangeListener(new SizeSliderListener());//####[660]####
            panel.add(sizeSlider);//####[661]####
            JOptionPane.showConfirmDialog(ImageProjectPanel.this, panel, "Mosaic Settings", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);//####[663]####
        }//####[664]####
    };//####[664]####
}//####[664]####
