package com.Assign_2;

//Sebastian Wood 6664189
//Intellij

//Node fro a tree structure
public class Node {
    //The "left" subtree
    Node firstChild;
    //The item that is actually stored in the node
    final String item;
    //The "right" subtree
    Node nextSibling;

    //Creates a new node with values given
    public Node(Node firstChild, String item, Node nextSibling) {
        this.firstChild = firstChild;
        this.item = item;
        this.nextSibling = nextSibling;
    }

    //Displays the node's item
    public void displayNode() {
        System.out.print(item);
    }

    //Displays the node's item in a format based on bool provided
    public void displayNode(boolean isParent) {
        //If node is a parent display item then add :
        if(isParent) {
            System.out.print(item + ": ");
        }
        //If node is child then display item then add ->
        else {
            System.out.print(" -> " + item);
        }
    }

    //Lets you have the string of the node
    //Mainly for readability during debugging process
    @Override
    public String toString() {
        return item;
    }
}
