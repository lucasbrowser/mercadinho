package br.com.mercadinho.servidor.produto;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ProdutoService {

    private final ProdutoRepository produtoRepository;

    public ProdutoService(ProdutoRepository produtoRepository) {
        this.produtoRepository = produtoRepository;
    }

    public Produto cadastrar(Produto produto) {
        validarProduto(produto);
        String codigoBarras = normalizarCodigoBarras(produto.getCodigoBarras());
        produto.setCodigoBarras(codigoBarras);
        produto.setNome(produto.getNome().trim());

        if (produtoRepository.existsByCodigoBarras(codigoBarras)) {
            throw new ProdutoJaCadastradoException(
                    "Já existe um produto cadastrado com o código de barras informado.");
        }
        return produtoRepository.save(produto);
    }

    @Transactional(readOnly = true)
    public Produto buscarPorId(UUID id) {
        return produtoRepository.findById(id)
                .orElseThrow(() -> new ProdutoNaoEncontradoException("Produto não encontrado: " + id));
    }

    @Transactional(readOnly = true)
    public Produto buscarPorCodigoBarras(String codigoBarras) {
        String codigoNormalizado = normalizarCodigoBarras(codigoBarras);
        return produtoRepository.findByCodigoBarras(codigoNormalizado)
                .orElseThrow(() -> new ProdutoNaoEncontradoException(
                        "Produto não encontrado para o código de barras informado."));
    }

    @Transactional(readOnly = true)
    public List<Produto> listar() {
        return produtoRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Produto> buscarPorNome(String nome) {
        if (nome == null || nome.isBlank()) {
            return listar();
        }
        return produtoRepository.findByNomeContainingIgnoreCaseOrderByNomeAsc(nome.trim());
    }

    public Produto atualizar(UUID id, Produto dadosAtualizados) {
        validarProduto(dadosAtualizados);
        Produto produtoExistente = buscarPorId(id);
        String codigoBarras = normalizarCodigoBarras(dadosAtualizados.getCodigoBarras());

        if (produtoRepository.existsByCodigoBarrasAndIdNot(codigoBarras, id)) {
            throw new ProdutoJaCadastradoException(
                    "Já existe outro produto cadastrado com o código de barras informado.");
        }

        produtoExistente.setCodigoBarras(codigoBarras);
        produtoExistente.setNome(dadosAtualizados.getNome().trim());
        produtoExistente.setPrecoVenda(dadosAtualizados.getPrecoVenda());
        produtoExistente.setPrecoCusto(dadosAtualizados.getPrecoCusto());
        produtoExistente.setQuantidadeEstoque(dadosAtualizados.getQuantidadeEstoque());
        produtoExistente.setEstoqueMinimo(dadosAtualizados.getEstoqueMinimo());
        produtoExistente.setAtivo(dadosAtualizados.isAtivo());
        return produtoRepository.save(produtoExistente);
    }

    private void validarProduto(Produto produto) {
        if (produto == null) {
            throw new IllegalArgumentException("Os dados do produto são obrigatórios.");
        }
        if (produto.getCodigoBarras() == null || produto.getCodigoBarras().isBlank()) {
            throw new IllegalArgumentException("O código de barras é obrigatório.");
        }
        if (produto.getCodigoBarras().trim().length() > 64) {
            throw new IllegalArgumentException("O código de barras deve ter no máximo 64 caracteres.");
        }
        if (produto.getNome() == null || produto.getNome().isBlank()) {
            throw new IllegalArgumentException("O nome do produto é obrigatório.");
        }
        if (produto.getNome().trim().length() > 180) {
            throw new IllegalArgumentException("O nome deve ter no máximo 180 caracteres.");
        }
        validarValorNaoNegativo(produto.getPrecoVenda(), "O preço de venda");
        validarValorNaoNegativo(produto.getPrecoCusto(), "O preço de custo");
        validarValorNaoNegativo(produto.getQuantidadeEstoque(), "A quantidade em estoque");
        validarValorNaoNegativo(produto.getEstoqueMinimo(), "O estoque mínimo");
    }

    private void validarValorNaoNegativo(BigDecimal valor, String campo) {
        if (valor == null || valor.signum() < 0) {
            throw new IllegalArgumentException(campo + " deve ser informado e não pode ser negativo.");
        }
    }

    private String normalizarCodigoBarras(String codigoBarras) {
        return codigoBarras == null ? null : codigoBarras.trim();
    }
}
