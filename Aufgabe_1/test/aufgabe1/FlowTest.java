package aufgabe1;

import static org.junit.Assert.*;

import java.io.File;

import org.jgrapht.Graph;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

public class FlowTest {

	static int fordFlow;
	static int edmondFlow;
	
	@BeforeClass
	public static void setUp() throws Exception {
		File file = new File("./resources/graph16(ford_fulk).gka");
		Graph graph = GraphParser.parse(file);
		FordFulkerson ford = new FordFulkerson();
		ford.search(graph, "q", "s");
		fordFlow = ford.getFlow();
		
		EdmondsKarp edmond = new EdmondsKarp();
		edmond.search(graph, "q", "s");
		edmondFlow = edmond.getFlow();
	}

	@Test
	public void test() {
		assertEquals(4, fordFlow);
		assertEquals(4, edmondFlow);
	}

}
