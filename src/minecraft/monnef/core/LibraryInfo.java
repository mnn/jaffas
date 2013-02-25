package monnef.core;

public class LibraryInfo {
    private final String Name;
    private final String Sha1Hash;
    private final String FileName;

    public LibraryInfo(String name, String sha1Hash, String fileName) {
        Name = name;
        Sha1Hash = sha1Hash;
        FileName = fileName;
    }

    public String getName() {
        return Name;
    }

    public String getSha1Hash() {
        return Sha1Hash;
    }

    public String getFileName() {
        return FileName;
    }
}
