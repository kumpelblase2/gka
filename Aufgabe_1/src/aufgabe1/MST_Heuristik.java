package aufgabe1;

import org.jgrapht.Graph;

public class MST_Heuristik implements SearchAlgorithm{

	@Override
	public Path search(Graph<String, WeightedNamedEdge> inGraph, String inStart, String inEnd) {
		
		Path path = new Path();
		
		//return empty path, if inStart or inEnd are not in the Graph
		if(!inGraph.containsVertex(inStart) || !inGraph.containsVertex(inEnd)){
			return path;
		}
			
		// TODO Indexing all Vertices for Kruskal Algorithm #35
		
		
		// TODO Kruskal Algorithm #36
		
		
		// TODO Convert to "Eulerschen Graphen" #37
		
		
		// TODO Fleury Algorithm #38
		

		
		
		//returning Euler-Tour
		return path;
			
	}

}
