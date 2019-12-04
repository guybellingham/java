package com.gus;

import java.util.LinkedHashMap;
import java.util.Objects;

public class TwoDigits {

	char[] alphabet = {'a','b','c','d','e','f','g','h','i','j',
            'k','l','m','n','o','p','q','r','s','t',
            'u','v','w','x','y','z'};
	
	boolean isBeautiful(String inputString) {
		char[] characters = inputString.toCharArray();
		LinkedHashMap<Character, Long> linkedMap = new LinkedHashMap<>();
		for (char c : alphabet) {

			long count = inputString.chars().filter(ch -> ch == c).count();
			linkedMap.put(Character.valueOf(c), count);
		}
		
		return false;
	}
	
	public class CharacterCount implements Comparable<CharacterCount> {
	    char character;
	    int count = 0;
	    CharacterCount(char theCharacter) {
	        this.character = theCharacter;
	    }
	    void increment() {
	        count++;
	    }
	    char getCharacter() {
	    	return character;
	    }
	    int getCount() {
	        return count;
	    }
	    @Override
	    public int hashCode() {
	        return Objects.hash(this);
	    }
		@Override
		public int compareTo(CharacterCount other) {
			int rc = Character.compare(getCharacter(), other.getCharacter());
			if (rc == 0) {
				rc = getCount() - other.getCount();
			}
			return rc;
		}
	}
}
