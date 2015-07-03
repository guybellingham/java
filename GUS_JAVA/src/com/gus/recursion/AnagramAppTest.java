package com.gus.recursion;

import static org.junit.Assert.*;

import java.util.Set;

import org.junit.Test;

public class AnagramAppTest {

	private AnagramApp app = new AnagramApp();
	
	@Test
	public void testCombinationsOfTwo() {
		Set<String> anagrams = app.getCombinations("AB");
		assertTrue(2 == anagrams.size());
		assertTrue(anagrams.contains("BA"));
	}

	@Test
	public void testCombinationsOfThree() {
		Set<String> anagrams = app.getCombinations("PAM");
		assertTrue(6 == anagrams.size());
		assertTrue(anagrams.contains("MAP"));
		assertTrue(anagrams.contains("AMP"));
		assertTrue(anagrams.contains("PMA"));
	}
	
	@Test
	public void testCombinationsOfFour() {
		Set<String> anagrams = app.getCombinations("BEAR");
		assertTrue(24 == anagrams.size());
		assertTrue(anagrams.contains("BARE"));
		assertTrue(anagrams.contains("ERAB"));
		assertTrue(anagrams.contains("ARBE"));
	}
}
