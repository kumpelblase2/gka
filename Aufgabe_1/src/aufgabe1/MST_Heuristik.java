package aufgabe1;

import aufgabe1.WeightedNamedEdge;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

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
			String source = edge.getSource();
			String target = edge.getTarget();
			System.out.println("Source: "+source+" Target: "+ target +" Edge: "+edge.toString2());
			
			//add vertices FIRST!!!
			graphEuler.addVertex(source);
			graphEuler.addVertex(target);
			
			//add edges SECOND!!!
			WeightedNamedEdge newEdge1 = new WeightedNamedEdge(source, target, true);
			graphEuler.addEdge(source, target, newEdge1);
			System.out.println("added newEdge1");
			
			WeightedNamedEdge newEdge2 = new WeightedNamedEdge(target, source, true);
			graphEuler.addEdge(source, target, newEdge2);
			System.out.println("added newEdge2");
			
		}
		
		//ShowMST mst = new ShowMST(graphEuler);
		
		
		// TODO Generate Eulerkreis #38
		
		//Initializing some variables
		int numberOfVerticesInGraph = inGraph.vertexSet().size();
		String theStartEndVertex;
		String theChosenOne;
		Stack<String> stackOfVertices = new Stack<>();
		List<String> verticesOfeulerKreis = new ArrayList<>();
		List<String> eulerKreis = new ArrayList<>();
		boolean unmarkedVertexWasFound;
		
		//chose one vertex of the Euler-graph to be the Start and End of the Circle
		for(String vertex : graphEuler.vertexSet()){
			verticesOfeulerKreis.add(vertex);	
		}
		
		System.out.println(verticesOfeulerKreis);
		theStartEndVertex = verticesOfeulerKreis.get(0);
		

		//push Start/End-vertex to the Stack
		stackOfVertices.push(theStartEndVertex);

		//add Start/End-vertex to the Euler-Kreis
		eulerKreis.add(theStartEndVertex);
		
		
		//WHILE1 START (not all Vertices has been marked)
		while(!stackOfVertices.isEmpty()){
		
			//look what is on top of the stack, take it for "theChosenOne"
			theChosenOne = stackOfVertices.peek();
			System.out.println("i took: "+theChosenOne);
			
			//set unmarkedVertexWasFound to false
			unmarkedVertexWasFound = false;
			
			//WHILE2 START (edge with unmarked Vertex was found)
			while(unmarkedVertexWasFound == false){
				for(WeightedNamedEdge edge : graphEuler.edgesOf(theChosenOne)){
					//the found target vertex was not marked yet
					if((!eulerKreis.contains(edge.getTarget())) && (edge.getTarget() != theChosenOne)){
						System.out.println("found new unmarked vertex: "+edge.getTarget());
						eulerKreis.add(edge.getTarget());
						stackOfVertices.add(edge.getTarget());
						unmarkedVertexWasFound = true;
					}
				}
				//no unmarked target vertex was found:
				//remove theChosenOne from stack
				String toBeRemoved = stackOfVertices.pop();
				System.out.println("found NO unmarked, removing: "+ toBeRemoved);
				unmarkedVertexWasFound = true;
			}
			//WHILE2 END
			
		}
		//WHILE1 END
		
		//add Start/End Vertex to complete the Circle
		eulerKreis.add(theStartEndVertex);
		
		//check if all vertices has been detected
		if(graphEuler.vertexSet().size()==numberOfVerticesInGraph){
			System.out.println("all detected");
			//creating Path
			path.setVertexes(eulerKreis);
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
