package com.project;

import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.FXML;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;

public class ControllerDesktop extends Controller {

    @FXML
    private VBox desktopElementsList = new VBox();
    @FXML
    private ChoiceBox desktopChoiceBox;
    @FXML
    private ImageView itemImage;
    @FXML
    private Label itemTitle, itemDescription;

    private String[] themes = { "Consoles", "Videojocs", "Personatges" };

    private JSONArray jsonCharacters;
    private JSONArray jsonConsoles;
    private JSONArray jsonGames;

    private URL listResource;

    @Override
    public void initialize() {
        
        try {
            // Obtenir el recurs del template .fxml
            listResource = this.getClass().getResource("/assets/layouts/desktopListItem.fxml");

            // Obtenir la llista
            URL jsonFileURL = getClass().getResource("/data/characters.json");
            Path path = Paths.get(jsonFileURL.toURI());
            String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            jsonCharacters = new JSONArray(content);

            jsonFileURL = getClass().getResource("/data/consoles.json");
            path = Paths.get(jsonFileURL.toURI());
            content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            jsonConsoles = new JSONArray(content);

            jsonFileURL = getClass().getResource("/data/games.json");
            path = Paths.get(jsonFileURL.toURI());
            content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            jsonGames = new JSONArray(content);

            generateChoices();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void generateChoices() {
        desktopChoiceBox.getSelectionModel().clearSelection();
        desktopChoiceBox.getItems().addAll(themes);

        desktopChoiceBox.setOnAction(event -> {
            if (desktopChoiceBox.getSelectionModel().getSelectedItem() == "Consoles") {
                try {
                    Main.currentTheme = "Consoles";
                    setFXML(jsonConsoles);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                ;
            } else if (desktopChoiceBox.getSelectionModel().getSelectedItem() == "Videojocs") {
                try {
                    Main.currentTheme = "Videogames";
                    setFXML(jsonGames);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (desktopChoiceBox.getSelectionModel().getSelectedItem() == "Personatges") {
                try {
                    Main.currentTheme = "Characters";
                    setFXML(jsonCharacters);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if (Main.currentTheme == "Consoles")
            desktopChoiceBox.getSelectionModel().select(0);
        else if (Main.currentTheme == "Videogames")
            desktopChoiceBox.getSelectionModel().select(1);
        else if (Main.currentTheme == "Characters")
            desktopChoiceBox.getSelectionModel().select(2);

    }

    private void setFXML(JSONArray jsonInfo) throws Exception {

        // Esborrar la llista anterior
        desktopElementsList.getChildren().clear();
        Main.currentObjects.clear();

        listResource = this.getClass().getResource("/assets/layouts/desktopListItem.fxml");

        // Generar la nova llista a partir de 'jsonInfo'
        for (int i = 0; i < jsonInfo.length(); i++) {
            // Obtenir l'objecte JSON individual (animal)
            JSONObject element = jsonInfo.getJSONObject(i);
            Main.currentObjects.add(element);

            // Extreure la informació necessària del JSON
            String name = element.getString("name");
            String image = element.getString("image");

            // Carregar el template de 'listItem.fxml'
            FXMLLoader loader = new FXMLLoader(listResource);
            Parent itemTemplate = loader.load();
            ControllerDesktopListController itemController = loader.getController();

            // Assignar els valors als controls del template
            itemController.setTitle(name);
            itemController.setImatge("/assets/images/" + image.toLowerCase());
            itemController.setIndex(i);
            itemController.setMainController(this);

            // Afegir el nou element a 'yPane'
            desktopElementsList.getChildren().add(itemTemplate);
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

    public void showData() {
        JSONObject element = Main.currentObjects.get(Main.currentObject);

        String name = element.getString("name");
        itemTitle.setText(name);
        try {
            Image image = new Image(Objects.requireNonNull(
                    getClass().getResourceAsStream("/assets/images/" + element.getString("image").toLowerCase())));
            itemImage.setImage(image);
        } catch (NullPointerException e) {
            System.err.println(
                    "Error loading image asset: " + "/assets/images/" + element.getString("image").toLowerCase());
            e.printStackTrace();
        }

        if (Main.currentTheme == "Consoles")
            itemDescription.setText(consoleDescription(element));
        else if (Main.currentTheme == "Videogames")
            itemDescription.setText(videogamesDescription(element));
        else if (Main.currentTheme == "Characters")
            itemDescription.setText(charactersDescription(element));
    }
}