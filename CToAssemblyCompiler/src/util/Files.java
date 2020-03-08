package util;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;

public class Files
{
    public static BufferedReader openInputFile(String fileName)
    {
    	BufferedReader reader = null;
        
        if (fileName != null)
        {
            try
            {
                reader = new BufferedReader( new InputStreamReader( new FileInputStream(fileName) ) );
            }

            catch (FileNotFoundException ex)
            {
            	reader = null;
                IO.printlnErr(IO.createFileErrorMsg("File not found", fileName));
            }
        }
        
        return reader;
    }
    
    public static PrintStream openOutputFile(String fileName)
    {
    	PrintStream printStream = null;
        
        if (fileName != null)
        {
            try
            {
            	printStream = new PrintStream(fileName);
            }

            catch (FileNotFoundException ex)
            {
            	printStream = null;
                IO.printlnErr(IO.createFileErrorMsg("File not found", fileName));
            }
        }
        
        return printStream;
    }

    public static boolean closeInputFile(BufferedReader reader)
    {
    	boolean success = false;
    	
    	if (reader != null)
    	{
    		try
			{
    			reader.close();
				success = true;
			}
    		
			catch (IOException e)
			{
				e.printStackTrace();
			}
    	}
    	
    	return success;
    }

    public static boolean closeOutputFile(PrintStream printStream)
    {
    	boolean success = false;
    	
    	if (printStream != null)
    	{
			printStream.close();
			success = true;
    	}
    	
    	return success;
    }
}
