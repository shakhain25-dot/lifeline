package main.java.com.lifeline.datastructures;

import java.util.*;

public class EmergencyPriorityQueue<T> {
    private List<PriorityItem<T>> items;

    public EmergencyPriorityQueue() {
        this.items = new ArrayList<>();
    }

    public static class PriorityItem<T> {
        T item;
        int priority;

        public PriorityItem(T item, int priority) {
            this.item = item;
            this.priority = priority;
        }
    }

    public void enqueue(T item, int priority) {
        items.add(new PriorityItem<>(item, priority));
        items.sort((a, b) -> Integer.compare(b.priority, a.priority));
    }

    public T dequeue() {
        if (items.isEmpty()) {
            return null;
        }
        return items.remove(0).item;
    }

    public T peek() {
        if (items.isEmpty()) {
            return null;
        }
        return items.get(0).item;
    }

    public boolean isEmpty() {
        return items.isEmpty();
    }

    public int size() {
        return items.size();
    }
}