package benchmark.api.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import benchmark.api.*;
import benchmark.generator.IntegerGenerator;
import benchmark.generator.UniformIntegerGenerator;
import benchmark.generator.Utils;
import benchmark.generator.ZipfianGenerator;

public class SimpleAPIOperations implements Operations<SimpleAPIInstance, Long, String, String>{
	BenchmarkInstance<SimpleAPI, Long, String, String> instance;
	final static int DELETE = 4;
	final static String NAME_DELETE = "DELETE";
	private static double ADD_RATIO, GET_RATIO, UPDATE_RATIO, DELETE_RATIO;
	static Map<Integer, Integer> opRatioMap;

	private final static String DEFAULT_ADD_RATIO = "1.0";
	private final static String DEFAULT_GET_RATIO = "87.5";
	private final static String DEFAULT_UPDATE_RATIO = "11.0";
	private final static String DEFAULT_DELETE_RATIO = "0.5";
	private final static String DEFAULT_VALUE_LENGTH = "8";

	List<String> data;
	IntegerGenerator rndKey;
	int valueLen;
	long loaded_recordsize;

	Properties props;

	public SimpleAPIOperations(Properties props) {
		this.props = props;
		this.instance = new SimpleAPIInstance();
		int loadedSize = Integer.parseInt(props.getProperty("loadcount", DEFAULT_LOADED_RECORDSIZE));
		valueLen = Integer.parseInt(props.getProperty("valuelen", DEFAULT_VALUE_LENGTH));

		String dist = props.getProperty("distribution", DEFAULT_DISTRIBUTION);
		if (dist.equals("zipfian")) {
			rndKey = new ZipfianGenerator(0, loadedSize);
		} else if (dist.equals("uniform")) {
			rndKey = new UniformIntegerGenerator(0, loadedSize);
		} else {
			rndKey = new ZipfianGenerator(0, loadedSize);
		}

		setOperationRatio();
	}

	@Override
	public void setOperationRatio() {
		ADD_RATIO = Double.parseDouble(props.getProperty(NAME_ADD, DEFAULT_ADD_RATIO));
		GET_RATIO = Double.parseDouble(props.getProperty(NAME_GET, DEFAULT_GET_RATIO));
		UPDATE_RATIO = Double.parseDouble(props.getProperty(NAME_UPDATE, DEFAULT_UPDATE_RATIO));
		DELETE_RATIO = Double.parseDouble(props.getProperty(NAME_DELETE, DEFAULT_DELETE_RATIO));
		if (ADD_RATIO > 0) {
			opRatioMap.put(ADD, (int)((ADD_RATIO) * 10));
		} else {
			opRatioMap.put(ADD, 0);
		}
		if (GET_RATIO > 0) {
			opRatioMap.put(GET, (int)((ADD_RATIO + GET_RATIO) * 10));
		} else {
			opRatioMap.put(GET, 0);
		}
		if (UPDATE_RATIO > 0) {
			opRatioMap.put(UPDATE, (int)((ADD_RATIO + GET_RATIO + UPDATE_RATIO) * 10));
		} else {
			opRatioMap.put(UPDATE, 0);
		}
		if (DELETE_RATIO > 0) {
			opRatioMap.put(DELETE, (int)((ADD_RATIO + GET_RATIO + UPDATE_RATIO + DELETE_RATIO) * 10));
		} else {
			opRatioMap.put(DELETE, 0);
		}
	}

	@Override
	public void setLoadedData(int loadcount) {
		List<String> data = new ArrayList<String>(loadcount);
		int valueLen = Integer.parseInt(props.getProperty("valuelen", "8"));
		for (int i = 0; i < loadcount; i++) {
			data.add(Utils.ASCIIString(valueLen));
		}
		setLoadedDataSize(loadcount);
	}

	@Override
	public void setLoadedDataSize(long size) {
		loaded_recordsize = size;
	}

	@Override
	public long getLoadedDataSize() {
		return loaded_recordsize;
	}

	@Override
	public void runOperation() {
		int rndNum = rnd.nextInt(100 * 10);
		if (rndNum < opRatioMap.get(ADD)) {
			add(null);
		} else if (rndNum < opRatioMap.get(GET)) {
			get();
		} else if (rndNum < opRatioMap.get(UPDATE)) {
			update();
		} else {
			delete();
		}
	}

	@Override
	public void loadOperation(long id) {
		String value = data.get((int) id);
		if (value != null) {
			add(value);
		}
	}

	@Override
	public Long getKeyId() {
		long keyId = 0;
		while (keyId <= 0) {
			keyId = rndKey.nextInt();
		}
		return keyId;
	}

	@Override
	public String getUpdateId() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void registerMeasurements(String operation, int latency, int code) {
		measurements.measure(operation, latency);
		measurements.reportReturnCode(operation, code);
	}

	@Override
	public String createValue() {
		// TODO 自動生成されたメソッド・スタブ
		return null;
	}

	@Override
	public void add(String value) {
		long st = System.nanoTime();
		String resVal = instance.add(value);
		long en = System.nanoTime();
		int res = resVal != null ? 0 : -1;
		registerMeasurements(NAME_ADD, (int)((en - st) / 1000), res);
	}

	@Override
	public void get() {
		Long key = getKeyId();

		long st = System.nanoTime();
		String resVal = instance.get(key);
		long en = System.nanoTime();
		int res = resVal != null ? 0 : -1;
		registerMeasurements(NAME_GET, (int)((en - st) / 1000), res);
	}

	@Override
	public void update() {
		Long key = getKeyId();
		String val = Utils.ASCIIString(valueLen);

		long st = System.nanoTime();
		String resVal = instance.update(key, val);
		long en = System.nanoTime();
		int res = resVal != null ? 0 : -1;
		registerMeasurements(NAME_UPDATE, (int)((en - st) / 1000), res);
	}

	public void delete() {
		Long key = getKeyId();
		long st = System.nanoTime();
		String resVal = instance.delete(key);
		long en = System.nanoTime();
		int res = resVal != null ? 0 : -1;
		registerMeasurements(NAME_DELETE, (int)((en - st) / 1000), res);
	}

}
