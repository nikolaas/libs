/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ns.store;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collection;
import java.util.Set;

/**
 *
 * @author stupak
 * @param <V>
 */
public interface DataStore<V extends Serializable> {

  /** Returns the data store factory. */
  DataStoreFactory getDataStoreFactory();

  /** Returns the data store ID. */
  String getId();

  /** Returns the number of stored keys. */
  int size() throws IOException;

  /** Returns whether there are any stored keys. */
  boolean isEmpty() throws IOException;

  /** Returns whether the store contains the given key. */
  boolean containsKey(String key) throws IOException;

  /** Returns whether the store contains the given value. */
  boolean containsValue(V value) throws IOException;

  /**
   * Returns the unmodifiable set of all stored keys.
   *
   * <p>
   * Order of the keys is not specified.
   * </p>
   */
  Set<String> keySet() throws IOException;

  /** Returns the unmodifiable collection of all stored values. */
  Collection<V> values() throws IOException;

  /**
   * Returns the stored value for the given key or {@code null} if not found.
   *
   * @param key key or {@code null} for {@code null} result
   */
  V get(String key) throws IOException;

  /**
   * Stores the given value for the given key (replacing any existing value).
   *
   * @param key key
   * @param value value object
   */
  DataStore<V> set(String key, V value) throws IOException;

  /** Deletes all of the stored keys and values. */
  DataStore<V> clear() throws IOException;

  /**
   * Deletes the stored key and value based on the given key, or ignored if the key doesn't already
   * exist.
   *
   * @param key key or {@code null} to ignore
   */
  DataStore<V> delete(String key) throws IOException;

  // TODO(yanivi): implement entrySet()?
}