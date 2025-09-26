package br.com.loja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.loja.model.CategoriaProduto;
import br.com.loja.model.Produto;

@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Long> {
	
	@Query(nativeQuery = true, value = "select count(1) > 0 from produto where upper(trim(nome)) = upper(trim(?1))")
	public boolean existeProduto(String produto);

	@Query("select p from Produto p where upper(trim(p.nome)) like %?1%")
	public List<Produto> buscarProdutoNome(String nome);
	
	
}
