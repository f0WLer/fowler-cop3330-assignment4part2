/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Alex Fowler
 */
package ucf.assignments;

import ucf.assignments.data.AppMemory;
import ucf.assignments.gui.AppGUI;

import java.util.Arrays;

public class App {
// Memory handler class.
public static AppMemory mem;
// GUI handler class.
public static AppGUI gui;

public static void main(String[] args) {
    // Initialize App memory class.
    App.mem = new AppMemory();
    // Launch GUI.
    App.gui = new AppGUI();
    App.gui.launch();
}

/* ---------- Auxiliary ---------- */

// Application Exception Handler.
public static void spitError(Thread t, Throwable e) {
    System.out.println("ERROR: " + e.getLocalizedMessage() + "\n" + Arrays.toString(e.getStackTrace()));
}

}
