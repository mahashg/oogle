package sm.nlp.oogle.datastore;


public class SpellingCorrectionCandidate {
	private String word;
	private double probability;
	private double normalized_probability;

	public SpellingCorrectionCandidate(String word, double probability) {
	
		this.word = word;
		this.probability = probability;
		this.normalized_probability = probability;
	}
	
	public String getWord() {
		return word;
	}
	
	public void setWord(String word) {
		this.word = word;
	}
	
	public double getPrior() {	
		return this.probability;
	}
	
	public void setPrior(double d) {
		this.probability = d;
		
	}

	public double getProbability(){
		return this.probability * 
		(DataStore.dataStore.getWordCount(word)*1.0/DataStore.dataStore.getTotal_words());
	}
	
	public double getNormalized_probability() {
		return this.normalized_probability;
	}

	public void setNormalized_probability(double normalized_probability) {
		this.normalized_probability = normalized_probability;
	}	
}