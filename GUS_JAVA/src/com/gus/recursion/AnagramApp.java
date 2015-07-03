package com.gus.recursion;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class AnagramApp {

	public static void main(String[] args) {
		// 
		if(args.length == 0 || args.length > 1){
			System.out.println("Usage> AnagramApp word \n Where word is a string of characters.");
			return;
		}
		AnagramApp app = new AnagramApp();
		String word = args[0];
		
		Set<String> anagrams = app.getCombinations(word);
		Iterator<String> itr = anagrams.iterator();
		while (itr.hasNext()) {
			System.out.println(itr.next());
		}
	}

	protected Set<String> getCombinations(String word) {
		Set<String> anagrams = new HashSet<String>();
		if(word.length()<2){
			anagrams.add(word);
		} else {
			char[] arr = word.toCharArray();
			int max = (arr.length - 1);
			Set<String> subAnagrams = null;
			for (int i = 0; i < arr.length; i++) {
				char wordChar = word.charAt(i);
				String sub = null;
				if(i == 0) {
					sub = word.substring(1);
				} else 
				if(i == max) {
					sub = word.substring(0, max);	
				} else {
					sub = word.substring(0, i) + word.substring(i+1);	
				}
				subAnagrams = getCombinations(sub);
				Iterator<String> itr = subAnagrams.iterator();
				while (itr.hasNext()) {
					anagrams.add(wordChar + itr.next());
				}
			}
		}
		return anagrams;
	}
}
