package aufgabe1;

import java.util.ArrayList;
import java.util.List;

public class Path
{
	private int m_steps = 0;
	private List<String> m_vertexes;
	
	public int getSteps() {
		return m_steps;
	}
	
	public void setSteps(int m_steps) {
		this.m_steps = m_steps;
	}
	
	public List<String> getVertexes() {
		return m_vertexes;
	}
	
	public void setVertexes(List<String> m_vertexes) {
		this.m_vertexes = m_vertexes;
	}
	
	public Path() {
		super();
		this.m_vertexes = new ArrayList<>();
	}
	
	
}