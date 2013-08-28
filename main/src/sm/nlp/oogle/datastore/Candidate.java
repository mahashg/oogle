package sm.nlp.oogle.datastore;

public class Candidate implements Comparable<Candidate>
{
	private String word;
	private double probability;
	
	public Candidate(String s,double p) {
		word=s;
		probability=p;
	}
	
	public String getWord() {
		return word;
	}

	public void setWord(String word) {
		this.word = word;
	}

	public double getProbability() {
		return probability;
	}

	public void setProbability(double probability) {
		this.probability = probability;
	}

	@Override
	public int compareTo(Candidate c) {

		if(this.probability > c.probability){
			return 1;
		} else if(this.probability == c.probability){
			return 0;
		} else{
			return -1;
		}
	}
}
