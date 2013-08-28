package sm.nlp.oogle.test;

import java.util.List;

import sm.nlp.oogle.datastore.SpellingCorrectionCandidate;
import sm.nlp.oogle.phrase_correction.PhraseCorrector;
import sm.nlp.oogle.word_correction.SpellingCorrector;

public class TestSentenceCorrection {
	public static void main(String[] args) {
		PhraseCorrector phraser = PhraseCorrector.phrase;
		SpellingCorrector corrector = SpellingCorrector.corrector;
		System.out.println(phraser.findPhraseCorrection("figt back"));
		System.out.println(phraser.findSentenceCorrection("I am so lonly"));
		
		List<SpellingCorrectionCandidate> list = corrector.correct("hekko");
		for(SpellingCorrectionCandidate candidate : list){
			System.out.println(candidate.getWord());
		}
	
	}
}
