package ucf.assignments.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.MenuItem;
import ucf.assignments.App;
import ucf.assignments.data.FileHandler;
import ucf.assignments.todo.Item;
import ucf.assignments.todo.List;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

public class MenuBarController {

@FXML
public CheckMenuItem incompleteCheck;
@FXML
public CheckMenuItem completedCheck;
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

/* ---------- Fields ---------- */
/* ---------- Initializer ---------- */
// Disable any menu items that can't be used without a list in the List Editor.
public void initialize() { this.toggleListItems(false); }

/* ---------- Auxiliary ---------- */
// Toggles all menu items that can't be used without a list in the List Editor.
public void toggleListItems(boolean state) {
    this.saveList.setDisable(!state);
    this.deleteList.setDisable(!state);
    this.completedCheck.setDisable(!state);
    this.incompleteCheck.setDisable(!state);
    this.changeTitle.setDisable(!state);
    this.newItem.setDisable(!state);
    this.clearItems.setDisable(!state);
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
    if ( file == null ) return;
    // Parse file into lists and addList each list to the GUI.
    ArrayList<List> newLists = FileHandler.readFile(file);
    assert newLists != null : "File read error";
    for (List newList : newLists)
        App.gui.newList(newList, true);
}
@FXML
private void file_saveAs() throws IOException {
    // Abort if no lists to save.
    if (App.mem.getAllLists().size() == 0) return;
    // Get file.
    File file = FileHandler.promptSaveFile();
    // Write to file.
    if ( file == null ) { return; }
    FileHandler.writeToFile(file, App.mem.getAllLists());
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
private void help_usage() throws IOException { App.gui.helpScreen(); }

}
