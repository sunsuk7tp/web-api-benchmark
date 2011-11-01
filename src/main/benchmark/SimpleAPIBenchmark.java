package benchmark;

import java.util.Properties;
import java.util.Vector;

import benchmark.api.Operations;
import benchmark.api.impl.SimpleAPIInstance;
import benchmark.api.impl.SimpleAPIOperations;
import benchmark.thread.BenchmarkThread;
import benchmark.thread.ThreadCreator;

public class SimpleAPIBenchmark extends Benchmark {
	public static void main(String[] args) {
		// set parameters for input arguments
		Properties props = parseArgs(args);
		// create benchmark threads
		boolean isBench = isBench();
		Operations<SimpleAPIInstance, Long, String, String> operations = new SimpleAPIOperations(props);
		ThreadCreator<SimpleAPIInstance, Long, String, String> tc =
				new ThreadCreator<SimpleAPIInstance, Long, String, String>(isBench, props, operations);
		Vector<BenchmarkThread> threads = tc.create();
		// benchmark run
		run(threads, props);
	}
}
