package main.java.com.lifeline.datastructures;

import java.util.LinkedList;

public class LinkedListQueue<T> {
    private LinkedList<T> list;

    public LinkedListQueue() {
        this.list = new LinkedList<>();
    }

    public void enqueue(T item) {
        list.addLast(item);
    }

    public T dequeue() {
        return list.isEmpty() ? null : list.removeFirst();
    }

    public T peek() {
        return list.isEmpty() ? null : list.getFirst();
    }

    public boolean isEmpty() {
        return list.isEmpty();
    }

    public int size() {
        return list.size();
    }
}