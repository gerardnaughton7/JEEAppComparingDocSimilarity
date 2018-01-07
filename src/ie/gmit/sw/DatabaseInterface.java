package ie.gmit.sw;

import java.util.List;

public interface DatabaseInterface {

	public List getDocuments();
	
	public  void addDocumentsToDatabase(Document doc);
	
}
