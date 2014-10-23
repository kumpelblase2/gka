package aufgabe1;

import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.*;

public class GraphParserTest
{
	private final String success1 = "" +
			"a -> b;\n" +
			"a->c;\n" +
			"e-> d : 5;\n" +
			"f ->g (testing);\n";

	private final String[] fail = {
			"a -< b;",
			"a -- c",
			"b -> d test;"
	};

	private final Graph<String, WeightedNamedEdge> resultGraph = new DefaultDirectedGraph<String, WeightedNamedEdge>(WeightedNamedEdge.class);

	@Before
	public void setup()
	{
		resultGraph.addVertex("a");
		resultGraph.addVertex("b");
		resultGraph.addVertex("c");
		resultGraph.addVertex("e");
		resultGraph.addVertex("d");
		resultGraph.addVertex("f");
		resultGraph.addVertex("g");

		resultGraph.addEdge("a", "b", new WeightedNamedEdge("a", "b", true));
		resultGraph.addEdge("a", "c", new WeightedNamedEdge("a", "c", true));
		WeightedNamedEdge weight = new WeightedNamedEdge("e", "d", true);
		weight.setWeigth(5);
		resultGraph.addEdge("e", "d", weight);
		WeightedNamedEdge named = new WeightedNamedEdge("f", "g", true);
		named.setName("testing");
		resultGraph.addEdge("f", "g", named);
	}

	@Test
	public void testParseSuccess()
	{
		Graph<String, WeightedNamedEdge> parsed = new GraphParser(success1).parse();
		Assert.assertEquals(parsed.vertexSet(), resultGraph.vertexSet());
		Assert.assertEquals(parsed.edgeSet(), resultGraph.edgeSet());
	}

	@Test
	public void testParseFail()
	{
		for(String failed : fail)
		{
			Graph<String, WeightedNamedEdge> parsed = new GraphParser(failed).parse();
			Assert.assertEquals(0, parsed.vertexSet().size());
			Assert.assertEquals(0, parsed.edgeSet().size());
		}
	}
}