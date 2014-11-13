package aufgabe1;

import java.util.ArrayList;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class FWSearchTest
{

	Graph<String, WeightedNamedEdge> graph2;
	final FWSearcher search = new FWSearcher();
	
	private final Graph<String, WeightedNamedEdge> resultGraph = new DefaultDirectedGraph<>(WeightedNamedEdge.class);
	private List<String> list = new ArrayList<String>();
    private Path resultPath = new Path();

	@Before
	public void setup()
	{

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
		
		//resultGraph
		resultGraph.addVertex("v1");
		resultGraph.addVertex("v2");
		resultGraph.addVertex("v3");
		resultGraph.addVertex("v4");
		resultGraph.addVertex("v5");
		resultGraph.addVertex("v6");

		WeightedNamedEdge edge1 = new WeightedNamedEdge("v1", "v2", true);
		edge1.setWeigth(1);
		resultGraph.addEdge("v1", "v2", edge1);
		
		WeightedNamedEdge edge2 = new WeightedNamedEdge("v2", "v3", true);
		edge2.setWeigth(1);
		resultGraph.addEdge("v2", "v3", edge2);
		
		WeightedNamedEdge edge3 = new WeightedNamedEdge("v3", "v5", true);
		edge3.setWeigth(1);
		resultGraph.addEdge("v3", "v5", edge3);
		
		WeightedNamedEdge edge4 = new WeightedNamedEdge("v1", "v5", true);
		edge4.setWeigth(6);
		resultGraph.addEdge("v1", "v5", edge4);
		
		WeightedNamedEdge edge5 = new WeightedNamedEdge("v5", "v4", true);
		edge5.setWeigth(1);
		resultGraph.addEdge("v5", "v4", edge5);
		
		WeightedNamedEdge edge6 = new WeightedNamedEdge("v4", "v6", true);
		edge6.setWeigth(1);
		resultGraph.addEdge("v4", "v6", edge6);
		
		WeightedNamedEdge edge7 = new WeightedNamedEdge("v3", "v4", true);
		edge7.setWeigth(4);
		resultGraph.addEdge("v3", "v4", edge7);
		
		list.add("v1");
		list.add("v2");
		list.add("v3");
		list.add("v5");
		list.add("v4");
		list.add("v6");
		
		resultPath.setVertexes(list);

	}
	

	@Test
	public void testFail()
	{
		// Prueft, dass kein Weg gefunden wurde, gegen die "Einbahnstrasse"
		Assert.assertEquals(search.search(resultGraph, "v3", "v1").getVertexes().size(), 0);
	}

	@Test
	public void testBestPath()
	{
		// Prueft, dass vom Wert her beste Weg gefunden wurde.
		Assert.assertEquals(search.search(graph2, "a", "c").getVertexes().size(), 3);
	}
	
	@Test
	public void testBestPath_resultGraph() {
		// Shortest Way
		Assert.assertEquals(resultPath.getVertexes(),(search.search(resultGraph, "v1", "v6")).getVertexes());

	}
	
	@Test
	public void testSuccess()
	{
		// Prueft ob ein Weg mit dem Parser gefunden wurde.
		Assert.assertTrue(search.search(resultGraph, "v1", "v6").getVertexes().size() > 0);
	}
}
