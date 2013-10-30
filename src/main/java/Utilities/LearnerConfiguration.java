/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;
import java.util.StringTokenizer;
import org.apache.log4j.Logger;

/**
 *
 * @author Ennio email:ennio_torre@hotmail.it
 */
public class LearnerConfiguration {
    static Logger logger = Logger.getLogger(LearnerConfiguration.class.getName());
    private Properties defaultProps;
    private static LearnerConfiguration myself=null; 
    public  LearnerConfiguration(){
        try{
        defaultProps = new Properties();
        FileInputStream in = new FileInputStream("conf/K-NN/DataSets.properties");
        defaultProps.load(in);
        in.close();
        }
        
        catch(FileNotFoundException e){
         logger.warn(e.getStackTrace()[0]);
         
      }
      
      catch(IOException ef){
        
          
          logger.warn(ef.getStackTrace()[0]);
          
      }
      
    
    }
    
    public boolean isTasARFFenable(){
     if (defaultProps.getProperty("TasARFF").equals("true"))
         return true;
     return false;
    }
    
    public boolean isDAGSARFFenable(){
     if (defaultProps.getProperty("DAGSARFF").equals("true"))
         return true;
     return false;
    }
    public boolean isMorphRARFFenable(){
     if (defaultProps.getProperty("MorphRARFF").equals("true"))
         return true;
     return false;
    }
    
    public boolean isValidationSetARFFenable(){
     if (defaultProps.getProperty("ValidationSetARFF").equals("true"))
         return true;
     return false;
    }
    
    public boolean isCSVOutputenable(){
     if (defaultProps.getProperty("CSVOutput").equals("true"))
         return true;
     return false;
    }
    
    public Class[] getOracles() throws ClassNotFoundException{
     StringTokenizer st=new StringTokenizer(defaultProps.getProperty("Oracles"),",");
     
     Class Oracles[]=new Class[st.countTokens()];
     int i=0;
     
     while(st.hasMoreElements()){
         
         Oracles[i++]=Class.forName(st.nextToken());
          
         
     }
     return Oracles;
    }
    
    public String getOracleInputDescription(){
        
        return defaultProps.getProperty("ARFFwithInputOracleParameterpath");
    
    }
    
    public String getCombiner(){
    
        return defaultProps.getProperty("combiner");
    
    }
    
    public static LearnerConfiguration getInstance(){
        if (myself==null)
          myself=new LearnerConfiguration();
          return myself;
     
      
    }
    
    public String getCsvOutputDir(){
       
        String filename=defaultProps.getProperty("CsvOutputDirectory");
       if (!filename.equals("")){
       File f=new File(filename);
       if(!f.exists())
           f.mkdir();
       filename=filename+"/";
    }
       return filename;
    }
    
    public String getCsvInputDir(){
       
       return defaultProps.getProperty("CsvInputDirectory");
    }
    
    
    
    
    
}
