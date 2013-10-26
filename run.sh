#/bin/bash

cp="combinedLearner-1.0-1.0-SNAPSHOT.jar:.:lib/*:dependencies/*"
javaOpts="-Djava.library.path=lib/"
srcdir=csvfilee
dstdir=csvfile

srcdirtest=csvfilee
dstdirtest=csvtest

export LD_LIBRARY_PATH=`pwd`


for i in ${srcdir}/*; do
N=5;


   for i in ${srcdir}/*; do
   if [ "$(ls -A ${srcdir})" ] ;then
      [ $((N--)) = 0 ] && break
      mv -t "${dstdir}" -- "$i"
      mv -t "${dstdirtest}" -- "$i" 
   fi
   done
   java ${javaOpts} -classpath ${cp} testsimulator.testsimulatorforecast
   if [ "$(ls -A ${dstdir})" ] ;then
   rm -r ${dstdir}/*
   rm -r ${dstdirtest}/*
   fi
done
  

