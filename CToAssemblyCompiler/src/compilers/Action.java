package compilers;

import java.util.function.Function;

public class Action
{
	boolean needToMatchAfter1Line;
	Function<Boolean, String> needToMatchBefore;
	Function<String, String> assembly;
}
