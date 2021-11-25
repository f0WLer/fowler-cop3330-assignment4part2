package ucf.assignments.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ucf.assignments.App;

public class TitlePromptController {

/* ---------- Fields ---------- */
private int listIndex;
private Stage stage;
//  FXML
@FXML
private TextField titleField;
@FXML
private Label label;

/* ---------- Initializer ---------- */
// Sets its fields and the appropriate getTitle.
public void init(int listIndex, Stage stage, boolean newList) {
	this.listIndex = listIndex;
	this.stage = stage;
	if ( !newList ) this.label.setText("Rename " + App.mem.getList(listIndex).getTitle() + "...");
}


@FXML
private void confirm() {
    // If the field has text, change the getTitle. Otherwise, do nothing.
    if ( !this.titleField.textProperty().getValue().trim().isEmpty() )
        App.gui.setListTitle(this.listIndex, titleField.textProperty().getValue().trim());
    // Close the popup.
    this.stage.close();
}
}
