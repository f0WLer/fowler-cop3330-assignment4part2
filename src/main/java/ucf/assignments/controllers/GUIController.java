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

public class GUIController {

/* ---------- FXML Fields ---------- */
@FXML
private AnchorPane appBody;

/* ---------- Initializer ---------- */
public void initialize() throws IOException {
    // Load the Menu Bar from FXML.
    FXMLLoader fxml = getFXML("views/menuBar");
    MenuBar menuBar = fxml.load();
    assert menuBar != null : "Menu Bar failed to load.";
    // Assign to the GUI.
    App.gui.menuBar = fxml.getController();

    // Load the Workspace View from FXML.
    fxml = getFXML("views/workspaceViewer");
    TreeView<String> workspaceView = fxml.load();
    assert workspaceView != null : "Workspace View failed to load.";
    // Initialize the WorkspaceView class and assign to the GUI.
    App.gui.workspaceView = new WorkspaceView(workspaceView);


    // Load the List Editor from FXML.
    fxml = getFXML("views/listEditor");
    TabPane listEditor = fxml.load();
    assert listEditor != null : "Menu Bar failed to load.";
    // Initialize the ListEditor class and assign to the GUI.
    App.gui.listEditor = new ListEditor(listEditor);

    // Create a separator to go between the Workspace View and List Editor.
    Separator s = new Separator(Orientation.VERTICAL);
    s.setLayoutX(259);
    AnchorPane.setTopAnchor(s, 744.0);
    AnchorPane.setBottomAnchor(s, 256.0);

    // Add the menu bar to the App's root element.
    ((VBox)appBody.getParent()).getChildren().add(0, menuBar);

    // Add all the elements to the GUI's body.
    appBody.getChildren().addAll(listEditor, s, workspaceView);
}

}
