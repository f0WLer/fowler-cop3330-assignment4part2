package ucf.assignments.gui;

import javafx.scene.Node;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import ucf.assignments.App;
import ucf.assignments.todo.Item;
import ucf.assignments.todo.List;

import java.io.IOException;
import java.util.ArrayList;

public class WorkspaceView {
private final TreeView<String> tree;
private final TreeItem<String> root;

private final ArrayList<branchMap> treeMap = new ArrayList<>();

/* ---------- Constructor ---------- */
public WorkspaceView(TreeView<String> node) {
    // Set TreeView and root property fields.
    this.tree = node;
    this.root = tree.getRoot();

    // Set mouse click handler
    tree.setOnMouseClicked((MouseEvent mouseEvent) -> {
        try{this.onMouseClicked(mouseEvent);}catch (IOException ignored){}
    });
}

/* ---------- Tree View Manipulation ---------- */

public void addList(List list, int listIndex) {
    // Create Branch
    TreeItem<String> branch = new TreeItem<>(list.title());
    // Create Branch Map
    branchMap branchMap = new branchMap();
    branchMap.id = listIndex;

    // For item in list
    for (int i = 0; i < list.getItems().size(); i++) {
        Item item = list.get(i);

        // Create Twig
        TreeItem<String> twig = new TreeItem<>(item.description());
        // Add Twig to Branch
        branch.getChildren().add(twig);
        // Add index to Branch Map
        branchMap.nodes.add(i);
    }

    // Add Branch to Tree
    this.root.getChildren().add(branch);
    // Add index to tree map
    this.treeMap.add(branchMap);
    // Set tree map back
}

public void addItem(int listIndex, Item item) {
    // Get the list's corresponding tree branchMap.
    int branchIndex = this.getBranchIndex(listIndex);
    assert branchIndex != -1 : "branch not found";
    branchMap bMap = this.treeMap.get(branchIndex);
    // Get the itemIndex -- the list's last item.
    int itemIndex = App.mem.get(listIndex).size() - 1;
    // Add the item to the branchMap.
    bMap.nodes.add(itemIndex, itemIndex);

    // Get the branch from the root branch.
    TreeItem<String> branch = this.getBranch(branchIndex);
    // Create new twig.
    TreeItem<String> twig = new TreeItem<>(item.description());
    assert branch != null : "null branch";
    branch.getChildren().add(itemIndex, twig);
}

public TreeItem<String> getBranch(int branchIndex) {
    if ( this.root.getChildren().size() > branchIndex )
        return this.root.getChildren().get(branchIndex);
    return null;
}

public void removeList(int listIndex) {
    for (int i = 0; i < this.treeMap.size(); i++) {
        if ( this.treeMap.get(i).id == listIndex ) {
            this.root.getChildren().remove(i);
            break;
        }
    }
}

public void removeItem(int listIndex, int itemIndex) {
    // Get branchIndex.
    int branchIndex = this.getBranchIndex(listIndex);
    // Get branchMap.
    branchMap bMap = this.treeMap.get(branchIndex);
    // Find the item in the branchMap's nodes and remove.
    int nodeIndex = -1;
    for (int i = 0; i < bMap.nodes.size(); i++)
        if ( bMap.nodes.get(i) == itemIndex ) {
            nodeIndex = i;
            bMap.nodes.remove(i);
            break;
        }
    assert nodeIndex != -1 : "Branch node not found";
    // Get the branch from the root.
    TreeItem<String> branch = root.getChildren().get(branchIndex);
    // Remove the node from the branch.
    branch.getChildren().remove(nodeIndex);
}

public void renameList(int listIndex, String newTitle) {
    int branchIndex = getBranchIndex(listIndex);
    assert branchIndex != -1 : "Branch not found";
    this.getBranch(branchIndex).setValue(newTitle);
}

/* ---------- Auxiliary ---------- */

public int getBranchIndex(int listIndex) {
    // Search for the branch in the treeMap and return.
    for (int i = 0; i < this.treeMap.size(); i++) {
        branchMap b = this.treeMap.get(i);
        if ( b.id == listIndex )
            return i;
    }
    // Not found, return -1.
    return -1;
}

public void reset() {
    this.treeMap.clear();
    this.root.getChildren().clear();
}

private void onMouseClicked(MouseEvent mouseEvent) throws IOException {
    Node node = mouseEvent.getPickResult().getIntersectedNode();
    if ( node instanceof Text && mouseEvent.getButton() == MouseButton.SECONDARY ) {
        // Open that list
        TreeItem<String> branch = tree.selectionModelProperty().get().getSelectedItem();
        // If clicked on a twig, don't try and open it.
        if ( branch.getParent() != root )
            return;
        for (int i = 0; i < root.getChildren().size(); i++) {
            if ( root.getChildren().get(i).equals(branch) )
                App.gui.goToList(i);
        }
    }
}

public int getListIndex(int branchIndex) {return this.treeMap.get(branchIndex).id;}

public void clearList(int listIndex) {
	// Get branch index.
	int branchIndex = this.getBranchIndex(listIndex);
	// Remove all nodes from branch.
	this.root.getChildren().get(branchIndex).getChildren().clear();
	// Remove all nodes from branchMap.
	this.treeMap.get(branchIndex).nodes.clear();
}

/* ---------- Helper Class ---------- */
public static class branchMap {
    int id;
    ArrayList<Integer> nodes = new ArrayList<>();
}
}
