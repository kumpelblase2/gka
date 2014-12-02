package aufgabe1;

import java.util.*;
import java.util.concurrent.ConcurrentLinkedQueue;
import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;

public class BFSSearcher implements SearchAlgorithm
{
	@Override
	public Path search(final Graph<String, WeightedNamedEdge> inGraph, final String inStart, final String inEnd)
	{
		Path path = new Path();
		if(!inGraph.containsVertex(inStart) || !inGraph.containsVertex(inEnd))
			return path;

		Queue<String> queue = new ConcurrentLinkedQueue<>();
		Map<String, VisitedNode> visited = new HashMap<>();
		VisitedNode startVisited = new VisitedNode();
		startVisited.name = inStart;
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

				if(target.equals(inStart))
					continue;

				int value = (node.ancestors == null || node.ancestors.size() == 0 ? 0 : node.ancestors.first().value + 1);
				VisitedNode newVisited;
				if(!visited.containsKey(target))
				{
					newVisited = new VisitedNode();
					newVisited.name = target;
					queue.offer(target);
				}
				else
					newVisited = visited.get(target);

				newVisited.ancestors.add(new Ancestor(current, value));
				visited.put(target, newVisited);
			}
		}

		if(!visited.containsKey(inEnd))
			return path;

		List<List<String>> result = createPaths(inEnd, visited);
		Iterator<List<String>> iterator = result.iterator();
		while(iterator.hasNext())
		{
			List<String> onePath = iterator.next();
			Collections.reverse(onePath);
			path.addAlternative(onePath);
		}
		return path;
	}

	private List<List<String>> createPaths(String inEnd, Map<String, VisitedNode> inData)
	{
		VisitedNode current = inData.get(inEnd);
		List<List<String>> path = new ArrayList<>();
		if(current.ancestors == null || current.ancestors.size() == 0)
		{
			List<String> ownPath = new ArrayList<>();
			ownPath.add(current.name);
			path.add(ownPath);
			return path;
		}

		Iterator<Ancestor> ancestors = current.ancestors.iterator();
		while(ancestors.hasNext())
		{
			Ancestor next = ancestors.next();
			List<List<String>> beforePaths = createPaths(next.name, inData);
			for(List<String> subPath : beforePaths)
			{
				subPath.add(current.name);
				path.add(subPath);
			}
		}

		return path;
	}

	public String toString()
	{
		return "Breadth First Search";
	}

	private static class VisitedNode
	{
		public String name;
		public TreeSet<Ancestor> ancestors = new TreeSet<>(new Comparator<Ancestor>()
		{
			@Override
			public int compare(final Ancestor o1, final Ancestor o2)
			{
				return o1.value - o2.value;
			}
		});
	}

	private static class Ancestor
	{
		public String name;
		public int value;

		public Ancestor(final String inName, final int inValue)
		{
			name = inName;
			value = inValue;
		}

		@Override
		public boolean equals(final Object o)
		{
			if(this == o)
				return true;
			if(o == null || getClass() != o.getClass())
				return false;

			Ancestor ancestor = (Ancestor)o;

			return name.equals(ancestor.name);
		}

		@Override
		public int hashCode()
		{
			return name.hashCode();
		}
	}
}
