package ucf.assignments.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ucf.assignments.App;
import ucf.assignments.todo.Item;
import ucf.assignments.todo.List;

import java.io.IOException;

import static ucf.assignments.gui.Auxiliary.getFXML;

public class TabController {
/* ---------- FXML Fields ---------- */
@FXML
private VBox body;
@FXML
private Tab root;
// Getters
public VBox getBody() { return this.body; }
public Tab getRoot() { return this.root; }

/* ---------- Fields ---------- */
public boolean allowCompleted;
public boolean allowIncomplete;
public int listIndex;

/* ---------- Initializer ---------- */
public void init(List list, int listIndex) throws IOException {
    // Set default filter settings;
    this.allowCompleted = true;
    this.allowIncomplete = true;
    // listIndex is index of this tab in App.lists().
    this.listIndex = listIndex;
    // Set tab's getTitle.
    this.root.setText(list.getTitle());
    // Create and addList a new item card for each item in the list.
    for (int i = 0; i < list.getItems().size(); i++)
        add(i);
}

/* ---------- Item/Card Manipulation ---------- */
//  Pre-condition:  itemIndex is a valid index to an existing item in the list.
//  Post-condition: Adds the item as a card to the tab.
public void add(int itemIndex) throws IOException {
    assert itemIndex < App.mem.getList(this.listIndex).size() : "item index out of bounds";
    // Abort if item doesn't pass through tab's filter.
    if ( !this.passesFilter(itemIndex) ) { return; }
    // Load a new card for the item.
    FXMLLoader fxml = getFXML("views/itemCard");
    Pane card = fxml.load();
    // Initialize the card's controller.
    ((ItemCardController)fxml.getController()).init(this, itemIndex);
    // Add the card to the tab.
    body.getChildren().add(card);
}

//  Post-condition: Clears the tab of all cards.
public void clear() { body.getChildren().clear(); }

/* ---------- Auxiliary ---------- */
//  Pre-condition:  itemIndex is a valid index to an item in the list.
//  Post-condition: Returns true if the item passes through the tab's
//                  current filter settings.
public boolean passesFilter(int itemIndex) {
    assert itemIndex < App.mem.getList(this.listIndex).size() : "item index out of bounds";
    // Get the item from the list.
    Item item = App.mem.getList(this.listIndex).get(itemIndex);
    // Compare the item's getCompleted property to the corresponding filter.
    if ( item.getCompleted() )
        return this.allowCompleted;
    else
        return this.allowIncomplete;
}

//  Pre-condition:  itemIndex is a valid index to an item in the list.
//  Post-condition: Returns the index of the item's card within the tab.
//                  If card does not exist, returns -1.
public int getCardIndex(int itemIndex) {
    // Traverse through the tab's cards.
    for (int i = 0; i < this.body.getChildren().size(); i++) {
        Pane card = (Pane)this.body.getChildren().get(i);
        // If this card's itemIndex property matches, return it.
        if ( (int)card.getProperties().get("itemIndex") == itemIndex )
            return i;
    }
    // Item card not found in tab.
    return -1;
}

}
