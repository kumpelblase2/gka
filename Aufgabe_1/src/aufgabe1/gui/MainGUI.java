package aufgabe1.gui;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import aufgabe1.GraphParser;

public class MainGUI
{
	public static void main(String[] args)
	{
		JFileChooser fileChooser = new JFileChooser(new File("."));
		fileChooser.setFileFilter(new FileFilter()
		{
			@Override
			public boolean accept(final File f)
			{
				return f.isDirectory() || (f.isFile() && f.getName().endsWith(".gka"));
			}

			@Override
			public String getDescription()
			{
				return "GKA Files";
			}
		});
		int result = fileChooser.showOpenDialog(null);
		if(result == JFileChooser.APPROVE_OPTION)
		{
			GraphParser parser = new GraphParser(fileChooser.getSelectedFile());
			MainWindow window = new MainWindow(parser.parse());
			window.setVisible(true);
		}
		else
			System.exit(0);
	}
}
