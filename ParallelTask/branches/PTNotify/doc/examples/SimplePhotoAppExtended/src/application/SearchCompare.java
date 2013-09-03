package application;//####[1]####
//####[1]####
import java.util.ArrayList;//####[3]####
import java.util.Arrays;//####[4]####
import java.util.Collections;//####[5]####
import java.util.List;//####[6]####
import java.awt.Color;//####[7]####
import java.awt.Graphics2D;//####[8]####
import java.awt.Image;//####[9]####
import java.awt.image.BufferedImage;//####[10]####
import javax.swing.JPanel;//####[11]####
//####[11]####
//-- ParaTask related imports//####[11]####
import pt.runtime.*;//####[11]####
import java.util.concurrent.ExecutionException;//####[11]####
import java.util.concurrent.locks.*;//####[11]####
import java.lang.reflect.*;//####[11]####
import pt.runtime.GuiThread;//####[11]####
import java.util.concurrent.BlockingQueue;//####[11]####
import java.util.ArrayList;//####[11]####
import java.util.List;//####[11]####
//####[11]####
public class SearchCompare {//####[13]####
    static{ParaTask.init();}//####[13]####
    /*  ParaTask helper method to access private/protected slots *///####[13]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[13]####
        if (m.getParameterTypes().length == 0)//####[13]####
            m.invoke(instance);//####[13]####
        else if ((m.getParameterTypes().length == 1))//####[13]####
            m.invoke(instance, arg);//####[13]####
        else //####[13]####
            m.invoke(instance, arg, interResult);//####[13]####
    }//####[13]####
//####[15]####
    public static List<PhotoPanelItem> compareHash(JPanel thumbnailsPanel, PhotoPanelItem compare, int accuracy) {//####[15]####
        List<PhotoPanelItem> result = Collections.synchronizedList(new ArrayList<PhotoPanelItem>());//####[16]####
        long imageHash = 0;//####[17]####
        BufferedImage image = new BufferedImage(accuracy, accuracy, BufferedImage.TYPE_INT_RGB);//####[19]####
        Graphics2D g2 = image.createGraphics();//####[20]####
        g2.drawImage(compare.getSquarePhoto().getScaledInstance(accuracy, accuracy, Image.SCALE_DEFAULT), null, null);//####[21]####
        for (int y = 0; y < accuracy; y++) //####[23]####
        {//####[23]####
            for (int x = 0; x < accuracy; x++) //####[24]####
            {//####[24]####
                imageHash = image.getRGB(x, y) + (imageHash << 6) + (imageHash << 16) - imageHash;//####[25]####
            }//####[26]####
        }//####[27]####
        for (int i = 0; i < thumbnailsPanel.getComponentCount(); i++) //####[29]####
        {//####[29]####
            PhotoPanelItem pi = (PhotoPanelItem) thumbnailsPanel.getComponent(i);//####[30]####
            long tempHash = 0;//####[31]####
            BufferedImage temp = new BufferedImage(accuracy, accuracy, BufferedImage.TYPE_INT_RGB);//####[33]####
            Graphics2D g3 = temp.createGraphics();//####[34]####
            g3.drawImage(pi.getSquarePhoto().getScaledInstance(accuracy, accuracy, Image.SCALE_DEFAULT), null, null);//####[35]####
            for (int y = 0; y < accuracy; y++) //####[37]####
            {//####[37]####
                for (int x = 0; x < accuracy; x++) //####[38]####
                {//####[38]####
                    tempHash = temp.getRGB(x, y) + (tempHash << 6) + (tempHash << 16) - tempHash;//####[39]####
                }//####[40]####
            }//####[41]####
            if (imageHash == tempHash) //####[43]####
            {//####[43]####
                result.add(pi);//####[44]####
            }//####[45]####
        }//####[46]####
        return result;//####[47]####
    }//####[48]####
//####[50]####
    public static List<PhotoPanelItem> compareHash2(JPanel thumbnailsPanel, PhotoPanelItem compare, int accuracy) {//####[50]####
        List<PhotoPanelItem> result = Collections.synchronizedList(new ArrayList<PhotoPanelItem>());//####[51]####
        long imageHash = 0;//####[52]####
        BufferedImage image = new BufferedImage(accuracy, accuracy, BufferedImage.TYPE_INT_RGB);//####[54]####
        Graphics2D g2 = image.createGraphics();//####[55]####
        g2.drawImage(compare.getSquarePhoto().getScaledInstance(accuracy, accuracy, Image.SCALE_DEFAULT), null, null);//####[56]####
        for (int y = 0; y < accuracy; y++) //####[58]####
        {//####[58]####
            for (int x = 0; x < accuracy; x++) //####[59]####
            {//####[59]####
                imageHash = image.getRGB(x, y) + (imageHash << 6) + (imageHash << 16) - imageHash;//####[60]####
            }//####[61]####
        }//####[62]####
        int CompCount = thumbnailsPanel.getComponentCount();//####[64]####
        TaskIDGroup group = new TaskIDGroup(CompCount);//####[65]####
        for (int i = 0; i < CompCount; i++) //####[67]####
        {//####[67]####
            TaskID id = compareHash2Task(thumbnailsPanel, result, compare, imageHash, accuracy, i);//####[68]####
            group.add(id);//####[69]####
        }//####[70]####
        try {//####[72]####
            group.waitTillFinished();//####[73]####
        } catch (ExecutionException e) {//####[74]####
            e.printStackTrace();//####[75]####
        } catch (InterruptedException e) {//####[76]####
            e.printStackTrace();//####[77]####
        }//####[78]####
        return result;//####[80]####
    }//####[81]####
//####[83]####
    private static volatile Method __pt__compareHash2Task_JPanel_ListPhotoPanelItem_PhotoPanelItem_long_int_int_method = null;//####[83]####
    private synchronized static void __pt__compareHash2Task_JPanel_ListPhotoPanelItem_PhotoPanelItem_long_int_int_ensureMethodVarSet() {//####[83]####
        if (__pt__compareHash2Task_JPanel_ListPhotoPanelItem_PhotoPanelItem_long_int_int_method == null) {//####[83]####
            try {//####[83]####
                __pt__compareHash2Task_JPanel_ListPhotoPanelItem_PhotoPanelItem_long_int_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__compareHash2Task", new Class[] {//####[83]####
                    JPanel.class, List.class, PhotoPanelItem.class, long.class, int.class, int.class//####[83]####
                });//####[83]####
            } catch (Exception e) {//####[83]####
                e.printStackTrace();//####[83]####
            }//####[83]####
        }//####[83]####
    }//####[83]####
    public static TaskID<Void> compareHash2Task(Object thumbnailsPanel, Object result, Object compare, Object imageHash, Object accuracy, Object i) {//####[83]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[83]####
        return compareHash2Task(thumbnailsPanel, result, compare, imageHash, accuracy, i, new TaskInfo());//####[83]####
    }//####[83]####
    public static TaskID<Void> compareHash2Task(Object thumbnailsPanel, Object result, Object compare, Object imageHash, Object accuracy, Object i, TaskInfo taskinfo) {//####[83]####
        // ensure Method variable is set//####[83]####
        if (__pt__compareHash2Task_JPanel_ListPhotoPanelItem_PhotoPanelItem_long_int_int_method == null) {//####[83]####
            __pt__compareHash2Task_JPanel_ListPhotoPanelItem_PhotoPanelItem_long_int_int_ensureMethodVarSet();//####[83]####
        }//####[83]####
        List<Integer> __pt__taskIdIndexList = new ArrayList<Integer>();//####[83]####
        List<Integer> __pt__queueIndexList = new ArrayList<Integer>();//####[83]####
        if (thumbnailsPanel instanceof BlockingQueue) {//####[83]####
            __pt__queueIndexList.add(0);//####[83]####
        }//####[83]####
        if (thumbnailsPanel instanceof TaskID) {//####[83]####
            taskinfo.addDependsOn((TaskID)thumbnailsPanel);//####[83]####
            __pt__taskIdIndexList.add(0);//####[83]####
        }//####[83]####
        if (result instanceof BlockingQueue) {//####[83]####
            __pt__queueIndexList.add(1);//####[83]####
        }//####[83]####
        if (result instanceof TaskID) {//####[83]####
            taskinfo.addDependsOn((TaskID)result);//####[83]####
            __pt__taskIdIndexList.add(1);//####[83]####
        }//####[83]####
        if (compare instanceof BlockingQueue) {//####[83]####
            __pt__queueIndexList.add(2);//####[83]####
        }//####[83]####
        if (compare instanceof TaskID) {//####[83]####
            taskinfo.addDependsOn((TaskID)compare);//####[83]####
            __pt__taskIdIndexList.add(2);//####[83]####
        }//####[83]####
        if (imageHash instanceof BlockingQueue) {//####[83]####
            __pt__queueIndexList.add(3);//####[83]####
        }//####[83]####
        if (imageHash instanceof TaskID) {//####[83]####
            taskinfo.addDependsOn((TaskID)imageHash);//####[83]####
            __pt__taskIdIndexList.add(3);//####[83]####
        }//####[83]####
        if (accuracy instanceof BlockingQueue) {//####[83]####
            __pt__queueIndexList.add(4);//####[83]####
        }//####[83]####
        if (accuracy instanceof TaskID) {//####[83]####
            taskinfo.addDependsOn((TaskID)accuracy);//####[83]####
            __pt__taskIdIndexList.add(4);//####[83]####
        }//####[83]####
        if (i instanceof BlockingQueue) {//####[83]####
            __pt__queueIndexList.add(5);//####[83]####
        }//####[83]####
        if (i instanceof TaskID) {//####[83]####
            taskinfo.addDependsOn((TaskID)i);//####[83]####
            __pt__taskIdIndexList.add(5);//####[83]####
        }//####[83]####
        int[] __pt__queueIndexArray = new int[__pt__queueIndexList.size()];//####[83]####
        for (int __pt__i = 0; __pt__i < __pt__queueIndexArray.length; __pt__i++) {//####[83]####
            __pt__queueIndexArray[__pt__i] = __pt__queueIndexList.get(__pt__i);//####[83]####
        }//####[83]####
        taskinfo.setQueueArgIndexes(__pt__queueIndexArray);//####[83]####
        if (__pt__queueIndexArray.length > 0) {//####[83]####
            taskinfo.setIsPipeline(true);//####[83]####
        }//####[83]####
        int[] __pt__taskIdIndexArray = new int[__pt__taskIdIndexList.size()];//####[83]####
        for (int __pt__i = 0; __pt__i < __pt__taskIdIndexArray.length; __pt__i++) {//####[83]####
            __pt__taskIdIndexArray[__pt__i] = __pt__taskIdIndexList.get(__pt__i);//####[83]####
        }//####[83]####
        taskinfo.setTaskIdArgIndexes(__pt__taskIdIndexArray);//####[83]####
        taskinfo.setParameters(thumbnailsPanel, result, compare, imageHash, accuracy, i);//####[83]####
        taskinfo.setMethod(__pt__compareHash2Task_JPanel_ListPhotoPanelItem_PhotoPanelItem_long_int_int_method);//####[83]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[83]####
    }//####[83]####
    public static void __pt__compareHash2Task(JPanel thumbnailsPanel, List<PhotoPanelItem> result, PhotoPanelItem compare, long imageHash, int accuracy, int i) {//####[83]####
        PhotoPanelItem pi = (PhotoPanelItem) thumbnailsPanel.getComponent(i);//####[84]####
        long tempHash = 0;//####[85]####
        BufferedImage temp = new BufferedImage(accuracy, accuracy, BufferedImage.TYPE_INT_RGB);//####[87]####
        Graphics2D g3 = temp.createGraphics();//####[88]####
        g3.drawImage(pi.getSquarePhoto().getScaledInstance(accuracy, accuracy, Image.SCALE_DEFAULT), null, null);//####[89]####
        for (int y = 0; y < accuracy; y++) //####[91]####
        {//####[91]####
            for (int x = 0; x < accuracy; x++) //####[92]####
            {//####[92]####
                tempHash = temp.getRGB(x, y) + (tempHash << 6) + (tempHash << 16) - tempHash;//####[93]####
            }//####[94]####
        }//####[95]####
        if (imageHash == tempHash) //####[97]####
        {//####[97]####
            result.add(pi);//####[98]####
        }//####[99]####
    }//####[100]####
//####[100]####
//####[103]####
    public static List<PhotoPanelItem> compareColor(JPanel thumbnailsPanel, PhotoPanelItem compare, int sensitivity, int accuracy) {//####[103]####
        List<PhotoPanelItem> result = Collections.synchronizedList(new ArrayList<PhotoPanelItem>());//####[104]####
        BufferedImage image = new BufferedImage(accuracy, accuracy, BufferedImage.TYPE_INT_RGB);//####[106]####
        Graphics2D g2 = image.createGraphics();//####[107]####
        g2.drawImage(compare.getSquarePhoto().getScaledInstance(accuracy, accuracy, Image.SCALE_DEFAULT), null, null);//####[108]####
        for (int i = 0; i < thumbnailsPanel.getComponentCount(); i++) //####[110]####
        {//####[110]####
            boolean match = true;//####[111]####
            PhotoPanelItem pi = (PhotoPanelItem) thumbnailsPanel.getComponent(i);//####[112]####
            BufferedImage temp = new BufferedImage(accuracy, accuracy, BufferedImage.TYPE_INT_RGB);//####[114]####
            Graphics2D g3 = temp.createGraphics();//####[115]####
            g3.drawImage(pi.getSquarePhoto().getScaledInstance(accuracy, accuracy, Image.SCALE_DEFAULT), null, null);//####[116]####
            for (int y = 0; y < accuracy; y++) //####[118]####
            {//####[118]####
                for (int x = 0; x < accuracy; x++) //####[119]####
                {//####[119]####
                    Color tc = new Color(image.getRGB(x, y));//####[120]####
                    int tr = tc.getRed();//####[121]####
                    int tg = tc.getGreen();//####[122]####
                    int tb = tc.getBlue();//####[123]####
                    Color pc = new Color(temp.getRGB(x, y));//####[125]####
                    int pr = pc.getRed();//####[126]####
                    int pg = pc.getGreen();//####[127]####
                    int pb = pc.getBlue();//####[128]####
                    int distance = (int) Math.sqrt(0.3 * (tr - pr) * (tr - pr) + 0.59 * (tg - pg) * (tg - pg) + 0.11 * (tb - pb) * (tb - pb));//####[130]####
                    if (distance > sensitivity) //####[131]####
                    {//####[131]####
                        match = false;//####[132]####
                    }//####[133]####
                }//####[134]####
            }//####[135]####
            if (match) //####[136]####
            {//####[136]####
                result.add(pi);//####[137]####
            }//####[138]####
        }//####[139]####
        return result;//####[140]####
    }//####[141]####
//####[143]####
    public static List<PhotoPanelItem> compareColor2(JPanel thumbnailsPanel, PhotoPanelItem compare, int sensitivity, int accuracy) {//####[143]####
        List<PhotoPanelItem> result = Collections.synchronizedList(new ArrayList<PhotoPanelItem>());//####[144]####
        BufferedImage image = new BufferedImage(accuracy, accuracy, BufferedImage.TYPE_INT_RGB);//####[146]####
        Graphics2D g2 = image.createGraphics();//####[147]####
        g2.drawImage(compare.getSquarePhoto().getScaledInstance(accuracy, accuracy, Image.SCALE_DEFAULT), null, null);//####[148]####
        int compCount = thumbnailsPanel.getComponentCount();//####[150]####
        TaskIDGroup group = new TaskIDGroup(compCount);//####[151]####
        for (int i = 0; i < compCount; i++) //####[153]####
        {//####[153]####
            TaskID id = compareColor2Task(thumbnailsPanel, result, image, i, sensitivity, accuracy);//####[154]####
            group.add(id);//####[155]####
        }//####[156]####
        try {//####[158]####
            group.waitTillFinished();//####[159]####
        } catch (ExecutionException e) {//####[160]####
            e.printStackTrace();//####[161]####
        } catch (InterruptedException e) {//####[162]####
            e.printStackTrace();//####[163]####
        }//####[164]####
        return result;//####[166]####
    }//####[167]####
//####[169]####
    private static volatile Method __pt__compareColor2Task_JPanel_ListPhotoPanelItem_BufferedImage_int_int_int_method = null;//####[169]####
    private synchronized static void __pt__compareColor2Task_JPanel_ListPhotoPanelItem_BufferedImage_int_int_int_ensureMethodVarSet() {//####[169]####
        if (__pt__compareColor2Task_JPanel_ListPhotoPanelItem_BufferedImage_int_int_int_method == null) {//####[169]####
            try {//####[169]####
                __pt__compareColor2Task_JPanel_ListPhotoPanelItem_BufferedImage_int_int_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__compareColor2Task", new Class[] {//####[169]####
                    JPanel.class, List.class, BufferedImage.class, int.class, int.class, int.class//####[169]####
                });//####[169]####
            } catch (Exception e) {//####[169]####
                e.printStackTrace();//####[169]####
            }//####[169]####
        }//####[169]####
    }//####[169]####
    public static TaskID<Void> compareColor2Task(Object thumbnailsPanel, Object result, Object image, Object i, Object sensitivity, Object accuracy) {//####[169]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[169]####
        return compareColor2Task(thumbnailsPanel, result, image, i, sensitivity, accuracy, new TaskInfo());//####[169]####
    }//####[169]####
    public static TaskID<Void> compareColor2Task(Object thumbnailsPanel, Object result, Object image, Object i, Object sensitivity, Object accuracy, TaskInfo taskinfo) {//####[169]####
        // ensure Method variable is set//####[169]####
        if (__pt__compareColor2Task_JPanel_ListPhotoPanelItem_BufferedImage_int_int_int_method == null) {//####[169]####
            __pt__compareColor2Task_JPanel_ListPhotoPanelItem_BufferedImage_int_int_int_ensureMethodVarSet();//####[169]####
        }//####[169]####
        List<Integer> __pt__taskIdIndexList = new ArrayList<Integer>();//####[169]####
        List<Integer> __pt__queueIndexList = new ArrayList<Integer>();//####[169]####
        if (thumbnailsPanel instanceof BlockingQueue) {//####[169]####
            __pt__queueIndexList.add(0);//####[169]####
        }//####[169]####
        if (thumbnailsPanel instanceof TaskID) {//####[169]####
            taskinfo.addDependsOn((TaskID)thumbnailsPanel);//####[169]####
            __pt__taskIdIndexList.add(0);//####[169]####
        }//####[169]####
        if (result instanceof BlockingQueue) {//####[169]####
            __pt__queueIndexList.add(1);//####[169]####
        }//####[169]####
        if (result instanceof TaskID) {//####[169]####
            taskinfo.addDependsOn((TaskID)result);//####[169]####
            __pt__taskIdIndexList.add(1);//####[169]####
        }//####[169]####
        if (image instanceof BlockingQueue) {//####[169]####
            __pt__queueIndexList.add(2);//####[169]####
        }//####[169]####
        if (image instanceof TaskID) {//####[169]####
            taskinfo.addDependsOn((TaskID)image);//####[169]####
            __pt__taskIdIndexList.add(2);//####[169]####
        }//####[169]####
        if (i instanceof BlockingQueue) {//####[169]####
            __pt__queueIndexList.add(3);//####[169]####
        }//####[169]####
        if (i instanceof TaskID) {//####[169]####
            taskinfo.addDependsOn((TaskID)i);//####[169]####
            __pt__taskIdIndexList.add(3);//####[169]####
        }//####[169]####
        if (sensitivity instanceof BlockingQueue) {//####[169]####
            __pt__queueIndexList.add(4);//####[169]####
        }//####[169]####
        if (sensitivity instanceof TaskID) {//####[169]####
            taskinfo.addDependsOn((TaskID)sensitivity);//####[169]####
            __pt__taskIdIndexList.add(4);//####[169]####
        }//####[169]####
        if (accuracy instanceof BlockingQueue) {//####[169]####
            __pt__queueIndexList.add(5);//####[169]####
        }//####[169]####
        if (accuracy instanceof TaskID) {//####[169]####
            taskinfo.addDependsOn((TaskID)accuracy);//####[169]####
            __pt__taskIdIndexList.add(5);//####[169]####
        }//####[169]####
        int[] __pt__queueIndexArray = new int[__pt__queueIndexList.size()];//####[169]####
        for (int __pt__i = 0; __pt__i < __pt__queueIndexArray.length; __pt__i++) {//####[169]####
            __pt__queueIndexArray[__pt__i] = __pt__queueIndexList.get(__pt__i);//####[169]####
        }//####[169]####
        taskinfo.setQueueArgIndexes(__pt__queueIndexArray);//####[169]####
        if (__pt__queueIndexArray.length > 0) {//####[169]####
            taskinfo.setIsPipeline(true);//####[169]####
        }//####[169]####
        int[] __pt__taskIdIndexArray = new int[__pt__taskIdIndexList.size()];//####[169]####
        for (int __pt__i = 0; __pt__i < __pt__taskIdIndexArray.length; __pt__i++) {//####[169]####
            __pt__taskIdIndexArray[__pt__i] = __pt__taskIdIndexList.get(__pt__i);//####[169]####
        }//####[169]####
        taskinfo.setTaskIdArgIndexes(__pt__taskIdIndexArray);//####[169]####
        taskinfo.setParameters(thumbnailsPanel, result, image, i, sensitivity, accuracy);//####[169]####
        taskinfo.setMethod(__pt__compareColor2Task_JPanel_ListPhotoPanelItem_BufferedImage_int_int_int_method);//####[169]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[169]####
    }//####[169]####
    public static void __pt__compareColor2Task(JPanel thumbnailsPanel, List<PhotoPanelItem> result, BufferedImage image, int i, int sensitivity, int accuracy) {//####[169]####
        boolean match = true;//####[170]####
        PhotoPanelItem pi = (PhotoPanelItem) thumbnailsPanel.getComponent(i);//####[171]####
        BufferedImage temp = new BufferedImage(accuracy, accuracy, BufferedImage.TYPE_INT_RGB);//####[173]####
        Graphics2D g3 = temp.createGraphics();//####[174]####
        g3.drawImage(pi.getSquarePhoto().getScaledInstance(accuracy, accuracy, Image.SCALE_DEFAULT), null, null);//####[175]####
        for (int y = 0; y < accuracy; y++) //####[177]####
        {//####[177]####
            for (int x = 0; x < accuracy; x++) //####[178]####
            {//####[178]####
                Color tc = new Color(image.getRGB(x, y));//####[179]####
                int tr = tc.getRed();//####[180]####
                int tg = tc.getGreen();//####[181]####
                int tb = tc.getBlue();//####[182]####
                Color pc = new Color(temp.getRGB(x, y));//####[184]####
                int pr = pc.getRed();//####[185]####
                int pg = pc.getGreen();//####[186]####
                int pb = pc.getBlue();//####[187]####
                int distance = (int) Math.sqrt(0.3 * (tr - pr) * (tr - pr) + 0.59 * (tg - pg) * (tg - pg) + 0.11 * (tb - pb) * (tb - pb));//####[189]####
                if (distance > sensitivity) //####[190]####
                {//####[190]####
                    match = false;//####[191]####
                }//####[192]####
            }//####[193]####
        }//####[194]####
        if (match) //####[195]####
        {//####[195]####
            result.add(pi);//####[196]####
        }//####[197]####
    }//####[198]####
//####[198]####
}//####[198]####
