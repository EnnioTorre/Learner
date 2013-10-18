/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;
import csv.CsvReader;


import Utilities.DataConverter.DataInputOracle;
import eu.cloudtm.autonomicManager.oracles.Oracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;

import eu.cloudtm.autonomicManager.simulator.SimulatorOracle;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import weka.core.Instance;
;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import morphr.MorphR;
import tasOracle.TasOracle;

import weka.core.Instances;

import weka.core.converters.ConverterUtils.DataSource;
/**
 *
 * @author etorre
 */
public class DataSets {
    static Logger logger = Logger.getLogger(DataSets.class.getName());
    
    public static Instances ARFFDataSet;
    public static HashMap<Instance,OutputOracle>ValidationSet;
    public static HashMap<Oracle,HashMap<Instance,OutputOracle>> predictionResults;
    public static HashMap<String,Instance>InstancesMap=new HashMap<String,Instance>();
    private CsvReader reader;
    
    private int numberOfFeatures;
    
    
    
    public DataSets(String Directory_path) throws Exception{
         PropertyConfigurator.configure("conf/log4j.properties");
         
         Init("conf/K-NN/dataset.arff");
         
        logger.info("Start of Datasets Creation");
        int numFiles=0;
        try{
        File dir = new File(Directory_path);
      for (File nextdir : dir.listFiles()) {
         if (nextdir.isDirectory()) {
            for (File csv : nextdir.listFiles()) {
               if (!csv(csv)) {
                  continue;
               }
               else{
                 
                   reader=new CsvReader(csv);
                   Instance i=DataConverter.FromInputOracleToInstance(reader);
                   ARFFDataSet.add(i);
                   InstancesMap.put(i.toStringNoWeight(), i);
                   UpdateValidationSet(i);
                   //InputOracle inp=DataConverter.FromInstancesToInputOracle(i);
                   UpdatePredictionSet(i);  
                   numFiles ++;
                         }
                             
                       
                     
                     }
               
               
               }  
            }
          //only for data Analisis
          DataPrinting.PrintValidationSet();
          DataPrinting.PrintMorpheRPrediction();
          DataPrinting.PrintSOPrediction();
          DataPrinting.PrintTasPrediction();
        }
        
        finally{
          logger.info(numFiles+" File Readed");
        }
            
         }
         
      
    
    

    
    private static boolean csv(File f) {
      
      return f.toString().endsWith("csv");
   }
    
    private void Init(String f)throws Exception{
       
        DataSource source = new DataSource(f);
        
             ARFFDataSet = source.getStructure();

            numberOfFeatures=ARFFDataSet.numAttributes();
            
            ValidationSet=new HashMap<Instance,OutputOracle>();
         
            predictionResults=new HashMap<Oracle,HashMap<Instance,OutputOracle>>(3);
            
            for(int i=0;i<3;i++){
            
                
                if(i==0)
                    predictionResults.put(new TasOracle(), new HashMap<Instance,OutputOracle>());
                if(i==1)
                     predictionResults.put(new MorphR(), new HashMap<Instance,OutputOracle>());
                if(i==2)
                    predictionResults.put(new SimulatorOracle(), new HashMap<Instance,OutputOracle>());
            }
            
         
             }
    
    private void UpdatePredictionSet(Instance i)throws Exception{
    
    
    DataInputOracle in=DataConverter.FromInstancesToInputOracle(i);
    logger.info(in.toString());
    
    
    for(Map.Entry<Oracle,HashMap<Instance,OutputOracle>> entry:predictionResults.entrySet()){
                DatasetOutputOracle dat=new DatasetOutputOracle();
                //System.out.println(entry.getKey());
                int errorflag=1;
                OutputOracle output=null;
                while(errorflag>0){//try several time to forecast,bug in the DAGS Oracle
                
                    try{
                
                    output=entry.getKey().forecast(in);
                    errorflag=0;
              
                    }
                
                    catch(Exception ex){
                   
                        if(errorflag<10){
                            errorflag++;
                            continue;
                        }
                        dat=new DatasetOutputOracle();
                        dat.initOnOracleError();
                        return;
                    }
                }
                 
          for (Field f: DatasetOutputOracle.class.getDeclaredFields()){
        
            Method method=DatasetOutputOracle.class.getDeclaredMethod("set"+f.getName(),int.class, double.class);
            Method method2=OutputOracle.class.getDeclaredMethod(f.getName(),int.class);
            method.invoke(dat,0, method2.invoke(output, 0));
            method.invoke(dat,1, method2.invoke(output, 1));
            
        }
          entry.getValue().put(i, dat);
          logger.info("Instance Output-> "+ValidationSet.get(i).toString());
          logger.info(output.toString()+"->"+dat.toString());
            }
              
    }
    
    private void UpdateValidationSet(Instance i)throws Exception{
        
        DatasetOutputOracle dat=new DatasetOutputOracle();
        for (Field f: DatasetOutputOracle.class.getDeclaredFields()){
        
            Method method=DatasetOutputOracle.class.getDeclaredMethod("set"+f.getName(),int.class, double.class);
            Method method2=CsvReader.class.getDeclaredMethod(f.getName(),int.class);
            method.invoke(dat,0, method2.invoke(reader, 0));
            method.invoke(dat,1, method2.invoke(reader, 1));
            
        }
        ValidationSet.put(i,dat);
        
    
    }
        
        
}





