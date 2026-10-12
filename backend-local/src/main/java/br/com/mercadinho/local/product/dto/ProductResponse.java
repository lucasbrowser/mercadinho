package br.com.mercadinho.local.product.dto;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;
import br.com.mercadinho.local.product.Product;

public record ProductResponse(
        UUID id,
        String barcode,
        String name,
        BigDecimal salePrice,
        BigDecimal costPrice,
        BigDecimal stockQuantity,
        BigDecimal minimumStock,
        boolean active,
        Instant updatedAt) {
    public static ProductResponse from(Product product) {
        return new ProductResponse(product.getId(), product.getBarcode(), product.getName(),
                product.getSalePrice(), product.getCostPrice(), product.getStockQuantity(),
                product.getMinimumStock(), product.isActive(), product.getUpdatedAt());
    }
}
