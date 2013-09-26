/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utilities;
import java.io.File;
import java.util.ArrayList;
import weka.core.Instance;
import weka.core.Instances;
import weka.core.converters.CSVLoader;
/**
 *
 * @author etorre
 */
public class NewClass {
   public void CsvLoader( String path) throws Exception{
   // load CSV
    Instance I;
    double value=0,number=0;
    ArrayList<Integer> arr3 = new ArrayList<Integer>();
    CSVLoader loader = new CSVLoader();
    loader.setSource(new File(path));
    Instances data = loader.getDataSet();
          System.out.println(data.instance(1));
          if(data.attribute(16).isString()||data.attribute(16).isNominal()){
          System.out.println(data.instance(1).attribute(16).value(0));
          }
          else
          System.out.println(data.meanOrMode(data.attribute(20)));
       }
    
    }
    
  
   

