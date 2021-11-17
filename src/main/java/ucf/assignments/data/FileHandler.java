package ucf.assignments.data;

import javafx.stage.FileChooser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ucf.assignments.App;
import ucf.assignments.todo.List;

import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {

/* ---------- File Manupulation ---------- */

// TODO: Merge writeLists and writeList into one method.
public static void writeList(File file, List list) throws IOException {
    // Create file writer.
    FileWriter writer = new FileWriter(file);
    // Create a JSON array to save the list into.
    JSONArray jsonLists = new JSONArray();
    // Convert the list to a JSON object and add it to the array.
    jsonLists.put(new JSONObject(list.getSaveData()));
    // Create the main JSON Object to enclose the JSON array.
    String jsonString = new JSONObject().put("lists", jsonLists).toString();
    writer.write(jsonString);
    // Close the writer.
    writer.close();
}

public static void writeLists(ArrayList<List> lists, String filePath) throws IOException {
    StringBuilder data = new StringBuilder("{[");
    for (int i = 0; i < lists.size(); i++) {
        List l = lists.get(i);
        data.append(l.getSaveData());
        if ( i != lists.size() - 1 )
            data.append(",");
    }
    BufferedWriter w = new BufferedWriter(new FileWriter(filePath));
    w.write(data.toString());
    w.close();
}

public static ArrayList<List> readFile(File file) throws FileNotFoundException {
    String data = new Scanner(file).nextLine();
    JSONArray fileLists;
    try{
        // Get a JSONObject from the file's data string.
        JSONObject fileData = new JSONObject(data);
        // Get the list array from the main JSONObject.
        fileLists = fileData.getJSONArray("lists");
    }
    catch(JSONException e) {
        System.out.println(e.getLocalizedMessage());
        return null;
    }
    // Parse each list and add it to the array.
    ArrayList<List> newLists = new ArrayList<>();
    for (int i = 0; i < fileLists.length(); i++)
        newLists.add(new List(fileLists.getJSONObject(i)));
    return newLists;
}

/* ---------- File Managing ---------- */
//  Pre-condition:  The App's GUI must be initialized;
//  Post-condition: Prompts the user for a file to open and returns it.
public static File promptOpenFile() {
	assert App.gui != null : "GUI not initialized";
    // Open file chooser.
    FileChooser fc = FileHandler.getFileChooser("Open...");
    // Get file.
    return fc.showOpenDialog(App.gui.stage);
}

//  Pre-condition:  The App's GUI must be initialized;
//  Post-condition: Prompts the user for a save file location and returns it.
public static File promptSaveFile() {
	assert App.gui != null : "GUI not initialized";
	// Open file chooser.
    FileChooser fc = FileHandler.getFileChooser("Save as...");
    // Get file.
    return fc.showSaveDialog(App.gui.stage);
}

//  Post-condition: Returns a list file chooser the appropriate title and extension filter.
public static FileChooser getFileChooser(String title) {
    FileChooser fc = new FileChooser();
    fc.setTitle(title);
    fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Todo List Files", "*.todo"));
    return fc;
}
}