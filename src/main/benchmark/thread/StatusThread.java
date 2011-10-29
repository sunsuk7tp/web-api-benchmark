package benchmark.thread;

import java.text.DecimalFormat;
import java.util.Vector;

import benchmark.measurements.Measurements;

public class StatusThread extends Thread {
	Vector<BenchmarkThread> _threads;

	/**
	 * The interval for reporting status.
	 */
	public static final long sleeptime = 10000;

	public StatusThread(Vector<BenchmarkThread> threads) {
		_threads = threads;
	}

	/**
	 * Run and periodically report status.
	 */
	@Override
	public void run() {
		long st = System.currentTimeMillis();

		long lasten = st;
		long lasttotalops = 0;

		boolean alldone;

		do {
			alldone = true;

			int totalops = 0;

			//terminate this thread when all the worker threads are done
			for (BenchmarkThread t : _threads) {
				if (t.getState() != Thread.State.TERMINATED) {
					alldone = false;
				}
				totalops += t.getOpsDone();
			}

			long en = System.currentTimeMillis();

			long interval = en - st;
			double curthroughput = 1000.0 * (((double)(totalops - lasttotalops)) / ((double)(en - lasten)));
			lasttotalops = totalops;
			lasten = en;

			DecimalFormat d = new DecimalFormat("#.##");

			if (totalops == 0) {
				System.err.println((interval / 1000) + " sec: " + totalops + " operations; "
					+ Measurements.getMeasurements().getSummary());
			} else {
				System.err.println((interval / 1000) + " sec: " + totalops + " operations; "
					+ d.format(curthroughput) + " current ops/sec; " + Measurements.getMeasurements().getSummary());
			}

			try {
				sleep(sleeptime);
			} catch (InterruptedException e) {
				//do nothing
			}

		} while (!alldone);
	}
}
