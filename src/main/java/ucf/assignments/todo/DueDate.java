/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Alex Fowler
 */
package ucf.assignments.todo;

import org.json.JSONObject;

import java.time.LocalDate;

public class DueDate {
		private int year;
		private int month;
		private int day;

		// Used when creating a new item. Automatically sets the due date a month ahead.
		public DueDate() {
				LocalDate defaultDue = LocalDate.now().plusMonths(1);
				this.year(defaultDue.getYear());
				this.month(defaultDue.getMonthValue());
				this.day(defaultDue.getDayOfMonth());
		}

		// Used when reading a saved todo list.
		public DueDate(JSONObject json) {
				this.year(json.getInt("year"));
				this.month(json.getInt("month"));
				this.day(json.getInt("day"));
		}

		// Used when setting a new due date in the GUI.
		public DueDate(LocalDate date) {
				this.year(date.getYear());
				this.month(date.getMonthValue());
				this.day(date.getDayOfMonth());
		}

		// Getters and Setter
		public int year() {
				return this.year;
		} // Getter

		public void year(int newYear) {
				this.year = newYear;
		} // Setter

		public int month() {
				return this.month;
		} // Getter

		public void month(int newMonth) {
				this.month = newMonth;
		} // Setter

		public int day() {
				return this.day;
		} // Getter

		public void day(int newDay) {
				this.day = newDay;
		} // Setter

		// Returns a corresponding LocalDate.
		public LocalDate toLocalDate() { return LocalDate.of(this.year, this.month, this.day); }
}
