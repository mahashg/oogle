package sm.nlp.oogle.phrase_correction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import sm.nlp.oogle.datastore.ApplicationPropertyStore;
import sm.nlp.oogle.datastore.Candidate;
import sm.nlp.oogle.datastore.DataStore;
import sm.nlp.oogle.datastore.SpellingCorrectionCandidate;
import sm.nlp.oogle.word_correction.SpellingCorrector;

public class PhraseCorrector {
	
	private SpellingCorrector spell = SpellingCorrector.corrector;
	private DataStore datastore = DataStore.dataStore;
	private NGramServiceManager ngramManager = NGramServiceManager.manager;
	private final String whitespace = "[ |\t]+";
	
	public final static PhraseCorrector phrase = new PhraseCorrector();
	private PhraseCorrector() {
		
	}
	
	public String findPhraseCorrection(String s){
		String[] words = s.toLowerCase().split(whitespace);
		List<Candidate> priorlist = new ArrayList<Candidate>();
		
		int errorindex = findErrorIndex(words);		
		if(errorindex == -1) {			
			return s;
		}
		
		String prefix = "";
		for(int i=0;i<errorindex;i++){
			prefix=prefix+words[i]+" ";
		}
		
		String postfix = "";
		for(int i=errorindex+1 ; i<words.length ; ++i){
			postfix += words[i]+" ";
		}
		
		List<SpellingCorrectionCandidate> corrections=spell.correct(words[errorindex]);
		// All possible phrases
		List<String> phrase = new ArrayList<String>();
		//double[] likelyhood = new double[corrections.size()];
		List<Double> likelyhood = new ArrayList<Double>();
		//int j=0;
		
		for(SpellingCorrectionCandidate c:corrections) {

			likelyhood.add(c.getNormalized_probability());
			phrase.add((prefix + c.getWord() + " "+postfix).trim());
			//j++;
		}
				
		List<Double> prob = ngramManager.getPhraseProbability(phrase);
		
		for(int i=0;i<phrase.size();i++) {
			priorlist.add(new Candidate(phrase.get(i), 
					(likelyhood.get(i) * Math.pow(10, prob.get(i))) ));
		}
		
		Collections.sort(priorlist);
		Collections.reverse(priorlist);
		
		return priorlist.get(0).getWord();
	}

	public String findSentenceCorrection(String s) {
		s = s.toLowerCase().replaceAll("[^a-zA-Z \t0-9]", "");
		String words[]=s.split(whitespace);
		int errorindex=findErrorIndex(words);
		
		int contextsize = ApplicationPropertyStore.propStore.getSentence_window_size();
		while(errorindex!=-1) {
			String prefix="";
			int i = errorindex-1, j=0;
			while(i>=0 && j<contextsize ) {
				prefix=prefix+words[i]+" ";
				i--;
				j++;
			}
			
			int errorplace=j;
			String postfix = "";
			i=errorindex+1;
			j=0;
			while(i<words.length && j<contextsize ) {
				postfix += words[i]+" ";
				i--;
				j++;
			}
			
			String error=findPhraseCorrection(prefix+words[errorindex]+" "+postfix).trim();
			words[errorindex]=error.split(whitespace)[errorplace];
			errorindex=findErrorIndex(words);
		}
		
		StringBuilder sb = new StringBuilder();
		for(String str:words){
			sb.append(str+" ");
		}
		
		return sb.toString().trim();
	}
	
	private int findErrorIndex(String[] phrase) {
		
		for(int i=0;i<phrase.length;i++) {
			if(!datastore.contains(phrase[i])) {	
				return i;
			}
		}
		return -1;
	}	
}
