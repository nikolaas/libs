/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ns.store;

import java.io.IOException;

/**
 *
 * @author stupak
 */
public final class DataStoreUtils {
  /**
   * Returns a debug string for the given data store to be used as an implementation of
   * {@link Object#toString()}.
   *
   * <p>
   * Implementation iterates over {@link DataStore#keySet()}, calling {@link DataStore#get(String)}
   * on each key.
   * </p>
   *
   * @param dataStore data store
   * @return debug string
   */
  public static String toString(DataStore<?> dataStore) {
    try {
      StringBuilder sb = new StringBuilder();
      sb.append('{');
      boolean first = true;
      for (String key : dataStore.keySet()) {
        if (first) {
          first = false;
        } else {
          sb.append(", ");
        }
        sb.append(key).append('=').append(dataStore.get(key));
      }
      return sb.append('}').toString();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private DataStoreUtils() {
  }
}
