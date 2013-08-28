package sm.nlp.oogle.logger;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class OogleLogger {

	public static final Logger logger = Logger.getLogger(OogleLogger.class.getCanonicalName());

	static {
		try{
		logger.setLevel(Level.INFO);
		FileHandler fHandler = new FileHandler("oogle.log");
		fHandler.setFormatter(new SimpleFormatter());
		logger.addHandler(fHandler);
		}catch(Exception e){
			System.err.println("Error could not initialize the logger");
		}
	}
}
