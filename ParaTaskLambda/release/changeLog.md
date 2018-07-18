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

- Basic big fixes and improvements with retrieving results, specifying reduction operations
  and performing customized one-off reduction operations on TaskIDGroups.

Version 1.5.0 - 30/03/2017
==========================
- TaskIDGroup has changed to notify handlers for future groups as well

- Asynchronous error handling mechansims for TaskIDGroup has been enhanced

- TaskIDGroup is now able to return an array of the results of its sub-tasks

The main purpose for these enhancements is to provide runtime support for advanced features
that are offered in the API level of @PT. 

Version 1.5.1 - 06/04/2017
========================== 
- Exception handling enhanced such that, exception class E receives a handler if there is
  a handler associated to itself or one of its parents. Previously, @PT was able to handle
  exceptions of type E if and only if there was a handler associated to exception class E,
  and parent types were not considered

Version 1.5.2 - 29/08/2017
==========================
- Minor bug fixes with the "getReturnResult" of taskIdGroups, so that tasks with "Void" 
  return type do not expect reductions to be specified. 

Versuib 2.0.0 - 18/07/2018
=========================
- ParaTask runtime extended with the new cloud-computing extension.

- Other components are modified, or bug fixed, in order to accommodate the new developments
  to the runtime. 