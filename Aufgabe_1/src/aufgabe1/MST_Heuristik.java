package aufgabe1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jgrapht.Graph;

import aufgabe1.gui.ShowMST;

public class MST_Heuristik implements SearchAlgorithm{
	
	List<WeightedNamedEdge> edgesOfMST = new ArrayList<>();
	List<WeightedNamedEdge> edgesOfGraphSorted = new ArrayList<>();

	@Override
	public Path search(Graph<String, WeightedNamedEdge> inGraph, String inStart, String inEnd) {
		
		Path path = new Path();
		
		//return empty path, if inStart or inEnd are not in the Graph
		if(!inGraph.containsVertex(inStart)){
			return path;
		}
			
		// TODO Indexing all Vertices for Kruskal Algorithm #35
		Map<String, Integer> indexedVerticesMap = new HashMap<>();
		int index = 0;
		for(String vertex : inGraph.vertexSet()){
			indexedVerticesMap.put(vertex, index);
			index++;
		}
		
		// TODO Kruskal Algorithm #36
		
		//indexing edges (increasing of weights)
		for(WeightedNamedEdge edge : inGraph.edgeSet()){
			insertEdge(edge, 0);
		}
		
		//edgesOfMST
		for(WeightedNamedEdge edge : edgesOfGraphSorted){
			
			int indexOfSource = indexedVerticesMap.get(edge.getSource());
			int indexOfTarget = indexedVerticesMap.get(edge.getTarget());
			
			if(indexOfSource != indexOfTarget){
				//add to edgesOfMST
				edgesOfMST.add(edge);
				
				//set ALL values of the bigger index to the smaller one
				int min_alpha = Math.min(indexOfSource, indexOfTarget);
				int max_beta = Math.max(indexOfSource, indexOfTarget);
				
				for(Map.Entry<String, Integer> entry : indexedVerticesMap.entrySet()){
					if(entry.getValue()==max_beta){
						entry.setValue(min_alpha);
					}
				}
			}
		}
		
		System.out.println("***Printing MST-Edges***");
		for(WeightedNamedEdge edge : edgesOfMST){
			System.out.println(edge.toString2());
		}
		
		
		// TODO Convert to "Eulerschen Graphen" #37
		Graph<String, WeightedNamedEdge> graphEuler = new DefaultGraph();
		for(WeightedNamedEdge edge : edgesOfMST){
			System.out.println("Source: "+edge.getSource()+" Target: "+ edge.getTarget()+" Edge: "+edge.toString2());
			
			//add vertices FIRST!!!
			graphEuler.addVertex(edge.getSource());
			graphEuler.addVertex(edge.getTarget());
			
			//add edge SECOND!!!
			graphEuler.addEdge(edge.getSource(), edge.getTarget(), edge);
			System.out.println("added");
			//clone edge
			WeightedNamedEdge cloned = edge.clone();
			//add cloned-edge
			graphEuler.addEdge(edge.getSource(), edge.getTarget(), cloned);
		}
		
		ShowMST mst = new ShowMST(graphEuler);
		
		
		// TODO Fleury Algorithm #38
		

		
		
		//returning Euler-Tour (START and END same vertex!!!)
		return path;
			
	}
	
	//method for sorting edges (increasing of weights)
	private void insertEdge(WeightedNamedEdge inEdge, int index){
		//begin (list is empty) OR end (all entries had been smaller)
		if(edgesOfGraphSorted.size()==index){
			edgesOfGraphSorted.add(index, inEdge);
		}
		else if(inEdge.getWeigth() <= edgesOfGraphSorted.get(index).getWeigth()){
			edgesOfGraphSorted.add(index, inEdge);
		}
		else{
			index = index+1;
			insertEdge(inEdge, index);
		}
	}

}
