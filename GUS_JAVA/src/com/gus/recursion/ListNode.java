package com.gus.recursion;

public class ListNode<T> {
	private T value;
	private ListNode<T> next;
	ListNode(T theValue) {
		setValue(theValue);
	}
	public T getValue() {
		return value;
	}
	void setValue(T theValue) {
		this.value = theValue;
	}
	public ListNode<T> getNext() {
		return next;
	}
	public void setNext(ListNode<T> nextNode) {
		this.next = nextNode;
	}
	boolean hasNext() {
		return (getNext() != null) ? true : false; 
	}
	
	public String toString() {
		return getValue().toString();
	}
}
