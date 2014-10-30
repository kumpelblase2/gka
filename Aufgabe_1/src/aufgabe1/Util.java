package aufgabe1;

import java.io.*;
import java.nio.charset.Charset;

public class Util
{
	public static String readFile(final File inFile)
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

	public static void writeFile(File inToWriteTo, String inToWrite)
	{
		String[] lines = inToWrite.split("\n");
		BufferedWriter writer = null;
		try
		{
			writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(inToWriteTo), Charset.forName("ISO-8859-1")));
			for(String line : lines)
			{
				writer.write(line);
				writer.write("\n");
				writer.flush();
			}
			writer.flush();
		}
		catch(Exception e)
		{
		}
		finally
		{
			try
			{
				if(writer != null)
				{
					writer.close();
				}
			}
			catch(Exception e)
			{
			}
		}
	}
}