package aufgabe1.gui;

import javax.swing.filechooser.FileFilter;
import java.io.File;

public class GKAFileFilter extends FileFilter
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
}