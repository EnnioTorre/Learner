/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Learners;

import Utilities.DataSets;

import eu.cloudtm.autonomicManager.oracles.Oracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.LinearNNSearch;

/**
 *
 * @author Ennio email:ennio_torre@hotmail.it
 */
public class Knearestneighbourg {


    
    protected Instances m_Training = null;
    protected String m_TestSetFile = null;
    protected Instance m_TestSet = null;
    protected String m_TrainingFile = null;
    private Instances Neighboughood;

   
   
    public Knearestneighbourg (Instances trainingset,Instance test,String distance,String option) throws Exception{
      
       //this.m_TrainingFile=trainingset;
       //this.m_TestSetFile=testset;
        this.m_TestSet=test;
        this.m_Training=trainingset;
       String[] options=new String[1];
       options[0] =option;
       java.lang.reflect.Method method;
       
       //setTraining(this.m_TrainingFile);
       //setTestSet(this.m_TestSetFile);
       LinearNNSearch KNN=new LinearNNSearch();
       
       
       Class c = Class.forName("weka.core."+distance);
	    Object distanceFunc = c.newInstance();
             method=distanceFunc.getClass().getMethod("setOptions", String[].class);
        method.invoke(distanceFunc, (Object) options);
        
        KNN.setDistanceFunction((DistanceFunction)distanceFunc);
       
        
        
       
        
        KNN.setInstances(m_Training);
        
        //Instance I =getInstanceToTest(0);
        System.out.println(test);
        //KNN.kNearestNeighbours(I,1);
        Neighboughood=KNN.kNearestNeighbours(test,10);
        
       
        
        
       
    }
    
    public void setTraining(String name) throws Exception {
    m_Training     = new Instances(
                        new BufferedReader(new FileReader(name)));
    m_Training.setClassIndex(m_Training.numAttributes() - 1);
  }
  
  /*public void setTestSet(String name) throws Exception {
    m_TestSet = new Instances(
                        new BufferedReader(new FileReader(name)));
   
  }
  
  public Instance getInstanceToTest(int index){
     return m_TestSet.instance(index);
  }*/
   public Instances getNeighboughood() {
        return Neighboughood;
    }
   
   public HashMap<Oracle,Double[]> RMSE(String Parameter) throws Exception{
     HashMap<Oracle,Double[]> rmse=new  HashMap<Oracle,Double[]>();
     Double [] RMSe=new Double[2];
     double errorOutputRO=0D;
     double errorOutputWO=0D;
     double SEOutputRO=0D;
     double SEOutputWO=0D;
     Method method=OutputOracle.class.getMethod(Parameter, int.class);
     OutputOracle outputValidationSet;
     OutputOracle outputOracle;
     
        for(Map.Entry<Oracle,HashMap<Instance,OutputOracle>> entry:DataSets.predictionResults.entrySet()){
            for (int i=0;i<Neighboughood.numInstances();i++){
     
                Instance inst= DataSets.InstancesMap.get(Neighboughood.instance(i).toStringNoWeight());
                outputValidationSet=DataSets.ValidationSet.get(inst);
                outputOracle=entry.getValue().get(inst);
                
                errorOutputRO=(Double)method.invoke(outputValidationSet,0)-(Double)method.invoke(outputOracle,0);
                SEOutputRO=SEOutputRO+Math.pow(errorOutputRO,2);
                
                errorOutputWO=(Double)method.invoke(outputValidationSet,1)-(Double)method.invoke(outputOracle,1);
                SEOutputWO=SEOutputWO+Math.pow(errorOutputWO,2);
            
            
        }
        RMSe[0]=Math.sqrt(SEOutputRO);
        RMSe[1]=Math.sqrt(SEOutputWO);
        System.out.println(entry.getKey().toString()+":"+RMSe[0]+"  "+RMSe[1]);
        rmse.put(entry.getKey(),RMSe);
     }
       return rmse;
   }


}
