package ucf.assignments.gui;

import javafx.fxml.FXMLLoader;

import java.util.Objects;

public class Auxiliary {
// Returns an FXMLLoader at the specified path in resources.
public static FXMLLoader getFXML(String path) {
    return new FXMLLoader(Objects.requireNonNull(Auxiliary.class.getResource("/" + path + ".fxml")));
}
}
