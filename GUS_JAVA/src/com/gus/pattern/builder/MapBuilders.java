package com.gus.pattern.builder;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.function.Supplier;
import java.util.stream.Collector;
import java.util.stream.Collectors;

public class MapBuilders {

	private MapBuilders() {
	}
	
	/**
	 * Gets a {@linkplain MapBuilder} for a HashMap.
	 * @return
	 */
	public static <K, V> MapBuilder<K, V> builder() {
        return builder(HashMap::new);
    }

	/**
	 * Can be used to get a map 'builder' for any kind of map from a {@linkplain Supplier}. 
	 * @param mapSupplier
	 * @return {@linkplain MapBuilder}.
	 */
    public static <K, V> MapBuilder<K, V> builder(Supplier<Map<K, V>> mapSupplier) {
        return new MapBuilder<>(mapSupplier.get());
    }

    /**
	 * Gets a {@linkplain ConcurrentMapBuilder} for a HashMap.
	 * @return
	 */
    public static <K, V> ConcurrentMapBuilder<K, V> concurrentBuilder() {
        return concurrentBuilder(ConcurrentHashMap::new);
    }
    /**
	 * Can be used to get a concurrent map 'builder' for any kind of map from a {@linkplain Supplier}. 
	 * @param mapSupplier
	 * @return {@linkplain MapBuilder}.
	 */
    public static <K, V> ConcurrentMapBuilder<K, V> concurrentBuilder(Supplier<ConcurrentMap<K, V>> mapSupplier) {
        return new ConcurrentMapBuilder<>(mapSupplier.get());
    }

    /**
     * Hidden 'archetypal' Map builder class, has a reference to the Map <M> and 
     * puts entries into it and returns it when <code>build()</code> is called.
     * 
     * @param <M> - the type of map to build Map<K, V>
     * @param <K> - the key type
     * @param <V> - the value type
     */
    private static class BaseBuilder<M extends Map<K, V>, K, V> {

        protected final M map;

        public BaseBuilder(M map) {
            this.map = map;
        }

        public BaseBuilder<M, K, V> put(K key, V value) {
            map.put(key, value);
            return this;
        }

        public M build() {
            return map;
        }

    }

    /**
     * Normal Map builder class. Can be configured to build an unmodifiable 
     * (Immutable) map. 
     *
     * @param <K>
     * @param <V>
     */
    public static class MapBuilder<K, V> extends BaseBuilder<Map<K, V>, K, V> {

        private boolean unmodifiable;

        public MapBuilder(Map<K, V> map) {
            super(map);
        }

        @Override
        public MapBuilder<K, V> put(K key, V value) {
            super.put(key, value);
            return this;
        }

        public MapBuilder<K, V> unmodifiable(boolean unmodifiable) {
            this.unmodifiable = unmodifiable;
            return this;
        }

        @Override
        public Map<K, V> build() {
            if (unmodifiable) {
                return Collections.unmodifiableMap(super.build());
            } else {
                return super.build();
            }
        }

    }

    public static class ConcurrentMapBuilder<K, V> extends BaseBuilder<ConcurrentMap<K, V>, K, V> {

        public ConcurrentMapBuilder(ConcurrentMap<K, V> map) {
            super(map);
        }

        @Override
        public ConcurrentMapBuilder<K, V> put(K key, V value) {
            super.put(key, value);
            return this;
        }

    }
    
    public static <K, V> Collector<Map.Entry<K, V>, ?, Map<K, V>> entriesToMap() {
        return Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue());
    }

    public static <K, V> Collector<Map.Entry<K, V>, ?, ConcurrentMap<K, V>> entriesToConcurrentMap() {
        return Collectors.toConcurrentMap((e) -> e.getKey(), (e) -> e.getValue());
    }
}
