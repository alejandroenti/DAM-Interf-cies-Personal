package com.project;

import java.net.URL;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.layout.VBox;

public class ControllerMobileMain extends Controller {

    @FXML
    private VBox mobileMainElementList = new VBox();
    
    private String[] themes = { "Consoles", "Videojocs", "Personatges" };

    private URL listResource;

    @Override
    public void initialize() {
        try {
            // Obtenir el recurs del template .fxml
            listResource = this.getClass().getResource("/assets/layouts/mobileMainListItem.fxml");

            setFXML();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setFXML() throws Exception {

        // Esborrar la llista anterior
        mobileMainElementList.getChildren().clear();
        Main.currentObjects.clear();

        // Generar la nova llista a partir de 'jsonInfo'
        for (int i = 0; i < themes.length; i++) {

            listResource = this.getClass().getResource("/assets/layouts/mobileMainListItem.fxml");

            // Extreure la informació necessària del JSON
            String name = themes[i];

            // Carregar el template de 'listItem.fxml'
            FXMLLoader loader = new FXMLLoader(listResource);
            Parent itemTemplate = loader.load();
            ControllerMobileMainListController itemController = loader.getController();
            
            // Assignar els valors als controls del template
            itemController.setTitle(name);
            itemController.setMainController(this);

            // Afegir el nou element a 'yPane'
            mobileMainElementList.getChildren().add(itemTemplate);
        }
    }

    public void passNextView() {
        
        UtilsViews.setView("MobileTypesView");
    }
}