package aufgabe1;

import java.util.ArrayList;
import java.util.List;
import aufgabe1.gui.ShowMatrixT;
import org.jgrapht.Graph;

public class FWSearcher implements SearchAlgorithm {
	
	Path path = new Path();
	ArrayList<String> vertices = new ArrayList<>();
	String vertexToAdd = "";
	int numberOfVertecis = 0;
	Graph<String, WeightedNamedEdge> lastGraph;
	ArrayList<ArrayList<Integer>> matrixD;
	ArrayList<ArrayList<Integer>> matrixT;
	
	public ArrayList<ArrayList<Integer>> getMatrixT(){
		return matrixT;
	}
	
	public Path search(Graph<String, WeightedNamedEdge> inGraph, String inStart, String inEnd){
		path = new Path();
		//Start or End-Vertex is not in Graph
		if(!inGraph.containsVertex(inStart) || !inGraph.containsVertex(inEnd)){
			return path;
		}

		if(this.lastGraph == null || !this.lastGraph.equals(inGraph))
		{
			this.lastGraph = inGraph;
			buildMatrix(inGraph);
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
		
		//buildPath method
		List<String> endList = buildPath(path_temp);
		
		printD(matrixD);
		printT(matrixT);
		
		path.addAlternative(endList);
		
		System.out.println("FW: "+endList);
		System.out.println("FW: "+path.getSteps()+" Accesses needed");

		ShowMatrixT show = new ShowMatrixT(this.getMatrixT());
		show.setVisible(true);
		return path;
	}

	private void buildMatrix(final Graph<String, WeightedNamedEdge> inGraph)
	{
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
		matrixT = new ArrayList<>();
		for(int i=0; i < numberOfVertecis; i++){
			matrixT.add(i, new ArrayList<Integer>());
		}

		//Filling Matrix D
		int indexOfArray=0;
		for(String knoten : vertices){

			for(int index=0; index < vertices.size(); index++){
				//Add step
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
	}

	//building the Path, checking recursive
	public List<String> buildPath(List<String> inList){
		int first = vertices.indexOf(inList.get(0));
		int last = vertices.indexOf(inList.get(1));
		int check = matrixT.get(first).get(last);
		if(check == -1){
			//Break Condition
			return inList;
		}
		else{
			List<String> newList1 = new ArrayList<>();
			newList1.add(vertices.get(first));
			newList1.add(vertices.get(check));
			
			List<String> newList2 = new ArrayList<>();
			newList2.add(vertices.get(check));
			newList2.add(vertices.get(last));
			
			return concat(buildPath(newList1),buildPath(newList2));
		}
	}
	
	public List<String> concat(List<String> inList1, List<String> inList2){
		//get size for removing later the doubled Vertex
		int size = inList1.size();
		inList1.addAll(inList2);
		inList1.remove(size);
		return inList1;
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
		for(ArrayList<Integer> aMatrixT : matrixT)
		{
			List<Integer> copy = new ArrayList<>(aMatrixT);
			for(int z = 0; z < copy.size(); z++)
			{
				Integer zahl = copy.get(z);
				copy.remove(z);
				copy.add(z, (zahl + 1));
			}
			System.out.println(aMatrixT);
		}

		System.out.println("------------------------");
	}

	public String toString()
	{
		return "Floyd-Warshall Search";
	}
}
