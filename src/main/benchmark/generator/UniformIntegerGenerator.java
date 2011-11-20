package benchmark.generator;

import java.util.Random;

public class UniformIntegerGenerator extends IntegerGenerator {
	Random _random;
	int _lb,_ub,_interval;

	/**
	 * Creates a generator that will return integers uniformly randomly from the interval [lb,ub] inclusive (that is, lb and ub are possible values)
	 *
	 * @param lb the lower bound (inclusive) of generated values
	 * @param ub the upper bound (inclusive) of generated values
	 */
	public UniformIntegerGenerator(int lb, int ub) {
		_random=new Random();
		_lb=lb;
		_ub=ub;
		_interval=_ub-_lb+1;
	}

	@Override
	public int nextInt() {
		int ret=_random.nextInt(_interval)+_lb;
		setLastInt(ret);

		return ret;
	}

}
