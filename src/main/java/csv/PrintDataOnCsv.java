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
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
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
    private static boolean isInit=false;
    private static HashMap<Instance,String>csvHash=new HashMap<Instance,String>();
   

     private static void PrintStringSet(PrintWriter out) throws Exception{
     
         
         
         for(Map.Entry<Instance,OutputOracle> outerentry:DataSets.ValidationSet.entrySet()){
             
             Names="csvPath";
                 Values=csvHash.get(outerentry.getKey());
                 int i=0;
                for(double d:outerentry.getKey().toDoubleArray()){
                
                    Names=Names+","+DataSets.ARFFDataSet.attribute(i++).name();
                    Values=Values+","+d;
                }
             
             for(Map.Entry<Oracle,HashMap<Instance,OutputOracle> >innerentry:DataSets.predictionResults.entrySet()){
                
                 
                
                StringTokenizer st =new StringTokenizer (innerentry.getValue().get(outerentry.getKey()).toString());
                while(st.hasMoreTokens()){
                
                    StringTokenizer st1 =new StringTokenizer (st.nextToken(),"=");
                    Names=Names+","+innerentry.getKey().toString().split("@")[0]+st1.nextToken();
                    Values=Values+","+st1.nextToken();
                }
                
             }
             
             StringTokenizer st =new StringTokenizer (outerentry.getValue().toString());
                while(st.hasMoreTokens()){
                
                    StringTokenizer st1 =new StringTokenizer (st.nextToken(),"=");
                    Names=Names+",Output"+st1.nextToken();
                    Values=Values+","+st1.nextToken();
                }
             if(!isInit){
             
                 out.println(Names);
                 out.println(Values);
                 isInit=true;
             }
             
             else
                  out.println(Values);   
         }
     
     }
     
     public static void setCsvPath(Instance i,String path){
             LearnerConfiguration LK=LearnerConfiguration.getInstance();
         
         if (LK.isCSVOutputenable()){
         
             csvHash.put(i, path);
         
         }
         
     }
     
     
     
     public static void PrintCsvFile() throws Exception{
            LearnerConfiguration LK=LearnerConfiguration.getInstance();
            PrintWriter out=null;
            FileWriter fstream=null;
         if (LK.isCSVOutputenable()&&DataSets.ValidationSet!=null&&DataSets.predictionResults!=null){
         
         try{
           
             
            
              fstream = new FileWriter("CsvTrainingSet.csv",false);//TODO change to false
         
              out = new PrintWriter(fstream);
              PrintStringSet(out);
             
         
         }
         catch(Exception e){
         
             e.printStackTrace();
         }
         finally{
         
             if(out!=null)
             
                 out.close();
         }
         
         }
         
     }
     
     public static void PrintStringOnCsvFile(String filepath,String towrite){
     
         PrintWriter out=null;
         FileWriter fstream=null;
         
         String header="";
         String Values="";
         boolean headerPrint=true;
         StringTokenizer st= new StringTokenizer(towrite,",");
         while(st.hasMoreTokens()){
             
             header=header+st.nextToken()+",";
             Values=Values+st.nextToken()+",";
         
         }
         
         try{
           
             
              if( new File(filepath).exists())
                  headerPrint=false;
              fstream = new FileWriter(filepath,true);
              
              out = new PrintWriter(fstream);
              if(headerPrint)
              out.println(header);
              out.println(Values);
             
         
         }
         catch(Exception e){
         
             e.printStackTrace();
         }
         finally{
         
             if(out!=null)
             
                 out.close();
         }
         
     
     }
}
