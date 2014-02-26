/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ns.store;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import org.ns.util.Assert;

/**
 *
 * @author stupak
 * @param <V>
 */
public class AbstractMemoryDataStore<V extends Serializable> extends AbstractDataStore<V> {

  /** Lock on access to the store. */
  private final Lock lock = new ReentrantLock();

  /** Data store map from the key to the value. */
  HashMap<String, byte[]> keyValueMap = new HashMap<>();

  /**
   * @param dataStoreFactory data store factory
   * @param id data store ID
   */
  protected AbstractMemoryDataStore(DataStoreFactory dataStoreFactory, String id) {
    super(dataStoreFactory, id);
  }

  @Override
  public final Set<String> keySet() throws IOException {
    lock.lock();
    try {
      return Collections.unmodifiableSet(keyValueMap.keySet());
    } finally {
      lock.unlock();
    }
  }

  @Override
  public final Collection<V> values() throws IOException {
    lock.lock();
    try {
      List<V> result = new ArrayList<>();
      for (byte[] bytes : keyValueMap.values()) {
        result.add(IOUtils.<V>deserialize(bytes));
      }
      return Collections.unmodifiableList(result);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public final V get(String key) throws IOException {
    if (key == null) {
      return null;
    }
    lock.lock();
    try {
      return IOUtils.deserialize(keyValueMap.get(key));
    } finally {
      lock.unlock();
    }
  }

  @Override
  public final DataStore<V> set(String key, V value) throws IOException {
    Assert.isNotNull(key, "key is null");
    Assert.isNotNull(value, "value is null");
    lock.lock();
    try {
      keyValueMap.put(key, IOUtils.serialize(value));
      save();
    } finally {
      lock.unlock();
    }
    return this;
  }

  @Override
  public DataStore<V> delete(String key) throws IOException {
    if (key == null) {
      return this;
    }
    lock.lock();
    try {
      keyValueMap.remove(key);
      save();
    } finally {
      lock.unlock();
    }
    return this;
  }

  @Override
  public final DataStore<V> clear() throws IOException {
    lock.lock();
    try {
      keyValueMap.clear();
      save();
    } finally {
      lock.unlock();
    }
    return this;
  }

  @Override
  public boolean containsKey(String key) throws IOException {
    if (key == null) {
      return false;
    }
    lock.lock();
    try {
      return keyValueMap.containsKey(key);
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean containsValue(V value) throws IOException {
    if (value == null) {
      return false;
    }
    lock.lock();
    try {
      byte[] serialized = IOUtils.serialize(value);
      for (byte[] bytes : keyValueMap.values()) {
        if (Arrays.equals(serialized, bytes)) {
          return true;
        }
      }
      return false;
    } finally {
      lock.unlock();
    }
  }

  @Override
  public boolean isEmpty() throws IOException {
    lock.lock();
    try {
      return keyValueMap.isEmpty();
    } finally {
      lock.unlock();
    }
  }

  @Override
  public int size() throws IOException {
    lock.lock();
    try {
      return keyValueMap.size();
    } finally {
      lock.unlock();
    }
  }

  /**
   * Persist the key-value map into storage at the end of {@link #set}, {@link #delete(String)}, and
   * {@link #clear()}.
   */
  @SuppressWarnings("unused")
  void save() throws IOException {
  }

  @Override
  public String toString() {
    return DataStoreUtils.toString(this);
  }
}
