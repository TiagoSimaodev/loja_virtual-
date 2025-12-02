package br.com.loja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.model.NotaFiscalVenda;

@Repository
@Transactional
public interface NotaIFiscalVendaRepository extends JpaRepository<NotaFiscalVenda, Long> {
	
	
	@Query(value = "select n from NotaFiscalVenda n  where n.vendaCompraLojaVirtual.id = ?1")
	List<NotaFiscalVenda>  buscaNotaPorVenda(Long idVenda);
	
	@Query(value = "select n from NotaFiscalVenda n  where n.vendaCompraLojaVirtual.id = ?1")
	NotaFiscalVenda  buscaNotaPorVendaUnica(Long idVenda);
}
