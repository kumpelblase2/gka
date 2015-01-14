package aufgabe1;

import java.io.File;
import java.util.List;
import org.jgrapht.Graph;
import org.junit.*;

public class NearestNeighborTest
{
	private Graph<String, WeightedNamedEdge> m_graph;
	private NearestNeighborSearch m_nearestNeighborSearch;

	@Before
	public void setup()
	{
		this.m_graph = GraphParser.parse(new File("./Aufgabe_1/resources/graph5.gka"));
		this.m_nearestNeighborSearch = new NearestNeighborSearch();
	}

	@Test
	public void testPath()
	{
		Path result = this.m_nearestNeighborSearch.search(this.m_graph, null, null);
		Assert.assertTrue(result.hasMore());
		List<String> next = result.next();
		Assert.assertTrue(next.containsAll(this.m_graph.vertexSet()));
		Assert.assertTrue(next.size() == this.m_graph.vertexSet().size() + 1);
	}
}
