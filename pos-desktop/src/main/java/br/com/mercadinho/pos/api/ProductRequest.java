package br.com.mercadinho.pos.api;

import java.math.BigDecimal;

public record ProductRequest(
        String barcode,
        String name,
        BigDecimal salePrice,
        BigDecimal costPrice,
        BigDecimal stockQuantity,
        BigDecimal minimumStock,
        Boolean active) {
}
