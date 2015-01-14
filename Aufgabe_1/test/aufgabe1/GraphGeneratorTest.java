package aufgabe1;

import org.jgrapht.DirectedGraph;
import org.jgrapht.Graph;
import org.junit.Assert;
import org.junit.Test;

public class GraphGeneratorTest
{

	public static final int MIN_WEIGHT = 10;
	public static final int MIN_EDGES = 1000;
	public static final int MAX_EDGES = 10000;
	public static final int MIN_VERTEXES = 100;
	public static final int MAX_VERTEXES = 1000;
	public static final boolean DIRECTED = true;
	public static final int MAX_WEIGHT = 20;

	public static final int TIMES = 1000;

	public void testGenerate()
	{
		GraphGenerator.GeneratorProperties properties = new GraphGenerator.GeneratorProperties(MIN_EDGES, MAX_EDGES, MIN_VERTEXES, MAX_VERTEXES, DIRECTED);
		Graph<String, WeightedNamedEdge> generated = GraphGenerator.generate(properties);

		Assert.assertTrue("Not right vertex size: " + generated.vertexSet().size() + " min: " + MIN_VERTEXES + " max: " + MAX_VERTEXES, generated.vertexSet().size() <= properties.maxVertexes && generated.vertexSet().size() >= properties.minVertexes);
		Assert.assertTrue("Not right edge size: " + generated.edgeSet().size() + " min: " + MIN_EDGES + " max: " + MAX_EDGES, generated.edgeSet().size() <= properties.maxEdges && generated.edgeSet().size() >= properties.minEdges);
		Assert.assertTrue(generated instanceof DirectedGraph);
		for(WeightedNamedEdge edge : generated.edgeSet())
		{
			Assert.assertFalse(edge.hasWeigth());
		}
	}

	public void testGenerateWeight()
	{
		GraphGenerator.GeneratorProperties properties = new GraphGenerator.GeneratorProperties(MIN_EDGES, MAX_EDGES, MIN_VERTEXES, MAX_VERTEXES, DIRECTED, MIN_WEIGHT, MAX_WEIGHT);
		Graph<String, WeightedNamedEdge> generated = GraphGenerator.generate(properties);

		Assert.assertTrue("Not right vertex size: " + generated.vertexSet().size() + " min: " + MIN_VERTEXES + " max: " + MAX_VERTEXES, generated.vertexSet().size() <= properties.maxVertexes && generated.vertexSet().size() >= properties.minVertexes);
		Assert.assertTrue("Not right edge size: " + generated.edgeSet().size() + " min: " + MIN_EDGES + " max: " + MAX_EDGES, generated.edgeSet().size() <= properties.maxEdges && generated.edgeSet().size() >= properties.minEdges);
		Assert.assertTrue(generated instanceof DirectedGraph);
		for(WeightedNamedEdge edge : generated.edgeSet())
		{
			Assert.assertTrue(edge.hasWeigth());
			Assert.assertTrue("Not right weight: " + edge.getWeigth() + " min: " + MIN_WEIGHT + " max: " + MAX_WEIGHT, edge.getWeigth() >= properties.minWeight && edge.getWeigth() <= properties.maxWeight);
		}
	}

	public void testGenerateMetric()
	{
		GraphGenerator.GeneratorProperties properties = new GraphGenerator.GeneratorProperties(MIN_EDGES, MAX_EDGES, MIN_VERTEXES, MAX_VERTEXES, DIRECTED, MIN_WEIGHT, MAX_WEIGHT);
		Graph<String, WeightedNamedEdge> generated = GraphGenerator.generate(properties);

		Assert.assertTrue("Not right vertex size: " + generated.vertexSet().size() + " min: " + MIN_VERTEXES + " max: " + MAX_VERTEXES, generated.vertexSet().size() <= properties.maxVertexes && generated.vertexSet().size() >= properties.minVertexes);
		Assert.assertTrue("Not right edge size: " + generated.edgeSet().size() + " min: " + MIN_EDGES + " max: " + MAX_EDGES, generated.edgeSet().size() <= properties.maxEdges && generated.edgeSet().size() >= properties.minEdges);
		Assert.assertTrue(generated instanceof DirectedGraph);
		for(WeightedNamedEdge edge : generated.edgeSet())
		{
			Assert.assertTrue(edge.hasWeigth());
			Assert.assertTrue("Not right weight: " + edge.getWeigth() + " min: " + MIN_WEIGHT + " max: " + MAX_WEIGHT, edge.getWeigth() >= properties.minWeight && edge.getWeigth() <= properties.maxWeight);
		}
	}

	@Test
	public void testGenerateTimes()
	{
		for(int i = 0; i < TIMES; i++)
		{
			testGenerate();
		}
	}

	@Test
	public void testGenerateWeightTimes()
	{
		for(int i = 0; i < TIMES; i++)
		{
			testGenerateWeight();
		}
	}

	@Test
	public void testGenerateMetricTimes()
	{
		for(int i = 0; i < TIMES; i++)
		{
			testGenerateMetric();
		}
	}

	@Test
	public void testGenerateNetwork()
	{
		for(int i = 0; i < TIMES; i++)
		{
			generateNetworkTest();
		}
	}

	private void generateNetworkTest()
	{
		GraphGenerator.GeneratorProperties properties = new GraphGenerator.GeneratorProperties(MIN_EDGES, MAX_EDGES, MIN_VERTEXES, MAX_VERTEXES, DIRECTED, MIN_WEIGHT, MAX_WEIGHT);
		properties.network = true;
		Graph<String, WeightedNamedEdge> generated = GraphGenerator.generate(properties);

		Assert.assertTrue("Not right vertex size: " + generated.vertexSet().size() + " min: " + MIN_VERTEXES + " max: " + MAX_VERTEXES, generated.vertexSet().size() <= properties.maxVertexes && generated.vertexSet().size() >= properties.minVertexes);
		Assert.assertTrue("Not right edge size: " + generated.edgeSet().size() + " min: " + MIN_EDGES + " max: " + MAX_EDGES, generated.edgeSet().size() <= properties.maxEdges && generated.edgeSet().size() >= properties.minEdges);
		Assert.assertTrue(generated instanceof DirectedGraph);
		for(WeightedNamedEdge edge : generated.edgeSet())
		{
			Assert.assertTrue(edge.hasWeigth());
			Assert.assertTrue("Not right weight: " + edge.getWeigth() + " min: " + MIN_WEIGHT + " max: " + MAX_WEIGHT, edge.getWeigth() >= properties.minWeight && edge.getWeigth() <= properties.maxWeight);
		}

		Assert.assertTrue(generated.containsVertex("Start"));
		Assert.assertTrue(generated.containsVertex("Sink"));
		for(WeightedNamedEdge edge : generated.edgesOf("Start"))
		{
			Assert.assertNotEquals(edge.getTarget(), "Start");
		}

		for(WeightedNamedEdge edge : generated.edgesOf("Sink"))
		{
			Assert.assertNotEquals(edge.getSource(), "Sink");
		}

		for(String vertex : generated.vertexSet())
		{
			if(vertex.equals("Start") || vertex.equals("Sink"))
				continue;

			boolean hasFrom = false;
			boolean hasTo = false;
			for(WeightedNamedEdge edge : generated.edgesOf(vertex))
			{
				if(edge.getTarget().equals(vertex))
					hasFrom = true;

				if(edge.getSource().equals(vertex))
					hasTo = true;
			}

			Assert.assertTrue(hasFrom);
			Assert.assertTrue(hasTo);
		}
	}
}
