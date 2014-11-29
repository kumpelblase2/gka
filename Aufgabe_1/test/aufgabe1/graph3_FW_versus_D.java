package aufgabe1;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jgrapht.Graph;
import org.jgrapht.graph.DefaultDirectedGraph;
import org.junit.*;

public class graph3_FW_versus_D {

	final FWSearcher searchFW = new FWSearcher();
	final DijkstraSearcher searchD = new DijkstraSearcher();
	
	//graph3
	File file = new File("./Aufgabe_1/resources/graph3.gka");
	Graph<String, WeightedNamedEdge> graph3 = GraphParser.parse(file);
	private List<String> listGraph3 = new ArrayList<String>();
	
	//Result Graph
	private final Graph<String, WeightedNamedEdge> resultGraph = new DefaultDirectedGraph<>(WeightedNamedEdge.class);
	private List<String> list = new ArrayList<String>();
    private int stepsFW = 101;
    private int stepsD = 9;

	@Before
	public void setup()
	{
		
		//resultGraph
		resultGraph.addVertex("v1");
		resultGraph.addVertex("v2");
		resultGraph.addVertex("v3");
		resultGraph.addVertex("v4");
		resultGraph.addVertex("v5");
		resultGraph.addVertex("v6");
		resultGraph.addVertex("v7");
		resultGraph.addVertex("v8");
		resultGraph.addVertex("v9");
		resultGraph.addVertex("v10");

		WeightedNamedEdge edge1 = new WeightedNamedEdge("v1", "v2", true);
		edge1.setWeigth(1);
		resultGraph.addEdge("v1", "v2", edge1);
		
		WeightedNamedEdge edge2 = new WeightedNamedEdge("v2", "v3", true);
		edge2.setWeigth(1);
		resultGraph.addEdge("v2", "v3", edge2);
		
		WeightedNamedEdge edge3 = new WeightedNamedEdge("v1", "v5", true);
		edge3.setWeigth(6);
		resultGraph.addEdge("v1", "v5", edge3);
		
		WeightedNamedEdge edge4 = new WeightedNamedEdge("v3", "v5", true);
		edge4.setWeigth(1);
		resultGraph.addEdge("v3", "v5", edge4);
		
		WeightedNamedEdge edge5 = new WeightedNamedEdge("v5", "v4", true);
		edge5.setWeigth(4);
		resultGraph.addEdge("v5", "v4", edge5);
		
		WeightedNamedEdge edge6 = new WeightedNamedEdge("v2", "v6", true);
		edge6.setWeigth(2);
		resultGraph.addEdge("v2", "v6", edge6);
		
		WeightedNamedEdge edge7 = new WeightedNamedEdge("v6", "v5", true);
		edge7.setWeigth(2);
		resultGraph.addEdge("v6", "v5", edge7);
		
		WeightedNamedEdge edge8 = new WeightedNamedEdge("v7", "v2", true);
		edge8.setWeigth(2);
		resultGraph.addEdge("v7", "v2", edge8);
		
		WeightedNamedEdge edge9 = new WeightedNamedEdge("v8", "v7", true);
		edge9.setWeigth(4);
		resultGraph.addEdge("v8", "v7", edge9);
		
		WeightedNamedEdge edge10 = new WeightedNamedEdge("v3", "v4", true);
		edge10.setWeigth(4);
		resultGraph.addEdge("v3", "v4", edge10);
		
		WeightedNamedEdge edge11 = new WeightedNamedEdge("v4", "v8", true);
		edge11.setWeigth(1);
		resultGraph.addEdge("v4", "v8", edge11);
		
		WeightedNamedEdge edge12 = new WeightedNamedEdge("v8", "v9", true);
		edge12.setWeigth(1);
		resultGraph.addEdge("v8", "v9", edge12);
		
		WeightedNamedEdge edge13 = new WeightedNamedEdge("v9", "v7", true);
		edge13.setWeigth(2);
		resultGraph.addEdge("v9", "v7", edge13);
		
		WeightedNamedEdge edge14 = new WeightedNamedEdge("v10", "v5", true);
		edge14.setWeigth(2);
		resultGraph.addEdge("v10", "v5", edge14);
		
		list.add("v1");
		list.add("v2");
		list.add("v3");
		list.add("v4");
		list.add("v8");
		list.add("v9");
		list.add("v7");
		
		//graph3
		listGraph3.add("Hamburg");
		listGraph3.add("Bremen");
		listGraph3.add("Cuxhaven");
	}
	@Test
	public void testShortestWay() {
		Assert.assertEquals(list,(searchFW.search(resultGraph, "v1", "v7")).next()); // [v1, v2, v3, v4, v8, v9, v7]
		Assert.assertEquals(list,(searchD.search(resultGraph, "v1", "v7")).next()); // [v1, v2, v3, v4, v8, v9, v7]
	}
	
	@Test
	public void testShortestWayGraph3() {
		Assert.assertEquals(listGraph3,(searchFW.search(graph3, "Hamburg", "Cuxhaven")).next()); // FW: [Hamburg, Bremen, Cuxhaven] - 485 Zugriffe
		Assert.assertEquals(listGraph3,(searchD.search(graph3, "Hamburg", "Cuxhaven")).next()); //
	}
	
	
	@Test
	public void testNumberOfAccess() {
		Assert.assertEquals(stepsFW,(searchFW.search(resultGraph, "v1", "v7")).getSteps()); // 101
		Assert.assertEquals(stepsD,(searchD.search(resultGraph, "v1", "v7")).getSteps()); // 9
	}

}




