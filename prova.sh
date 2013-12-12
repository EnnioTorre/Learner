#!/bin/sh

options=$1
dir=$2
dir2=$3
fn=""
tmpdir=".$dir.tmp"
cp="combinedLearner-1.0-SNAPSHOT.jar:.:lib/*:dependencies/*"
javaOpts="-Djava.library.path=lib/"
rundir="runnable"
N=0

run_with_loop(){
N=$(ls $dir | wc -w)
if [ ! -d $tmpdir ];then
rm -r $tmpdir
fi
mkdir $tmpdir
echo "$N folders in $dir"
  
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

}


run(){
 
  echo "run application"
  java ${javaOpts} -classpath ${cp} testsimulator.testsimulatorforecast $fn $dir $dir2

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

 if [ -z $dir ] ;then
  echo "$options need a dir $dir $2"
  exit
 fi
}


control_on_2dir(){
 if [ -z $dir -o -z $dir2] ;then
   echo "$options need a trainingset dir and test set dir"
   exit
 fi
}
 



case $options in
	--help) echo "Usage prova [options] [dir] [dir2]" 
           echo "used to run application
             
	     -c   [dir]					--create datasets
							    creates dataset 								    from folders in  								    dir if dataset 								    does not already 								    present 	        
             -cl  [dir]					--create datasets with loop	
							    creates dataset 							  	    from folders in 								    dir choosing one 								    at time if 	                                                           dataset does 								    not already 							    present  
             -p						--packaging		        maven packaging
             -pt  [dir]=testdir				--prediction on testset         
							    runs prediction 								    on files in dir 
             -ptl [dir]=testdir				--prediction on testset with loop 							  	    behaves as -cl
             -cpt [dir]=trainingdir [dir2]=test		--datast creation and  test prediction  						            creates datasets 								    form dir and runs        								    prediction on 							            files in dir2 
             ";;

	-cl) if [ ! -d dataset ];then
             control_on_dir
             fn=1 
             run_with_loop 
             else
             echo "Wrong Choise , Training Set Already Exist !!!"
             fi  ;;
	-c)  if [ ! -d dataset ];then
             control_on_dir
             fn=1
             run 
             else
             echo "Wrong Choise , Training Set Already Exist !!!"
             fi ;;
           
	-p)  packaging ;;
        -pt) if [ -d dataset ];then
             control_on_dir
             fn=3
             run 
             else
             echo "Wrong Choise , Training Set Does Not Exist !!!"
             fi ;;
	-ptl)if [ -d dataset ];then
             control_on_dir
             fn=3 
             run_with_loop  
             else
             echo "Wrong Choise , Training Set Does Not Exist !!!"
             fi ;;
	-cpt) echo "Dataset creation and prediction "
             control_on_2dir
	     if [ -d dataset ];then
		echo "training set dir already exist,it will be updated (remove dataset dir or chamge learner configuration to avoid this) !!!"
	     fi
             fn=2
             run ;; 
	-r) echo "Dataset reading"
           if [ -d dataset ];then
             fn=4
             run 
             else
             echo "Wrong Choise , Training Set Does Not Exist !!!"
             fi ;;
	9) echo "nine" ;;
	10) echo "ten" ;;
	*) echo "prova miss options  use --help for more info" ;;
esac




