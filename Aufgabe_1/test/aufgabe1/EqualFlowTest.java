package aufgabe1;

import aufgabe1.GraphGenerator.GeneratorProperties;
import org.jgrapht.Graph;
import org.junit.*;

public class EqualFlowTest
{
	private static final int RUNS = 100;
	private static GeneratorProperties properties;
	private static GeneratorProperties properties2;
	private static final int MIN_VERTEXES = 30;
	private static final int MAX_VERTEXES = 40;
	private static final int MIN_EDGES = 100;
	private static final int MAX_EDGES = 300;
	private static final int MIN_WEIGHT = 10;
	private static final int MAX_WEIGHT = 20;

	@BeforeClass
	public static void preSetup()
	{
		properties = new GeneratorProperties(MIN_EDGES, MAX_EDGES, MIN_VERTEXES, MAX_VERTEXES, true, MIN_WEIGHT, MAX_WEIGHT, true);
		properties2 = new GeneratorProperties(MIN_EDGES * 10, MAX_EDGES * 10, MIN_VERTEXES * 10, MAX_VERTEXES * 10, true, MIN_WEIGHT, MAX_WEIGHT, true);
	}

	@Test
	public void bulkFlow()
	{
		FordFulkerson fordFulkerson = new FordFulkerson();
		EdmondsKarp edmondsKarp = new EdmondsKarp();
		for(int i = 0; i < RUNS; i++)
		{
			Graph<String, WeightedNamedEdge> graph = GraphGenerator.generate((i > 0.75 * RUNS ? properties2 : properties));
			fordFulkerson.search(graph, "Start", "Sink");
			edmondsKarp.search(graph, "Start", "Sink");

			Assert.assertEquals(fordFulkerson.getFlow(), edmondsKarp.getFlow());
		}
	}
}