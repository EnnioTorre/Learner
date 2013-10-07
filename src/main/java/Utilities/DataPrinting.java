/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Utilities;


import eu.cloudtm.autonomicManager.oracles.OutputOracle;

import java.util.HashMap;
import java.util.Map;
import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;
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
    static Logger logger = Logger.getLogger(DataSets.class.getName());

   static void PrintARFF(){
   
      PropertyConfigurator.configure("conf/log4j.properties"); 
      logger.info(DataSets.ARFFDataSet);
   }
   
   static void PrintSet(HashMap <Instance,OutputOracle> set) throws Exception{
      PropertyConfigurator.configure("conf/log4j.properties"); 
      double [] Outputs=new double [6];
      double[] both;
       
       Instances NewData=new DataSource("conf/K-NN/dataset.arff").getStructure();
       NewData.insertAttributeAt(new Attribute("ThroughputRO"), NewData.numAttributes());
        
       NewData.insertAttributeAt(new Attribute("ThroughputWO"), NewData.numAttributes());
       NewData.insertAttributeAt(new Attribute("AbortRateRO"), NewData.numAttributes());
       NewData.insertAttributeAt(new Attribute("AbortRateWO"), NewData.numAttributes());
       NewData.insertAttributeAt(new Attribute("ResponseTimeRO"), NewData.numAttributes());
       NewData.insertAttributeAt(new Attribute("ResponseTimeWO"), NewData.numAttributes());
       for(Map.Entry<Instance,OutputOracle> entry:set.entrySet()){
           
           Outputs[0]=entry.getValue().throughput(0);
           Outputs[1]=entry.getValue().throughput(1);
           Outputs[2]=entry.getValue().abortRate(0);
           Outputs[3]=entry.getValue().abortRate(1);
           Outputs[4]=entry.getValue().responseTime(0);
           Outputs[5]=entry.getValue().responseTime(1);

           both = addTwoArray(entry.getKey().toDoubleArray(),Outputs);
           Instance I=new DenseInstance(0,both);
           
           NewData.add(I);
           
   
   }
       
      
        logger.info(NewData);
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
   
   
}
