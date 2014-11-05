package aufgabe1.gui;

import javax.swing.*;
import java.io.File;
import aufgabe1.*;
import aufgabe1.GraphGenerator.GeneratorProperties;
import org.jgrapht.Graph;

public class BIGGraphGen
{
	public static void main(String[] args)
	{
		Graph<String, WeightedNamedEdge> bigGraph = GraphGenerator.generate(new GeneratorProperties(1000, 1100, 100, 110, true));
		JFileChooser choose = new JFileChooser(new File("."));
		choose.setFileFilter(new GKAFileFilter());
		choose.showSaveDialog(null);
		if(choose.getSelectedFile() != null)
		{
			GraphParser.parse(bigGraph, choose.getSelectedFile());
		}
	}
}