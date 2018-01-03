package ie.gmit.sw;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Worker extends Thread{
	private BlockingQueue<Job> inQueue = new ArrayBlockingQueue<Job>(10);
	private BlockingQueue<Job> outQueue = new ArrayBlockingQueue<Job>(10);
	private Job j;
	
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
					
					outQueue.offer (j); 
					
				}
			}
		}
		
}
