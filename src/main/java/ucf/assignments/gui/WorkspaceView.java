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

/* ---------- FXML Elements ---------- */
private final TreeView<String> tree;
private final TreeItem<String> root;

/* ---------- Fields ---------- */
// Map of TreeView branches and their corresponding lists and items.
private final ArrayList<branchMap> treeMap = new ArrayList<>();

/* ---------- Constructor ---------- */
public WorkspaceView(TreeView<String> node) {
    // Set TreeView and root property fields.
    this.tree = node;
    this.root = tree.getRoot();

    // Set mouse click handler
    tree.setOnMouseClicked((MouseEvent mouseEvent) -> {
        try{ this.onMouseClicked(mouseEvent); }catch (IOException ignored){ }
    });
}

/* ---------- Tree View Manipulation ---------- */
//  Pre-condition:  list is a new list at index listIndex in memory.
//  Post-condition: Adds the list and its items to the tree.
public void addList(List list, int listIndex) {
    // Create Branch
    TreeItem<String> branch = new TreeItem<>(list.getTitle());
    // Create Branch Map
    branchMap branchMap = new branchMap();
    branchMap.listIndex = listIndex;
    // For item in list
    for (int i = 0; i < list.getItems().size(); i++) {
        Item item = list.get(i);

        // Create Twig
        TreeItem<String> twig = new TreeItem<>(item.getDescription());
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

//  Pre-condition:  listIndex is the index of an item in memory.
//                  item is a new item.
//  Post-condition: Adds the item to the list's corresponding tree branch.
public void addItem(int listIndex, Item item) {
    // Get the list's corresponding tree branchMap.
    int branchIndex = this.getBranchIndex(listIndex);
    assert branchIndex != -1 : "branch not found";
    branchMap bMap = this.treeMap.get(branchIndex);
    // Get the itemIndex -- the list's last item.
    int itemIndex = App.mem.getList(listIndex).size() - 1;
    // Add the item to the branchMap.
    bMap.nodes.add(itemIndex, itemIndex);

    // Get the branch from the root branch.
    TreeItem<String> branch = this.getBranch(branchIndex);
    // Create new twig.
    TreeItem<String> twig = new TreeItem<>(item.getDescription());
    assert branch != null : "null branch";
    branch.getChildren().add(itemIndex, twig);
}

//  Pre-condition:  listIndex is a valid index to a list in memory.
//  Post-condition: Removes the list's corresponding branch from the tree.
public void removeList(int listIndex) {
    for (int i = 0; i < this.treeMap.size(); i++) {
        if ( this.treeMap.get(i).listIndex == listIndex ) {
            this.root.getChildren().remove(i);
            break;
        }
    }
}

//  Pre-condition:  listIndex and itemIndex are valid indexes
//                  to a branch and node in the tree.
//  Post-condition: Removes the item's node from the list's branch in the tree.
public void removeItem(int listIndex, int itemIndex) {
    // Get branchIndex.
    int branchIndex = this.getBranchIndex(listIndex);
    // Get branchMap.
    branchMap bMap = this.treeMap.get(branchIndex);
    // Find the item in the branchMap's nodes and removeList.
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
    // Update all succeeding nodes in the branchMap.
    for (int i = 0; i < bMap.nodes.size(); i++)
        if ( bMap.nodes.get(i) >= nodeIndex )
            bMap.nodes.set(i, bMap.nodes.get(i) - 1);
}

//  Pre-condition:  listIndex is a valid index to a list's branch
//                  in the tree view.
//  Post-condition: Clear's the list's branch of all its nodes.
public void clearList(int listIndex) {
    // Get branch index.
    int branchIndex = this.getBranchIndex(listIndex);
    // Remove all nodes from branch.
    this.root.getChildren().get(branchIndex).getChildren().clear();
    // Remove all nodes from branchMap.
    this.treeMap.get(branchIndex).nodes.clear();
}

//  Post-condition: Removes all branches from the tree.
public void clear() {
    this.treeMap.clear();
    this.root.getChildren().clear();
}

//  Pre-condition:  listIndex is a valid index to a branch in the tree.
//                  newTitle is the new getTitle of the list.
//  Post-condition: Renames the list's corresponding branch in the tree.
public void renameList(int listIndex, String newTitle) {
    int branchIndex = getBranchIndex(listIndex);
    assert branchIndex != -1 : "Branch not found";
    this.getBranch(branchIndex).setValue(newTitle);
}

/* ---------- Auxiliary ---------- */

//  Pre-condition:  listIndex is a valid index to list with a branch
//                  in the tree view.
//  Post-condition: Returns the index of the list's branch in the tree view.
public int getBranchIndex(int listIndex) {
    // Search for the branch in the treeMap and return.
    for (int i = 0; i < this.treeMap.size(); i++) {
        branchMap b = this.treeMap.get(i);
        if ( b.listIndex == listIndex ) return i;
    }
    // Not found, return -1.
    return -1;
}

//  Pre-condition:  branchIndex is the index of a branch in the Tree View.
//  Post-condition: Returns the branch from the tree at index branchIndex.
public TreeItem<String> getBranch(int branchIndex) {
    if ( this.root.getChildren().size() > branchIndex ) return this.root.getChildren().get(branchIndex);
    return null;
}

//  Post-condition: If a branch's getTitle is right-clicked, opens that branch
//                  in the TreeView.
private void onMouseClicked(MouseEvent mouseEvent) throws IOException {
    Node node = mouseEvent.getPickResult().getIntersectedNode();
    if ( node instanceof Text && mouseEvent.getButton() == MouseButton.SECONDARY ) {
        // Open that list
        TreeItem<String> branch = tree.selectionModelProperty().get().getSelectedItem();
        // If clicked on a twig, don't try and open it.
        if ( branch.getParent() != root ) return;
        for (int i = 0; i < root.getChildren().size(); i++) {
            if ( root.getChildren().get(i).equals(branch) ) App.gui.openBranchInTab(i);
        }
    }
}

//  Pre-condition:  branchIndex is a valid index to a branch in the tree view.
//  Post-condition: Returns the index of the branch's corresponding list.
public int getListIndex(int branchIndex) { return this.treeMap.get(branchIndex).listIndex; }

/* ---------- Helper Class ---------- */
// this.treeMap item class.
public static class branchMap {
    // The branch's corresponding list.
    public int listIndex;
    // Each index is the index of a node in the branch.
    // Its value is the corresponding item's index in the branch's list..
    public ArrayList<Integer> nodes = new ArrayList<>();
}
}
