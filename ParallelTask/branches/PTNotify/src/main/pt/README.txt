When you download the source file of Paratask Version2 and try to compile it, you will find some parser file missing.

Follow the instructions below to get it work:

1. Prepare the JavaCC. You could either use the Eclipse JavaCC Plugin or JavaCC Package. The Recommanded JavaCC version MUST BE 5.0

2. Go the the package of paratask.compier.parser, find the grammar file Java_1_5.jj

3. Compile the grammar file directly. DO NOT Modify ANYTHING or DELETE ANY OTHER JAVA files under that package.

4. After the compiling procedure, three new JAVA files will be generated. They are:
	JavaParser.java
	JavaParserConstants.java
	JavaParserTokenManager.java

5. Refresh the whole project to make the whole project rebuilt.

6. It should works, GOOD LUCK~~!

NOTICE:

The both of compiler and runtime are dependent on the package of android.os

if something goes wrong, check if it exists first.