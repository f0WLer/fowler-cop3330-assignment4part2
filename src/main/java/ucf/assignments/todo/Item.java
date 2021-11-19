/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Alex Fowler
 */
package ucf.assignments.todo;

import org.json.JSONObject;

public class Item {

/* ---------- Fields ---------- */
private String description = "New Item";
private DueDate due = new DueDate();
private Boolean completed = false;

/* ---------- Constructors ---------- */
// Default new Item.
public Item() { }
// Populate fields from JSON Object -- Used in file reading.
public Item(JSONObject json) {
    this.description = json.getString("description");
    this.due = new DueDate(json.getJSONObject("due"));
    this.completed = json.getBoolean("completed");
}

/* ---------- Getters/Setters ---------- */
public String getDescription() { return this.description; }
public void setDescription(String newDesc) { this.description = newDesc; }
public DueDate getDue() { return this.due; }
public void setDue(DueDate newDate) { this.due = newDate; }
public Boolean getCompleted() { return this.completed; }
public void setCompleted(Boolean newState) { this.completed = newState; }

/* ---------- File Saving ---------- */
public String toString() { return new JSONObject(this).toString(); }
}
