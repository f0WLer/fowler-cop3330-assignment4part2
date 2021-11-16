/*
 *  UCF COP3330 Fall 2021 Assignment 4 Solution
 *  Copyright 2021 Alex Fowler
 */
package ucf.assignments;

import ucf.assignments.data.AppMemory;
import ucf.assignments.gui.GUI;
import ucf.assignments.todo.List;

import java.util.ArrayList;

public class App {
    // Memory handler class.
    public static AppMemory mem;
    // GUI handler class.
    public static GUI gui;

    public static void main(String[] args) {
        // Initialize App memory class.
        App.mem = new AppMemory();
        // Launch GUI.
        App.gui = new GUI();
        App.gui.launch();
    }



    /* ---------- Auxiliary ---------- */
    // Application Exception Handler.
    public static void spitError(Thread t, Throwable e) {
        System.out.println("ERROR: " + e.getLocalizedMessage());
    }

}
