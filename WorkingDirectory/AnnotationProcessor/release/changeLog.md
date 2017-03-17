change log has been added for ParaTaskLambda from version 1.2.0

Template:
Version X.Y.Z - DD/MM/YYYY
==========================
- added feature 1
- added feature 2


Version 1.2.0 - 16/03/2017
==========================
- Updated with latest update of ParaTaskLambda-1.2.0
  
  * call to "getPtWrapper" changed to "getPtHybridWrapper"
 
  * Registering dependences is now done via calling "registerDependence"
    on "ParaTask", instead of direct calls on "TaskInfo"

  * Wrapper names are changed accordingly with the latest updates of 
    ParaTaskLambda-1.2.0
    
    - PtHybridList, PtHybridSet, PtHybridMap and ptHybridCollection

- Lengthy name for method "ParaTask.registerReductionOperationForTaskIDGroup"
  has changed to "ParaTask.registerReduction"

- Generic arguments of a user-declared class are now inferred fromt he user
  specified string

- Prior to version 1.2.0, the framework would declare a new variable for 
  recording the size of the array corresponding to a future group, in the
  case of lazy initializations. The variable would be used in for loops 
  for retrieving the results of sub-futures into the elements of the 
  corresponding array. This approach was unnecessary and error-porne. In 
  the current version this variable is not defined anymore, rather the 
  loop condition is changed to "<arrayName>.length" 

Version 1.3.0 - 16/03/2017
==========================
- New annotation @Gui added to @PT
  * Corresponding annotation process in progress

- Sequential style for asynchronous execution handling in progress