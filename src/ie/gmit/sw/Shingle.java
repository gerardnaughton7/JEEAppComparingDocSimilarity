package ie.gmit.sw;

/**
 * Shingle object which is made up of a docID and a hashCode
 * 
 * @author Gerard Naughton
 *
 */
public class Shingle {
	//declare variables
	private String docID;
	private int hashCode;
	
	//constructor
	public Shingle(String docID, int hashCode) {
		super();
		this.docID = docID;
		this.hashCode = hashCode;
	}
	
	//getters and setters
	public String getDocID() {
		return docID;
	}

	public void setDocID(String docID) {
		this.docID = docID;
	}

	public int getHashCode() {
		return hashCode;
	}

	public void setHashCode(int hashCode) {
		this.hashCode = hashCode;
	}
	
}
