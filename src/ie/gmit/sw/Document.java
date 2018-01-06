package ie.gmit.sw;

import java.util.List;

public class Document {
	private List shinglelist;
	private String docID;
	private String name;
	
	public Document(String docID, String name, List shinglelist) {
		super();
		this.docID = docID;
		this.name =  name;
		this.shinglelist = shinglelist;
	}

	public List getShinglelist() {
		return shinglelist;
	}

	public void setShinglelist(List shinglelist) {
		this.shinglelist = shinglelist;
	}

	public String getDocID() {
		return docID;
	}

	public void setDocID(String docID) {
		this.docID = docID;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}
