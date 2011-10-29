package api;

import java.util.HashMap;
import java.util.Map;

public class SimpleAPI {

	Map<Long, String> data = new HashMap<Long, String>();

	public Long getNewKey() {
		Long lastKey = new Long(data.size());
		return lastKey + 1;
	}

	public String add(String value) {
		Long newKey = getNewKey();
		return data.put(newKey, value);
	}

	public String get(Long key) {
		return data.containsKey(key) ? data.get(key) : null;
	}

	public String update(Long key, String value) {
		if (data.containsKey(key)) {
			return data.put(key, value);
		} else {
			return null;
		}
	}

	public boolean delete(Long key) {
		if (data.containsKey(key)) {
			return data.remove(key) != null ? true : false;
		} else {
			return false;
		}
	}
}
