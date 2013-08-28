package sm.nlp.oogle.test;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import sm.nlp.oogle.datastore.SpellingCorrectionCandidate;
import sm.nlp.oogle.logger.OogleLogger;
import sm.nlp.oogle.main.OogleCorrector;

public class TestFileInputReader {
	private static Scanner reader;
	private static BufferedWriter writer;
	
	private static final String phraseFileName = "phrases.tsv";
	private static final String wordFileName = "words.tsv";
	private static final String sentenceFileName = "sentences.tsv";
	
	private static Logger logger = OogleLogger.logger;
	
	public static void main(String[] args) throws IOException {
		logger.info("Initializing the system..");
		OogleCorrector corrector = OogleCorrector.corrector;
		String line = null;
		
		logger.info("Word Correction Started..");
		initScanner(wordFileName);
		while((line = readNextLine()) != null){
			String correction = spellingCorrectorListToString(line, corrector.correctWord(line));
			writeNextLine(correction);
		}
		
		logger.info("Phrase Correction Started..");
		initScanner(phraseFileName);		
		while((line = readNextLine()) != null){
			String correction = corrector.correctPhrase(line);
			writeNextLine(line+"\t"+correction);
		}
		
		logger.info("Sentence Correction Started..");
		initScanner(sentenceFileName);
		while((line = readNextLine()) != null){
			String correction = corrector.correctSentence(line);
			writeNextLine(line+"\t"+correction);
		}
	
		if(writer != null){
			writer.flush();
			writer.close();
		}
		logger.info("End of Processing");
	}
	
	private static String spellingCorrectorListToString(
			String word, List<SpellingCorrectionCandidate> correctWord) {

		StringBuilder sb = new StringBuilder();
		sb.append(word+"\t");
		
		for(SpellingCorrectionCandidate candidate: correctWord){
			sb.append(candidate.getWord()+"\t"+candidate.getNormalized_probability()+"\t");
		}
		return sb.toString().trim();
	}

	private static void initScanner(String fileName){
		 try{
			 reader = new Scanner(new File(fileName));
			 if(writer != null){
				 writer.flush();
				 writer.close();
			 }
			 writer = new BufferedWriter(new FileWriter(fileName.replace("tsv", "out")));
		 }catch(Exception e){
			 System.err.print("Error While reading input file");
		 }
	}
	
	public static String readNextLine(){
				
		if(reader == null){
			return null;
		}
		
		if(reader.hasNext()){
			return reader.nextLine();
		}
		return null;
	}
	
	public static void writeNextLine(String line){
		try {
			writer.write(line+"\n");
		} catch (IOException e) {
			System.err.println("Error while writing the output");
		}
	}
}
