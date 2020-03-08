package compilers;

import java.util.ArrayList;

import util.IO;
import util.Regexes;

public class Operator
{
	public enum Associativity
	{
		LEFT, NONE, RIGHT
	}
	
	String name;
	String uniqueName;
	int precedence;
	Associativity associativity;
	boolean commutative;
	ArrayList<String> connectionsWithOtherOperators;
	
	public Operator(
			String name,
			String uniqueName,
			int precedence,
			Associativity associativity,
			boolean commutative,
			ArrayList<String> connectionsWithOtherOperators)
	{
		this.name = name;
		this.uniqueName = uniqueName;
		this.precedence = precedence;
		this.associativity = associativity;
		this.commutative = commutative;
		this.connectionsWithOtherOperators = connectionsWithOtherOperators;
	}
}
