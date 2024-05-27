package pl.github.korbecki.krzysciak;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.io.File;

public class Main extends Application {

    private Spelling spellfixer;

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        File file = new File(getClass().getResource("/big.txt").getPath());
        spellfixer = new Spelling(file);

        primaryStage.setTitle("Spelling Corrector");

        Label titleLabel = new Label("Spelling Corrector");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");

        TextField inputField = new TextField();
        inputField.setPromptText("Enter a word to correct");
        inputField.setStyle("-fx-font-size: 18px;");

        Button correctButton = new Button("Correct");
        correctButton.setStyle("-fx-font-size: 18px;");

        TextArea outputArea = new TextArea();
        outputArea.setWrapText(true);
        outputArea.setEditable(false);
        outputArea.setStyle("-fx-font-size: 18px;");
        ScrollPane scrollPane = new ScrollPane(outputArea);

        correctButton.setOnAction(event -> {
            String input = inputField.getText().trim();
            if (!input.isEmpty()) {
                String corrected = spellfixer.correct(input);
                outputArea.setText(corrected);
            }
        });

        // Układ aplikacji
        VBox inputBox = new VBox(10, inputField, correctButton);
        inputBox.setAlignment(Pos.CENTER);
        inputBox.setPadding(new Insets(10));

        VBox mainBox = new VBox(20, titleLabel, inputBox, scrollPane);
        mainBox.setAlignment(Pos.CENTER);
        mainBox.setPadding(new Insets(20));

        BorderPane root = new BorderPane(mainBox);
        Scene scene = new Scene(root, 500, 400);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
