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
import com.aetrion.flickr.*;//####[16]####
import com.aetrion.flickr.photos.*;//####[17]####
//####[17]####
//-- ParaTask related imports//####[17]####
import paratask.runtime.*;//####[17]####
import java.util.concurrent.ExecutionException;//####[17]####
import java.util.concurrent.locks.*;//####[17]####
import java.lang.reflect.*;//####[17]####
import javax.swing.SwingUtilities;//####[17]####
//####[17]####
public class Search {//####[19]####
//####[19]####
    /*  ParaTask helper method to access private/protected slots *///####[19]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[19]####
        if (m.getParameterTypes().length == 0)//####[19]####
            m.invoke(instance);//####[19]####
        else if ((m.getParameterTypes().length == 1))//####[19]####
            m.invoke(instance, arg);//####[19]####
        else //####[19]####
            m.invoke(instance, arg, interResult);//####[19]####
    }//####[19]####
//####[21]####
    public static final String API_KEY = "98cac2ba2664223a4cf1bb5b97d39fab";//####[21]####
//####[23]####
    private static Flickr flickr = new Flickr(API_KEY);//####[23]####
//####[24]####
    private static PhotosInterface photoInterface = flickr.getPhotosInterface();//####[24]####
//####[41]####
    private static Method __pt__getSquareImageTask_Photo_method = null;//####[41]####
    private static Lock __pt__getSquareImageTask_Photo_lock = new ReentrantLock();//####[41]####
    public static TaskID<Image> getSquareImageTask(Photo p)  {//####[41]####
//####[41]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[41]####
        return getSquareImageTask(p, new TaskInfo());//####[41]####
    }//####[41]####
    public static TaskID<Image> getSquareImageTask(Photo p, TaskInfo taskinfo)  {//####[41]####
        if (__pt__getSquareImageTask_Photo_method == null) {//####[41]####
            try {//####[41]####
                __pt__getSquareImageTask_Photo_lock.lock();//####[41]####
                if (__pt__getSquareImageTask_Photo_method == null) //####[41]####
                    __pt__getSquareImageTask_Photo_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__getSquareImageTask", new Class[] {Photo.class});//####[41]####
            } catch (Exception e) {//####[41]####
                e.printStackTrace();//####[41]####
            } finally {//####[41]####
                __pt__getSquareImageTask_Photo_lock.unlock();//####[41]####
            }//####[41]####
        }//####[41]####
//####[41]####
        Object[] args = new Object[] {p};//####[41]####
        taskinfo.setTaskArguments(args);//####[41]####
        taskinfo.setMethod(__pt__getSquareImageTask_Photo_method);//####[41]####
        taskinfo.setInteractive(true);//####[41]####
//####[41]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[41]####
    }//####[41]####
    public static Image __pt__getSquareImageTask(Photo p) {//####[41]####
        return getSquareImage(p);//####[42]####
    }//####[43]####
//####[43]####
//####[45]####
    public static Image getSquareImage(Photo p) {//####[45]####
        Image image = null;//####[46]####
        try {//####[47]####
            image = photoInterface.getImage(p, Size.SQUARE);//####[48]####
        } catch (IOException e) {//####[49]####
            e.printStackTrace();//####[50]####
        } catch (FlickrException e) {//####[51]####
            e.printStackTrace();//####[52]####
        }//####[53]####
        return image;//####[54]####
    }//####[55]####
//####[57]####
    public static Image getThumbnailImage(Photo p) {//####[57]####
        Image image = null;//####[58]####
        try {//####[59]####
            image = photoInterface.getImage(p, Size.THUMB);//####[60]####
        } catch (IOException e) {//####[61]####
            e.printStackTrace();//####[62]####
        } catch (FlickrException e) {//####[63]####
            e.printStackTrace();//####[64]####
        }//####[65]####
        return image;//####[66]####
    }//####[67]####
//####[69]####
    private static Method __pt__getMediumImageTask_Photo_method = null;//####[69]####
    private static Lock __pt__getMediumImageTask_Photo_lock = new ReentrantLock();//####[69]####
    public static TaskID<Image> getMediumImageTask(Photo p)  {//####[69]####
//####[69]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[69]####
        return getMediumImageTask(p, new TaskInfo());//####[69]####
    }//####[69]####
    public static TaskID<Image> getMediumImageTask(Photo p, TaskInfo taskinfo)  {//####[69]####
        if (__pt__getMediumImageTask_Photo_method == null) {//####[69]####
            try {//####[69]####
                __pt__getMediumImageTask_Photo_lock.lock();//####[69]####
                if (__pt__getMediumImageTask_Photo_method == null) //####[69]####
                    __pt__getMediumImageTask_Photo_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__getMediumImageTask", new Class[] {Photo.class});//####[69]####
            } catch (Exception e) {//####[69]####
                e.printStackTrace();//####[69]####
            } finally {//####[69]####
                __pt__getMediumImageTask_Photo_lock.unlock();//####[69]####
            }//####[69]####
        }//####[69]####
//####[69]####
        Object[] args = new Object[] {p};//####[69]####
        taskinfo.setTaskArguments(args);//####[69]####
        taskinfo.setMethod(__pt__getMediumImageTask_Photo_method);//####[69]####
        taskinfo.setInteractive(true);//####[69]####
//####[69]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[69]####
    }//####[69]####
    public static Image __pt__getMediumImageTask(Photo p) {//####[69]####
        return getMediumImage(p);//####[70]####
    }//####[71]####
//####[71]####
//####[74]####
    public static Image getMediumImage(Photo p) {//####[74]####
        Image image = null;//####[75]####
        try {//####[76]####
            image = photoInterface.getImage(p, Size.MEDIUM);//####[77]####
        } catch (IOException e) {//####[78]####
            e.printStackTrace();//####[79]####
        } catch (FlickrException e) {//####[80]####
            e.printStackTrace();//####[81]####
        }//####[82]####
        return image;//####[83]####
    }//####[84]####
//####[86]####
    public static Image getSmallImage(Photo p) {//####[86]####
        Image image = null;//####[87]####
        try {//####[88]####
            image = photoInterface.getImage(p, Size.SMALL);//####[89]####
        } catch (IOException e) {//####[90]####
            e.printStackTrace();//####[91]####
        } catch (FlickrException e) {//####[92]####
            e.printStackTrace();//####[93]####
        }//####[94]####
        return image;//####[95]####
    }//####[96]####
//####[97]####
    public static Image getLargeImage(Photo p) {//####[97]####
        Image image = null;//####[98]####
        try {//####[99]####
            image = photoInterface.getImage(p, Size.LARGE);//####[100]####
        } catch (IOException e) {//####[101]####
            e.printStackTrace();//####[102]####
        } catch (FlickrException e) {//####[103]####
            e.printStackTrace();//####[104]####
        }//####[105]####
        return image;//####[106]####
    }//####[107]####
//####[134]####
    private static Method __pt__searchTask_String_int_int_method = null;//####[134]####
    private static Lock __pt__searchTask_String_int_int_lock = new ReentrantLock();//####[134]####
    public static TaskID<List<PhotoWithImage>> searchTask(String str, int picsPerPage, int pageOffset)  {//####[134]####
//####[134]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[134]####
        return searchTask(str, picsPerPage, pageOffset, new TaskInfo());//####[134]####
    }//####[134]####
    public static TaskID<List<PhotoWithImage>> searchTask(String str, int picsPerPage, int pageOffset, TaskInfo taskinfo)  {//####[134]####
        if (__pt__searchTask_String_int_int_method == null) {//####[134]####
            try {//####[134]####
                __pt__searchTask_String_int_int_lock.lock();//####[134]####
                if (__pt__searchTask_String_int_int_method == null) //####[134]####
                    __pt__searchTask_String_int_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__searchTask", new Class[] {String.class, int.class, int.class});//####[134]####
            } catch (Exception e) {//####[134]####
                e.printStackTrace();//####[134]####
            } finally {//####[134]####
                __pt__searchTask_String_int_int_lock.unlock();//####[134]####
            }//####[134]####
        }//####[134]####
//####[134]####
        Object[] args = new Object[] {str, picsPerPage, pageOffset};//####[134]####
        taskinfo.setTaskArguments(args);//####[134]####
        taskinfo.setMethod(__pt__searchTask_String_int_int_method);//####[134]####
        taskinfo.setInteractive(true);//####[134]####
//####[134]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[134]####
    }//####[134]####
    public static List<PhotoWithImage> __pt__searchTask(String str, int picsPerPage, int pageOffset) {//####[134]####
        return search(str, picsPerPage, pageOffset);//####[135]####
    }//####[136]####
//####[136]####
//####[138]####
    public static List<PhotoWithImage> search(String str, int picsPerPage, int pageOffset) {//####[138]####
        try {//####[139]####
            SearchParameters sp = new SearchParameters();//####[140]####
            sp.setText(str);//####[141]####
            PhotoList pl = photoInterface.search(sp, picsPerPage, pageOffset);//####[142]####
            List<PhotoWithImage> list = new ArrayList<PhotoWithImage>();//####[143]####
            for (int i = 0; i < pl.size(); i++) //####[144]####
            {//####[144]####
                Photo p = (Photo) pl.get(i);//####[145]####
                Image image = Search.getSquareImage(p);//####[146]####
                PhotoWithImage pi = new PhotoWithImage(p, image);//####[147]####
                if (CurrentTask.insideTask()) //####[149]####
                {//####[149]####
                    if (CurrentTask.cancelRequested()) //####[150]####
                    {//####[150]####
                        CurrentTask.setProgress(100);//####[151]####
                        CurrentTask.publishInterim(pi);//####[152]####
                        return list;//####[153]####
                    } else {//####[154]####
                        CurrentTask.setProgress((int) ((i + 1.0) / pl.size() * 100));//####[155]####
                        CurrentTask.publishInterim(pi);//####[156]####
                    }//####[157]####
                }//####[158]####
                list.add(pi);//####[159]####
            }//####[160]####
            return list;//####[161]####
        } catch (IOException e) {//####[162]####
            e.printStackTrace();//####[163]####
        } catch (SAXException e) {//####[164]####
            e.printStackTrace();//####[165]####
        } catch (FlickrException e) {//####[166]####
            e.printStackTrace();//####[167]####
        }//####[168]####
        return null;//####[169]####
    }//####[170]####
}//####[170]####
