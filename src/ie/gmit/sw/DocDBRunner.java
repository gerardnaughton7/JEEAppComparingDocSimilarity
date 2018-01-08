package ie.gmit.sw;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.ta.TransparentActivationSupport;
import com.db4o.ta.TransparentPersistenceSupport;
import xtea_db4o.XTEA;
import xtea_db4o.XTeaEncryptionStorage;



public class DocDBRunner {
	//declare variables
	private ObjectContainer db = null;
	private List<Document> dList = new ArrayList<Document>();
	private Shinglator s;
	private List<Shingle> sList = new ArrayList<Shingle>();
	private Document doc;
	
	//constructoe
	public DocDBRunner() throws IOException {
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		config.common().add(new TransparentActivationSupport()); //Configuration
		config.common().add(new TransparentPersistenceSupport()); //Configuration
		config.common().updateDepth(7); //Propagate updates
		
		//Use the XTea lib for encryption. 
		config.file().storage(new XTeaEncryptionStorage("password", XTEA.ITERATIONS64));

		//Open a local database. 
		db = Db4oEmbedded.openFile(config, "C:/Users/gerar/Desktop/JEEAppComparingDocSimilarity/CompareDocs/WebContent/documents.data");
		dList = getDocuments();
		
		if(dList.size() == 0) {
			init(); //Populate files in directory and put into db4o
		}
		
		//showDocuments(); used to check if documents were being added
	
	}

	// save the text as shingles and store as documents in db40
	private void init() throws IOException {
		int i = 0;
		// find directory with existing files
		File dir = new File("C:/Users/gerar/Desktop/JEEAppComparingDocSimilarity/CompareDocs/TextFiles");
		//loop through folder and make file into shingles and document object and save to database
		for (File file : dir.listFiles()) {
	   	   i++;
		   s = new Shinglator(file, "r"+i);
		   sList = s.createShingles();
		   doc = new Document("r"+i, file.getName(), sList);
		   //add doc to list
		   dList.add(doc);
		}
		addFilesToDatabase();
		
	}
	
	//adds files to database upon init
	private void addFilesToDatabase(){
		for(Document d: dList)
		{
			db.store(d);//adds document objects to database
		}
		db.commit();//commits the transaction
	}
	
	//add one document
	public void addDocumentsToDatabase(Document d) {
		db.store(d);
		
		db.commit();	
	}
	
	//shows documents function to help check if docs were being saved
	private void showDocuments()
	{
		//An ObjectSet is a specialised List for storing results
		ObjectSet<Document> documents = db.query(Document.class);
		for (Document document : documents) {
			System.out.println("[Document] " + document.getName() + "\t ***Database ObjID: " + db.ext().getID(document));

			//Removing objects from the database is as easy as adding them just using to clear some docs
			//db.delete(document);
			db.commit();
		}
	}
	
	// close db
	public void closeDB()
	{
		db.close();
	}
	
	// get list of documents in batabase
	public List getDocuments()
	{
		List<Document> temp = new ArrayList<Document>();
		ObjectSet<Document> documents = db.query(Document.class);
		for (Document document : documents) {
			temp.add(document);
			db.commit();
		}
		return temp;
		
	}
	
	//main method
	public static void main(String[] args) throws IOException
	{
		new DocDBRunner();
	}
	
}
	
	


