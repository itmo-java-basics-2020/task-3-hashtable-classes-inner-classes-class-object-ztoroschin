package ru.itmo.java;

public class HashTable {
    private final static double DEFAULT_LOAD_FACTOR = 0.5;
    private final static int DEFAULT_INITIAL_SIZE = 13;
    private final static double RESIZE_MULTIPLY = 2;

    private double loadFactor;
    private int size = 0;
    private int threshold;
    private Entry[] hashTable;

    public HashTable(int initialCapacity, double loadFactor) {
        this.loadFactor = loadFactor;
        this.hashTable = new Entry[nextPrime(initialCapacity)];
        this.threshold = (int)(hashTable.length * loadFactor);
    }

    public HashTable(int initialCapacity) {
        this(initialCapacity, DEFAULT_LOAD_FACTOR);
    }

    public HashTable() {
        this(DEFAULT_INITIAL_SIZE, DEFAULT_LOAD_FACTOR);
    }

    Object put(Object key, Object value) {
        int hash = hash(key, hashTable.length);
        while(hashTable[hash] != null) {
            if(key.equals(hashTable[hash].key)) {
                return hashTable[hash].setValue(value);
            }

            if(++hash == hashTable.length) {
                hash = 0;
            }
        }

        hashTable[hash] = new Entry(key, value);
        size++;

        if(size > threshold) {
            resize();
        }

        return null;
    }

    Object get(Object key) {
        int hash = hash(key, hashTable.length);
        while(hashTable[hash] != null) {
            if(key.equals(hashTable[hash].key)) {
                return hashTable[hash].value;
            }

            if(++hash == hashTable.length) {
                hash = 0;
            }
        }

        return null;
    }

    Object remove(Object key) {
        int hash = hash(key, hashTable.length);
        Object value = null;

        while(hashTable[hash] != null) {
            if(key.equals(hashTable[hash].key)) {
                value = hashTable[hash].value;
                hashTable[hash] = null;
                size--;
            } else {
                Object tempKey = hashTable[hash].key;
                Object tempValue = hashTable[hash].value;
                hashTable[hash] = null;
                size--;
                put(tempKey, tempValue);
            }

            if(++hash == hashTable.length) {
                hash = 0;
            }
        }

        return value;
    }

    int getSize() {
        return size;
    }

    private static int hash(Object key, Integer capacity) {
        return (key.hashCode() % capacity + capacity) % capacity;
    }

    private void resize() {
        Entry[] oldHashTable = hashTable;
        int newSize = nextPrime((int)(oldHashTable.length * RESIZE_MULTIPLY));
        hashTable = new Entry[newSize];
        this.size = 0;
        this.threshold = (int)(hashTable.length * loadFactor);

        for(var entry : oldHashTable) {
            if(entry != null) {
                put(entry.key, entry.value);
            }
        }
    }

    private static int nextPrime(int num) {
        while(!isPrime(num)) {
            num++;
        }
        return num;
    }

    private static boolean isPrime(int num) {
        for(int i = 2; i*i <= num; i++)
            if(num % i == 0) {
                return false;
            }
        return true;
    }

    private static class Entry {
        Object key;
        Object value;

        private Entry(Object key, Object value) {
            this.key = key;
            this.value = value;
        }

        private Object setValue(Object value) {
            Object oldValue = this.value;
            this.value = value;
            return oldValue;
        }
    }

}
