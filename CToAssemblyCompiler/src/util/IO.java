package util;

/**
 * Classe para manuseamento de entrada e saída.
 * 
 * @author Axell Brendow ( https://github.com/axell-brendow )
 */

public class IO
{
	// Output functions
	public static void print(Object msg)
	{
		System.out.print(msg);
	}
	
	public static void println(Object msg)
	{
		print(msg + System.lineSeparator());
	}
	
	public static void printErr(Object msg)
	{
		System.err.print(msg);
	}
	
	public static void printlnErr(Object msg)
	{
		printErr(msg + System.lineSeparator());
	}
    
	// Error report functions
    public static String createFileErrorMsg(String msg, String fileName)
    {
        String errorMsg = "";
        
        if (msg != null && fileName != null)
        {
            errorMsg = msg + ". (name = '" + fileName + "')";
        }
        
        return errorMsg;
    }
}
