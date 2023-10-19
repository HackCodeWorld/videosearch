package com.demo.videosearch.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class HashTableSortedSets<KeyType, ValueType extends Comparable<ValueType>>
		extends HashtableMap<KeyType, List<ValueType>> implements IHashTableSortedSets<KeyType, ValueType> {

	public void add(KeyType key, ValueType value) {
		// check if key is null, if key is null, don't add it to the value list.
		if (key == null) {
			return;
		}
		// check if key is null, if value is null, don't add it to the value list.
		if (value == null) {
			return;
		}
		// check if key is in the Hashtable
		if (!containsKey(key)) {
			// if not, create a new value_list and add it to the corresponding key
			List<ValueType> value_list = new ArrayList<ValueType>();
			value_list.add(value);
			// put the key and value_list back in Hashtable
			put(key, value_list);
		} else {
			// if yes, get the value_list from the key
			List<ValueType> value_list = get(key);
			// check if the value is a duplicate
			if (find_duplicate(value_list, value)) {
				return;
			}
			// add value into the value_list
			value_list.add(value);
			// sort the value_list
			Collections.sort(value_list);
		}
	}

	/**
	 * private helper method to see if the value corresponding to the key that we
	 * try to add is already in the List
	 * 
	 * @param value_list where the key is store
	 * @param value
	 * @return true if has duplicate value, false if not found duplicates
	 */
	private boolean find_duplicate(List<ValueType> value_list, ValueType value) {
		for (int j = 0; j < value_list.size(); j++) {
			if (value_list.get(j).equals(value))
				return true;
		}
		return false;
	}

}

