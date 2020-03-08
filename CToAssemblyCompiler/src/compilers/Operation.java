package compilers;

import java.util.ArrayList;

import util.IO;
import util.Regexes;

public class Operation
{
	Operator operator;
	ArrayList< Operation > operands;
	String value;
	String toStr;
	
	public Operation(Operator operator, ArrayList< Operation > operands, String value)
	{
		this.operator = operator;
		this.operands = operands;
		this.value = value;
	}
	
	public Operation(Operator operator, String value)
	{
		this(operator, new ArrayList< Operation >(), value);
	}
	
	public Operation(String value)
	{
		this(null, value);
	}
	
	public Operation(Operator operator)
	{
		this(operator, null);
	}
	
	public Operation()
	{
		this((Operator) null);
	}
	
	public boolean addOperand(String operand)
	{
		boolean success = false;
		
		if (operand != null && operand.matches(Regexes.CRE_IDENTIFIER) && operands != null)
		{
			operands.add( new Operation(operand) );
		}
		
		else
		{
			IO.println("Invalid operand (" + operand + ").");
		}
		
		return success;
	}
	
	private void toString(Operation operation)
	{
		if (operation.operands.isEmpty())
		{
			toStr += operation.value + " ";
		}
		
		else
		{
			operation.operands.forEach
			(
				(operand) -> toString(operand)
			);
		}
	}
	
	@Override
	public String toString()
	{
		toStr = "";
		
		toString(this);
		
		return toStr;
	}
	
	public void print()
	{
		IO.println( toString() );
	}
}
