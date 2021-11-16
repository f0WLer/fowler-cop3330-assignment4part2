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
/* ---------- Fields ---------- */
public boolean showCompleted;
public boolean showIncomplete;
public int listIndex;
/* ---------- FXML Fields ---------- */
@FXML
private VBox body;
@FXML
private Tab root;

// Getters
public VBox getBody() {return this.body;}

public Tab getRoot() {return this.root;}

/* ---------- Initializer ---------- */
public void init(List list, int listIndex) throws IOException {
	// Set default filter settings;
	this.showCompleted = true;
	this.showIncomplete = true;

	// listIndex is index of this tab in App.lists().
	this.listIndex = listIndex;

	// Set tab's title.
	this.root.setText(list.title());

	if ( list.getItems().size() == 0 )
		return;

	// For item in list:
	for (int i = 0;i < list.getItems().size();i++) {
		// Create and add a new item card
		add(i);
	}
}

/* ---------- Item/Card Manipulation ---------- */
public void add(int itemIndex) throws IOException {
	assert itemIndex < App.mem.get(this.listIndex).size() : "item index OOB";
	// Abort if item doesn't pass through tab's filter.
	if ( !this.passesFilter(itemIndex) ) { return; }
	FXMLLoader cardLoader = getFXML("views/itemCard");
	Pane card = cardLoader.load();
	((ItemCardController)cardLoader.getController()).init(this, this.listIndex, itemIndex);

	body.getChildren().add(card);
}

public void clear() {body.getChildren().clear();}


/* ---------- Auxiliary ---------- */
public boolean passesFilter(int itemIndex) {
	Item item = App.mem.get(this.listIndex).get(itemIndex);
	if (item.completed())
		return this.showCompleted;
	else
		return this.showIncomplete;
}

public int getCardIndex(int itemIndex) {
	for (int i = 0; i < this.body.getChildren().size(); i++) {
		Pane card = (Pane)this.body.getChildren().get(i);
		if ((int)card.getProperties().get("itemIndex") == itemIndex)
			return i;
	}
	return -1;
}

}
