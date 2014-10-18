package aufgabe1;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.jgrapht.Graph;

public class BFSSearcher
{
	public List<String> search(Graph<String, WeightedNamedEdge> inGraph, String inStart, String inEnd)
	{
		List<String> path = new ArrayList<String>();
		if(!inGraph.containsVertex(inStart) || !inGraph.containsVertex(inEnd))
			return path;

		Queue<String> queue = new ConcurrentLinkedQueue<String>();
		Map<String, VisitedNode> visited = new HashMap<String, VisitedNode>();
		queue.offer(inStart);
		while(!queue.isEmpty())
		{
			String current = queue.poll();

		}

		if(!visited.containsKey(inEnd))
			return path;

		// todo gen path
		return path;
	}

	private class VisitedNode
	{
		public String name;
		public String value;
		public String parent;
	}
}