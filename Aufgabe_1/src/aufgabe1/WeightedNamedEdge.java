package aufgabe1;

import org.jgrapht.graph.DefaultEdge;

public class WeightedNamedEdge extends DefaultEdge
{
	private final String m_source;
	private final String m_target;
	private int m_weigth = Integer.MIN_VALUE;
	private String m_name;
	private final boolean m_directed;

									//Start          Ende              gerichtet?
	public WeightedNamedEdge(String inSource, String inTarget, boolean inDirected)
	{
		super();
		this.m_source = inSource;
		this.m_target = inTarget;
		this.m_directed = inDirected;
	}

	public int getWeigth()
	{
		return (hasWeigth() ? this.m_weigth : 1);
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

	public boolean hasWeigth()
	{
		return this.m_weigth != Integer.MIN_VALUE;
	}
	//toString()
	public String toString()
	{
		String returnName = "";
		if(getName() != null)
			returnName += (isDirected() ? "->" : "--") + " " + getName();

		if(hasWeigth())
			returnName += (returnName.length() > 0 ? " : " : "") + getWeigth();

		return returnName;
	}
}
