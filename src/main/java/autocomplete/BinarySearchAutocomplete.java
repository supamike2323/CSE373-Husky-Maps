package autocomplete;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Binary search implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class BinarySearchAutocomplete implements Autocomplete {
    /**
     * {@link List} of added autocompletion terms.
     */
    private final List<CharSequence> elements;

    /**
     * Constructs an empty instance.
     */
    public BinarySearchAutocomplete() {
        elements = new ArrayList<>();
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        elements.addAll(terms);
        elements.sort(CharSequence::compare);
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> results = new ArrayList<>();
        if (prefix == null || prefix.isEmpty()) {
            return results;
        }
        int startIndex = Collections.binarySearch(elements, prefix, CharSequence::compare);
        if (startIndex < 0) {
            startIndex = -(startIndex + 1);
        }

        for (
                int i = startIndex; i < elements.size(); i++) {
            CharSequence term = elements.get(i);
            if (startsWith(term, prefix)) {
                results.add(term);
            } else {
                break;
            }
        }

        return results;
    }

    private boolean startsWith(CharSequence term, CharSequence prefix) {
        if (term.length() < prefix.length()) {
            return false;
        }
        for (int i = 0; i < prefix.length(); i++) {
            if (term.charAt(i) != prefix.charAt(i)) {
                return false;
            }
        }
        return true;
    }
}