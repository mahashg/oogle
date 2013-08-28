package sm.nlp.oogle.test;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import sm.nlp.oogle.datastore.DataStore;

public class TestSerialization {
	public static void main(String[] args) throws Exception {
		DataStore datastore = DataStore.dataStore;
		String fileName ="datastore.obj"; 
		writeObject(fileName, datastore);
		
		DataStore ds = (DataStore)readObject(fileName);
		
		System.out.println(datastore.getTotal_words());
		System.out.println(ds.getTotal_words());
		
		System.out.println(datastore.confusion.getInsertion('a', 'e'));
		System.out.println(ds.confusion.getInsertion('a', 'e'));
		
		System.out.println(ds.contains("resolution"));
		/*System.out.println(ds.getInsertionCandidateFor("rain", 'i', 'j').getProbability());*/
	}

	private static Object readObject(String fileName) throws Exception{
		ObjectInputStream ois = new ObjectInputStream(new FileInputStream(fileName));
	
		return ois.readObject();
	}

	private static void writeObject(String fileName, Object obj) throws IOException {

		ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(fileName));
		
		oos.writeObject(obj);
		
		oos.close();		
	}
}
