package house;//####[1]####
//####[1]####
import java.awt.Color;//####[3]####
import java.awt.Dimension;//####[4]####
import java.awt.Font;//####[5]####
import java.awt.Graphics;//####[6]####
import java.util.ArrayList;//####[7]####
import java.util.Collections;//####[8]####
import java.util.Iterator;//####[9]####
import java.util.concurrent.ConcurrentLinkedQueue;//####[10]####
import javax.swing.JApplet;//####[12]####
//####[12]####
//-- ParaTask related imports//####[12]####
import pt.runtime.*;//####[12]####
import java.util.concurrent.ExecutionException;//####[12]####
import java.util.concurrent.locks.*;//####[12]####
import java.lang.reflect.*;//####[12]####
import pt.runtime.GuiThread;//####[12]####
import java.util.concurrent.BlockingQueue;//####[12]####
import java.util.ArrayList;//####[12]####
import java.util.List;//####[12]####
//####[12]####
public class House extends JApplet {//####[14]####
    static{ParaTask.init();}//####[14]####
    /*  ParaTask helper method to access private/protected slots *///####[14]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[14]####
        if (m.getParameterTypes().length == 0)//####[14]####
            m.invoke(instance);//####[14]####
        else if ((m.getParameterTypes().length == 1))//####[14]####
            m.invoke(instance, arg);//####[14]####
        else //####[14]####
            m.invoke(instance, arg, interResult);//####[14]####
    }//####[14]####
//####[16]####
    private Build builder;//####[16]####
//####[18]####
    private int width = 500;//####[18]####
//####[19]####
    private int height = 320;//####[19]####
//####[21]####
    private int N = 20;//####[21]####
//####[22]####
    private boolean clearScreen = true;//####[22]####
//####[24]####
    private ArrayList<BuildingMaterial> foundation = new ArrayList<BuildingMaterial>();//####[24]####
//####[25]####
    private ArrayList<BuildingMaterial> wallSiding = new ArrayList<BuildingMaterial>();//####[25]####
//####[26]####
    private ArrayList<BuildingMaterial> roofTiles = new ArrayList<BuildingMaterial>();//####[26]####
//####[27]####
    private ArrayList<BuildingMaterial> windows = new ArrayList<BuildingMaterial>();//####[27]####
//####[28]####
    private BuildingMaterial door = null;//####[28]####
//####[29]####
    private BuildingMaterial forSaleSign = null;//####[29]####
//####[31]####
    private Color colorRoof;//####[31]####
//####[32]####
    private Color colorWalls;//####[32]####
//####[34]####
    public House(Build builder) {//####[34]####
        this.builder = builder;//####[35]####
        setPreferredSize(new Dimension(width, height));//####[36]####
        initFoundation();//####[37]####
        initWallSiding();//####[38]####
        initRoofTiles();//####[39]####
        initWindowsAndDoor();//####[40]####
        initForSaleSign();//####[41]####
    }//####[42]####
//####[44]####
    private static volatile Method __pt__buildAllTask_ConcurrentLinkedQueueBuildingMaterial_method = null;//####[44]####
    private synchronized static void __pt__buildAllTask_ConcurrentLinkedQueueBuildingMaterial_ensureMethodVarSet() {//####[44]####
        if (__pt__buildAllTask_ConcurrentLinkedQueueBuildingMaterial_method == null) {//####[44]####
            try {//####[44]####
                __pt__buildAllTask_ConcurrentLinkedQueueBuildingMaterial_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__buildAllTask", new Class[] {//####[44]####
                    ConcurrentLinkedQueue.class//####[44]####
                });//####[44]####
            } catch (Exception e) {//####[44]####
                e.printStackTrace();//####[44]####
            }//####[44]####
        }//####[44]####
    }//####[44]####
    private TaskIDGroup<Void> buildAllTask(ConcurrentLinkedQueue<BuildingMaterial> items) {//####[44]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[44]####
        return buildAllTask(items, new TaskInfo());//####[44]####
    }//####[44]####
    private TaskIDGroup<Void> buildAllTask(ConcurrentLinkedQueue<BuildingMaterial> items, TaskInfo taskinfo) {//####[44]####
        // ensure Method variable is set//####[44]####
        if (__pt__buildAllTask_ConcurrentLinkedQueueBuildingMaterial_method == null) {//####[44]####
            __pt__buildAllTask_ConcurrentLinkedQueueBuildingMaterial_ensureMethodVarSet();//####[44]####
        }//####[44]####
        taskinfo.setParameters(items);//####[44]####
        taskinfo.setMethod(__pt__buildAllTask_ConcurrentLinkedQueueBuildingMaterial_method);//####[44]####
        taskinfo.setInstance(this);//####[44]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[44]####
    }//####[44]####
    private TaskIDGroup<Void> buildAllTask(TaskID<ConcurrentLinkedQueue<BuildingMaterial>> items) {//####[44]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[44]####
        return buildAllTask(items, new TaskInfo());//####[44]####
    }//####[44]####
    private TaskIDGroup<Void> buildAllTask(TaskID<ConcurrentLinkedQueue<BuildingMaterial>> items, TaskInfo taskinfo) {//####[44]####
        // ensure Method variable is set//####[44]####
        if (__pt__buildAllTask_ConcurrentLinkedQueueBuildingMaterial_method == null) {//####[44]####
            __pt__buildAllTask_ConcurrentLinkedQueueBuildingMaterial_ensureMethodVarSet();//####[44]####
        }//####[44]####
        taskinfo.setTaskIdArgIndexes(0);//####[44]####
        taskinfo.addDependsOn(items);//####[44]####
        taskinfo.setParameters(items);//####[44]####
        taskinfo.setMethod(__pt__buildAllTask_ConcurrentLinkedQueueBuildingMaterial_method);//####[44]####
        taskinfo.setInstance(this);//####[44]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[44]####
    }//####[44]####
    private TaskIDGroup<Void> buildAllTask(BlockingQueue<ConcurrentLinkedQueue<BuildingMaterial>> items) {//####[44]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[44]####
        return buildAllTask(items, new TaskInfo());//####[44]####
    }//####[44]####
    private TaskIDGroup<Void> buildAllTask(BlockingQueue<ConcurrentLinkedQueue<BuildingMaterial>> items, TaskInfo taskinfo) {//####[44]####
        // ensure Method variable is set//####[44]####
        if (__pt__buildAllTask_ConcurrentLinkedQueueBuildingMaterial_method == null) {//####[44]####
            __pt__buildAllTask_ConcurrentLinkedQueueBuildingMaterial_ensureMethodVarSet();//####[44]####
        }//####[44]####
        taskinfo.setQueueArgIndexes(0);//####[44]####
        taskinfo.setIsPipeline(true);//####[44]####
        taskinfo.setParameters(items);//####[44]####
        taskinfo.setMethod(__pt__buildAllTask_ConcurrentLinkedQueueBuildingMaterial_method);//####[44]####
        taskinfo.setInstance(this);//####[44]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[44]####
    }//####[44]####
    public void __pt__buildAllTask(ConcurrentLinkedQueue<BuildingMaterial> items) {//####[44]####
        BuildingMaterial b = null;//####[45]####
        while ((b = items.poll()) != null) //####[47]####
        {//####[47]####
            buildItem(b);//####[48]####
        }//####[49]####
    }//####[50]####
//####[50]####
//####[52]####
    private void buildAll(ArrayList<BuildingMaterial> items) {//####[52]####
        BuildingMaterial b = null;//####[53]####
        Iterator<BuildingMaterial> it = items.iterator();//####[54]####
        while (it.hasNext()) //####[55]####
        {//####[55]####
            buildItem(it.next());//####[56]####
        }//####[57]####
    }//####[58]####
//####[60]####
    private static volatile Method __pt__buildTask_Color_Color_method = null;//####[60]####
    private synchronized static void __pt__buildTask_Color_Color_ensureMethodVarSet() {//####[60]####
        if (__pt__buildTask_Color_Color_method == null) {//####[60]####
            try {//####[60]####
                __pt__buildTask_Color_Color_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__buildTask", new Class[] {//####[60]####
                    Color.class, Color.class//####[60]####
                });//####[60]####
            } catch (Exception e) {//####[60]####
                e.printStackTrace();//####[60]####
            }//####[60]####
        }//####[60]####
    }//####[60]####
    public TaskID<Void> buildTask(Color colorWalls, Color colorRoof) {//####[60]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[60]####
        return buildTask(colorWalls, colorRoof, new TaskInfo());//####[60]####
    }//####[60]####
    public TaskID<Void> buildTask(Color colorWalls, Color colorRoof, TaskInfo taskinfo) {//####[60]####
        // ensure Method variable is set//####[60]####
        if (__pt__buildTask_Color_Color_method == null) {//####[60]####
            __pt__buildTask_Color_Color_ensureMethodVarSet();//####[60]####
        }//####[60]####
        taskinfo.setParameters(colorWalls, colorRoof);//####[60]####
        taskinfo.setMethod(__pt__buildTask_Color_Color_method);//####[60]####
        taskinfo.setInstance(this);//####[60]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[60]####
    }//####[60]####
    public TaskID<Void> buildTask(TaskID<Color> colorWalls, Color colorRoof) {//####[60]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[60]####
        return buildTask(colorWalls, colorRoof, new TaskInfo());//####[60]####
    }//####[60]####
    public TaskID<Void> buildTask(TaskID<Color> colorWalls, Color colorRoof, TaskInfo taskinfo) {//####[60]####
        // ensure Method variable is set//####[60]####
        if (__pt__buildTask_Color_Color_method == null) {//####[60]####
            __pt__buildTask_Color_Color_ensureMethodVarSet();//####[60]####
        }//####[60]####
        taskinfo.setTaskIdArgIndexes(0);//####[60]####
        taskinfo.addDependsOn(colorWalls);//####[60]####
        taskinfo.setParameters(colorWalls, colorRoof);//####[60]####
        taskinfo.setMethod(__pt__buildTask_Color_Color_method);//####[60]####
        taskinfo.setInstance(this);//####[60]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[60]####
    }//####[60]####
    public TaskID<Void> buildTask(BlockingQueue<Color> colorWalls, Color colorRoof) {//####[60]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[60]####
        return buildTask(colorWalls, colorRoof, new TaskInfo());//####[60]####
    }//####[60]####
    public TaskID<Void> buildTask(BlockingQueue<Color> colorWalls, Color colorRoof, TaskInfo taskinfo) {//####[60]####
        // ensure Method variable is set//####[60]####
        if (__pt__buildTask_Color_Color_method == null) {//####[60]####
            __pt__buildTask_Color_Color_ensureMethodVarSet();//####[60]####
        }//####[60]####
        taskinfo.setQueueArgIndexes(0);//####[60]####
        taskinfo.setIsPipeline(true);//####[60]####
        taskinfo.setParameters(colorWalls, colorRoof);//####[60]####
        taskinfo.setMethod(__pt__buildTask_Color_Color_method);//####[60]####
        taskinfo.setInstance(this);//####[60]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[60]####
    }//####[60]####
    public TaskID<Void> buildTask(Color colorWalls, TaskID<Color> colorRoof) {//####[60]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[60]####
        return buildTask(colorWalls, colorRoof, new TaskInfo());//####[60]####
    }//####[60]####
    public TaskID<Void> buildTask(Color colorWalls, TaskID<Color> colorRoof, TaskInfo taskinfo) {//####[60]####
        // ensure Method variable is set//####[60]####
        if (__pt__buildTask_Color_Color_method == null) {//####[60]####
            __pt__buildTask_Color_Color_ensureMethodVarSet();//####[60]####
        }//####[60]####
        taskinfo.setTaskIdArgIndexes(1);//####[60]####
        taskinfo.addDependsOn(colorRoof);//####[60]####
        taskinfo.setParameters(colorWalls, colorRoof);//####[60]####
        taskinfo.setMethod(__pt__buildTask_Color_Color_method);//####[60]####
        taskinfo.setInstance(this);//####[60]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[60]####
    }//####[60]####
    public TaskID<Void> buildTask(TaskID<Color> colorWalls, TaskID<Color> colorRoof) {//####[60]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[60]####
        return buildTask(colorWalls, colorRoof, new TaskInfo());//####[60]####
    }//####[60]####
    public TaskID<Void> buildTask(TaskID<Color> colorWalls, TaskID<Color> colorRoof, TaskInfo taskinfo) {//####[60]####
        // ensure Method variable is set//####[60]####
        if (__pt__buildTask_Color_Color_method == null) {//####[60]####
            __pt__buildTask_Color_Color_ensureMethodVarSet();//####[60]####
        }//####[60]####
        taskinfo.setTaskIdArgIndexes(0, 1);//####[60]####
        taskinfo.addDependsOn(colorWalls);//####[60]####
        taskinfo.addDependsOn(colorRoof);//####[60]####
        taskinfo.setParameters(colorWalls, colorRoof);//####[60]####
        taskinfo.setMethod(__pt__buildTask_Color_Color_method);//####[60]####
        taskinfo.setInstance(this);//####[60]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[60]####
    }//####[60]####
    public TaskID<Void> buildTask(BlockingQueue<Color> colorWalls, TaskID<Color> colorRoof) {//####[60]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[60]####
        return buildTask(colorWalls, colorRoof, new TaskInfo());//####[60]####
    }//####[60]####
    public TaskID<Void> buildTask(BlockingQueue<Color> colorWalls, TaskID<Color> colorRoof, TaskInfo taskinfo) {//####[60]####
        // ensure Method variable is set//####[60]####
        if (__pt__buildTask_Color_Color_method == null) {//####[60]####
            __pt__buildTask_Color_Color_ensureMethodVarSet();//####[60]####
        }//####[60]####
        taskinfo.setQueueArgIndexes(0);//####[60]####
        taskinfo.setIsPipeline(true);//####[60]####
        taskinfo.setTaskIdArgIndexes(1);//####[60]####
        taskinfo.addDependsOn(colorRoof);//####[60]####
        taskinfo.setParameters(colorWalls, colorRoof);//####[60]####
        taskinfo.setMethod(__pt__buildTask_Color_Color_method);//####[60]####
        taskinfo.setInstance(this);//####[60]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[60]####
    }//####[60]####
    public TaskID<Void> buildTask(Color colorWalls, BlockingQueue<Color> colorRoof) {//####[60]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[60]####
        return buildTask(colorWalls, colorRoof, new TaskInfo());//####[60]####
    }//####[60]####
    public TaskID<Void> buildTask(Color colorWalls, BlockingQueue<Color> colorRoof, TaskInfo taskinfo) {//####[60]####
        // ensure Method variable is set//####[60]####
        if (__pt__buildTask_Color_Color_method == null) {//####[60]####
            __pt__buildTask_Color_Color_ensureMethodVarSet();//####[60]####
        }//####[60]####
        taskinfo.setQueueArgIndexes(1);//####[60]####
        taskinfo.setIsPipeline(true);//####[60]####
        taskinfo.setParameters(colorWalls, colorRoof);//####[60]####
        taskinfo.setMethod(__pt__buildTask_Color_Color_method);//####[60]####
        taskinfo.setInstance(this);//####[60]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[60]####
    }//####[60]####
    public TaskID<Void> buildTask(TaskID<Color> colorWalls, BlockingQueue<Color> colorRoof) {//####[60]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[60]####
        return buildTask(colorWalls, colorRoof, new TaskInfo());//####[60]####
    }//####[60]####
    public TaskID<Void> buildTask(TaskID<Color> colorWalls, BlockingQueue<Color> colorRoof, TaskInfo taskinfo) {//####[60]####
        // ensure Method variable is set//####[60]####
        if (__pt__buildTask_Color_Color_method == null) {//####[60]####
            __pt__buildTask_Color_Color_ensureMethodVarSet();//####[60]####
        }//####[60]####
        taskinfo.setQueueArgIndexes(1);//####[60]####
        taskinfo.setIsPipeline(true);//####[60]####
        taskinfo.setTaskIdArgIndexes(0);//####[60]####
        taskinfo.addDependsOn(colorWalls);//####[60]####
        taskinfo.setParameters(colorWalls, colorRoof);//####[60]####
        taskinfo.setMethod(__pt__buildTask_Color_Color_method);//####[60]####
        taskinfo.setInstance(this);//####[60]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[60]####
    }//####[60]####
    public TaskID<Void> buildTask(BlockingQueue<Color> colorWalls, BlockingQueue<Color> colorRoof) {//####[60]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[60]####
        return buildTask(colorWalls, colorRoof, new TaskInfo());//####[60]####
    }//####[60]####
    public TaskID<Void> buildTask(BlockingQueue<Color> colorWalls, BlockingQueue<Color> colorRoof, TaskInfo taskinfo) {//####[60]####
        // ensure Method variable is set//####[60]####
        if (__pt__buildTask_Color_Color_method == null) {//####[60]####
            __pt__buildTask_Color_Color_ensureMethodVarSet();//####[60]####
        }//####[60]####
        taskinfo.setQueueArgIndexes(0, 1);//####[60]####
        taskinfo.setIsPipeline(true);//####[60]####
        taskinfo.setParameters(colorWalls, colorRoof);//####[60]####
        taskinfo.setMethod(__pt__buildTask_Color_Color_method);//####[60]####
        taskinfo.setInstance(this);//####[60]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[60]####
    }//####[60]####
    public void __pt__buildTask(Color colorWalls, Color colorRoof) {//####[60]####
        this.colorWalls = colorWalls;//####[61]####
        this.colorRoof = colorRoof;//####[62]####
        TaskID idFoundation = buildAllTask(new ConcurrentLinkedQueue<BuildingMaterial>(foundation));//####[64]####
        TaskInfo __pt__idWalls = new TaskInfo();//####[65]####
//####[65]####
        /*  -- ParaTask dependsOn clause for 'idWalls' -- *///####[65]####
        __pt__idWalls.addDependsOn(idFoundation);//####[65]####
//####[65]####
        TaskID idWalls = buildAllTask(new ConcurrentLinkedQueue<BuildingMaterial>(wallSiding), __pt__idWalls);//####[65]####
        TaskInfo __pt__idRoof = new TaskInfo();//####[66]####
//####[66]####
        /*  -- ParaTask dependsOn clause for 'idRoof' -- *///####[66]####
        __pt__idRoof.addDependsOn(idWalls);//####[66]####
//####[66]####
        TaskID idRoof = buildAllTask(new ConcurrentLinkedQueue<BuildingMaterial>(roofTiles), __pt__idRoof);//####[66]####
        TaskInfo __pt__idDoor = new TaskInfo();//####[67]####
//####[67]####
        /*  -- ParaTask dependsOn clause for 'idDoor' -- *///####[67]####
        __pt__idDoor.addDependsOn(idWalls);//####[67]####
//####[67]####
        TaskID idDoor = buildItemTask(door, __pt__idDoor);//####[67]####
        TaskInfo __pt__idWindows = new TaskInfo();//####[68]####
//####[68]####
        /*  -- ParaTask dependsOn clause for 'idWindows' -- *///####[68]####
        __pt__idWindows.addDependsOn(idWalls);//####[68]####
//####[68]####
        TaskID idWindows = buildAllTask(new ConcurrentLinkedQueue<BuildingMaterial>(windows), __pt__idWindows);//####[68]####
        TaskInfo __pt__idSign = new TaskInfo();//####[69]####
//####[69]####
        /*  -- ParaTask dependsOn clause for 'idSign' -- *///####[69]####
        __pt__idSign.addDependsOn(idRoof);//####[69]####
        __pt__idSign.addDependsOn(idDoor);//####[69]####
        __pt__idSign.addDependsOn(idWindows);//####[69]####
//####[69]####
        TaskID idSign = buildItemTask(forSaleSign, __pt__idSign);//####[69]####
        try {//####[71]####
            idSign.waitTillFinished();//####[72]####
        } catch (ExecutionException e) {//####[73]####
            e.printStackTrace();//####[74]####
        } catch (InterruptedException e) {//####[75]####
            e.printStackTrace();//####[76]####
        }//####[77]####
    }//####[78]####
//####[78]####
//####[80]####
    public void build(Color colorWalls, Color colorRoof) {//####[80]####
        this.colorWalls = colorWalls;//####[81]####
        this.colorRoof = colorRoof;//####[82]####
        buildAll(foundation);//####[84]####
        buildAll(wallSiding);//####[85]####
        buildAll(roofTiles);//####[86]####
        buildItem(door);//####[87]####
        buildAll(windows);//####[88]####
        buildItem(forSaleSign);//####[89]####
    }//####[90]####
//####[92]####
    private static volatile Method __pt__buildItemTask_BuildingMaterial_method = null;//####[92]####
    private synchronized static void __pt__buildItemTask_BuildingMaterial_ensureMethodVarSet() {//####[92]####
        if (__pt__buildItemTask_BuildingMaterial_method == null) {//####[92]####
            try {//####[92]####
                __pt__buildItemTask_BuildingMaterial_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__buildItemTask", new Class[] {//####[92]####
                    BuildingMaterial.class//####[92]####
                });//####[92]####
            } catch (Exception e) {//####[92]####
                e.printStackTrace();//####[92]####
            }//####[92]####
        }//####[92]####
    }//####[92]####
    private TaskID<Void> buildItemTask(BuildingMaterial item) {//####[92]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[92]####
        return buildItemTask(item, new TaskInfo());//####[92]####
    }//####[92]####
    private TaskID<Void> buildItemTask(BuildingMaterial item, TaskInfo taskinfo) {//####[92]####
        // ensure Method variable is set//####[92]####
        if (__pt__buildItemTask_BuildingMaterial_method == null) {//####[92]####
            __pt__buildItemTask_BuildingMaterial_ensureMethodVarSet();//####[92]####
        }//####[92]####
        taskinfo.setParameters(item);//####[92]####
        taskinfo.setMethod(__pt__buildItemTask_BuildingMaterial_method);//####[92]####
        taskinfo.setInstance(this);//####[92]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[92]####
    }//####[92]####
    private TaskID<Void> buildItemTask(TaskID<BuildingMaterial> item) {//####[92]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[92]####
        return buildItemTask(item, new TaskInfo());//####[92]####
    }//####[92]####
    private TaskID<Void> buildItemTask(TaskID<BuildingMaterial> item, TaskInfo taskinfo) {//####[92]####
        // ensure Method variable is set//####[92]####
        if (__pt__buildItemTask_BuildingMaterial_method == null) {//####[92]####
            __pt__buildItemTask_BuildingMaterial_ensureMethodVarSet();//####[92]####
        }//####[92]####
        taskinfo.setTaskIdArgIndexes(0);//####[92]####
        taskinfo.addDependsOn(item);//####[92]####
        taskinfo.setParameters(item);//####[92]####
        taskinfo.setMethod(__pt__buildItemTask_BuildingMaterial_method);//####[92]####
        taskinfo.setInstance(this);//####[92]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[92]####
    }//####[92]####
    private TaskID<Void> buildItemTask(BlockingQueue<BuildingMaterial> item) {//####[92]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[92]####
        return buildItemTask(item, new TaskInfo());//####[92]####
    }//####[92]####
    private TaskID<Void> buildItemTask(BlockingQueue<BuildingMaterial> item, TaskInfo taskinfo) {//####[92]####
        // ensure Method variable is set//####[92]####
        if (__pt__buildItemTask_BuildingMaterial_method == null) {//####[92]####
            __pt__buildItemTask_BuildingMaterial_ensureMethodVarSet();//####[92]####
        }//####[92]####
        taskinfo.setQueueArgIndexes(0);//####[92]####
        taskinfo.setIsPipeline(true);//####[92]####
        taskinfo.setParameters(item);//####[92]####
        taskinfo.setMethod(__pt__buildItemTask_BuildingMaterial_method);//####[92]####
        taskinfo.setInstance(this);//####[92]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[92]####
    }//####[92]####
    public void __pt__buildItemTask(BuildingMaterial item) {//####[92]####
        buildItem(item);//####[93]####
    }//####[94]####
//####[94]####
//####[96]####
    private void buildItem(BuildingMaterial item) {//####[96]####
        if (!item.isVisible()) //####[97]####
        {//####[97]####
            simulateWork(N);//####[98]####
            item.setVisible(true);//####[99]####
            repaint();//####[100]####
        }//####[101]####
    }//####[102]####
//####[104]####
    public void update(Graphics g) {//####[104]####
        paint(g);//####[105]####
    }//####[106]####
//####[108]####
    public void paint(Graphics g) {//####[108]####
        if (clearScreen) //####[110]####
        {//####[110]####
            g.clearRect(0, 0, width, height);//####[111]####
            clearScreen = false;//####[112]####
        }//####[113]####
        for (BuildingMaterial b : foundation) //####[116]####
        {//####[116]####
            if (b.isVisible()) //####[117]####
            {//####[117]####
                g.setColor(new Color(162, 158, 24));//####[118]####
                g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());//####[119]####
                g.setColor(Color.BLACK);//####[120]####
                g.drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());//####[121]####
            }//####[122]####
        }//####[123]####
        for (BuildingMaterial b : wallSiding) //####[126]####
        {//####[126]####
            if (b.isVisible()) //####[128]####
            {//####[128]####
                g.setColor(colorWalls);//####[129]####
                g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());//####[130]####
                g.setColor(Color.BLACK);//####[131]####
                g.drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());//####[132]####
            }//####[133]####
        }//####[134]####
        for (BuildingMaterial b : roofTiles) //####[137]####
        {//####[137]####
            if (b.isVisible()) //####[139]####
            {//####[139]####
                g.setColor(colorRoof);//####[140]####
                g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());//####[141]####
                g.setColor(Color.BLACK);//####[142]####
                g.drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());//####[143]####
            }//####[144]####
        }//####[145]####
        if (door.isVisible()) //####[148]####
        {//####[148]####
            g.setColor(new Color(152, 118, 84));//####[149]####
            g.fillRect(door.getX(), door.getY(), door.getWidth(), door.getHeight());//####[150]####
            g.setColor(Color.BLACK);//####[151]####
            g.drawRect(door.getX(), door.getY(), door.getWidth(), door.getHeight());//####[152]####
        }//####[153]####
        for (BuildingMaterial b : windows) //####[156]####
        {//####[156]####
            if (b.isVisible()) //####[157]####
            {//####[157]####
                g.setColor(new Color(173, 216, 230));//####[158]####
                g.fillRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());//####[159]####
                g.setColor(Color.BLACK);//####[160]####
                g.drawRect(b.getX(), b.getY(), b.getWidth(), b.getHeight());//####[161]####
            }//####[162]####
        }//####[163]####
        if (forSaleSign.isVisible()) //####[166]####
        {//####[166]####
            g.setColor(new Color(152, 118, 84));//####[167]####
            g.fillRect(400, 260, 15, 20);//####[168]####
            g.fillRect(445, 260, 15, 20);//####[169]####
            g.setColor(Color.BLACK);//####[170]####
            g.drawRect(400, 260, 15, 20);//####[171]####
            g.drawRect(445, 260, 15, 20);//####[172]####
            g.setColor(new Color(255, 239, 0));//####[174]####
            g.fillRect(forSaleSign.getX(), forSaleSign.getY(), forSaleSign.getWidth(), forSaleSign.getHeight());//####[175]####
            g.setColor(Color.BLACK);//####[176]####
            g.drawRect(forSaleSign.getX(), forSaleSign.getY(), forSaleSign.getWidth(), forSaleSign.getHeight());//####[177]####
            Font f = g.getFont();//####[179]####
            g.setFont(new Font(f.getName(), Font.BOLD, 21));//####[180]####
            g.drawString("For", forSaleSign.getX() + 30, forSaleSign.getY() + 30);//####[181]####
            g.drawString("Sale", forSaleSign.getX() + 25, forSaleSign.getY() + 60);//####[182]####
        }//####[183]####
    }//####[184]####
//####[186]####
    private void initFoundation() {//####[186]####
        int numCols = 31;//####[187]####
        int w = 15;//####[188]####
        int h = 10;//####[189]####
        int x = 20;//####[191]####
        int y = 280;//####[192]####
        for (int i = 0; i < numCols; i++) //####[193]####
        foundation.add(new BuildingMaterial(x + i * w, y, w, h, false));//####[194]####
        x -= 7;//####[196]####
        y += h;//####[197]####
        for (int i = 0; i < numCols; i++) //####[198]####
        foundation.add(new BuildingMaterial(x + i * w, y, w, h, false));//####[199]####
        x += 7;//####[201]####
        y += h;//####[202]####
        for (int i = 0; i < numCols; i++) //####[203]####
        foundation.add(new BuildingMaterial(x + i * w, y, w, h, false));//####[204]####
        Collections.shuffle(foundation);//####[206]####
    }//####[207]####
//####[209]####
    private void initWallSiding() {//####[209]####
        int numPlanks = 40;//####[210]####
        int totalW = 320;//####[211]####
        int w = totalW / numPlanks;//####[212]####
        int h = 150;//####[213]####
        int x = 40;//####[215]####
        int y = 130;//####[216]####
        for (int i = 0; i < numPlanks; i++) //####[217]####
        wallSiding.add(new BuildingMaterial(x + i * w, y, w, h, false));//####[218]####
        Collections.shuffle(wallSiding);//####[220]####
    }//####[221]####
//####[223]####
    private void initRoofTiles() {//####[223]####
        int numPlanks = 12;//####[224]####
        int dec = 10;//####[225]####
        int h = 10;//####[226]####
        int w = 360;//####[227]####
        int x = 20;//####[229]####
        int y = 130 - h;//####[230]####
        for (int i = 0; i < numPlanks; i++) //####[231]####
        {//####[231]####
            roofTiles.add(new BuildingMaterial(x + i * dec, y - i * h, w, h, false));//####[232]####
            w -= 2 * dec;//####[233]####
        }//####[234]####
        Collections.shuffle(roofTiles);//####[235]####
    }//####[236]####
//####[238]####
    private void initWindowsAndDoor() {//####[238]####
        door = new BuildingMaterial(63, 160, 75, 120, false);//####[239]####
        windows.add(new BuildingMaterial(160, 160, 80, 60, false));//####[241]####
        windows.add(new BuildingMaterial(253, 160, 75, 60, false));//####[242]####
    }//####[243]####
//####[245]####
    private void initForSaleSign() {//####[245]####
        forSaleSign = new BuildingMaterial(380, 180, 100, 80, false);//####[246]####
    }//####[247]####
//####[249]####
    public void setComputationLevel(int N) {//####[249]####
        this.N = N;//####[250]####
    }//####[251]####
//####[253]####
    public void reset() {//####[253]####
        clearScreen = true;//####[254]####
        for (BuildingMaterial b : foundation) //####[256]####
        {//####[256]####
            b.setVisible(false);//####[257]####
        }//####[258]####
        for (BuildingMaterial b : wallSiding) //####[260]####
        {//####[260]####
            b.setVisible(false);//####[261]####
        }//####[262]####
        for (BuildingMaterial b : roofTiles) //####[264]####
        {//####[264]####
            b.setVisible(false);//####[265]####
        }//####[266]####
        door.setVisible(false);//####[268]####
        for (BuildingMaterial b : windows) //####[270]####
        {//####[270]####
            b.setVisible(false);//####[271]####
        }//####[272]####
        forSaleSign.setVisible(false);//####[274]####
        repaint();//####[275]####
    }//####[276]####
//####[278]####
    private void simulateWork(int N) {//####[278]####
        double xmin = -1.0;//####[279]####
        double ymin = -1.0;//####[280]####
        double width = 2.0;//####[281]####
        double height = 2.0;//####[282]####
        for (int i = 0; i < N; i++) //####[283]####
        {//####[283]####
            for (int j = 0; j < N; j++) //####[284]####
            {//####[284]####
                double x = xmin + i * width / N;//####[285]####
                double y = ymin + j * height / N;//####[286]####
                Complex z = new Complex(x, y);//####[287]####
                newton(z);//####[288]####
            }//####[289]####
        }//####[290]####
    }//####[291]####
//####[293]####
    private Color newton(Complex z) {//####[293]####
        double EPSILON = 0.00000001;//####[294]####
        Complex four = new Complex(4, 0);//####[295]####
        Complex one = new Complex(1, 0);//####[296]####
        Complex root1 = new Complex(1, 0);//####[297]####
        Complex root2 = new Complex(-1, 0);//####[298]####
        Complex root3 = new Complex(0, 1);//####[299]####
        Complex root4 = new Complex(0, -1);//####[300]####
        for (int i = 0; i < 100; i++) //####[301]####
        {//####[301]####
            Complex f = z.times(z).times(z).times(z).minus(one);//####[302]####
            Complex fp = four.times(z).times(z).times(z);//####[303]####
            z = z.minus(f.divides(fp));//####[304]####
            if (z.minus(root1).abs() <= EPSILON) //####[305]####
            return Color.WHITE;//####[305]####
            if (z.minus(root2).abs() <= EPSILON) //####[306]####
            return Color.RED;//####[306]####
            if (z.minus(root3).abs() <= EPSILON) //####[307]####
            return Color.GREEN;//####[307]####
            if (z.minus(root4).abs() <= EPSILON) //####[308]####
            return Color.BLUE;//####[308]####
        }//####[309]####
        return Color.BLACK;//####[310]####
    }//####[311]####
}//####[311]####
