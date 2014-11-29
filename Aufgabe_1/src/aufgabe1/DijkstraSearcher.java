package aufgabe1;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;

import org.jgrapht.DirectedGraph;
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
				{
					if(inGraph instanceof DirectedGraph)
						continue;

					target = edge.getSource();
				}

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
		List<String> pathNodes = new ArrayList<>();
		while(current != null)
		{
			pathNodes.add(current.name);
			current = visited.get(current.parent);
		}
		Collections.reverse(pathNodes);
		path.addAlternative(pathNodes);
		
		System.out.println("Dij: "+pathNodes);
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