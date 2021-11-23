package ucf.assignments.gui;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import ucf.assignments.App;
import ucf.assignments.controllers.TabController;
import ucf.assignments.data.FileHandler;
import ucf.assignments.todo.List;

import java.io.IOException;
import java.util.ArrayList;

public class ListEditor {

/* ---------- Fields ---------- */
// TabPane node.
private final TabPane pane;
// Array of every loaded tab's controller.
private final ArrayList<TabController> tabs = new ArrayList<>();

/* ---------- Constructor ---------- */
public ListEditor(TabPane node) throws IOException {
    // Set the tab pane.
    this.pane = node;
    // Add Usage Tab to editor.
    this.openUsageTab();
    // Add tab select listener.
    this.pane.getSelectionModel().selectedItemProperty().addListener(this::updateMenuBar);
}

/* ---------- Getters ---------- */
public TabPane getTabPane() { return this.pane; }

/* ---------- TabPane Manipulation ---------- */
//  Pre-condition:  list is a new list at index listIndex in memory. fromFile is
//                  whether the list is from a file.
//  Post-condition: Adds the list as a tab in the List Editor.
public void newTab(List list, int listIndex, boolean fromFile) throws IOException {
    // Load new tab from FXML.
    FXMLLoader fxml = FileHandler.getFXML("views/tab");
    Tab tab = fxml.load();
    // Initialize Tab.
    TabController controller = fxml.getController();
    controller.init(list, listIndex);
    this.tabs.add(controller);
    // Set onClosed handler to remove controller from tabs.
    tab.setOnClosed(event -> this.removeTab(listIndex));
    // Add Tab to editor.
    this.pane.getTabs().add(tab);
    this.pane.getSelectionModel().select(this.pane.getTabs().size() - 1);
    // Prompt user for tab (list) getTitle if it's new.
    if ( !fromFile ) App.gui.promptForTitle(true);
}

//  Pre-condition:  listIndex is the index of a list in the List Editor.
//  Post-condition: Removes the list's tab from the List Editor.
public void removeList(int listIndex) {
    // Get the tab's index.
    int tabIndex = this.getTabIndex(listIndex);
    // If list is open in the List Editor, removeList its tab.
    if ( tabIndex != -1 ) this.pane.getTabs().remove(tabIndex);
    // Removes the tab's controller from this.tabs if it exists.
    this.tabs.remove(listIndex);
}

//  Pre-condition:  listIndex and itemIndex are valid list and item indexes.
//                  in the List Editor.
//  Post-condition: Removes the item's card in the list's tab in the List Editor.
public void removeItem(int listIndex, int itemIndex) {
    // Get tab.
    TabController tab = this.getTab(listIndex);
    // Abort if containing tab not open.
    if ( tab == null ) return;
    // Abort if item would not be in tab.
    if ( tab.passesFilter(itemIndex) ) return;
    int cardIndex = tab.getCardIndex(itemIndex);
    // Abort if card not in tab.
    if ( cardIndex == -1 ) return;
    tab.getBody().getChildren().remove(cardIndex);
    // Update itemIndex property of succeeding items.
    for (int i = cardIndex; i < tab.getBody().getChildren().size(); i++)
        tab.getBody().getChildren().get(i).getProperties().put("itemIndex", cardIndex);
}

//  Post-condition: Clears the List Editor of all tabs.
public void clear() {
    // Clear the tab controllers.
    this.tabs.clear();
    // Clear the TabPane.
    this.pane.getTabs().clear();
}

//  Pre-condition:  listIndex is a valid index of a list opened in the
//                  List Editor. newTitle is the tab's new getTitle.
//  Post-condition: Renames the tab in the List Editor.
public void renameTab(int listIndex, String newTitle) {
    // Get the current tab.
    TabController tab = this.getTab(listIndex);
    if ( tab == null ) { return; }
    // Set the getTitle of the current tab.
    tab.getRoot().setText(newTitle);
}

//  Pre-condition:  listIndex is a valid index of a list in memory.
//  Post-condition: Opens the list in a tab in the List Editor.
public void openTab(int listIndex) throws IOException {
    // Get the list's corresponding tab in the List Editor.
    TabController tab = this.getTab(listIndex);
    if ( tab == null )
        // If list isn't open in the List Editor, open it.
        this.newTab(App.mem.getList(listIndex), listIndex, true);
    else {
        // If the list is open, select its corresponding tab.
        int tabIndex = this.getTabPane().getTabs().indexOf(tab.getRoot());
        this.getTabPane().getSelectionModel().select(tabIndex);
    }
}

/* ---------- Auxiliary ---------- */

public void openUsageTab() throws IOException {
    // Abort if the Usage tab is already open.
    if ( this.pane.getSelectionModel().getSelectedItem() != null && this.pane.getSelectionModel().getSelectedItem().getProperties().get("isUsageTab") != null )
        return;
    // Load the Usage tab from FXML.
    FXMLLoader fxmlLoader = FileHandler.getFXML("views/usageTab");
    Tab usageTab = fxmlLoader.load();
    usageTab.getProperties().put("isUsageTab", true);
    // Add the Usage tab to the TabPane.
    this.pane.getTabs().add(usageTab);
    // Go to the Usage tab.
    this.pane.getSelectionModel().select(this.pane.getTabs().size() - 1);
}

//  Pre-condition:  The List Editor must contain at least one list.
//  Post-condition: Returns the controller of the currently opened tab
//                  in the List Editor.
public TabController getCurrentTab() {
    // Get the index of the current tab.
    int tabIndex = this.currentTabIndex();
    if ( tabIndex == -1 ) return null;
    // Return the current opened tab.
    return this.tabs.get(tabIndex);
}

//  Pre-condition:  listIndex is a valid index of an item open in the List Editor.
//  Post-condition: Returns the controller to the item's tab in the List Editor.
private TabController getTab(int listIndex) {
    // Go through each controller and find the item's tab's controller.
    for (TabController t : this.tabs)
        if ( t.listIndex == listIndex ) return t;
    return null;
}

//  Pre-condition:  listIndex is the index of a list opened in the List Editor.
//  Post-condition: Returns the index of the list's tab in the List Editor.
private int getTabIndex(int listIndex) {
    // Look for the tab with the corresponding listIndex property.
    for (int i = 0; i < this.tabs.size(); i++)
        if ( this.tabs.get(i).listIndex == listIndex ) return i;
    return -1;
}

//  Post-condition: Tab close handler -- Removes the corresponding controller from this.tabs.
private void removeTab(int listIndex) {
    for (int i = 0; i < this.tabs.size(); i++)
        if ( this.tabs.get(i).listIndex == listIndex ) {
            this.tabs.remove(i);
            return;
        }
}

//  Tab select listener method. If the List Editor is empty, disables all list
//  manipulating menu items. If a tab is open, updates the item filter menu items.
private void updateMenuBar(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {
    // If the old tab was the Usage tab, close it.
    if ( oldTab != null && oldTab.getProperties().get("isUsageTab") != null ) this.pane.getTabs().remove(oldTab);
    // If no tab is open or the new tab is the Usage tab, disable all list manipulating menu items.
    if ( newTab == null || newTab.getProperties().get("isUsageTab") != null ) {
        App.gui.getMenuBar().toggleListItems(false);
        return;
    }
    // If there's an opened tab set its filter menuItems.
    TabController tab = this.tabs.get(this.pane.getTabs().indexOf(newTab));
    App.gui.getMenuBar().toggleListItems(true);
    App.gui.getMenuBar().completedCheck.setSelected(tab.allowCompleted);
    App.gui.getMenuBar().incompleteCheck.setSelected(tab.allowIncomplete);
}

//  Pre-condition:  The List Editor must contain at least one list.
//  Post-condition: Returns the index of the currently selected tab.
private int currentTabIndex() { return this.pane.getSelectionModel().getSelectedIndex(); }
}
