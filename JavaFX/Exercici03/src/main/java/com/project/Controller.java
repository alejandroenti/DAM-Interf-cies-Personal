package com.project;

import java.io.InputStream;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpResponse;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;

public class Controller implements Initializable {

    // Models
    private static final String TEXT_MODEL = "gemma3:1b";
    private static final String VISION_MODEL = "llava-phi3";

    @FXML
    private ImageView btnAddImage, btnSubmitPrompt;
    @FXML
    private Rectangle btnStopResponse;
    @FXML
    private TextField textPrompt;
    @FXML
    private VBox responseBox = new VBox();

    private final HttpClient httpClient = HttpClient.newHttpClient();
    private CompletableFuture<HttpResponse<InputStream>> streamRequest;
    private CompletableFuture<HttpResponse<String>> completeRequest;
    private final AtomicBoolean isCancelled = new AtomicBoolean(false);
    private InputStream currentInputStream;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private Future<?> streamReadingTask;
    private volatile boolean isFirst = false;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        URL urlImage = getClass().getResource("/images/attach_btn.png");
        Image image = new Image(urlImage.toExternalForm());
        btnAddImage.setImage(image);

        urlImage = getClass().getResource("/images/send_btn.png");
        image = new Image(urlImage.toExternalForm());
        btnSubmitPrompt.setImage(image);
    }

    @FXML
    private void callStream() {
        
    }
}