package aufgabe1;

import java.util.*;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;

public class NearestNeighborSearch implements SearchAlgorithm
{
	//@Override
	public Path search1(Graph<String, WeightedNamedEdge> inGraph, String inStart, String inEnd)
	{
		Path path = new Path();
		if(inStart == null)
		{
			Iterator<String> it = inGraph.vertexSet().iterator();
			for(int i = 0; i < new Random().nextInt(inGraph.vertexSet().size()) - 1; i++)
				it.next();

			inStart = it.next();
		}

		if(!inGraph.containsVertex(inStart))
			return path;


		String current = inStart;
		List<String> visited = new ArrayList<>(inGraph.vertexSet().size());
		while(visited.size() < inGraph.vertexSet().size())
		{
			WeightedNamedEdge shortest = null;
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

				if(shortest == null || (edge.getWeigth() < shortest.getWeigth() && !visited.contains(target)))
				{
					if(!visited.contains(target))
						shortest = edge;
				}
			}

			if(shortest == null)
				return path;

			if(shortest.getTarget().equals(current))
				current = shortest.getSource();
			else
				current = shortest.getTarget();

			visited.add(current);
		}

		path.setSteps(path.getSteps() + 1);
		WeightedNamedEdge endStart = inGraph.getEdge(current, visited.get(0));
		if(endStart == null || (endStart.isDirected() && endStart.getTarget().equals(current)))
			return path;

		visited.add(visited.get(0));

		path.addAlternative(visited);
		return path;
	}

	public Path search(Graph<String ,WeightedNamedEdge> inGraph, String inStart, String inEnd)
	{
		Path path = new Path();
		if(inStart == null)
		{
			Iterator<String> it = inGraph.vertexSet().iterator();
			for(int i = 0; i < new Random().nextInt(inGraph.vertexSet().size()) - 1; i++)
				it.next();

			inStart = it.next();
		}

		if(!inGraph.containsVertex(inStart))
			return path;

		List<String> currentPath = new ArrayList<>(inGraph.vertexSet().size() + 1);
		currentPath.add(inStart);
		currentPath.add(inStart);

		for(String vertex : inGraph.vertexSet())
		{
			if(currentPath.contains(vertex))
				continue;

			int min = Integer.MAX_VALUE;
			int index = -1;
			for(int i = 1; i < currentPath.size(); i++)
			{
				currentPath.add(i, vertex);
				int length = getDistance(inGraph, currentPath);
				if(length < min)
				{
					min = length;
					index = i;
				}
				currentPath.remove(i);
			}

			currentPath.add(index, vertex);
		}

		path.addAlternative(currentPath);
		return path;
	}

	private int getDistance(Graph<String, WeightedNamedEdge> inGraph, List<String> inDistance)
	{
		if(inDistance.size() <= 1)
			return 0;

		int length = 0;
		String current = null;
		String next = null;
		for(int i = 0; i < inDistance.size() - 1; i++)
		{
			current = inDistance.get(i);
			next = inDistance.get(i + 1);
			WeightedNamedEdge edge = inGraph.getEdge(current, next);
			if(edge == null)
				edge = inGraph.getEdge(next, current);

			length += edge.getWeigth();
		}

		WeightedNamedEdge edge = inGraph.getEdge(current, inDistance.get(0));
		if(edge == null)
			edge = inGraph.getEdge(inDistance.get(0), current);
		return length + edge.getWeigth();
	}

	@Override
	public String toString()
	{
		return "Nearest Neighbor";
	}
}