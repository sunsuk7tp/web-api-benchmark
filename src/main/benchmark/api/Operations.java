package benchmark.api;

import java.util.Random;

import benchmark.measurements.Measurements;

public interface Operations<S, K, V, U> {
	final static int ADD = 1;
	final static int GET = 2;
	final static int UPDATE = 3;
	final static String NAME_ADD = "ADD";
	final static String NAME_GET = "GET";
	final static String NAME_UPDATE = "UPDATE";

	final static String DEFAULT_LOADED_RECORDSIZE = "10000";

	Random rnd = new Random();
	Measurements measurements = Measurements.getMeasurements();

	void setOperationRatio();

	void setLoadedData(int loadcount);

	void setLoadedDataSize(long size);

	long getLoadedDataSize();

	void runOperation();

	void loadOperation(long id);

	K getKeyId();

	U getUpdateId();

	void registerMeasurements(String operation, int latency, int code);

	V createValue();

	void add(V value);

	void get();

	void update();
}
