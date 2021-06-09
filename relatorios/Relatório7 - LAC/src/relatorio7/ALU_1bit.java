package relatorio7;

import Util.AxellIO;
import compiler.ULACompiler;
import executer.Executer;

public class ALU_1bit
{
	/**
	 * Caso o programa seja executado sem nenhum argumento,
	 * será pressuposto que a porta COM usada é a com3.
	 * Caso contrário, a porta usada será o primeiro
	 * argumento da linha de comando.
	 * 
	 * @param args Argumentos da linha de comando.
	 */
	
	public static void main(String[] args)
	{
		ULACompiler ulaCompiler = new ULACompiler("testeula.ula");
		Executer executer;
		
		if (args != null && args.length > 0)
		{
			executer = new Executer(args[0], "testeula.hex");
		}
		
		else
		{
			executer = new Executer(null, "testeula.hex");
		}
		
		if (!ulaCompiler.compile())
		{
			AxellIO.println("Erro na compilação");
		}
		
		else
		{
			executer.execute();
		}
	}

}
