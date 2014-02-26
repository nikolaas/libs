/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ns.store;

import java.io.IOException;
import java.io.Serializable;

/**
 *
 * @author stupak
 */
public interface DataStoreFactory {

  /**
   * Returns a type-specific data store based on the given unique ID.
   *
   * <p>
   * If a data store by that ID does not already exist, it should be created now, stored for later
   * access, and returned. Otherwise, if there is already a data store by that ID, it should be
   * returned. The {@link DataStore#getId()} must match the {@code id} parameter from this method.
   * </p>
   *
   * <p>
   * The ID must be at least 1 and at most 30 characters long, and must contain only alphanumeric or
   * underscore characters.
   * </p>
   *
   * @param id unique ID to refer to typed data store
   * @param <V> serializable type of the mapped value
   * @return 
   * @throws java.io.IOException
   */
  <V extends Serializable> DataStore<V> getDataStore(String id) throws IOException;
}
