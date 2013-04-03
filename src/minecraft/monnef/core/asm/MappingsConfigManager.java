package monnef.core.asm;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.OutputStream;
import java.util.HashMap;

public class MappingsConfigManager {
    @SuppressWarnings("unchecked")
    public static HashMap<MappedObjectType, MappingDictionary> loadConfig(InputStream inputStream) {
        try {
            ObjectInputStream ois = new ObjectInputStream(inputStream);
            Object read = ois.readObject();
            return (HashMap<MappedObjectType, MappingDictionary>) read;
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot load mappings config file.");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot load mappings config file.");
        }
    }

    public static void saveConfig(HashMap<MappedObjectType, MappingDictionary> database, OutputStream output) {
        try {
            ObjectOutputStream oos = new ObjectOutputStream(output);
            oos.writeObject(database);
            oos.close();
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Cannot save mappings config file.");
        }
    }
}
