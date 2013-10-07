/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Learners;

import Utilities.DataSets;
import Utilities.DatasetOutputOracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import java.io.BufferedReader;
import java.io.FileReader;
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
   
   private double RMSE(String Parameter){
     double [] rmse;
     for (int i=0;i<Neighboughood.numInstances();i++){
     
        Instance inst= DataSets.InstancesMap.get(Neighboughood.instance(i));
        OutputOracle outputValidationSet=DataSets.ValidationSet.get(inst);
        OutputOracle [] output;
     }
       return 1;
   }


}
