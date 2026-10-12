package br.com.mercadinho.pos.api;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

public class ProductApiClient {
    private final HttpClient httpClient;
    private final ObjectMapper objectMapper;

    public ProductApiClient() {
        this.httpClient = HttpClient.newBuilder().build();
        this.objectMapper = new ObjectMapper();
    }

    public List<ProductResponse> findProducts(
            String baseUrl, String username, String password, String nameFilter)
            throws IOException, InterruptedException {
        String url = normalizeBaseUrl(baseUrl) + "/api/products";
        if (nameFilter != null && !nameFilter.isBlank()) {
            url += "?name=" + URLEncoder.encode(nameFilter.trim(), StandardCharsets.UTF_8);
        }

        HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                .header("Accept", "application/json")
                .header("Authorization", authorization(username, password))
                .GET()
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensureSuccess(response.statusCode(), response.body());
        return objectMapper.readValue(response.body(), new TypeReference<List<ProductResponse>>() {});
    }

    public ProductResponse createProduct(
            String baseUrl, String username, String password, ProductRequest productRequest)
            throws IOException, InterruptedException {
        String body = objectMapper.writeValueAsString(productRequest);
        HttpRequest request = HttpRequest.newBuilder(
                        URI.create(normalizeBaseUrl(baseUrl) + "/api/products"))
                .header("Accept", "application/json")
                .header("Content-Type", "application/json")
                .header("Authorization", authorization(username, password))
                .POST(HttpRequest.BodyPublishers.ofString(body, StandardCharsets.UTF_8))
                .build();

        HttpResponse<String> response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        ensureSuccess(response.statusCode(), response.body());
        return objectMapper.readValue(response.body(), ProductResponse.class);
    }

    private String normalizeBaseUrl(String baseUrl) {
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException("Informe o endereço da API local.");
        }
        String normalized = baseUrl.trim();
        while (normalized.endsWith("/")) {
            normalized = normalized.substring(0, normalized.length() - 1);
        }
        return normalized;
    }

    private String authorization(String username, String password) {
        String credentials = (username == null ? "" : username) + ":" + (password == null ? "" : password);
        String encoded = Base64.getEncoder().encodeToString(credentials.getBytes(StandardCharsets.UTF_8));
        return "Basic " + encoded;
    }

    private void ensureSuccess(int statusCode, String body) throws IOException {
        if (statusCode >= 200 && statusCode < 300) {
            return;
        }
        String detail = body == null || body.isBlank() ? "sem detalhes adicionais" : body;
        if (statusCode == 401 || statusCode == 403) {
            throw new IOException("Acesso negado pela API (HTTP " + statusCode
                    + "). Confira o usuário e a senha de desenvolvimento.");
        }
        if (statusCode == 400 || statusCode == 409) {
            throw new IOException("A API rejeitou a operação (HTTP " + statusCode + "): " + detail);
        }
        throw new IOException("Falha na API (HTTP " + statusCode + "): " + detail);
    }
}
