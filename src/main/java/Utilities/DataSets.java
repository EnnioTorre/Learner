/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;
import CsvOracles.params.CsvRgParams;



import Utilities.DataConverter.DataInputOracle;
import csv.CsvReader;
import eu.cloudtm.autonomicManager.oracles.Oracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;


import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import weka.core.Instance;


import eu.cloudtm.autonomicManager.oracles.InputOracle;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import morphr.MorphR;
import tasOracle.common.TasOracle;
import eu.cloudtm.autonomicManager.simulator.SimulatorOracle;
import weka.core.Instances;
import weka.core.converters.ConverterUtils.DataSource;
import CsvOracles.params.CsvRgParams;
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
         
         init();
         
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
                   
                   reader=new CsvReader(new CsvRgParams(csv.getPath()));
                   System.out.println(reader);
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
          DataPrinting.PrintARFF();
        }
        
        catch(Exception e){
          
            e.printStackTrace();
        }
        
        finally{
          logger.info(numFiles+" File Readed");
        }
            
         }
         
      
    
    

    
    private static boolean csv(File f) {
      
      return f.toString().endsWith("csv");
   }
    
    private void init()throws Exception{
       
        LearnerConfiguration LK=LearnerConfiguration.getInstance();
        
        DataSource source = new DataSource(LK.getOracleInputDescription());
        
             ARFFDataSet = source.getStructure();

            numberOfFeatures=ARFFDataSet.numAttributes();
            
            ValidationSet=new HashMap<Instance,OutputOracle>();
         
            predictionResults=new HashMap<Oracle,HashMap<Instance,OutputOracle>>(3);
            
           for (Class c:LK.getOracles()){
              predictionResults.put((Oracle)c.newInstance(), new HashMap<Instance,OutputOracle>());
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
                while(errorflag>0){//try several time to forecast,bug in the DAGS Oracle!!!
                
                    try{
                    System.out.println(entry.getKey());
                    output=entry.getKey().forecast(in);
                    System.out.println(output);
                    errorflag=0;
              
                    }
                   
                    catch(Exception ex){
                        ex.printStackTrace();
                        if(errorflag<10){
                            errorflag++;
                            continue;
                        }
                        
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





