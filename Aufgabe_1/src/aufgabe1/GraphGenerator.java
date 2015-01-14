package aufgabe1;

import java.security.InvalidParameterException;
import java.util.Random;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;

public class GraphGenerator
{
	private static final int RETRIES = 3;
	private static final int SKIPS = 6;

	public static Graph<String, WeightedNamedEdge> generate(GeneratorProperties inProperties)
	{
		Graph<String, WeightedNamedEdge> gen;
		if(inProperties.directed)
			gen = new DefaultDirectedGraph<>(WeightedNamedEdge.class);
		else
			gen = new DefaultGraph();

		if(inProperties.network)
			generateNetwork(gen, inProperties);
		else if(inProperties.complete)
			generateComplete(gen, inProperties);
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
			for(int i = 0; i <= edgeCount || inGen.edgeSet().size() < inProperties.minEdges; i++)
			{
				String source = "v" + (r.nextInt(vertexCount) + 1);
				String target = "v" + (r.nextInt(vertexCount) + 1);
				WeightedNamedEdge edge = new WeightedNamedEdge(source, target, inProperties.directed);
				if(inProperties.weighted)
				{
					int weight = random(r, inProperties.minWeight, inProperties.maxWeight);
					edge.setWeigth(weight);
				}

				inGen.addEdge(source, target, edge);
			}
		}
	}

	private static void generateComplete(Graph<String, WeightedNamedEdge> inGen, GeneratorProperties inProperties)
	{
		Random r = new Random();
		int vertexCount = random(r, inProperties.minVertexes, inProperties.maxVertexes);
		for(int i = 1; i <= vertexCount; i++)
		{
			inGen.addVertex("v" + i);
		}

		if(vertexCount > 0)
		{
			int skips = SKIPS;

			generateEdge:
			for(int i = 1; i <= vertexCount && skips > 0; i++)
			{
				String source = "v" + i;
				for(int i2 = i + 1; i2 <= vertexCount; i2++)
				{
					if(i == i2)
						continue;

					String target = "v" + i2;
					WeightedNamedEdge edge = new WeightedNamedEdge(source, target, inProperties.directed);
					if(inProperties.weighted)
					{
						int weight = random(r, inProperties.minWeight, inProperties.maxWeight);
						if(inProperties.metric)
						{
							for(WeightedNamedEdge secondary : inGen.edgesOf(source))
							{
								WeightedNamedEdge tertier = inGen.getEdge(secondary.getTarget(), target);
								if(tertier != null)
								{
									System.out.println((tertier.getWeigth() + secondary.getWeigth()) + " VS " + weight);
									int tries = RETRIES;
									while(tertier.getWeigth() + secondary.getWeigth() < weight && tries > 0)
									{
										System.out.println("Had to readjust: " + source + ":" + target + "; previous: " + weight);
										weight -= random(r, inProperties.minWeight - weight, inProperties.maxWeight - weight);
										tries--;
									}

									if(tries == 0 && tertier.getWeigth() + secondary.getWeigth() < weight)
									{
										skips--;
										continue generateEdge;
									}
								}
							}
						}
						edge.setWeigth(weight);
					}

					inGen.addEdge(source, target, edge);
				}
			}

			if(skips == 0)
			{
				throw new RuntimeException("Skipped too many edge generations due to failing to create a metric edge.");
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
		}

		for(; edgeCount >= 0 || inGen.edgeSet().size() < inProperties.minEdges; edgeCount--)
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
		public boolean metric = false;
		public boolean complete = false;

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
