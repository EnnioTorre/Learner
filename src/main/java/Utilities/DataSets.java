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
import java.lang.instrument.Instrumentation;

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
import csv.PrintDataOnCsv;
import csv.ReadDataFromCsv;
import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
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
    
    
    private int numberOfFeatures;
    
    
    
    public DataSets(String Directory_path) throws Exception{
         PropertyConfigurator.configure("conf/log4j.properties");
         int numFiles=0;
         logger.info("Start of Datasets Creation");
         
         try{
             
             
         init();
         
         File dir=new File("dataset");
         
         if( dir.exists()&&dir.list().length>0) {
        
             logger.info("Datasets Creation from Csv File" );
                 numFiles=ImportDataset("dataset");
             }
         
         else{
             
                logger.info("Datasets Creation from Queries to Oracles" );
                 numFiles=CreateDataset(Directory_path);
                 PrintDataOnCsv.PrintCsvFile();
             }
         
        
       
          //only for data Analisis
          DataPrinting.PrintARFF();
          
          DataPrinting.PrintCombinedPrediction();
        }
    
       
        
        catch(Exception e){
          
            e.printStackTrace();
        }
        
        finally{
          logger.info("Dataset Created "+numFiles+" File Readed");
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
    
    private void UpdatePredictionSet(InputOracle i,Instance inst)throws Exception{
    
    
    
   
    
    
    for(Map.Entry<Oracle,HashMap<Instance,OutputOracle>> entry:predictionResults.entrySet()){
                DatasetOutputOracle dat=new DatasetOutputOracle();
                //System.out.println(entry.getKey());
                int errorflag=1;
                OutputOracle output=null;
                while(errorflag>0){//try several time to forecast,bug in the DAGS Oracle!!!
                
                    try{
                    
                    output=entry.getKey().forecast(i);
                    
                    
                    errorflag=0;
              
                    }
                   
                    catch(Exception ex){
                        ex.printStackTrace();
                        if(errorflag<1){
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
          
          entry.getValue().put(inst, dat);
          
          logger.info(entry.getKey().toString()+"->"+dat.toString());
            }
              
    }
    
    private void UpdateValidationSet(InputOracle i,Instance inst)throws Exception{
        
        DatasetOutputOracle dat=new DatasetOutputOracle();
        for (Field f: DatasetOutputOracle.class.getDeclaredFields()){
        
            Method method=DatasetOutputOracle.class.getDeclaredMethod("set"+f.getName(),int.class, double.class);
            Method method2=CsvReader.class.getDeclaredMethod(f.getName(),int.class);
            method.invoke(dat,0, method2.invoke(i, 0));
            method.invoke(dat,1, method2.invoke(i, 1));
            
        }
        ValidationSet.put(inst,dat);
        logger.info("Instance Output-> "+ValidationSet.get(inst).toString());
        
    
    }
    
    private void UpdatePredictionSet(ReadDataFromCsv i,Instance inst) throws  Exception {
    
        // DataInputOracle in=DataConverter.FromInstancesToInputOracle(i);
        //  logger.info(in.toString());
    
    
    for(Map.Entry<Oracle,HashMap<Instance,OutputOracle>> entry:predictionResults.entrySet()){
                DatasetOutputOracle dat=i.getOutputOracle(entry.toString().split("@")[0]);
                
                entry.getValue().put(inst, dat);
               
               logger.info(entry.getKey().toString()+"->"+dat.toString());
    }
    
            
            
    
    }
    
    private void UpdateValidationSet(ReadDataFromCsv i,Instance inst) throws  Exception {
    
        // DataInputOracle in=DataConverter.FromInstancesToInputOracle(i);
        //  logger.info(in.toString());
    
                DatasetOutputOracle dat=i.getOutputOracle("Output");
                ValidationSet.put(inst, dat);

               logger.info("Instance Output-> "+ValidationSet.get(inst).toString());
               
       }
    
    
    
    private int CreateDataset(String Directory_path) throws Exception{
    
        int numFiles=0;
        
        File dir = new File(Directory_path);
      for (File nextdir : dir.listFiles()) {
         if (nextdir.isDirectory()) {
            for (File csv : nextdir.listFiles()) {
               if (!csv(csv)) {
                  continue;
               }
               else{
                   
                 CsvReader  reader=new CsvReader(new CsvRgParams(csv.getPath()));
                   
                   Instance i=DataConverter.FromInputOracleToInstance(reader);
                   logger.info(i.toString());
                   ByteArrayOutputStream out = new ByteArrayOutputStream();
                   
                   System.out.println("SIZE "+ out.toByteArray().length);
                   System.out.println("oracle on csv "+csv.getAbsolutePath());
                   ARFFDataSet.add(i);
                   InstancesMap.put(i.toStringNoWeight(), i);
                   UpdateValidationSet(reader,i);
                   //InputOracle inp=DataConverter.FromInstancesToInputOracle(i);
                   UpdatePredictionSet(reader,i);
                   PrintDataOnCsv.setCsvPath(i,csv.getAbsolutePath());
                   numFiles ++;
                         }
                             
                       
                     
                     }
               
               
               }  
            }
    
         return numFiles;
    
    }
    
    
    
    private int ImportDataset(String Directory_path) throws FileNotFoundException, IOException, Exception{
    
        int numFIles=0;
        ReadDataFromCsv reader=new ReadDataFromCsv(Directory_path);
        
        while(reader.ReadNextRow()){
            Instance i=DataConverter.FromInputOracleToInstance(reader);
            logger.info(i.toString());
            UpdatePredictionSet(reader,i);
            UpdateValidationSet(reader, i);
             ARFFDataSet.add(i);
            InstancesMap.put(i.toStringNoWeight(), i);
            
            numFIles++;
        
    
    
    
        } 
        return numFIles;
    
    }
        
        
}





