package br.com.mercadinho.pos.api;

import java.math.BigDecimal;
import java.util.UUID;

public record ProductResponse(
        UUID id,
        String barcode,
        String name,
        BigDecimal salePrice,
        BigDecimal costPrice,
        BigDecimal stockQuantity,
        BigDecimal minimumStock,
        boolean active,
        String updatedAt) {
}
