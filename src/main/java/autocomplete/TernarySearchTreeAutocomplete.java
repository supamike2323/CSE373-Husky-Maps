package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Ternary search tree (TST) implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class TernarySearchTreeAutocomplete implements Autocomplete {
    /**
     * The overall root of the tree: the first character of the first autocompletion term added to this tree.
     */
    private Node overallRoot;

    /**
     * Constructs an empty instance.
     */
    public TernarySearchTreeAutocomplete() {
        overallRoot = null;
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        for (CharSequence term : terms) {
            overallRoot = add(overallRoot, term, 0);
        }
    }

    private Node add(Node root, CharSequence term, int index) {
        char currentChar = term.charAt(index);

        if (root == null) {
            root = new Node(currentChar);
        }

        if (currentChar < root.data) {
            root.left = add(root.left, term, index);
        } else if (currentChar > root.data) {
            root.right = add(root.right, term, index);
        } else if (index < term.length() - 1) {
            root.mid = add(root.mid, term, index + 1);
        } else {
            root.isTerm = true;
        }

        return root;
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> results = new ArrayList<>();
        Node prefixNode = getNode(overallRoot, prefix, 0);

        if (prefixNode != null) {
            if (prefixNode.isTerm) {
                results.add(prefix);
            }
            collect(prefixNode.mid, new StringBuilder(prefix), results);
        }

        return results;
    }

    private Node getNode(Node root, CharSequence prefix, int index) {
        if (root == null) {
            return null;
        }

        char currentChar = prefix.charAt(index);

        if (currentChar < root.data) {
            return getNode(root.left, prefix, index);
        } else if (currentChar > root.data) {
            return getNode(root.right, prefix, index);
        } else if (index < prefix.length() - 1) {
            return getNode(root.mid, prefix, index + 1);
        } else {
            return root;
        }
    }

    private void collect(Node root, StringBuilder prefix, List<CharSequence> results) {
        if (root == null) {
            return;
        }

        collect(root.left, prefix, results);

        prefix.append(root.data);
        if (root.isTerm) {
            results.add(prefix.toString());
        }

        collect(root.mid, prefix, results);
        prefix.deleteCharAt(prefix.length() - 1);

        collect(root.right, prefix, results);
    }

    /**
     * A search tree node representing a single character in an autocompletion term.
     */
    private static class Node {
        private final char data;
        private boolean isTerm;
        private Node left;
        private Node mid;
        private Node right;

        public Node(char data) {
            this.data = data;
            this.isTerm = false;
            this.left = null;
            this.mid = null;
            this.right = null;
        }
    }
}
