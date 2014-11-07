package aufgabe1;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.jgrapht.Graph;

public class FWSearcher implements SearchAlgorithm {
	
	Path path = new Path();
	ArrayList<String> vertices = new ArrayList<>();
	String vertexToAdd = "";
	
	public Path search(Graph<String, WeightedNamedEdge> inGraph, String inStart, String inEnd){
		
		//Start or End-Vertex is not in Graph
		if(!inGraph.containsVertex(inStart) || !inGraph.containsVertex(inEnd)){
			return path;
		}
		
		int numberOfVertecis = inGraph.vertexSet().size(); 
		path.setSteps(path.getSteps() + 1);
		//Set -> ArrayList
		vertices.addAll(inGraph.vertexSet());
		
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
		
		//path finding
		int i = vertices.indexOf(inStart);
		int j = vertices.indexOf(inEnd);
		
		//if matrixD(inStart, inEnd) = MAX INTEGER (there no way!)
		if(matrixD.get(i).get(j)==Integer.MAX_VALUE){
			return path;
		}
		
		//else
		//Start-End representation in T-Matrix
		int t_matrix_current = matrixT.get(i).get(j);
		
		//add End-Vertex
		path.getVertexes().add(inEnd);
		
		//for saving current
		int t_matrix_last = j; //start with End-Vertex
		
		//stop when (Start-Vertex, t_matrix_current) < 0 --> no shorter "way around" (-1 in matrixT)
		while(t_matrix_current >= 0){
			
			//Add found vertex to the path
			vertexToAdd = vertices.get(t_matrix_current);
			path.getVertexes().add(vertexToAdd);
				
				//if there is a shorter "way around" through another vertex
				int checker = matrixT.get(t_matrix_current).get(t_matrix_last);
				//Check it
				check(checker);
			
					//save t_matrix_current for next round
					t_matrix_last = t_matrix_current;
					//set t_matrix_current new
					t_matrix_current = matrixT.get(i).get(t_matrix_current);
		}
		
		printD(matrixD);
		printT(matrixT);
		
		//add Start-Vertex
		path.getVertexes().add(inStart);
		
		Collections.reverse(path.getVertexes());
		
		System.out.println(path.getVertexes());
		
		return path;
	}
	
	//Checker-Method
	private void check(int toCheck){
		if(toCheck!=-1){
			System.out.println("found:"+vertices.get(toCheck));
			int indexOfaddedVertex = path.getVertexes().indexOf(vertexToAdd);
			path.getVertexes().add(indexOfaddedVertex, vertices.get(toCheck));
		    }
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
	public void printT(ArrayList<ArrayList<Integer>> matrix){
		System.out.println("Matrix T:");
		ArrayList<ArrayList<Integer>> m_print = new ArrayList<>();
		
		for(ArrayList<Integer> aL : matrix){
			m_print.add(aL);
		}
		
		for(ArrayList<Integer> list : m_print){
			for(int z=0; z < list.size(); z++){
				Integer zahl = list.get(z);
					list.remove(z);
					list.add(z, (zahl+1));
			}
		}
		
		for(int i=0; i < m_print.size(); i++){
			System.out.println(m_print.get(i));
		}
		System.out.println("------------------------");
	}

}
