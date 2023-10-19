package com.demo.videosearch.db;

import java.util.LinkedList;
import java.util.NoSuchElementException;

/**
 * HashtableMap data structure to implement the logic of the table list storing each KeyValuePair within a
 * linkedList in a unique index of (hashcode % capacity) of the list
 * @param <KeyType>   The key generic type
 * @param <ValueType> The value generic type
 **/
public class HashtableMap<KeyType, ValueType> implements MapADT<KeyType, ValueType> {

    protected LinkedList<KeyValuePair<KeyType, ValueType>>[] keyValuePairs; //list to store keyValuePairs
    private int size; //current size of list

    /**
     * constructor if capacity of hashtable is given
     * initialization of all the required instances
     * and list with required capacity argument
     *
     * @param capacity
     */
    @SuppressWarnings("unchecked")
    public HashtableMap(int capacity) {
        this.keyValuePairs = new LinkedList[capacity];
        this.size = 0;

        for (int i = 0; i < this.keyValuePairs.length; i++) {
            this.keyValuePairs[i] = new LinkedList<>();
        }
    }

    /**
     * constructor if no capacity of hashtable is given
     * initialization of all the required instances
     * and list with default capacity 20
     */
    @SuppressWarnings("unchecked")
    public HashtableMap() { // with default capacity = 20
        this.keyValuePairs = new LinkedList[20]; //cast raw type to generic type
        this.size = 0;

        for (int i = 0; i < this.keyValuePairs.length; i++) {
            this.keyValuePairs[i] = new LinkedList<>();
        }
    }

    /**
     * private helper to get the capacity of the list
     *
     * @return capacity an int of the capacity in this hash table
     */
    private int getCapacity() {
        return this.keyValuePairs.length;
    }

    /**
     * dynamically grow the collection
     * rehashHelper() helps put() to resize the size of list when
     * the size encounters the threshold of 75% capacity rate
     * assign all the old pairs data to the new list
     */
    @SuppressWarnings("unchecked")
    private void rehashHelper() {
        LinkedList<KeyValuePair<KeyType, ValueType>>[] keyValuePairsPrevious = this.keyValuePairs;//store the old pairs
        this.keyValuePairs = new LinkedList[(this.keyValuePairs.length * 2)];//create new empty pairs' list with
        // doubled rehashing size
        this.size = 0;

        //create empty linkedList for each index in the new list
        for (int i = 0; i < this.keyValuePairs.length; i++) {
            this.keyValuePairs[i] = new LinkedList<>();
        }

        //assign old to new without changing data but probably with different index in list
        for (int i = 0; i < keyValuePairsPrevious.length; i++) {
            LinkedList oldLinkedList = keyValuePairsPrevious[i];
            for (int idx = 0; idx < oldLinkedList.size(); idx++) {
                KeyValuePair<KeyType, ValueType> keyValuePair = (KeyValuePair<KeyType, ValueType>) oldLinkedList.get(idx);
                put(keyValuePair.getKey(), keyValuePair.getValue());
            }
        }
    }


    /**
     * store new values in your hash table at the index corresponding to { the absolute value of the key's hashCode() }
     * modulus the HashtableMap's current capacity. When the put method is passed a key that is null or is equal to
     * a key that is already stored in your hash table, that call should return false without making any changes to
     * the hash table. The put method should only return true after successfully storing a new key-value pair.
     *
     * @param key   the KeyType key of the pair of data
     * @param value the ValueType value of the pair of data
     * @return boolean true or false to indicate the success of putting a pair
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean put(KeyType key, ValueType value) {
        // eliminate it and quit if key is null
        if (key == null) {
            return false;
        }

        // use hash function and get the unique position to place keyValue pair
        int hashcode = key.hashCode();
        hashcode = hashcode >= 0 ? hashcode : -hashcode; //get the absolute value of hashcode
        int idx = hashcode % this.getCapacity();
        LinkedList linkedList = this.keyValuePairs[idx];

        // if the list has the keyValue pair/ a key already had in the list
        // eliminate it and quit with a return false
        for (int i = 0; i < linkedList.size(); i++) {
            KeyValuePair<KeyType, ValueType> keyValuePair = (KeyValuePair<KeyType, ValueType>) linkedList.get(i);
            if (key.equals(keyValuePair.getKey())) {
                return false;
            }
        }

        // other cases then just put normally
        linkedList.add(new KeyValuePair<KeyType, ValueType>(key, value));
        this.size++;

        // call rehash helper method if the capacity rate >= 75%
        if (this.size >= 0.75 * this.keyValuePairs.length) {
            rehashHelper();
        }
        return true;
    }

    /**
     * looking for the key and return the value based on the searching
     *
     * @param key the KeyType key of the pair of data
     * @return value the ValueType value of the pair of data based on the key
     * @throws NoSuchElementException indicates there is no key looking for
     **/
    @Override
    @SuppressWarnings("unchecked")
    public ValueType get(KeyType key) throws NoSuchElementException {

        if (key == null) { // it shouldn't be a null for key cuz the base of put()
            throw new NoSuchElementException("this key is not in the collection!");
        }

        int hashCode = key.hashCode();
        hashCode = hashCode >= 0 ? hashCode : -hashCode; //get the absolute value of hashcode

        // use hash function and get the unique position to get keyValue pair
        int capacity = this.keyValuePairs.length;
        int idx = hashCode % capacity;

        LinkedList linkedList = this.keyValuePairs[idx];
        for (int i = 0; i < linkedList.size(); i++) { // tracing through the collection of the specific data pair
            KeyValuePair<KeyType, ValueType> keyValuePair = (KeyValuePair<KeyType, ValueType>) linkedList.get(i);
            if (keyValuePair.getKey().equals(key)) // find out the one with corresponding key and return the value
                return keyValuePair.getValue();
        }

        throw new NoSuchElementException("this key is not in the collection!");
    }

    /**
     * a size method that returns the number of key-value pairs stored in this collection
     *
     * @return size the number of key-value pairs stored in this collection
     */
    @Override
    public int size() {
        return this.size;
    }

    /**
     * check whether the key was in the collection before doing any other behaviors and changes
     *
     * @param key the KeyType key of the pair of data
     * @return boolean decides whether the key is included
     */
    @Override
    @SuppressWarnings("unchecked")
    public boolean containsKey(KeyType key) {
        if (key == null) {
            return false;
        }
        int hashCode = key.hashCode();
        hashCode = hashCode >= 0 ? hashCode : -hashCode; //get the absolute value of hashcode

        // use hash function and get the unique position to get keyValue pair
        int capacity = this.keyValuePairs.length;
        int index = hashCode % capacity;

        LinkedList linkedList = this.keyValuePairs[index];
        for (int i = 0; i < linkedList.size(); i++) { // tracing through the collection of the specific data pair
            // find out the one with corresponding key and return boolean deciding whether the key is included
            KeyValuePair<KeyType, ValueType> keyValuePair = (KeyValuePair<KeyType, ValueType>) linkedList.get(i);
            if (keyValuePair.getKey().equals(key))
                return true;
        }

        return false;
    }

    /**
     * a remove method that returns a reference to the value associated with the key that is being removed.
     * When the key being removed cannot be found in the HashtableMap, this method should instead return null.
     *
     * @param key KeyType key that points to the key that is going to be removed
     * @return value returns a reference to the ValueType value associated with the key that is being removed
     */
    @Override
    @SuppressWarnings("unchecked")
    public ValueType remove(KeyType key) {
        if (key == null) {
            return null;
        }

        int hashCode = key.hashCode();
        hashCode = hashCode >= 0 ? hashCode : -hashCode;  //get the absolute value of hashcode

        // use hash function and get the unique position to get keyValue pair
        int capacity = this.keyValuePairs.length;
        int index = hashCode % capacity;

        LinkedList linkedList = this.keyValuePairs[index];
        for (int i = 0; i < linkedList.size(); i++) { // tracing through the collection of the specific data pair
            KeyValuePair<KeyType, ValueType> keyValuePair = (KeyValuePair<KeyType, ValueType>) linkedList.get(i);
            if (keyValuePair.getKey().equals(key)) { // find out the one with corresponding key and remove it then return
                // the value corresponding to that key being removed
                linkedList.remove(i);
                return keyValuePair.getValue();
            }
        }

        return null;
    }

    /**
     * a clear method that removes all key-value pairs from this collection
     * (without changing the underlying array capacity).
     */
    @Override
    public void clear() {
        int capacity = this.keyValuePairs.length;
        this.keyValuePairs = new LinkedList[capacity];

        for (int i = 0; i < capacity; i++)
            this.keyValuePairs[i] = new LinkedList<>();

        this.size = 0; //changing the current size instead of the capacity
    }
}