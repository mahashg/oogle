package sm.nlp.oogle.main;

import java.util.List;

import sm.nlp.oogle.datastore.SpellingCorrectionCandidate;
import sm.nlp.oogle.logger.OogleLogger;
import sm.nlp.oogle.phrase_correction.PhraseCorrector;
import sm.nlp.oogle.word_correction.SpellingCorrector;

public class OogleCorrector {
	private final PhraseCorrector phraseCorrector = PhraseCorrector.phrase;
	private final SpellingCorrector wordCorrector = SpellingCorrector.corrector;
	
	public final static OogleCorrector corrector = new OogleCorrector();
	
	private OogleCorrector() {
		
	}
	
	public List<SpellingCorrectionCandidate> correctWord(String word){
		try{
			return wordCorrector.correct(word);
		}catch(Exception e){
			OogleLogger.logger.info(e.getMessage()+" error occured during correction of "+word+" word.");
		}
		return null;
	}
	
	public String correctPhrase(String phrase){
		try{
			return phraseCorrector.findPhraseCorrection(phrase);
		}catch(Exception e){
			OogleLogger.logger.info(e.getMessage()+" error occured during correction of "+phrase+" phrase.");
		}
		return null;
	}
	
	public String correctSentence(String sentence){
		try{
			return phraseCorrector.findSentenceCorrection(sentence);
		}catch(Exception e){
			OogleLogger.logger.info(e.getMessage()+" error occured during correction of "+sentence+" sentence.");
			e.printStackTrace();
		}
		return null;
	}
}
