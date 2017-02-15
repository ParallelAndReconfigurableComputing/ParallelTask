

package smallProject;


public class Main {
    public static void main(java.lang.String[] args) {
        pt.runtime.ParaTask.init(pt.runtime.ParaTask.PTSchedulingPolicy.MixedSchedule, java.lang.Runtime.getRuntime().availableProcessors());
        smallProject.FutureArray f = new smallProject.FutureArray(15);
        f.processTasks();
    }
}

