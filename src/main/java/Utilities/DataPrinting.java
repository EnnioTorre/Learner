/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Utilities;



import eu.cloudtm.autonomicManager.oracles.Oracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import tasOracle.common.TasOracle;
import morphr.MorphR;
import eu.cloudtm.autonomicManager.simulator.SimulatorOracle;
import weka.core.Instance;

import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instances;

import weka.core.converters.ConverterUtils.DataSource;

/**
 *
 * @author Ennio email:ennio_torre@hotmail.it
 */
public class DataPrinting {
  static Logger logger = Logger.getLogger(DataPrinting.class.getName()); 
    
   static void PrintARFF() throws Exception{
      
     LearnerConfiguration LK=LearnerConfiguration.getInstance();
     if(LK.isTasARFFenable())
         PrintTasPrediction();
     if(LK.isMorphRARFFenable())
         PrintMorpheRPrediction();
     if(LK.isValidationSetARFFenable())
         PrintValidationSet();
     if(LK.isDAGSARFFenable())
         PrintSOPrediction();
         
      //logger.info(DataSets.ARFFDataSet);
   }
   
   static void PrintValidationSet() throws Exception{
    Logger log = Logger.getLogger("VLogger"); 
    
    
        if(DataSets.ValidationSet!=null)  
               PrintSet(DataSets.ValidationSet,log);       
        
        else{
          logger.warn("--Tas Oracle Dataset not present");
          throw new NoSuchFieldException();  
        } 
          
   }
   
   
   
   static void PrintTasPrediction() throws Exception{
     Logger log = Logger.getLogger("TasLogger"); 
    
     for(Map.Entry<Oracle,HashMap<Instance,OutputOracle> >entry:DataSets.predictionResults.entrySet()){
     
         if(TasOracle.class.isInstance(entry.getKey())){
           
               PrintSet(entry.getValue(),log);
               return;
         }         
             
         }
          logger.warn("--Tas Oracle Dataset not present");
          throw new NoSuchFieldException();     
   }
   
    static void PrintMorpheRPrediction() throws Exception{
       Logger log = Logger.getLogger("MorphRLogger");
     
     for(Map.Entry<Oracle,HashMap<Instance,OutputOracle> >entry:DataSets.predictionResults.entrySet()){
     
         if(MorphR.class.isInstance(entry.getKey())){
           
            PrintSet(entry.getValue(),log);
            
            return;
         }         
             
     
     }
     logger.warn("--MorphR Oracle Dataset not present");
    throw new NoSuchFieldException();
   
   }
     
    static void PrintSOPrediction() throws Exception{
     Logger log = Logger.getLogger("SOLogger");
    
     for(Map.Entry<Oracle,HashMap<Instance,OutputOracle> >entry:DataSets.predictionResults.entrySet()){
     
         if(SimulatorOracle.class.isInstance(entry.getKey())){
              PrintSet(entry.getValue(),log);
               return;
         }         
             
         }
          logger.warn("--Simulator Oracle Dataset not present");
          throw new NoSuchFieldException();     
   }
    
   
   private static void PrintSet(HashMap <Instance,OutputOracle> set,Logger log) throws Exception{
      
      double [] Outputs=new double [6];
      double[] both;
       
       Instances NewData=new DataSource(LearnerConfiguration.getInstance().getOracleInputDescription()).getStructure();
       NewData.insertAttributeAt(new Attribute("ThroughputRO"), NewData.numAttributes());
        
       NewData.insertAttributeAt(new Attribute("ThroughputWO"), NewData.numAttributes());
       NewData.insertAttributeAt(new Attribute("AbortRateRO"), NewData.numAttributes());
       NewData.insertAttributeAt(new Attribute("AbortRateWO"), NewData.numAttributes());
       NewData.insertAttributeAt(new Attribute("ResponseTimeRO"), NewData.numAttributes());
       NewData.insertAttributeAt(new Attribute("ResponseTimeWO"), NewData.numAttributes());
       for(Map.Entry<Instance,OutputOracle> entry:set.entrySet()){
           
           Outputs[0]=entry.getValue().throughput(0);
           Outputs[1]=entry.getValue().throughput(1);
           Outputs[2]=entry.getValue().abortRate(0);
           Outputs[3]=entry.getValue().abortRate(1);
           Outputs[4]=entry.getValue().responseTime(0);
           Outputs[5]=entry.getValue().responseTime(1);

           both = addTwoArray(entry.getKey().toDoubleArray(),Outputs);
           Instance I=new DenseInstance(1,both);
           
           NewData.add(I);
           
   
   }
       
      
        log.info(NewData);
   }
   
   private static double[] addTwoArray(double[] objArr1, double[] objArr2){
    int arr1Length = objArr1!=null && objArr1.length>0?objArr1.length:0;
    int arr2Length = objArr2!=null && objArr2.length>0?objArr2.length:0;
    double[] resutlentArray = new double[arr1Length+arr2Length]; 
    for(int i=0,j=0;i<resutlentArray.length;i++){
        if(i+1<=arr1Length){
            resutlentArray[i]=objArr1[i];
        }else{
            resutlentArray[i]=objArr2[j];
            j++;
        }
    }

    return resutlentArray;
}
   
  // public static PrintAdditionalInfo()
   
   
}
