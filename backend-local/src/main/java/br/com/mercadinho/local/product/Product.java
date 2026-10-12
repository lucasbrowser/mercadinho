package br.com.mercadinho.local.product;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "product")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "barcode", nullable = false, unique = true, length = 64)
    private String barcode;

    @Column(name = "name", nullable = false, length = 180)
    private String name;

    @Column(name = "sale_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal salePrice;

    @Column(name = "cost_price", nullable = false, precision = 12, scale = 2)
    private BigDecimal costPrice = BigDecimal.ZERO;

    @Column(name = "stock_quantity", nullable = false, precision = 12, scale = 3)
    private BigDecimal stockQuantity = BigDecimal.ZERO;

    @Column(name = "minimum_stock", nullable = false, precision = 12, scale = 3)
    private BigDecimal minimumStock = BigDecimal.ZERO;

    @Column(name = "active", nullable = false)
    private boolean active = true;

    @Column(name = "updated_at", nullable = false)
    private Instant updatedAt;

    protected Product() { }

    public Product(String barcode, String name, BigDecimal salePrice, BigDecimal costPrice,
                   BigDecimal stockQuantity, BigDecimal minimumStock, boolean active) {
        this.barcode = barcode;
        this.name = name;
        this.salePrice = salePrice;
        this.costPrice = costPrice == null ? BigDecimal.ZERO : costPrice;
        this.stockQuantity = stockQuantity == null ? BigDecimal.ZERO : stockQuantity;
        this.minimumStock = minimumStock == null ? BigDecimal.ZERO : minimumStock;
        this.active = active;
    }

    @PrePersist
    @PreUpdate
    void updateTimestamp() { updatedAt = Instant.now(); }

    public UUID getId() { return id; }
    public String getBarcode() { return barcode; }
    public void setBarcode(String barcode) { this.barcode = barcode; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public BigDecimal getSalePrice() { return salePrice; }
    public void setSalePrice(BigDecimal salePrice) { this.salePrice = salePrice; }
    public BigDecimal getCostPrice() { return costPrice; }
    public void setCostPrice(BigDecimal costPrice) { this.costPrice = costPrice; }
    public BigDecimal getStockQuantity() { return stockQuantity; }
    public void setStockQuantity(BigDecimal stockQuantity) { this.stockQuantity = stockQuantity; }
    public BigDecimal getMinimumStock() { return minimumStock; }
    public void setMinimumStock(BigDecimal minimumStock) { this.minimumStock = minimumStock; }
    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }
    public Instant getUpdatedAt() { return updatedAt; }
}
