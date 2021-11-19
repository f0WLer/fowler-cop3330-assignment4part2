package ucf.assignments.data;

import ucf.assignments.todo.List;

import java.util.ArrayList;

public class AppMemory {
/* ---------- Fields ---------- */
private final ArrayList<List> lists;

/* ---------- Constructor ---------- */
public AppMemory() { this.lists = new ArrayList<>(); }

/* ---------- List Manipulation ---------- */
// Returns the List at listIndex.
public List getList(int listIndex) { return this.lists.get(listIndex); }

// Returns all Lists.
public ArrayList<List> getAllLists() { return this.lists; }

// Adds list to the current lists.
public void addList(List list) { this.lists.add(list); }

// Removes the List at listIndex from the current lists.
public void removeList(int listIndex) {
    if ( listIndex < this.lists.size() ) this.lists.remove(listIndex);

}
// Returns the number of lists.
public int size() { return this.lists.size(); }

// Removes all lists.
public void clear() { this.lists.clear(); }
}
