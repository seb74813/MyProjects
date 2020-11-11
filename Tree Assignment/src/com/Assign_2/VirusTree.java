package com.Assign_2;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.Scanner;

//Sebastian Wood 6664189
//Intellij

//Allows the creation and testing of a tree structure
public class VirusTree {
    //Root of the tree
    Node root;

    //Creates a tree and then tests various traversals of that tree
    public VirusTree() throws FileNotFoundException {
        //Scanner which allows input from user
        Scanner scan = new Scanner(System.in);
        //Request for input from user
        System.out.println("Input file name");
        //tree_of_virus_input.txt
        //Reads user input
        String fileName = scan.nextLine();
        //Creates file from user input
        File file = new File(fileName);
        //Scanner which allows input from file
        scan = new Scanner(file);

        //Stores input from file
        LinkedList<String> words;

        //Current node
        Node current;
        //Reads from file to create tree, Stops when file is empty
        while(scan.hasNext()) {
            //The next line of the file
            String line = scan.nextLine();
            //Turns the line from file into separate values
            words = stringMaker(line);

            //If this is the first time through the wile loop
            if(root == null) {
                //Create and display root with first value from file
                root = new Node(null, words.removeFirst(), null);
                root.displayNode(true);
                //Sets the roots first child as second value from file
                root.firstChild = new Node(null, words.removeFirst(), null);
                //Sets current for next part
                current = root.firstChild;
                current.displayNode(false);
            }
            //If this is not the first time running the while loop
            else {
                //Sets current to parent
                current = searchTree(root, words.removeFirst());
                //Checks if there if the parent already has a child
                if(current.firstChild != null) {
                    current.displayNode(true);
                    //Sets current to first child
                    current = current.firstChild;
                    current.displayNode(false);
                    //Sets current to farthest sibling for next part
                    while(current.nextSibling != null) {
                        current = current.nextSibling;
                        current.displayNode(false);
                    }
                }
                //If the parent has no children
                else {
                    //Create first child and set current to it for next part
                    current.firstChild = new Node(null, words.removeFirst(), null);
                    current = current.firstChild;
                    current.displayNode(false);
                }
            }
            //Sets the siblings of current. Keeps going until there are no values left from the line
            while(!words.isEmpty()) {
                current.nextSibling = new Node(null, words.removeFirst(), null);
                current = current.nextSibling;
                current.displayNode(false);
            }
            System.out.println();
        }
        //Testing of the traversal methods
        System.out.println("Breadth First:");
        breadthFirst(root);
        System.out.println();

        System.out.println("Pre-Order");
        preOrder(root);
        System.out.println();

        System.out.println("Post-Order");
        postOrder(root);
        System.out.println();

        //Testing of the height method
        System.out.println("Height:");
        System.out.println(getHeight(root));
        System.out.println();

        //Testing of the length between method
        //Something to note, The last two test should result in 2 and 3 respectively. I don't know why the pdf says 1 and 2
        //By the logic put out by the first two tests that should be wrong
        lengthBetween(root, "Ebola virus", "Bombali virus");
        System.out.println();

        lengthBetween(root, "Ebola virus", "Marburg virus");
        System.out.println();

        lengthBetween(root, "HCoV-OC43", "Hcov-229E");
        System.out.println();

        lengthBetween(root, "SARS-CoV", "Zika virus");
        System.out.println();
    }

    //Searches the tree iteratively for a Node with a specific key, Searches Breadth First
    private Node searchTree(Node T, String key) {
        //Queue that allows searching nodes in specific order
        LinkedList<Node> queue = new LinkedList<>();

        //Adds root to queue
        queue.add(T);
        //Loops while there are still nodes left
        while(!queue.isEmpty()) {
            //Removes current node
            Node temp = queue.removeFirst();
            //Checks if node is null
            if(temp != null) {
                //Algorithm for breadth first searching of a tree
                if (temp.item.equals(key)) return temp;
                if (temp.firstChild != null)
                    queue.addLast(temp.firstChild);
                if (temp.nextSibling != null)
                    queue.add(temp.nextSibling);
            }
        }
        //Means Node with key was not found
        return null;
    }

    //Takes the raw input from input file and turns it into separate values
    private LinkedList<String> stringMaker(String line) {
        //Creates list for storage
        LinkedList<String> words = new LinkedList<>();
        //Turns line into char array
        char[] array = line.toCharArray();
        //Makes a builder for the string
        StringBuilder builder = new StringBuilder();

        //Creates word from char array. Separates each word based on commas
        for (char c : array) {
            if (c == ',') {
                words.addLast(builder.toString());
                builder = new StringBuilder();
            } else {
                builder.append(c);
            }
        }

        //Adds last word to storage
        words.addLast(builder.toString());
        builder = new StringBuilder();

        //Returns result
        return words;
    }

    //Gets the height of a node
    private int getHeight(Node T) {
        //Checks if the end of a branch has been reached and adjusts final result
        if(T == null) {
            return -1;
        }
        //Checks if T has a sibling
        else if(T.nextSibling != null) {
            //Gets the max value between the child subtree and sibling subtree I.E. left and right subtree
            // Adds 1 to left subtree because it is traversing down
            return Math.max(1 + getHeight(T.firstChild), getHeight(T.nextSibling));
        }
        //If T is the last sibling
        else {
            return 1 + getHeight(T.firstChild);
        }
    }
    //The time complexity of getHeight is O(n) this is because each node of the tree is visited and during
    //each visit only one operation is performed

    //Finds the length between two nodes from its ancestor. This assumes that the two nodes
    //are the leaves of the tree and the tree is complete
    private Node lengthBetween(Node T, String key1, String key2) {
        //Check for the end of a branch
        if(T == null) return null;

        //Checks if the two key nodes are both direct siblings of current node and outputs result
        if(isSiblings(T.firstChild, key1, key2)) {
            System.out.println("The distance between " + key1 + " and " +
                    key2 + " is " + getHeight(T) + ". They have common ancestor " + T.item);
            //Returns the current Node
            return T;
        }
        //Checks to see if this node is a key node and returns it if it is
        else {
            if (T.item.equals(key1)) {
                return T;
            }
            if (T.item.equals(key2)) {
                return T;
            }
        }

        //The recursive call. The results are stored in variables
        Node node1 = lengthBetween(T.firstChild, key1, key2);
        Node node2 = lengthBetween(T.nextSibling, key1, key2);

        //Because of the nature of a general tree the only node that matters when checking if this node is the lowest common ancestor is the
        //result from the child node. This is because if the key node comes from a sibling then the current node cannot be the common ancestor
        //and is in fact only a part of the subtree whose root is the lowest common ancestor.
        //Check if node 1 is null, which means nothing was found from the child subtree
        if(node1 != null) {
            //Checks if the two key nodes are contained within the current nodes subtree. Note that each key is checked twice but on
            //different subtrees each time
            if (searchTree(node1.firstChild, key1) != null && searchTree(node1.nextSibling, key2) != null ||
                    searchTree(node1.firstChild, key2) != null && searchTree(node1.nextSibling, key1) != null) {
                //Output result
                System.out.println("The distance between " + key1 + " and " +
                        key2 + " is " + getHeight(T) + ". They have common ancestor " + T.item);
                //Returns current node up to stop another node from satisfying previous if condition
                return T;
            }
        }

        //Check if both key nodes were found and returns current
        if(node1 != null & node2 != null) return T;
        //Otherwise return non-null node
        if(node1 != null) return node1;
        if(node2 != null) return node2;
        //Key node not found
        return null;
    }

    //Checks if key nodes are siblings
    private boolean isSiblings(Node T, String key1, String key2) {
        //Storage of results
        boolean result1 = false;
        boolean result2 = false;

        //Sets pointer
        Node curr = T;
        //Checks each sibling to see if they are a key node. then continues down the list
        while(curr != null) {
            if(curr.item.equals(key1)) {
                result1 = true;
            }
            if(curr.item.equals(key2)) {
                result2 = true;
            }
            curr = curr.nextSibling;
        }

        //If both results are true then both key nodes are siblings
        if(result1 && result2) return true;
        return false;
    }

    //Iteratively traverses tree in a breath first traversal
    private void breadthFirst(Node T) {
        //Queue that allows traversal of nodes in specific order
        LinkedList<Node> queue = new LinkedList<>();

        //Adds root to queue
        queue.add(T);
        //Loops while there are still nodes left
        while(!queue.isEmpty()) {
            Node temp = queue.removeFirst();
            //Visits node
            temp.displayNode();
            System.out.println();
            //Adds subtrees if they exist
            if(temp.firstChild != null)
                queue.addLast(temp.firstChild);
            if(temp.nextSibling != null)
                queue.addFirst(temp.nextSibling);
        }
    }

    //Recursively traverses tree in a pre-order traversal
    private void preOrder(Node T) {
        //If end of branch was reached
        if(T == null) return;
        else {
            //Visit node
            T.displayNode();
            System.out.println();
            //recursive call to subtrees
            preOrder(T.firstChild);
            preOrder(T.nextSibling);
        }
    }

    //Recursively traverses tree in a post-order traversal
    private void postOrder(Node T) {
        //If end of branch was reached
        if(T == null) return;
        else {
            //recursive call on left subtree
            postOrder(T.firstChild);
            //If T has  sibling then visit node and then recursively call teh right subtree
            if(T.nextSibling != null) {
                T.displayNode();
                System.out.println();
                postOrder(T.nextSibling);
            }
            //If not then just visit node
            else {
                T.displayNode();
                System.out.println();
            }

        }
    }

    //Starts the program by making a new VirusTree.
    public static void main ( String[] args ) throws java.io.FileNotFoundException { VirusTree d = new VirusTree(); }
}
