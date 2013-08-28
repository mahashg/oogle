package sm.nlp.oogle.datastore;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.Serializable;

public class ConfusionMatrix implements Serializable{

	private static final long serialVersionUID = 5247603297527569847L;

	public static final ConfusionMatrix confusion = new ConfusionMatrix();
	private int[][] insertionMatrix;
	private int[][] deletionMatrix;
	private int[][] substitutionMatrix;
	private int[][] transposeMatrix;

	private final String basepath = "resource//confusion//";

	private ConfusionMatrix() {
		readInsertionMatrix();
		readDeletionMatrix();
		readTransposeMatrix();
		readSubstitutionMatrix();
	}

	public int getInsertion(char ch1, char ch2){
		int val1 = ch1 - (int)'a';
		int val2 = ch2 - (int)'a';

		return (insertionMatrix[val1][val2] != 0 ? insertionMatrix[val1][val2] : 1);
	}

	public int getDeletion(char ch1, char ch2){
		int val1 = ch1 - (int)'a';
		int val2 = ch2 - (int)'a';

		return (deletionMatrix[val1][val2] != 0 ? deletionMatrix[val1][val2] : 1);
	}

	public int getSubstitution(char ch1, char ch2){
		int val1 = ch1 - (int)'a';
		int val2 = ch2 - (int)'a';

		return (substitutionMatrix[val1][val2] != 0 ? substitutionMatrix[val1][val2] : 1);
	}

	public int getTransposition(char ch1, char ch2){
		int val1 = ch1 - (int)'a';
		int val2 = ch2 - (int)'a';

		return (transposeMatrix[val1][val2] != 0 ? transposeMatrix[val1][val2] : 1);
	}

	private int[][] readMatrixFromFile(String fileName){
		int[][] matrix = null;

		try{
			FileReader input=new FileReader(fileName);
			BufferedReader br=new BufferedReader(input);

			matrix = new int[26][];
			for(int i=0;i<26;i++) {
				String line=br.readLine();
				String[] entries = line.split(" ");
				matrix[i] = new int[entries.length];

				for(int j=0 ; j<matrix[i].length ; j++)
					matrix[i][j]=Integer.parseInt(entries[j]);
			}

		}catch(Exception e){
			e.printStackTrace();
		}
		return matrix;
	}

	private void readInsertionMatrix(){
		insertionMatrix = readMatrixFromFile(basepath +"insertion.txt");	
	}

	private void readDeletionMatrix(){
		deletionMatrix = readMatrixFromFile(basepath +"deletion.txt");
	}

	private void readTransposeMatrix(){
		transposeMatrix = readMatrixFromFile(basepath +"reversal.txt");
	}

	private void readSubstitutionMatrix(){
		substitutionMatrix = readMatrixFromFile(basepath +"substitution.txt");
	}
}