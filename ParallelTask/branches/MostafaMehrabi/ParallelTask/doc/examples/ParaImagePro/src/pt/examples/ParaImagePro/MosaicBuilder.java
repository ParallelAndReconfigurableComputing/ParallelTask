package pt.examples.ParaImagePro;//####[1]####
//####[1]####
import java.awt.Color;//####[3]####
import java.awt.Graphics2D;//####[4]####
import java.awt.Image;//####[5]####
import java.awt.image.BufferedImage;//####[6]####
import java.lang.reflect.Method;//####[7]####
import java.util.List;//####[8]####
import java.util.concurrent.locks.Lock;//####[9]####
import java.util.concurrent.locks.ReentrantLock;//####[10]####
//####[10]####
//-- ParaTask related imports//####[10]####
import pt.runtime.*;//####[10]####
import java.util.concurrent.ExecutionException;//####[10]####
import java.util.concurrent.locks.*;//####[10]####
import java.lang.reflect.*;//####[10]####
import pt.runtime.GuiThread;//####[10]####
import java.util.concurrent.BlockingQueue;//####[10]####
import java.util.ArrayList;//####[10]####
import java.util.List;//####[10]####
//####[10]####
public class MosaicBuilder {//####[12]####
    static{ParaTask.init();}//####[12]####
    /*  ParaTask helper method to access private/protected slots *///####[12]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[12]####
        if (m.getParameterTypes().length == 0)//####[12]####
            m.invoke(instance);//####[12]####
        else if ((m.getParameterTypes().length == 1))//####[12]####
            m.invoke(instance, arg);//####[12]####
        else //####[12]####
            m.invoke(instance, arg, interResult);//####[12]####
    }//####[12]####
//####[14]####
    private static volatile Method __pt__buildMosaicTask_ImagePanelItem_int_int_method = null;//####[14]####
    private synchronized static void __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet() {//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            try {//####[14]####
                __pt__buildMosaicTask_ImagePanelItem_int_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__buildMosaicTask", new Class[] {//####[14]####
                    ImagePanelItem.class, int.class, int.class//####[14]####
                });//####[14]####
            } catch (Exception e) {//####[14]####
                e.printStackTrace();//####[14]####
            }//####[14]####
        }//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(ImagePanelItem panel, int density, int size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(ImagePanelItem panel, int density, int size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(TaskID<ImagePanelItem> panel, int density, int size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(TaskID<ImagePanelItem> panel, int density, int size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setTaskIdArgIndexes(0);//####[14]####
        taskinfo.addDependsOn(panel);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(BlockingQueue<ImagePanelItem> panel, int density, int size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(BlockingQueue<ImagePanelItem> panel, int density, int size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setQueueArgIndexes(0);//####[14]####
        taskinfo.setIsPipeline(true);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(ImagePanelItem panel, TaskID<Integer> density, int size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(ImagePanelItem panel, TaskID<Integer> density, int size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setTaskIdArgIndexes(1);//####[14]####
        taskinfo.addDependsOn(density);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(TaskID<ImagePanelItem> panel, TaskID<Integer> density, int size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(TaskID<ImagePanelItem> panel, TaskID<Integer> density, int size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[14]####
        taskinfo.addDependsOn(panel);//####[14]####
        taskinfo.addDependsOn(density);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(BlockingQueue<ImagePanelItem> panel, TaskID<Integer> density, int size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(BlockingQueue<ImagePanelItem> panel, TaskID<Integer> density, int size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setQueueArgIndexes(0);//####[14]####
        taskinfo.setIsPipeline(true);//####[14]####
        taskinfo.setTaskIdArgIndexes(1);//####[14]####
        taskinfo.addDependsOn(density);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(ImagePanelItem panel, BlockingQueue<Integer> density, int size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(ImagePanelItem panel, BlockingQueue<Integer> density, int size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setQueueArgIndexes(1);//####[14]####
        taskinfo.setIsPipeline(true);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(TaskID<ImagePanelItem> panel, BlockingQueue<Integer> density, int size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(TaskID<ImagePanelItem> panel, BlockingQueue<Integer> density, int size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setQueueArgIndexes(1);//####[14]####
        taskinfo.setIsPipeline(true);//####[14]####
        taskinfo.setTaskIdArgIndexes(0);//####[14]####
        taskinfo.addDependsOn(panel);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(BlockingQueue<ImagePanelItem> panel, BlockingQueue<Integer> density, int size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(BlockingQueue<ImagePanelItem> panel, BlockingQueue<Integer> density, int size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setQueueArgIndexes(0, 1);//####[14]####
        taskinfo.setIsPipeline(true);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(ImagePanelItem panel, int density, TaskID<Integer> size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(ImagePanelItem panel, int density, TaskID<Integer> size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setTaskIdArgIndexes(2);//####[14]####
        taskinfo.addDependsOn(size);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(TaskID<ImagePanelItem> panel, int density, TaskID<Integer> size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(TaskID<ImagePanelItem> panel, int density, TaskID<Integer> size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setTaskIdArgIndexes(0, 2);//####[14]####
        taskinfo.addDependsOn(panel);//####[14]####
        taskinfo.addDependsOn(size);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(BlockingQueue<ImagePanelItem> panel, int density, TaskID<Integer> size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(BlockingQueue<ImagePanelItem> panel, int density, TaskID<Integer> size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setQueueArgIndexes(0);//####[14]####
        taskinfo.setIsPipeline(true);//####[14]####
        taskinfo.setTaskIdArgIndexes(2);//####[14]####
        taskinfo.addDependsOn(size);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(ImagePanelItem panel, TaskID<Integer> density, TaskID<Integer> size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(ImagePanelItem panel, TaskID<Integer> density, TaskID<Integer> size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setTaskIdArgIndexes(1, 2);//####[14]####
        taskinfo.addDependsOn(density);//####[14]####
        taskinfo.addDependsOn(size);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(TaskID<ImagePanelItem> panel, TaskID<Integer> density, TaskID<Integer> size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(TaskID<ImagePanelItem> panel, TaskID<Integer> density, TaskID<Integer> size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setTaskIdArgIndexes(0, 1, 2);//####[14]####
        taskinfo.addDependsOn(panel);//####[14]####
        taskinfo.addDependsOn(density);//####[14]####
        taskinfo.addDependsOn(size);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(BlockingQueue<ImagePanelItem> panel, TaskID<Integer> density, TaskID<Integer> size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(BlockingQueue<ImagePanelItem> panel, TaskID<Integer> density, TaskID<Integer> size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setQueueArgIndexes(0);//####[14]####
        taskinfo.setIsPipeline(true);//####[14]####
        taskinfo.setTaskIdArgIndexes(1, 2);//####[14]####
        taskinfo.addDependsOn(density);//####[14]####
        taskinfo.addDependsOn(size);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(ImagePanelItem panel, BlockingQueue<Integer> density, TaskID<Integer> size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(ImagePanelItem panel, BlockingQueue<Integer> density, TaskID<Integer> size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setQueueArgIndexes(1);//####[14]####
        taskinfo.setIsPipeline(true);//####[14]####
        taskinfo.setTaskIdArgIndexes(2);//####[14]####
        taskinfo.addDependsOn(size);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(TaskID<ImagePanelItem> panel, BlockingQueue<Integer> density, TaskID<Integer> size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(TaskID<ImagePanelItem> panel, BlockingQueue<Integer> density, TaskID<Integer> size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setQueueArgIndexes(1);//####[14]####
        taskinfo.setIsPipeline(true);//####[14]####
        taskinfo.setTaskIdArgIndexes(0, 2);//####[14]####
        taskinfo.addDependsOn(panel);//####[14]####
        taskinfo.addDependsOn(size);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(BlockingQueue<ImagePanelItem> panel, BlockingQueue<Integer> density, TaskID<Integer> size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(BlockingQueue<ImagePanelItem> panel, BlockingQueue<Integer> density, TaskID<Integer> size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setQueueArgIndexes(0, 1);//####[14]####
        taskinfo.setIsPipeline(true);//####[14]####
        taskinfo.setTaskIdArgIndexes(2);//####[14]####
        taskinfo.addDependsOn(size);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(ImagePanelItem panel, int density, BlockingQueue<Integer> size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(ImagePanelItem panel, int density, BlockingQueue<Integer> size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setQueueArgIndexes(2);//####[14]####
        taskinfo.setIsPipeline(true);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(TaskID<ImagePanelItem> panel, int density, BlockingQueue<Integer> size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(TaskID<ImagePanelItem> panel, int density, BlockingQueue<Integer> size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setQueueArgIndexes(2);//####[14]####
        taskinfo.setIsPipeline(true);//####[14]####
        taskinfo.setTaskIdArgIndexes(0);//####[14]####
        taskinfo.addDependsOn(panel);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(BlockingQueue<ImagePanelItem> panel, int density, BlockingQueue<Integer> size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(BlockingQueue<ImagePanelItem> panel, int density, BlockingQueue<Integer> size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setQueueArgIndexes(0, 2);//####[14]####
        taskinfo.setIsPipeline(true);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(ImagePanelItem panel, TaskID<Integer> density, BlockingQueue<Integer> size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(ImagePanelItem panel, TaskID<Integer> density, BlockingQueue<Integer> size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setQueueArgIndexes(2);//####[14]####
        taskinfo.setIsPipeline(true);//####[14]####
        taskinfo.setTaskIdArgIndexes(1);//####[14]####
        taskinfo.addDependsOn(density);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(TaskID<ImagePanelItem> panel, TaskID<Integer> density, BlockingQueue<Integer> size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(TaskID<ImagePanelItem> panel, TaskID<Integer> density, BlockingQueue<Integer> size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setQueueArgIndexes(2);//####[14]####
        taskinfo.setIsPipeline(true);//####[14]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[14]####
        taskinfo.addDependsOn(panel);//####[14]####
        taskinfo.addDependsOn(density);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(BlockingQueue<ImagePanelItem> panel, TaskID<Integer> density, BlockingQueue<Integer> size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(BlockingQueue<ImagePanelItem> panel, TaskID<Integer> density, BlockingQueue<Integer> size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setQueueArgIndexes(0, 2);//####[14]####
        taskinfo.setIsPipeline(true);//####[14]####
        taskinfo.setTaskIdArgIndexes(1);//####[14]####
        taskinfo.addDependsOn(density);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(ImagePanelItem panel, BlockingQueue<Integer> density, BlockingQueue<Integer> size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(ImagePanelItem panel, BlockingQueue<Integer> density, BlockingQueue<Integer> size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setQueueArgIndexes(1, 2);//####[14]####
        taskinfo.setIsPipeline(true);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(TaskID<ImagePanelItem> panel, BlockingQueue<Integer> density, BlockingQueue<Integer> size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(TaskID<ImagePanelItem> panel, BlockingQueue<Integer> density, BlockingQueue<Integer> size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setQueueArgIndexes(1, 2);//####[14]####
        taskinfo.setIsPipeline(true);//####[14]####
        taskinfo.setTaskIdArgIndexes(0);//####[14]####
        taskinfo.addDependsOn(panel);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(BlockingQueue<ImagePanelItem> panel, BlockingQueue<Integer> density, BlockingQueue<Integer> size) {//####[14]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[14]####
        return buildMosaicTask(panel, density, size, new TaskInfo());//####[14]####
    }//####[14]####
    public static TaskID<ImageCombo> buildMosaicTask(BlockingQueue<ImagePanelItem> panel, BlockingQueue<Integer> density, BlockingQueue<Integer> size, TaskInfo taskinfo) {//####[14]####
        // ensure Method variable is set//####[14]####
        if (__pt__buildMosaicTask_ImagePanelItem_int_int_method == null) {//####[14]####
            __pt__buildMosaicTask_ImagePanelItem_int_int_ensureMethodVarSet();//####[14]####
        }//####[14]####
        taskinfo.setQueueArgIndexes(0, 1, 2);//####[14]####
        taskinfo.setIsPipeline(true);//####[14]####
        taskinfo.setParameters(panel, density, size);//####[14]####
        taskinfo.setMethod(__pt__buildMosaicTask_ImagePanelItem_int_int_method);//####[14]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[14]####
    }//####[14]####
    public static ImageCombo __pt__buildMosaicTask(ImagePanelItem panel, int density, int size) {//####[14]####
        return buildMosaic(panel, density, size);//####[15]####
    }//####[16]####
//####[16]####
//####[18]####
    public static ImageCombo buildMosaic(ImagePanelItem panel, int density, int size) {//####[18]####
        Image imageLarge = panel.getImageLarge();//####[19]####
        int width = imageLarge.getWidth(null) / density;//####[20]####
        if (!(width > 0)) //####[21]####
        {//####[21]####
            width = 1;//####[22]####
        }//####[23]####
        int height = imageLarge.getHeight(null) / density;//####[24]####
        if (!(height > 0)) //####[25]####
        {//####[25]####
            height = 1;//####[26]####
        }//####[27]####
        BufferedImage template = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);//####[28]####
        Graphics2D g2 = template.createGraphics();//####[29]####
        g2.drawImage(imageLarge.getScaledInstance(width, height, Image.SCALE_DEFAULT), null, null);//####[30]####
        BufferedImage mosaicLarge = new BufferedImage(width * size, height * size, BufferedImage.TYPE_INT_RGB);//####[31]####
        for (int y = 0; y < height * size; y++) //####[33]####
        {//####[33]####
            for (int x = 0; x < width * size; x++) //####[34]####
            {//####[34]####
                mosaicLarge.setRGB(x, y, template.getRGB(x / size, y / size));//####[35]####
            }//####[36]####
        }//####[37]####
        Image imageSquare = panel.getImageSquare();//####[39]####
        BufferedImage mosaicSquare = new BufferedImage(imageSquare.getWidth(null), imageSquare.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[40]####
        Graphics2D g3 = mosaicSquare.createGraphics();//####[41]####
        g3.drawImage(mosaicLarge.getScaledInstance(imageSquare.getWidth(null), imageSquare.getHeight(null), Image.SCALE_DEFAULT), null, null);//####[42]####
        Image imageMedium = panel.getImageMedium();//####[44]####
        BufferedImage mosaicMedium = new BufferedImage(imageMedium.getWidth(null), imageMedium.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[45]####
        Graphics2D g4 = mosaicMedium.createGraphics();//####[46]####
        g4.drawImage(mosaicLarge.getScaledInstance(imageMedium.getWidth(null), imageMedium.getHeight(null), Image.SCALE_DEFAULT), null, null);//####[47]####
        return new ImageCombo(mosaicLarge, mosaicSquare, mosaicMedium);//####[49]####
    }//####[50]####
//####[53]####
    public static ImageCombo buildImageMosaic(ImagePanelItem panel, List<PaletteItem> palette, int density) {//####[53]####
        if (!palette.isEmpty()) //####[54]####
        {//####[54]####
            Image imageLarge = panel.getImageLarge();//####[55]####
            int width = imageLarge.getWidth(null) / density;//####[56]####
            if (!(width > 0)) //####[57]####
            {//####[57]####
                width = 1;//####[58]####
            }//####[59]####
            int height = imageLarge.getHeight(null) / density;//####[60]####
            if (!(height > 0)) //####[61]####
            {//####[61]####
                height = 1;//####[62]####
            }//####[63]####
            int size = palette.get(0).getImage().getWidth(null);//####[64]####
            BufferedImage template = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);//####[66]####
            Graphics2D g2 = template.createGraphics();//####[67]####
            g2.drawImage(imageLarge.getScaledInstance(width, height, Image.SCALE_DEFAULT), null, null);//####[68]####
            BufferedImage mosaicLarge = new BufferedImage(width * size, height * size, BufferedImage.TYPE_INT_RGB);//####[69]####
            Graphics2D g3 = mosaicLarge.createGraphics();//####[70]####
            for (int y = 0; y < height; y++) //####[72]####
            {//####[72]####
                for (int x = 0; x < width; x++) //####[73]####
                {//####[73]####
                    Color tc = new Color(template.getRGB(x, y));//####[74]####
                    int tr = tc.getRed();//####[75]####
                    int tg = tc.getGreen();//####[76]####
                    int tb = tc.getBlue();//####[77]####
                    int closest = 0;//####[78]####
                    int distance = 512;//####[79]####
                    for (int i = 0; i < palette.size(); i++) //####[80]####
                    {//####[80]####
                        Color pc = new Color(palette.get(i).getColor());//####[81]####
                        int pr = pc.getRed();//####[82]####
                        int pg = pc.getGreen();//####[83]####
                        int pb = pc.getBlue();//####[84]####
                        int temp = (int) Math.sqrt(0.3 * (tr - pr) * (tr - pr) + 0.59 * (tg - pg) * (tg - pg) + 0.11 * (tb - pb) * (tb - pb));//####[85]####
                        if (temp == 0) //####[86]####
                        {//####[86]####
                            closest = i;//####[87]####
                            break;//####[88]####
                        } else if (temp < distance) //####[89]####
                        {//####[89]####
                            closest = i;//####[90]####
                            distance = temp;//####[91]####
                        }//####[92]####
                    }//####[93]####
                    g3.drawImage(palette.get(closest).getImage(), x * size, y * size, null);//####[94]####
                }//####[95]####
            }//####[96]####
            Image imageSquare = panel.getImageSquare();//####[98]####
            BufferedImage mosaicSquare = new BufferedImage(imageSquare.getWidth(null), imageSquare.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[99]####
            Graphics2D g4 = mosaicSquare.createGraphics();//####[100]####
            g4.drawImage(mosaicLarge.getScaledInstance(imageSquare.getWidth(null), imageSquare.getHeight(null), Image.SCALE_DEFAULT), null, null);//####[101]####
            Image imageMedium = panel.getImageMedium();//####[103]####
            BufferedImage mosaicMedium = new BufferedImage(imageMedium.getWidth(null), imageMedium.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[104]####
            Graphics2D g5 = mosaicMedium.createGraphics();//####[105]####
            g5.drawImage(mosaicLarge.getScaledInstance(imageMedium.getWidth(null), imageMedium.getHeight(null), Image.SCALE_DEFAULT), null, null);//####[106]####
            return new ImageCombo(mosaicLarge, mosaicSquare, mosaicMedium);//####[108]####
        } else {//####[109]####
            return new ImageCombo(panel.getImageLarge(), panel.getImageSquare(), panel.getImageMedium());//####[110]####
        }//####[111]####
    }//####[112]####
//####[115]####
    private static volatile Method __pt__buildImageMosaicTask_ImagePanelItem_ListPaletteItem_int_int_method = null;//####[115]####
    private synchronized static void __pt__buildImageMosaicTask_ImagePanelItem_ListPaletteItem_int_int_ensureMethodVarSet() {//####[115]####
        if (__pt__buildImageMosaicTask_ImagePanelItem_ListPaletteItem_int_int_method == null) {//####[115]####
            try {//####[115]####
                __pt__buildImageMosaicTask_ImagePanelItem_ListPaletteItem_int_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__buildImageMosaicTask", new Class[] {//####[115]####
                    ImagePanelItem.class, List.class, int.class, int.class//####[115]####
                });//####[115]####
            } catch (Exception e) {//####[115]####
                e.printStackTrace();//####[115]####
            }//####[115]####
        }//####[115]####
    }//####[115]####
    public static TaskID<ImageCombo> buildImageMosaicTask(Object panel, Object palette, Object density, Object parallelism) {//####[115]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[115]####
        return buildImageMosaicTask(panel, palette, density, parallelism, new TaskInfo());//####[115]####
    }//####[115]####
    public static TaskID<ImageCombo> buildImageMosaicTask(Object panel, Object palette, Object density, Object parallelism, TaskInfo taskinfo) {//####[115]####
        // ensure Method variable is set//####[115]####
        if (__pt__buildImageMosaicTask_ImagePanelItem_ListPaletteItem_int_int_method == null) {//####[115]####
            __pt__buildImageMosaicTask_ImagePanelItem_ListPaletteItem_int_int_ensureMethodVarSet();//####[115]####
        }//####[115]####
        List<Integer> __pt__taskIdIndexList = new ArrayList<Integer>();//####[115]####
        List<Integer> __pt__queueIndexList = new ArrayList<Integer>();//####[115]####
        if (panel instanceof BlockingQueue) {//####[115]####
            __pt__queueIndexList.add(0);//####[115]####
        }//####[115]####
        if (panel instanceof TaskID) {//####[115]####
            taskinfo.addDependsOn((TaskID)panel);//####[115]####
            __pt__taskIdIndexList.add(0);//####[115]####
        }//####[115]####
        if (palette instanceof BlockingQueue) {//####[115]####
            __pt__queueIndexList.add(1);//####[115]####
        }//####[115]####
        if (palette instanceof TaskID) {//####[115]####
            taskinfo.addDependsOn((TaskID)palette);//####[115]####
            __pt__taskIdIndexList.add(1);//####[115]####
        }//####[115]####
        if (density instanceof BlockingQueue) {//####[115]####
            __pt__queueIndexList.add(2);//####[115]####
        }//####[115]####
        if (density instanceof TaskID) {//####[115]####
            taskinfo.addDependsOn((TaskID)density);//####[115]####
            __pt__taskIdIndexList.add(2);//####[115]####
        }//####[115]####
        if (parallelism instanceof BlockingQueue) {//####[115]####
            __pt__queueIndexList.add(3);//####[115]####
        }//####[115]####
        if (parallelism instanceof TaskID) {//####[115]####
            taskinfo.addDependsOn((TaskID)parallelism);//####[115]####
            __pt__taskIdIndexList.add(3);//####[115]####
        }//####[115]####
        int[] __pt__queueIndexArray = new int[__pt__queueIndexList.size()];//####[115]####
        for (int __pt__i = 0; __pt__i < __pt__queueIndexArray.length; __pt__i++) {//####[115]####
            __pt__queueIndexArray[__pt__i] = __pt__queueIndexList.get(__pt__i);//####[115]####
        }//####[115]####
        taskinfo.setQueueArgIndexes(__pt__queueIndexArray);//####[115]####
        if (__pt__queueIndexArray.length > 0) {//####[115]####
            taskinfo.setIsPipeline(true);//####[115]####
        }//####[115]####
        int[] __pt__taskIdIndexArray = new int[__pt__taskIdIndexList.size()];//####[115]####
        for (int __pt__i = 0; __pt__i < __pt__taskIdIndexArray.length; __pt__i++) {//####[115]####
            __pt__taskIdIndexArray[__pt__i] = __pt__taskIdIndexList.get(__pt__i);//####[115]####
        }//####[115]####
        taskinfo.setTaskIdArgIndexes(__pt__taskIdIndexArray);//####[115]####
        taskinfo.setParameters(panel, palette, density, parallelism);//####[115]####
        taskinfo.setMethod(__pt__buildImageMosaicTask_ImagePanelItem_ListPaletteItem_int_int_method);//####[115]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[115]####
    }//####[115]####
    public static ImageCombo __pt__buildImageMosaicTask(ImagePanelItem panel, List<PaletteItem> palette, int density, int parallelism) {//####[115]####
        return buildImageMosaic(panel, palette, density);//####[116]####
    }//####[117]####
//####[117]####
//####[119]####
    private static volatile Method __pt__buildImageMosaicTask2_ImagePanelItem_ListPaletteItem_int_int_method = null;//####[119]####
    private synchronized static void __pt__buildImageMosaicTask2_ImagePanelItem_ListPaletteItem_int_int_ensureMethodVarSet() {//####[119]####
        if (__pt__buildImageMosaicTask2_ImagePanelItem_ListPaletteItem_int_int_method == null) {//####[119]####
            try {//####[119]####
                __pt__buildImageMosaicTask2_ImagePanelItem_ListPaletteItem_int_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__buildImageMosaicTask2", new Class[] {//####[119]####
                    ImagePanelItem.class, List.class, int.class, int.class//####[119]####
                });//####[119]####
            } catch (Exception e) {//####[119]####
                e.printStackTrace();//####[119]####
            }//####[119]####
        }//####[119]####
    }//####[119]####
    public static TaskID<ImageCombo> buildImageMosaicTask2(Object panel, Object palette, Object density, Object parallelism) {//####[119]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[119]####
        return buildImageMosaicTask2(panel, palette, density, parallelism, new TaskInfo());//####[119]####
    }//####[119]####
    public static TaskID<ImageCombo> buildImageMosaicTask2(Object panel, Object palette, Object density, Object parallelism, TaskInfo taskinfo) {//####[119]####
        // ensure Method variable is set//####[119]####
        if (__pt__buildImageMosaicTask2_ImagePanelItem_ListPaletteItem_int_int_method == null) {//####[119]####
            __pt__buildImageMosaicTask2_ImagePanelItem_ListPaletteItem_int_int_ensureMethodVarSet();//####[119]####
        }//####[119]####
        List<Integer> __pt__taskIdIndexList = new ArrayList<Integer>();//####[119]####
        List<Integer> __pt__queueIndexList = new ArrayList<Integer>();//####[119]####
        if (panel instanceof BlockingQueue) {//####[119]####
            __pt__queueIndexList.add(0);//####[119]####
        }//####[119]####
        if (panel instanceof TaskID) {//####[119]####
            taskinfo.addDependsOn((TaskID)panel);//####[119]####
            __pt__taskIdIndexList.add(0);//####[119]####
        }//####[119]####
        if (palette instanceof BlockingQueue) {//####[119]####
            __pt__queueIndexList.add(1);//####[119]####
        }//####[119]####
        if (palette instanceof TaskID) {//####[119]####
            taskinfo.addDependsOn((TaskID)palette);//####[119]####
            __pt__taskIdIndexList.add(1);//####[119]####
        }//####[119]####
        if (density instanceof BlockingQueue) {//####[119]####
            __pt__queueIndexList.add(2);//####[119]####
        }//####[119]####
        if (density instanceof TaskID) {//####[119]####
            taskinfo.addDependsOn((TaskID)density);//####[119]####
            __pt__taskIdIndexList.add(2);//####[119]####
        }//####[119]####
        if (parallelism instanceof BlockingQueue) {//####[119]####
            __pt__queueIndexList.add(3);//####[119]####
        }//####[119]####
        if (parallelism instanceof TaskID) {//####[119]####
            taskinfo.addDependsOn((TaskID)parallelism);//####[119]####
            __pt__taskIdIndexList.add(3);//####[119]####
        }//####[119]####
        int[] __pt__queueIndexArray = new int[__pt__queueIndexList.size()];//####[119]####
        for (int __pt__i = 0; __pt__i < __pt__queueIndexArray.length; __pt__i++) {//####[119]####
            __pt__queueIndexArray[__pt__i] = __pt__queueIndexList.get(__pt__i);//####[119]####
        }//####[119]####
        taskinfo.setQueueArgIndexes(__pt__queueIndexArray);//####[119]####
        if (__pt__queueIndexArray.length > 0) {//####[119]####
            taskinfo.setIsPipeline(true);//####[119]####
        }//####[119]####
        int[] __pt__taskIdIndexArray = new int[__pt__taskIdIndexList.size()];//####[119]####
        for (int __pt__i = 0; __pt__i < __pt__taskIdIndexArray.length; __pt__i++) {//####[119]####
            __pt__taskIdIndexArray[__pt__i] = __pt__taskIdIndexList.get(__pt__i);//####[119]####
        }//####[119]####
        taskinfo.setTaskIdArgIndexes(__pt__taskIdIndexArray);//####[119]####
        taskinfo.setParameters(panel, palette, density, parallelism);//####[119]####
        taskinfo.setMethod(__pt__buildImageMosaicTask2_ImagePanelItem_ListPaletteItem_int_int_method);//####[119]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[119]####
    }//####[119]####
    public static ImageCombo __pt__buildImageMosaicTask2(ImagePanelItem panel, List<PaletteItem> palette, int density, int parallelism) {//####[119]####
        return buildImageMosaic2(panel, palette, density);//####[120]####
    }//####[121]####
//####[121]####
//####[123]####
    public static ImageCombo buildImageMosaic2(ImagePanelItem panel, List<PaletteItem> palette, int density) {//####[123]####
        if (!palette.isEmpty()) //####[124]####
        {//####[124]####
            Image imageLarge = panel.getImageLarge();//####[125]####
            int width = imageLarge.getWidth(null) / density;//####[126]####
            if (!(width > 0)) //####[127]####
            {//####[127]####
                width = 1;//####[128]####
            }//####[129]####
            int height = imageLarge.getHeight(null) / density;//####[130]####
            if (!(height > 0)) //####[131]####
            {//####[131]####
                height = 1;//####[132]####
            }//####[133]####
            int size = palette.get(0).getImage().getWidth(null);//####[134]####
            BufferedImage template = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);//####[136]####
            Graphics2D g2 = template.createGraphics();//####[137]####
            g2.drawImage(imageLarge.getScaledInstance(width, height, Image.SCALE_DEFAULT), null, null);//####[138]####
            BufferedImage mosaicLarge = new BufferedImage(width * size, height * size, BufferedImage.TYPE_INT_RGB);//####[139]####
            Graphics2D g3 = mosaicLarge.createGraphics();//####[140]####
            TaskIDGroup group = new TaskIDGroup(height * width);//####[143]####
            for (int y = 0; y < height; y++) //####[145]####
            {//####[145]####
                for (int x = 0; x < width; x++) //####[146]####
                {//####[146]####
                    TaskID id = buildImageMosaic2Task(palette, template, g3, size, x, y);//####[147]####
                    group.add(id);//####[148]####
                }//####[149]####
            }//####[150]####
            try {//####[152]####
                group.waitTillFinished();//####[153]####
            } catch (ExecutionException e) {//####[154]####
                e.printStackTrace();//####[155]####
            } catch (InterruptedException e) {//####[156]####
                e.printStackTrace();//####[157]####
            }//####[158]####
            Image imageSquare = panel.getImageSquare();//####[160]####
            BufferedImage mosaicSquare = new BufferedImage(imageSquare.getWidth(null), imageSquare.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[161]####
            Graphics2D g4 = mosaicSquare.createGraphics();//####[162]####
            g4.drawImage(mosaicLarge.getScaledInstance(imageSquare.getWidth(null), imageSquare.getHeight(null), Image.SCALE_DEFAULT), null, null);//####[163]####
            Image imageMedium = panel.getImageMedium();//####[165]####
            BufferedImage mosaicMedium = new BufferedImage(imageMedium.getWidth(null), imageMedium.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[166]####
            Graphics2D g5 = mosaicMedium.createGraphics();//####[167]####
            g5.drawImage(mosaicLarge.getScaledInstance(imageMedium.getWidth(null), imageMedium.getHeight(null), Image.SCALE_DEFAULT), null, null);//####[168]####
            return new ImageCombo(mosaicLarge, mosaicSquare, mosaicMedium);//####[170]####
        } else {//####[171]####
            return new ImageCombo(panel.getImageLarge(), panel.getImageSquare(), panel.getImageMedium());//####[172]####
        }//####[173]####
    }//####[174]####
//####[176]####
    private static volatile Method __pt__buildImageMosaic2Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_method = null;//####[176]####
    private synchronized static void __pt__buildImageMosaic2Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_ensureMethodVarSet() {//####[176]####
        if (__pt__buildImageMosaic2Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_method == null) {//####[176]####
            try {//####[176]####
                __pt__buildImageMosaic2Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__buildImageMosaic2Task", new Class[] {//####[176]####
                    List.class, BufferedImage.class, Graphics2D.class, int.class, int.class, int.class//####[176]####
                });//####[176]####
            } catch (Exception e) {//####[176]####
                e.printStackTrace();//####[176]####
            }//####[176]####
        }//####[176]####
    }//####[176]####
    private static TaskID<Void> buildImageMosaic2Task(Object palette, Object template, Object g3, Object size, Object x, Object y) {//####[176]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[176]####
        return buildImageMosaic2Task(palette, template, g3, size, x, y, new TaskInfo());//####[176]####
    }//####[176]####
    private static TaskID<Void> buildImageMosaic2Task(Object palette, Object template, Object g3, Object size, Object x, Object y, TaskInfo taskinfo) {//####[176]####
        // ensure Method variable is set//####[176]####
        if (__pt__buildImageMosaic2Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_method == null) {//####[176]####
            __pt__buildImageMosaic2Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_ensureMethodVarSet();//####[176]####
        }//####[176]####
        List<Integer> __pt__taskIdIndexList = new ArrayList<Integer>();//####[176]####
        List<Integer> __pt__queueIndexList = new ArrayList<Integer>();//####[176]####
        if (palette instanceof BlockingQueue) {//####[176]####
            __pt__queueIndexList.add(0);//####[176]####
        }//####[176]####
        if (palette instanceof TaskID) {//####[176]####
            taskinfo.addDependsOn((TaskID)palette);//####[176]####
            __pt__taskIdIndexList.add(0);//####[176]####
        }//####[176]####
        if (template instanceof BlockingQueue) {//####[176]####
            __pt__queueIndexList.add(1);//####[176]####
        }//####[176]####
        if (template instanceof TaskID) {//####[176]####
            taskinfo.addDependsOn((TaskID)template);//####[176]####
            __pt__taskIdIndexList.add(1);//####[176]####
        }//####[176]####
        if (g3 instanceof BlockingQueue) {//####[176]####
            __pt__queueIndexList.add(2);//####[176]####
        }//####[176]####
        if (g3 instanceof TaskID) {//####[176]####
            taskinfo.addDependsOn((TaskID)g3);//####[176]####
            __pt__taskIdIndexList.add(2);//####[176]####
        }//####[176]####
        if (size instanceof BlockingQueue) {//####[176]####
            __pt__queueIndexList.add(3);//####[176]####
        }//####[176]####
        if (size instanceof TaskID) {//####[176]####
            taskinfo.addDependsOn((TaskID)size);//####[176]####
            __pt__taskIdIndexList.add(3);//####[176]####
        }//####[176]####
        if (x instanceof BlockingQueue) {//####[176]####
            __pt__queueIndexList.add(4);//####[176]####
        }//####[176]####
        if (x instanceof TaskID) {//####[176]####
            taskinfo.addDependsOn((TaskID)x);//####[176]####
            __pt__taskIdIndexList.add(4);//####[176]####
        }//####[176]####
        if (y instanceof BlockingQueue) {//####[176]####
            __pt__queueIndexList.add(5);//####[176]####
        }//####[176]####
        if (y instanceof TaskID) {//####[176]####
            taskinfo.addDependsOn((TaskID)y);//####[176]####
            __pt__taskIdIndexList.add(5);//####[176]####
        }//####[176]####
        int[] __pt__queueIndexArray = new int[__pt__queueIndexList.size()];//####[176]####
        for (int __pt__i = 0; __pt__i < __pt__queueIndexArray.length; __pt__i++) {//####[176]####
            __pt__queueIndexArray[__pt__i] = __pt__queueIndexList.get(__pt__i);//####[176]####
        }//####[176]####
        taskinfo.setQueueArgIndexes(__pt__queueIndexArray);//####[176]####
        if (__pt__queueIndexArray.length > 0) {//####[176]####
            taskinfo.setIsPipeline(true);//####[176]####
        }//####[176]####
        int[] __pt__taskIdIndexArray = new int[__pt__taskIdIndexList.size()];//####[176]####
        for (int __pt__i = 0; __pt__i < __pt__taskIdIndexArray.length; __pt__i++) {//####[176]####
            __pt__taskIdIndexArray[__pt__i] = __pt__taskIdIndexList.get(__pt__i);//####[176]####
        }//####[176]####
        taskinfo.setTaskIdArgIndexes(__pt__taskIdIndexArray);//####[176]####
        taskinfo.setParameters(palette, template, g3, size, x, y);//####[176]####
        taskinfo.setMethod(__pt__buildImageMosaic2Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_method);//####[176]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[176]####
    }//####[176]####
    public static void __pt__buildImageMosaic2Task(List<PaletteItem> palette, BufferedImage template, Graphics2D g3, int size, int x, int y) {//####[176]####
        Color tc = new Color(template.getRGB(x, y));//####[177]####
        int tr = tc.getRed();//####[178]####
        int tg = tc.getGreen();//####[179]####
        int tb = tc.getBlue();//####[180]####
        int closest = 0;//####[181]####
        int distance = 512;//####[182]####
        for (int i = 0; i < palette.size(); i++) //####[183]####
        {//####[183]####
            Color pc = new Color(palette.get(i).getColor());//####[184]####
            int pr = pc.getRed();//####[185]####
            int pg = pc.getGreen();//####[186]####
            int pb = pc.getBlue();//####[187]####
            int temp = (int) Math.sqrt(0.3 * (tr - pr) * (tr - pr) + 0.59 * (tg - pg) * (tg - pg) + 0.11 * (tb - pb) * (tb - pb));//####[188]####
            if (temp == 0) //####[189]####
            {//####[189]####
                closest = i;//####[190]####
                break;//####[191]####
            } else if (temp < distance) //####[192]####
            {//####[192]####
                closest = i;//####[193]####
                distance = temp;//####[194]####
            }//####[195]####
        }//####[196]####
        g3.drawImage(palette.get(closest).getImage(), x * size, y * size, null);//####[197]####
    }//####[198]####
//####[198]####
//####[200]####
    private static volatile Method __pt__buildImageMosaicTask3_ImagePanelItem_ListPaletteItem_int_int_method = null;//####[200]####
    private synchronized static void __pt__buildImageMosaicTask3_ImagePanelItem_ListPaletteItem_int_int_ensureMethodVarSet() {//####[200]####
        if (__pt__buildImageMosaicTask3_ImagePanelItem_ListPaletteItem_int_int_method == null) {//####[200]####
            try {//####[200]####
                __pt__buildImageMosaicTask3_ImagePanelItem_ListPaletteItem_int_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__buildImageMosaicTask3", new Class[] {//####[200]####
                    ImagePanelItem.class, List.class, int.class, int.class//####[200]####
                });//####[200]####
            } catch (Exception e) {//####[200]####
                e.printStackTrace();//####[200]####
            }//####[200]####
        }//####[200]####
    }//####[200]####
    public static TaskID<ImageCombo> buildImageMosaicTask3(Object panel, Object palette, Object density, Object parallelism) {//####[200]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[200]####
        return buildImageMosaicTask3(panel, palette, density, parallelism, new TaskInfo());//####[200]####
    }//####[200]####
    public static TaskID<ImageCombo> buildImageMosaicTask3(Object panel, Object palette, Object density, Object parallelism, TaskInfo taskinfo) {//####[200]####
        // ensure Method variable is set//####[200]####
        if (__pt__buildImageMosaicTask3_ImagePanelItem_ListPaletteItem_int_int_method == null) {//####[200]####
            __pt__buildImageMosaicTask3_ImagePanelItem_ListPaletteItem_int_int_ensureMethodVarSet();//####[200]####
        }//####[200]####
        List<Integer> __pt__taskIdIndexList = new ArrayList<Integer>();//####[200]####
        List<Integer> __pt__queueIndexList = new ArrayList<Integer>();//####[200]####
        if (panel instanceof BlockingQueue) {//####[200]####
            __pt__queueIndexList.add(0);//####[200]####
        }//####[200]####
        if (panel instanceof TaskID) {//####[200]####
            taskinfo.addDependsOn((TaskID)panel);//####[200]####
            __pt__taskIdIndexList.add(0);//####[200]####
        }//####[200]####
        if (palette instanceof BlockingQueue) {//####[200]####
            __pt__queueIndexList.add(1);//####[200]####
        }//####[200]####
        if (palette instanceof TaskID) {//####[200]####
            taskinfo.addDependsOn((TaskID)palette);//####[200]####
            __pt__taskIdIndexList.add(1);//####[200]####
        }//####[200]####
        if (density instanceof BlockingQueue) {//####[200]####
            __pt__queueIndexList.add(2);//####[200]####
        }//####[200]####
        if (density instanceof TaskID) {//####[200]####
            taskinfo.addDependsOn((TaskID)density);//####[200]####
            __pt__taskIdIndexList.add(2);//####[200]####
        }//####[200]####
        if (parallelism instanceof BlockingQueue) {//####[200]####
            __pt__queueIndexList.add(3);//####[200]####
        }//####[200]####
        if (parallelism instanceof TaskID) {//####[200]####
            taskinfo.addDependsOn((TaskID)parallelism);//####[200]####
            __pt__taskIdIndexList.add(3);//####[200]####
        }//####[200]####
        int[] __pt__queueIndexArray = new int[__pt__queueIndexList.size()];//####[200]####
        for (int __pt__i = 0; __pt__i < __pt__queueIndexArray.length; __pt__i++) {//####[200]####
            __pt__queueIndexArray[__pt__i] = __pt__queueIndexList.get(__pt__i);//####[200]####
        }//####[200]####
        taskinfo.setQueueArgIndexes(__pt__queueIndexArray);//####[200]####
        if (__pt__queueIndexArray.length > 0) {//####[200]####
            taskinfo.setIsPipeline(true);//####[200]####
        }//####[200]####
        int[] __pt__taskIdIndexArray = new int[__pt__taskIdIndexList.size()];//####[200]####
        for (int __pt__i = 0; __pt__i < __pt__taskIdIndexArray.length; __pt__i++) {//####[200]####
            __pt__taskIdIndexArray[__pt__i] = __pt__taskIdIndexList.get(__pt__i);//####[200]####
        }//####[200]####
        taskinfo.setTaskIdArgIndexes(__pt__taskIdIndexArray);//####[200]####
        taskinfo.setParameters(panel, palette, density, parallelism);//####[200]####
        taskinfo.setMethod(__pt__buildImageMosaicTask3_ImagePanelItem_ListPaletteItem_int_int_method);//####[200]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[200]####
    }//####[200]####
    public static ImageCombo __pt__buildImageMosaicTask3(ImagePanelItem panel, List<PaletteItem> palette, int density, int parallelism) {//####[200]####
        return buildImageMosaic3(panel, palette, density);//####[201]####
    }//####[202]####
//####[202]####
//####[204]####
    public static ImageCombo buildImageMosaic3(ImagePanelItem panel, List<PaletteItem> palette, int density) {//####[204]####
        if (!palette.isEmpty()) //####[205]####
        {//####[205]####
            Image imageLarge = panel.getImageLarge();//####[206]####
            int width = imageLarge.getWidth(null) / density;//####[207]####
            if (!(width > 0)) //####[208]####
            {//####[208]####
                width = 1;//####[209]####
            }//####[210]####
            int height = imageLarge.getHeight(null) / density;//####[211]####
            if (!(height > 0)) //####[212]####
            {//####[212]####
                height = 1;//####[213]####
            }//####[214]####
            int size = palette.get(0).getImage().getWidth(null);//####[215]####
            BufferedImage template = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);//####[217]####
            Graphics2D g2 = template.createGraphics();//####[218]####
            g2.drawImage(imageLarge.getScaledInstance(width, height, Image.SCALE_DEFAULT), null, null);//####[219]####
            BufferedImage mosaicLarge = new BufferedImage(width * size, height * size, BufferedImage.TYPE_INT_RGB);//####[220]####
            Graphics2D g3 = mosaicLarge.createGraphics();//####[221]####
            TaskIDGroup group = new TaskIDGroup(height);//####[223]####
            for (int y = 0; y < height; y++) //####[225]####
            {//####[225]####
                TaskID id = buildImageMosaic3Task(palette, template, g3, size, width, y);//####[226]####
                group.add(id);//####[227]####
            }//####[228]####
            try {//####[230]####
                group.waitTillFinished();//####[231]####
            } catch (ExecutionException e) {//####[232]####
                e.printStackTrace();//####[233]####
            } catch (InterruptedException e) {//####[234]####
                e.printStackTrace();//####[235]####
            }//####[236]####
            Image imageSquare = panel.getImageSquare();//####[238]####
            BufferedImage mosaicSquare = new BufferedImage(imageSquare.getWidth(null), imageSquare.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[239]####
            Graphics2D g4 = mosaicSquare.createGraphics();//####[240]####
            g4.drawImage(mosaicLarge.getScaledInstance(imageSquare.getWidth(null), imageSquare.getHeight(null), Image.SCALE_DEFAULT), null, null);//####[241]####
            Image imageMedium = panel.getImageMedium();//####[243]####
            BufferedImage mosaicMedium = new BufferedImage(imageMedium.getWidth(null), imageMedium.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[244]####
            Graphics2D g5 = mosaicMedium.createGraphics();//####[245]####
            g5.drawImage(mosaicLarge.getScaledInstance(imageMedium.getWidth(null), imageMedium.getHeight(null), Image.SCALE_DEFAULT), null, null);//####[246]####
            return new ImageCombo(mosaicLarge, mosaicSquare, mosaicMedium);//####[248]####
        } else {//####[249]####
            return new ImageCombo(panel.getImageLarge(), panel.getImageSquare(), panel.getImageMedium());//####[250]####
        }//####[251]####
    }//####[252]####
//####[254]####
    private static volatile Method __pt__buildImageMosaic3Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_method = null;//####[254]####
    private synchronized static void __pt__buildImageMosaic3Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_ensureMethodVarSet() {//####[254]####
        if (__pt__buildImageMosaic3Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_method == null) {//####[254]####
            try {//####[254]####
                __pt__buildImageMosaic3Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__buildImageMosaic3Task", new Class[] {//####[254]####
                    List.class, BufferedImage.class, Graphics2D.class, int.class, int.class, int.class//####[254]####
                });//####[254]####
            } catch (Exception e) {//####[254]####
                e.printStackTrace();//####[254]####
            }//####[254]####
        }//####[254]####
    }//####[254]####
    private static TaskID<Void> buildImageMosaic3Task(Object palette, Object template, Object g3, Object size, Object width, Object y) {//####[254]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[254]####
        return buildImageMosaic3Task(palette, template, g3, size, width, y, new TaskInfo());//####[254]####
    }//####[254]####
    private static TaskID<Void> buildImageMosaic3Task(Object palette, Object template, Object g3, Object size, Object width, Object y, TaskInfo taskinfo) {//####[254]####
        // ensure Method variable is set//####[254]####
        if (__pt__buildImageMosaic3Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_method == null) {//####[254]####
            __pt__buildImageMosaic3Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_ensureMethodVarSet();//####[254]####
        }//####[254]####
        List<Integer> __pt__taskIdIndexList = new ArrayList<Integer>();//####[254]####
        List<Integer> __pt__queueIndexList = new ArrayList<Integer>();//####[254]####
        if (palette instanceof BlockingQueue) {//####[254]####
            __pt__queueIndexList.add(0);//####[254]####
        }//####[254]####
        if (palette instanceof TaskID) {//####[254]####
            taskinfo.addDependsOn((TaskID)palette);//####[254]####
            __pt__taskIdIndexList.add(0);//####[254]####
        }//####[254]####
        if (template instanceof BlockingQueue) {//####[254]####
            __pt__queueIndexList.add(1);//####[254]####
        }//####[254]####
        if (template instanceof TaskID) {//####[254]####
            taskinfo.addDependsOn((TaskID)template);//####[254]####
            __pt__taskIdIndexList.add(1);//####[254]####
        }//####[254]####
        if (g3 instanceof BlockingQueue) {//####[254]####
            __pt__queueIndexList.add(2);//####[254]####
        }//####[254]####
        if (g3 instanceof TaskID) {//####[254]####
            taskinfo.addDependsOn((TaskID)g3);//####[254]####
            __pt__taskIdIndexList.add(2);//####[254]####
        }//####[254]####
        if (size instanceof BlockingQueue) {//####[254]####
            __pt__queueIndexList.add(3);//####[254]####
        }//####[254]####
        if (size instanceof TaskID) {//####[254]####
            taskinfo.addDependsOn((TaskID)size);//####[254]####
            __pt__taskIdIndexList.add(3);//####[254]####
        }//####[254]####
        if (width instanceof BlockingQueue) {//####[254]####
            __pt__queueIndexList.add(4);//####[254]####
        }//####[254]####
        if (width instanceof TaskID) {//####[254]####
            taskinfo.addDependsOn((TaskID)width);//####[254]####
            __pt__taskIdIndexList.add(4);//####[254]####
        }//####[254]####
        if (y instanceof BlockingQueue) {//####[254]####
            __pt__queueIndexList.add(5);//####[254]####
        }//####[254]####
        if (y instanceof TaskID) {//####[254]####
            taskinfo.addDependsOn((TaskID)y);//####[254]####
            __pt__taskIdIndexList.add(5);//####[254]####
        }//####[254]####
        int[] __pt__queueIndexArray = new int[__pt__queueIndexList.size()];//####[254]####
        for (int __pt__i = 0; __pt__i < __pt__queueIndexArray.length; __pt__i++) {//####[254]####
            __pt__queueIndexArray[__pt__i] = __pt__queueIndexList.get(__pt__i);//####[254]####
        }//####[254]####
        taskinfo.setQueueArgIndexes(__pt__queueIndexArray);//####[254]####
        if (__pt__queueIndexArray.length > 0) {//####[254]####
            taskinfo.setIsPipeline(true);//####[254]####
        }//####[254]####
        int[] __pt__taskIdIndexArray = new int[__pt__taskIdIndexList.size()];//####[254]####
        for (int __pt__i = 0; __pt__i < __pt__taskIdIndexArray.length; __pt__i++) {//####[254]####
            __pt__taskIdIndexArray[__pt__i] = __pt__taskIdIndexList.get(__pt__i);//####[254]####
        }//####[254]####
        taskinfo.setTaskIdArgIndexes(__pt__taskIdIndexArray);//####[254]####
        taskinfo.setParameters(palette, template, g3, size, width, y);//####[254]####
        taskinfo.setMethod(__pt__buildImageMosaic3Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_method);//####[254]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[254]####
    }//####[254]####
    public static void __pt__buildImageMosaic3Task(List<PaletteItem> palette, BufferedImage template, Graphics2D g3, int size, int width, int y) {//####[254]####
        for (int x = 0; x < width; x++) //####[255]####
        {//####[255]####
            Color tc = new Color(template.getRGB(x, y));//####[256]####
            int tr = tc.getRed();//####[257]####
            int tg = tc.getGreen();//####[258]####
            int tb = tc.getBlue();//####[259]####
            int closest = 0;//####[260]####
            int distance = 512;//####[261]####
            for (int i = 0; i < palette.size(); i++) //####[262]####
            {//####[262]####
                Color pc = new Color(palette.get(i).getColor());//####[263]####
                int pr = pc.getRed();//####[264]####
                int pg = pc.getGreen();//####[265]####
                int pb = pc.getBlue();//####[266]####
                int temp = (int) Math.sqrt(0.3 * (tr - pr) * (tr - pr) + 0.59 * (tg - pg) * (tg - pg) + 0.11 * (tb - pb) * (tb - pb));//####[267]####
                if (temp == 0) //####[268]####
                {//####[268]####
                    closest = i;//####[269]####
                    break;//####[270]####
                } else if (temp < distance) //####[271]####
                {//####[271]####
                    closest = i;//####[272]####
                    distance = temp;//####[273]####
                }//####[274]####
            }//####[275]####
            g3.drawImage(palette.get(closest).getImage(), x * size, y * size, null);//####[276]####
        }//####[277]####
    }//####[278]####
//####[278]####
//####[280]####
    private static volatile Method __pt__buildImageMosaicTask4_ImagePanelItem_ListPaletteItem_int_int_method = null;//####[280]####
    private synchronized static void __pt__buildImageMosaicTask4_ImagePanelItem_ListPaletteItem_int_int_ensureMethodVarSet() {//####[280]####
        if (__pt__buildImageMosaicTask4_ImagePanelItem_ListPaletteItem_int_int_method == null) {//####[280]####
            try {//####[280]####
                __pt__buildImageMosaicTask4_ImagePanelItem_ListPaletteItem_int_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__buildImageMosaicTask4", new Class[] {//####[280]####
                    ImagePanelItem.class, List.class, int.class, int.class//####[280]####
                });//####[280]####
            } catch (Exception e) {//####[280]####
                e.printStackTrace();//####[280]####
            }//####[280]####
        }//####[280]####
    }//####[280]####
    public static TaskID<ImageCombo> buildImageMosaicTask4(Object panel, Object palette, Object density, Object parallelism) {//####[280]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[280]####
        return buildImageMosaicTask4(panel, palette, density, parallelism, new TaskInfo());//####[280]####
    }//####[280]####
    public static TaskID<ImageCombo> buildImageMosaicTask4(Object panel, Object palette, Object density, Object parallelism, TaskInfo taskinfo) {//####[280]####
        // ensure Method variable is set//####[280]####
        if (__pt__buildImageMosaicTask4_ImagePanelItem_ListPaletteItem_int_int_method == null) {//####[280]####
            __pt__buildImageMosaicTask4_ImagePanelItem_ListPaletteItem_int_int_ensureMethodVarSet();//####[280]####
        }//####[280]####
        List<Integer> __pt__taskIdIndexList = new ArrayList<Integer>();//####[280]####
        List<Integer> __pt__queueIndexList = new ArrayList<Integer>();//####[280]####
        if (panel instanceof BlockingQueue) {//####[280]####
            __pt__queueIndexList.add(0);//####[280]####
        }//####[280]####
        if (panel instanceof TaskID) {//####[280]####
            taskinfo.addDependsOn((TaskID)panel);//####[280]####
            __pt__taskIdIndexList.add(0);//####[280]####
        }//####[280]####
        if (palette instanceof BlockingQueue) {//####[280]####
            __pt__queueIndexList.add(1);//####[280]####
        }//####[280]####
        if (palette instanceof TaskID) {//####[280]####
            taskinfo.addDependsOn((TaskID)palette);//####[280]####
            __pt__taskIdIndexList.add(1);//####[280]####
        }//####[280]####
        if (density instanceof BlockingQueue) {//####[280]####
            __pt__queueIndexList.add(2);//####[280]####
        }//####[280]####
        if (density instanceof TaskID) {//####[280]####
            taskinfo.addDependsOn((TaskID)density);//####[280]####
            __pt__taskIdIndexList.add(2);//####[280]####
        }//####[280]####
        if (parallelism instanceof BlockingQueue) {//####[280]####
            __pt__queueIndexList.add(3);//####[280]####
        }//####[280]####
        if (parallelism instanceof TaskID) {//####[280]####
            taskinfo.addDependsOn((TaskID)parallelism);//####[280]####
            __pt__taskIdIndexList.add(3);//####[280]####
        }//####[280]####
        int[] __pt__queueIndexArray = new int[__pt__queueIndexList.size()];//####[280]####
        for (int __pt__i = 0; __pt__i < __pt__queueIndexArray.length; __pt__i++) {//####[280]####
            __pt__queueIndexArray[__pt__i] = __pt__queueIndexList.get(__pt__i);//####[280]####
        }//####[280]####
        taskinfo.setQueueArgIndexes(__pt__queueIndexArray);//####[280]####
        if (__pt__queueIndexArray.length > 0) {//####[280]####
            taskinfo.setIsPipeline(true);//####[280]####
        }//####[280]####
        int[] __pt__taskIdIndexArray = new int[__pt__taskIdIndexList.size()];//####[280]####
        for (int __pt__i = 0; __pt__i < __pt__taskIdIndexArray.length; __pt__i++) {//####[280]####
            __pt__taskIdIndexArray[__pt__i] = __pt__taskIdIndexList.get(__pt__i);//####[280]####
        }//####[280]####
        taskinfo.setTaskIdArgIndexes(__pt__taskIdIndexArray);//####[280]####
        taskinfo.setParameters(panel, palette, density, parallelism);//####[280]####
        taskinfo.setMethod(__pt__buildImageMosaicTask4_ImagePanelItem_ListPaletteItem_int_int_method);//####[280]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[280]####
    }//####[280]####
    public static ImageCombo __pt__buildImageMosaicTask4(ImagePanelItem panel, List<PaletteItem> palette, int density, int parallelism) {//####[280]####
        return buildImageMosaic4(panel, palette, density);//####[281]####
    }//####[282]####
//####[282]####
//####[284]####
    public static ImageCombo buildImageMosaic4(ImagePanelItem panel, List<PaletteItem> palette, int density) {//####[284]####
        if (!palette.isEmpty()) //####[285]####
        {//####[285]####
            Image imageLarge = panel.getImageLarge();//####[286]####
            int width = imageLarge.getWidth(null) / density;//####[287]####
            if (!(width > 0)) //####[288]####
            {//####[288]####
                width = 1;//####[289]####
            }//####[290]####
            int height = imageLarge.getHeight(null) / density;//####[291]####
            if (!(height > 0)) //####[292]####
            {//####[292]####
                height = 1;//####[293]####
            }//####[294]####
            int size = palette.get(0).getImage().getWidth(null);//####[295]####
            BufferedImage template = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);//####[297]####
            Graphics2D g2 = template.createGraphics();//####[298]####
            g2.drawImage(imageLarge.getScaledInstance(width, height, Image.SCALE_DEFAULT), null, null);//####[299]####
            BufferedImage mosaicLarge = new BufferedImage(width * size, height * size, BufferedImage.TYPE_INT_RGB);//####[300]####
            Graphics2D g3 = mosaicLarge.createGraphics();//####[301]####
            TaskIDGroup group = new TaskIDGroup(width);//####[303]####
            for (int x = 0; x < width; x++) //####[305]####
            {//####[305]####
                TaskID id = buildImageMosaic4Task(palette, template, g3, size, height, x);//####[306]####
                group.add(id);//####[307]####
            }//####[308]####
            try {//####[310]####
                group.waitTillFinished();//####[311]####
            } catch (ExecutionException e) {//####[312]####
                e.printStackTrace();//####[313]####
            } catch (InterruptedException e) {//####[314]####
                e.printStackTrace();//####[315]####
            }//####[316]####
            Image imageSquare = panel.getImageSquare();//####[318]####
            BufferedImage mosaicSquare = new BufferedImage(imageSquare.getWidth(null), imageSquare.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[319]####
            Graphics2D g4 = mosaicSquare.createGraphics();//####[320]####
            g4.drawImage(mosaicLarge.getScaledInstance(imageSquare.getWidth(null), imageSquare.getHeight(null), Image.SCALE_DEFAULT), null, null);//####[321]####
            Image imageMedium = panel.getImageMedium();//####[323]####
            BufferedImage mosaicMedium = new BufferedImage(imageMedium.getWidth(null), imageMedium.getHeight(null), BufferedImage.TYPE_INT_RGB);//####[324]####
            Graphics2D g5 = mosaicMedium.createGraphics();//####[325]####
            g5.drawImage(mosaicLarge.getScaledInstance(imageMedium.getWidth(null), imageMedium.getHeight(null), Image.SCALE_DEFAULT), null, null);//####[326]####
            return new ImageCombo(mosaicLarge, mosaicSquare, mosaicMedium);//####[328]####
        } else {//####[329]####
            return new ImageCombo(panel.getImageLarge(), panel.getImageSquare(), panel.getImageMedium());//####[330]####
        }//####[331]####
    }//####[332]####
//####[334]####
    private static volatile Method __pt__buildImageMosaic4Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_method = null;//####[334]####
    private synchronized static void __pt__buildImageMosaic4Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_ensureMethodVarSet() {//####[334]####
        if (__pt__buildImageMosaic4Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_method == null) {//####[334]####
            try {//####[334]####
                __pt__buildImageMosaic4Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__buildImageMosaic4Task", new Class[] {//####[334]####
                    List.class, BufferedImage.class, Graphics2D.class, int.class, int.class, int.class//####[334]####
                });//####[334]####
            } catch (Exception e) {//####[334]####
                e.printStackTrace();//####[334]####
            }//####[334]####
        }//####[334]####
    }//####[334]####
    private static TaskID<Void> buildImageMosaic4Task(Object palette, Object template, Object g3, Object size, Object height, Object x) {//####[334]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[334]####
        return buildImageMosaic4Task(palette, template, g3, size, height, x, new TaskInfo());//####[334]####
    }//####[334]####
    private static TaskID<Void> buildImageMosaic4Task(Object palette, Object template, Object g3, Object size, Object height, Object x, TaskInfo taskinfo) {//####[334]####
        // ensure Method variable is set//####[334]####
        if (__pt__buildImageMosaic4Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_method == null) {//####[334]####
            __pt__buildImageMosaic4Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_ensureMethodVarSet();//####[334]####
        }//####[334]####
        List<Integer> __pt__taskIdIndexList = new ArrayList<Integer>();//####[334]####
        List<Integer> __pt__queueIndexList = new ArrayList<Integer>();//####[334]####
        if (palette instanceof BlockingQueue) {//####[334]####
            __pt__queueIndexList.add(0);//####[334]####
        }//####[334]####
        if (palette instanceof TaskID) {//####[334]####
            taskinfo.addDependsOn((TaskID)palette);//####[334]####
            __pt__taskIdIndexList.add(0);//####[334]####
        }//####[334]####
        if (template instanceof BlockingQueue) {//####[334]####
            __pt__queueIndexList.add(1);//####[334]####
        }//####[334]####
        if (template instanceof TaskID) {//####[334]####
            taskinfo.addDependsOn((TaskID)template);//####[334]####
            __pt__taskIdIndexList.add(1);//####[334]####
        }//####[334]####
        if (g3 instanceof BlockingQueue) {//####[334]####
            __pt__queueIndexList.add(2);//####[334]####
        }//####[334]####
        if (g3 instanceof TaskID) {//####[334]####
            taskinfo.addDependsOn((TaskID)g3);//####[334]####
            __pt__taskIdIndexList.add(2);//####[334]####
        }//####[334]####
        if (size instanceof BlockingQueue) {//####[334]####
            __pt__queueIndexList.add(3);//####[334]####
        }//####[334]####
        if (size instanceof TaskID) {//####[334]####
            taskinfo.addDependsOn((TaskID)size);//####[334]####
            __pt__taskIdIndexList.add(3);//####[334]####
        }//####[334]####
        if (height instanceof BlockingQueue) {//####[334]####
            __pt__queueIndexList.add(4);//####[334]####
        }//####[334]####
        if (height instanceof TaskID) {//####[334]####
            taskinfo.addDependsOn((TaskID)height);//####[334]####
            __pt__taskIdIndexList.add(4);//####[334]####
        }//####[334]####
        if (x instanceof BlockingQueue) {//####[334]####
            __pt__queueIndexList.add(5);//####[334]####
        }//####[334]####
        if (x instanceof TaskID) {//####[334]####
            taskinfo.addDependsOn((TaskID)x);//####[334]####
            __pt__taskIdIndexList.add(5);//####[334]####
        }//####[334]####
        int[] __pt__queueIndexArray = new int[__pt__queueIndexList.size()];//####[334]####
        for (int __pt__i = 0; __pt__i < __pt__queueIndexArray.length; __pt__i++) {//####[334]####
            __pt__queueIndexArray[__pt__i] = __pt__queueIndexList.get(__pt__i);//####[334]####
        }//####[334]####
        taskinfo.setQueueArgIndexes(__pt__queueIndexArray);//####[334]####
        if (__pt__queueIndexArray.length > 0) {//####[334]####
            taskinfo.setIsPipeline(true);//####[334]####
        }//####[334]####
        int[] __pt__taskIdIndexArray = new int[__pt__taskIdIndexList.size()];//####[334]####
        for (int __pt__i = 0; __pt__i < __pt__taskIdIndexArray.length; __pt__i++) {//####[334]####
            __pt__taskIdIndexArray[__pt__i] = __pt__taskIdIndexList.get(__pt__i);//####[334]####
        }//####[334]####
        taskinfo.setTaskIdArgIndexes(__pt__taskIdIndexArray);//####[334]####
        taskinfo.setParameters(palette, template, g3, size, height, x);//####[334]####
        taskinfo.setMethod(__pt__buildImageMosaic4Task_ListPaletteItem_BufferedImage_Graphics2D_int_int_int_method);//####[334]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[334]####
    }//####[334]####
    public static void __pt__buildImageMosaic4Task(List<PaletteItem> palette, BufferedImage template, Graphics2D g3, int size, int height, int x) {//####[334]####
        for (int y = 0; y < height; y++) //####[335]####
        {//####[335]####
            Color tc = new Color(template.getRGB(x, y));//####[336]####
            int tr = tc.getRed();//####[337]####
            int tg = tc.getGreen();//####[338]####
            int tb = tc.getBlue();//####[339]####
            int closest = 0;//####[340]####
            int distance = 512;//####[341]####
            for (int i = 0; i < palette.size(); i++) //####[342]####
            {//####[342]####
                Color pc = new Color(palette.get(i).getColor());//####[343]####
                int pr = pc.getRed();//####[344]####
                int pg = pc.getGreen();//####[345]####
                int pb = pc.getBlue();//####[346]####
                int temp = (int) Math.sqrt(0.3 * (tr - pr) * (tr - pr) + 0.59 * (tg - pg) * (tg - pg) + 0.11 * (tb - pb) * (tb - pb));//####[347]####
                if (temp == 0) //####[348]####
                {//####[348]####
                    closest = i;//####[349]####
                    break;//####[350]####
                } else if (temp < distance) //####[351]####
                {//####[351]####
                    closest = i;//####[352]####
                    distance = temp;//####[353]####
                }//####[354]####
            }//####[355]####
            g3.drawImage(palette.get(closest).getImage(), x * size, y * size, null);//####[356]####
        }//####[357]####
    }//####[358]####
//####[358]####
//####[361]####
    private static volatile Method __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method = null;//####[361]####
    private synchronized static void __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet() {//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            try {//####[361]####
                __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__buildMosaicPaletteItemTask", new Class[] {//####[361]####
                    ImagePanelItem.class, List.class, int.class//####[361]####
                });//####[361]####
            } catch (Exception e) {//####[361]####
                e.printStackTrace();//####[361]####
            }//####[361]####
        }//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(ImagePanelItem panel, List<PaletteItem> palette, int size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(ImagePanelItem panel, List<PaletteItem> palette, int size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(TaskID<ImagePanelItem> panel, List<PaletteItem> palette, int size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(TaskID<ImagePanelItem> panel, List<PaletteItem> palette, int size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setTaskIdArgIndexes(0);//####[361]####
        taskinfo.addDependsOn(panel);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(BlockingQueue<ImagePanelItem> panel, List<PaletteItem> palette, int size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(BlockingQueue<ImagePanelItem> panel, List<PaletteItem> palette, int size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setQueueArgIndexes(0);//####[361]####
        taskinfo.setIsPipeline(true);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(ImagePanelItem panel, TaskID<List<PaletteItem>> palette, int size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(ImagePanelItem panel, TaskID<List<PaletteItem>> palette, int size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setTaskIdArgIndexes(1);//####[361]####
        taskinfo.addDependsOn(palette);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(TaskID<ImagePanelItem> panel, TaskID<List<PaletteItem>> palette, int size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(TaskID<ImagePanelItem> panel, TaskID<List<PaletteItem>> palette, int size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[361]####
        taskinfo.addDependsOn(panel);//####[361]####
        taskinfo.addDependsOn(palette);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(BlockingQueue<ImagePanelItem> panel, TaskID<List<PaletteItem>> palette, int size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(BlockingQueue<ImagePanelItem> panel, TaskID<List<PaletteItem>> palette, int size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setQueueArgIndexes(0);//####[361]####
        taskinfo.setIsPipeline(true);//####[361]####
        taskinfo.setTaskIdArgIndexes(1);//####[361]####
        taskinfo.addDependsOn(palette);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(ImagePanelItem panel, BlockingQueue<List<PaletteItem>> palette, int size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(ImagePanelItem panel, BlockingQueue<List<PaletteItem>> palette, int size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setQueueArgIndexes(1);//####[361]####
        taskinfo.setIsPipeline(true);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(TaskID<ImagePanelItem> panel, BlockingQueue<List<PaletteItem>> palette, int size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(TaskID<ImagePanelItem> panel, BlockingQueue<List<PaletteItem>> palette, int size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setQueueArgIndexes(1);//####[361]####
        taskinfo.setIsPipeline(true);//####[361]####
        taskinfo.setTaskIdArgIndexes(0);//####[361]####
        taskinfo.addDependsOn(panel);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(BlockingQueue<ImagePanelItem> panel, BlockingQueue<List<PaletteItem>> palette, int size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(BlockingQueue<ImagePanelItem> panel, BlockingQueue<List<PaletteItem>> palette, int size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setQueueArgIndexes(0, 1);//####[361]####
        taskinfo.setIsPipeline(true);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(ImagePanelItem panel, List<PaletteItem> palette, TaskID<Integer> size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(ImagePanelItem panel, List<PaletteItem> palette, TaskID<Integer> size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setTaskIdArgIndexes(2);//####[361]####
        taskinfo.addDependsOn(size);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(TaskID<ImagePanelItem> panel, List<PaletteItem> palette, TaskID<Integer> size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(TaskID<ImagePanelItem> panel, List<PaletteItem> palette, TaskID<Integer> size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setTaskIdArgIndexes(0, 2);//####[361]####
        taskinfo.addDependsOn(panel);//####[361]####
        taskinfo.addDependsOn(size);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(BlockingQueue<ImagePanelItem> panel, List<PaletteItem> palette, TaskID<Integer> size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(BlockingQueue<ImagePanelItem> panel, List<PaletteItem> palette, TaskID<Integer> size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setQueueArgIndexes(0);//####[361]####
        taskinfo.setIsPipeline(true);//####[361]####
        taskinfo.setTaskIdArgIndexes(2);//####[361]####
        taskinfo.addDependsOn(size);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(ImagePanelItem panel, TaskID<List<PaletteItem>> palette, TaskID<Integer> size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(ImagePanelItem panel, TaskID<List<PaletteItem>> palette, TaskID<Integer> size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setTaskIdArgIndexes(1, 2);//####[361]####
        taskinfo.addDependsOn(palette);//####[361]####
        taskinfo.addDependsOn(size);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(TaskID<ImagePanelItem> panel, TaskID<List<PaletteItem>> palette, TaskID<Integer> size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(TaskID<ImagePanelItem> panel, TaskID<List<PaletteItem>> palette, TaskID<Integer> size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setTaskIdArgIndexes(0, 1, 2);//####[361]####
        taskinfo.addDependsOn(panel);//####[361]####
        taskinfo.addDependsOn(palette);//####[361]####
        taskinfo.addDependsOn(size);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(BlockingQueue<ImagePanelItem> panel, TaskID<List<PaletteItem>> palette, TaskID<Integer> size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(BlockingQueue<ImagePanelItem> panel, TaskID<List<PaletteItem>> palette, TaskID<Integer> size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setQueueArgIndexes(0);//####[361]####
        taskinfo.setIsPipeline(true);//####[361]####
        taskinfo.setTaskIdArgIndexes(1, 2);//####[361]####
        taskinfo.addDependsOn(palette);//####[361]####
        taskinfo.addDependsOn(size);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(ImagePanelItem panel, BlockingQueue<List<PaletteItem>> palette, TaskID<Integer> size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(ImagePanelItem panel, BlockingQueue<List<PaletteItem>> palette, TaskID<Integer> size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setQueueArgIndexes(1);//####[361]####
        taskinfo.setIsPipeline(true);//####[361]####
        taskinfo.setTaskIdArgIndexes(2);//####[361]####
        taskinfo.addDependsOn(size);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(TaskID<ImagePanelItem> panel, BlockingQueue<List<PaletteItem>> palette, TaskID<Integer> size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(TaskID<ImagePanelItem> panel, BlockingQueue<List<PaletteItem>> palette, TaskID<Integer> size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setQueueArgIndexes(1);//####[361]####
        taskinfo.setIsPipeline(true);//####[361]####
        taskinfo.setTaskIdArgIndexes(0, 2);//####[361]####
        taskinfo.addDependsOn(panel);//####[361]####
        taskinfo.addDependsOn(size);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(BlockingQueue<ImagePanelItem> panel, BlockingQueue<List<PaletteItem>> palette, TaskID<Integer> size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(BlockingQueue<ImagePanelItem> panel, BlockingQueue<List<PaletteItem>> palette, TaskID<Integer> size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setQueueArgIndexes(0, 1);//####[361]####
        taskinfo.setIsPipeline(true);//####[361]####
        taskinfo.setTaskIdArgIndexes(2);//####[361]####
        taskinfo.addDependsOn(size);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(ImagePanelItem panel, List<PaletteItem> palette, BlockingQueue<Integer> size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(ImagePanelItem panel, List<PaletteItem> palette, BlockingQueue<Integer> size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setQueueArgIndexes(2);//####[361]####
        taskinfo.setIsPipeline(true);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(TaskID<ImagePanelItem> panel, List<PaletteItem> palette, BlockingQueue<Integer> size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(TaskID<ImagePanelItem> panel, List<PaletteItem> palette, BlockingQueue<Integer> size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setQueueArgIndexes(2);//####[361]####
        taskinfo.setIsPipeline(true);//####[361]####
        taskinfo.setTaskIdArgIndexes(0);//####[361]####
        taskinfo.addDependsOn(panel);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(BlockingQueue<ImagePanelItem> panel, List<PaletteItem> palette, BlockingQueue<Integer> size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(BlockingQueue<ImagePanelItem> panel, List<PaletteItem> palette, BlockingQueue<Integer> size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setQueueArgIndexes(0, 2);//####[361]####
        taskinfo.setIsPipeline(true);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(ImagePanelItem panel, TaskID<List<PaletteItem>> palette, BlockingQueue<Integer> size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(ImagePanelItem panel, TaskID<List<PaletteItem>> palette, BlockingQueue<Integer> size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setQueueArgIndexes(2);//####[361]####
        taskinfo.setIsPipeline(true);//####[361]####
        taskinfo.setTaskIdArgIndexes(1);//####[361]####
        taskinfo.addDependsOn(palette);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(TaskID<ImagePanelItem> panel, TaskID<List<PaletteItem>> palette, BlockingQueue<Integer> size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(TaskID<ImagePanelItem> panel, TaskID<List<PaletteItem>> palette, BlockingQueue<Integer> size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setQueueArgIndexes(2);//####[361]####
        taskinfo.setIsPipeline(true);//####[361]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[361]####
        taskinfo.addDependsOn(panel);//####[361]####
        taskinfo.addDependsOn(palette);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(BlockingQueue<ImagePanelItem> panel, TaskID<List<PaletteItem>> palette, BlockingQueue<Integer> size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(BlockingQueue<ImagePanelItem> panel, TaskID<List<PaletteItem>> palette, BlockingQueue<Integer> size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setQueueArgIndexes(0, 2);//####[361]####
        taskinfo.setIsPipeline(true);//####[361]####
        taskinfo.setTaskIdArgIndexes(1);//####[361]####
        taskinfo.addDependsOn(palette);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(ImagePanelItem panel, BlockingQueue<List<PaletteItem>> palette, BlockingQueue<Integer> size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(ImagePanelItem panel, BlockingQueue<List<PaletteItem>> palette, BlockingQueue<Integer> size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setQueueArgIndexes(1, 2);//####[361]####
        taskinfo.setIsPipeline(true);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(TaskID<ImagePanelItem> panel, BlockingQueue<List<PaletteItem>> palette, BlockingQueue<Integer> size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(TaskID<ImagePanelItem> panel, BlockingQueue<List<PaletteItem>> palette, BlockingQueue<Integer> size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setQueueArgIndexes(1, 2);//####[361]####
        taskinfo.setIsPipeline(true);//####[361]####
        taskinfo.setTaskIdArgIndexes(0);//####[361]####
        taskinfo.addDependsOn(panel);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(BlockingQueue<ImagePanelItem> panel, BlockingQueue<List<PaletteItem>> palette, BlockingQueue<Integer> size) {//####[361]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[361]####
        return buildMosaicPaletteItemTask(panel, palette, size, new TaskInfo());//####[361]####
    }//####[361]####
    public static TaskID<List<PaletteItem>> buildMosaicPaletteItemTask(BlockingQueue<ImagePanelItem> panel, BlockingQueue<List<PaletteItem>> palette, BlockingQueue<Integer> size, TaskInfo taskinfo) {//####[361]####
        // ensure Method variable is set//####[361]####
        if (__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method == null) {//####[361]####
            __pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_ensureMethodVarSet();//####[361]####
        }//####[361]####
        taskinfo.setQueueArgIndexes(0, 1, 2);//####[361]####
        taskinfo.setIsPipeline(true);//####[361]####
        taskinfo.setParameters(panel, palette, size);//####[361]####
        taskinfo.setMethod(__pt__buildMosaicPaletteItemTask_ImagePanelItem_ListPaletteItem_int_method);//####[361]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[361]####
    }//####[361]####
    public static List<PaletteItem> __pt__buildMosaicPaletteItemTask(ImagePanelItem panel, List<PaletteItem> palette, int size) {//####[361]####
        return buildMosaicPaletteItem(panel, palette, size);//####[362]####
    }//####[363]####
//####[363]####
//####[365]####
    public static List<PaletteItem> buildMosaicPaletteItem(ImagePanelItem panel, List<PaletteItem> palette, int size) {//####[365]####
        Image imageLarge = panel.getImageLarge();//####[366]####
        BufferedImage color = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);//####[368]####
        Graphics2D g2 = color.createGraphics();//####[369]####
        g2.drawImage(imageLarge.getScaledInstance(1, 1, Image.SCALE_DEFAULT), null, null);//####[370]####
        BufferedImage image = new BufferedImage(size, size, BufferedImage.TYPE_INT_RGB);//####[372]####
        Graphics2D g3 = image.createGraphics();//####[373]####
        g3.drawImage(imageLarge.getScaledInstance(size, size, Image.SCALE_DEFAULT), null, null);//####[374]####
        palette.add(new PaletteItem(color.getRGB(0, 0), image));//####[376]####
        return palette;//####[377]####
    }//####[378]####
}//####[378]####
