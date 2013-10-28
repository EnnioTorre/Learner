#/bin/bash

cp="combinedLearner-1.0-1.0-SNAPSHOT.jar:.:lib/*:dependencies/*"
javaOpts="-Djava.library.path=lib/"
srcdir=csvfilee
dstdir=csvfile

srcdirtest=csvfilee
dstdirtest=csvtest

export LD_LIBRARY_PATH=`pwd`
java ${javaOpts} -classpath ${cp} testsimulator.testsimulatorforecast
