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
import weka.core.converters.ConverterUtils;
import weka.core.converters.ConverterUtils.DataSource;
/**
 *
 * @author Ennio email:ennio_torre@hotmail.it
 */
public class DataConverter {

   static InputOracle FromInstancesToInputOracle(Instance data){
        
         HashMap<Param,Double>param=new HashMap<Param,Double>();
         HashMap<EvaluatedParam,Double>evaluatedparam= new HashMap<EvaluatedParam,Double>();
         HashMap<ForecastParam,Double>forecastparam=new HashMap<ForecastParam,Double> ();
        double[] instValue=data.toDoubleArray();
        
        for(int i=0;i<data.numAttributes();i++){
           String parameter=data.attribute(i).name();
           try{
              
               
               param.put(Param.valueOf(parameter),instValue[i]);
               //System.out.println(parameter);
           
           }
           
           catch (IllegalArgumentException e){
           
               try{
                    evaluatedparam.put(EvaluatedParam.valueOf(parameter),instValue[i]);
                }
                  catch (IllegalArgumentException ef){
                   
                 try{
                    forecastparam.put(ForecastParam.valueOf(parameter),instValue[i]);
                 }
                 catch (IllegalArgumentException ex){
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
      
        private HashMap<Param,Double>param;
        private HashMap<EvaluatedParam,Double>evaluatedparam;
        private HashMap<ForecastParam,Double>forecastparam;
        
        public DataInputOracle(HashMap<Param,Double>param,HashMap<EvaluatedParam,Double>evaluatedparam,HashMap<ForecastParam,Double>forecastparam){
        
            this.param=param;
            this.evaluatedparam=evaluatedparam;
            this.forecastparam=forecastparam;
    }

        @Override
        public Object getParam(Param param) {
           return this.param.get(param);
        }

        @Override
        public Object getEvaluatedParam(EvaluatedParam ep) {
         
            return this.evaluatedparam.get(ep);
        }

        @Override
        public Object getForecastParam(ForecastParam fp) {
            return this.forecastparam.get(fp);
        }
      
    }
    
}
