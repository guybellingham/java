package com.gus.tree;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
/**
 * Simple Node implementation for a {@link Tree} that holds 
 * some <code>data</code> (that can't be null) of type <code>T</code>, 
 * and can have a list of <code>children</code> nodes.
 * <li>All nodes except the root node have a <code>parent</code> node.  
 * <li>We could get fancy and keep the 'level' of the Node and 'left -> right' indexes 
 * and references to Siblings ...etc But we currently don't (kiss). 
 * 
 * @author guybe
 *
 * @param <T> = my data or entity type
 */
public class Node<T> implements TreeNode<T> {
	/**
	 * My unique id, which probably comes from my {@link #data}.
	 */
	private long id;  			
	/**
	 * My data bean or business object. 
	 */
	private T data;
	/**
	 * Reference to my 'parent' Node (unless I'm the root).
	 */
    private TreeNode<T> parent;
    /**
     * I can have a list of 'child' nodes if I'm not a 'leaf' node.
     */
    private List<TreeNode<T>> children;
    
	public Node(TreeNode<T> parentNode, T data) {
		assert(data != null);
		this.parent = parentNode;
//		this.id = data.getId();   			//T needs to have a getId() method!
		this.data = data;
        this.children = new ArrayList<TreeNode<T>>();
	}

	/* (non-Javadoc)
	 * @see com.innotas.service.json.entity.tree.TreeNode#getId()
	 */
	@Override
	public long getId() {
		return id;
	}

	/* (non-Javadoc)
	 * @see com.innotas.service.json.entity.tree.TreeNode#getData()
	 */
	@Override
	public T getData() {
		return data;
	}

	/* (non-Javadoc)
	 * @see com.innotas.service.json.entity.tree.TreeNode#getParent()
	 */
	@Override
	public TreeNode<T> getParent() {
		return parent;
	}

	/* (non-Javadoc)
	 * @see com.innotas.service.json.entity.tree.TreeNode#getChildren()
	 */
	@Override
	public List<TreeNode<T>> getChildren() {
		return children;
	}

	@Override
	public int getChildCount() {
		return children.size();
	}

	@Override
	public void addChildData(Collection<T> dataCollection) {
		for (Iterator<T> iterator = dataCollection.iterator(); iterator.hasNext();) {
			T data = iterator.next();
			getChildren().add(new Node<T>(this, data));
		}
	}
   
    @Override
    public int hashCode() {
        return Math.toIntExact(id);
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.lang.Object#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if(obj instanceof Node) {
        	Node other = (Node)obj;
        	if(other.getId() == getId()) {
        		return true;
        	}
        }
        return false;
    }

    @Override
    public String toString() {
        return "node:{\"id\":"+id+", \"parent\":"+getParent()==null?null:getParent().getId()+"}";
    }

}
