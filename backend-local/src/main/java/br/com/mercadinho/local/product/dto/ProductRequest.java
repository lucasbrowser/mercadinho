package br.com.mercadinho.local.product.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;

public record ProductRequest(
        @NotBlank @Size(max = 64) String barcode,
        @NotBlank @Size(max = 180) String name,
        @NotNull @DecimalMin("0.00") BigDecimal salePrice,
        @NotNull @DecimalMin("0.00") BigDecimal costPrice,
        @NotNull @DecimalMin("0.00") BigDecimal stockQuantity,
        @NotNull @DecimalMin("0.00") BigDecimal minimumStock,
        Boolean active) {
}
