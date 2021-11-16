package ucf.assignments.gui;

import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.Pane;
import ucf.assignments.App;
import ucf.assignments.controllers.TabController;
import ucf.assignments.todo.List;

import java.io.IOException;
import java.util.ArrayList;

import static ucf.assignments.gui.Auxiliary.getFXML;

public class ListEditor {
private final TabPane pane;
private final ArrayList<TabController> tabs = new ArrayList<>();

/* ---------- Constructor ---------- */
public ListEditor(TabPane node) {
    this.pane = node;
    this.pane.getSelectionModel().selectedItemProperty().addListener(this::updateMenuBar);
}

/* ---------- Getters ---------- */
public TabPane getTabPane() {return this.pane;}

private TabController getTab(int listIndex) {
    for (TabController t : this.tabs)
        if ( t.listIndex == listIndex )
            return t;
    return null;
}

/* ---------- TabPane Manipulation ---------- */

public void newTab(List list, int listIndex, boolean fromFile) throws IOException {
    // Load new tab from FXML.
    FXMLLoader fxml = getFXML("views/tab");
    Tab tab = fxml.load();
    tab.setOnClosed(event -> this.tabs.remove(listIndex));
    // Initialize Tab.
    TabController controller = fxml.getController();
    controller.init(list, listIndex);
    this.tabs.add(controller);
    // Add Tab to editor.
    this.pane.getTabs().add(tab);
    this.pane.getSelectionModel().select(this.pane.getTabs().size() - 1);
    // Prompt user for tab (list) title if it's new.
    if ( !fromFile )
        App.gui.promptForTitle(true);
}

public void removeList(int listIndex) {
    int tabIndex = this.getTabIndex(listIndex);
    if ( tabIndex != -1 )
		this.pane.getTabs().remove(tabIndex);
    this.tabs.remove(listIndex);
}

private int getTabIndex(int listIndex) {
	for (int i = 0; i < this.tabs.size(); i++)
		if (this.tabs.get(i).listIndex == listIndex)
			return i;
	return -1;
}

public void renameTab(int listIndex, String newTitle) {
    TabController tab = this.getTab(listIndex);
    if ( tab == null ) {return;}
    tab.getRoot().setText(newTitle);
}

public void goToTab(int listIndex) throws IOException {
    TabController tab = this.getTab(listIndex);
    if ( tab == null ) {
        this.newTab(App.mem.get(listIndex), listIndex, true);
    } else {
        int tabIndex = this.getTabPane().getTabs().indexOf(tab.getRoot());
        this.getTabPane().getSelectionModel().select(tabIndex);
    }
}

public void reset() {
    this.tabs.clear();
    this.pane.getTabs().clear();
}

/* ---------- Auxiliary ---------- */

public TabController getCurrentTab() {
    int tabIndex = this.currentTabIndex();
	if ( tabIndex == -1 )
        return null;
    return this.tabs.get(tabIndex);
}

private void updateMenuBar(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {
    if ( newTab == null ) {
        App.gui.menuBar.toggleListItems(false);
        return;
    }
    TabController tab = this.tabs.get(this.pane.getTabs().indexOf(newTab));

    App.gui.menuBar.toggleListItems(true);
    App.gui.menuBar.completedCheck.setSelected(tab.showCompleted);
    App.gui.menuBar.incompleteCheck.setSelected(tab.showIncomplete);
}

public int currentTabIndex() {return this.pane.getSelectionModel().getSelectedIndex();}

public void removeItem(int listIndex, int itemIndex) {
    // Get tab.
    TabController tab = this.getTab(listIndex);
    // Abort if containing tab not open.
    if ( tab == null ) return;
    // Abort if item would not be in tab.
    if (!tab.passesFilter(itemIndex)) return;
    int cardIndex = tab.getCardIndex(itemIndex);
    // Abort if card not in tab.
    if (cardIndex == -1)
        return;
    tab.getBody().getChildren().remove(cardIndex);
}
}
