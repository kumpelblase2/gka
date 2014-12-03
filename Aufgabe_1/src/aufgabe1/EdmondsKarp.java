package aufgabe1;

import java.util.*;
import org.jgrapht.Graph;

public class EdmondsKarp implements SearchAlgorithm{
	
	    //for initializing
		public int INFINITY = Integer.MAX_VALUE; 
		//max flow
		public int max;
		//total flow at the end
		public int flow;
		//length of listOfVerticesForInspection
		public int length;
		//Senke
		public String Senke;
		
		public Path search(Graph<String, WeightedNamedEdge> inGraph, String inStart, String inEnd)
		{
			
			Path path2 = new Path();
			//
			BFSSearcher bfs = new BFSSearcher();
			Path pathList = bfs.search(inGraph, inStart, inEnd);
			
			Senke = inEnd;
			
			//List for all INSPECTED vertices 
			List<String> listOfInspectedVertices = new ArrayList<String>();
			
			//max d (Summe der inzidenten (Output) Kanten der Quelle) 
			for(WeightedNamedEdge edge : inGraph.edgesOf(inStart))
			{
				if(edge.getSource().equals(inStart)){
					max += edge.getWeigth();	
				}
			}
			//System.out.println("MaxD: "+ maxD);
			
			//Map for MARKED vertices
			Map<String, MarkedVertex> mapOfmarkedVertices = new HashMap<String, MarkedVertex>();
		
			//List of MARKED but not INSPECTED vertices
			List<String> listOfVerticesForInspection = new ArrayList<>();
			length = listOfVerticesForInspection.size();
			
			//First MARKED vertex is always "Quelle"
			MarkedVertex marked = new MarkedVertex();
			marked.name = inStart;
			
			//initializing with null (Quelle hat keinen Vorg)
			marked.vorgaenger = null;
			
			//initializing with infinity
			marked.currentInkrement = INFINITY;
			
			//First Map Entry is: "Quelle" : "Quelle"
			mapOfmarkedVertices.put(inStart, marked);
			
			boolean senkeWasMarked = true;
			
		//WHILE 1 START
			//So lange ein vergroe�ernder Fluss gefunden wird
			System.out.println(pathList.getPathAmount());
		while(pathList.hasMore()){
			System.out.println("***START OF WHILE 1***");
			
			List<String> currentPath = pathList.next();
			System.out.println("Current path: " + currentPath);
			senkeWasMarked = false;
			int rounds = 1;
			
			//FOR  Start
			for(int i = 0; i < currentPath.size()-1; i++)
			{	
				//get "Quelle"
				String currentVertex = currentPath.get(i);
				//get next to "Quelle"
				String currentNeighbor = currentPath.get(i+1);
				
				//Get Map Value of current, name it node
				MarkedVertex node = mapOfmarkedVertices.get(currentVertex);
				
				//FOR-Schleife START
			    //Get Edge
				WeightedNamedEdge edge = inGraph.getEdge(currentVertex, currentNeighbor);
					
				System.out.println("Edge: "+edge.toString2());
				int possibleCapacity = edge.getWeigth();
				//Condition: f < c
				if(edge.getCurrentFlow() < possibleCapacity)
				{
					//Create new MarkedVertex
					MarkedVertex newMarked = new MarkedVertex();
					//His own name = name of target
					newMarked.name = currentNeighbor;
					//Direction (true = plus)
					newMarked.direction = true;
				    //Vorgaenger is current
					newMarked.vorgaenger = currentVertex;
					//set possible Inkrement                                             =Vorgaenger (am Anfang INFINITY)
					int minimum = Math.min((possibleCapacity-edge.getCurrentFlow()), node.currentInkrement);
					newMarked.currentInkrement = minimum;
					mapOfmarkedVertices.put(currentNeighbor, newMarked);

					if(currentNeighbor.equals(Senke))
					{
						System.out.println("Senke erreicht");
						senkeWasMarked = true;
					}
			    }
				else
					break;
				
				rounds++;
				System.out.println("- - - - - - - - - - - -");
			}//FOR END
			System.out.println("---END OF FOR---");
			
			if(!senkeWasMarked)
				continue;
			//Build Path
			System.out.println("++++Building Path++++");
			List<String> path = new ArrayList<>();
			
			MarkedVertex current2 = mapOfmarkedVertices.get(inEnd);
			while(current2 != null)
			{
				path.add(current2.name);
				current2 = mapOfmarkedVertices.get(current2.vorgaenger);
			}
			Collections.reverse(path);
			System.out.println(path);
			//f um Inkrement erh�hen bzw. erniedrigen
			for(int i = 0; i < path.size()-1; i++){
				//Set mit allen Kanten von jeweils zwei Knoten aus dem Path
				Set<WeightedNamedEdge> mapOfEdges = inGraph.getAllEdges(path.get(i), path.get(i+1));
				for(WeightedNamedEdge edge : mapOfEdges){
					System.out.println("Kante vor �nderung: "+edge.toString2());
					//Add Increment to current flow of the plus-directed edges
					if(mapOfmarkedVertices.get(edge.getTarget()).direction==true){
						edge.setCurrentFlow(mapOfmarkedVertices.get(Senke).currentInkrement);
					}
					//Decrease Increment from current flow of the minus-directed edges
					else{
						edge.setCurrentFlow(-(mapOfmarkedVertices.get(Senke).currentInkrement));
					}
					
					System.out.println("Kante nach �nderung: "+edge.toString2());
				}
			}
			
			System.out.println("--------------");
			
			//Print Map
			if(senkeWasMarked){
				System.out.println("+++Printing Map+++");
				for (Map.Entry<String, MarkedVertex> entry : mapOfmarkedVertices.entrySet())
				{
					System.out.println(entry.getKey() + "/" + entry.getValue());
				}
				System.out.println("___________________________________");
			}
			
			//SET VALUES to NULL for next round (except inStart)
			for (Map.Entry<String, MarkedVertex> entry : mapOfmarkedVertices.entrySet())
			{
			    if(!entry.getKey().equals(inStart))
			    mapOfmarkedVertices.put(entry.getKey(),new MarkedVertex());
			}
			
			
		}
		System.out.println("END OF WHILE 1");
		//END OF WHILE 1
		
		/*
		if(!mapOfmarkedVertices.containsKey(inEnd)){
			return path;
		}
		*/

		for(WeightedNamedEdge edge : inGraph.edgesOf(inStart))
		{
			if(edge.getSource().equals(inStart)){
			   flow += edge.getCurrentFlow();
			}
		}
				System.out.println("Max flow could be: "+max);
				System.out.println("Actual flow is: "+flow);
			
		
			return path2;
		}
		
		//Private Class "MarkedVertex"
		public static class MarkedVertex{
			public String name;
			public boolean direction; // true = plus
			public int currentInkrement;
			public String vorgaenger;
			
			public String toString(){
				return "Name: "+name+"("+(direction==true ? "+" : "-")+""+vorgaenger+", "+currentInkrement+")";
			}
		}

}
