package ucf.assignments.gui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;

import java.util.Objects;

public class Auxiliary {
    public static FXMLLoader getFXML(String path) {
        return new FXMLLoader(Objects.requireNonNull(Auxiliary.class.getResource("/" + path + ".fxml")));
    }
    public static void setAnchors(Node node, Double t, Double r, Double b, Double l) {
        if (t != null)
            AnchorPane.setTopAnchor(node, t);
        if (r != null)
            AnchorPane.setRightAnchor(node, r);
        if (b != null)
            AnchorPane.setBottomAnchor(node, b);
        if (l != null)
            AnchorPane.setLeftAnchor(node, l);
    }
}
