package aufgabe1;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class BFSSearcherTest
{
	Graph<String, WeightedNamedEdge> graph;
	Graph<String, WeightedNamedEdge> graph2;
	Graph<String, WeightedNamedEdge> graph3;

	@Before
	public void setup()
	{
		graph = new DefaultDirectedGraph<String, WeightedNamedEdge>(WeightedNamedEdge.class);
		graph.addVertex("a");
		graph.addVertex("b");
		graph.addVertex("c");
		graph.addVertex("d");

		graph.addEdge("a", "b", new WeightedNamedEdge("a", "b", true));
		graph.addEdge("a", "c", new WeightedNamedEdge("a", "c", true));
		graph.addEdge("c", "b", new WeightedNamedEdge("c", "b", true));
		graph.addEdge("c", "d", new WeightedNamedEdge("c", "d", true));

		graph2 = new DefaultDirectedGraph<String, WeightedNamedEdge>(WeightedNamedEdge.class);
		graph2.addVertex("a");
		graph2.addVertex("b");
		graph2.addVertex("c");

		WeightedNamedEdge weightedEdge1 = new WeightedNamedEdge("a", "c", false);
		weightedEdge1.setWeigth(10);
		graph2.addEdge("a", "c", weightedEdge1);
		WeightedNamedEdge weightedEdge2 = new WeightedNamedEdge("a", "b", false);
		weightedEdge2.setWeigth(1);
		graph2.addEdge("a", "b", weightedEdge2);
		WeightedNamedEdge weightedEdge3 = new WeightedNamedEdge("b", "c", false);
		weightedEdge3.setWeigth(1);
		graph2.addEdge("b", "c", weightedEdge3);

		graph3 = new DefaultDirectedGraph<String, WeightedNamedEdge>(WeightedNamedEdge.class);
		graph3.addVertex("a");
		graph3.addVertex("b");
		graph3.addVertex("c");

		graph3.addEdge("a", "b", new WeightedNamedEdge("a", "b", true));
	}

	@Test
	public void testSuccess()
	{
		// Prüft ob ein Weg mit dem Parser gefunden wurde.
		Assert.assertTrue(BFSSearcher.search(graph, "a", "c").getVertexes().size() > 0);
		Assert.assertTrue(BFSSearcher.search(graph, "a", "b").getVertexes().size() > 0);
		Assert.assertTrue(BFSSearcher.search(graph, "c", "b").getVertexes().size() > 0);
		Assert.assertTrue(BFSSearcher.search(graph, "a", "d").getVertexes().size() > 0);
	}

	@Test
	public void testFail()
	{
		// Prüft, dass keine Weg gefunden wurde, auch wenn im Ungerichteten Graphen ein Weg vorliegt.
		Assert.assertEquals(BFSSearcher.search(graph, "c", "a").getVertexes().size(), 0);
		Assert.assertEquals(BFSSearcher.search(graph, "b", "a").getVertexes().size(), 0);
		Assert.assertEquals(BFSSearcher.search(graph, "b", "c").getVertexes().size(), 0);
		Assert.assertEquals(BFSSearcher.search(graph, "d", "a").getVertexes().size(), 0);

		// Prüft, dass kein Weg gefunden wurde, wenn keine Verbindung besteht.
		Assert.assertEquals(BFSSearcher.search(graph3, "a", "c").getVertexes().size(), 0);
	}

	@Test
	public void testBestPath()
	{
		// Prüft, dass der vom Wert her beste Weg gefunden wurde.
		Assert.assertEquals(BFSSearcher.search(graph2, "a", "c").getVertexes().size(), 3);
	}
}