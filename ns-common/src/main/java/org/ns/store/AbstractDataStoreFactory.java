/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ns.store;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.regex.Pattern;
import org.ns.util.Assert;

/**
 *
 * @author stupak
 */
public abstract class AbstractDataStoreFactory implements DataStoreFactory {

  /** Lock on access to the data store map. */
  private final Lock lock = new ReentrantLock();

  /** Map of data store ID to data store. */
  private final Map<String, DataStore<? extends Serializable>> dataStoreMap = new HashMap<>();

  /**
   * Pattern to control possible values for the {@code id} parameter of
   * {@link #getDataStore(String)}.
   */
  private static final Pattern ID_PATTERN = Pattern.compile("\\w{1,30}");

  public final <V extends Serializable> DataStore<V> getDataStore(String id) throws IOException {
    Assert.isTrue(
        ID_PATTERN.matcher(id).matches(), "%s does not match pattern %s", id, ID_PATTERN);
    lock.lock();
    try {
      @SuppressWarnings("unchecked")
      DataStore<V> dataStore = (DataStore<V>) dataStoreMap.get(id);
      if (dataStore == null) {
        dataStore = createDataStore(id);
        dataStoreMap.put(id, dataStore);
      }
      return dataStore;
    } finally {
      lock.unlock();
    }
  }

  /**
   * Returns a new instance of a type-specific data store based on the given unique ID.
   *
   * <p>
   * The {@link DataStore#getId()} must match the {@code id} parameter from this method.
   * </p>
   *
   * @param id unique ID to refer to typed data store
   * @param <V> serializable type of the mapped value
   */
  protected abstract <V extends Serializable> DataStore<V> createDataStore(String id)
      throws IOException;
}
