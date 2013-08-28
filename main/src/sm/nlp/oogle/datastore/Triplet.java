package sm.nlp.oogle.datastore;

import java.io.Serializable;

public class Triplet implements Serializable {
	
	private static final long serialVersionUID = 3729226206291090776L;
	
	private char key1;
	private char key2;
	private int count;
	
	public Triplet(char key1, char key2){
		this.key1 = key1;
		this.key2 = key2;
		this.count = 1;
	}
	
	public char getKey1(){ return this.key1; }
	public char getKey2(){ return this.key2; }
	
	public int getCount() { return this.count; }
	public void incrementCount(){ ++this.count; }

	public void incrementCountByValue(int countValue) {
		this.count += countValue;		
	}

	public boolean matches(char key11, char key12) {
		if(key1 == key11){
			return key2== key12;
		}else if(key1 == key12){
			return key2 == key11;
		}
		return false;
	}
}