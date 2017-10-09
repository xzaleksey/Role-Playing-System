package com.valyakinaleksey.roleplayingsystem.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SerializableUtils {
  public static <T extends Serializable> byte[] serialize(T object) throws IOException {
    ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
    ObjectOutputStream objectOutputStream = null;
    try {
      objectOutputStream = new ObjectOutputStream(byteArrayOutputStream);
      objectOutputStream.writeObject(object);
    } finally {
      if (objectOutputStream != null) {
        try {
          objectOutputStream.close();
        } catch (IOException ignored) {
        }
      }
    }
    return byteArrayOutputStream.toByteArray();
  }

  @SuppressWarnings("unchecked") public static <T extends Serializable> T deserialize(byte[] b)
      throws IOException, ClassNotFoundException {
    ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(b);
    ObjectInputStream objectInputStream = null;
    T object = null;
    try {
      objectInputStream = new ObjectInputStream(byteArrayInputStream);
      object = (T) objectInputStream.readObject();
    } finally {
      if (objectInputStream != null) {
        try {
          objectInputStream.close();
        } catch (IOException ignored) {
        }
      }
    }
    return object;
  }
}
