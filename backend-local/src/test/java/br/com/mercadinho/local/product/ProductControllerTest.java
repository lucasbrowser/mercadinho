package br.com.mercadinho.local.product;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import br.com.mercadinho.local.common.exception.GlobalExceptionHandler;
import br.com.mercadinho.local.config.SecurityConfig;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.boot.test.mock.mockito.MockBean;

@WebMvcTest(ProductController.class)
@Import({SecurityConfig.class, GlobalExceptionHandler.class})
class ProductControllerTest {
    @Autowired private MockMvc mockMvc;
    @Autowired private ObjectMapper objectMapper;
    @MockBean private ProductService productService;

    @Test
    @WithMockUser
    void shouldCreateProduct() throws Exception {
        UUID id = UUID.randomUUID();
        Product product = sampleProduct();
        ReflectionTestUtils.setField(product, "id", id);
        when(productService.create(any(Product.class))).thenReturn(product);

        mockMvc.perform(post("/api/products")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(requestBody())))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.barcode").value("7891234567890"))
            .andExpect(jsonPath("$.name").value("Rice Type 1"));
    }

    @Test
    @WithMockUser
    void shouldReturnBadRequestWhenBarcodeIsBlank() throws Exception {
        var invalid = new java.util.LinkedHashMap<String, Object>();
        invalid.put("barcode", " ");
        invalid.put("name", "Rice Type 1");
        invalid.put("salePrice", 25.90);
        invalid.put("costPrice", 19.00);
        invalid.put("stockQuantity", 0);
        invalid.put("minimumStock", 5);
        invalid.put("active", true);

        mockMvc.perform(post("/api/products")
                .contentType("application/json")
                .content(objectMapper.writeValueAsBytes(invalid)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.validationErrors.barcode").exists());
    }

    @Test
    @WithMockUser
    void shouldReturnProductById() throws Exception {
        UUID id = UUID.randomUUID();
        Product product = sampleProduct();
        ReflectionTestUtils.setField(product, "id", id);
        when(productService.findById(id)).thenReturn(product);

        mockMvc.perform(get("/api/products/{id}", id))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(id.toString()));
    }

    @Test
    @WithMockUser
    void shouldListProducts() throws Exception {
        Product product = sampleProduct();
        ReflectionTestUtils.setField(product, "id", UUID.randomUUID());
        when(productService.findAll(eq(null), eq(null), eq(null))).thenReturn(List.of(product));

        mockMvc.perform(get("/api/products"))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].barcode").value("7891234567890"));
    }

    private Product sampleProduct() {
        return new Product("7891234567890", "Rice Type 1", new BigDecimal("25.90"),
                new BigDecimal("19.00"), BigDecimal.ZERO, new BigDecimal("5.000"), true);
    }

    private java.util.Map<String, Object> requestBody() {
        return java.util.Map.of(
                "barcode", "7891234567890",
                "name", "Rice Type 1",
                "salePrice", new BigDecimal("25.90"),
                "costPrice", new BigDecimal("19.00"),
                "stockQuantity", BigDecimal.ZERO,
                "minimumStock", new BigDecimal("5.000"),
                "active", true);
    }
}
