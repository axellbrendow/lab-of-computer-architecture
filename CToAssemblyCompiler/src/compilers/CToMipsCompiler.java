package compilers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

import util.IO;
import util.Regexes;

public class CToMipsCompiler extends CompilerAbstract
{
	// The preffix RE in the variables below means REGEX
	public static final String CRE_VALID_REGISTER = "(t[0-9])|(s[0-7])|(a[0-3t])";
	public static final String CRE_REGISTER = "\\$" + CRE_VALID_REGISTER;

	protected HashMap<String, String> identifiersAndRegisters;
	protected ArrayList< ArrayList< String > > identifiersPerScope;
	private String lastRegister; // last used register (without it's number)
	private int lastNumber; // number of the last used register
	private int stepToNextRegister; // (inc)decrement in "lastNumber" to reserve a new register
	
	protected void addNewScopeForIdentifiers()
	{
		identifiersPerScope.add( new ArrayList<String>() );
	}
	
	protected void addNewIdentifier(String identifier, String register)
	{
		identifiersAndRegisters.put(identifier, register);
		identifiersPerScope.get(numberOfScopes).add(identifier);
	}
	
	public CToMipsCompiler(
		HashMap<String, String> identifiersAndRegisters,
		String fileToCompile,
		String outputFile,
		String startingRegister,
		int startingNumber,
		int stepToNextRegister,
		boolean nocomment)
	{
		super(
			identifiersAndRegisters,
			fileToCompile,
			outputFile,
			startingRegister,
			startingNumber,
			nocomment
		);
		
		this.lastRegister = startingRegister;
		this.lastNumber = startingNumber;
		this.stepToNextRegister = stepToNextRegister;
		this.identifiersAndRegisters = identifiersAndRegisters;
		this.identifiersPerScope = new ArrayList<ArrayList<String>>();
		addNewScopeForIdentifiers();
	}
	
	private String removeSpecificIdentifier(String identifier)
	{
		identifiersPerScope.get(numberOfScopes).remove(identifier);
		
		return identifiersAndRegisters.remove(identifier);
	}
	
	/**
	 * Reserves an specific register to an specific identifier if
	 * that register is not in use.
	 * 
	 * @param newIdentifier New identifier.
	 * @param register Register name. Ex.: t9.
	 * 
	 * @return "$" + {@code register} if the process is succeeded. Otherwise,
	 * returns {@code null}.
	 */
	
	private String reserveSpecificRegister(String newIdentifier, String register)
	{
		boolean success = false;
		String newRegister = identifiersAndRegisters.get(newIdentifier);
		
		if (!identifiersAndRegisters.containsKey(newIdentifier))
		{
			if (newIdentifier != null && register != null &&
					newIdentifier.matches(Regexes.CRE_NAME_IDENTIFIER) &&
					register.matches(CRE_VALID_REGISTER))
				{
					addNewIdentifier(newIdentifier, register);
					
					success = true;
					newRegister = register;
					IO.println("NEW REGISTER (" + register + ")");
				}
				
				else
				{
					printError("Tried to reserve the register (" + register + ").");
				}
		}
		
		else
		{
			success = true;
		}
		
		return ( success ? "$" + newRegister : null );
	}
	
	/**
	 * Reserves a new register to the identificator if the
	 * identificator does not has yet a register.
	 * 
	 * @param newIdentifier New identifier.
	 * 
	 * @return {@code "$" + reservedRegister} if the process is succeeded. Otherwise,
	 * returns {@code null}.
	 */
	
	private String reserveRegister(String newIdentifier)
	{
		String newRegister = identifiersAndRegisters.get(newIdentifier);
		
		if (newRegister == null)
		{
			newRegister = lastRegister + lastNumber;
			lastNumber += stepToNextRegister;
			
			newRegister = reserveSpecificRegister(newIdentifier, newRegister);
		}
		
		else
		{
			newRegister  = "$" + newRegister;
		}
		
		return newRegister;
	}
	
	protected String getRegisterIfThisIsNotAnImediate(String identifier)
	{
		String newIdentifier = identifier;
		
		if (!identifier.matches( Regexes.CRE_IMEDIATE ))
		{
			newIdentifier = identifiersAndRegisters.get( identifier );
			newIdentifier = ( newIdentifier == null ? null/*reserveRegister(identifier)*/ : "$" + newIdentifier );
		}
		
		return newIdentifier;
	}

	@Override
	protected String[] handleOperands(String operand0, String operand1)
	{
		String[] newOperands = new String[2];
		
		operand0 = getRegisterIfThisIsNotAnImediate(operand0);
		
		if (operand1 != null)
		{
			operand1 = getRegisterIfThisIsNotAnImediate(operand1);
		}
		
		newOperands[0] = operand0;
		newOperands[1] = operand1;
		
		return newOperands;
	}
	
	@Override
	protected boolean functionCall(Matcher matcher)
	{
		boolean success = false;
		
		return success;
	}

	@Override
	protected boolean functionDefinition(Matcher matcher)
	{
		boolean success = false;
		String output = "";
		String functionName = matcher.group(Regexes.GROUP_FUNCTION_NAME).trim();
		success = functionName != null;
		
		if (!nocomment) // check if user do not request to not comment
		{
			// split parameters list
			String[] params = matcher.group(Regexes.GROUP_PARAMS_LIST).split(",");
			
			if (params.length > 0)
			{
				// create a comment line before function definition
				// explaining the registers that will be used as the
				// parameters
				output = "#" + functionName + "($a0 = " + params[0].trim();
				
				for (int i = 1; i < params.length; i++)
				{
					output += ", $a" + i + " = " + params[i].trim();
				}
				
				output += ")";
				outputFile.println(output);
			}
		}
		
		output = functionName + ":";
		outputFile.println(output);
		
		return success;
	}

	@Override
	protected boolean variableDeclaration(Matcher matcher)
	{
		boolean success = false;

		String[] fields = matcher.group().split("=");
		String variableName = fields[0].trim();
		String expression = fields[1].trim().replace(";", "");
		String register = reserveRegister(variableName);
		
		if (expression != null)
		{
			ArrayList< Operation > operations = extractOperations(expression);
			Operation operation = operations.get(0);
			
			String[] operands = handleOperands(operation);
			
			String assembly = convertOperationToAssembly(
				operands[0].isEmpty() ? OP_ADD : operands[0],
				register,
				operands[1],
				operands[2]
			);
			
			if (assembly != null)
			{
				success = true;
				outputFile.println(assembly);
			}
		}
		
		else
		{
			success = true;
		}
		
		return success;
	}

	@Override
	protected boolean WHILE(Matcher matcher)
	{
		boolean success = false;

		
		
		return success;
	}

	@Override
	protected boolean FOR(Matcher matcher)
	{
		boolean success = false;

		
		
		return success;
	}

	@Override
	protected boolean assignment(Matcher matcher)
	{
		return variableDeclaration(matcher);
	}

	@Override
	protected boolean IF(Matcher matcher)
	{
		boolean success = false;

		
		
		return success;
	}

	@Override
	protected boolean RETURN(Matcher matcher)
	{
		boolean success = false;

		
		
		return success;
	}
	
	private String prependZerosToComplete(int size, String str)
	{
		int remainingSize = size - str.length();
		String result = str;
		
		if (remainingSize > 0)
		{
			for (int i = 0; i < remainingSize; i++)
			{
				result = "0" + result;
			}
		}
		
		return result;
	}

	public Integer hexToInt(String str)
	{
		return Integer.parseInt(str.replaceFirst("0[Xx]", ""), 16);
	}
	
	private String concatIfNotNull(String notNull, String prependToNotNull, String str)
	{
		return (notNull == null ? str : str + prependToNotNull + notNull);
	}
	
	private String formatInstruction(String instruction, String firstParam, String operand0, String operand1)
	{
		String assembly = instruction;
		assembly = concatIfNotNull(firstParam, "\t, ", assembly);
		assembly = concatIfNotNull(operand0, "\t, ", assembly);
		assembly = concatIfNotNull(operand1, "\t, ", assembly);
		return assembly;
	}
	
	private String createMultipleInstructions(String... instructions)
	{
		String assembly = instructions[0];
		
		for (int i = 1; i < instructions.length; i++)
		{
			assembly += System.lineSeparator() + instructions[i];
		}
		
		return assembly;
	}
	
	private Integer convertImediateToInteger(String imediate)
	{
		return imediate.matches(Regexes.RE_HEX) ?
			hexToInt(imediate) : Integer.parseInt(imediate);
	}
	
	private String loadUpImediate(String destination, String imediate)
	{
		String assembly = null;
		Integer integer = convertImediateToInteger(imediate);
		
		imediate = prependZerosToComplete(8, Integer.toHexString( integer ) );
		String imediate0 = "0x" + imediate.substring(0, 4);
		String imediate1 = "0x" + imediate.substring(4);
		
		assembly = createMultipleInstructions(
			convertOrBitwiseToAssembly(destination, "$zero", imediate0),
			convertSllToAssembly(destination, destination, "16"),
			convertOrBitwiseToAssembly(destination, destination, imediate1)
		);
		
		return assembly;
	}
	
	private boolean imediateGreaterThan16Bit(String imediate)
	{
		return convertImediateToInteger(imediate) >= 65536;
	}
	
	private void printInstructionError(String instruction, String firstParam, String operand0, String operand1)
	{
		String assembly = instruction;
		
		assembly = concatIfNotNull(firstParam, "\t, ", assembly);
		assembly = concatIfNotNull(operand0, "\t, ", assembly);
		assembly = concatIfNotNull(operand1, "\t, ", assembly);
		
		printError(assembly);
	}

	protected String handleInstructionWithImediate(
		String instructionWithImediate,
		String instructionWithoutImediate,
		String destination, String operand0, String operand1)
	{
		String assembly = null;
		operand0 = ( operand0 == null ? "$zero" : operand0 );
		operand1 = ( operand1 == null ? "$zero" : operand1 );
		
		if (operand0.matches(Regexes.CRE_IMEDIATE))
		{
			assembly = imediateGreaterThan16Bit(operand0) ?
				loadUpImediate(destination, operand0) :
				formatInstruction(instructionWithImediate, destination, operand1, operand0);
		}
		
		else if (operand1.matches(Regexes.CRE_IMEDIATE))
		{
			assembly = imediateGreaterThan16Bit(operand1) ?
				loadUpImediate(destination, operand1) :
				formatInstruction(instructionWithImediate, destination, operand0, operand1);
		}
		
		else
		{
			assembly = formatInstruction(instructionWithoutImediate, destination, operand0, operand1);
		}
		
		return assembly;
	}
	
	@Override
	protected String convertAndBitwiseToAssembly(String destination, String operand0, String operand1)
	{
		return handleInstructionWithImediate("andi", "and", destination, operand0, operand1);
	}

	@Override
	protected String convertOrBitwiseToAssembly(String destination, String operand0, String operand1)
	{
		return handleInstructionWithImediate("ori", "or", destination, operand0, operand1);
	}
	
	@Override
	protected String convertAddToAssembly(String destination, String operand0, String operand1)
	{
		return handleInstructionWithImediate("addi", "add", destination, operand0, operand1);
		/*String assembly = null;
		operand0 = ( operand0 == null ? "$zero" : operand0 );
		operand1 = ( operand1 == null ? "$zero" : operand1 );
		
		if (operand0.matches(Regexes.CRE_IMEDIATE))
		{
			assembly = imediateGreaterThan16Bit(operand0) ?
				loadUpImediate(destination, operand0) :
				formatInstruction("addi", destination, operand1, operand0);
		}
		
		else if (operand1.matches(Regexes.CRE_IMEDIATE))
		{
			assembly = imediateGreaterThan16Bit(operand1) ?
				loadUpImediate(destination, operand1) :
				formatInstruction("addi", destination, operand0, operand1);
		}
		
		else
		{
			assembly = formatInstruction("add", destination, operand0, operand1);
		}
		
		return assembly;*/
	}

	@Override
	protected String convertSubToAssembly(String destination, String operand0, String operand1)
	{
		String assembly = null;

		if (operand0.matches(CRE_REGISTER) && operand1.matches(CRE_REGISTER))
		{
			assembly = formatInstruction("sub", destination, operand0, operand1);
		}
		
		else
		{
			printInstructionError("Invalid subtraction: sub", destination, operand0, operand1);
		}
		
		return assembly;
	}

	@Override
	protected String convertMultToAssembly(String destination, String operand0, String operand1)
	{
		String assembly = null;

		if (operand0.matches(CRE_REGISTER) && operand1.matches(CRE_REGISTER))
		{
			assembly = createMultipleInstructions(
				formatInstruction("mult", null, operand0, operand1),
				formatInstruction("mflo", destination, null, null)
			);
		}
		
		else
		{
			printInstructionError("Invalid subtraction: mult", null, operand0, operand1);
		}
		
		return assembly;
	}

	@Override
	protected String convertDivToAssembly(String destination, String operand0, String operand1)
	{
		String assembly = null;

		if (operand0.matches(CRE_REGISTER) && operand1.matches(CRE_REGISTER))
		{
			assembly = createMultipleInstructions(
				formatInstruction("div", null, operand0, operand1),
				formatInstruction("mflo", destination, null, null)
			);
		}
		
		else
		{
			printInstructionError("Invalid subtraction: div", null, operand0, operand1);
		}
		
		return assembly;
	}

	@Override
	protected String convertPowToAssembly(String destination, String operand0, String operand1)
	{
		String assembly = null;

		
		
		return assembly;
	}

	@Override
	protected String convertSllToAssembly(String destination, String operand0, String operand1)
	{
		String assembly = null;

		if (operand0.matches(CRE_REGISTER) && operand1.matches(Regexes.RE_INT_POSITIVE))
		{
			assembly = formatInstruction("sll", destination, operand0, operand1);
		}
		
		else
		{
			printInstructionError("Invalid shift left: sll", destination, operand0, operand1);
		}
		
		return assembly;
	}

	@Override
	protected String convertSrlToAssembly(String destination, String operand0, String operand1)
	{
		String assembly = null;

		if (operand0.matches(CRE_REGISTER) && operand1.matches(Regexes.RE_INT_POSITIVE))
		{
			assembly = formatInstruction("srl", destination, operand0, operand1);
		}
		
		else
		{
			printInstructionError("Invalid shift right: srl", destination, operand0, operand1);
		}
		
		return assembly;
	}

	@Override
	protected String convertLtToAssembly(String destination, String operand0, String operand1)
	{
		return handleInstructionWithImediate("slt", "slt", null, operand0, operand1);
	}

	@Override
	protected String convertLtEqToAssembly(String destination, String operand0, String operand1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String convertGtToAssembly(String destination, String operand0, String operand1)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String convertGtEqToAssembly(String destination, String operand0, String operand1)
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	protected void removeIdentifiersFromLastScope()
	{
		int size = identifiersPerScope.size();
		
		if (size > 0)
		{
			identifiersPerScope.get(size - 1)
			.forEach(
				(identifier) ->
				{
					removeSpecificIdentifier(identifier);
				}
			);
			
			identifiersPerScope.remove(size - 1);
		}
	}

	@Override
	protected void handleNewScope()
	{
		addNewScopeForIdentifiers();
	}

	@Override
	protected void handleScopeEnd()
	{
		removeIdentifiersFromLastScope();
	}
	
}
