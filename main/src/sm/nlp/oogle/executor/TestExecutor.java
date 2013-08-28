package sm.nlp.oogle.executor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import sm.nlp.oogle.datastore.SpellingCorrectionCandidate;
import sm.nlp.oogle.logger.OogleLogger;
import sm.nlp.oogle.main.OogleCorrector;

public class TestExecutor {
	private static Scanner reader;
	private static BufferedWriter writer;
	
	private static final String phraseFileName = "phrases.tsv";
	private static final String wordFileName = "words.tsv";
	private static final String sentenceFileName = "sentences.tsv";
	
	private static final String baseInputPath = "input//";
	private static final String baseOutputPath = "output//";
	private static Logger logger = OogleLogger.logger;
	
	public static void main(String[] args) throws IOException {
		logger.info("Initializing the system..");
		OogleCorrector corrector = OogleCorrector.corrector;
		String line = null;
		
		// Word Correction..
		
		/*
		logger.info("Word Correction Started..");
		initScanner(wordFileName);
		
		int total_candidate_size = 0;
		double total_match_probability = 0;
		int total_suggestion_size = 0;
		int total_match_size = 0;
		
		double recall_value=0;
		int no_of_queries = 0;
		
		while((line = readNextLine()) != null){
			String word = line.split("[ |\t]+")[0];
			WordResult wr = spellingCorrectorListToString(line.substring(word.length()).trim(), corrector.correctWord(word));
			
			writeNextLine(wr.Word);
						
			++no_of_queries;
			
			total_candidate_size += wr.candiate_size;
			total_suggestion_size += wr.suggestion_size;
			total_match_probability += wr.match_probabiliy;
			total_match_size += wr.match_size;
			
			recall_value += (wr.match_size*1.0 / wr.suggestion_size);
		}
		
		 
		double expected_precision = (total_match_probability / no_of_queries);	
		double expected_recall=(recall_value *1.0/ no_of_queries);

		double ef1 = 0.5 * ((1.0/expected_precision) + (1.0 / expected_recall));
		
		Logger matchWriter = Logger.getLogger("matchlogger");
		
		matchWriter.setLevel(Level.INFO);
		FileHandler fHandler = new FileHandler("match.log");
		fHandler.setFormatter(new SimpleFormatter());
		matchWriter.addHandler(fHandler);

		matchWriter.info("ef1: "+ef1);
		
		*/
		// Phrase Correction
		logger.info("Phrase Correction Started..");
		initScanner(phraseFileName);		
		while((line = readNextLine()) != null){
			String correction = corrector.correctPhrase(line);
			writeNextLine(line+"\t"+correction);
		}
		
		// Sentence Correction
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
	
	private static WordResult spellingCorrectorListToString(
			String line, List<SpellingCorrectionCandidate> correctWord) {

		/*String[] wordList = line.split("[ |\t]+");
		String word = wordList[0];
		*/
		String[] wordList = line.split("[ |\t]+");
		List<String> suggestion = new ArrayList<String>();
		for (int i=0 ; i<wordList.length ; ++i){
			suggestion.add(wordList[i]);
		}
		
		StringBuilder sb = new StringBuilder();
		sb.append(line+"\t");
		
		double matchPr=0.0;
		int matchCount=0;
		for(SpellingCorrectionCandidate candidate: correctWord){
			if(suggestion.contains(candidate.getWord())){
				matchPr += candidate.getNormalized_probability();
				++matchCount;
			}
			sb.append(candidate.getWord()+"\t"+candidate.getNormalized_probability()+"\t");
		}
		
		WordResult wr = new WordResult();
		wr.candiate_size = correctWord.size();
		wr.match_probabiliy = matchPr;
		wr.match_size = matchCount;
		wr.suggestion_size = suggestion.size();
		wr.Word = sb.toString().trim();
	
		
		//System.out.println("match size: "+matchCount);
		return wr;
	}

	private static void initScanner(String fileName){
		 try{
			 reader = new Scanner(new File(baseInputPath+fileName));
			 if(writer != null){
				 writer.flush();
				 writer.close();
			 }
			 writer = new BufferedWriter(new FileWriter(baseOutputPath+fileName.replace("tsv", "out")));
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
