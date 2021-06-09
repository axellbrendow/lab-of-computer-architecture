package compiler;

import java.io.File;

import Util.AxellIO;
import Util.MyString;

public class ULACompiler
{
	String A = "0";
	String B = "0";
	
	String[] filesNames; // lista de arquivos a serem compilados
	
	/**
	 * Inicia o compilar com a lista de arquivos a serem compilados.
	 * 
	 * @param filesNames Lista com os nomes dos arquivos.
	 */
	
	public ULACompiler(String... filesNames)
	{
		this.filesNames = filesNames;
	}
	
	private void makeAssignment(String line)
	{
		// obtem a variável e o valor da atribuição
		String[] assignmentTerms = line.split("=");
		String variable = assignmentTerms[0];
		String value = assignmentTerms[1];
		
		switch (variable)
		{
			case "A":
				A = value;
				break;
				
			case "B":
				B = value;
				break;
				
			default:
				AxellIO.printError
				(
					this.getClass(),
					"Variavel incompativel (" + variable + ")",
					"makeAssignment(MyString)"
				);
				break;
		}
	}
	
	private String compileLine(String fileLine)
	{
		String compiledLine = new String();
		String line = MyString.removeLastChar(fileLine);
		String tabulation =
			MyString.createClonesAndConcatThem
			(
				" ",
				Operations.getGreatestOperationName().length() + 1 - fileLine.length()
				
			) + "\t";
		
		if (line.contains("=")) // atribuição
		{
			makeAssignment(line);
			AxellIO.printlnS("Atribuição\t-> " + fileLine + tabulation + "A = " + A + tabulation + "B = " + B);
		}
		
		else // operação lógica
		{
			AxellIO.printS("Operação\t-> " + fileLine);
			
			compiledLine = A + B + Operations.convertToHex(line);
			
			AxellIO.printlnS(tabulation + "Compilação\t-> " + compiledLine);
		}
		
		return compiledLine;
	}
	
	private boolean closeStreams()
	{
		AxellIO.closePrintStream();
		
		return AxellIO.closeInputStream();
	}
	
	private boolean openStreamsFor(String fileName)
	{
		return
		fileName != null &&
		!fileName.isEmpty() &&
		AxellIO.setInputStream(new File(fileName)) &&
		AxellIO.setPrintStream
		(
			MyString.removePattern("\\.{1}.*", fileName)
			.concat(".hex")
		);
				
	}
	
	public boolean compile(String fileName)
	{
		boolean success = false;
		
		AxellIO.saveInputStream();
		AxellIO.savePrintStream();
		
		if (openStreamsFor(fileName))
		{
			String line = "";
			String compiledLine;
			
			do
			{
				line = AxellIO.readLinesUntilANonEmptyLine();
				
			} while (!line.contains("inicio") && AxellIO.isPossibleToRead());
			
			if (!line.contains("fim") && AxellIO.isPossibleToRead())
			{
				line = AxellIO.readLinesUntilANonEmptyLine();
				
				while (!line.contains("fim") && AxellIO.isPossibleToRead())
				{
					compiledLine = compileLine(line);
					
					if (!compiledLine.isEmpty())
					{
						AxellIO.println(compiledLine);
					}
					
					line = AxellIO.readLinesUntilANonEmptyLine();
				}
			}
			
			success = closeStreams() && !AxellIO.hasIOError();
		}
		
		AxellIO.recoverSavedInputStream();
		AxellIO.recoverSavedPrintStream();
		
		return success;
	}
	
	public boolean compile()
	{
		boolean success = false;
		
		if (filesNames != null)
		{
			success = true;
			int length = filesNames.length;
			
			for (int i = 0; i < length; i++)
			{
				if (!compile(filesNames[i]))
				{
					AxellIO.printFileError
					(
						this.getClass(),
						"A compilação falhou para o " + (i + 1) + "º arquivo.",
						filesNames[i],
						"compile()"
					);
					
					success = false;
				}
			}
		}
		
		else
		{
			AxellIO.printError(this.getClass(), "Nenhum arquivo informado.", "compile()");
		}
		
		return success;
	}
}
