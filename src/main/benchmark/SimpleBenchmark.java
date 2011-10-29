package benchmark;

import java.util.Properties;
import java.util.Vector;

import benchmark.api.Operations;
import benchmark.api.impl.SimpleInstance;
import benchmark.api.impl.SimpleOperations;
import benchmark.thread.BenchmarkThread;
import benchmark.thread.LoadThread;
import benchmark.thread.OperationThread;

public class SimpleBenchmark extends Benchmark {
	public static void main(String[] args) {
		// set parameters for input arguments
		Properties props = parseArgs(args);
		// create benchmark threads
		Vector<BenchmarkThread> threads = createBenchThreads(props);
		// benchmark run
		run(threads, props);
	}

	public static Vector<BenchmarkThread> createBenchThreads(Properties props) {
		// get number of threads, opcount per a thread
		int threadcount = Integer.parseInt(props.getProperty("threadcount", DEFAULT_THREADSIZE));
		int opcount = Integer.parseInt(props.getProperty("operationcount", DEFAULT_OPERATIONSIZE)) / threadcount;

		// create benchmark threads
		Vector<BenchmarkThread> threads = new Vector<BenchmarkThread>();
		Operations<SimpleInstance, Long, String, String> operations = new SimpleOperations(props);
		if (isBench()) {
			for (int threadId = 0; threadId < threadcount; threadId++) {
				threads.add(new OperationThread(operations, threadId, threadcount, opcount));
			}
		} else {
			operations.setLoadedData(opcount);
			for (int threadId = 0; threadId < threadcount; threadId++) {
				threads.add(new LoadThread(operations, threadId, threadcount, opcount));
			}
		}

		return threads;
	}
}
