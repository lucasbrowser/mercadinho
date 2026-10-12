package br.com.mercadinho.local.product;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, UUID> {
    Optional<Product> findByBarcode(String barcode);
    boolean existsByBarcode(String barcode);
    boolean existsByBarcodeAndIdNot(String barcode, UUID id);
    List<Product> findByNameContainingIgnoreCaseOrderByNameAsc(String name);
}
