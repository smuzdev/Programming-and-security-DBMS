package com.smuzdev.lab_05;

import android.util.Log;

import java.util.ArrayList;

public class CustomHashtable<K, V> {
    private static class LinkedListNode<K, V> {
        public LinkedListNode<K, V> next;
        public LinkedListNode<K, V> prev;
        public K key;
        public V value;

        public LinkedListNode(K k, V v) {
            key = k;
            value = v;
        }

        public String printForward() {
            String data = "(" + key + "," + value + ")";
            if (next != null) {
                return data + "->" + next.printForward();
            } else {
                return data;
            }
        }
    }

    private ArrayList<LinkedListNode<K, V>> array;
    public CustomHashtable(int capacity) {
        array = new ArrayList<LinkedListNode<K, V>>();
        array.ensureCapacity(capacity);
        for (int i = 0; i < capacity; i++) {
            array.add(null);
        }
    }

    //Put key -> value to hashtable
    public V put (K key, V value){
        LinkedListNode<K, V> node = getNodeForKey(key);
        if (node != null) {
            V oldValue = node.value;
            node.value = value;
            return oldValue;
        }

        node = new LinkedListNode<K, V>(key, value);
        int index = getIndexForKey(key);
        if (array.get(index) != null) {
            node.next = array.get(index);
            node.next.prev = node;
        }
        array.set(index, node);
        return null;
    }

    //Get value by key
    public V get(K key) {
        if (key == null) return null;
        LinkedListNode<K, V> node = getNodeForKey(key);
        return node == null ? null : node.value;
    }

    private LinkedListNode<K, V> getNodeForKey(K key) {
        int index = getIndexForKey(key);
        LinkedListNode<K, V> current = array.get(index);
        while (current != null) {
            if (current.key.equals(key)) {
                return current;
            }
            current = current.next;
        }
        return null;
    }

    public int getIndexForKey(K key) {
        return key.hashCode() % array.size();
    }

    public void printTable() {
        for (int i = 0; i < array.size(); i++) {
            String s = array.get(i) == null ? "" : array.get(i).printForward();
            Log.d("Events",i + ": " + s);
        }
    }
}
