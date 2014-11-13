package aufgabe1;

import org.jgrapht.Graph;

public interface SearchAlgorithm
{
	public Path search(Graph<String, WeightedNamedEdge> inGraph, String inStart, String inEnd);
}