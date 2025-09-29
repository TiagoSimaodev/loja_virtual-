package br.com.loja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.model.NotaFiscalCompra;

@Repository
@Transactional
public interface NotaFiscalCompraRepository extends JpaRepository<NotaFiscalCompra, Long> {

	@Query("select a from NotaFiscalCompra a where upper(trim(a.descricaoObs)) like %?1%")
	List<NotaFiscalCompra> buscarNotaDesc(String desc);
	
	@Query("select a from NotaFiscalCompra a where a.pessoa.id = ?1")
	List<NotaFiscalCompra> buscaNotaPorPessoa(Long idPessoa);
	
	@Query("select a from NotaFiscalCompra a where a.contaPagar.id = ?1")
	List<NotaFiscalCompra> buscarNotaContaPagar(Long idContaPagar);
	
	
	@Query("select a from NotaFiscalCompra a where a.empresa.id = ?1")
	List<NotaFiscalCompra> buscaNotaPorEmpresa(Long idEmpresa);
	
	@Transactional
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(nativeQuery = true, value = "delete from nota_item_produto where nota_fiscal_compra_id = ?1")
	void deleteItemNotaFiscalCompra(Long idNotaFiscalCompra);
}
