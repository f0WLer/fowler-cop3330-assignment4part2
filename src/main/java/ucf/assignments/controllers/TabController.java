package ucf.assignments.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import ucf.assignments.todo.List;

import java.io.IOException;

import static ucf.assignments.gui.Util.getFXML;

public class TabController {
		@FXML
		public VBox body;
		public Tab root;

		public boolean showCompleted = true;
		public boolean showIncomplete = true;

		// listIndex property getter.
		public int listIndex() {
				return (int)root.getProperties().get("listIndex");
		}

		public void init(List list, int listIndex) throws IOException {
				// listIndex is index of this tab in App.lists().
				this.root.getProperties().put("listIndex", listIndex);

				// Set tab's title.
				this.root.setText(list.title());

				if (list.getItems().size() == 0)
						return;

				// For item in list:
				for (int i = 0; i < list.getItems().size(); i++) {
						// Create and add a new item card
						newCard(i);
				}
		}

		public void newCard(int itemIndex) throws IOException {
				FXMLLoader cardLoader = getFXML("views/itemCard");
				Pane card = cardLoader.load();
				((ItemCardController)cardLoader.getController()).init(this, body, this.listIndex(), itemIndex);

				body.getChildren().add(card);
		}

		public void clearCards() {
				body.getChildren().clear();
		}
}

