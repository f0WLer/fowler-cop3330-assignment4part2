package ucf.assignments.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import ucf.assignments.App;

public class NewListPromptController {
		@FXML
		private TextField titleField;
		@FXML
		private Label label;

		private int listIndex;
		private Stage stage;


		@FXML
		private void confirm() {
				// If the field has text, change the title. Otherwise, do nothing.
				if (!this.titleField.textProperty().getValue().trim().isEmpty())
					App.gui.setListTitle(this.listIndex, titleField.textProperty().getValueSafe());
				// Close the popup.
				this.stage.close();
		}

		public void init(int listIndex, Stage stage, boolean newList) {
				this.listIndex = listIndex;
				this.stage = stage;

				if (!newList)
						this.label.setText("Edit title...");
		}
}
