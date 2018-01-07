package ie.gmit.sw;

import com.db4o.*;
import com.db4o.config.*;
import com.db4o.query.*;
import com.db4o.ta.*;
import xtea_db4o.XTEA;
import xtea_db4o.XTeaEncryptionStorage;

import java.util.*;

import static java.lang.System.*;

import java.io.File;
import java.io.IOException;

public class DocDBRunner {
	private ObjectContainer db = null;
	private List<Document> dList = new ArrayList<Document>();
	private Shinglator s;
	private List<Shingle> sList = new ArrayList<Shingle>();
	private Document doc;
	
	public DocDBRunner() throws IOException {
		init(); //Populate the customers collection
		
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		config.common().add(new TransparentActivationSupport()); //Real lazy. Saves all the config commented out below
		config.common().add(new TransparentPersistenceSupport()); //Lazier still. Saves all the config commented out below
		config.common().updateDepth(7); //Propagate updates
		
		//Use the XTea lib for encryption. The basic Db4O container only has a Caesar cypher... Dicas quod non est ita!
		config.file().storage(new XTeaEncryptionStorage("password", XTEA.ITERATIONS64));

		//Open a local database. Use Db4o.openServer(config, server, port) for full client / server
		db = Db4oEmbedded.openFile(config, "documents.data");
		//add files to database
		addDocumentsToDatabase();
		//showfiles in database
		showDocuments();
		
	
	}

	private void init() throws IOException {
		int i = 0;
		// find directory with existing files
		File dir = new File("textFiles/");
		//loop through folder and make file into shingles and document object and save to database
		for (File file : dir.listFiles()) {
	   	   i++;
		   s = new Shinglator(file, "r"+i);
		   sList = s.createShingles();
		   doc = new Document("r"+i, file.getName(), sList);
		   //save this
		   dList.add(doc);
		}
		
	}
	
	private void addDocumentsToDatabase(){
		for(Document d: dList)
		{
			db.store(d);//adds document objects to database
		}
		db.commit();//commits the transaction
	}
	
	private void showDocuments()
	{
		//An ObjectSet is a specialised List for storing results
		ObjectSet<Document> documents = db.query(Document.class);
		for (Document document : documents) {
			out.println("[Document] " + document.getName() + "\t ***Database ObjID: " + db.ext().getID(document));

			//Removing objects from the database is as easy as adding them
			//db.delete(customer);
			db.commit();
		}
	}
	public static void main(String[] args) throws IOException
	{
		new DocDBRunner();
	}
	
	
}
	
	


