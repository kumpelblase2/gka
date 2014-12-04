package aufgabe1;

import java.util.*;
import org.jgrapht.Graph;

public class FordFulkerson implements SearchAlgorithm{

	//for initializing
	public int INFINITY = Integer.MAX_VALUE;
	//max flow
	public int max;
	//total flow at the end
	public int flow;
	//Get flow
	public int getFlow(){
		return flow;
	}
	//length of listOfVerticesForInspection
	public int length;
	//Senke
	public String Senke;

	public String toString()
	{
		return "Ford-Fulkersson";
	}

	public Path search(Graph<String, WeightedNamedEdge> inGraph, String inStart, String inEnd)
	{
		Senke = inEnd;
		//Creating new Path
		Path path = new Path();
		if(!inGraph.containsVertex(inStart) || !inGraph.containsVertex(inEnd))
			return path;

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

		//List of MARKED vertices
		List<String> listOfMarkedVertices = new ArrayList<>();

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
		while(senkeWasMarked==true){
			System.out.println("***START OF WHILE 1***");

			senkeWasMarked = false;
			int rounds = 1;

			//put "Quelle" to the "Marked"-List
			listOfMarkedVertices.add(inStart);
			//put "Quelle" to the "For Inspection"-List
			listOfVerticesForInspection.add(inStart);

			System.out.println("Liste Marked:"+listOfMarkedVertices);
			System.out.println("Liste For Inspection:"+listOfVerticesForInspection);

			System.out.println("---START OF WHILE 2---");

			//WHILE 2 Start
			//Ein Durchgang: So lange bis die Senke markiert wurde
			//und es noch markierte Knoten gibt, die noch nicht inspiziert wurden
			while(!listOfVerticesForInspection.isEmpty() && senkeWasMarked==false)
			{
				System.out.println("Durchgang: "+rounds);

				//possible vertices for choosing
				System.out.println("M�gliche Knoten: "+listOfVerticesForInspection);

				//the first current is "Quelle"
				String current = listOfVerticesForInspection.get(randomVertex());
				System.out.println("Gew�hlter Knoten: "+current);

				//Remove from list
				listOfVerticesForInspection.remove(current);

				//Add current to listOfInspectedVertices
				listOfInspectedVertices.add(current);
				System.out.println("Liste inspezierter Knoten: "+listOfInspectedVertices);

				//Get Map Value (MarkedVertex) of current, name it node
				MarkedVertex node = mapOfmarkedVertices.get(current);

				System.out.println("Map-Knoten: "+node);
				//FOR-Schleife START
				for(WeightedNamedEdge edge : inGraph.edgesOf(current))
				{
					//Get an Output-Edge
					if(!edge.getTarget().equals(current))
					{
						System.out.println("Output-Edge: "+edge.toString2());
						String target = edge.getTarget();
						int possibleCapacity = edge.getWeigth();
						//Condition: f < c
						if(edge.getCurrentFlow() < possibleCapacity)
						{
							//Vertex has not been MARKED so far:
							if(!listOfMarkedVertices.contains(target) && senkeWasMarked==false)
							{

								//Create new MarkedVertex
								MarkedVertex newMarked = new MarkedVertex();
								//His own name = name of target
								newMarked.name = target;
								//Direction (true = plus)
								newMarked.direction = true;
								//Vorgaenger is current
								newMarked.vorgaenger = current;
								//set possible Inkrement                                             =Vorgaenger (am Anfang INFINITY)
								int minimum = Math.min((possibleCapacity-edge.getCurrentFlow()), node.currentInkrement);
								newMarked.currentInkrement = minimum;

								//Add to Map
								mapOfmarkedVertices.put(target, newMarked);
								//Add to listOfMarkedVertices
								listOfMarkedVertices.add(target);
								//Add to listOfVerticesForInspection
								listOfVerticesForInspection.add(target);

								if(target.equals(Senke))
								{
									System.out.println("Senke erreicht");
									senkeWasMarked = true;
								}


							}


						}



					}
					//Get an Input-Edge
					else
					{
						System.out.println("Input-Edge: "+edge.toString2());
						String source = edge.getSource();
						//Condition: f > 0
						if(edge.getCurrentFlow() > 0)
						{
							//Vertex has not been MARKED so far:
							if(!listOfMarkedVertices.contains(source) && senkeWasMarked==false)
							{

								//Create new MarkedVertex
								MarkedVertex newMarked = new MarkedVertex();
								//His own name = name of source
								newMarked.name = source;
								//Direction (false = minus)
								newMarked.direction = false;
								//Vorgaenger is current
								newMarked.vorgaenger = current;
								//set possible Inkrement

								//aktueller Fluss		Inkrement of Vorgaenger
								int minimum = Math.min((edge.getCurrentFlow()), node.currentInkrement);
								newMarked.currentInkrement = minimum;

								//Add to Map
								mapOfmarkedVertices.put(source, newMarked);
								//Add to listOfMarkedVertices
								listOfMarkedVertices.add(source);
								//Add to listOfVerticesForInspection
								listOfVerticesForInspection.add(source);

								/*
								if(source.equals(Senke))
								{
									System.out.println("Senke erreicht");	
									senkeWasMarked = true;
								}
								*/
							}
						}
					}

				}//FOR END

				rounds++;
				System.out.println("- - - - - - - - - - - -");
			}//W2 END
			System.out.println("---END OF WHILE 2---");

			//Build Path
			System.out.println("++++Building Path++++");
			MarkedVertex current2 = mapOfmarkedVertices.get(inEnd);
			while(current2 != null)
			{
				path.getVertexes().add(current2.name);
				current2 = mapOfmarkedVertices.get(current2.vorgaenger);
			}
			Collections.reverse(path.getVertexes());
			System.out.println(path.getVertexes());
			//f um Inkrement erh�hen bzw. erniedrigen
			for(int i = 0; i < path.getVertexes().size()-1; i++){
				//Set mit allen Kanten von jeweils zwei Knoten aus dem Path
				Set<WeightedNamedEdge> mapOfEdges = inGraph.getAllEdges(path.getVertexes().get(i), path.getVertexes().get(i+1));
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

			//delete path
			path.setVertexes(new ArrayList<String>());

			//delete listOfInspectedVertices
			while(!listOfInspectedVertices.isEmpty()){
				listOfInspectedVertices.clear();
			}

			//delete listOfMarkedVertices
			while(!listOfMarkedVertices.isEmpty()){
				listOfMarkedVertices.clear();
			}

			//delete listOfVerticesForInspection
			while(!listOfVerticesForInspection.isEmpty()){
				listOfVerticesForInspection.clear();
			}

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
		if(path.getVertexes().isEmpty()){
			for(WeightedNamedEdge edge : inGraph.edgesOf(inStart))
			{
				if(edge.getSource().equals(inStart)){
					flow += edge.getCurrentFlow();
				}
			}
			System.out.println("Max flow could be: "+max);
			System.out.println("Actual flow is: "+flow);
		}

		path.addAlternative(new ArrayList<String>());
		return path;
	}

	//Random number between 0 and "listOfVericesForInspection.size()-1"
	//To pick random a vertex from the list
	public int randomVertex(){
		int min = 0;
		int randomNum = min + (int)(Math.random()*(length-1)); //length = Maximum
		return randomNum;
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
