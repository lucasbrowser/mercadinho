package br.com.mercadinho.local.product;

import br.com.mercadinho.local.product.dto.ProductRequest;
import br.com.mercadinho.local.product.dto.ProductResponse;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@RestController
@RequestMapping("/api/products")
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @PostMapping
    public ResponseEntity<ProductResponse> create(@Valid @RequestBody ProductRequest request) {
        Product created = productService.create(toProduct(request));
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}")
                .buildAndExpand(created.getId()).toUri();
        return ResponseEntity.created(location).body(ProductResponse.from(created));
    }

    @GetMapping
    public List<ProductResponse> findAll(
            @RequestParam(name = "name", required = false) String name,
            @RequestParam(name = "barcode", required = false) String barcode,
            @RequestParam(name = "active", required = false) Boolean active) {

        return productService.findAll(name, barcode, active)
                .stream()
                .map(ProductResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public ProductResponse findById(
            @PathVariable("id") UUID id) {

        return ProductResponse.from(productService.findById(id));
    }

    @PutMapping("/{id}")
    public ProductResponse update(@PathVariable("id") UUID id, @Valid @RequestBody ProductRequest request) {
        return ProductResponse.from(productService.update(id, toProduct(request)));
    }

    private Product toProduct(ProductRequest request) {
        return new Product(request.barcode(), request.name(), request.salePrice(), request.costPrice(),
                request.stockQuantity(), request.minimumStock(), request.active() == null || request.active());
    }
}
