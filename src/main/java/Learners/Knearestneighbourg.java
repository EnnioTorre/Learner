
package Learners;

import Utilities.DataConverter;
import Utilities.DataSets;

import eu.cloudtm.autonomicManager.oracles.InputOracle;
import eu.cloudtm.autonomicManager.oracles.Oracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;
import org.apache.log4j.Logger;
import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.LinearNNSearch;

/**
 *
 * @author Ennio email:ennio_torre@hotmail.it
 */



public class Knearestneighbourg extends Learner implements Oracle {
    static Logger logger = Logger.getLogger(Knearestneighbourg.class.getName()); 
    protected LinearNNSearch KNN;
    protected int NumNeighbours;
    protected String ConsideredOutOracle;

   
   
    public Knearestneighbourg (String distance,String option,int Numnearest,String outputs) throws Exception{
      
        
       //this.m_TrainingFile=trainingset;
       //this.m_TestSetFile=testset;
       String[] options=new String[1];
       options[0] =option;
       Method method;
       this.NumNeighbours=Numnearest;
       this.ConsideredOutOracle=outputs;
       //setTraining(this.m_TrainingFile);
       //setTestSet(this.m_TestSetFile);
       KNN=new LinearNNSearch();
       
       
       Class c = Class.forName("weka.core."+distance);
	    Object distanceFunc = c.newInstance();
             method=distanceFunc.getClass().getMethod("setOptions", String[].class);
        method.invoke(distanceFunc, (Object) options);
        
        KNN.setDistanceFunction((DistanceFunction)distanceFunc);
       
        
        
       
        if(DataSets.ARFFDataSet!=null){
        
            this.m_Training=DataSets.ARFFDataSet;
        
        
        }
        else{
            logger.warn("--"+"Datasets Not instanziated");
            throw new InstantiationException("Datasets Not instanziated");
        }
        
        
        //Instance I =getInstanceToTest(0);
        //KNN.kNearestNeighbours(I,1);
        
        
       
        
        
       
    }
    
  
  /*public void setTestSet(String name) throws Exception {
    m_TestSet = new Instances(
                        new BufferedReader(new FileReader(name)));
   
  }
  
  public Instance getInstanceToTest(int index){
     return m_TestSet.instance(index);
  }*/
   public Instances getNeighboughood() {
        return Neighbourshood;
    }
   

   

    @Override
    public OutputOracle forecast(InputOracle io) throws OracleException {
        
        double AVGrmse=Double.MAX_VALUE;
        Oracle best=null;
        
        try{
           
           
           SelectInstancesRP(io);
           KNN.setInstances(m_Training);
           m_TestSet=DataConverter.FromInputOracleToInstance(io);
           System.out.println(m_TestSet);
           Neighbourshood=KNN.kNearestNeighbours(m_TestSet,NumNeighbours);
           
           RMSE=RMSE(this.ConsideredOutOracle);
           
        }
        catch( Exception ex){
            logger.warn("--error in class "+ex.getClass()+" "+" caused by "+ex.getCause()+" at "+ex.getStackTrace()[0]+" --");
            throw new OracleException(ex);
        }
 
        double actual;
        for(Map.Entry<Oracle,Double[]>entry:RMSE.entrySet()){
            
          
            actual=(entry.getValue()[0]+entry.getValue()[1])/2;
            
            if(actual<=AVGrmse){
                 AVGrmse=actual;
                 best=entry.getKey();
                 
               }
        }
        
       logger.info("ORACLE SELECTED FOR PREDICTION : "+best.toString().split("@")[0]);
        
        return best.forecast(io);
    }
    
   
    
    

 
}
