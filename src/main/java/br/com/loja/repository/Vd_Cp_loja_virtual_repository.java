package br.com.loja.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.model.VendaCompraLojaVirtual;

@Repository
@Transactional
public interface Vd_Cp_loja_virtual_repository extends JpaRepository<VendaCompraLojaVirtual, Long> {

	
	
}
