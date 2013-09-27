/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;
import csv.CsvReader;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.ForecastParam;
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
import weka.core.converters.CSVLoader;
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
    
    public DataSetCreator(String Directory_path) throws IOException{
         PropertyConfigurator.configure("conf/log4jLearner.properties");
         
        File dir = new File(Directory_path);
      for (File nextdir : dir.listFiles()) {
         if (nextdir.isDirectory()) {
            for (File csv : nextdir.listFiles()) {
               if (!csv(csv)) {
                  continue;
               }
               else{
                 
                   reader=new CsvReader(csv);
                 AcquiringDatasetInformation(csv);
                 
                 data.add(FillInstance());
                 logger.info(data.instance(1));
               
               }  
            }
         }
         
      }
    }
    
    
    private Instance FillInstance(){
        
       int numberOfattribute=numberOfFeatures;
        // Create empty instance with numberOffeatures attribute values
       Instance inst = new DenseInstance(numberOfattribute);
        // Set instance's values for the attributes
       for(Param p :Param.values()){
       
       inst.setValue(new Attribute(p.getKey()), reader.getParam(p));
       }
      
       for(EvaluatedParam p :EvaluatedParam.values()){
     
        
        inst.setValue(new Attribute(p.getKey()), reader.getEvaluatedParam(p));
       }
       
       //serve solo a  contare l'inserimento successivo di ReplicationProtoco
       Attribute ReplicationProtocol = new Attribute("ReplicationProtocol");
       inst.setValue(ReplicationProtocol,"TWOPC");
       
       return inst;
         
}
    private static boolean csv(File f) {
      System.out.println(f);
      return f.toString().endsWith("csv");
   }
    
    private void AcquiringDatasetInformation(File f)throws IOException{
        
       CSVLoader loader = new CSVLoader();
        loader.setSource(f);
        Instances tmp = loader.getDataSet();
        
        if(numberOfFeatures==0){
            data=tmp;
            numberOfFeatures=data.numAttributes();
            data.delete();
             }
        else if(numberOfFeatures!=tmp.numAttributes()){
           logger.warn("csv file not appropriate:numberOfFeatures!=numAttributes");
           throw new IOException("csv file not appropriate:numberOfFeatures!=numAttributes");
        }
        
        
    }

}
