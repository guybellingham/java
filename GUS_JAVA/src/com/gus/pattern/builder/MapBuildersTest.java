package com.gus.pattern.builder;

import static org.junit.Assert.*;
import static java.util.AbstractMap.*;

import java.util.AbstractMap;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.Test;

public class MapBuildersTest {

	@Test
	public void testMapBuilder() {
		Map<Integer, String> zeroToTenMap = MapBuilders.<Integer, String>builder().
	        put(0, "zero").
	        put(1, "one").
	        put(2, "two").
	        put(3, "three").
	        put(4, "four").
	        put(5, "five").
	        put(6, "six").
	        put(7, "seven").
	        put(8, "eight").
	        put(9, "nine").
	        put(10, "ten").
	        unmodifiable(true).
	        build();
		assertNotNull(zeroToTenMap);
		assertTrue(zeroToTenMap.size() == 11);
		assertEquals("one", zeroToTenMap.get(1));
		try {
			zeroToTenMap.put(12, "twelve");
		} catch(Exception e) {
			log("Attempt to modify zeroToTenMap failed with exception "+e);
		}
	}

	/**
	 * <li>creates a 'Stream' of map entries {@linkplain SimpleEntry}.
	 * <li>collects all the entries and creates a Map by splitting up each Entry in a key and a value
	 * <li>The 'diamond' is important after each <code>SimpleEntry</code> unfortunately.
	 */
	@Test
	public void testJava8StreamToMap() {
		Map<Integer,String> oneToTwelveMap = Collections.unmodifiableMap(Stream.of(
                new SimpleEntry<>(1, "one"),
                new SimpleEntry<>(2, "two"),
                new SimpleEntry<>(3, "three"),
                new SimpleEntry<>(4, "four"),
                new SimpleEntry<>(5, "five"),
                new SimpleEntry<>(6, "six"),
                new SimpleEntry<>(7, "seven"),
                new SimpleEntry<>(8, "eight"),
                new SimpleEntry<>(9, "nine"),
                new SimpleEntry<>(10, "ten"),
                new SimpleEntry<>(11, "eleven"),
                new SimpleEntry<>(12, "twelve"))
                .collect(Collectors.toMap((e) -> e.getKey(), (e) -> e.getValue())));
		assertNotNull(oneToTwelveMap);
		assertTrue(oneToTwelveMap.size() == 12);
		assertEquals("twelve", oneToTwelveMap.get(12));
	}
	
	@Test
	public void testSimpleStreamToMap() {
		Map<Integer,String> primesMap = Collections.unmodifiableMap(Stream.of(
                entry(1, "one"),
                entry(2, "three"),
                entry(3, "five"),
                entry(4, "seven"),
                entry(5, "eleven"),
                entry(6, "thirteen"))
                .collect(MapBuilders.entriesToMap()));
		
		assertNotNull(primesMap);
		assertTrue(primesMap.size() == 6);
		assertEquals("thirteen", primesMap.get(6));
	}
	
	public void log(String message) {
		System.out.println("MapBuildersTest: " + message);
	}
	/**
	 * Helper method to 'build' a simple map entry.
	 * @param key
	 * @param value
	 * @return the map Entry.
	 */
	public static <K, V> Map.Entry<K, V> entry(K key, V value) {
        return new AbstractMap.SimpleEntry<>(key, value);
    }

}
