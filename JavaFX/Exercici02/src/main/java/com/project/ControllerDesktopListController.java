package com.project;

import java.awt.Label;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.ResourceBundle;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;

import java.util.ArrayList;

import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;

public class ControllerDesktopListController implements Serializable {

    @FXML
    private VBox desktopElementsList = new VBox();

    @FXML
    private ChoiceBox desktopChoiceBox;

    private String[] themes = {"Consoles", "Videojocs", "Personatges"};

    private JSONArray jsonCharacters;
    private JSONArray jsonConsoles;
    private JSONArray jsonGames;

    private URL listResource;

    // Called when the FXML file is loaded
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            // Obtenir el recurs del template .fxml
            listResource = this.getClass().getResource("/resources/assets/listItem.fxml");

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
        desktopChoiceBox.getItems().addAll(themes);

        desktopChoiceBox.setOnAction(event -> {
            if (desktopChoiceBox.getSelectionModel().getSelectedItem() == "Consoles") {
                loadConsoles();
            }
            else if (desktopChoiceBox.getSelectionModel().getSelectedItem() == "Videojocs") {
                loadVideogames();
            }
            else if (desktopChoiceBox.getSelectionModel().getSelectedItem() == "Charcters") {
                loadCharacters();
            }
        });
    }
    
    private void loadConsoles() {

    }

    private void loadVideogames() {

    }

    private void loadCharacters() {

    }

    private void setFXML(JSONArray jsonInfo) throws Exception {

        // Obtenir el recurs del template .fxml
        URL resource = this.getClass().getResource("/resources/listItem.fxml");

        // Esborrar la llista anterior
        desktopElementsList.getChildren().clear();

        // Generar la nova llista a partir de 'jsonInfo'
        for (int i = 0; i < jsonInfo.length(); i++) {
            // Obtenir l'objecte JSON individual (animal)
            JSONObject element = jsonInfo.getJSONObject(i);

            // Extreure la informació necessària del JSON
            String category = element.getString("category");
            String name = element.getString("animal");
            String color = element.getString("color");

            // Carregar el template de 'listItem.fxml'
            FXMLLoader loader = new FXMLLoader(resource);
            Parent itemTemplate = loader.load();
            ControllerDesktopListItem itemController = loader.getController();

            // Assignar els valors als controls del template
            itemController.setTitle(name);
            itemController.setSubtitle(category);
            itemController.setImatge("/assets/images/" + name.toLowerCase() + ".png");
            itemController.setCircleColor(color);

            // Afegir el nou element a 'yPane'
            desktopElementsList.getChildren().add(itemTemplate);
        }
    }
}