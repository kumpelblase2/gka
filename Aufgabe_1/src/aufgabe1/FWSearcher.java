package aufgabe1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;
import org.jgrapht.util.PrefetchIterator.NextElementFunctor;

public class FWSearcher implements SearchAlgorithm {
	
	Path path = new Path();
	ArrayList<String> vertices = new ArrayList<>();
	String vertexToAdd = "";
	int numberOfVertecis = 0;
	ArrayList<ArrayList<Integer>> matrixD;
	ArrayList<ArrayList<Integer>> matrixT;
	
	public Path search(Graph<String, WeightedNamedEdge> inGraph, String inStart, String inEnd){
		
		//Start or End-Vertex is not in Graph
		if(!inGraph.containsVertex(inStart) || !inGraph.containsVertex(inEnd)){
			return path;
		}
		
		//Number of Vertices in the Graph
		numberOfVertecis = inGraph.vertexSet().size(); 
		
		//Add one step
		path.setSteps(path.getSteps() + 1);
		
		//Set -> ArrayList
		vertices.addAll(inGraph.vertexSet());
		
		System.out.println(vertices);
		System.out.println("----------------------");
		
		//Matrix D (2-D Array)
		matrixD = new ArrayList<>();
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
				path.setSteps(path.getSteps() + 1);
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
		
		//Filling Matrix T (with "-1")
		for(ArrayList<Integer> array: matrixT){
			for(int i=0; i < vertices.size(); i++){
				array.add(-1);
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
		
		//Algorithms
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
									
									matrixT.get(i).add(k, j);
								}
							}
							
						}
					}
							
				}	
			}		
		}
		
		//indices of Start and End for path finding
		int i = vertices.indexOf(inStart);
		int j = vertices.indexOf(inEnd);
		
		//if matrixD(inStart, inEnd) = MAX INTEGER (there no way!)
		if(matrixD.get(i).get(j)==Integer.MAX_VALUE){
			return path;
		}
		
		//else
		
		//path
		ArrayList<String> path_temp = new ArrayList<>();
		
		//Add Start / End to the path_temp
		path_temp.add(vertices.get(i));
		path_temp.add(vertices.get(j));
		System.out.println("Path: "+path_temp);
		System.out.println("--------------");
		
		//Break Condition for the while-loop
		boolean vertexWasAdded = true;
		
		while(vertexWasAdded==true){
			vertexWasAdded=false;
			//i.e. path_temp = [v1, v4, v6] -> check (v1,v4) and (v4,v6)
			for(int index = 0; index < path_temp.size()-1; index++){  //-1 because the last vertex is at the right position
				int temp_int = matrixT.get(vertices.indexOf(path_temp.get(index))).get(vertices.indexOf(path_temp.get(index+1)));
				if(temp_int != -1){
					String vertexFound = vertices.get(temp_int);
					System.out.println("check("+path_temp.get(index)+","+path_temp.get(index+1)+")");
					System.out.println("Another vertex was found: "+vertexFound);
					System.out.println("--------------");
					path_temp.add(index+1, vertexFound);
					System.out.println("Path: "+path_temp);
					System.out.println("--------------");
					vertexWasAdded = true;
				}
				else{
					System.out.println("check("+path_temp.get(index)+","+path_temp.get(index+1)+")");
					System.out.println("No vertex was found in T-Matrix");
					System.out.println("--------------");
				}
			}
			
		}
		
		printD(matrixD);
		printT(matrixT);
		
		path.setVertexes(path_temp);
		
		System.out.println(path.getVertexes());
		
		return path;
	}
	
	//Print matrix D
	public void printD(ArrayList<ArrayList<Integer>> matrix){
		System.out.println("Matrix D:");
		for(int i=0; i < matrix.size(); i++){
			System.out.println(matrix.get(i));
		}
		System.out.println("------------------------");
	}
	
	//Print matrix T
	public void printT(ArrayList<ArrayList<Integer>> matrixT){
		System.out.println("Matrix T:");
		
		//plus 1, because "internal" sight is -1
		for(ArrayList<Integer> list : matrixT){
			for(int z=0; z < list.size(); z++){
				Integer zahl = list.get(z);
					list.remove(z);
					list.add(z, (zahl+1));
			}
		}
		
		for(int i=0; i < matrixT.size(); i++){
			System.out.println(matrixT.get(i));
		}
		System.out.println("------------------------");
	}

}
