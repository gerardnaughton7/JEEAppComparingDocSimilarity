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

/**
 * Worker Thread is spawned in ServiceHandler constructor and the thread pulls from the Global inQueue retrieving jobs.
 * It then processes these job by then comparing the Document in the job similarity with other documents saved in the DB4O.
 * Run method:
 * Checks for Job
 * if Job exists it computes the Jaccard similarity with existing documents, saves the job document in DB4O and then outputs the campare Results to the Global OutQueue. 
 * 
 * @author Gerard Naughton
 *
 */
public class Worker extends Thread{
	private BlockingQueue<Job> inQueue = new ArrayBlockingQueue<Job>(100);
	private BlockingQueue<List<Result>> outQueue = new ArrayBlockingQueue<List<Result>>(100);
	private Job j = null;
	private List<Document> dList = new ArrayList<Document>();
	private List<Result> rList = new ArrayList<Result>();
	private ComputeJaccard cj;
	
	//constructor 
	public Worker() {
		
	}
	
	//run method which runs our jobs
	public void run() {
		//keeps running
		while(true) {
			//get handle on inQueue
			inQueue = Global.getInQueue();
			//get job to queue
			j = inQueue.poll();
			
			//if j is null do nothing till job is available
			if(j != null)
			{
				//get handle on database
				DocDBRunner db;
				//get Docs from database and call Compute Jaccard
				try {
					db = new DocDBRunner();
					dList = db.getDocuments();
					cj = new ComputeJaccard(dList,j.getDoc());
					//compute returns a list of results for that job
					rList = cj.Compute();
					//add document to db40 database
					//db.addDocumentsToDatabase(j.getDoc());
					//close database
					db.closeDB();
					
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				
				// return result to user outqueue
				Global.addToOutQueue(rList);
				
			}
		}
	}
		
}
