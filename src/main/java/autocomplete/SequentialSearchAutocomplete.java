package autocomplete;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Sequential search implementation of the {@link Autocomplete} interface.
 *
 * @see Autocomplete
 */
public class SequentialSearchAutocomplete implements Autocomplete {
    /**
     * {@link List} of added autocompletion terms.
     */
    private final List<CharSequence> elements;

    /**
     * Constructs an empty instance.
     */
    public SequentialSearchAutocomplete() {
        elements = new ArrayList<>();
    }

    @Override
    public void addAll(Collection<? extends CharSequence> terms) {
        elements.addAll(terms);
    }

    @Override
    public List<CharSequence> allMatches(CharSequence prefix) {
        List<CharSequence> results = new ArrayList<>();

        if (prefix == null || prefix.isEmpty()) {
            return results;
        }


        for (CharSequence term : elements) {
            if (startsWith(term, prefix)) {
                results.add(term);
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
