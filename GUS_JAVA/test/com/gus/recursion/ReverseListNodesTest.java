package com.gus.recursion;

import static org.junit.Assert.*;

import org.junit.Test;

public class ReverseListNodesTest {

	/**
	 * Given a list of nodes [1, 2] then reverseListNodes(node1) 
	 * should return [2, 1].
	 */
	@Test
	public void testReverseTwo() {
		ListNode<Integer> node2 = new ListNode<>(2);
		ListNode<Integer> node1 = new ListNode<>(1);
		node1.setNext(node2);
		ListNode<Integer> lastNode = ReverseListNodes.reverseListNodes(node1);
		assertEquals(node2, lastNode);
		assertEquals(node1, lastNode.getNext());
	}

	/**
	 * Given a list of nodes [1, 2, 3, 4, 5] then reverseListNodes(node1) 
	 * should return [5, 4, 3, 2, 1].
	 */
	@Test
	public void testReverseFive() {
		ListNode<Integer> node5 = new ListNode<>(5);
		ListNode<Integer> node4 = new ListNode<>(4);
		node4.setNext(node5);
		ListNode<Integer> node3 = new ListNode<>(3);
		node3.setNext(node4);
		ListNode<Integer> node2 = new ListNode<>(2);
		node2.setNext(node3);
		ListNode<Integer> node1 = new ListNode<>(1);
		node1.setNext(node2);
		ListNode<Integer> lastNode = ReverseListNodes.reverseListNodes(node1);
		assertEquals(node5, lastNode);
		assertEquals(node4, lastNode.getNext());
		assertEquals(node3, lastNode.getNext().getNext());
		assertEquals(node2, lastNode.getNext().getNext().getNext());
	}
}
