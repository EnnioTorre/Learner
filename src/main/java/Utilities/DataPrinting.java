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
   
  private static void PrintValidationSet() throws Exception{
    Logger log = Logger.getLogger("VLogger"); 
    
    Instances i=new DataSource(LearnerConfiguration.getInstance().getOracleInputDescription()).getStructure();
        if(DataSets.ValidationSet!=null) { 
               HashMap []tmp = {DataSets.ValidationSet};
               String []oracle ={"training set"};
             log.info( PrintSet(tmp,oracle));       
        }
        else{
          logger.warn("--Tas Oracle Dataset not present");
          throw new NoSuchFieldException();  
        } 
          
   }
   
   
   
  private static void PrintTasPrediction() throws Exception{
     Logger log = Logger.getLogger("TasLogger"); 
     
    try{
        Instances i=new DataSource(LearnerConfiguration.getInstance().getOracleInputDescription()).getStructure();
    
        for(Map.Entry<Oracle,HashMap<Instance,OutputOracle> >entry:DataSets.predictionResults.entrySet()){
     
         if(TasOracle.class.isInstance(entry.getKey())){
           
               HashMap []tmp = {entry.getValue()};
               String []oracle ={""};
             log.info( PrintSet(tmp,oracle));
               return;
         }         
             
         }
    }
    catch(Exception e){
          logger.warn("--Tas Oracle Dataset not present");
          e.printStackTrace();
          throw new NoSuchFieldException();
    } 
   ;   
    
   }
   
   private static void PrintMorpheRPrediction() throws Exception{
       Logger log = Logger.getLogger("MorphRLogger");
      
       Instances i=new DataSource(LearnerConfiguration.getInstance().getOracleInputDescription()).getStructure();
     try{
     for(Map.Entry<Oracle,HashMap<Instance,OutputOracle> >entry:DataSets.predictionResults.entrySet()){
     
         if(MorphR.class.isInstance(entry.getKey())){
           
            HashMap []tmp = {entry.getValue()};
            String []oracle ={""};
             log.info( PrintSet(tmp,oracle));
            
            return;
         }         
             
     
     }
     
     }
     catch(Exception e){
     logger.warn("--MorphR Oracle Dataset not present");
    throw new NoSuchFieldException();
     }
         
    
    
   }
     
    private static void PrintSOPrediction() throws Exception{
     Logger log = Logger.getLogger("SOLogger");
     
     try{
         Instances i=new DataSource(LearnerConfiguration.getInstance().getOracleInputDescription()).getStructure();
     for(Map.Entry<Oracle,HashMap<Instance,OutputOracle> >entry:DataSets.predictionResults.entrySet()){
         
         if(SimulatorOracle.class.isInstance(entry.getKey())){
             HashMap []tmp = {entry.getValue()};
             String []oracle ={""};
             log.info( PrintSet(tmp,oracle));
               return;
         }         
             
         }
     } catch(Exception e){
          logger.warn("--Simulator Oracle Dataset not present");
          throw new NoSuchFieldException(); 
          
     }
     
   }
    
    
    public static void PrintCombinedPrediction() throws  Exception{
        Logger log = Logger.getLogger("CombinedLogger");
     try{
         
     
         String []Oracles=new String[DataSets.predictionResults.size()+1];
         HashMap <Instance,OutputOracle>[]tmp = new HashMap[DataSets.predictionResults.size()+1];
         int position=0;
         for(Map.Entry<Oracle,HashMap<Instance,OutputOracle> >entry:DataSets.predictionResults.entrySet()){
         
         
            tmp[position]=entry.getValue();
            Oracles[position++]=entry.getKey().toString().split("@")[0];
                 
             
         }
        tmp[position]=DataSets.ValidationSet;
        Oracles[position]="training output";
        log.info(PrintSet(tmp,Oracles));
     } 
     catch(Exception e){
          logger.warn("--error in PrintCombinedPrediction "+e.getStackTrace()[0]);
          e.printStackTrace();
          throw new Exception(e.getCause());
          
          
     }
    
    }
    
    
   //add a set of instances and relative oracle output to another instances set .
   private static Instances PrintSet(HashMap <Instance,OutputOracle>[] set,String [] Oracles) throws Exception{
      
      double [] Outputs=new double [6];
      double[] both=null;
      HashMap <Instance,OutputOracle> tmp=new HashMap <Instance,OutputOracle>();
      
       Instances NewData=new DataSource(LearnerConfiguration.getInstance().getOracleInputDescription()).getStructure();
       for(String o:Oracles){
       NewData.insertAttributeAt(new Attribute("ThroughputRO"+o), NewData.numAttributes());
        
       NewData.insertAttributeAt(new Attribute("ThroughputWO"+o), NewData.numAttributes());
       NewData.insertAttributeAt(new Attribute("AbortRateRO"+o), NewData.numAttributes());
       NewData.insertAttributeAt(new Attribute("AbortRateWO"+o), NewData.numAttributes());
       NewData.insertAttributeAt(new Attribute("ResponseTimeRO"+o), NewData.numAttributes());
       NewData.insertAttributeAt(new Attribute("ResponseTimeWO"+o), NewData.numAttributes());
       }
       
       for(Map.Entry<Instance,OutputOracle> entry:set[0].entrySet()){
           both = addTwoArray(entry.getKey().toDoubleArray(),null);
           for(HashMap <Instance,OutputOracle> s:set){
           
               Outputs[0]=s.get(entry.getKey()).throughput(0);
           
               Outputs[1]=s.get(entry.getKey()).throughput(1);
           
               Outputs[2]=s.get(entry.getKey()).abortRate(0);
           
               Outputs[3]=s.get(entry.getKey()).abortRate(1);
           
               Outputs[4]=s.get(entry.getKey()).responseTime(0);
           
               Outputs[5]=s.get(entry.getKey()).responseTime(1);

           
           both = addTwoArray(both,Outputs);
           
      
           }
           Instance I=new DenseInstance(1,both);
           NewData.add(I);
       }
           
           return NewData;
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
