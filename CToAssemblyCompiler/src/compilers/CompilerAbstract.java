package compilers;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import compilers.Operator.Associativity;
import util.Files;
import util.IO;
import util.Regexes;

public abstract class CompilerAbstract
{
	// arguments to the compiler
	protected HashMap<String, Function<Matcher, Boolean>> grammar;
	protected String inputFileName;
	protected String outputFileName;
	protected BufferedReader inputFile;
	protected PrintStream outputFile;
	protected String startingRegister;
	protected int startingNumber;
	protected boolean nocomment;
	
	// status variables
	/**
	 * Each scope will have some actions to perform when some line is read.
	 * Each Function<String, String> will receive the current read line and
	 * will return the corresponding assembly to that line if it match
	 * some condition or will return null if the line can be ignored for that
	 * condition. Ex.: "do while" start in a line with "do" and end in
	 * a line with "while". 
	 */
	ArrayList< ArrayList< Function<String, String> > > actionsPerScope;
	protected int numberOfLinesCovered;
	protected int totalNumberOfScopes;
	protected int numberOfScopes;
	protected int numberOfLabels;
	protected int tabulation;
	protected String currentLine;
	
	// arithmetic operators
	public static final String OP_NOPE = ";";
	public static final String OP_AND_BITWISE = "&";
	public static final String OP_OR_BITWISE = "|";
	public static final String OP_ADD = "+";
	public static final String OP_SUB = "-";
	public static final String OP_MULT = "*";
	public static final String OP_DIV = "/";
	public static final String OP_POW = "^";
	public static final String OP_SHIFT_LEFT = "<<";
	public static final String OP_SHIFT_RIGHT = ">>";
	
	// logical operators
	public static final String OP_LT = "<";
	public static final String OP_LT_EQ = "<=";
	public static final String OP_GT = ">";
	public static final String OP_GT_EQ = ">=";
	
	// other operators
	public static final String OP_PARENTHESIS = "(";/*
	public static final String OP_NOPE = "nope";
	public static final String OP_AND_BITWISE = "andbitwise";
	public static final String OP_OR_BITWISE = "orbitwise";
	public static final String OP_ADD = "add";
	public static final String OP_SUB = "sub";
	public static final String OP_MULT = "mult";
	public static final String OP_DIV = "div";
	public static final String OP_POW = "pow";
	public static final String OP_SHIFT_LEFT = "sll";
	public static final String OP_SHIFT_RIGHT = "srl";
	public static final String OP_PARENTHESIS = "parenthesis";*/
	
	public static final HashMap<String, TriFunction<String, String, String, String>>
	operatorAndInstruction = new HashMap<>();
	{
		operatorAndInstruction.put(OP_AND_BITWISE, this::convertAndBitwiseToAssembly);
		operatorAndInstruction.put(OP_OR_BITWISE, this::convertOrBitwiseToAssembly);
		operatorAndInstruction.put(OP_ADD, this::convertAddToAssembly);
		operatorAndInstruction.put(OP_SUB, this::convertSubToAssembly);
		operatorAndInstruction.put(OP_MULT, this::convertMultToAssembly);
		operatorAndInstruction.put(OP_DIV, this::convertDivToAssembly);
		operatorAndInstruction.put(OP_POW, this::convertPowToAssembly);
		operatorAndInstruction.put(OP_SHIFT_LEFT, this::convertSllToAssembly);
		operatorAndInstruction.put(OP_SHIFT_RIGHT, this::convertSrlToAssembly);
		
		operatorAndInstruction.put(OP_LT, this::convertLtToAssembly);
		operatorAndInstruction.put(OP_LT_EQ, this::convertLtEqToAssembly);
		operatorAndInstruction.put(OP_GT, this::convertGtToAssembly);
		operatorAndInstruction.put(OP_GT_EQ, this::convertGtEqToAssembly);
	}
	
	public static final HashMap<String, Operator> operators =
			new HashMap<String, Operator>();
	{
		ArrayList<String> connections = new ArrayList<String>();
		connections.addAll( Arrays.asList( new String[]{ OP_MULT, OP_DIV, OP_POW } ) );
		
		operators.put(OP_NOPE,
				new Operator
				(
					OP_NOPE, OP_NOPE, 0, Associativity.LEFT, true,
					new ArrayList<String>()
				)
			);
		
		operators.put(OP_AND_BITWISE,
				new Operator
				(
					OP_AND_BITWISE, OP_AND_BITWISE, 20, Associativity.LEFT, true,
					new ArrayList<String>()
				)
			);
		
		operators.put(OP_OR_BITWISE,
				new Operator
				(
					OP_OR_BITWISE, OP_OR_BITWISE, 15, Associativity.LEFT, true,
					new ArrayList<String>()
				)
			);
		
		operators.put(OP_ADD,
				new Operator
				(
					OP_ADD, OP_ADD, 10, Associativity.LEFT, true,
					new ArrayList<String>()
				)
			);
		
		operators.put(OP_SUB,
				new Operator
				(
					OP_SUB, OP_SUB, 10, Associativity.LEFT, false,
					new ArrayList<String>()
				)
			);
		
		operators.put(OP_MULT,
				new Operator
				(
					OP_MULT, OP_MULT, 30, Associativity.LEFT, true, connections
				)
			);
		
		operators.put(OP_DIV,
				new Operator
				(
					OP_DIV, OP_DIV, 30, Associativity.LEFT, false, connections
				)
			);
		
		operators.put(OP_POW,
				new Operator
				(
					OP_POW, OP_POW, 50, Associativity.RIGHT, false, connections
				)
			);
		
		operators.put(OP_SHIFT_LEFT,
				new Operator
				(
					OP_SHIFT_LEFT, OP_SHIFT_LEFT, 25, Associativity.LEFT, false, connections
				)
			);
		
		operators.put(OP_SHIFT_RIGHT,
				new Operator
				(
					OP_SHIFT_RIGHT, OP_SHIFT_RIGHT, 25, Associativity.LEFT, false, connections
				)
			);
		
		operators.put(OP_PARENTHESIS,
				new Operator
				(
					OP_PARENTHESIS, OP_PARENTHESIS, 100, Associativity.LEFT, false, connections
				)
			);
	}
	
	// methods to convert pseudo C to assembly
	protected abstract boolean functionCall(Matcher matcher);
	protected abstract boolean functionDefinition(Matcher matcher);
	protected abstract boolean variableDeclaration(Matcher matcher);
	protected abstract boolean WHILE(Matcher matcher);
	protected abstract boolean FOR(Matcher matcher);
	protected abstract boolean assignment(Matcher matcher);
	protected abstract boolean IF(Matcher matcher);
	protected abstract boolean RETURN(Matcher matcher);

	protected abstract void handleNewScope();
	protected abstract void handleScopeEnd();
	
	protected abstract String convertAndBitwiseToAssembly(String destination, String operand0, String operand1);
	protected abstract String convertOrBitwiseToAssembly(String destination, String operand0, String operand1);
	protected abstract String convertAddToAssembly(String destination, String operand0, String operand1);
	protected abstract String convertSubToAssembly(String destination, String operand0, String operand1);
	protected abstract String convertMultToAssembly(String destination, String operand0, String operand1);
	protected abstract String convertDivToAssembly(String destination, String operand0, String operand1);
	protected abstract String convertPowToAssembly(String destination, String operand0, String operand1);
	protected abstract String convertSllToAssembly(String destination, String operand0, String operand1);
	protected abstract String convertSrlToAssembly(String destination, String operand0, String operand1);

	protected abstract String convertLtToAssembly(String destination, String operand0, String operand1);
	protected abstract String convertLtEqToAssembly(String destination, String operand0, String operand1);
	protected abstract String convertGtToAssembly(String destination, String operand0, String operand1);
	protected abstract String convertGtEqToAssembly(String destination, String operand0, String operand1);
	
	/**
	 * Transforms the operands in high level to operands in low level.
	 * For example, transforms variables in registers.
	 * 
	 * @param operand0 First operand.
	 * @param operand1 Second operand.
	 * 
	 * @return An array with the new first and second operands respectively.
	 */
	protected abstract String[] handleOperands(String operand0, String operand1);
	
	/**
	 * Method that creates the grammar of the pseudo language C.
	 * Associates each regular expression to it's method.
	 * 
	 * @return The grammar of the pseudo language C.
	 */
	
	private HashMap<String, Function<Matcher, Boolean>> createGrammar()
	{
		HashMap<String, Function<Matcher, Boolean>> grammar = new HashMap<>();

		grammar.put(Regexes.CRE_FUNCTION_CALL, this::functionCall);
		grammar.put(Regexes.CRE_FUNCTION_DEFINITION, this::functionDefinition);
		grammar.put(Regexes.CRE_VARIABLE_DECLARATION, this::variableDeclaration);
		grammar.put(Regexes.CRE_WHILE, this::WHILE);
		grammar.put(Regexes.CRE_FOR, this::FOR);
		grammar.put(Regexes.CRE_ASSIGNMENT, this::assignment);
		grammar.put(Regexes.CRE_IF, this::IF);
		grammar.put(Regexes.CRE_RETURN, this::RETURN);
		
		return grammar;
	}
	
	protected void addNewScopeForActions()
	{
		actionsPerScope.add( new ArrayList<Function<String,String>>() );
	}
	
	protected void addNewAction(Function<String,String> action)
	{
		actionsPerScope.get(totalNumberOfScopes).add(action);
	}
	
	// constructors
	private CompilerAbstract(
		HashMap<String, Function<Matcher, Boolean>> grammar,
		HashMap<String, String> identifiersAndRegisters,
		String inputFileName,
		String outputFileName,
		String startingRegister,
		int startingNumber,
		boolean nocomment)
	{
		this.grammar = grammar;
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		this.startingRegister = startingRegister;
		this.startingNumber = startingNumber;
		this.nocomment = nocomment;
		
		this.numberOfLinesCovered = 0;
		this.totalNumberOfScopes = 0;
		this.numberOfScopes = 0;
		this.numberOfLabels = 0;
		this.actionsPerScope = new ArrayList<ArrayList<Function<String,String>>>();
		addNewScopeForActions();
	}
	
	
	public CompilerAbstract(
		HashMap<String, String> identifiersAndRegisters,
		String inputFileName,
		String outputFileName,
		String startingRegister,
		int startingNumber,
		boolean nocomment)
	{
		this(
			null,
			identifiersAndRegisters,
			inputFileName,
			outputFileName,
			startingRegister,
			startingNumber,
			nocomment
		);
		
		this.grammar = createGrammar();
	}

	/**
	 * Displays an error message saying the line it occurred.
	 * 
	 * @param msg Custom random message.
	 */
	
	public void printError(Object msg)
	{
		IO.printlnErr("Error. Line:" + numberOfLinesCovered + " -> " + msg);
	}
	
	// methods to analyze expressions
	
	protected String convertOperationToAssembly(String operationName, String destination, String operand0, String operand1)
	{
		TriFunction<String, String, String, String> operation = operatorAndInstruction.get(operationName);
		
		if (operation == null)
		{
			printError("This operation is not supported -> " + operationName);
		}
		
		return ( operation == null ? "" : operation.apply(destination, operand0, operand1) );
	}
	
	protected String[] handleOperands(Operation operation)
	{
		String[] operands = new String[3];
		String operatorName = ( operation.operator != null ? operation.operator.name : "" );
		operands[0] = operatorName;
		
		if (!operatorName.isEmpty())
		{
			operands[1] = operation.operands.get(0).value;
			operands[2] = operation.operands.get(1).value;
		}
		
		else
		{
			operands[1] = operation.value;
		}
		
		String[] newOperands = handleOperands(operands[1], operands[2]);
		operands[1] = newOperands[0];
		operands[2] = newOperands[1];
		
		return operands;
	}
	
	protected ArrayList< Operation > extractOperations(String str)
	{
		// creates an empty Operation list
		ArrayList< Operation > operations = new ArrayList< Operation >();
		
		// removes all white characters
		String expression = str.replaceAll(Regexes.RE_WHITE, "");
		
		// tries to match the entire matcher string as an operation of the type: <identifier> or <identifier> <operator> <identifier>
		Matcher myMatcher = Pattern.compile(
				"(?<arg1>" + Regexes.CRE_IDENTIFIER + ")" +
				"((?<op>" + Regexes.CRE_ALL_OPERATORS + ")" +
				"(?<arg2>" + Regexes.CRE_IDENTIFIER + "))?").matcher(expression);
		
		if (myMatcher.matches()) // run matcher
		{
			String operator = myMatcher.group("op"); // get operator
			String arg1 = myMatcher.group("arg1"); // get arg1
			Operation operation;
			
			// checks if operator was found and if it is supported in the operators HashMap
			if (operator != null && operators.containsKey(operator))
			{
				String arg2 = myMatcher.group("arg2"); // get arg2
				Operator op = operators.get(operator); // get operator
				operation = new Operation(op); // creates an operation with just the operator
				operation.addOperand(arg1); // adds the operands
				operation.addOperand(arg2);
			}
			
			else
			{
				operation = new Operation(arg1); // creates an operation with just the value
			}

			operations.add(operation);
		}
		
		return operations;
	}
	
	protected ArrayList< Operation > extractOperations(Matcher matcher)
	{
		return extractOperations(matcher.group());
	}
	
	protected boolean analyzeArithmeticExpression(Matcher matcher)
	{
		boolean success = false;
		
		
		
		return success;
	}
	
	/**
	 * Test all the gramatical regexes and see if any of them match with the line.
	 * If any match, the corresponding method of the regex is called to handle the
	 * string.
	 * 
	 * @param line High level code line.
	 * 
	 * @return {@code true} if the process is succeeded. Otherwise,
	 * {@code false}.
	 */
	
	public boolean applyGrammar(String line)
	{
		boolean success = false;
		// Obtains an iterator over the gramatical regexes
		Iterator<String> iterator = grammar.keySet().iterator();
		String regex = null;
		Matcher matcher = null;
		
		while (!success && iterator.hasNext()) // While has more elements,
		{
			regex = iterator.next(); // Get the next, which is a regex,
			
			// Compiles and applies it to the line received as a parameter
			matcher = Pattern.compile(regex).matcher(line);
			success = matcher.matches();
		}
		
		if (success) // If some of the regexes matched,
		{
			// grammar.get(regex) will return an interface of type
			// Function and the method apply of that interface
			// is called to interpret the match. Each regex has it's
			// action. For example, the regex for VARIABLE_DECLARATION
			// will interpret the matcher object and create the
			// corresponding assembly to declarate and initialize the variable
			success = grammar.get(regex).apply(matcher);
		}
		
		return success;
	}
	
	/**
	 * Reset some counters of the compiler
	 */
	
	public void resetStatusVariables()
	{
		numberOfLinesCovered = 0;
		numberOfScopes = 0;
	}
	
	/**
	 * Compiles a file in pseudo C to an assembly of an architeture.
	 * 
	 * @param fileName Name of the file in pseudo C.
	 * 
	 * @return {@code true} if the process is succeeded. Otherwise,
	 * {@code false}.
	 */
	
	public boolean compile(String inputFileName, String outputFileName)
	{
		boolean success = false;
		inputFile = Files.openInputFile(inputFileName);
		outputFile = Files.openOutputFile(outputFileName);
		
		resetStatusVariables();
		
		if (inputFile != null || outputFile != null)
		{
			try
			{
				currentLine = inputFile.readLine();
				success = true;
				
				while (currentLine != null && success)
				{
					numberOfLinesCovered++;
					currentLine = currentLine.trim();
					
					if (!currentLine.isEmpty() && !currentLine.startsWith("//"))
					{
						switch (currentLine.charAt(0))
						{
							case '{':
								numberOfScopes++;
								totalNumberOfScopes++;
								break;
								
							case '}':
								numberOfScopes--;
								totalNumberOfScopes--;
								break;
								
							default:
								// Try to apply the gramatical regexes on this line
								// to know if any of them match with this line
								success = applyGrammar(currentLine);
								break;
						}
					}

					currentLine = inputFile.readLine();
				}
			}
			
			catch (IOException e)
			{
				printError(IO.createFileErrorMsg("File could not be read", inputFileName));
				e.printStackTrace();
			}
		}
		
		else
		{
			IO.printlnErr(IO.createFileErrorMsg("File could not be opened", inputFileName));
		}
		
		Files.closeInputFile(inputFile);
		Files.closeOutputFile(outputFile);
		
		return success;
	}
	
	/**
	 * Compiles the file received in the constructor of this object
	 * to an assembly of an architeture.
	 * 
	 * @return {@code true} if the process is succeeded. Otherwise,
	 * returns {@code false}.
	 */
	
	public boolean compile()
	{
		return compile(inputFileName, outputFileName);
	}
}
