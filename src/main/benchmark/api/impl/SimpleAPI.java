package benchmark.api.impl;

import java.util.HashMap;
import java.util.Map;

public class SimpleAPI {

	SimpleData<Long, String> data;

	public SimpleAPI() {
		data = new SimpleData<Long, String>();
	}

	private Long getNewKey() {
		Long lastKey = new Long(data.getSize());
		return lastKey + 1;
	}

	public synchronized String add(String value) {
		Long newKey = getNewKey();
		return data.add(newKey, value);
	}

	public String get(Long key) {
		return data.containsKey(key) ? data.get(key) : null;
	}

	public String update(Long key, String value) {
		return data.containsKey(key) ? data.update(key, value) : null;
	}

	public boolean delete(Long key) {
		if (data.containsKey(key)) {
			return data.delete(key) != null ? true : false;
		} else {
			return false;
		}
	}
}

class SimpleData<K, V> {
	Map<K, V> data;

	public SimpleData() {
		data = new HashMap<K, V>();
	}

	public int getSize() {
		return data.size();
	}

	public boolean containsKey(K key) {
		return data.containsKey(key);
	}

	public V add(K key, V value) {
		return data.put(key, value);
	}

	public V get(K key) {
		return data.get(key);
	}

	public V update (K key, V value) {
		return data.put(key, value);
	}

	public V delete(K key) {
		return data.remove(key);
	}
}
