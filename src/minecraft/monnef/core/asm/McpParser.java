package monnef.core.asm;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;

import static monnef.core.MonnefCorePlugin.Log;

public class McpParser {
    private static MappingDictionary methods, fields;

    public static void parse(HashMap<MappedObjectType, MappingDictionary> database, String path) {
        methods = new MappingDictionary();
        fields = new MappingDictionary();

        parseCsv(database.get(MappedObjectType.METHOD), path + "methods.csv", false);
        parseCsv(database.get(MappedObjectType.FIELD), path + "fields.csv", false);
        parsePackaged(database, path + "packaged.srg");
    }

    private static void parsePackaged(HashMap<MappedObjectType, MappingDictionary> database, String filePath) {
        //format:
        //CL: aat net/minecraft/world/WorldProviderEnd

        MappingDictionary classes = database.get(MappedObjectType.CLASS);
        if (classes.countKeys() > 0) {
            Log.printWarning("Map is not blank, eh?");
        }

        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(filePath);
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bReader = new BufferedReader(reader);

            while (bReader.ready()) {
                String line = bReader.readLine();
                String[] chopped = line.split(" ");
                if (chopped.length != 3 && chopped.length != 5) {
                    Log.printWarning("Probably damaged packaged file from MCP, weird line: \"" + line + "\"");
                } else {
                    if ("CL:".equals(chopped[0])) {
                        classes.put(chopped[2].replace('/', '.'), chopped[1].replace('/', '.'));
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String afterLastSlash(String input) {
        String[] tmp = input.split("/");
        return tmp[tmp.length - 1];
    }

    private static void parseCsv(MappingDictionary table, String filePath, boolean reversed) {
        if (table.countKeys() > 0) {
            Log.printWarning("Map is not blank, eh?");
        }
        try {
            FileInputStream inputStream = new FileInputStream(filePath);
            InputStreamReader reader = new InputStreamReader(inputStream);
            BufferedReader bReader = new BufferedReader(reader);
            bReader.readLine(); // skip first non-data line
            while (bReader.ready()) {
                String line = bReader.readLine();
                String[] chopped = line.split(",");
                if (chopped.length < 3) {
                    Log.printWarning("Probably damaged methods/fields file from MCP, weird line: \"" + line + "\"");
                } else {
                    //format:
                    //searge,name,side,desc
                    //func_70000_a,addServerStatsToSnooper,2,
                    if (reversed) {
                        table.put(chopped[0], chopped[1]);
                    } else {
                        table.put(chopped[1], chopped[0]);
                    }
                }
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
