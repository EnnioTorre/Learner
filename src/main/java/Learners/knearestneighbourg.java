/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Learners;

import java.io.BufferedReader;
import java.io.FileReader;
import weka.core.neighboursearch.LinearNNSearch;
import weka.classifiers.lazy.IBk;
import weka.classifiers.Evaluation;
import weka.core.ManhattanDistance;
import weka.core.EuclideanDistance;
import weka.core.DistanceFunction;
import weka.core.Instances;
import weka.core.Instance;


/**
 *
 * @author ENNIO
 */
public class knearestneighbourg  {
    
    protected Instances m_Training = null;
    protected String m_TestSetFile = null;
    protected Instances m_TestSet = null;
    protected String m_TrainingFile = null;
   
    public knearestneighbourg (String trainingset,String testset,String distance,String option) throws Exception{
      
       this.m_TrainingFile=trainingset;
       this.m_TestSetFile=testset;
       String[] options=new String[1];
       options[0] =option;
       java.lang.reflect.Method method;
       
       setTraining(this.m_TrainingFile);
       setTestSet(this.m_TestSetFile);
       LinearNNSearch KNN=new LinearNNSearch();
       
       
       Class c = Class.forName("weka.core."+distance);
	    Object distanceFunc = c.newInstance();
             method=distanceFunc.getClass().getMethod("setOptions", String[].class);
        method.invoke(distanceFunc, (Object) options);
        
        KNN.setDistanceFunction((DistanceFunction)distanceFunc);
       
        
        
       
        
        KNN.setInstances(m_Training);
        
        Instance I =getInstanceToTest(0);
        System.out.println(I);
        //KNN.kNearestNeighbours(I,1);
        System.out.println(KNN.kNearestNeighbours(I,10));
        
       
        
        
       
    }
    
    public void setTraining(String name) throws Exception {
    m_Training     = new Instances(
                        new BufferedReader(new FileReader(name)));
    m_Training.setClassIndex(m_Training.numAttributes() - 1);
  }
  
  public void setTestSet(String name) throws Exception {
    m_TestSet = new Instances(
                        new BufferedReader(new FileReader(name)));
   
  }
  
  public Instance getInstanceToTest(int index){
     return m_TestSet.instance(index);
  }
  
}

