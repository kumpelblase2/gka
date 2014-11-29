package aufgabe1;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import org.jgrapht.Graph;
import org.junit.*;

public class BIG_with_100_Vertices {
	
	final FWSearcher searchFW = new FWSearcher();
	final FWSearcher searchFW2 = new FWSearcher();
	
	final DijkstraSearcher searchD = new DijkstraSearcher();
	final DijkstraSearcher searchD2 = new DijkstraSearcher();
	
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
		listBig.add("v6");
		listBig.add("v55");
		listBig.add("v59");
		listBig.add("v69");

		listBig2.add("v50");
		listBig2.add("v2");
		listBig2.add("v8");
		listBig2.add("v5");
		
	}

	//FW
	@Test
	public void testShortestWayBIG() {
		Assert.assertEquals(listBig,(searchFW.search(big, "v6", "v69")).next());
		Assert.assertEquals(listBig2,(searchFW2.search(big, "v50", "v5")).next());
	}
	
	//Dij
	@Test
	public void testShortestWayBIG2() {
		//Assert.assertEquals(resultPathBig.getVertexes(),(searchD.search(big, "v6", "v69")).getVertexes()); //[v6, v73, v76, v69]
		//Assert.assertEquals(resultPathBig2.getVertexes(),(searchD2.search(big, "v50", "v5")).getVertexes()); //[v5, v24, v102, v5]
	}
	

}
