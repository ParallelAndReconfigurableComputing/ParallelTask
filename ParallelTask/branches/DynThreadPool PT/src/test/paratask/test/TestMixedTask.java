package paratask.test;//####[1]####
//####[1]####
import java.lang.reflect.InvocationTargetException;//####[3]####
import java.lang.reflect.Method;//####[4]####
import java.util.concurrent.ConcurrentLinkedQueue;//####[5]####
//####[5]####
//-- ParaTask related imports//####[5]####
import paratask.runtime.*;//####[5]####
import java.util.concurrent.ExecutionException;//####[5]####
import java.util.concurrent.locks.*;//####[5]####
import java.lang.reflect.*;//####[5]####
import paratask.runtime.GuiThread;//####[5]####
import java.util.concurrent.BlockingQueue;//####[5]####
import java.util.ArrayList;//####[5]####
import java.util.List;//####[5]####
//####[5]####
public class TestMixedTask {//####[7]####
    static{ParaTask.init();}//####[7]####
    /*  ParaTask helper method to access private/protected slots *///####[7]####
    public void __pt__accessPrivateSlot(Method m, Object instance, TaskID arg, Object interResult ) throws IllegalArgumentException, IllegalAccessException, InvocationTargetException {//####[7]####
        if (m.getParameterTypes().length == 0)//####[7]####
            m.invoke(instance);//####[7]####
        else if ((m.getParameterTypes().length == 1))//####[7]####
            m.invoke(instance, arg);//####[7]####
        else //####[7]####
            m.invoke(instance, arg, interResult);//####[7]####
    }//####[7]####
//####[8]####
    private static final int N_DATASIZE = 0;//####[8]####
//####[10]####
    private static final String BM_METHOD = "execute";//####[10]####
//####[12]####
    private static final Class<?>[] BM_METHOD_ARGUEMENT_TYPE = { int.class };//####[12]####
//####[14]####
    private static final String MOL = "MOL";//####[14]####
//####[16]####
    private static final String MOL_CLASS = "core.moldyn.Molcore";//####[16]####
//####[18]####
    private static final String MON = "MON";//####[18]####
//####[20]####
    private static final String MON_CLASS = "core.montecarlo.Moncore";//####[20]####
//####[22]####
    private static final String RAY = "RAY";//####[22]####
//####[24]####
    private static final String RAY_CLASS = "core.raytracer.Raycore";//####[24]####
//####[26]####
    private static ConcurrentLinkedQueue<Benchmark> concurrentLinkedQueue = null;//####[26]####
//####[32]####
    public static void main(String[] args) {//####[32]####
        if (null == args || args.length != 3) //####[33]####
        {//####[33]####
            try {//####[34]####
                throw new Exception("Wrong arguemnts setting");//####[35]####
            } catch (Exception e) {//####[36]####
                e.printStackTrace();//####[37]####
            }//####[38]####
        }//####[39]####
        int totalNum = Integer.valueOf(args[0]);//####[41]####
        TaskID[] taskIDs = new TaskID[totalNum];//####[43]####
        taskIDs[0] = runBMS_0(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[46]####
        taskIDs[1] = runBMS_1(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[47]####
        taskIDs[2] = runBM_2(createBenchmark(getBenchmarkClass(args[1])));//####[48]####
        taskIDs[3] = runBMS_3(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[49]####
        taskIDs[4] = runBMS_4(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[50]####
        taskIDs[5] = runBMS_5(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[51]####
        taskIDs[6] = runBM_6(createBenchmark(getBenchmarkClass(args[1])));//####[52]####
        taskIDs[7] = runBM_7(createBenchmark(getBenchmarkClass(args[1])));//####[53]####
        taskIDs[8] = runBMS_8(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[54]####
        taskIDs[9] = runBMS_9(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[55]####
        taskIDs[10] = runBM_10(createBenchmark(getBenchmarkClass(args[1])));//####[56]####
        taskIDs[11] = runBM_11(createBenchmark(getBenchmarkClass(args[1])));//####[57]####
        taskIDs[12] = runBM_12(createBenchmark(getBenchmarkClass(args[1])));//####[58]####
        taskIDs[13] = runBMS_13(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[59]####
        taskIDs[14] = runBMS_14(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[60]####
        taskIDs[15] = runBM_15(createBenchmark(getBenchmarkClass(args[1])));//####[61]####
        taskIDs[16] = runBM_16(createBenchmark(getBenchmarkClass(args[1])));//####[62]####
        taskIDs[17] = runBMS_17(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[63]####
        taskIDs[18] = runBMS_18(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[64]####
        taskIDs[19] = runBMS_19(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[65]####
        taskIDs[20] = runBM_20(createBenchmark(getBenchmarkClass(args[1])));//####[66]####
        taskIDs[21] = runBMS_21(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[67]####
        taskIDs[22] = runBMS_22(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[68]####
        taskIDs[23] = runBMS_23(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[69]####
        taskIDs[24] = runBM_24(createBenchmark(getBenchmarkClass(args[1])));//####[70]####
        taskIDs[25] = runBMS_25(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[71]####
        taskIDs[26] = runBMS_26(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[72]####
        taskIDs[27] = runBMS_27(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[73]####
        taskIDs[28] = runBM_28(createBenchmark(getBenchmarkClass(args[1])));//####[74]####
        taskIDs[29] = runBMS_29(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[75]####
        taskIDs[30] = runBM_30(createBenchmark(getBenchmarkClass(args[1])));//####[76]####
        taskIDs[31] = runBM_31(createBenchmark(getBenchmarkClass(args[1])));//####[77]####
        taskIDs[32] = runBMS_32(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[78]####
        taskIDs[33] = runBM_33(createBenchmark(getBenchmarkClass(args[1])));//####[79]####
        taskIDs[34] = runBM_34(createBenchmark(getBenchmarkClass(args[1])));//####[80]####
        taskIDs[35] = runBM_35(createBenchmark(getBenchmarkClass(args[1])));//####[81]####
        taskIDs[36] = runBM_36(createBenchmark(getBenchmarkClass(args[1])));//####[82]####
        taskIDs[37] = runBM_37(createBenchmark(getBenchmarkClass(args[1])));//####[83]####
        taskIDs[38] = runBMS_38(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[84]####
        taskIDs[39] = runBM_39(createBenchmark(getBenchmarkClass(args[1])));//####[85]####
        taskIDs[40] = runBM_40(createBenchmark(getBenchmarkClass(args[1])));//####[86]####
        taskIDs[41] = runBM_41(createBenchmark(getBenchmarkClass(args[1])));//####[87]####
        taskIDs[42] = runBM_42(createBenchmark(getBenchmarkClass(args[1])));//####[88]####
        taskIDs[43] = runBM_43(createBenchmark(getBenchmarkClass(args[1])));//####[89]####
        taskIDs[44] = runBM_44(createBenchmark(getBenchmarkClass(args[1])));//####[90]####
        taskIDs[45] = runBM_45(createBenchmark(getBenchmarkClass(args[1])));//####[91]####
        taskIDs[46] = runBM_46(createBenchmark(getBenchmarkClass(args[1])));//####[92]####
        taskIDs[47] = runBM_47(createBenchmark(getBenchmarkClass(args[1])));//####[93]####
        taskIDs[48] = runBM_48(createBenchmark(getBenchmarkClass(args[1])));//####[94]####
        taskIDs[49] = runBM_49(createBenchmark(getBenchmarkClass(args[1])));//####[95]####
        taskIDs[50] = runBMS_50(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[96]####
        taskIDs[51] = runBMS_51(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[97]####
        taskIDs[52] = runBM_52(createBenchmark(getBenchmarkClass(args[1])));//####[98]####
        taskIDs[53] = runBMS_53(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[99]####
        taskIDs[54] = runBM_54(createBenchmark(getBenchmarkClass(args[1])));//####[100]####
        taskIDs[55] = runBMS_55(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[101]####
        taskIDs[56] = runBM_56(createBenchmark(getBenchmarkClass(args[1])));//####[102]####
        taskIDs[57] = runBMS_57(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[103]####
        taskIDs[58] = runBMS_58(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[104]####
        taskIDs[59] = runBMS_59(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[105]####
        taskIDs[60] = runBMS_60(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[106]####
        taskIDs[61] = runBM_61(createBenchmark(getBenchmarkClass(args[1])));//####[107]####
        taskIDs[62] = runBM_62(createBenchmark(getBenchmarkClass(args[1])));//####[108]####
        taskIDs[63] = runBMS_63(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[109]####
        taskIDs[64] = runBMS_64(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[110]####
        taskIDs[65] = runBM_65(createBenchmark(getBenchmarkClass(args[1])));//####[111]####
        taskIDs[66] = runBM_66(createBenchmark(getBenchmarkClass(args[1])));//####[112]####
        taskIDs[67] = runBMS_67(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[113]####
        taskIDs[68] = runBM_68(createBenchmark(getBenchmarkClass(args[1])));//####[114]####
        taskIDs[69] = runBMS_69(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[115]####
        taskIDs[70] = runBMS_70(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[116]####
        taskIDs[71] = runBMS_71(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[117]####
        taskIDs[72] = runBM_72(createBenchmark(getBenchmarkClass(args[1])));//####[118]####
        taskIDs[73] = runBMS_73(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[119]####
        taskIDs[74] = runBMS_74(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[120]####
        taskIDs[75] = runBM_75(createBenchmark(getBenchmarkClass(args[1])));//####[121]####
        taskIDs[76] = runBM_76(createBenchmark(getBenchmarkClass(args[1])));//####[122]####
        taskIDs[77] = runBM_77(createBenchmark(getBenchmarkClass(args[1])));//####[123]####
        taskIDs[78] = runBMS_78(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[124]####
        taskIDs[79] = runBM_79(createBenchmark(getBenchmarkClass(args[1])));//####[125]####
        taskIDs[80] = runBM_80(createBenchmark(getBenchmarkClass(args[1])));//####[126]####
        taskIDs[81] = runBMS_81(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[127]####
        taskIDs[82] = runBMS_82(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[128]####
        taskIDs[83] = runBM_83(createBenchmark(getBenchmarkClass(args[1])));//####[129]####
        taskIDs[84] = runBM_84(createBenchmark(getBenchmarkClass(args[1])));//####[130]####
        taskIDs[85] = runBMS_85(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[131]####
        taskIDs[86] = runBMS_86(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[132]####
        taskIDs[87] = runBMS_87(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[133]####
        taskIDs[88] = runBMS_88(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[134]####
        taskIDs[89] = runBM_89(createBenchmark(getBenchmarkClass(args[1])));//####[135]####
        taskIDs[90] = runBM_90(createBenchmark(getBenchmarkClass(args[1])));//####[136]####
        taskIDs[91] = runBMS_91(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[137]####
        taskIDs[92] = runBMS_92(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[138]####
        taskIDs[93] = runBM_93(createBenchmark(getBenchmarkClass(args[1])));//####[139]####
        taskIDs[94] = runBM_94(createBenchmark(getBenchmarkClass(args[1])));//####[140]####
        taskIDs[95] = runBM_95(createBenchmark(getBenchmarkClass(args[1])));//####[141]####
        taskIDs[96] = runBMS_96(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[142]####
        taskIDs[97] = runBMS_97(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[143]####
        taskIDs[98] = runBMS_98(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[144]####
        taskIDs[99] = runBMS_99(createBenchmarkSet(getBenchmarkClass(args[1]), Integer.valueOf(args[2])));//####[145]####
        TaskIDGroup tig = new TaskIDGroup(totalNum);//####[146]####
        for (int i = 0; i < totalNum; i++) //####[148]####
        {//####[148]####
            tig.add(taskIDs[i]);//####[149]####
        }//####[150]####
        try {//####[152]####
            tig.waitTillFinished();//####[153]####
        } catch (ExecutionException e) {//####[154]####
            e.printStackTrace();//####[155]####
        } catch (InterruptedException e) {//####[156]####
            e.printStackTrace();//####[157]####
        }//####[158]####
    }//####[159]####
//####[161]####
    private static volatile Method __pt__runBMS_0_ConcurrentLinkedQueueBenchmark_method = null;//####[161]####
    private synchronized static void __pt__runBMS_0_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[161]####
        if (__pt__runBMS_0_ConcurrentLinkedQueueBenchmark_method == null) {//####[161]####
            try {//####[161]####
                __pt__runBMS_0_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_0", new Class[] {//####[161]####
                    ConcurrentLinkedQueue.class//####[161]####
                });//####[161]####
            } catch (Exception e) {//####[161]####
                e.printStackTrace();//####[161]####
            }//####[161]####
        }//####[161]####
    }//####[161]####
    private static TaskIDGroup<Void> runBMS_0(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[161]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[161]####
        return runBMS_0(benchmarkQueue, new TaskInfo());//####[161]####
    }//####[161]####
    private static TaskIDGroup<Void> runBMS_0(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[161]####
        // ensure Method variable is set//####[161]####
        if (__pt__runBMS_0_ConcurrentLinkedQueueBenchmark_method == null) {//####[161]####
            __pt__runBMS_0_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[161]####
        }//####[161]####
        taskinfo.setParameters(benchmarkQueue);//####[161]####
        taskinfo.setMethod(__pt__runBMS_0_ConcurrentLinkedQueueBenchmark_method);//####[161]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[161]####
    }//####[161]####
    private static TaskIDGroup<Void> runBMS_0(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[161]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[161]####
        return runBMS_0(benchmarkQueue, new TaskInfo());//####[161]####
    }//####[161]####
    private static TaskIDGroup<Void> runBMS_0(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[161]####
        // ensure Method variable is set//####[161]####
        if (__pt__runBMS_0_ConcurrentLinkedQueueBenchmark_method == null) {//####[161]####
            __pt__runBMS_0_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[161]####
        }//####[161]####
        taskinfo.setTaskIdArgIndexes(0);//####[161]####
        taskinfo.addDependsOn(benchmarkQueue);//####[161]####
        taskinfo.setParameters(benchmarkQueue);//####[161]####
        taskinfo.setMethod(__pt__runBMS_0_ConcurrentLinkedQueueBenchmark_method);//####[161]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[161]####
    }//####[161]####
    private static TaskIDGroup<Void> runBMS_0(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[161]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[161]####
        return runBMS_0(benchmarkQueue, new TaskInfo());//####[161]####
    }//####[161]####
    private static TaskIDGroup<Void> runBMS_0(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[161]####
        // ensure Method variable is set//####[161]####
        if (__pt__runBMS_0_ConcurrentLinkedQueueBenchmark_method == null) {//####[161]####
            __pt__runBMS_0_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[161]####
        }//####[161]####
        taskinfo.setQueueArgIndexes(0);//####[161]####
        taskinfo.setIsPipeline(true);//####[161]####
        taskinfo.setParameters(benchmarkQueue);//####[161]####
        taskinfo.setMethod(__pt__runBMS_0_ConcurrentLinkedQueueBenchmark_method);//####[161]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[161]####
    }//####[161]####
    public static void __pt__runBMS_0(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[161]####
        Benchmark benchmark = null;//####[161]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[161]####
        {//####[161]####
            runBM(benchmark);//####[161]####
        }//####[161]####
    }//####[161]####
//####[161]####
//####[162]####
    private static volatile Method __pt__runBMS_1_ConcurrentLinkedQueueBenchmark_method = null;//####[162]####
    private synchronized static void __pt__runBMS_1_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[162]####
        if (__pt__runBMS_1_ConcurrentLinkedQueueBenchmark_method == null) {//####[162]####
            try {//####[162]####
                __pt__runBMS_1_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_1", new Class[] {//####[162]####
                    ConcurrentLinkedQueue.class//####[162]####
                });//####[162]####
            } catch (Exception e) {//####[162]####
                e.printStackTrace();//####[162]####
            }//####[162]####
        }//####[162]####
    }//####[162]####
    private static TaskIDGroup<Void> runBMS_1(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[162]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[162]####
        return runBMS_1(benchmarkQueue, new TaskInfo());//####[162]####
    }//####[162]####
    private static TaskIDGroup<Void> runBMS_1(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[162]####
        // ensure Method variable is set//####[162]####
        if (__pt__runBMS_1_ConcurrentLinkedQueueBenchmark_method == null) {//####[162]####
            __pt__runBMS_1_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[162]####
        }//####[162]####
        taskinfo.setParameters(benchmarkQueue);//####[162]####
        taskinfo.setMethod(__pt__runBMS_1_ConcurrentLinkedQueueBenchmark_method);//####[162]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[162]####
    }//####[162]####
    private static TaskIDGroup<Void> runBMS_1(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[162]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[162]####
        return runBMS_1(benchmarkQueue, new TaskInfo());//####[162]####
    }//####[162]####
    private static TaskIDGroup<Void> runBMS_1(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[162]####
        // ensure Method variable is set//####[162]####
        if (__pt__runBMS_1_ConcurrentLinkedQueueBenchmark_method == null) {//####[162]####
            __pt__runBMS_1_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[162]####
        }//####[162]####
        taskinfo.setTaskIdArgIndexes(0);//####[162]####
        taskinfo.addDependsOn(benchmarkQueue);//####[162]####
        taskinfo.setParameters(benchmarkQueue);//####[162]####
        taskinfo.setMethod(__pt__runBMS_1_ConcurrentLinkedQueueBenchmark_method);//####[162]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[162]####
    }//####[162]####
    private static TaskIDGroup<Void> runBMS_1(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[162]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[162]####
        return runBMS_1(benchmarkQueue, new TaskInfo());//####[162]####
    }//####[162]####
    private static TaskIDGroup<Void> runBMS_1(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[162]####
        // ensure Method variable is set//####[162]####
        if (__pt__runBMS_1_ConcurrentLinkedQueueBenchmark_method == null) {//####[162]####
            __pt__runBMS_1_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[162]####
        }//####[162]####
        taskinfo.setQueueArgIndexes(0);//####[162]####
        taskinfo.setIsPipeline(true);//####[162]####
        taskinfo.setParameters(benchmarkQueue);//####[162]####
        taskinfo.setMethod(__pt__runBMS_1_ConcurrentLinkedQueueBenchmark_method);//####[162]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[162]####
    }//####[162]####
    public static void __pt__runBMS_1(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[162]####
        Benchmark benchmark = null;//####[162]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[162]####
        {//####[162]####
            runBM(benchmark);//####[162]####
        }//####[162]####
    }//####[162]####
//####[162]####
//####[163]####
    private static volatile Method __pt__runBMS_3_ConcurrentLinkedQueueBenchmark_method = null;//####[163]####
    private synchronized static void __pt__runBMS_3_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[163]####
        if (__pt__runBMS_3_ConcurrentLinkedQueueBenchmark_method == null) {//####[163]####
            try {//####[163]####
                __pt__runBMS_3_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_3", new Class[] {//####[163]####
                    ConcurrentLinkedQueue.class//####[163]####
                });//####[163]####
            } catch (Exception e) {//####[163]####
                e.printStackTrace();//####[163]####
            }//####[163]####
        }//####[163]####
    }//####[163]####
    private static TaskIDGroup<Void> runBMS_3(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[163]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[163]####
        return runBMS_3(benchmarkQueue, new TaskInfo());//####[163]####
    }//####[163]####
    private static TaskIDGroup<Void> runBMS_3(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[163]####
        // ensure Method variable is set//####[163]####
        if (__pt__runBMS_3_ConcurrentLinkedQueueBenchmark_method == null) {//####[163]####
            __pt__runBMS_3_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[163]####
        }//####[163]####
        taskinfo.setParameters(benchmarkQueue);//####[163]####
        taskinfo.setMethod(__pt__runBMS_3_ConcurrentLinkedQueueBenchmark_method);//####[163]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[163]####
    }//####[163]####
    private static TaskIDGroup<Void> runBMS_3(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[163]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[163]####
        return runBMS_3(benchmarkQueue, new TaskInfo());//####[163]####
    }//####[163]####
    private static TaskIDGroup<Void> runBMS_3(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[163]####
        // ensure Method variable is set//####[163]####
        if (__pt__runBMS_3_ConcurrentLinkedQueueBenchmark_method == null) {//####[163]####
            __pt__runBMS_3_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[163]####
        }//####[163]####
        taskinfo.setTaskIdArgIndexes(0);//####[163]####
        taskinfo.addDependsOn(benchmarkQueue);//####[163]####
        taskinfo.setParameters(benchmarkQueue);//####[163]####
        taskinfo.setMethod(__pt__runBMS_3_ConcurrentLinkedQueueBenchmark_method);//####[163]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[163]####
    }//####[163]####
    private static TaskIDGroup<Void> runBMS_3(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[163]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[163]####
        return runBMS_3(benchmarkQueue, new TaskInfo());//####[163]####
    }//####[163]####
    private static TaskIDGroup<Void> runBMS_3(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[163]####
        // ensure Method variable is set//####[163]####
        if (__pt__runBMS_3_ConcurrentLinkedQueueBenchmark_method == null) {//####[163]####
            __pt__runBMS_3_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[163]####
        }//####[163]####
        taskinfo.setQueueArgIndexes(0);//####[163]####
        taskinfo.setIsPipeline(true);//####[163]####
        taskinfo.setParameters(benchmarkQueue);//####[163]####
        taskinfo.setMethod(__pt__runBMS_3_ConcurrentLinkedQueueBenchmark_method);//####[163]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[163]####
    }//####[163]####
    public static void __pt__runBMS_3(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[163]####
        Benchmark benchmark = null;//####[163]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[163]####
        {//####[163]####
            runBM(benchmark);//####[163]####
        }//####[163]####
    }//####[163]####
//####[163]####
//####[164]####
    private static volatile Method __pt__runBMS_4_ConcurrentLinkedQueueBenchmark_method = null;//####[164]####
    private synchronized static void __pt__runBMS_4_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[164]####
        if (__pt__runBMS_4_ConcurrentLinkedQueueBenchmark_method == null) {//####[164]####
            try {//####[164]####
                __pt__runBMS_4_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_4", new Class[] {//####[164]####
                    ConcurrentLinkedQueue.class//####[164]####
                });//####[164]####
            } catch (Exception e) {//####[164]####
                e.printStackTrace();//####[164]####
            }//####[164]####
        }//####[164]####
    }//####[164]####
    private static TaskIDGroup<Void> runBMS_4(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[164]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[164]####
        return runBMS_4(benchmarkQueue, new TaskInfo());//####[164]####
    }//####[164]####
    private static TaskIDGroup<Void> runBMS_4(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[164]####
        // ensure Method variable is set//####[164]####
        if (__pt__runBMS_4_ConcurrentLinkedQueueBenchmark_method == null) {//####[164]####
            __pt__runBMS_4_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[164]####
        }//####[164]####
        taskinfo.setParameters(benchmarkQueue);//####[164]####
        taskinfo.setMethod(__pt__runBMS_4_ConcurrentLinkedQueueBenchmark_method);//####[164]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[164]####
    }//####[164]####
    private static TaskIDGroup<Void> runBMS_4(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[164]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[164]####
        return runBMS_4(benchmarkQueue, new TaskInfo());//####[164]####
    }//####[164]####
    private static TaskIDGroup<Void> runBMS_4(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[164]####
        // ensure Method variable is set//####[164]####
        if (__pt__runBMS_4_ConcurrentLinkedQueueBenchmark_method == null) {//####[164]####
            __pt__runBMS_4_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[164]####
        }//####[164]####
        taskinfo.setTaskIdArgIndexes(0);//####[164]####
        taskinfo.addDependsOn(benchmarkQueue);//####[164]####
        taskinfo.setParameters(benchmarkQueue);//####[164]####
        taskinfo.setMethod(__pt__runBMS_4_ConcurrentLinkedQueueBenchmark_method);//####[164]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[164]####
    }//####[164]####
    private static TaskIDGroup<Void> runBMS_4(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[164]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[164]####
        return runBMS_4(benchmarkQueue, new TaskInfo());//####[164]####
    }//####[164]####
    private static TaskIDGroup<Void> runBMS_4(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[164]####
        // ensure Method variable is set//####[164]####
        if (__pt__runBMS_4_ConcurrentLinkedQueueBenchmark_method == null) {//####[164]####
            __pt__runBMS_4_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[164]####
        }//####[164]####
        taskinfo.setQueueArgIndexes(0);//####[164]####
        taskinfo.setIsPipeline(true);//####[164]####
        taskinfo.setParameters(benchmarkQueue);//####[164]####
        taskinfo.setMethod(__pt__runBMS_4_ConcurrentLinkedQueueBenchmark_method);//####[164]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[164]####
    }//####[164]####
    public static void __pt__runBMS_4(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[164]####
        Benchmark benchmark = null;//####[164]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[164]####
        {//####[164]####
            runBM(benchmark);//####[164]####
        }//####[164]####
    }//####[164]####
//####[164]####
//####[165]####
    private static volatile Method __pt__runBMS_5_ConcurrentLinkedQueueBenchmark_method = null;//####[165]####
    private synchronized static void __pt__runBMS_5_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[165]####
        if (__pt__runBMS_5_ConcurrentLinkedQueueBenchmark_method == null) {//####[165]####
            try {//####[165]####
                __pt__runBMS_5_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_5", new Class[] {//####[165]####
                    ConcurrentLinkedQueue.class//####[165]####
                });//####[165]####
            } catch (Exception e) {//####[165]####
                e.printStackTrace();//####[165]####
            }//####[165]####
        }//####[165]####
    }//####[165]####
    private static TaskIDGroup<Void> runBMS_5(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[165]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[165]####
        return runBMS_5(benchmarkQueue, new TaskInfo());//####[165]####
    }//####[165]####
    private static TaskIDGroup<Void> runBMS_5(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[165]####
        // ensure Method variable is set//####[165]####
        if (__pt__runBMS_5_ConcurrentLinkedQueueBenchmark_method == null) {//####[165]####
            __pt__runBMS_5_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[165]####
        }//####[165]####
        taskinfo.setParameters(benchmarkQueue);//####[165]####
        taskinfo.setMethod(__pt__runBMS_5_ConcurrentLinkedQueueBenchmark_method);//####[165]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[165]####
    }//####[165]####
    private static TaskIDGroup<Void> runBMS_5(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[165]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[165]####
        return runBMS_5(benchmarkQueue, new TaskInfo());//####[165]####
    }//####[165]####
    private static TaskIDGroup<Void> runBMS_5(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[165]####
        // ensure Method variable is set//####[165]####
        if (__pt__runBMS_5_ConcurrentLinkedQueueBenchmark_method == null) {//####[165]####
            __pt__runBMS_5_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[165]####
        }//####[165]####
        taskinfo.setTaskIdArgIndexes(0);//####[165]####
        taskinfo.addDependsOn(benchmarkQueue);//####[165]####
        taskinfo.setParameters(benchmarkQueue);//####[165]####
        taskinfo.setMethod(__pt__runBMS_5_ConcurrentLinkedQueueBenchmark_method);//####[165]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[165]####
    }//####[165]####
    private static TaskIDGroup<Void> runBMS_5(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[165]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[165]####
        return runBMS_5(benchmarkQueue, new TaskInfo());//####[165]####
    }//####[165]####
    private static TaskIDGroup<Void> runBMS_5(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[165]####
        // ensure Method variable is set//####[165]####
        if (__pt__runBMS_5_ConcurrentLinkedQueueBenchmark_method == null) {//####[165]####
            __pt__runBMS_5_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[165]####
        }//####[165]####
        taskinfo.setQueueArgIndexes(0);//####[165]####
        taskinfo.setIsPipeline(true);//####[165]####
        taskinfo.setParameters(benchmarkQueue);//####[165]####
        taskinfo.setMethod(__pt__runBMS_5_ConcurrentLinkedQueueBenchmark_method);//####[165]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[165]####
    }//####[165]####
    public static void __pt__runBMS_5(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[165]####
        Benchmark benchmark = null;//####[165]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[165]####
        {//####[165]####
            runBM(benchmark);//####[165]####
        }//####[165]####
    }//####[165]####
//####[165]####
//####[166]####
    private static volatile Method __pt__runBMS_8_ConcurrentLinkedQueueBenchmark_method = null;//####[166]####
    private synchronized static void __pt__runBMS_8_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[166]####
        if (__pt__runBMS_8_ConcurrentLinkedQueueBenchmark_method == null) {//####[166]####
            try {//####[166]####
                __pt__runBMS_8_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_8", new Class[] {//####[166]####
                    ConcurrentLinkedQueue.class//####[166]####
                });//####[166]####
            } catch (Exception e) {//####[166]####
                e.printStackTrace();//####[166]####
            }//####[166]####
        }//####[166]####
    }//####[166]####
    private static TaskIDGroup<Void> runBMS_8(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[166]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[166]####
        return runBMS_8(benchmarkQueue, new TaskInfo());//####[166]####
    }//####[166]####
    private static TaskIDGroup<Void> runBMS_8(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[166]####
        // ensure Method variable is set//####[166]####
        if (__pt__runBMS_8_ConcurrentLinkedQueueBenchmark_method == null) {//####[166]####
            __pt__runBMS_8_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[166]####
        }//####[166]####
        taskinfo.setParameters(benchmarkQueue);//####[166]####
        taskinfo.setMethod(__pt__runBMS_8_ConcurrentLinkedQueueBenchmark_method);//####[166]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[166]####
    }//####[166]####
    private static TaskIDGroup<Void> runBMS_8(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[166]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[166]####
        return runBMS_8(benchmarkQueue, new TaskInfo());//####[166]####
    }//####[166]####
    private static TaskIDGroup<Void> runBMS_8(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[166]####
        // ensure Method variable is set//####[166]####
        if (__pt__runBMS_8_ConcurrentLinkedQueueBenchmark_method == null) {//####[166]####
            __pt__runBMS_8_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[166]####
        }//####[166]####
        taskinfo.setTaskIdArgIndexes(0);//####[166]####
        taskinfo.addDependsOn(benchmarkQueue);//####[166]####
        taskinfo.setParameters(benchmarkQueue);//####[166]####
        taskinfo.setMethod(__pt__runBMS_8_ConcurrentLinkedQueueBenchmark_method);//####[166]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[166]####
    }//####[166]####
    private static TaskIDGroup<Void> runBMS_8(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[166]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[166]####
        return runBMS_8(benchmarkQueue, new TaskInfo());//####[166]####
    }//####[166]####
    private static TaskIDGroup<Void> runBMS_8(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[166]####
        // ensure Method variable is set//####[166]####
        if (__pt__runBMS_8_ConcurrentLinkedQueueBenchmark_method == null) {//####[166]####
            __pt__runBMS_8_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[166]####
        }//####[166]####
        taskinfo.setQueueArgIndexes(0);//####[166]####
        taskinfo.setIsPipeline(true);//####[166]####
        taskinfo.setParameters(benchmarkQueue);//####[166]####
        taskinfo.setMethod(__pt__runBMS_8_ConcurrentLinkedQueueBenchmark_method);//####[166]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[166]####
    }//####[166]####
    public static void __pt__runBMS_8(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[166]####
        Benchmark benchmark = null;//####[166]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[166]####
        {//####[166]####
            runBM(benchmark);//####[166]####
        }//####[166]####
    }//####[166]####
//####[166]####
//####[167]####
    private static volatile Method __pt__runBMS_9_ConcurrentLinkedQueueBenchmark_method = null;//####[167]####
    private synchronized static void __pt__runBMS_9_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[167]####
        if (__pt__runBMS_9_ConcurrentLinkedQueueBenchmark_method == null) {//####[167]####
            try {//####[167]####
                __pt__runBMS_9_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_9", new Class[] {//####[167]####
                    ConcurrentLinkedQueue.class//####[167]####
                });//####[167]####
            } catch (Exception e) {//####[167]####
                e.printStackTrace();//####[167]####
            }//####[167]####
        }//####[167]####
    }//####[167]####
    private static TaskIDGroup<Void> runBMS_9(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[167]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[167]####
        return runBMS_9(benchmarkQueue, new TaskInfo());//####[167]####
    }//####[167]####
    private static TaskIDGroup<Void> runBMS_9(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[167]####
        // ensure Method variable is set//####[167]####
        if (__pt__runBMS_9_ConcurrentLinkedQueueBenchmark_method == null) {//####[167]####
            __pt__runBMS_9_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[167]####
        }//####[167]####
        taskinfo.setParameters(benchmarkQueue);//####[167]####
        taskinfo.setMethod(__pt__runBMS_9_ConcurrentLinkedQueueBenchmark_method);//####[167]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[167]####
    }//####[167]####
    private static TaskIDGroup<Void> runBMS_9(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[167]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[167]####
        return runBMS_9(benchmarkQueue, new TaskInfo());//####[167]####
    }//####[167]####
    private static TaskIDGroup<Void> runBMS_9(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[167]####
        // ensure Method variable is set//####[167]####
        if (__pt__runBMS_9_ConcurrentLinkedQueueBenchmark_method == null) {//####[167]####
            __pt__runBMS_9_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[167]####
        }//####[167]####
        taskinfo.setTaskIdArgIndexes(0);//####[167]####
        taskinfo.addDependsOn(benchmarkQueue);//####[167]####
        taskinfo.setParameters(benchmarkQueue);//####[167]####
        taskinfo.setMethod(__pt__runBMS_9_ConcurrentLinkedQueueBenchmark_method);//####[167]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[167]####
    }//####[167]####
    private static TaskIDGroup<Void> runBMS_9(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[167]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[167]####
        return runBMS_9(benchmarkQueue, new TaskInfo());//####[167]####
    }//####[167]####
    private static TaskIDGroup<Void> runBMS_9(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[167]####
        // ensure Method variable is set//####[167]####
        if (__pt__runBMS_9_ConcurrentLinkedQueueBenchmark_method == null) {//####[167]####
            __pt__runBMS_9_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[167]####
        }//####[167]####
        taskinfo.setQueueArgIndexes(0);//####[167]####
        taskinfo.setIsPipeline(true);//####[167]####
        taskinfo.setParameters(benchmarkQueue);//####[167]####
        taskinfo.setMethod(__pt__runBMS_9_ConcurrentLinkedQueueBenchmark_method);//####[167]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[167]####
    }//####[167]####
    public static void __pt__runBMS_9(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[167]####
        Benchmark benchmark = null;//####[167]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[167]####
        {//####[167]####
            runBM(benchmark);//####[167]####
        }//####[167]####
    }//####[167]####
//####[167]####
//####[168]####
    private static volatile Method __pt__runBMS_13_ConcurrentLinkedQueueBenchmark_method = null;//####[168]####
    private synchronized static void __pt__runBMS_13_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[168]####
        if (__pt__runBMS_13_ConcurrentLinkedQueueBenchmark_method == null) {//####[168]####
            try {//####[168]####
                __pt__runBMS_13_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_13", new Class[] {//####[168]####
                    ConcurrentLinkedQueue.class//####[168]####
                });//####[168]####
            } catch (Exception e) {//####[168]####
                e.printStackTrace();//####[168]####
            }//####[168]####
        }//####[168]####
    }//####[168]####
    private static TaskIDGroup<Void> runBMS_13(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[168]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[168]####
        return runBMS_13(benchmarkQueue, new TaskInfo());//####[168]####
    }//####[168]####
    private static TaskIDGroup<Void> runBMS_13(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[168]####
        // ensure Method variable is set//####[168]####
        if (__pt__runBMS_13_ConcurrentLinkedQueueBenchmark_method == null) {//####[168]####
            __pt__runBMS_13_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[168]####
        }//####[168]####
        taskinfo.setParameters(benchmarkQueue);//####[168]####
        taskinfo.setMethod(__pt__runBMS_13_ConcurrentLinkedQueueBenchmark_method);//####[168]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[168]####
    }//####[168]####
    private static TaskIDGroup<Void> runBMS_13(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[168]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[168]####
        return runBMS_13(benchmarkQueue, new TaskInfo());//####[168]####
    }//####[168]####
    private static TaskIDGroup<Void> runBMS_13(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[168]####
        // ensure Method variable is set//####[168]####
        if (__pt__runBMS_13_ConcurrentLinkedQueueBenchmark_method == null) {//####[168]####
            __pt__runBMS_13_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[168]####
        }//####[168]####
        taskinfo.setTaskIdArgIndexes(0);//####[168]####
        taskinfo.addDependsOn(benchmarkQueue);//####[168]####
        taskinfo.setParameters(benchmarkQueue);//####[168]####
        taskinfo.setMethod(__pt__runBMS_13_ConcurrentLinkedQueueBenchmark_method);//####[168]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[168]####
    }//####[168]####
    private static TaskIDGroup<Void> runBMS_13(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[168]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[168]####
        return runBMS_13(benchmarkQueue, new TaskInfo());//####[168]####
    }//####[168]####
    private static TaskIDGroup<Void> runBMS_13(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[168]####
        // ensure Method variable is set//####[168]####
        if (__pt__runBMS_13_ConcurrentLinkedQueueBenchmark_method == null) {//####[168]####
            __pt__runBMS_13_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[168]####
        }//####[168]####
        taskinfo.setQueueArgIndexes(0);//####[168]####
        taskinfo.setIsPipeline(true);//####[168]####
        taskinfo.setParameters(benchmarkQueue);//####[168]####
        taskinfo.setMethod(__pt__runBMS_13_ConcurrentLinkedQueueBenchmark_method);//####[168]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[168]####
    }//####[168]####
    public static void __pt__runBMS_13(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[168]####
        Benchmark benchmark = null;//####[168]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[168]####
        {//####[168]####
            runBM(benchmark);//####[168]####
        }//####[168]####
    }//####[168]####
//####[168]####
//####[169]####
    private static volatile Method __pt__runBMS_14_ConcurrentLinkedQueueBenchmark_method = null;//####[169]####
    private synchronized static void __pt__runBMS_14_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[169]####
        if (__pt__runBMS_14_ConcurrentLinkedQueueBenchmark_method == null) {//####[169]####
            try {//####[169]####
                __pt__runBMS_14_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_14", new Class[] {//####[169]####
                    ConcurrentLinkedQueue.class//####[169]####
                });//####[169]####
            } catch (Exception e) {//####[169]####
                e.printStackTrace();//####[169]####
            }//####[169]####
        }//####[169]####
    }//####[169]####
    private static TaskIDGroup<Void> runBMS_14(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[169]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[169]####
        return runBMS_14(benchmarkQueue, new TaskInfo());//####[169]####
    }//####[169]####
    private static TaskIDGroup<Void> runBMS_14(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[169]####
        // ensure Method variable is set//####[169]####
        if (__pt__runBMS_14_ConcurrentLinkedQueueBenchmark_method == null) {//####[169]####
            __pt__runBMS_14_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[169]####
        }//####[169]####
        taskinfo.setParameters(benchmarkQueue);//####[169]####
        taskinfo.setMethod(__pt__runBMS_14_ConcurrentLinkedQueueBenchmark_method);//####[169]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[169]####
    }//####[169]####
    private static TaskIDGroup<Void> runBMS_14(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[169]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[169]####
        return runBMS_14(benchmarkQueue, new TaskInfo());//####[169]####
    }//####[169]####
    private static TaskIDGroup<Void> runBMS_14(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[169]####
        // ensure Method variable is set//####[169]####
        if (__pt__runBMS_14_ConcurrentLinkedQueueBenchmark_method == null) {//####[169]####
            __pt__runBMS_14_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[169]####
        }//####[169]####
        taskinfo.setTaskIdArgIndexes(0);//####[169]####
        taskinfo.addDependsOn(benchmarkQueue);//####[169]####
        taskinfo.setParameters(benchmarkQueue);//####[169]####
        taskinfo.setMethod(__pt__runBMS_14_ConcurrentLinkedQueueBenchmark_method);//####[169]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[169]####
    }//####[169]####
    private static TaskIDGroup<Void> runBMS_14(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[169]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[169]####
        return runBMS_14(benchmarkQueue, new TaskInfo());//####[169]####
    }//####[169]####
    private static TaskIDGroup<Void> runBMS_14(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[169]####
        // ensure Method variable is set//####[169]####
        if (__pt__runBMS_14_ConcurrentLinkedQueueBenchmark_method == null) {//####[169]####
            __pt__runBMS_14_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[169]####
        }//####[169]####
        taskinfo.setQueueArgIndexes(0);//####[169]####
        taskinfo.setIsPipeline(true);//####[169]####
        taskinfo.setParameters(benchmarkQueue);//####[169]####
        taskinfo.setMethod(__pt__runBMS_14_ConcurrentLinkedQueueBenchmark_method);//####[169]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[169]####
    }//####[169]####
    public static void __pt__runBMS_14(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[169]####
        Benchmark benchmark = null;//####[169]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[169]####
        {//####[169]####
            runBM(benchmark);//####[169]####
        }//####[169]####
    }//####[169]####
//####[169]####
//####[170]####
    private static volatile Method __pt__runBMS_17_ConcurrentLinkedQueueBenchmark_method = null;//####[170]####
    private synchronized static void __pt__runBMS_17_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[170]####
        if (__pt__runBMS_17_ConcurrentLinkedQueueBenchmark_method == null) {//####[170]####
            try {//####[170]####
                __pt__runBMS_17_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_17", new Class[] {//####[170]####
                    ConcurrentLinkedQueue.class//####[170]####
                });//####[170]####
            } catch (Exception e) {//####[170]####
                e.printStackTrace();//####[170]####
            }//####[170]####
        }//####[170]####
    }//####[170]####
    private static TaskIDGroup<Void> runBMS_17(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[170]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[170]####
        return runBMS_17(benchmarkQueue, new TaskInfo());//####[170]####
    }//####[170]####
    private static TaskIDGroup<Void> runBMS_17(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[170]####
        // ensure Method variable is set//####[170]####
        if (__pt__runBMS_17_ConcurrentLinkedQueueBenchmark_method == null) {//####[170]####
            __pt__runBMS_17_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[170]####
        }//####[170]####
        taskinfo.setParameters(benchmarkQueue);//####[170]####
        taskinfo.setMethod(__pt__runBMS_17_ConcurrentLinkedQueueBenchmark_method);//####[170]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[170]####
    }//####[170]####
    private static TaskIDGroup<Void> runBMS_17(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[170]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[170]####
        return runBMS_17(benchmarkQueue, new TaskInfo());//####[170]####
    }//####[170]####
    private static TaskIDGroup<Void> runBMS_17(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[170]####
        // ensure Method variable is set//####[170]####
        if (__pt__runBMS_17_ConcurrentLinkedQueueBenchmark_method == null) {//####[170]####
            __pt__runBMS_17_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[170]####
        }//####[170]####
        taskinfo.setTaskIdArgIndexes(0);//####[170]####
        taskinfo.addDependsOn(benchmarkQueue);//####[170]####
        taskinfo.setParameters(benchmarkQueue);//####[170]####
        taskinfo.setMethod(__pt__runBMS_17_ConcurrentLinkedQueueBenchmark_method);//####[170]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[170]####
    }//####[170]####
    private static TaskIDGroup<Void> runBMS_17(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[170]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[170]####
        return runBMS_17(benchmarkQueue, new TaskInfo());//####[170]####
    }//####[170]####
    private static TaskIDGroup<Void> runBMS_17(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[170]####
        // ensure Method variable is set//####[170]####
        if (__pt__runBMS_17_ConcurrentLinkedQueueBenchmark_method == null) {//####[170]####
            __pt__runBMS_17_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[170]####
        }//####[170]####
        taskinfo.setQueueArgIndexes(0);//####[170]####
        taskinfo.setIsPipeline(true);//####[170]####
        taskinfo.setParameters(benchmarkQueue);//####[170]####
        taskinfo.setMethod(__pt__runBMS_17_ConcurrentLinkedQueueBenchmark_method);//####[170]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[170]####
    }//####[170]####
    public static void __pt__runBMS_17(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[170]####
        Benchmark benchmark = null;//####[170]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[170]####
        {//####[170]####
            runBM(benchmark);//####[170]####
        }//####[170]####
    }//####[170]####
//####[170]####
//####[171]####
    private static volatile Method __pt__runBMS_18_ConcurrentLinkedQueueBenchmark_method = null;//####[171]####
    private synchronized static void __pt__runBMS_18_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[171]####
        if (__pt__runBMS_18_ConcurrentLinkedQueueBenchmark_method == null) {//####[171]####
            try {//####[171]####
                __pt__runBMS_18_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_18", new Class[] {//####[171]####
                    ConcurrentLinkedQueue.class//####[171]####
                });//####[171]####
            } catch (Exception e) {//####[171]####
                e.printStackTrace();//####[171]####
            }//####[171]####
        }//####[171]####
    }//####[171]####
    private static TaskIDGroup<Void> runBMS_18(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[171]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[171]####
        return runBMS_18(benchmarkQueue, new TaskInfo());//####[171]####
    }//####[171]####
    private static TaskIDGroup<Void> runBMS_18(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[171]####
        // ensure Method variable is set//####[171]####
        if (__pt__runBMS_18_ConcurrentLinkedQueueBenchmark_method == null) {//####[171]####
            __pt__runBMS_18_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[171]####
        }//####[171]####
        taskinfo.setParameters(benchmarkQueue);//####[171]####
        taskinfo.setMethod(__pt__runBMS_18_ConcurrentLinkedQueueBenchmark_method);//####[171]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[171]####
    }//####[171]####
    private static TaskIDGroup<Void> runBMS_18(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[171]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[171]####
        return runBMS_18(benchmarkQueue, new TaskInfo());//####[171]####
    }//####[171]####
    private static TaskIDGroup<Void> runBMS_18(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[171]####
        // ensure Method variable is set//####[171]####
        if (__pt__runBMS_18_ConcurrentLinkedQueueBenchmark_method == null) {//####[171]####
            __pt__runBMS_18_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[171]####
        }//####[171]####
        taskinfo.setTaskIdArgIndexes(0);//####[171]####
        taskinfo.addDependsOn(benchmarkQueue);//####[171]####
        taskinfo.setParameters(benchmarkQueue);//####[171]####
        taskinfo.setMethod(__pt__runBMS_18_ConcurrentLinkedQueueBenchmark_method);//####[171]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[171]####
    }//####[171]####
    private static TaskIDGroup<Void> runBMS_18(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[171]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[171]####
        return runBMS_18(benchmarkQueue, new TaskInfo());//####[171]####
    }//####[171]####
    private static TaskIDGroup<Void> runBMS_18(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[171]####
        // ensure Method variable is set//####[171]####
        if (__pt__runBMS_18_ConcurrentLinkedQueueBenchmark_method == null) {//####[171]####
            __pt__runBMS_18_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[171]####
        }//####[171]####
        taskinfo.setQueueArgIndexes(0);//####[171]####
        taskinfo.setIsPipeline(true);//####[171]####
        taskinfo.setParameters(benchmarkQueue);//####[171]####
        taskinfo.setMethod(__pt__runBMS_18_ConcurrentLinkedQueueBenchmark_method);//####[171]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[171]####
    }//####[171]####
    public static void __pt__runBMS_18(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[171]####
        Benchmark benchmark = null;//####[171]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[171]####
        {//####[171]####
            runBM(benchmark);//####[171]####
        }//####[171]####
    }//####[171]####
//####[171]####
//####[172]####
    private static volatile Method __pt__runBMS_19_ConcurrentLinkedQueueBenchmark_method = null;//####[172]####
    private synchronized static void __pt__runBMS_19_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[172]####
        if (__pt__runBMS_19_ConcurrentLinkedQueueBenchmark_method == null) {//####[172]####
            try {//####[172]####
                __pt__runBMS_19_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_19", new Class[] {//####[172]####
                    ConcurrentLinkedQueue.class//####[172]####
                });//####[172]####
            } catch (Exception e) {//####[172]####
                e.printStackTrace();//####[172]####
            }//####[172]####
        }//####[172]####
    }//####[172]####
    private static TaskIDGroup<Void> runBMS_19(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[172]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[172]####
        return runBMS_19(benchmarkQueue, new TaskInfo());//####[172]####
    }//####[172]####
    private static TaskIDGroup<Void> runBMS_19(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[172]####
        // ensure Method variable is set//####[172]####
        if (__pt__runBMS_19_ConcurrentLinkedQueueBenchmark_method == null) {//####[172]####
            __pt__runBMS_19_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[172]####
        }//####[172]####
        taskinfo.setParameters(benchmarkQueue);//####[172]####
        taskinfo.setMethod(__pt__runBMS_19_ConcurrentLinkedQueueBenchmark_method);//####[172]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[172]####
    }//####[172]####
    private static TaskIDGroup<Void> runBMS_19(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[172]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[172]####
        return runBMS_19(benchmarkQueue, new TaskInfo());//####[172]####
    }//####[172]####
    private static TaskIDGroup<Void> runBMS_19(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[172]####
        // ensure Method variable is set//####[172]####
        if (__pt__runBMS_19_ConcurrentLinkedQueueBenchmark_method == null) {//####[172]####
            __pt__runBMS_19_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[172]####
        }//####[172]####
        taskinfo.setTaskIdArgIndexes(0);//####[172]####
        taskinfo.addDependsOn(benchmarkQueue);//####[172]####
        taskinfo.setParameters(benchmarkQueue);//####[172]####
        taskinfo.setMethod(__pt__runBMS_19_ConcurrentLinkedQueueBenchmark_method);//####[172]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[172]####
    }//####[172]####
    private static TaskIDGroup<Void> runBMS_19(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[172]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[172]####
        return runBMS_19(benchmarkQueue, new TaskInfo());//####[172]####
    }//####[172]####
    private static TaskIDGroup<Void> runBMS_19(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[172]####
        // ensure Method variable is set//####[172]####
        if (__pt__runBMS_19_ConcurrentLinkedQueueBenchmark_method == null) {//####[172]####
            __pt__runBMS_19_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[172]####
        }//####[172]####
        taskinfo.setQueueArgIndexes(0);//####[172]####
        taskinfo.setIsPipeline(true);//####[172]####
        taskinfo.setParameters(benchmarkQueue);//####[172]####
        taskinfo.setMethod(__pt__runBMS_19_ConcurrentLinkedQueueBenchmark_method);//####[172]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[172]####
    }//####[172]####
    public static void __pt__runBMS_19(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[172]####
        Benchmark benchmark = null;//####[172]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[172]####
        {//####[172]####
            runBM(benchmark);//####[172]####
        }//####[172]####
    }//####[172]####
//####[172]####
//####[173]####
    private static volatile Method __pt__runBMS_21_ConcurrentLinkedQueueBenchmark_method = null;//####[173]####
    private synchronized static void __pt__runBMS_21_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[173]####
        if (__pt__runBMS_21_ConcurrentLinkedQueueBenchmark_method == null) {//####[173]####
            try {//####[173]####
                __pt__runBMS_21_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_21", new Class[] {//####[173]####
                    ConcurrentLinkedQueue.class//####[173]####
                });//####[173]####
            } catch (Exception e) {//####[173]####
                e.printStackTrace();//####[173]####
            }//####[173]####
        }//####[173]####
    }//####[173]####
    private static TaskIDGroup<Void> runBMS_21(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[173]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[173]####
        return runBMS_21(benchmarkQueue, new TaskInfo());//####[173]####
    }//####[173]####
    private static TaskIDGroup<Void> runBMS_21(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[173]####
        // ensure Method variable is set//####[173]####
        if (__pt__runBMS_21_ConcurrentLinkedQueueBenchmark_method == null) {//####[173]####
            __pt__runBMS_21_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[173]####
        }//####[173]####
        taskinfo.setParameters(benchmarkQueue);//####[173]####
        taskinfo.setMethod(__pt__runBMS_21_ConcurrentLinkedQueueBenchmark_method);//####[173]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[173]####
    }//####[173]####
    private static TaskIDGroup<Void> runBMS_21(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[173]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[173]####
        return runBMS_21(benchmarkQueue, new TaskInfo());//####[173]####
    }//####[173]####
    private static TaskIDGroup<Void> runBMS_21(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[173]####
        // ensure Method variable is set//####[173]####
        if (__pt__runBMS_21_ConcurrentLinkedQueueBenchmark_method == null) {//####[173]####
            __pt__runBMS_21_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[173]####
        }//####[173]####
        taskinfo.setTaskIdArgIndexes(0);//####[173]####
        taskinfo.addDependsOn(benchmarkQueue);//####[173]####
        taskinfo.setParameters(benchmarkQueue);//####[173]####
        taskinfo.setMethod(__pt__runBMS_21_ConcurrentLinkedQueueBenchmark_method);//####[173]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[173]####
    }//####[173]####
    private static TaskIDGroup<Void> runBMS_21(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[173]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[173]####
        return runBMS_21(benchmarkQueue, new TaskInfo());//####[173]####
    }//####[173]####
    private static TaskIDGroup<Void> runBMS_21(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[173]####
        // ensure Method variable is set//####[173]####
        if (__pt__runBMS_21_ConcurrentLinkedQueueBenchmark_method == null) {//####[173]####
            __pt__runBMS_21_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[173]####
        }//####[173]####
        taskinfo.setQueueArgIndexes(0);//####[173]####
        taskinfo.setIsPipeline(true);//####[173]####
        taskinfo.setParameters(benchmarkQueue);//####[173]####
        taskinfo.setMethod(__pt__runBMS_21_ConcurrentLinkedQueueBenchmark_method);//####[173]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[173]####
    }//####[173]####
    public static void __pt__runBMS_21(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[173]####
        Benchmark benchmark = null;//####[173]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[173]####
        {//####[173]####
            runBM(benchmark);//####[173]####
        }//####[173]####
    }//####[173]####
//####[173]####
//####[174]####
    private static volatile Method __pt__runBMS_22_ConcurrentLinkedQueueBenchmark_method = null;//####[174]####
    private synchronized static void __pt__runBMS_22_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[174]####
        if (__pt__runBMS_22_ConcurrentLinkedQueueBenchmark_method == null) {//####[174]####
            try {//####[174]####
                __pt__runBMS_22_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_22", new Class[] {//####[174]####
                    ConcurrentLinkedQueue.class//####[174]####
                });//####[174]####
            } catch (Exception e) {//####[174]####
                e.printStackTrace();//####[174]####
            }//####[174]####
        }//####[174]####
    }//####[174]####
    private static TaskIDGroup<Void> runBMS_22(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[174]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[174]####
        return runBMS_22(benchmarkQueue, new TaskInfo());//####[174]####
    }//####[174]####
    private static TaskIDGroup<Void> runBMS_22(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[174]####
        // ensure Method variable is set//####[174]####
        if (__pt__runBMS_22_ConcurrentLinkedQueueBenchmark_method == null) {//####[174]####
            __pt__runBMS_22_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[174]####
        }//####[174]####
        taskinfo.setParameters(benchmarkQueue);//####[174]####
        taskinfo.setMethod(__pt__runBMS_22_ConcurrentLinkedQueueBenchmark_method);//####[174]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[174]####
    }//####[174]####
    private static TaskIDGroup<Void> runBMS_22(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[174]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[174]####
        return runBMS_22(benchmarkQueue, new TaskInfo());//####[174]####
    }//####[174]####
    private static TaskIDGroup<Void> runBMS_22(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[174]####
        // ensure Method variable is set//####[174]####
        if (__pt__runBMS_22_ConcurrentLinkedQueueBenchmark_method == null) {//####[174]####
            __pt__runBMS_22_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[174]####
        }//####[174]####
        taskinfo.setTaskIdArgIndexes(0);//####[174]####
        taskinfo.addDependsOn(benchmarkQueue);//####[174]####
        taskinfo.setParameters(benchmarkQueue);//####[174]####
        taskinfo.setMethod(__pt__runBMS_22_ConcurrentLinkedQueueBenchmark_method);//####[174]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[174]####
    }//####[174]####
    private static TaskIDGroup<Void> runBMS_22(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[174]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[174]####
        return runBMS_22(benchmarkQueue, new TaskInfo());//####[174]####
    }//####[174]####
    private static TaskIDGroup<Void> runBMS_22(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[174]####
        // ensure Method variable is set//####[174]####
        if (__pt__runBMS_22_ConcurrentLinkedQueueBenchmark_method == null) {//####[174]####
            __pt__runBMS_22_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[174]####
        }//####[174]####
        taskinfo.setQueueArgIndexes(0);//####[174]####
        taskinfo.setIsPipeline(true);//####[174]####
        taskinfo.setParameters(benchmarkQueue);//####[174]####
        taskinfo.setMethod(__pt__runBMS_22_ConcurrentLinkedQueueBenchmark_method);//####[174]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[174]####
    }//####[174]####
    public static void __pt__runBMS_22(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[174]####
        Benchmark benchmark = null;//####[174]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[174]####
        {//####[174]####
            runBM(benchmark);//####[174]####
        }//####[174]####
    }//####[174]####
//####[174]####
//####[175]####
    private static volatile Method __pt__runBMS_23_ConcurrentLinkedQueueBenchmark_method = null;//####[175]####
    private synchronized static void __pt__runBMS_23_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[175]####
        if (__pt__runBMS_23_ConcurrentLinkedQueueBenchmark_method == null) {//####[175]####
            try {//####[175]####
                __pt__runBMS_23_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_23", new Class[] {//####[175]####
                    ConcurrentLinkedQueue.class//####[175]####
                });//####[175]####
            } catch (Exception e) {//####[175]####
                e.printStackTrace();//####[175]####
            }//####[175]####
        }//####[175]####
    }//####[175]####
    private static TaskIDGroup<Void> runBMS_23(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[175]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[175]####
        return runBMS_23(benchmarkQueue, new TaskInfo());//####[175]####
    }//####[175]####
    private static TaskIDGroup<Void> runBMS_23(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[175]####
        // ensure Method variable is set//####[175]####
        if (__pt__runBMS_23_ConcurrentLinkedQueueBenchmark_method == null) {//####[175]####
            __pt__runBMS_23_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[175]####
        }//####[175]####
        taskinfo.setParameters(benchmarkQueue);//####[175]####
        taskinfo.setMethod(__pt__runBMS_23_ConcurrentLinkedQueueBenchmark_method);//####[175]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[175]####
    }//####[175]####
    private static TaskIDGroup<Void> runBMS_23(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[175]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[175]####
        return runBMS_23(benchmarkQueue, new TaskInfo());//####[175]####
    }//####[175]####
    private static TaskIDGroup<Void> runBMS_23(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[175]####
        // ensure Method variable is set//####[175]####
        if (__pt__runBMS_23_ConcurrentLinkedQueueBenchmark_method == null) {//####[175]####
            __pt__runBMS_23_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[175]####
        }//####[175]####
        taskinfo.setTaskIdArgIndexes(0);//####[175]####
        taskinfo.addDependsOn(benchmarkQueue);//####[175]####
        taskinfo.setParameters(benchmarkQueue);//####[175]####
        taskinfo.setMethod(__pt__runBMS_23_ConcurrentLinkedQueueBenchmark_method);//####[175]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[175]####
    }//####[175]####
    private static TaskIDGroup<Void> runBMS_23(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[175]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[175]####
        return runBMS_23(benchmarkQueue, new TaskInfo());//####[175]####
    }//####[175]####
    private static TaskIDGroup<Void> runBMS_23(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[175]####
        // ensure Method variable is set//####[175]####
        if (__pt__runBMS_23_ConcurrentLinkedQueueBenchmark_method == null) {//####[175]####
            __pt__runBMS_23_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[175]####
        }//####[175]####
        taskinfo.setQueueArgIndexes(0);//####[175]####
        taskinfo.setIsPipeline(true);//####[175]####
        taskinfo.setParameters(benchmarkQueue);//####[175]####
        taskinfo.setMethod(__pt__runBMS_23_ConcurrentLinkedQueueBenchmark_method);//####[175]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[175]####
    }//####[175]####
    public static void __pt__runBMS_23(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[175]####
        Benchmark benchmark = null;//####[175]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[175]####
        {//####[175]####
            runBM(benchmark);//####[175]####
        }//####[175]####
    }//####[175]####
//####[175]####
//####[176]####
    private static volatile Method __pt__runBMS_25_ConcurrentLinkedQueueBenchmark_method = null;//####[176]####
    private synchronized static void __pt__runBMS_25_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[176]####
        if (__pt__runBMS_25_ConcurrentLinkedQueueBenchmark_method == null) {//####[176]####
            try {//####[176]####
                __pt__runBMS_25_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_25", new Class[] {//####[176]####
                    ConcurrentLinkedQueue.class//####[176]####
                });//####[176]####
            } catch (Exception e) {//####[176]####
                e.printStackTrace();//####[176]####
            }//####[176]####
        }//####[176]####
    }//####[176]####
    private static TaskIDGroup<Void> runBMS_25(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[176]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[176]####
        return runBMS_25(benchmarkQueue, new TaskInfo());//####[176]####
    }//####[176]####
    private static TaskIDGroup<Void> runBMS_25(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[176]####
        // ensure Method variable is set//####[176]####
        if (__pt__runBMS_25_ConcurrentLinkedQueueBenchmark_method == null) {//####[176]####
            __pt__runBMS_25_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[176]####
        }//####[176]####
        taskinfo.setParameters(benchmarkQueue);//####[176]####
        taskinfo.setMethod(__pt__runBMS_25_ConcurrentLinkedQueueBenchmark_method);//####[176]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[176]####
    }//####[176]####
    private static TaskIDGroup<Void> runBMS_25(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[176]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[176]####
        return runBMS_25(benchmarkQueue, new TaskInfo());//####[176]####
    }//####[176]####
    private static TaskIDGroup<Void> runBMS_25(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[176]####
        // ensure Method variable is set//####[176]####
        if (__pt__runBMS_25_ConcurrentLinkedQueueBenchmark_method == null) {//####[176]####
            __pt__runBMS_25_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[176]####
        }//####[176]####
        taskinfo.setTaskIdArgIndexes(0);//####[176]####
        taskinfo.addDependsOn(benchmarkQueue);//####[176]####
        taskinfo.setParameters(benchmarkQueue);//####[176]####
        taskinfo.setMethod(__pt__runBMS_25_ConcurrentLinkedQueueBenchmark_method);//####[176]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[176]####
    }//####[176]####
    private static TaskIDGroup<Void> runBMS_25(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[176]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[176]####
        return runBMS_25(benchmarkQueue, new TaskInfo());//####[176]####
    }//####[176]####
    private static TaskIDGroup<Void> runBMS_25(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[176]####
        // ensure Method variable is set//####[176]####
        if (__pt__runBMS_25_ConcurrentLinkedQueueBenchmark_method == null) {//####[176]####
            __pt__runBMS_25_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[176]####
        }//####[176]####
        taskinfo.setQueueArgIndexes(0);//####[176]####
        taskinfo.setIsPipeline(true);//####[176]####
        taskinfo.setParameters(benchmarkQueue);//####[176]####
        taskinfo.setMethod(__pt__runBMS_25_ConcurrentLinkedQueueBenchmark_method);//####[176]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[176]####
    }//####[176]####
    public static void __pt__runBMS_25(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[176]####
        Benchmark benchmark = null;//####[176]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[176]####
        {//####[176]####
            runBM(benchmark);//####[176]####
        }//####[176]####
    }//####[176]####
//####[176]####
//####[177]####
    private static volatile Method __pt__runBMS_26_ConcurrentLinkedQueueBenchmark_method = null;//####[177]####
    private synchronized static void __pt__runBMS_26_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[177]####
        if (__pt__runBMS_26_ConcurrentLinkedQueueBenchmark_method == null) {//####[177]####
            try {//####[177]####
                __pt__runBMS_26_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_26", new Class[] {//####[177]####
                    ConcurrentLinkedQueue.class//####[177]####
                });//####[177]####
            } catch (Exception e) {//####[177]####
                e.printStackTrace();//####[177]####
            }//####[177]####
        }//####[177]####
    }//####[177]####
    private static TaskIDGroup<Void> runBMS_26(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[177]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[177]####
        return runBMS_26(benchmarkQueue, new TaskInfo());//####[177]####
    }//####[177]####
    private static TaskIDGroup<Void> runBMS_26(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[177]####
        // ensure Method variable is set//####[177]####
        if (__pt__runBMS_26_ConcurrentLinkedQueueBenchmark_method == null) {//####[177]####
            __pt__runBMS_26_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[177]####
        }//####[177]####
        taskinfo.setParameters(benchmarkQueue);//####[177]####
        taskinfo.setMethod(__pt__runBMS_26_ConcurrentLinkedQueueBenchmark_method);//####[177]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[177]####
    }//####[177]####
    private static TaskIDGroup<Void> runBMS_26(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[177]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[177]####
        return runBMS_26(benchmarkQueue, new TaskInfo());//####[177]####
    }//####[177]####
    private static TaskIDGroup<Void> runBMS_26(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[177]####
        // ensure Method variable is set//####[177]####
        if (__pt__runBMS_26_ConcurrentLinkedQueueBenchmark_method == null) {//####[177]####
            __pt__runBMS_26_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[177]####
        }//####[177]####
        taskinfo.setTaskIdArgIndexes(0);//####[177]####
        taskinfo.addDependsOn(benchmarkQueue);//####[177]####
        taskinfo.setParameters(benchmarkQueue);//####[177]####
        taskinfo.setMethod(__pt__runBMS_26_ConcurrentLinkedQueueBenchmark_method);//####[177]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[177]####
    }//####[177]####
    private static TaskIDGroup<Void> runBMS_26(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[177]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[177]####
        return runBMS_26(benchmarkQueue, new TaskInfo());//####[177]####
    }//####[177]####
    private static TaskIDGroup<Void> runBMS_26(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[177]####
        // ensure Method variable is set//####[177]####
        if (__pt__runBMS_26_ConcurrentLinkedQueueBenchmark_method == null) {//####[177]####
            __pt__runBMS_26_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[177]####
        }//####[177]####
        taskinfo.setQueueArgIndexes(0);//####[177]####
        taskinfo.setIsPipeline(true);//####[177]####
        taskinfo.setParameters(benchmarkQueue);//####[177]####
        taskinfo.setMethod(__pt__runBMS_26_ConcurrentLinkedQueueBenchmark_method);//####[177]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[177]####
    }//####[177]####
    public static void __pt__runBMS_26(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[177]####
        Benchmark benchmark = null;//####[177]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[177]####
        {//####[177]####
            runBM(benchmark);//####[177]####
        }//####[177]####
    }//####[177]####
//####[177]####
//####[178]####
    private static volatile Method __pt__runBMS_27_ConcurrentLinkedQueueBenchmark_method = null;//####[178]####
    private synchronized static void __pt__runBMS_27_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[178]####
        if (__pt__runBMS_27_ConcurrentLinkedQueueBenchmark_method == null) {//####[178]####
            try {//####[178]####
                __pt__runBMS_27_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_27", new Class[] {//####[178]####
                    ConcurrentLinkedQueue.class//####[178]####
                });//####[178]####
            } catch (Exception e) {//####[178]####
                e.printStackTrace();//####[178]####
            }//####[178]####
        }//####[178]####
    }//####[178]####
    private static TaskIDGroup<Void> runBMS_27(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[178]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[178]####
        return runBMS_27(benchmarkQueue, new TaskInfo());//####[178]####
    }//####[178]####
    private static TaskIDGroup<Void> runBMS_27(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[178]####
        // ensure Method variable is set//####[178]####
        if (__pt__runBMS_27_ConcurrentLinkedQueueBenchmark_method == null) {//####[178]####
            __pt__runBMS_27_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[178]####
        }//####[178]####
        taskinfo.setParameters(benchmarkQueue);//####[178]####
        taskinfo.setMethod(__pt__runBMS_27_ConcurrentLinkedQueueBenchmark_method);//####[178]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[178]####
    }//####[178]####
    private static TaskIDGroup<Void> runBMS_27(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[178]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[178]####
        return runBMS_27(benchmarkQueue, new TaskInfo());//####[178]####
    }//####[178]####
    private static TaskIDGroup<Void> runBMS_27(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[178]####
        // ensure Method variable is set//####[178]####
        if (__pt__runBMS_27_ConcurrentLinkedQueueBenchmark_method == null) {//####[178]####
            __pt__runBMS_27_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[178]####
        }//####[178]####
        taskinfo.setTaskIdArgIndexes(0);//####[178]####
        taskinfo.addDependsOn(benchmarkQueue);//####[178]####
        taskinfo.setParameters(benchmarkQueue);//####[178]####
        taskinfo.setMethod(__pt__runBMS_27_ConcurrentLinkedQueueBenchmark_method);//####[178]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[178]####
    }//####[178]####
    private static TaskIDGroup<Void> runBMS_27(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[178]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[178]####
        return runBMS_27(benchmarkQueue, new TaskInfo());//####[178]####
    }//####[178]####
    private static TaskIDGroup<Void> runBMS_27(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[178]####
        // ensure Method variable is set//####[178]####
        if (__pt__runBMS_27_ConcurrentLinkedQueueBenchmark_method == null) {//####[178]####
            __pt__runBMS_27_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[178]####
        }//####[178]####
        taskinfo.setQueueArgIndexes(0);//####[178]####
        taskinfo.setIsPipeline(true);//####[178]####
        taskinfo.setParameters(benchmarkQueue);//####[178]####
        taskinfo.setMethod(__pt__runBMS_27_ConcurrentLinkedQueueBenchmark_method);//####[178]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[178]####
    }//####[178]####
    public static void __pt__runBMS_27(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[178]####
        Benchmark benchmark = null;//####[178]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[178]####
        {//####[178]####
            runBM(benchmark);//####[178]####
        }//####[178]####
    }//####[178]####
//####[178]####
//####[179]####
    private static volatile Method __pt__runBMS_29_ConcurrentLinkedQueueBenchmark_method = null;//####[179]####
    private synchronized static void __pt__runBMS_29_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[179]####
        if (__pt__runBMS_29_ConcurrentLinkedQueueBenchmark_method == null) {//####[179]####
            try {//####[179]####
                __pt__runBMS_29_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_29", new Class[] {//####[179]####
                    ConcurrentLinkedQueue.class//####[179]####
                });//####[179]####
            } catch (Exception e) {//####[179]####
                e.printStackTrace();//####[179]####
            }//####[179]####
        }//####[179]####
    }//####[179]####
    private static TaskIDGroup<Void> runBMS_29(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[179]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[179]####
        return runBMS_29(benchmarkQueue, new TaskInfo());//####[179]####
    }//####[179]####
    private static TaskIDGroup<Void> runBMS_29(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[179]####
        // ensure Method variable is set//####[179]####
        if (__pt__runBMS_29_ConcurrentLinkedQueueBenchmark_method == null) {//####[179]####
            __pt__runBMS_29_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[179]####
        }//####[179]####
        taskinfo.setParameters(benchmarkQueue);//####[179]####
        taskinfo.setMethod(__pt__runBMS_29_ConcurrentLinkedQueueBenchmark_method);//####[179]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[179]####
    }//####[179]####
    private static TaskIDGroup<Void> runBMS_29(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[179]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[179]####
        return runBMS_29(benchmarkQueue, new TaskInfo());//####[179]####
    }//####[179]####
    private static TaskIDGroup<Void> runBMS_29(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[179]####
        // ensure Method variable is set//####[179]####
        if (__pt__runBMS_29_ConcurrentLinkedQueueBenchmark_method == null) {//####[179]####
            __pt__runBMS_29_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[179]####
        }//####[179]####
        taskinfo.setTaskIdArgIndexes(0);//####[179]####
        taskinfo.addDependsOn(benchmarkQueue);//####[179]####
        taskinfo.setParameters(benchmarkQueue);//####[179]####
        taskinfo.setMethod(__pt__runBMS_29_ConcurrentLinkedQueueBenchmark_method);//####[179]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[179]####
    }//####[179]####
    private static TaskIDGroup<Void> runBMS_29(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[179]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[179]####
        return runBMS_29(benchmarkQueue, new TaskInfo());//####[179]####
    }//####[179]####
    private static TaskIDGroup<Void> runBMS_29(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[179]####
        // ensure Method variable is set//####[179]####
        if (__pt__runBMS_29_ConcurrentLinkedQueueBenchmark_method == null) {//####[179]####
            __pt__runBMS_29_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[179]####
        }//####[179]####
        taskinfo.setQueueArgIndexes(0);//####[179]####
        taskinfo.setIsPipeline(true);//####[179]####
        taskinfo.setParameters(benchmarkQueue);//####[179]####
        taskinfo.setMethod(__pt__runBMS_29_ConcurrentLinkedQueueBenchmark_method);//####[179]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[179]####
    }//####[179]####
    public static void __pt__runBMS_29(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[179]####
        Benchmark benchmark = null;//####[179]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[179]####
        {//####[179]####
            runBM(benchmark);//####[179]####
        }//####[179]####
    }//####[179]####
//####[179]####
//####[180]####
    private static volatile Method __pt__runBMS_32_ConcurrentLinkedQueueBenchmark_method = null;//####[180]####
    private synchronized static void __pt__runBMS_32_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[180]####
        if (__pt__runBMS_32_ConcurrentLinkedQueueBenchmark_method == null) {//####[180]####
            try {//####[180]####
                __pt__runBMS_32_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_32", new Class[] {//####[180]####
                    ConcurrentLinkedQueue.class//####[180]####
                });//####[180]####
            } catch (Exception e) {//####[180]####
                e.printStackTrace();//####[180]####
            }//####[180]####
        }//####[180]####
    }//####[180]####
    private static TaskIDGroup<Void> runBMS_32(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[180]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[180]####
        return runBMS_32(benchmarkQueue, new TaskInfo());//####[180]####
    }//####[180]####
    private static TaskIDGroup<Void> runBMS_32(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[180]####
        // ensure Method variable is set//####[180]####
        if (__pt__runBMS_32_ConcurrentLinkedQueueBenchmark_method == null) {//####[180]####
            __pt__runBMS_32_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[180]####
        }//####[180]####
        taskinfo.setParameters(benchmarkQueue);//####[180]####
        taskinfo.setMethod(__pt__runBMS_32_ConcurrentLinkedQueueBenchmark_method);//####[180]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[180]####
    }//####[180]####
    private static TaskIDGroup<Void> runBMS_32(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[180]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[180]####
        return runBMS_32(benchmarkQueue, new TaskInfo());//####[180]####
    }//####[180]####
    private static TaskIDGroup<Void> runBMS_32(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[180]####
        // ensure Method variable is set//####[180]####
        if (__pt__runBMS_32_ConcurrentLinkedQueueBenchmark_method == null) {//####[180]####
            __pt__runBMS_32_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[180]####
        }//####[180]####
        taskinfo.setTaskIdArgIndexes(0);//####[180]####
        taskinfo.addDependsOn(benchmarkQueue);//####[180]####
        taskinfo.setParameters(benchmarkQueue);//####[180]####
        taskinfo.setMethod(__pt__runBMS_32_ConcurrentLinkedQueueBenchmark_method);//####[180]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[180]####
    }//####[180]####
    private static TaskIDGroup<Void> runBMS_32(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[180]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[180]####
        return runBMS_32(benchmarkQueue, new TaskInfo());//####[180]####
    }//####[180]####
    private static TaskIDGroup<Void> runBMS_32(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[180]####
        // ensure Method variable is set//####[180]####
        if (__pt__runBMS_32_ConcurrentLinkedQueueBenchmark_method == null) {//####[180]####
            __pt__runBMS_32_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[180]####
        }//####[180]####
        taskinfo.setQueueArgIndexes(0);//####[180]####
        taskinfo.setIsPipeline(true);//####[180]####
        taskinfo.setParameters(benchmarkQueue);//####[180]####
        taskinfo.setMethod(__pt__runBMS_32_ConcurrentLinkedQueueBenchmark_method);//####[180]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[180]####
    }//####[180]####
    public static void __pt__runBMS_32(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[180]####
        Benchmark benchmark = null;//####[180]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[180]####
        {//####[180]####
            runBM(benchmark);//####[180]####
        }//####[180]####
    }//####[180]####
//####[180]####
//####[181]####
    private static volatile Method __pt__runBMS_38_ConcurrentLinkedQueueBenchmark_method = null;//####[181]####
    private synchronized static void __pt__runBMS_38_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[181]####
        if (__pt__runBMS_38_ConcurrentLinkedQueueBenchmark_method == null) {//####[181]####
            try {//####[181]####
                __pt__runBMS_38_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_38", new Class[] {//####[181]####
                    ConcurrentLinkedQueue.class//####[181]####
                });//####[181]####
            } catch (Exception e) {//####[181]####
                e.printStackTrace();//####[181]####
            }//####[181]####
        }//####[181]####
    }//####[181]####
    private static TaskIDGroup<Void> runBMS_38(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[181]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[181]####
        return runBMS_38(benchmarkQueue, new TaskInfo());//####[181]####
    }//####[181]####
    private static TaskIDGroup<Void> runBMS_38(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[181]####
        // ensure Method variable is set//####[181]####
        if (__pt__runBMS_38_ConcurrentLinkedQueueBenchmark_method == null) {//####[181]####
            __pt__runBMS_38_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[181]####
        }//####[181]####
        taskinfo.setParameters(benchmarkQueue);//####[181]####
        taskinfo.setMethod(__pt__runBMS_38_ConcurrentLinkedQueueBenchmark_method);//####[181]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[181]####
    }//####[181]####
    private static TaskIDGroup<Void> runBMS_38(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[181]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[181]####
        return runBMS_38(benchmarkQueue, new TaskInfo());//####[181]####
    }//####[181]####
    private static TaskIDGroup<Void> runBMS_38(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[181]####
        // ensure Method variable is set//####[181]####
        if (__pt__runBMS_38_ConcurrentLinkedQueueBenchmark_method == null) {//####[181]####
            __pt__runBMS_38_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[181]####
        }//####[181]####
        taskinfo.setTaskIdArgIndexes(0);//####[181]####
        taskinfo.addDependsOn(benchmarkQueue);//####[181]####
        taskinfo.setParameters(benchmarkQueue);//####[181]####
        taskinfo.setMethod(__pt__runBMS_38_ConcurrentLinkedQueueBenchmark_method);//####[181]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[181]####
    }//####[181]####
    private static TaskIDGroup<Void> runBMS_38(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[181]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[181]####
        return runBMS_38(benchmarkQueue, new TaskInfo());//####[181]####
    }//####[181]####
    private static TaskIDGroup<Void> runBMS_38(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[181]####
        // ensure Method variable is set//####[181]####
        if (__pt__runBMS_38_ConcurrentLinkedQueueBenchmark_method == null) {//####[181]####
            __pt__runBMS_38_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[181]####
        }//####[181]####
        taskinfo.setQueueArgIndexes(0);//####[181]####
        taskinfo.setIsPipeline(true);//####[181]####
        taskinfo.setParameters(benchmarkQueue);//####[181]####
        taskinfo.setMethod(__pt__runBMS_38_ConcurrentLinkedQueueBenchmark_method);//####[181]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[181]####
    }//####[181]####
    public static void __pt__runBMS_38(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[181]####
        Benchmark benchmark = null;//####[181]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[181]####
        {//####[181]####
            runBM(benchmark);//####[181]####
        }//####[181]####
    }//####[181]####
//####[181]####
//####[182]####
    private static volatile Method __pt__runBMS_50_ConcurrentLinkedQueueBenchmark_method = null;//####[182]####
    private synchronized static void __pt__runBMS_50_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[182]####
        if (__pt__runBMS_50_ConcurrentLinkedQueueBenchmark_method == null) {//####[182]####
            try {//####[182]####
                __pt__runBMS_50_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_50", new Class[] {//####[182]####
                    ConcurrentLinkedQueue.class//####[182]####
                });//####[182]####
            } catch (Exception e) {//####[182]####
                e.printStackTrace();//####[182]####
            }//####[182]####
        }//####[182]####
    }//####[182]####
    private static TaskIDGroup<Void> runBMS_50(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[182]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[182]####
        return runBMS_50(benchmarkQueue, new TaskInfo());//####[182]####
    }//####[182]####
    private static TaskIDGroup<Void> runBMS_50(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[182]####
        // ensure Method variable is set//####[182]####
        if (__pt__runBMS_50_ConcurrentLinkedQueueBenchmark_method == null) {//####[182]####
            __pt__runBMS_50_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[182]####
        }//####[182]####
        taskinfo.setParameters(benchmarkQueue);//####[182]####
        taskinfo.setMethod(__pt__runBMS_50_ConcurrentLinkedQueueBenchmark_method);//####[182]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[182]####
    }//####[182]####
    private static TaskIDGroup<Void> runBMS_50(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[182]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[182]####
        return runBMS_50(benchmarkQueue, new TaskInfo());//####[182]####
    }//####[182]####
    private static TaskIDGroup<Void> runBMS_50(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[182]####
        // ensure Method variable is set//####[182]####
        if (__pt__runBMS_50_ConcurrentLinkedQueueBenchmark_method == null) {//####[182]####
            __pt__runBMS_50_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[182]####
        }//####[182]####
        taskinfo.setTaskIdArgIndexes(0);//####[182]####
        taskinfo.addDependsOn(benchmarkQueue);//####[182]####
        taskinfo.setParameters(benchmarkQueue);//####[182]####
        taskinfo.setMethod(__pt__runBMS_50_ConcurrentLinkedQueueBenchmark_method);//####[182]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[182]####
    }//####[182]####
    private static TaskIDGroup<Void> runBMS_50(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[182]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[182]####
        return runBMS_50(benchmarkQueue, new TaskInfo());//####[182]####
    }//####[182]####
    private static TaskIDGroup<Void> runBMS_50(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[182]####
        // ensure Method variable is set//####[182]####
        if (__pt__runBMS_50_ConcurrentLinkedQueueBenchmark_method == null) {//####[182]####
            __pt__runBMS_50_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[182]####
        }//####[182]####
        taskinfo.setQueueArgIndexes(0);//####[182]####
        taskinfo.setIsPipeline(true);//####[182]####
        taskinfo.setParameters(benchmarkQueue);//####[182]####
        taskinfo.setMethod(__pt__runBMS_50_ConcurrentLinkedQueueBenchmark_method);//####[182]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[182]####
    }//####[182]####
    public static void __pt__runBMS_50(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[182]####
        Benchmark benchmark = null;//####[182]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[182]####
        {//####[182]####
            runBM(benchmark);//####[182]####
        }//####[182]####
    }//####[182]####
//####[182]####
//####[183]####
    private static volatile Method __pt__runBMS_51_ConcurrentLinkedQueueBenchmark_method = null;//####[183]####
    private synchronized static void __pt__runBMS_51_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[183]####
        if (__pt__runBMS_51_ConcurrentLinkedQueueBenchmark_method == null) {//####[183]####
            try {//####[183]####
                __pt__runBMS_51_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_51", new Class[] {//####[183]####
                    ConcurrentLinkedQueue.class//####[183]####
                });//####[183]####
            } catch (Exception e) {//####[183]####
                e.printStackTrace();//####[183]####
            }//####[183]####
        }//####[183]####
    }//####[183]####
    private static TaskIDGroup<Void> runBMS_51(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[183]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[183]####
        return runBMS_51(benchmarkQueue, new TaskInfo());//####[183]####
    }//####[183]####
    private static TaskIDGroup<Void> runBMS_51(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[183]####
        // ensure Method variable is set//####[183]####
        if (__pt__runBMS_51_ConcurrentLinkedQueueBenchmark_method == null) {//####[183]####
            __pt__runBMS_51_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[183]####
        }//####[183]####
        taskinfo.setParameters(benchmarkQueue);//####[183]####
        taskinfo.setMethod(__pt__runBMS_51_ConcurrentLinkedQueueBenchmark_method);//####[183]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[183]####
    }//####[183]####
    private static TaskIDGroup<Void> runBMS_51(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[183]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[183]####
        return runBMS_51(benchmarkQueue, new TaskInfo());//####[183]####
    }//####[183]####
    private static TaskIDGroup<Void> runBMS_51(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[183]####
        // ensure Method variable is set//####[183]####
        if (__pt__runBMS_51_ConcurrentLinkedQueueBenchmark_method == null) {//####[183]####
            __pt__runBMS_51_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[183]####
        }//####[183]####
        taskinfo.setTaskIdArgIndexes(0);//####[183]####
        taskinfo.addDependsOn(benchmarkQueue);//####[183]####
        taskinfo.setParameters(benchmarkQueue);//####[183]####
        taskinfo.setMethod(__pt__runBMS_51_ConcurrentLinkedQueueBenchmark_method);//####[183]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[183]####
    }//####[183]####
    private static TaskIDGroup<Void> runBMS_51(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[183]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[183]####
        return runBMS_51(benchmarkQueue, new TaskInfo());//####[183]####
    }//####[183]####
    private static TaskIDGroup<Void> runBMS_51(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[183]####
        // ensure Method variable is set//####[183]####
        if (__pt__runBMS_51_ConcurrentLinkedQueueBenchmark_method == null) {//####[183]####
            __pt__runBMS_51_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[183]####
        }//####[183]####
        taskinfo.setQueueArgIndexes(0);//####[183]####
        taskinfo.setIsPipeline(true);//####[183]####
        taskinfo.setParameters(benchmarkQueue);//####[183]####
        taskinfo.setMethod(__pt__runBMS_51_ConcurrentLinkedQueueBenchmark_method);//####[183]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[183]####
    }//####[183]####
    public static void __pt__runBMS_51(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[183]####
        Benchmark benchmark = null;//####[183]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[183]####
        {//####[183]####
            runBM(benchmark);//####[183]####
        }//####[183]####
    }//####[183]####
//####[183]####
//####[184]####
    private static volatile Method __pt__runBMS_53_ConcurrentLinkedQueueBenchmark_method = null;//####[184]####
    private synchronized static void __pt__runBMS_53_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[184]####
        if (__pt__runBMS_53_ConcurrentLinkedQueueBenchmark_method == null) {//####[184]####
            try {//####[184]####
                __pt__runBMS_53_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_53", new Class[] {//####[184]####
                    ConcurrentLinkedQueue.class//####[184]####
                });//####[184]####
            } catch (Exception e) {//####[184]####
                e.printStackTrace();//####[184]####
            }//####[184]####
        }//####[184]####
    }//####[184]####
    private static TaskIDGroup<Void> runBMS_53(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[184]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[184]####
        return runBMS_53(benchmarkQueue, new TaskInfo());//####[184]####
    }//####[184]####
    private static TaskIDGroup<Void> runBMS_53(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[184]####
        // ensure Method variable is set//####[184]####
        if (__pt__runBMS_53_ConcurrentLinkedQueueBenchmark_method == null) {//####[184]####
            __pt__runBMS_53_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[184]####
        }//####[184]####
        taskinfo.setParameters(benchmarkQueue);//####[184]####
        taskinfo.setMethod(__pt__runBMS_53_ConcurrentLinkedQueueBenchmark_method);//####[184]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[184]####
    }//####[184]####
    private static TaskIDGroup<Void> runBMS_53(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[184]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[184]####
        return runBMS_53(benchmarkQueue, new TaskInfo());//####[184]####
    }//####[184]####
    private static TaskIDGroup<Void> runBMS_53(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[184]####
        // ensure Method variable is set//####[184]####
        if (__pt__runBMS_53_ConcurrentLinkedQueueBenchmark_method == null) {//####[184]####
            __pt__runBMS_53_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[184]####
        }//####[184]####
        taskinfo.setTaskIdArgIndexes(0);//####[184]####
        taskinfo.addDependsOn(benchmarkQueue);//####[184]####
        taskinfo.setParameters(benchmarkQueue);//####[184]####
        taskinfo.setMethod(__pt__runBMS_53_ConcurrentLinkedQueueBenchmark_method);//####[184]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[184]####
    }//####[184]####
    private static TaskIDGroup<Void> runBMS_53(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[184]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[184]####
        return runBMS_53(benchmarkQueue, new TaskInfo());//####[184]####
    }//####[184]####
    private static TaskIDGroup<Void> runBMS_53(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[184]####
        // ensure Method variable is set//####[184]####
        if (__pt__runBMS_53_ConcurrentLinkedQueueBenchmark_method == null) {//####[184]####
            __pt__runBMS_53_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[184]####
        }//####[184]####
        taskinfo.setQueueArgIndexes(0);//####[184]####
        taskinfo.setIsPipeline(true);//####[184]####
        taskinfo.setParameters(benchmarkQueue);//####[184]####
        taskinfo.setMethod(__pt__runBMS_53_ConcurrentLinkedQueueBenchmark_method);//####[184]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[184]####
    }//####[184]####
    public static void __pt__runBMS_53(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[184]####
        Benchmark benchmark = null;//####[184]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[184]####
        {//####[184]####
            runBM(benchmark);//####[184]####
        }//####[184]####
    }//####[184]####
//####[184]####
//####[185]####
    private static volatile Method __pt__runBMS_55_ConcurrentLinkedQueueBenchmark_method = null;//####[185]####
    private synchronized static void __pt__runBMS_55_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[185]####
        if (__pt__runBMS_55_ConcurrentLinkedQueueBenchmark_method == null) {//####[185]####
            try {//####[185]####
                __pt__runBMS_55_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_55", new Class[] {//####[185]####
                    ConcurrentLinkedQueue.class//####[185]####
                });//####[185]####
            } catch (Exception e) {//####[185]####
                e.printStackTrace();//####[185]####
            }//####[185]####
        }//####[185]####
    }//####[185]####
    private static TaskIDGroup<Void> runBMS_55(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[185]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[185]####
        return runBMS_55(benchmarkQueue, new TaskInfo());//####[185]####
    }//####[185]####
    private static TaskIDGroup<Void> runBMS_55(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[185]####
        // ensure Method variable is set//####[185]####
        if (__pt__runBMS_55_ConcurrentLinkedQueueBenchmark_method == null) {//####[185]####
            __pt__runBMS_55_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[185]####
        }//####[185]####
        taskinfo.setParameters(benchmarkQueue);//####[185]####
        taskinfo.setMethod(__pt__runBMS_55_ConcurrentLinkedQueueBenchmark_method);//####[185]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[185]####
    }//####[185]####
    private static TaskIDGroup<Void> runBMS_55(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[185]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[185]####
        return runBMS_55(benchmarkQueue, new TaskInfo());//####[185]####
    }//####[185]####
    private static TaskIDGroup<Void> runBMS_55(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[185]####
        // ensure Method variable is set//####[185]####
        if (__pt__runBMS_55_ConcurrentLinkedQueueBenchmark_method == null) {//####[185]####
            __pt__runBMS_55_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[185]####
        }//####[185]####
        taskinfo.setTaskIdArgIndexes(0);//####[185]####
        taskinfo.addDependsOn(benchmarkQueue);//####[185]####
        taskinfo.setParameters(benchmarkQueue);//####[185]####
        taskinfo.setMethod(__pt__runBMS_55_ConcurrentLinkedQueueBenchmark_method);//####[185]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[185]####
    }//####[185]####
    private static TaskIDGroup<Void> runBMS_55(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[185]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[185]####
        return runBMS_55(benchmarkQueue, new TaskInfo());//####[185]####
    }//####[185]####
    private static TaskIDGroup<Void> runBMS_55(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[185]####
        // ensure Method variable is set//####[185]####
        if (__pt__runBMS_55_ConcurrentLinkedQueueBenchmark_method == null) {//####[185]####
            __pt__runBMS_55_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[185]####
        }//####[185]####
        taskinfo.setQueueArgIndexes(0);//####[185]####
        taskinfo.setIsPipeline(true);//####[185]####
        taskinfo.setParameters(benchmarkQueue);//####[185]####
        taskinfo.setMethod(__pt__runBMS_55_ConcurrentLinkedQueueBenchmark_method);//####[185]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[185]####
    }//####[185]####
    public static void __pt__runBMS_55(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[185]####
        Benchmark benchmark = null;//####[185]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[185]####
        {//####[185]####
            runBM(benchmark);//####[185]####
        }//####[185]####
    }//####[185]####
//####[185]####
//####[186]####
    private static volatile Method __pt__runBMS_57_ConcurrentLinkedQueueBenchmark_method = null;//####[186]####
    private synchronized static void __pt__runBMS_57_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[186]####
        if (__pt__runBMS_57_ConcurrentLinkedQueueBenchmark_method == null) {//####[186]####
            try {//####[186]####
                __pt__runBMS_57_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_57", new Class[] {//####[186]####
                    ConcurrentLinkedQueue.class//####[186]####
                });//####[186]####
            } catch (Exception e) {//####[186]####
                e.printStackTrace();//####[186]####
            }//####[186]####
        }//####[186]####
    }//####[186]####
    private static TaskIDGroup<Void> runBMS_57(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[186]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[186]####
        return runBMS_57(benchmarkQueue, new TaskInfo());//####[186]####
    }//####[186]####
    private static TaskIDGroup<Void> runBMS_57(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[186]####
        // ensure Method variable is set//####[186]####
        if (__pt__runBMS_57_ConcurrentLinkedQueueBenchmark_method == null) {//####[186]####
            __pt__runBMS_57_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[186]####
        }//####[186]####
        taskinfo.setParameters(benchmarkQueue);//####[186]####
        taskinfo.setMethod(__pt__runBMS_57_ConcurrentLinkedQueueBenchmark_method);//####[186]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[186]####
    }//####[186]####
    private static TaskIDGroup<Void> runBMS_57(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[186]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[186]####
        return runBMS_57(benchmarkQueue, new TaskInfo());//####[186]####
    }//####[186]####
    private static TaskIDGroup<Void> runBMS_57(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[186]####
        // ensure Method variable is set//####[186]####
        if (__pt__runBMS_57_ConcurrentLinkedQueueBenchmark_method == null) {//####[186]####
            __pt__runBMS_57_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[186]####
        }//####[186]####
        taskinfo.setTaskIdArgIndexes(0);//####[186]####
        taskinfo.addDependsOn(benchmarkQueue);//####[186]####
        taskinfo.setParameters(benchmarkQueue);//####[186]####
        taskinfo.setMethod(__pt__runBMS_57_ConcurrentLinkedQueueBenchmark_method);//####[186]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[186]####
    }//####[186]####
    private static TaskIDGroup<Void> runBMS_57(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[186]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[186]####
        return runBMS_57(benchmarkQueue, new TaskInfo());//####[186]####
    }//####[186]####
    private static TaskIDGroup<Void> runBMS_57(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[186]####
        // ensure Method variable is set//####[186]####
        if (__pt__runBMS_57_ConcurrentLinkedQueueBenchmark_method == null) {//####[186]####
            __pt__runBMS_57_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[186]####
        }//####[186]####
        taskinfo.setQueueArgIndexes(0);//####[186]####
        taskinfo.setIsPipeline(true);//####[186]####
        taskinfo.setParameters(benchmarkQueue);//####[186]####
        taskinfo.setMethod(__pt__runBMS_57_ConcurrentLinkedQueueBenchmark_method);//####[186]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[186]####
    }//####[186]####
    public static void __pt__runBMS_57(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[186]####
        Benchmark benchmark = null;//####[186]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[186]####
        {//####[186]####
            runBM(benchmark);//####[186]####
        }//####[186]####
    }//####[186]####
//####[186]####
//####[187]####
    private static volatile Method __pt__runBMS_58_ConcurrentLinkedQueueBenchmark_method = null;//####[187]####
    private synchronized static void __pt__runBMS_58_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[187]####
        if (__pt__runBMS_58_ConcurrentLinkedQueueBenchmark_method == null) {//####[187]####
            try {//####[187]####
                __pt__runBMS_58_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_58", new Class[] {//####[187]####
                    ConcurrentLinkedQueue.class//####[187]####
                });//####[187]####
            } catch (Exception e) {//####[187]####
                e.printStackTrace();//####[187]####
            }//####[187]####
        }//####[187]####
    }//####[187]####
    private static TaskIDGroup<Void> runBMS_58(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[187]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[187]####
        return runBMS_58(benchmarkQueue, new TaskInfo());//####[187]####
    }//####[187]####
    private static TaskIDGroup<Void> runBMS_58(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[187]####
        // ensure Method variable is set//####[187]####
        if (__pt__runBMS_58_ConcurrentLinkedQueueBenchmark_method == null) {//####[187]####
            __pt__runBMS_58_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[187]####
        }//####[187]####
        taskinfo.setParameters(benchmarkQueue);//####[187]####
        taskinfo.setMethod(__pt__runBMS_58_ConcurrentLinkedQueueBenchmark_method);//####[187]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[187]####
    }//####[187]####
    private static TaskIDGroup<Void> runBMS_58(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[187]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[187]####
        return runBMS_58(benchmarkQueue, new TaskInfo());//####[187]####
    }//####[187]####
    private static TaskIDGroup<Void> runBMS_58(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[187]####
        // ensure Method variable is set//####[187]####
        if (__pt__runBMS_58_ConcurrentLinkedQueueBenchmark_method == null) {//####[187]####
            __pt__runBMS_58_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[187]####
        }//####[187]####
        taskinfo.setTaskIdArgIndexes(0);//####[187]####
        taskinfo.addDependsOn(benchmarkQueue);//####[187]####
        taskinfo.setParameters(benchmarkQueue);//####[187]####
        taskinfo.setMethod(__pt__runBMS_58_ConcurrentLinkedQueueBenchmark_method);//####[187]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[187]####
    }//####[187]####
    private static TaskIDGroup<Void> runBMS_58(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[187]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[187]####
        return runBMS_58(benchmarkQueue, new TaskInfo());//####[187]####
    }//####[187]####
    private static TaskIDGroup<Void> runBMS_58(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[187]####
        // ensure Method variable is set//####[187]####
        if (__pt__runBMS_58_ConcurrentLinkedQueueBenchmark_method == null) {//####[187]####
            __pt__runBMS_58_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[187]####
        }//####[187]####
        taskinfo.setQueueArgIndexes(0);//####[187]####
        taskinfo.setIsPipeline(true);//####[187]####
        taskinfo.setParameters(benchmarkQueue);//####[187]####
        taskinfo.setMethod(__pt__runBMS_58_ConcurrentLinkedQueueBenchmark_method);//####[187]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[187]####
    }//####[187]####
    public static void __pt__runBMS_58(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[187]####
        Benchmark benchmark = null;//####[187]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[187]####
        {//####[187]####
            runBM(benchmark);//####[187]####
        }//####[187]####
    }//####[187]####
//####[187]####
//####[188]####
    private static volatile Method __pt__runBMS_59_ConcurrentLinkedQueueBenchmark_method = null;//####[188]####
    private synchronized static void __pt__runBMS_59_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[188]####
        if (__pt__runBMS_59_ConcurrentLinkedQueueBenchmark_method == null) {//####[188]####
            try {//####[188]####
                __pt__runBMS_59_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_59", new Class[] {//####[188]####
                    ConcurrentLinkedQueue.class//####[188]####
                });//####[188]####
            } catch (Exception e) {//####[188]####
                e.printStackTrace();//####[188]####
            }//####[188]####
        }//####[188]####
    }//####[188]####
    private static TaskIDGroup<Void> runBMS_59(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[188]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[188]####
        return runBMS_59(benchmarkQueue, new TaskInfo());//####[188]####
    }//####[188]####
    private static TaskIDGroup<Void> runBMS_59(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[188]####
        // ensure Method variable is set//####[188]####
        if (__pt__runBMS_59_ConcurrentLinkedQueueBenchmark_method == null) {//####[188]####
            __pt__runBMS_59_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[188]####
        }//####[188]####
        taskinfo.setParameters(benchmarkQueue);//####[188]####
        taskinfo.setMethod(__pt__runBMS_59_ConcurrentLinkedQueueBenchmark_method);//####[188]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[188]####
    }//####[188]####
    private static TaskIDGroup<Void> runBMS_59(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[188]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[188]####
        return runBMS_59(benchmarkQueue, new TaskInfo());//####[188]####
    }//####[188]####
    private static TaskIDGroup<Void> runBMS_59(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[188]####
        // ensure Method variable is set//####[188]####
        if (__pt__runBMS_59_ConcurrentLinkedQueueBenchmark_method == null) {//####[188]####
            __pt__runBMS_59_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[188]####
        }//####[188]####
        taskinfo.setTaskIdArgIndexes(0);//####[188]####
        taskinfo.addDependsOn(benchmarkQueue);//####[188]####
        taskinfo.setParameters(benchmarkQueue);//####[188]####
        taskinfo.setMethod(__pt__runBMS_59_ConcurrentLinkedQueueBenchmark_method);//####[188]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[188]####
    }//####[188]####
    private static TaskIDGroup<Void> runBMS_59(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[188]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[188]####
        return runBMS_59(benchmarkQueue, new TaskInfo());//####[188]####
    }//####[188]####
    private static TaskIDGroup<Void> runBMS_59(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[188]####
        // ensure Method variable is set//####[188]####
        if (__pt__runBMS_59_ConcurrentLinkedQueueBenchmark_method == null) {//####[188]####
            __pt__runBMS_59_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[188]####
        }//####[188]####
        taskinfo.setQueueArgIndexes(0);//####[188]####
        taskinfo.setIsPipeline(true);//####[188]####
        taskinfo.setParameters(benchmarkQueue);//####[188]####
        taskinfo.setMethod(__pt__runBMS_59_ConcurrentLinkedQueueBenchmark_method);//####[188]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[188]####
    }//####[188]####
    public static void __pt__runBMS_59(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[188]####
        Benchmark benchmark = null;//####[188]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[188]####
        {//####[188]####
            runBM(benchmark);//####[188]####
        }//####[188]####
    }//####[188]####
//####[188]####
//####[189]####
    private static volatile Method __pt__runBMS_60_ConcurrentLinkedQueueBenchmark_method = null;//####[189]####
    private synchronized static void __pt__runBMS_60_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[189]####
        if (__pt__runBMS_60_ConcurrentLinkedQueueBenchmark_method == null) {//####[189]####
            try {//####[189]####
                __pt__runBMS_60_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_60", new Class[] {//####[189]####
                    ConcurrentLinkedQueue.class//####[189]####
                });//####[189]####
            } catch (Exception e) {//####[189]####
                e.printStackTrace();//####[189]####
            }//####[189]####
        }//####[189]####
    }//####[189]####
    private static TaskIDGroup<Void> runBMS_60(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[189]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[189]####
        return runBMS_60(benchmarkQueue, new TaskInfo());//####[189]####
    }//####[189]####
    private static TaskIDGroup<Void> runBMS_60(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[189]####
        // ensure Method variable is set//####[189]####
        if (__pt__runBMS_60_ConcurrentLinkedQueueBenchmark_method == null) {//####[189]####
            __pt__runBMS_60_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[189]####
        }//####[189]####
        taskinfo.setParameters(benchmarkQueue);//####[189]####
        taskinfo.setMethod(__pt__runBMS_60_ConcurrentLinkedQueueBenchmark_method);//####[189]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[189]####
    }//####[189]####
    private static TaskIDGroup<Void> runBMS_60(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[189]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[189]####
        return runBMS_60(benchmarkQueue, new TaskInfo());//####[189]####
    }//####[189]####
    private static TaskIDGroup<Void> runBMS_60(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[189]####
        // ensure Method variable is set//####[189]####
        if (__pt__runBMS_60_ConcurrentLinkedQueueBenchmark_method == null) {//####[189]####
            __pt__runBMS_60_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[189]####
        }//####[189]####
        taskinfo.setTaskIdArgIndexes(0);//####[189]####
        taskinfo.addDependsOn(benchmarkQueue);//####[189]####
        taskinfo.setParameters(benchmarkQueue);//####[189]####
        taskinfo.setMethod(__pt__runBMS_60_ConcurrentLinkedQueueBenchmark_method);//####[189]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[189]####
    }//####[189]####
    private static TaskIDGroup<Void> runBMS_60(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[189]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[189]####
        return runBMS_60(benchmarkQueue, new TaskInfo());//####[189]####
    }//####[189]####
    private static TaskIDGroup<Void> runBMS_60(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[189]####
        // ensure Method variable is set//####[189]####
        if (__pt__runBMS_60_ConcurrentLinkedQueueBenchmark_method == null) {//####[189]####
            __pt__runBMS_60_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[189]####
        }//####[189]####
        taskinfo.setQueueArgIndexes(0);//####[189]####
        taskinfo.setIsPipeline(true);//####[189]####
        taskinfo.setParameters(benchmarkQueue);//####[189]####
        taskinfo.setMethod(__pt__runBMS_60_ConcurrentLinkedQueueBenchmark_method);//####[189]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[189]####
    }//####[189]####
    public static void __pt__runBMS_60(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[189]####
        Benchmark benchmark = null;//####[189]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[189]####
        {//####[189]####
            runBM(benchmark);//####[189]####
        }//####[189]####
    }//####[189]####
//####[189]####
//####[190]####
    private static volatile Method __pt__runBMS_63_ConcurrentLinkedQueueBenchmark_method = null;//####[190]####
    private synchronized static void __pt__runBMS_63_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[190]####
        if (__pt__runBMS_63_ConcurrentLinkedQueueBenchmark_method == null) {//####[190]####
            try {//####[190]####
                __pt__runBMS_63_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_63", new Class[] {//####[190]####
                    ConcurrentLinkedQueue.class//####[190]####
                });//####[190]####
            } catch (Exception e) {//####[190]####
                e.printStackTrace();//####[190]####
            }//####[190]####
        }//####[190]####
    }//####[190]####
    private static TaskIDGroup<Void> runBMS_63(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[190]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[190]####
        return runBMS_63(benchmarkQueue, new TaskInfo());//####[190]####
    }//####[190]####
    private static TaskIDGroup<Void> runBMS_63(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[190]####
        // ensure Method variable is set//####[190]####
        if (__pt__runBMS_63_ConcurrentLinkedQueueBenchmark_method == null) {//####[190]####
            __pt__runBMS_63_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[190]####
        }//####[190]####
        taskinfo.setParameters(benchmarkQueue);//####[190]####
        taskinfo.setMethod(__pt__runBMS_63_ConcurrentLinkedQueueBenchmark_method);//####[190]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[190]####
    }//####[190]####
    private static TaskIDGroup<Void> runBMS_63(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[190]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[190]####
        return runBMS_63(benchmarkQueue, new TaskInfo());//####[190]####
    }//####[190]####
    private static TaskIDGroup<Void> runBMS_63(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[190]####
        // ensure Method variable is set//####[190]####
        if (__pt__runBMS_63_ConcurrentLinkedQueueBenchmark_method == null) {//####[190]####
            __pt__runBMS_63_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[190]####
        }//####[190]####
        taskinfo.setTaskIdArgIndexes(0);//####[190]####
        taskinfo.addDependsOn(benchmarkQueue);//####[190]####
        taskinfo.setParameters(benchmarkQueue);//####[190]####
        taskinfo.setMethod(__pt__runBMS_63_ConcurrentLinkedQueueBenchmark_method);//####[190]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[190]####
    }//####[190]####
    private static TaskIDGroup<Void> runBMS_63(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[190]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[190]####
        return runBMS_63(benchmarkQueue, new TaskInfo());//####[190]####
    }//####[190]####
    private static TaskIDGroup<Void> runBMS_63(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[190]####
        // ensure Method variable is set//####[190]####
        if (__pt__runBMS_63_ConcurrentLinkedQueueBenchmark_method == null) {//####[190]####
            __pt__runBMS_63_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[190]####
        }//####[190]####
        taskinfo.setQueueArgIndexes(0);//####[190]####
        taskinfo.setIsPipeline(true);//####[190]####
        taskinfo.setParameters(benchmarkQueue);//####[190]####
        taskinfo.setMethod(__pt__runBMS_63_ConcurrentLinkedQueueBenchmark_method);//####[190]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[190]####
    }//####[190]####
    public static void __pt__runBMS_63(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[190]####
        Benchmark benchmark = null;//####[190]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[190]####
        {//####[190]####
            runBM(benchmark);//####[190]####
        }//####[190]####
    }//####[190]####
//####[190]####
//####[191]####
    private static volatile Method __pt__runBMS_64_ConcurrentLinkedQueueBenchmark_method = null;//####[191]####
    private synchronized static void __pt__runBMS_64_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[191]####
        if (__pt__runBMS_64_ConcurrentLinkedQueueBenchmark_method == null) {//####[191]####
            try {//####[191]####
                __pt__runBMS_64_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_64", new Class[] {//####[191]####
                    ConcurrentLinkedQueue.class//####[191]####
                });//####[191]####
            } catch (Exception e) {//####[191]####
                e.printStackTrace();//####[191]####
            }//####[191]####
        }//####[191]####
    }//####[191]####
    private static TaskIDGroup<Void> runBMS_64(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[191]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[191]####
        return runBMS_64(benchmarkQueue, new TaskInfo());//####[191]####
    }//####[191]####
    private static TaskIDGroup<Void> runBMS_64(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[191]####
        // ensure Method variable is set//####[191]####
        if (__pt__runBMS_64_ConcurrentLinkedQueueBenchmark_method == null) {//####[191]####
            __pt__runBMS_64_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[191]####
        }//####[191]####
        taskinfo.setParameters(benchmarkQueue);//####[191]####
        taskinfo.setMethod(__pt__runBMS_64_ConcurrentLinkedQueueBenchmark_method);//####[191]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[191]####
    }//####[191]####
    private static TaskIDGroup<Void> runBMS_64(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[191]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[191]####
        return runBMS_64(benchmarkQueue, new TaskInfo());//####[191]####
    }//####[191]####
    private static TaskIDGroup<Void> runBMS_64(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[191]####
        // ensure Method variable is set//####[191]####
        if (__pt__runBMS_64_ConcurrentLinkedQueueBenchmark_method == null) {//####[191]####
            __pt__runBMS_64_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[191]####
        }//####[191]####
        taskinfo.setTaskIdArgIndexes(0);//####[191]####
        taskinfo.addDependsOn(benchmarkQueue);//####[191]####
        taskinfo.setParameters(benchmarkQueue);//####[191]####
        taskinfo.setMethod(__pt__runBMS_64_ConcurrentLinkedQueueBenchmark_method);//####[191]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[191]####
    }//####[191]####
    private static TaskIDGroup<Void> runBMS_64(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[191]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[191]####
        return runBMS_64(benchmarkQueue, new TaskInfo());//####[191]####
    }//####[191]####
    private static TaskIDGroup<Void> runBMS_64(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[191]####
        // ensure Method variable is set//####[191]####
        if (__pt__runBMS_64_ConcurrentLinkedQueueBenchmark_method == null) {//####[191]####
            __pt__runBMS_64_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[191]####
        }//####[191]####
        taskinfo.setQueueArgIndexes(0);//####[191]####
        taskinfo.setIsPipeline(true);//####[191]####
        taskinfo.setParameters(benchmarkQueue);//####[191]####
        taskinfo.setMethod(__pt__runBMS_64_ConcurrentLinkedQueueBenchmark_method);//####[191]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[191]####
    }//####[191]####
    public static void __pt__runBMS_64(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[191]####
        Benchmark benchmark = null;//####[191]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[191]####
        {//####[191]####
            runBM(benchmark);//####[191]####
        }//####[191]####
    }//####[191]####
//####[191]####
//####[192]####
    private static volatile Method __pt__runBMS_67_ConcurrentLinkedQueueBenchmark_method = null;//####[192]####
    private synchronized static void __pt__runBMS_67_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[192]####
        if (__pt__runBMS_67_ConcurrentLinkedQueueBenchmark_method == null) {//####[192]####
            try {//####[192]####
                __pt__runBMS_67_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_67", new Class[] {//####[192]####
                    ConcurrentLinkedQueue.class//####[192]####
                });//####[192]####
            } catch (Exception e) {//####[192]####
                e.printStackTrace();//####[192]####
            }//####[192]####
        }//####[192]####
    }//####[192]####
    private static TaskIDGroup<Void> runBMS_67(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[192]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[192]####
        return runBMS_67(benchmarkQueue, new TaskInfo());//####[192]####
    }//####[192]####
    private static TaskIDGroup<Void> runBMS_67(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[192]####
        // ensure Method variable is set//####[192]####
        if (__pt__runBMS_67_ConcurrentLinkedQueueBenchmark_method == null) {//####[192]####
            __pt__runBMS_67_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[192]####
        }//####[192]####
        taskinfo.setParameters(benchmarkQueue);//####[192]####
        taskinfo.setMethod(__pt__runBMS_67_ConcurrentLinkedQueueBenchmark_method);//####[192]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[192]####
    }//####[192]####
    private static TaskIDGroup<Void> runBMS_67(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[192]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[192]####
        return runBMS_67(benchmarkQueue, new TaskInfo());//####[192]####
    }//####[192]####
    private static TaskIDGroup<Void> runBMS_67(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[192]####
        // ensure Method variable is set//####[192]####
        if (__pt__runBMS_67_ConcurrentLinkedQueueBenchmark_method == null) {//####[192]####
            __pt__runBMS_67_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[192]####
        }//####[192]####
        taskinfo.setTaskIdArgIndexes(0);//####[192]####
        taskinfo.addDependsOn(benchmarkQueue);//####[192]####
        taskinfo.setParameters(benchmarkQueue);//####[192]####
        taskinfo.setMethod(__pt__runBMS_67_ConcurrentLinkedQueueBenchmark_method);//####[192]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[192]####
    }//####[192]####
    private static TaskIDGroup<Void> runBMS_67(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[192]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[192]####
        return runBMS_67(benchmarkQueue, new TaskInfo());//####[192]####
    }//####[192]####
    private static TaskIDGroup<Void> runBMS_67(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[192]####
        // ensure Method variable is set//####[192]####
        if (__pt__runBMS_67_ConcurrentLinkedQueueBenchmark_method == null) {//####[192]####
            __pt__runBMS_67_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[192]####
        }//####[192]####
        taskinfo.setQueueArgIndexes(0);//####[192]####
        taskinfo.setIsPipeline(true);//####[192]####
        taskinfo.setParameters(benchmarkQueue);//####[192]####
        taskinfo.setMethod(__pt__runBMS_67_ConcurrentLinkedQueueBenchmark_method);//####[192]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[192]####
    }//####[192]####
    public static void __pt__runBMS_67(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[192]####
        Benchmark benchmark = null;//####[192]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[192]####
        {//####[192]####
            runBM(benchmark);//####[192]####
        }//####[192]####
    }//####[192]####
//####[192]####
//####[193]####
    private static volatile Method __pt__runBMS_69_ConcurrentLinkedQueueBenchmark_method = null;//####[193]####
    private synchronized static void __pt__runBMS_69_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[193]####
        if (__pt__runBMS_69_ConcurrentLinkedQueueBenchmark_method == null) {//####[193]####
            try {//####[193]####
                __pt__runBMS_69_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_69", new Class[] {//####[193]####
                    ConcurrentLinkedQueue.class//####[193]####
                });//####[193]####
            } catch (Exception e) {//####[193]####
                e.printStackTrace();//####[193]####
            }//####[193]####
        }//####[193]####
    }//####[193]####
    private static TaskIDGroup<Void> runBMS_69(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[193]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[193]####
        return runBMS_69(benchmarkQueue, new TaskInfo());//####[193]####
    }//####[193]####
    private static TaskIDGroup<Void> runBMS_69(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[193]####
        // ensure Method variable is set//####[193]####
        if (__pt__runBMS_69_ConcurrentLinkedQueueBenchmark_method == null) {//####[193]####
            __pt__runBMS_69_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[193]####
        }//####[193]####
        taskinfo.setParameters(benchmarkQueue);//####[193]####
        taskinfo.setMethod(__pt__runBMS_69_ConcurrentLinkedQueueBenchmark_method);//####[193]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[193]####
    }//####[193]####
    private static TaskIDGroup<Void> runBMS_69(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[193]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[193]####
        return runBMS_69(benchmarkQueue, new TaskInfo());//####[193]####
    }//####[193]####
    private static TaskIDGroup<Void> runBMS_69(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[193]####
        // ensure Method variable is set//####[193]####
        if (__pt__runBMS_69_ConcurrentLinkedQueueBenchmark_method == null) {//####[193]####
            __pt__runBMS_69_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[193]####
        }//####[193]####
        taskinfo.setTaskIdArgIndexes(0);//####[193]####
        taskinfo.addDependsOn(benchmarkQueue);//####[193]####
        taskinfo.setParameters(benchmarkQueue);//####[193]####
        taskinfo.setMethod(__pt__runBMS_69_ConcurrentLinkedQueueBenchmark_method);//####[193]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[193]####
    }//####[193]####
    private static TaskIDGroup<Void> runBMS_69(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[193]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[193]####
        return runBMS_69(benchmarkQueue, new TaskInfo());//####[193]####
    }//####[193]####
    private static TaskIDGroup<Void> runBMS_69(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[193]####
        // ensure Method variable is set//####[193]####
        if (__pt__runBMS_69_ConcurrentLinkedQueueBenchmark_method == null) {//####[193]####
            __pt__runBMS_69_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[193]####
        }//####[193]####
        taskinfo.setQueueArgIndexes(0);//####[193]####
        taskinfo.setIsPipeline(true);//####[193]####
        taskinfo.setParameters(benchmarkQueue);//####[193]####
        taskinfo.setMethod(__pt__runBMS_69_ConcurrentLinkedQueueBenchmark_method);//####[193]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[193]####
    }//####[193]####
    public static void __pt__runBMS_69(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[193]####
        Benchmark benchmark = null;//####[193]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[193]####
        {//####[193]####
            runBM(benchmark);//####[193]####
        }//####[193]####
    }//####[193]####
//####[193]####
//####[194]####
    private static volatile Method __pt__runBMS_70_ConcurrentLinkedQueueBenchmark_method = null;//####[194]####
    private synchronized static void __pt__runBMS_70_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[194]####
        if (__pt__runBMS_70_ConcurrentLinkedQueueBenchmark_method == null) {//####[194]####
            try {//####[194]####
                __pt__runBMS_70_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_70", new Class[] {//####[194]####
                    ConcurrentLinkedQueue.class//####[194]####
                });//####[194]####
            } catch (Exception e) {//####[194]####
                e.printStackTrace();//####[194]####
            }//####[194]####
        }//####[194]####
    }//####[194]####
    private static TaskIDGroup<Void> runBMS_70(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[194]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[194]####
        return runBMS_70(benchmarkQueue, new TaskInfo());//####[194]####
    }//####[194]####
    private static TaskIDGroup<Void> runBMS_70(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[194]####
        // ensure Method variable is set//####[194]####
        if (__pt__runBMS_70_ConcurrentLinkedQueueBenchmark_method == null) {//####[194]####
            __pt__runBMS_70_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[194]####
        }//####[194]####
        taskinfo.setParameters(benchmarkQueue);//####[194]####
        taskinfo.setMethod(__pt__runBMS_70_ConcurrentLinkedQueueBenchmark_method);//####[194]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[194]####
    }//####[194]####
    private static TaskIDGroup<Void> runBMS_70(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[194]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[194]####
        return runBMS_70(benchmarkQueue, new TaskInfo());//####[194]####
    }//####[194]####
    private static TaskIDGroup<Void> runBMS_70(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[194]####
        // ensure Method variable is set//####[194]####
        if (__pt__runBMS_70_ConcurrentLinkedQueueBenchmark_method == null) {//####[194]####
            __pt__runBMS_70_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[194]####
        }//####[194]####
        taskinfo.setTaskIdArgIndexes(0);//####[194]####
        taskinfo.addDependsOn(benchmarkQueue);//####[194]####
        taskinfo.setParameters(benchmarkQueue);//####[194]####
        taskinfo.setMethod(__pt__runBMS_70_ConcurrentLinkedQueueBenchmark_method);//####[194]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[194]####
    }//####[194]####
    private static TaskIDGroup<Void> runBMS_70(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[194]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[194]####
        return runBMS_70(benchmarkQueue, new TaskInfo());//####[194]####
    }//####[194]####
    private static TaskIDGroup<Void> runBMS_70(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[194]####
        // ensure Method variable is set//####[194]####
        if (__pt__runBMS_70_ConcurrentLinkedQueueBenchmark_method == null) {//####[194]####
            __pt__runBMS_70_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[194]####
        }//####[194]####
        taskinfo.setQueueArgIndexes(0);//####[194]####
        taskinfo.setIsPipeline(true);//####[194]####
        taskinfo.setParameters(benchmarkQueue);//####[194]####
        taskinfo.setMethod(__pt__runBMS_70_ConcurrentLinkedQueueBenchmark_method);//####[194]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[194]####
    }//####[194]####
    public static void __pt__runBMS_70(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[194]####
        Benchmark benchmark = null;//####[194]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[194]####
        {//####[194]####
            runBM(benchmark);//####[194]####
        }//####[194]####
    }//####[194]####
//####[194]####
//####[195]####
    private static volatile Method __pt__runBMS_71_ConcurrentLinkedQueueBenchmark_method = null;//####[195]####
    private synchronized static void __pt__runBMS_71_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[195]####
        if (__pt__runBMS_71_ConcurrentLinkedQueueBenchmark_method == null) {//####[195]####
            try {//####[195]####
                __pt__runBMS_71_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_71", new Class[] {//####[195]####
                    ConcurrentLinkedQueue.class//####[195]####
                });//####[195]####
            } catch (Exception e) {//####[195]####
                e.printStackTrace();//####[195]####
            }//####[195]####
        }//####[195]####
    }//####[195]####
    private static TaskIDGroup<Void> runBMS_71(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[195]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[195]####
        return runBMS_71(benchmarkQueue, new TaskInfo());//####[195]####
    }//####[195]####
    private static TaskIDGroup<Void> runBMS_71(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[195]####
        // ensure Method variable is set//####[195]####
        if (__pt__runBMS_71_ConcurrentLinkedQueueBenchmark_method == null) {//####[195]####
            __pt__runBMS_71_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[195]####
        }//####[195]####
        taskinfo.setParameters(benchmarkQueue);//####[195]####
        taskinfo.setMethod(__pt__runBMS_71_ConcurrentLinkedQueueBenchmark_method);//####[195]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[195]####
    }//####[195]####
    private static TaskIDGroup<Void> runBMS_71(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[195]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[195]####
        return runBMS_71(benchmarkQueue, new TaskInfo());//####[195]####
    }//####[195]####
    private static TaskIDGroup<Void> runBMS_71(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[195]####
        // ensure Method variable is set//####[195]####
        if (__pt__runBMS_71_ConcurrentLinkedQueueBenchmark_method == null) {//####[195]####
            __pt__runBMS_71_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[195]####
        }//####[195]####
        taskinfo.setTaskIdArgIndexes(0);//####[195]####
        taskinfo.addDependsOn(benchmarkQueue);//####[195]####
        taskinfo.setParameters(benchmarkQueue);//####[195]####
        taskinfo.setMethod(__pt__runBMS_71_ConcurrentLinkedQueueBenchmark_method);//####[195]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[195]####
    }//####[195]####
    private static TaskIDGroup<Void> runBMS_71(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[195]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[195]####
        return runBMS_71(benchmarkQueue, new TaskInfo());//####[195]####
    }//####[195]####
    private static TaskIDGroup<Void> runBMS_71(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[195]####
        // ensure Method variable is set//####[195]####
        if (__pt__runBMS_71_ConcurrentLinkedQueueBenchmark_method == null) {//####[195]####
            __pt__runBMS_71_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[195]####
        }//####[195]####
        taskinfo.setQueueArgIndexes(0);//####[195]####
        taskinfo.setIsPipeline(true);//####[195]####
        taskinfo.setParameters(benchmarkQueue);//####[195]####
        taskinfo.setMethod(__pt__runBMS_71_ConcurrentLinkedQueueBenchmark_method);//####[195]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[195]####
    }//####[195]####
    public static void __pt__runBMS_71(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[195]####
        Benchmark benchmark = null;//####[195]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[195]####
        {//####[195]####
            runBM(benchmark);//####[195]####
        }//####[195]####
    }//####[195]####
//####[195]####
//####[196]####
    private static volatile Method __pt__runBMS_73_ConcurrentLinkedQueueBenchmark_method = null;//####[196]####
    private synchronized static void __pt__runBMS_73_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[196]####
        if (__pt__runBMS_73_ConcurrentLinkedQueueBenchmark_method == null) {//####[196]####
            try {//####[196]####
                __pt__runBMS_73_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_73", new Class[] {//####[196]####
                    ConcurrentLinkedQueue.class//####[196]####
                });//####[196]####
            } catch (Exception e) {//####[196]####
                e.printStackTrace();//####[196]####
            }//####[196]####
        }//####[196]####
    }//####[196]####
    private static TaskIDGroup<Void> runBMS_73(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[196]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[196]####
        return runBMS_73(benchmarkQueue, new TaskInfo());//####[196]####
    }//####[196]####
    private static TaskIDGroup<Void> runBMS_73(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[196]####
        // ensure Method variable is set//####[196]####
        if (__pt__runBMS_73_ConcurrentLinkedQueueBenchmark_method == null) {//####[196]####
            __pt__runBMS_73_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[196]####
        }//####[196]####
        taskinfo.setParameters(benchmarkQueue);//####[196]####
        taskinfo.setMethod(__pt__runBMS_73_ConcurrentLinkedQueueBenchmark_method);//####[196]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[196]####
    }//####[196]####
    private static TaskIDGroup<Void> runBMS_73(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[196]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[196]####
        return runBMS_73(benchmarkQueue, new TaskInfo());//####[196]####
    }//####[196]####
    private static TaskIDGroup<Void> runBMS_73(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[196]####
        // ensure Method variable is set//####[196]####
        if (__pt__runBMS_73_ConcurrentLinkedQueueBenchmark_method == null) {//####[196]####
            __pt__runBMS_73_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[196]####
        }//####[196]####
        taskinfo.setTaskIdArgIndexes(0);//####[196]####
        taskinfo.addDependsOn(benchmarkQueue);//####[196]####
        taskinfo.setParameters(benchmarkQueue);//####[196]####
        taskinfo.setMethod(__pt__runBMS_73_ConcurrentLinkedQueueBenchmark_method);//####[196]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[196]####
    }//####[196]####
    private static TaskIDGroup<Void> runBMS_73(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[196]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[196]####
        return runBMS_73(benchmarkQueue, new TaskInfo());//####[196]####
    }//####[196]####
    private static TaskIDGroup<Void> runBMS_73(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[196]####
        // ensure Method variable is set//####[196]####
        if (__pt__runBMS_73_ConcurrentLinkedQueueBenchmark_method == null) {//####[196]####
            __pt__runBMS_73_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[196]####
        }//####[196]####
        taskinfo.setQueueArgIndexes(0);//####[196]####
        taskinfo.setIsPipeline(true);//####[196]####
        taskinfo.setParameters(benchmarkQueue);//####[196]####
        taskinfo.setMethod(__pt__runBMS_73_ConcurrentLinkedQueueBenchmark_method);//####[196]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[196]####
    }//####[196]####
    public static void __pt__runBMS_73(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[196]####
        Benchmark benchmark = null;//####[196]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[196]####
        {//####[196]####
            runBM(benchmark);//####[196]####
        }//####[196]####
    }//####[196]####
//####[196]####
//####[197]####
    private static volatile Method __pt__runBMS_74_ConcurrentLinkedQueueBenchmark_method = null;//####[197]####
    private synchronized static void __pt__runBMS_74_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[197]####
        if (__pt__runBMS_74_ConcurrentLinkedQueueBenchmark_method == null) {//####[197]####
            try {//####[197]####
                __pt__runBMS_74_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_74", new Class[] {//####[197]####
                    ConcurrentLinkedQueue.class//####[197]####
                });//####[197]####
            } catch (Exception e) {//####[197]####
                e.printStackTrace();//####[197]####
            }//####[197]####
        }//####[197]####
    }//####[197]####
    private static TaskIDGroup<Void> runBMS_74(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[197]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[197]####
        return runBMS_74(benchmarkQueue, new TaskInfo());//####[197]####
    }//####[197]####
    private static TaskIDGroup<Void> runBMS_74(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[197]####
        // ensure Method variable is set//####[197]####
        if (__pt__runBMS_74_ConcurrentLinkedQueueBenchmark_method == null) {//####[197]####
            __pt__runBMS_74_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[197]####
        }//####[197]####
        taskinfo.setParameters(benchmarkQueue);//####[197]####
        taskinfo.setMethod(__pt__runBMS_74_ConcurrentLinkedQueueBenchmark_method);//####[197]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[197]####
    }//####[197]####
    private static TaskIDGroup<Void> runBMS_74(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[197]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[197]####
        return runBMS_74(benchmarkQueue, new TaskInfo());//####[197]####
    }//####[197]####
    private static TaskIDGroup<Void> runBMS_74(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[197]####
        // ensure Method variable is set//####[197]####
        if (__pt__runBMS_74_ConcurrentLinkedQueueBenchmark_method == null) {//####[197]####
            __pt__runBMS_74_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[197]####
        }//####[197]####
        taskinfo.setTaskIdArgIndexes(0);//####[197]####
        taskinfo.addDependsOn(benchmarkQueue);//####[197]####
        taskinfo.setParameters(benchmarkQueue);//####[197]####
        taskinfo.setMethod(__pt__runBMS_74_ConcurrentLinkedQueueBenchmark_method);//####[197]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[197]####
    }//####[197]####
    private static TaskIDGroup<Void> runBMS_74(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[197]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[197]####
        return runBMS_74(benchmarkQueue, new TaskInfo());//####[197]####
    }//####[197]####
    private static TaskIDGroup<Void> runBMS_74(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[197]####
        // ensure Method variable is set//####[197]####
        if (__pt__runBMS_74_ConcurrentLinkedQueueBenchmark_method == null) {//####[197]####
            __pt__runBMS_74_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[197]####
        }//####[197]####
        taskinfo.setQueueArgIndexes(0);//####[197]####
        taskinfo.setIsPipeline(true);//####[197]####
        taskinfo.setParameters(benchmarkQueue);//####[197]####
        taskinfo.setMethod(__pt__runBMS_74_ConcurrentLinkedQueueBenchmark_method);//####[197]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[197]####
    }//####[197]####
    public static void __pt__runBMS_74(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[197]####
        Benchmark benchmark = null;//####[197]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[197]####
        {//####[197]####
            runBM(benchmark);//####[197]####
        }//####[197]####
    }//####[197]####
//####[197]####
//####[198]####
    private static volatile Method __pt__runBMS_78_ConcurrentLinkedQueueBenchmark_method = null;//####[198]####
    private synchronized static void __pt__runBMS_78_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[198]####
        if (__pt__runBMS_78_ConcurrentLinkedQueueBenchmark_method == null) {//####[198]####
            try {//####[198]####
                __pt__runBMS_78_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_78", new Class[] {//####[198]####
                    ConcurrentLinkedQueue.class//####[198]####
                });//####[198]####
            } catch (Exception e) {//####[198]####
                e.printStackTrace();//####[198]####
            }//####[198]####
        }//####[198]####
    }//####[198]####
    private static TaskIDGroup<Void> runBMS_78(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[198]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[198]####
        return runBMS_78(benchmarkQueue, new TaskInfo());//####[198]####
    }//####[198]####
    private static TaskIDGroup<Void> runBMS_78(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[198]####
        // ensure Method variable is set//####[198]####
        if (__pt__runBMS_78_ConcurrentLinkedQueueBenchmark_method == null) {//####[198]####
            __pt__runBMS_78_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[198]####
        }//####[198]####
        taskinfo.setParameters(benchmarkQueue);//####[198]####
        taskinfo.setMethod(__pt__runBMS_78_ConcurrentLinkedQueueBenchmark_method);//####[198]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[198]####
    }//####[198]####
    private static TaskIDGroup<Void> runBMS_78(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[198]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[198]####
        return runBMS_78(benchmarkQueue, new TaskInfo());//####[198]####
    }//####[198]####
    private static TaskIDGroup<Void> runBMS_78(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[198]####
        // ensure Method variable is set//####[198]####
        if (__pt__runBMS_78_ConcurrentLinkedQueueBenchmark_method == null) {//####[198]####
            __pt__runBMS_78_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[198]####
        }//####[198]####
        taskinfo.setTaskIdArgIndexes(0);//####[198]####
        taskinfo.addDependsOn(benchmarkQueue);//####[198]####
        taskinfo.setParameters(benchmarkQueue);//####[198]####
        taskinfo.setMethod(__pt__runBMS_78_ConcurrentLinkedQueueBenchmark_method);//####[198]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[198]####
    }//####[198]####
    private static TaskIDGroup<Void> runBMS_78(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[198]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[198]####
        return runBMS_78(benchmarkQueue, new TaskInfo());//####[198]####
    }//####[198]####
    private static TaskIDGroup<Void> runBMS_78(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[198]####
        // ensure Method variable is set//####[198]####
        if (__pt__runBMS_78_ConcurrentLinkedQueueBenchmark_method == null) {//####[198]####
            __pt__runBMS_78_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[198]####
        }//####[198]####
        taskinfo.setQueueArgIndexes(0);//####[198]####
        taskinfo.setIsPipeline(true);//####[198]####
        taskinfo.setParameters(benchmarkQueue);//####[198]####
        taskinfo.setMethod(__pt__runBMS_78_ConcurrentLinkedQueueBenchmark_method);//####[198]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[198]####
    }//####[198]####
    public static void __pt__runBMS_78(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[198]####
        Benchmark benchmark = null;//####[198]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[198]####
        {//####[198]####
            runBM(benchmark);//####[198]####
        }//####[198]####
    }//####[198]####
//####[198]####
//####[199]####
    private static volatile Method __pt__runBMS_81_ConcurrentLinkedQueueBenchmark_method = null;//####[199]####
    private synchronized static void __pt__runBMS_81_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[199]####
        if (__pt__runBMS_81_ConcurrentLinkedQueueBenchmark_method == null) {//####[199]####
            try {//####[199]####
                __pt__runBMS_81_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_81", new Class[] {//####[199]####
                    ConcurrentLinkedQueue.class//####[199]####
                });//####[199]####
            } catch (Exception e) {//####[199]####
                e.printStackTrace();//####[199]####
            }//####[199]####
        }//####[199]####
    }//####[199]####
    private static TaskIDGroup<Void> runBMS_81(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[199]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[199]####
        return runBMS_81(benchmarkQueue, new TaskInfo());//####[199]####
    }//####[199]####
    private static TaskIDGroup<Void> runBMS_81(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[199]####
        // ensure Method variable is set//####[199]####
        if (__pt__runBMS_81_ConcurrentLinkedQueueBenchmark_method == null) {//####[199]####
            __pt__runBMS_81_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[199]####
        }//####[199]####
        taskinfo.setParameters(benchmarkQueue);//####[199]####
        taskinfo.setMethod(__pt__runBMS_81_ConcurrentLinkedQueueBenchmark_method);//####[199]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[199]####
    }//####[199]####
    private static TaskIDGroup<Void> runBMS_81(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[199]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[199]####
        return runBMS_81(benchmarkQueue, new TaskInfo());//####[199]####
    }//####[199]####
    private static TaskIDGroup<Void> runBMS_81(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[199]####
        // ensure Method variable is set//####[199]####
        if (__pt__runBMS_81_ConcurrentLinkedQueueBenchmark_method == null) {//####[199]####
            __pt__runBMS_81_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[199]####
        }//####[199]####
        taskinfo.setTaskIdArgIndexes(0);//####[199]####
        taskinfo.addDependsOn(benchmarkQueue);//####[199]####
        taskinfo.setParameters(benchmarkQueue);//####[199]####
        taskinfo.setMethod(__pt__runBMS_81_ConcurrentLinkedQueueBenchmark_method);//####[199]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[199]####
    }//####[199]####
    private static TaskIDGroup<Void> runBMS_81(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[199]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[199]####
        return runBMS_81(benchmarkQueue, new TaskInfo());//####[199]####
    }//####[199]####
    private static TaskIDGroup<Void> runBMS_81(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[199]####
        // ensure Method variable is set//####[199]####
        if (__pt__runBMS_81_ConcurrentLinkedQueueBenchmark_method == null) {//####[199]####
            __pt__runBMS_81_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[199]####
        }//####[199]####
        taskinfo.setQueueArgIndexes(0);//####[199]####
        taskinfo.setIsPipeline(true);//####[199]####
        taskinfo.setParameters(benchmarkQueue);//####[199]####
        taskinfo.setMethod(__pt__runBMS_81_ConcurrentLinkedQueueBenchmark_method);//####[199]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[199]####
    }//####[199]####
    public static void __pt__runBMS_81(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[199]####
        Benchmark benchmark = null;//####[199]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[199]####
        {//####[199]####
            runBM(benchmark);//####[199]####
        }//####[199]####
    }//####[199]####
//####[199]####
//####[200]####
    private static volatile Method __pt__runBMS_82_ConcurrentLinkedQueueBenchmark_method = null;//####[200]####
    private synchronized static void __pt__runBMS_82_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[200]####
        if (__pt__runBMS_82_ConcurrentLinkedQueueBenchmark_method == null) {//####[200]####
            try {//####[200]####
                __pt__runBMS_82_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_82", new Class[] {//####[200]####
                    ConcurrentLinkedQueue.class//####[200]####
                });//####[200]####
            } catch (Exception e) {//####[200]####
                e.printStackTrace();//####[200]####
            }//####[200]####
        }//####[200]####
    }//####[200]####
    private static TaskIDGroup<Void> runBMS_82(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[200]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[200]####
        return runBMS_82(benchmarkQueue, new TaskInfo());//####[200]####
    }//####[200]####
    private static TaskIDGroup<Void> runBMS_82(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[200]####
        // ensure Method variable is set//####[200]####
        if (__pt__runBMS_82_ConcurrentLinkedQueueBenchmark_method == null) {//####[200]####
            __pt__runBMS_82_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[200]####
        }//####[200]####
        taskinfo.setParameters(benchmarkQueue);//####[200]####
        taskinfo.setMethod(__pt__runBMS_82_ConcurrentLinkedQueueBenchmark_method);//####[200]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[200]####
    }//####[200]####
    private static TaskIDGroup<Void> runBMS_82(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[200]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[200]####
        return runBMS_82(benchmarkQueue, new TaskInfo());//####[200]####
    }//####[200]####
    private static TaskIDGroup<Void> runBMS_82(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[200]####
        // ensure Method variable is set//####[200]####
        if (__pt__runBMS_82_ConcurrentLinkedQueueBenchmark_method == null) {//####[200]####
            __pt__runBMS_82_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[200]####
        }//####[200]####
        taskinfo.setTaskIdArgIndexes(0);//####[200]####
        taskinfo.addDependsOn(benchmarkQueue);//####[200]####
        taskinfo.setParameters(benchmarkQueue);//####[200]####
        taskinfo.setMethod(__pt__runBMS_82_ConcurrentLinkedQueueBenchmark_method);//####[200]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[200]####
    }//####[200]####
    private static TaskIDGroup<Void> runBMS_82(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[200]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[200]####
        return runBMS_82(benchmarkQueue, new TaskInfo());//####[200]####
    }//####[200]####
    private static TaskIDGroup<Void> runBMS_82(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[200]####
        // ensure Method variable is set//####[200]####
        if (__pt__runBMS_82_ConcurrentLinkedQueueBenchmark_method == null) {//####[200]####
            __pt__runBMS_82_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[200]####
        }//####[200]####
        taskinfo.setQueueArgIndexes(0);//####[200]####
        taskinfo.setIsPipeline(true);//####[200]####
        taskinfo.setParameters(benchmarkQueue);//####[200]####
        taskinfo.setMethod(__pt__runBMS_82_ConcurrentLinkedQueueBenchmark_method);//####[200]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[200]####
    }//####[200]####
    public static void __pt__runBMS_82(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[200]####
        Benchmark benchmark = null;//####[200]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[200]####
        {//####[200]####
            runBM(benchmark);//####[200]####
        }//####[200]####
    }//####[200]####
//####[200]####
//####[201]####
    private static volatile Method __pt__runBMS_85_ConcurrentLinkedQueueBenchmark_method = null;//####[201]####
    private synchronized static void __pt__runBMS_85_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[201]####
        if (__pt__runBMS_85_ConcurrentLinkedQueueBenchmark_method == null) {//####[201]####
            try {//####[201]####
                __pt__runBMS_85_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_85", new Class[] {//####[201]####
                    ConcurrentLinkedQueue.class//####[201]####
                });//####[201]####
            } catch (Exception e) {//####[201]####
                e.printStackTrace();//####[201]####
            }//####[201]####
        }//####[201]####
    }//####[201]####
    private static TaskIDGroup<Void> runBMS_85(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[201]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[201]####
        return runBMS_85(benchmarkQueue, new TaskInfo());//####[201]####
    }//####[201]####
    private static TaskIDGroup<Void> runBMS_85(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[201]####
        // ensure Method variable is set//####[201]####
        if (__pt__runBMS_85_ConcurrentLinkedQueueBenchmark_method == null) {//####[201]####
            __pt__runBMS_85_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[201]####
        }//####[201]####
        taskinfo.setParameters(benchmarkQueue);//####[201]####
        taskinfo.setMethod(__pt__runBMS_85_ConcurrentLinkedQueueBenchmark_method);//####[201]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[201]####
    }//####[201]####
    private static TaskIDGroup<Void> runBMS_85(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[201]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[201]####
        return runBMS_85(benchmarkQueue, new TaskInfo());//####[201]####
    }//####[201]####
    private static TaskIDGroup<Void> runBMS_85(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[201]####
        // ensure Method variable is set//####[201]####
        if (__pt__runBMS_85_ConcurrentLinkedQueueBenchmark_method == null) {//####[201]####
            __pt__runBMS_85_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[201]####
        }//####[201]####
        taskinfo.setTaskIdArgIndexes(0);//####[201]####
        taskinfo.addDependsOn(benchmarkQueue);//####[201]####
        taskinfo.setParameters(benchmarkQueue);//####[201]####
        taskinfo.setMethod(__pt__runBMS_85_ConcurrentLinkedQueueBenchmark_method);//####[201]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[201]####
    }//####[201]####
    private static TaskIDGroup<Void> runBMS_85(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[201]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[201]####
        return runBMS_85(benchmarkQueue, new TaskInfo());//####[201]####
    }//####[201]####
    private static TaskIDGroup<Void> runBMS_85(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[201]####
        // ensure Method variable is set//####[201]####
        if (__pt__runBMS_85_ConcurrentLinkedQueueBenchmark_method == null) {//####[201]####
            __pt__runBMS_85_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[201]####
        }//####[201]####
        taskinfo.setQueueArgIndexes(0);//####[201]####
        taskinfo.setIsPipeline(true);//####[201]####
        taskinfo.setParameters(benchmarkQueue);//####[201]####
        taskinfo.setMethod(__pt__runBMS_85_ConcurrentLinkedQueueBenchmark_method);//####[201]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[201]####
    }//####[201]####
    public static void __pt__runBMS_85(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[201]####
        Benchmark benchmark = null;//####[201]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[201]####
        {//####[201]####
            runBM(benchmark);//####[201]####
        }//####[201]####
    }//####[201]####
//####[201]####
//####[202]####
    private static volatile Method __pt__runBMS_86_ConcurrentLinkedQueueBenchmark_method = null;//####[202]####
    private synchronized static void __pt__runBMS_86_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[202]####
        if (__pt__runBMS_86_ConcurrentLinkedQueueBenchmark_method == null) {//####[202]####
            try {//####[202]####
                __pt__runBMS_86_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_86", new Class[] {//####[202]####
                    ConcurrentLinkedQueue.class//####[202]####
                });//####[202]####
            } catch (Exception e) {//####[202]####
                e.printStackTrace();//####[202]####
            }//####[202]####
        }//####[202]####
    }//####[202]####
    private static TaskIDGroup<Void> runBMS_86(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[202]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[202]####
        return runBMS_86(benchmarkQueue, new TaskInfo());//####[202]####
    }//####[202]####
    private static TaskIDGroup<Void> runBMS_86(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[202]####
        // ensure Method variable is set//####[202]####
        if (__pt__runBMS_86_ConcurrentLinkedQueueBenchmark_method == null) {//####[202]####
            __pt__runBMS_86_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[202]####
        }//####[202]####
        taskinfo.setParameters(benchmarkQueue);//####[202]####
        taskinfo.setMethod(__pt__runBMS_86_ConcurrentLinkedQueueBenchmark_method);//####[202]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[202]####
    }//####[202]####
    private static TaskIDGroup<Void> runBMS_86(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[202]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[202]####
        return runBMS_86(benchmarkQueue, new TaskInfo());//####[202]####
    }//####[202]####
    private static TaskIDGroup<Void> runBMS_86(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[202]####
        // ensure Method variable is set//####[202]####
        if (__pt__runBMS_86_ConcurrentLinkedQueueBenchmark_method == null) {//####[202]####
            __pt__runBMS_86_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[202]####
        }//####[202]####
        taskinfo.setTaskIdArgIndexes(0);//####[202]####
        taskinfo.addDependsOn(benchmarkQueue);//####[202]####
        taskinfo.setParameters(benchmarkQueue);//####[202]####
        taskinfo.setMethod(__pt__runBMS_86_ConcurrentLinkedQueueBenchmark_method);//####[202]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[202]####
    }//####[202]####
    private static TaskIDGroup<Void> runBMS_86(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[202]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[202]####
        return runBMS_86(benchmarkQueue, new TaskInfo());//####[202]####
    }//####[202]####
    private static TaskIDGroup<Void> runBMS_86(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[202]####
        // ensure Method variable is set//####[202]####
        if (__pt__runBMS_86_ConcurrentLinkedQueueBenchmark_method == null) {//####[202]####
            __pt__runBMS_86_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[202]####
        }//####[202]####
        taskinfo.setQueueArgIndexes(0);//####[202]####
        taskinfo.setIsPipeline(true);//####[202]####
        taskinfo.setParameters(benchmarkQueue);//####[202]####
        taskinfo.setMethod(__pt__runBMS_86_ConcurrentLinkedQueueBenchmark_method);//####[202]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[202]####
    }//####[202]####
    public static void __pt__runBMS_86(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[202]####
        Benchmark benchmark = null;//####[202]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[202]####
        {//####[202]####
            runBM(benchmark);//####[202]####
        }//####[202]####
    }//####[202]####
//####[202]####
//####[203]####
    private static volatile Method __pt__runBMS_87_ConcurrentLinkedQueueBenchmark_method = null;//####[203]####
    private synchronized static void __pt__runBMS_87_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[203]####
        if (__pt__runBMS_87_ConcurrentLinkedQueueBenchmark_method == null) {//####[203]####
            try {//####[203]####
                __pt__runBMS_87_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_87", new Class[] {//####[203]####
                    ConcurrentLinkedQueue.class//####[203]####
                });//####[203]####
            } catch (Exception e) {//####[203]####
                e.printStackTrace();//####[203]####
            }//####[203]####
        }//####[203]####
    }//####[203]####
    private static TaskIDGroup<Void> runBMS_87(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[203]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[203]####
        return runBMS_87(benchmarkQueue, new TaskInfo());//####[203]####
    }//####[203]####
    private static TaskIDGroup<Void> runBMS_87(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[203]####
        // ensure Method variable is set//####[203]####
        if (__pt__runBMS_87_ConcurrentLinkedQueueBenchmark_method == null) {//####[203]####
            __pt__runBMS_87_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[203]####
        }//####[203]####
        taskinfo.setParameters(benchmarkQueue);//####[203]####
        taskinfo.setMethod(__pt__runBMS_87_ConcurrentLinkedQueueBenchmark_method);//####[203]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[203]####
    }//####[203]####
    private static TaskIDGroup<Void> runBMS_87(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[203]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[203]####
        return runBMS_87(benchmarkQueue, new TaskInfo());//####[203]####
    }//####[203]####
    private static TaskIDGroup<Void> runBMS_87(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[203]####
        // ensure Method variable is set//####[203]####
        if (__pt__runBMS_87_ConcurrentLinkedQueueBenchmark_method == null) {//####[203]####
            __pt__runBMS_87_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[203]####
        }//####[203]####
        taskinfo.setTaskIdArgIndexes(0);//####[203]####
        taskinfo.addDependsOn(benchmarkQueue);//####[203]####
        taskinfo.setParameters(benchmarkQueue);//####[203]####
        taskinfo.setMethod(__pt__runBMS_87_ConcurrentLinkedQueueBenchmark_method);//####[203]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[203]####
    }//####[203]####
    private static TaskIDGroup<Void> runBMS_87(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[203]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[203]####
        return runBMS_87(benchmarkQueue, new TaskInfo());//####[203]####
    }//####[203]####
    private static TaskIDGroup<Void> runBMS_87(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[203]####
        // ensure Method variable is set//####[203]####
        if (__pt__runBMS_87_ConcurrentLinkedQueueBenchmark_method == null) {//####[203]####
            __pt__runBMS_87_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[203]####
        }//####[203]####
        taskinfo.setQueueArgIndexes(0);//####[203]####
        taskinfo.setIsPipeline(true);//####[203]####
        taskinfo.setParameters(benchmarkQueue);//####[203]####
        taskinfo.setMethod(__pt__runBMS_87_ConcurrentLinkedQueueBenchmark_method);//####[203]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[203]####
    }//####[203]####
    public static void __pt__runBMS_87(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[203]####
        Benchmark benchmark = null;//####[203]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[203]####
        {//####[203]####
            runBM(benchmark);//####[203]####
        }//####[203]####
    }//####[203]####
//####[203]####
//####[204]####
    private static volatile Method __pt__runBMS_88_ConcurrentLinkedQueueBenchmark_method = null;//####[204]####
    private synchronized static void __pt__runBMS_88_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[204]####
        if (__pt__runBMS_88_ConcurrentLinkedQueueBenchmark_method == null) {//####[204]####
            try {//####[204]####
                __pt__runBMS_88_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_88", new Class[] {//####[204]####
                    ConcurrentLinkedQueue.class//####[204]####
                });//####[204]####
            } catch (Exception e) {//####[204]####
                e.printStackTrace();//####[204]####
            }//####[204]####
        }//####[204]####
    }//####[204]####
    private static TaskIDGroup<Void> runBMS_88(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[204]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[204]####
        return runBMS_88(benchmarkQueue, new TaskInfo());//####[204]####
    }//####[204]####
    private static TaskIDGroup<Void> runBMS_88(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[204]####
        // ensure Method variable is set//####[204]####
        if (__pt__runBMS_88_ConcurrentLinkedQueueBenchmark_method == null) {//####[204]####
            __pt__runBMS_88_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[204]####
        }//####[204]####
        taskinfo.setParameters(benchmarkQueue);//####[204]####
        taskinfo.setMethod(__pt__runBMS_88_ConcurrentLinkedQueueBenchmark_method);//####[204]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[204]####
    }//####[204]####
    private static TaskIDGroup<Void> runBMS_88(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[204]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[204]####
        return runBMS_88(benchmarkQueue, new TaskInfo());//####[204]####
    }//####[204]####
    private static TaskIDGroup<Void> runBMS_88(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[204]####
        // ensure Method variable is set//####[204]####
        if (__pt__runBMS_88_ConcurrentLinkedQueueBenchmark_method == null) {//####[204]####
            __pt__runBMS_88_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[204]####
        }//####[204]####
        taskinfo.setTaskIdArgIndexes(0);//####[204]####
        taskinfo.addDependsOn(benchmarkQueue);//####[204]####
        taskinfo.setParameters(benchmarkQueue);//####[204]####
        taskinfo.setMethod(__pt__runBMS_88_ConcurrentLinkedQueueBenchmark_method);//####[204]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[204]####
    }//####[204]####
    private static TaskIDGroup<Void> runBMS_88(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[204]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[204]####
        return runBMS_88(benchmarkQueue, new TaskInfo());//####[204]####
    }//####[204]####
    private static TaskIDGroup<Void> runBMS_88(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[204]####
        // ensure Method variable is set//####[204]####
        if (__pt__runBMS_88_ConcurrentLinkedQueueBenchmark_method == null) {//####[204]####
            __pt__runBMS_88_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[204]####
        }//####[204]####
        taskinfo.setQueueArgIndexes(0);//####[204]####
        taskinfo.setIsPipeline(true);//####[204]####
        taskinfo.setParameters(benchmarkQueue);//####[204]####
        taskinfo.setMethod(__pt__runBMS_88_ConcurrentLinkedQueueBenchmark_method);//####[204]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[204]####
    }//####[204]####
    public static void __pt__runBMS_88(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[204]####
        Benchmark benchmark = null;//####[204]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[204]####
        {//####[204]####
            runBM(benchmark);//####[204]####
        }//####[204]####
    }//####[204]####
//####[204]####
//####[205]####
    private static volatile Method __pt__runBMS_91_ConcurrentLinkedQueueBenchmark_method = null;//####[205]####
    private synchronized static void __pt__runBMS_91_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[205]####
        if (__pt__runBMS_91_ConcurrentLinkedQueueBenchmark_method == null) {//####[205]####
            try {//####[205]####
                __pt__runBMS_91_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_91", new Class[] {//####[205]####
                    ConcurrentLinkedQueue.class//####[205]####
                });//####[205]####
            } catch (Exception e) {//####[205]####
                e.printStackTrace();//####[205]####
            }//####[205]####
        }//####[205]####
    }//####[205]####
    private static TaskIDGroup<Void> runBMS_91(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[205]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[205]####
        return runBMS_91(benchmarkQueue, new TaskInfo());//####[205]####
    }//####[205]####
    private static TaskIDGroup<Void> runBMS_91(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[205]####
        // ensure Method variable is set//####[205]####
        if (__pt__runBMS_91_ConcurrentLinkedQueueBenchmark_method == null) {//####[205]####
            __pt__runBMS_91_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[205]####
        }//####[205]####
        taskinfo.setParameters(benchmarkQueue);//####[205]####
        taskinfo.setMethod(__pt__runBMS_91_ConcurrentLinkedQueueBenchmark_method);//####[205]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[205]####
    }//####[205]####
    private static TaskIDGroup<Void> runBMS_91(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[205]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[205]####
        return runBMS_91(benchmarkQueue, new TaskInfo());//####[205]####
    }//####[205]####
    private static TaskIDGroup<Void> runBMS_91(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[205]####
        // ensure Method variable is set//####[205]####
        if (__pt__runBMS_91_ConcurrentLinkedQueueBenchmark_method == null) {//####[205]####
            __pt__runBMS_91_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[205]####
        }//####[205]####
        taskinfo.setTaskIdArgIndexes(0);//####[205]####
        taskinfo.addDependsOn(benchmarkQueue);//####[205]####
        taskinfo.setParameters(benchmarkQueue);//####[205]####
        taskinfo.setMethod(__pt__runBMS_91_ConcurrentLinkedQueueBenchmark_method);//####[205]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[205]####
    }//####[205]####
    private static TaskIDGroup<Void> runBMS_91(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[205]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[205]####
        return runBMS_91(benchmarkQueue, new TaskInfo());//####[205]####
    }//####[205]####
    private static TaskIDGroup<Void> runBMS_91(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[205]####
        // ensure Method variable is set//####[205]####
        if (__pt__runBMS_91_ConcurrentLinkedQueueBenchmark_method == null) {//####[205]####
            __pt__runBMS_91_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[205]####
        }//####[205]####
        taskinfo.setQueueArgIndexes(0);//####[205]####
        taskinfo.setIsPipeline(true);//####[205]####
        taskinfo.setParameters(benchmarkQueue);//####[205]####
        taskinfo.setMethod(__pt__runBMS_91_ConcurrentLinkedQueueBenchmark_method);//####[205]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[205]####
    }//####[205]####
    public static void __pt__runBMS_91(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[205]####
        Benchmark benchmark = null;//####[205]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[205]####
        {//####[205]####
            runBM(benchmark);//####[205]####
        }//####[205]####
    }//####[205]####
//####[205]####
//####[206]####
    private static volatile Method __pt__runBMS_92_ConcurrentLinkedQueueBenchmark_method = null;//####[206]####
    private synchronized static void __pt__runBMS_92_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[206]####
        if (__pt__runBMS_92_ConcurrentLinkedQueueBenchmark_method == null) {//####[206]####
            try {//####[206]####
                __pt__runBMS_92_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_92", new Class[] {//####[206]####
                    ConcurrentLinkedQueue.class//####[206]####
                });//####[206]####
            } catch (Exception e) {//####[206]####
                e.printStackTrace();//####[206]####
            }//####[206]####
        }//####[206]####
    }//####[206]####
    private static TaskIDGroup<Void> runBMS_92(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[206]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[206]####
        return runBMS_92(benchmarkQueue, new TaskInfo());//####[206]####
    }//####[206]####
    private static TaskIDGroup<Void> runBMS_92(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[206]####
        // ensure Method variable is set//####[206]####
        if (__pt__runBMS_92_ConcurrentLinkedQueueBenchmark_method == null) {//####[206]####
            __pt__runBMS_92_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[206]####
        }//####[206]####
        taskinfo.setParameters(benchmarkQueue);//####[206]####
        taskinfo.setMethod(__pt__runBMS_92_ConcurrentLinkedQueueBenchmark_method);//####[206]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[206]####
    }//####[206]####
    private static TaskIDGroup<Void> runBMS_92(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[206]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[206]####
        return runBMS_92(benchmarkQueue, new TaskInfo());//####[206]####
    }//####[206]####
    private static TaskIDGroup<Void> runBMS_92(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[206]####
        // ensure Method variable is set//####[206]####
        if (__pt__runBMS_92_ConcurrentLinkedQueueBenchmark_method == null) {//####[206]####
            __pt__runBMS_92_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[206]####
        }//####[206]####
        taskinfo.setTaskIdArgIndexes(0);//####[206]####
        taskinfo.addDependsOn(benchmarkQueue);//####[206]####
        taskinfo.setParameters(benchmarkQueue);//####[206]####
        taskinfo.setMethod(__pt__runBMS_92_ConcurrentLinkedQueueBenchmark_method);//####[206]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[206]####
    }//####[206]####
    private static TaskIDGroup<Void> runBMS_92(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[206]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[206]####
        return runBMS_92(benchmarkQueue, new TaskInfo());//####[206]####
    }//####[206]####
    private static TaskIDGroup<Void> runBMS_92(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[206]####
        // ensure Method variable is set//####[206]####
        if (__pt__runBMS_92_ConcurrentLinkedQueueBenchmark_method == null) {//####[206]####
            __pt__runBMS_92_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[206]####
        }//####[206]####
        taskinfo.setQueueArgIndexes(0);//####[206]####
        taskinfo.setIsPipeline(true);//####[206]####
        taskinfo.setParameters(benchmarkQueue);//####[206]####
        taskinfo.setMethod(__pt__runBMS_92_ConcurrentLinkedQueueBenchmark_method);//####[206]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[206]####
    }//####[206]####
    public static void __pt__runBMS_92(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[206]####
        Benchmark benchmark = null;//####[206]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[206]####
        {//####[206]####
            runBM(benchmark);//####[206]####
        }//####[206]####
    }//####[206]####
//####[206]####
//####[207]####
    private static volatile Method __pt__runBMS_96_ConcurrentLinkedQueueBenchmark_method = null;//####[207]####
    private synchronized static void __pt__runBMS_96_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[207]####
        if (__pt__runBMS_96_ConcurrentLinkedQueueBenchmark_method == null) {//####[207]####
            try {//####[207]####
                __pt__runBMS_96_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_96", new Class[] {//####[207]####
                    ConcurrentLinkedQueue.class//####[207]####
                });//####[207]####
            } catch (Exception e) {//####[207]####
                e.printStackTrace();//####[207]####
            }//####[207]####
        }//####[207]####
    }//####[207]####
    private static TaskIDGroup<Void> runBMS_96(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[207]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[207]####
        return runBMS_96(benchmarkQueue, new TaskInfo());//####[207]####
    }//####[207]####
    private static TaskIDGroup<Void> runBMS_96(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[207]####
        // ensure Method variable is set//####[207]####
        if (__pt__runBMS_96_ConcurrentLinkedQueueBenchmark_method == null) {//####[207]####
            __pt__runBMS_96_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[207]####
        }//####[207]####
        taskinfo.setParameters(benchmarkQueue);//####[207]####
        taskinfo.setMethod(__pt__runBMS_96_ConcurrentLinkedQueueBenchmark_method);//####[207]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[207]####
    }//####[207]####
    private static TaskIDGroup<Void> runBMS_96(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[207]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[207]####
        return runBMS_96(benchmarkQueue, new TaskInfo());//####[207]####
    }//####[207]####
    private static TaskIDGroup<Void> runBMS_96(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[207]####
        // ensure Method variable is set//####[207]####
        if (__pt__runBMS_96_ConcurrentLinkedQueueBenchmark_method == null) {//####[207]####
            __pt__runBMS_96_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[207]####
        }//####[207]####
        taskinfo.setTaskIdArgIndexes(0);//####[207]####
        taskinfo.addDependsOn(benchmarkQueue);//####[207]####
        taskinfo.setParameters(benchmarkQueue);//####[207]####
        taskinfo.setMethod(__pt__runBMS_96_ConcurrentLinkedQueueBenchmark_method);//####[207]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[207]####
    }//####[207]####
    private static TaskIDGroup<Void> runBMS_96(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[207]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[207]####
        return runBMS_96(benchmarkQueue, new TaskInfo());//####[207]####
    }//####[207]####
    private static TaskIDGroup<Void> runBMS_96(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[207]####
        // ensure Method variable is set//####[207]####
        if (__pt__runBMS_96_ConcurrentLinkedQueueBenchmark_method == null) {//####[207]####
            __pt__runBMS_96_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[207]####
        }//####[207]####
        taskinfo.setQueueArgIndexes(0);//####[207]####
        taskinfo.setIsPipeline(true);//####[207]####
        taskinfo.setParameters(benchmarkQueue);//####[207]####
        taskinfo.setMethod(__pt__runBMS_96_ConcurrentLinkedQueueBenchmark_method);//####[207]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[207]####
    }//####[207]####
    public static void __pt__runBMS_96(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[207]####
        Benchmark benchmark = null;//####[207]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[207]####
        {//####[207]####
            runBM(benchmark);//####[207]####
        }//####[207]####
    }//####[207]####
//####[207]####
//####[208]####
    private static volatile Method __pt__runBMS_97_ConcurrentLinkedQueueBenchmark_method = null;//####[208]####
    private synchronized static void __pt__runBMS_97_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[208]####
        if (__pt__runBMS_97_ConcurrentLinkedQueueBenchmark_method == null) {//####[208]####
            try {//####[208]####
                __pt__runBMS_97_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_97", new Class[] {//####[208]####
                    ConcurrentLinkedQueue.class//####[208]####
                });//####[208]####
            } catch (Exception e) {//####[208]####
                e.printStackTrace();//####[208]####
            }//####[208]####
        }//####[208]####
    }//####[208]####
    private static TaskIDGroup<Void> runBMS_97(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[208]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[208]####
        return runBMS_97(benchmarkQueue, new TaskInfo());//####[208]####
    }//####[208]####
    private static TaskIDGroup<Void> runBMS_97(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[208]####
        // ensure Method variable is set//####[208]####
        if (__pt__runBMS_97_ConcurrentLinkedQueueBenchmark_method == null) {//####[208]####
            __pt__runBMS_97_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[208]####
        }//####[208]####
        taskinfo.setParameters(benchmarkQueue);//####[208]####
        taskinfo.setMethod(__pt__runBMS_97_ConcurrentLinkedQueueBenchmark_method);//####[208]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[208]####
    }//####[208]####
    private static TaskIDGroup<Void> runBMS_97(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[208]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[208]####
        return runBMS_97(benchmarkQueue, new TaskInfo());//####[208]####
    }//####[208]####
    private static TaskIDGroup<Void> runBMS_97(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[208]####
        // ensure Method variable is set//####[208]####
        if (__pt__runBMS_97_ConcurrentLinkedQueueBenchmark_method == null) {//####[208]####
            __pt__runBMS_97_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[208]####
        }//####[208]####
        taskinfo.setTaskIdArgIndexes(0);//####[208]####
        taskinfo.addDependsOn(benchmarkQueue);//####[208]####
        taskinfo.setParameters(benchmarkQueue);//####[208]####
        taskinfo.setMethod(__pt__runBMS_97_ConcurrentLinkedQueueBenchmark_method);//####[208]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[208]####
    }//####[208]####
    private static TaskIDGroup<Void> runBMS_97(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[208]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[208]####
        return runBMS_97(benchmarkQueue, new TaskInfo());//####[208]####
    }//####[208]####
    private static TaskIDGroup<Void> runBMS_97(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[208]####
        // ensure Method variable is set//####[208]####
        if (__pt__runBMS_97_ConcurrentLinkedQueueBenchmark_method == null) {//####[208]####
            __pt__runBMS_97_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[208]####
        }//####[208]####
        taskinfo.setQueueArgIndexes(0);//####[208]####
        taskinfo.setIsPipeline(true);//####[208]####
        taskinfo.setParameters(benchmarkQueue);//####[208]####
        taskinfo.setMethod(__pt__runBMS_97_ConcurrentLinkedQueueBenchmark_method);//####[208]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[208]####
    }//####[208]####
    public static void __pt__runBMS_97(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[208]####
        Benchmark benchmark = null;//####[208]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[208]####
        {//####[208]####
            runBM(benchmark);//####[208]####
        }//####[208]####
    }//####[208]####
//####[208]####
//####[209]####
    private static volatile Method __pt__runBMS_98_ConcurrentLinkedQueueBenchmark_method = null;//####[209]####
    private synchronized static void __pt__runBMS_98_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[209]####
        if (__pt__runBMS_98_ConcurrentLinkedQueueBenchmark_method == null) {//####[209]####
            try {//####[209]####
                __pt__runBMS_98_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_98", new Class[] {//####[209]####
                    ConcurrentLinkedQueue.class//####[209]####
                });//####[209]####
            } catch (Exception e) {//####[209]####
                e.printStackTrace();//####[209]####
            }//####[209]####
        }//####[209]####
    }//####[209]####
    private static TaskIDGroup<Void> runBMS_98(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[209]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[209]####
        return runBMS_98(benchmarkQueue, new TaskInfo());//####[209]####
    }//####[209]####
    private static TaskIDGroup<Void> runBMS_98(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[209]####
        // ensure Method variable is set//####[209]####
        if (__pt__runBMS_98_ConcurrentLinkedQueueBenchmark_method == null) {//####[209]####
            __pt__runBMS_98_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[209]####
        }//####[209]####
        taskinfo.setParameters(benchmarkQueue);//####[209]####
        taskinfo.setMethod(__pt__runBMS_98_ConcurrentLinkedQueueBenchmark_method);//####[209]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[209]####
    }//####[209]####
    private static TaskIDGroup<Void> runBMS_98(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[209]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[209]####
        return runBMS_98(benchmarkQueue, new TaskInfo());//####[209]####
    }//####[209]####
    private static TaskIDGroup<Void> runBMS_98(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[209]####
        // ensure Method variable is set//####[209]####
        if (__pt__runBMS_98_ConcurrentLinkedQueueBenchmark_method == null) {//####[209]####
            __pt__runBMS_98_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[209]####
        }//####[209]####
        taskinfo.setTaskIdArgIndexes(0);//####[209]####
        taskinfo.addDependsOn(benchmarkQueue);//####[209]####
        taskinfo.setParameters(benchmarkQueue);//####[209]####
        taskinfo.setMethod(__pt__runBMS_98_ConcurrentLinkedQueueBenchmark_method);//####[209]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[209]####
    }//####[209]####
    private static TaskIDGroup<Void> runBMS_98(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[209]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[209]####
        return runBMS_98(benchmarkQueue, new TaskInfo());//####[209]####
    }//####[209]####
    private static TaskIDGroup<Void> runBMS_98(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[209]####
        // ensure Method variable is set//####[209]####
        if (__pt__runBMS_98_ConcurrentLinkedQueueBenchmark_method == null) {//####[209]####
            __pt__runBMS_98_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[209]####
        }//####[209]####
        taskinfo.setQueueArgIndexes(0);//####[209]####
        taskinfo.setIsPipeline(true);//####[209]####
        taskinfo.setParameters(benchmarkQueue);//####[209]####
        taskinfo.setMethod(__pt__runBMS_98_ConcurrentLinkedQueueBenchmark_method);//####[209]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[209]####
    }//####[209]####
    public static void __pt__runBMS_98(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[209]####
        Benchmark benchmark = null;//####[209]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[209]####
        {//####[209]####
            runBM(benchmark);//####[209]####
        }//####[209]####
    }//####[209]####
//####[209]####
//####[210]####
    private static volatile Method __pt__runBMS_99_ConcurrentLinkedQueueBenchmark_method = null;//####[210]####
    private synchronized static void __pt__runBMS_99_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet() {//####[210]####
        if (__pt__runBMS_99_ConcurrentLinkedQueueBenchmark_method == null) {//####[210]####
            try {//####[210]####
                __pt__runBMS_99_ConcurrentLinkedQueueBenchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBMS_99", new Class[] {//####[210]####
                    ConcurrentLinkedQueue.class//####[210]####
                });//####[210]####
            } catch (Exception e) {//####[210]####
                e.printStackTrace();//####[210]####
            }//####[210]####
        }//####[210]####
    }//####[210]####
    private static TaskIDGroup<Void> runBMS_99(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[210]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[210]####
        return runBMS_99(benchmarkQueue, new TaskInfo());//####[210]####
    }//####[210]####
    private static TaskIDGroup<Void> runBMS_99(ConcurrentLinkedQueue<Benchmark> benchmarkQueue, TaskInfo taskinfo) {//####[210]####
        // ensure Method variable is set//####[210]####
        if (__pt__runBMS_99_ConcurrentLinkedQueueBenchmark_method == null) {//####[210]####
            __pt__runBMS_99_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[210]####
        }//####[210]####
        taskinfo.setParameters(benchmarkQueue);//####[210]####
        taskinfo.setMethod(__pt__runBMS_99_ConcurrentLinkedQueueBenchmark_method);//####[210]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[210]####
    }//####[210]####
    private static TaskIDGroup<Void> runBMS_99(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[210]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[210]####
        return runBMS_99(benchmarkQueue, new TaskInfo());//####[210]####
    }//####[210]####
    private static TaskIDGroup<Void> runBMS_99(TaskID<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[210]####
        // ensure Method variable is set//####[210]####
        if (__pt__runBMS_99_ConcurrentLinkedQueueBenchmark_method == null) {//####[210]####
            __pt__runBMS_99_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[210]####
        }//####[210]####
        taskinfo.setTaskIdArgIndexes(0);//####[210]####
        taskinfo.addDependsOn(benchmarkQueue);//####[210]####
        taskinfo.setParameters(benchmarkQueue);//####[210]####
        taskinfo.setMethod(__pt__runBMS_99_ConcurrentLinkedQueueBenchmark_method);//####[210]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[210]####
    }//####[210]####
    private static TaskIDGroup<Void> runBMS_99(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue) {//####[210]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[210]####
        return runBMS_99(benchmarkQueue, new TaskInfo());//####[210]####
    }//####[210]####
    private static TaskIDGroup<Void> runBMS_99(BlockingQueue<ConcurrentLinkedQueue<Benchmark>> benchmarkQueue, TaskInfo taskinfo) {//####[210]####
        // ensure Method variable is set//####[210]####
        if (__pt__runBMS_99_ConcurrentLinkedQueueBenchmark_method == null) {//####[210]####
            __pt__runBMS_99_ConcurrentLinkedQueueBenchmark_ensureMethodVarSet();//####[210]####
        }//####[210]####
        taskinfo.setQueueArgIndexes(0);//####[210]####
        taskinfo.setIsPipeline(true);//####[210]####
        taskinfo.setParameters(benchmarkQueue);//####[210]####
        taskinfo.setMethod(__pt__runBMS_99_ConcurrentLinkedQueueBenchmark_method);//####[210]####
        return TaskpoolFactory.getTaskpool().enqueueMulti(taskinfo, -1);//####[210]####
    }//####[210]####
    public static void __pt__runBMS_99(ConcurrentLinkedQueue<Benchmark> benchmarkQueue) {//####[210]####
        Benchmark benchmark = null;//####[210]####
        while (null != (benchmark = benchmarkQueue.poll())) //####[210]####
        {//####[210]####
            runBM(benchmark);//####[210]####
        }//####[210]####
    }//####[210]####
//####[210]####
//####[212]####
    private static volatile Method __pt__runBM_2_Benchmark_method = null;//####[212]####
    private synchronized static void __pt__runBM_2_Benchmark_ensureMethodVarSet() {//####[212]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[212]####
            try {//####[212]####
                __pt__runBM_2_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_2", new Class[] {//####[212]####
                    Benchmark.class//####[212]####
                });//####[212]####
            } catch (Exception e) {//####[212]####
                e.printStackTrace();//####[212]####
            }//####[212]####
        }//####[212]####
    }//####[212]####
    private static TaskID<Void> runBM_2(Benchmark benchmark) {//####[212]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[212]####
        return runBM_2(benchmark, new TaskInfo());//####[212]####
    }//####[212]####
    private static TaskID<Void> runBM_2(Benchmark benchmark, TaskInfo taskinfo) {//####[212]####
        // ensure Method variable is set//####[212]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[212]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[212]####
        }//####[212]####
        taskinfo.setParameters(benchmark);//####[212]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[212]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[212]####
    }//####[212]####
    private static TaskID<Void> runBM_2(TaskID<Benchmark> benchmark) {//####[212]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[212]####
        return runBM_2(benchmark, new TaskInfo());//####[212]####
    }//####[212]####
    private static TaskID<Void> runBM_2(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[212]####
        // ensure Method variable is set//####[212]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[212]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[212]####
        }//####[212]####
        taskinfo.setTaskIdArgIndexes(0);//####[212]####
        taskinfo.addDependsOn(benchmark);//####[212]####
        taskinfo.setParameters(benchmark);//####[212]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[212]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[212]####
    }//####[212]####
    private static TaskID<Void> runBM_2(BlockingQueue<Benchmark> benchmark) {//####[212]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[212]####
        return runBM_2(benchmark, new TaskInfo());//####[212]####
    }//####[212]####
    private static TaskID<Void> runBM_2(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[212]####
        // ensure Method variable is set//####[212]####
        if (__pt__runBM_2_Benchmark_method == null) {//####[212]####
            __pt__runBM_2_Benchmark_ensureMethodVarSet();//####[212]####
        }//####[212]####
        taskinfo.setQueueArgIndexes(0);//####[212]####
        taskinfo.setIsPipeline(true);//####[212]####
        taskinfo.setParameters(benchmark);//####[212]####
        taskinfo.setMethod(__pt__runBM_2_Benchmark_method);//####[212]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[212]####
    }//####[212]####
    public static void __pt__runBM_2(Benchmark benchmark) {//####[212]####
        try {//####[212]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[212]####
        } catch (IllegalAccessException e) {//####[212]####
            e.printStackTrace();//####[212]####
        } catch (IllegalArgumentException e) {//####[212]####
            e.printStackTrace();//####[212]####
        } catch (InvocationTargetException e) {//####[212]####
            e.printStackTrace();//####[212]####
        }//####[212]####
    }//####[212]####
//####[212]####
//####[213]####
    private static volatile Method __pt__runBM_6_Benchmark_method = null;//####[213]####
    private synchronized static void __pt__runBM_6_Benchmark_ensureMethodVarSet() {//####[213]####
        if (__pt__runBM_6_Benchmark_method == null) {//####[213]####
            try {//####[213]####
                __pt__runBM_6_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_6", new Class[] {//####[213]####
                    Benchmark.class//####[213]####
                });//####[213]####
            } catch (Exception e) {//####[213]####
                e.printStackTrace();//####[213]####
            }//####[213]####
        }//####[213]####
    }//####[213]####
    private static TaskID<Void> runBM_6(Benchmark benchmark) {//####[213]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[213]####
        return runBM_6(benchmark, new TaskInfo());//####[213]####
    }//####[213]####
    private static TaskID<Void> runBM_6(Benchmark benchmark, TaskInfo taskinfo) {//####[213]####
        // ensure Method variable is set//####[213]####
        if (__pt__runBM_6_Benchmark_method == null) {//####[213]####
            __pt__runBM_6_Benchmark_ensureMethodVarSet();//####[213]####
        }//####[213]####
        taskinfo.setParameters(benchmark);//####[213]####
        taskinfo.setMethod(__pt__runBM_6_Benchmark_method);//####[213]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[213]####
    }//####[213]####
    private static TaskID<Void> runBM_6(TaskID<Benchmark> benchmark) {//####[213]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[213]####
        return runBM_6(benchmark, new TaskInfo());//####[213]####
    }//####[213]####
    private static TaskID<Void> runBM_6(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[213]####
        // ensure Method variable is set//####[213]####
        if (__pt__runBM_6_Benchmark_method == null) {//####[213]####
            __pt__runBM_6_Benchmark_ensureMethodVarSet();//####[213]####
        }//####[213]####
        taskinfo.setTaskIdArgIndexes(0);//####[213]####
        taskinfo.addDependsOn(benchmark);//####[213]####
        taskinfo.setParameters(benchmark);//####[213]####
        taskinfo.setMethod(__pt__runBM_6_Benchmark_method);//####[213]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[213]####
    }//####[213]####
    private static TaskID<Void> runBM_6(BlockingQueue<Benchmark> benchmark) {//####[213]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[213]####
        return runBM_6(benchmark, new TaskInfo());//####[213]####
    }//####[213]####
    private static TaskID<Void> runBM_6(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[213]####
        // ensure Method variable is set//####[213]####
        if (__pt__runBM_6_Benchmark_method == null) {//####[213]####
            __pt__runBM_6_Benchmark_ensureMethodVarSet();//####[213]####
        }//####[213]####
        taskinfo.setQueueArgIndexes(0);//####[213]####
        taskinfo.setIsPipeline(true);//####[213]####
        taskinfo.setParameters(benchmark);//####[213]####
        taskinfo.setMethod(__pt__runBM_6_Benchmark_method);//####[213]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[213]####
    }//####[213]####
    public static void __pt__runBM_6(Benchmark benchmark) {//####[213]####
        try {//####[213]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[213]####
        } catch (IllegalAccessException e) {//####[213]####
            e.printStackTrace();//####[213]####
        } catch (IllegalArgumentException e) {//####[213]####
            e.printStackTrace();//####[213]####
        } catch (InvocationTargetException e) {//####[213]####
            e.printStackTrace();//####[213]####
        }//####[213]####
    }//####[213]####
//####[213]####
//####[214]####
    private static volatile Method __pt__runBM_7_Benchmark_method = null;//####[214]####
    private synchronized static void __pt__runBM_7_Benchmark_ensureMethodVarSet() {//####[214]####
        if (__pt__runBM_7_Benchmark_method == null) {//####[214]####
            try {//####[214]####
                __pt__runBM_7_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_7", new Class[] {//####[214]####
                    Benchmark.class//####[214]####
                });//####[214]####
            } catch (Exception e) {//####[214]####
                e.printStackTrace();//####[214]####
            }//####[214]####
        }//####[214]####
    }//####[214]####
    private static TaskID<Void> runBM_7(Benchmark benchmark) {//####[214]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[214]####
        return runBM_7(benchmark, new TaskInfo());//####[214]####
    }//####[214]####
    private static TaskID<Void> runBM_7(Benchmark benchmark, TaskInfo taskinfo) {//####[214]####
        // ensure Method variable is set//####[214]####
        if (__pt__runBM_7_Benchmark_method == null) {//####[214]####
            __pt__runBM_7_Benchmark_ensureMethodVarSet();//####[214]####
        }//####[214]####
        taskinfo.setParameters(benchmark);//####[214]####
        taskinfo.setMethod(__pt__runBM_7_Benchmark_method);//####[214]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[214]####
    }//####[214]####
    private static TaskID<Void> runBM_7(TaskID<Benchmark> benchmark) {//####[214]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[214]####
        return runBM_7(benchmark, new TaskInfo());//####[214]####
    }//####[214]####
    private static TaskID<Void> runBM_7(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[214]####
        // ensure Method variable is set//####[214]####
        if (__pt__runBM_7_Benchmark_method == null) {//####[214]####
            __pt__runBM_7_Benchmark_ensureMethodVarSet();//####[214]####
        }//####[214]####
        taskinfo.setTaskIdArgIndexes(0);//####[214]####
        taskinfo.addDependsOn(benchmark);//####[214]####
        taskinfo.setParameters(benchmark);//####[214]####
        taskinfo.setMethod(__pt__runBM_7_Benchmark_method);//####[214]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[214]####
    }//####[214]####
    private static TaskID<Void> runBM_7(BlockingQueue<Benchmark> benchmark) {//####[214]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[214]####
        return runBM_7(benchmark, new TaskInfo());//####[214]####
    }//####[214]####
    private static TaskID<Void> runBM_7(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[214]####
        // ensure Method variable is set//####[214]####
        if (__pt__runBM_7_Benchmark_method == null) {//####[214]####
            __pt__runBM_7_Benchmark_ensureMethodVarSet();//####[214]####
        }//####[214]####
        taskinfo.setQueueArgIndexes(0);//####[214]####
        taskinfo.setIsPipeline(true);//####[214]####
        taskinfo.setParameters(benchmark);//####[214]####
        taskinfo.setMethod(__pt__runBM_7_Benchmark_method);//####[214]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[214]####
    }//####[214]####
    public static void __pt__runBM_7(Benchmark benchmark) {//####[214]####
        try {//####[214]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[214]####
        } catch (IllegalAccessException e) {//####[214]####
            e.printStackTrace();//####[214]####
        } catch (IllegalArgumentException e) {//####[214]####
            e.printStackTrace();//####[214]####
        } catch (InvocationTargetException e) {//####[214]####
            e.printStackTrace();//####[214]####
        }//####[214]####
    }//####[214]####
//####[214]####
//####[215]####
    private static volatile Method __pt__runBM_10_Benchmark_method = null;//####[215]####
    private synchronized static void __pt__runBM_10_Benchmark_ensureMethodVarSet() {//####[215]####
        if (__pt__runBM_10_Benchmark_method == null) {//####[215]####
            try {//####[215]####
                __pt__runBM_10_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_10", new Class[] {//####[215]####
                    Benchmark.class//####[215]####
                });//####[215]####
            } catch (Exception e) {//####[215]####
                e.printStackTrace();//####[215]####
            }//####[215]####
        }//####[215]####
    }//####[215]####
    private static TaskID<Void> runBM_10(Benchmark benchmark) {//####[215]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[215]####
        return runBM_10(benchmark, new TaskInfo());//####[215]####
    }//####[215]####
    private static TaskID<Void> runBM_10(Benchmark benchmark, TaskInfo taskinfo) {//####[215]####
        // ensure Method variable is set//####[215]####
        if (__pt__runBM_10_Benchmark_method == null) {//####[215]####
            __pt__runBM_10_Benchmark_ensureMethodVarSet();//####[215]####
        }//####[215]####
        taskinfo.setParameters(benchmark);//####[215]####
        taskinfo.setMethod(__pt__runBM_10_Benchmark_method);//####[215]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[215]####
    }//####[215]####
    private static TaskID<Void> runBM_10(TaskID<Benchmark> benchmark) {//####[215]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[215]####
        return runBM_10(benchmark, new TaskInfo());//####[215]####
    }//####[215]####
    private static TaskID<Void> runBM_10(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[215]####
        // ensure Method variable is set//####[215]####
        if (__pt__runBM_10_Benchmark_method == null) {//####[215]####
            __pt__runBM_10_Benchmark_ensureMethodVarSet();//####[215]####
        }//####[215]####
        taskinfo.setTaskIdArgIndexes(0);//####[215]####
        taskinfo.addDependsOn(benchmark);//####[215]####
        taskinfo.setParameters(benchmark);//####[215]####
        taskinfo.setMethod(__pt__runBM_10_Benchmark_method);//####[215]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[215]####
    }//####[215]####
    private static TaskID<Void> runBM_10(BlockingQueue<Benchmark> benchmark) {//####[215]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[215]####
        return runBM_10(benchmark, new TaskInfo());//####[215]####
    }//####[215]####
    private static TaskID<Void> runBM_10(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[215]####
        // ensure Method variable is set//####[215]####
        if (__pt__runBM_10_Benchmark_method == null) {//####[215]####
            __pt__runBM_10_Benchmark_ensureMethodVarSet();//####[215]####
        }//####[215]####
        taskinfo.setQueueArgIndexes(0);//####[215]####
        taskinfo.setIsPipeline(true);//####[215]####
        taskinfo.setParameters(benchmark);//####[215]####
        taskinfo.setMethod(__pt__runBM_10_Benchmark_method);//####[215]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[215]####
    }//####[215]####
    public static void __pt__runBM_10(Benchmark benchmark) {//####[215]####
        try {//####[215]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[215]####
        } catch (IllegalAccessException e) {//####[215]####
            e.printStackTrace();//####[215]####
        } catch (IllegalArgumentException e) {//####[215]####
            e.printStackTrace();//####[215]####
        } catch (InvocationTargetException e) {//####[215]####
            e.printStackTrace();//####[215]####
        }//####[215]####
    }//####[215]####
//####[215]####
//####[216]####
    private static volatile Method __pt__runBM_11_Benchmark_method = null;//####[216]####
    private synchronized static void __pt__runBM_11_Benchmark_ensureMethodVarSet() {//####[216]####
        if (__pt__runBM_11_Benchmark_method == null) {//####[216]####
            try {//####[216]####
                __pt__runBM_11_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_11", new Class[] {//####[216]####
                    Benchmark.class//####[216]####
                });//####[216]####
            } catch (Exception e) {//####[216]####
                e.printStackTrace();//####[216]####
            }//####[216]####
        }//####[216]####
    }//####[216]####
    private static TaskID<Void> runBM_11(Benchmark benchmark) {//####[216]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[216]####
        return runBM_11(benchmark, new TaskInfo());//####[216]####
    }//####[216]####
    private static TaskID<Void> runBM_11(Benchmark benchmark, TaskInfo taskinfo) {//####[216]####
        // ensure Method variable is set//####[216]####
        if (__pt__runBM_11_Benchmark_method == null) {//####[216]####
            __pt__runBM_11_Benchmark_ensureMethodVarSet();//####[216]####
        }//####[216]####
        taskinfo.setParameters(benchmark);//####[216]####
        taskinfo.setMethod(__pt__runBM_11_Benchmark_method);//####[216]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[216]####
    }//####[216]####
    private static TaskID<Void> runBM_11(TaskID<Benchmark> benchmark) {//####[216]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[216]####
        return runBM_11(benchmark, new TaskInfo());//####[216]####
    }//####[216]####
    private static TaskID<Void> runBM_11(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[216]####
        // ensure Method variable is set//####[216]####
        if (__pt__runBM_11_Benchmark_method == null) {//####[216]####
            __pt__runBM_11_Benchmark_ensureMethodVarSet();//####[216]####
        }//####[216]####
        taskinfo.setTaskIdArgIndexes(0);//####[216]####
        taskinfo.addDependsOn(benchmark);//####[216]####
        taskinfo.setParameters(benchmark);//####[216]####
        taskinfo.setMethod(__pt__runBM_11_Benchmark_method);//####[216]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[216]####
    }//####[216]####
    private static TaskID<Void> runBM_11(BlockingQueue<Benchmark> benchmark) {//####[216]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[216]####
        return runBM_11(benchmark, new TaskInfo());//####[216]####
    }//####[216]####
    private static TaskID<Void> runBM_11(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[216]####
        // ensure Method variable is set//####[216]####
        if (__pt__runBM_11_Benchmark_method == null) {//####[216]####
            __pt__runBM_11_Benchmark_ensureMethodVarSet();//####[216]####
        }//####[216]####
        taskinfo.setQueueArgIndexes(0);//####[216]####
        taskinfo.setIsPipeline(true);//####[216]####
        taskinfo.setParameters(benchmark);//####[216]####
        taskinfo.setMethod(__pt__runBM_11_Benchmark_method);//####[216]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[216]####
    }//####[216]####
    public static void __pt__runBM_11(Benchmark benchmark) {//####[216]####
        try {//####[216]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[216]####
        } catch (IllegalAccessException e) {//####[216]####
            e.printStackTrace();//####[216]####
        } catch (IllegalArgumentException e) {//####[216]####
            e.printStackTrace();//####[216]####
        } catch (InvocationTargetException e) {//####[216]####
            e.printStackTrace();//####[216]####
        }//####[216]####
    }//####[216]####
//####[216]####
//####[217]####
    private static volatile Method __pt__runBM_12_Benchmark_method = null;//####[217]####
    private synchronized static void __pt__runBM_12_Benchmark_ensureMethodVarSet() {//####[217]####
        if (__pt__runBM_12_Benchmark_method == null) {//####[217]####
            try {//####[217]####
                __pt__runBM_12_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_12", new Class[] {//####[217]####
                    Benchmark.class//####[217]####
                });//####[217]####
            } catch (Exception e) {//####[217]####
                e.printStackTrace();//####[217]####
            }//####[217]####
        }//####[217]####
    }//####[217]####
    private static TaskID<Void> runBM_12(Benchmark benchmark) {//####[217]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[217]####
        return runBM_12(benchmark, new TaskInfo());//####[217]####
    }//####[217]####
    private static TaskID<Void> runBM_12(Benchmark benchmark, TaskInfo taskinfo) {//####[217]####
        // ensure Method variable is set//####[217]####
        if (__pt__runBM_12_Benchmark_method == null) {//####[217]####
            __pt__runBM_12_Benchmark_ensureMethodVarSet();//####[217]####
        }//####[217]####
        taskinfo.setParameters(benchmark);//####[217]####
        taskinfo.setMethod(__pt__runBM_12_Benchmark_method);//####[217]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[217]####
    }//####[217]####
    private static TaskID<Void> runBM_12(TaskID<Benchmark> benchmark) {//####[217]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[217]####
        return runBM_12(benchmark, new TaskInfo());//####[217]####
    }//####[217]####
    private static TaskID<Void> runBM_12(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[217]####
        // ensure Method variable is set//####[217]####
        if (__pt__runBM_12_Benchmark_method == null) {//####[217]####
            __pt__runBM_12_Benchmark_ensureMethodVarSet();//####[217]####
        }//####[217]####
        taskinfo.setTaskIdArgIndexes(0);//####[217]####
        taskinfo.addDependsOn(benchmark);//####[217]####
        taskinfo.setParameters(benchmark);//####[217]####
        taskinfo.setMethod(__pt__runBM_12_Benchmark_method);//####[217]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[217]####
    }//####[217]####
    private static TaskID<Void> runBM_12(BlockingQueue<Benchmark> benchmark) {//####[217]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[217]####
        return runBM_12(benchmark, new TaskInfo());//####[217]####
    }//####[217]####
    private static TaskID<Void> runBM_12(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[217]####
        // ensure Method variable is set//####[217]####
        if (__pt__runBM_12_Benchmark_method == null) {//####[217]####
            __pt__runBM_12_Benchmark_ensureMethodVarSet();//####[217]####
        }//####[217]####
        taskinfo.setQueueArgIndexes(0);//####[217]####
        taskinfo.setIsPipeline(true);//####[217]####
        taskinfo.setParameters(benchmark);//####[217]####
        taskinfo.setMethod(__pt__runBM_12_Benchmark_method);//####[217]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[217]####
    }//####[217]####
    public static void __pt__runBM_12(Benchmark benchmark) {//####[217]####
        try {//####[217]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[217]####
        } catch (IllegalAccessException e) {//####[217]####
            e.printStackTrace();//####[217]####
        } catch (IllegalArgumentException e) {//####[217]####
            e.printStackTrace();//####[217]####
        } catch (InvocationTargetException e) {//####[217]####
            e.printStackTrace();//####[217]####
        }//####[217]####
    }//####[217]####
//####[217]####
//####[218]####
    private static volatile Method __pt__runBM_15_Benchmark_method = null;//####[218]####
    private synchronized static void __pt__runBM_15_Benchmark_ensureMethodVarSet() {//####[218]####
        if (__pt__runBM_15_Benchmark_method == null) {//####[218]####
            try {//####[218]####
                __pt__runBM_15_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_15", new Class[] {//####[218]####
                    Benchmark.class//####[218]####
                });//####[218]####
            } catch (Exception e) {//####[218]####
                e.printStackTrace();//####[218]####
            }//####[218]####
        }//####[218]####
    }//####[218]####
    private static TaskID<Void> runBM_15(Benchmark benchmark) {//####[218]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[218]####
        return runBM_15(benchmark, new TaskInfo());//####[218]####
    }//####[218]####
    private static TaskID<Void> runBM_15(Benchmark benchmark, TaskInfo taskinfo) {//####[218]####
        // ensure Method variable is set//####[218]####
        if (__pt__runBM_15_Benchmark_method == null) {//####[218]####
            __pt__runBM_15_Benchmark_ensureMethodVarSet();//####[218]####
        }//####[218]####
        taskinfo.setParameters(benchmark);//####[218]####
        taskinfo.setMethod(__pt__runBM_15_Benchmark_method);//####[218]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[218]####
    }//####[218]####
    private static TaskID<Void> runBM_15(TaskID<Benchmark> benchmark) {//####[218]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[218]####
        return runBM_15(benchmark, new TaskInfo());//####[218]####
    }//####[218]####
    private static TaskID<Void> runBM_15(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[218]####
        // ensure Method variable is set//####[218]####
        if (__pt__runBM_15_Benchmark_method == null) {//####[218]####
            __pt__runBM_15_Benchmark_ensureMethodVarSet();//####[218]####
        }//####[218]####
        taskinfo.setTaskIdArgIndexes(0);//####[218]####
        taskinfo.addDependsOn(benchmark);//####[218]####
        taskinfo.setParameters(benchmark);//####[218]####
        taskinfo.setMethod(__pt__runBM_15_Benchmark_method);//####[218]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[218]####
    }//####[218]####
    private static TaskID<Void> runBM_15(BlockingQueue<Benchmark> benchmark) {//####[218]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[218]####
        return runBM_15(benchmark, new TaskInfo());//####[218]####
    }//####[218]####
    private static TaskID<Void> runBM_15(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[218]####
        // ensure Method variable is set//####[218]####
        if (__pt__runBM_15_Benchmark_method == null) {//####[218]####
            __pt__runBM_15_Benchmark_ensureMethodVarSet();//####[218]####
        }//####[218]####
        taskinfo.setQueueArgIndexes(0);//####[218]####
        taskinfo.setIsPipeline(true);//####[218]####
        taskinfo.setParameters(benchmark);//####[218]####
        taskinfo.setMethod(__pt__runBM_15_Benchmark_method);//####[218]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[218]####
    }//####[218]####
    public static void __pt__runBM_15(Benchmark benchmark) {//####[218]####
        try {//####[218]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[218]####
        } catch (IllegalAccessException e) {//####[218]####
            e.printStackTrace();//####[218]####
        } catch (IllegalArgumentException e) {//####[218]####
            e.printStackTrace();//####[218]####
        } catch (InvocationTargetException e) {//####[218]####
            e.printStackTrace();//####[218]####
        }//####[218]####
    }//####[218]####
//####[218]####
//####[219]####
    private static volatile Method __pt__runBM_16_Benchmark_method = null;//####[219]####
    private synchronized static void __pt__runBM_16_Benchmark_ensureMethodVarSet() {//####[219]####
        if (__pt__runBM_16_Benchmark_method == null) {//####[219]####
            try {//####[219]####
                __pt__runBM_16_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_16", new Class[] {//####[219]####
                    Benchmark.class//####[219]####
                });//####[219]####
            } catch (Exception e) {//####[219]####
                e.printStackTrace();//####[219]####
            }//####[219]####
        }//####[219]####
    }//####[219]####
    private static TaskID<Void> runBM_16(Benchmark benchmark) {//####[219]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[219]####
        return runBM_16(benchmark, new TaskInfo());//####[219]####
    }//####[219]####
    private static TaskID<Void> runBM_16(Benchmark benchmark, TaskInfo taskinfo) {//####[219]####
        // ensure Method variable is set//####[219]####
        if (__pt__runBM_16_Benchmark_method == null) {//####[219]####
            __pt__runBM_16_Benchmark_ensureMethodVarSet();//####[219]####
        }//####[219]####
        taskinfo.setParameters(benchmark);//####[219]####
        taskinfo.setMethod(__pt__runBM_16_Benchmark_method);//####[219]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[219]####
    }//####[219]####
    private static TaskID<Void> runBM_16(TaskID<Benchmark> benchmark) {//####[219]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[219]####
        return runBM_16(benchmark, new TaskInfo());//####[219]####
    }//####[219]####
    private static TaskID<Void> runBM_16(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[219]####
        // ensure Method variable is set//####[219]####
        if (__pt__runBM_16_Benchmark_method == null) {//####[219]####
            __pt__runBM_16_Benchmark_ensureMethodVarSet();//####[219]####
        }//####[219]####
        taskinfo.setTaskIdArgIndexes(0);//####[219]####
        taskinfo.addDependsOn(benchmark);//####[219]####
        taskinfo.setParameters(benchmark);//####[219]####
        taskinfo.setMethod(__pt__runBM_16_Benchmark_method);//####[219]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[219]####
    }//####[219]####
    private static TaskID<Void> runBM_16(BlockingQueue<Benchmark> benchmark) {//####[219]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[219]####
        return runBM_16(benchmark, new TaskInfo());//####[219]####
    }//####[219]####
    private static TaskID<Void> runBM_16(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[219]####
        // ensure Method variable is set//####[219]####
        if (__pt__runBM_16_Benchmark_method == null) {//####[219]####
            __pt__runBM_16_Benchmark_ensureMethodVarSet();//####[219]####
        }//####[219]####
        taskinfo.setQueueArgIndexes(0);//####[219]####
        taskinfo.setIsPipeline(true);//####[219]####
        taskinfo.setParameters(benchmark);//####[219]####
        taskinfo.setMethod(__pt__runBM_16_Benchmark_method);//####[219]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[219]####
    }//####[219]####
    public static void __pt__runBM_16(Benchmark benchmark) {//####[219]####
        try {//####[219]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[219]####
        } catch (IllegalAccessException e) {//####[219]####
            e.printStackTrace();//####[219]####
        } catch (IllegalArgumentException e) {//####[219]####
            e.printStackTrace();//####[219]####
        } catch (InvocationTargetException e) {//####[219]####
            e.printStackTrace();//####[219]####
        }//####[219]####
    }//####[219]####
//####[219]####
//####[220]####
    private static volatile Method __pt__runBM_20_Benchmark_method = null;//####[220]####
    private synchronized static void __pt__runBM_20_Benchmark_ensureMethodVarSet() {//####[220]####
        if (__pt__runBM_20_Benchmark_method == null) {//####[220]####
            try {//####[220]####
                __pt__runBM_20_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_20", new Class[] {//####[220]####
                    Benchmark.class//####[220]####
                });//####[220]####
            } catch (Exception e) {//####[220]####
                e.printStackTrace();//####[220]####
            }//####[220]####
        }//####[220]####
    }//####[220]####
    private static TaskID<Void> runBM_20(Benchmark benchmark) {//####[220]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[220]####
        return runBM_20(benchmark, new TaskInfo());//####[220]####
    }//####[220]####
    private static TaskID<Void> runBM_20(Benchmark benchmark, TaskInfo taskinfo) {//####[220]####
        // ensure Method variable is set//####[220]####
        if (__pt__runBM_20_Benchmark_method == null) {//####[220]####
            __pt__runBM_20_Benchmark_ensureMethodVarSet();//####[220]####
        }//####[220]####
        taskinfo.setParameters(benchmark);//####[220]####
        taskinfo.setMethod(__pt__runBM_20_Benchmark_method);//####[220]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[220]####
    }//####[220]####
    private static TaskID<Void> runBM_20(TaskID<Benchmark> benchmark) {//####[220]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[220]####
        return runBM_20(benchmark, new TaskInfo());//####[220]####
    }//####[220]####
    private static TaskID<Void> runBM_20(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[220]####
        // ensure Method variable is set//####[220]####
        if (__pt__runBM_20_Benchmark_method == null) {//####[220]####
            __pt__runBM_20_Benchmark_ensureMethodVarSet();//####[220]####
        }//####[220]####
        taskinfo.setTaskIdArgIndexes(0);//####[220]####
        taskinfo.addDependsOn(benchmark);//####[220]####
        taskinfo.setParameters(benchmark);//####[220]####
        taskinfo.setMethod(__pt__runBM_20_Benchmark_method);//####[220]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[220]####
    }//####[220]####
    private static TaskID<Void> runBM_20(BlockingQueue<Benchmark> benchmark) {//####[220]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[220]####
        return runBM_20(benchmark, new TaskInfo());//####[220]####
    }//####[220]####
    private static TaskID<Void> runBM_20(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[220]####
        // ensure Method variable is set//####[220]####
        if (__pt__runBM_20_Benchmark_method == null) {//####[220]####
            __pt__runBM_20_Benchmark_ensureMethodVarSet();//####[220]####
        }//####[220]####
        taskinfo.setQueueArgIndexes(0);//####[220]####
        taskinfo.setIsPipeline(true);//####[220]####
        taskinfo.setParameters(benchmark);//####[220]####
        taskinfo.setMethod(__pt__runBM_20_Benchmark_method);//####[220]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[220]####
    }//####[220]####
    public static void __pt__runBM_20(Benchmark benchmark) {//####[220]####
        try {//####[220]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[220]####
        } catch (IllegalAccessException e) {//####[220]####
            e.printStackTrace();//####[220]####
        } catch (IllegalArgumentException e) {//####[220]####
            e.printStackTrace();//####[220]####
        } catch (InvocationTargetException e) {//####[220]####
            e.printStackTrace();//####[220]####
        }//####[220]####
    }//####[220]####
//####[220]####
//####[221]####
    private static volatile Method __pt__runBM_24_Benchmark_method = null;//####[221]####
    private synchronized static void __pt__runBM_24_Benchmark_ensureMethodVarSet() {//####[221]####
        if (__pt__runBM_24_Benchmark_method == null) {//####[221]####
            try {//####[221]####
                __pt__runBM_24_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_24", new Class[] {//####[221]####
                    Benchmark.class//####[221]####
                });//####[221]####
            } catch (Exception e) {//####[221]####
                e.printStackTrace();//####[221]####
            }//####[221]####
        }//####[221]####
    }//####[221]####
    private static TaskID<Void> runBM_24(Benchmark benchmark) {//####[221]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[221]####
        return runBM_24(benchmark, new TaskInfo());//####[221]####
    }//####[221]####
    private static TaskID<Void> runBM_24(Benchmark benchmark, TaskInfo taskinfo) {//####[221]####
        // ensure Method variable is set//####[221]####
        if (__pt__runBM_24_Benchmark_method == null) {//####[221]####
            __pt__runBM_24_Benchmark_ensureMethodVarSet();//####[221]####
        }//####[221]####
        taskinfo.setParameters(benchmark);//####[221]####
        taskinfo.setMethod(__pt__runBM_24_Benchmark_method);//####[221]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[221]####
    }//####[221]####
    private static TaskID<Void> runBM_24(TaskID<Benchmark> benchmark) {//####[221]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[221]####
        return runBM_24(benchmark, new TaskInfo());//####[221]####
    }//####[221]####
    private static TaskID<Void> runBM_24(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[221]####
        // ensure Method variable is set//####[221]####
        if (__pt__runBM_24_Benchmark_method == null) {//####[221]####
            __pt__runBM_24_Benchmark_ensureMethodVarSet();//####[221]####
        }//####[221]####
        taskinfo.setTaskIdArgIndexes(0);//####[221]####
        taskinfo.addDependsOn(benchmark);//####[221]####
        taskinfo.setParameters(benchmark);//####[221]####
        taskinfo.setMethod(__pt__runBM_24_Benchmark_method);//####[221]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[221]####
    }//####[221]####
    private static TaskID<Void> runBM_24(BlockingQueue<Benchmark> benchmark) {//####[221]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[221]####
        return runBM_24(benchmark, new TaskInfo());//####[221]####
    }//####[221]####
    private static TaskID<Void> runBM_24(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[221]####
        // ensure Method variable is set//####[221]####
        if (__pt__runBM_24_Benchmark_method == null) {//####[221]####
            __pt__runBM_24_Benchmark_ensureMethodVarSet();//####[221]####
        }//####[221]####
        taskinfo.setQueueArgIndexes(0);//####[221]####
        taskinfo.setIsPipeline(true);//####[221]####
        taskinfo.setParameters(benchmark);//####[221]####
        taskinfo.setMethod(__pt__runBM_24_Benchmark_method);//####[221]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[221]####
    }//####[221]####
    public static void __pt__runBM_24(Benchmark benchmark) {//####[221]####
        try {//####[221]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[221]####
        } catch (IllegalAccessException e) {//####[221]####
            e.printStackTrace();//####[221]####
        } catch (IllegalArgumentException e) {//####[221]####
            e.printStackTrace();//####[221]####
        } catch (InvocationTargetException e) {//####[221]####
            e.printStackTrace();//####[221]####
        }//####[221]####
    }//####[221]####
//####[221]####
//####[222]####
    private static volatile Method __pt__runBM_28_Benchmark_method = null;//####[222]####
    private synchronized static void __pt__runBM_28_Benchmark_ensureMethodVarSet() {//####[222]####
        if (__pt__runBM_28_Benchmark_method == null) {//####[222]####
            try {//####[222]####
                __pt__runBM_28_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_28", new Class[] {//####[222]####
                    Benchmark.class//####[222]####
                });//####[222]####
            } catch (Exception e) {//####[222]####
                e.printStackTrace();//####[222]####
            }//####[222]####
        }//####[222]####
    }//####[222]####
    private static TaskID<Void> runBM_28(Benchmark benchmark) {//####[222]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[222]####
        return runBM_28(benchmark, new TaskInfo());//####[222]####
    }//####[222]####
    private static TaskID<Void> runBM_28(Benchmark benchmark, TaskInfo taskinfo) {//####[222]####
        // ensure Method variable is set//####[222]####
        if (__pt__runBM_28_Benchmark_method == null) {//####[222]####
            __pt__runBM_28_Benchmark_ensureMethodVarSet();//####[222]####
        }//####[222]####
        taskinfo.setParameters(benchmark);//####[222]####
        taskinfo.setMethod(__pt__runBM_28_Benchmark_method);//####[222]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[222]####
    }//####[222]####
    private static TaskID<Void> runBM_28(TaskID<Benchmark> benchmark) {//####[222]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[222]####
        return runBM_28(benchmark, new TaskInfo());//####[222]####
    }//####[222]####
    private static TaskID<Void> runBM_28(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[222]####
        // ensure Method variable is set//####[222]####
        if (__pt__runBM_28_Benchmark_method == null) {//####[222]####
            __pt__runBM_28_Benchmark_ensureMethodVarSet();//####[222]####
        }//####[222]####
        taskinfo.setTaskIdArgIndexes(0);//####[222]####
        taskinfo.addDependsOn(benchmark);//####[222]####
        taskinfo.setParameters(benchmark);//####[222]####
        taskinfo.setMethod(__pt__runBM_28_Benchmark_method);//####[222]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[222]####
    }//####[222]####
    private static TaskID<Void> runBM_28(BlockingQueue<Benchmark> benchmark) {//####[222]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[222]####
        return runBM_28(benchmark, new TaskInfo());//####[222]####
    }//####[222]####
    private static TaskID<Void> runBM_28(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[222]####
        // ensure Method variable is set//####[222]####
        if (__pt__runBM_28_Benchmark_method == null) {//####[222]####
            __pt__runBM_28_Benchmark_ensureMethodVarSet();//####[222]####
        }//####[222]####
        taskinfo.setQueueArgIndexes(0);//####[222]####
        taskinfo.setIsPipeline(true);//####[222]####
        taskinfo.setParameters(benchmark);//####[222]####
        taskinfo.setMethod(__pt__runBM_28_Benchmark_method);//####[222]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[222]####
    }//####[222]####
    public static void __pt__runBM_28(Benchmark benchmark) {//####[222]####
        try {//####[222]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[222]####
        } catch (IllegalAccessException e) {//####[222]####
            e.printStackTrace();//####[222]####
        } catch (IllegalArgumentException e) {//####[222]####
            e.printStackTrace();//####[222]####
        } catch (InvocationTargetException e) {//####[222]####
            e.printStackTrace();//####[222]####
        }//####[222]####
    }//####[222]####
//####[222]####
//####[223]####
    private static volatile Method __pt__runBM_30_Benchmark_method = null;//####[223]####
    private synchronized static void __pt__runBM_30_Benchmark_ensureMethodVarSet() {//####[223]####
        if (__pt__runBM_30_Benchmark_method == null) {//####[223]####
            try {//####[223]####
                __pt__runBM_30_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_30", new Class[] {//####[223]####
                    Benchmark.class//####[223]####
                });//####[223]####
            } catch (Exception e) {//####[223]####
                e.printStackTrace();//####[223]####
            }//####[223]####
        }//####[223]####
    }//####[223]####
    private static TaskID<Void> runBM_30(Benchmark benchmark) {//####[223]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[223]####
        return runBM_30(benchmark, new TaskInfo());//####[223]####
    }//####[223]####
    private static TaskID<Void> runBM_30(Benchmark benchmark, TaskInfo taskinfo) {//####[223]####
        // ensure Method variable is set//####[223]####
        if (__pt__runBM_30_Benchmark_method == null) {//####[223]####
            __pt__runBM_30_Benchmark_ensureMethodVarSet();//####[223]####
        }//####[223]####
        taskinfo.setParameters(benchmark);//####[223]####
        taskinfo.setMethod(__pt__runBM_30_Benchmark_method);//####[223]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[223]####
    }//####[223]####
    private static TaskID<Void> runBM_30(TaskID<Benchmark> benchmark) {//####[223]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[223]####
        return runBM_30(benchmark, new TaskInfo());//####[223]####
    }//####[223]####
    private static TaskID<Void> runBM_30(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[223]####
        // ensure Method variable is set//####[223]####
        if (__pt__runBM_30_Benchmark_method == null) {//####[223]####
            __pt__runBM_30_Benchmark_ensureMethodVarSet();//####[223]####
        }//####[223]####
        taskinfo.setTaskIdArgIndexes(0);//####[223]####
        taskinfo.addDependsOn(benchmark);//####[223]####
        taskinfo.setParameters(benchmark);//####[223]####
        taskinfo.setMethod(__pt__runBM_30_Benchmark_method);//####[223]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[223]####
    }//####[223]####
    private static TaskID<Void> runBM_30(BlockingQueue<Benchmark> benchmark) {//####[223]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[223]####
        return runBM_30(benchmark, new TaskInfo());//####[223]####
    }//####[223]####
    private static TaskID<Void> runBM_30(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[223]####
        // ensure Method variable is set//####[223]####
        if (__pt__runBM_30_Benchmark_method == null) {//####[223]####
            __pt__runBM_30_Benchmark_ensureMethodVarSet();//####[223]####
        }//####[223]####
        taskinfo.setQueueArgIndexes(0);//####[223]####
        taskinfo.setIsPipeline(true);//####[223]####
        taskinfo.setParameters(benchmark);//####[223]####
        taskinfo.setMethod(__pt__runBM_30_Benchmark_method);//####[223]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[223]####
    }//####[223]####
    public static void __pt__runBM_30(Benchmark benchmark) {//####[223]####
        try {//####[223]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[223]####
        } catch (IllegalAccessException e) {//####[223]####
            e.printStackTrace();//####[223]####
        } catch (IllegalArgumentException e) {//####[223]####
            e.printStackTrace();//####[223]####
        } catch (InvocationTargetException e) {//####[223]####
            e.printStackTrace();//####[223]####
        }//####[223]####
    }//####[223]####
//####[223]####
//####[224]####
    private static volatile Method __pt__runBM_31_Benchmark_method = null;//####[224]####
    private synchronized static void __pt__runBM_31_Benchmark_ensureMethodVarSet() {//####[224]####
        if (__pt__runBM_31_Benchmark_method == null) {//####[224]####
            try {//####[224]####
                __pt__runBM_31_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_31", new Class[] {//####[224]####
                    Benchmark.class//####[224]####
                });//####[224]####
            } catch (Exception e) {//####[224]####
                e.printStackTrace();//####[224]####
            }//####[224]####
        }//####[224]####
    }//####[224]####
    private static TaskID<Void> runBM_31(Benchmark benchmark) {//####[224]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[224]####
        return runBM_31(benchmark, new TaskInfo());//####[224]####
    }//####[224]####
    private static TaskID<Void> runBM_31(Benchmark benchmark, TaskInfo taskinfo) {//####[224]####
        // ensure Method variable is set//####[224]####
        if (__pt__runBM_31_Benchmark_method == null) {//####[224]####
            __pt__runBM_31_Benchmark_ensureMethodVarSet();//####[224]####
        }//####[224]####
        taskinfo.setParameters(benchmark);//####[224]####
        taskinfo.setMethod(__pt__runBM_31_Benchmark_method);//####[224]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[224]####
    }//####[224]####
    private static TaskID<Void> runBM_31(TaskID<Benchmark> benchmark) {//####[224]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[224]####
        return runBM_31(benchmark, new TaskInfo());//####[224]####
    }//####[224]####
    private static TaskID<Void> runBM_31(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[224]####
        // ensure Method variable is set//####[224]####
        if (__pt__runBM_31_Benchmark_method == null) {//####[224]####
            __pt__runBM_31_Benchmark_ensureMethodVarSet();//####[224]####
        }//####[224]####
        taskinfo.setTaskIdArgIndexes(0);//####[224]####
        taskinfo.addDependsOn(benchmark);//####[224]####
        taskinfo.setParameters(benchmark);//####[224]####
        taskinfo.setMethod(__pt__runBM_31_Benchmark_method);//####[224]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[224]####
    }//####[224]####
    private static TaskID<Void> runBM_31(BlockingQueue<Benchmark> benchmark) {//####[224]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[224]####
        return runBM_31(benchmark, new TaskInfo());//####[224]####
    }//####[224]####
    private static TaskID<Void> runBM_31(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[224]####
        // ensure Method variable is set//####[224]####
        if (__pt__runBM_31_Benchmark_method == null) {//####[224]####
            __pt__runBM_31_Benchmark_ensureMethodVarSet();//####[224]####
        }//####[224]####
        taskinfo.setQueueArgIndexes(0);//####[224]####
        taskinfo.setIsPipeline(true);//####[224]####
        taskinfo.setParameters(benchmark);//####[224]####
        taskinfo.setMethod(__pt__runBM_31_Benchmark_method);//####[224]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[224]####
    }//####[224]####
    public static void __pt__runBM_31(Benchmark benchmark) {//####[224]####
        try {//####[224]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[224]####
        } catch (IllegalAccessException e) {//####[224]####
            e.printStackTrace();//####[224]####
        } catch (IllegalArgumentException e) {//####[224]####
            e.printStackTrace();//####[224]####
        } catch (InvocationTargetException e) {//####[224]####
            e.printStackTrace();//####[224]####
        }//####[224]####
    }//####[224]####
//####[224]####
//####[225]####
    private static volatile Method __pt__runBM_33_Benchmark_method = null;//####[225]####
    private synchronized static void __pt__runBM_33_Benchmark_ensureMethodVarSet() {//####[225]####
        if (__pt__runBM_33_Benchmark_method == null) {//####[225]####
            try {//####[225]####
                __pt__runBM_33_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_33", new Class[] {//####[225]####
                    Benchmark.class//####[225]####
                });//####[225]####
            } catch (Exception e) {//####[225]####
                e.printStackTrace();//####[225]####
            }//####[225]####
        }//####[225]####
    }//####[225]####
    private static TaskID<Void> runBM_33(Benchmark benchmark) {//####[225]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[225]####
        return runBM_33(benchmark, new TaskInfo());//####[225]####
    }//####[225]####
    private static TaskID<Void> runBM_33(Benchmark benchmark, TaskInfo taskinfo) {//####[225]####
        // ensure Method variable is set//####[225]####
        if (__pt__runBM_33_Benchmark_method == null) {//####[225]####
            __pt__runBM_33_Benchmark_ensureMethodVarSet();//####[225]####
        }//####[225]####
        taskinfo.setParameters(benchmark);//####[225]####
        taskinfo.setMethod(__pt__runBM_33_Benchmark_method);//####[225]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[225]####
    }//####[225]####
    private static TaskID<Void> runBM_33(TaskID<Benchmark> benchmark) {//####[225]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[225]####
        return runBM_33(benchmark, new TaskInfo());//####[225]####
    }//####[225]####
    private static TaskID<Void> runBM_33(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[225]####
        // ensure Method variable is set//####[225]####
        if (__pt__runBM_33_Benchmark_method == null) {//####[225]####
            __pt__runBM_33_Benchmark_ensureMethodVarSet();//####[225]####
        }//####[225]####
        taskinfo.setTaskIdArgIndexes(0);//####[225]####
        taskinfo.addDependsOn(benchmark);//####[225]####
        taskinfo.setParameters(benchmark);//####[225]####
        taskinfo.setMethod(__pt__runBM_33_Benchmark_method);//####[225]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[225]####
    }//####[225]####
    private static TaskID<Void> runBM_33(BlockingQueue<Benchmark> benchmark) {//####[225]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[225]####
        return runBM_33(benchmark, new TaskInfo());//####[225]####
    }//####[225]####
    private static TaskID<Void> runBM_33(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[225]####
        // ensure Method variable is set//####[225]####
        if (__pt__runBM_33_Benchmark_method == null) {//####[225]####
            __pt__runBM_33_Benchmark_ensureMethodVarSet();//####[225]####
        }//####[225]####
        taskinfo.setQueueArgIndexes(0);//####[225]####
        taskinfo.setIsPipeline(true);//####[225]####
        taskinfo.setParameters(benchmark);//####[225]####
        taskinfo.setMethod(__pt__runBM_33_Benchmark_method);//####[225]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[225]####
    }//####[225]####
    public static void __pt__runBM_33(Benchmark benchmark) {//####[225]####
        try {//####[225]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[225]####
        } catch (IllegalAccessException e) {//####[225]####
            e.printStackTrace();//####[225]####
        } catch (IllegalArgumentException e) {//####[225]####
            e.printStackTrace();//####[225]####
        } catch (InvocationTargetException e) {//####[225]####
            e.printStackTrace();//####[225]####
        }//####[225]####
    }//####[225]####
//####[225]####
//####[226]####
    private static volatile Method __pt__runBM_34_Benchmark_method = null;//####[226]####
    private synchronized static void __pt__runBM_34_Benchmark_ensureMethodVarSet() {//####[226]####
        if (__pt__runBM_34_Benchmark_method == null) {//####[226]####
            try {//####[226]####
                __pt__runBM_34_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_34", new Class[] {//####[226]####
                    Benchmark.class//####[226]####
                });//####[226]####
            } catch (Exception e) {//####[226]####
                e.printStackTrace();//####[226]####
            }//####[226]####
        }//####[226]####
    }//####[226]####
    private static TaskID<Void> runBM_34(Benchmark benchmark) {//####[226]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[226]####
        return runBM_34(benchmark, new TaskInfo());//####[226]####
    }//####[226]####
    private static TaskID<Void> runBM_34(Benchmark benchmark, TaskInfo taskinfo) {//####[226]####
        // ensure Method variable is set//####[226]####
        if (__pt__runBM_34_Benchmark_method == null) {//####[226]####
            __pt__runBM_34_Benchmark_ensureMethodVarSet();//####[226]####
        }//####[226]####
        taskinfo.setParameters(benchmark);//####[226]####
        taskinfo.setMethod(__pt__runBM_34_Benchmark_method);//####[226]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[226]####
    }//####[226]####
    private static TaskID<Void> runBM_34(TaskID<Benchmark> benchmark) {//####[226]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[226]####
        return runBM_34(benchmark, new TaskInfo());//####[226]####
    }//####[226]####
    private static TaskID<Void> runBM_34(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[226]####
        // ensure Method variable is set//####[226]####
        if (__pt__runBM_34_Benchmark_method == null) {//####[226]####
            __pt__runBM_34_Benchmark_ensureMethodVarSet();//####[226]####
        }//####[226]####
        taskinfo.setTaskIdArgIndexes(0);//####[226]####
        taskinfo.addDependsOn(benchmark);//####[226]####
        taskinfo.setParameters(benchmark);//####[226]####
        taskinfo.setMethod(__pt__runBM_34_Benchmark_method);//####[226]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[226]####
    }//####[226]####
    private static TaskID<Void> runBM_34(BlockingQueue<Benchmark> benchmark) {//####[226]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[226]####
        return runBM_34(benchmark, new TaskInfo());//####[226]####
    }//####[226]####
    private static TaskID<Void> runBM_34(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[226]####
        // ensure Method variable is set//####[226]####
        if (__pt__runBM_34_Benchmark_method == null) {//####[226]####
            __pt__runBM_34_Benchmark_ensureMethodVarSet();//####[226]####
        }//####[226]####
        taskinfo.setQueueArgIndexes(0);//####[226]####
        taskinfo.setIsPipeline(true);//####[226]####
        taskinfo.setParameters(benchmark);//####[226]####
        taskinfo.setMethod(__pt__runBM_34_Benchmark_method);//####[226]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[226]####
    }//####[226]####
    public static void __pt__runBM_34(Benchmark benchmark) {//####[226]####
        try {//####[226]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[226]####
        } catch (IllegalAccessException e) {//####[226]####
            e.printStackTrace();//####[226]####
        } catch (IllegalArgumentException e) {//####[226]####
            e.printStackTrace();//####[226]####
        } catch (InvocationTargetException e) {//####[226]####
            e.printStackTrace();//####[226]####
        }//####[226]####
    }//####[226]####
//####[226]####
//####[227]####
    private static volatile Method __pt__runBM_35_Benchmark_method = null;//####[227]####
    private synchronized static void __pt__runBM_35_Benchmark_ensureMethodVarSet() {//####[227]####
        if (__pt__runBM_35_Benchmark_method == null) {//####[227]####
            try {//####[227]####
                __pt__runBM_35_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_35", new Class[] {//####[227]####
                    Benchmark.class//####[227]####
                });//####[227]####
            } catch (Exception e) {//####[227]####
                e.printStackTrace();//####[227]####
            }//####[227]####
        }//####[227]####
    }//####[227]####
    private static TaskID<Void> runBM_35(Benchmark benchmark) {//####[227]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[227]####
        return runBM_35(benchmark, new TaskInfo());//####[227]####
    }//####[227]####
    private static TaskID<Void> runBM_35(Benchmark benchmark, TaskInfo taskinfo) {//####[227]####
        // ensure Method variable is set//####[227]####
        if (__pt__runBM_35_Benchmark_method == null) {//####[227]####
            __pt__runBM_35_Benchmark_ensureMethodVarSet();//####[227]####
        }//####[227]####
        taskinfo.setParameters(benchmark);//####[227]####
        taskinfo.setMethod(__pt__runBM_35_Benchmark_method);//####[227]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[227]####
    }//####[227]####
    private static TaskID<Void> runBM_35(TaskID<Benchmark> benchmark) {//####[227]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[227]####
        return runBM_35(benchmark, new TaskInfo());//####[227]####
    }//####[227]####
    private static TaskID<Void> runBM_35(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[227]####
        // ensure Method variable is set//####[227]####
        if (__pt__runBM_35_Benchmark_method == null) {//####[227]####
            __pt__runBM_35_Benchmark_ensureMethodVarSet();//####[227]####
        }//####[227]####
        taskinfo.setTaskIdArgIndexes(0);//####[227]####
        taskinfo.addDependsOn(benchmark);//####[227]####
        taskinfo.setParameters(benchmark);//####[227]####
        taskinfo.setMethod(__pt__runBM_35_Benchmark_method);//####[227]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[227]####
    }//####[227]####
    private static TaskID<Void> runBM_35(BlockingQueue<Benchmark> benchmark) {//####[227]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[227]####
        return runBM_35(benchmark, new TaskInfo());//####[227]####
    }//####[227]####
    private static TaskID<Void> runBM_35(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[227]####
        // ensure Method variable is set//####[227]####
        if (__pt__runBM_35_Benchmark_method == null) {//####[227]####
            __pt__runBM_35_Benchmark_ensureMethodVarSet();//####[227]####
        }//####[227]####
        taskinfo.setQueueArgIndexes(0);//####[227]####
        taskinfo.setIsPipeline(true);//####[227]####
        taskinfo.setParameters(benchmark);//####[227]####
        taskinfo.setMethod(__pt__runBM_35_Benchmark_method);//####[227]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[227]####
    }//####[227]####
    public static void __pt__runBM_35(Benchmark benchmark) {//####[227]####
        try {//####[227]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[227]####
        } catch (IllegalAccessException e) {//####[227]####
            e.printStackTrace();//####[227]####
        } catch (IllegalArgumentException e) {//####[227]####
            e.printStackTrace();//####[227]####
        } catch (InvocationTargetException e) {//####[227]####
            e.printStackTrace();//####[227]####
        }//####[227]####
    }//####[227]####
//####[227]####
//####[228]####
    private static volatile Method __pt__runBM_36_Benchmark_method = null;//####[228]####
    private synchronized static void __pt__runBM_36_Benchmark_ensureMethodVarSet() {//####[228]####
        if (__pt__runBM_36_Benchmark_method == null) {//####[228]####
            try {//####[228]####
                __pt__runBM_36_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_36", new Class[] {//####[228]####
                    Benchmark.class//####[228]####
                });//####[228]####
            } catch (Exception e) {//####[228]####
                e.printStackTrace();//####[228]####
            }//####[228]####
        }//####[228]####
    }//####[228]####
    private static TaskID<Void> runBM_36(Benchmark benchmark) {//####[228]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[228]####
        return runBM_36(benchmark, new TaskInfo());//####[228]####
    }//####[228]####
    private static TaskID<Void> runBM_36(Benchmark benchmark, TaskInfo taskinfo) {//####[228]####
        // ensure Method variable is set//####[228]####
        if (__pt__runBM_36_Benchmark_method == null) {//####[228]####
            __pt__runBM_36_Benchmark_ensureMethodVarSet();//####[228]####
        }//####[228]####
        taskinfo.setParameters(benchmark);//####[228]####
        taskinfo.setMethod(__pt__runBM_36_Benchmark_method);//####[228]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[228]####
    }//####[228]####
    private static TaskID<Void> runBM_36(TaskID<Benchmark> benchmark) {//####[228]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[228]####
        return runBM_36(benchmark, new TaskInfo());//####[228]####
    }//####[228]####
    private static TaskID<Void> runBM_36(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[228]####
        // ensure Method variable is set//####[228]####
        if (__pt__runBM_36_Benchmark_method == null) {//####[228]####
            __pt__runBM_36_Benchmark_ensureMethodVarSet();//####[228]####
        }//####[228]####
        taskinfo.setTaskIdArgIndexes(0);//####[228]####
        taskinfo.addDependsOn(benchmark);//####[228]####
        taskinfo.setParameters(benchmark);//####[228]####
        taskinfo.setMethod(__pt__runBM_36_Benchmark_method);//####[228]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[228]####
    }//####[228]####
    private static TaskID<Void> runBM_36(BlockingQueue<Benchmark> benchmark) {//####[228]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[228]####
        return runBM_36(benchmark, new TaskInfo());//####[228]####
    }//####[228]####
    private static TaskID<Void> runBM_36(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[228]####
        // ensure Method variable is set//####[228]####
        if (__pt__runBM_36_Benchmark_method == null) {//####[228]####
            __pt__runBM_36_Benchmark_ensureMethodVarSet();//####[228]####
        }//####[228]####
        taskinfo.setQueueArgIndexes(0);//####[228]####
        taskinfo.setIsPipeline(true);//####[228]####
        taskinfo.setParameters(benchmark);//####[228]####
        taskinfo.setMethod(__pt__runBM_36_Benchmark_method);//####[228]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[228]####
    }//####[228]####
    public static void __pt__runBM_36(Benchmark benchmark) {//####[228]####
        try {//####[228]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[228]####
        } catch (IllegalAccessException e) {//####[228]####
            e.printStackTrace();//####[228]####
        } catch (IllegalArgumentException e) {//####[228]####
            e.printStackTrace();//####[228]####
        } catch (InvocationTargetException e) {//####[228]####
            e.printStackTrace();//####[228]####
        }//####[228]####
    }//####[228]####
//####[228]####
//####[229]####
    private static volatile Method __pt__runBM_37_Benchmark_method = null;//####[229]####
    private synchronized static void __pt__runBM_37_Benchmark_ensureMethodVarSet() {//####[229]####
        if (__pt__runBM_37_Benchmark_method == null) {//####[229]####
            try {//####[229]####
                __pt__runBM_37_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_37", new Class[] {//####[229]####
                    Benchmark.class//####[229]####
                });//####[229]####
            } catch (Exception e) {//####[229]####
                e.printStackTrace();//####[229]####
            }//####[229]####
        }//####[229]####
    }//####[229]####
    private static TaskID<Void> runBM_37(Benchmark benchmark) {//####[229]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[229]####
        return runBM_37(benchmark, new TaskInfo());//####[229]####
    }//####[229]####
    private static TaskID<Void> runBM_37(Benchmark benchmark, TaskInfo taskinfo) {//####[229]####
        // ensure Method variable is set//####[229]####
        if (__pt__runBM_37_Benchmark_method == null) {//####[229]####
            __pt__runBM_37_Benchmark_ensureMethodVarSet();//####[229]####
        }//####[229]####
        taskinfo.setParameters(benchmark);//####[229]####
        taskinfo.setMethod(__pt__runBM_37_Benchmark_method);//####[229]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[229]####
    }//####[229]####
    private static TaskID<Void> runBM_37(TaskID<Benchmark> benchmark) {//####[229]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[229]####
        return runBM_37(benchmark, new TaskInfo());//####[229]####
    }//####[229]####
    private static TaskID<Void> runBM_37(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[229]####
        // ensure Method variable is set//####[229]####
        if (__pt__runBM_37_Benchmark_method == null) {//####[229]####
            __pt__runBM_37_Benchmark_ensureMethodVarSet();//####[229]####
        }//####[229]####
        taskinfo.setTaskIdArgIndexes(0);//####[229]####
        taskinfo.addDependsOn(benchmark);//####[229]####
        taskinfo.setParameters(benchmark);//####[229]####
        taskinfo.setMethod(__pt__runBM_37_Benchmark_method);//####[229]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[229]####
    }//####[229]####
    private static TaskID<Void> runBM_37(BlockingQueue<Benchmark> benchmark) {//####[229]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[229]####
        return runBM_37(benchmark, new TaskInfo());//####[229]####
    }//####[229]####
    private static TaskID<Void> runBM_37(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[229]####
        // ensure Method variable is set//####[229]####
        if (__pt__runBM_37_Benchmark_method == null) {//####[229]####
            __pt__runBM_37_Benchmark_ensureMethodVarSet();//####[229]####
        }//####[229]####
        taskinfo.setQueueArgIndexes(0);//####[229]####
        taskinfo.setIsPipeline(true);//####[229]####
        taskinfo.setParameters(benchmark);//####[229]####
        taskinfo.setMethod(__pt__runBM_37_Benchmark_method);//####[229]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[229]####
    }//####[229]####
    public static void __pt__runBM_37(Benchmark benchmark) {//####[229]####
        try {//####[229]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[229]####
        } catch (IllegalAccessException e) {//####[229]####
            e.printStackTrace();//####[229]####
        } catch (IllegalArgumentException e) {//####[229]####
            e.printStackTrace();//####[229]####
        } catch (InvocationTargetException e) {//####[229]####
            e.printStackTrace();//####[229]####
        }//####[229]####
    }//####[229]####
//####[229]####
//####[230]####
    private static volatile Method __pt__runBM_39_Benchmark_method = null;//####[230]####
    private synchronized static void __pt__runBM_39_Benchmark_ensureMethodVarSet() {//####[230]####
        if (__pt__runBM_39_Benchmark_method == null) {//####[230]####
            try {//####[230]####
                __pt__runBM_39_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_39", new Class[] {//####[230]####
                    Benchmark.class//####[230]####
                });//####[230]####
            } catch (Exception e) {//####[230]####
                e.printStackTrace();//####[230]####
            }//####[230]####
        }//####[230]####
    }//####[230]####
    private static TaskID<Void> runBM_39(Benchmark benchmark) {//####[230]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[230]####
        return runBM_39(benchmark, new TaskInfo());//####[230]####
    }//####[230]####
    private static TaskID<Void> runBM_39(Benchmark benchmark, TaskInfo taskinfo) {//####[230]####
        // ensure Method variable is set//####[230]####
        if (__pt__runBM_39_Benchmark_method == null) {//####[230]####
            __pt__runBM_39_Benchmark_ensureMethodVarSet();//####[230]####
        }//####[230]####
        taskinfo.setParameters(benchmark);//####[230]####
        taskinfo.setMethod(__pt__runBM_39_Benchmark_method);//####[230]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[230]####
    }//####[230]####
    private static TaskID<Void> runBM_39(TaskID<Benchmark> benchmark) {//####[230]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[230]####
        return runBM_39(benchmark, new TaskInfo());//####[230]####
    }//####[230]####
    private static TaskID<Void> runBM_39(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[230]####
        // ensure Method variable is set//####[230]####
        if (__pt__runBM_39_Benchmark_method == null) {//####[230]####
            __pt__runBM_39_Benchmark_ensureMethodVarSet();//####[230]####
        }//####[230]####
        taskinfo.setTaskIdArgIndexes(0);//####[230]####
        taskinfo.addDependsOn(benchmark);//####[230]####
        taskinfo.setParameters(benchmark);//####[230]####
        taskinfo.setMethod(__pt__runBM_39_Benchmark_method);//####[230]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[230]####
    }//####[230]####
    private static TaskID<Void> runBM_39(BlockingQueue<Benchmark> benchmark) {//####[230]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[230]####
        return runBM_39(benchmark, new TaskInfo());//####[230]####
    }//####[230]####
    private static TaskID<Void> runBM_39(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[230]####
        // ensure Method variable is set//####[230]####
        if (__pt__runBM_39_Benchmark_method == null) {//####[230]####
            __pt__runBM_39_Benchmark_ensureMethodVarSet();//####[230]####
        }//####[230]####
        taskinfo.setQueueArgIndexes(0);//####[230]####
        taskinfo.setIsPipeline(true);//####[230]####
        taskinfo.setParameters(benchmark);//####[230]####
        taskinfo.setMethod(__pt__runBM_39_Benchmark_method);//####[230]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[230]####
    }//####[230]####
    public static void __pt__runBM_39(Benchmark benchmark) {//####[230]####
        try {//####[230]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[230]####
        } catch (IllegalAccessException e) {//####[230]####
            e.printStackTrace();//####[230]####
        } catch (IllegalArgumentException e) {//####[230]####
            e.printStackTrace();//####[230]####
        } catch (InvocationTargetException e) {//####[230]####
            e.printStackTrace();//####[230]####
        }//####[230]####
    }//####[230]####
//####[230]####
//####[231]####
    private static volatile Method __pt__runBM_40_Benchmark_method = null;//####[231]####
    private synchronized static void __pt__runBM_40_Benchmark_ensureMethodVarSet() {//####[231]####
        if (__pt__runBM_40_Benchmark_method == null) {//####[231]####
            try {//####[231]####
                __pt__runBM_40_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_40", new Class[] {//####[231]####
                    Benchmark.class//####[231]####
                });//####[231]####
            } catch (Exception e) {//####[231]####
                e.printStackTrace();//####[231]####
            }//####[231]####
        }//####[231]####
    }//####[231]####
    private static TaskID<Void> runBM_40(Benchmark benchmark) {//####[231]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[231]####
        return runBM_40(benchmark, new TaskInfo());//####[231]####
    }//####[231]####
    private static TaskID<Void> runBM_40(Benchmark benchmark, TaskInfo taskinfo) {//####[231]####
        // ensure Method variable is set//####[231]####
        if (__pt__runBM_40_Benchmark_method == null) {//####[231]####
            __pt__runBM_40_Benchmark_ensureMethodVarSet();//####[231]####
        }//####[231]####
        taskinfo.setParameters(benchmark);//####[231]####
        taskinfo.setMethod(__pt__runBM_40_Benchmark_method);//####[231]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[231]####
    }//####[231]####
    private static TaskID<Void> runBM_40(TaskID<Benchmark> benchmark) {//####[231]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[231]####
        return runBM_40(benchmark, new TaskInfo());//####[231]####
    }//####[231]####
    private static TaskID<Void> runBM_40(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[231]####
        // ensure Method variable is set//####[231]####
        if (__pt__runBM_40_Benchmark_method == null) {//####[231]####
            __pt__runBM_40_Benchmark_ensureMethodVarSet();//####[231]####
        }//####[231]####
        taskinfo.setTaskIdArgIndexes(0);//####[231]####
        taskinfo.addDependsOn(benchmark);//####[231]####
        taskinfo.setParameters(benchmark);//####[231]####
        taskinfo.setMethod(__pt__runBM_40_Benchmark_method);//####[231]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[231]####
    }//####[231]####
    private static TaskID<Void> runBM_40(BlockingQueue<Benchmark> benchmark) {//####[231]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[231]####
        return runBM_40(benchmark, new TaskInfo());//####[231]####
    }//####[231]####
    private static TaskID<Void> runBM_40(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[231]####
        // ensure Method variable is set//####[231]####
        if (__pt__runBM_40_Benchmark_method == null) {//####[231]####
            __pt__runBM_40_Benchmark_ensureMethodVarSet();//####[231]####
        }//####[231]####
        taskinfo.setQueueArgIndexes(0);//####[231]####
        taskinfo.setIsPipeline(true);//####[231]####
        taskinfo.setParameters(benchmark);//####[231]####
        taskinfo.setMethod(__pt__runBM_40_Benchmark_method);//####[231]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[231]####
    }//####[231]####
    public static void __pt__runBM_40(Benchmark benchmark) {//####[231]####
        try {//####[231]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[231]####
        } catch (IllegalAccessException e) {//####[231]####
            e.printStackTrace();//####[231]####
        } catch (IllegalArgumentException e) {//####[231]####
            e.printStackTrace();//####[231]####
        } catch (InvocationTargetException e) {//####[231]####
            e.printStackTrace();//####[231]####
        }//####[231]####
    }//####[231]####
//####[231]####
//####[232]####
    private static volatile Method __pt__runBM_41_Benchmark_method = null;//####[232]####
    private synchronized static void __pt__runBM_41_Benchmark_ensureMethodVarSet() {//####[232]####
        if (__pt__runBM_41_Benchmark_method == null) {//####[232]####
            try {//####[232]####
                __pt__runBM_41_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_41", new Class[] {//####[232]####
                    Benchmark.class//####[232]####
                });//####[232]####
            } catch (Exception e) {//####[232]####
                e.printStackTrace();//####[232]####
            }//####[232]####
        }//####[232]####
    }//####[232]####
    private static TaskID<Void> runBM_41(Benchmark benchmark) {//####[232]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[232]####
        return runBM_41(benchmark, new TaskInfo());//####[232]####
    }//####[232]####
    private static TaskID<Void> runBM_41(Benchmark benchmark, TaskInfo taskinfo) {//####[232]####
        // ensure Method variable is set//####[232]####
        if (__pt__runBM_41_Benchmark_method == null) {//####[232]####
            __pt__runBM_41_Benchmark_ensureMethodVarSet();//####[232]####
        }//####[232]####
        taskinfo.setParameters(benchmark);//####[232]####
        taskinfo.setMethod(__pt__runBM_41_Benchmark_method);//####[232]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[232]####
    }//####[232]####
    private static TaskID<Void> runBM_41(TaskID<Benchmark> benchmark) {//####[232]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[232]####
        return runBM_41(benchmark, new TaskInfo());//####[232]####
    }//####[232]####
    private static TaskID<Void> runBM_41(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[232]####
        // ensure Method variable is set//####[232]####
        if (__pt__runBM_41_Benchmark_method == null) {//####[232]####
            __pt__runBM_41_Benchmark_ensureMethodVarSet();//####[232]####
        }//####[232]####
        taskinfo.setTaskIdArgIndexes(0);//####[232]####
        taskinfo.addDependsOn(benchmark);//####[232]####
        taskinfo.setParameters(benchmark);//####[232]####
        taskinfo.setMethod(__pt__runBM_41_Benchmark_method);//####[232]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[232]####
    }//####[232]####
    private static TaskID<Void> runBM_41(BlockingQueue<Benchmark> benchmark) {//####[232]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[232]####
        return runBM_41(benchmark, new TaskInfo());//####[232]####
    }//####[232]####
    private static TaskID<Void> runBM_41(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[232]####
        // ensure Method variable is set//####[232]####
        if (__pt__runBM_41_Benchmark_method == null) {//####[232]####
            __pt__runBM_41_Benchmark_ensureMethodVarSet();//####[232]####
        }//####[232]####
        taskinfo.setQueueArgIndexes(0);//####[232]####
        taskinfo.setIsPipeline(true);//####[232]####
        taskinfo.setParameters(benchmark);//####[232]####
        taskinfo.setMethod(__pt__runBM_41_Benchmark_method);//####[232]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[232]####
    }//####[232]####
    public static void __pt__runBM_41(Benchmark benchmark) {//####[232]####
        try {//####[232]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[232]####
        } catch (IllegalAccessException e) {//####[232]####
            e.printStackTrace();//####[232]####
        } catch (IllegalArgumentException e) {//####[232]####
            e.printStackTrace();//####[232]####
        } catch (InvocationTargetException e) {//####[232]####
            e.printStackTrace();//####[232]####
        }//####[232]####
    }//####[232]####
//####[232]####
//####[233]####
    private static volatile Method __pt__runBM_42_Benchmark_method = null;//####[233]####
    private synchronized static void __pt__runBM_42_Benchmark_ensureMethodVarSet() {//####[233]####
        if (__pt__runBM_42_Benchmark_method == null) {//####[233]####
            try {//####[233]####
                __pt__runBM_42_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_42", new Class[] {//####[233]####
                    Benchmark.class//####[233]####
                });//####[233]####
            } catch (Exception e) {//####[233]####
                e.printStackTrace();//####[233]####
            }//####[233]####
        }//####[233]####
    }//####[233]####
    private static TaskID<Void> runBM_42(Benchmark benchmark) {//####[233]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[233]####
        return runBM_42(benchmark, new TaskInfo());//####[233]####
    }//####[233]####
    private static TaskID<Void> runBM_42(Benchmark benchmark, TaskInfo taskinfo) {//####[233]####
        // ensure Method variable is set//####[233]####
        if (__pt__runBM_42_Benchmark_method == null) {//####[233]####
            __pt__runBM_42_Benchmark_ensureMethodVarSet();//####[233]####
        }//####[233]####
        taskinfo.setParameters(benchmark);//####[233]####
        taskinfo.setMethod(__pt__runBM_42_Benchmark_method);//####[233]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[233]####
    }//####[233]####
    private static TaskID<Void> runBM_42(TaskID<Benchmark> benchmark) {//####[233]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[233]####
        return runBM_42(benchmark, new TaskInfo());//####[233]####
    }//####[233]####
    private static TaskID<Void> runBM_42(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[233]####
        // ensure Method variable is set//####[233]####
        if (__pt__runBM_42_Benchmark_method == null) {//####[233]####
            __pt__runBM_42_Benchmark_ensureMethodVarSet();//####[233]####
        }//####[233]####
        taskinfo.setTaskIdArgIndexes(0);//####[233]####
        taskinfo.addDependsOn(benchmark);//####[233]####
        taskinfo.setParameters(benchmark);//####[233]####
        taskinfo.setMethod(__pt__runBM_42_Benchmark_method);//####[233]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[233]####
    }//####[233]####
    private static TaskID<Void> runBM_42(BlockingQueue<Benchmark> benchmark) {//####[233]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[233]####
        return runBM_42(benchmark, new TaskInfo());//####[233]####
    }//####[233]####
    private static TaskID<Void> runBM_42(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[233]####
        // ensure Method variable is set//####[233]####
        if (__pt__runBM_42_Benchmark_method == null) {//####[233]####
            __pt__runBM_42_Benchmark_ensureMethodVarSet();//####[233]####
        }//####[233]####
        taskinfo.setQueueArgIndexes(0);//####[233]####
        taskinfo.setIsPipeline(true);//####[233]####
        taskinfo.setParameters(benchmark);//####[233]####
        taskinfo.setMethod(__pt__runBM_42_Benchmark_method);//####[233]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[233]####
    }//####[233]####
    public static void __pt__runBM_42(Benchmark benchmark) {//####[233]####
        try {//####[233]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[233]####
        } catch (IllegalAccessException e) {//####[233]####
            e.printStackTrace();//####[233]####
        } catch (IllegalArgumentException e) {//####[233]####
            e.printStackTrace();//####[233]####
        } catch (InvocationTargetException e) {//####[233]####
            e.printStackTrace();//####[233]####
        }//####[233]####
    }//####[233]####
//####[233]####
//####[234]####
    private static volatile Method __pt__runBM_43_Benchmark_method = null;//####[234]####
    private synchronized static void __pt__runBM_43_Benchmark_ensureMethodVarSet() {//####[234]####
        if (__pt__runBM_43_Benchmark_method == null) {//####[234]####
            try {//####[234]####
                __pt__runBM_43_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_43", new Class[] {//####[234]####
                    Benchmark.class//####[234]####
                });//####[234]####
            } catch (Exception e) {//####[234]####
                e.printStackTrace();//####[234]####
            }//####[234]####
        }//####[234]####
    }//####[234]####
    private static TaskID<Void> runBM_43(Benchmark benchmark) {//####[234]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[234]####
        return runBM_43(benchmark, new TaskInfo());//####[234]####
    }//####[234]####
    private static TaskID<Void> runBM_43(Benchmark benchmark, TaskInfo taskinfo) {//####[234]####
        // ensure Method variable is set//####[234]####
        if (__pt__runBM_43_Benchmark_method == null) {//####[234]####
            __pt__runBM_43_Benchmark_ensureMethodVarSet();//####[234]####
        }//####[234]####
        taskinfo.setParameters(benchmark);//####[234]####
        taskinfo.setMethod(__pt__runBM_43_Benchmark_method);//####[234]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[234]####
    }//####[234]####
    private static TaskID<Void> runBM_43(TaskID<Benchmark> benchmark) {//####[234]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[234]####
        return runBM_43(benchmark, new TaskInfo());//####[234]####
    }//####[234]####
    private static TaskID<Void> runBM_43(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[234]####
        // ensure Method variable is set//####[234]####
        if (__pt__runBM_43_Benchmark_method == null) {//####[234]####
            __pt__runBM_43_Benchmark_ensureMethodVarSet();//####[234]####
        }//####[234]####
        taskinfo.setTaskIdArgIndexes(0);//####[234]####
        taskinfo.addDependsOn(benchmark);//####[234]####
        taskinfo.setParameters(benchmark);//####[234]####
        taskinfo.setMethod(__pt__runBM_43_Benchmark_method);//####[234]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[234]####
    }//####[234]####
    private static TaskID<Void> runBM_43(BlockingQueue<Benchmark> benchmark) {//####[234]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[234]####
        return runBM_43(benchmark, new TaskInfo());//####[234]####
    }//####[234]####
    private static TaskID<Void> runBM_43(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[234]####
        // ensure Method variable is set//####[234]####
        if (__pt__runBM_43_Benchmark_method == null) {//####[234]####
            __pt__runBM_43_Benchmark_ensureMethodVarSet();//####[234]####
        }//####[234]####
        taskinfo.setQueueArgIndexes(0);//####[234]####
        taskinfo.setIsPipeline(true);//####[234]####
        taskinfo.setParameters(benchmark);//####[234]####
        taskinfo.setMethod(__pt__runBM_43_Benchmark_method);//####[234]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[234]####
    }//####[234]####
    public static void __pt__runBM_43(Benchmark benchmark) {//####[234]####
        try {//####[234]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[234]####
        } catch (IllegalAccessException e) {//####[234]####
            e.printStackTrace();//####[234]####
        } catch (IllegalArgumentException e) {//####[234]####
            e.printStackTrace();//####[234]####
        } catch (InvocationTargetException e) {//####[234]####
            e.printStackTrace();//####[234]####
        }//####[234]####
    }//####[234]####
//####[234]####
//####[235]####
    private static volatile Method __pt__runBM_44_Benchmark_method = null;//####[235]####
    private synchronized static void __pt__runBM_44_Benchmark_ensureMethodVarSet() {//####[235]####
        if (__pt__runBM_44_Benchmark_method == null) {//####[235]####
            try {//####[235]####
                __pt__runBM_44_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_44", new Class[] {//####[235]####
                    Benchmark.class//####[235]####
                });//####[235]####
            } catch (Exception e) {//####[235]####
                e.printStackTrace();//####[235]####
            }//####[235]####
        }//####[235]####
    }//####[235]####
    private static TaskID<Void> runBM_44(Benchmark benchmark) {//####[235]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[235]####
        return runBM_44(benchmark, new TaskInfo());//####[235]####
    }//####[235]####
    private static TaskID<Void> runBM_44(Benchmark benchmark, TaskInfo taskinfo) {//####[235]####
        // ensure Method variable is set//####[235]####
        if (__pt__runBM_44_Benchmark_method == null) {//####[235]####
            __pt__runBM_44_Benchmark_ensureMethodVarSet();//####[235]####
        }//####[235]####
        taskinfo.setParameters(benchmark);//####[235]####
        taskinfo.setMethod(__pt__runBM_44_Benchmark_method);//####[235]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[235]####
    }//####[235]####
    private static TaskID<Void> runBM_44(TaskID<Benchmark> benchmark) {//####[235]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[235]####
        return runBM_44(benchmark, new TaskInfo());//####[235]####
    }//####[235]####
    private static TaskID<Void> runBM_44(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[235]####
        // ensure Method variable is set//####[235]####
        if (__pt__runBM_44_Benchmark_method == null) {//####[235]####
            __pt__runBM_44_Benchmark_ensureMethodVarSet();//####[235]####
        }//####[235]####
        taskinfo.setTaskIdArgIndexes(0);//####[235]####
        taskinfo.addDependsOn(benchmark);//####[235]####
        taskinfo.setParameters(benchmark);//####[235]####
        taskinfo.setMethod(__pt__runBM_44_Benchmark_method);//####[235]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[235]####
    }//####[235]####
    private static TaskID<Void> runBM_44(BlockingQueue<Benchmark> benchmark) {//####[235]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[235]####
        return runBM_44(benchmark, new TaskInfo());//####[235]####
    }//####[235]####
    private static TaskID<Void> runBM_44(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[235]####
        // ensure Method variable is set//####[235]####
        if (__pt__runBM_44_Benchmark_method == null) {//####[235]####
            __pt__runBM_44_Benchmark_ensureMethodVarSet();//####[235]####
        }//####[235]####
        taskinfo.setQueueArgIndexes(0);//####[235]####
        taskinfo.setIsPipeline(true);//####[235]####
        taskinfo.setParameters(benchmark);//####[235]####
        taskinfo.setMethod(__pt__runBM_44_Benchmark_method);//####[235]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[235]####
    }//####[235]####
    public static void __pt__runBM_44(Benchmark benchmark) {//####[235]####
        try {//####[235]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[235]####
        } catch (IllegalAccessException e) {//####[235]####
            e.printStackTrace();//####[235]####
        } catch (IllegalArgumentException e) {//####[235]####
            e.printStackTrace();//####[235]####
        } catch (InvocationTargetException e) {//####[235]####
            e.printStackTrace();//####[235]####
        }//####[235]####
    }//####[235]####
//####[235]####
//####[236]####
    private static volatile Method __pt__runBM_45_Benchmark_method = null;//####[236]####
    private synchronized static void __pt__runBM_45_Benchmark_ensureMethodVarSet() {//####[236]####
        if (__pt__runBM_45_Benchmark_method == null) {//####[236]####
            try {//####[236]####
                __pt__runBM_45_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_45", new Class[] {//####[236]####
                    Benchmark.class//####[236]####
                });//####[236]####
            } catch (Exception e) {//####[236]####
                e.printStackTrace();//####[236]####
            }//####[236]####
        }//####[236]####
    }//####[236]####
    private static TaskID<Void> runBM_45(Benchmark benchmark) {//####[236]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[236]####
        return runBM_45(benchmark, new TaskInfo());//####[236]####
    }//####[236]####
    private static TaskID<Void> runBM_45(Benchmark benchmark, TaskInfo taskinfo) {//####[236]####
        // ensure Method variable is set//####[236]####
        if (__pt__runBM_45_Benchmark_method == null) {//####[236]####
            __pt__runBM_45_Benchmark_ensureMethodVarSet();//####[236]####
        }//####[236]####
        taskinfo.setParameters(benchmark);//####[236]####
        taskinfo.setMethod(__pt__runBM_45_Benchmark_method);//####[236]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[236]####
    }//####[236]####
    private static TaskID<Void> runBM_45(TaskID<Benchmark> benchmark) {//####[236]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[236]####
        return runBM_45(benchmark, new TaskInfo());//####[236]####
    }//####[236]####
    private static TaskID<Void> runBM_45(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[236]####
        // ensure Method variable is set//####[236]####
        if (__pt__runBM_45_Benchmark_method == null) {//####[236]####
            __pt__runBM_45_Benchmark_ensureMethodVarSet();//####[236]####
        }//####[236]####
        taskinfo.setTaskIdArgIndexes(0);//####[236]####
        taskinfo.addDependsOn(benchmark);//####[236]####
        taskinfo.setParameters(benchmark);//####[236]####
        taskinfo.setMethod(__pt__runBM_45_Benchmark_method);//####[236]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[236]####
    }//####[236]####
    private static TaskID<Void> runBM_45(BlockingQueue<Benchmark> benchmark) {//####[236]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[236]####
        return runBM_45(benchmark, new TaskInfo());//####[236]####
    }//####[236]####
    private static TaskID<Void> runBM_45(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[236]####
        // ensure Method variable is set//####[236]####
        if (__pt__runBM_45_Benchmark_method == null) {//####[236]####
            __pt__runBM_45_Benchmark_ensureMethodVarSet();//####[236]####
        }//####[236]####
        taskinfo.setQueueArgIndexes(0);//####[236]####
        taskinfo.setIsPipeline(true);//####[236]####
        taskinfo.setParameters(benchmark);//####[236]####
        taskinfo.setMethod(__pt__runBM_45_Benchmark_method);//####[236]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[236]####
    }//####[236]####
    public static void __pt__runBM_45(Benchmark benchmark) {//####[236]####
        try {//####[236]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[236]####
        } catch (IllegalAccessException e) {//####[236]####
            e.printStackTrace();//####[236]####
        } catch (IllegalArgumentException e) {//####[236]####
            e.printStackTrace();//####[236]####
        } catch (InvocationTargetException e) {//####[236]####
            e.printStackTrace();//####[236]####
        }//####[236]####
    }//####[236]####
//####[236]####
//####[237]####
    private static volatile Method __pt__runBM_46_Benchmark_method = null;//####[237]####
    private synchronized static void __pt__runBM_46_Benchmark_ensureMethodVarSet() {//####[237]####
        if (__pt__runBM_46_Benchmark_method == null) {//####[237]####
            try {//####[237]####
                __pt__runBM_46_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_46", new Class[] {//####[237]####
                    Benchmark.class//####[237]####
                });//####[237]####
            } catch (Exception e) {//####[237]####
                e.printStackTrace();//####[237]####
            }//####[237]####
        }//####[237]####
    }//####[237]####
    private static TaskID<Void> runBM_46(Benchmark benchmark) {//####[237]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[237]####
        return runBM_46(benchmark, new TaskInfo());//####[237]####
    }//####[237]####
    private static TaskID<Void> runBM_46(Benchmark benchmark, TaskInfo taskinfo) {//####[237]####
        // ensure Method variable is set//####[237]####
        if (__pt__runBM_46_Benchmark_method == null) {//####[237]####
            __pt__runBM_46_Benchmark_ensureMethodVarSet();//####[237]####
        }//####[237]####
        taskinfo.setParameters(benchmark);//####[237]####
        taskinfo.setMethod(__pt__runBM_46_Benchmark_method);//####[237]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[237]####
    }//####[237]####
    private static TaskID<Void> runBM_46(TaskID<Benchmark> benchmark) {//####[237]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[237]####
        return runBM_46(benchmark, new TaskInfo());//####[237]####
    }//####[237]####
    private static TaskID<Void> runBM_46(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[237]####
        // ensure Method variable is set//####[237]####
        if (__pt__runBM_46_Benchmark_method == null) {//####[237]####
            __pt__runBM_46_Benchmark_ensureMethodVarSet();//####[237]####
        }//####[237]####
        taskinfo.setTaskIdArgIndexes(0);//####[237]####
        taskinfo.addDependsOn(benchmark);//####[237]####
        taskinfo.setParameters(benchmark);//####[237]####
        taskinfo.setMethod(__pt__runBM_46_Benchmark_method);//####[237]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[237]####
    }//####[237]####
    private static TaskID<Void> runBM_46(BlockingQueue<Benchmark> benchmark) {//####[237]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[237]####
        return runBM_46(benchmark, new TaskInfo());//####[237]####
    }//####[237]####
    private static TaskID<Void> runBM_46(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[237]####
        // ensure Method variable is set//####[237]####
        if (__pt__runBM_46_Benchmark_method == null) {//####[237]####
            __pt__runBM_46_Benchmark_ensureMethodVarSet();//####[237]####
        }//####[237]####
        taskinfo.setQueueArgIndexes(0);//####[237]####
        taskinfo.setIsPipeline(true);//####[237]####
        taskinfo.setParameters(benchmark);//####[237]####
        taskinfo.setMethod(__pt__runBM_46_Benchmark_method);//####[237]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[237]####
    }//####[237]####
    public static void __pt__runBM_46(Benchmark benchmark) {//####[237]####
        try {//####[237]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[237]####
        } catch (IllegalAccessException e) {//####[237]####
            e.printStackTrace();//####[237]####
        } catch (IllegalArgumentException e) {//####[237]####
            e.printStackTrace();//####[237]####
        } catch (InvocationTargetException e) {//####[237]####
            e.printStackTrace();//####[237]####
        }//####[237]####
    }//####[237]####
//####[237]####
//####[238]####
    private static volatile Method __pt__runBM_47_Benchmark_method = null;//####[238]####
    private synchronized static void __pt__runBM_47_Benchmark_ensureMethodVarSet() {//####[238]####
        if (__pt__runBM_47_Benchmark_method == null) {//####[238]####
            try {//####[238]####
                __pt__runBM_47_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_47", new Class[] {//####[238]####
                    Benchmark.class//####[238]####
                });//####[238]####
            } catch (Exception e) {//####[238]####
                e.printStackTrace();//####[238]####
            }//####[238]####
        }//####[238]####
    }//####[238]####
    private static TaskID<Void> runBM_47(Benchmark benchmark) {//####[238]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[238]####
        return runBM_47(benchmark, new TaskInfo());//####[238]####
    }//####[238]####
    private static TaskID<Void> runBM_47(Benchmark benchmark, TaskInfo taskinfo) {//####[238]####
        // ensure Method variable is set//####[238]####
        if (__pt__runBM_47_Benchmark_method == null) {//####[238]####
            __pt__runBM_47_Benchmark_ensureMethodVarSet();//####[238]####
        }//####[238]####
        taskinfo.setParameters(benchmark);//####[238]####
        taskinfo.setMethod(__pt__runBM_47_Benchmark_method);//####[238]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[238]####
    }//####[238]####
    private static TaskID<Void> runBM_47(TaskID<Benchmark> benchmark) {//####[238]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[238]####
        return runBM_47(benchmark, new TaskInfo());//####[238]####
    }//####[238]####
    private static TaskID<Void> runBM_47(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[238]####
        // ensure Method variable is set//####[238]####
        if (__pt__runBM_47_Benchmark_method == null) {//####[238]####
            __pt__runBM_47_Benchmark_ensureMethodVarSet();//####[238]####
        }//####[238]####
        taskinfo.setTaskIdArgIndexes(0);//####[238]####
        taskinfo.addDependsOn(benchmark);//####[238]####
        taskinfo.setParameters(benchmark);//####[238]####
        taskinfo.setMethod(__pt__runBM_47_Benchmark_method);//####[238]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[238]####
    }//####[238]####
    private static TaskID<Void> runBM_47(BlockingQueue<Benchmark> benchmark) {//####[238]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[238]####
        return runBM_47(benchmark, new TaskInfo());//####[238]####
    }//####[238]####
    private static TaskID<Void> runBM_47(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[238]####
        // ensure Method variable is set//####[238]####
        if (__pt__runBM_47_Benchmark_method == null) {//####[238]####
            __pt__runBM_47_Benchmark_ensureMethodVarSet();//####[238]####
        }//####[238]####
        taskinfo.setQueueArgIndexes(0);//####[238]####
        taskinfo.setIsPipeline(true);//####[238]####
        taskinfo.setParameters(benchmark);//####[238]####
        taskinfo.setMethod(__pt__runBM_47_Benchmark_method);//####[238]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[238]####
    }//####[238]####
    public static void __pt__runBM_47(Benchmark benchmark) {//####[238]####
        try {//####[238]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[238]####
        } catch (IllegalAccessException e) {//####[238]####
            e.printStackTrace();//####[238]####
        } catch (IllegalArgumentException e) {//####[238]####
            e.printStackTrace();//####[238]####
        } catch (InvocationTargetException e) {//####[238]####
            e.printStackTrace();//####[238]####
        }//####[238]####
    }//####[238]####
//####[238]####
//####[239]####
    private static volatile Method __pt__runBM_48_Benchmark_method = null;//####[239]####
    private synchronized static void __pt__runBM_48_Benchmark_ensureMethodVarSet() {//####[239]####
        if (__pt__runBM_48_Benchmark_method == null) {//####[239]####
            try {//####[239]####
                __pt__runBM_48_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_48", new Class[] {//####[239]####
                    Benchmark.class//####[239]####
                });//####[239]####
            } catch (Exception e) {//####[239]####
                e.printStackTrace();//####[239]####
            }//####[239]####
        }//####[239]####
    }//####[239]####
    private static TaskID<Void> runBM_48(Benchmark benchmark) {//####[239]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[239]####
        return runBM_48(benchmark, new TaskInfo());//####[239]####
    }//####[239]####
    private static TaskID<Void> runBM_48(Benchmark benchmark, TaskInfo taskinfo) {//####[239]####
        // ensure Method variable is set//####[239]####
        if (__pt__runBM_48_Benchmark_method == null) {//####[239]####
            __pt__runBM_48_Benchmark_ensureMethodVarSet();//####[239]####
        }//####[239]####
        taskinfo.setParameters(benchmark);//####[239]####
        taskinfo.setMethod(__pt__runBM_48_Benchmark_method);//####[239]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[239]####
    }//####[239]####
    private static TaskID<Void> runBM_48(TaskID<Benchmark> benchmark) {//####[239]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[239]####
        return runBM_48(benchmark, new TaskInfo());//####[239]####
    }//####[239]####
    private static TaskID<Void> runBM_48(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[239]####
        // ensure Method variable is set//####[239]####
        if (__pt__runBM_48_Benchmark_method == null) {//####[239]####
            __pt__runBM_48_Benchmark_ensureMethodVarSet();//####[239]####
        }//####[239]####
        taskinfo.setTaskIdArgIndexes(0);//####[239]####
        taskinfo.addDependsOn(benchmark);//####[239]####
        taskinfo.setParameters(benchmark);//####[239]####
        taskinfo.setMethod(__pt__runBM_48_Benchmark_method);//####[239]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[239]####
    }//####[239]####
    private static TaskID<Void> runBM_48(BlockingQueue<Benchmark> benchmark) {//####[239]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[239]####
        return runBM_48(benchmark, new TaskInfo());//####[239]####
    }//####[239]####
    private static TaskID<Void> runBM_48(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[239]####
        // ensure Method variable is set//####[239]####
        if (__pt__runBM_48_Benchmark_method == null) {//####[239]####
            __pt__runBM_48_Benchmark_ensureMethodVarSet();//####[239]####
        }//####[239]####
        taskinfo.setQueueArgIndexes(0);//####[239]####
        taskinfo.setIsPipeline(true);//####[239]####
        taskinfo.setParameters(benchmark);//####[239]####
        taskinfo.setMethod(__pt__runBM_48_Benchmark_method);//####[239]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[239]####
    }//####[239]####
    public static void __pt__runBM_48(Benchmark benchmark) {//####[239]####
        try {//####[239]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[239]####
        } catch (IllegalAccessException e) {//####[239]####
            e.printStackTrace();//####[239]####
        } catch (IllegalArgumentException e) {//####[239]####
            e.printStackTrace();//####[239]####
        } catch (InvocationTargetException e) {//####[239]####
            e.printStackTrace();//####[239]####
        }//####[239]####
    }//####[239]####
//####[239]####
//####[240]####
    private static volatile Method __pt__runBM_49_Benchmark_method = null;//####[240]####
    private synchronized static void __pt__runBM_49_Benchmark_ensureMethodVarSet() {//####[240]####
        if (__pt__runBM_49_Benchmark_method == null) {//####[240]####
            try {//####[240]####
                __pt__runBM_49_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_49", new Class[] {//####[240]####
                    Benchmark.class//####[240]####
                });//####[240]####
            } catch (Exception e) {//####[240]####
                e.printStackTrace();//####[240]####
            }//####[240]####
        }//####[240]####
    }//####[240]####
    private static TaskID<Void> runBM_49(Benchmark benchmark) {//####[240]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[240]####
        return runBM_49(benchmark, new TaskInfo());//####[240]####
    }//####[240]####
    private static TaskID<Void> runBM_49(Benchmark benchmark, TaskInfo taskinfo) {//####[240]####
        // ensure Method variable is set//####[240]####
        if (__pt__runBM_49_Benchmark_method == null) {//####[240]####
            __pt__runBM_49_Benchmark_ensureMethodVarSet();//####[240]####
        }//####[240]####
        taskinfo.setParameters(benchmark);//####[240]####
        taskinfo.setMethod(__pt__runBM_49_Benchmark_method);//####[240]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[240]####
    }//####[240]####
    private static TaskID<Void> runBM_49(TaskID<Benchmark> benchmark) {//####[240]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[240]####
        return runBM_49(benchmark, new TaskInfo());//####[240]####
    }//####[240]####
    private static TaskID<Void> runBM_49(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[240]####
        // ensure Method variable is set//####[240]####
        if (__pt__runBM_49_Benchmark_method == null) {//####[240]####
            __pt__runBM_49_Benchmark_ensureMethodVarSet();//####[240]####
        }//####[240]####
        taskinfo.setTaskIdArgIndexes(0);//####[240]####
        taskinfo.addDependsOn(benchmark);//####[240]####
        taskinfo.setParameters(benchmark);//####[240]####
        taskinfo.setMethod(__pt__runBM_49_Benchmark_method);//####[240]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[240]####
    }//####[240]####
    private static TaskID<Void> runBM_49(BlockingQueue<Benchmark> benchmark) {//####[240]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[240]####
        return runBM_49(benchmark, new TaskInfo());//####[240]####
    }//####[240]####
    private static TaskID<Void> runBM_49(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[240]####
        // ensure Method variable is set//####[240]####
        if (__pt__runBM_49_Benchmark_method == null) {//####[240]####
            __pt__runBM_49_Benchmark_ensureMethodVarSet();//####[240]####
        }//####[240]####
        taskinfo.setQueueArgIndexes(0);//####[240]####
        taskinfo.setIsPipeline(true);//####[240]####
        taskinfo.setParameters(benchmark);//####[240]####
        taskinfo.setMethod(__pt__runBM_49_Benchmark_method);//####[240]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[240]####
    }//####[240]####
    public static void __pt__runBM_49(Benchmark benchmark) {//####[240]####
        try {//####[240]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[240]####
        } catch (IllegalAccessException e) {//####[240]####
            e.printStackTrace();//####[240]####
        } catch (IllegalArgumentException e) {//####[240]####
            e.printStackTrace();//####[240]####
        } catch (InvocationTargetException e) {//####[240]####
            e.printStackTrace();//####[240]####
        }//####[240]####
    }//####[240]####
//####[240]####
//####[241]####
    private static volatile Method __pt__runBM_52_Benchmark_method = null;//####[241]####
    private synchronized static void __pt__runBM_52_Benchmark_ensureMethodVarSet() {//####[241]####
        if (__pt__runBM_52_Benchmark_method == null) {//####[241]####
            try {//####[241]####
                __pt__runBM_52_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_52", new Class[] {//####[241]####
                    Benchmark.class//####[241]####
                });//####[241]####
            } catch (Exception e) {//####[241]####
                e.printStackTrace();//####[241]####
            }//####[241]####
        }//####[241]####
    }//####[241]####
    private static TaskID<Void> runBM_52(Benchmark benchmark) {//####[241]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[241]####
        return runBM_52(benchmark, new TaskInfo());//####[241]####
    }//####[241]####
    private static TaskID<Void> runBM_52(Benchmark benchmark, TaskInfo taskinfo) {//####[241]####
        // ensure Method variable is set//####[241]####
        if (__pt__runBM_52_Benchmark_method == null) {//####[241]####
            __pt__runBM_52_Benchmark_ensureMethodVarSet();//####[241]####
        }//####[241]####
        taskinfo.setParameters(benchmark);//####[241]####
        taskinfo.setMethod(__pt__runBM_52_Benchmark_method);//####[241]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[241]####
    }//####[241]####
    private static TaskID<Void> runBM_52(TaskID<Benchmark> benchmark) {//####[241]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[241]####
        return runBM_52(benchmark, new TaskInfo());//####[241]####
    }//####[241]####
    private static TaskID<Void> runBM_52(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[241]####
        // ensure Method variable is set//####[241]####
        if (__pt__runBM_52_Benchmark_method == null) {//####[241]####
            __pt__runBM_52_Benchmark_ensureMethodVarSet();//####[241]####
        }//####[241]####
        taskinfo.setTaskIdArgIndexes(0);//####[241]####
        taskinfo.addDependsOn(benchmark);//####[241]####
        taskinfo.setParameters(benchmark);//####[241]####
        taskinfo.setMethod(__pt__runBM_52_Benchmark_method);//####[241]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[241]####
    }//####[241]####
    private static TaskID<Void> runBM_52(BlockingQueue<Benchmark> benchmark) {//####[241]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[241]####
        return runBM_52(benchmark, new TaskInfo());//####[241]####
    }//####[241]####
    private static TaskID<Void> runBM_52(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[241]####
        // ensure Method variable is set//####[241]####
        if (__pt__runBM_52_Benchmark_method == null) {//####[241]####
            __pt__runBM_52_Benchmark_ensureMethodVarSet();//####[241]####
        }//####[241]####
        taskinfo.setQueueArgIndexes(0);//####[241]####
        taskinfo.setIsPipeline(true);//####[241]####
        taskinfo.setParameters(benchmark);//####[241]####
        taskinfo.setMethod(__pt__runBM_52_Benchmark_method);//####[241]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[241]####
    }//####[241]####
    public static void __pt__runBM_52(Benchmark benchmark) {//####[241]####
        try {//####[241]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[241]####
        } catch (IllegalAccessException e) {//####[241]####
            e.printStackTrace();//####[241]####
        } catch (IllegalArgumentException e) {//####[241]####
            e.printStackTrace();//####[241]####
        } catch (InvocationTargetException e) {//####[241]####
            e.printStackTrace();//####[241]####
        }//####[241]####
    }//####[241]####
//####[241]####
//####[242]####
    private static volatile Method __pt__runBM_54_Benchmark_method = null;//####[242]####
    private synchronized static void __pt__runBM_54_Benchmark_ensureMethodVarSet() {//####[242]####
        if (__pt__runBM_54_Benchmark_method == null) {//####[242]####
            try {//####[242]####
                __pt__runBM_54_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_54", new Class[] {//####[242]####
                    Benchmark.class//####[242]####
                });//####[242]####
            } catch (Exception e) {//####[242]####
                e.printStackTrace();//####[242]####
            }//####[242]####
        }//####[242]####
    }//####[242]####
    private static TaskID<Void> runBM_54(Benchmark benchmark) {//####[242]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[242]####
        return runBM_54(benchmark, new TaskInfo());//####[242]####
    }//####[242]####
    private static TaskID<Void> runBM_54(Benchmark benchmark, TaskInfo taskinfo) {//####[242]####
        // ensure Method variable is set//####[242]####
        if (__pt__runBM_54_Benchmark_method == null) {//####[242]####
            __pt__runBM_54_Benchmark_ensureMethodVarSet();//####[242]####
        }//####[242]####
        taskinfo.setParameters(benchmark);//####[242]####
        taskinfo.setMethod(__pt__runBM_54_Benchmark_method);//####[242]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[242]####
    }//####[242]####
    private static TaskID<Void> runBM_54(TaskID<Benchmark> benchmark) {//####[242]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[242]####
        return runBM_54(benchmark, new TaskInfo());//####[242]####
    }//####[242]####
    private static TaskID<Void> runBM_54(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[242]####
        // ensure Method variable is set//####[242]####
        if (__pt__runBM_54_Benchmark_method == null) {//####[242]####
            __pt__runBM_54_Benchmark_ensureMethodVarSet();//####[242]####
        }//####[242]####
        taskinfo.setTaskIdArgIndexes(0);//####[242]####
        taskinfo.addDependsOn(benchmark);//####[242]####
        taskinfo.setParameters(benchmark);//####[242]####
        taskinfo.setMethod(__pt__runBM_54_Benchmark_method);//####[242]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[242]####
    }//####[242]####
    private static TaskID<Void> runBM_54(BlockingQueue<Benchmark> benchmark) {//####[242]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[242]####
        return runBM_54(benchmark, new TaskInfo());//####[242]####
    }//####[242]####
    private static TaskID<Void> runBM_54(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[242]####
        // ensure Method variable is set//####[242]####
        if (__pt__runBM_54_Benchmark_method == null) {//####[242]####
            __pt__runBM_54_Benchmark_ensureMethodVarSet();//####[242]####
        }//####[242]####
        taskinfo.setQueueArgIndexes(0);//####[242]####
        taskinfo.setIsPipeline(true);//####[242]####
        taskinfo.setParameters(benchmark);//####[242]####
        taskinfo.setMethod(__pt__runBM_54_Benchmark_method);//####[242]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[242]####
    }//####[242]####
    public static void __pt__runBM_54(Benchmark benchmark) {//####[242]####
        try {//####[242]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[242]####
        } catch (IllegalAccessException e) {//####[242]####
            e.printStackTrace();//####[242]####
        } catch (IllegalArgumentException e) {//####[242]####
            e.printStackTrace();//####[242]####
        } catch (InvocationTargetException e) {//####[242]####
            e.printStackTrace();//####[242]####
        }//####[242]####
    }//####[242]####
//####[242]####
//####[243]####
    private static volatile Method __pt__runBM_56_Benchmark_method = null;//####[243]####
    private synchronized static void __pt__runBM_56_Benchmark_ensureMethodVarSet() {//####[243]####
        if (__pt__runBM_56_Benchmark_method == null) {//####[243]####
            try {//####[243]####
                __pt__runBM_56_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_56", new Class[] {//####[243]####
                    Benchmark.class//####[243]####
                });//####[243]####
            } catch (Exception e) {//####[243]####
                e.printStackTrace();//####[243]####
            }//####[243]####
        }//####[243]####
    }//####[243]####
    private static TaskID<Void> runBM_56(Benchmark benchmark) {//####[243]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[243]####
        return runBM_56(benchmark, new TaskInfo());//####[243]####
    }//####[243]####
    private static TaskID<Void> runBM_56(Benchmark benchmark, TaskInfo taskinfo) {//####[243]####
        // ensure Method variable is set//####[243]####
        if (__pt__runBM_56_Benchmark_method == null) {//####[243]####
            __pt__runBM_56_Benchmark_ensureMethodVarSet();//####[243]####
        }//####[243]####
        taskinfo.setParameters(benchmark);//####[243]####
        taskinfo.setMethod(__pt__runBM_56_Benchmark_method);//####[243]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[243]####
    }//####[243]####
    private static TaskID<Void> runBM_56(TaskID<Benchmark> benchmark) {//####[243]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[243]####
        return runBM_56(benchmark, new TaskInfo());//####[243]####
    }//####[243]####
    private static TaskID<Void> runBM_56(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[243]####
        // ensure Method variable is set//####[243]####
        if (__pt__runBM_56_Benchmark_method == null) {//####[243]####
            __pt__runBM_56_Benchmark_ensureMethodVarSet();//####[243]####
        }//####[243]####
        taskinfo.setTaskIdArgIndexes(0);//####[243]####
        taskinfo.addDependsOn(benchmark);//####[243]####
        taskinfo.setParameters(benchmark);//####[243]####
        taskinfo.setMethod(__pt__runBM_56_Benchmark_method);//####[243]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[243]####
    }//####[243]####
    private static TaskID<Void> runBM_56(BlockingQueue<Benchmark> benchmark) {//####[243]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[243]####
        return runBM_56(benchmark, new TaskInfo());//####[243]####
    }//####[243]####
    private static TaskID<Void> runBM_56(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[243]####
        // ensure Method variable is set//####[243]####
        if (__pt__runBM_56_Benchmark_method == null) {//####[243]####
            __pt__runBM_56_Benchmark_ensureMethodVarSet();//####[243]####
        }//####[243]####
        taskinfo.setQueueArgIndexes(0);//####[243]####
        taskinfo.setIsPipeline(true);//####[243]####
        taskinfo.setParameters(benchmark);//####[243]####
        taskinfo.setMethod(__pt__runBM_56_Benchmark_method);//####[243]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[243]####
    }//####[243]####
    public static void __pt__runBM_56(Benchmark benchmark) {//####[243]####
        try {//####[243]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[243]####
        } catch (IllegalAccessException e) {//####[243]####
            e.printStackTrace();//####[243]####
        } catch (IllegalArgumentException e) {//####[243]####
            e.printStackTrace();//####[243]####
        } catch (InvocationTargetException e) {//####[243]####
            e.printStackTrace();//####[243]####
        }//####[243]####
    }//####[243]####
//####[243]####
//####[244]####
    private static volatile Method __pt__runBM_61_Benchmark_method = null;//####[244]####
    private synchronized static void __pt__runBM_61_Benchmark_ensureMethodVarSet() {//####[244]####
        if (__pt__runBM_61_Benchmark_method == null) {//####[244]####
            try {//####[244]####
                __pt__runBM_61_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_61", new Class[] {//####[244]####
                    Benchmark.class//####[244]####
                });//####[244]####
            } catch (Exception e) {//####[244]####
                e.printStackTrace();//####[244]####
            }//####[244]####
        }//####[244]####
    }//####[244]####
    private static TaskID<Void> runBM_61(Benchmark benchmark) {//####[244]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[244]####
        return runBM_61(benchmark, new TaskInfo());//####[244]####
    }//####[244]####
    private static TaskID<Void> runBM_61(Benchmark benchmark, TaskInfo taskinfo) {//####[244]####
        // ensure Method variable is set//####[244]####
        if (__pt__runBM_61_Benchmark_method == null) {//####[244]####
            __pt__runBM_61_Benchmark_ensureMethodVarSet();//####[244]####
        }//####[244]####
        taskinfo.setParameters(benchmark);//####[244]####
        taskinfo.setMethod(__pt__runBM_61_Benchmark_method);//####[244]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[244]####
    }//####[244]####
    private static TaskID<Void> runBM_61(TaskID<Benchmark> benchmark) {//####[244]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[244]####
        return runBM_61(benchmark, new TaskInfo());//####[244]####
    }//####[244]####
    private static TaskID<Void> runBM_61(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[244]####
        // ensure Method variable is set//####[244]####
        if (__pt__runBM_61_Benchmark_method == null) {//####[244]####
            __pt__runBM_61_Benchmark_ensureMethodVarSet();//####[244]####
        }//####[244]####
        taskinfo.setTaskIdArgIndexes(0);//####[244]####
        taskinfo.addDependsOn(benchmark);//####[244]####
        taskinfo.setParameters(benchmark);//####[244]####
        taskinfo.setMethod(__pt__runBM_61_Benchmark_method);//####[244]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[244]####
    }//####[244]####
    private static TaskID<Void> runBM_61(BlockingQueue<Benchmark> benchmark) {//####[244]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[244]####
        return runBM_61(benchmark, new TaskInfo());//####[244]####
    }//####[244]####
    private static TaskID<Void> runBM_61(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[244]####
        // ensure Method variable is set//####[244]####
        if (__pt__runBM_61_Benchmark_method == null) {//####[244]####
            __pt__runBM_61_Benchmark_ensureMethodVarSet();//####[244]####
        }//####[244]####
        taskinfo.setQueueArgIndexes(0);//####[244]####
        taskinfo.setIsPipeline(true);//####[244]####
        taskinfo.setParameters(benchmark);//####[244]####
        taskinfo.setMethod(__pt__runBM_61_Benchmark_method);//####[244]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[244]####
    }//####[244]####
    public static void __pt__runBM_61(Benchmark benchmark) {//####[244]####
        try {//####[244]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[244]####
        } catch (IllegalAccessException e) {//####[244]####
            e.printStackTrace();//####[244]####
        } catch (IllegalArgumentException e) {//####[244]####
            e.printStackTrace();//####[244]####
        } catch (InvocationTargetException e) {//####[244]####
            e.printStackTrace();//####[244]####
        }//####[244]####
    }//####[244]####
//####[244]####
//####[245]####
    private static volatile Method __pt__runBM_62_Benchmark_method = null;//####[245]####
    private synchronized static void __pt__runBM_62_Benchmark_ensureMethodVarSet() {//####[245]####
        if (__pt__runBM_62_Benchmark_method == null) {//####[245]####
            try {//####[245]####
                __pt__runBM_62_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_62", new Class[] {//####[245]####
                    Benchmark.class//####[245]####
                });//####[245]####
            } catch (Exception e) {//####[245]####
                e.printStackTrace();//####[245]####
            }//####[245]####
        }//####[245]####
    }//####[245]####
    private static TaskID<Void> runBM_62(Benchmark benchmark) {//####[245]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[245]####
        return runBM_62(benchmark, new TaskInfo());//####[245]####
    }//####[245]####
    private static TaskID<Void> runBM_62(Benchmark benchmark, TaskInfo taskinfo) {//####[245]####
        // ensure Method variable is set//####[245]####
        if (__pt__runBM_62_Benchmark_method == null) {//####[245]####
            __pt__runBM_62_Benchmark_ensureMethodVarSet();//####[245]####
        }//####[245]####
        taskinfo.setParameters(benchmark);//####[245]####
        taskinfo.setMethod(__pt__runBM_62_Benchmark_method);//####[245]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[245]####
    }//####[245]####
    private static TaskID<Void> runBM_62(TaskID<Benchmark> benchmark) {//####[245]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[245]####
        return runBM_62(benchmark, new TaskInfo());//####[245]####
    }//####[245]####
    private static TaskID<Void> runBM_62(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[245]####
        // ensure Method variable is set//####[245]####
        if (__pt__runBM_62_Benchmark_method == null) {//####[245]####
            __pt__runBM_62_Benchmark_ensureMethodVarSet();//####[245]####
        }//####[245]####
        taskinfo.setTaskIdArgIndexes(0);//####[245]####
        taskinfo.addDependsOn(benchmark);//####[245]####
        taskinfo.setParameters(benchmark);//####[245]####
        taskinfo.setMethod(__pt__runBM_62_Benchmark_method);//####[245]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[245]####
    }//####[245]####
    private static TaskID<Void> runBM_62(BlockingQueue<Benchmark> benchmark) {//####[245]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[245]####
        return runBM_62(benchmark, new TaskInfo());//####[245]####
    }//####[245]####
    private static TaskID<Void> runBM_62(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[245]####
        // ensure Method variable is set//####[245]####
        if (__pt__runBM_62_Benchmark_method == null) {//####[245]####
            __pt__runBM_62_Benchmark_ensureMethodVarSet();//####[245]####
        }//####[245]####
        taskinfo.setQueueArgIndexes(0);//####[245]####
        taskinfo.setIsPipeline(true);//####[245]####
        taskinfo.setParameters(benchmark);//####[245]####
        taskinfo.setMethod(__pt__runBM_62_Benchmark_method);//####[245]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[245]####
    }//####[245]####
    public static void __pt__runBM_62(Benchmark benchmark) {//####[245]####
        try {//####[245]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[245]####
        } catch (IllegalAccessException e) {//####[245]####
            e.printStackTrace();//####[245]####
        } catch (IllegalArgumentException e) {//####[245]####
            e.printStackTrace();//####[245]####
        } catch (InvocationTargetException e) {//####[245]####
            e.printStackTrace();//####[245]####
        }//####[245]####
    }//####[245]####
//####[245]####
//####[246]####
    private static volatile Method __pt__runBM_65_Benchmark_method = null;//####[246]####
    private synchronized static void __pt__runBM_65_Benchmark_ensureMethodVarSet() {//####[246]####
        if (__pt__runBM_65_Benchmark_method == null) {//####[246]####
            try {//####[246]####
                __pt__runBM_65_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_65", new Class[] {//####[246]####
                    Benchmark.class//####[246]####
                });//####[246]####
            } catch (Exception e) {//####[246]####
                e.printStackTrace();//####[246]####
            }//####[246]####
        }//####[246]####
    }//####[246]####
    private static TaskID<Void> runBM_65(Benchmark benchmark) {//####[246]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[246]####
        return runBM_65(benchmark, new TaskInfo());//####[246]####
    }//####[246]####
    private static TaskID<Void> runBM_65(Benchmark benchmark, TaskInfo taskinfo) {//####[246]####
        // ensure Method variable is set//####[246]####
        if (__pt__runBM_65_Benchmark_method == null) {//####[246]####
            __pt__runBM_65_Benchmark_ensureMethodVarSet();//####[246]####
        }//####[246]####
        taskinfo.setParameters(benchmark);//####[246]####
        taskinfo.setMethod(__pt__runBM_65_Benchmark_method);//####[246]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[246]####
    }//####[246]####
    private static TaskID<Void> runBM_65(TaskID<Benchmark> benchmark) {//####[246]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[246]####
        return runBM_65(benchmark, new TaskInfo());//####[246]####
    }//####[246]####
    private static TaskID<Void> runBM_65(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[246]####
        // ensure Method variable is set//####[246]####
        if (__pt__runBM_65_Benchmark_method == null) {//####[246]####
            __pt__runBM_65_Benchmark_ensureMethodVarSet();//####[246]####
        }//####[246]####
        taskinfo.setTaskIdArgIndexes(0);//####[246]####
        taskinfo.addDependsOn(benchmark);//####[246]####
        taskinfo.setParameters(benchmark);//####[246]####
        taskinfo.setMethod(__pt__runBM_65_Benchmark_method);//####[246]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[246]####
    }//####[246]####
    private static TaskID<Void> runBM_65(BlockingQueue<Benchmark> benchmark) {//####[246]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[246]####
        return runBM_65(benchmark, new TaskInfo());//####[246]####
    }//####[246]####
    private static TaskID<Void> runBM_65(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[246]####
        // ensure Method variable is set//####[246]####
        if (__pt__runBM_65_Benchmark_method == null) {//####[246]####
            __pt__runBM_65_Benchmark_ensureMethodVarSet();//####[246]####
        }//####[246]####
        taskinfo.setQueueArgIndexes(0);//####[246]####
        taskinfo.setIsPipeline(true);//####[246]####
        taskinfo.setParameters(benchmark);//####[246]####
        taskinfo.setMethod(__pt__runBM_65_Benchmark_method);//####[246]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[246]####
    }//####[246]####
    public static void __pt__runBM_65(Benchmark benchmark) {//####[246]####
        try {//####[246]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[246]####
        } catch (IllegalAccessException e) {//####[246]####
            e.printStackTrace();//####[246]####
        } catch (IllegalArgumentException e) {//####[246]####
            e.printStackTrace();//####[246]####
        } catch (InvocationTargetException e) {//####[246]####
            e.printStackTrace();//####[246]####
        }//####[246]####
    }//####[246]####
//####[246]####
//####[247]####
    private static volatile Method __pt__runBM_66_Benchmark_method = null;//####[247]####
    private synchronized static void __pt__runBM_66_Benchmark_ensureMethodVarSet() {//####[247]####
        if (__pt__runBM_66_Benchmark_method == null) {//####[247]####
            try {//####[247]####
                __pt__runBM_66_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_66", new Class[] {//####[247]####
                    Benchmark.class//####[247]####
                });//####[247]####
            } catch (Exception e) {//####[247]####
                e.printStackTrace();//####[247]####
            }//####[247]####
        }//####[247]####
    }//####[247]####
    private static TaskID<Void> runBM_66(Benchmark benchmark) {//####[247]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[247]####
        return runBM_66(benchmark, new TaskInfo());//####[247]####
    }//####[247]####
    private static TaskID<Void> runBM_66(Benchmark benchmark, TaskInfo taskinfo) {//####[247]####
        // ensure Method variable is set//####[247]####
        if (__pt__runBM_66_Benchmark_method == null) {//####[247]####
            __pt__runBM_66_Benchmark_ensureMethodVarSet();//####[247]####
        }//####[247]####
        taskinfo.setParameters(benchmark);//####[247]####
        taskinfo.setMethod(__pt__runBM_66_Benchmark_method);//####[247]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[247]####
    }//####[247]####
    private static TaskID<Void> runBM_66(TaskID<Benchmark> benchmark) {//####[247]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[247]####
        return runBM_66(benchmark, new TaskInfo());//####[247]####
    }//####[247]####
    private static TaskID<Void> runBM_66(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[247]####
        // ensure Method variable is set//####[247]####
        if (__pt__runBM_66_Benchmark_method == null) {//####[247]####
            __pt__runBM_66_Benchmark_ensureMethodVarSet();//####[247]####
        }//####[247]####
        taskinfo.setTaskIdArgIndexes(0);//####[247]####
        taskinfo.addDependsOn(benchmark);//####[247]####
        taskinfo.setParameters(benchmark);//####[247]####
        taskinfo.setMethod(__pt__runBM_66_Benchmark_method);//####[247]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[247]####
    }//####[247]####
    private static TaskID<Void> runBM_66(BlockingQueue<Benchmark> benchmark) {//####[247]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[247]####
        return runBM_66(benchmark, new TaskInfo());//####[247]####
    }//####[247]####
    private static TaskID<Void> runBM_66(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[247]####
        // ensure Method variable is set//####[247]####
        if (__pt__runBM_66_Benchmark_method == null) {//####[247]####
            __pt__runBM_66_Benchmark_ensureMethodVarSet();//####[247]####
        }//####[247]####
        taskinfo.setQueueArgIndexes(0);//####[247]####
        taskinfo.setIsPipeline(true);//####[247]####
        taskinfo.setParameters(benchmark);//####[247]####
        taskinfo.setMethod(__pt__runBM_66_Benchmark_method);//####[247]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[247]####
    }//####[247]####
    public static void __pt__runBM_66(Benchmark benchmark) {//####[247]####
        try {//####[247]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[247]####
        } catch (IllegalAccessException e) {//####[247]####
            e.printStackTrace();//####[247]####
        } catch (IllegalArgumentException e) {//####[247]####
            e.printStackTrace();//####[247]####
        } catch (InvocationTargetException e) {//####[247]####
            e.printStackTrace();//####[247]####
        }//####[247]####
    }//####[247]####
//####[247]####
//####[248]####
    private static volatile Method __pt__runBM_68_Benchmark_method = null;//####[248]####
    private synchronized static void __pt__runBM_68_Benchmark_ensureMethodVarSet() {//####[248]####
        if (__pt__runBM_68_Benchmark_method == null) {//####[248]####
            try {//####[248]####
                __pt__runBM_68_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_68", new Class[] {//####[248]####
                    Benchmark.class//####[248]####
                });//####[248]####
            } catch (Exception e) {//####[248]####
                e.printStackTrace();//####[248]####
            }//####[248]####
        }//####[248]####
    }//####[248]####
    private static TaskID<Void> runBM_68(Benchmark benchmark) {//####[248]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[248]####
        return runBM_68(benchmark, new TaskInfo());//####[248]####
    }//####[248]####
    private static TaskID<Void> runBM_68(Benchmark benchmark, TaskInfo taskinfo) {//####[248]####
        // ensure Method variable is set//####[248]####
        if (__pt__runBM_68_Benchmark_method == null) {//####[248]####
            __pt__runBM_68_Benchmark_ensureMethodVarSet();//####[248]####
        }//####[248]####
        taskinfo.setParameters(benchmark);//####[248]####
        taskinfo.setMethod(__pt__runBM_68_Benchmark_method);//####[248]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[248]####
    }//####[248]####
    private static TaskID<Void> runBM_68(TaskID<Benchmark> benchmark) {//####[248]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[248]####
        return runBM_68(benchmark, new TaskInfo());//####[248]####
    }//####[248]####
    private static TaskID<Void> runBM_68(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[248]####
        // ensure Method variable is set//####[248]####
        if (__pt__runBM_68_Benchmark_method == null) {//####[248]####
            __pt__runBM_68_Benchmark_ensureMethodVarSet();//####[248]####
        }//####[248]####
        taskinfo.setTaskIdArgIndexes(0);//####[248]####
        taskinfo.addDependsOn(benchmark);//####[248]####
        taskinfo.setParameters(benchmark);//####[248]####
        taskinfo.setMethod(__pt__runBM_68_Benchmark_method);//####[248]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[248]####
    }//####[248]####
    private static TaskID<Void> runBM_68(BlockingQueue<Benchmark> benchmark) {//####[248]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[248]####
        return runBM_68(benchmark, new TaskInfo());//####[248]####
    }//####[248]####
    private static TaskID<Void> runBM_68(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[248]####
        // ensure Method variable is set//####[248]####
        if (__pt__runBM_68_Benchmark_method == null) {//####[248]####
            __pt__runBM_68_Benchmark_ensureMethodVarSet();//####[248]####
        }//####[248]####
        taskinfo.setQueueArgIndexes(0);//####[248]####
        taskinfo.setIsPipeline(true);//####[248]####
        taskinfo.setParameters(benchmark);//####[248]####
        taskinfo.setMethod(__pt__runBM_68_Benchmark_method);//####[248]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[248]####
    }//####[248]####
    public static void __pt__runBM_68(Benchmark benchmark) {//####[248]####
        try {//####[248]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[248]####
        } catch (IllegalAccessException e) {//####[248]####
            e.printStackTrace();//####[248]####
        } catch (IllegalArgumentException e) {//####[248]####
            e.printStackTrace();//####[248]####
        } catch (InvocationTargetException e) {//####[248]####
            e.printStackTrace();//####[248]####
        }//####[248]####
    }//####[248]####
//####[248]####
//####[249]####
    private static volatile Method __pt__runBM_72_Benchmark_method = null;//####[249]####
    private synchronized static void __pt__runBM_72_Benchmark_ensureMethodVarSet() {//####[249]####
        if (__pt__runBM_72_Benchmark_method == null) {//####[249]####
            try {//####[249]####
                __pt__runBM_72_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_72", new Class[] {//####[249]####
                    Benchmark.class//####[249]####
                });//####[249]####
            } catch (Exception e) {//####[249]####
                e.printStackTrace();//####[249]####
            }//####[249]####
        }//####[249]####
    }//####[249]####
    private static TaskID<Void> runBM_72(Benchmark benchmark) {//####[249]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[249]####
        return runBM_72(benchmark, new TaskInfo());//####[249]####
    }//####[249]####
    private static TaskID<Void> runBM_72(Benchmark benchmark, TaskInfo taskinfo) {//####[249]####
        // ensure Method variable is set//####[249]####
        if (__pt__runBM_72_Benchmark_method == null) {//####[249]####
            __pt__runBM_72_Benchmark_ensureMethodVarSet();//####[249]####
        }//####[249]####
        taskinfo.setParameters(benchmark);//####[249]####
        taskinfo.setMethod(__pt__runBM_72_Benchmark_method);//####[249]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[249]####
    }//####[249]####
    private static TaskID<Void> runBM_72(TaskID<Benchmark> benchmark) {//####[249]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[249]####
        return runBM_72(benchmark, new TaskInfo());//####[249]####
    }//####[249]####
    private static TaskID<Void> runBM_72(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[249]####
        // ensure Method variable is set//####[249]####
        if (__pt__runBM_72_Benchmark_method == null) {//####[249]####
            __pt__runBM_72_Benchmark_ensureMethodVarSet();//####[249]####
        }//####[249]####
        taskinfo.setTaskIdArgIndexes(0);//####[249]####
        taskinfo.addDependsOn(benchmark);//####[249]####
        taskinfo.setParameters(benchmark);//####[249]####
        taskinfo.setMethod(__pt__runBM_72_Benchmark_method);//####[249]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[249]####
    }//####[249]####
    private static TaskID<Void> runBM_72(BlockingQueue<Benchmark> benchmark) {//####[249]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[249]####
        return runBM_72(benchmark, new TaskInfo());//####[249]####
    }//####[249]####
    private static TaskID<Void> runBM_72(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[249]####
        // ensure Method variable is set//####[249]####
        if (__pt__runBM_72_Benchmark_method == null) {//####[249]####
            __pt__runBM_72_Benchmark_ensureMethodVarSet();//####[249]####
        }//####[249]####
        taskinfo.setQueueArgIndexes(0);//####[249]####
        taskinfo.setIsPipeline(true);//####[249]####
        taskinfo.setParameters(benchmark);//####[249]####
        taskinfo.setMethod(__pt__runBM_72_Benchmark_method);//####[249]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[249]####
    }//####[249]####
    public static void __pt__runBM_72(Benchmark benchmark) {//####[249]####
        try {//####[249]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[249]####
        } catch (IllegalAccessException e) {//####[249]####
            e.printStackTrace();//####[249]####
        } catch (IllegalArgumentException e) {//####[249]####
            e.printStackTrace();//####[249]####
        } catch (InvocationTargetException e) {//####[249]####
            e.printStackTrace();//####[249]####
        }//####[249]####
    }//####[249]####
//####[249]####
//####[250]####
    private static volatile Method __pt__runBM_75_Benchmark_method = null;//####[250]####
    private synchronized static void __pt__runBM_75_Benchmark_ensureMethodVarSet() {//####[250]####
        if (__pt__runBM_75_Benchmark_method == null) {//####[250]####
            try {//####[250]####
                __pt__runBM_75_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_75", new Class[] {//####[250]####
                    Benchmark.class//####[250]####
                });//####[250]####
            } catch (Exception e) {//####[250]####
                e.printStackTrace();//####[250]####
            }//####[250]####
        }//####[250]####
    }//####[250]####
    private static TaskID<Void> runBM_75(Benchmark benchmark) {//####[250]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[250]####
        return runBM_75(benchmark, new TaskInfo());//####[250]####
    }//####[250]####
    private static TaskID<Void> runBM_75(Benchmark benchmark, TaskInfo taskinfo) {//####[250]####
        // ensure Method variable is set//####[250]####
        if (__pt__runBM_75_Benchmark_method == null) {//####[250]####
            __pt__runBM_75_Benchmark_ensureMethodVarSet();//####[250]####
        }//####[250]####
        taskinfo.setParameters(benchmark);//####[250]####
        taskinfo.setMethod(__pt__runBM_75_Benchmark_method);//####[250]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[250]####
    }//####[250]####
    private static TaskID<Void> runBM_75(TaskID<Benchmark> benchmark) {//####[250]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[250]####
        return runBM_75(benchmark, new TaskInfo());//####[250]####
    }//####[250]####
    private static TaskID<Void> runBM_75(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[250]####
        // ensure Method variable is set//####[250]####
        if (__pt__runBM_75_Benchmark_method == null) {//####[250]####
            __pt__runBM_75_Benchmark_ensureMethodVarSet();//####[250]####
        }//####[250]####
        taskinfo.setTaskIdArgIndexes(0);//####[250]####
        taskinfo.addDependsOn(benchmark);//####[250]####
        taskinfo.setParameters(benchmark);//####[250]####
        taskinfo.setMethod(__pt__runBM_75_Benchmark_method);//####[250]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[250]####
    }//####[250]####
    private static TaskID<Void> runBM_75(BlockingQueue<Benchmark> benchmark) {//####[250]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[250]####
        return runBM_75(benchmark, new TaskInfo());//####[250]####
    }//####[250]####
    private static TaskID<Void> runBM_75(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[250]####
        // ensure Method variable is set//####[250]####
        if (__pt__runBM_75_Benchmark_method == null) {//####[250]####
            __pt__runBM_75_Benchmark_ensureMethodVarSet();//####[250]####
        }//####[250]####
        taskinfo.setQueueArgIndexes(0);//####[250]####
        taskinfo.setIsPipeline(true);//####[250]####
        taskinfo.setParameters(benchmark);//####[250]####
        taskinfo.setMethod(__pt__runBM_75_Benchmark_method);//####[250]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[250]####
    }//####[250]####
    public static void __pt__runBM_75(Benchmark benchmark) {//####[250]####
        try {//####[250]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[250]####
        } catch (IllegalAccessException e) {//####[250]####
            e.printStackTrace();//####[250]####
        } catch (IllegalArgumentException e) {//####[250]####
            e.printStackTrace();//####[250]####
        } catch (InvocationTargetException e) {//####[250]####
            e.printStackTrace();//####[250]####
        }//####[250]####
    }//####[250]####
//####[250]####
//####[251]####
    private static volatile Method __pt__runBM_76_Benchmark_method = null;//####[251]####
    private synchronized static void __pt__runBM_76_Benchmark_ensureMethodVarSet() {//####[251]####
        if (__pt__runBM_76_Benchmark_method == null) {//####[251]####
            try {//####[251]####
                __pt__runBM_76_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_76", new Class[] {//####[251]####
                    Benchmark.class//####[251]####
                });//####[251]####
            } catch (Exception e) {//####[251]####
                e.printStackTrace();//####[251]####
            }//####[251]####
        }//####[251]####
    }//####[251]####
    private static TaskID<Void> runBM_76(Benchmark benchmark) {//####[251]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[251]####
        return runBM_76(benchmark, new TaskInfo());//####[251]####
    }//####[251]####
    private static TaskID<Void> runBM_76(Benchmark benchmark, TaskInfo taskinfo) {//####[251]####
        // ensure Method variable is set//####[251]####
        if (__pt__runBM_76_Benchmark_method == null) {//####[251]####
            __pt__runBM_76_Benchmark_ensureMethodVarSet();//####[251]####
        }//####[251]####
        taskinfo.setParameters(benchmark);//####[251]####
        taskinfo.setMethod(__pt__runBM_76_Benchmark_method);//####[251]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[251]####
    }//####[251]####
    private static TaskID<Void> runBM_76(TaskID<Benchmark> benchmark) {//####[251]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[251]####
        return runBM_76(benchmark, new TaskInfo());//####[251]####
    }//####[251]####
    private static TaskID<Void> runBM_76(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[251]####
        // ensure Method variable is set//####[251]####
        if (__pt__runBM_76_Benchmark_method == null) {//####[251]####
            __pt__runBM_76_Benchmark_ensureMethodVarSet();//####[251]####
        }//####[251]####
        taskinfo.setTaskIdArgIndexes(0);//####[251]####
        taskinfo.addDependsOn(benchmark);//####[251]####
        taskinfo.setParameters(benchmark);//####[251]####
        taskinfo.setMethod(__pt__runBM_76_Benchmark_method);//####[251]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[251]####
    }//####[251]####
    private static TaskID<Void> runBM_76(BlockingQueue<Benchmark> benchmark) {//####[251]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[251]####
        return runBM_76(benchmark, new TaskInfo());//####[251]####
    }//####[251]####
    private static TaskID<Void> runBM_76(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[251]####
        // ensure Method variable is set//####[251]####
        if (__pt__runBM_76_Benchmark_method == null) {//####[251]####
            __pt__runBM_76_Benchmark_ensureMethodVarSet();//####[251]####
        }//####[251]####
        taskinfo.setQueueArgIndexes(0);//####[251]####
        taskinfo.setIsPipeline(true);//####[251]####
        taskinfo.setParameters(benchmark);//####[251]####
        taskinfo.setMethod(__pt__runBM_76_Benchmark_method);//####[251]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[251]####
    }//####[251]####
    public static void __pt__runBM_76(Benchmark benchmark) {//####[251]####
        try {//####[251]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[251]####
        } catch (IllegalAccessException e) {//####[251]####
            e.printStackTrace();//####[251]####
        } catch (IllegalArgumentException e) {//####[251]####
            e.printStackTrace();//####[251]####
        } catch (InvocationTargetException e) {//####[251]####
            e.printStackTrace();//####[251]####
        }//####[251]####
    }//####[251]####
//####[251]####
//####[252]####
    private static volatile Method __pt__runBM_77_Benchmark_method = null;//####[252]####
    private synchronized static void __pt__runBM_77_Benchmark_ensureMethodVarSet() {//####[252]####
        if (__pt__runBM_77_Benchmark_method == null) {//####[252]####
            try {//####[252]####
                __pt__runBM_77_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_77", new Class[] {//####[252]####
                    Benchmark.class//####[252]####
                });//####[252]####
            } catch (Exception e) {//####[252]####
                e.printStackTrace();//####[252]####
            }//####[252]####
        }//####[252]####
    }//####[252]####
    private static TaskID<Void> runBM_77(Benchmark benchmark) {//####[252]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[252]####
        return runBM_77(benchmark, new TaskInfo());//####[252]####
    }//####[252]####
    private static TaskID<Void> runBM_77(Benchmark benchmark, TaskInfo taskinfo) {//####[252]####
        // ensure Method variable is set//####[252]####
        if (__pt__runBM_77_Benchmark_method == null) {//####[252]####
            __pt__runBM_77_Benchmark_ensureMethodVarSet();//####[252]####
        }//####[252]####
        taskinfo.setParameters(benchmark);//####[252]####
        taskinfo.setMethod(__pt__runBM_77_Benchmark_method);//####[252]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[252]####
    }//####[252]####
    private static TaskID<Void> runBM_77(TaskID<Benchmark> benchmark) {//####[252]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[252]####
        return runBM_77(benchmark, new TaskInfo());//####[252]####
    }//####[252]####
    private static TaskID<Void> runBM_77(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[252]####
        // ensure Method variable is set//####[252]####
        if (__pt__runBM_77_Benchmark_method == null) {//####[252]####
            __pt__runBM_77_Benchmark_ensureMethodVarSet();//####[252]####
        }//####[252]####
        taskinfo.setTaskIdArgIndexes(0);//####[252]####
        taskinfo.addDependsOn(benchmark);//####[252]####
        taskinfo.setParameters(benchmark);//####[252]####
        taskinfo.setMethod(__pt__runBM_77_Benchmark_method);//####[252]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[252]####
    }//####[252]####
    private static TaskID<Void> runBM_77(BlockingQueue<Benchmark> benchmark) {//####[252]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[252]####
        return runBM_77(benchmark, new TaskInfo());//####[252]####
    }//####[252]####
    private static TaskID<Void> runBM_77(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[252]####
        // ensure Method variable is set//####[252]####
        if (__pt__runBM_77_Benchmark_method == null) {//####[252]####
            __pt__runBM_77_Benchmark_ensureMethodVarSet();//####[252]####
        }//####[252]####
        taskinfo.setQueueArgIndexes(0);//####[252]####
        taskinfo.setIsPipeline(true);//####[252]####
        taskinfo.setParameters(benchmark);//####[252]####
        taskinfo.setMethod(__pt__runBM_77_Benchmark_method);//####[252]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[252]####
    }//####[252]####
    public static void __pt__runBM_77(Benchmark benchmark) {//####[252]####
        try {//####[252]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[252]####
        } catch (IllegalAccessException e) {//####[252]####
            e.printStackTrace();//####[252]####
        } catch (IllegalArgumentException e) {//####[252]####
            e.printStackTrace();//####[252]####
        } catch (InvocationTargetException e) {//####[252]####
            e.printStackTrace();//####[252]####
        }//####[252]####
    }//####[252]####
//####[252]####
//####[253]####
    private static volatile Method __pt__runBM_79_Benchmark_method = null;//####[253]####
    private synchronized static void __pt__runBM_79_Benchmark_ensureMethodVarSet() {//####[253]####
        if (__pt__runBM_79_Benchmark_method == null) {//####[253]####
            try {//####[253]####
                __pt__runBM_79_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_79", new Class[] {//####[253]####
                    Benchmark.class//####[253]####
                });//####[253]####
            } catch (Exception e) {//####[253]####
                e.printStackTrace();//####[253]####
            }//####[253]####
        }//####[253]####
    }//####[253]####
    private static TaskID<Void> runBM_79(Benchmark benchmark) {//####[253]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[253]####
        return runBM_79(benchmark, new TaskInfo());//####[253]####
    }//####[253]####
    private static TaskID<Void> runBM_79(Benchmark benchmark, TaskInfo taskinfo) {//####[253]####
        // ensure Method variable is set//####[253]####
        if (__pt__runBM_79_Benchmark_method == null) {//####[253]####
            __pt__runBM_79_Benchmark_ensureMethodVarSet();//####[253]####
        }//####[253]####
        taskinfo.setParameters(benchmark);//####[253]####
        taskinfo.setMethod(__pt__runBM_79_Benchmark_method);//####[253]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[253]####
    }//####[253]####
    private static TaskID<Void> runBM_79(TaskID<Benchmark> benchmark) {//####[253]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[253]####
        return runBM_79(benchmark, new TaskInfo());//####[253]####
    }//####[253]####
    private static TaskID<Void> runBM_79(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[253]####
        // ensure Method variable is set//####[253]####
        if (__pt__runBM_79_Benchmark_method == null) {//####[253]####
            __pt__runBM_79_Benchmark_ensureMethodVarSet();//####[253]####
        }//####[253]####
        taskinfo.setTaskIdArgIndexes(0);//####[253]####
        taskinfo.addDependsOn(benchmark);//####[253]####
        taskinfo.setParameters(benchmark);//####[253]####
        taskinfo.setMethod(__pt__runBM_79_Benchmark_method);//####[253]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[253]####
    }//####[253]####
    private static TaskID<Void> runBM_79(BlockingQueue<Benchmark> benchmark) {//####[253]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[253]####
        return runBM_79(benchmark, new TaskInfo());//####[253]####
    }//####[253]####
    private static TaskID<Void> runBM_79(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[253]####
        // ensure Method variable is set//####[253]####
        if (__pt__runBM_79_Benchmark_method == null) {//####[253]####
            __pt__runBM_79_Benchmark_ensureMethodVarSet();//####[253]####
        }//####[253]####
        taskinfo.setQueueArgIndexes(0);//####[253]####
        taskinfo.setIsPipeline(true);//####[253]####
        taskinfo.setParameters(benchmark);//####[253]####
        taskinfo.setMethod(__pt__runBM_79_Benchmark_method);//####[253]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[253]####
    }//####[253]####
    public static void __pt__runBM_79(Benchmark benchmark) {//####[253]####
        try {//####[253]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[253]####
        } catch (IllegalAccessException e) {//####[253]####
            e.printStackTrace();//####[253]####
        } catch (IllegalArgumentException e) {//####[253]####
            e.printStackTrace();//####[253]####
        } catch (InvocationTargetException e) {//####[253]####
            e.printStackTrace();//####[253]####
        }//####[253]####
    }//####[253]####
//####[253]####
//####[254]####
    private static volatile Method __pt__runBM_80_Benchmark_method = null;//####[254]####
    private synchronized static void __pt__runBM_80_Benchmark_ensureMethodVarSet() {//####[254]####
        if (__pt__runBM_80_Benchmark_method == null) {//####[254]####
            try {//####[254]####
                __pt__runBM_80_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_80", new Class[] {//####[254]####
                    Benchmark.class//####[254]####
                });//####[254]####
            } catch (Exception e) {//####[254]####
                e.printStackTrace();//####[254]####
            }//####[254]####
        }//####[254]####
    }//####[254]####
    private static TaskID<Void> runBM_80(Benchmark benchmark) {//####[254]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[254]####
        return runBM_80(benchmark, new TaskInfo());//####[254]####
    }//####[254]####
    private static TaskID<Void> runBM_80(Benchmark benchmark, TaskInfo taskinfo) {//####[254]####
        // ensure Method variable is set//####[254]####
        if (__pt__runBM_80_Benchmark_method == null) {//####[254]####
            __pt__runBM_80_Benchmark_ensureMethodVarSet();//####[254]####
        }//####[254]####
        taskinfo.setParameters(benchmark);//####[254]####
        taskinfo.setMethod(__pt__runBM_80_Benchmark_method);//####[254]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[254]####
    }//####[254]####
    private static TaskID<Void> runBM_80(TaskID<Benchmark> benchmark) {//####[254]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[254]####
        return runBM_80(benchmark, new TaskInfo());//####[254]####
    }//####[254]####
    private static TaskID<Void> runBM_80(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[254]####
        // ensure Method variable is set//####[254]####
        if (__pt__runBM_80_Benchmark_method == null) {//####[254]####
            __pt__runBM_80_Benchmark_ensureMethodVarSet();//####[254]####
        }//####[254]####
        taskinfo.setTaskIdArgIndexes(0);//####[254]####
        taskinfo.addDependsOn(benchmark);//####[254]####
        taskinfo.setParameters(benchmark);//####[254]####
        taskinfo.setMethod(__pt__runBM_80_Benchmark_method);//####[254]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[254]####
    }//####[254]####
    private static TaskID<Void> runBM_80(BlockingQueue<Benchmark> benchmark) {//####[254]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[254]####
        return runBM_80(benchmark, new TaskInfo());//####[254]####
    }//####[254]####
    private static TaskID<Void> runBM_80(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[254]####
        // ensure Method variable is set//####[254]####
        if (__pt__runBM_80_Benchmark_method == null) {//####[254]####
            __pt__runBM_80_Benchmark_ensureMethodVarSet();//####[254]####
        }//####[254]####
        taskinfo.setQueueArgIndexes(0);//####[254]####
        taskinfo.setIsPipeline(true);//####[254]####
        taskinfo.setParameters(benchmark);//####[254]####
        taskinfo.setMethod(__pt__runBM_80_Benchmark_method);//####[254]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[254]####
    }//####[254]####
    public static void __pt__runBM_80(Benchmark benchmark) {//####[254]####
        try {//####[254]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[254]####
        } catch (IllegalAccessException e) {//####[254]####
            e.printStackTrace();//####[254]####
        } catch (IllegalArgumentException e) {//####[254]####
            e.printStackTrace();//####[254]####
        } catch (InvocationTargetException e) {//####[254]####
            e.printStackTrace();//####[254]####
        }//####[254]####
    }//####[254]####
//####[254]####
//####[255]####
    private static volatile Method __pt__runBM_83_Benchmark_method = null;//####[255]####
    private synchronized static void __pt__runBM_83_Benchmark_ensureMethodVarSet() {//####[255]####
        if (__pt__runBM_83_Benchmark_method == null) {//####[255]####
            try {//####[255]####
                __pt__runBM_83_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_83", new Class[] {//####[255]####
                    Benchmark.class//####[255]####
                });//####[255]####
            } catch (Exception e) {//####[255]####
                e.printStackTrace();//####[255]####
            }//####[255]####
        }//####[255]####
    }//####[255]####
    private static TaskID<Void> runBM_83(Benchmark benchmark) {//####[255]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[255]####
        return runBM_83(benchmark, new TaskInfo());//####[255]####
    }//####[255]####
    private static TaskID<Void> runBM_83(Benchmark benchmark, TaskInfo taskinfo) {//####[255]####
        // ensure Method variable is set//####[255]####
        if (__pt__runBM_83_Benchmark_method == null) {//####[255]####
            __pt__runBM_83_Benchmark_ensureMethodVarSet();//####[255]####
        }//####[255]####
        taskinfo.setParameters(benchmark);//####[255]####
        taskinfo.setMethod(__pt__runBM_83_Benchmark_method);//####[255]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[255]####
    }//####[255]####
    private static TaskID<Void> runBM_83(TaskID<Benchmark> benchmark) {//####[255]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[255]####
        return runBM_83(benchmark, new TaskInfo());//####[255]####
    }//####[255]####
    private static TaskID<Void> runBM_83(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[255]####
        // ensure Method variable is set//####[255]####
        if (__pt__runBM_83_Benchmark_method == null) {//####[255]####
            __pt__runBM_83_Benchmark_ensureMethodVarSet();//####[255]####
        }//####[255]####
        taskinfo.setTaskIdArgIndexes(0);//####[255]####
        taskinfo.addDependsOn(benchmark);//####[255]####
        taskinfo.setParameters(benchmark);//####[255]####
        taskinfo.setMethod(__pt__runBM_83_Benchmark_method);//####[255]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[255]####
    }//####[255]####
    private static TaskID<Void> runBM_83(BlockingQueue<Benchmark> benchmark) {//####[255]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[255]####
        return runBM_83(benchmark, new TaskInfo());//####[255]####
    }//####[255]####
    private static TaskID<Void> runBM_83(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[255]####
        // ensure Method variable is set//####[255]####
        if (__pt__runBM_83_Benchmark_method == null) {//####[255]####
            __pt__runBM_83_Benchmark_ensureMethodVarSet();//####[255]####
        }//####[255]####
        taskinfo.setQueueArgIndexes(0);//####[255]####
        taskinfo.setIsPipeline(true);//####[255]####
        taskinfo.setParameters(benchmark);//####[255]####
        taskinfo.setMethod(__pt__runBM_83_Benchmark_method);//####[255]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[255]####
    }//####[255]####
    public static void __pt__runBM_83(Benchmark benchmark) {//####[255]####
        try {//####[255]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[255]####
        } catch (IllegalAccessException e) {//####[255]####
            e.printStackTrace();//####[255]####
        } catch (IllegalArgumentException e) {//####[255]####
            e.printStackTrace();//####[255]####
        } catch (InvocationTargetException e) {//####[255]####
            e.printStackTrace();//####[255]####
        }//####[255]####
    }//####[255]####
//####[255]####
//####[256]####
    private static volatile Method __pt__runBM_84_Benchmark_method = null;//####[256]####
    private synchronized static void __pt__runBM_84_Benchmark_ensureMethodVarSet() {//####[256]####
        if (__pt__runBM_84_Benchmark_method == null) {//####[256]####
            try {//####[256]####
                __pt__runBM_84_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_84", new Class[] {//####[256]####
                    Benchmark.class//####[256]####
                });//####[256]####
            } catch (Exception e) {//####[256]####
                e.printStackTrace();//####[256]####
            }//####[256]####
        }//####[256]####
    }//####[256]####
    private static TaskID<Void> runBM_84(Benchmark benchmark) {//####[256]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[256]####
        return runBM_84(benchmark, new TaskInfo());//####[256]####
    }//####[256]####
    private static TaskID<Void> runBM_84(Benchmark benchmark, TaskInfo taskinfo) {//####[256]####
        // ensure Method variable is set//####[256]####
        if (__pt__runBM_84_Benchmark_method == null) {//####[256]####
            __pt__runBM_84_Benchmark_ensureMethodVarSet();//####[256]####
        }//####[256]####
        taskinfo.setParameters(benchmark);//####[256]####
        taskinfo.setMethod(__pt__runBM_84_Benchmark_method);//####[256]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[256]####
    }//####[256]####
    private static TaskID<Void> runBM_84(TaskID<Benchmark> benchmark) {//####[256]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[256]####
        return runBM_84(benchmark, new TaskInfo());//####[256]####
    }//####[256]####
    private static TaskID<Void> runBM_84(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[256]####
        // ensure Method variable is set//####[256]####
        if (__pt__runBM_84_Benchmark_method == null) {//####[256]####
            __pt__runBM_84_Benchmark_ensureMethodVarSet();//####[256]####
        }//####[256]####
        taskinfo.setTaskIdArgIndexes(0);//####[256]####
        taskinfo.addDependsOn(benchmark);//####[256]####
        taskinfo.setParameters(benchmark);//####[256]####
        taskinfo.setMethod(__pt__runBM_84_Benchmark_method);//####[256]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[256]####
    }//####[256]####
    private static TaskID<Void> runBM_84(BlockingQueue<Benchmark> benchmark) {//####[256]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[256]####
        return runBM_84(benchmark, new TaskInfo());//####[256]####
    }//####[256]####
    private static TaskID<Void> runBM_84(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[256]####
        // ensure Method variable is set//####[256]####
        if (__pt__runBM_84_Benchmark_method == null) {//####[256]####
            __pt__runBM_84_Benchmark_ensureMethodVarSet();//####[256]####
        }//####[256]####
        taskinfo.setQueueArgIndexes(0);//####[256]####
        taskinfo.setIsPipeline(true);//####[256]####
        taskinfo.setParameters(benchmark);//####[256]####
        taskinfo.setMethod(__pt__runBM_84_Benchmark_method);//####[256]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[256]####
    }//####[256]####
    public static void __pt__runBM_84(Benchmark benchmark) {//####[256]####
        try {//####[256]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[256]####
        } catch (IllegalAccessException e) {//####[256]####
            e.printStackTrace();//####[256]####
        } catch (IllegalArgumentException e) {//####[256]####
            e.printStackTrace();//####[256]####
        } catch (InvocationTargetException e) {//####[256]####
            e.printStackTrace();//####[256]####
        }//####[256]####
    }//####[256]####
//####[256]####
//####[257]####
    private static volatile Method __pt__runBM_89_Benchmark_method = null;//####[257]####
    private synchronized static void __pt__runBM_89_Benchmark_ensureMethodVarSet() {//####[257]####
        if (__pt__runBM_89_Benchmark_method == null) {//####[257]####
            try {//####[257]####
                __pt__runBM_89_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_89", new Class[] {//####[257]####
                    Benchmark.class//####[257]####
                });//####[257]####
            } catch (Exception e) {//####[257]####
                e.printStackTrace();//####[257]####
            }//####[257]####
        }//####[257]####
    }//####[257]####
    private static TaskID<Void> runBM_89(Benchmark benchmark) {//####[257]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[257]####
        return runBM_89(benchmark, new TaskInfo());//####[257]####
    }//####[257]####
    private static TaskID<Void> runBM_89(Benchmark benchmark, TaskInfo taskinfo) {//####[257]####
        // ensure Method variable is set//####[257]####
        if (__pt__runBM_89_Benchmark_method == null) {//####[257]####
            __pt__runBM_89_Benchmark_ensureMethodVarSet();//####[257]####
        }//####[257]####
        taskinfo.setParameters(benchmark);//####[257]####
        taskinfo.setMethod(__pt__runBM_89_Benchmark_method);//####[257]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[257]####
    }//####[257]####
    private static TaskID<Void> runBM_89(TaskID<Benchmark> benchmark) {//####[257]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[257]####
        return runBM_89(benchmark, new TaskInfo());//####[257]####
    }//####[257]####
    private static TaskID<Void> runBM_89(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[257]####
        // ensure Method variable is set//####[257]####
        if (__pt__runBM_89_Benchmark_method == null) {//####[257]####
            __pt__runBM_89_Benchmark_ensureMethodVarSet();//####[257]####
        }//####[257]####
        taskinfo.setTaskIdArgIndexes(0);//####[257]####
        taskinfo.addDependsOn(benchmark);//####[257]####
        taskinfo.setParameters(benchmark);//####[257]####
        taskinfo.setMethod(__pt__runBM_89_Benchmark_method);//####[257]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[257]####
    }//####[257]####
    private static TaskID<Void> runBM_89(BlockingQueue<Benchmark> benchmark) {//####[257]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[257]####
        return runBM_89(benchmark, new TaskInfo());//####[257]####
    }//####[257]####
    private static TaskID<Void> runBM_89(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[257]####
        // ensure Method variable is set//####[257]####
        if (__pt__runBM_89_Benchmark_method == null) {//####[257]####
            __pt__runBM_89_Benchmark_ensureMethodVarSet();//####[257]####
        }//####[257]####
        taskinfo.setQueueArgIndexes(0);//####[257]####
        taskinfo.setIsPipeline(true);//####[257]####
        taskinfo.setParameters(benchmark);//####[257]####
        taskinfo.setMethod(__pt__runBM_89_Benchmark_method);//####[257]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[257]####
    }//####[257]####
    public static void __pt__runBM_89(Benchmark benchmark) {//####[257]####
        try {//####[257]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[257]####
        } catch (IllegalAccessException e) {//####[257]####
            e.printStackTrace();//####[257]####
        } catch (IllegalArgumentException e) {//####[257]####
            e.printStackTrace();//####[257]####
        } catch (InvocationTargetException e) {//####[257]####
            e.printStackTrace();//####[257]####
        }//####[257]####
    }//####[257]####
//####[257]####
//####[258]####
    private static volatile Method __pt__runBM_90_Benchmark_method = null;//####[258]####
    private synchronized static void __pt__runBM_90_Benchmark_ensureMethodVarSet() {//####[258]####
        if (__pt__runBM_90_Benchmark_method == null) {//####[258]####
            try {//####[258]####
                __pt__runBM_90_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_90", new Class[] {//####[258]####
                    Benchmark.class//####[258]####
                });//####[258]####
            } catch (Exception e) {//####[258]####
                e.printStackTrace();//####[258]####
            }//####[258]####
        }//####[258]####
    }//####[258]####
    private static TaskID<Void> runBM_90(Benchmark benchmark) {//####[258]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[258]####
        return runBM_90(benchmark, new TaskInfo());//####[258]####
    }//####[258]####
    private static TaskID<Void> runBM_90(Benchmark benchmark, TaskInfo taskinfo) {//####[258]####
        // ensure Method variable is set//####[258]####
        if (__pt__runBM_90_Benchmark_method == null) {//####[258]####
            __pt__runBM_90_Benchmark_ensureMethodVarSet();//####[258]####
        }//####[258]####
        taskinfo.setParameters(benchmark);//####[258]####
        taskinfo.setMethod(__pt__runBM_90_Benchmark_method);//####[258]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[258]####
    }//####[258]####
    private static TaskID<Void> runBM_90(TaskID<Benchmark> benchmark) {//####[258]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[258]####
        return runBM_90(benchmark, new TaskInfo());//####[258]####
    }//####[258]####
    private static TaskID<Void> runBM_90(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[258]####
        // ensure Method variable is set//####[258]####
        if (__pt__runBM_90_Benchmark_method == null) {//####[258]####
            __pt__runBM_90_Benchmark_ensureMethodVarSet();//####[258]####
        }//####[258]####
        taskinfo.setTaskIdArgIndexes(0);//####[258]####
        taskinfo.addDependsOn(benchmark);//####[258]####
        taskinfo.setParameters(benchmark);//####[258]####
        taskinfo.setMethod(__pt__runBM_90_Benchmark_method);//####[258]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[258]####
    }//####[258]####
    private static TaskID<Void> runBM_90(BlockingQueue<Benchmark> benchmark) {//####[258]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[258]####
        return runBM_90(benchmark, new TaskInfo());//####[258]####
    }//####[258]####
    private static TaskID<Void> runBM_90(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[258]####
        // ensure Method variable is set//####[258]####
        if (__pt__runBM_90_Benchmark_method == null) {//####[258]####
            __pt__runBM_90_Benchmark_ensureMethodVarSet();//####[258]####
        }//####[258]####
        taskinfo.setQueueArgIndexes(0);//####[258]####
        taskinfo.setIsPipeline(true);//####[258]####
        taskinfo.setParameters(benchmark);//####[258]####
        taskinfo.setMethod(__pt__runBM_90_Benchmark_method);//####[258]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[258]####
    }//####[258]####
    public static void __pt__runBM_90(Benchmark benchmark) {//####[258]####
        try {//####[258]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[258]####
        } catch (IllegalAccessException e) {//####[258]####
            e.printStackTrace();//####[258]####
        } catch (IllegalArgumentException e) {//####[258]####
            e.printStackTrace();//####[258]####
        } catch (InvocationTargetException e) {//####[258]####
            e.printStackTrace();//####[258]####
        }//####[258]####
    }//####[258]####
//####[258]####
//####[259]####
    private static volatile Method __pt__runBM_93_Benchmark_method = null;//####[259]####
    private synchronized static void __pt__runBM_93_Benchmark_ensureMethodVarSet() {//####[259]####
        if (__pt__runBM_93_Benchmark_method == null) {//####[259]####
            try {//####[259]####
                __pt__runBM_93_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_93", new Class[] {//####[259]####
                    Benchmark.class//####[259]####
                });//####[259]####
            } catch (Exception e) {//####[259]####
                e.printStackTrace();//####[259]####
            }//####[259]####
        }//####[259]####
    }//####[259]####
    private static TaskID<Void> runBM_93(Benchmark benchmark) {//####[259]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[259]####
        return runBM_93(benchmark, new TaskInfo());//####[259]####
    }//####[259]####
    private static TaskID<Void> runBM_93(Benchmark benchmark, TaskInfo taskinfo) {//####[259]####
        // ensure Method variable is set//####[259]####
        if (__pt__runBM_93_Benchmark_method == null) {//####[259]####
            __pt__runBM_93_Benchmark_ensureMethodVarSet();//####[259]####
        }//####[259]####
        taskinfo.setParameters(benchmark);//####[259]####
        taskinfo.setMethod(__pt__runBM_93_Benchmark_method);//####[259]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[259]####
    }//####[259]####
    private static TaskID<Void> runBM_93(TaskID<Benchmark> benchmark) {//####[259]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[259]####
        return runBM_93(benchmark, new TaskInfo());//####[259]####
    }//####[259]####
    private static TaskID<Void> runBM_93(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[259]####
        // ensure Method variable is set//####[259]####
        if (__pt__runBM_93_Benchmark_method == null) {//####[259]####
            __pt__runBM_93_Benchmark_ensureMethodVarSet();//####[259]####
        }//####[259]####
        taskinfo.setTaskIdArgIndexes(0);//####[259]####
        taskinfo.addDependsOn(benchmark);//####[259]####
        taskinfo.setParameters(benchmark);//####[259]####
        taskinfo.setMethod(__pt__runBM_93_Benchmark_method);//####[259]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[259]####
    }//####[259]####
    private static TaskID<Void> runBM_93(BlockingQueue<Benchmark> benchmark) {//####[259]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[259]####
        return runBM_93(benchmark, new TaskInfo());//####[259]####
    }//####[259]####
    private static TaskID<Void> runBM_93(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[259]####
        // ensure Method variable is set//####[259]####
        if (__pt__runBM_93_Benchmark_method == null) {//####[259]####
            __pt__runBM_93_Benchmark_ensureMethodVarSet();//####[259]####
        }//####[259]####
        taskinfo.setQueueArgIndexes(0);//####[259]####
        taskinfo.setIsPipeline(true);//####[259]####
        taskinfo.setParameters(benchmark);//####[259]####
        taskinfo.setMethod(__pt__runBM_93_Benchmark_method);//####[259]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[259]####
    }//####[259]####
    public static void __pt__runBM_93(Benchmark benchmark) {//####[259]####
        try {//####[259]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[259]####
        } catch (IllegalAccessException e) {//####[259]####
            e.printStackTrace();//####[259]####
        } catch (IllegalArgumentException e) {//####[259]####
            e.printStackTrace();//####[259]####
        } catch (InvocationTargetException e) {//####[259]####
            e.printStackTrace();//####[259]####
        }//####[259]####
    }//####[259]####
//####[259]####
//####[260]####
    private static volatile Method __pt__runBM_94_Benchmark_method = null;//####[260]####
    private synchronized static void __pt__runBM_94_Benchmark_ensureMethodVarSet() {//####[260]####
        if (__pt__runBM_94_Benchmark_method == null) {//####[260]####
            try {//####[260]####
                __pt__runBM_94_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_94", new Class[] {//####[260]####
                    Benchmark.class//####[260]####
                });//####[260]####
            } catch (Exception e) {//####[260]####
                e.printStackTrace();//####[260]####
            }//####[260]####
        }//####[260]####
    }//####[260]####
    private static TaskID<Void> runBM_94(Benchmark benchmark) {//####[260]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[260]####
        return runBM_94(benchmark, new TaskInfo());//####[260]####
    }//####[260]####
    private static TaskID<Void> runBM_94(Benchmark benchmark, TaskInfo taskinfo) {//####[260]####
        // ensure Method variable is set//####[260]####
        if (__pt__runBM_94_Benchmark_method == null) {//####[260]####
            __pt__runBM_94_Benchmark_ensureMethodVarSet();//####[260]####
        }//####[260]####
        taskinfo.setParameters(benchmark);//####[260]####
        taskinfo.setMethod(__pt__runBM_94_Benchmark_method);//####[260]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[260]####
    }//####[260]####
    private static TaskID<Void> runBM_94(TaskID<Benchmark> benchmark) {//####[260]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[260]####
        return runBM_94(benchmark, new TaskInfo());//####[260]####
    }//####[260]####
    private static TaskID<Void> runBM_94(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[260]####
        // ensure Method variable is set//####[260]####
        if (__pt__runBM_94_Benchmark_method == null) {//####[260]####
            __pt__runBM_94_Benchmark_ensureMethodVarSet();//####[260]####
        }//####[260]####
        taskinfo.setTaskIdArgIndexes(0);//####[260]####
        taskinfo.addDependsOn(benchmark);//####[260]####
        taskinfo.setParameters(benchmark);//####[260]####
        taskinfo.setMethod(__pt__runBM_94_Benchmark_method);//####[260]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[260]####
    }//####[260]####
    private static TaskID<Void> runBM_94(BlockingQueue<Benchmark> benchmark) {//####[260]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[260]####
        return runBM_94(benchmark, new TaskInfo());//####[260]####
    }//####[260]####
    private static TaskID<Void> runBM_94(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[260]####
        // ensure Method variable is set//####[260]####
        if (__pt__runBM_94_Benchmark_method == null) {//####[260]####
            __pt__runBM_94_Benchmark_ensureMethodVarSet();//####[260]####
        }//####[260]####
        taskinfo.setQueueArgIndexes(0);//####[260]####
        taskinfo.setIsPipeline(true);//####[260]####
        taskinfo.setParameters(benchmark);//####[260]####
        taskinfo.setMethod(__pt__runBM_94_Benchmark_method);//####[260]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[260]####
    }//####[260]####
    public static void __pt__runBM_94(Benchmark benchmark) {//####[260]####
        try {//####[260]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[260]####
        } catch (IllegalAccessException e) {//####[260]####
            e.printStackTrace();//####[260]####
        } catch (IllegalArgumentException e) {//####[260]####
            e.printStackTrace();//####[260]####
        } catch (InvocationTargetException e) {//####[260]####
            e.printStackTrace();//####[260]####
        }//####[260]####
    }//####[260]####
//####[260]####
//####[261]####
    private static volatile Method __pt__runBM_95_Benchmark_method = null;//####[261]####
    private synchronized static void __pt__runBM_95_Benchmark_ensureMethodVarSet() {//####[261]####
        if (__pt__runBM_95_Benchmark_method == null) {//####[261]####
            try {//####[261]####
                __pt__runBM_95_Benchmark_method = ParaTaskHelper.getDeclaredMethod(new ParaTaskHelper.ClassGetter().getCurrentClass(), "__pt__runBM_95", new Class[] {//####[261]####
                    Benchmark.class//####[261]####
                });//####[261]####
            } catch (Exception e) {//####[261]####
                e.printStackTrace();//####[261]####
            }//####[261]####
        }//####[261]####
    }//####[261]####
    private static TaskID<Void> runBM_95(Benchmark benchmark) {//####[261]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[261]####
        return runBM_95(benchmark, new TaskInfo());//####[261]####
    }//####[261]####
    private static TaskID<Void> runBM_95(Benchmark benchmark, TaskInfo taskinfo) {//####[261]####
        // ensure Method variable is set//####[261]####
        if (__pt__runBM_95_Benchmark_method == null) {//####[261]####
            __pt__runBM_95_Benchmark_ensureMethodVarSet();//####[261]####
        }//####[261]####
        taskinfo.setParameters(benchmark);//####[261]####
        taskinfo.setMethod(__pt__runBM_95_Benchmark_method);//####[261]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[261]####
    }//####[261]####
    private static TaskID<Void> runBM_95(TaskID<Benchmark> benchmark) {//####[261]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[261]####
        return runBM_95(benchmark, new TaskInfo());//####[261]####
    }//####[261]####
    private static TaskID<Void> runBM_95(TaskID<Benchmark> benchmark, TaskInfo taskinfo) {//####[261]####
        // ensure Method variable is set//####[261]####
        if (__pt__runBM_95_Benchmark_method == null) {//####[261]####
            __pt__runBM_95_Benchmark_ensureMethodVarSet();//####[261]####
        }//####[261]####
        taskinfo.setTaskIdArgIndexes(0);//####[261]####
        taskinfo.addDependsOn(benchmark);//####[261]####
        taskinfo.setParameters(benchmark);//####[261]####
        taskinfo.setMethod(__pt__runBM_95_Benchmark_method);//####[261]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[261]####
    }//####[261]####
    private static TaskID<Void> runBM_95(BlockingQueue<Benchmark> benchmark) {//####[261]####
        //-- execute asynchronously by enqueuing onto the taskpool//####[261]####
        return runBM_95(benchmark, new TaskInfo());//####[261]####
    }//####[261]####
    private static TaskID<Void> runBM_95(BlockingQueue<Benchmark> benchmark, TaskInfo taskinfo) {//####[261]####
        // ensure Method variable is set//####[261]####
        if (__pt__runBM_95_Benchmark_method == null) {//####[261]####
            __pt__runBM_95_Benchmark_ensureMethodVarSet();//####[261]####
        }//####[261]####
        taskinfo.setQueueArgIndexes(0);//####[261]####
        taskinfo.setIsPipeline(true);//####[261]####
        taskinfo.setParameters(benchmark);//####[261]####
        taskinfo.setMethod(__pt__runBM_95_Benchmark_method);//####[261]####
        return TaskpoolFactory.getTaskpool().enqueue(taskinfo);//####[261]####
    }//####[261]####
    public static void __pt__runBM_95(Benchmark benchmark) {//####[261]####
        try {//####[261]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[261]####
        } catch (IllegalAccessException e) {//####[261]####
            e.printStackTrace();//####[261]####
        } catch (IllegalArgumentException e) {//####[261]####
            e.printStackTrace();//####[261]####
        } catch (InvocationTargetException e) {//####[261]####
            e.printStackTrace();//####[261]####
        }//####[261]####
    }//####[261]####
//####[261]####
//####[264]####
    private static void runBM(Benchmark benchmark) {//####[264]####
        System.out.println("thread : " + Thread.currentThread().getId() + " will execute this benchmark");//####[265]####
        try {//####[266]####
            benchmark.getMethod().invoke(benchmark.getInstance(), benchmark.getArguments());//####[267]####
        } catch (IllegalAccessException e) {//####[268]####
            e.printStackTrace();//####[269]####
        } catch (IllegalArgumentException e) {//####[270]####
            e.printStackTrace();//####[271]####
        } catch (InvocationTargetException e) {//####[272]####
            e.printStackTrace();//####[273]####
        }//####[274]####
    }//####[275]####
//####[277]####
    private static ConcurrentLinkedQueue<Benchmark> createBenchmarkSet(Class<?> bmClass, Integer setLen) {//####[277]####
        concurrentLinkedQueue = new ConcurrentLinkedQueue<Benchmark>();//####[278]####
        for (int i = 0; i < setLen; i++) //####[279]####
        {//####[279]####
            Object benchmark = null;//####[280]####
            Method method = null;//####[281]####
            try {//####[282]####
                benchmark = bmClass.newInstance();//####[283]####
                method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[284]####
            } catch (InstantiationException e) {//####[285]####
                e.printStackTrace();//####[286]####
            } catch (IllegalAccessException e) {//####[287]####
                e.printStackTrace();//####[288]####
            } catch (NoSuchMethodException e) {//####[289]####
                e.printStackTrace();//####[290]####
            } catch (SecurityException e) {//####[291]####
                e.printStackTrace();//####[292]####
            } catch (IllegalArgumentException e) {//####[293]####
                e.printStackTrace();//####[294]####
            }//####[295]####
            Object[] arguments = new Object[1];//####[296]####
            arguments[0] = N_DATASIZE;//####[297]####
            concurrentLinkedQueue.add(new Benchmark(benchmark, method, arguments));//####[299]####
        }//####[301]####
        return concurrentLinkedQueue;//####[302]####
    }//####[303]####
//####[305]####
    private static Benchmark createBenchmark(Class<?> bmClass) {//####[305]####
        Object benchmark = null;//####[306]####
        Method method = null;//####[307]####
        try {//####[308]####
            benchmark = bmClass.newInstance();//####[309]####
            method = bmClass.getMethod(BM_METHOD, BM_METHOD_ARGUEMENT_TYPE);//####[310]####
        } catch (InstantiationException e) {//####[311]####
            e.printStackTrace();//####[312]####
        } catch (IllegalAccessException e) {//####[313]####
            e.printStackTrace();//####[314]####
        } catch (NoSuchMethodException e) {//####[315]####
            e.printStackTrace();//####[316]####
        } catch (SecurityException e) {//####[317]####
            e.printStackTrace();//####[318]####
        } catch (IllegalArgumentException e) {//####[319]####
            e.printStackTrace();//####[320]####
        }//####[321]####
        Object[] arguments = new Object[1];//####[322]####
        arguments[0] = N_DATASIZE;//####[323]####
        return new Benchmark(benchmark, method, arguments);//####[325]####
    }//####[326]####
//####[328]####
    private static Class<?> getBenchmarkClass(String bmName) {//####[328]####
        Class<?> bmClass = null;//####[330]####
        try {//####[332]####
            if (bmName.equalsIgnoreCase(MOL)) //####[333]####
            {//####[333]####
                bmClass = Class.forName(MOL_CLASS);//####[334]####
            } else if (bmName.equalsIgnoreCase(MON)) //####[335]####
            {//####[335]####
                bmClass = Class.forName(MON_CLASS);//####[336]####
            } else if (bmName.equalsIgnoreCase(RAY)) //####[337]####
            {//####[337]####
                bmClass = Class.forName(RAY_CLASS);//####[338]####
            } else {//####[339]####
                throw new Exception("Can not find the Benchmark " + bmName);//####[340]####
            }//####[341]####
        } catch (ClassNotFoundException e) {//####[342]####
            e.printStackTrace();//####[343]####
        } catch (Exception e) {//####[344]####
            e.printStackTrace();//####[345]####
        }//####[346]####
        return bmClass;//####[348]####
    }//####[349]####
}//####[349]####
