package benchmark.thread;

import benchmark.Operations;

public abstract class BenchmarkThread extends Thread {
	Operations<?, ?, ?, ?> operations;
	int opcount;
	int threadid;
	int threadcount;
	private int opsDone = 0;

	public void incrOpsDone() {
		opsDone++;
	}

	public int getOpsDone() {
		return opsDone;
	}
}
