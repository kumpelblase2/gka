package aufgabe1;

import java.security.InvalidParameterException;
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

		if(inProperties.network)
			generateNetwork(gen, inProperties);
		else
			generateNormal(gen, inProperties);

		return gen;
	}

	private static void generateNormal(Graph<String, WeightedNamedEdge> inGen, GeneratorProperties inProperties)
	{
		Random r = new Random();
		int edgeCount = random(r, inProperties.minEdges, inProperties.maxEdges);
		int vertexCount = random(r, inProperties.minVertexes, inProperties.maxVertexes);
		for(int i = 1; i <= vertexCount; i++)
		{
			inGen.addVertex("v" + i);
		}

		if(vertexCount > 0)
		{
			for(int i = 0; i <= edgeCount; i++)
			{
				String source = "v" + (r.nextInt(vertexCount) + 1);
				String target = "v" + (r.nextInt(vertexCount) + 1);
				WeightedNamedEdge edge = new WeightedNamedEdge(source, target, inProperties.directed);
				if(inProperties.weighted)
					edge.setWeigth(random(r, inProperties.minWeight, inProperties.maxWeight));

				inGen.addEdge(source, target, edge);
			}
		}
	}

	private static void generateNetwork(Graph<String, WeightedNamedEdge> inGen, GeneratorProperties inProperties)
	{
		if(inProperties.minVertexes < 3)
			throw new InvalidParameterException("Not enough vertexes specified for network.");

		if(inProperties.minEdges < inProperties.minVertexes * 2 - 2)
			throw new InvalidParameterException("Not enough edges specified for given network.");

		Random r = new Random();
		int edgeCount = random(r, inProperties.minEdges, inProperties.maxEdges);
		int vertexCount = random(r, inProperties.minVertexes, inProperties.maxVertexes);
		inGen.addVertex("Start");
		inGen.addVertex("Sink");
		int remainingVertexes = vertexCount - 2;
		for(int i = 0; i < remainingVertexes; i++)
		{
			inGen.addVertex("v" + i);
		}

		int usedEdges = 0;
		for(int i = 0; i < ((remainingVertexes % 10) + 2); i++)
		{
			WeightedNamedEdge edge = new WeightedNamedEdge("Start", "v" + i, true);
			edge.setWeigth(random(r, inProperties.minWeight, inProperties.maxWeight));
			inGen.addEdge("Start", "v" + i, edge);
			usedEdges++;
		}

		for(int i = remainingVertexes - ((remainingVertexes % 10) + 2); i < remainingVertexes; i++)
		{
			WeightedNamedEdge edge = new WeightedNamedEdge("v" + i, "Sink", true);
			edge.setWeigth(random(r, inProperties.minWeight, inProperties.maxWeight));
			inGen.addEdge("v" + i, "Sink", edge);
			usedEdges++;
		}

		edgeCount -= usedEdges;
		edgeCount -= remainingVertexes * 2;
		for(int i = 0; i < remainingVertexes; i++)
		{
			int source;
			do
			{
				source = random(r, 0, remainingVertexes - 1);
			} while(source == i);

			int target;
			do
			{
				target = random(r, 0, remainingVertexes - 1);
			} while(target == i || target == source);

			WeightedNamedEdge fromSource = new WeightedNamedEdge("v" + source, "v" + i, true);
			WeightedNamedEdge toTarget = new WeightedNamedEdge("v" + i, "v" + target, true);
			fromSource.setWeigth(random(r, inProperties.minWeight, inProperties.maxWeight));
			toTarget.setWeigth(random(r, inProperties.minWeight, inProperties.maxWeight));
			inGen.addEdge("v" + source, "v" + i, fromSource);
			inGen.addEdge("v" + i, "v" + target, toTarget);

			if(edgeCount > 0 && r.nextBoolean())
			{
				int nextTarget;
				do
				{
					nextTarget = random(r, 0, remainingVertexes - 1);
				} while(nextTarget == i);

				WeightedNamedEdge edge = new WeightedNamedEdge("v" + i, "v" + nextTarget, true);
				edge.setWeigth(random(r, inProperties.minWeight, inProperties.maxWeight));
				inGen.addEdge("v" + i, "v" + nextTarget, edge);
				edgeCount--;
			}
		}

		for(; edgeCount >= 0; edgeCount--)
		{
			int first;
			int second;

			do
			{
				first = random(r, 0, remainingVertexes - 1);
				second = random(r, 0, remainingVertexes - 1);
			} while(first == second || inGen.containsEdge("v" + first, "v" + second));

			WeightedNamedEdge edge = new WeightedNamedEdge("v" + first, "v" + second, true);
			edge.setWeigth(random(r, inProperties.minWeight, inProperties.maxWeight));
			inGen.addEdge("v" + first, "v" + second, edge);
		}
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
		public boolean network = false;

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

		public GeneratorProperties(final int inMinEdges, final int inMaxEdges, final int inMinVertexes, final int inMaxVertexes, final boolean inDirected, final int inMinWeight, final int inMaxWeight, boolean inAsNetwork)
		{
			minEdges = inMinEdges;
			maxEdges = inMaxEdges;
			minVertexes = inMinVertexes;
			maxVertexes = inMaxVertexes;
			directed = inDirected;
			minWeight = inMinWeight;
			maxWeight = inMaxWeight;
			weighted = true;
			network = inAsNetwork;
		}
	}
}
