package aufgabe1;
import java.io.File;
import org.jgrapht.Graph;
import org.junit.BeforeClass;
import org.junit.Test;


public class Zeitmessung {

	public static Graph graph;
	
	@BeforeClass
	public static void setUp() throws Exception {
		File file = new File("./Aufgabe_1/resources/graph16(ford_fulk).gka");
		graph = GraphParser.parse(file);
	}

	@Test
	public void test() {
		long akkuFord = 0;
		long akkuEdmond = 0;
		
		long timeFord;
		long diffFord;
		
		long timeEdmond;
		long diffEdmond;
		
		for(int i = 0; i < 100; i++)
		{
		
		FordFulkerson ford = new FordFulkerson();
		timeFord = System.currentTimeMillis();
		ford.search(graph, "q", "s");
		diffFord = System.currentTimeMillis() - timeFord;
		akkuFord = akkuFord + diffFord;
		System.out.println("Ford: "+diffFord);
		
		EdmondsKarp edmond = new EdmondsKarp();
		timeEdmond = System.currentTimeMillis();
		edmond.search(graph, "q", "s");
		diffEdmond = System.currentTimeMillis() - timeEdmond;
		akkuEdmond = akkuEdmond + diffEdmond;
		System.out.println("Edmond: "+diffEdmond);
	    }
		
		System.out.println("Akku Ford: "+akkuFord);
		System.out.println("Akku Edmond: "+akkuEdmond);
		if(akkuFord < akkuEdmond){
			System.out.println("Ford is faster");
		}	
		else
		{
			double diff = akkuFord - akkuEdmond;
			System.out.println("Edmond is faster"+"("+(int)((((double)diff)/((double)akkuFord))*100)+"%)");
		}	
			System.out.flush();
	}
	
	
	

}
