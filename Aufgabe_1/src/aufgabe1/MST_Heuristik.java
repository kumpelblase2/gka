package aufgabe1;

import java.util.*;
import aufgabe1.gui.ShowMST;
import org.jgrapht.Graph;

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
		
		int weightOfAllEdgesMST = 0;
		//System.out.println("***Printing MST-Edges***");
		for(WeightedNamedEdge edge : edgesOfMST){
			//System.out.println(edge.toString2());
			weightOfAllEdgesMST = weightOfAllEdgesMST+edge.getWeigth();
		}
		//Print weight-of-all-edges of the 
		System.out.println("Weight-Of-All-Edges-MST: "+weightOfAllEdgesMST);
		
		
		// TODO Convert to "Eulerschen Graphen" #37
		Graph<String, WeightedNamedEdge> graphEuler = new DefaultGraph();
		for(WeightedNamedEdge edge : edgesOfMST){
			String source = edge.getSource();
			String target = edge.getTarget();
			//System.out.println("Source: "+source+" Target: "+ target +" Edge: "+edge.toString2());
			
			//add vertices FIRST!!!
			graphEuler.addVertex(source);
			graphEuler.addVertex(target);
			
			//add edges SECOND!!!
			WeightedNamedEdge newEdge1 = new WeightedNamedEdge(source, target, true);
			newEdge1.setWeigth(edge.getWeigth());
			graphEuler.addEdge(source, target, newEdge1);
			//System.out.println("added: "+newEdge1.toString2());
			
			WeightedNamedEdge newEdge2 = new WeightedNamedEdge(target, source, true);
			newEdge2.setWeigth(edge.getWeigth());
			graphEuler.addEdge(target, source, newEdge2);
			//System.out.println("added: "+newEdge2.toString2());
			
		}
		
		/*
		System.out.println("Anzahl der Kanten: "+graphEuler.edgeSet().size());
		for(WeightedNamedEdge edge : graphEuler.edgeSet()){
			System.out.println(edge.toString2());
		}
		*/
		
		ShowMST mst = new ShowMST(graphEuler);
		
		
		// TODO Generate Eulerkreis #38
		
		//Initializing some variables
		int numberOfVerticesInGraph = inGraph.vertexSet().size();
		String theStartEndVertex;
		String theChosenOne;
		Stack<String> stackOfVertices = new Stack<>();
		List<String> verticesOfeulerKreis = new ArrayList<>();
		List<String> eulerKreis = new ArrayList<>();
		boolean unmarkedVertexWasFound;
		
		//add all vertices to the ArrayList
		for(String vertex : graphEuler.vertexSet()){
			verticesOfeulerKreis.add(vertex);	
		}
		
		System.out.println("Vertices Of Eulerkreis: "+verticesOfeulerKreis);
		
		//chose one vertex of the Euler-graph to be the Start and End of the Circle
		
		//THE TOTAL WEIGHT DEPENDS ON THE FIRST CHOSEN VERTEX!!! (Weight-in-total:)
		theStartEndVertex = verticesOfeulerKreis.get(0);   //55 -> "c" was chosen first in JUnit Test Graph
		//theStartEndVertex = verticesOfeulerKreis.get(1);   //71 -> "e"
		//theStartEndVertex = verticesOfeulerKreis.get(2);   //66 -> "b"
		//theStartEndVertex = verticesOfeulerKreis.get(3);   //67 -> "d"
		//theStartEndVertex = verticesOfeulerKreis.get(4);   //71 -> "f"
		//theStartEndVertex = verticesOfeulerKreis.get(5);   //55 -> "a" was chosen first in JUnit Test Graph
		
		//push Start/End-vertex to the Stack
		stackOfVertices.push(theStartEndVertex);

		//add Start/End-vertex to the Euler-Kreis
		eulerKreis.add(theStartEndVertex);
		
		
		//WHILE1 START (not all Vertices has been marked)
		while(!stackOfVertices.isEmpty()){
		
			//look what is on top of the stack, take it for "theChosenOne"
			theChosenOne = stackOfVertices.peek();
			System.out.println("On the top of the stack is: "+theChosenOne);
			
			//set unmarkedVertexWasFound to false
			unmarkedVertexWasFound = false;
			
			//WHILE2 START (edge with unmarked Vertex was found)
			while(unmarkedVertexWasFound == false){
				for(WeightedNamedEdge edge : graphEuler.edgesOf(theChosenOne)){
					//the found target vertex was not marked yet
					if((!eulerKreis.contains(edge.getTarget())) && (edge.getTarget() != theChosenOne)){
						System.out.println("found new unmarked vertex: "+edge.getTarget());
						System.out.println("Put "+edge.getTarget()+" on the stack");
						eulerKreis.add(edge.getTarget());
						stackOfVertices.add(edge.getTarget()); //will be the next "theChosenOne"
						unmarkedVertexWasFound = true;
						break; //stop looking
					}
				}
				
				if(unmarkedVertexWasFound == false){
				//no unmarked target vertex was found:
				//remove theChosenOne from stack
				String toBeRemoved = stackOfVertices.pop();
				System.out.println("found NO unmarked, removing: "+ toBeRemoved);
				unmarkedVertexWasFound = true;
				}
				
			}
			//WHILE2 END
			
		}
		//WHILE1 END
		
		//add Start/End Vertex to complete the Circle
		eulerKreis.add(theStartEndVertex);
		
		//check if all vertices has been detected
		if(graphEuler.vertexSet().size()==numberOfVerticesInGraph){
			//System.out.println("all detected");
			//creating Path
			path.setVertexes(eulerKreis);
			path.addAlternative(eulerKreis);
			//Show Euler-Kreis as a JDialog
			//ShowMST eulerCircle = new ShowMST(createEulerCircle(eulerKreis));
		}
		
		System.out.println("EulerKreis: "+eulerKreis);
		//returning Euler-Kreis (START and END same vertex!!!)
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
	
	//method for creating Euler-Kreis as a graph
	private Graph<String, WeightedNamedEdge> createEulerCircle(List<String> inList){
		Graph<String, WeightedNamedEdge> eulerCircle = new DefaultGraph();
		for(int i = 0; i < inList.size()-1; i++){
			//add Vertices
			eulerCircle.addVertex(inList.get(i));
			eulerCircle.addVertex(inList.get(i+1));
			//Create directed Edge
			WeightedNamedEdge edge = new WeightedNamedEdge(inList.get(i), inList.get(i+1), true);
			eulerCircle.addEdge(inList.get(i), inList.get(i+1), edge);
		}
		return eulerCircle;
	}
	

}
