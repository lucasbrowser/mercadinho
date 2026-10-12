package br.com.mercadinho.servidor.produto;

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
@Table(name = "produto")
public class Produto {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "codigo_barras", nullable = false, unique = true, length = 64)
    private String codigoBarras;

    @Column(name = "nome", nullable = false, length = 180)
    private String nome;

    @Column(name = "preco_venda", nullable = false, precision = 12, scale = 2)
    private BigDecimal precoVenda;

    @Column(name = "preco_custo", nullable = false, precision = 12, scale = 2)
    private BigDecimal precoCusto = BigDecimal.ZERO;

    @Column(name = "quantidade_estoque", nullable = false, precision = 12, scale = 3)
    private BigDecimal quantidadeEstoque = BigDecimal.ZERO;

    @Column(name = "estoque_minimo", nullable = false, precision = 12, scale = 3)
    private BigDecimal estoqueMinimo = BigDecimal.ZERO;

    @Column(name = "ativo", nullable = false)
    private boolean ativo = true;

    @Column(name = "atualizado_em", nullable = false)
    private Instant atualizadoEm;

    protected Produto() {
        // Construtor exigido pelo JPA.
    }

    public Produto(String codigoBarras, String nome, BigDecimal precoVenda,
                   BigDecimal precoCusto, BigDecimal quantidadeEstoque, BigDecimal estoqueMinimo) {
        this.codigoBarras = codigoBarras;
        this.nome = nome;
        this.precoVenda = precoVenda;
        this.precoCusto = precoCusto == null ? BigDecimal.ZERO : precoCusto;
        this.quantidadeEstoque = quantidadeEstoque == null ? BigDecimal.ZERO : quantidadeEstoque;
        this.estoqueMinimo = estoqueMinimo == null ? BigDecimal.ZERO : estoqueMinimo;
        this.ativo = true;
    }

    @PrePersist
    @PreUpdate
    void atualizarDataDeAtualizacao() {
        atualizadoEm = Instant.now();
    }

    public UUID getId() { return id; }
    public String getCodigoBarras() { return codigoBarras; }
    public void setCodigoBarras(String codigoBarras) { this.codigoBarras = codigoBarras; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public BigDecimal getPrecoVenda() { return precoVenda; }
    public void setPrecoVenda(BigDecimal precoVenda) { this.precoVenda = precoVenda; }
    public BigDecimal getPrecoCusto() { return precoCusto; }
    public void setPrecoCusto(BigDecimal precoCusto) { this.precoCusto = precoCusto; }
    public BigDecimal getQuantidadeEstoque() { return quantidadeEstoque; }
    public void setQuantidadeEstoque(BigDecimal quantidadeEstoque) { this.quantidadeEstoque = quantidadeEstoque; }
    public BigDecimal getEstoqueMinimo() { return estoqueMinimo; }
    public void setEstoqueMinimo(BigDecimal estoqueMinimo) { this.estoqueMinimo = estoqueMinimo; }
    public boolean isAtivo() { return ativo; }
    public void setAtivo(boolean ativo) { this.ativo = ativo; }
    public Instant getAtualizadoEm() { return atualizadoEm; }
}
