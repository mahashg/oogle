package sm.nlp.oogle.datastore;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashMap;

import sm.nlp.oogle.logger.OogleLogger;

public class DataStore implements Serializable{

	private static final long serialVersionUID = -8475444080542841948L;
	
	private long total_words = 0;
	
	private final HashMap<String, Integer> nWords = new HashMap<String, Integer>();	
	private final HashMap<Character, Integer> char1DMatrix = new HashMap<Character, Integer>();		
	private final TripletCollection char2DMatrix = new TripletCollection();
	
	public final ConfusionMatrix confusion = ConfusionMatrix.confusion;
	
	public static DataStore dataStore = new DataStore();
	
	private DataStore() {
		loadDataStoreFromFile();
	//	loadDataStoreFromObject();
	}
		
	private void loadDataStoreFromObject() {
		try{
			String fileName ="datastore.obj";
			ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
		
			dataStore = (DataStore)ois.readObject();
		}catch(Exception e){
			e.printStackTrace();
		}
	}

	private void loadDataStoreFromFile(){
		try {
			// this file loads words.
			loadFile("resource//words.txt");

			// this file loads statistics	
			loadFile("resource//big.txt");
			
		} catch (IOException e) {		
			OogleLogger.logger.info("Could not load data from file.");
			OogleLogger.logger.info(e.getMessage()+"Error occured.");
		}

	}
	
	private void loadFile(String file)throws IOException {
		BufferedReader in = new BufferedReader(new FileReader(file));

		String whitespace = "[ |\t]+";
		String wordPattern = "[a-zA-Z0-9]+";

		for(String temp = ""; temp != null; temp = in.readLine()){
			String[] wordArr = temp.toLowerCase().split(whitespace);
			for(String word : wordArr){				
				if((!word.matches(wordPattern)) ||
						(word.length() == 1 && !word.equals("a") && !word.equals("i"))){
					continue;
				}
				
				nWords.put(word, nWords.containsKey(word) ? nWords.get(word) + 1 : 1);
			}
		}
		in.close();

		update();
	}
	
	public long getTotal_words() {
		return total_words;
	}
	
	public boolean contains(String str){
		return nWords.containsKey(str);
	}
	
	// total number of times the word appears
	public int getWordCount(String word){
			
		Integer value = this.nWords.get(word.toLowerCase());
		if(value == null){ return 0;}
		return value.intValue();
	}
	
	// total number of times character appears
	public int getCharOccurence(char ch){
		Integer value = char1DMatrix.get(ch);
		if(value == null){
			return 1;
		}
		return (value.intValue() != 0 ? value.intValue() : 1);
	}
	
	// get number of times char1 appears immediately before char2
	public int getCharOccurence(char ch1, char ch2){
		return this.char2DMatrix.get(ch1, ch2);
	}

	// in given 'word' the probability that XY is replaced by X
	public SpellingCorrectionCandidate getRemovalCandidaeFor(String word, char X, char Y){
		if(word == null){
			return null;
		}

		int ourCharMatrix = getCharOccurence(X, Y);
		int confusionFactor=confusion.getDeletion(X, Y);
		
		//System.out.println(word+" "+fr+" "+ourCharMatrix+" "+confusionFactor);
		SpellingCorrectionCandidate candiate = new SpellingCorrectionCandidate(word, 
				(confusionFactor*1.0/ourCharMatrix));
				
		return candiate;
	}

	// in given 'word' the probability that XY is replaced by YX
	public SpellingCorrectionCandidate getTransposeCandidateFor(String word, char ch1, char ch2) {
		if(word == null){
			return null;
		}
		
		int ourCharMatrix = getCharOccurence(ch1, ch2);
		int confusionFactor=confusion.getTransposition(ch1, ch2);
		
		SpellingCorrectionCandidate candiate = new SpellingCorrectionCandidate(word, 
				(confusionFactor*1.0/ourCharMatrix));
				
		return candiate;		
	}

	// in given 'word' the probability that X is replaced by Y
	public SpellingCorrectionCandidate getSubstitutionCandidateFor(String word, char ch1, char ch2) {
		if(word == null){
			return null;
		}
		
		int ourCharMatrix = getCharOccurence(ch1, ch2);
		int confusionFactor=confusion.getSubstitution(ch1, ch2);
		
		SpellingCorrectionCandidate candiate = new SpellingCorrectionCandidate(word, 
				(confusionFactor*1.0/ourCharMatrix));
				
		return candiate;
		
	}

	// in given 'word' the probability that X is replaced by XY
	public SpellingCorrectionCandidate getInsertionCandidateFor(String word, char ch1, char ch2) {
		if(word == null){
			return null;
		}
		
		int ourCharMatrix = getCharOccurence(ch1, ch2);
		int confusionFactor = confusion.getInsertion(ch1, ch2);
		
		SpellingCorrectionCandidate candiate = new SpellingCorrectionCandidate(word, 
				(confusionFactor*1.0/ourCharMatrix));
				
		return candiate;		
	}
		
	private void update() {
		updateCountValues();
		update1DCharMatrix();
		update2DCharMatrix();
		
	}

	private void updateCountValues(){
		total_words = 0;
		for(String str : nWords.keySet()){
			total_words += nWords.get(str);
		}
	}
	
	private void update1DCharMatrix(){
		char1DMatrix.clear();
		for( char ch = 'a' ; ch <= 'z' ; ++ch){
			char1DMatrix.put(ch, 0);
		}
		
		for(String str : nWords.keySet()){
			int countValue = nWords.get(str);
			
			for(int i=0 ; i<str.length() ; ++i){
				char ch = str.charAt(i);
				if(Character.isLetter(ch))
					char1DMatrix.put(ch, char1DMatrix.get(ch) + countValue);
			}
		}
	}
	
	private void update2DCharMatrix(){
		for(char ch1 = 'a' ; ch1 <= 'z' ; ++ch1){
			for(char ch2 = 'a' ; ch2 <= 'z' ; ++ch2){
				char2DMatrix.addCount(ch1, ch2, 1);
			}
		}
		
		for(String str : nWords.keySet()){
			int countValue = nWords.get(str);
			
			for(int i=0 ; i<str.length()-1 ; ++i){
				char ch1 =  str.charAt(i);
				char ch2 = str.charAt(i+1);
				
				if(Character.isLetter(ch1) && Character.isLetter(ch2))
					char2DMatrix.addCount(ch1, ch2, countValue);
			}
		}
	}
	
	public void print(){
		System.out.println("Word Matrix:");
		for(String str : nWords.keySet()){
			System.out.println(str+"\t"+nWords.get(str));
		}
				
		System.out.println("2 D Matrix: ");
		char2DMatrix.printMatrix();
		

		System.out.println("1 D Matrix: ");
		for(char ch : char1DMatrix.keySet()){
			System.out.println(ch + "\t"+char1DMatrix.get(ch));
		}
		
		System.out.println("Total number of words == "+total_words);
	}
}
