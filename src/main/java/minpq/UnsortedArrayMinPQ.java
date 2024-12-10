package minpq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

/**
 * Unsorted array (or {@link ArrayList}) implementation of the {@link MinPQ} interface.
 *
 * @param <E> the type of elements in this priority queue.
 * @see MinPQ
 */
public class UnsortedArrayMinPQ<E> implements MinPQ<E> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the element-priority pairs in no specific order.
     */
    private final List<PriorityNode<E>> elements;

    /**
     * Constructs an empty instance.
     */
    public UnsortedArrayMinPQ() {
        elements = new ArrayList<>();
    }

    /**
     * Constructs an instance containing all the given elements and their priority values.
     *
     * @param elementsAndPriorities each element and its corresponding priority.
     */
    public UnsortedArrayMinPQ(Map<E, Double> elementsAndPriorities) {
        elements = new ArrayList<>(elementsAndPriorities.size());
        for (Map.Entry<E, Double> entry : elementsAndPriorities.entrySet()) {
            add(entry.getKey(), entry.getValue());
        }
    }

    @Override
    public void add(E element, double priority) {
        if (contains(element)) {
            throw new IllegalArgumentException("Already contains " + element);
        }
        elements.add(new PriorityNode<>(element, priority));
    }

    @Override
    public boolean contains(E element) {
        for (PriorityNode<E> node : elements) {
            if (node.getElement().equals(element)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public double getPriority(E element) {
        for (PriorityNode<E> node : elements) {
            if (node.getElement().equals(element)) {
                return node.getPriority();
            }
        }
        throw new NoSuchElementException("Priority queue does not contain the element: " + element);
    }

    @Override
    public E peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        PriorityNode<E> minNode = elements.get(0);
        for (PriorityNode<E> node : elements) {
            if (node.getPriority() < minNode.getPriority()) {
                minNode = node;
            }
        }
        return minNode.getElement();
    }

    @Override
    public E removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        int minIndex = 0;
        for (int i = 1; i < elements.size(); i++) {
            if (elements.get(i).getPriority() < elements.get(minIndex).getPriority()) {
                minIndex = i;
            }
        }

        return elements.remove(minIndex).getElement();
    }

    @Override
    public void changePriority(E element, double priority) {
        for (PriorityNode<E> node : elements) {
            if (node.getElement().equals(element)) {
                node.setPriority(priority);
                return;
            }
        }
        throw new NoSuchElementException("Priority queue does not contain the element: " + element);
    }

    @Override
    public int size() {
        return elements.size();
    }
}
