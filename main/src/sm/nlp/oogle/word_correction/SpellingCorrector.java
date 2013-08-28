package sm.nlp.oogle.word_correction;

import java.util.ArrayList;
import java.util.List;

import sm.nlp.oogle.datastore.ApplicationPropertyStore;
import sm.nlp.oogle.datastore.DataStore;
import sm.nlp.oogle.datastore.SpellingCorrectionCandidate;

public class SpellingCorrector {

	public static final SpellingCorrector corrector = new SpellingCorrector();	
	private DataStore datastore = DataStore.dataStore;
	
	private SpellingCorrector() {
		
	}

	// generate possible candidate correction & their probability
	public final List<SpellingCorrectionCandidate> edits(String word) {
		
		List<SpellingCorrectionCandidate> list = new ArrayList<SpellingCorrectionCandidate>();
		
		for(int i=1; i < word.length(); ++i){
			// removal candidate
			char X = word.charAt(i-1);
			char Y = word.charAt(i);
			
			//XY -> X
			String candidate = word.substring(0, i) + word.substring(i+1);
			 
			SpellingCorrectionCandidate corr = datastore.getRemovalCandidaeFor(candidate, X, Y);			
			
			addToList(list, corr);
		}
		
		for(int i=0; i < word.length()-1; ++i){
			// transpose candidate
			char ch1 = word.charAt(i);
			char ch2 = word.charAt(i+1);
			
			String candidate = word.substring(0, i) + word.substring(i+1, i+2) + word.substring(i, i+1) + word.substring(i+2);
			SpellingCorrectionCandidate corr = datastore.getTransposeCandidateFor(candidate, ch1, ch2);
						
			addToList(list, corr);
		}
		
		for(int i=0; i < word.length(); ++i){
			for(char c='a'; c <= 'z'; ++c){
				// substitution correction
				char ch1 = c;
				char ch2 = word.charAt(i);
				
				String candidate = word.substring(0, i) + String.valueOf(c) + word.substring(i+1);				
				SpellingCorrectionCandidate corr = datastore.getSubstitutionCandidateFor(candidate, ch1, ch2);
				
				addToList(list, corr);
			}
		}
		
		for(int i=0; i < word.length(); ++i){
			char ch2 = word.charAt(i);
			
			for(char c='a'; c <= 'z'; ++c){
				// addition correction
				char ch1 = c;
				
				String candidate = word.substring(0, i+1) + String.valueOf(c) + word.substring(i+1);
				SpellingCorrectionCandidate corr = datastore.getInsertionCandidateFor(candidate, ch1, ch2);
			
				addToList(list, corr);
			}
		}
		
		return list;
	}
	
	
	public final List<SpellingCorrectionCandidate> correct(String word) {
		
		List<SpellingCorrectionCandidate> candidate = new ArrayList<SpellingCorrectionCandidate>();
		
		if(datastore.contains(word)){
			candidate.add(new SpellingCorrectionCandidate(word, 1.0));
			return candidate;
		}
		
		// edit distance 1
		List<SpellingCorrectionCandidate> list = edits(word);	// find list of candiates		
		
		List<SpellingCorrectionCandidate> candidates = new ArrayList<SpellingCorrectionCandidate>();
		
		double total_gen = 0;
		for(SpellingCorrectionCandidate s : list){
			double gen = getGenerativeCountFor(s.getWord());
			total_gen += gen;
		}
		
		
		total_gen = (total_gen / list.size())*ApplicationPropertyStore.propStore.getGeneration_reduction_factor();
		
		// edit distance 2
		for(SpellingCorrectionCandidate s : list){	// for each candidate
			
			double gen = getGenerativeCountFor(s.getWord());
			if(gen < total_gen){
				continue;
			}

			List<SpellingCorrectionCandidate> currentCandidate = edits(s.getWord());

			for(SpellingCorrectionCandidate tempCandidate : currentCandidate){
				
				tempCandidate.setPrior(tempCandidate.getPrior()*s.getPrior());
				addToList(candidates, tempCandidate);				
			}
		}

		for(SpellingCorrectionCandidate currentCandidate : candidates){
			addToList(list, currentCandidate);
		}
		
		// filter the candidate
		candidates = filter(list); // map contains better candidates				
		
		// normalize the probability value
		normalizeCandidateList(candidates);

		// sort the candidates
		candidates = sort(candidates);
		
		// pick top solutions
		int total_no_of_solutions = 20;
		for(int i=0 ; i< Math.min(total_no_of_solutions, candidates.size()) ; ++i){
			candidate.add(candidates.get(i));		
		}
		
		if(candidate.isEmpty()){
			
			String str = "";
			List<String> splitArr = splitbyWhiteSpace(word);
			
			if(splitArr != null){
				splitArr = mergeSplits(splitArr);
			
				for(String temp : splitArr){
					str = str + " " + temp;
				}
			}
			
			if(str.isEmpty()) {str = word;}
				candidate.add(new SpellingCorrectionCandidate(str, 0.0));
		}
		
		int no = ApplicationPropertyStore.propStore.getNo_candidate_word_correction();
		//System.out.println(no);
		if(no <= 0 || candidate.size() <= no){
			return candidate;	
		}
		else {
			while(candidate.size() > no){				
				candidate.remove(no);
			}
			return candidate;
		}
	}
	
	public double getGenerativeCountFor(String str){
		if(str == null || str.isEmpty()){	return 0.0d;}
		
		double countValue = 1;
		
		for(int i=0 ; i<str.length()-1 ; ++i){
			countValue = countValue + Math.log10(this.datastore.getCharOccurence(str.charAt(i), str.charAt(i+1)));
		}
		
		double gen = countValue *1.0/ str.length();
		return gen;
	}

	// we have the list of strings each are different
	// try merging neighbouring candidates and see
	// if the merging produces correct word.
	// if it does prefer it over the existing
	private List<String> mergeSplits(List<String> splitArr) {
		for(int cluster_size = 1; cluster_size <splitArr.size() ; ++cluster_size){
			
			for(int i=0 ; i<(splitArr.size()-cluster_size) ; ++i){
				String str="";
				for(int j=0 ; j<=cluster_size ; ++j){
					str += splitArr.get(i+j);
				}
				
				if(corrector.datastore.contains(str)){
					for(int j=0 ; j<=cluster_size ; ++j){
						splitArr.remove(i);
					}
					splitArr.add(i, str);
					i = i-1;
				}
			}
		}
		
		return splitArr;
	}

	// given word may contain various words merged.
	// from the start just split it as
	// <word1> <word2> such that word1 is in dictionary
	// recursively divide word2 such that each of its subpart has meaning
	// if it is not possible then word1 splitting is incorrect.
	// then increase the window size for word1 and try again
	private List<String> splitbyWhiteSpace(String word) {

		if(word == null || word.isEmpty()){
			return new ArrayList<String>();
		}

		StringBuilder sb = new StringBuilder();
		for(int i=0 ; i<word.length() ; ++i){
			sb.append(word.charAt(i));
			
			if(this.datastore.contains(sb.toString())){			
				List<String> next = splitbyWhiteSpace(word.substring(i+1));
				if(next != null){
					next.add(0, sb.toString());
					return next;
				}
			}
		}
		
		return null;
	}
	
	// sort the given spelling correction candidates based on their probability values.
	private List<SpellingCorrectionCandidate> sort(
			List<SpellingCorrectionCandidate> candidates) {


		for(int i=0 ; i<candidates.size() ; ++i){
			for(int j=i+1 ; j<candidates.size() ; ++j){
				if(candidates.get(j).getProbability() > candidates.get(i).getProbability()){
					SpellingCorrectionCandidate temp = candidates.get(i);
					candidates.set(i, candidates.get(j));
					candidates.set(j, temp);
				}
			}
		}
		
		
		return candidates;
	}

	// normalize the candidate list
	// necessay in case large no of candidates and the probability underflow might occur
	private void normalizeCandidateList(
			List<SpellingCorrectionCandidate> candidates) {
		double total_probability = 0.0;
		
		for(SpellingCorrectionCandidate current : candidates){
			total_probability += current.getProbability();			
		}
		
		for(SpellingCorrectionCandidate current : candidates){
			current.setNormalized_probability(current.getProbability() / total_probability);			
		}
		
	}

	// from given list remove those candidates which are not
	// the dictionary words.
	public List<SpellingCorrectionCandidate> filter (List<SpellingCorrectionCandidate> list){
		if(list == null){
			return null;
		}
		
		List<SpellingCorrectionCandidate> newMap = new ArrayList<SpellingCorrectionCandidate>();
		if(list.isEmpty()){
			return newMap;
		}
		
		for(SpellingCorrectionCandidate s : list){
			if(this.datastore.contains(s.getWord())){
				newMap.add(s);
			}
		}
		
		return newMap;
	}
	
	// add new SpellingCorrectionCandidate to the list
	// if the same word already in the list
	// then don't create new entry only
	// add their probability together
	private void addToList(List<SpellingCorrectionCandidate> list,
			SpellingCorrectionCandidate corr) {
		
		
		for(SpellingCorrectionCandidate candidate : list){
			if(candidate.getPrior() == 0.0){
				continue;
			}

			if(candidate.getWord().equals(corr.getWord())){
				
				candidate.setPrior(candidate.getPrior()+corr.getPrior());
				return;
			}
		}
		
		list.add(corr);		
	}
}
