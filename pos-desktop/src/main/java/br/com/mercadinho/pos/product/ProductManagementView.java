package br.com.mercadinho.pos.product;

import br.com.mercadinho.pos.api.ProductApiClient;
import br.com.mercadinho.pos.api.ProductRequest;
import br.com.mercadinho.pos.api.ProductResponse;
import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;

public class ProductManagementView extends BorderPane {
    private final TextField apiUrlField = new TextField("http://localhost:8080");
    private final TextField usernameField = new TextField("user");
    private final PasswordField passwordField = new PasswordField();
    private final TextField barcodeField = new TextField();
    private final TextField nameField = new TextField();
    private final TextField salePriceField = new TextField();
    private final TextField costPriceField = new TextField();
    private final TextField stockQuantityField = new TextField("0");
    private final TextField minimumStockField = new TextField("0");
    private final CheckBox activeCheckBox = new CheckBox("Produto ativo");
    private final TextField searchField = new TextField();
    private final Label statusLabel = new Label("Informe as credenciais da API local e clique em Atualizar.");
    private final Button saveButton = new Button("Cadastrar produto");
    private final Button refreshButton = new Button("Atualizar lista");
    private final ObservableList<ProductResponse> products = FXCollections.observableArrayList();
    private final TableView<ProductResponse> productTable = new TableView<>(products);
    private final ProductApiClient apiClient = new ProductApiClient();

    public ProductManagementView() {
        setPadding(new Insets(20));
        setTop(buildHeader());
        setCenter(buildContent());
        setBottom(buildStatusBar());
        configureTable();
        activeCheckBox.setSelected(true);
        saveButton.setDefaultButton(true);
        saveButton.setOnAction(event -> createProduct());
        refreshButton.setOnAction(event -> loadProducts());
        searchField.setOnAction(event -> loadProducts());
        barcodeField.setOnAction(event -> nameField.requestFocus());
    }

    private VBox buildHeader() {
        Label title = new Label("Cadastro de produtos");
        title.setStyle("-fx-font-size: 24px; -fx-font-weight: bold;");
        Label subtitle = new Label("Gerencie produtos, preços e níveis de estoque do mercadinho.");
        subtitle.setStyle("-fx-text-fill: #596579;");
        VBox header = new VBox(5, title, subtitle);
        header.setPadding(new Insets(0, 0, 18, 0));
        return header;
    }

    private VBox buildContent() {
        GridPane connectionForm = new GridPane();
        connectionForm.setHgap(10);
        connectionForm.setVgap(8);
        connectionForm.add(new Label("Endereço da API"), 0, 0);
        connectionForm.add(apiUrlField, 1, 0);
        connectionForm.add(new Label("Usuário"), 2, 0);
        connectionForm.add(usernameField, 3, 0);
        connectionForm.add(new Label("Senha"), 4, 0);
        connectionForm.add(passwordField, 5, 0);
        GridPane.setHgrow(apiUrlField, Priority.ALWAYS);
        GridPane.setHgrow(usernameField, Priority.ALWAYS);
        GridPane.setHgrow(passwordField, Priority.ALWAYS);

        GridPane productForm = new GridPane();
        productForm.setHgap(12);
        productForm.setVgap(10);
        productForm.setPadding(new Insets(14));
        productForm.setStyle("-fx-background-color: #f4f6f8; -fx-background-radius: 8;");
        productForm.add(new Label("Código de barras *"), 0, 0);
        productForm.add(barcodeField, 1, 0);
        productForm.add(new Label("Nome do produto *"), 2, 0);
        productForm.add(nameField, 3, 0);
        productForm.add(new Label("Preço de venda *"), 0, 1);
        productForm.add(salePriceField, 1, 1);
        productForm.add(new Label("Preço de custo *"), 2, 1);
        productForm.add(costPriceField, 3, 1);
        productForm.add(new Label("Quantidade em estoque"), 0, 2);
        productForm.add(stockQuantityField, 1, 2);
        productForm.add(new Label("Estoque mínimo"), 2, 2);
        productForm.add(minimumStockField, 3, 2);
        productForm.add(activeCheckBox, 0, 3);
        HBox buttons = new HBox(8, saveButton, new Button("Limpar"));
        Button clearButton = (Button) buttons.getChildren().get(1);
        clearButton.setOnAction(event -> clearForm());
        productForm.add(buttons, 2, 3);
        GridPane.setHgrow(barcodeField, Priority.ALWAYS);
        GridPane.setHgrow(nameField, Priority.ALWAYS);
        GridPane.setHgrow(salePriceField, Priority.ALWAYS);
        GridPane.setHgrow(costPriceField, Priority.ALWAYS);
        GridPane.setHgrow(stockQuantityField, Priority.ALWAYS);
        GridPane.setHgrow(minimumStockField, Priority.ALWAYS);

        HBox searchBar = new HBox(10, new Label("Pesquisar por nome"), searchField, refreshButton);
        searchBar.setAlignment(Pos.CENTER_LEFT);
        searchField.setPromptText("Digite parte do nome do produto");
        HBox.setHgrow(searchField, Priority.ALWAYS);

        VBox content = new VBox(14, connectionForm, productForm, searchBar, productTable);
        VBox.setVgrow(productTable, Priority.ALWAYS);
        return content;
    }

    private HBox buildStatusBar() {
        HBox bar = new HBox(statusLabel);
        bar.setPadding(new Insets(14, 0, 0, 0));
        return bar;
    }

    private void configureTable() {
        TableColumn<ProductResponse, String> barcodeColumn = new TableColumn<>("Código de barras");
        barcodeColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().barcode()));
        barcodeColumn.setPrefWidth(150);

        TableColumn<ProductResponse, String> nameColumn = new TableColumn<>("Produto");
        nameColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().name()));
        nameColumn.setPrefWidth(260);

        TableColumn<ProductResponse, BigDecimal> saleColumn = new TableColumn<>("Venda (R$)");
        saleColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().salePrice()));
        saleColumn.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(BigDecimal value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(value));
            }
        });

        TableColumn<ProductResponse, BigDecimal> costColumn = new TableColumn<>("Custo (R$)");
        costColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().costPrice()));
        costColumn.setCellFactory(column -> new javafx.scene.control.TableCell<>() {
            @Override
            protected void updateItem(BigDecimal value, boolean empty) {
                super.updateItem(value, empty);
                setText(empty || value == null ? null : NumberFormat.getCurrencyInstance(new Locale("pt", "BR")).format(value));
            }
        });

        TableColumn<ProductResponse, BigDecimal> stockColumn = new TableColumn<>("Estoque");
        stockColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().stockQuantity()));

        TableColumn<ProductResponse, BigDecimal> minimumColumn = new TableColumn<>("Mínimo");
        minimumColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().minimumStock()));

        TableColumn<ProductResponse, String> activeColumn = new TableColumn<>("Situação");
        activeColumn.setCellValueFactory(cell -> new ReadOnlyObjectWrapper<>(cell.getValue().active() ? "Ativo" : "Inativo"));

        productTable.getColumns().setAll(barcodeColumn, nameColumn, saleColumn, costColumn, stockColumn, minimumColumn, activeColumn);
        productTable.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY_ALL_COLUMNS);
        productTable.setPlaceholder(new Label("Nenhum produto encontrado."));
    }

    private void createProduct() {
        ProductRequest request;
        try {
            if (barcodeField.getText().isBlank() || nameField.getText().isBlank()) {
                throw new IllegalArgumentException("Informe o código de barras e o nome do produto.");
            }
            request = new ProductRequest(
                    barcodeField.getText().trim(),
                    nameField.getText().trim(),
                    parseDecimal(salePriceField.getText(), "preço de venda"),
                    parseDecimal(costPriceField.getText(), "preço de custo"),
                    parseDecimalOrDefault(stockQuantityField.getText(), "quantidade em estoque"),
                    parseDecimalOrDefault(minimumStockField.getText(), "estoque mínimo"),
                    activeCheckBox.isSelected());
        } catch (IllegalArgumentException exception) {
            showStatus(exception.getMessage(), true);
            return;
        }

        setBusy(true, "Cadastrando produto...");
        String baseUrl = apiUrlField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();

        CompletableFuture.supplyAsync(() -> {
            try {
                return apiClient.createProduct(baseUrl, username, password, request);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }).whenComplete((created, failure) -> Platform.runLater(() -> {
            setBusy(false, null);
            if (failure != null) {
                showStatus(messageOf(failure), true);
                return;
            }
            showStatus("Produto cadastrado com sucesso: " + created.name(), false);
            clearForm();
            loadProducts();
        }));
    }

    private void loadProducts() {
        setBusy(true, "Consultando produtos...");
        String baseUrl = apiUrlField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String nameFilter = searchField.getText();

        CompletableFuture.supplyAsync(() -> {
            try {
                return apiClient.findProducts(baseUrl, username, password, nameFilter);
            } catch (Exception exception) {
                throw new RuntimeException(exception);
            }
        }).whenComplete((result, failure) -> Platform.runLater(() -> {
            setBusy(false, null);
            if (failure != null) {
                showStatus(messageOf(failure), true);
                return;
            }
            products.setAll(result);
            showStatus(result.size() + " produto(s) encontrado(s).", false);
        }));
    }

    private BigDecimal parseDecimal(String text, String fieldName) {
        if (text == null || text.isBlank()) {
            throw new IllegalArgumentException("Informe o " + fieldName + ".");
        }
        try {
            BigDecimal value = new BigDecimal(text.trim().replace(",", "."));
            if (value.signum() < 0) {
                throw new IllegalArgumentException("O " + fieldName + " não pode ser negativo.");
            }
            return value;
        } catch (NumberFormatException exception) {
            throw new IllegalArgumentException("Valor inválido para " + fieldName + ". Use, por exemplo, 12,50.");
        }
    }

    private BigDecimal parseDecimalOrDefault(String text, String fieldName) {
        if (text == null || text.isBlank()) {
            return BigDecimal.ZERO;
        }
        return parseDecimal(text, fieldName);
    }

    private void clearForm() {
        barcodeField.clear();
        nameField.clear();
        salePriceField.clear();
        costPriceField.clear();
        stockQuantityField.setText("0");
        minimumStockField.setText("0");
        activeCheckBox.setSelected(true);
        barcodeField.requestFocus();
    }

    private void setBusy(boolean busy, String message) {
        saveButton.setDisable(busy);
        refreshButton.setDisable(busy);
        if (message != null) {
            showStatus(message, false);
        }
    }

    private void showStatus(String message, boolean error) {
        statusLabel.setText(message);
        statusLabel.setStyle(error ? "-fx-text-fill: #b42318;" : "-fx-text-fill: #256029;");
    }

    private String messageOf(Throwable throwable) {
        Throwable current = throwable;
        while (current.getCause() != null) {
            current = current.getCause();
        }
        String message = current.getMessage();
        return message == null || message.isBlank() ? "Falha inesperada ao comunicar com a API." : message;
    }
}
