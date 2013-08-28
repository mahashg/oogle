package sm.nlp.oogle.phrase_correction;

import java.util.List;

import sm.nlp.oogle.datastore.ApplicationPropertyStore;
import sm.nlp.oogle.logger.OogleLogger;

import com.microsoft.research.webngram.service.LookupService;
import com.microsoft.research.webngram.service.NgramServiceFactory;

public class NGramServiceManager {
	
	public final static NGramServiceManager manager = new NGramServiceManager();
	private final String token="82c54bdc-56ba-4a45-9949-a919ceaae5d9";
	private NgramServiceFactory fac;
	private LookupService lookup;
	
	private NGramServiceManager(){
		configure();
		fac=NgramServiceFactory.newInstance(token);
		lookup=fac.newLookupService();
	}
	
	private void configure(){
		ApplicationPropertyStore props = ApplicationPropertyStore.propStore;
		
		if(props.isProxy_enable()){
			System.setProperty("http.proxyHost", props.getProxy_host());
			System.setProperty("http.proxyPort", props.getProxy_port());
		}
	}
	
	public List<Double> getPhraseProbability(List<String> phrase){		
		try{
			List<Double> prob=lookup.getProbabilities(token, "bing-body/jun09/3", phrase);
			return prob;
		}catch(Exception e){
			OogleLogger.logger.info("Error in communication with NGram Server.");
			OogleLogger.logger.info(e.getMessage()+"Error Due to, "+e.getCause());
		}
		return null;	
		
	}
}