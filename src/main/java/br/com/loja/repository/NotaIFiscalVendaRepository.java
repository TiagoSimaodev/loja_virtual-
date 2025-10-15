package br.com.loja.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.loja.model.NotaFiscalVenda;

@Repository
@Transactional
public interface NotaIFiscalVendaRepository extends JpaRepository<NotaFiscalVenda, Long> {

}
