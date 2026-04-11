// -----------------------------------------------------
// Assignment 3
// Class: Repository
// Written by: Yousef Yousef (40299095) & Hamza Shaheed (40341727)
// -----------------------------------------------------

package service;

import exceptions.EntityNotFoundException;
import interfaces.Identifiable;

import java.util.ArrayList;
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

    /** Finds an item by ID or throws when the repository does not contain it. */
    public T findById(String id) throws EntityNotFoundException {
        if (id == null) {
            throw new EntityNotFoundException("Entity not found: null");
        }

        for (int index = 0; index < items.size(); index++) {
            T item = items.get(index);
            if (item != null && id.equals(item.getId())) {
                return item;
            }
        }

        throw new EntityNotFoundException("Entity not found: " + id);
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

        for (int outerIndex = 0; outerIndex < sortedItems.size() - 1; outerIndex++) {
            int smallestIndex = outerIndex;

            for (int innerIndex = outerIndex + 1; innerIndex < sortedItems.size(); innerIndex++) {
                T currentItem = sortedItems.get(innerIndex);
                T smallestItem = sortedItems.get(smallestIndex);

                if (currentItem.compareTo(smallestItem) < 0) {
                    smallestIndex = innerIndex;
                }
            }

            if (smallestIndex != outerIndex) {
                T temp = sortedItems.get(outerIndex);
                sortedItems.set(outerIndex, sortedItems.get(smallestIndex));
                sortedItems.set(smallestIndex, temp);
            }
        }

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
