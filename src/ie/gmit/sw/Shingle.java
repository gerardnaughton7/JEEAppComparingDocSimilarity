package ie.gmit.sw;

public class Shingle {
	
	private String docID;
	private int hashCode;
	
	public Shingle(String docID, int hashCode) {
		super();
		this.docID = docID;
		this.hashCode = hashCode;
	}

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
