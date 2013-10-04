/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;
import csv.CsvReader;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.ForecastParam;
import static eu.cloudtm.autonomicManager.commons.ForecastParam.NumNodes;
import eu.cloudtm.autonomicManager.oracles.InputOracle;
import eu.cloudtm.autonomicManager.oracles.Oracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import eu.cloudtm.autonomicManager.oracles.exceptions.OracleException;
import eu.cloudtm.autonomicManager.simulator.SimulatorOracle;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
import testsimulator.testsimulatorforecast;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Map.Entry;
import morphr.MorphR;
import tasOracle.TasOracle;

import weka.core.Instances;

import weka.core.converters.ConverterUtils.DataSource;
/**
 *
 * @author etorre
 */
public class DataSetCreator {
    static Logger logger = Logger.getLogger(DataSetCreator.class.getName());
    private Instances ARFFDataSet;
    private HashMap<Instance,OutputOracle>ValidationSet;
    private HashMap<Oracle,HashMap<Instance,OutputOracle>> predictionResults;
    private HashMap<Instance,OutputOracle>TrainingSet=new HashMap<Instance,OutputOracle>();
    private CsvReader reader;
    
    private int numberOfFeatures;
    
    
    
    public DataSetCreator(String Directory_path) throws Exception{
         PropertyConfigurator.configure("conf/log4jLearner.properties");
         
         Init("conf/K-NN/dataset.arff");
         
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
                   UpdateValidationSet(i);
                   
                   
                         
                         
                         }
                             
                       
                     
                     }
               
               
               }  
            }
      logger.info(ARFFDataSet);
      
         }
         
      
    
    

    
    private static boolean csv(File f) {
      System.out.println(f);
      return f.toString().endsWith("csv");
   }
    
    private void Init(String f)throws Exception{
       System.out.println(new File(f).exists());
        DataSource source = new DataSource(f);
        
             ARFFDataSet = source.getStructure();

            numberOfFeatures=ARFFDataSet.numAttributes();
            
            ValidationSet=new HashMap<Instance,OutputOracle>();
         
            predictionResults=new HashMap<Oracle,HashMap<Instance,OutputOracle>>(3);
            
            for(int i=0;i<predictionResults.size();i++){
            
                if(i==0)
                    predictionResults.put(new TasOracle(), new HashMap<Instance,OutputOracle>());
                if(i==1)
                     predictionResults.put(new MorphR(), new HashMap<Instance,OutputOracle>());
                if(i==2)
                    predictionResults.put(new SimulatorOracle(), new HashMap<Instance,OutputOracle>());
            }
            
         
             }
    
    private void UpdatePredictionSet(Instance i)throws Exception{
    
    
    InputOracle in=DataConverter.FromInstancesToInputOracle(i);
    
    TasOracle t = new TasOracle();
    MorphR morphr = new MorphR();
    SimulatorOracle simulatorOracle = new SimulatorOracle();
    int classIndex=1;
    
    for(Map.Entry<Oracle,HashMap<Instance,OutputOracle>> entry:predictionResults.entrySet()){
                DatasetOutputOracle dat=new DatasetOutputOracle();
                entry.getKey().forecast(in).
                entry.setValue(new HashMap<Instance,OutputOracle>());
          for (Field f: DatasetOutputOracle.class.getDeclaredFields()){
        
            Method method=DatasetOutputOracle.class.getDeclaredMethod("set"+f.getName(), double.class);
            
            method.invoke(dat, entry.getKey().forecast(in).);
            
        }
            }
    t.forecast(in)
        
        ValidationSet.put(i,dat);
        
    }
    
    private void UpdateValidationSet(Instance i)throws Exception{
        
        DatasetOutputOracle dat=new DatasetOutputOracle();
        for (Field f: DatasetOutputOracle.class.getDeclaredFields()){
        
            Method method=DatasetOutputOracle.class.getDeclaredMethod("set"+f.getName(), double.class);
            Method method2=CsvReader.class.getDeclaredMethod(f.getName());
            method.invoke(dat, method2.invoke(reader));
            
        }
        ValidationSet.put(i,dat);
        
    
    }
        
        
}





