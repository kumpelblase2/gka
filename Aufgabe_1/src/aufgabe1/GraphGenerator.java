package aufgabe1;

import java.util.Random;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

public class GraphGenerator
{
	public static Graph<String, WeightedNamedEdge> generate(GeneratorProperties inProperties)
	{
		Graph<String, WeightedNamedEdge> gen;
		if(inProperties.directed)
			gen = new DefaultDirectedGraph<>(WeightedNamedEdge.class);
		else
			gen = new DefaultGraph();

		Random r = new Random();
		int edgeCount = random(r, inProperties.minEdges, inProperties.maxEdges);
		int vertexCount = random(r, inProperties.minVertexes, inProperties.maxVertexes);
		for(int i = 1; i <= vertexCount; i++)
		{
			gen.addVertex("v" + i);
		}

		if(vertexCount > 0)
		{
			for(int i = 0; i < edgeCount; i++)
			{
				String source = "v" + (r.nextInt(vertexCount) + 1);
				String target = "v" + (r.nextInt(vertexCount) + 1);
				WeightedNamedEdge edge = new WeightedNamedEdge(source, target, inProperties.directed);
				if(inProperties.weighted)
					edge.setWeigth(random(r, inProperties.minWeight, inProperties.maxWeight));

				gen.addEdge(source, target, edge);
			}
		}

		return gen;
	}

	private static int random(Random inRandom, int min, int max)
	{
		return inRandom.nextInt(max - min + 1) + min;
	}

	public static class GeneratorProperties
	{
		public int minEdges;
		public int maxEdges;
		public int minVertexes;
		public int maxVertexes;
		public boolean directed;
		public boolean weighted;
		public int minWeight;
		public int maxWeight;

		public GeneratorProperties(final int inMinEdges, final int inMaxEdges, final int inMinVertexes, final int inMaxVertexes, final boolean inDirected)
		{
			minEdges = inMinEdges;
			maxEdges = inMaxEdges;
			minVertexes = inMinVertexes;
			maxVertexes = inMaxVertexes;
			directed = inDirected;
		}

		public GeneratorProperties(final int inMinEdges, final int inMaxEdges, final int inMinVertexes, final int inMaxVertexes, final boolean inDirected, final int inMinWeight, final int inMaxWeight)
		{
			minEdges = inMinEdges;
			maxEdges = inMaxEdges;
			minVertexes = inMinVertexes;
			maxVertexes = inMaxVertexes;
			directed = inDirected;
			minWeight = inMinWeight;
			maxWeight = inMaxWeight;
			weighted = true;
		}
	}
}
