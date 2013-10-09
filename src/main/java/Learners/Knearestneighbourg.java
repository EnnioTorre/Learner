/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.StringTokenizer;
import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.LinearNNSearch;

/**
 *
 * @author Ennio email:ennio_torre@hotmail.it
 */
public class Knearestneighbourg implements Oracle {


    
    protected Instances m_Training = null;
    protected String m_TestSetFile = null;
    protected Instance m_TestSet = null;
    protected String m_TrainingFile = null;
    private Instances Neighbourshood;
    private HashMap<Oracle,Double[]> RMSE;
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
       
        
        
       
        try{
            
        KNN.setInstances(DataSets.ARFFDataSet);
        
        }
        catch(NullPointerException e){
        
            throw new InstantiationException("Datasets Not instanziated");
        }
        
        
        //Instance I =getInstanceToTest(0);
        //KNN.kNearestNeighbours(I,1);
        
        
       
        
        
       
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
        return Neighbourshood;
    }
   
   /**
 *
 * Valid value for Parameter are:
 * throughput
 * abortRate
 * responseTime
 * everyone separated by a space
 */
   
   private HashMap<Oracle,Double[]> RMSE(String Parameter) throws Exception{
     HashMap<Oracle,Double[]> rmse=new  HashMap<Oracle,Double[]>();
     Double [] RMSe;
     double errorOutputRO;
     double errorOutputWO;
     double SEOutputRO=0D;
     double SEOutputWO=0D;
     Method method;
     OutputOracle outputValidationSet;
     OutputOracle outputOracle;
     StringTokenizer token;
     String outputname;
     
        for(Map.Entry<Oracle,HashMap<Instance,OutputOracle>> entry:DataSets.predictionResults.entrySet()){
            for (int i=0;i<Neighbourshood.numInstances();i++){
     
                Instance inst= DataSets.InstancesMap.get(Neighbourshood.instance(i).toStringNoWeight());
                
                outputValidationSet=DataSets.ValidationSet.get(inst);
                outputOracle=entry.getValue().get(inst);
                
                DataSets.logger.info("Instance :"+inst +"\n"+"validationOutput= "+outputValidationSet+"\n"+"Oracle Output= "+outputOracle);
                token=new StringTokenizer(Parameter);
                while(token.hasMoreTokens()){
                    
                    outputname=token.nextToken();
                    
                    method=OutputOracle.class.getMethod(outputname, int.class);
                    errorOutputRO=(Double)method.invoke(outputValidationSet,0)-(Double)method.invoke(outputOracle,0);
                    SEOutputRO=SEOutputRO+Math.pow(errorOutputRO,2);
                    DataSets.logger.info( outputname+"RMSERO"+entry.getKey().toString()+" = " +errorOutputRO );
                
                    errorOutputWO=(Double)method.invoke(outputValidationSet,1)-(Double)method.invoke(outputOracle,1);
                    SEOutputWO=SEOutputWO+Math.pow(errorOutputWO,2);
                    DataSets.logger.info( outputname+"RMSEWO"+entry.getKey().toString()+" = " +errorOutputWO );
                    }
            
             }
            RMSe=new Double[2];
            RMSe[0]=Math.sqrt(SEOutputRO)/Neighbourshood.numInstances();
            RMSe[1]=Math.sqrt(SEOutputWO)/Neighbourshood.numInstances();
            SEOutputRO=0D;
            SEOutputWO=0D;
            System.out.println(entry.getKey().toString()+":"+RMSe[0]+"  "+RMSe[1]);
            rmse.put(entry.getKey(),RMSe);
     }
       return rmse;
   }

    @Override
    public OutputOracle forecast(InputOracle io) throws OracleException {
        
        double AVGrmse=Double.MAX_VALUE;
        Oracle best=null;
        
        try{
           m_TestSet=DataConverter.FromInputOracleToInstance(io);
           System.out.println(m_TestSet);
           Neighbourshood=KNN.kNearestNeighbours(m_TestSet,NumNeighbours);
           
           RMSE=RMSE(this.ConsideredOutOracle);
           
        }
        catch(Exception ex){
            System.out.println(ex.getMessage());
            System.out.println(ex.getCause());
            System.out.println(ex.getStackTrace());
            throw new OracleException(ex.getCause());
        }
 
        double actual;
        for(Map.Entry<Oracle,Double[]>entry:RMSE.entrySet()){
            
          
            actual=(entry.getValue()[0]+entry.getValue()[1])/2;
            
            if(actual<=AVGrmse){
                 AVGrmse=actual;
                 best=entry.getKey();
                 
               }
        }
        
        System.out.println(best.toString());
        
        return best.forecast(io);
    }

 


}
