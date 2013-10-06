/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package Utilities;
import eu.cloudtm.autonomicManager.commons.EvaluatedParam;
import eu.cloudtm.autonomicManager.commons.ForecastParam;
import eu.cloudtm.autonomicManager.commons.Param;
import weka.core.Instance;
import eu.cloudtm.autonomicManager.oracles.InputOracle;
import java.util.HashMap;
import weka.core.DenseInstance;
import weka.core.Instances;
import weka.core.converters.ConverterUtils;
import weka.core.converters.ConverterUtils.DataSource;
/**
 *
 * @author Ennio email:ennio_torre@hotmail.it
 */
public class DataConverter {

   static InputOracle FromInstancesToInputOracle(Instance data) throws Exception{
        
         
         HashMap<Param,Object>param=new HashMap<Param,Object>();
         HashMap<EvaluatedParam,Object>evaluatedparam= new HashMap<EvaluatedParam,Object>();
         HashMap<ForecastParam,Object>forecastparam=new HashMap<ForecastParam,Object> ();
        double[] instValue=data.toDoubleArray();
        Object value;
        String parameter;
        DataSource source = new DataSource("conf/K-NN/dataset.arff");
        
        for(int i=0;i<source.getDataSet().numAttributes();i++){
           parameter=source.getDataSet().attribute(i).name();
           
           try{
              
               value=ParameterClassConversion.ConvertTo(ForecastParam.valueOf(parameter), instValue[i]);
               System.out.println((ForecastParam.valueOf(parameter))+" ; "+value);
               forecastparam.put(ForecastParam.valueOf(parameter),value);
               
           
           }
           
           catch (IllegalArgumentException e){
           
               try{
                    value=ParameterClassConversion.ConvertTo(EvaluatedParam.valueOf(parameter), instValue[i]);
                    System.out.println((EvaluatedParam.valueOf(parameter))+" ; "+value);
                    evaluatedparam.put(EvaluatedParam.valueOf(parameter),value);
                }
                  catch (IllegalArgumentException ef){
                       
                 try{
                     
                     value=ParameterClassConversion.ConvertTo(Param.valueOf(parameter), instValue[i]);
                     param.put(Param.valueOf(parameter),value);
                     System.out.println((Param.valueOf(parameter))+" ; "+value);
                     
                 }
                 catch (IllegalArgumentException ex){
                     System.out.println(ex);
                    throw new IllegalArgumentException(parameter+"is not a valid parameter");
                 }
        }
      
      
        
        
    }
        }
    
        return new DataInputOracle(param,evaluatedparam,forecastparam);
    }
    
   
   static Instance FromInputOracleToInstance(InputOracle input) throws Exception{
   
       DataSource source = new DataSource("conf/K-NN/dataset.arff");
       Instance inst=new DenseInstance(source.getStructure().numAttributes());
       
       for(int i=0;i<source.getStructure().numAttributes();i++){
           String parameter=source.getStructure().attribute(i).name();
           System.out.println(parameter+"2");
           
           try{
              
               double ParamValue =Double.class.cast(input.getParam(Param.valueOf(parameter)));
               inst.setValue(source.getStructure().attribute(i), ParamValue);
               //System.out.println(parameter);
           
           }
           
           catch (IllegalArgumentException e){
           
               try{
                   
                    double ForecastValue =Double.class.cast(input.getForecastParam(ForecastParam.valueOf(parameter)));
                    inst.setValue(source.getStructure().attribute(i), ForecastValue);
                }
                  catch (IllegalArgumentException ef){
                   
                 try{
                    double EvaluatedParamValue =Double.class.cast(input.getEvaluatedParam(EvaluatedParam.valueOf(parameter)));
                    inst.setValue(source.getStructure().attribute(i),EvaluatedParamValue);
                 }
                 catch (IllegalArgumentException ex){
                    throw new IllegalArgumentException(parameter+"is not a valid parameter");
                 }
               }
               
           }
           
           
           }
       
       //System.out.println(inst);
       
       return inst;
       
   }
   
   static DatasetOutputOracle FromInputOracleToOutputOracle(){
       
       
       return null;
   
   }
    
    
    static class DataInputOracle implements InputOracle{
      
        private HashMap<Param,Object>param;
        private HashMap<EvaluatedParam,Object>evaluatedparam;
        private HashMap<ForecastParam,Object>forecastparam;
        
        public DataInputOracle(HashMap<Param,Object>param,HashMap<EvaluatedParam,Object>evaluatedparam,HashMap<ForecastParam,Object>forecastparam){
        
            this.param=param;
            this.evaluatedparam=evaluatedparam;
            this.forecastparam=forecastparam;
            System.out.println("data set created");
    }

        @Override
        public Object getParam(Param param) {
            System.out.println("required "+param);
           return this.param.get(param);
        }

        @Override
        public Object getEvaluatedParam(EvaluatedParam ep) {
            System.out.println("required "+ep);
            return this.evaluatedparam.get(ep);
        }

        @Override
        public Object getForecastParam(ForecastParam fp) {
            System.out.println("required "+fp);
            return this.forecastparam.get(fp);
        }
      
    }
    
}
