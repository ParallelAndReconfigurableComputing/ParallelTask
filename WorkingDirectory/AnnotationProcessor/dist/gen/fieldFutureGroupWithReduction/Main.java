

package fieldFutureGroupWithReduction;


public class Main {
    public static void main(java.lang.String[] args) {
        pt.runtime.ParaTask.init(pt.runtime.ParaTask.PTSchedulingPolicy.MixedSchedule, java.lang.Runtime.getRuntime().availableProcessors());
        fieldFutureGroupWithReduction.FieldFutureGroupClass object = new fieldFutureGroupWithReduction.FieldFutureGroupClass(5);
        object.startTasks();
    }
}

