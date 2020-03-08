package executer;

import java.io.IOException;

import Util.Array;
import Util.AxellIO;
import Util.MyString;

public class ArgsSender
{
	public static final String NOME_DO_PROGRAMA = "envia.exe";
	private String portaCOM = "com1"; // NÃO SE ESQUEÇA DE COLOCAR A PORTA DO ARDUINO !!!!!
	
	public ArgsSender(String portaCOM)
	{
		if (Array.exists(portaCOM))
		{
			setPortaCOM(portaCOM);
		}
	}
	
	public void setPortaCOM(String portaCOM)
	{
		if (MyString.matchesCaseInsensitive("com\\d{1}", portaCOM))
		{
			this.portaCOM = portaCOM;
		}
		
		else
		{
			AxellIO.printError(
				getClass(),
				"Porta COM inválida (" + portaCOM + ")",
				"setPortaCOM(String)"
			);
		}
	}
	
	/**
	 * Chama o programa {@link #NOME_DO_PROGRAMA} fazendo-o enviar
	 * {@code arguments} pela porta {@link #portaCOM} do computador.
	 *  
	 * @param arguments Argumentos do programa {@link #NOME_DO_PROGRAMA}. 
	 * 
	 * @return o tempo, em milisegundos, desde a inicialização do processo
	 * do programa até o seu término.
	 */
	
	public Long callProgram(String arguments)
	{
		ProcessBuilder processBuilder = new ProcessBuilder(NOME_DO_PROGRAMA, portaCOM, arguments);
		Process process;
		Long startTime;
		Long timeDifference = (long) -1;
		
		try
		{
			process = processBuilder.start();
			startTime = System.nanoTime();
			
			process.waitFor(); // espera o processo acabar
			
			// em milisegundos
			timeDifference = (long) ( (System.nanoTime() - startTime) / Math.pow(10, 6) );
		}
		
		catch (IOException ioex)
		{
			ioex.printStackTrace();
		}
		
		catch (InterruptedException iex)
		{
			iex.printStackTrace();
		}
		
		return timeDifference;
	}
}
