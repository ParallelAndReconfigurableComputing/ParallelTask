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
import java.util.Iterator;//####[13]####
import java.util.List;//####[14]####
import javax.swing.AbstractAction;//####[16]####
import javax.swing.Action;//####[17]####
import javax.swing.ImageIcon;//####[18]####
import javax.swing.JButton;//####[19]####
import javax.swing.JFileChooser;//####[20]####
import javax.swing.JLabel;//####[21]####
import javax.swing.JPanel;//####[22]####
import javax.swing.JScrollPane;//####[23]####
import javax.swing.UIManager;//####[24]####
import paratask.runtime.*;//####[26]####
//####[26]####
//-- ParaTask related imports//####[26]####
import paratask.runtime.*;//####[26]####
import java.util.concurrent.ExecutionException;//####[26]####
import java.util.concurrent.locks.*;//####[26]####
import java.lang.reflect.*;//####[26]####
import javax.swing.SwingUtilities;//####[26]####
//####[26]####
public class ImageProjectPanel extends ProjectPanel {//####[28]####
//####[28]####
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
    private JPanel thumbnailsPanel;//####[30]####
//####[32]####
    private Method __pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_method = null;//####[32]####
    private Lock __pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_lock = new ReentrantLock();//####[32]####
    private TaskID<Void> addToThumbnailsPanelTask(final File file, final TaskID<Image> large, final TaskID<Image> square, final TaskID<Image> med)  {//####[32]####
//####[32]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[32]####
        return addToThumbnailsPanelTask(file, large, square, med, new TaskInfo());//####[32]####
    }//####[32]####
    private TaskID<Void> addToThumbnailsPanelTask(final File file, final TaskID<Image> large, final TaskID<Image> square, final TaskID<Image> med, TaskInfo taskinfo)  {//####[32]####
        if (__pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_method == null) {//####[32]####
            try {//####[32]####
                __pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_lock.lock();//####[32]####
                if (__pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_method == null) //####[32]####
                    __pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_method = ParaTaskHelper.getDeclaredMethod(getClass(), "__pt__addToThumbnailsPanelTask", new Class[] {File.class, TaskID.class, TaskID.class, TaskID.class});//####[32]####
            } catch (Exception e) {//####[32]####
                e.printStackTrace();//####[32]####
            } finally {//####[32]####
                __pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_lock.unlock();//####[32]####
            }//####[32]####
        }//####[32]####
//####[32]####
        Object[] args = new Object[] {file, large, square, med};//####[32]####
        taskinfo.setTaskArguments(args);//####[32]####
        taskinfo.setMethod(__pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_method);//####[32]####
        taskinfo.setInstance(this);//####[32]####
//####[32]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[32]####
    }//####[32]####
    public void __pt__addToThumbnailsPanelTask(final File file, final TaskID<Image> large, final TaskID<Image> square, final TaskID<Image> med) {//####[32]####
        SwingUtilities.invokeLater(new Runnable() {//####[32]####
//####[35]####
            @Override//####[35]####
            public void run() {//####[35]####
                try {//####[36]####
                    addToThumbnailsPanel(file, large.getReturnResult(), square.getReturnResult(), med.getReturnResult());//####[37]####
                } catch (ExecutionException e) {//####[38]####
                    e.printStackTrace();//####[39]####
                } catch (InterruptedException e) {//####[40]####
                    e.printStackTrace();//####[41]####
                }//####[42]####
            }//####[43]####
        });//####[43]####
    }//####[45]####
//####[45]####
//####[47]####
    private void addToThumbnailsPanel(File file, Image large, Image square, Image medium) {//####[47]####
        thumbnailsPanel.add(new ImagePanelItem(file, large, square, medium, ImageProjectPanel.this));//####[48]####
        updateUI();//####[49]####
    }//####[50]####
//####[52]####
    private Method __pt__finishedAddingNewPanelItemsTask_method = null;//####[52]####
    private Lock __pt__finishedAddingNewPanelItemsTask_lock = new ReentrantLock();//####[52]####
    private TaskID<Void> finishedAddingNewPanelItemsTask()  {//####[52]####
//####[52]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[52]####
        return finishedAddingNewPanelItemsTask(new TaskInfo());//####[52]####
    }//####[52]####
    private TaskID<Void> finishedAddingNewPanelItemsTask(TaskInfo taskinfo)  {//####[52]####
        if (__pt__finishedAddingNewPanelItemsTask_method == null) {//####[52]####
            try {//####[52]####
                __pt__finishedAddingNewPanelItemsTask_lock.lock();//####[52]####
                if (__pt__finishedAddingNewPanelItemsTask_method == null) //####[52]####
                    __pt__finishedAddingNewPanelItemsTask_method = ParaTaskHelper.getDeclaredMethod(getClass(), "__pt__finishedAddingNewPanelItemsTask", new Class[] {});//####[52]####
            } catch (Exception e) {//####[52]####
                e.printStackTrace();//####[52]####
            } finally {//####[52]####
                __pt__finishedAddingNewPanelItemsTask_lock.unlock();//####[52]####
            }//####[52]####
        }//####[52]####
//####[52]####
        taskinfo.setMethod(__pt__finishedAddingNewPanelItemsTask_method);//####[52]####
        taskinfo.setInstance(this);//####[52]####
//####[52]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[52]####
    }//####[52]####
    public void __pt__finishedAddingNewPanelItemsTask() {//####[52]####
        SwingUtilities.invokeLater(new Runnable() {//####[52]####
//####[55]####
            @Override//####[55]####
            public void run() {//####[55]####
                finishedAddingNewPanelItems();//####[56]####
            }//####[57]####
        });//####[57]####
    }//####[59]####
//####[59]####
//####[61]####
    private void finishedAddingNewPanelItems() {//####[61]####
        isModified = true;//####[62]####
        updateActions();//####[63]####
        thumbnailsPanel.updateUI();//####[64]####
        mainFrame.updateTabIcons();//####[65]####
        mainFrame.updateProjectActions();//####[66]####
    }//####[67]####
//####[69]####
    private Action actionAddImage = new AbstractAction() {//####[69]####
//####[72]####
        @Override//####[72]####
        public void actionPerformed(ActionEvent arg0) {//####[72]####
            UIManager.put("FileChooser.readOnly", Boolean.TRUE);//####[73]####
            JFileChooser fc = new JFileChooser(projectDir);//####[74]####
            fc.setMultiSelectionEnabled(true);//####[75]####
            fc.setAcceptAllFileFilterUsed(false);//####[76]####
            fc.addChoosableFileFilter(new ImageFilter());//####[77]####
            int retValue = fc.showOpenDialog(ImageProjectPanel.this);//####[78]####
            if (retValue == JFileChooser.APPROVE_OPTION) //####[79]####
            {//####[79]####
                File[] inputImages = fc.getSelectedFiles();//####[80]####
                if (MainFrame.isParallel) //####[81]####
                {//####[81]####
                    TaskIDGroup grp = new TaskIDGroup(inputImages.length);//####[82]####
                    for (int i = 0; i < inputImages.length; i++) //####[83]####
                    {//####[83]####
                        TaskID<Image> idImage = ImageManipulation.getImageFullTask(inputImages[i]);//####[84]####
                        TaskInfo __pt__idMedium = new TaskInfo();//####[85]####
//####[85]####
                        /*  -- ParaTask dependsOn clause for 'idMedium' -- *///####[85]####
                        __pt__idMedium.addDependsOn(idImage);//####[85]####
//####[85]####
                        TaskID<Image> idMedium = ImageManipulation.getMediumTask(idImage, __pt__idMedium);//####[85]####
                        TaskInfo __pt__idSmall = new TaskInfo();//####[86]####
//####[86]####
                        /*  -- ParaTask dependsOn clause for 'idSmall' -- *///####[86]####
                        __pt__idSmall.addDependsOn(idImage);//####[86]####
//####[86]####
                        TaskID<Image> idSmall = ImageManipulation.getSmallSquareTask(idImage, __pt__idSmall);//####[86]####
                        TaskInfo __pt__id = new TaskInfo();//####[87]####
//####[87]####
                        /*  -- ParaTask dependsOn clause for 'id' -- *///####[87]####
                        __pt__id.addDependsOn(idSmall);//####[87]####
                        __pt__id.addDependsOn(idMedium);//####[87]####
//####[87]####
                        TaskID id = addToThumbnailsPanelTask(inputImages[i], idImage, idSmall, idMedium, __pt__id);//####[87]####
                        grp.add(id);//####[88]####
                    }//####[89]####
                    TaskInfo __pt__finalTask = new TaskInfo();//####[90]####
//####[90]####
                    /*  -- ParaTask dependsOn clause for 'finalTask' -- *///####[90]####
                    __pt__finalTask.addDependsOn(grp);//####[90]####
//####[90]####
                    TaskID finalTask = finishedAddingNewPanelItemsTask(__pt__finalTask);//####[90]####
                } else {//####[91]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[92]####
                    for (int i = 0; i < inputImages.length; i++) //####[93]####
                    {//####[93]####
                        Image large = ImageManipulation.getImageFull(inputImages[i]);//####[94]####
                        Image small = ImageManipulation.getSmallSquare(large);//####[95]####
                        Image medium = ImageManipulation.getMedium(large);//####[96]####
                        addToThumbnailsPanel(inputImages[i], large, small, medium);//####[97]####
                    }//####[98]####
                    finishedAddingNewPanelItems();//####[99]####
                    setCursor(Cursor.getDefaultCursor());//####[100]####
                }//####[101]####
            }//####[102]####
        }//####[103]####
    };//####[103]####
//####[107]####
    private Action actionUndo = new AbstractAction() {//####[107]####
//####[109]####
        @Override//####[109]####
        public void actionPerformed(ActionEvent arg0) {//####[109]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[110]####
            while (it.hasNext()) //####[111]####
            {//####[111]####
                ImagePanelItem panel = it.next();//####[112]####
                panel.restore();//####[113]####
            }//####[114]####
            updateActions();//####[115]####
        }//####[116]####
    };//####[116]####
//####[120]####
    private Action actionSelectAll = new AbstractAction() {//####[120]####
//####[122]####
        @Override//####[122]####
        public void actionPerformed(ActionEvent arg0) {//####[122]####
            Component[] comps = thumbnailsPanel.getComponents();//####[123]####
            for (int i = 0; i < comps.length; i++) //####[124]####
            {//####[124]####
                ImagePanelItem panel = (ImagePanelItem) comps[i];//####[125]####
                panel.setSelected(true);//####[126]####
            }//####[127]####
            updateActions();//####[128]####
        }//####[129]####
    };//####[129]####
//####[132]####
    private List<ImagePanelItem> getAllPanels() {//####[132]####
        ArrayList<ImagePanelItem> list = new ArrayList<ImagePanelItem>();//####[133]####
        if (thumbnailsPanel != null) //####[135]####
        {//####[135]####
            Component[] comps = thumbnailsPanel.getComponents();//####[136]####
            for (int i = 0; i < comps.length; i++) //####[137]####
            {//####[137]####
                ImagePanelItem panel = (ImagePanelItem) comps[i];//####[138]####
                list.add(panel);//####[139]####
            }//####[140]####
        }//####[141]####
        return list;//####[143]####
    }//####[144]####
//####[146]####
    private List<ImagePanelItem> getSelectedPanels() {//####[146]####
        ArrayList<ImagePanelItem> list = new ArrayList<ImagePanelItem>();//####[147]####
        Component[] comps = thumbnailsPanel.getComponents();//####[149]####
        for (int i = 0; i < comps.length; i++) //####[150]####
        {//####[150]####
            ImagePanelItem panel = (ImagePanelItem) comps[i];//####[151]####
            if (panel.isSelected()) //####[152]####
            list.add(panel);//####[153]####
        }//####[154]####
        return list;//####[155]####
    }//####[156]####
//####[158]####
    private Action actionSelectNone = new AbstractAction() {//####[158]####
//####[160]####
        @Override//####[160]####
        public void actionPerformed(ActionEvent arg0) {//####[160]####
            Component[] comps = thumbnailsPanel.getComponents();//####[161]####
            for (int i = 0; i < comps.length; i++) //####[162]####
            {//####[162]####
                ImagePanelItem panel = (ImagePanelItem) comps[i];//####[163]####
                panel.setSelected(false);//####[164]####
            }//####[165]####
            updateActions();//####[166]####
        }//####[167]####
    };//####[167]####
//####[170]####
    private void disableButtons() {//####[170]####
        actionInvert.setEnabled(false);//####[171]####
        actionApplyEdge.setEnabled(false);//####[172]####
        actionBlur.setEnabled(false);//####[173]####
        actionSharpen.setEnabled(false);//####[174]####
    }//####[175]####
//####[177]####
    private void enableButtons() {//####[177]####
        actionInvert.setEnabled(true);//####[178]####
        actionApplyEdge.setEnabled(true);//####[179]####
        actionBlur.setEnabled(true);//####[180]####
        actionSharpen.setEnabled(true);//####[181]####
    }//####[182]####
//####[184]####
    private Action actionInvert = new AbstractAction() {//####[184]####
//####[186]####
        @Override//####[186]####
        public void actionPerformed(ActionEvent arg0) {//####[186]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[188]####
            while (it.hasNext()) //####[190]####
            {//####[190]####
                ImagePanelItem panel = it.next();//####[191]####
                if (mainFrame.isParallel) //####[193]####
                {//####[193]####
                    TaskInfo __pt__id = new TaskInfo();//####[195]####
//####[195]####
                    /*  -- ParaTask dependsOn clause for 'id' -- *///####[195]####
                    __pt__id.addDependsOn(panel.getHistory());//####[197]####
//####[197]####
                    boolean isEDT = SwingUtilities.isEventDispatchThread();//####[197]####
//####[197]####
//####[197]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[197]####
                    try {//####[197]####
                        Method __pt__id_slot_0 = null;//####[197]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { TaskID.class });//####[197]####
                        TaskID __pt__id_slot_0_dummy_0 = null;//####[197]####
                        if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[197]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, isEDT, false));//####[197]####
//####[197]####
                        Method __pt__id_slot_1 = null;//####[197]####
                        __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[197]####
                        if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[197]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, isEDT, false));//####[197]####
//####[197]####
                    } catch(Exception __pt__e) { //####[197]####
                        System.err.println("Problem registering method in clause:");//####[197]####
                        __pt__e.printStackTrace();//####[197]####
                    }//####[197]####
                    TaskID<ImageCombo> id = ImageManipulation.invertTask(panel, __pt__id);//####[197]####
                    panel.addToHistory(id);//####[198]####
                } else {//####[201]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[203]####
                    ImageCombo res = ImageManipulation.invert(panel);//####[205]####
                    panel.setImage(res);//####[206]####
                    guiChanged();//####[207]####
                    setCursor(Cursor.getDefaultCursor());//####[208]####
                }//####[209]####
            }//####[210]####
        }//####[211]####
    };//####[211]####
//####[214]####
    private void guiChanged() {//####[214]####
        isModified = true;//####[215]####
        updateActions();//####[216]####
        thumbnailsPanel.updateUI();//####[217]####
        mainFrame.updateTabIcons();//####[218]####
        mainFrame.updateProjectActions();//####[219]####
    }//####[220]####
//####[222]####
    private Action actionBlur = new AbstractAction() {//####[222]####
//####[224]####
        @Override//####[224]####
        public void actionPerformed(ActionEvent arg0) {//####[224]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[226]####
            while (it.hasNext()) //####[230]####
            {//####[230]####
                ImagePanelItem panel = it.next();//####[231]####
                if (mainFrame.isParallel) //####[233]####
                {//####[233]####
                    TaskInfo __pt__id = new TaskInfo();//####[235]####
//####[235]####
                    /*  -- ParaTask dependsOn clause for 'id' -- *///####[235]####
                    __pt__id.addDependsOn(panel.getHistory());//####[237]####
//####[237]####
                    boolean isEDT = SwingUtilities.isEventDispatchThread();//####[237]####
//####[237]####
//####[237]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[237]####
                    try {//####[237]####
                        Method __pt__id_slot_0 = null;//####[237]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { TaskID.class });//####[237]####
                        TaskID __pt__id_slot_0_dummy_0 = null;//####[237]####
                        if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[237]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, isEDT, false));//####[237]####
//####[237]####
                        Method __pt__id_slot_1 = null;//####[237]####
                        __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[237]####
                        if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[237]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, isEDT, false));//####[237]####
//####[237]####
                    } catch(Exception __pt__e) { //####[237]####
                        System.err.println("Problem registering method in clause:");//####[237]####
                        __pt__e.printStackTrace();//####[237]####
                    }//####[237]####
                    TaskID<ImageCombo> id = ImageManipulation.blurTask(panel, __pt__id);//####[237]####
                    panel.addToHistory(id);//####[238]####
                } else {//####[241]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[242]####
                    ImageCombo res = ImageManipulation.blur(panel);//####[243]####
                    panel.setImage(res);//####[244]####
                    guiChanged();//####[245]####
                    setCursor(Cursor.getDefaultCursor());//####[246]####
                }//####[247]####
            }//####[248]####
        }//####[249]####
    };//####[249]####
//####[252]####
    private Action actionSharpen = new AbstractAction() {//####[252]####
//####[254]####
        @Override//####[254]####
        public void actionPerformed(ActionEvent arg0) {//####[254]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[256]####
            while (it.hasNext()) //####[260]####
            {//####[260]####
                ImagePanelItem panel = it.next();//####[261]####
                if (mainFrame.isParallel) //####[263]####
                {//####[263]####
                    TaskInfo __pt__id = new TaskInfo();//####[265]####
//####[265]####
                    /*  -- ParaTask dependsOn clause for 'id' -- *///####[265]####
                    __pt__id.addDependsOn(panel.getHistory());//####[267]####
//####[267]####
                    boolean isEDT = SwingUtilities.isEventDispatchThread();//####[267]####
//####[267]####
//####[267]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[267]####
                    try {//####[267]####
                        Method __pt__id_slot_0 = null;//####[267]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { TaskID.class });//####[267]####
                        TaskID __pt__id_slot_0_dummy_0 = null;//####[267]####
                        if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[267]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, isEDT, false));//####[267]####
//####[267]####
                        Method __pt__id_slot_1 = null;//####[267]####
                        __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[267]####
                        if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[267]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, isEDT, false));//####[267]####
//####[267]####
                    } catch(Exception __pt__e) { //####[267]####
                        System.err.println("Problem registering method in clause:");//####[267]####
                        __pt__e.printStackTrace();//####[267]####
                    }//####[267]####
                    TaskID<ImageCombo> id = ImageManipulation.sharpenTask(panel, __pt__id);//####[267]####
                    panel.addToHistory(id);//####[268]####
                } else {//####[270]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[271]####
                    ImageCombo res = ImageManipulation.sharpen(panel);//####[272]####
                    panel.setImage(res);//####[273]####
                    guiChanged();//####[274]####
                    setCursor(Cursor.getDefaultCursor());//####[275]####
                }//####[276]####
            }//####[277]####
        }//####[278]####
    };//####[278]####
//####[281]####
    private void savePanels(List<ImagePanelItem> list) {//####[281]####
        Iterator<ImagePanelItem> it = list.iterator();//####[282]####
        while (it.hasNext()) //####[283]####
        {//####[283]####
            ImagePanelItem panel = it.next();//####[284]####
            panel.commit();//####[285]####
        }//####[286]####
        updateActions();//####[287]####
    }//####[288]####
//####[291]####
    @Override//####[291]####
    public void saveProject() {//####[291]####
        super.saveProject();//####[292]####
        savePanels(getAllPanels());//####[293]####
    }//####[294]####
//####[296]####
    private Action actionSaveSelected = new AbstractAction() {//####[296]####
//####[298]####
        @Override//####[298]####
        public void actionPerformed(ActionEvent e) {//####[298]####
            savePanels(getSelectedPanels());//####[300]####
        }//####[301]####
    };//####[301]####
//####[304]####
    private Action actionApplyEdge = new AbstractAction() {//####[304]####
//####[306]####
        @Override//####[306]####
        public void actionPerformed(ActionEvent arg0) {//####[306]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[308]####
            while (it.hasNext()) //####[312]####
            {//####[312]####
                ImagePanelItem panel = it.next();//####[313]####
                if (mainFrame.isParallel) //####[315]####
                {//####[315]####
                    TaskInfo __pt__id = new TaskInfo();//####[317]####
//####[317]####
                    /*  -- ParaTask dependsOn clause for 'id' -- *///####[317]####
                    __pt__id.addDependsOn(panel.getHistory());//####[319]####
//####[319]####
                    boolean isEDT = SwingUtilities.isEventDispatchThread();//####[319]####
//####[319]####
//####[319]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[319]####
                    try {//####[319]####
                        Method __pt__id_slot_0 = null;//####[319]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { TaskID.class });//####[319]####
                        TaskID __pt__id_slot_0_dummy_0 = null;//####[319]####
                        if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[319]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, isEDT, false));//####[319]####
//####[319]####
                        Method __pt__id_slot_1 = null;//####[319]####
                        __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[319]####
                        if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[319]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, isEDT, false));//####[319]####
//####[319]####
                    } catch(Exception __pt__e) { //####[319]####
                        System.err.println("Problem registering method in clause:");//####[319]####
                        __pt__e.printStackTrace();//####[319]####
                    }//####[319]####
                    TaskID<ImageCombo> id = ImageManipulation.edgeDetectTask(panel, __pt__id);//####[319]####
                    panel.addToHistory(id);//####[320]####
                } else {//####[322]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[323]####
                    ImageCombo res = ImageManipulation.edgeDetect(panel);//####[324]####
                    panel.setImage(res);//####[325]####
                    guiChanged();//####[326]####
                    setCursor(Cursor.getDefaultCursor());//####[327]####
                }//####[328]####
            }//####[329]####
        }//####[330]####
    };//####[330]####
//####[333]####
    private Action actionRemoveImage = new AbstractAction() {//####[333]####
//####[335]####
        @Override//####[335]####
        public void actionPerformed(ActionEvent arg0) {//####[335]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[337]####
            if (it.hasNext()) //####[338]####
            isModified = true;//####[339]####
            while (it.hasNext()) //####[340]####
            {//####[340]####
                thumbnailsPanel.remove(it.next());//####[341]####
            }//####[342]####
            updateActions();//####[344]####
            thumbnailsPanel.updateUI();//####[345]####
            mainFrame.updateTabIcons();//####[346]####
            mainFrame.updateProjectActions();//####[347]####
        }//####[348]####
    };//####[348]####
//####[351]####
    private boolean canUndoSomethingSelected() {//####[351]####
        Iterator<ImagePanelItem> it = getAllPanels().iterator();//####[352]####
        while (it.hasNext()) //####[353]####
        {//####[353]####
            ImagePanelItem panel = it.next();//####[354]####
            if (panel.isModified() && panel.isSelected()) //####[355]####
            return true;//####[356]####
        }//####[357]####
        return false;//####[358]####
    }//####[359]####
//####[361]####
    public void updateActions() {//####[361]####
        boolean empty = true;//####[363]####
        boolean somethingSelected = false;//####[364]####
        boolean allSelected = false;//####[365]####
        if (thumbnailsPanel != null) //####[367]####
        {//####[367]####
            Component[] comps = thumbnailsPanel.getComponents();//####[368]####
            if (comps.length != 0) //####[369]####
            {//####[369]####
                empty = false;//####[370]####
                somethingSelected = getSelectedPanels().size() > 0;//####[371]####
                allSelected = getSelectedPanels().size() == comps.length;//####[372]####
            }//####[373]####
        }//####[374]####
        if (!empty) //####[376]####
        {//####[376]####
            actionSelectAll.setEnabled(!allSelected);//####[377]####
            actionRemoveImage.setEnabled(somethingSelected);//####[378]####
            actionSelectNone.setEnabled(somethingSelected);//####[379]####
            actionInvert.setEnabled(somethingSelected);//####[380]####
            actionApplyEdge.setEnabled(somethingSelected);//####[381]####
            actionBlur.setEnabled(somethingSelected);//####[382]####
            actionSharpen.setEnabled(somethingSelected);//####[383]####
        } else {//####[384]####
            actionSelectAll.setEnabled(false);//####[385]####
            actionSelectNone.setEnabled(false);//####[386]####
            actionRemoveImage.setEnabled(false);//####[387]####
            actionInvert.setEnabled(false);//####[388]####
            actionApplyEdge.setEnabled(false);//####[389]####
            actionBlur.setEnabled(false);//####[390]####
            actionSharpen.setEnabled(false);//####[391]####
        }//####[392]####
        actionUndo.setEnabled(canUndoSomethingSelected());//####[393]####
        actionSaveSelected.setEnabled(canUndoSomethingSelected());//####[394]####
    }//####[395]####
//####[397]####
    public ImageProjectPanel(MainFrame mainFrame, String projectName) {//####[397]####
        super(mainFrame, projectName);//####[398]####
        setLayout(new BorderLayout());//####[399]####
        addToolButtonsPanel();//####[401]####
        thumbnailsPanel = new JPanel(new GridLayout(0, 3));//####[403]####
        JScrollPane scroll = new JScrollPane(thumbnailsPanel);//####[404]####
        thumbnailsPanel.setVisible(true);//####[405]####
        scroll.setVisible(true);//####[406]####
        add(scroll, BorderLayout.CENTER);//####[407]####
    }//####[408]####
//####[410]####
    private int buttonSize = 80;//####[410]####
//####[412]####
    private JButton makeButton(String icon, Action action, String tooltip) {//####[412]####
        JButton btn = new JButton(action);//####[413]####
        btn.setToolTipText(tooltip);//####[414]####
        btn.setIcon(new ImageIcon(icon));//####[415]####
        btn.setPreferredSize(new Dimension(buttonSize, buttonSize));//####[416]####
        return btn;//####[417]####
    }//####[418]####
//####[420]####
    private void addToolButtonsPanel() {//####[420]####
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));//####[421]####
        panel.add(makeButton("images/add.png", actionAddImage, "Add more image(s) to the project"));//####[423]####
        panel.add(makeButton("images/saveimage.png", actionSaveSelected, "Apply changes to the selected image(s)"));//####[424]####
        panel.add(makeButton("images/undo.png", actionUndo, "Undo changes to the selected image(s)"));//####[425]####
        panel.add(makeButton("images/remove.png", actionRemoveImage, "Remove selected image(s) from view"));//####[426]####
        panel.add(makeButton("images/gradient.png", actionApplyEdge, "Edge detect on the selected image(s)"));//####[427]####
        panel.add(makeButton("images/video.png", actionInvert, "Invert colors on the selected image(s)"));//####[428]####
        panel.add(makeButton("images/blur.png", actionBlur, "Blur the selected image(s)"));//####[429]####
        panel.add(makeButton("images/sharpen.png", actionSharpen, "Sharpen the selected image(s)"));//####[430]####
        JPanel grp = new JPanel(new GridLayout(3, 1));//####[432]####
        grp.add(new JLabel("Select..", JLabel.CENTER));//####[433]####
        JButton btnAll = new JButton(actionSelectAll);//####[434]####
        btnAll.setText("All");//####[435]####
        btnAll.setToolTipText("Select all image(s)");//####[436]####
        grp.add(btnAll);//####[437]####
        JButton btnNone = new JButton(actionSelectNone);//####[438]####
        btnNone.setText("None");//####[439]####
        btnNone.setToolTipText("Deselect all image(s)");//####[440]####
        grp.add(btnNone);//####[441]####
        grp.setPreferredSize(new Dimension(buttonSize, buttonSize));//####[442]####
        panel.add(grp);//####[443]####
        add(panel, BorderLayout.NORTH);//####[445]####
        updateActions();//####[447]####
    }//####[448]####
}//####[448]####
