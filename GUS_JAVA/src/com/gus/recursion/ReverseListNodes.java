package com.gus.recursion;
/**
 * Given a string of ListNodes with values of say [1, 2, 3, 4] 
 * this class can reverse the order of the nodes to give 
 * [4, 3, 2, 1] using recursion.
 * @author Gus
 * see ReverseListNodesTest
 */
public class ReverseListNodes {

	public static ListNode<Integer> reverseListNodes(ListNode<Integer> node) {
		ListNode<Integer> lastNode = findLastNode(node);
		return lastNode;
	}
	
	static ListNode<Integer> findLastNode(ListNode<Integer> node) {
		ListNode<Integer> last = node, penultimate = null; 
		while(last.hasNext()) {
			penultimate = last;
			last = last.getNext();
		}
		System.out.println("penultimate node="+penultimate+" last node="+last);
		if (penultimate != null) {
			//remove the last node
			penultimate.setNext(null);
			//recurse the remaining tail
			last.setNext(findLastNode(node));
		}
		return last;
	}
	
}
