package br.com.mercadinho.pos;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PosApplication extends Application {
    @Override
    public void start(Stage stage) {
        Label title = new Label("Mercadinho — Frente de Caixa");
        title.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        Label status = new Label("Inicialização visual concluída. API local: http://localhost:8080");
        TextField barcode = new TextField();
        barcode.setPromptText("Leia ou digite o código de barras");
        VBox root = new VBox(14, title, status, barcode);
        root.setPadding(new Insets(24));
        stage.setTitle("Mercadinho PDV");
        stage.setScene(new Scene(root, 900, 600));
        stage.show();
        barcode.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
