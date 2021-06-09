package compiler;

import java.util.HashMap;

import Util.MyString;

public class Operations
{
	private static final HashMap<String, String> OPERATIONS = new HashMap<>();
	private static String greatestName;
	
	static
	{
		OPERATIONS.put("zeroL", "0");
		OPERATIONS.put("umL", "1");
		OPERATIONS.put("An", "2");
		OPERATIONS.put("Bn", "3");
		OPERATIONS.put("AouB", "4");
		OPERATIONS.put("AeB", "5");
		OPERATIONS.put("AxorB", "6");
		OPERATIONS.put("AnandB", "7");
		OPERATIONS.put("AnorB", "8");
		OPERATIONS.put("AxnorB", "9");
		OPERATIONS.put("AnouB", "A");
		OPERATIONS.put("AouBn", "B");
		OPERATIONS.put("AneB", "C");
		OPERATIONS.put("AeBn", "D");
		OPERATIONS.put("AnouBn", "E");
		OPERATIONS.put("AneBn", "F");
	}
	
	public static String convertToHex(String operation)
	{
		return OPERATIONS.get(operation);
	}
	
	public static String getGreatestOperationName()
	{
		greatestName = "";
		
		OPERATIONS.forEach( (key, value) ->
		{
			if (key != null && value != null)
			{
				if (key.length() > greatestName.length())
				{
					greatestName = key;
				}
			}
		});
		
		return greatestName;
	}
}
