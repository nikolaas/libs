package org.ns.store;

import java.io.IOException;
import java.io.Serializable;
import org.ns.util.Assert;

/**
 *
 * @author stupak
 * @param <V>
 */
public abstract class AbstractDataStore<V extends Serializable> implements DataStore<V> {

  /** Data store factory. */
  private final DataStoreFactory dataStoreFactory;

  /** Data store ID. */
  private final String id;

  /**
   * @param dataStoreFactory data store factory
   * @param id data store ID
   */
  protected AbstractDataStore(DataStoreFactory dataStoreFactory, String id) {
    this.dataStoreFactory = Assert.isNotNull(dataStoreFactory, "dataStoreFactory is null");
    this.id = Assert.isNotNull(id, "id is null");
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * Overriding is only supported for the purpose of calling the super implementation and changing
   * the return type, but nothing else.
   * </p>
     * @return 
   */
  @Override
  public DataStoreFactory getDataStoreFactory() {
    return dataStoreFactory;
  }

  @Override
  public final String getId() {
    return id;
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * Default implementation is to call {@link #get(String)} and check if it is {@code null}.
   * </p>
     * @param key
     * @return 
     * @throws java.io.IOException
   */
  @Override
  public boolean containsKey(String key) throws IOException {
    return get(key) != null;
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * Default implementation is to call {@link Collection#contains(Object)} on {@link #values()}.
   * </p>
     * @param value
     * @return 
     * @throws java.io.IOException
   */
  @Override
  public boolean containsValue(V value) throws IOException {
    return values().contains(value);
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * Default implementation is to check if {@link #size()} is {@code 0}.
   * </p>
     * @return 
     * @throws java.io.IOException
   */
  @Override
  public boolean isEmpty() throws IOException {
    return size() == 0;
  }

  /**
   * {@inheritDoc}
   *
   * <p>
   * Default implementation is to call {@link Set#size()} on {@link #keySet()}.
   * </p>
     * @return 
     * @throws java.io.IOException
   */
  @Override
  public int size() throws IOException {
    return keySet().size();
  }
}
