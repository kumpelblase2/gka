package aufgabe1;

import java.util.ArrayList;

import org.jgrapht.Graph;

public class FWSearcher {
	
	public static Path search(Graph<String, WeightedNamedEdge> inGraph, String inStart, String inEnd){
		
		Path path = new Path();
		
		/*
		
		//Start or End-Vertex is not in Graph
		if(!inGraph.containsVertex(inStart) || !inGraph.containsVertex(inEnd)){
			return path;
		}
		
		*/
		
		int numberOfVertecis = inGraph.vertexSet().size(); 
		ArrayList<String> vertices = new ArrayList<>(inGraph.vertexSet());
		
		System.out.println(vertices);
		System.out.println("----------------------");
		
		//Matrix D (2-D Array)
		ArrayList<ArrayList<Integer>> matrixD = new ArrayList<>();
		for(int i=0; i < numberOfVertecis; i++){
			matrixD.add(i, new ArrayList<Integer>());
		}
		
		//Matrix T (2-D Array)
		ArrayList<ArrayList<Integer>> matrixT = new ArrayList<>();
		for(int i=0; i < numberOfVertecis; i++){
				matrixT.add(i, new ArrayList<Integer>());
		}
		
		//Filling Matrix D
		int indexOfArray=0;
		for(String knoten : vertices){
			
			for(int index=0; index < vertices.size(); index++){
				int weight;
				//Distance to your own
				if(knoten.equals(vertices.get(index))){
					weight=0;
				}
				//there is "no" edge to the vertex
				else if(inGraph.getAllEdges(knoten, vertices.get(index)).size()==0){
					weight=9999;
				}
				//there is an edge to the vertex
				else{
					weight = inGraph.getEdge(knoten, vertices.get(index)).getWeigth();
				}
				matrixD.get(indexOfArray).add(index, weight);
			}
			indexOfArray++;
		}
		
		//Filling Matrix T (with Zeros)
		for(ArrayList<Integer> array: matrixT){
			for(int i=0; i < vertices.size(); i++){
				array.add(0);
			}
		}
		
		//Print matrix D in Console
		for(int i=0; i < matrixD.size(); i++){
			System.out.println(matrixD.get(i));
		}
		
		System.out.println("----------------------");
		
		//Print matrix T in Console
		for(int i=0; i < matrixD.size(); i++){
			System.out.println(matrixT.get(i));
		}
		
		//Algorithmus
		for(int j=0; j < vertices.size(); j++ ){
			for(int i=0; i < vertices.size(); i++){
				//i and j have to be different
				if(i!=j){
					for(int k=0; k < vertices.size(); k++){
						//k and j have to be different
						if(k!=j){
							int temp_ik = matrixD.get(i).get(k);
							int temp_ij = matrixD.get(i).get(j);
							int temp_jk = matrixD.get(j).get(k);
							//get minimum
							int minimum = Math.min(temp_ik, (temp_ij + temp_jk));
							if(temp_ik!=minimum){
								//remove old value
								matrixD.get(i).remove(k);
								//add minimum as new value
								matrixD.get(i).add(k, minimum);
								//set matrixT_ik
								matrixT.get(i).remove(k);
								matrixT.get(i).add(k, (j+1)); //j+1 because we start with j=0
							}
							
						}
					}
							
				}	
			}		
		}
		
		System.out.println("----------------------");
		
		//Print matrix in Console
		for(int i=0; i < matrixD.size(); i++){
			System.out.println(matrixD.get(i));
		}
		System.out.println("----------------------");
		
		//Print matrix T in Console
		for(int i=0; i < matrixD.size(); i++){
			System.out.println(matrixT.get(i));
		}
		
		//path finding
		//TO-DO

		
		return path;
	}

}
