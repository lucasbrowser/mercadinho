package br.com.mercadinho.local.product;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProductService {
    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product create(Product product) {
        validate(product);
        product.setBarcode(normalizeBarcode(product.getBarcode()));
        product.setName(product.getName().trim());
        if (productRepository.existsByBarcode(product.getBarcode())) {
            throw new ProductAlreadyExistsException("A product with this barcode already exists.");
        }
        return productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public Product findById(UUID id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product not found: " + id));
    }

    @Transactional(readOnly = true)
    public Product findByBarcode(String barcode) {
        return productRepository.findByBarcode(normalizeBarcode(barcode))
                .orElseThrow(() -> new ProductNotFoundException("Product not found for the supplied barcode."));
    }

    @Transactional(readOnly = true)
    public List<Product> findAll(String name, String barcode, Boolean active) {
        List<Product> products;
        if (barcode != null && !barcode.isBlank()) {
            products = productRepository.findByBarcode(normalizeBarcode(barcode)).map(List::of).orElseGet(List::of);
        } else if (name != null && !name.isBlank()) {
            products = productRepository.findByNameContainingIgnoreCaseOrderByNameAsc(name.trim());
        } else {
            products = productRepository.findAll();
        }
        if (active == null) return products;
        return products.stream().filter(product -> product.isActive() == active).toList();
    }

    public Product update(UUID id, Product updatedData) {
        validate(updatedData);
        Product current = findById(id);
        String barcode = normalizeBarcode(updatedData.getBarcode());
        if (productRepository.existsByBarcodeAndIdNot(barcode, id)) {
            throw new ProductAlreadyExistsException("Another product already uses this barcode.");
        }
        current.setBarcode(barcode);
        current.setName(updatedData.getName().trim());
        current.setSalePrice(updatedData.getSalePrice());
        current.setCostPrice(updatedData.getCostPrice());
        current.setStockQuantity(updatedData.getStockQuantity());
        current.setMinimumStock(updatedData.getMinimumStock());
        current.setActive(updatedData.isActive());
        return productRepository.save(current);
    }

    private void validate(Product product) {
        if (product == null) throw new IllegalArgumentException("Product data is required.");
        if (product.getBarcode() == null || product.getBarcode().isBlank()) {
            throw new IllegalArgumentException("Barcode is required.");
        }
        if (product.getBarcode().trim().length() > 64) throw new IllegalArgumentException("Barcode must have at most 64 characters.");
        if (product.getName() == null || product.getName().isBlank()) throw new IllegalArgumentException("Product name is required.");
        if (product.getName().trim().length() > 180) throw new IllegalArgumentException("Product name must have at most 180 characters.");
        validateNonNegative(product.getSalePrice(), "Sale price");
        validateNonNegative(product.getCostPrice(), "Cost price");
        validateNonNegative(product.getStockQuantity(), "Stock quantity");
        validateNonNegative(product.getMinimumStock(), "Minimum stock");
    }

    private void validateNonNegative(BigDecimal value, String field) {
        if (value == null || value.signum() < 0) throw new IllegalArgumentException(field + " must be provided and cannot be negative.");
    }

    private String normalizeBarcode(String barcode) {
        return barcode == null ? null : barcode.trim();
    }
}
