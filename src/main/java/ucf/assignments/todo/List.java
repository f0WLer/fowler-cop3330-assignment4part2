/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Alex Fowler
 */
package ucf.assignments.todo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class List {

/* ---------- Fields ---------- */
private String title = "New List";
private ArrayList<Item> items = new ArrayList<>();

/* ---------- Constructors ---------- */
// Default List.
public List() { }
// Populate data from JSON Object -- Used in file reading.
public List(JSONObject json) {
    this.setTitle(json.getString("title"));
    JSONArray items = json.getJSONArray("items");
    for (int i = 0; i < items.length(); i++)
        this.add(new Item(items.getJSONObject(i)));
}

/* ---------- Getters/Setters ---------- */
public String getTitle() {
    return this.title;
}
public void setTitle(String newTitle) {
    this.title = newTitle;
}
public ArrayList<Item> getItems() {
    return this.items;
}
public void setItems(ArrayList<Item> newItems) { this.items = newItems; }

/* ---------- Item Manipulation ---------- */
//  Adds newItem to the list.
public void add(Item newItem) {
    this.items.add(newItem);
}

//  Removes item at itemIndex from list.
public void remove(int itemIndex) { this.items.remove(itemIndex); }

//  Returns the Item at itemIndex in the list.
public Item get(int itemIndex) {
    return this.items.get(itemIndex);
}

//  Clears the list of all Items.
public void clear() { this.items.clear(); }

//  Returns the number of Items in the list.
public int size() { return this.items.size(); }

/* ---------- File Saving ---------- */
public String toString() { return new JSONObject(this).toString(); }
}
