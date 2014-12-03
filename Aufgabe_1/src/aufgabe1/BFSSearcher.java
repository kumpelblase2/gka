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

				//if(target.equals(inStart))
				//	continue;

				boolean isAncestor = false;
				for(Ancestor ancestor : node.ancestors)
				{
					if(ancestor.name.equals(target))
					{
						isAncestor = true;
						break;
					}
				}

				if(isAncestor)
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


				boolean added = newVisited.ancestors.add(new Ancestor(current, value));
				visited.put(target, newVisited);
			}
		}

		if(!visited.containsKey(inEnd))
			return path;

		List<List<String>> result = createPaths(inEnd, inStart, visited, new ArrayList<String>());
		Iterator<List<String>> iterator = result.iterator();
		while(iterator.hasNext())
		{
			List<String> onePath = iterator.next();
			Collections.reverse(onePath);
			path.addAlternative(onePath);
		}
		return path;
	}

	private List<List<String>> createPaths(String inEnd, String inStart, Map<String, VisitedNode> inData, List<String> inAcc)
	{
		VisitedNode current = inData.get(inEnd);
		if(inEnd.equals(inStart))
		{
			inAcc.add(inEnd);
			List<List<String>> paths = new ArrayList<>();
			paths.add(inAcc);
			return paths;
		}

		inAcc.add(inEnd);
		List<List<String>> results = new ArrayList<>();
		for(Ancestor ancestor : current.ancestors)
		{
			if(inAcc.contains(ancestor.name))
				continue;

			List<List<String>> result = createPaths(ancestor.name, inStart, inData, new ArrayList<>(inAcc));
			results.addAll(result);
		}

		return results;
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
				int firstResult = new Integer(o1.value).compareTo(o2.value);
				if(firstResult != 0)
					return firstResult;

				return o1.name.compareTo(o2.name);
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
