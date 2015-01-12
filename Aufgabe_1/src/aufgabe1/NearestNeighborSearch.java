package aufgabe1;

import java.util.*;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;

public class NearestNeighborSearch implements SearchAlgorithm
{
	@Override
	public Path search(Graph<String, WeightedNamedEdge> inGraph, String inStart, String inEnd)
	{
		Path path = new Path();
		if(inStart == null)
		{
			Iterator<String> it = inGraph.vertexSet().iterator();
			for(int i = 0; i < new Random().nextInt(inGraph.vertexSet().size()) - 1; i++)
				it.next();

			inStart = it.next();
		}

		if(!inGraph.containsVertex(inStart) || !inGraph.containsVertex(inEnd))
			return path;


		String current = inStart;
		List<String> visited = new ArrayList<>(inGraph.vertexSet().size());
		while(visited.size() < inGraph.vertexSet().size())
		{
			WeightedNamedEdge shortest = null;
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
					shortest = edge;
			}

			if(shortest == null)
				return path;

			if(shortest.getTarget().equals(current))
				current = shortest.getSource();
			else
				current = shortest.getTarget();

			visited.add(current);
		}
		visited.add(inStart);

		path.addAlternative(visited);
		return path;
	}
}