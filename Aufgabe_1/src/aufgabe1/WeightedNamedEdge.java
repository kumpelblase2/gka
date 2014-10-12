package aufgabe1;

import org.jgrapht.graph.DefaultEdge;

public class WeightedNamedEdge extends DefaultEdge
{
	private final String m_source;
	private final String m_target;
	private int m_weigth = 0;
	private String m_name;
	private final boolean m_directed;

	public WeightedNamedEdge(String inSource, String inTarget, boolean inDirected)
	{
		super();
		this.m_source = inSource;
		this.m_target = inTarget;
		this.m_directed = inDirected;
	}

	public int getWeigth()
	{
		return m_weigth;
	}

	public void setWeigth(final int inWeigth)
	{
		m_weigth = inWeigth;
	}

	public String getName()
	{
		return m_name;
	}

	public void setName(final String inName)
	{
		m_name = inName;
	}

	@Override
	public String getSource()
	{
		return m_source;
	}

	@Override
	public String getTarget()
	{
		return m_target;
	}

	public boolean isDirected()
	{
		return m_directed;
	}
}
