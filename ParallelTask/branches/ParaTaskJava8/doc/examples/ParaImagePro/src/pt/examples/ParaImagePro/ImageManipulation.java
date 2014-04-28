package pt.examples.ParaImagePro;//####[1]####
//####[1]####
import java.awt.Color;//####[3]####
import java.awt.Graphics2D;//####[4]####
import java.awt.Image;//####[5]####
import java.awt.image.BufferedImage;//####[6]####
import java.awt.image.BufferedImageOp;//####[7]####
import java.awt.image.ConvolveOp;//####[8]####
import java.io.*;//####[9]####
import javax.imageio.ImageIO;//####[10]####
import java.awt.image.Kernel;//####[11]####
import java.awt.image.LookupOp;//####[12]####
import java.awt.image.ShortLookupTable;//####[13]####
//####[13]####
//-- ParaTask related imports//####[13]####
import pt.runtime.*;//####[13]####
import java.util.concurrent.ExecutionException;//####[13]####
import java.util.concurrent.locks.*;//####[13]####
import java.lang.reflect.*;//####[13]####
import pt.runtime.GuiThread;//####[13]####
import java.util.concurrent.BlockingQueue;//####[13]####
import java.util.ArrayList;//####[13]####
import java.util.List;//####[13]####
//####[13]####
public class ImageManipulation {//####[15]####
    static{ParaTask.init();}//####[15]####
    /*  ParaTask helper method to access private/protected slots *///####[15]####
    public void __pt__accessPrivateSlot(Method m, Object instance, Future arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[15]####
        if (m.getParameterTypes().length == 0)//####[15]####
            m.invoke(instance);//####[15]####
        else if ((m.getParameterTypes().length == 1))//####[15]####
            m.invoke(instance, arg);//####[15]####
        else //####[15]####
            m.invoke(instance, arg, interResult);//####[15]####
    }//####[15]####
//####[17]####
    public static void timeWaster(int n) {//####[17]####
        long start = System.currentTimeMillis();//####[18]####
        newton(n);//####[20]####
        long end = System.currentTimeMillis();//####[22]####
    }//####[24]####
//####[26]####
    private static volatile Method __pt__invertTask_ImagePanelItem_method = null;//####[26]####
    private synchronized static void __pt__invertTask_ImagePanelItem_ensureMethodVarSet() {//####[26]####
        if (__pt__invertTask_ImagePanelItem_method == null) {//####[26]####
            try {//####[26]####
                __pt__invertTask_ImagePanelItem_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__invertTask", new Class[] {//####[26]####
                    ImagePanelItem.class//####[26]####
                });//####[26]####
            } catch (Exception e) {//####[26]####
                e.printStackTrace();//####[26]####
            }//####[26]####
        }//####[26]####
    }//####[26]####
    public static Future<ImageCombo> invertTask(ImagePanelItem panel) {//####[26]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[26]####
        return invertTask(panel, new Task());//####[26]####
    }//####[26]####
    public static Future<ImageCombo> invertTask(ImagePanelItem panel, Task taskinfo) {//####[26]####
        // ensure Method variable is set//####[26]####
        if (__pt__invertTask_ImagePanelItem_method == null) {//####[26]####
            __pt__invertTask_ImagePanelItem_ensureMethodVarSet();//####[26]####
        }//####[26]####
        taskinfo.setParameters(panel);//####[26]####
        taskinfo.setMethod(__pt__invertTask_ImagePanelItem_method);//####[26]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[26]####
    }//####[26]####
    public static Future<ImageCombo> invertTask(Future<ImagePanelItem> panel) {//####[26]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[26]####
        return invertTask(panel, new Task());//####[26]####
    }//####[26]####
    public static Future<ImageCombo> invertTask(Future<ImagePanelItem> panel, Task taskinfo) {//####[26]####
        // ensure Method variable is set//####[26]####
        if (__pt__invertTask_ImagePanelItem_method == null) {//####[26]####
            __pt__invertTask_ImagePanelItem_ensureMethodVarSet();//####[26]####
        }//####[26]####
        taskinfo.setTaskIdArgIndexes(0);//####[26]####
        taskinfo.addDependsOn(panel);//####[26]####
        taskinfo.setParameters(panel);//####[26]####
        taskinfo.setMethod(__pt__invertTask_ImagePanelItem_method);//####[26]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[26]####
    }//####[26]####
    public static Future<ImageCombo> invertTask(BlockingQueue<ImagePanelItem> panel) {//####[26]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[26]####
        return invertTask(panel, new Task());//####[26]####
    }//####[26]####
    public static Future<ImageCombo> invertTask(BlockingQueue<ImagePanelItem> panel, Task taskinfo) {//####[26]####
        // ensure Method variable is set//####[26]####
        if (__pt__invertTask_ImagePanelItem_method == null) {//####[26]####
            __pt__invertTask_ImagePanelItem_ensureMethodVarSet();//####[26]####
        }//####[26]####
        taskinfo.setQueueArgIndexes(0);//####[26]####
        taskinfo.setIsPipeline(true);//####[26]####
        taskinfo.setParameters(panel);//####[26]####
        taskinfo.setMethod(__pt__invertTask_ImagePanelItem_method);//####[26]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[26]####
    }//####[26]####
    public static ImageCombo __pt__invertTask(ImagePanelItem panel) {//####[26]####
        return invert(panel);//####[27]####
    }//####[28]####
//####[28]####
//####[30]####
    public static ImageCombo invert(ImagePanelItem panel) {//####[30]####
        Image imageLarge = panel.getImageLarge();//####[31]####
        short[] invert = new short[256];//####[32]####
        for (int i = 0; i < 256; i++) //####[33]####
        invert[i] = (short) (255 - i);//####[33]####
        BufferedImageOp invertOp = new LookupOp(new ShortLookupTable(0, invert), null);//####[34]####
        BufferedImage mBufferedImageLarge = new BufferedImage(imageLarge.getWidth(null), imageLarge.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[35]####
        Graphics2D g2 = mBufferedImageLarge.createGraphics();//####[36]####
        g2.drawImage(imageLarge, null, null);//####[37]####
        mBufferedImageLarge = invertOp.filter(mBufferedImageLarge, null);//####[38]####
        Image imageSquare = panel.getImageSquare();//####[40]####
        BufferedImage mBufferedImageSquare = new BufferedImage(imageSquare.getWidth(null), imageSquare.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[41]####
        Graphics2D g3 = mBufferedImageSquare.createGraphics();//####[42]####
        g3.drawImage(imageSquare, null, null);//####[43]####
        mBufferedImageSquare = invertOp.filter(mBufferedImageSquare, null);//####[44]####
        Image imageMedium = panel.getImageMedium();//####[46]####
        BufferedImage mBufferedImageMedium = new BufferedImage(imageMedium.getWidth(null), imageMedium.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[47]####
        Graphics2D g4 = mBufferedImageMedium.createGraphics();//####[48]####
        g4.drawImage(imageMedium, null, null);//####[49]####
        mBufferedImageMedium = invertOp.filter(mBufferedImageMedium, null);//####[50]####
        ImageCombo combo = new ImageCombo(mBufferedImageLarge, mBufferedImageSquare, mBufferedImageMedium);//####[52]####
        timeWaster(MainFrame.timeWasterSize);//####[54]####
        return combo;//####[55]####
    }//####[56]####
//####[58]####
    private static volatile Method __pt__edgeDetectTask_ImagePanelItem_method = null;//####[58]####
    private synchronized static void __pt__edgeDetectTask_ImagePanelItem_ensureMethodVarSet() {//####[58]####
        if (__pt__edgeDetectTask_ImagePanelItem_method == null) {//####[58]####
            try {//####[58]####
                __pt__edgeDetectTask_ImagePanelItem_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__edgeDetectTask", new Class[] {//####[58]####
                    ImagePanelItem.class//####[58]####
                });//####[58]####
            } catch (Exception e) {//####[58]####
                e.printStackTrace();//####[58]####
            }//####[58]####
        }//####[58]####
    }//####[58]####
    public static Future<ImageCombo> edgeDetectTask(ImagePanelItem panel) {//####[58]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[58]####
        return edgeDetectTask(panel, new Task());//####[58]####
    }//####[58]####
    public static Future<ImageCombo> edgeDetectTask(ImagePanelItem panel, Task taskinfo) {//####[58]####
        // ensure Method variable is set//####[58]####
        if (__pt__edgeDetectTask_ImagePanelItem_method == null) {//####[58]####
            __pt__edgeDetectTask_ImagePanelItem_ensureMethodVarSet();//####[58]####
        }//####[58]####
        taskinfo.setParameters(panel);//####[58]####
        taskinfo.setMethod(__pt__edgeDetectTask_ImagePanelItem_method);//####[58]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[58]####
    }//####[58]####
    public static Future<ImageCombo> edgeDetectTask(Future<ImagePanelItem> panel) {//####[58]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[58]####
        return edgeDetectTask(panel, new Task());//####[58]####
    }//####[58]####
    public static Future<ImageCombo> edgeDetectTask(Future<ImagePanelItem> panel, Task taskinfo) {//####[58]####
        // ensure Method variable is set//####[58]####
        if (__pt__edgeDetectTask_ImagePanelItem_method == null) {//####[58]####
            __pt__edgeDetectTask_ImagePanelItem_ensureMethodVarSet();//####[58]####
        }//####[58]####
        taskinfo.setTaskIdArgIndexes(0);//####[58]####
        taskinfo.addDependsOn(panel);//####[58]####
        taskinfo.setParameters(panel);//####[58]####
        taskinfo.setMethod(__pt__edgeDetectTask_ImagePanelItem_method);//####[58]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[58]####
    }//####[58]####
    public static Future<ImageCombo> edgeDetectTask(BlockingQueue<ImagePanelItem> panel) {//####[58]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[58]####
        return edgeDetectTask(panel, new Task());//####[58]####
    }//####[58]####
    public static Future<ImageCombo> edgeDetectTask(BlockingQueue<ImagePanelItem> panel, Task taskinfo) {//####[58]####
        // ensure Method variable is set//####[58]####
        if (__pt__edgeDetectTask_ImagePanelItem_method == null) {//####[58]####
            __pt__edgeDetectTask_ImagePanelItem_ensureMethodVarSet();//####[58]####
        }//####[58]####
        taskinfo.setQueueArgIndexes(0);//####[58]####
        taskinfo.setIsPipeline(true);//####[58]####
        taskinfo.setParameters(panel);//####[58]####
        taskinfo.setMethod(__pt__edgeDetectTask_ImagePanelItem_method);//####[58]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[58]####
    }//####[58]####
    public static ImageCombo __pt__edgeDetectTask(ImagePanelItem panel) {//####[58]####
        return edgeDetect(panel);//####[59]####
    }//####[60]####
//####[60]####
//####[62]####
    public static ImageCombo edgeDetect(ImagePanelItem panel) {//####[62]####
        Image imageLarge = panel.getImageLarge();//####[63]####
        float[] kernel = { 0.0f, -1.0f, 0.0f, -1.0f, 4.0f, -1.0f, 0.0f, -1.0f, 0.0f };//####[64]####
        BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, kernel));//####[65]####
        BufferedImage mBufferedImageLarge = new BufferedImage(imageLarge.getWidth(null), imageLarge.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[66]####
        Graphics2D g2 = mBufferedImageLarge.createGraphics();//####[67]####
        g2.drawImage(imageLarge, null, null);//####[68]####
        mBufferedImageLarge = op.filter(mBufferedImageLarge, null);//####[69]####
        Image imageSquare = panel.getImageSquare();//####[71]####
        BufferedImage mBufferedImageSquare = new BufferedImage(imageSquare.getWidth(null), imageSquare.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[72]####
        Graphics2D g3 = mBufferedImageSquare.createGraphics();//####[73]####
        g3.drawImage(imageSquare, null, null);//####[74]####
        mBufferedImageSquare = op.filter(mBufferedImageSquare, null);//####[75]####
        Image imageMed = panel.getImageMedium();//####[77]####
        BufferedImage mBufferedImageMed = new BufferedImage(imageMed.getWidth(null), imageMed.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[78]####
        Graphics2D g4 = mBufferedImageMed.createGraphics();//####[79]####
        g4.drawImage(imageMed, null, null);//####[80]####
        mBufferedImageMed = op.filter(mBufferedImageMed, null);//####[81]####
        ImageCombo combo = new ImageCombo(mBufferedImageLarge, mBufferedImageSquare, mBufferedImageMed);//####[83]####
        timeWaster(MainFrame.timeWasterSize);//####[84]####
        return combo;//####[85]####
    }//####[86]####
//####[88]####
    private static volatile Method __pt__getSmallSquareTask_TaskIDImage_method = null;//####[88]####
    private synchronized static void __pt__getSmallSquareTask_TaskIDImage_ensureMethodVarSet() {//####[88]####
        if (__pt__getSmallSquareTask_TaskIDImage_method == null) {//####[88]####
            try {//####[88]####
                __pt__getSmallSquareTask_TaskIDImage_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__getSmallSquareTask", new Class[] {//####[88]####
                    Future.class//####[88]####
                });//####[88]####
            } catch (Exception e) {//####[88]####
                e.printStackTrace();//####[88]####
            }//####[88]####
        }//####[88]####
    }//####[88]####
    public static Future<Image> getSmallSquareTask(Future<Image> image) {//####[88]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[88]####
        return getSmallSquareTask(image, new Task());//####[88]####
    }//####[88]####
    public static Future<Image> getSmallSquareTask(Future<Image> image, Task taskinfo) {//####[88]####
        // ensure Method variable is set//####[88]####
        if (__pt__getSmallSquareTask_TaskIDImage_method == null) {//####[88]####
            __pt__getSmallSquareTask_TaskIDImage_ensureMethodVarSet();//####[88]####
        }//####[88]####
        taskinfo.setParameters(image);//####[88]####
        taskinfo.setMethod(__pt__getSmallSquareTask_TaskIDImage_method);//####[88]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[88]####
    }//####[88]####
    public static Future<Image> getSmallSquareTask(BlockingQueue<Future<Image>> image) {//####[88]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[88]####
        return getSmallSquareTask(image, new Task());//####[88]####
    }//####[88]####
    public static Future<Image> getSmallSquareTask(BlockingQueue<Future<Image>> image, Task taskinfo) {//####[88]####
        // ensure Method variable is set//####[88]####
        if (__pt__getSmallSquareTask_TaskIDImage_method == null) {//####[88]####
            __pt__getSmallSquareTask_TaskIDImage_ensureMethodVarSet();//####[88]####
        }//####[88]####
        taskinfo.setQueueArgIndexes(0);//####[88]####
        taskinfo.setIsPipeline(true);//####[88]####
        taskinfo.setParameters(image);//####[88]####
        taskinfo.setMethod(__pt__getSmallSquareTask_TaskIDImage_method);//####[88]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[88]####
    }//####[88]####
    public static Image __pt__getSmallSquareTask(Future<Image> image) {//####[88]####
        try {//####[89]####
            return getSmallSquare(image.getReturnResult());//####[90]####
        } catch (Exception e) {//####[91]####
            e.printStackTrace();//####[92]####
            return null;//####[93]####
        }//####[94]####
    }//####[95]####
//####[95]####
//####[97]####
    private static volatile Method __pt__getMediumTask_TaskIDImage_method = null;//####[97]####
    private synchronized static void __pt__getMediumTask_TaskIDImage_ensureMethodVarSet() {//####[97]####
        if (__pt__getMediumTask_TaskIDImage_method == null) {//####[97]####
            try {//####[97]####
                __pt__getMediumTask_TaskIDImage_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__getMediumTask", new Class[] {//####[97]####
                    Future.class//####[97]####
                });//####[97]####
            } catch (Exception e) {//####[97]####
                e.printStackTrace();//####[97]####
            }//####[97]####
        }//####[97]####
    }//####[97]####
    public static Future<Image> getMediumTask(Future<Image> image) {//####[97]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[97]####
        return getMediumTask(image, new Task());//####[97]####
    }//####[97]####
    public static Future<Image> getMediumTask(Future<Image> image, Task taskinfo) {//####[97]####
        // ensure Method variable is set//####[97]####
        if (__pt__getMediumTask_TaskIDImage_method == null) {//####[97]####
            __pt__getMediumTask_TaskIDImage_ensureMethodVarSet();//####[97]####
        }//####[97]####
        taskinfo.setParameters(image);//####[97]####
        taskinfo.setMethod(__pt__getMediumTask_TaskIDImage_method);//####[97]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[97]####
    }//####[97]####
    public static Future<Image> getMediumTask(BlockingQueue<Future<Image>> image) {//####[97]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[97]####
        return getMediumTask(image, new Task());//####[97]####
    }//####[97]####
    public static Future<Image> getMediumTask(BlockingQueue<Future<Image>> image, Task taskinfo) {//####[97]####
        // ensure Method variable is set//####[97]####
        if (__pt__getMediumTask_TaskIDImage_method == null) {//####[97]####
            __pt__getMediumTask_TaskIDImage_ensureMethodVarSet();//####[97]####
        }//####[97]####
        taskinfo.setQueueArgIndexes(0);//####[97]####
        taskinfo.setIsPipeline(true);//####[97]####
        taskinfo.setParameters(image);//####[97]####
        taskinfo.setMethod(__pt__getMediumTask_TaskIDImage_method);//####[97]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[97]####
    }//####[97]####
    public static Image __pt__getMediumTask(Future<Image> image) {//####[97]####
        try {//####[98]####
            return getMedium(image.getReturnResult());//####[99]####
        } catch (Exception e) {//####[100]####
            e.printStackTrace();//####[101]####
            return null;//####[102]####
        }//####[103]####
    }//####[104]####
//####[104]####
//####[106]####
    public static Image getMedium(Image image) {//####[106]####
        int w = image.getWidth(null);//####[107]####
        int h = image.getHeight(null);//####[108]####
        Image im;//####[109]####
        w = Math.min(ImagePanelItem.dialogWidth, w);//####[111]####
        h = Math.min(ImagePanelItem.dialogHeight, h);//####[112]####
        if (w > h) //####[114]####
        im = image.getScaledInstance(w, -1, Image.SCALE_SMOOTH); else im = image.getScaledInstance(-1, h, Image.SCALE_SMOOTH);//####[115]####
        BufferedImage buf = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[119]####
        buf.getGraphics().drawImage(im, 0, 0, null);//####[120]####
        return buf;//####[121]####
    }//####[122]####
//####[125]####
    public static Image getSmallSquare(Image image) {//####[125]####
        Image im = image.getScaledInstance(ImagePanelItem.imageSize, ImagePanelItem.imageSize, Image.SCALE_SMOOTH);//####[126]####
        BufferedImage buf = new BufferedImage(im.getWidth(null), im.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[127]####
        buf.getGraphics().drawImage(im, 0, 0, null);//####[128]####
        return buf;//####[129]####
    }//####[130]####
//####[132]####
    private static volatile Method __pt__getImageFullTask_File_method = null;//####[132]####
    private synchronized static void __pt__getImageFullTask_File_ensureMethodVarSet() {//####[132]####
        if (__pt__getImageFullTask_File_method == null) {//####[132]####
            try {//####[132]####
                __pt__getImageFullTask_File_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__getImageFullTask", new Class[] {//####[132]####
                    File.class//####[132]####
                });//####[132]####
            } catch (Exception e) {//####[132]####
                e.printStackTrace();//####[132]####
            }//####[132]####
        }//####[132]####
    }//####[132]####
    public static Future<Image> getImageFullTask(File file) {//####[132]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[132]####
        return getImageFullTask(file, new Task());//####[132]####
    }//####[132]####
    public static Future<Image> getImageFullTask(File file, Task taskinfo) {//####[132]####
        // ensure Method variable is set//####[132]####
        if (__pt__getImageFullTask_File_method == null) {//####[132]####
            __pt__getImageFullTask_File_ensureMethodVarSet();//####[132]####
        }//####[132]####
        taskinfo.setParameters(file);//####[132]####
        taskinfo.setMethod(__pt__getImageFullTask_File_method);//####[132]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[132]####
    }//####[132]####
    public static Future<Image> getImageFullTask(Future<File> file) {//####[132]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[132]####
        return getImageFullTask(file, new Task());//####[132]####
    }//####[132]####
    public static Future<Image> getImageFullTask(Future<File> file, Task taskinfo) {//####[132]####
        // ensure Method variable is set//####[132]####
        if (__pt__getImageFullTask_File_method == null) {//####[132]####
            __pt__getImageFullTask_File_ensureMethodVarSet();//####[132]####
        }//####[132]####
        taskinfo.setTaskIdArgIndexes(0);//####[132]####
        taskinfo.addDependsOn(file);//####[132]####
        taskinfo.setParameters(file);//####[132]####
        taskinfo.setMethod(__pt__getImageFullTask_File_method);//####[132]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[132]####
    }//####[132]####
    public static Future<Image> getImageFullTask(BlockingQueue<File> file) {//####[132]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[132]####
        return getImageFullTask(file, new Task());//####[132]####
    }//####[132]####
    public static Future<Image> getImageFullTask(BlockingQueue<File> file, Task taskinfo) {//####[132]####
        // ensure Method variable is set//####[132]####
        if (__pt__getImageFullTask_File_method == null) {//####[132]####
            __pt__getImageFullTask_File_ensureMethodVarSet();//####[132]####
        }//####[132]####
        taskinfo.setQueueArgIndexes(0);//####[132]####
        taskinfo.setIsPipeline(true);//####[132]####
        taskinfo.setParameters(file);//####[132]####
        taskinfo.setMethod(__pt__getImageFullTask_File_method);//####[132]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[132]####
    }//####[132]####
    public static Image __pt__getImageFullTask(File file) {//####[132]####
        return getImageFull(file);//####[133]####
    }//####[134]####
//####[134]####
//####[135]####
    public static Image getImageFull(File file) {//####[135]####
        try {//####[136]####
            return ImageIO.read(file);//####[137]####
        } catch (IOException e) {//####[138]####
            e.printStackTrace();//####[139]####
        }//####[140]####
        return null;//####[141]####
    }//####[142]####
//####[144]####
    private static volatile Method __pt__sharpenTask_ImagePanelItem_method = null;//####[144]####
    private synchronized static void __pt__sharpenTask_ImagePanelItem_ensureMethodVarSet() {//####[144]####
        if (__pt__sharpenTask_ImagePanelItem_method == null) {//####[144]####
            try {//####[144]####
                __pt__sharpenTask_ImagePanelItem_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__sharpenTask", new Class[] {//####[144]####
                    ImagePanelItem.class//####[144]####
                });//####[144]####
            } catch (Exception e) {//####[144]####
                e.printStackTrace();//####[144]####
            }//####[144]####
        }//####[144]####
    }//####[144]####
    public static Future<ImageCombo> sharpenTask(ImagePanelItem panel) {//####[144]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[144]####
        return sharpenTask(panel, new Task());//####[144]####
    }//####[144]####
    public static Future<ImageCombo> sharpenTask(ImagePanelItem panel, Task taskinfo) {//####[144]####
        // ensure Method variable is set//####[144]####
        if (__pt__sharpenTask_ImagePanelItem_method == null) {//####[144]####
            __pt__sharpenTask_ImagePanelItem_ensureMethodVarSet();//####[144]####
        }//####[144]####
        taskinfo.setParameters(panel);//####[144]####
        taskinfo.setMethod(__pt__sharpenTask_ImagePanelItem_method);//####[144]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[144]####
    }//####[144]####
    public static Future<ImageCombo> sharpenTask(Future<ImagePanelItem> panel) {//####[144]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[144]####
        return sharpenTask(panel, new Task());//####[144]####
    }//####[144]####
    public static Future<ImageCombo> sharpenTask(Future<ImagePanelItem> panel, Task taskinfo) {//####[144]####
        // ensure Method variable is set//####[144]####
        if (__pt__sharpenTask_ImagePanelItem_method == null) {//####[144]####
            __pt__sharpenTask_ImagePanelItem_ensureMethodVarSet();//####[144]####
        }//####[144]####
        taskinfo.setTaskIdArgIndexes(0);//####[144]####
        taskinfo.addDependsOn(panel);//####[144]####
        taskinfo.setParameters(panel);//####[144]####
        taskinfo.setMethod(__pt__sharpenTask_ImagePanelItem_method);//####[144]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[144]####
    }//####[144]####
    public static Future<ImageCombo> sharpenTask(BlockingQueue<ImagePanelItem> panel) {//####[144]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[144]####
        return sharpenTask(panel, new Task());//####[144]####
    }//####[144]####
    public static Future<ImageCombo> sharpenTask(BlockingQueue<ImagePanelItem> panel, Task taskinfo) {//####[144]####
        // ensure Method variable is set//####[144]####
        if (__pt__sharpenTask_ImagePanelItem_method == null) {//####[144]####
            __pt__sharpenTask_ImagePanelItem_ensureMethodVarSet();//####[144]####
        }//####[144]####
        taskinfo.setQueueArgIndexes(0);//####[144]####
        taskinfo.setIsPipeline(true);//####[144]####
        taskinfo.setParameters(panel);//####[144]####
        taskinfo.setMethod(__pt__sharpenTask_ImagePanelItem_method);//####[144]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[144]####
    }//####[144]####
    public static ImageCombo __pt__sharpenTask(ImagePanelItem panel) {//####[144]####
        return sharpen(panel);//####[145]####
    }//####[146]####
//####[146]####
//####[148]####
    public static ImageCombo sharpen(ImagePanelItem panel) {//####[148]####
        Image imageLarge = panel.getImageLarge();//####[149]####
        float[] sharpKernel = { 0.0f, -1.0f, 0.0f, -1.0f, 5.0f, -1.0f, 0.0f, -1.0f, 0.0f };//####[150]####
        BufferedImageOp sharpen = new ConvolveOp(new Kernel(3, 3, sharpKernel), ConvolveOp.EDGE_NO_OP, null);//####[151]####
        BufferedImage mBufferedImageLarge = new BufferedImage(imageLarge.getWidth(null), imageLarge.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[152]####
        Graphics2D g2 = mBufferedImageLarge.createGraphics();//####[153]####
        g2.drawImage(imageLarge, null, null);//####[154]####
        mBufferedImageLarge = sharpen.filter(mBufferedImageLarge, null);//####[155]####
        Image imageSquare = panel.getImageSquare();//####[157]####
        BufferedImage mBufferedImageSquare = new BufferedImage(imageSquare.getWidth(null), imageSquare.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[158]####
        Graphics2D g3 = mBufferedImageSquare.createGraphics();//####[159]####
        g3.drawImage(imageSquare, null, null);//####[160]####
        mBufferedImageSquare = sharpen.filter(mBufferedImageSquare, null);//####[161]####
        Image imageMed = panel.getImageMedium();//####[163]####
        BufferedImage mBufferedImagemed = new BufferedImage(imageMed.getWidth(null), imageMed.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[164]####
        Graphics2D g4 = mBufferedImagemed.createGraphics();//####[165]####
        g4.drawImage(imageMed, null, null);//####[166]####
        mBufferedImagemed = sharpen.filter(mBufferedImagemed, null);//####[167]####
        ImageCombo combo = new ImageCombo(mBufferedImageLarge, mBufferedImageSquare, mBufferedImagemed);//####[168]####
        timeWaster(MainFrame.timeWasterSize);//####[169]####
        return combo;//####[170]####
    }//####[171]####
//####[173]####
    private static volatile Method __pt__blurTask_ImagePanelItem_method = null;//####[173]####
    private synchronized static void __pt__blurTask_ImagePanelItem_ensureMethodVarSet() {//####[173]####
        if (__pt__blurTask_ImagePanelItem_method == null) {//####[173]####
            try {//####[173]####
                __pt__blurTask_ImagePanelItem_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__blurTask", new Class[] {//####[173]####
                    ImagePanelItem.class//####[173]####
                });//####[173]####
            } catch (Exception e) {//####[173]####
                e.printStackTrace();//####[173]####
            }//####[173]####
        }//####[173]####
    }//####[173]####
    public static Future<ImageCombo> blurTask(ImagePanelItem panel) {//####[173]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[173]####
        return blurTask(panel, new Task());//####[173]####
    }//####[173]####
    public static Future<ImageCombo> blurTask(ImagePanelItem panel, Task taskinfo) {//####[173]####
        // ensure Method variable is set//####[173]####
        if (__pt__blurTask_ImagePanelItem_method == null) {//####[173]####
            __pt__blurTask_ImagePanelItem_ensureMethodVarSet();//####[173]####
        }//####[173]####
        taskinfo.setParameters(panel);//####[173]####
        taskinfo.setMethod(__pt__blurTask_ImagePanelItem_method);//####[173]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[173]####
    }//####[173]####
    public static Future<ImageCombo> blurTask(Future<ImagePanelItem> panel) {//####[173]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[173]####
        return blurTask(panel, new Task());//####[173]####
    }//####[173]####
    public static Future<ImageCombo> blurTask(Future<ImagePanelItem> panel, Task taskinfo) {//####[173]####
        // ensure Method variable is set//####[173]####
        if (__pt__blurTask_ImagePanelItem_method == null) {//####[173]####
            __pt__blurTask_ImagePanelItem_ensureMethodVarSet();//####[173]####
        }//####[173]####
        taskinfo.setTaskIdArgIndexes(0);//####[173]####
        taskinfo.addDependsOn(panel);//####[173]####
        taskinfo.setParameters(panel);//####[173]####
        taskinfo.setMethod(__pt__blurTask_ImagePanelItem_method);//####[173]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[173]####
    }//####[173]####
    public static Future<ImageCombo> blurTask(BlockingQueue<ImagePanelItem> panel) {//####[173]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[173]####
        return blurTask(panel, new Task());//####[173]####
    }//####[173]####
    public static Future<ImageCombo> blurTask(BlockingQueue<ImagePanelItem> panel, Task taskinfo) {//####[173]####
        // ensure Method variable is set//####[173]####
        if (__pt__blurTask_ImagePanelItem_method == null) {//####[173]####
            __pt__blurTask_ImagePanelItem_ensureMethodVarSet();//####[173]####
        }//####[173]####
        taskinfo.setQueueArgIndexes(0);//####[173]####
        taskinfo.setIsPipeline(true);//####[173]####
        taskinfo.setParameters(panel);//####[173]####
        taskinfo.setMethod(__pt__blurTask_ImagePanelItem_method);//####[173]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[173]####
    }//####[173]####
    public static ImageCombo __pt__blurTask(ImagePanelItem panel) {//####[173]####
        return blur(panel);//####[174]####
    }//####[175]####
//####[175]####
//####[176]####
    public static ImageCombo blur(ImagePanelItem panel) {//####[176]####
        Image imageLarge = panel.getImageLarge();//####[177]####
        float ninth = 1.0f / 9.0f;//####[178]####
        float[] kernel = { ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth, ninth };//####[179]####
        BufferedImageOp op = new ConvolveOp(new Kernel(3, 3, kernel));//####[180]####
        BufferedImage mBufferedImageLarge = new BufferedImage(imageLarge.getWidth(null), imageLarge.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[181]####
        Graphics2D g2 = mBufferedImageLarge.createGraphics();//####[182]####
        g2.drawImage(imageLarge, null, null);//####[183]####
        mBufferedImageLarge = op.filter(mBufferedImageLarge, null);//####[184]####
        Image imageSquare = panel.getImageSquare();//####[186]####
        BufferedImage mBufferedImageSquare = new BufferedImage(imageSquare.getWidth(null), imageSquare.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[187]####
        Graphics2D g3 = mBufferedImageSquare.createGraphics();//####[188]####
        g3.drawImage(imageSquare, null, null);//####[189]####
        mBufferedImageSquare = op.filter(mBufferedImageSquare, null);//####[190]####
        Image imageMed = panel.getImageMedium();//####[192]####
        BufferedImage mBufferedImageMed = new BufferedImage(imageMed.getWidth(null), imageMed.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[193]####
        Graphics2D g4 = mBufferedImageMed.createGraphics();//####[194]####
        g4.drawImage(imageMed, null, null);//####[195]####
        mBufferedImageMed = op.filter(mBufferedImageMed, null);//####[196]####
        ImageCombo combo = new ImageCombo(mBufferedImageLarge, mBufferedImageSquare, mBufferedImageMed);//####[198]####
        timeWaster(MainFrame.timeWasterSize);//####[199]####
        return combo;//####[200]####
    }//####[201]####
//####[203]####
    private static Color newton(Complex z) {//####[203]####
        double EPSILON = 0.00000001;//####[204]####
        Complex four = new Complex(4, 0);//####[205]####
        Complex one = new Complex(1, 0);//####[206]####
        Complex root1 = new Complex(1, 0);//####[207]####
        Complex root2 = new Complex(-1, 0);//####[208]####
        Complex root3 = new Complex(0, 1);//####[209]####
        Complex root4 = new Complex(0, -1);//####[210]####
        for (int i = 0; i < 100; i++) //####[211]####
        {//####[211]####
            Complex f = z.times(z).times(z).times(z).minus(one);//####[212]####
            Complex fp = four.times(z).times(z).times(z);//####[213]####
            z = z.minus(f.divides(fp));//####[214]####
            if (z.minus(root1).abs() <= EPSILON) //####[215]####
            return Color.WHITE;//####[215]####
            if (z.minus(root2).abs() <= EPSILON) //####[216]####
            return Color.RED;//####[216]####
            if (z.minus(root3).abs() <= EPSILON) //####[217]####
            return Color.GREEN;//####[217]####
            if (z.minus(root4).abs() <= EPSILON) //####[218]####
            return Color.BLUE;//####[218]####
        }//####[219]####
        return Color.BLACK;//####[220]####
    }//####[221]####
//####[223]####
    private static void newton(int N) {//####[223]####
        double xmin = -1.0;//####[224]####
        double ymin = -1.0;//####[225]####
        double width = 2.0;//####[226]####
        double height = 2.0;//####[227]####
        for (int i = 0; i < N; i++) //####[228]####
        {//####[228]####
            for (int j = 0; j < N; j++) //####[229]####
            {//####[229]####
                double x = xmin + i * width / N;//####[230]####
                double y = ymin + j * height / N;//####[231]####
                Complex z = new Complex(x, y);//####[232]####
                newton(z);//####[233]####
            }//####[234]####
        }//####[235]####
    }//####[236]####
}//####[236]####
