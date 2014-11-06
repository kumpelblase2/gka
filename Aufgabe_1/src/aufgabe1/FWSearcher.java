package aufgabe1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;

public class FWSearcher implements SearchAlgorithm {
	
	public Path search(Graph<String, WeightedNamedEdge> inGraph, String inStart, String inEnd){
		
		Path path = new Path();
		
		//Start or End-Vertex is not in Graph
		if(!inGraph.containsVertex(inStart) || !inGraph.containsVertex(inEnd)){
			return path;
		}
		
		
		int numberOfVertecis = inGraph.vertexSet().size(); 
		//Set -> ArrayList
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
					weight=Integer.MAX_VALUE;
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
							if(temp_ij < Integer.MAX_VALUE && temp_jk < Integer.MAX_VALUE)
							{
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
		int i = vertices.indexOf(inStart);
		int j = vertices.indexOf(inEnd);
		
		//if matrixD(inStart, inEnd) = MAX INTEGER (there no way!)
		if(matrixD.get(i).get(j)==Integer.MAX_VALUE){
			return path;
		}
		
		//else
		int t_temp = matrixT.get(i).get(j);
		
		path.getVertexes().add(inEnd);
		
		while(t_temp > 0){
			t_temp = t_temp-1; //because we had added +1
			//System.out.println(t_temp);
			path.getVertexes().add(vertices.get(t_temp));
			//System.out.println(path.getVertexes());
			t_temp = matrixT.get(i).get(t_temp);
		}
		path.getVertexes().add(inStart);
		
		Collections.reverse(path.getVertexes());
		
		System.out.println(path.getVertexes());
		
		return path;
	}

}
