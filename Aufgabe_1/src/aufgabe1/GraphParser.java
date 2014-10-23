package aufgabe1;

import java.io.*;
import java.nio.charset.Charset;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.jgrapht.Graph;
import org.jgrapht.graph.*;

public class GraphParser
{	
	//Pattern definieren für das Zeilenlayout
	public static final Pattern REGEX = Pattern.compile("([a-zA-Z0-9]+) ?(-(>|-) ?([a-zA-Z0-9]+) ?(\\(([a-zA-Z0-9]+)\\))?)? ?( ?: ?([0-9]+))?;");
	//zu parsender String
	private String m_content = "";
	
	//1. Konstruktor
	public GraphParser(String inContent)
	{
		this.m_content = inContent;
	}
	
	//2. Konstruktor übergebene Datei wird eingelesen
	public GraphParser(File inFile)
	{
		this.m_content = this.readFile(inFile); //beinhaltet dann das whole aus Zeile 38
	}
	
	//liest den kompletten Inhalt einer Datei und gibt diesen zurück
	private String readFile(final File inFile)
	{
		BufferedReader reader = null;
		try
		{
			reader = new BufferedReader(new InputStreamReader(new FileInputStream(inFile), Charset.forName("ISO-8859-1")));
			String currentLine;
			//gesamte String
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

	//Funktion zum eigenlichen Parsen
	public Graph<String, WeightedNamedEdge> parse()
	{
		//Matcher für den gegebenen String
		Matcher matcher = REGEX.matcher(this.m_content);
		Graph<String, WeightedNamedEdge> graph;
		//schaut ob gerichtet oder ungerichtet
		if(this.m_content.contains("->"))
			graph = new DefaultDirectedGraph<String, WeightedNamedEdge>(WeightedNamedEdge.class);
		else
			graph = new DefaultGraph();
		//so lange eine Zeile gefunden wird
		while(matcher.find())
		{
			//Zuweisung der Values
			String vertexStart = matcher.group(1);
			String edgeType = matcher.group(3);
			String vertexEnd = matcher.group(4);
			String edgeName = matcher.group(6);
			String edgeWeight = matcher.group(8);

			WeightedNamedEdge edge = null;
			//Knoten zum Graph hinzufügen
			graph.addVertex(vertexStart);
			//wenn eine Kante definiert wurde
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
				//Kante zum Graph hinzufügen
				graph.addEdge(vertexStart, vertexEnd, edge);
			}
		}
		
		return graph;
	}
}
