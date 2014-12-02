package aufgabe1.gui;

import javax.swing.*;

import java.io.File;

import aufgabe1.FordFulkerson;
import aufgabe1.GraphParser;

public class MainGUI
{
	public static void main(String[] args)
	{
		JFileChooser fileChooser = new JFileChooser(new File("."));
		fileChooser.setFileFilter(new GKAFileFilter());
		int result = fileChooser.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION)
		{
			MainWindow window = new MainWindow(GraphParser.parse(fileChooser.getSelectedFile()));
			window.setVisible(true);
			//FordFulkerson ford = new FordFulkerson();
			//ford.search(GraphParser.parse(fileChooser.getSelectedFile()), "q", "s");
		}
		else
			System.exit(0);
	}
}
