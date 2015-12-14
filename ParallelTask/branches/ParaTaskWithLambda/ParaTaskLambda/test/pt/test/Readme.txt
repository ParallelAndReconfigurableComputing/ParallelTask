This package is only used to test PT runtime, it is not part of the PT runtime.

The package fully depends on JGFB4PT project since the JGF benchmarks are used here to support the test. 
Before running the test case, make sure JGFB4PT had been involved.

The test idea comes as the description follows.

1. Each JGF multi-thread benchmark is allowed to run with single thread here. 
2. Each JGF multi-thread benchmark is treated as a single task.
3. A set of JGF multi-thread benchmarks is treated as a entire test case. All the benchmarks in the set are added 
	into a CurrentLinkedQueue.
4. The queue is set as a parameter of a method which is accessed and executed by multi-thread.
	The pseudo code looks like : TASK(*) public void method(CurrentLinkedQueue<benchmark> queue){}
5. Each thread can execute one single independent JGF benchmark at one time, after it finishes, it can get another
	benchmark from the queue, and restart it again. This procedure goes over and over until the queue is empty.
6. The result could be measured as a average value of results came from each single benchmark in the queue. 