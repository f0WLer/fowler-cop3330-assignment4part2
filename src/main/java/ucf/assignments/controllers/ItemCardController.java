package ucf.assignments.controllers;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import ucf.assignments.App;
import ucf.assignments.todo.DueDate;
import ucf.assignments.todo.Item;

import java.time.LocalDate;


public class ItemCardController {
/* ---------- FXML Fields ---------- */
@FXML
private Pane root;
@FXML
private Pane checkPane;
@FXML
private Pane delPane;
@FXML
private TextArea description;
@FXML
private DatePicker due;

/* ---------- Fields ---------- */
private TabController tab;
private Boolean completed;
// itemIndex property getter.
private int itemIndex() { return (int)this.root.getProperties().get("itemIndex"); }

/* ---------- Initializer ---------- */
//  Pre-condition:  tab is the controller of the tab that this card belongs to.
//  Post-condition: Populates the controller with its corresponding item's data.
public void init(TabController tab, int itemIndex) {
    this.tab = tab;
    this.root.getProperties().put("itemIndex", itemIndex);

    // Populate the card's properties with its corresponding item's data.
    Item item = App.mem.getList(this.tab.listIndex).get(itemIndex);
    // Description.
    this.description.setText(item.getDescription());
    // Completed/Incomplete state.
    this.completed = item.getCompleted();
    this.updateCheckButton();
    // Due date.
    this.due.setValue(item.getDue().toLocalDate());

    // Add getDescription "on change" listener.
    description.focusedProperty().addListener(this::changeDescription);
    // Add getDue date "on change" listener
    due.valueProperty().addListener(this::changeDue);
}


/* ---------- Item Data Manipulation ---------- */
@FXML
private void pressCheckButton() {
    // Flip item.getCompleted property.
    this.completed = !this.completed;
    // Update the item at index 'itemIndex' from list at index 'listIndex' in App.mem.
    App.mem.getList(this.tab.listIndex).get(this.itemIndex()).setCompleted(this.completed);
    // If the item's new getCompleted state doesn't pass the tab's item filter, removeList it.
    if ( (this.completed && !tab.allowCompleted) || (!this.completed && !tab.allowIncomplete) ) {
        this.tab.getBody().getChildren().remove(this.root);
        return;
    }
    // Otherwise, update the button's appearance.
    this.updateCheckButton();
}

@FXML
private void pressDelButton() { App.gui.deleteItem(this.tab.listIndex, this.itemIndex()); }
@FXML
private void changeDescription(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
    // If newPropertyValue is false, the getDescription has left focus, so update changes made.
    if ( !newPropertyValue ) {
        // Trim whitespace and cut off text at 256 characters.
        String text = description.getText().trim();
        text = text.substring(0, Math.min(text.length(), 256));
        // Update the corresponding item's getDescription.
        App.mem.getList(this.tab.listIndex).getItems().get(this.itemIndex()).setDescription(text);
        description.setText(text);
    }
}
@FXML
private void changeDue(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
    // If new date is null, remove the current due date. If new date is a valid date, set the due date to that date.
    if ( newValue == null )
        App.mem.getList(this.tab.listIndex).getItems().get(this.itemIndex()).setDue(null);
    else
        App.mem.getList(this.tab.listIndex).getItems().get(this.itemIndex()).setDue(new DueDate(newValue));
}

/* ---------- Auxiliary ---------- */
private void updateCheckButton() {
    // If getCompleted, set color to green.
    if ( this.completed ) checkPane.setStyle("-fx-background-color: rgb(95, 185, 95)");
    else checkPane.setStyle("-fx-background-color: rgb(185, 185, 185)");
}
@FXML
private void highlightCheckButton() {
    // Highlight the opposite state (getCompleted: green->grey, incomplete: grey->green).
    if ( !this.completed ) checkPane.setStyle("-fx-background-color: rgb(95, 185, 95)");
    else checkPane.setStyle("-fx-background-color: rgb(185, 185, 185)");
}
@FXML
private void unhighlightCheckButton() {
    if ( !this.completed ) checkPane.setStyle("-fx-background-color: rgb(185, 185, 185)");
    else checkPane.setStyle("-fx-background-color: rgb(95, 185, 95)");
}
@FXML
private void highlightDelButton() { delPane.setStyle("-fx-background-color: rgb(185, 95, 95)"); }
@FXML
private void unhighlightDelButton() { delPane.setStyle("-fx-background-color: rgb(185, 185, 185)"); }
}
