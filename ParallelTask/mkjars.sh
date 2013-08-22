#!/bin/bash

# Creates PTCompiler.jar and PTRuntime.jar.
# Usage:
#     ./mkjars.sh

echo Creating PTCompiler.jar
jar -cf PTCompiler.jar -C bin paratask/compiler
echo Creating PTRuntime.jar
jar -cf PTRuntime.jar -C bin paratask/queues -C bin paratask/runtime
echo Done!

