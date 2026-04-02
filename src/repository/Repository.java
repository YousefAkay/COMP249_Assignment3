package repository;

import contracts.Identifiable;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Predicate;

/** Generic repository backed by an ArrayList for A3 collection-based storage. */
public class Repository<T extends Identifiable & Comparable<? super T>> {

    private final List<T> items;

    public Repository() {
        this.items = new ArrayList<T>();
    }

    /** Adds a non-null item to the repository. */
    public void add(T item) {
        if (item != null) {
            items.add(item);
        }
    }

    /** Finds an item by ID or returns null when not found. */
    public T findById(String id) {
        if (id == null) {
            return null;
        }

        for (int index = 0; index < items.size(); index++) {
            T item = items.get(index);
            if (item != null && id.equals(item.getId())) {
                return item;
            }
        }

        return null;
    }

    /** Returns all items matching the provided predicate in encounter order. */
    public List<T> filter(Predicate<T> predicate) {
        List<T> filteredItems = new ArrayList<T>();
        if (predicate == null) {
            return filteredItems;
        }

        for (int index = 0; index < items.size(); index++) {
            T item = items.get(index);
            if (item != null && predicate.test(item)) {
                filteredItems.add(item);
            }
        }

        return filteredItems;
    }

    /** Returns a new list sorted using the model's natural ordering. */
    public List<T> getSorted() {
        List<T> sortedItems = new ArrayList<T>(items);
        Collections.sort(sortedItems);
        return sortedItems;
    }

    /** Returns a snapshot copy of all stored items. */
    public List<T> getAll() {
        return new ArrayList<T>(items);
    }

    /** Removes the first item matching the given ID and returns whether removal happened. */
    public boolean removeById(String id) {
        if (id == null) {
            return false;
        }

        for (int index = 0; index < items.size(); index++) {
            T item = items.get(index);
            if (item != null && id.equals(item.getId())) {
                items.remove(index);
                return true;
            }
        }

        return false;
    }

    /** Removes all stored items. */
    public void clear() {
        items.clear();
    }
}
