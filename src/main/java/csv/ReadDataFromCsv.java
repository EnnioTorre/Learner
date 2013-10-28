/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package csv;

import Utilities.DataSets;
import Utilities.DatasetOutputOracle;
import Utilities.ParameterClassConversion;
import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.ForecastParam;
import eu.cloudtm.autonomicManager.commons.Param;
import eu.cloudtm.autonomicManager.oracles.InputOracle;
import eu.cloudtm.autonomicManager.oracles.OutputOracle;
import java.util.StringTokenizer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import org.apache.log4j.Logger;
import weka.core.Instance;

/**
 *
 * @author Ennio email:ennio_torre@hotmail.it
 */
public class ReadDataFromCsv implements InputOracle {
    private Logger logger = Logger.getLogger(ReadDataFromCsv.class.getName());
    private HashMap<String,Double>tmprow;
    private FileReader reader;
    private BufferedReader buffReader ;
    private String Header;
    
    public ReadDataFromCsv(String Directory_path) throws FileNotFoundException, IOException{
         
          tmprow=new HashMap<String,Double>();
         File dir = new File(Directory_path);
         if (dir.list().length<2 ){
           reader = new FileReader(dir.listFiles()[0]);
           buffReader = new BufferedReader(reader);  
           getHeader(); 
         }
      
         else{
             logger.error("Directory "+Directory_path+"exsist but contains More than just a file,decide wich one to use ");
             throw new IOException(Directory_path+" too many files "); 
         }
    }
    
    private void getHeader() throws IOException{
        try{
        Header=buffReader.readLine();
        }
        catch(NullPointerException ex){
        
            buffReader.close();
            reader.close();
            ex.printStackTrace();
        }
    } 
    
    public boolean ReadNextRow() throws IOException{
    
        
        String next=buffReader.readLine();
        double value;
        if(next!=null){
        StringTokenizer st=new StringTokenizer(next,",");
        StringTokenizer header=new StringTokenizer(Header,",");
        
        if(st.countTokens()==header.countTokens()){
            
            logger.info("Structure Update with a value read from" +header.nextToken()+" : "+st.nextToken());
             
       while (header.hasMoreTokens()){
        
            value=new Double(st.nextToken());
            tmprow.put(header.nextToken(),value);
        
        }
        
        }
        else{
            buffReader.close();
            reader.close();
            throw new IOException("Number of Attributes value in row ("+st.countTokens()+") differs from number of attributes ("+header.countTokens()+")");
       
        }
        
        return true;
        }
        else{
            buffReader.close();
            reader.close();
            return false;
            
        }
    }

    @Override
    public Object getParam(Param param) {
        try {
            System.out.println(param.toString());
            System.out.println(ParameterClassConversion.ConvertTo(param,tmprow.get(param.toString())));
            
            return ParameterClassConversion.ConvertTo(param,tmprow.get(param.toString()));
        } catch (Exception ex) {
            logger.info("Impossible to Get Parameter "+param+ex.getCause());
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Object getEvaluatedParam(EvaluatedParam ep) {
       try{
           System.out.println(ep.toString());
           System.out.println(ParameterClassConversion.ConvertTo(ep,tmprow.get(ep.toString())));
           return ParameterClassConversion.ConvertTo(ep,tmprow.get(ep.toString()));
       } catch (Exception ex) {
            logger.info("Impossible to Get Parameter "+ep+ex.getCause());
            ex.printStackTrace();
            return null;
        }
    }

    @Override
    public Object getForecastParam(ForecastParam fp) {
        try{
            System.out.println(fp.toString());
            System.out.println(ParameterClassConversion.ConvertTo(fp,tmprow.get(fp.toString())));
         return ParameterClassConversion.ConvertTo(fp,tmprow.get(fp.toString()));
        } catch (Exception ex) {
            logger.info("Impossible to Get Parameter "+fp+ex.getCause());
            ex.printStackTrace();
            return null;
        }
    }
    
    public DatasetOutputOracle getOutputOracle(String Oracle) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
        String keyWO=Oracle;
        String keyRO=Oracle;
        DatasetOutputOracle OO=new DatasetOutputOracle();
        
        for (Field f: DatasetOutputOracle.class.getDeclaredFields()){
        
            Method method=DatasetOutputOracle.class.getDeclaredMethod("set"+f.getName(),int.class, double.class);
            method.invoke(OO,0,tmprow.get(keyWO+f.getName()+"RO"));
            method.invoke(OO,1, tmprow.get(keyRO+f.getName()+"WO"));
            
            
        }
    
        
        
        
    
        return OO;
    }
}
