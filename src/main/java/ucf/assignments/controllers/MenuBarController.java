package ucf.assignments.controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import javafx.stage.FileChooser;
import org.json.JSONArray;
import org.json.JSONObject;
import ucf.assignments.App;
import ucf.assignments.data.FileHandler;
import ucf.assignments.todo.Item;
import ucf.assignments.todo.List;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

public class MenuBarController {

/* ---------- FXML Fields ---------- */
@FXML
private MenuItem saveList;
@FXML
private MenuItem deleteList;
@FXML
private MenuItem changeTitle;
@FXML
private MenuItem newItem;
@FXML
private MenuItem clearItems;
@FXML
private CheckMenuItem incompleteCheck;
@FXML
private CheckMenuItem completedCheck;

/* ---------- Fields ---------- */
private final Object[] listDependentItems = { this.saveList, this.deleteList, this.completedCheck, this.incompleteCheck, this.changeTitle, this.newItem, this.clearItems};

/* ---------- Initializer ---------- */
// Disable any menu items that can't be used without a list in the List Editor.
public void initialize() { this.toggleListItems(false); }

/* ---------- Auxiliary ---------- */
// Toggles all menu items that can't be used without a list in the List Editor.
public void toggleListItems(boolean state) {
    for (Node n : (Node[])this.listDependentItems)
        n.setDisable(!state);
}

/* ---------- MenuBar Methods ---------- */
@FXML
private void file_new() throws IOException {
    this.file_saveAs();
    App.gui.resetGUI();
}
@FXML
private void file_open() throws IOException {
    File file = FileHandler.promptOpenFile();
    if ( file == null )
        return;
    // Parse file into lists and add each list to the GUI.
    ArrayList<List> newLists = FileHandler.readFile(file);
    for (List newList : newLists)
        App.gui.newList(newList, true);
}
@FXML
private void file_saveAs() throws IOException {
    // Get file.
    File file = FileHandler.promptSaveFile();

    if ( file == null ) { return; }
    FileWriter writer = new FileWriter(file);
    JSONArray lists = new JSONArray();
    for (List l : App.mem.getAll())
        lists.put(new JSONObject(l.getSaveData()));
    writer.write(new JSONObject().put("lists", lists).toString());
    writer.close();
}

@FXML
private void list_new() throws IOException { App.gui.newList(new List(), false); }

@FXML
private void list_newItem() throws IOException { App.gui.newItem(new Item()); }

@FXML
private void list_changeTitle() throws IOException { App.gui.promptForTitle(false); }

@FXML
private void list_clearItems() { App.gui.clearList(); }

@FXML
private void list_showCompleted() throws IOException { App.gui.toggleItemFilter(true); }

@FXML
private void list_showIncomplete() throws IOException { App.gui.toggleItemFilter(false); }

@FXML
private void list_save() throws IOException { App.gui.saveList(); }

@FXML
private void list_delete() { App.gui.deleteList(); }

@FXML
private void help_usage() { }

}
