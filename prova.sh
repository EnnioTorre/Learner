#!/bin/sh

options=$1
dir=$2
fn=""
cp="combinedLearner-1.0-1.0-SNAPSHOT.jar:.:lib/*:dependencies/*"
javaOpts="-Djava.library.path=lib/"
rundir="runnable"

run_with_loop_1(){

if [ ! -z $dir ] ;then
  for i in ${dir}/*; do
  echo "run application"
  java ${javaOpts} -classpath ${cp} testsimulator.testsimulatorforecast 1
  done
  else echo "-cl need a dir "
 fi

}


run_with_loop_2(){

if [ ! -z $dir ] ;then
  for i in ${dir}/*; do
  echo "run application"
  java ${javaOpts} -classpath ${cp} testsimulator.testsimulatorforecast 2 $dir
  done
  else echo "-cl need a dir "
 fi

}

run(){
if [ ! -z $dir ] ;then
  
  echo "run application"
  java ${javaOpts} -classpath ${cp} testsimulator.testsimulatorforecast

  else echo "-c need a dir "
fi

}

packaging(){

 echo "Packaging starts"
 mvn package
 cp $rundir/target/*-1.0-SNAPSHOT.jar $rundir
 chmod +x $rundir/conf/simulator/DAGSwithCubist/Debug/DAGSwithCubist
 chmod +x $rundir/prova.sh 
 echo "Packaging terminated succesfully"

}




control_on_dir(){

 if [ ! -z $dir ] ;then
  run_with_loop
  else echo "-c need a dir $dir $2"
 fi
}




case $options in
	--help) echo "Usage prova [options] [dir] " 
           echo "used to run application"
	   echo "   -c --create datasets	creates dataset from file in dir"
           echo "   -cl --create datasets with loop	creates dataset from files in dir choosing one at time " 
           echo "   -p --packaging		maven packaging"
           echo "   -pt --test generation       runs prediction on files in dir "
           echo "   -ptl --create datasets	behaves as -cl";;
	-cl) run_with_loop_1 ;;
	-c)  run ;;
	-p)  packaging ;;
	-ctl) run_with_loop_2  ;;
	7) echo "seven" ;;
	8) echo "eight" ;;
	9) echo "nine" ;;
	10) echo "ten" ;;
	*) echo "prova miss options  use --help for more info" ;;
esac




