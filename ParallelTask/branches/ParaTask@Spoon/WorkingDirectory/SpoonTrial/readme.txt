27 June 2014

This is an experimental implementation of ParaTask with annotations.
It uses Spoon (http://spoon.gforge.inria.fr/) as an annotation processing
tool, which is based on the Eclipse JDT core library to parse Java code.

See build.xml to compile and run the examples.

The jar file "libs/spoon-core-1.4-jar-with-dependencies.jar", which is
available at Spoon's website, is required to compile the code in "./src"
and process the code in "./input". But Spoon's source code in "./spoon14"
is not required. It is here just for reference and debugging purposes for
future development, e.g. printing out more information.

Besides, the generated code, which will be under "./dist/gen", depends on the
library of ParaTask with Lambda Expressions, currently available at
https://svn.ece.auckland.ac.nz/svn/taschto/ParallelTask/branches/ParaTaskWithLambda.

For more information, please check out the two reports at
https://svn.ece.auckland.ac.nz/svn/taschto/MEStd-projects/2014/Haoming_Ma
and the corresponding papers at https://svn.ece.auckland.ac.nz/svn/taschto/papers.

Hope this readme.txt helps.

Haoming Ma
mmin779@aucklanduni.ac.nz
