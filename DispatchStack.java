package main.java.com.lifeline.datastructures;

import java.util.Stack;

public class DispatchStack<T> {
    private Stack<T> stack;

    public DispatchStack() {
        this.stack = new Stack<>();
    }

    public void push(T item) {
        stack.push(item);
    }

    public T pop() {
        return stack.isEmpty() ? null : stack.pop();
    }

    public T peek() {
        return stack.isEmpty() ? null : stack.peek();
    }

    public boolean isEmpty() {
        return stack.isEmpty();
    }

    public int size() {
        return stack.size();
    }
}