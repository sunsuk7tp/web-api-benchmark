package benchmark.thread;

import benchmark.api.Operations;

public class OperationThread extends BenchmarkThread {

	public OperationThread(Operations<?, ?, ?, ?> operations, int threadid, int threadcount, int opcount) {
		this.operations = operations;
		this.opcount = opcount;
		this.threadid = threadid;
		this.threadcount = threadcount;
	}

	@Override
	public void run() {
		while (getOpsDone() < opcount) {
			operations.runOperation();
			incrOpsDone();
		}
	}
}
