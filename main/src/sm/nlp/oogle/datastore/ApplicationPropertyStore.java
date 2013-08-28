package sm.nlp.oogle.datastore;

import java.io.FileReader;
import java.util.Properties;

import sm.nlp.oogle.logger.OogleLogger;

public class ApplicationPropertyStore {
	public static ApplicationPropertyStore propStore = new ApplicationPropertyStore();
	
	private final String property_fileName = "resource//application.properties";
	private int sentence_window_size = 2;
	private double generation_reduction_factor = 1.0;
	private int no_candidate_word_correction = -1;
	
	private boolean proxy_enable;
	private String proxy_host;
	private String proxy_port;
	
	private ApplicationPropertyStore() {
		loadProperties();
	}
	
	public int getSentence_window_size() {
		return sentence_window_size;
	}

	public double getGeneration_reduction_factor() {
		return generation_reduction_factor;
	}

	public int getNo_candidate_word_correction(){
		return this.no_candidate_word_correction;
	}
	
	public boolean isProxy_enable() {
		return proxy_enable;
	}

	public String getProxy_host() {
		return proxy_host;
	}

	public String getProxy_port() {
		return proxy_port;
	}

	private void loadProperties(){
		try{
			Properties props = new Properties();
			props.load(new FileReader(property_fileName));
			
			sentence_window_size = Integer.parseInt(props.getProperty("sentence_context_size"));
			generation_reduction_factor = Double.parseDouble(props.getProperty("generation_reduction_factor"));
			no_candidate_word_correction = Integer.parseInt(props.getProperty("no_candidate"));

			proxy_enable = Boolean.parseBoolean(props.getProperty("proxy_enable"));
			proxy_host = props.getProperty("proxy_host").trim();
			proxy_port = props.getProperty("proxy_port").trim();
			
		}catch(Exception e){
			OogleLogger.logger.info("Error Loading Properites. Using Default Values.");
		}
	}
}
