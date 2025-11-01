package br.com.loja.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.model.VendaCompraLojaVirtual;

@Repository
@Transactional
public interface Vd_Cp_loja_virtual_repository extends JpaRepository<VendaCompraLojaVirtual, Long> {

	
	@Query(value = "select a from VendaCompraLojaVirtual a where a.id = ?1 and a.excluido = false")
	VendaCompraLojaVirtual findByIdExclusao(Long id); 
		
	@Query(value = "select i.vendaCompraLojaVirtual from ItemVendaLoja i where i.vendaCompraLojaVirtual.excluido = false and i.produto.id = ?1")
	List<VendaCompraLojaVirtual> vendaPorProduto(Long idProduto);

	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i where i.vendaCompraLojaVirtual.excluido = false and upper(trim(i.produto.nome)) like %?1% ")
	List<VendaCompraLojaVirtual> vendaPorNomeProduto(String valor);

	@Query(value = "select distinct(i.vendaCompraLojaVirtual) from ItemVendaLoja i where i.vendaCompraLojaVirtual.excluido = false and upper(trim(i.vendaCompraLojaVirtual.pessoa.nome)) like %?1% ")
	List<VendaCompraLojaVirtual> vendaPorNomeCliente(String nomepessoa);
		
	
	
}
