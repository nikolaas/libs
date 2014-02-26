/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.ns.store;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;

/**
 *
 * @author stupak
 */
public class IOUtils {

  /**
   * {@link Beta} <br/>
   * Serializes the given object value to a newly allocated byte array.
   *
   * @param value object value to serialize
   * @since 1.16
   */
  public static byte[] serialize(Object value) throws IOException {
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    serialize(value, out);
    return out.toByteArray();
  }

  /**
   * {@link Beta} <br/>
   * Serializes the given object value to an output stream, and close the output stream.
   *
   * @param value object value to serialize
   * @param outputStream output stream to serialize into
   * @since 1.16
   */
  public static void serialize(Object value, OutputStream outputStream) throws IOException {
    try {
      new ObjectOutputStream(outputStream).writeObject(value);
    } finally {
      outputStream.close();
    }
  }

  /**
   * {@link Beta} <br/>
   * Deserializes the given byte array into to a newly allocated object.
   *
   * @param bytes byte array to deserialize or {@code null} for {@code null} result
   * @return new allocated object or {@code null} for {@code null} input
   * @since 1.16
   */
  public static <S extends Serializable> S deserialize(byte[] bytes) throws IOException {
    if (bytes == null) {
      return null;
    }
    return deserialize(new ByteArrayInputStream(bytes));
  }

  /**
   * {@link Beta} <br/>
   * Deserializes the given input stream into to a newly allocated object, and close the input
   * stream.
   *
   * @param inputStream input stream to deserialize
   * @since 1.16
   */
  @SuppressWarnings("unchecked")
  public static <S extends Serializable> S deserialize(InputStream inputStream) throws IOException {
    try {
      return (S) new ObjectInputStream(inputStream).readObject();
    } catch (ClassNotFoundException exception) {
      IOException ioe = new IOException("Failed to deserialize object");
      ioe.initCause(exception);
      throw ioe;
    } finally {
      inputStream.close();
    }
  }

  /**
   * {@link Beta} <br/>
   * Returns whether the given file is a symbolic link.
   *
   * @since 1.16
   */
  public static boolean isSymbolicLink(File file) throws IOException {
    // first try using Java 7
    try {
      // use reflection here
      Class<?> filesClass = Class.forName("java.nio.file.Files");
      Class<?> pathClass = Class.forName("java.nio.file.Path");
      Object path = File.class.getMethod("toPath").invoke(file);
      return ((Boolean) filesClass.getMethod("isSymbolicLink", pathClass).invoke(null, path))
          .booleanValue();
    } catch (InvocationTargetException exception) {
      Throwable cause = exception.getCause();
      //Throwables.propagateIfPossible(cause, IOException.class);
      // shouldn't reach this point, but just in case...
      throw new RuntimeException(cause);
    } catch (ClassNotFoundException exception) {
      // handled below
    } catch (IllegalArgumentException exception) {
      // handled below
    } catch (SecurityException exception) {
      // handled below
    } catch (IllegalAccessException exception) {
      // handled below
    } catch (NoSuchMethodException exception) {
      // handled below
    }
    // backup option compatible with earlier Java
    // this won't work on Windows, which is where separator char is '\\'
    if (File.separatorChar == '\\') {
      return false;
    }
    File canonical = file;
    if (file.getParent() != null) {
      canonical = new File(file.getParentFile().getCanonicalFile(), file.getName());
    }
    return !canonical.getCanonicalFile().equals(canonical.getAbsoluteFile());
  }
}
