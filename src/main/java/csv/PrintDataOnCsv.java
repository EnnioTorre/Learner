/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csv;

import Utilities.DataSets;
import Utilities.DatasetOutputOracle;
import Utilities.LearnerConfiguration;
import eu.cloudtm.autonomicManager.oracles.Oracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;

/**
 *
 * @author Ennio email:ennio_torre@hotmail.it
 */
public class PrintDataOnCsv {
    
    private static String Names;
    private static String Values;
    private static HashMap<Instance,String>csvHash=new HashMap<Instance,String>();
   

     public static void StringSet(HashMap <Instance,OutputOracle> set,Logger log) throws Exception{
     
         
         
         for(Map.Entry<Instance,OutputOracle> outerentry:DataSets.ValidationSet.entrySet()){
             
             for(Map.Entry<Oracle,HashMap<Instance,OutputOracle> >innerentry:DataSets.predictionResults.entrySet()){
                
                 Names="csvPath";
                 Values=csvHash.get(outerentry.getKey());
                 int i=0;
                for(double d:outerentry.getKey().toDoubleArray()){
                
                    Names=Names+outerentry.getKey().attribute(i).name()+",";
                    Values=Values+d+",";
                }
                
                Names=Names+innerentry.getKey().toString().split("@")[0]+",";
                Values=Values+"{"+innerentry.getValue().get(outerentry.getKey()).toString()+"}"+",";
                
             }
             
             Names=Names+"Output,";
             Values=Values+"{"+outerentry.getValue().toString()+"}"+",";
         }
     
     }
     
     public static void setCsvFile(Instance i,String path){
             LearnerConfiguration LK=LearnerConfiguration.getInstance();
         
         if (LK.isCSVOutputenable()){
         
             csvHash.put(i, path);
         
         }
         
     }
}
