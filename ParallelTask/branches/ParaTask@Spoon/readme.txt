This project uses Spoon (http://spoon.gforge.inria.fr/) as an annotation processing

tool, which is based on the Eclipse JDT core library to parse Java code.



See build.xml as an example for compiling and running projects.



Required "jar" files can be found in the "libs" folder. The latest spoon versions
must be updated in this folder ("spoon-core-5.0.2" as of the time of creating this
document)


The command line for running a project under spoon is as follows:


$ java -classpath /path/to/binary/of/your/processor.jar:spoon-core-5.0.2-jar-with-dependencies.jar -jar spoon-core-5.0.2-jar-with-dependencies.jar -i /path/to/src/of/your/project -p fr.inria.gforge.spoon.processors.CatchProcessor

If this command doesn't work for the ant builder, then use the following:

$ java -cp /path/to/binary/of/your/processor.jar:/path/to/built/classes/of/source:spoon-core-5.0.2-jar-with-dependencies.jar spoon.Launcher -o distination/dir -i path/to/input/directory(or file) -p /path/to/annotation/processor (e.g., sp.processors.PtFutureProcesor)

For implementing java command lines in ant, use a target like the following:

<target name="process" depends="compile, compressJar" description="Process the annotated code">
		<exec executable="java">
			<arg line="-cp ${spoon.jar}${Delim}dist/classes spoon.Launcher -o ${gen.dir} -i input/test/FutureArrayTest.java -p sp.processors.ArrayProcessor" />
		</exec> 
		<exec executable="java">
			<arg line="-cp ${PT.jar}${Delim}${spoon.jar}${Delim}dist/classes spoon.Launcher -o ${gen.dir} -i input/test/CollectionWrapperTest.java -p sp.processors.PtFutureProcessor" />
		</exec> 
	</target>

Defines a target that runs "ArrayProcessor" first, and then runs "PtFutureProcessor". Both processors are located under 'sp.processors.PtFutureProcessor' in the 'src' folder, so the same structure would be searched for the built
files. 


Note: '-i' and '-p' are command line options for the spoon executable jar file.
These options can be listed as follows.


$ java -jar spoon-core-5.0.2-jar-with-dependencies.jar --help
Spoon version 5.0.2 
Usage: java <launcher name> [option(s)]

Options : 

  [-h|--help]

  [-v|--verbose]
        Argument deprecated, see the argument level. Output messages about what
        the compiler is doing.

  [--tabs]
        Use tabulations instead of spaces in the generated code (use spaces by
        default).

  [--tabsize <tabsize>]
        Define tabulation size. (default: 4)

  [--level <level>]
        Level of the ouput messages about what spoon is doing. Default value is
        ALL level. (default: OFF)

  [--vvv]
        Argument deprecated, see the argument level. Generate all debugging
        info.

  [--with-imports]
        Enable imports in generated files.

  [--compliance <compliance>]
        Java source code compliance level (1,2,3,4,5, 6, 7 or 8). (default: 8)

  [--encoding <encoding>]
        Forces the compiler to use a specific encoding (UTF-8, UTF-16, ...).
        (default: UTF-8)

  [(-s|--spoonlet) <spoonlet>]
        List of spoonlet files to load.

  [(-i|--input) <input>]
        List of path to sources files.

  [(-p|--processors) <processors>]
        List of processor's qualified name to be used.

  [(-t|--template) <template>]
        List of path to templates java files.

  [(-o|--output) <output>]
        Specify where to place generated java files. (default: spooned)

  [--properties <properties>]
        Directory to search for spoon properties files.

  [--source-classpath <source-classpath>]
        An optional classpath to be passed to the internal Java compiler when
        building or compiling the input sources.

  [--template-classpath <template-classpath>]
        An optional classpath to be passed to the internal Java compiler when
        building the template sources.

  [(-d|--destination) <destination>]
        An optional destination directory for the generated class files.
        (default: spooned-classes)

  [--output-type <output-type>]
        States how to print the processed source code:
        nooutput|classes|compilationunits (default: classes)

  [--compile]
        Enable compilation and output class files.

  [--precompile]
        Enable pre-compilation of input source files before processing. Compiled
        classes will be added to the classpath so that they are accessible to
        the processing manager (typically, processors, annotations, and
        templates should be pre-compiled most of the time).

  [--buildOnlyOutdatedFiles]
        Set Spoon to build only the source files that have been modified since
        the latest source code generation, for performance purpose. Note that
        this option requires to have the --ouput-type option not set to none.
        This option is not appropriate to all kinds of processing. In particular
        processings that implement or rely on a global analysis should avoid
        this option because the processor will only have access to the outdated
        source code (the files modified since the latest processing).

  [--lines]
        Set Spoon to try to preserve the original line numbers when generating
        the source code (may lead to human-unfriendly formatting).

  [-x|--noclasspath]
        Does not assume a full classpath

  [-g|--gui]
        Show spoon model after processing

  [-r|--no-copy-resources]
        Disable the copy of resources from source to destination folder.

  [-j|--generate-javadoc]
        Enable the generation of the javadoc.