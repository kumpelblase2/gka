package aufgabe1;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jgrapht.Graph;

public class DijkstraSearcher implements SearchAlgorithm
{
	public Path search(Graph<String, WeightedNamedEdge> inGraph, String inStart, String inEnd)
	{
		Path path = new Path();
		if(!inGraph.containsVertex(inStart) || !inGraph.containsVertex(inEnd))
			return path;

		Queue<String> queue = new ConcurrentLinkedQueue<>();
		Map<String, VisitedNode> visited = new HashMap<>();
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
			path.setSteps(path.getSteps() + 1);
			for(WeightedNamedEdge edge : inGraph.edgesOf(current))
			{
				String target = edge.getTarget();
				if(target.equals(current))
					target = edge.getSource();

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
			path.getVertexes().add(current.name);
			current = visited.get(current.parent);
		}
		Collections.reverse(path.getVertexes());
		
		System.out.println("Dij: "+path.getVertexes());
		System.out.println("Dij: "+path.getSteps());
		
		return path;
	}

	public String toString()
	{
		return "Dijkstra Search";
	}

	private static class VisitedNode
	{
		public String name;
		public int value;
		public String parent;
	}
}