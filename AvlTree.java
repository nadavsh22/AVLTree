package oop.ex4.data_structures;

import java.util.ArrayList;
import java.util.Iterator;
import java.lang.Math;
import java.util.NoSuchElementException;


/**
 * Created by nadav on 10-May-17.
 */
public class AvlTree implements Iterable<Integer> {
    private Node root;
    private int size;
    private ArrayList<Integer> arrayList;
    private int currentSize;

    /**
     * the default constructor
     */
    public AvlTree() {
        this.root = null;
        this.size = 0;
    }

    /**
     * A constructor that builds a new AVL tree containing all unique values in the input array.
     *
     * @param data the values to add to tree.
     */
    public AvlTree(int[] data) {
        this.root = null;
        this.size = 0;
        for (int i = 0; i < data.length; i++) {
            this.add(data[i]);
        }
    }

    /**
     * A copy constructor that creates a deep copy of the given AvlTree. The new tree contains all the values
     * of the given tree, but not necessarily in the same structure.
     *
     * @param tree The AVL tree to be copied.
     */
    public AvlTree(AvlTree tree) {
        Iterator<Integer> myIterator = tree.iterator();
        while (myIterator.hasNext()) {
            this.add(myIterator.next());
        }
    }

    /**
     * Check whether the tree contains the given input value.
     *
     * @param current     the root of the subtree that is checked
     * @param searchValue value to search for
     * @param depth       the depth of the current root
     * @return the depth of the node containing requested value;
     */
    private int containsHelper(Node current, int searchValue, int depth) {
        //base case
        if (current == null) {
            return -1;
            //found the node
        } else if (current.getValue() == searchValue) {
            return depth;
        } else if (searchValue < current.getValue()) {
            return containsHelper(current.getLeft(), searchValue, depth + 1);
        } else {
            return containsHelper(current.getRight(), searchValue, depth + 1);
        }
    }

    /**
     * Check whether the tree contains the given input value.
     *
     * @param searchVal value to search for
     * @return if val is found in the tree, return the depth of the node (0 for the root) with the given
     * value if it was found in the tree, -1 otherwise.
     */
    public int contains(int searchVal) {
        return containsHelper(this.root, searchVal, 0);
    }

    /**
     * recursively finding and adding a given Node to a subtree.
     *
     * @param root    the root of the subtree
     * @param newNode a node containing the value to be added
     * @return the root of the tree after addition.
     */
    private Node addHelper(Node root, Node newNode) {
        //base case
        if (root == null) {
            return newNode;
        }
        //find out which subtree the new node belongs in
        if (newNode.getValue() < root.getValue()) {
            root.setLeft(addHelper(root.getLeft(), newNode));
        } else if (newNode.getValue() > root.getValue()) {
            root.setRight(addHelper(root.getRight(), newNode));
        } else {
            return root;
        }
        root.updateHeight();
        int balanceFactor = root.balanceFactor();
        //check 4 unbalance scenarios
        //left left
        if (balanceFactor > 1 && newNode.getValue() < root.getLeft().getValue()) {
            return rightRotate(root);
        }
        //right right
        if (balanceFactor < -1 && newNode.getValue() > root.getRight().getValue()) {
            return leftRotate(root);
        }
        //left right
        if (balanceFactor > 1 && newNode.getValue() > root.getLeft().getValue()) {
            root.setLeft(leftRotate(root.getLeft()));
            return rightRotate(root);
        }
        //right left
        if (balanceFactor < -1 && newNode.getValue() < root.getRight().getValue()) {
            root.setRight(rightRotate(root.getRight()));
            return leftRotate(root);
        }
        return root;


    }

    /**
     * Add a new node with the given key to the tree
     *
     * @param newValue the value of the new node to add
     * @return true if the value was added successfully to the tree, false otherwise(if the tree already
     * contained the value)
     */
    public boolean add(int newValue) {
        if (this.contains(newValue) > -1) {
            return false;
        } else {
            Node newNode = new Node(newValue);
            this.root = addHelper(root, newNode);
            this.size++;
            return true;
        }
    }

    /**
     * rotate subtree rooted with father
     *
     * @param father the unbalanced root
     * @return the new root of the subtree after rotation
     */
    private Node rightRotate(Node father) {
        Node child = father.getLeft();
        Node grandson = child.getRight();
        //rotation
        child.setRight(father);
        father.setLeft(grandson);
        //update height
        father.updateHeight();
        child.updateHeight();
        return child;
    }

    /**
     * rotate subtree rooted with father
     *
     * @param father the unbalanced root
     * @return the new root of the subtree after rotation
     */
    private Node leftRotate(Node father) {
        Node child = father.getRight();
        Node grandson = child.getLeft();
        //rotation
        child.setLeft(father);
        father.setRight(grandson);
        //update height
        father.updateHeight();
        child.updateHeight();
        return child;
    }

    /**
     * calculates the height difference between the two subtrees of a given root
     *
     * @param node the root to be checked
     * @return the height difference
     */
    private int balanceFactor(Node node) {
        if (node == null) {
            return 0;
        }
        return node.getLeft().height - node.getRight().height;
    }

    /**
     * finds the minimum value in a bst
     *
     * @param current the root of the tree
     * @return the minimum value.
     */
    private Node minValue(Node current) {
        Node min = current;
        while (min.getLeft() != null) {
            min = min.getLeft();
        }
        return min;
    }

    /**
     * recursively deletes a given value in a subtree, given its root
     *
     * @param current  the root of subtree
     * @param toDelete the value to delete from the tree
     * @return the root of the subtree after the value was removed
     */
    private Node deleteHelper(Node current, int toDelete) {
        if (current == null) {
            return current;
        }//checks in which subtree the value should be in
        if (toDelete < current.getValue()) {
            current.setLeft(deleteHelper(current.getLeft(), toDelete));
        } else if (toDelete > current.getValue()) {
            current.setRight(deleteHelper(current.getRight(), toDelete));
        }//if it the same as current value, this is the node to delete
        else {
            if ((current.getLeft() == null) || (current.getRight() == null)) {
                Node temp = null;
                if (current.getLeft() == null) {
                    temp = current.getRight();
                } else {
                    temp = current.getLeft();
                }
                if (temp == null) {
                    temp = current;
                    current = null;
                } else {
                    current = temp;
                }
            } else {
                Node temp = minValue(current.getRight());
                current.setValue(temp.getValue());
                current.setRight(deleteHelper(current.getRight(), temp.getValue()));
            }
        }
        if (current == null) {
            return current;
        }
        //update height
        current.updateHeight();
        int balanceFactor = current.balanceFactor();
        //check for unbalance scenarios
        //left left
        if (balanceFactor > 1 && current.getLeft().balanceFactor() >= 0) {
            return rightRotate(current);
        }
        //left right
        if (balanceFactor > 1 && current.getLeft().balanceFactor() < 0) {
            current.setLeft(leftRotate(current.getLeft()));
            return rightRotate(current);
        }
        //right right
        if (balanceFactor < -1 && current.getRight().balanceFactor() <= 0) {
            return leftRotate(current);
        }
        //right left
        if (balanceFactor < -1 && current.getRight().balanceFactor() > 0) {
            current.setRight(rightRotate(current.getRight()));
            return leftRotate(current);
        }
        return current;
    }

    /**
     * removes the node with the given value from the tree, if it exists
     *
     * @param toDelete the value to remove from the tree
     * @return true if value was found and deleted, else otherwise
     */
    public boolean delete(int toDelete) {
        if (this.contains(toDelete) > -1) {
            this.root = deleteHelper(this.root, toDelete);
            this.size--;
            return true;
        } else {
            return false;
        }
    }

    /**
     * @return the number of nodes in the tree
     */
    public int size() {
        return this.size;
    }

    /**
     * given an Array list, and a root, adds the tree to the array, inorder
     *
     * @param array
     * @param node
     */
    private void traverseAndAdd(ArrayList<Integer> array, Node node) {
        if (node != null) {
            traverseAndAdd(array, node.getLeft());
            array.add(node.getValue());
            traverseAndAdd(array, node.getRight());
        }
    }

    /**
     * @return an iterator for the Avl Tree. The returned iterator iterates over the tree nodes in an
     * ascending order, and does NOT implement the remove() method.
     */
    public Iterator<Integer> iterator() {
        arrayList = new ArrayList<>();
        traverseAndAdd(arrayList, this.root);
        this.currentSize = arrayList.size();
        Iterator<Integer> it = new Iterator<Integer>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex<currentSize && arrayList.get(currentIndex)!=null;
            }

            @Override
            public Integer next()  {
                return arrayList.get(currentIndex++);
            }

            @Override
            public void remove() {
                throw new UnsupportedOperationException();
            }
        };
        return it;
    }


    /**
     * Calculates the minimum number of nodes in an AVL tree of height h
     *
     * @param h given height
     * @return the minimum number of nodes in the tree
     */
    public static int findMinNodes(int h) {
        if (h == 0) {
            return 1;
        }
        int a = 1;
        int b = 2;
        int c;
        for (int i = 1; i < h; i++) {
            c = a + b + 1;
            a = b;
            b = c;
        }
        return b;

    }


    /**
     * Calculates the maximum number of nodes in an AVL tree of height h
     *
     * @param h given height
     * @return the maximum number of nodes in the tree
     */
    public static int findMaxNodes(int h) {
        return (int) (Math.pow(2, (h + 1))) - 1;
    }

}




