package benchmark.api;

public interface BenchmarkInstance<S, K, V, U> {
	void setInstance();

	S getInstance();

	V add(V value);

	V get(K key);

	U update(K key, U uval);

	V delete(K key);
}
