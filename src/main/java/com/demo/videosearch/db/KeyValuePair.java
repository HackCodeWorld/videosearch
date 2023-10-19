package com.demo.videosearch.db;

/**
 * this class represents each KeyValuePair<KeyType,ValueType> instance og key and value
 * @param <KeyType> the key of data
 * @param <ValueType> the value of data
 *
 **/
public class KeyValuePair<KeyType,ValueType> {
    private KeyType key; // the key of the specific instance of KeyValuePair
    private ValueType value;  // the value of the specific instance of KeyValuePair

    /**
     * constructor to make the instacne
     * @param key the key of the specific instance of KeyValuePair
     * @param value the value of the specific instance of KeyValuePair
     */
    public KeyValuePair(KeyType key, ValueType value) {
        this.key = key;
        this.value = value;
    }

    /**
     * getter for the key
     * @return key the key of the specific instance of KeyValuePair being get
     */
    public KeyType getKey() {
        return key;
    }

    /**
     * setter for the key
     * @return key the key of the specific instance of KeyValuePair being set
     */
    public void setKey(KeyType key) {
        this.key = key;
    }

    /**
     * getter for the value
     * @return value the value of the specific instance of KeyValuePair being get
     */
    public ValueType getValue() {
        return value;
    }

    /**
     * setter for the value
     * @return value the value of the specific instance of KeyValuePair being set
     */
    public void setValue(ValueType value) {
        this.value = value;
    }
}
