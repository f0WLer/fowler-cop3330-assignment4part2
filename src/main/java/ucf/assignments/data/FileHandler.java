package ucf.assignments.data;

import javafx.stage.FileChooser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import ucf.assignments.App;
import ucf.assignments.todo.List;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class FileHandler {

/* ---------- File Load/Save ---------- */
//  Pre-condition:  'file' is a valid .todo file to write 'lists' to.
//  Post-condition: Writes the Lists as JSON objects to the file.
public static void writeToFile(File file, ArrayList<List> lists) throws IOException {
    // Create file writer.
    FileWriter writer = new FileWriter(file);
    // Create a JSON array to save the lists into.
    JSONArray jsonLists = new JSONArray();
    // Convert each list into a JSON object and addList it into the array.
    for (List l : lists)
        jsonLists.put(new JSONObject(l));
    // Create the main JSON Object to enclose the JSON array.
    String jsonString = new JSONObject().put("lists", jsonLists).toString();
    writer.write(jsonString);
    // Close the writer.
    writer.close();
}
// Override for single list.
public static void writeToFile(File file, List list) throws IOException {
    ArrayList<List> array = new ArrayList<>();
    array.add(list);
    FileHandler.writeToFile(file, array);
}

//  Pre-condition:  file is a valid .todo file.
//  Post-condition: Returns an ArrayList of the Lists stored in the file.
public static ArrayList<List> readFile(File file) throws FileNotFoundException {
    // Get the file data.
    String data = new Scanner(file).nextLine();

    // Get the JSONArray of Lists (as JSONObjects) from the data.
    JSONArray fileLists;
    try{
        // Create a new JSONObject from the file's data string.
        JSONObject fileData = new JSONObject(data);
        // Pull the list array from the JSONObject.
        fileLists = fileData.getJSONArray("lists");
    }catch (JSONException e){
        System.out.println(e.getLocalizedMessage());
        return null;
    }
    // Parse each JSONObject into a List and addList it to the array.
    ArrayList<List> newLists = new ArrayList<>();
    for (int i = 0; i < fileLists.length(); i++)
        newLists.add(new List(fileLists.getJSONObject(i)));
    return newLists;
}

/* ---------- File Managing ---------- */
//  Pre-condition:  The App's GUI must be initialized.
//  Post-condition: Prompts the user for a file to open and returns it.
public static File promptOpenFile() {
    assert App.gui != null : "GUI not initialized";
    // Open file chooser.
    FileChooser fc = FileHandler.getFileChooser("Open...");
    // Get file.
    return fc.showOpenDialog(App.gui.stage);
}

//  Pre-condition:  The App's GUI must be initialized.
//  Post-condition: Prompts the user for a save file location and returns it.
public static File promptSaveFile() {
    assert App.gui != null : "GUI not initialized";
    // Open file chooser.
    FileChooser fc = FileHandler.getFileChooser("Save as...");
    // Get file.
    return fc.showSaveDialog(App.gui.stage);
}

//  Post-condition: Returns a list file chooser the appropriate getTitle and extension filter.
public static FileChooser getFileChooser(String title) {
    FileChooser fc = new FileChooser();
    fc.setTitle(title);
    fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Todo List Files", "*.todo"));
    return fc;
}
}