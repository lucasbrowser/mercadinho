package br.com.mercadinho.caixa;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class AplicacaoCaixa extends Application {
    @Override
    public void start(Stage janela) {
        Label titulo = new Label("Mercadinho — Frente de Caixa");
        titulo.setStyle("-fx-font-size: 22px; -fx-font-weight: bold;");
        Label estado = new Label("Interface inicial. Servidor local: http://localhost:8080");
        TextField codigoBarras = new TextField();
        codigoBarras.setPromptText("Leia ou digite o código de barras");
        VBox painel = new VBox(14, titulo, estado, codigoBarras);
        painel.setPadding(new Insets(24));
        janela.setTitle("Mercadinho — Caixa");
        janela.setScene(new Scene(painel, 900, 600));
        janela.show();
        codigoBarras.requestFocus();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
