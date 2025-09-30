package com.project;

import java.util.ArrayList;

import org.json.JSONObject;

import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

public class Main extends Application {

    final int WINDOW_WIDTH = 440;
    final int WINDOW_HEIGHT = 600;

    public static ArrayList<JSONObject> currentObjects = new ArrayList<>();
    public static int currentObject = -1;
    public static String currentTheme = null;

    @Override
    public void start(Stage stage) throws Exception {

        // Carrega la vista inicial des del fitxer FXML
        UtilsViews.addView(getClass(), "Desktop", "/assets/layouts/desktopMainView.fxml");
        UtilsViews.addView(getClass(), "MobileMain", "/assets/layouts/mobileMainView.fxml");
        UtilsViews.addView(getClass(), "MobileItems", "/assets/layouts/mobileItemsView.fxml");
        UtilsViews.addView(getClass(), "MobileItem", "/assets/layouts/mobileItemView.fxml");

        Scene scene = new Scene(UtilsViews.parentContainer);

        scene.widthProperty().addListener((ChangeListener<? super Number>) new ChangeListener<Number>() {
            @Override
            public void changed(ObservableValue<? extends Number> observable, Number oldWidth, Number newWidth) {
                _setLayout(newWidth.intValue());
            }

        });

        stage.setScene(scene);
        stage.setTitle("NintendoDB - Alejandro López");
        stage.setWidth(WINDOW_WIDTH);
        stage.setHeight(WINDOW_HEIGHT);
        stage.show();

        // Afegeix una icona només si no és un Mac
        if (!System.getProperty("os.name").contains("Mac")) {
            Image icon = new Image("file:icons/icon.png");
            stage.getIcons().add(icon);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }

    private void _setLayout(int width) {
        if (width < 750) {
            if (currentObject != -1)
                UtilsViews.setView("MobileItem");
            else if (!currentObjects.isEmpty())
                UtilsViews.setView("MobileItems");
            else
                UtilsViews.setView("MobileMain");
        } else {
            UtilsViews.setView("Desktop");
        }
    }
}
