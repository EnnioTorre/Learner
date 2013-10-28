/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Learners;

import static Learners.Knearestneighbourg.logger;
import Utilities.DataSets;
import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.ForecastParam;
import eu.cloudtm.autonomicManager.commons.ReplicationProtocol;
import static eu.cloudtm.autonomicManager.commons.ReplicationProtocol.PB;
import static eu.cloudtm.autonomicManager.commons.ReplicationProtocol.TO;
import eu.cloudtm.autonomicManager.oracles.InputOracle;
import eu.cloudtm.autonomicManager.oracles.Oracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import java.io.BufferedReader;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.Utils;
import weka.filters.Filter;
import weka.filters.unsupervised.instance.RemoveWithValues;
import eu.cloudtm.autonomicManager.commons.IsolationLevel;

/**
 *
 * @author Ennio email:ennio_torre@hotmail.it
 */
public abstract class Learner {

    protected Instances m_Training = null;
    protected String m_TestSetFile = null;
    protected Instance m_TestSet = null;
    protected String m_TrainingFile = null;
    protected HashMap<Oracle,Double[]> RMSE;
    protected Instances Neighbourshood;
    
    
    
    public void setTraining(String name) throws Exception {
    m_Training     = new Instances(
                        new BufferedReader(new FileReader(name)));
    m_Training.setClassIndex(m_Training.numAttributes() - 1);
  }
    
       /**
 *
 * Valid value for Parameter are:
 * throughput
 * abortRate
 * responseTime
 * everyone separated by a space
 */
    
    protected HashMap<Oracle,Double[]> RMSE(String Parameter) throws Exception{
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
    
    protected Instances FilterInstances(String[] options) throws Exception{        
        
     
        RemoveWithValues remove = new RemoveWithValues();                         // new instance of filter
        
        remove.setOptions(options);                                            // set options
        
        remove.setInputFormat(this.m_Training);                          // inform filter about dataset **AFTER** setting options
           
        Instances newData = Filter.useFilter(this.m_Training, remove);   // apply filter
    
    return newData;
    }
    
    protected void SelectInstancesRP(InputOracle io) throws Exception{
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
    
    protected void SelectInstancesIL(InputOracle io) throws Exception{
        int index=DataSets.ARFFDataSet.attribute("ISOLATION_LEVEL").index()+1;
        String[] options;
        switch((IsolationLevel)io.getEvaluatedParam(EvaluatedParam.ISOLATION_LEVEL)){
                            case RR:{
                                options = Utils.splitOptions("-C "+index+" -S "+1);
                                this.m_Training=FilterInstances(options);
                                 break;
                            }
                            case RC:{
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