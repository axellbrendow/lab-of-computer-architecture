import java.io.IOException;

import util.*;

public class ArgsSender
{
	public static final String NOME_DO_PROGRAMA = "envia.exe";
	public static final String PORTA_COM = "com4"; // NÃO SE ESQUEÇA DE COLOCAR A PORTA DO ARDUINO !!!!!
	
	public static final String[] AVAILABLE_BITS = { "0", "1" };
	public static final String[] AVAILABLE_OPCODES = { "0", "1", "2", "3" };

	public static final long DELAY = 1000;
	
	public static final String[] semaphoreStates =
	{
		createSemaphoreState("red blue"),
		createSemaphoreState("red"),
		createSemaphoreState("red blue"),
		createSemaphoreState("red"),
		createSemaphoreState("red blue"),
		createSemaphoreState("red"),
		createSemaphoreState("green blue"),
		createSemaphoreState("green"),
		createSemaphoreState("green blue"),
		createSemaphoreState("green"),
		createSemaphoreState("green blue"),
		createSemaphoreState("green"),
		createSemaphoreState("green blue"),
		createSemaphoreState("green"),
		createSemaphoreState("yellow blue"),
		createSemaphoreState("yellow"),
		createSemaphoreState("yellow blue"),
		createSemaphoreState("yellow")
	};
	
	public static boolean semaphoreStop = true;
	public static Thread semaphoreThread;
	
	public static final Runnable semaphoreRunnable = new Runnable()
	{
		@Override
		public void run()
		{
			int i;
			Long elapsedTime;
			
			IO.println("");
			
			while (semaphoreIsRunning(false))
			{
				i = 0;
				
				elapsedTime = callProgram(semaphoreStates[i]);
				
				for (i = 1; semaphoreIsRunning(false) && i < semaphoreStates.length; i++)
				{
					try
					{
						IO.println("Estado enviado, tempo de resposta da JVM = " + elapsedTime + " ms");
						
						elapsedTime = ( elapsedTime > DELAY ? DELAY : elapsedTime );
						
						Thread.sleep(DELAY - elapsedTime);
						
						callProgram(semaphoreStates[i]);
					}
					
					catch (InterruptedException iex)
					{
						IO.println("\nSemáforo interrompido");
					}
				}
				
				if (semaphoreIsRunning(false))
				{
					IO.println("Um looping completado\n");
				}
			}

			IO.println("Semáforo finalizado");
		}
	};
	
	/**
	 * Checa se o gerador de estados para o semáforo está rodando.
	 * 
	 * @return {@code true} se o gerador de estados para o semáforo
	 * estiver rodando. Caso contrário, {@code false}.
	 */
	
	public static boolean semaphoreIsRunning(boolean alertUser)
	{
		if (!semaphoreStop && alertUser)
		{
			IO.println("Não é possível abrir outra comunicação com o Arduino.");
		}
		
		return !semaphoreStop;
	}
	
	/**
	 * Chama o programa {@link #NOME_DO_PROGRAMA} fazendo-o enviar
	 * {@code arguments} pela porta {@link #PORTA_COM} do computador.
	 *  
	 * @param arguments Argumentos do programa {@link #NOME_DO_PROGRAMA}. 
	 * 
	 * @return o tempo, em milisegundos, desde a inicialização do processo
	 * do programa até o seu término.
	 */
	
	public static Long callProgram(String arguments)
	{
		ProcessBuilder processBuilder = new ProcessBuilder(NOME_DO_PROGRAMA, PORTA_COM, arguments);
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
	
	/**
	 * Checa o um arranjo de objetos contém um elemento específico.
	 * 
	 * @param element Elemento a ser procurado.
	 * @param array Arranjo a ser percorrido.
	 * 
	 * @return {@code true} se {@code element} existir no arranjo.
	 * Caso contrário, retorna {@code false}.
	 */
	
	public static boolean contains(Object element, Object[] array)
	{
		boolean contains = false;
		int length = ( element == null || array == null ? 0 : array.length );
		
		for (int i = 0; !contains && i < length; i++)
		{
			contains = ( element.equals(array[i]) );
		}
		
		return contains;
	}
	
	/**
	 * Fica lendo valores da entrada padrão até que um deles seja algo
	 * existente em {@code allowedArguments}.
	 * 
	 * @param message Mensagem para o usuário.
	 * @param allowedArguments Valores permitidos.
	 * 
	 * @return o último valor lido.
	 */
	
	public static String readValue(String message, String[] allowedValues)
	{
		String value = null;
		
		do
		{
			value = IO.readLine(message);
			
		} while (!contains(value, allowedValues));
		
		return value;
	}
	
	/**
	 * Le as entradas de uma operação da ALU e as envia para o programa envia.exe.
	 * 
	 * @return o tempo, em milisegundos, desde a inicialização do processo
	 * do programa até o seu término.
	 */
	
	public static Long ALU()
	{
		Long elapsedTime = (long) 0;
		
		if (!semaphoreIsRunning(true))
		{
			String a = readValue("Informe o valor de a: ", AVAILABLE_BITS);
			String b = readValue("Informe o valor de b: ", AVAILABLE_BITS);
			String opcode = readValue("Informe o valor do opcode: ", AVAILABLE_OPCODES);
			
			elapsedTime = callProgram(a + " " + b + " " + opcode);
		}

		IO.println("\nEstado enviado, tempo de resposta da JVM = " + elapsedTime + " ms");
		
		return elapsedTime;
	}
	
	/**
	 * Cria uma String com um estado específico para o semáforo.
	 * 
	 * @param ledsToTurnOn Strings com as cores dos leds escritas
	 * com letras minúsculas e em inglês.
	 * 
	 * @return String com um estado específico para o semáforo.
	 */
	
	public static String createSemaphoreState(String ledsToTurnOn)
	{
		String redLed = ( ledsToTurnOn.contains("red") ? "1" : "0" );
		String yellowLed = ( ledsToTurnOn.contains("yellow") ? "1" : "0" );
		String greenLed = ( ledsToTurnOn.contains("green") ? "1" : "0" );
		String blueLed = ( ledsToTurnOn.contains("blue") ? "1" : "0" );
		
		return redLed + " " + yellowLed + " " + greenLed + " " + blueLed;
	}
	
	/**
	 * Envia os estados do semáforo em looping infinito com um delay equivalente
	 * ao campo {@link #DELAY} para o programa envia.exe.
	 * 
	 * @return o tempo, em milisegundos, desde a inicialização do processo
	 * do programa até o seu término.
	 */
	
	public static void semaphore()
	{
		if (!semaphoreIsRunning(true))
		{
			semaphoreStop = false;
			
			semaphoreThread = new Thread(semaphoreRunnable);
			
			semaphoreThread.start();
		}
	}
	
	public static void mainMenu()
	{
		String questao = "";
		
		do
		{
			IO.println("Escolha a questão:\n");
			IO.println("1 - Unidade Lógico Aritmética");
			IO.println("2 - Semáforo");
			IO.println("0 - Sair\n");
			questao = IO.readLine("Opção: ");
			
			IO.println("");
			
			switch (questao)
			{
				case "0":
					if (semaphoreIsRunning(false))
					{
						semaphoreStop = true;
						
						questao = "@";
					}
					
					break;

				case "1":
					ALU();
					break;

				case "2":
					semaphore();
					break;

				default:
					break;
			}
			
			IO.print("\n---------------------------------\n");
			
		} while (!questao.equals("0"));
	}
	
	public static void main(String[] args) throws Exception
	{
		mainMenu();
	}
}
