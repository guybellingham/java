package com.gus.tree;

public class Tree<T> {
	/**
	 * The 'root' node usually has no 'parent' node 
	 * but has children and data.
	 */
    private TreeNode<T> root;  

    public Tree(T rootData) {
        root = new Node<T>(null, rootData);
    }

	public TreeNode<T> getRoot() {
		return root;
	}

	public boolean isEmpty() {
		return root == null;
	}
}
