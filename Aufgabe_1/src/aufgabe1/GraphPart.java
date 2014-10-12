package aufgabe1;

public class GraphPart
{
	private final String m_vertex;
	private final WeightedNamedEdge m_edge;

	public GraphPart(final String inVertex, final WeightedNamedEdge inEdge)
	{
		m_vertex = inVertex;
		m_edge = inEdge;
	}

	public String getVertex()
	{
		return m_vertex;
	}

	public WeightedNamedEdge getEdge()
	{
		return m_edge;
	}
}
