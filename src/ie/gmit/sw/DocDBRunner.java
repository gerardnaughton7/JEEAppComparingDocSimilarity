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



public class DocDBRunner implements DatabaseInterface {
	private ObjectContainer db = null;
	private List<Document> dList = new ArrayList<Document>();
	private Shinglator s;
	private List<Shingle> sList = new ArrayList<Shingle>();
	private Document doc;
	
	public DocDBRunner() throws IOException {
		System.out.println("in db40");
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		config.common().add(new TransparentActivationSupport()); //Real lazy. Saves all the config commented out below
		config.common().add(new TransparentPersistenceSupport()); //Lazier still. Saves all the config commented out below
		config.common().updateDepth(7); //Propagate updates
		
		//Use the XTea lib for encryption. The basic Db4O container only has a Caesar cypher... Dicas quod non est ita!
		config.file().storage(new XTeaEncryptionStorage("password", XTEA.ITERATIONS64));

		//Open a local database. Use Db4o.openServer(config, server, port) for full client / server
		db = Db4oEmbedded.openFile(config, "documents.data");
		dList = getDocuments();
		
		if(dList.size() == 0) {
			init(); //Populate files in directory and put into db4o
		}
		
		showDocuments();
		
	
	}

	private void init() throws IOException {
		System.out.println("in init");
		int i = 0;
		// find directory with existing files
		File dir = new File("C:/Users/gerar/Desktop/JEEAppComparingDocSimilarity/CompareDocs/TextFiles");
		//loop through folder and make file into shingles and document object and save to database
		for (File file : dir.listFiles()) {
	   	   i++;
		   s = new Shinglator(file, "r"+i);
		   sList = s.createShingles();
		   doc = new Document("r"+i, file.getName(), sList);
		   //save this
		   dList.add(doc);
		}
		addFilesToDatabase();
		
	}
	
	private void addFilesToDatabase(){
		for(Document d: dList)
		{
			db.store(d);//adds document objects to database
		}
		db.commit();//commits the transaction
	}
	
	public void addDocumentsToDatabase(Document d) {
		db.store(d);
		
		db.commit();	
	}
	
	public void showDocuments()
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
	
	public void closeDB()
	{
		db.close();
	}
	
	public List getDocuments()
	{
		List<Document> temp = new ArrayList<Document>();
		ObjectSet<Document> documents = db.query(Document.class);
		System.out.println("in get documents"+ documents.size());
		for (Document document : documents) {
			temp.add(document);
			db.commit();
		}
		return temp;
		
	}
	
	public static void main(String[] args) throws IOException
	{
		new DocDBRunner();
	}
	
}
	
	


