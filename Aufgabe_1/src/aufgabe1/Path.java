package aufgabe1;

import java.util.*;

public class Path
{
	private int m_steps = 0;
	private List<List<String>> m_vertexes = new ArrayList<>();
	private final Iterator<List<String>> m_iterator = m_vertexes.iterator();
	
	public int getSteps() {
		return m_steps;
	}
	
	public void setSteps(int m_steps) {
		this.m_steps = m_steps;
	}

	public void addAlternative(List<String> inAlternative)
	{
		this.m_vertexes.add(inAlternative);
	}
	
	public List<String> next()
	{
		return this.m_iterator.next();
	}

	public boolean hasMore()
	{
		return this.m_iterator.hasNext();
	}
}