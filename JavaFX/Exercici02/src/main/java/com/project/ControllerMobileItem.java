package com.project;

import java.net.URL;
import java.util.Objects;

import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Circle;

public class ControllerMobileItem extends Controller {

    @FXML
    private Label windowTitle, itemDescription;
    @FXML
    private Pane windowBackground;
    @FXML
    private ImageView imgArrowBack, itemImage;
    @FXML
    private Circle itemCircle;

    @Override
    public void initialize() {

        try {

            if (Main.currentObject == -1)
                return;

            URL urlImage = getClass().getResource("/assets/images/arrow-back.png");
            Image image = new Image(urlImage.toExternalForm());
            imgArrowBack.setImage(image);

            setFXML();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private String consoleDescription(JSONObject element) {

        String description = "La consola " + element.getString("name") + " va sortir al mercat el "
                + element.getString("date") + ". Compta amb un processador " + element.getString("name")
                + ". Va vendre un total de "
                + element.getInt("units_sold") + " unitats.";

        return description;
    }

    private String videogamesDescription(JSONObject element) {

        String description = "El videojoc " + element.getString("name") + " va sortir al mercat l'any "
                + element.getInt("year") + ". És un joc de tipus " + element.getString("type")
                + ". El seu argument és: " + element.getString("plot") + ".";

        return description;
    }

    private String charactersDescription(JSONObject element) {

        String description = "El personatge " + element.getString("name") + " surt al videojoc "
                + element.getString("game") + ".";

        return description;
    }

    private void setFXML() throws Exception {
        JSONObject element = Main.currentObjects.get(Main.currentObject);

        String name = element.getString("name");
        windowTitle.setText(name);

        try {
            Image image = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/assets/images/" + element.getString("image").toLowerCase())));
            itemImage.setImage(image);
        } catch (NullPointerException e) {
            System.err.println(
                    "Error loading image asset: " + "/assets/images/" + element.getString("image").toLowerCase());
            e.printStackTrace();
        }

        if (Main.currentTheme == "Consoles") {
            itemDescription.setText(consoleDescription(element));
            windowBackground.setStyle("-fx-background-color: #dd1212ff;");
            windowTitle.setStyle("-fx-text-fill: #ffffff");
        } else if (Main.currentTheme == "Videogames") {
            itemDescription.setText(videogamesDescription(element));
            windowBackground.setStyle("-fx-background-color: #4812ddff;");
            windowTitle.setStyle("-fx-text-fill: #ffffff");
        } else if (Main.currentTheme == "Characters") {
            itemDescription.setText(charactersDescription(element));
            windowBackground.setStyle("-fx-background-color: #12dd45ff;");
            windowTitle.setStyle("-fx-text-fill: #000000");
        }

        if (Main.currentTheme == "Videogame")
            return;

        String color = element.getString("color");
        itemCircle.setStyle("-fx-fill: " + color + ";");
    }

    @FXML
    private void toViewItems() {
        UtilsViews.setView("MobileItems");
    }
}