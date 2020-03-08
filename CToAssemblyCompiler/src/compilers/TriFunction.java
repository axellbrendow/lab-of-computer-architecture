package compilers;

public interface TriFunction<P0_TYPE, P1_TYPE, P2_TYPE, RETURN_TYPE>
{
	RETURN_TYPE apply(P0_TYPE p0, P1_TYPE p1, P2_TYPE p2);
}
