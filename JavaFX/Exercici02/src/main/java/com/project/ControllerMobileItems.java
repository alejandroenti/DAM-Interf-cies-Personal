package com.project;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

public class ControllerMobileItems extends Controller {

    @FXML
    private VBox mobileElementList = new VBox();
    @FXML
    private Label windowTitle;
    @FXML
    private Pane windowBackground;
    @FXML
    private ImageView imgArrowBack;
    
    private JSONArray jsonInfo;

    private URL listResource;
    private String resourceInfo;

    @Override
    public void initialize() {
        try {
            if (Main.currentTheme == null) return;

            if (Main.currentTheme == "Consoles") {
                resourceInfo = "/data/consoles.json";
                windowTitle.setText("Consoles");
                windowBackground.setStyle("-fx-background-color: #dd1212ff;");
            }
            else if (Main.currentTheme == "Videogames") {
                resourceInfo = "/data/games.json";
                windowTitle.setText("Videogames");
                windowBackground.setStyle("-fx-background-color: #4812ddff;");
            }
            else if (Main.currentTheme == "Characters") {
                resourceInfo = "/data/characters.json";
                windowTitle.setText("Characters");
                windowBackground.setStyle("-fx-background-color: #12dd45ff;");
            }

            URL urlImage = getClass().getResource("/assets/images/arrow-back.png");
            Image image = new Image(urlImage.toExternalForm());
            imgArrowBack.setImage(image);

            URL jsonFileURL = getClass().getResource(resourceInfo);
            Path path = Paths.get(jsonFileURL.toURI());
            String content = new String(Files.readAllBytes(path), StandardCharsets.UTF_8);
            jsonInfo = new JSONArray(content);

            setFXML(jsonInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFXML(JSONArray jsonInfo) throws Exception {
        // Esborrar la llista anterior
        mobileElementList.getChildren().clear();
        Main.currentObjects.clear();

        listResource = this.getClass().getResource("/assets/layouts/mobileListItem.fxml");

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
            ControllerMobileListController itemController = loader.getController();

            // Assignar els valors als controls del template
            itemController.setTitle(name);
            itemController.setImatge("/assets/images/" + image.toLowerCase());
            itemController.setIndex(i);
            itemController.setMainController(this);

            // Afegir el nou element a 'yPane'
            mobileElementList.getChildren().add(itemTemplate);

            System.out.println(mobileElementList.getChildren());
        }
    }

    @FXML
    private void toViewMain() {
        UtilsViews.setView("MobileMain");
    }

    public void passNextView() {
        System.out.println("Clicked");
        //UtilsViews.setView("MobileItemsView");
    }
}