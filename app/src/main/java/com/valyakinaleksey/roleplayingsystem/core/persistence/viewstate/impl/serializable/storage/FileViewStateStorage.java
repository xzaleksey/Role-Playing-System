package com.valyakinaleksey.roleplayingsystem.core.persistence.viewstate.impl.serializable.storage;


import com.valyakinaleksey.roleplayingsystem.core.utils.lambda.Action1;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Implementation of {@link ViewStateStorage} that saves objects to file by fileName specified
 */
public class FileViewStateStorage implements ViewStateStorage {

    private final static String TEMP_PREFIX = ".temp";

    private final String fileName;

    public FileViewStateStorage(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public synchronized void save(Action1<ObjectOutputStream> action) {
        ObjectOutputStream objectOut = null;
        try {
            File tempFile = getTempFile();
            FileOutputStream fileOut = new FileOutputStream(tempFile);
            objectOut = new ObjectOutputStream(fileOut);
            action.apply(objectOut);
            tempFile.renameTo(getMainFile());
            fileOut.getFD().sync();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objectOut != null) {
                try {
                    objectOut.close();
                } catch (IOException e) {

                }
            }
        }
    }

    @Override
    public synchronized void restore(Action1<ObjectInputStream> action) {
        ObjectInputStream objectIn = null;
        try {
            FileInputStream fileIn =  new FileInputStream(new File(fileName));
            objectIn = new ObjectInputStream(fileIn);
            action.apply(objectIn);
        } catch (FileNotFoundException e) {

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (objectIn != null) {
                try {
                    objectIn.close();
                } catch (IOException e) {

                }
            }
        }
    }

    @Override
    public synchronized void cleanUp() {
        File file = new File(fileName);
        file.delete();
        File tempFile = new File(fileName.concat(TEMP_PREFIX));
        tempFile.delete();
    }

    private File getTempFile() {
        return new File(fileName.concat(TEMP_PREFIX));
    }

    private File getMainFile() {
        return new File(fileName);
    }
}
