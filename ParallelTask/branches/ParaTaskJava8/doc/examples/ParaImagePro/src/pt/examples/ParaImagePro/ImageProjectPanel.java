package pt.examples.ParaImagePro;//####[1]####
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
import javax.swing.SwingUtilities;//####[29]####
import javax.swing.event.ChangeEvent;//####[31]####
import javax.swing.event.ChangeListener;//####[32]####
//####[32]####
//-- ParaTask related imports//####[32]####
import pt.runtime.*;//####[32]####
import java.util.concurrent.ExecutionException;//####[32]####
import java.util.concurrent.locks.*;//####[32]####
import java.lang.reflect.*;//####[32]####
import pt.runtime.GuiThread;//####[32]####
import java.util.concurrent.BlockingQueue;//####[32]####
import java.util.ArrayList;//####[32]####
import java.util.List;//####[32]####
//####[32]####
public class ImageProjectPanel extends ProjectPanel {//####[34]####
    static{ParaTask.init();}//####[34]####
    /*  ParaTask helper method to access private/protected slots *///####[34]####
    public void __pt__accessPrivateSlot(Method m, Object instance, Future arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[34]####
        if (m.getParameterTypes().length == 0)//####[34]####
            m.invoke(instance);//####[34]####
        else if ((m.getParameterTypes().length == 1))//####[34]####
            m.invoke(instance, arg);//####[34]####
        else //####[34]####
            m.invoke(instance, arg, interResult);//####[34]####
    }//####[34]####
//####[36]####
    private JPanel thumbnailsPanel;//####[36]####
//####[37]####
    private List<PaletteItem> palette = Collections.synchronizedList(new ArrayList<PaletteItem>());//####[37]####
//####[39]####
    private int parallelism = 1;//####[39]####
//####[40]####
    private int density = 16;//####[40]####
//####[41]####
    private int size = 16;//####[41]####
//####[43]####
    private static volatile Method __pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_method = null;//####[43]####
    private synchronized static void __pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_ensureMethodVarSet() {//####[43]####
        if (__pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_method == null) {//####[43]####
            try {//####[43]####
                __pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__addToThumbnailsPanelTask", new Class[] {//####[43]####
                    File.class, Future.class, Future.class, Future.class//####[43]####
                });//####[43]####
            } catch (Exception e) {//####[43]####
                e.printStackTrace();//####[43]####
            }//####[43]####
        }//####[43]####
    }//####[43]####
    private Future<Void> addToThumbnailsPanelTask(final Object file, final Object large, final Object square, final Object med) {//####[43]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[43]####
        return addToThumbnailsPanelTask(file, large, square, med, new Task());//####[43]####
    }//####[43]####
    private Future<Void> addToThumbnailsPanelTask(final Object file, final Object large, final Object square, final Object med, Task taskinfo) {//####[43]####
        // ensure Method variable is set//####[43]####
        if (__pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_method == null) {//####[43]####
            __pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_ensureMethodVarSet();//####[43]####
        }//####[43]####
        List<Integer> __pt__taskIdIndexList = new ArrayList<Integer>();//####[43]####
        List<Integer> __pt__queueIndexList = new ArrayList<Integer>();//####[43]####
        if (file instanceof BlockingQueue) {//####[43]####
            __pt__queueIndexList.add(0);//####[43]####
        }//####[43]####
        if (file instanceof Future) {//####[43]####
            taskinfo.addDependsOn((Future)file);//####[43]####
            __pt__taskIdIndexList.add(0);//####[43]####
        }//####[43]####
        if (large instanceof BlockingQueue) {//####[43]####
            __pt__queueIndexList.add(1);//####[43]####
        }//####[43]####
        if (square instanceof BlockingQueue) {//####[43]####
            __pt__queueIndexList.add(2);//####[43]####
        }//####[43]####
        if (med instanceof BlockingQueue) {//####[43]####
            __pt__queueIndexList.add(3);//####[43]####
        }//####[43]####
        int[] __pt__queueIndexArray = new int[__pt__queueIndexList.size()];//####[43]####
        for (int __pt__i = 0; __pt__i < __pt__queueIndexArray.length; __pt__i++) {//####[43]####
            __pt__queueIndexArray[__pt__i] = __pt__queueIndexList.get(__pt__i);//####[43]####
        }//####[43]####
        taskinfo.setQueueArgIndexes(__pt__queueIndexArray);//####[43]####
        if (__pt__queueIndexArray.length > 0) {//####[43]####
            taskinfo.setIsPipeline(true);//####[43]####
        }//####[43]####
        int[] __pt__taskIdIndexArray = new int[__pt__taskIdIndexList.size()];//####[43]####
        for (int __pt__i = 0; __pt__i < __pt__taskIdIndexArray.length; __pt__i++) {//####[43]####
            __pt__taskIdIndexArray[__pt__i] = __pt__taskIdIndexList.get(__pt__i);//####[43]####
        }//####[43]####
        taskinfo.setTaskIdArgIndexes(__pt__taskIdIndexArray);//####[43]####
        taskinfo.setParameters(file, large, square, med);//####[43]####
        taskinfo.setMethod(__pt__addToThumbnailsPanelTask_File_TaskIDImage_TaskIDImage_TaskIDImage_method);//####[43]####
        taskinfo.setInstance(this);//####[43]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[43]####
    }//####[43]####
    public void __pt__addToThumbnailsPanelTask(final File file, final Future<Image> large, final Future<Image> square, final Future<Image> med) {//####[43]####
        SwingUtilities.invokeLater(new Runnable() {//####[43]####
//####[46]####
            @Override//####[46]####
            public void run() {//####[46]####
                try {//####[47]####
                    addToThumbnailsPanel(file, large.getReturnResult(), square.getReturnResult(), med.getReturnResult());//####[48]####
                } catch (ExecutionException e) {//####[49]####
                    e.printStackTrace();//####[50]####
                } catch (InterruptedException e) {//####[51]####
                    e.printStackTrace();//####[52]####
                }//####[53]####
            }//####[54]####
        });//####[54]####
    }//####[56]####
//####[56]####
//####[58]####
    private void addToThumbnailsPanel(File file, Image large, Image square, Image medium) {//####[58]####
        thumbnailsPanel.add(new ImagePanelItem(file, large, square, medium, ImageProjectPanel.this));//####[59]####
        updateUI();//####[60]####
    }//####[61]####
//####[63]####
    private static volatile Method __pt__finishedAddingNewPanelItemsTask__method = null;//####[63]####
    private synchronized static void __pt__finishedAddingNewPanelItemsTask__ensureMethodVarSet() {//####[63]####
        if (__pt__finishedAddingNewPanelItemsTask__method == null) {//####[63]####
            try {//####[63]####
                __pt__finishedAddingNewPanelItemsTask__method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__finishedAddingNewPanelItemsTask", new Class[] {//####[63]####
                    //####[63]####
                });//####[63]####
            } catch (Exception e) {//####[63]####
                e.printStackTrace();//####[63]####
            }//####[63]####
        }//####[63]####
    }//####[63]####
    private Future<Void> finishedAddingNewPanelItemsTask() {//####[63]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[63]####
        return finishedAddingNewPanelItemsTask(new Task());//####[63]####
    }//####[63]####
    private Future<Void> finishedAddingNewPanelItemsTask(Task taskinfo) {//####[63]####
        // ensure Method variable is set//####[63]####
        if (__pt__finishedAddingNewPanelItemsTask__method == null) {//####[63]####
            __pt__finishedAddingNewPanelItemsTask__ensureMethodVarSet();//####[63]####
        }//####[63]####
        taskinfo.setParameters();//####[63]####
        taskinfo.setMethod(__pt__finishedAddingNewPanelItemsTask__method);//####[63]####
        taskinfo.setInstance(this);//####[63]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[63]####
    }//####[63]####
    public void __pt__finishedAddingNewPanelItemsTask() {//####[63]####
        SwingUtilities.invokeLater(new Runnable() {//####[63]####
//####[66]####
            @Override//####[66]####
            public void run() {//####[66]####
                finishedAddingNewPanelItems();//####[67]####
            }//####[68]####
        });//####[68]####
    }//####[70]####
//####[70]####
//####[72]####
    private void finishedAddingNewPanelItems() {//####[72]####
        isModified = true;//####[73]####
        updateActions();//####[74]####
        thumbnailsPanel.updateUI();//####[75]####
        mainFrame.updateTabIcons();//####[76]####
        mainFrame.updateProjectActions();//####[77]####
    }//####[78]####
//####[80]####
    private Action actionAddImage = new AbstractAction() {//####[80]####
//####[83]####
        @Override//####[83]####
        public void actionPerformed(ActionEvent arg0) {//####[83]####
            UIManager.put("FileChooser.readOnly", Boolean.TRUE);//####[84]####
            JFileChooser fc = new JFileChooser(projectDir);//####[85]####
            fc.setMultiSelectionEnabled(true);//####[86]####
            fc.setAcceptAllFileFilterUsed(false);//####[87]####
            fc.addChoosableFileFilter(new ImageFilter());//####[88]####
            int retValue = fc.showOpenDialog(ImageProjectPanel.this);//####[89]####
            if (retValue == JFileChooser.APPROVE_OPTION) //####[90]####
            {//####[90]####
                Timer timer = new Timer(fc.getSelectedFiles().length, "Add Image");//####[91]####
                File[] inputImages = fc.getSelectedFiles();//####[92]####
                if (MainFrame.isParallel) //####[93]####
                {//####[93]####
                    FutureGroup grp = new FutureGroup(inputImages.length);//####[94]####
                    for (int i = 0; i < inputImages.length; i++) //####[95]####
                    {//####[95]####
                        Future<Image> idImage = ImageManipulation.getImageFullTask(inputImages[i]);//####[96]####
                        Task __pt__idMedium = new Task();//####[97]####
//####[97]####
                        /*  -- ParaTask dependsOn clause for 'idMedium' -- *///####[97]####
                        __pt__idMedium.addDependsOn(idImage);//####[97]####
//####[97]####
                        Future<Image> idMedium = ImageManipulation.getMediumTask(idImage, __pt__idMedium);//####[97]####
                        Task __pt__idSmall = new Task();//####[98]####
//####[98]####
                        /*  -- ParaTask dependsOn clause for 'idSmall' -- *///####[98]####
                        __pt__idSmall.addDependsOn(idImage);//####[98]####
//####[98]####
                        Future<Image> idSmall = ImageManipulation.getSmallSquareTask(idImage, __pt__idSmall);//####[98]####
                        Task __pt__id = new Task();//####[99]####
//####[99]####
                        /*  -- ParaTask dependsOn clause for 'id' -- *///####[99]####
                        __pt__id.addDependsOn(idSmall);//####[101]####
                        __pt__id.addDependsOn(idMedium);//####[101]####
//####[101]####
                        boolean isEDT = GuiThread.isEventDispatchThread();//####[101]####
//####[101]####
//####[101]####
                        /*  -- ParaTask notify clause for 'id' -- *///####[101]####
                        try {//####[101]####
                            Method __pt__id_slot_0 = null;//####[101]####
                            __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[101]####
                            if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[101]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, timer, false));//####[101]####
//####[101]####
                        } catch(Exception __pt__e) { //####[101]####
                            System.err.println("Problem registering method in clause:");//####[101]####
                            __pt__e.printStackTrace();//####[101]####
                        }//####[101]####
                        Future id = addToThumbnailsPanelTask(inputImages[i], idImage, idSmall, idMedium, __pt__id);//####[101]####
                        grp.add(id);//####[102]####
                    }//####[103]####
                    Task __pt__finalTask = new Task();//####[104]####
//####[104]####
                    /*  -- ParaTask dependsOn clause for 'finalTask' -- *///####[104]####
                    __pt__finalTask.addDependsOn(grp);//####[104]####
//####[104]####
                    Future finalTask = finishedAddingNewPanelItemsTask(__pt__finalTask);//####[104]####
                } else {//####[105]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[106]####
                    for (int i = 0; i < inputImages.length; i++) //####[107]####
                    {//####[107]####
                        Image large = ImageManipulation.getImageFull(inputImages[i]);//####[108]####
                        Image small = ImageManipulation.getSmallSquare(large);//####[109]####
                        Image medium = ImageManipulation.getMedium(large);//####[110]####
                        addToThumbnailsPanel(inputImages[i], large, small, medium);//####[111]####
                        timer.taskComplete();//####[112]####
                    }//####[113]####
                    finishedAddingNewPanelItems();//####[114]####
                    setCursor(Cursor.getDefaultCursor());//####[115]####
                }//####[116]####
            }//####[117]####
        }//####[118]####
    };//####[118]####
//####[122]####
    private Action actionUndo = new AbstractAction() {//####[122]####
//####[124]####
        @Override//####[124]####
        public void actionPerformed(ActionEvent arg0) {//####[124]####
            Timer timer = new Timer("Undo");//####[125]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[126]####
            while (it.hasNext()) //####[127]####
            {//####[127]####
                ImagePanelItem panel = it.next();//####[128]####
                panel.restore();//####[129]####
            }//####[130]####
            updateActions();//####[131]####
            timer.taskComplete();//####[132]####
        }//####[133]####
    };//####[133]####
//####[137]####
    private Action actionSelectAll = new AbstractAction() {//####[137]####
//####[139]####
        @Override//####[139]####
        public void actionPerformed(ActionEvent arg0) {//####[139]####
            Timer timer = new Timer("Select All");//####[140]####
            Component[] comps = thumbnailsPanel.getComponents();//####[141]####
            for (int i = 0; i < comps.length; i++) //####[142]####
            {//####[142]####
                ImagePanelItem panel = (ImagePanelItem) comps[i];//####[143]####
                panel.setSelected(true);//####[144]####
            }//####[145]####
            updateActions();//####[146]####
            timer.taskComplete();//####[147]####
        }//####[148]####
    };//####[148]####
//####[151]####
    private List<ImagePanelItem> getAllPanels() {//####[151]####
        ArrayList<ImagePanelItem> list = new ArrayList<ImagePanelItem>();//####[152]####
        if (thumbnailsPanel != null) //####[154]####
        {//####[154]####
            Component[] comps = thumbnailsPanel.getComponents();//####[155]####
            for (int i = 0; i < comps.length; i++) //####[156]####
            {//####[156]####
                ImagePanelItem panel = (ImagePanelItem) comps[i];//####[157]####
                list.add(panel);//####[158]####
            }//####[159]####
        }//####[160]####
        return list;//####[162]####
    }//####[163]####
//####[165]####
    private List<ImagePanelItem> getSelectedPanels() {//####[165]####
        ArrayList<ImagePanelItem> list = new ArrayList<ImagePanelItem>();//####[166]####
        Component[] comps = thumbnailsPanel.getComponents();//####[168]####
        for (int i = 0; i < comps.length; i++) //####[169]####
        {//####[169]####
            ImagePanelItem panel = (ImagePanelItem) comps[i];//####[170]####
            if (panel.isSelected()) //####[171]####
            list.add(panel);//####[172]####
        }//####[173]####
        return list;//####[174]####
    }//####[175]####
//####[177]####
    private Action actionSelectNone = new AbstractAction() {//####[177]####
//####[179]####
        @Override//####[179]####
        public void actionPerformed(ActionEvent arg0) {//####[179]####
            Timer timer = new Timer("Select None");//####[180]####
            Component[] comps = thumbnailsPanel.getComponents();//####[181]####
            for (int i = 0; i < comps.length; i++) //####[182]####
            {//####[182]####
                ImagePanelItem panel = (ImagePanelItem) comps[i];//####[183]####
                panel.setSelected(false);//####[184]####
            }//####[185]####
            updateActions();//####[186]####
            timer.taskComplete();//####[187]####
        }//####[188]####
    };//####[188]####
//####[191]####
    private Action actionInvert = new AbstractAction() {//####[191]####
//####[193]####
        @Override//####[193]####
        public void actionPerformed(ActionEvent arg0) {//####[193]####
            Timer timer = new Timer(getSelectedPanels().size(), "Invert Colors");//####[194]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[195]####
            while (it.hasNext()) //####[197]####
            {//####[197]####
                ImagePanelItem panel = it.next();//####[198]####
                if (mainFrame.isParallel) //####[200]####
                {//####[200]####
                    Task __pt__id = new Task();//####[202]####
//####[202]####
                    /*  -- ParaTask dependsOn clause for 'id' -- *///####[202]####
                    __pt__id.addDependsOn(panel.getHistory());//####[204]####
//####[204]####
                    boolean isEDT = GuiThread.isEventDispatchThread();//####[204]####
//####[204]####
//####[204]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[204]####
                    try {//####[204]####
                        Method __pt__id_slot_0 = null;//####[204]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { Future.class });//####[204]####
                        Future __pt__id_slot_0_dummy_0 = null;//####[204]####
                        if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[204]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, false));//####[204]####
//####[204]####
                        Method __pt__id_slot_1 = null;//####[204]####
                        __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[204]####
                        if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[204]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, false));//####[204]####
//####[204]####
                        Method __pt__id_slot_2 = null;//####[204]####
                        __pt__id_slot_2 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[204]####
                        if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[204]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_2, timer, false));//####[204]####
//####[204]####
                    } catch(Exception __pt__e) { //####[204]####
                        System.err.println("Problem registering method in clause:");//####[204]####
                        __pt__e.printStackTrace();//####[204]####
                    }//####[204]####
                    Future<ImageCombo> id = ImageManipulation.invertTask(panel, __pt__id);//####[204]####
                    panel.addToHistory(id);//####[205]####
                } else {//####[208]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[210]####
                    ImageCombo res = ImageManipulation.invert(panel);//####[212]####
                    panel.setImage(res);//####[213]####
                    guiChanged();//####[214]####
                    setCursor(Cursor.getDefaultCursor());//####[215]####
                    timer.taskComplete();//####[216]####
                }//####[217]####
            }//####[218]####
        }//####[219]####
    };//####[219]####
//####[222]####
    private void guiChanged() {//####[222]####
        isModified = true;//####[223]####
        updateActions();//####[224]####
        thumbnailsPanel.updateUI();//####[225]####
        mainFrame.updateTabIcons();//####[226]####
        mainFrame.updateProjectActions();//####[227]####
    }//####[228]####
//####[230]####
    private Action actionBlur = new AbstractAction() {//####[230]####
//####[232]####
        @Override//####[232]####
        public void actionPerformed(ActionEvent arg0) {//####[232]####
            Timer timer = new Timer(getSelectedPanels().size(), "Blur");//####[233]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[234]####
            while (it.hasNext()) //####[238]####
            {//####[238]####
                ImagePanelItem panel = it.next();//####[239]####
                if (mainFrame.isParallel) //####[241]####
                {//####[241]####
                    Task __pt__id = new Task();//####[243]####
//####[243]####
                    /*  -- ParaTask dependsOn clause for 'id' -- *///####[243]####
                    __pt__id.addDependsOn(panel.getHistory());//####[245]####
//####[245]####
                    boolean isEDT = GuiThread.isEventDispatchThread();//####[245]####
//####[245]####
//####[245]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[245]####
                    try {//####[245]####
                        Method __pt__id_slot_0 = null;//####[245]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { Future.class });//####[245]####
                        Future __pt__id_slot_0_dummy_0 = null;//####[245]####
                        if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[245]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, false));//####[245]####
//####[245]####
                        Method __pt__id_slot_1 = null;//####[245]####
                        __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[245]####
                        if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[245]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, false));//####[245]####
//####[245]####
                        Method __pt__id_slot_2 = null;//####[245]####
                        __pt__id_slot_2 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[245]####
                        if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[245]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_2, timer, false));//####[245]####
//####[245]####
                    } catch(Exception __pt__e) { //####[245]####
                        System.err.println("Problem registering method in clause:");//####[245]####
                        __pt__e.printStackTrace();//####[245]####
                    }//####[245]####
                    Future<ImageCombo> id = ImageManipulation.blurTask(panel, __pt__id);//####[245]####
                    panel.addToHistory(id);//####[246]####
                } else {//####[249]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[250]####
                    ImageCombo res = ImageManipulation.blur(panel);//####[251]####
                    panel.setImage(res);//####[252]####
                    guiChanged();//####[253]####
                    setCursor(Cursor.getDefaultCursor());//####[254]####
                    timer.taskComplete();//####[255]####
                }//####[256]####
            }//####[257]####
        }//####[258]####
    };//####[258]####
//####[261]####
    private Action actionSharpen = new AbstractAction() {//####[261]####
//####[263]####
        @Override//####[263]####
        public void actionPerformed(ActionEvent arg0) {//####[263]####
            Timer timer = new Timer(getSelectedPanels().size(), "Sharpen");//####[264]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[265]####
            while (it.hasNext()) //####[269]####
            {//####[269]####
                ImagePanelItem panel = it.next();//####[270]####
                if (mainFrame.isParallel) //####[272]####
                {//####[272]####
                    Task __pt__id = new Task();//####[274]####
//####[274]####
                    /*  -- ParaTask dependsOn clause for 'id' -- *///####[274]####
                    __pt__id.addDependsOn(panel.getHistory());//####[276]####
//####[276]####
                    boolean isEDT = GuiThread.isEventDispatchThread();//####[276]####
//####[276]####
//####[276]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[276]####
                    try {//####[276]####
                        Method __pt__id_slot_0 = null;//####[276]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { Future.class });//####[276]####
                        Future __pt__id_slot_0_dummy_0 = null;//####[276]####
                        if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[276]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, false));//####[276]####
//####[276]####
                        Method __pt__id_slot_1 = null;//####[276]####
                        __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[276]####
                        if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[276]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, false));//####[276]####
//####[276]####
                        Method __pt__id_slot_2 = null;//####[276]####
                        __pt__id_slot_2 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[276]####
                        if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[276]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_2, timer, false));//####[276]####
//####[276]####
                    } catch(Exception __pt__e) { //####[276]####
                        System.err.println("Problem registering method in clause:");//####[276]####
                        __pt__e.printStackTrace();//####[276]####
                    }//####[276]####
                    Future<ImageCombo> id = ImageManipulation.sharpenTask(panel, __pt__id);//####[276]####
                    panel.addToHistory(id);//####[277]####
                } else {//####[279]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[280]####
                    ImageCombo res = ImageManipulation.sharpen(panel);//####[281]####
                    panel.setImage(res);//####[282]####
                    guiChanged();//####[283]####
                    setCursor(Cursor.getDefaultCursor());//####[284]####
                    timer.taskComplete();//####[285]####
                }//####[286]####
            }//####[287]####
        }//####[288]####
    };//####[288]####
//####[291]####
    private void savePanels(List<ImagePanelItem> list) {//####[291]####
        Iterator<ImagePanelItem> it = list.iterator();//####[292]####
        while (it.hasNext()) //####[293]####
        {//####[293]####
            ImagePanelItem panel = it.next();//####[294]####
            panel.commit();//####[295]####
        }//####[296]####
        updateActions();//####[297]####
    }//####[298]####
//####[301]####
    @Override//####[301]####
    public void saveProject() {//####[301]####
        super.saveProject();//####[302]####
        savePanels(getAllPanels());//####[303]####
    }//####[304]####
//####[306]####
    private Action actionSaveSelected = new AbstractAction() {//####[306]####
//####[308]####
        @Override//####[308]####
        public void actionPerformed(ActionEvent e) {//####[308]####
            Timer timer = new Timer("Apply Changes");//####[309]####
            savePanels(getSelectedPanels());//####[311]####
            timer.taskComplete();//####[312]####
        }//####[313]####
    };//####[313]####
//####[316]####
    private Action actionApplyEdge = new AbstractAction() {//####[316]####
//####[318]####
        @Override//####[318]####
        public void actionPerformed(ActionEvent arg0) {//####[318]####
            Timer timer = new Timer(getSelectedPanels().size(), "Edge Detect");//####[319]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[320]####
            while (it.hasNext()) //####[324]####
            {//####[324]####
                ImagePanelItem panel = it.next();//####[325]####
                if (mainFrame.isParallel) //####[327]####
                {//####[327]####
                    Task __pt__id = new Task();//####[329]####
//####[329]####
                    /*  -- ParaTask dependsOn clause for 'id' -- *///####[329]####
                    __pt__id.addDependsOn(panel.getHistory());//####[331]####
//####[331]####
                    boolean isEDT = GuiThread.isEventDispatchThread();//####[331]####
//####[331]####
//####[331]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[331]####
                    try {//####[331]####
                        Method __pt__id_slot_0 = null;//####[331]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { Future.class });//####[331]####
                        Future __pt__id_slot_0_dummy_0 = null;//####[331]####
                        if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[331]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, false));//####[331]####
//####[331]####
                        Method __pt__id_slot_1 = null;//####[331]####
                        __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[331]####
                        if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[331]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, false));//####[331]####
//####[331]####
                        Method __pt__id_slot_2 = null;//####[331]####
                        __pt__id_slot_2 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[331]####
                        if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[331]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_2, timer, false));//####[331]####
//####[331]####
                    } catch(Exception __pt__e) { //####[331]####
                        System.err.println("Problem registering method in clause:");//####[331]####
                        __pt__e.printStackTrace();//####[331]####
                    }//####[331]####
                    Future<ImageCombo> id = ImageManipulation.edgeDetectTask(panel, __pt__id);//####[331]####
                    panel.addToHistory(id);//####[332]####
                } else {//####[334]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[335]####
                    ImageCombo res = ImageManipulation.edgeDetect(panel);//####[336]####
                    panel.setImage(res);//####[337]####
                    guiChanged();//####[338]####
                    setCursor(Cursor.getDefaultCursor());//####[339]####
                    timer.taskComplete();//####[340]####
                }//####[341]####
            }//####[342]####
        }//####[343]####
    };//####[343]####
//####[346]####
    private Action actionRemoveImage = new AbstractAction() {//####[346]####
//####[348]####
        @Override//####[348]####
        public void actionPerformed(ActionEvent arg0) {//####[348]####
            Timer timer = new Timer("Remove Image");//####[349]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[350]####
            if (it.hasNext()) //####[351]####
            isModified = true;//####[352]####
            while (it.hasNext()) //####[353]####
            {//####[353]####
                thumbnailsPanel.remove(it.next());//####[354]####
            }//####[355]####
            updateActions();//####[357]####
            thumbnailsPanel.updateUI();//####[358]####
            mainFrame.updateTabIcons();//####[359]####
            mainFrame.updateProjectActions();//####[360]####
            timer.taskComplete();//####[361]####
        }//####[362]####
    };//####[362]####
//####[365]####
    private boolean canUndoSomethingSelected() {//####[365]####
        Iterator<ImagePanelItem> it = getAllPanels().iterator();//####[366]####
        while (it.hasNext()) //####[367]####
        {//####[367]####
            ImagePanelItem panel = it.next();//####[368]####
            if (panel.isModified() && panel.isSelected()) //####[369]####
            return true;//####[370]####
        }//####[371]####
        return false;//####[372]####
    }//####[373]####
//####[375]####
    public void updateActions() {//####[375]####
        boolean empty = true;//####[377]####
        boolean somethingSelected = false;//####[378]####
        boolean allSelected = false;//####[379]####
        boolean paletteReady = false;//####[380]####
        if (thumbnailsPanel != null) //####[382]####
        {//####[382]####
            Component[] comps = thumbnailsPanel.getComponents();//####[383]####
            if (comps.length != 0) //####[384]####
            {//####[384]####
                empty = false;//####[385]####
                somethingSelected = getSelectedPanels().size() > 0;//####[386]####
                allSelected = getSelectedPanels().size() == comps.length;//####[387]####
                paletteReady = palette.size() > 0;//####[388]####
            }//####[389]####
        }//####[390]####
        if (!empty) //####[392]####
        {//####[392]####
            actionSelectAll.setEnabled(!allSelected);//####[393]####
            actionRemoveImage.setEnabled(somethingSelected);//####[394]####
            actionSelectNone.setEnabled(somethingSelected);//####[395]####
            actionInvert.setEnabled(somethingSelected);//####[396]####
            actionApplyEdge.setEnabled(somethingSelected);//####[397]####
            actionBlur.setEnabled(somethingSelected);//####[398]####
            actionSharpen.setEnabled(somethingSelected);//####[399]####
            actionBuildMosaic.setEnabled(somethingSelected);//####[400]####
            actionBuildImageMosaic.setEnabled(somethingSelected && paletteReady);//####[401]####
            actionBuildPalette.setEnabled(somethingSelected && !paletteReady);//####[402]####
            actionClearPalette.setEnabled(paletteReady);//####[403]####
        } else {//####[404]####
            actionSelectAll.setEnabled(false);//####[405]####
            actionSelectNone.setEnabled(false);//####[406]####
            actionRemoveImage.setEnabled(false);//####[407]####
            actionInvert.setEnabled(false);//####[408]####
            actionApplyEdge.setEnabled(false);//####[409]####
            actionBlur.setEnabled(false);//####[410]####
            actionSharpen.setEnabled(false);//####[411]####
            actionBuildMosaic.setEnabled(false);//####[412]####
            actionBuildImageMosaic.setEnabled(false);//####[413]####
            actionBuildPalette.setEnabled(false);//####[414]####
            actionClearPalette.setEnabled(paletteReady);//####[415]####
        }//####[416]####
        actionUndo.setEnabled(canUndoSomethingSelected());//####[417]####
        actionSaveSelected.setEnabled(canUndoSomethingSelected());//####[418]####
    }//####[419]####
//####[421]####
    public ImageProjectPanel(MainFrame mainFrame, String projectName) {//####[421]####
        super(mainFrame, projectName);//####[422]####
        setLayout(new BorderLayout());//####[423]####
        addToolButtonsPanel();//####[425]####
        thumbnailsPanel = new JPanel(new GridLayout(0, 5));//####[427]####
        JScrollPane scroll = new JScrollPane(thumbnailsPanel);//####[428]####
        thumbnailsPanel.setVisible(true);//####[429]####
        scroll.setVisible(true);//####[430]####
        add(scroll, BorderLayout.CENTER);//####[431]####
    }//####[432]####
//####[434]####
    private int buttonSize = 80;//####[434]####
//####[436]####
    private JButton makeButton(String icon, Action action, String tooltip) {//####[436]####
        JButton btn = new JButton(action);//####[437]####
        btn.setToolTipText(tooltip);//####[438]####
        btn.setIcon(new ImageIcon(Utils.getImg(icon)));//####[439]####
        btn.setPreferredSize(new Dimension(buttonSize, buttonSize));//####[440]####
        return btn;//####[441]####
    }//####[442]####
//####[444]####
    private void addToolButtonsPanel() {//####[444]####
        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));//####[445]####
        panel.add(makeButton("add.png", actionAddImage, "Add more image(s) to the project"));//####[447]####
        panel.add(makeButton("saveimage.png", actionSaveSelected, "Apply changes to the selected image(s)"));//####[448]####
        panel.add(makeButton("undo.png", actionUndo, "Undo changes to the selected image(s)"));//####[449]####
        panel.add(makeButton("remove.png", actionRemoveImage, "Remove selected image(s) from view"));//####[450]####
        panel.add(makeButton("gradient.png", actionApplyEdge, "Edge detect on the selected image(s)"));//####[451]####
        panel.add(makeButton("video.png", actionInvert, "Invert colors on the selected image(s)"));//####[452]####
        panel.add(makeButton("blur.png", actionBlur, "Blur the selected image(s)"));//####[453]####
        panel.add(makeButton("sharpen.png", actionSharpen, "Sharpen the selected image(s)"));//####[454]####
        panel.add(makeButton("canvas.png", actionBuildMosaic, "Build a mosaic of the selected image(s)"));//####[455]####
        panel.add(makeButton("artwork.png", actionBuildImageMosaic, "Build an image mosaic of the selected image(s)"));//####[456]####
        panel.add(makeButton("palette.png", actionBuildPalette, "Build the palette to be used to make image mosaics"));//####[457]####
        panel.add(makeButton("clearPalette.png", actionClearPalette, "Clear the palette of images"));//####[458]####
        panel.add(makeButton("settings.png", actionMosaicSettings, "Modify attributes related to building mosaics"));//####[459]####
        JPanel grp = new JPanel(new GridLayout(3, 1));//####[461]####
        grp.add(new JLabel("Select..", JLabel.CENTER));//####[462]####
        JButton btnAll = new JButton(actionSelectAll);//####[463]####
        btnAll.setText("All");//####[464]####
        btnAll.setToolTipText("Select all image(s)");//####[465]####
        grp.add(btnAll);//####[466]####
        JButton btnNone = new JButton(actionSelectNone);//####[467]####
        btnNone.setText("None");//####[468]####
        btnNone.setToolTipText("Deselect all image(s)");//####[469]####
        grp.add(btnNone);//####[470]####
        grp.setPreferredSize(new Dimension(buttonSize, buttonSize));//####[471]####
        panel.add(grp);//####[472]####
        add(panel, BorderLayout.NORTH);//####[474]####
        updateActions();//####[476]####
    }//####[477]####
//####[479]####
    private Action actionBuildMosaic = new AbstractAction() {//####[479]####
//####[482]####
        @Override//####[482]####
        public void actionPerformed(ActionEvent arg0) {//####[482]####
            Timer timer = new Timer(getSelectedPanels().size(), "Build Mosaic");//####[483]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[484]####
            while (it.hasNext()) //####[485]####
            {//####[485]####
                ImagePanelItem panel = it.next();//####[486]####
                if (mainFrame.isParallel) //####[488]####
                {//####[488]####
                    Task __pt__id = new Task();//####[489]####
//####[489]####
                    /*  -- ParaTask dependsOn clause for 'id' -- *///####[489]####
                    __pt__id.addDependsOn(panel.getHistory());//####[491]####
//####[491]####
                    boolean isEDT = GuiThread.isEventDispatchThread();//####[491]####
//####[491]####
//####[491]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[491]####
                    try {//####[491]####
                        Method __pt__id_slot_0 = null;//####[491]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { Future.class });//####[491]####
                        Future __pt__id_slot_0_dummy_0 = null;//####[491]####
                        if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[491]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, false));//####[491]####
//####[491]####
                        Method __pt__id_slot_1 = null;//####[491]####
                        __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[491]####
                        if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[491]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, false));//####[491]####
//####[491]####
                        Method __pt__id_slot_2 = null;//####[491]####
                        __pt__id_slot_2 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[491]####
                        if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[491]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_2, timer, false));//####[491]####
//####[491]####
                    } catch(Exception __pt__e) { //####[491]####
                        System.err.println("Problem registering method in clause:");//####[491]####
                        __pt__e.printStackTrace();//####[491]####
                    }//####[491]####
                    Future<ImageCombo> id = MosaicBuilder.buildMosaicTask(panel, density, size, __pt__id);//####[491]####
                    panel.addToHistory(id);//####[492]####
                } else {//####[493]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[494]####
                    ImageCombo res = MosaicBuilder.buildMosaic(panel, density, size);//####[495]####
                    panel.setImage(res);//####[496]####
                    guiChanged();//####[497]####
                    setCursor(Cursor.getDefaultCursor());//####[498]####
                    timer.taskComplete();//####[499]####
                }//####[500]####
            }//####[501]####
        }//####[502]####
    };//####[502]####
//####[505]####
    private Action actionBuildImageMosaic = new AbstractAction() {//####[505]####
//####[508]####
        @Override//####[508]####
        public void actionPerformed(ActionEvent arg0) {//####[508]####
            Timer timer = new Timer(getSelectedPanels().size(), "Build Image Mosaic");//####[509]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[510]####
            while (it.hasNext()) //####[511]####
            {//####[511]####
                ImagePanelItem panel = it.next();//####[512]####
                if (mainFrame.isParallel) //####[514]####
                {//####[514]####
                    if (parallelism == 1) //####[515]####
                    {//####[515]####
                        setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[516]####
                        ImageCombo res = MosaicBuilder.buildImageMosaic2(panel, palette, density);//####[517]####
                        panel.setImage(res);//####[518]####
                        guiChanged();//####[519]####
                        setCursor(Cursor.getDefaultCursor());//####[520]####
                        timer.taskComplete();//####[521]####
                    } else if (parallelism == 2) //####[522]####
                    {//####[522]####
                        Task __pt__id = new Task();//####[523]####
//####[523]####
                        /*  -- ParaTask dependsOn clause for 'id' -- *///####[523]####
                        __pt__id.addDependsOn(panel.getHistory());//####[525]####
//####[525]####
                        boolean isEDT = GuiThread.isEventDispatchThread();//####[525]####
//####[525]####
//####[525]####
                        /*  -- ParaTask notify clause for 'id' -- *///####[525]####
                        try {//####[525]####
                            Method __pt__id_slot_0 = null;//####[525]####
                            __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { Future.class });//####[525]####
                            Future __pt__id_slot_0_dummy_0 = null;//####[525]####
                            if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[525]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, false));//####[525]####
//####[525]####
                            Method __pt__id_slot_1 = null;//####[525]####
                            __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[525]####
                            if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[525]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, false));//####[525]####
//####[525]####
                            Method __pt__id_slot_2 = null;//####[525]####
                            __pt__id_slot_2 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[525]####
                            if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[525]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_2, timer, false));//####[525]####
//####[525]####
                        } catch(Exception __pt__e) { //####[525]####
                            System.err.println("Problem registering method in clause:");//####[525]####
                            __pt__e.printStackTrace();//####[525]####
                        }//####[525]####
                        Future<ImageCombo> id = MosaicBuilder.buildImageMosaicTask(panel, palette, density, parallelism, __pt__id);//####[525]####
                        panel.addToHistory(id);//####[526]####
                    } else if (parallelism == 3) //####[527]####
                    {//####[527]####
                        Task __pt__id = new Task();//####[528]####
//####[528]####
                        /*  -- ParaTask dependsOn clause for 'id' -- *///####[528]####
                        __pt__id.addDependsOn(panel.getHistory());//####[530]####
//####[530]####
                        boolean isEDT = GuiThread.isEventDispatchThread();//####[530]####
//####[530]####
//####[530]####
                        /*  -- ParaTask notify clause for 'id' -- *///####[530]####
                        try {//####[530]####
                            Method __pt__id_slot_0 = null;//####[530]####
                            __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { Future.class });//####[530]####
                            Future __pt__id_slot_0_dummy_0 = null;//####[530]####
                            if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[530]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, false));//####[530]####
//####[530]####
                            Method __pt__id_slot_1 = null;//####[530]####
                            __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[530]####
                            if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[530]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, false));//####[530]####
//####[530]####
                            Method __pt__id_slot_2 = null;//####[530]####
                            __pt__id_slot_2 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[530]####
                            if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[530]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_2, timer, false));//####[530]####
//####[530]####
                        } catch(Exception __pt__e) { //####[530]####
                            System.err.println("Problem registering method in clause:");//####[530]####
                            __pt__e.printStackTrace();//####[530]####
                        }//####[530]####
                        Future<ImageCombo> id = MosaicBuilder.buildImageMosaicTask3(panel, palette, density, parallelism, __pt__id);//####[530]####
                        panel.addToHistory(id);//####[531]####
                    } else if (parallelism == 4) //####[532]####
                    {//####[532]####
                        Task __pt__id = new Task();//####[533]####
//####[533]####
                        /*  -- ParaTask dependsOn clause for 'id' -- *///####[533]####
                        __pt__id.addDependsOn(panel.getHistory());//####[535]####
//####[535]####
                        boolean isEDT = GuiThread.isEventDispatchThread();//####[535]####
//####[535]####
//####[535]####
                        /*  -- ParaTask notify clause for 'id' -- *///####[535]####
                        try {//####[535]####
                            Method __pt__id_slot_0 = null;//####[535]####
                            __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { Future.class });//####[535]####
                            Future __pt__id_slot_0_dummy_0 = null;//####[535]####
                            if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[535]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, false));//####[535]####
//####[535]####
                            Method __pt__id_slot_1 = null;//####[535]####
                            __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[535]####
                            if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[535]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, false));//####[535]####
//####[535]####
                            Method __pt__id_slot_2 = null;//####[535]####
                            __pt__id_slot_2 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[535]####
                            if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[535]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_2, timer, false));//####[535]####
//####[535]####
                        } catch(Exception __pt__e) { //####[535]####
                            System.err.println("Problem registering method in clause:");//####[535]####
                            __pt__e.printStackTrace();//####[535]####
                        }//####[535]####
                        Future<ImageCombo> id = MosaicBuilder.buildImageMosaicTask4(panel, palette, density, parallelism, __pt__id);//####[535]####
                        panel.addToHistory(id);//####[536]####
                    } else {//####[537]####
                        Task __pt__id = new Task();//####[538]####
//####[538]####
                        /*  -- ParaTask dependsOn clause for 'id' -- *///####[538]####
                        __pt__id.addDependsOn(panel.getHistory());//####[540]####
//####[540]####
                        boolean isEDT = GuiThread.isEventDispatchThread();//####[540]####
//####[540]####
//####[540]####
                        /*  -- ParaTask notify clause for 'id' -- *///####[540]####
                        try {//####[540]####
                            Method __pt__id_slot_0 = null;//####[540]####
                            __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(panel.getClass(), "setImageTask", new Class[] { Future.class });//####[540]####
                            Future __pt__id_slot_0_dummy_0 = null;//####[540]####
                            if (false) panel.setImageTask(__pt__id_slot_0_dummy_0); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[540]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, panel, false));//####[540]####
//####[540]####
                            Method __pt__id_slot_1 = null;//####[540]####
                            __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[540]####
                            if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[540]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, ImageProjectPanel.this, false));//####[540]####
//####[540]####
                            Method __pt__id_slot_2 = null;//####[540]####
                            __pt__id_slot_2 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[540]####
                            if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[540]####
                            __pt__id.addSlotToNotify(new Slot(__pt__id_slot_2, timer, false));//####[540]####
//####[540]####
                        } catch(Exception __pt__e) { //####[540]####
                            System.err.println("Problem registering method in clause:");//####[540]####
                            __pt__e.printStackTrace();//####[540]####
                        }//####[540]####
                        Future<ImageCombo> id = MosaicBuilder.buildImageMosaicTask2(panel, palette, density, parallelism, __pt__id);//####[540]####
                        panel.addToHistory(id);//####[541]####
                    }//####[542]####
                } else {//####[543]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[544]####
                    ImageCombo res = MosaicBuilder.buildImageMosaic(panel, palette, density);//####[545]####
                    panel.setImage(res);//####[546]####
                    guiChanged();//####[547]####
                    setCursor(Cursor.getDefaultCursor());//####[548]####
                    timer.taskComplete();//####[549]####
                }//####[550]####
            }//####[551]####
        }//####[552]####
    };//####[552]####
//####[555]####
    private Action actionBuildPalette = new AbstractAction() {//####[555]####
//####[558]####
        @Override//####[558]####
        public void actionPerformed(ActionEvent arg0) {//####[558]####
            Timer timer = new Timer(getSelectedPanels().size(), "Build Palette");//####[559]####
            Iterator<ImagePanelItem> it = getSelectedPanels().iterator();//####[560]####
            while (it.hasNext()) //####[561]####
            {//####[561]####
                ImagePanelItem panel = it.next();//####[562]####
                if (mainFrame.isParallel) //####[564]####
                {//####[564]####
                    Task __pt__id = new Task();//####[565]####
//####[565]####
                    boolean isEDT = GuiThread.isEventDispatchThread();//####[565]####
//####[565]####
//####[565]####
                    /*  -- ParaTask notify clause for 'id' -- *///####[565]####
                    try {//####[565]####
                        Method __pt__id_slot_0 = null;//####[565]####
                        __pt__id_slot_0 = ParaTaskHelper.getDeclaredMethod(ImageProjectPanel.this.getClass(), "guiChanged", new Class[] {});//####[566]####
                        if (false) ImageProjectPanel.this.guiChanged(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[566]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_0, ImageProjectPanel.this, false));//####[566]####
//####[566]####
                        Method __pt__id_slot_1 = null;//####[566]####
                        __pt__id_slot_1 = ParaTaskHelper.getDeclaredMethod(timer.getClass(), "taskComplete", new Class[] {});//####[566]####
                        if (false) timer.taskComplete(); //-- ParaTask uses this dummy statement to ensure the slot exists (otherwise Java compiler will complain)//####[566]####
                        __pt__id.addSlotToNotify(new Slot(__pt__id_slot_1, timer, false));//####[566]####
//####[566]####
                    } catch(Exception __pt__e) { //####[566]####
                        System.err.println("Problem registering method in clause:");//####[566]####
                        __pt__e.printStackTrace();//####[566]####
                    }//####[566]####
                    Future<List<PaletteItem>> id = MosaicBuilder.buildMosaicPaletteItemTask(panel, palette, size, __pt__id);//####[566]####
                    try {//####[567]####
                        palette = id.getReturnResult();//####[568]####
                    } catch (ExecutionException e) {//####[569]####
                        e.printStackTrace();//####[570]####
                    } catch (InterruptedException e) {//####[571]####
                        e.printStackTrace();//####[572]####
                    }//####[573]####
                } else {//####[575]####
                    setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[576]####
                    palette = MosaicBuilder.buildMosaicPaletteItem(panel, palette, size);//####[577]####
                    guiChanged();//####[578]####
                    setCursor(Cursor.getDefaultCursor());//####[579]####
                    timer.taskComplete();//####[580]####
                }//####[581]####
            }//####[582]####
        }//####[583]####
    };//####[583]####
//####[586]####
    private Action actionClearPalette = new AbstractAction() {//####[586]####
//####[589]####
        @Override//####[589]####
        public void actionPerformed(ActionEvent arg0) {//####[589]####
            Timer timer = new Timer("Clear Palette");//####[590]####
            setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));//####[591]####
            palette.clear();//####[592]####
            guiChanged();//####[593]####
            setCursor(Cursor.getDefaultCursor());//####[594]####
            timer.taskComplete();//####[595]####
        }//####[596]####
    };//####[596]####
//####[599]####
    private Action actionMosaicSettings = new AbstractAction() {//####[599]####
//####[602]####
        @Override//####[602]####
        public void actionPerformed(ActionEvent arg0) {//####[602]####
            JPanel panel = new JPanel();//####[603]####
            JSlider parallelismSlider = new JSlider(JSlider.HORIZONTAL, 1, 5, parallelism);//####[604]####
            parallelismSlider.setBorder(BorderFactory.createTitledBorder("Parallelism Level"));//####[606]####
            parallelismSlider.setMajorTickSpacing(2);//####[607]####
            parallelismSlider.setMinorTickSpacing(1);//####[608]####
            parallelismSlider.setPaintTicks(true);//####[609]####
            parallelismSlider.setPaintLabels(true);//####[610]####
            class ParallelismSliderListener implements ChangeListener {//####[612]####
//####[612]####
                /*  ParaTask helper method to access private/protected slots *///####[612]####
                public void __pt__accessPrivateSlot(Method m, Object instance, Future arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[612]####
                    if (m.getParameterTypes().length == 0)//####[612]####
                        m.invoke(instance);//####[612]####
                    else if ((m.getParameterTypes().length == 1))//####[612]####
                        m.invoke(instance, arg);//####[612]####
                    else //####[612]####
                        m.invoke(instance, arg, interResult);//####[612]####
                }//####[612]####
//####[613]####
                public void stateChanged(ChangeEvent e) {//####[613]####
                    JSlider source = (JSlider) e.getSource();//####[614]####
                    if (!source.getValueIsAdjusting()) //####[615]####
                    {//####[615]####
                        parallelism = (int) source.getValue();//####[616]####
                    }//####[617]####
                }//####[618]####
            }//####[618]####
            parallelismSlider.addChangeListener(new ParallelismSliderListener());//####[621]####
            panel.add(parallelismSlider);//####[622]####
            JSlider densitySlider = new JSlider(JSlider.HORIZONTAL, 1, 64, density);//####[624]####
            densitySlider.setBorder(BorderFactory.createTitledBorder("Mosaic Density"));//####[626]####
            densitySlider.setMajorTickSpacing(63);//####[627]####
            densitySlider.setMinorTickSpacing(3);//####[628]####
            densitySlider.setPaintTicks(true);//####[629]####
            densitySlider.setPaintLabels(true);//####[630]####
            class DensitySliderListener implements ChangeListener {//####[632]####
//####[632]####
                /*  ParaTask helper method to access private/protected slots *///####[632]####
                public void __pt__accessPrivateSlot(Method m, Object instance, Future arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[632]####
                    if (m.getParameterTypes().length == 0)//####[632]####
                        m.invoke(instance);//####[632]####
                    else if ((m.getParameterTypes().length == 1))//####[632]####
                        m.invoke(instance, arg);//####[632]####
                    else //####[632]####
                        m.invoke(instance, arg, interResult);//####[632]####
                }//####[632]####
//####[633]####
                public void stateChanged(ChangeEvent e) {//####[633]####
                    JSlider source = (JSlider) e.getSource();//####[634]####
                    if (!source.getValueIsAdjusting()) //####[635]####
                    {//####[635]####
                        density = (int) source.getValue();//####[636]####
                    }//####[637]####
                }//####[638]####
            }//####[638]####
            densitySlider.addChangeListener(new DensitySliderListener());//####[641]####
            panel.add(densitySlider);//####[642]####
            JSlider sizeSlider = new JSlider(JSlider.HORIZONTAL, 1, 64, size);//####[644]####
            sizeSlider.setBorder(BorderFactory.createTitledBorder("Paint Size (requires new palette)"));//####[646]####
            sizeSlider.setMajorTickSpacing(63);//####[647]####
            sizeSlider.setMinorTickSpacing(3);//####[648]####
            sizeSlider.setPaintTicks(true);//####[649]####
            sizeSlider.setPaintLabels(true);//####[650]####
            class SizeSliderListener implements ChangeListener {//####[652]####
//####[652]####
                /*  ParaTask helper method to access private/protected slots *///####[652]####
                public void __pt__accessPrivateSlot(Method m, Object instance, Future arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[652]####
                    if (m.getParameterTypes().length == 0)//####[652]####
                        m.invoke(instance);//####[652]####
                    else if ((m.getParameterTypes().length == 1))//####[652]####
                        m.invoke(instance, arg);//####[652]####
                    else //####[652]####
                        m.invoke(instance, arg, interResult);//####[652]####
                }//####[652]####
//####[653]####
                public void stateChanged(ChangeEvent e) {//####[653]####
                    JSlider source = (JSlider) e.getSource();//####[654]####
                    if (!source.getValueIsAdjusting()) //####[655]####
                    {//####[655]####
                        size = (int) source.getValue();//####[656]####
                    }//####[657]####
                }//####[658]####
            }//####[658]####
            sizeSlider.addChangeListener(new SizeSliderListener());//####[661]####
            panel.add(sizeSlider);//####[662]####
            JOptionPane.showConfirmDialog(ImageProjectPanel.this, panel, "Mosaic Settings", JOptionPane.DEFAULT_OPTION, JOptionPane.PLAIN_MESSAGE);//####[664]####
        }//####[665]####
    };//####[665]####
}//####[665]####
