package com.project;

import javafx.fxml.FXML;
import javafx.scene.layout.VBox;

public class ControllerMobileMain extends Controller {

    @FXML
    private VBox mobileMainElementList = new VBox();

    @Override
    public void initialize() {
        try {
            this.getClass().getResource("/assets/layouts/mobileMainListItem.fxml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void onMouseClickedConsoles() {
        Main.currentTheme = "Consoles";
        passNextView();
    }

    @FXML
    private void onMouseClickedVideogames() {
        Main.currentTheme = "Videogames";
        passNextView();
    }

    @FXML
    private void onMouseClickedCharacters() {
        Main.currentTheme = "Characters";
        passNextView();
    }

    public void passNextView() {
        // System.out.println("Clicked");
        UtilsViews.setView("MobileItems");
    }
}