package benchmark.thread;

import benchmark.Operations;

public class LoadThread extends BenchmarkThread {
	long loadedsize;

	public LoadThread(Operations<?, ?, ?, ?> operations, int threadid, int threadcount, int opcount) {
		this.operations = operations;
		this.loadedsize = operations.getLoadedDataSize();
		this.opcount = opcount;
		this.threadid = threadid;
		this.threadcount = threadcount;
	}

	@Override
	public void run() {
		int c = 0;
		while (getOpsDone() < opcount) {
			long id = (getOpsDone() * threadid + ++c) % loadedsize;
			operations.loadOperation(id);
			incrOpsDone();
		}
	}
}
