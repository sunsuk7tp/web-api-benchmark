package benchmark.generator;

import java.util.Random;

public class ZipfRandom {
	private double[] cdf = null;
	private Random rnd = null;

	public ZipfRandom(int m, double k, Random rnd) {
		if (m <= 0)
			throw new IllegalArgumentException("m must be positive");
		if (rnd == null)
			throw new IllegalArgumentException("rnd is null");

		this.rnd = rnd;
		cdf = new double[m];

		double C = 0.0;
		for (int i = 0; i < m; i++) {
			C += 1.0 / Math.pow(i + 1, k);
		}

		double sum = 0.0;
		for (int i = 0; i < m; i++) {
			sum += 1.0 / (Math.pow(i + 1, k) * C);
			cdf[i] = sum;
		}
	}

	public int getNext() {
		double r = rnd.nextDouble();
		for (int i = 0; i < cdf.length; i++) {
			if (r < cdf[i]) {
				return i;
			}
		}

		throw new IllegalStateException();
	}
}
