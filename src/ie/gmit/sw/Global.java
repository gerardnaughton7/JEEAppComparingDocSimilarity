package ie.gmit.sw;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

public class Global {
	
	private static BlockingQueue<Job> inQueue;
	private static BlockingQueue<List<Result>> outQueue;
	
	private Global()
	{
		
	}
	
	public static synchronized Boolean init()
	{
		
		inQueue = new ArrayBlockingQueue<Job>(100);
		outQueue = new ArrayBlockingQueue<List<Result>>(100);
		
		return true;
	}

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
	
	public static void addToInQueue(Job j)
	{
		Global.inQueue.add(j);
	}
	
	public static void addToOutQueue(List<Result> r)
	{
		Global.outQueue.add(r);
	}
	
}
