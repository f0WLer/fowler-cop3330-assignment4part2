/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Alex Fowler
 */
package ucf.assignments.todo;


import org.json.JSONObject;

import java.util.ArrayList;

public class Item {
		private String description = "New Item";
		private DueDate due = new DueDate();
		private Boolean completed = false;

		public Item() {
		}

		public Item(JSONObject json) {
				this.description = json.getString("description");
				this.due = new DueDate(json.getJSONObject("due"));
				this.completed = json.getBoolean("completed");
		}

		public Item(String description, DueDate due, Boolean completed) {
				this.description = description;
				this.due = due;
				this.completed = completed;
		}

		// Getters and Setters
		public String description() {
				return this.description;
		} // Getter

		public void description(String newDesc) {
				this.description = newDesc;
		} // Setter

		public DueDate due() {
				return this.due;
		} // Getter

		public void due(DueDate newDate) {
				this.due = newDate;
		} // setter

		public Boolean completed() {
				return this.completed;
		} // Getter

		public void completed(Boolean newState) {
				this.completed = newState;
		} /// Setter

		public String getSaveData() {
				return this.toString().replace("\n", "").replace("\t", "");
		}

		public String toString() {
				ArrayList<String> s = new ArrayList<>();
				s.add("{");
				s.add("\"description\":\"" + this.description + "\",");
				s.add("\"due\":{");
				s.add("\t\"year\":" + this.due.year() + ",");
				s.add("\t\"month\":" + this.due.month() + ",");
				s.add("\t\"day\":" + this.due.day());
				s.add("\t},");
				s.add("\"completed\":" + this.completed);
				s.add("}");

				StringBuilder sb = new StringBuilder();
				for (String l : s)
						sb.append(l).append('\n');

				return sb.toString();
		}
}
