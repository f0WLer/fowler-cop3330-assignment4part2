package ucf.assignments.controllers;

import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ucf.assignments.App;
import ucf.assignments.todo.DueDate;
import ucf.assignments.todo.Item;

import java.time.LocalDate;


public class ItemCardController {
		private TabController tab;

		@FXML
		private Pane root;
		private VBox parent;

		@FXML
		private Pane checkPane;
		@FXML
		private Pane delPane;

		@FXML
		private TextArea description;

		@FXML
		private DatePicker due;

		private Boolean completed;

		// 'listIndex' and 'itemIndex' property getters.
		private int listIndex() {
				return (int)this.root.getProperties().get("listIndex");
		}
		private int itemIndex() {
				return (int)this.root.getProperties().get("itemIndex");
		}

		public void init(TabController tab, VBox parent, int listIndex, int itemIndex) {
				this.tab = tab;
				this.parent = parent;
				this.root.getProperties().put("listIndex", listIndex);
				this.root.getProperties().put("itemIndex", itemIndex);

				// Populate the card's properties with its corresponding item's data.
				Item item = App.lists().get(listIndex).getItem(itemIndex);
				this.description.setText(item.description());
				this.description.getProperties().put("lastEntry", this.description.getText());

				this.completed = item.completed();
				this.updateCheckButton();

				this.due.setValue(item.due().toLocalDate());

				// Add description "on change" listener.
				description.focusedProperty().addListener(this::changeDescription);

				// Add due date "on change" listener
				due.valueProperty().addListener(this::changeDue);
		}

		public void highlightCheckButton() {
				// Highlight the opposite state (completed: green->grey, incomplete: grey->green).
				if (!this.completed)
						checkPane.setStyle("-fx-background-color: rgb(95, 185, 95)");
				else
						checkPane.setStyle("-fx-background-color: rgb(185, 185, 185)");
		}
		public void unhighlightCheckButton() {
				// Same principle as highlightCheckButton().
				if (!this.completed)
						checkPane.setStyle("-fx-background-color: rgb(185, 185, 185)");
				else
						checkPane.setStyle("-fx-background-color: rgb(95, 185, 95)");
		}

		public void updateCheckButton() {
				// If completed, set color to green.
				if (this.completed)
						checkPane.setStyle("-fx-background-color: rgb(95, 185, 95)");
				else
						checkPane.setStyle("-fx-background-color: rgb(185, 185, 185)");
		}

		public void pressCheckButton() {
				// Flip item.completed property.
				this.completed = !this.completed;
				// Update the item at index 'itemIndex' from list at index 'listIndex' in App.lists().
				App.lists().get(this.listIndex()).getItem(this.itemIndex()).completed(this.completed);
				// If the item's new compeleted state doesn't pass the tab's item filter, remove it.
				if ((this.completed && !tab.showCompleted) || (!this.completed && !tab.showIncomplete)) {
						this.tab.body.getChildren().remove(this.root);
						return;
				}
				// Otherwise, update the button's appearance.
				this.updateCheckButton();
		}

		public void highlightDelButton() {
				delPane.setStyle("-fx-background-color: rgb(185, 95, 95)");
		}
		public void unhighlightDelButton() {
				delPane.setStyle("-fx-background-color: rgb(185, 185, 185)");
		}

		public void pressDelButton() {
				App.gui.deleteItem(this.tab, this.listIndex(), this.itemIndex());
		}

		public void changeDescription(ObservableValue<? extends Boolean> arg0, Boolean oldPropertyValue, Boolean newPropertyValue) {
				// If newPropertyValue is false, the description has left focus, so update changes made.
				if (!newPropertyValue) {
						String text = description.getText().trim();
						text = text.substring(0, Math.min(text.length(), 256));
						description.setText(text);
						// Update the corresponding item's description.
						App.lists().get(this.listIndex()).getItems().get(this.itemIndex()).description(description.getText());
				}
		}

		public void changeDue(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
				App.lists().get(this.listIndex()).getItems().get(this.itemIndex()).due(new DueDate(newValue));
		}
}
