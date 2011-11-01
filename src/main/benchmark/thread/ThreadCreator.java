package benchmark.thread;

import java.util.Properties;
import java.util.Vector;

import benchmark.api.Operations;

public class ThreadCreator<S, K, V, U> {
	// default size
	public final static String DEFAULT_THREADSIZE = "1";
	public final static String DEFAULT_OPERATIONSIZE = "1";

	boolean isBench = true;
	int threadcount;
	int opcount;
	Operations<S, K, V, U> operations;

	public ThreadCreator(boolean isBench, Properties props, Operations<S, K, V, U> operations) {
		this.isBench = isBench;
		this.threadcount = Integer.parseInt(props.getProperty("threadcount", DEFAULT_THREADSIZE));;
		this.opcount = Integer.parseInt(props.getProperty("operationcount", DEFAULT_OPERATIONSIZE)) / threadcount;
		this.operations = operations;
	}

	public Vector<BenchmarkThread> create() {
		Vector<BenchmarkThread> threads = new Vector<BenchmarkThread>(threadcount);
		if (isBench) {
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
