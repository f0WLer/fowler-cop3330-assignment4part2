/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Alex Fowler
 */
package ucf.assignments.todo;

import org.json.JSONObject;

import java.time.LocalDate;

public class DueDate {
/* ---------- Fields ---------- */
private int year, month, day;

/* ---------- Constructors ---------- */
// Default due -- One getMonth ahead.
public DueDate() {
    LocalDate defaultDue = LocalDate.now().plusMonths(1);
    this.setYear(defaultDue.getYear());
    this.setMonth(defaultDue.getMonthValue());
    this.setDay(defaultDue.getDayOfMonth());
}
// Used when reading a saved todo list.
public DueDate(JSONObject json) {
    this.setYear(json.getInt("year"));
    this.setMonth(json.getInt("month"));
    this.setDay(json.getInt("day"));
}
// Used when setting a new getDue date in the GUI.
public DueDate(LocalDate date) {
    this.setYear(date.getYear());
    this.setMonth(date.getMonthValue());
    this.setDay(date.getDayOfMonth());
}

/* ---------- Getters/Setters ---------- */
public int getYear() { return this.year; }
public void setYear(int newYear) { this.year = newYear; }
public int getMonth() { return this.month; }
public void setMonth(int newMonth) { this.month = newMonth; }
public int getDay() { return this.day; }
public void setDay(int newDay) { this.day = newDay; }

/* ---------- Auxiliary ---------- */
public LocalDate toLocalDate() { return LocalDate.of(this.year, this.month, this.day); }
}
