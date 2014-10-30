package aufgabe1;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

public class GraphParser
{
	public static final Pattern REGEX = Pattern.compile("([a-zA-Z0-9]+) ?(-(>|-) ?([a-zA-Z0-9]+) ?(\\(([a-zA-Z0-9]+)\\))?)? ?( ?: ?([0-9]+))?;");

	public static Graph<String, WeightedNamedEdge> parse(File inFile)
	{
		return parse(Util.readFile(inFile));
	}

	public static Graph<String, WeightedNamedEdge> parse(String inContent)
	{
		Matcher matcher = REGEX.matcher(inContent);
		Graph<String, WeightedNamedEdge> graph;
		if(inContent.contains("->"))
			graph = new DefaultDirectedGraph<>(WeightedNamedEdge.class);
		else
			graph = new DefaultGraph();

		while(matcher.find())
		{
			String vertexStart = matcher.group(1);
			String edgeType = matcher.group(3);
			String vertexEnd = matcher.group(4);
			String edgeName = matcher.group(6);
			String edgeWeight = matcher.group(8);

			WeightedNamedEdge edge = null;
			//add Vertex to the graphT
			graph.addVertex(vertexStart);
			//if there is an edge between two vertices
			if(vertexEnd != null)
			{
				edge = new WeightedNamedEdge(vertexStart, vertexEnd, edgeType.equals(">"));
				graph.addVertex(vertexEnd);

				//ggf Gewichtung hinzufuegen
				if (edgeWeight != null)
					edge.setWeigth(Integer.parseInt(edgeWeight));

				//ggf Namen hinzufuegen
				if (edgeName != null)
					edge.setName(edgeName);

				graph.addEdge(vertexStart, vertexEnd, edge);
			}
		}

		return graph;
	}

	public static String parse(Graph<String, WeightedNamedEdge> inGraph)
	{
		StringBuilder result = new StringBuilder();
		Set<String> foundEdges = new HashSet<>();
		for(WeightedNamedEdge edge : inGraph.edgeSet())
		{
			result.append(edge.getSource()).append(" ").append(edge.isDirected() ? "->" : "--").append(" ").append(edge.getTarget());
			foundEdges.add(edge.getSource());
			foundEdges.add(edge.getTarget());
			if(edge.getName() != null && edge.getName().length() > 0)
				result.append(" (").append(edge.getName()).append(")");

			if(edge.hasWeigth())
				result.append(" : ").append(edge.getWeigth());

			result.append(";\n");
		}

		for(String vertex : inGraph.vertexSet())
		{
			if(!foundEdges.contains(vertex))
				result.append(vertex).append(";\n"); // TODO should be system line separator
		}

		foundEdges.clear();

		return result.toString();
	}
}
