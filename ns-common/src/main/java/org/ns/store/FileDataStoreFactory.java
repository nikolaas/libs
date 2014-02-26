package org.ns.store;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * {@link Beta} <br/>
 * Thread-safe file implementation of a credential store.
 *
 * <p>
 * For security purposes, the file's permissions are set to be accessible only by the file's owner.
 * Note that Java 1.5 does not support manipulating file permissions, and must be done manually or
 * using the JNI.
 * </p>
 *
 * @since 1.16
 * @author Yaniv Inbar
 */
public class FileDataStoreFactory extends AbstractDataStoreFactory {

  private static final Logger LOGGER = Logger.getLogger(FileDataStoreFactory.class.getName());

  /** Directory to store data. */
  private final File dataDirectory;

  /**
   * @param dataDirectory data directory
     * @throws java.io.IOException
   */
  public FileDataStoreFactory(File dataDirectory) throws IOException {
    dataDirectory = dataDirectory.getCanonicalFile();
    this.dataDirectory = dataDirectory;
    // error if it is a symbolic link
    if (IOUtils.isSymbolicLink(dataDirectory)) {
      throw new IOException("unable to use a symbolic link: " + dataDirectory);
    }
    // create parent directory (if necessary)
    if (!dataDirectory.exists() && !dataDirectory.mkdirs()) {
      throw new IOException("unable to create directory: " + dataDirectory);
    }
    setPermissionsToOwnerOnly(dataDirectory);
  }

  /** Returns the data directory.
     * @return  */
  public final File getDataDirectory() {
    return dataDirectory;
  }

  @Override
  protected <V extends Serializable> DataStore<V> createDataStore(String id) throws IOException {
    return new FileDataStore<>(this, dataDirectory, id);
  }

  /**
   * File data store that inherits from the abstract memory data store because the key-value pairs
   * are stored in a memory cache, and saved in the file (see {@link #save()} when changing values.
   *
   * @param <V> serializable type of the mapped value
   */
  static class FileDataStore<V extends Serializable> extends AbstractMemoryDataStore<V> {

    /** File to store data. */
    private final File dataFile;

    FileDataStore(FileDataStoreFactory dataStore, File dataDirectory, String id)
        throws IOException {
      super(dataStore, id);
      this.dataFile = new File(dataDirectory, id);
      // error if it is a symbolic link
      if (IOUtils.isSymbolicLink(dataFile)) {
        throw new IOException("unable to use a symbolic link: " + dataFile);
      }
      // create new file (if necessary)
      if (dataFile.createNewFile()) {
        keyValueMap = new HashMap<>();
        // save the credentials to create a new file
        save();
      } else {
        // load credentials from existing file
        keyValueMap = IOUtils.deserialize(new FileInputStream(dataFile));
      }
    }

    @Override
    void save() throws IOException {
      IOUtils.serialize(keyValueMap, new FileOutputStream(dataFile));
    }

    @Override
    public FileDataStoreFactory getDataStoreFactory() {
      return (FileDataStoreFactory) super.getDataStoreFactory();
    }
  }

  /**
   * Attempts to set the given file's permissions such that it can only be read, written, and
   * executed by the file's owner.
   *
   * @param file the file's permissions to modify
   * @throws IOException
   */
  static void setPermissionsToOwnerOnly(File file) throws IOException {
    // Disable access by other users if O/S allows it and set file permissions to readable and
    // writable by user. Use reflection since JDK 1.5 will not have these methods
    try {
      Method setReadable = File.class.getMethod("setReadable", boolean.class, boolean.class);
      Method setWritable = File.class.getMethod("setWritable", boolean.class, boolean.class);
      Method setExecutable = File.class.getMethod("setExecutable", boolean.class, boolean.class);
      if (!(Boolean) setReadable.invoke(file, false, false)
          || !(Boolean) setWritable.invoke(file, false, false)
          || !(Boolean) setExecutable.invoke(file, false, false)) {
        LOGGER.warning("unable to change permissions for everybody: " + file);
      }
      if (!(Boolean) setReadable.invoke(file, true, true)
          || !(Boolean) setWritable.invoke(file, true, true)
          || !(Boolean) setExecutable.invoke(file, true, true)) {
        LOGGER.warning("unable to change permissions for owner: " + file);
      }
    } catch (InvocationTargetException exception) {
      Throwable cause = exception.getCause();
      //Throwables.propagateIfPossible(cause, IOException.class);
      // shouldn't reach this point, but just in case...
      throw new RuntimeException(cause);
    } catch (NoSuchMethodException exception) {
      LOGGER.warning("Unable to set permissions for " + file
          + ", likely because you are running a version of Java prior to 1.6");
    } catch (SecurityException exception) {
      // ignored
    } catch (IllegalAccessException exception) {
      // ignored
    } catch (IllegalArgumentException exception) {
      // ignored
    }
  }
  
  public static FileDataStoreFactory createFrom(String patch) {
      FileDataStoreFactory factory = null;
      try {
        factory = new FileDataStoreFactory(new File(patch));
      } catch (IOException ex) {
        Logger.getLogger(FileDataStoreFactory.class.getName()).log(Level.SEVERE, null, ex);
      }
      return factory;
  }
}
