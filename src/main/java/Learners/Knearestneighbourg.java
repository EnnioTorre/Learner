
package Learners;

import Utilities.DataConverter;
import Utilities.DataSets;
import eu.cloudtm.autonomicManager.commons.ForecastParam;
import eu.cloudtm.autonomicManager.commons.ReplicationProtocol;
import eu.cloudtm.autonomicManager.oracles.InputOracle;

import eu.cloudtm.autonomicManager.oracles.Oracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.HashMap;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;
import weka.core.DistanceFunction;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.neighboursearch.LinearNNSearch;
import weka.filters.unsupervised.instance.RemoveWithValues;
import weka.core.Utils;
import weka.filters.Filter;

/**
 *
 * @author Ennio email:ennio_torre@hotmail.it
 */



public class Knearestneighbourg implements Oracle {
    static Logger logger = Logger.getLogger(Knearestneighbourg.class.getName()); 
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
                
               logger.info("Instance :"+inst +"\n"+"validationOutput= "+outputValidationSet+"\n"+"Oracle Output= "+outputOracle);
                token=new StringTokenizer(Parameter);
                while(token.hasMoreTokens()){
                    
                    outputname=token.nextToken();
                    
                    method=OutputOracle.class.getMethod(outputname, int.class);
                    errorOutputRO=(Double)method.invoke(outputValidationSet,0)-(Double)method.invoke(outputOracle,0);
                    SEOutputRO=SEOutputRO+Math.pow(errorOutputRO,2);
                    logger.info( "error on "+outputname+"RO prediction for "+entry.getKey().toString().split("@")[0]+" = " +errorOutputRO );
                
                    errorOutputWO=(Double)method.invoke(outputValidationSet,1)-(Double)method.invoke(outputOracle,1);
                    SEOutputWO=SEOutputWO+Math.pow(errorOutputWO,2);
                    logger.info(   "error on "+outputname+"WO prediction for "+entry.getKey().toString().split("@")[0]+" = " +errorOutputWO );
                    }
            
             }
            RMSe=new Double[2];
            RMSe[0]=Math.sqrt(SEOutputRO)/Neighbourshood.numInstances();
            RMSe[1]=Math.sqrt(SEOutputWO)/Neighbourshood.numInstances();
            SEOutputRO=0D;
            SEOutputWO=0D;
            logger.info("RESULT considering "+Parameter+" of  "+entry.getKey().toString().split("@")[0]+":"+"RMSERO ="+RMSe[0]+"  RMSEWO ="+RMSe[1]);
            rmse.put(entry.getKey(),RMSe);
     }
       return rmse;
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
    
    private Instances FilterInstances(String[] options) throws Exception{        
        
     
        RemoveWithValues remove = new RemoveWithValues();                         // new instance of filter
        
        remove.setOptions(options);                                            // set options
        
        remove.setInputFormat(this.m_Training);                          // inform filter about dataset **AFTER** setting options
           
        Instances newData = Filter.useFilter(this.m_Training, remove);   // apply filter
    
    return newData;
    }
    
    private void SelectInstancesRP(InputOracle io) throws Exception{
        int index=DataSets.ARFFDataSet.attribute("ReplicationProtocol").index()+1;
        String[] options;
        switch((ReplicationProtocol)io.getForecastParam(ForecastParam.ReplicationProtocol)){
                            case TO:{
                                options = Utils.splitOptions("-C "+index+" -S "+1);
                                this.m_Training=FilterInstances(options);
                                 break;
                            }
                            case PB:{
                                options = Utils.splitOptions("-C "+index+" -S "+1.1+" -V ");
                                this.m_Training=FilterInstances(options);
                                options = Utils.splitOptions("-C "+index+" -S "+0.9);
                                this.m_Training=FilterInstances(options);
                                break;
                            }
                            default :{
                                options = Utils.splitOptions("-C "+index+" -S "+1+" -V ");
                                this.m_Training=FilterInstances(options);
                                break;
                            
                            }
                              
                        }
    
    }

 
}
