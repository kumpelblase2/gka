package aufgabe1;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert;

public class DijkstraSearcherTest
{
	Graph<String, WeightedNamedEdge> graph;
	Graph<String, WeightedNamedEdge> graph2;
	Graph<String, WeightedNamedEdge> graph3;
	final DijkstraSearcher search = new DijkstraSearcher();

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
		// Pr�ft ob ein Weg mit dem Parser gefunden wurde.
		Assert.assertTrue(search.search(graph, "a", "c").hasMore());
		Assert.assertTrue(search.search(graph, "a", "b").hasMore());
		Assert.assertTrue(search.search(graph, "c", "b").hasMore());
		Assert.assertTrue(search.search(graph, "a", "d").hasMore());
	}

	@Test
	public void testFail()
	{
		// Pr�ft, dass keine Weg gefunden wurde, auch wenn im Ungerichteten Graphen ein Weg vorliegt.
		Assert.assertFalse(search.search(graph, "c", "a").hasMore());
		Assert.assertFalse(search.search(graph, "b", "a").hasMore());
		Assert.assertFalse(search.search(graph, "b", "c").hasMore());
		Assert.assertFalse(search.search(graph, "d", "a").hasMore());

		// Pr�ft, dass kein Weg gefunden wurde, wenn keine Verbindung besteht.
		Assert.assertFalse(search.search(graph3, "a", "c").hasMore());
	}

	@Test
	public void testBestPath()
	{
		// Pr�ft, dass der vom Wert her beste Weg gefunden wurde.
		Assert.assertEquals(search.search(graph2, "a", "c").next().size(), 3);
	}
}