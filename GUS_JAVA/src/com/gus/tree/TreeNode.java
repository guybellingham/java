package com.gus.tree;

import java.util.Collection;
import java.util.List;

public interface TreeNode<T> {

	/**
	 * @return the <i>unique</i> numeric <code>id</code> of this node.
	 */
	long getId();
	/**
	 * @return the data or business object contained in this node.
	 */
	T getData();
	/**
	 * @return a copy of the reference to my 'parent' node or <code>null</code> 
	 * if this node is the 'root' of the tree.
	 */
	TreeNode<T> getParent();
	/**
	 * @return the List of my child nodes (which may be empty but not null).
	 */
	List<TreeNode<T>> getChildren();
	/**
	 * Adds the given <code>data</code> in child Nodes under this Node.
	 * @param data
	 */
	void addChildData(Collection<T> data);
	
	int getChildCount();
	
	default boolean isLeaf() {
		return getChildCount() == 0;
	}
	
	String toString();
	
}