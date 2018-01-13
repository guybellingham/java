package com.gus.tree;

import java.util.Iterator;


/**
 * A class that specializes in 'walking' up and down a tree of business objects 
 * and finding nodes.
 * @author guybe
 */
public class TreeWalker<T> {
	/**
	 * Must have a tree.
	 */
	Tree<T> tree; 
	/**
	 * 'Level' we are at in the tree, the root is at level 1. 
	 * The level of a node is defined by 1 + (the number of connections between the node and the root).
	 */
	int level; 
	/**
	 * Ref to the 'current' node in the Tree.
	 */
	TreeNode<T> current; 
	
	public TreeWalker(Tree<T> tree) {
		this.tree = tree;
	}
	/**
	 * Find a node by walking the tree and comparing the <code>id</code> of 
	 * each node with the given <code>id</code>. 
	 * @param id
	 * @return the TreeNode with the given <code>id</code> or <code>null</code>.
	 */
	public TreeNode<T> getTreeNodeById(long id) {
		TreeNode<T> node = tree.getRoot();
		return getTreeNodeById(node, id);
	}
	
	TreeNode<T> getTreeNodeById(TreeNode<T> node, long id) {
		if(node.getId() == id) {
			return node; 
		} else {
			if(node.getChildCount() > 0) {
				for(Iterator<TreeNode<T>> childIter = node.getChildren().iterator(); childIter.hasNext();) {
					TreeNode<T> childNode = childIter.next();
					TreeNode<T> foundNode = getTreeNodeById(childNode, id);
					if(foundNode != null) {
						return foundNode;
					}
				}
			}
		}
		return null;
	}
	
	public TreeNode<T> next() {
		TreeNode<T> next = null;
		if(current == null) {
			next = tree.getRoot();  					//start at the root
		} else
		if(current.getChildren().isEmpty()) {  			//no children,get my next sibling or parents sibling
			if(current.getParent() != null) {
				next = getSibling(current); 
			}
		} else {
			next = current.getChildren().get(0);  		//get my first child	
		}
		current = next;
		return next;
	}

	public TreeNode<T> getSibling(TreeNode<T> current) {
		TreeNode<T> parent = current.getParent();
		if(parent != null) {
			int index = parent.getChildren().indexOf(current);
			if(index > -1) {  							//found current
				if(++index < parent.getChildCount()) {			//is there a next?
					return parent.getChildren().get(index);
				}
			}
			return getSibling(parent);
		}
		return null;
	}
}
