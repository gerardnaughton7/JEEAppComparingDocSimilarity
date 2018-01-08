package ie.gmit.sw;

public class Result {
	
	//declare variables
	private String NewDoc;
	private String oldDoc;
	private double result;
	//constructor
	public Result(String newDoc, String oldDoc, double result) {
		super();
		NewDoc = newDoc;
		this.oldDoc = oldDoc;
		this.result = result;
	}
	
	//getters and setters
	public String getNewDoc() {
		return NewDoc;
	}
	public void setNewDoc(String newDoc) {
		NewDoc = newDoc;
	}
	public String getOldDoc() {
		return oldDoc;
	}
	public void setOldDoc(String oldDoc) {
		this.oldDoc = oldDoc;
	}
	public double getResult() {
		return result;
	}
	public void setResult(double result) {
		this.result = result;
	}
	@Override
	public boolean equals(Object obj) {
		if((String) obj == this.getNewDoc())
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	

}
