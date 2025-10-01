package com.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.ResourceBundle;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
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
    private VBox responseBox = new VBox(10);
    @FXML 
    private ScrollPane scrollPane;

    Label textResponse = new Label();

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
        String prompt = textPrompt.getText();
        textPrompt.setText("");
        //setButtonsRunning();
        isCancelled.set(false);

        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/assets/textScreenLayout.fxml"));
            Parent itemTemplate = loader.load();
            ControllerScreen itemController = loader.getController();
            itemController.setText(prompt);
            itemController.setIsQuestion(true);
            responseBox.getChildren().add(itemTemplate);
        } catch (IOException ex) {
        }

        try {
            FXMLLoader loader = new FXMLLoader(this.getClass().getResource("/assets/textScreenLayout.fxml"));
            Parent itemTemplate = loader.load();
            ControllerScreen itemController = loader.getController();
            itemController.setText("");
            itemController.setIsQuestion(false);
            responseBox.getChildren().add(itemTemplate);
            textResponse = itemController.getText();
        } catch (IOException ex) {
        }

        btnStopResponse.setVisible(true);
        
        ensureModelLoaded(TEXT_MODEL).whenComplete((v, err) -> {
            if (err != null) {
                Platform.runLater(() -> { textResponse.setText("Error loading model."); });// setButtonsIdle(); });
                return;
            }
            executeTextRequest(TEXT_MODEL, prompt, true);
        });
    }

    @FXML
    private void cancelCall() {
        btnStopResponse.setVisible(false);
    }

    // --- Request helpers ---

    // Text-only (stream or not)
    private void executeTextRequest(String model, String prompt, boolean stream) {
        JSONObject body = new JSONObject()
            .put("model", model)
            .put("prompt", prompt)
            .put("stream", stream)
            .put("keep_alive", "10m");

        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create("http://localhost:11434/api/generate"))
            .header("Content-Type", "application/json")
            .POST(BodyPublishers.ofString(body.toString()))
            .build();

        if (stream) {
            Platform.runLater(() -> textResponse.setText("Wait stream ... " + prompt));
            isFirst = true;

            streamRequest = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofInputStream())
                .thenApply(response -> {
                    currentInputStream = response.body();
                    streamReadingTask = executorService.submit(this::handleStreamResponse);
                    return response;
                })
                .exceptionally(e -> {
                    if (!isCancelled.get()) e.printStackTrace();
                    //Platform.runLater(this::setButtonsIdle);
                    return null;
                });

        } else {
            Platform.runLater(() -> textResponse.setText("Wait complete ..."));

            completeRequest = httpClient.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                .thenApply(response -> {
                    String responseText = safeExtractTextResponse(response.body());
                    Platform.runLater(() -> {scrollPane.setVvalue(1.0);});
                    //Platform.runLater(() -> { textInfo.setText(responseText); setButtonsIdle(); });
                    return response;
                })
                .exceptionally(e -> {
                    if (!isCancelled.get()) e.printStackTrace();
                    //Platform.runLater(this::setButtonsIdle);
                    return null;
                });
        }
    }

    // Stream reader for text responses
    private void handleStreamResponse() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(currentInputStream, StandardCharsets.UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (isCancelled.get()) break;
                if (line.isBlank()) continue;

                JSONObject jsonResponse = new JSONObject(line);
                String chunk = jsonResponse.optString("response", "");
                if (chunk.isEmpty()) continue;

                if (isFirst) {
                    Platform.runLater(() -> {textResponse.setText(chunk); scrollPane.setVvalue(1.0);});
                    isFirst = false;
                } else {
                    Platform.runLater(() -> {textResponse.setText(textResponse.getText() + chunk); scrollPane.setVvalue(1.0);});
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Platform.runLater(() -> { textResponse.setText("Error during streaming."); }); //; setButtonsIdle(); });
        } finally {
            try { if (currentInputStream != null) currentInputStream.close(); } catch (Exception ignore) {}
            btnStopResponse.setVisible(false);
            //Platform.runLater(this::setButtonsIdle);
        }
    }

    // --- Small utils ---

    private String safeExtractTextResponse(String bodyStr) {
        // Extract "response" or fallback to error/message if present
        try {
            JSONObject o = new JSONObject(bodyStr);
            String r = o.optString("response", null);
            if (r != null && !r.isBlank()) return r;
            if (o.has("message")) return o.optString("message");
            if (o.has("error"))   return "Error: " + o.optString("error");
        } catch (Exception ignore) {}
        return bodyStr != null && !bodyStr.isBlank() ? bodyStr : "(empty)";
    }

    // Ensure given model is in memory; preload if needed
    private CompletableFuture<Void> ensureModelLoaded(String modelName) {
        return httpClient.sendAsync(
                HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:11434/api/ps"))
                    .GET()
                    .build(),
                HttpResponse.BodyHandlers.ofString()
            )
            .thenCompose(resp -> {
                boolean loaded = false;
                try {
                    JSONObject o = new JSONObject(resp.body());
                    JSONArray models = o.optJSONArray("models");
                    if (models != null) {
                        for (int i = 0; i < models.length(); i++) {
                            String name = models.getJSONObject(i).optString("name", "");
                            String model = models.getJSONObject(i).optString("model", "");
                            if (name.startsWith(modelName) || model.startsWith(modelName)) { loaded = true; break; }
                        }
                    }
                } catch (Exception ignore) {}

                if (loaded) return CompletableFuture.completedFuture(null);

                Platform.runLater(() -> textResponse.setText("Loading model..."));

                String preloadJson = new JSONObject()
                    .put("model", modelName)
                    .put("stream", false)
                    .put("keep_alive", "10m")
                    .toString();

                HttpRequest preloadReq = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:11434/api/generate"))
                    .header("Content-Type", "application/json")
                    .POST(BodyPublishers.ofString(preloadJson))
                    .build();

                return httpClient.sendAsync(preloadReq, HttpResponse.BodyHandlers.ofString())
                        .thenAccept(r -> { /* warmed */ });
            });
    }
}