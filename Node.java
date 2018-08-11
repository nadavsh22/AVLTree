package oop.ex4.data_structures;
/**
 * Created by nadav on 10-May-17.
 */
public class Node {
    private int value;
    private Node right;
    private Node left;
    public int height;

    /**
     * constructor of a node with a value
     *
     * @param value the nodes value
     */
    public Node(int value) {
        this.value = value;
        this.height = 0;
        this.left = null;
        this.right = null;
    }

    /**
     * update the nodes height when called
     */
    public void updateHeight() {
        int leftHeight = -1;
        int rightHeight = -1;
        if (this.left != null) {
            leftHeight = left.height;
        }
        if (this.right != null) {
            rightHeight = right.height;
        }
        if (leftHeight > rightHeight) {
            this.height = leftHeight + 1;
        } else {
            this.height = rightHeight + 1;
        }
    }

    public int balanceFactor() {
        int leftHeight = -1;
        int rightHeight = -1;
        if (this.left != null) {
            leftHeight = left.height;
        }
        if (this.right != null) {
            rightHeight = right.height;
        }
        return (leftHeight - rightHeight);
    }

    public Node getLeft() {
        return this.left;
    }

    public Node getRight() {
        return this.right;
    }


    public int getValue() {
        return this.value;
    }

    public void setValue(int value) {

        this.value = value;
    }

    public void setRight(Node right) {

        this.right = right;
    }

    public void setLeft(Node left) {

        this.left = left;
    }


}

