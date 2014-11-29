package aufgabe1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jgrapht.Graph;
import org.junit.*;

public class BIG_FW_versus_D {

	final FWSearcher searchFW = new FWSearcher();
	final DijkstraSearcher searchD = new DijkstraSearcher();
	
	//BIG with 100 Vertices and 6000 Edges
	
	//BIG
    File file = new File("./Aufgabe_1/resources/big.gka");
    Graph<String, WeightedNamedEdge> big = GraphParser.parse(file);
    private List<String> listBig = new ArrayList<String>();
    private List<String> listBig2 = new ArrayList<String>();
    
    /*
	GeneratorProperties prop = new GeneratorProperties(6000, 6000, 100, 100, true);
	Graph<String, WeightedNamedEdge> big = GraphGenerator.generate(prop);
	private List<String> listBIG = new ArrayList<String>();
	private Path resultPathBIG = new Path();
	*/

	@Before
	public void setUp() throws Exception {
		System.out.println(new File(".").getAbsolutePath());
		listBig.add("v3");
		listBig.add("v78");
		listBig.add("v45");
		listBig.add("v26");
		
		listBig2.add("v6");
		listBig2.add("v32");
		listBig2.add("v70");
	}

	@Test
	public void testShortestWayBIG_1() {
		Assert.assertEquals(listBig,(searchFW.search(big, "v3", "v26")).next()); // FW: [v3, v78, v45, v26] - 11026 Zugriffe
		Assert.assertEquals(listBig,(searchD.search(big, "v3", "v26")).next()); // Dij: [v3, v78, v45, v26] - 105 Zugriffe
	}
	
	@Test
	public void testShortestWayBIG_2() {
		Assert.assertEquals(listBig2,(searchFW.search(big, "v6", "v70")).next()); // FW: [v6, v32, v70] - 11026 Zugriffe
		Assert.assertEquals(listBig2,(searchD.search(big, "v6", "v70")).next()); // Dij: [v6, v32, v70] - 105 Zugriffe
	}

}
