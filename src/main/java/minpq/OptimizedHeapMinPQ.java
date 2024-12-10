package minpq;

import java.util.*;

/**
 * Optimized binary heap implementation of the {@link MinPQ} interface.
 *
 * @param <E> the type of elements in this priority queue.
 * @see MinPQ
 */
public class OptimizedHeapMinPQ<E> implements MinPQ<E> {
    /**
     * {@link List} of {@link PriorityNode} objects representing the heap of element-priority pairs.
     */
    private final List<PriorityNode<E>> elements;
    /**
     * {@link Map} of each element to its associated index in the {@code elements} heap.
     */
    private final Map<E, Integer> elementsToIndex;

    /**
     * Constructs an empty instance.
     */
    public OptimizedHeapMinPQ() {
        elements = new ArrayList<>();
        elementsToIndex = new HashMap<>();
        elements.add(null);
    }

    /**
     * Constructs an instance containing all the given elements and their priority values.
     *
     * @param elementsAndPriorities each element and its corresponding priority.
     */
    public OptimizedHeapMinPQ(Map<E, Double> elementsAndPriorities) {
        elements = new ArrayList<>(elementsAndPriorities.size());
        elementsToIndex = new HashMap<>(elementsAndPriorities.size());
        elements.add(null);

        for (Map.Entry<E, Double> entry : elementsAndPriorities.entrySet()) {
            E element = entry.getKey();
            double priority = entry.getValue();

            PriorityNode<E> node = new PriorityNode<>(element, priority);
            elements.add(node);
            elementsToIndex.put(element, elements.size() - 1);
        }


        for (int i = elements.size() / 2; i > 0; i--) {
            downHeap(i);
        }
    }

    @Override
    public void add(E element, double priority) {
        if (contains(element)) {
            throw new IllegalArgumentException("Already contains " + element);
        }
        PriorityNode<E> newNode = new PriorityNode<>(element, priority);
        elements.add(newNode);
        int index = elements.size() - 1;
        elementsToIndex.put(element, index);
        upHeap(index);
    }
    private void upHeap(int index) {
        while (index > 1) {
            int parentIndex = index / 2;

            if (elements.get(index).getPriority() >= elements.get(parentIndex).getPriority()) {
                break;
            }
            swap(index, parentIndex);

            index = parentIndex;
        }
    }


    @Override
    public boolean contains(E element) {
        return elementsToIndex.containsKey(element);
    }

    @Override
    public double getPriority(E element) {
        if (!contains(element)){
            throw new NoSuchElementException("OpHeapminPQ does not contain the element");
        }
        int index = elementsToIndex.get(element);
        return elements.get(index).getPriority();
    }

    @Override
    public E peekMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        return elements.get(1).getElement();
    }

    @Override
    public E removeMin() {
        if (isEmpty()) {
            throw new NoSuchElementException("PQ is empty");
        }
        PriorityNode<E> minNode = elements.get(1);
        E minElement = minNode.getElement();

        swap(1, elements.size() - 1);
        elements.remove(elements.size() - 1);
        elementsToIndex.remove(minElement);

        if (!isEmpty()) {
            downHeap(1);
        }

        return minElement;
    }

    @Override
    public void changePriority(E element, double newPriority) {
        if (!contains(element)) {
            throw new NoSuchElementException("Priority queue does not contain the element: " + element);
        }

        int index = elementsToIndex.get(element);
        PriorityNode<E> node = elements.get(index);
        double oldPriority = node.getPriority();
        node.setPriority(newPriority);


        if (newPriority < oldPriority) {
            upHeap(index);
        } else if (newPriority > oldPriority) {
            downHeap(index);
        }
    }


    @Override
    public int size() {
        return elements.size() -1;
    }

    private void downHeap(int index) {
        while (index * 2 < elements.size()) {
            int childIndex = index * 2;

            if (childIndex + 1 < elements.size() && elements.get(childIndex + 1).getPriority() < elements.get(childIndex).getPriority()) {
                childIndex++;
            }

            if (elements.get(index).getPriority() <= elements.get(childIndex).getPriority()) {
                break;
            }

            swap(index, childIndex);
            index = childIndex;
        }
    }
    private void swap(int i, int j) {
        PriorityNode<E> temp = elements.get(i);
        elements.set(i, elements.get(j));
        elements.set(j, temp);
        elementsToIndex.put(elements.get(i).getElement(), i);
        elementsToIndex.put(elements.get(j).getElement(), j);
    }
}

