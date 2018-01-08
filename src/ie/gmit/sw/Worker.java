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
	private BlockingQueue<Job> inQueue = new ArrayBlockingQueue<Job>(100);
	private BlockingQueue<List<Result>> outQueue = new ArrayBlockingQueue<List<Result>>(100);
	private Job j = null;
	private ObjectContainer db = null;
	private List<Document> dList = new ArrayList<Document>();
	private List<Result> rList = new ArrayList<Result>();
	private ComputeJaccard cj;
	
	//constructor with args inqueue and outqueue
	public Worker() {
		
	}
	
	//run method which runs our jobs
		public void run() {
			//keeps running
			while(true) {
				//checks queue every 10 second
				inQueue = Global.getInQueue();
				j = inQueue.poll();
				//System.out.println("in worker checking for job"+ j.getDoc());
				//if j is null do nothing till job is available
				if(j != null)
				{
			
					DocDBRunner db;
					try {
						db = new DocDBRunner();
						//db.addDocumentsToDatabase(j.getDoc());
						dList = db.getDocuments();
						cj = new ComputeJaccard(dList,j.getDoc());
						rList = cj.Compute();
						db.addDocumentsToDatabase(j.getDoc());
						db.closeDB();
						
						
						//System.out.println(dList.size());
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
					// compare document to other documents in database
					
					// return result to user outqueue
					System.out.println("list size result"+rList.size());
					Global.addToOutQueue(rList);
					outQueue =  Global.getOutQueue();
					
				}
			}
		}
		
}
