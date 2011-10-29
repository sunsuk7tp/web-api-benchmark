package benchmark.measurements.exporter;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;

import org.codehaus.jackson.JsonFactory;
import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.impl.DefaultPrettyPrinter;

/**
 * Export measurements into a machine readable JSON file.
 */
public class JSONMeasurementsExporter implements MeasurementsExporter {

	private JsonFactory factory = new JsonFactory();
	private JsonGenerator g;

	public JSONMeasurementsExporter(OutputStream os) throws IOException {

		BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(os));
		g = factory.createJsonGenerator(bw);
		g.setPrettyPrinter(new DefaultPrettyPrinter());
	}

	@Override
	public void write(String metric, String measurement, int i) throws IOException {
		g.writeStartObject();
		g.writeStringField("metric", metric);
		g.writeStringField("measurement", measurement);
		g.writeNumberField("value", i);
		g.writeEndObject();
	}

	@Override
	public void write(String metric, String measurement, double d) throws IOException {
		g.writeStartObject();
		g.writeStringField("metric", metric);
		g.writeStringField("measurement", measurement);
		g.writeNumberField("value", d);
		g.writeEndObject();
	}

	@Override
	public void close() throws IOException {
		if (g != null) {
			g.close();
		}
	}

}
