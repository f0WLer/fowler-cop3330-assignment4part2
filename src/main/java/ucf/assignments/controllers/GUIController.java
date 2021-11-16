package ucf.assignments.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Orientation;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Separator;
import javafx.scene.control.TabPane;
import javafx.scene.control.TreeView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import ucf.assignments.App;
import ucf.assignments.gui.ListEditor;
import ucf.assignments.gui.WorkspaceView;

import java.io.IOException;

import static ucf.assignments.gui.Auxiliary.getFXML;
import static ucf.assignments.gui.Auxiliary.setAnchors;

public class GUIController {
		@FXML
		private AnchorPane appBody;

		/* ---------- Initializer ---------- */
		public void initialize() throws IOException {
				TreeView<String> workspaceView;
				TabPane listEditor;
				// Load the Menu Bar from FXML.
				FXMLLoader fxml = getFXML("views/menuBar");
				MenuBar menuBar = fxml.load();
				App.gui.menuBar = fxml.getController();

				// Load the Workspace Viewer from FXML.
				fxml = getFXML("views/workspaceViewer");
				workspaceView = fxml.load();
				// Initialize the WorkspaceView class.
				App.gui.workspaceView = new WorkspaceView(workspaceView);

				// Load the List Editor from FXML.
				fxml = getFXML("views/listEditor");
				listEditor = fxml.load();
				// Initialize the ListEditor class.
				App.gui.listEditor = new ListEditor(listEditor);

				// Create a separator to go between the two.
				Separator s = new Separator(Orientation.VERTICAL);
				s.setLayoutX(259);
				setAnchors(s, 0.0, 744.0, 0.0, 256.0);

				// Add the menu bar to the App's root element.
				((VBox)appBody.getParent()).getChildren().add(0, menuBar);

				// Add all the elements to the GUI's body.
				appBody.getChildren().addAll(listEditor, s, workspaceView);
		}

}
