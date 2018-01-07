package ie.gmit.sw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;
import com.db4o.query.Query;
import com.db4o.ta.TransparentActivationSupport;
import com.db4o.ta.TransparentPersistenceSupport;

import xtea_db4o.XTEA;
import xtea_db4o.XTeaEncryptionStorage;

public class Worker extends Thread{
	private BlockingQueue<Job> inQueue = new ArrayBlockingQueue<Job>(10);
	private BlockingQueue<Job> outQueue = new ArrayBlockingQueue<Job>(10);
	private Job j;
	private ObjectContainer db = null;
	private List<Document> dList = new ArrayList<Document>();
	
	//constructor with args inqueue and outqueue
	public Worker(BlockingQueue<Job> inQueue, BlockingQueue<Job> outQueue) {
		super();
		this.inQueue = inQueue;
		this.outQueue = outQueue;
	}
	
	//run method which runs our jobs
		public void run() {
			//keeps running
			while(true) {
				//checks queue every 10 second
				j = inQueue.poll();
				
				//if j is null do nothing till job is available
				if(j != null)
				{
			
					DocDBRunner db;
					try {
						db = new DocDBRunner();
						System.out.println("in worker show docs");
						db.showDocuments();
						db.addDocumentsToDatabase(j.getDoc());
						db.showDocuments();
						//dList = db.getDocuments();
						//System.out.println(dList.size());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// compare document to other documents in database
					
					// return result to user outqueue
					outQueue.offer (j); 
					
				}
			}
		}
		
}
