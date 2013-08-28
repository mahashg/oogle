package sm.nlp.oogle.datastore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class TripletCollection implements Serializable{
	
	private static final long serialVersionUID = 972620961468151217L;
	
	private List<Triplet> list;
	
	public TripletCollection() {
		this.list = new ArrayList<Triplet>();
	}
	
	public void addCount(char key1, char key2, int countValue) {
		for(Triplet trp : list){
			if(trp.matches(key1, key2)){
				trp.incrementCountByValue(countValue);
				return;
			}
		}
		
		list.add(new Triplet(key1, key2));
		
	}

	public void printMatrix() {
		for(Triplet tp : list){
			System.out.println("("+tp.getKey1()+", "+tp.getKey2()+")\t"+tp.getCount());
		}		
	}

	public int get(char ch1, char ch2) {
		for(Triplet tp : this.list){
			if(tp.matches(ch1, ch2)){
				return tp.getCount();
			}
		}
		return 1;
	}
}