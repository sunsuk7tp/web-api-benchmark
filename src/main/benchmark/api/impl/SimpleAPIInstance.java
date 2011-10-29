package benchmark.api.impl;

import benchmark.api.BenchmarkInstance;

public class SimpleAPIInstance implements BenchmarkInstance<SimpleAPI, Long, String, String> {

	SimpleAPI simpleAPI;

	@Override
	public void setInstance() {
		simpleAPI = new SimpleAPI();
	}

	@Override
	public SimpleAPI getInstance() {
		return simpleAPI;
	}

	@Override
	public String add(String value) {
		return simpleAPI.add(value);
	}

	@Override
	public String get(Long key) {
		return simpleAPI.get(key);
	}

	@Override
	public String update(Long key, String uval) {
		return simpleAPI.update(key, uval);
	}

	@Override
	public String delete(Long key) {
		return simpleAPI.delete(key) ? "true" : null;
	}
}
