package com.github.edm;

import com.github.edm.EDMJavaFX.nodes.UI;
import com.github.edm.EDMJavaFX.repository.entity.DocumentFX;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        UI UI = new UI();

        TableView<DocumentFX> tableView = UI.TableView();
        Button addButton = UI.createAddButton();
        VBox vBox = new VBox(tableView, addButton);

        Scene scene = new Scene(vBox, 800, 800);

        stage.setScene(scene);

        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}