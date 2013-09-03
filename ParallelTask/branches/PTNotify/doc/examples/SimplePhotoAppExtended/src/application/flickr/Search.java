package application.flickr;//####[1]####
//####[1]####
import java.awt.Image;//####[3]####
import java.util.*;//####[4]####
import java.io.IOException;//####[5]####
import java.util.Collection;//####[6]####
import java.util.Collections;//####[7]####
import org.xml.sax.SAXException;//####[9]####
import paratask.runtime.CurrentTask;//####[11]####
import application.SearchProjectPanel;//####[12]####
import application.ImageManipulation;//####[14]####
import com.aetrion.flickr.Flickr;//####[16]####
import com.aetrion.flickr.FlickrException;//####[17]####
import com.aetrion.flickr.photos.Photo;//####[18]####
import com.aetrion.flickr.photos.PhotoList;//####[19]####
import com.aetrion.flickr.photos.PhotosInterface;//####[20]####
import com.aetrion.flickr.photos.SearchParameters;//####[21]####
import com.aetrion.flickr.photos.Size;//####[22]####
//####[22]####
//-- ParaTask related imports//####[22]####
import pt.runtime.*;//####[22]####
import java.util.concurrent.ExecutionException;//####[22]####
import java.util.concurrent.locks.*;//####[22]####
import java.lang.reflect.*;//####[22]####
import pt.runtime.GuiThread;//####[22]####
import java.util.concurrent.BlockingQueue;//####[22]####
import java.util.ArrayList;//####[22]####
import java.util.List;//####[22]####
//####[22]####
public class Search {//####[24]####
    static{ParaTask.init();}//####[24]####
    /*  ParaTask helper method to access private/protected slots *///####[24]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[24]####
        if (m.getParameterTypes().length == 0)//####[24]####
            m.invoke(instance);//####[24]####
        else if ((m.getParameterTypes().length == 1))//####[24]####
            m.invoke(instance, arg);//####[24]####
        else //####[24]####
            m.invoke(instance, arg, interResult);//####[24]####
    }//####[24]####
//####[26]####
    public static final String API_KEY = "98cac2ba2664223a4cf1bb5b97d39fab";//####[26]####
//####[28]####
    private static Flickr flickr = new Flickr(API_KEY);//####[28]####
//####[29]####
    private static PhotosInterface photoInterface = flickr.getPhotosInterface();//####[29]####
//####[46]####
    private static volatile Method __pt__getSquareImageTask_Photo_method = null;//####[46]####
    private synchronized static void __pt__getSquareImageTask_Photo_ensureMethodVarSet() {//####[46]####
        if (__pt__getSquareImageTask_Photo_method == null) {//####[46]####
            try {//####[46]####
                __pt__getSquareImageTask_Photo_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__getSquareImageTask", new Class[] {//####[46]####
                    Photo.class//####[46]####
                });//####[46]####
            } catch (Exception e) {//####[46]####
                e.printStackTrace();//####[46]####
            }//####[46]####
        }//####[46]####
    }//####[46]####
    public static TaskID<Image> getSquareImageTask(Photo p) {//####[46]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[46]####
        return getSquareImageTask(p, new TaskInfo());//####[46]####
    }//####[46]####
    public static TaskID<Image> getSquareImageTask(Photo p, TaskInfo taskinfo) {//####[46]####
        // ensure Method variable is set//####[46]####
        if (__pt__getSquareImageTask_Photo_method == null) {//####[46]####
            __pt__getSquareImageTask_Photo_ensureMethodVarSet();//####[46]####
        }//####[46]####
        taskinfo.setParameters(p);//####[46]####
        taskinfo.setMethod(__pt__getSquareImageTask_Photo_method);//####[46]####
        taskinfo.setInteractive(true);//####[46]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[46]####
    }//####[46]####
    public static TaskID<Image> getSquareImageTask(TaskID<Photo> p) {//####[46]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[46]####
        return getSquareImageTask(p, new TaskInfo());//####[46]####
    }//####[46]####
    public static TaskID<Image> getSquareImageTask(TaskID<Photo> p, TaskInfo taskinfo) {//####[46]####
        // ensure Method variable is set//####[46]####
        if (__pt__getSquareImageTask_Photo_method == null) {//####[46]####
            __pt__getSquareImageTask_Photo_ensureMethodVarSet();//####[46]####
        }//####[46]####
        taskinfo.setTaskIdArgIndexes(0);//####[46]####
        taskinfo.addDependsOn(p);//####[46]####
        taskinfo.setParameters(p);//####[46]####
        taskinfo.setMethod(__pt__getSquareImageTask_Photo_method);//####[46]####
        taskinfo.setInteractive(true);//####[46]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[46]####
    }//####[46]####
    public static TaskID<Image> getSquareImageTask(BlockingQueue<Photo> p) {//####[46]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[46]####
        return getSquareImageTask(p, new TaskInfo());//####[46]####
    }//####[46]####
    public static TaskID<Image> getSquareImageTask(BlockingQueue<Photo> p, TaskInfo taskinfo) {//####[46]####
        // ensure Method variable is set//####[46]####
        if (__pt__getSquareImageTask_Photo_method == null) {//####[46]####
            __pt__getSquareImageTask_Photo_ensureMethodVarSet();//####[46]####
        }//####[46]####
        taskinfo.setQueueArgIndexes(0);//####[46]####
        taskinfo.setIsPipeline(true);//####[46]####
        taskinfo.setParameters(p);//####[46]####
        taskinfo.setMethod(__pt__getSquareImageTask_Photo_method);//####[46]####
        taskinfo.setInteractive(true);//####[46]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[46]####
    }//####[46]####
    public static Image __pt__getSquareImageTask(Photo p) {//####[46]####
        return getSquareImage(p);//####[47]####
    }//####[48]####
//####[48]####
//####[50]####
    public static Image getSquareImage(Photo p) {//####[50]####
        Image image = null;//####[51]####
        try {//####[52]####
            image = photoInterface.getImage(p, Size.SQUARE);//####[53]####
        } catch (IOException e) {//####[54]####
            e.printStackTrace();//####[55]####
        } catch (FlickrException e) {//####[56]####
            e.printStackTrace();//####[57]####
        }//####[58]####
        return image;//####[59]####
    }//####[60]####
//####[62]####
    public static Image getThumbnailImage(Photo p) {//####[62]####
        Image image = null;//####[63]####
        try {//####[64]####
            image = photoInterface.getImage(p, Size.THUMB);//####[65]####
        } catch (IOException e) {//####[66]####
            e.printStackTrace();//####[67]####
        } catch (FlickrException e) {//####[68]####
            e.printStackTrace();//####[69]####
        }//####[70]####
        return image;//####[71]####
    }//####[72]####
//####[74]####
    private static volatile Method __pt__getMediumImageTask_Photo_method = null;//####[74]####
    private synchronized static void __pt__getMediumImageTask_Photo_ensureMethodVarSet() {//####[74]####
        if (__pt__getMediumImageTask_Photo_method == null) {//####[74]####
            try {//####[74]####
                __pt__getMediumImageTask_Photo_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__getMediumImageTask", new Class[] {//####[74]####
                    Photo.class//####[74]####
                });//####[74]####
            } catch (Exception e) {//####[74]####
                e.printStackTrace();//####[74]####
            }//####[74]####
        }//####[74]####
    }//####[74]####
    public static TaskID<Image> getMediumImageTask(Photo p) {//####[74]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[74]####
        return getMediumImageTask(p, new TaskInfo());//####[74]####
    }//####[74]####
    public static TaskID<Image> getMediumImageTask(Photo p, TaskInfo taskinfo) {//####[74]####
        // ensure Method variable is set//####[74]####
        if (__pt__getMediumImageTask_Photo_method == null) {//####[74]####
            __pt__getMediumImageTask_Photo_ensureMethodVarSet();//####[74]####
        }//####[74]####
        taskinfo.setParameters(p);//####[74]####
        taskinfo.setMethod(__pt__getMediumImageTask_Photo_method);//####[74]####
        taskinfo.setInteractive(true);//####[74]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[74]####
    }//####[74]####
    public static TaskID<Image> getMediumImageTask(TaskID<Photo> p) {//####[74]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[74]####
        return getMediumImageTask(p, new TaskInfo());//####[74]####
    }//####[74]####
    public static TaskID<Image> getMediumImageTask(TaskID<Photo> p, TaskInfo taskinfo) {//####[74]####
        // ensure Method variable is set//####[74]####
        if (__pt__getMediumImageTask_Photo_method == null) {//####[74]####
            __pt__getMediumImageTask_Photo_ensureMethodVarSet();//####[74]####
        }//####[74]####
        taskinfo.setTaskIdArgIndexes(0);//####[74]####
        taskinfo.addDependsOn(p);//####[74]####
        taskinfo.setParameters(p);//####[74]####
        taskinfo.setMethod(__pt__getMediumImageTask_Photo_method);//####[74]####
        taskinfo.setInteractive(true);//####[74]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[74]####
    }//####[74]####
    public static TaskID<Image> getMediumImageTask(BlockingQueue<Photo> p) {//####[74]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[74]####
        return getMediumImageTask(p, new TaskInfo());//####[74]####
    }//####[74]####
    public static TaskID<Image> getMediumImageTask(BlockingQueue<Photo> p, TaskInfo taskinfo) {//####[74]####
        // ensure Method variable is set//####[74]####
        if (__pt__getMediumImageTask_Photo_method == null) {//####[74]####
            __pt__getMediumImageTask_Photo_ensureMethodVarSet();//####[74]####
        }//####[74]####
        taskinfo.setQueueArgIndexes(0);//####[74]####
        taskinfo.setIsPipeline(true);//####[74]####
        taskinfo.setParameters(p);//####[74]####
        taskinfo.setMethod(__pt__getMediumImageTask_Photo_method);//####[74]####
        taskinfo.setInteractive(true);//####[74]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[74]####
    }//####[74]####
    public static Image __pt__getMediumImageTask(Photo p) {//####[74]####
        return getMediumImage(p);//####[75]####
    }//####[76]####
//####[76]####
//####[79]####
    public static Image getMediumImage(Photo p) {//####[79]####
        Image image = null;//####[80]####
        try {//####[81]####
            image = photoInterface.getImage(p, Size.MEDIUM);//####[82]####
        } catch (IOException e) {//####[83]####
            e.printStackTrace();//####[84]####
        } catch (FlickrException e) {//####[85]####
            e.printStackTrace();//####[86]####
        }//####[87]####
        return image;//####[88]####
    }//####[89]####
//####[91]####
    public static Image getSmallImage(Photo p) {//####[91]####
        Image image = null;//####[92]####
        try {//####[93]####
            image = photoInterface.getImage(p, Size.SMALL);//####[94]####
        } catch (IOException e) {//####[95]####
            e.printStackTrace();//####[96]####
        } catch (FlickrException e) {//####[97]####
            e.printStackTrace();//####[98]####
        }//####[99]####
        return image;//####[100]####
    }//####[101]####
//####[102]####
    public static Image getLargeImage(Photo p) {//####[102]####
        Image image = null;//####[103]####
        try {//####[104]####
            image = photoInterface.getImage(p, Size.LARGE);//####[105]####
        } catch (IOException e) {//####[106]####
            e.printStackTrace();//####[107]####
        } catch (FlickrException e) {//####[108]####
            e.printStackTrace();//####[109]####
        }//####[110]####
        return image;//####[111]####
    }//####[112]####
//####[139]####
    private static volatile Method __pt__searchTask_String_int_int_method = null;//####[139]####
    private synchronized static void __pt__searchTask_String_int_int_ensureMethodVarSet() {//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            try {//####[139]####
                __pt__searchTask_String_int_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__searchTask", new Class[] {//####[139]####
                    String.class, int.class, int.class//####[139]####
                });//####[139]####
            } catch (Exception e) {//####[139]####
                e.printStackTrace();//####[139]####
            }//####[139]####
        }//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(String str, int picsPerPage, int pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(String str, int picsPerPage, int pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(TaskID<String> str, int picsPerPage, int pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(TaskID<String> str, int picsPerPage, int pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setTaskIdArgIndexes(0);//####[139]####
        taskinfo.addDependsOn(str);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(BlockingQueue<String> str, int picsPerPage, int pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(BlockingQueue<String> str, int picsPerPage, int pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setQueueArgIndexes(0);//####[139]####
        taskinfo.setIsPipeline(true);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(String str, TaskID<Integer> picsPerPage, int pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(String str, TaskID<Integer> picsPerPage, int pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setTaskIdArgIndexes(1);//####[139]####
        taskinfo.addDependsOn(picsPerPage);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(TaskID<String> str, TaskID<Integer> picsPerPage, int pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(TaskID<String> str, TaskID<Integer> picsPerPage, int pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[139]####
        taskinfo.addDependsOn(str);//####[139]####
        taskinfo.addDependsOn(picsPerPage);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(BlockingQueue<String> str, TaskID<Integer> picsPerPage, int pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(BlockingQueue<String> str, TaskID<Integer> picsPerPage, int pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setQueueArgIndexes(0);//####[139]####
        taskinfo.setIsPipeline(true);//####[139]####
        taskinfo.setTaskIdArgIndexes(1);//####[139]####
        taskinfo.addDependsOn(picsPerPage);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(String str, BlockingQueue<Integer> picsPerPage, int pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(String str, BlockingQueue<Integer> picsPerPage, int pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setQueueArgIndexes(1);//####[139]####
        taskinfo.setIsPipeline(true);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(TaskID<String> str, BlockingQueue<Integer> picsPerPage, int pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(TaskID<String> str, BlockingQueue<Integer> picsPerPage, int pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setQueueArgIndexes(1);//####[139]####
        taskinfo.setIsPipeline(true);//####[139]####
        taskinfo.setTaskIdArgIndexes(0);//####[139]####
        taskinfo.addDependsOn(str);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(BlockingQueue<String> str, BlockingQueue<Integer> picsPerPage, int pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(BlockingQueue<String> str, BlockingQueue<Integer> picsPerPage, int pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setQueueArgIndexes(0, 1);//####[139]####
        taskinfo.setIsPipeline(true);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(String str, int picsPerPage, TaskID<Integer> pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(String str, int picsPerPage, TaskID<Integer> pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setTaskIdArgIndexes(2);//####[139]####
        taskinfo.addDependsOn(pageOffset);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(TaskID<String> str, int picsPerPage, TaskID<Integer> pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(TaskID<String> str, int picsPerPage, TaskID<Integer> pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setTaskIdArgIndexes(0, 2);//####[139]####
        taskinfo.addDependsOn(str);//####[139]####
        taskinfo.addDependsOn(pageOffset);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(BlockingQueue<String> str, int picsPerPage, TaskID<Integer> pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(BlockingQueue<String> str, int picsPerPage, TaskID<Integer> pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setQueueArgIndexes(0);//####[139]####
        taskinfo.setIsPipeline(true);//####[139]####
        taskinfo.setTaskIdArgIndexes(2);//####[139]####
        taskinfo.addDependsOn(pageOffset);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(String str, TaskID<Integer> picsPerPage, TaskID<Integer> pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(String str, TaskID<Integer> picsPerPage, TaskID<Integer> pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setTaskIdArgIndexes(1, 2);//####[139]####
        taskinfo.addDependsOn(picsPerPage);//####[139]####
        taskinfo.addDependsOn(pageOffset);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(TaskID<String> str, TaskID<Integer> picsPerPage, TaskID<Integer> pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(TaskID<String> str, TaskID<Integer> picsPerPage, TaskID<Integer> pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setTaskIdArgIndexes(0, 1, 2);//####[139]####
        taskinfo.addDependsOn(str);//####[139]####
        taskinfo.addDependsOn(picsPerPage);//####[139]####
        taskinfo.addDependsOn(pageOffset);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(BlockingQueue<String> str, TaskID<Integer> picsPerPage, TaskID<Integer> pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(BlockingQueue<String> str, TaskID<Integer> picsPerPage, TaskID<Integer> pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setQueueArgIndexes(0);//####[139]####
        taskinfo.setIsPipeline(true);//####[139]####
        taskinfo.setTaskIdArgIndexes(1, 2);//####[139]####
        taskinfo.addDependsOn(picsPerPage);//####[139]####
        taskinfo.addDependsOn(pageOffset);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(String str, BlockingQueue<Integer> picsPerPage, TaskID<Integer> pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(String str, BlockingQueue<Integer> picsPerPage, TaskID<Integer> pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setQueueArgIndexes(1);//####[139]####
        taskinfo.setIsPipeline(true);//####[139]####
        taskinfo.setTaskIdArgIndexes(2);//####[139]####
        taskinfo.addDependsOn(pageOffset);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(TaskID<String> str, BlockingQueue<Integer> picsPerPage, TaskID<Integer> pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(TaskID<String> str, BlockingQueue<Integer> picsPerPage, TaskID<Integer> pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setQueueArgIndexes(1);//####[139]####
        taskinfo.setIsPipeline(true);//####[139]####
        taskinfo.setTaskIdArgIndexes(0, 2);//####[139]####
        taskinfo.addDependsOn(str);//####[139]####
        taskinfo.addDependsOn(pageOffset);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(BlockingQueue<String> str, BlockingQueue<Integer> picsPerPage, TaskID<Integer> pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(BlockingQueue<String> str, BlockingQueue<Integer> picsPerPage, TaskID<Integer> pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setQueueArgIndexes(0, 1);//####[139]####
        taskinfo.setIsPipeline(true);//####[139]####
        taskinfo.setTaskIdArgIndexes(2);//####[139]####
        taskinfo.addDependsOn(pageOffset);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(String str, int picsPerPage, BlockingQueue<Integer> pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(String str, int picsPerPage, BlockingQueue<Integer> pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setQueueArgIndexes(2);//####[139]####
        taskinfo.setIsPipeline(true);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(TaskID<String> str, int picsPerPage, BlockingQueue<Integer> pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(TaskID<String> str, int picsPerPage, BlockingQueue<Integer> pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setQueueArgIndexes(2);//####[139]####
        taskinfo.setIsPipeline(true);//####[139]####
        taskinfo.setTaskIdArgIndexes(0);//####[139]####
        taskinfo.addDependsOn(str);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(BlockingQueue<String> str, int picsPerPage, BlockingQueue<Integer> pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(BlockingQueue<String> str, int picsPerPage, BlockingQueue<Integer> pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setQueueArgIndexes(0, 2);//####[139]####
        taskinfo.setIsPipeline(true);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(String str, TaskID<Integer> picsPerPage, BlockingQueue<Integer> pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(String str, TaskID<Integer> picsPerPage, BlockingQueue<Integer> pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setQueueArgIndexes(2);//####[139]####
        taskinfo.setIsPipeline(true);//####[139]####
        taskinfo.setTaskIdArgIndexes(1);//####[139]####
        taskinfo.addDependsOn(picsPerPage);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(TaskID<String> str, TaskID<Integer> picsPerPage, BlockingQueue<Integer> pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(TaskID<String> str, TaskID<Integer> picsPerPage, BlockingQueue<Integer> pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setQueueArgIndexes(2);//####[139]####
        taskinfo.setIsPipeline(true);//####[139]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[139]####
        taskinfo.addDependsOn(str);//####[139]####
        taskinfo.addDependsOn(picsPerPage);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(BlockingQueue<String> str, TaskID<Integer> picsPerPage, BlockingQueue<Integer> pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(BlockingQueue<String> str, TaskID<Integer> picsPerPage, BlockingQueue<Integer> pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setQueueArgIndexes(0, 2);//####[139]####
        taskinfo.setIsPipeline(true);//####[139]####
        taskinfo.setTaskIdArgIndexes(1);//####[139]####
        taskinfo.addDependsOn(picsPerPage);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(String str, BlockingQueue<Integer> picsPerPage, BlockingQueue<Integer> pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(String str, BlockingQueue<Integer> picsPerPage, BlockingQueue<Integer> pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setQueueArgIndexes(1, 2);//####[139]####
        taskinfo.setIsPipeline(true);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(TaskID<String> str, BlockingQueue<Integer> picsPerPage, BlockingQueue<Integer> pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(TaskID<String> str, BlockingQueue<Integer> picsPerPage, BlockingQueue<Integer> pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setQueueArgIndexes(1, 2);//####[139]####
        taskinfo.setIsPipeline(true);//####[139]####
        taskinfo.setTaskIdArgIndexes(0);//####[139]####
        taskinfo.addDependsOn(str);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(BlockingQueue<String> str, BlockingQueue<Integer> picsPerPage, BlockingQueue<Integer> pageOffset) {//####[139]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[139]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[139]####
    }//####[139]####
    public static TaskID<List<PhotoWithImage>> searchTask(BlockingQueue<String> str, BlockingQueue<Integer> picsPerPage, BlockingQueue<Integer> pageOffset, TaskInfo taskinfo) {//####[139]####
        // ensure Method variable is set//####[139]####
        if (__pt__searchTask_String_int_int_method == null) {//####[139]####
            __pt__searchTask_String_int_int_ensureMethodVarSet();//####[139]####
        }//####[139]####
        taskinfo.setQueueArgIndexes(0, 1, 2);//####[139]####
        taskinfo.setIsPipeline(true);//####[139]####
        taskinfo.setParameters(str, picsPerPage, pageOffset);//####[139]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[139]####
        taskinfo.setInteractive(true);//####[139]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[139]####
    }//####[139]####
    public static List<PhotoWithImage> __pt__searchTask(String str, int picsPerPage, int pageOffset) {//####[139]####
        return search(str, picsPerPage, pageOffset);//####[140]####
    }//####[141]####
//####[141]####
//####[143]####
    public static List<PhotoWithImage> search(String str, int picsPerPage, int pageOffset) {//####[143]####
        try {//####[144]####
            SearchParameters sp = new SearchParameters();//####[145]####
            sp.setText(str);//####[146]####
            PhotoList pl = photoInterface.search(sp, picsPerPage, pageOffset);//####[147]####
            List<PhotoWithImage> list = new ArrayList<PhotoWithImage>();//####[148]####
            for (int i = 0; i < pl.size(); i++) //####[149]####
            {//####[149]####
                Photo p = (Photo) pl.get(i);//####[150]####
                Image image = Search.getSquareImage(p);//####[151]####
                PhotoWithImage pi = new PhotoWithImage(p, image);//####[152]####
                if (CurrentTask.insideTask()) //####[154]####
                {//####[154]####
                    if (CurrentTask.cancelRequested()) //####[155]####
                    {//####[155]####
                        CurrentTask.setProgress(100);//####[156]####
                        CurrentTask.publishInterim(pi);//####[157]####
                        return list;//####[158]####
                    } else {//####[159]####
                        CurrentTask.setProgress((int) ((i + 1.0) / pl.size() * 100));//####[160]####
                        CurrentTask.publishInterim(pi);//####[161]####
                    }//####[162]####
                }//####[163]####
                list.add(pi);//####[164]####
            }//####[165]####
            return list;//####[166]####
        } catch (IOException e) {//####[167]####
            e.printStackTrace();//####[168]####
        } catch (SAXException e) {//####[169]####
            e.printStackTrace();//####[170]####
        } catch (FlickrException e) {//####[171]####
            e.printStackTrace();//####[172]####
        }//####[173]####
        return null;//####[174]####
    }//####[175]####
}//####[175]####
