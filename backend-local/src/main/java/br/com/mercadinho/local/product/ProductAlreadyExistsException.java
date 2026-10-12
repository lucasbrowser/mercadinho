package br.com.mercadinho.local.product;

public class ProductAlreadyExistsException extends RuntimeException {
    public ProductAlreadyExistsException(String message) { super(message); }
}
