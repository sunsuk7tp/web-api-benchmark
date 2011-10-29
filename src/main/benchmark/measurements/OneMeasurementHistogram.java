package benchmark.measurements;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Properties;

import benchmark.measurements.exporter.MeasurementsExporter;

public class OneMeasurementHistogram extends OneMeasurement {
	public static final String BUCKETS = "histogram.buckets";
	public static final String BUCKETS_DEFAULT = "1000";

	int _buckets;
	int[] histogram;
	int histogramoverflow;
	int operations;
	long totallatency;

	//keep a windowed version of these stats for printing status
	int windowoperations;
	long windowtotallatency;

	int min;
	int max;
	HashMap<Integer, int[]> returncodes;

	public OneMeasurementHistogram(String name, Properties props) {
		super(name);
		_buckets = Integer.parseInt(props.getProperty(BUCKETS, BUCKETS_DEFAULT));
		histogram = new int[_buckets];
		histogramoverflow = 0;
		operations = 0;
		totallatency = 0;
		windowoperations = 0;
		windowtotallatency = 0;
		min = -1;
		max = -1;
		returncodes = new HashMap<Integer, int[]>();
	}

	@Override
	public synchronized void reportReturnCode(int code) {
		Integer Icode = code;
		if (!returncodes.containsKey(Icode))
		{
			int[] val = new int[1];
			val[0] = 0;
			returncodes.put(Icode, val);
		}
		returncodes.get(Icode)[0]++;
	}

	@Override
	public synchronized void measure(int latency) {
		if (latency / 1000 >= _buckets) {
			histogramoverflow++;
		} else {
			histogram[latency / 1000]++;
		}
		operations++;
		totallatency += latency;
		windowoperations++;
		windowtotallatency += latency;

		if ((min < 0) || (latency < min)) {
			min = latency;
		}

		if ((max < 0) || (latency > max)) {
			max = latency;
		}
	}

	@Override
	public void exportMeasurements(MeasurementsExporter exporter) throws IOException {
		double avglatency = (double)totallatency / (double)operations;
		exporter.write(getName(), "Operations", operations);
		exporter.write(getName(), "AverageLatency(us)", avglatency);
		exporter.write(getName(), "MinLatency(us)", min);
		exporter.write(getName(), "MaxLatency(us)", max);

		int opcounter = 0;
		boolean done95th = false;
		boolean done99th = false;
		double std = 0;
		double avglatencyMsec = avglatency / 1000;
		for (int i = 0; i < _buckets; i++) {
			opcounter += histogram[i];
			if ((!done95th) && (((double)opcounter) / ((double)operations) >= 0.95)) {
				exporter.write(getName(), "95thPercentileLatency(ms)", i);
				done95th = true;
			}
			if ((!done99th) && ((double)opcounter) / ((double)operations) >= 0.99) {
				exporter.write(getName(), "99thPercentileLatency(ms)", i);
				done99th = true;
			}
			std += histogram[i] * Math.pow(avglatencyMsec - i, 2);

		}
		std = Math.sqrt((double)(std / opcounter));
		exporter.write(getName(), "StdLatency(ms)", std);

		for (Integer I : returncodes.keySet()) {
			int[] val = returncodes.get(I);
			exporter.write(getName(), "Return=" + I, val[0]);
		}

		for (int i = 0; i < _buckets; i++) {
			exporter.write(getName(), Integer.toString(i), histogram[i]);
		}
		exporter.write(getName(), ">" + _buckets, histogramoverflow);
	}

	@Override
	public String getSummary() {
		if (windowoperations == 0) {
			return "";
		}
		DecimalFormat d = new DecimalFormat("#.##");
		double report = ((double)windowtotallatency) / ((double)windowoperations);
		windowtotallatency = 0;
		windowoperations = 0;
		return "[" + getName() + " AverageLatency(us)=" + d.format(report) + "]";
	}

}
