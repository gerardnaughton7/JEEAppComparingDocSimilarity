package ie.gmit.sw;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * Global sets up BlockingQueues and allows access to ServiceHandler and ServicePollHandler
 * 
 * @author Gerard Naughton
 *
 */
public class Global {
	
	//declare variables
	private static BlockingQueue<Job> inQueue;
	private static BlockingQueue<List<Result>> outQueue;
	
	private Global()
	{
		
	}
	// creates inqueue and outqueue on init and makes them accessible to servlets and workers
	public static synchronized Boolean init()
	{
		
		inQueue = new ArrayBlockingQueue<Job>(100);
		outQueue = new ArrayBlockingQueue<List<Result>>(100);
		
		return true;
	}

	//get set methods
	public static BlockingQueue<Job> getInQueue() {
		return inQueue;
	}

	public static void setInQueue(BlockingQueue<Job> inQueue) {
		Global.inQueue = inQueue;
	}

	public static BlockingQueue<List<Result>> getOutQueue() {
		return outQueue;
	}

	public static void setOutQueue(BlockingQueue<List<Result>> outQueue) {
		Global.outQueue = outQueue;
	}
	
	//add job to inqueue
	public static void addToInQueue(Job j)
	{
		Global.inQueue.add(j);
	}
	
	// adds list of results to outQueue
	public static void addToOutQueue(List<Result> r)
	{
		Global.outQueue.add(r);
	}
	
}
