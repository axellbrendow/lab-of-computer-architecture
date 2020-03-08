package executer;

import Util.Array;
import Util.AxellIO;

public class Executer
{
	ArgsSender argsSender; // classe que comunicara' com o Arduino
	String[] filesNames; // lista de arquivos a serem executados
	
	/**
	 * Inicia o executador com a lista de arquivos a serem executados.
	 * 
	 * @param filesNames Lista com os nomes dos arquivos.
	 */
	
	public Executer(String portaCOM, String... filesNames)
	{
		argsSender = new ArgsSender(portaCOM);
		
		this.filesNames = filesNames;
	}
	
	public boolean executeFile(int fileIndex)
	{
		boolean success = false;
		
		AxellIO.saveInputStream();
		
		if (!Array.indexIsOutOfBounds(fileIndex, filesNames) &&
			AxellIO.setInputStream(filesNames[fileIndex]))
		{
			String line;
			
			AxellIO.printlnS();
			
			while (AxellIO.isPossibleToRead())
			{
				AxellIO.pause("Pressione ENTER para continuar...");
				
				line = AxellIO.readLine();
				
				if (!line.isEmpty())
				{
					argsSender.callProgram(line);
				}
				
				else
				{
					AxellIO.println("\nPrograma finalizado");
				}
			}
			
			success = AxellIO.closeInputStream();
		}
		
		AxellIO.recoverSavedInputStream();
		
		return success;
	}
	
	public boolean execute()
	{
		boolean success = true;
		
		if (Array.exists(filesNames))
		{
			for (int i = 0; i < filesNames.length; i++)
			{
				if (!executeFile(i))
				{
					success = false;
				}
			}
		}
		
		return success;
	}
}
