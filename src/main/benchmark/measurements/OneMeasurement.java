package benchmark.measurements;

import java.io.IOException;

import benchmark.measurements.exporter.MeasurementsExporter;

/**
 * A single measured metric (such as READ LATENCY)
 */
public abstract class OneMeasurement {

	String _name;

	public String getName() {
		return _name;
	}

	/**
	 * @param _name
	 */
	public OneMeasurement(String _name) {
		this._name = _name;
	}

	public abstract void reportReturnCode(int code);

	public abstract void measure(int latency);

	public abstract String getSummary();

	/**
	 * Export the current measurements to a suitable format.
	 *
	 * @param exporter Exporter representing the type of format to write to.
	 * @throws IOException Thrown if the export failed.
	 */
	public abstract void exportMeasurements(MeasurementsExporter exporter) throws IOException;
}
