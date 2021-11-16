package ucf.assignments.gui;

import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import ucf.assignments.App;
import ucf.assignments.controllers.TabController;
import ucf.assignments.todo.List;

import java.io.IOException;
import java.util.ArrayList;

import static ucf.assignments.gui.Util.getFXML;

public class ListEditor {
		private final TabPane pane;
		private ArrayList<TabController> tabs = new ArrayList<>();

		/* ---------- Constructor ---------- */
		public ListEditor(TabPane node) {
				this.pane = node;
				this.pane.getSelectionModel().selectedItemProperty().addListener(this::updateMenuBar);
		}

		/* ---------- Getters ---------- */
		public TabPane getTabPane() { return this.pane; }

		private TabController findTab(int listIndex) {
				for (TabController t : this.tabs)
						if (t.listIndex() == listIndex)
								return t;
				return null;
		}

		/* ---------- TabPane Manipulation ---------- */

		public void newTab(List list, int listIndex, boolean fromFile) throws IOException {
				// Load new tab from FXML.
				FXMLLoader fxml = getFXML("views/tab");
				Tab tab = fxml.load();
				tab.setOnClosed((Event event) -> { this.tabs.remove(listIndex); });
				// Initialize Tab.
				TabController controller = fxml.getController();
				controller.init(list, listIndex);
				this.tabs.add(controller);
				// Add Tab to editor.
				this.pane.getTabs().add(tab);
				this.pane.getSelectionModel().select(this.pane.getTabs().size() - 1);
				// Prompt user for tab (list) title if it's new.
				if (!fromFile)
					App.gui.promptForTitle(true);
		}

		public void removeTab(int listIndex) {
				this.tabs.remove(listIndex);
		}

		public void renameTab(int listIndex, String newTitle) {
				TabController tab = this.findTab(listIndex);
				if (tab == null) { return; }
				tab.root.setText(newTitle);
		}

		public void goToTab(int listIndex) throws IOException {
				TabController tab = this.findTab(listIndex);
				if (tab == null) {
						this.newTab(App.lists().get(listIndex), listIndex, true);
				} else {
						int tabIndex = this.getTabPane().getTabs().indexOf(tab.root);
						this.getTabPane().getSelectionModel().select(tabIndex);
				}
		}

		public void reset() {
				this.tabs.clear();
				this.pane.getTabs().clear();
		}

		/* ---------- Auxiliary ---------- */

		public TabController getCurrentTab() {
				int tabIndex = this.pane.getSelectionModel().getSelectedIndex();
				if (tabIndex == -1)
						return null;
				return this.tabs.get(tabIndex);
		}

		private void updateMenuBar(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {
				if (newTab == null) {
						App.gui.menuBar.toggleListItems(false);
						return;
				}
				TabController tab = this.tabs.get(this.pane.getTabs().indexOf(newTab));

				App.gui.menuBar.toggleListItems(true);
				App.gui.menuBar.completedCheck.setSelected(tab.showCompleted);
				App.gui.menuBar.incompleteCheck.setSelected(tab.showIncomplete);
		}
}
