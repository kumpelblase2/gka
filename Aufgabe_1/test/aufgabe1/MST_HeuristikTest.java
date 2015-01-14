package aufgabe1;

import static org.junit.Assert.*;

import java.util.List;

import aufgabe1.WeightedNamedEdge;

import org.jgrapht.Graph;
import org.junit.Before;
import org.junit.Test;

public class MST_HeuristikTest {
	
	Graph<String, WeightedNamedEdge> testGraph;
	int weightAll;
	
	@Before
	public void setUp() throws Exception {
		testGraph = new DefaultGraph();
		
		testGraph.addVertex("a");
		testGraph.addVertex("b");
		testGraph.addVertex("c");
		testGraph.addVertex("d");
		testGraph.addVertex("e");
		testGraph.addVertex("f");
		
		WeightedNamedEdge edgeAB = new WeightedNamedEdge("a", "b", false);
		edgeAB.setWeigth(20);
		testGraph.addEdge("a", "b", edgeAB);
		WeightedNamedEdge edgeAC = new WeightedNamedEdge("a", "c", false);
		edgeAC.setWeigth(10);
		testGraph.addEdge("a", "c", edgeAC);
		WeightedNamedEdge edgeAD = new WeightedNamedEdge("a", "d", false);
		edgeAD.setWeigth(15);
		testGraph.addEdge("a", "d", edgeAD);
		WeightedNamedEdge edgeAE = new WeightedNamedEdge("a", "e", false);
		edgeAE.setWeigth(18);
		testGraph.addEdge("a", "e", edgeAE);
		WeightedNamedEdge edgeAF = new WeightedNamedEdge("a", "f", false);
		edgeAF.setWeigth(18);
		testGraph.addEdge("a", "f", edgeAF);
		
		WeightedNamedEdge edgeBC = new WeightedNamedEdge("b", "c", false);
		edgeBC.setWeigth(10);
		testGraph.addEdge("b", "c", edgeBC);
		WeightedNamedEdge edgeBD = new WeightedNamedEdge("b", "d", false);
		edgeBD.setWeigth(15);
		testGraph.addEdge("b", "d", edgeBD);
		WeightedNamedEdge edgeBE = new WeightedNamedEdge("b", "e", false);
		edgeBE.setWeigth(5);
		testGraph.addEdge("b", "e", edgeBE);
		WeightedNamedEdge edgeBF = new WeightedNamedEdge("b", "f", false);
		edgeBF.setWeigth(12);
		testGraph.addEdge("b", "f", edgeBF);
		
		WeightedNamedEdge edgeCD = new WeightedNamedEdge("c", "d", false);
		edgeCD.setWeigth(24);
		testGraph.addEdge("c", "d", edgeCD);
		WeightedNamedEdge edgeCE = new WeightedNamedEdge("c", "e", false);
		edgeCE.setWeigth(5);
		testGraph.addEdge("c", "e", edgeCE);
		WeightedNamedEdge edgeCF = new WeightedNamedEdge("c", "f", false);
		edgeCF.setWeigth(13);
		testGraph.addEdge("c", "f", edgeCF);
		
		WeightedNamedEdge edgeDE = new WeightedNamedEdge("d", "e", false);
		edgeDE.setWeigth(16);
		testGraph.addEdge("d", "e", edgeDE);
		WeightedNamedEdge edgeDF = new WeightedNamedEdge("d", "f", false);
		edgeDF.setWeigth(8);
		testGraph.addEdge("d", "f", edgeDF);
		
		WeightedNamedEdge edgeEF = new WeightedNamedEdge("e", "f", false);
		edgeEF.setWeigth(9);
		testGraph.addEdge("e", "f", edgeEF);
		
		MST_Heuristik mstHeu = new MST_Heuristik();
		Path pathResult = mstHeu.search(testGraph, "a", "a");
		
		//weight-of-all-edges of the path
		weightAll = 0; 
		Graph<String, WeightedNamedEdge> graphForWeightMeasurement = createEulerCircleWithWeight(pathResult.getVertexes());
		for(WeightedNamedEdge edge : graphForWeightMeasurement.edgeSet()){
			weightAll = weightAll+edge.getWeigth();
		}
		System.out.println("JUNIT - Weight of final path-Graph: "+weightAll);
		
	}
	
	//method for creating "Euler-Kreis with weights" as a graph
	private Graph<String, WeightedNamedEdge> createEulerCircleWithWeight(List<String> inList){
		//System.out.println("inList: "+inList);
		Graph<String, WeightedNamedEdge> eulerCircleWithWeights = new DefaultGraph();
		for(int i = 0; i < inList.size()-1; i++){
			//add Vertices
			eulerCircleWithWeights.addVertex(inList.get(i));
			eulerCircleWithWeights.addVertex(inList.get(i+1));
			//Create directed Edge
			WeightedNamedEdge edge = new WeightedNamedEdge(inList.get(i), inList.get(i+1), true);
			edge.setWeigth( testGraph.getEdge(inList.get(i), inList.get(i+1)).getWeigth());
			eulerCircleWithWeights.addEdge(inList.get(i), inList.get(i+1), edge);
			//System.out.println("JUNIT - Added: "+edge.toString2());
		}
		return eulerCircleWithWeights;
	}

	@Test
	public void test() {
		assertTrue(weightAll <= (2*37)); //Algorithm works better or equal to factor 2 (double of MST)
	}

}
