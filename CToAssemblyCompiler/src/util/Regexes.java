package util;

public class Regexes
{
	// Site to visualize regexes
	// https://regexper.com
	
	// The preffix RE in the variables below means REGEX
	
	// numbers
	public static final String RE_DIGIT = "[0-9]";
	public static final String RE_HEX_DIGIT = "[" + RE_DIGIT + "[A-Za-z]" + "]";
	public static final String RE_DIGITS = RE_DIGIT + "+";
	public static final String RE_HEX_DIGITS = RE_HEX_DIGIT + "+";
	public static final String RE_INT = "[+-]?" + RE_DIGITS;
	public static final String RE_INT_POSITIVE = "[+]?" + RE_DIGITS;
	public static final String RE_FLOAT = RE_INT + "(\\." + RE_DIGITS + ")?";
	public static final String RE_FLOAT_POSITIVE = RE_INT_POSITIVE + "(\\." + RE_DIGITS + ")?";
	public static final String RE_HEX = "[+-]?0[xX]" + RE_HEX_DIGITS;
	
	// strings
	public static final String RE_WHITE = "\\s*"; // white characters (spaces, etc)
	public static final String RE_LOWER_CASE = "[a-z]";
	public static final String RE_UPPER_CASE = "[A-Z]";
	public static final String RE_LETTER = "[" + RE_LOWER_CASE + RE_UPPER_CASE + "]";
	public static final String RE_ALFANUMERIC = "[" + RE_LETTER + RE_DIGIT + "]";
	
	
	// some constants for named groups in regular expressions
	
	public static final String GROUP_FUNCTION_NAME = "funcName";
	public static final String GROUP_PARAMS_LIST = "paramsList";
	public static final String GROUP_VARIABLE_NAME = "varName";
	public static final String GROUP_ASSIGN_EXPRESSION = "assignExp";
	
	
	// C language grammar
	// strings
	public static final String CRE_COMMAND_END = ";";
	public static final String CRE_LETTER = "[" + RE_LETTER + "]";
	public static final String CRE_ALFANUMERIC = "[_" + CRE_LETTER + RE_DIGIT + "]";
	
	// imediates
	public static final String CRE_IMEDIATE = "(" + RE_INT + "|" + RE_HEX + ")";
	public static final String CRE_IMEDIATE_WHITE = RE_WHITE + CRE_IMEDIATE + RE_WHITE;
	
	// name identifiers (variables names, functions names, etc)
	public static final String CRE_NAME_IDENTIFIER = "(" + CRE_LETTER + CRE_ALFANUMERIC + "*)";
	public static final String CRE_NAME_IDENTIFIER_WHITE = RE_WHITE + CRE_NAME_IDENTIFIER + RE_WHITE;
	
	// identifiers (imediates and name identifiers)
	public static final String CRE_IDENTIFIER = "(" + CRE_IMEDIATE_WHITE + "|" + CRE_NAME_IDENTIFIER_WHITE + ")";

	public static final String CRE_NEW_SCOPE = RE_WHITE + "(?<newScope>{)?" + RE_WHITE;
	public static final String CRE_SCOPE_END = RE_WHITE + "(?<scopeEnd>})?" + RE_WHITE;
	public static final String CRE_LABEL = RE_WHITE + CRE_NAME_IDENTIFIER + ":" + RE_WHITE;
	
	// arithmetic operators and arithmetic expressions
	public static final String CRE_ARITHMETIC_OPERATORS = "([+\\-/*^%]|\\+\\+|\\-\\-|<<|>>)";
	public static final String CRE_ARITHMETIC_EXPRESSION =
			CRE_IDENTIFIER + "(" + CRE_ARITHMETIC_OPERATORS + CRE_IDENTIFIER + ")*?";

	// logical operators and logic expressions that can be chained ( Ex: a && b || c || d && c )
	public static final String CRE_LOGICAL_CHAIN_OPERATORS = "(&&|\\|\\||&|\\|)";
	public static final String CRE_LOGICAL_CHAIN_EXPRESSION =
			CRE_IDENTIFIER + "(" + CRE_LOGICAL_CHAIN_OPERATORS + CRE_IDENTIFIER + ")*?";

	// logical operators and logic expressions that can not be chained ( Ex: a <= b )
	public static final String CRE_LOGICAL_NO_CHAIN_OPERATORS = "(<|<=|>|>=|==|!=)";
	public static final String CRE_LOGICAL_NO_CHAIN_EXPRESSION =
			CRE_IDENTIFIER + CRE_LOGICAL_NO_CHAIN_OPERATORS + CRE_IDENTIFIER;

	// all logical operators and logic expressions
	public static final String CRE_LOGICAL_EXPRESSION =
			"(" + CRE_LOGICAL_NO_CHAIN_EXPRESSION + "|" + CRE_LOGICAL_CHAIN_EXPRESSION + ")";

	// all logical operators
	public static final String CRE_LOGICAL_OPERATORS = "(" + CRE_LOGICAL_CHAIN_OPERATORS + "|" + CRE_LOGICAL_NO_CHAIN_OPERATORS + ")";

	// all operators
	public static final String CRE_ALL_OPERATORS = "(" + CRE_ARITHMETIC_OPERATORS + "|" + CRE_LOGICAL_OPERATORS + ")";
	
	// all logical operators and logic expressions
	public static final String CRE_ARRAY_EXPRESSION =
			CRE_NAME_IDENTIFIER_WHITE + "[" + CRE_ARITHMETIC_EXPRESSION + "]" + RE_WHITE;
	
	// assignment (left side)
	public static final String CRE_ASSIGNMENT_LEFT =
			"(" + CRE_NAME_IDENTIFIER_WHITE + "|" + CRE_ARRAY_EXPRESSION + ")"; // left side
	
	// assignment (right side)
	public static final String CRE_ASSIGNMENT_RIGHT =
			"(" + CRE_ARITHMETIC_EXPRESSION + "|" + CRE_ARRAY_EXPRESSION + "|" + CRE_LOGICAL_CHAIN_EXPRESSION + ")"; // right side
	
	// assignment
	public static final String CRE_ASSIGNMENT =
			CRE_ASSIGNMENT_LEFT + "=" + CRE_ASSIGNMENT_RIGHT + CRE_COMMAND_END + "?";
	
	// variable declaration
	public static final String CRE_VARIABLE_DECLARATION =
			"(var)?(?<" + GROUP_VARIABLE_NAME + ">" + CRE_NAME_IDENTIFIER_WHITE + ")" +
			RE_WHITE + "(=" + "(" + CRE_ASSIGNMENT_RIGHT + "))?" + CRE_COMMAND_END + "?";
	
	// if
	public static final String CRE_IF =
			"if" + RE_WHITE + "\\(" + CRE_LOGICAL_EXPRESSION + "\\)" + CRE_COMMAND_END + "?";
	
	// while
	public static final String CRE_WHILE =
			"while" + RE_WHITE + "\\(" + CRE_LOGICAL_EXPRESSION + "\\)" + CRE_COMMAND_END + "?";
	
	// for
	public static final String CRE_FOR =
			"for" + RE_WHITE + "\\(" + // for key word
					CRE_ASSIGNMENT + CRE_COMMAND_END + // initial assignment
					CRE_LOGICAL_EXPRESSION + CRE_COMMAND_END + // test
					CRE_ASSIGNMENT + // assignment for each iteration
					"\\)" + CRE_COMMAND_END + "?";
	
	// parameters list
	public static final String CRE_PARAMETERS_LIST =
			"(?<" + GROUP_PARAMS_LIST + ">" + CRE_NAME_IDENTIFIER_WHITE + "(," + CRE_NAME_IDENTIFIER_WHITE + ")*?)?";
	
	// parenthesis parameters list
	public static final String CRE_PARAMETERS_LIST_WITH_PARENTHESIS =
			"\\(" + CRE_PARAMETERS_LIST + "\\)"; // params
	
	// function call
	public static final String CRE_FUNCTION_CALL =
			"(?<" + GROUP_FUNCTION_NAME + ">" + CRE_NAME_IDENTIFIER_WHITE + ")" +// function name
			CRE_PARAMETERS_LIST_WITH_PARENTHESIS + // parameters list
			CRE_COMMAND_END + "?"; // function end
	
	// function definition
	public static final String CRE_FUNCTION_DEFINITION = "function" + CRE_FUNCTION_CALL;
	
	// return
	public static final String CRE_RETURN =
			"return" + RE_WHITE + // return key word
			"(" + CRE_PARAMETERS_LIST_WITH_PARENTHESIS + ")?" + // parameters
			CRE_COMMAND_END + "?"; // return end
}
