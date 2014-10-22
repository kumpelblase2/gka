package aufgabe1;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.jgrapht.Graph;

public class BFSSearcher
{
	public static List<String> search(Graph<String, WeightedNamedEdge> inGraph, String inStart, String inEnd)
	{
		List<String> path = new ArrayList<String>();
		if(!inGraph.containsVertex(inStart) || !inGraph.containsVertex(inEnd))
			return path;

		Queue<String> queue = new ConcurrentLinkedQueue<String>();
		Map<String, VisitedNode> visited = new HashMap<String, VisitedNode>();
		VisitedNode startVisited = new VisitedNode();
		startVisited.name = inStart;
		startVisited.parent = null;
		startVisited.value = 0;
		visited.put(inStart, startVisited);
		queue.offer(inStart);
		while(!queue.isEmpty())
		{
			String current = queue.poll();
			VisitedNode node = visited.get(current);
			for(WeightedNamedEdge edge : inGraph.edgesOf(current))
			{
				String target = edge.getTarget();
				int value = node.value + edge.getWeigth();
				if(!visited.containsKey(target))
				{
					VisitedNode newVisited = new VisitedNode();
					newVisited.name = target;
					newVisited.parent = current;
					newVisited.value = value;
					visited.put(target, newVisited);
					queue.offer(target);
				}
				else
				{
					VisitedNode existing = visited.get(target);
					if(existing.value > value)
					{
						existing.parent = current;
						existing.value = value;
					}
				}
			}
		}

		if(!visited.containsKey(inEnd))
			return path;

		VisitedNode current = visited.get(inEnd);
		while(current != null)
		{
			path.add(current.name);
			current = visited.get(current.parent);
		}
		Collections.reverse(path);
		return path;
	}

	private static class VisitedNode
	{
		public String name;
		public int value;
		public String parent;
	}
}