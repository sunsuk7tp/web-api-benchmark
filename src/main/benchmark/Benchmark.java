package benchmark;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Properties;
import java.util.Vector;

import benchmark.measurements.Measurements;
import benchmark.measurements.exporter.MeasurementsExporter;
import benchmark.measurements.exporter.TextMeasurementsExporter;
import benchmark.thread.BenchmarkThread;
import benchmark.thread.StatusThread;

public abstract class Benchmark {

	// print status info if true
	private static boolean isBench = true;
	private static boolean status = false;

	// default size
	public final static String DEFAULT_THREADSIZE = "1";
	public final static String DEFAULT_OPERATIONSIZE = "1";

	public static void usageMessage() {
		System.out.println("Usage: java **Benchmark [options]");
		System.out.println("Options:");
		System.out.println("  -threads n: execute using n threads (default: 1)");
		System.out.println("  -bench:  run the benchmark phases of the workload (default)");
		System.out.println("  -load:  run the loading phase of the workload");
		System.out.println("  -ops n:  n operations (default: 1)");
		System.out.println("  -loads n:  n loaded records (default: 10000)");
		System.out.println("  -valuelen:  value length (default: 8)");
		System.out.println("  -s:  show status during run (default: no status)");
		System.out.println(" -<OP> ratio: specify execution ratio[%] for <OP>");
		System.out.println("  -h | -help:  show this message");
	}

	// parse args and set to props
	public static Properties parseArgs(String[] args) {
		Properties props = new Properties();

		int argindex = 0;
		if (args.length == 0) {
			System.err.println("no input.");
			usageMessage();
			System.exit(0);
		}
		while (args[argindex].startsWith("-")) {
			if (args[argindex].compareTo("-load") == 0) {
				setBench(false);
				argindex++;
			} else if (args[argindex].compareTo("-bench") == 0) {
				setBench(true);
				argindex++;
			} else if (args[argindex].compareTo("-threads") == 0) {
				argindex++;
				if (argindex >= args.length) {
					usageMessage();
					System.exit(0);
				}
				props.setProperty("threadcount", Integer.parseInt(args[argindex]) + "");
				argindex++;
			} else if (args[argindex].compareTo("-ops") == 0) {
				argindex++;
				if (argindex >= args.length) {
					usageMessage();
					System.exit(0);
				}
				props.setProperty("operationcount", Integer.parseInt(args[argindex]) + "");
				argindex++;
			} else if (args[argindex].compareTo("-loads") == 0) {
				argindex++;
				if (argindex >= args.length) {
					usageMessage();
					System.exit(0);
				}
				props.setProperty("loadcount", Integer.parseInt(args[argindex]) + "");
				argindex++;
			} else if (args[argindex].compareTo("-s") == 0) {
				onStatus();
				argindex++;
			} else if (args[argindex].compareTo("-h") == 0 || args[argindex].compareTo("-help") == 0) {
				usageMessage();
				System.exit(0);
			} else { // each operations ratio setting
				String paramName = args[argindex].substring(1).toUpperCase();
				argindex++;
				if (argindex >= args.length) {
					usageMessage();
					System.exit(0);
				}
				props.setProperty(paramName, Double.parseDouble(args[argindex]) + "");
			}

			if (argindex >= args.length) {
				break;
			}
		}

		return props;
	}

	public static boolean isBench() {
		return isBench;
	}

	public static void setBench(boolean isBench) {
		Benchmark.isBench = isBench;
	}

	public static void onStatus() {
		status = true;
	}

	// benchmark run
	public static void run(Vector<BenchmarkThread> threads, Properties props) {
		Measurements.setProperties(props);

		StatusThread statusthread = null;
		if (status) {
			statusthread = new StatusThread(threads);
			statusthread.start();
		}

		// benchmark start
		long st = System.currentTimeMillis();
		for (Thread t : threads) {
			t.start();
		}

		int opsDone = 0;
		for (BenchmarkThread t : threads) {
			try {
				t.join(); // wait for each thread closed.
				opsDone += t.getOpsDone();
			} catch (InterruptedException ee) {
				System.err.println("fail to join threads.");
			}
		}
		long en = System.currentTimeMillis();
		// benchmark end

		if (statusthread != null) {
			statusthread.interrupt();
		}

		try {
			exportMeasurements(props, opsDone, en - st);
		} catch (IOException e) {
			System.err.println("Could not export measurements, error: " + e.getMessage());
			e.printStackTrace();
			System.exit(-1);
		}
		System.exit(0);
	}

	// display benchmark result
	public static void exportMeasurements(Properties props, int opcount, long runtime) throws IOException {
		MeasurementsExporter exporter = null;
		try {
			String exportFile = props.getProperty("exportfile");
			OutputStream out = exportFile == null ? System.out : new FileOutputStream(exportFile);

			// if no exporter is provided the default text one will be used
			String exporterStr = props.getProperty("exporter", "benchmark.measurements.exporter.TextMeasurementsExporter");
			try {
				exporter = (MeasurementsExporter)Class.forName(exporterStr).getConstructor(OutputStream.class).newInstance(out);
			} catch (Exception e) {
				System.err.println("Could not find exporter " + exporterStr
					+ ", will use default text reporter.");
				e.printStackTrace();
				exporter = new TextMeasurementsExporter(out);
			}

			exporter.write("OVERALL", "RunTime(ms)", runtime);
			double throughput = 1000.0 * (double)opcount / (double)runtime;
			exporter.write("OVERALL", "Throughput(ops/sec)", throughput);

			Measurements.getMeasurements().exportMeasurements(exporter);
		} finally {
			if (exporter != null) {
				exporter.close();
			}
		}
	}
}
