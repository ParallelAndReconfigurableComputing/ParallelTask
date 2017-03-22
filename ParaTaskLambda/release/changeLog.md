change log has been added for ParaTaskLambda from version 1.2.0

Template:
Version X.Y.Z - DD/MM/YYYY
==========================
- added feature 1
- added feature 2


Version 1.2.0 - 15/03/2017
==========================
- Dependencies are now registered via the static method 
  ParaTask.registerDependencies(TaskInfo<R> taskInfo, TaskID<?>... taskIDs), 
  rather than direct calls to a taskInfo

- Changed the name of wrapper classes to Hybrid class
  (e.g., ListHybrid, SetHybrid, MapHybrid, CollectionHybrid)

- Static method "ParaTask.getPtWrapper" changed to "ParaTask.getPtHybridWrapper"

- Static method "ParaTask.setReductionOperationForTaskIDGroup" changed to
  "ParaTask.registerReduction(TaskIDGroup<?> id, Reduction<T> reduction)"


Version 1.2.1 - 17/03/2017
==========================
- ParaTask.executeInterimHandler bug fixed, to receive Functors with no arg
  and with one arg as an input, and create the corresponding slot object internally
  by runtime. 


Version 1.3.0 - 22/03/2017
==========================
- ParaTask functors originally were not able to throw Throwable objects, effectively
  making the asynchronous exception handling uneffective. Version 1.3.0 enables throwing
  Throwable objects from functors and their corresponding tasks to TaskThreads, which 
  in return will store the Throwable objects.

Version 1.4.0 - 22/03/2017
==========================
- TaskIDGroups that represent future groups can now receive handler methods to notify
  on their synchronization. 