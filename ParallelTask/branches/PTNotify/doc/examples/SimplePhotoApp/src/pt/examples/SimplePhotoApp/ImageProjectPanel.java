package pt.examples.SimplePhotoApp;//####[1]####
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
import javax.swing.SwingUtilities;//####[25]####
//####[25]####
//-- ParaTask related imports//####[25]####
import pt.runtime.*;//####[25]####
import java.util.concurrent.ExecutionException;//####[25]####
import java.util.concurrent.locks.*;//####[25]####
import java.lang.reflect.*;//####[25]####
import pt.runtime.GuiThread;//####[25]####
import java.util.concurrent.BlockingQueue;//####[25]####
import java.util.ArrayList;//####[25]####
import java.util.List;//####[25]####
//####[25]####
public class ImageProjectPanel extends ProjectPanel {//####[27]####
    static{ParaTask.init();}//####[27]####
    /*  ParaTask helper method to access private/protected slots *///####[27]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[27]####
        if (m.getParameterTypes().length == 0)//####[27]####
            m.invoke(instance);//####[27]####
        else if ((m.getParameterTypes().length == 1))//####[27]####
            m.invoke(instance, arg);//####[27]####
        else //####[27]####
            m.invoke(instance, arg, interResult);//####[27]####
    }//####[27]####
//####[29]####
    private JPanel thumbnailsPanel;//####[29]####
//####[31]####
    private static volatile Method __pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_method = null;//####[31]####
    private synchronized static void __pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_ensureMethodVarSet() {//####[31]####
        if (__pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_method == null) {//####[31]####
            try {//####[31]####
                __pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__addToThumbnailsPanelTask", new Class[] {//####[31]####
                    File.class, TaskID.class, TaskID.class, TaskID.class//####[31]####
                });//####[31]####
            } catch (Exception e) {//####[31]####
                e.printStackTrace();//####[31]####
            }//####[31]####
        }//####[31]####
    }//####[31]####
    private TaskID<Void> addToThumbnailsPanelTask(final Object file, final Object large, final Object square, final Object med) {//####[31]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[31]####
        return addToThumbnailsPanelTask(file, large, square, med, new TaskInfo());//####[31]####
    }//####[31]####
    private TaskID<Void> addToThumbnailsPanelTask(final Object file, final Object large, final Object square, final Object med, TaskInfo taskinfo) {//####[31]####
        // ensure Method variable is set//####[31]####
        if (__pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_method == null) {//####[31]####
            __pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_ensureMethodVarSet();//####[31]####
        }//####[31]####
        List<Integer> __pt__taskIdIndexList = new ArrayList<Integer>();//####[31]####
        List<Integer> __pt__queueIndexList = new ArrayList<Integer>();//####[31]####
        if (file instanceof BlockingQueue) {//####[31]####
            __pt__queueIndexList.add(0);//####[31]####
        }//####[31]####
        if (file instanceof TaskID) {//####[31]####
            taskinfo.addDependsOn((TaskID)file);//####[31]####
            __pt__taskIdIndexList.add(0);//####[31]####
        }//####[31]####
        if (large instanceof BlockingQueue) {//####[31]####
            __pt__queueIndexList.add(1);//####[31]####
        }//####[31]####
        if (square instanceof BlockingQueue) {//####[31]####
            __pt__queueIndexList.add(2);//####[31]####
        }//####[31]####
        if (med instanceof BlockingQueue) {//####[31]####
            __pt__queueIndexList.add(3);//####[31]####
        }//####[31]####
        int[] __pt__queueIndexArray = new int[__pt__queueIndexList.size()];//####[31]####
        for (int __pt__i = 0; __pt__i < __pt__queueIndexArray.length; __pt__i++) {//####[31]####
            __pt__queueIndexArray[__pt__i] = __pt__queueIndexList.get(__pt__i);//####[31]####
        }//####[31]####
        taskinfo.setQueueArgIndexes(__pt__queueIndexArray);//####[31]####
        if (__pt__queueIndexArray.length > 0) {//####[31]####
            taskinfo.setIsPipeline(true);//####[31]####
        }//####[31]####
        int[] __pt__taskIdIndexArray = new int[__pt__taskIdIndexList.size()];//####[31]####
        for (int __pt__i = 0; __pt__i < __pt__taskIdIndexArray.length; __pt__i++) {//####[31]####
            __pt__taskIdIndexArray[__pt__i] = __pt__taskIdIndexList.get(__pt__i);//####[31]####
        }//####[31]####
        taskinfo.setTaskIdArgIndexes(__pt__taskIdIndexArray);//####[31]####
        taskinfo.setParameters(file, large, square, med);//####[31]####
        taskinfo.setMethod(__pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_method);//####[31]####
        taskinfo.setInstance(this);//####[31]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[31]####
    }//####[31]####
    public void __pt__addToThumbnailsPanelTask(final File file, final TaskID<Image> large, final TaskID<Image> square, final TaskID<Image> med) {//####[31]####
        SwingUtilities.invokeLater(new Runnable() {//####[31]####
//####[34]####
            @Override//####[34]####
            public void run() {//####[34]####
                try {//####[35]####
                    addToThumbnailsPanel(file, large.getReturnResult(), square.getReturnResult(), med.getReturnResult());//####[36]####
                } catch (ExecutionException e) {//####[37]####
                    e.printStackTrace();//####[38]####
                } catch (InterruptedException e) {//####[39]####
                    e.printStackTrace();//####[40]####
                }//####[41]####
            }//####[42]####
        });//####[42]####
    }//####[44]####
//####[44]####
//####[46]####
    private void addToThumbnailsPanel(File file, Image large, Image square, Image medium) {//####[46]####
        thumbnailsPanel.add(new ImagePanelItem(file, large, square, medium, ImageProjectPanel.this));//####[47]####
        updateUI();//####[48]####
    }//####[49]####
//####[51]####
    private static volatile Method __pt__finishedAddingNewPanelItemsTask__method = null;//####[51]####
    private synchronized static void __pt__finishedAddingNewPanelItemsTask__ensureMethodVarSet() {//####[51]####
        if (__pt__finishedAddingNewPanelItemsTask__method == null) {//####[51]####
            try {//####[51]####
                __pt__finishedAddingNewPanelItemsTask__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__finishedAddingNewPanelItemsTask", new Class[] {//####[51]####
                    //####[51]####
                });//####[51]####
            } catch (Exception e) {//####[51]####
                e.printStackTrace();//####[51]####
            }//####[51]####
        }//####[51]####
    }//####[51]####
    private TaskID<Void> finishedAddingNewPanelItemsTask() {//####[51]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[51]####
        return finishedAddingNewPanelItemsTask(new TaskInfo());//####[51]####
    }//####[51]####
    private TaskID<Void> finishedAddingNewPanelItemsTask(TaskInfo taskinfo) {//####[51]####
        // ensure Method variable is set//####[51]####
        if (__pt__finishedAddingNewPanelItemsTask__method == null) {//####[51]####
            __pt__finishedAddingNewPanelItemsTask__ensureMethodVarSet();//####[51]####
        }//####[51]####
        taskinfo.setParameters();//####[51]####
        taskinfo.setMethod(__pt__finishedAddingNewPanelItemsTask__method);//####[51]####
        taskinfo.setInstance(this);//####[51]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[51]####
    }//####[51]####
    public void __pt__finishedAddingNewPanelItemsTask() {//####[51]####
        SwingUtilities.invokeLater(new Runnable() {//####[51]####
//####[54]####
            @Override//####[54]####
            public void run() {//####[54]####
                finishedAddingNewPanelItems();//####[55]####
            }//####[56]####
        });//####[56]####
    }//####[58]####
//####[58]####
//####[60]####
    private void finishedAddingNewPanelItems() {//####[60]####
        isModified = true;//####[61]####
        updateActions();//####[62]####
        thumbnailsPanel.updateUI();//####[63]####
        mainFrame.updateTabIcons();//####[64]####
        mainFrame.updateProjectActions();//####[65]####
    }//####[66]####
//####[68]####
    private Action actionAddImage = new AbstractAction() {//####[68]####
//####[71]####
        @Override//####[71]####
        public void actionPerformed(ActionEvent arg0) {//####[71]####
            UIManager.put("FileChooser.readOnly", Boolean.TRUE);//####[72]####
            JFileChooser fc = new JFileChooser(projectDir);//####[73]####
            fc.setMultiSelectionEnabled(true);//####[74]####
            fc.setAcceptAllFileFilterUsed(false);//####[75]####
            fc.addChoosableFileFilter(new ImageFilter());//####[76]####
            int retValue = fc.showOpenDialog(ImageProjectPanel.this);//####[77]####
            if (retValue == JFileChooser.APPROVE_OPTION) //####[78]####
            {//####[78]####
                File[] inputImages = fc.getSelectedFiles();//####[79]####
                if (MainFrame.isParallel) //####[80]####
                {//####[80]####
                    TaskIDGroup grp = new TaskIDGroup(inputImages.length);//####[81]####
                    for (int i = 0; i < inputImages.length; i++) //####[82]####
                    {//####[82]####
                        TaskID<Image> idImage = ImageManipulation.getImageFullTask(inputImages[i]);//####[83]####
                        TaskInfo __pt__idMedium = new TaskInfo();//####[84]####
//####[84]####
                        /*  -- ParaTask dependsOn clause for 'idMedium' -- *///####[84]####
                        __pt__idMedium.addDependsOn(idImage);//####[84]####
//####[84]####
                        TaskID<Image> idMedium = ImageManipulation.getMediumTask(idImage, __pt__idMedium);//####[84]####
                        TaskInfo __pt__idSmall = new TaskInfo();//####[85]####
//####[85]####
                        /*  -- ParaTask dependsOn clause for 'idSmall' -- *///####[85]####
                        __pt__idSmall.addDependsOn(idImage);//####[85]####
//####[85]####
                        TaskID<Image> idSmall = ImageManipulation.getSmallSquareTask(idImage, __pt__idSmall);//####[85]####
                        TaskInfo __pt__id = new TaskInfo();//####[86]####
//####[86]####
                        /*  -- ParaTask dependsOn clause for 'id' -- *///####[86]####
                        __pt__id.addDependsOn(idSmall);//####[86]####
                        __pt__id.addDependsOn(idMedium);//####[86]####
//####[86]####
                        TaskID id = addToThumbnailsPanelTask(inputImages[i], idImage, idSmall, idMedium, __pt__id);//####[86]####
                        grp.add(id);//####[87]####
                    }//####[88]####
                    TaskInfo __pt__finalTask = new TaskInfo();//####[89]####
//####[89]####
                    /*  -- ParaTask dependsOn clause for 'finalTask' -- *///####[89]####
                    __pt__finalTask.addDependsOn(grp);//####[89]####
//####[89]####
                    TaskID finalTask = finishedAddingNewPanelItemsTask(__pt__finalTask);//####[89]####
                } else {//####[90]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[91]####
                    for (int i = 0; i < inputImages.length; i++) //####[92]####
                    {//####[92]####
                        Image large = ImageManipulation.getImageFull(inputImages[i]);//####[93]####
                        Image small = ImageManipulation.getSmallSquare(large);//####[94]####
                        Image medium = ImageManipulation.getMedium(large);//####[95]####
                        addToThumbnailsPanel(inputImages[i], large, small, medium);//####[96]####
                    }//####[97]####
                    finishedAddingNewPanelItems();//####[98]####
                    setCursor(Cursor.getDefaultCursor());//####[99]####
                }//####[100]####
            }//####[101]####
        }//####[102]####
    };//####[102]####
//####[106]####
    private Action actionUndo = new AbstractAction() {//####[106]####
//####[108]####
        @Override//####[108]####
        public void actionPerformed(ActionEvent arg0) {//####[108]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[109]####
            while (it.hasNext()) //####[110]####
            {//####[110]####
                ImagePanelItem panel = it.next();//####[111]####
                panel.restore();//####[112]####
            }//####[113]####
            updateActions();//####[114]####
        }//####[115]####
    };//####[115]####
//####[119]####
    private Action actionSelectAll = new AbstractAction() {//####[119]####
//####[121]####
        @Override//####[121]####
        public void actionPerformed(ActionEvent arg0) {//####[121]####
            Component[] comps = thumbnailsPanel.getComponents();//####[122]####
            for (int i = 0; i < comps.length; i++) //####[123]####
            {//####[123]####
                ImagePanelItem panel = (ImagePanelItem) comps[i];//####[124]####
                panel.setSelected(true);//####[125]####
            }//####[126]####
            updateActions();//####[127]####
        }//####[128]####
    };//####[128]####
//####[131]####
    private List<ImagePanelItem> getAllPanels() {//####[131]####
        ArrayList<ImagePanelItem> list = new ArrayList<ImagePanelItem>();//####[132]####
        if (thumbnailsPanel != null) //####[134]####
        {//####[134]####
            Component[] comps = thumbnailsPanel.getComponents();//####[135]####
            for (int i = 0; i < comps.length; i++) //####[136]####
            {//####[136]####
                ImagePanelItem panel = (ImagePanelItem) comps[i];//####[137]####
                list.add(panel);//####[138]####
            }//####[139]####
        }//####[140]####
        return list;//####[142]####
    }//####[143]####
//####[145]####
    private List<ImagePanelItem> getSelectedPanels() {//####[145]####
        ArrayList<ImagePanelItem> list = new ArrayList<ImagePanelItem>();//####[146]####
        Component[] comps = thumbnailsPanel.getComponents();//####[148]####
        for (int i = 0; i < comps.length; i++) //####[149]####
        {//####[149]####
            ImagePanelItem panel = (ImagePanelItem) comps[i];//####[150]####
            if (panel.isSelected()) //####[151]####
            list.add(panel);//####[152]####
        }//####[153]####
        return list;//####[154]####
    }//####[155]####
//####[157]####
    private Action actionSelectNone = new AbstractAction() {//####[157]####
//####[159]####
        @Override//####[159]####
        public void actionPerformed(ActionEvent arg0) {//####[159]####
            Component[] comps = thumbnailsPanel.getComponents();//####[160]####
            for (int i = 0; i < comps.length; i++) //####[161]####
            {//####[161]####
                ImagePanelItem panel = (ImagePanelItem) comps[i];//####[162]####
                panel.setSelected(false);//####[163]####
            }//####[164]####
            updateActions();//####[165]####
        }//####[166]####
    };//####[166]####
//####[169]####
    private void disableButtons() {//####[169]####
        actionInvert.setEnabled(false);//####[170]####
        actionApplyEdge.setEnabled(false);//####[171]####
        actionBlur.setEnabled(false);//####[172]####
        actionSharpen.setEnabled(false);//####[173]####
    }//####[174]####
//####[176]####
    private void enableButtons() {//####[176]####
        actionInvert.setEnabled(true);//####[177]####
        actionApplyEdge.setEnabled(true);//####[178]####
        actionBlur.setEnabled(true);//####[179]####
        actionSharpen.setEnabled(true);//####[180]####
    }//####[181]####
//####[183]####
    private Action actionInvert = new AbstractAction() {//####[183]####
//####[185]####
        @Override//####[185]####
        public void actionPerformed(ActionEvent arg0) {//####[185]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[187]####
            while (it.hasNext()) //####[189]####
            {//####[189]####
                ImagePanelItem panel = it.next();//####[190]####
                if (mainFrame.isParallel) //####[192]####
                {//####[192]####
                    TaskInfo __pt__id = new TaskInfo();//####[194]####
//####[194]####
                    /*  -- ParaTask dependsOn clause for 'id' -- *///####[194]####
                    __pt__id.addDependsOn(panel.getHistory());//####[196]####
//####[196]####
                    boolean isEDT = GuiThread.isEventDispatchThread();//####[196]####
//####[196]####
//####[196]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[196]####
                    try {//####[196]####
                        Method __pt__id_slot_0 = null;//####[196]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { TaskID.class });//####[196]####
                        TaskID __pt__id_slot_0_dummy_0 = null;//####[196]####
                        if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[196]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, false));//####[196]####
//####[196]####
                        Method __pt__id_slot_1 = null;//####[196]####
                        __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[196]####
                        if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[196]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, false));//####[196]####
//####[196]####
                    } catch(Exception __pt__e) { //####[196]####
                        System.err.println("Problem registering method in clause:");//####[196]####
                        __pt__e.printStackTrace();//####[196]####
                    }//####[196]####
                    TaskID<ImageCombo> id = ImageManipulation.invertTask(panel, __pt__id);//####[196]####
                    panel.addToHistory(id);//####[197]####
                } else {//####[200]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[202]####
                    ImageCombo res = ImageManipulation.invert(panel);//####[204]####
                    panel.setImage(res);//####[205]####
                    guiChanged();//####[206]####
                    setCursor(Cursor.getDefaultCursor());//####[207]####
                }//####[208]####
            }//####[209]####
        }//####[210]####
    };//####[210]####
//####[213]####
    private void guiChanged() {//####[213]####
        isModified = true;//####[214]####
        updateActions();//####[215]####
        thumbnailsPanel.updateUI();//####[216]####
        mainFrame.updateTabIcons();//####[217]####
        mainFrame.updateProjectActions();//####[218]####
    }//####[219]####
//####[221]####
    private Action actionBlur = new AbstractAction() {//####[221]####
//####[223]####
        @Override//####[223]####
        public void actionPerformed(ActionEvent arg0) {//####[223]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[225]####
            while (it.hasNext()) //####[229]####
            {//####[229]####
                ImagePanelItem panel = it.next();//####[230]####
                if (mainFrame.isParallel) //####[232]####
                {//####[232]####
                    TaskInfo __pt__id = new TaskInfo();//####[234]####
//####[234]####
                    /*  -- ParaTask dependsOn clause for 'id' -- *///####[234]####
                    __pt__id.addDependsOn(panel.getHistory());//####[236]####
//####[236]####
                    boolean isEDT = GuiThread.isEventDispatchThread();//####[236]####
//####[236]####
//####[236]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[236]####
                    try {//####[236]####
                        Method __pt__id_slot_0 = null;//####[236]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { TaskID.class });//####[236]####
                        TaskID __pt__id_slot_0_dummy_0 = null;//####[236]####
                        if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[236]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, false));//####[236]####
//####[236]####
                        Method __pt__id_slot_1 = null;//####[236]####
                        __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[236]####
                        if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[236]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, false));//####[236]####
//####[236]####
                    } catch(Exception __pt__e) { //####[236]####
                        System.err.println("Problem registering method in clause:");//####[236]####
                        __pt__e.printStackTrace();//####[236]####
                    }//####[236]####
                    TaskID<ImageCombo> id = ImageManipulation.blurTask(panel, __pt__id);//####[236]####
                    panel.addToHistory(id);//####[237]####
                } else {//####[240]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[241]####
                    ImageCombo res = ImageManipulation.blur(panel);//####[242]####
                    panel.setImage(res);//####[243]####
                    guiChanged();//####[244]####
                    setCursor(Cursor.getDefaultCursor());//####[245]####
                }//####[246]####
            }//####[247]####
        }//####[248]####
    };//####[248]####
//####[251]####
    private Action actionSharpen = new AbstractAction() {//####[251]####
//####[253]####
        @Override//####[253]####
        public void actionPerformed(ActionEvent arg0) {//####[253]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[255]####
            while (it.hasNext()) //####[259]####
            {//####[259]####
                ImagePanelItem panel = it.next();//####[260]####
                if (mainFrame.isParallel) //####[262]####
                {//####[262]####
                    TaskInfo __pt__id = new TaskInfo();//####[264]####
//####[264]####
                    /*  -- ParaTask dependsOn clause for 'id' -- *///####[264]####
                    __pt__id.addDependsOn(panel.getHistory());//####[266]####
//####[266]####
                    boolean isEDT = GuiThread.isEventDispatchThread();//####[266]####
//####[266]####
//####[266]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[266]####
                    try {//####[266]####
                        Method __pt__id_slot_0 = null;//####[266]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { TaskID.class });//####[266]####
                        TaskID __pt__id_slot_0_dummy_0 = null;//####[266]####
                        if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[266]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, false));//####[266]####
//####[266]####
                        Method __pt__id_slot_1 = null;//####[266]####
                        __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[266]####
                        if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[266]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, false));//####[266]####
//####[266]####
                    } catch(Exception __pt__e) { //####[266]####
                        System.err.println("Problem registering method in clause:");//####[266]####
                        __pt__e.printStackTrace();//####[266]####
                    }//####[266]####
                    TaskID<ImageCombo> id = ImageManipulation.sharpenTask(panel, __pt__id);//####[266]####
                    panel.addToHistory(id);//####[267]####
                } else {//####[269]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[270]####
                    ImageCombo res = ImageManipulation.sharpen(panel);//####[271]####
                    panel.setImage(res);//####[272]####
                    guiChanged();//####[273]####
                    setCursor(Cursor.getDefaultCursor());//####[274]####
                }//####[275]####
            }//####[276]####
        }//####[277]####
    };//####[277]####
//####[280]####
    private void savePanels(List<ImagePanelItem> list) {//####[280]####
        Iterator<ImagePanelItem> it = list.iterator();//####[281]####
        while (it.hasNext()) //####[282]####
        {//####[282]####
            ImagePanelItem panel = it.next();//####[283]####
            panel.commit();//####[284]####
        }//####[285]####
        updateActions();//####[286]####
    }//####[287]####
//####[290]####
    @Override//####[290]####
    public void saveProject() {//####[290]####
        super.saveProject();//####[291]####
        savePanels(getAllPanels());//####[292]####
    }//####[293]####
//####[295]####
    private Action actionSaveSelected = new AbstractAction() {//####[295]####
//####[297]####
        @Override//####[297]####
        public void actionPerformed(ActionEvent e) {//####[297]####
            savePanels(getSelectedPanels());//####[299]####
        }//####[300]####
    };//####[300]####
//####[303]####
    private Action actionApplyEdge = new AbstractAction() {//####[303]####
//####[305]####
        @Override//####[305]####
        public void actionPerformed(ActionEvent arg0) {//####[305]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[307]####
            while (it.hasNext()) //####[311]####
            {//####[311]####
                ImagePanelItem panel = it.next();//####[312]####
                if (mainFrame.isParallel) //####[314]####
                {//####[314]####
                    TaskInfo __pt__id = new TaskInfo();//####[316]####
//####[316]####
                    /*  -- ParaTask dependsOn clause for 'id' -- *///####[316]####
                    __pt__id.addDependsOn(panel.getHistory());//####[318]####
//####[318]####
                    boolean isEDT = GuiThread.isEventDispatchThread();//####[318]####
//####[318]####
//####[318]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[318]####
                    try {//####[318]####
                        Method __pt__id_slot_0 = null;//####[318]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { TaskID.class });//####[318]####
                        TaskID __pt__id_slot_0_dummy_0 = null;//####[318]####
                        if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[318]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, false));//####[318]####
//####[318]####
                        Method __pt__id_slot_1 = null;//####[318]####
                        __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[318]####
                        if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[318]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, false));//####[318]####
//####[318]####
                    } catch(Exception __pt__e) { //####[318]####
                        System.err.println("Problem registering method in clause:");//####[318]####
                        __pt__e.printStackTrace();//####[318]####
                    }//####[318]####
                    TaskID<ImageCombo> id = ImageManipulation.edgeDetectTask(panel, __pt__id);//####[318]####
                    panel.addToHistory(id);//####[319]####
                } else {//####[321]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[322]####
                    ImageCombo res = ImageManipulation.edgeDetect(panel);//####[323]####
                    panel.setImage(res);//####[324]####
                    guiChanged();//####[325]####
                    setCursor(Cursor.getDefaultCursor());//####[326]####
                }//####[327]####
            }//####[328]####
        }//####[329]####
    };//####[329]####
//####[332]####
    private Action actionRemoveImage = new AbstractAction() {//####[332]####
//####[334]####
        @Override//####[334]####
        public void actionPerformed(ActionEvent arg0) {//####[334]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[336]####
            if (it.hasNext()) //####[337]####
            isModified = true;//####[338]####
            while (it.hasNext()) //####[339]####
            {//####[339]####
                thumbnailsPanel.remove(it.next());//####[340]####
            }//####[341]####
            updateActions();//####[343]####
            thumbnailsPanel.updateUI();//####[344]####
            mainFrame.updateTabIcons();//####[345]####
            mainFrame.updateProjectActions();//####[346]####
        }//####[347]####
    };//####[347]####
//####[350]####
    private boolean canUndoSomethingSelected() {//####[350]####
        Iterator<ImagePanelItem> it = getAllPanels().iterator();//####[351]####
        while (it.hasNext()) //####[352]####
        {//####[352]####
            ImagePanelItem panel = it.next();//####[353]####
            if (panel.isModified() && panel.isSelected()) //####[354]####
            return true;//####[355]####
        }//####[356]####
        return false;//####[357]####
    }//####[358]####
//####[360]####
    public void updateActions() {//####[360]####
        boolean empty = true;//####[362]####
        boolean somethingSelected = false;//####[363]####
        boolean allSelected = false;//####[364]####
        if (thumbnailsPanel != null) //####[366]####
        {//####[366]####
            Component[] comps = thumbnailsPanel.getComponents();//####[367]####
            if (comps.length != 0) //####[368]####
            {//####[368]####
                empty = false;//####[369]####
                somethingSelected = getSelectedPanels().size() > 0;//####[370]####
                allSelected = getSelectedPanels().size() == comps.length;//####[371]####
            }//####[372]####
        }//####[373]####
        if (!empty) //####[375]####
        {//####[375]####
            actionSelectAll.setEnabled(!allSelected);//####[376]####
            actionRemoveImage.setEnabled(somethingSelected);//####[377]####
            actionSelectNone.setEnabled(somethingSelected);//####[378]####
            actionInvert.setEnabled(somethingSelected);//####[379]####
            actionApplyEdge.setEnabled(somethingSelected);//####[380]####
            actionBlur.setEnabled(somethingSelected);//####[381]####
            actionSharpen.setEnabled(somethingSelected);//####[382]####
        } else {//####[383]####
            actionSelectAll.setEnabled(false);//####[384]####
            actionSelectNone.setEnabled(false);//####[385]####
            actionRemoveImage.setEnabled(false);//####[386]####
            actionInvert.setEnabled(false);//####[387]####
            actionApplyEdge.setEnabled(false);//####[388]####
            actionBlur.setEnabled(false);//####[389]####
            actionSharpen.setEnabled(false);//####[390]####
        }//####[391]####
        actionUndo.setEnabled(canUndoSomethingSelected());//####[392]####
        actionSaveSelected.setEnabled(canUndoSomethingSelected());//####[393]####
    }//####[394]####
//####[396]####
    public ImageProjectPanel(MainFrame mainFrame, String projectName) {//####[396]####
        super(mainFrame, projectName);//####[397]####
        setLayout(new BorderLayout());//####[398]####
        addToolButtonsPanel();//####[400]####
        thumbnailsPanel = new JPanel(new GridLayout(0, 3));//####[402]####
        JScrollPane scroll = new JScrollPane(thumbnailsPanel);//####[403]####
        thumbnailsPanel.setVisible(true);//####[404]####
        scroll.setVisible(true);//####[405]####
        add(scroll, BorderLayout.CENTER);//####[406]####
    }//####[407]####
//####[409]####
    private int buttonSize = 80;//####[409]####
//####[411]####
    private JButton makeButton(String icon, Action action, String tooltip) {//####[411]####
        JButton btn = new JButton(action);//####[412]####
        btn.setToolTipText(tooltip);//####[413]####
        btn.setIcon(new ImageIcon(Utils.getImg(icon)));//####[414]####
        btn.setPreferredSize(new Dimension(buttonSize, buttonSize));//####[415]####
        return btn;//####[416]####
    }//####[417]####
//####[419]####
    private void addToolButtonsPanel() {//####[419]####
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));//####[420]####
        panel.add(makeButton("add.png", actionAddImage, "Add more image(s) to the project"));//####[422]####
        panel.add(makeButton("saveimage.png", actionSaveSelected, "Apply changes to the selected image(s)"));//####[423]####
        panel.add(makeButton("undo.png", actionUndo, "Undo changes to the selected image(s)"));//####[424]####
        panel.add(makeButton("remove.png", actionRemoveImage, "Remove selected image(s) from view"));//####[425]####
        panel.add(makeButton("gradient.png", actionApplyEdge, "Edge detect on the selected image(s)"));//####[426]####
        panel.add(makeButton("video.png", actionInvert, "Invert colors on the selected image(s)"));//####[427]####
        panel.add(makeButton("blur.png", actionBlur, "Blur the selected image(s)"));//####[428]####
        panel.add(makeButton("sharpen.png", actionSharpen, "Sharpen the selected image(s)"));//####[429]####
        JPanel grp = new JPanel(new GridLayout(3, 1));//####[431]####
        grp.add(new JLabel("Select..", JLabel.CENTER));//####[432]####
        JButton btnAll = new JButton(actionSelectAll);//####[433]####
        btnAll.setText("All");//####[434]####
        btnAll.setToolTipText("Select all image(s)");//####[435]####
        grp.add(btnAll);//####[436]####
        JButton btnNone = new JButton(actionSelectNone);//####[437]####
        btnNone.setText("None");//####[438]####
        btnNone.setToolTipText("Deselect all image(s)");//####[439]####
        grp.add(btnNone);//####[440]####
        grp.setPreferredSize(new Dimension(buttonSize, buttonSize));//####[441]####
        panel.add(grp);//####[442]####
        add(panel, BorderLayout.NORTH);//####[444]####
        updateActions();//####[446]####
    }//####[447]####
}//####[447]####
