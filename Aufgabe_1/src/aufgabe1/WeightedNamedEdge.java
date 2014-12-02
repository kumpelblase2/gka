package aufgabe1;

import org.jgrapht.graph.DefaultEdge;

public class WeightedNamedEdge extends DefaultEdge
{
	private final String m_source;
	private final String m_target;
	private int m_weigth = Integer.MIN_VALUE;
	private String m_name;
	private final boolean m_directed;
	
	//NEU für Ford-Fulkerson-Algorithmus (Initialisierung)
	private int currentFlow;
	public int getCurrentFlow(){
		return currentFlow;
	}
	public void setCurrentFlow(int inFlow){
		currentFlow += inFlow;
	}

									//Start          Ende              gerichtet?
	public WeightedNamedEdge(String inSource, String inTarget, boolean inDirected)
	{
		super();
		this.m_source = inSource;
		this.m_target = inTarget;
		this.m_directed = inDirected;
		//NEU für Ford-Fulkerson-Algorithmus (Initialisierung)
		this.currentFlow = 0;
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
		String returnName = (isDirected() ? "->" : "--");
		if(getName() != null)
			returnName += " " + getName();

		if(hasWeigth())
			returnName += " : " + getWeigth();

		return returnName;
	}
	
	//toString() for FordFulkerson
		public String toString2()
		{
			String returnName = "";
			String isdirected = (isDirected() ? "-> " : "-- ");

				returnName += getSource() + " " + isdirected;

				returnName += getTarget();	
				
			if(hasWeigth())
				returnName += " : " + getWeigth();
			
			if(hasCurrentFlow())
				returnName += "("+currentFlow+")";
			
			return returnName;
		}

		private boolean hasCurrentFlow(){
			if(currentFlow>=0){
				return true;	
			}
			else{
				return false;
			}
		}

	@Override
	public boolean equals(Object o)
	{
		if(this == o) return true;
		if(o == null || getClass() != o.getClass()) return false;

		WeightedNamedEdge that = (WeightedNamedEdge)o;

		if(m_directed != that.m_directed) return false;
		if(m_weigth != that.m_weigth) return false;
		if(m_name != null ? !m_name.equals(that.m_name) : that.m_name != null) return false;
		if(!m_source.equals(that.m_source)) return false;
		if(!m_target.equals(that.m_target)) return false;

		return true;
	}

	@Override
	public int hashCode()
	{
		int result = m_source.hashCode();
		result = 31 * result + m_target.hashCode();
		result = 31 * result + m_weigth;
		result = 31 * result + (m_name != null ? m_name.hashCode() : 0);
		result = 31 * result + (m_directed ? 1 : 0);
		return result;
	}
}
