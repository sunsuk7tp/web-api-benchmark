package benchmark.measurements;

import java.io.IOException;
import java.util.HashMap;
import java.util.Properties;

import benchmark.measurements.exporter.MeasurementsExporter;

public class Measurements {

	static Measurements singleton = null;

	static Properties measurementproperties = null;

	HashMap<String, OneMeasurement> data;
	private Properties _props;

	public static void setProperties(Properties props) {
		measurementproperties = props;
	}

	public synchronized static Measurements getMeasurements() {
		if (singleton == null) {
			singleton = new Measurements(measurementproperties);
		}

		return singleton;
	}

	public Measurements(Properties props) {
		data = new HashMap<String, OneMeasurement>();

		_props = props;

	}

	OneMeasurement constructOneMeasurement(String name) {
		return new OneMeasurementHistogram(name, _props);
	}

	public synchronized void measure(String operation, int latency) {
		if (!data.containsKey(operation)) {
			synchronized (this) {
				if (!data.containsKey(operation)) {
					data.put(operation, constructOneMeasurement(operation));
				}
			}
		}

		try {
			data.get(operation).measure(latency);
		} catch (ArrayIndexOutOfBoundsException e) {
			System.out.println("measure: ArrayIndexOutOfBoundsException");
		}
	}

	public void reportReturnCode(String operation, int code) {
		if (!data.containsKey(operation)) {
			synchronized (this) {
				if (!data.containsKey(operation)) {
					data.put(operation, constructOneMeasurement(operation));
				}
			}
		}
		data.get(operation).reportReturnCode(code);
	}

	public void exportMeasurements(MeasurementsExporter exporter) throws IOException {
		for (OneMeasurement measurement : data.values()) {
			measurement.exportMeasurements(exporter);
		}
	}

	public String getSummary() {
		String ret = "";
		for (OneMeasurement m : data.values()) {
			ret += m.getSummary() + " ";
		}

		return ret;
	}
}
