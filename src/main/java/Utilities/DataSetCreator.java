/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;
import csv.CsvReader;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.ForecastParam;
import java.util.HashMap;
import java.util.Map;
import weka.core.Attribute;
import weka.core.DenseInstance;
import weka.core.Instance;
/**
 *
 * @author etorre
 */
public class DataSetCreator {
    private HashMap<Param, Attribute> PMap = new HashMap<Param, Attribute>();
    private HashMap<EvaluatedParam, Attribute> EMap = new HashMap<EvaluatedParam, Attribute>();
    private HashMap<ForecastParam, Attribute> FMap = new HashMap<ForecastParam, Attribute>();
    private CsvReader reader;
    String path="";
    
    public DataSetCreator(){
         
         reader=new CsvReader(path);
        

        // Create empty instance with three attribute values
        Instance inst = new DenseInstance(3);
        Attribute length = new Attribute("length");
        Attribute weight = new Attribute("weight");
        Attribute position = new Attribute("position");
      // Set instance's values for the attributes "length", "weight", and "position"
        inst.setValue(length, 5.3);
        inst.setValue(weight, 300);
        inst.setValue(position, "first"); 
    }
    
    private void FillInstance(){
        
       int numberOfParam=0;
       for(Param p :Param.values()){
       numberOfParam++;
       PMap.put(p,new Attribute(p.getKey()));
       }
       
       for(EvaluatedParam p :EvaluatedParam.values()){
       numberOfParam++;
        EMap.put(p,new Attribute(p.getKey()));
       }
       numberOfParam++;
       //serve solo a  contare l'inserimento successivo di ReplicationProtoco
       Attribute ReplicationProtocol = new Attribute("ReplicationProtocol");
       
       
       Instance inst = new DenseInstance(numberOfParam);
       for (Map.Entry<Param, Attribute> entry : PMap.entrySet()) {
 
          inst.setValue(entry.getValue(), reader.getParam(entry.getKey()));
          
       }
       
       for (Map.Entry<EvaluatedParam, Attribute> entry : EMap.entrySet()) {
 
          inst.setValue(entry.getValue(), reader.getEvaluatedParam(entry.getKey()));
          
       }
       
      inst.setValue(ReplicationProtocol,"TWOPC");
       
       
       
   
}

}
