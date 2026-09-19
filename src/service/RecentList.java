// -----------------------------------------------------
// SmartTravel Manager
// Class: RecentList
// Written by: Yousef Yousef & Hamza Shaheed
// -----------------------------------------------------

package service;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/** Bounded recent-item list used by the analytics workflow. */
public class RecentList<T> {

    private static final int MAX_SIZE = 10;

    private final LinkedList<T> recentItems;

    public RecentList() {
        this.recentItems = new LinkedList<T>();
    }

    /** Inserts the newest item at the front and trims the oldest item from the end. */
    public void addRecent(T item) {
        if (item == null) {
            return;
        }

        recentItems.addFirst(item);
        if (recentItems.size() > MAX_SIZE) {
            recentItems.removeLast();
        }
    }

    /** Prints up to the requested number of recent items from newest to oldest. */
    public void printRecent(int maxToShow) {
        if (recentItems.isEmpty()) {
            System.out.println("No recent items found.");
            return;
        }

        int limit = maxToShow;
        if (limit < 0) {
            limit = 0;
        }
        if (limit > recentItems.size()) {
            limit = recentItems.size();
        }

        if (limit == 0) {
            System.out.println("No items requested.");
            return;
        }

        for (int index = 0; index < limit; index++) {
            System.out.println(recentItems.get(index));
        }
    }

    public int size() {
        return recentItems.size();
    }

    public boolean isEmpty() {
        return recentItems.isEmpty();
    }

    /** Returns a snapshot of the current recent-item order. */
    public List<T> getItems() {
        return new ArrayList<T>(recentItems);
    }

    /** Removes all recent items. */
    public void clear() {
        recentItems.clear();
    }
}
