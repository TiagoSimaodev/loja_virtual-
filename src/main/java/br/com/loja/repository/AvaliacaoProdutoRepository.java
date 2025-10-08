package br.com.loja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.model.AvaliacaoProduto;

@Repository
@Transactional
public interface AvaliacaoProdutoRepository extends JpaRepository<AvaliacaoProduto, Long> {
	
	@Query(value = "select a from AvaliacaoProduto a where a.produto.id = ?1 ")
	List<AvaliacaoProduto> avaliacaoProduto(Long idProduto);
	
	@Query(value = "select a from AvaliacaoProduto a where a.produto.id = ?1  and a.pessoa.id = ?2 ")
	List<AvaliacaoProduto> avaliacaoProdutoPessoa(Long idProduto, Long idPessoa);
	
	@Query(value = "select a from AvaliacaoProduto a where a.pessoa.id = ?1 ")
	List<AvaliacaoProduto> avaliacaoPessoa(Long idPessoa);
	
	@Query("SELECT a FROM AvaliacaoProduto a WHERE a.produto.id = :idProduto")
	AvaliacaoProduto findByProdutoId(@Param("idProduto") Long idProduto);

	
}
