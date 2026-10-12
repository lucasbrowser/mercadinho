package br.com.mercadinho.pos;

import br.com.mercadinho.pos.product.ProductManagementView;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class PosApplication extends Application {
    @Override
    public void start(Stage stage) {
        stage.setTitle("Mercadinho — Gestão de Produtos");
        stage.setMinWidth(1050);
        stage.setMinHeight(680);
        stage.setScene(new Scene(new ProductManagementView(), 1200, 760));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
