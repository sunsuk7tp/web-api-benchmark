package benchmark.generator;

import java.util.Vector;

/**
 * An expression that generates a random integer in the specified range
 */
public class UniformGenerator extends Generator {

	Vector<String> _values;
	String _laststring;
	UniformIntegerGenerator _gen;


	/**
	 * Creates a generator that will return strings from the specified set uniformly randomly
	 */
	@SuppressWarnings( "unchecked" )
	public UniformGenerator(Vector<String> values) {
		_values=(Vector<String>)values.clone();
		_laststring=null;
		_gen=new UniformIntegerGenerator(0,values.size()-1);
	}

	/**
	 * Generate the next string in the distribution.
	 */
	public String nextString() {
		_laststring=_values.elementAt(_gen.nextInt());
		return _laststring;
	}

	/**
	 * Return the previous string generated by the distribution; e.g., returned from the last nextString() call.
	 * Calling lastString() should not advance the distribution or have any side effects. If nextString() has not yet
	 * been called, lastString() should return something reasonable.
	 */
	public String lastString() {
		if (_laststring==null)
		{
			nextString();
		}
		return _laststring;
	}
}

