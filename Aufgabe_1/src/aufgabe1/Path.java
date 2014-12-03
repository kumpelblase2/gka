package aufgabe1;

import java.util.*;

public class Path
{
	private int m_steps = 0;
	private List<String> m_vertexes_fordFulkerson;
	private List<List<String>> m_vertexes = new ArrayList<>();
	private int index = 0;
	
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
		return this.m_vertexes.get(index++);
	}

	public boolean hasMore()
	{
		return this.m_vertexes.size() > index;
	}

	public int getPathAmount()
	{
		return this.m_vertexes.size();
	}
	
	//NEU f�r FordFulkerson
	public List<String> getVertexes() {
		return m_vertexes_fordFulkerson;
	}
	
	public void setVertexes(List<String> m_vertexes) {
		this.m_vertexes_fordFulkerson = m_vertexes;
	}
	
	public Path() {
		super();
		this.m_vertexes_fordFulkerson = new ArrayList<>();
	}
}