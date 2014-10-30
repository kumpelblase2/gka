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
			"f ->g (testing);\n" +
			"h;\n";

	private final String[] fail = {
			"a -< b;",
			"a -- c",
			"b -> d test;"
	};

	private final Graph<String, WeightedNamedEdge> resultGraph = new DefaultDirectedGraph<>(WeightedNamedEdge.class);

	private final String resultString = "" +
			"a -> b;\n" +
			"a -> c;\n" +
			"e -> d : 5;\n" +
			"f -> g (testing);\n" +
			"h;\n";

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
		resultGraph.addVertex("h");

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
		Graph<String, WeightedNamedEdge> parsed = GraphParser.parse(success1);
		Assert.assertEquals(parsed.vertexSet(), resultGraph.vertexSet());
		Assert.assertEquals(parsed.edgeSet(), resultGraph.edgeSet());
	}

	@Test
	public void testParseFail()
	{
		for(String failed : fail)
		{
			Graph<String, WeightedNamedEdge> parsed = GraphParser.parse(failed);
			Assert.assertEquals("No error in vertexes when parsing " + failed, 0, parsed.vertexSet().size());
			Assert.assertEquals("No error in edges when parsing " + failed, 0, parsed.edgeSet().size());
		}
	}

	@Test
	public void testToStringParse()
	{
		String parsed = GraphParser.parse(resultGraph);
		Assert.assertEquals(parsed, resultString);
	}
}