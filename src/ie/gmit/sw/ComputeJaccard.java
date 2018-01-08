package ie.gmit.sw;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.TreeSet;

public class ComputeJaccard {
	//declare variables
	private List<Document> dList = new ArrayList<Document>();
	private Document d;
	private TreeSet<Integer> hashes = new TreeSet<Integer>();
	private List<Integer> newDocs = new ArrayList<Integer>();
	private List<Integer> prevDocs = new ArrayList<Integer>();
	private List<Integer> common = new ArrayList<Integer>();
	private List<Result> rList = new ArrayList<Result>();
	private final int SET_MAX = 200;
	private double jaccard;
	private Result result;
	
	//contructor
	public ComputeJaccard(List<Document> dList, Document d) {
		super();
		this.dList = dList;
		this.d = d;
	}
	
	//compute method which calls generateNumbers and minHases and computes the jaccard function between the uploaded doc and existing docs
	public List Compute()
	{
		hashes = generateNumbers();
		newDocs = generateMinHashes(d);
		for(Document doc : dList)
		{
			prevDocs = generateMinHashes(doc);
			common.addAll(newDocs);//copies newDocs
			common.retainAll(prevDocs);//finds whats common between them
			jaccard = ((double)common.size()) / newDocs.size();
			//create result object
			result = new Result(d.getName(),doc.getName(),jaccard);
			//add result to result list
			rList.add(result);
			//clear common for next iteration
			common.clear();
		}
		//return list
		return rList;
		
	}
	
	// create treeset of 200 random integers
	public TreeSet<Integer> generateNumbers()
	{
		hashes = new TreeSet<Integer>();
		Random r = new Random();
		for(int i=0; i<SET_MAX;i++)
		{
			hashes.add(r.nextInt());
		}
		
		return hashes;
		
	}
	
	//gets the minhash of our shingles and hashes and creates a list of 200 of the lowest
	public List<Integer> generateMinHashes(Document doc)
	{
		List<Integer> temp = new ArrayList<Integer>();
		List<Shingle> s = new ArrayList<Shingle>();
		s = doc.getShinglelist();
		
		for(Integer hash : hashes)
		{
			int min = Integer.MAX_VALUE;
			for(int i = 0 ; i < s.size(); i++)
			{
				int minHash = s.get(i).getHashCode()^hash;
		        if(minHash < min)
			      {
			    	  min = minHash;
			      }
				
			}
			temp.add(min);
		}
		
		return temp;
	}
	

}
