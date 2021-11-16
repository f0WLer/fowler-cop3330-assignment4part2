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

		private final ArrayList<Branch> treeMap = new ArrayList<>();

		/* ---------- Constructor ---------- */
		public WorkspaceView(TreeView<String> node) {
				// Set TreeView and root property fields.
				this.tree = node;
				this.root = tree.getRoot();

				// Set mouse click handler
				tree.setOnMouseClicked((MouseEvent mouseEvent) -> {
						try { this.onMouseClicked(mouseEvent); } catch (IOException ignored) {}
				});
		}

		/* ---------- Tree View Manipulation ---------- */

		public void addList(List list, int listIndex) {
				// Create Branch
				TreeItem<String> branch = new TreeItem<>(list.title());
				// Create Branch Map
				Branch branchMap = new Branch();
				branchMap.id = listIndex;

				// For item in list
				for (int i = 0; i < list.getItems().size(); i++) {
						Item item = list.getItem(i);

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

		public void addItem(Branch branchMap, Item item, int itemIndex) {
				branchMap.nodes.add(itemIndex, itemIndex);
				// Get the branch from the root branch
				TreeItem<String> branch = root.getChildren().get(this.treeMap.indexOf(branchMap));
				// Create new twig.
				TreeItem<String> twig = new TreeItem<>(item.description());
				branch.getChildren().add(itemIndex, twig);
		}

		public void removeList(int listIndex) {
				for (int i = 0; i < this.treeMap.size(); i++) {
						if (this.treeMap.get(i).id == listIndex) {
								this.root.getChildren().remove(i);
								break;
						}
				}
		}

		public void removeItem(Branch branchMap, int listIndex, int itemIndex) {
				// Remove the node from the map.
				branchMap.nodes.remove(itemIndex);
				// Remove the twig from the branch.
				this.root.getChildren().get(this.treeMap.indexOf(branchMap)).getChildren().remove(itemIndex);
		}

		public void renameList(int listIndex, String newTitle) {
				Branch branch = findBranch(listIndex);
				if (branch == null) { return; }
				this.root.getChildren().get(treeMap.indexOf(branch)).setValue(newTitle);
		}

		/* ---------- Auxiliary ---------- */

		public Branch findBranch(int listIndex) {
				// Search for the branch in the treeMap and return.
				for (Branch b : this.treeMap) {
						if (b.id == listIndex)
								return b;
				}
				// Not found, return null.
				return null;
		}

		public void reset() {
				this.treeMap.clear();
				this.root.getChildren().clear();
		}

		private void onMouseClicked(MouseEvent mouseEvent) throws IOException {
				Node node = mouseEvent.getPickResult().getIntersectedNode();
				if (node instanceof Text && mouseEvent.getButton() == MouseButton.SECONDARY) {
						// Open that list
						TreeItem<String> branch = tree.selectionModelProperty().get().getSelectedItem();
						// If clicked on a twig, don't try and open it.
						if (branch.getParent() != root)
								return;
						for (int i = 0; i < root.getChildren().size(); i++) {
								if (root.getChildren().get(i).equals(branch))
										App.gui.goToList(i);
						}
				}
		}

		public int getListIndex(int branchIndex) {
				return this.treeMap.get(branchIndex).id;
		}

		/* ---------- Helper Class ---------- */
		public static class Branch {
				int id;
				ArrayList<Integer> nodes = new ArrayList<>();
		}
}
