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