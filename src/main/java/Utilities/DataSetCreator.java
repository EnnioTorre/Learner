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

import weka.core.Instances;

import weka.core.converters.ConverterUtils.DataSource;
/**
 *
 * @author etorre
 */
public class DataSetCreator {
    static Logger logger = Logger.getLogger(DataSetCreator.class.getName());
    private Instances data;
    private HashMap<EvaluatedParam, Attribute> EMap = new HashMap<EvaluatedParam, Attribute>();
    private HashMap<ForecastParam, Attribute> FMap = new HashMap<ForecastParam, Attribute>();
    private CsvReader reader;
    
    private int numberOfFeatures;
    
    public DataSetCreator(String Directory_path) throws Exception{
         PropertyConfigurator.configure("conf/log4jLearner.properties");
         
         AcquiringDatasetInformation("conf/K-NN/dataset.arff");
         
        File dir = new File(Directory_path);
      for (File nextdir : dir.listFiles()) {
         if (nextdir.isDirectory()) {
            for (File csv : nextdir.listFiles()) {
               if (!csv(csv)) {
                  continue;
               }
               else{
                 
                   reader=new CsvReader(csv);
                 
                 
                 data.add(FillInstance());
                 logger.info(csv.toString()+data.toString());
               
               }  
            }
         }
         
      }
    }
    
    
    private Instance FillInstance(){
        
       
        // Create empty instance with numberOffeatures attribute values
       Instance inst = new DenseInstance(numberOfFeatures);//total number of features needed by oracles
        // Set instance's values for the attributes
       for(int i=0;i<data.numAttributes();i++){
           String parameter=data.attribute(i).name();
           System.out.println(parameter+"2");
           
           try{
              
               
               inst.setValue(data.attribute(i), reader.getParam(Param.valueOf(parameter)));
               System.out.println(parameter);
           
           }
           
           catch (IllegalArgumentException e){
           
               try{
                    inst.setValue(data.attribute(i), reader.getForecastParam(ForecastParam.valueOf(parameter)));
                }
                  catch (IllegalArgumentException ef){
                   
                 try{
                    inst.setValue(data.attribute(i), reader.getEvaluatedParam(EvaluatedParam.valueOf(parameter)));
                 }
                 catch (IllegalArgumentException ex){
                    throw new IllegalArgumentException(parameter+"is not a valid parameter");
                 }
               }
               
           }
           
           
           }
       
       System.out.println(inst);
       
       return inst;
         
}
    
    private static boolean csv(File f) {
      System.out.println(f);
      return f.toString().endsWith("csv");
   }
    
    private void AcquiringDatasetInformation(String f)throws Exception{
       System.out.println(new File(f).exists());
        DataSource source = new DataSource(f);
        
         data = source.getStructure();

            numberOfFeatures=data.numAttributes();
         
             }
        
        
    }



