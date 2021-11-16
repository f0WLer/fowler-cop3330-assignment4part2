/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Alex Fowler
 */
package ucf.assignments.todo;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class List {
		private String title = "New List";
		private ArrayList<Item> items = new ArrayList<>();

		public List() {
		}

		public List(JSONObject json) {
				this.title(json.getString("title"));
				JSONArray items = json.getJSONArray("items");
				for (int i = 0; i < items.length(); i++)
						this.addItem(new Item(items.getJSONObject(i)));
		}

		public List(String title, ArrayList<Item> items) {
				this.title(title);
				this.setItems(items);
		}

		// Getters and Setters
		public String title() {
				return this.title;
		}

		public void title(String newTitle) {
				this.title = newTitle;
		}

		// Item Manipulation
		public void addItem(Item newItem) {
				this.items.add(newItem);
		}

		public Item getItem(int itemIndex) {
				return this.items.get(itemIndex);
		}

		public ArrayList<Item> getItems() {
				return this.items;
		}

		public void setItems(ArrayList<Item> items) {
				this.items = items;
		}


		// Item Filtering
		public ArrayList<Item> getCompletedItems() {
				// Create ArrayList to store completed items.
				ArrayList<Item> t = new ArrayList<>();

				// Put all completed items in the ArrayList.
				for (Item i : this.items)
						if (i.completed())
								t.add(i);

				return t;
		}

		public ArrayList<Item> getIncompleteItems() {
				// Create ArrayList to store incomplete items.
				ArrayList<Item> t = new ArrayList<>();

				// Put all completed items incomplete the ArrayList.
				for (Item i : this.items)
						if (!i.completed())
								t.add(i);

				return t;
		}

		public String getSaveData() {
				return this.toString().replace("\n", "").replace("\t", "");
		}

		public String toString() {
				int indent = 1;
				ArrayList<String> l = new ArrayList<>();
				l.add("{");
				l.add("\"title\":\"" + this.title + "\",");
				if (this.items.size() > 0) {
						l.add("\"items\":[");
						for (int i = 0; i < this.items.size(); i++) {
								Item item = this.getItem(i);
								String[] iData = item.toString().split("\n");
								for (int j = 0; j < iData.length; j++) {
										String ss = iData[j];
										if (j == iData.length - 1 && i != this.items.size() - 1)
												ss += ",";

										l.add("\t".repeat(indent) + ss);
								}
						}
						l.add("\t]");
				} else {
						l.add("\"items\":[]");
				}
				l.add("}");

				StringBuilder sb = new StringBuilder();
				for (String s : l)
						sb.append(s).append('\n');

				return sb.toString();
		}
}
