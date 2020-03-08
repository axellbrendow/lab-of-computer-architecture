package main;

import java.io.File;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import compilers.CToMipsCompiler;
import util.IO;
import util.Regexes;

public class cac // cac = C to Assembly Compiler
{
	public static HashMap<String, String> identifiersAndRegisters = new HashMap<>();
	public static boolean noComment = false;
	
	public static String startingRegister = "t";
	public static int startingNumber = 9;
	public static int stepToNextRegister = -1;
	public static String inputFileName;
	public static String outputFileName;
	
	public static boolean isAValidRegister(String register)
	{
		return register.matches(CToMipsCompiler.CRE_VALID_REGISTER);
	}
	
	public static boolean getRegister(String register)
	{
		boolean success = true;
		Matcher tRegMatcher = Pattern.compile("[tT]([0-9])").matcher(register);
		Matcher sRegMatcher = Pattern.compile("[sS]([0-7])").matcher(register);
		Matcher aRegMatcher = Pattern.compile("[aA]([0-3])").matcher(register);
		
		if (tRegMatcher.matches())
		{
			startingRegister = "t";
			startingNumber = Integer.parseInt(tRegMatcher.group(1));
		}
		
		else if (sRegMatcher.matches())
		{
			startingRegister = "s";
			startingNumber = Integer.parseInt(sRegMatcher.group(1));
		}

		else if (aRegMatcher.matches())
		{
			startingRegister = "a";
			startingNumber = Integer.parseInt(aRegMatcher.group(1));
		}
		
		else
		{
			success = false;
			IO.printlnErr("Invalid register.");
			startingRegister = "t";
			startingNumber = 9;
			stepToNextRegister = -1;
		}
		
		return success;
	}
	
	public static boolean getIdentifiers(String arg)
	{
		boolean success = false;
		
		// Possibilities for arg:
		
		// variable:register
		// 		Variable will be replaced by a specific register.
		
		// register
		// 		"register" will be the first register to be used if necessary.
		// 		If more registers are needed, the next ones will be used. And
		// 		they are chosen in a decrescent manner, that is, if "register"
		// 		is "t9", the next will be "t8".
		
		// register:stepToNextRegister
		// 		"register" will be the first register to be used if necessary.
		// 		If more registers are needed, the next ones will be used. And
		// 		they are chosen according to the "step" given. "step" can be
		// 		a number between -9 and 9.
		
		String[] argFields = arg.split(":");
		
		if (argFields.length == 1)
		{
			getRegister(argFields[0]);
			success = true;
		}
		
		else if (argFields.length == 2)
		{
			// try to get the register from the first field
			if (getRegister(argFields[0])) // case "register:stepToNextRegister"
			{
				if (argFields[1].matches("[-+]?[0-9]")) // check if the "step" is between -9 e 9
				{
					stepToNextRegister = Integer.parseInt(argFields[1]);
					success = true;
				}
				
				else
				{
					IO.printlnErr("Number out of range [-9, 9].");
					stepToNextRegister = -1;
				}
			}
			
			// try to get from the second field
			else if (isAValidRegister(argFields[1])) // case "variable:register"
			{
				// check if the variable name is coherent 
				if (argFields[0].matches("[a-zA-Z_][a-zA-Z0-9]*"))
				{
					identifiersAndRegisters.put(argFields[0], argFields[1]);
					success = true;
				}
				
				else
				{
					IO.printlnErr("Bad variable name.");
				}
			}
		}
		
		return success;
	}
	
	public static boolean interpretArg(String arg)
	{
		boolean success = false;
		
		if (arg.contains(".")) // files to compile need an extension
		{
			if (new File(arg).exists())
			{
				inputFileName = arg;
				outputFileName = arg.replaceAll("\\..*", "") + ".asm";
				success = true;
			}
			
			else
			{
				IO.printlnErr(IO.createFileErrorMsg("File not found", arg));
			}
		}
		
		// checks if the region from the index 0 to arg.length() of arg
		// matches with "nocomment" considering the same region.
		else if (arg.regionMatches(true, 0, "nocomment", 0, arg.length()))
		{
			noComment = true;
			success = true;
		}
		
		else
		{
			success = getIdentifiers(arg);
		}
		
		return success;
	}
	
	public static boolean interpretArgs(String[] args)
	{
		boolean success = true;

		for (int i = 0; i < args.length && success; i++) // Percorre os argumentos.
		{
			success = interpretArg(args[i]);
		}
		
		return success;
	}
	
	public static void main(String[] args)
	{
		/*Matcher matcher = Pattern.compile(Regexes.CRE_VARIABLE_DECLARATION).matcher("var indiceFinalDeOrdenacao = inicio + numeroDeElementos - 1;");
		IO.println("-> " + matcher.matches());
		IO.println("-> " + matcher.groupCount());
		IO.println("a-> " + matcher.group(Regexes.GROUP_ASSIGN_EXPRESSION));
		for (int i = 0; i < matcher.groupCount(); i++)
		{
			IO.println(i + "-> " + matcher.group(i + 1));
		}*//*
		Matcher matcher = Pattern.compile(Regexes.CRE_IMEDIATE).matcher("inicio + numeroDeElementos - 1");
		IO.println("-> " + matcher.matches());
		IO.println("-> " + matcher.group());
		IO.println("-> " + matcher.groupCount());
		for (int i = 0; i < matcher.groupCount(); i++)
		{
			IO.println(i + "-> " + matcher.group(i + 1));
		}*/
		if (args != null && args.length > 0)
		{
			if (!interpretArgs(args))
			{
				IO.printlnErr("\nCompilation ended with errors.");
			}
			
			else
			{
				new CToMipsCompiler(
					identifiersAndRegisters,
					inputFileName,
					outputFileName,
					startingRegister,
					startingNumber,
					stepToNextRegister,
					noComment
				).compile();
			}
		}
	}

}
