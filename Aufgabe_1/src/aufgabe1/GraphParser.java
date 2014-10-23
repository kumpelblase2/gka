package aufgabe1;

import java.io.*;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jgrapht.Graph;
import org.jgrapht.graph.*;

public class GraphParser
{
	public static final Pattern REGEX = Pattern.compile("([a-zA-Z0-9]+) ?(-(>|-) ?([a-zA-Z0-9]+) ?(\\(([a-zA-Z0-9]+)\\))?)? ?( ?: ?([0-9]+))?;");
	private String m_content = "";

	public GraphParser(String inContent)
	{
		this.m_content = inContent;
	}

	public GraphParser(File inFile)
	{
		this.m_content = this.readFile(inFile);
	}

	private String readFile(final File inFile)
	{
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), Charset.forName("ISO-8859-1")));
			String currentLine;
			String whole = "";
			while((currentLine = reader.readLine()) != null)
			{
				whole += currentLine + "\n";
			}

			return whole;
		}
		catch(Exception e)
		{
		}
		finally
		{
			if(reader != null)
			{
				try
				{
					reader.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}

		return "";
	}

	public Graph<String, WeightedNamedEdge> parse()
	{
		Matcher matcher = REGEX.matcher(this.m_content);
		Graph<String, WeightedNamedEdge> graph;
		if(this.m_content.contains("->"))
			graph = new DefaultDirectedGraph<String, WeightedNamedEdge>(WeightedNamedEdge.class);
		else
			graph = new DefaultGraph();

		while(matcher.find())
		{
			String vertexStart = matcher.group(1);
			String edgeType = matcher.group(3);
			String vertexEnd = matcher.group(4);
			String edgeName = matcher.group(6);
			String edgeWeight = matcher.group(8);

			WeightedNamedEdge edge = null;
			//add Vertex to the graphT
			graph.addVertex(vertexStart);
			//if there is an edge between two vertices
			if(vertexEnd != null)
			{
				edge = new WeightedNamedEdge(vertexStart, vertexEnd, edgeType.equals(">"));
				graph.addVertex(vertexEnd);

				//ggf Gewichtung hinzufuegen
				if (edgeWeight != null)
					edge.setWeigth(Integer.parseInt(edgeWeight));

				//ggf Namen hinzufuegen
				if (edgeName != null)
					edge.setName(edgeName);

				graph.addEdge(vertexStart, vertexEnd, edge);
			}
		}

		return graph;
	}
}
