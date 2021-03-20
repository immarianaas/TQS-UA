package ua.tqs;

import java.util.NoSuchElementException;

public class TqsStack<T> {
    private int size;
    private Node<T> head;

    public TqsStack() {
        size = 0;
        head = null;
    }

    public boolean isEmpty() {
        return size == 0 && head == null;
    }
 
    public int size() {
        return size;
    }

    public void push(T elem) {
        Node<T> nnode = new Node<T>(elem);
        if (!isEmpty()) nnode.next = head;
        head = nnode;
        size++;
    }

    public T peek() {
        if (isEmpty()) throw new NoSuchElementException(); // dunno yet
        return head.elem;
    }

    public T pop() {
        if (isEmpty()) throw new NoSuchElementException(); // dunno yet
        T ret = head.elem;
        head = head.next;
        size--;
        return ret;
    }


    private class Node<V> {
        Node<V> next;
        V elem;
        public Node(V e) {
            elem = e;
        }
    }

}
