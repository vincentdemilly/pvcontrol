package sample;

import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;

import java.net.URL;

/**
 * Created by vincentdemilly on 18/07/2017.
 */
class MyListCell extends ListCell<String> {
    protected void updateItem(String item, boolean empty) {
        super.updateItem(item, empty);
        setGraphic(null);
        setText(null);
        if (item != null) {
            ImageView imageView = new ImageView(new Image(item + ".png"));
            imageView.setFitWidth(25);
            imageView.setFitHeight(25);
            setGraphic(imageView);
        }
    }
}