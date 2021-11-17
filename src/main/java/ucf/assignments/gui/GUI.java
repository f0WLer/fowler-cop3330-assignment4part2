package ucf.assignments.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import ucf.assignments.App;
import ucf.assignments.controllers.MenuBarController;
import ucf.assignments.controllers.NewListPromptController;
import ucf.assignments.controllers.TabController;
import ucf.assignments.data.FileHandler;
import ucf.assignments.todo.Item;
import ucf.assignments.todo.List;

import java.io.File;
import java.io.IOException;

import static ucf.assignments.gui.Auxiliary.getFXML;

public class GUI extends Application {
// Main interface components.
public MenuBarController menuBar;
public WorkspaceView workspaceView;
public ListEditor listEditor;
// JavaFX Stage.
public Stage stage;

/* ---------- Constructor ---------- */
public GUI() { }

/* ---------- JavaFX ---------- */
// Launches JavaFX Application.
public void launch() { Application.launch(GUI.class); }

@Override
public void start(Stage stage) throws Exception {
    // Acknowledge App start in console.
    System.out.println("Application Started");
    Thread.setDefaultUncaughtExceptionHandler(App::spitError);

    // Set up JavaFX Stage.
    this.stage = stage;
    FXMLLoader fxml = getFXML("views/AppGUI");
    this.stage.setScene(new Scene(fxml.load()));

    // Launch the app.
    stage.show();
}



/* ---------- List Manipulation ---------- */

//  Pre-condition:  list is a new list not in App's memory.
//                  fromFile true if list was loaded from a file.
//  Post-condition: Adds the list to the App's memory and the interface,
//                  prompting for a title if fromFile is false.
public void newList(List list, boolean fromFile) throws IOException {
    // Add to the App lists.
    App.mem.add(list);
    int listIndex = App.mem.size() - 1;
    // Open in the List Editor.
    listEditor.newTab(list, listIndex, fromFile);
    // Add to the Workspace View.
    workspaceView.addList(list, listIndex);
}

//  Pre-condition:  The List Editor must contain at least one list.
//  Post-condition: Clears the interface of all of the currently displayed
//                  list's items.
public void clearList() {
    // Get the current tab/list.
    TabController tab = this.listEditor.getCurrentTab();
    // Abort if no tabs are displayed.
    if ( tab == null ) { return; }
    int listIndex = tab.listIndex;

    // Clear the tab.
    tab.clear();
    // Claer list in the Workspace View.
    this.workspaceView.clearList(listIndex);

    // Clear the list in memory.
    App.mem.get(listIndex).clear();
}

//  Pre-condition:  The List Editor must contain at least one list.
//  Post-Condition: Deletes the list from the interface and from memory.
public void deleteList() {
    // Get current tab/list.
    TabController tab = this.listEditor.getCurrentTab();
    // Abort if List Editor is empty.
    if ( tab == null )
        return;
    int listIndex = tab.listIndex;
    // Remove from List Editor.
    this.listEditor.removeList(listIndex);

    // Remove from Workspace View.
    this.workspaceView.removeList(listIndex);
    // Remove from memory.
    App.mem.remove(listIndex);
}

//  Pre-condition:  listIndex is an index to a list in memory.
//                  newTitle length 0 < n chars <= 32.
//  Post-condition: Sets the title of the list to the new title.
public void setListTitle(int listIndex, String newTitle) {
    // Trim newTitle.
    newTitle = newTitle.trim().substring(0, Math.min(newTitle.length(), 32));
    if ( newTitle.length() == 0 ) { newTitle = "Todo List"; }

    // Change list title.
    App.mem.get(listIndex).title(newTitle);
    // Update in List Editor.
    this.listEditor.renameTab(listIndex, newTitle);
    // Update in WorkspaceView.
    this.workspaceView.renameList(listIndex, newTitle);
}


/* ------------ Item Manipulation ---------- */

//  Pre-condition:  The List Editor must have at least one list.
//  Post-condition: Adds a new list to the interface and App memory.
public void newItem(Item item) throws IOException {
    // Get current tab/list.
    TabController tab = this.listEditor.getCurrentTab();
    // Abort if List Editor is empty.
    if ( tab == null ) { return; }
    int listIndex = tab.listIndex;
    List list = App.mem.get(listIndex);
    // Add the item to the list.
    App.mem.get(listIndex).add(item);
    // Get the index of the new item -- the end of the list.
    int itemIndex = list.size() - 1;
    // Add the new card to the list.
    tab.add(itemIndex);
    // Add the item to its corresponding branch in the Workspace View.
    this.workspaceView.addItem(listIndex, item);
}

//  Pre-condition:  listIndex and itemIndex are valid list and item indexes.
//  Post-condition: Removes the item from the list on the interface and in memory.
public void deleteItem(int listIndex, int itemIndex) {
    // Remove from Workspace View.
    this.workspaceView.removeItem(listIndex, itemIndex);
    // Remove from List Editor.
    this.listEditor.removeItem(listIndex, itemIndex);
    // Remove from App memory.
    App.mem.get(listIndex).remove(itemIndex);
}


/* ---------- Interface ---------- */

//  Post-condition: Clears the Workspace View, List Editor, and App memory.
public void resetGUI() {
    // Clear all lists.
    App.mem.clear();
    // Reset the Workspace Viewer.
    this.workspaceView.clear();
    // Reset the List Editor.
    this.listEditor.clear();
}

//  Pre-condition:  branchIndex is a Workspace View tree branch index.
//  Post-condition: Opens the list's tab if it exists, creates one if not.
public void openBranchInTab(int branchIndex) throws IOException {
    // Get the corresponding list index from the branch.
    int listIndex = this.workspaceView.getListIndex(branchIndex);
    // Open the list in the List Editor.
    this.listEditor.openTab(listIndex);
}

//  Pre-condition:  completedFilter is true for Completed Item filter, false
//                  for Incomplete Item filter.
//  Post-condition: Toggles the Completed or Incomplete Item filter.
public void toggleItemFilter(boolean completedFilter) throws IOException {
    // Get the current tab/list
    TabController tab = this.listEditor.getCurrentTab();
    // Abort if no list open in the List Editor.
    if ( tab == null )
        return;
    List list = App.mem.get(tab.listIndex);

    // Toggle the appropriate filter.
    if ( completedFilter )
        tab.showCompleted = !tab.showCompleted;
    else
        tab.showIncomplete = !tab.showIncomplete;
    // Clear the tab.
    tab.clear();
    // Repopulate the tab with the new filter conditions.
    for (int i = 0; i < list.getItems().size(); i++)
        tab.add(i); // Automatically filtered.
}

//  Pre-condition:  newList is true if the list has just been created.
//                  i.e. not loaded from a file.
public void promptForTitle(boolean newList) throws IOException {
    // Get the current tab.
    TabController tab = this.listEditor.getCurrentTab();
    // Abort if no list is open in the List Editor.
    if ( tab == null )
        return;
    int listIndex = tab.listIndex;

    // Create a new JavaFX pop-up window.
    final Stage popUp = new Stage();
    popUp.initModality(Modality.APPLICATION_MODAL);
    popUp.initOwner(this.stage);
    // Load FXML into the pop-up.
    FXMLLoader fxml = getFXML("views/newListPrompt");
    VBox promptBox = fxml.load();
    NewListPromptController pCont = fxml.getController();
    // Initialize pop-up.
    pCont.init(listIndex, popUp, newList);
    // Display pop-up.
    Scene popUpScene = new Scene(promptBox);
    popUp.setScene(popUpScene);
    popUp.show();
}

/* ---------- File Managing ---------- */

//  Pre-condition:  The List Editor must contain at least one list.
//  Post-condition: Prompts the user for a file location/name and saves
//                  the current tab's list to the file.
public void saveList() throws IOException {
    // Get current tab/list.
    TabController tab = this.listEditor.getCurrentTab();
    // Abort if no list open in List Editor.
    if ( tab == null ) { return; }
    List list = App.mem.get(tab.listIndex);

    // Create new file chooser.
    File file = FileHandler.promptSaveFile();
    // Abort if file null (user cancels).
    if ( file == null ) { return; }
    // Write list to file.
    FileHandler.writeList(file, list);
}
}
