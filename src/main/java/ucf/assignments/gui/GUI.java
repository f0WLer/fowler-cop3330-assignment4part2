package ucf.assignments.gui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.json.JSONArray;
import org.json.JSONObject;
import ucf.assignments.App;
import ucf.assignments.controllers.GUIController;
import ucf.assignments.controllers.MenuBarController;
import ucf.assignments.controllers.TabController;
import ucf.assignments.controllers.NewListPromptController;
import ucf.assignments.todo.Item;
import ucf.assignments.todo.List;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import static ucf.assignments.gui.Util.getFXML;

public class GUI extends Application {
		public Stage stage;
		public Scene scene;
		public GUIController controller;

		public WorkspaceView workspaceView;
		public ListEditor listEditor;
		public MenuBarController menuBar;

		public GUI() {}

		// Launches JavaFX Application.
		public void launch() {
				Application.launch(GUI.class);
		}

		@Override
		public void start(Stage stage) throws Exception {
				System.out.println("Application Started");
				Thread.setDefaultUncaughtExceptionHandler(App::spitError);

				this.stage = stage;
				FXMLLoader fxml = getFXML("views/AppGUI");
				this.stage.setScene(new Scene(fxml.load()));
				this.scene = this.stage.getScene();
				this.controller = fxml.getController();

				stage.show();
		}


		public void newList(List list, boolean fromFile) throws IOException {
				// Add to the App lists.
				App.lists().add(list);
				int listIndex = App.lists().size() - 1;
				// Open in the List Editor.
				listEditor.newTab(list, listIndex, fromFile);
				// Add to the Workspace View.
				workspaceView.addList(list, listIndex);
		}

		public void newItem(Item item) throws IOException {
				TabController tab = this.listEditor.getCurrentTab();
				if (tab == null) { return; }
				// Get the list index of the current tab.
				// Add the item to the list.
				List list = App.lists().get(tab.listIndex());
				list.addItem(item);
				int itemIndex = list.getItems().size()-1;
				// Add the new card to the list.
				if (tab.showIncomplete)
						tab.newCard(itemIndex);
				// Add the item to its corresponding branch in the Workspace View.
				WorkspaceView.Branch branchMap = this.workspaceView.findBranch(tab.listIndex());
				if (branchMap == null) { return; }
				this.workspaceView.addItem(branchMap, item, itemIndex);
		}

		public void deleteItem(TabController tab, int listIndex, int itemIndex) {
				// Remove the corresp. item at index 'itemIndex' from list at index 'listIndex' in App.lists().
				App.lists().get(listIndex).getItems().remove(itemIndex);
				// Remove the card from the tab.
				tab.body.getChildren().remove(itemIndex);
				// Update all succeeding cards.
				for (int i = itemIndex; i < tab.body.getChildren().size(); i++) {
						// Move the card up one.
						Pane card = (Pane)tab.body.getChildren().get(i);
						Bounds bounds = card.getBoundsInParent();
						card.setLayoutY(bounds.getMinY() - card.getHeight() + card.getInsets().getTop());
						// Update the card's 'itemIndex'.
						card.getProperties().put("itemIndex", (int)card.getProperties().get("itemIndex") - 1);
				}
				// Update the Workspace View
				// Find the respective branchMap
				WorkspaceView.Branch branchMap = this.workspaceView.findBranch(listIndex);
				this.workspaceView.removeItem(branchMap, listIndex, itemIndex);
				System.out.println(App.lists().get(0).toString());
		}

		public void promptForTitle(boolean newList) throws IOException {
				// Get the current tab.
				TabController tab = this.listEditor.getCurrentTab();
				if (tab == null)
						return;
				int listIndex = tab.listIndex();

				// Create pop-up window.
				final Stage popUp = new Stage();
				popUp.initModality(Modality.APPLICATION_MODAL);
				popUp.initOwner(this.stage);
				// Load FXML
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

		public void setListTitle(int listIndex, String newTitle) {
				// Change list title.
				App.lists().get(listIndex).title(newTitle);
				// Update in List Editor.
				this.listEditor.renameTab(listIndex, newTitle);
				// Update in WorkspaceView.
				this.workspaceView.renameList(listIndex, newTitle);
		}

		public void clearItems() {
				// Get the current tab/list.
				TabController tab = this.listEditor.getCurrentTab();
				int listIndex = tab.listIndex();
				List list = App.lists().get(listIndex);

				// Update Workspace View.
				// Get corresponding branch map.
				WorkspaceView.Branch branchMap = this.workspaceView.findBranch(listIndex);
				// Remove each twig from the branch.
				int listSize = list.getItems().size();
				for (int i = 0; i < listSize; i++)
					this.workspaceView.removeItem(branchMap, listIndex, 0);

				// Update List Editor.
				tab.clearCards();

				// Set the list's Item ArrayList to a new blank one.
				list.setItems(new ArrayList<>());
		}

		public void toggleItemFilter(boolean completed) throws IOException {
				// Get the current tab/list
				TabController tab = this.listEditor.getCurrentTab();
				List list = App.lists().get(tab.listIndex());

				if (completed)
						tab.showCompleted = !tab.showCompleted;
				else
						tab.showIncomplete = !tab.showIncomplete;

				// Clear the tab.
				tab.clearCards();

				// Repopulate the tab.
				for (int i = 0; i < list.getItems().size(); i++) {
						Item item = list.getItem(i);
						if ((item.completed() && tab.showCompleted) || (!item.completed() && tab.showIncomplete))
								tab.newCard(i);
				}
		}

		public void resetGUI() {
				// Clear all lists.
				App.lists().clear();
				// Reset the Workspace Viewer.
				this.workspaceView.reset();
				// Reset the List Editor.
				this.listEditor.reset();
		}

		public void goToList(int branchIndex) throws IOException {
				int listIndex = this.workspaceView.getListIndex(branchIndex);
				this.listEditor.goToTab(listIndex);
		}

		public void saveList() throws IOException {
				// Get current tab/list.
				TabController tab = this.listEditor.getCurrentTab();
				if (tab == null) { return; }
				List list = App.lists().get(tab.listIndex());
				FileChooser fc = new FileChooser();
				fc.setTitle("Save as...");
				fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("Todo List File", "*.todo"));
				// Get file.
				File file = fc.showSaveDialog(App.gui.stage);
				if (file == null) { return; }
				FileWriter writer = new FileWriter(file);
				JSONArray lists = new JSONArray();
				lists.put(new JSONObject(list.getSaveData()));
				writer.write(new JSONObject().put("lists", lists).toString());
				writer.close();
		}

		public void deleteList() {
				// Get current tab/list.
				TabController tab = this.listEditor.getCurrentTab();
				if (tab == null) { return; }
				int listIndex = tab.listIndex();
				// Remove from Workspace View.
				this.workspaceView.removeList(listIndex);
				// Remove from List Editor
				int tabIndex = this.listEditor.getTabPane().getSelectionModel().getSelectedIndex();
				this.listEditor.getTabPane().getTabs().remove(tabIndex);
				this.listEditor.removeTab(listIndex);
				// Remove from App.lists()
				App.lists().remove(listIndex);
		}
}
