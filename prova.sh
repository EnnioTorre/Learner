#!/bin/sh

options=$1
dir=$2
fn=""
tmpdir=".$dir.tmp"
cp="combinedLearner-1.0-SNAPSHOT.jar:.:lib/*:dependencies/*"
javaOpts="-Djava.library.path=lib/"
rundir="runnable"
N=0

run_with_loop(){
N=$(ls $dir | wc -w)
mkdir $tmpdir
echo "$N folders in $dir"
if [ ! -z $dir ] ;then
   
   
   for i in ${dir}/*; do

   if [ "$(ls -A ${dir})" ]; then
   cp -rt "${tmpdir}" -- "$i"
   else break
   fi

  echo "run application"
  java ${javaOpts} -classpath ${cp} testsimulator.testsimulatorforecast $fn $tmpdir
  rm -r ${tmpdir}/*
  N=$(( N-1 ))
  echo "$N folders to read left"
  done
  rm -r $tmpdir
  else echo "-cl need a dir "
 fi

}


run_with_loop_2(){

if [ ! -z $dir ] ;then
  for i in ${dir}/*; do
  echo "run application"
  java ${javaOpts} -classpath ${cp} testsimulator.testsimulatorforecast 2 $tmpdir
  done
  else echo "-cl need a dir "
 fi

}

run(){
if [ ! -z $dir ] ;then
  
  echo "run application"
  java ${javaOpts} -classpath ${cp} testsimulator.testsimulatorforecast $fn $dir

  else echo "-c need a dir "
fi

}

packaging(){

 echo "Packaging starts"
 mvn package
 cp $rundir/target/*-1.0-SNAPSHOT.jar $rundir
 chmod +x $rundir/conf/simulator/DAGSwithCubistRR/Debug/DAGSwithCubistRR
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
	   echo "   -c --create datasets	creates dataset from folders in dir if dataset not already present"
           echo "   -cl --create datasets with loop	creates dataset from folders in dir choosing one at time if dataset not already present " 
           echo "   -p --packaging		maven packaging"
           echo "   -pt --prediction on testset        runs prediction on files in dir "
           echo "   -ptl --prediction on testset with loop 	behaves as -cl";;

	-cl) if [ ! -d dataset ];then
             fn=1 
             run_with_loop 
             else
             echo "Wrong Choise , Training Set Already Exist !!!"
             fi  ;;
	-c)  if [ ! -d dataset ];then
             fn=1
             run 
             else
             echo "Wrong Choise , Training Set Already Exist !!!"
             fi ;;
           
	-p)  packaging ;;
        -pt) if [ -d dataset ];then
             fn=2
             run 
             else
             echo "Wrong Choise , Training Set Does Not Exist !!!"
             fi ;;
	-ptl)if [ -d dataset ];then
             fn=2 
             run_with_loop  
             else
             echo "Wrong Choise , Training Set Does Not Exist !!!"
             fi ;;
	-cpt) echo "Dataset creation and prediction "
	     if [ !-d dataset ];then
             fn=2
             run 
             else
             echo "Wrong Choise , Training Set Already Exist run with option -pt or -ptl !!!"
             fi ;; 
	-r) echo "Dataset reading"
           if [ -d dataset ];then
             fn=1
             run 
             else
             echo "Wrong Choise , Training Set Does Not Exist !!!"
             fi ;;
	9) echo "nine" ;;
	10) echo "ten" ;;
	*) echo "prova miss options  use --help for more info" ;;
esac




