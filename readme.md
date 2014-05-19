JMaxAlign
=========
This is the sourcecode for JMaxAlign: An open-source maximum entropy parallel sentence alignment tool written in Java.
You can read the original research paper here(http://aclweb.org/anthology//C/C12/C12-3035.pdf)


Developers
==========
JMaxAlign was originally developed by Joseph Kaufmann at Decisive Analytics (http://www.dac.us/)


Dependencies
=============
JMaxAlign depends on:
- Berkeley Aligner (https://code.google.com/p/berkeleyaligner/)
- Stanford CoreNLP (http://nlp.stanford.edu/software/corenlp.shtml)
- Stanford Classifier (http://nlp.stanford.edu/software/classifier.shtml)


Build Instructions
==================
1. Out of the 3 dependencies, Stanford CoreNLP is the only one that is part of the Maven central repository. Add the
Maven dependency for CoreNLP to your pom file. The crucial thing to know is that CoreNLP needs its models to run
(most parts beyond the tokenizer) and so you need to specify both the code jar and the models jar in your pom.xml, as follows:

    <dependencies>
    <dependency>
        <groupId>edu.stanford.nlp</groupId>
        <artifactId>stanford-corenlp</artifactId>
        <version>3.3.1</version>
    </dependency>
    <dependency>
        <groupId>edu.stanford.nlp</groupId>
        <artifactId>stanford-corenlp</artifactId>
        <version>3.3.1</version>
        <classifier>models</classifier>
    </dependency>
    </dependencies>

2. Download the Stanford Classifier and Berkeley Aligner jar files to your local machine.
3. Install the local jar files to your local Maven repository as follows:

    mvn install:install-file -Dfile={path/to/file}.jar -DgroupId={put.groupid.here}
    -DartifactId={artifactname} -Dversion={version} -Dpackaging=jar

4. Add the new dependencies to your pom file as follows:

    <dependency>
        <groupId>{put.groupid.here}</groupId>
        <artifactId>{artifactname}</artifactId>
        <version>{version}</version>
        <classifier>models</classifier>
    </dependency>

You are now done.



